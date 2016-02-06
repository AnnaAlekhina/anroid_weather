package com.example.anna.weather_5days;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Anna on 24.01.2016.
 */
public class DB {
    private static final int DB_VERSION = 1;
    private static final String NAME_DB = "myDB2";
    public static final String NAME_TABLE1 = "mycity";
    public static final String NAME_TABLE2 = "myweather";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TXT = "txt";

    private static final String DB_CREATEcity = "create table " + NAME_TABLE1 + "(" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_TXT + " text" +
            ");";
    public static final String date = "date";
    public static final String temp = "temp";
    public static String description = "description";
    public static final String icon = "icon";

    private static final String DB_CREATEweather =
            "create table " + NAME_TABLE2 + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    date + " text, " + temp + " text, " + description + " text, " + icon + " text"+");";

    private final Context mCtx;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DB(Context ctx) {
        mCtx = ctx;
    }

    public void open() {
        mDBHelper = new DBHelper(mCtx, NAME_DB, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    public void close() {
        if (mDBHelper != null) mDBHelper.close();
    }

    public Cursor getAllData(int namedb) {
        if (namedb == 1) return mDB.query(NAME_TABLE1, null, null, null, null, null, null);
        if (namedb == 2) return mDB.query(NAME_TABLE2, null, null, null, null, null, null);
        else return null;
    }

    public void addRec(ArrayList<String> str, int namedb) {
        if (namedb == 1) {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_TXT, str.get(0));
            mDB.insert(NAME_TABLE1, null, cv);
        }
        if (namedb == 2){
            ContentValues cv = new ContentValues();

            switch (description) {
                case "Clouds":
                    description = "Облачно";
                    break;
                case "Brocken clouds":
                    description = "Переменная облачность";
                    break;
                case "Rain":
                    description = "Дождь";
                    break;
                case "Light rain":
                    description = "Небольшой дождь";
                    break;
                case "Sky is clear":
                    description = "Ясно";
                    break;
                case "Clear":
                    description = "Ясно";
                    break;
                case "snow":
                    description = "Снег";
                    break;
                case "light snow":
                    description = "Небольшой снег";
                    break;
                case "overcast clouds":
                    description = "Тучи";
                    break;
                case "moderate rain":
                    description = "Небольшой дождь";
                    break;
                case "scattered clouds":
                    description = "Рассеянные облака";
                    break;
                case "few clouds":
                    description = "Лёгкая облачность";
                    break;
            }

            cv.put(date,str.get(0));
            cv.put(temp, str.get(1));
            cv.put(description, str.get(2));

            switch (str.get(3)) {
                case "01d":
                    cv.put(icon,R.drawable.ic_01d);
                    break;
                case "01n":
                    cv.put(icon, R.drawable.ic_01n);
                    break;
                case "02d":
                    cv.put(icon, R.drawable.ic_02d);
                    break;
                case "02n":
                    cv.put(icon, R.drawable.ic_02n);
                    break;
                case "03d":
                    cv.put(icon, R.drawable.ic_03d);
                    break;
                case "03n":
                    cv.put(icon, R.drawable.ic_03n);
                    break;
                case "04d":
                    cv.put(icon, R.drawable.ic_04d);
                    break;
                case "04n":
                    cv.put(icon, R.drawable.ic_04n);
                    break;
                case "09d":
                    cv.put(icon, R.drawable.ic_09d);
                    break;
                case "09n":
                    cv.put(icon, R.drawable.ic_09n);
                    break;
                case "10d":
                    cv.put(icon, R.drawable.ic_10d);
                    break;
                case "10n":
                    cv.put(icon, R.drawable.ic_10n);
                    break;
                case "11d":
                    cv.put(icon, R.drawable.ic_11d);
                    break;
                case "11n":
                    cv.put(icon, R.drawable.ic_11n);
                    break;
                case "13d":
                    cv.put(icon, R.drawable.ic_13d);
                    break;
                case "13n":
                    cv.put(icon, R.drawable.ic_13n);
                    break;
                case "50d":
                    cv.put(icon, R.drawable.ic_50d);
                    break;
                case "50n":
                    cv.put(icon, R.drawable.ic_50n);
                    break;
            }

            mDB.insert(NAME_TABLE2, null, cv);
        }
    }

    public String getRec(int positon) {
        mDB = mDBHelper.getReadableDatabase();
        Cursor c = mDB.query(NAME_TABLE1, null, null, null, null, null, null);
        int nameColIndex = c.getColumnIndex(COLUMN_TXT);
        c.moveToPosition(positon);
        return c.getString(nameColIndex);
    }

    public void delRec(long id) {
        mDB.delete(NAME_TABLE1, COLUMN_ID + " = " + id, null);
    }

    public void delAll() {
        int clearCount1 = mDB.delete(NAME_TABLE1, null, null);
        int clearCount2 = mDB.delete(NAME_TABLE2, null, null);
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATEcity);
            db.execSQL(DB_CREATEweather);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
