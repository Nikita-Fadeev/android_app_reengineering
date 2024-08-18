package com.foodmarble.pressdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Toast;


import l4.m1;

import d4.zi;

public class MainActivity extends AppCompatActivity {
    private static Context mContext;
    m1 dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        Log.d("llllll", mContext.toString());

        m1 dbHelper = new m1(this);
    }

    public static Context getContext() {
        return mContext;
    }
    public void onStartButtonClick(View view) {
        zi Myzi = new zi();
        //
        putTestData();
        //
        Toast.makeText(this, "dddd", Toast.LENGTH_SHORT).show();
    }
    public static void putTestData(){

        m1 dbHelper = new m1(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(m1.COLUMN_NAME, "name");
        values.put(m1.COLUMN_SCORE, 9.6);

        long newRowId = db.insert(m1.TABLE_TESTS, null, values);

        if (newRowId != -1) {
            Log.d("Insert", "New row inserted with ID: " + newRowId);
        } else {
            Log.d("Insert", "Error inserting new row");
        }

        db.close();
    }
}