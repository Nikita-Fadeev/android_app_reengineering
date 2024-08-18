from flask import Flask, jsonify, abort, make_response, request
import json
from datetime import datetime
import os 
from getPlot import plotter

app = Flask(__name__)

@app.route("/")
def hello():
    return "Hello World!"

@app.route('/', methods=['POST'])
def receive_message():
    message = request.data.decode('utf-8')
    print(message)
    
    data = json.loads(message)
    file_name = str(datetime.now()).replace(":", "_") + '.json'
    file_path = os.path.join(cwd, file_name)
    with open(file_path, 'w') as file:
        json.dump(data, file, indent=4)
    plot = plotter()
    plot.plotAllData()
    return 'Message received and printed to console'

if __name__ == '__main__':  
    FOLDER_WITH_DATA = 'DATA'
    cwd = os.path.join(os.getcwd(), FOLDER_WITH_DATA)
    app.run(debug=True)