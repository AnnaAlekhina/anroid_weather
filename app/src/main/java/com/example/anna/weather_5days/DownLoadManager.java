package com.example.anna.weather_5days;

public class DownLoadManager {
    String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    String DAY5_WEATHER = "forecast?q=";
    String MODE = "&mode=json";
    String forAPPID = "&appid=";
    String APPID="d8a54da4ada01c1de42961246c36847a";

//http://api.openweathermap.org/data/2.5/forecast?q=london&mode=json&appid=d8a54da4ada01c1de42961246c36847a

    DB db;

    public DownLoadManager(DB dbh){
        db = dbh;
    }

    public void downloadJsonToDB(String cityName){
        new DownloadJson(db).execute(BASE_URL + DAY5_WEATHER + cityName + MODE + forAPPID + APPID);
    }
}
