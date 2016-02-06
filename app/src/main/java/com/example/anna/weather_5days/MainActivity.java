package com.example.anna.weather_5days;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,LoaderManager.LoaderCallbacks<Cursor>{

    private ListView listView;
    private EditText CityNameE;
    private Button loadBut;
    private Button delBut;
    public DB db;
    private  RadioGroup radiogroup;
    private RadioButton RadioButton_now;
    private RadioButton RadioButton_5days;
    SimpleCursorAdapter scAdapter;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    private int selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radiogroup = (RadioGroup )findViewById(R.id.radioGroup);
        selected =  radiogroup.getCheckedRadioButtonId();
        //RadioButton_now=(RadioButton)findViewById(R.id.radio_now);
       // RadioButton_now = new RadioButton(this);
        //RadioButton_5days = new RadioButton(this);


        CityNameE = (EditText) findViewById(R.id.EditCity);
        listView = (ListView) findViewById(R.id.lvData);
        cd = new ConnectionDetector(getApplicationContext());


       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View itemClicked,
                                   int position, long id) {
               switch(selected){
                   case R.id.radio_now: {
                       Intent intent = new Intent(MainActivity.this, NewActivity2.class);
                       intent.putExtra("txt", db.getRec(position));
                       startActivity(intent);
                       break;
                   }
                   case R.id.radio_5days: {
                       Intent intent = new Intent(MainActivity.this, NewActivity.class);
                       intent.putExtra("txt", db.getRec(position));
                       startActivity(intent);
                       break;
                   }
               }
           }
       });


        loadBut = (Button) findViewById(R.id.loadCity);
        delBut = (Button) findViewById(R.id.delCity);
        loadBut.setOnClickListener(this);
        delBut.setOnClickListener(this);

        db = new DB(this);
        db.open();

        String[] from = new String[]{DB.COLUMN_TXT};
        int[] to = new int[]{R.id.tvText};

        scAdapter = new SimpleCursorAdapter(this, R.layout.item, null, from, to, 0);
        listView.setAdapter(scAdapter);
        getLoaderManager().initLoader(0, null, this);
    }

    public void onClickRadio(View v)
    {
        selected = v.getId();
    }
        @Override
        public void onClick (View view){
            switch (view.getId()) {
                case R.id.loadCity: {
                    ArrayList<String> al = new ArrayList<String>();
                    al.add(CityNameE.getText().toString() + "");

                    isInternetPresent = cd.ConnectingToInternet();
                    if (isInternetPresent) {
                        showAlertDialog(MainActivity.this, "Интернет соединение",
                                "У вас есть Интернет соединение", true);
                        db.addRec(al, 1);
                        getLoaderManager().getLoader(0).forceLoad();
                    } else {
                        showAlertDialog(MainActivity.this, "Интернет соединение отсутствует",
                                "У вас нет Интернет соединения", false);
                    }
                    break;
                }
                case R.id.delCity: {
                    db.delAll();
                    getLoaderManager().getLoader(0).forceLoad();
                    break;
                }
            }
        }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon((status) ? R.drawable.eagle1 : R.drawable.chicken1);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();
    }


    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MyCursorLoader(this, db);
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
            Cursor cursor = db.getAllData(1);
            return cursor;
        }

    }


}
