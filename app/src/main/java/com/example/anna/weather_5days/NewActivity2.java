package com.example.anna.weather_5days;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class NewActivity2 extends Activity implements View.OnClickListener {

    private TextView CityNameT;
    private TextView Final;
    String name;
    private final static String TAG = "JsonManager";
    private Button btnJson;
    private ImageView image;


    String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    String forAPPID = "&appid=";
    String APPID = "d8a54da4ada01c1de42961246c36847a";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new2);
        Intent intent = getIntent();
        name = intent.getStringExtra("txt");
        Final = (TextView) findViewById(R.id.FinalText);
        CityNameT = (TextView) findViewById(R.id.cityText);
        CityNameT.setText("Сейчас в " + name);
        btnJson = (Button)findViewById(R.id.btnOnJson);
        btnJson.setOnClickListener(this);
        image=(ImageView)findViewById(R.id.imageV);
    }

    @Override
    public void onClick(View v) {
        downloadJson(BASE_URL + name + forAPPID + APPID);
    }

    public void downloadJson(String url) {
        new DownloadJsonTask().execute(url);
    }

    public class DownloadJsonTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject json = null;
            String iUrl = params[0];
            HttpURLConnection conn = null;
            InputStreamReader in = null;
            String str = " ";
            try {
                Log.v(TAG, "Starting loading image by URL: " + iUrl);
                conn = (HttpURLConnection) new URL(iUrl).openConnection();
                conn.setDoInput(true);
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setReadTimeout(10000);
                conn.connect();
                StringBuilder sb = new StringBuilder();
                in = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(in);
                int cp;
                while ((cp = bufferedReader.read()) != -1) {
                    sb.append((char) cp);
                }
                bufferedReader.close();
                str = sb.toString();
                json = new JSONObject(str);

            } catch (MalformedURLException ex) {
                Log.e(TAG, "Url parsing was failed: " + iUrl);
            } catch (IOException ex) {
                Log.d(TAG, iUrl + " does not exists");
            } catch (OutOfMemoryError e) {
                Log.w(TAG, "Out of memory!!!");
            } catch (JSONException e) {
                Log.w(TAG, "JSON ERROR");
            } catch (Exception e) {
                Log.d(TAG, "Something wrong!");
            } finally {

                if (conn != null)
                    conn.disconnect();
            }
            return json;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);}

        @Override
        protected void onPostExecute(JSONObject result){
            super.onPostExecute(result);

            if (result == null) Final.setText("Результат не найден");
            else {
                try {
                    Final.setText("ok");
                    JSONObject jsobj = result.getJSONObject("coord");
                    JSONObject jsobj1 = result.getJSONObject("wind");
                    JSONObject jsobj2 = result.getJSONObject("main");

                    JSONArray weather = result.getJSONArray("weather");
                    JSONObject wet = weather.getJSONObject(0);;

                    String descrp = wet.getString("description");
                    String main = wet.getString("main");
                    String icon = wet.getString("icon");

                    String str1 = jsobj.getString("lat");
                    String str2 = jsobj.getString("lon");
                    String str3 = jsobj1.getString("speed");
                    String str4 = jsobj2.getString("temp");

                    double t1 = Double.parseDouble(str4);
                    int t2=(int)t1-273;
                    String Itemp=t2+"";
                    if(t2>0) Itemp = "+"+ t2;

                    switch (icon) {
                        case "01d":
                            image.setImageResource(R.drawable.ic_01d);
                            break;
                        case "01n":
                            image.setImageResource( R.drawable.ic_01n);
                            break;
                        case "02d":
                            image.setImageResource( R.drawable.ic_02d);
                            break;
                        case "02n":
                            image.setImageResource(R.drawable.ic_02n);
                            break;
                        case "03d":
                            image.setImageResource(R.drawable.ic_03d);
                            break;
                        case "03n":
                            image.setImageResource( R.drawable.ic_03n);
                            break;
                        case "04d":
                            image.setImageResource( R.drawable.ic_04d);
                            break;
                        case "04n":
                            image.setImageResource(R.drawable.ic_04n);
                            break;
                        case "09d":
                            image.setImageResource( R.drawable.ic_09d);
                            break;
                        case "09n":
                            image.setImageResource(R.drawable.ic_09n);
                            break;
                        case "10d":
                            image.setImageResource(R.drawable.ic_10d);
                            break;
                        case "10n":
                            image.setImageResource(R.drawable.ic_10n);
                            break;
                        case "11d":
                            image.setImageResource(R.drawable.ic_11d);
                            break;
                        case "11n":
                            image.setImageResource(R.drawable.ic_11n);
                            break;
                        case "13d":
                            image.setImageResource(R.drawable.ic_13d);
                            break;
                        case "13n":
                            image.setImageResource( R.drawable.ic_13n);
                            break;
                        case "50d":
                            image.setImageResource(R.drawable.ic_50d);
                            break;
                        case "50n":
                            image.setImageResource(R.drawable.ic_50n);
                            break;
                    }

                    switch (main) {
                        case "Drizzle":
                            main = "Морось";
                            break;
                        case "Smoke":
                            main = "Туман";
                            break;
                        case "Mist":
                            main = "Туман";
                            break;
                        case "Clouds":
                            main = "Облачно";
                            break;
                        case "brocken clouds":
                            main = "Переменная облачность";
                            break;
                        case "Rain":
                            main = "Дождь";
                            break;
                        case "light rain":
                            main = "Небольшой дождь";
                            break;
                        case "sky is clear":
                            main = "Ясно";
                            break;
                        case "Clear":
                            main = "Ясно";
                            break;
                        case "snow":
                            main = "Снег";
                            break;
                        case "light snow":
                            main = "Небольшой снег";
                            break;
                        case "overcast clouds":
                            main = "Тучи";
                            break;
                        case "moderate rain":
                            main = "Небольшой дождь";
                            break;
                        case "scattered clouds":
                            main = "Рассеянные облака";
                            break;
                        case "few clouds":
                            main = "Лёгкая облачность";
                            break;
                    }

                    Final.setText( "Координаты: " + str1 + " * " + str2 + "\n" + "Описание: "+/*descrp+ "\n" + */main+ "\n" +"Скорость ветра: "+ str3+ " м/с"+"\n" +"Температура: "+ Itemp);

                } catch (JSONException e) {
                    Final.setText("Произошла ошибка");
                }
            }
        }
    }
}
