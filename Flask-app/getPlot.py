import pandas as pd, numpy as np
import os, json, re
import seaborn as sns
import matplotlib.pyplot as plt
from datetime import timedelta, datetime

class plotter:
    def __init__(self):
        self.TIME_STAP = '10min'
        self.CHANGE_DATA_TYPES_TESTS = {'h2_ppm': float, 'ch4_ppm': float, 't_ms': np.int64, 'uid': int, 'f_score_h2': float, 'f_score_ch4':float, 'f_score':float} ## this not work clear
        self.CHANGE_DATA_TYPES_MEALS_CONTENTS = {'quantity' : float}
        self.FOLDER_WITH_DATA = 'DATA'
        self.TABLE_TESTS = 'tests'
        self.TABLE_MEALS_CONTENTS = 'meal_contents'
        self.cwd = os.path.join(os.getcwd(), self.FOLDER_WITH_DATA)
        self.pathes = list()
        self.df_tests = pd.DataFrame()
        self.df_meals = pd.DataFrame()
        pass
 
    ## currently don't used
    def parse_report_file(self, report_input_file):
        with open(report_input_file, 'r') as unknown_file:
            # Remove tabs, spaces, and new lines when reading
            data = re.sub(r'\s+', '', unknown_file.read())
            if (re.match(r'^<.+>$', data)):
                return 'Is XML'
            if (re.match(r'^({|[).+(}|])$', data)):
                return 'Is JSON'
            return 'Is INVALID'
            
    def is_non_zero_file(self, fpath):  
        return os.path.isfile(fpath) and os.path.getsize(fpath) > 0
            
    def getPathesJson(self):
        for filename in os.listdir(self.cwd):
            f = os.path.join(self.cwd, filename)
            if self.is_non_zero_file(f) :
                self.pathes.append(f)
        return self.pathes
        
    def pandasFromJSON(self, tableName, path):
        with open(path,'r') as file:
            dataJSON = json.load(file)
        for i in dataJSON:
            if tableName in i.keys():
                return pd.DataFrame(i[tableName])
        raise NameError(f"YOUR table named {tableName} isn't in JSON or structure of JSON is changed")
        
    def transformData(self, df, tableName):
        if tableName == self.TABLE_TESTS:
            EVENT_DT = 't_ms'
            change_dict = self.CHANGE_DATA_TYPES_TESTS
        elif tableName == self.TABLE_MEALS_CONTENTS:
            EVENT_DT = 'm_created_ms'
            change_dict = self.CHANGE_DATA_TYPES_MEALS_CONTENTS
        else:
            raise NameError(f"YOUR table named {tableName} is wrong")
        df = df.astype(change_dict)
        df[EVENT_DT] = df[EVENT_DT].astype(np.int64)
        df['event_dt'] = pd.to_datetime(df[EVENT_DT], unit='ms').dt.date
        df['event_time'] = pd.to_datetime(df[EVENT_DT], unit='ms').dt.floor(self.TIME_STAP)
        return df
        
    def getData(self):
        # currently get last JSON in files list
        if len(self.getPathesJson()) > 0:
            path = self.getPathesJson()[-1]
            self.df_tests = self.pandasFromJSON(self.TABLE_TESTS, path)
            self.df_meals = self.pandasFromJSON(self.TABLE_MEALS_CONTENTS, path)
            return self.transformData(self.df_tests, self.TABLE_TESTS), self.transformData(self.df_meals, self.TABLE_MEALS_CONTENTS)
        else:
            print(self.FOLDER_WITH_DATA, " folder is empty")
            return pd.DataFrame(), pd.DataFrame()
        
    def plotAllData(self):
        df_tests, df_meals = self.getData()

        if df_tests.empty:
            return 
        
        df_plot_tests = (df_tests
                   .groupby(['event_dt', 'event_time'])
                   .agg(
                       f_score = pd.NamedAgg('f_score','mean'),
                       f_score_h2 = pd.NamedAgg('f_score_h2','mean'),
                       f_score_ch4 = pd.NamedAgg('f_score_ch4','mean'),
                   )
                  ).reset_index()
        
        df_plot_meals = (df_meals
                   .groupby(['event_time'])
                   .agg(
                       quantity = pd.NamedAgg('quantity','sum')
                   )
                  ).reset_index()
                
        max_date = df_plot_tests['event_dt'].max() # not used
        
        dates = df_plot_tests['event_dt'].drop_duplicates().sort_values(ascending = False)
        # currently is last date only used
        for max_date in dates:
            date_range = pd.date_range(start=max_date, end=max_date + timedelta(hours = 24), freq=plot.TIME_STAP)
            df_timestep = pd.DataFrame({'event_time': date_range.floor(plot.TIME_STAP)})
            
            fig, ax1 = plt.subplots(1, 1, figsize=(10, 5))
            
            df_plot = df_timestep.merge(df_plot_tests, on = 'event_time', how = 'left')
            df_plot = df_plot.merge(df_plot_meals, on = 'event_time', how = 'left')#.fillna(0)
            df_plot['event_time'] = df_plot['event_time'].dt.strftime('%H:%M')
            
            THIN_NUMBER = 10
            thin_out_lables = ['' if i % THIN_NUMBER else y for i, y in enumerate(df_plot['event_time'])]
            TITLE = max_date.strftime('%Y.%m.%d')
            
            Y_PLOT_VALUES = {
                'f_score': { 'color' : 'whitesmoke', 'label' : 'Score', 'linewidth' : 3},
                'f_score_h2': { 'color' : 'skyblue', 'label' : 'H2', 'linewidth' : 3},
                'f_score_ch4': { 'color' : 'navy', 'label' : 'CH4', 'linewidth' : 3},
            }
            
            def snsStackedLinePlot (ax, data, x, y_array):
                for index, line in enumerate(y_array.keys()):
                    sns.lineplot(ax = ax, data = data, x = x, y = line, **y_array[line])
                    l1 = ax1.lines[index]
                    x1 = l1.get_xydata()[:, 0]
                    y1 = l1.get_xydata()[:, 1]
                    ax.fill_between(x1, y1, color=y_array[line]['color'], alpha=0.4)
                pass
            
            snsStackedLinePlot(ax = ax1, data = df_plot, x = 'event_time', y_array = Y_PLOT_VALUES)
            ax1.set_ylim(bottom = 0, top = 100)
            
            ax2 = ax1.twinx()
            sns.barplot(ax = ax2, data = df_plot, x = 'event_time', y = 'quantity', color = 'green', width = 4)
            ax2.set_xticks(thin_out_lables, thin_out_lables, rotation=45, ha='right')
            
            plt.title(TITLE)
            plt.show(block=False)
            plt.pause(30)
            plt.close()
        pass
        
plot = plotter()
plot.plotAllData()