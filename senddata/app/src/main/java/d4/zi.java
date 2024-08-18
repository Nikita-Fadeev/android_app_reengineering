package d4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.preference.PreferenceManager;

import com.foodmarble.pressdemo.MainActivity;
import l4.m1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class zi {
    MainActivity V0;
    m1 dbHelper;

    public zi() {
        new HttpUrlConnectionTask().execute();

    }

    private class HttpUrlConnectionTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            //Log.d("dddddd", "ddddd");
            //HttpUrlConnectionSend("[{\"f_score\":\"62.8935\",\"f_score_h2\":\"62.8935\"}]");

            //String table = "tests";
            String [] tables = {"tests", "meals", "readings", "q_answers"};
            JSONArray jsonArray = new JSONArray();
            for (String tableName : tables) {

                JSONArray JSONdata = getAllData(tableName);
                JSONObject JSONtable = new JSONObject();

                try {
                    JSONtable.put(tableName, JSONdata);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                jsonArray.put(JSONtable);
            }
            Log.d("jjjjj",jsonArray.toString());
            HttpUrlConnectionSend(jsonArray.toString());
            return null;
        }
    }

    public static void HttpUrlConnectionSend(String json_query) {
        try {
            String URL_link = "https://39d2-89-113-148-252.ngrok-free.app/";
            URL url = new URL(URL_link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "text/plain");
            connection.setDoOutput(true);

            String data = json_query;
            OutputStream os = connection.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            os.close();

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("Range")
    public JSONArray getAllData(String table) {
        String query = "SELECT * FROM " + table;
        Context context = MainActivity.getContext();
        this.dbHelper = m1.d(context, "user1");

        ArrayList<Cursor> result = dbHelper.a(query);

        JSONArray jsonArray = new JSONArray();
        Cursor cursor = result.get(0);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                JSONObject jsonObject = new JSONObject();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    try {
                        jsonObject.put(cursor.getColumnName(i), cursor.getString(i));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                jsonArray.put(jsonObject);
            } while (cursor.moveToNext());
        }

        return jsonArray;
    }
}
