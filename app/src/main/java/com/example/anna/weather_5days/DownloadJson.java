package com.example.anna.weather_5days;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Anna on 24.01.2016.
 */
public class DownloadJson extends AsyncTask<String, Void, JSONObject> {
    DB dbhelper;

    DownloadJson(DB dbh) {
        super();
        dbhelper = dbh;
    }

    @Override
    protected JSONObject doInBackground(String... params) {

        JSONObject json = null;
        String iUrl = params[0];
        HttpURLConnection conn = null;
        InputStreamReader in = null;
        String str = " ";
        try {
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
            Log.v("downloadJson", ex.toString());
        } catch (IOException ex) {
            Log.v("downloadJson", ex.toString());
        } catch (OutOfMemoryError e) {
            Log.v("downloadJson", e.toString());
        } catch (JSONException e) {
            Log.v("downloadJson", e.toString());
        } catch (Exception e) {
            Log.v("downloadJson", e.toString());
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        return json;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);
        int i = 0;
        ArrayList<String> str;
        try {
            JSONArray jlist = result.getJSONArray("list");
            JSONArray jweather;
            JSONObject jmain;
            JSONObject jweather0;
            while (true) {
                str = new ArrayList<String>();

                JSONObject list0 = jlist.getJSONObject(i);
                str.add(list0.getString("dt_txt"));
                jweather = list0.getJSONArray("weather");
                jmain = list0.getJSONObject("main");
                jweather0 = jweather.getJSONObject(0);

                double t1=Double.parseDouble(jmain.getString("temp"));
                int t2=(int)t1-273;
                String t=t2+"";
                if(t2>0) t = "+"+ t2;

                str.add(""+t);
                str.add(jweather0.getString("description"));
                str.add(jweather0.getString("icon"));
                Log.v("str: ", str.get(0) + " " + str.get(1) + " " + str.get(2) +" "+ str.get(3));
                dbhelper.addRec(str,2);
                i++;
            }
        } catch (JSONException e) {
            Log.v("downloadJson", e.toString());
        } catch (Exception e) {

            Log.v("downloadJson", e.toString());
        }

    }

}
