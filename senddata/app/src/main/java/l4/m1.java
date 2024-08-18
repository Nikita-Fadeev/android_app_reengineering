package l4;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.foodmarble.pressdemo.MainActivity;

import java.lang.Exception;

import java.util.ArrayList;

import d4.zi;

public class m1 extends SQLiteOpenHelper {

    private static m1 f29392e;
    private static final String DATABASE_NAME = "tests.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TESTS = "tests";
    public static final String TABLE_MEALS = "meals";
    public static final String TABLE_READINGS = "readings";
    public static final String TABLE_ANSWERS = "q_answers";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SCORE = "score";

    private static final String CREATE_TABLE_TESTS = "CREATE TABLE " + TABLE_TESTS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_SCORE + " REAL);";
    private static final String CREATE_TABLE_MEALS = "CREATE TABLE " + TABLE_MEALS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_SCORE + " REAL);";
    private static final String CREATE_TABLE_READINGS = "CREATE TABLE " + TABLE_READINGS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_SCORE + " REAL);";

    private static final String CREATE_TABLE_ANSWERS = "CREATE TABLE " + TABLE_ANSWERS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_SCORE + " REAL);";


    public m1(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public m1(Context context, String str) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static m1 d(Context context, String str) {
        if (f29392e == null) {

            f29392e = new m1(context.getApplicationContext(), str);
        }

        return f29392e;
    }

    public ArrayList<Cursor> a(String str) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ArrayList<Cursor> arrayList = new ArrayList<>(2);
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"message"});
        arrayList.add(null);
        arrayList.add(null);
        try {
            Cursor rawQuery = writableDatabase.rawQuery(str, null);
            matrixCursor.addRow(new Object[]{"Success"});
            arrayList.set(1, matrixCursor);
            if (rawQuery != null && rawQuery.getCount() > 0) {
                arrayList.set(0, rawQuery);
                rawQuery.moveToFirst();
            }
            return arrayList;
        } catch (SQLException e10) {
            Log.d("printing exception", e10.getMessage());
            matrixCursor.addRow(new Object[]{"" + e10.getMessage()});
            arrayList.set(1, matrixCursor);
            return arrayList;
        } catch (Exception  e11) {
            Log.d("printing exception", e11.getMessage());
            matrixCursor.addRow(new Object[]{"" + e11.getMessage()});
            arrayList.set(1, matrixCursor);
            return arrayList;
        }
    }

        @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TESTS);
        db.execSQL(CREATE_TABLE_MEALS);
        db.execSQL(CREATE_TABLE_READINGS);
        db.execSQL(CREATE_TABLE_ANSWERS);

    }

    @Override   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TESTS);
        onCreate(db);
    }
}