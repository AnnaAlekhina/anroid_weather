package com.example.anna.weather_5days;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class NewActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ListView listView;
    private TextView CityNameT;
    public DB db;
    SimpleCursorAdapter scAdapter;
    String name;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        Intent intent = getIntent();
        name = intent.getStringExtra("txt");

        CityNameT = (TextView) findViewById(R.id.cityText);
        CityNameT.setText("город " + name);
        listView = (ListView) findViewById(R.id.lv2Data);
        db = new DB(this);
        db.open();
        DownLoadManager dm = new DownLoadManager(db);
        dm.downloadJsonToDB(name);


        String[] from = new String[]{DB.date, DB.description, DB.temp,DB.icon};
        int[] to = new int[]{R.id.tvTextdata, R.id.tvTextdiscrp, R.id.tvTexttemp,R.id.ivImg};

        scAdapter = new SimpleCursorAdapter(this, R.layout.item2, null, from, to, 0);
        listView.setAdapter(scAdapter);
        getLoaderManager().initLoader(0, null, this);
    }

    public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {

        Toast.makeText(getApplicationContext(), ((TextView) itemClicked).getText(),
                Toast.LENGTH_SHORT).show();
    }


    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MyCursorLoader(this,db);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        scAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    public static class MyCursorLoader extends CursorLoader {
        DB db;
        public MyCursorLoader(Context context, DB db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db.getAllData(2);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return cursor;
        }
    }
}
