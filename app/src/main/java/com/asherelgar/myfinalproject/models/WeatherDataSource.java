package com.asherelgar.myfinalproject.models;


import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.asherelgar.myfinalproject.IO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherDataSource {


    //TODO: Fix this to show the real list
    public static List<Weather> ITEMS = new ArrayList<>();
    OkHttpClient client = new OkHttpClient();

    public static void getWeather(final OnWeatherListener listener, final double LATITUDE, final double LONGITUDE) {

//        if (searchLink == null){
//            searchLink = "Tel Aviv";
//        }
//        final String copy = searchLink;
        //1)Internet Permission
        //2)Secondary Thread
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                try {
                    //web address:
                    WeatherDataSource example = new WeatherDataSource();
                    //String response = example.run("http://api.openweathermap.org/data/2.5/weather?q=" + copy + "&appid=288ca3c192923f79bd74f4d01a9299c0&units=metric");

                    String response2 = example.run("http://api.openweathermap.org/data/2.5/weather?lat=" + LATITUDE + "&lon=" + LONGITUDE + "&appid=288ca3c192923f79bd74f4d01a9299c0&units=metric");


                    Log.d("RRR", "response: " + response2);
                    //URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + copy + ",&appid=288ca3c192923f79bd74f4d01a9299c0&units=metric");
                    //open connection
                    // HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    //TODO: Check how asher got nulls here!

                    //int responseCode = con.getResponseCode();


                    // String dateResponse = con.getHeaderField(1);


                    // Log.d("DDDD22", "run: " + dateResponse);

                    //con.getInputStream (binary input stream.)
                    //InputStream in = con.getInputStream();

                    //parseJson
                    Weather data = parseJson2(response2);
                    listener.onWeather(data); //notify the listeners-> send the data to the listener

                    Log.d("GGG5", "run: " + data);
                } catch (Exception e) {
                    //Server connection can't...
                    e.printStackTrace();
                    //Log.d("GGG5", ">>" + copy + " err: " + e);

                }
            }
        });

        service.shutdown();

    }

    private static Weather parseJson2(String r) throws IOException, JSONException {
        Weather data = null;
        String json = IO.getString2(r);
        //Log.d("Hackeru", json);
        JSONObject root = new JSONObject(json);
        JSONObject weatherObject = root.getJSONArray("weather").getJSONObject(0);
        String description = weatherObject.getString("description");
        String icon = weatherObject.getString("icon");
        String cityName = root.getString("name");

        double temp = root.getJSONObject("main").getDouble("temp");

        JSONObject sysObject = root.getJSONObject("sys");
        long sunrise = sysObject.getLong("sunrise");
        long sunset = sysObject.getLong("sunset");

        Weather d = new Weather(description, temp, icon, sunrise, sunset, cityName);
        Log.d("Hackeru", d.toString());

        return d;
    }

    private static Weather parseJson(InputStream in) throws IOException, JSONException {
        Weather data = null;
        String json = IO.getString(in);
        //Log.d("Hackeru", json);
        JSONObject root = new JSONObject(json);
        JSONObject weatherObject = root.getJSONArray("weather").getJSONObject(0);
        String description = weatherObject.getString("description");
        String icon = weatherObject.getString("icon");
        String cityName = root.getString("name");

        double temp = root.getJSONObject("main").getDouble("temp");

        JSONObject sysObject = root.getJSONObject("sys");
        long sunrise = sysObject.getLong("sunrise");
        long sunset = sysObject.getLong("sunset");

        Weather d = new Weather(description, temp, icon, sunrise, sunset, cityName);
        Log.d("Hackeru", d.toString());

        return d;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Log.d("DateRR", "run: " + request.toString());


        try (Response response = client.newCall(request).execute()) {
            String v = response.header("Date").intern();
            Log.d("DateR", "run: " + v);
            return response.body().string();
        }
    }

    public interface OnWeatherListener {
        void onWeather(Weather data);
    }

    /**
     * //Step 0:
     * A dummy item representing a piece of content.
     */
    public static class Weather {
        //Properties:

        private final String description;
        private final String icon;
        private final double temp;
        private final long sunrise;
        private final long sunset;
        private final String cityName;

        //Constructor:
        public Weather(String description, double temp, String icon, long sunrise, long sunset, String cityName) {
            this.description = description;
            this.temp = temp;
            this.icon = icon;
            this.sunrise = sunrise;
            this.sunset = sunset;
            this.cityName = cityName;

        }

        public String getCityName() {
            return cityName;
        }

        //Getters:
        public String getDescription() {
            return description;
        }

        public double getTemp() {
            return temp;
        }

        public String getIcon() {
            return icon;
        }

        public long getSunrise() {
            return sunrise;
        }

        public long getSunset() {
            return sunset;
        }

        //toString()

        @Override
        public String toString() {
            return "Weather{" +
                    "description='" + description + '\'' +
                    ", icon='" + icon + '\'' +
                    ", temp=" + temp +
                    ", sunrise=" + sunrise +
                    ", sunset=" + sunset +
                    ", cityName='" + cityName + '\'' +
                    '}';
        }
    }


}
