package com.example.tianc.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForecast extends Activity {

    protected static final String ACTIVITY_NAME = "WeatherForecast";

    protected static final String URL = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";
    protected ProgressBar progressBar;
    protected ImageView currentWeather;
    protected TextView currentTemp;
    protected TextView minTemp;
    protected TextView maxTemp;
    protected TextView windSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        currentWeather = findViewById(R.id.current_weather);
        currentTemp = findViewById(R.id.current_temperature);
        minTemp = findViewById(R.id.min_temperature);
        maxTemp = findViewById(R.id.max_temperature);
        windSpeed = findViewById(R.id.wind_speed);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        ForecastQuery weatherForecast = new ForecastQuery();
        weatherForecast.execute(URL);
        Log.i(ACTIVITY_NAME,"onCreate");
    }

    private class ForecastQuery extends AsyncTask<String,Integer,String>{

        private String wind_speed;
        private String min_temp;
        private String max_temp;
        private String current_temp;
        private Bitmap icon;

        @Override
        protected String doInBackground(String... args){
            XmlPullParser xmlPullParser = Xml.newPullParser();
            try {
                InputStream in = downloadUrl(URL);
                xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xmlPullParser.setInput(in,"UTF-8");
                xmlPullParser.nextTag();

                while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT){
                    if(xmlPullParser.getEventType()==xmlPullParser.START_TAG) {
                        Log.i(ACTIVITY_NAME, "Iterating the XML tags");
                        System.out.println(xmlPullParser.getName());
                        if (xmlPullParser.getName().equals("temperature")) {
                            current_temp = xmlPullParser.getAttributeValue(null, "value");
                            publishProgress(25);
                            min_temp = xmlPullParser.getAttributeValue(null, "min");
                            max_temp = xmlPullParser.getAttributeValue(null, "max");
                            publishProgress(50);
                        }
                        if (xmlPullParser.getName().equals("speed")) {
                            wind_speed = xmlPullParser.getAttributeValue(null, "value");
                            publishProgress(75);
                        }

                        if (xmlPullParser.getName().equals("weather")) {
                            String iconName = xmlPullParser.getAttributeValue(null, "icon");
                            String iconFile = iconName + ".png";
                            if (fileExistence(iconFile)) {
                                FileInputStream fis = new FileInputStream(getBaseContext().getFileStreamPath(iconFile));
                                icon = BitmapFactory.decodeStream(fis);
                                Log.i(ACTIVITY_NAME, "Image exists");
                            } else {
                                URL iconURL = new URL("http://openweathermap.org/img/w/" + iconName + ".png");
                                icon = getImage(iconURL);
                                FileOutputStream fos = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                                icon.compress(Bitmap.CompressFormat.PNG, 80, fos);
                                fos.flush();
                                fos.close();
                                Log.i(ACTIVITY_NAME, "A new image is added");

                            }
                            publishProgress(100);
                        }

                    }
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.i(ACTIVITY_NAME, "Background processing completed");


            return "Done";
        }

        private InputStream downloadUrl(String urlStr) throws IOException {
            URL url = new URL(URL);
            HttpURLConnection conn  = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            return conn.getInputStream();

        }
        private boolean fileExistence(String iconFile) {
            File file = getBaseContext().getFileStreamPath(iconFile);
            return file.exists();
        }

        public Bitmap getImage(URL url) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else
                    return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
        public Bitmap getImage(String urlString) {
            try {
                URL url = new URL(urlString);
                return getImage(url);
            } catch (MalformedURLException e) {
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
            Log.i(ACTIVITY_NAME, "In onProgressUpdate");

        }

        @Override
        protected void onPostExecute(String string) {

            String degree = " "+Character.toString((char) 0x00B0)+" ";
            currentTemp.setText("Current Temperature: "+ currentTemp.getText()+current_temp+degree+"C");
            minTemp.setText("Lowest Temperature: " + minTemp.getText()+min_temp+degree+"C");
            maxTemp.setText("Highest Temperatrue: " + maxTemp.getText()+max_temp+degree+"C");
            windSpeed.setText(" Wind Speed: " + windSpeed.getText() +wind_speed+" m/s");
            currentWeather.setImageBitmap(icon);
            progressBar.setVisibility(View.INVISIBLE);

        }


    }
}
