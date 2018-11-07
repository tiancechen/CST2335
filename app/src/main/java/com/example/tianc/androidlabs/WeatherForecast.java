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

    protected static final String urlString = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";

    //protected static final String ImageURL = "http://openweathermap.org/img/w/";

    ProgressBar progressBar;

    TextView cT;
    TextView minT;
    TextView maxT;
    TextView wS;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);

        cT = findViewById(R.id.currentTemp);
        minT = findViewById(R.id.minTemp);
        maxT = findViewById(R.id.maxTemp);
        wS = findViewById(R.id.windSpeed);
        image = findViewById(R.id.weatherImage);

        ForecastQuery myQuery = new ForecastQuery();
        myQuery.execute(urlString);

        Log.i(ACTIVITY_NAME,"In onCreate()");



    }

    public static Bitmap getImage(URL url) {
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
    public static Bitmap getImage(String urlString) {
        try {
            URL url = new URL(urlString);
            return getImage(url);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public boolean fileExistance(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

    public class ForecastQuery extends AsyncTask<String, Integer, String>{

        String mint;
        String maxt;
        String ct;
        String wind;
        String iconName;
        Bitmap icon;

        @Override
        protected String doInBackground(String... arg) {
            progressBar.setVisibility(ProgressBar.VISIBLE);

            try{

                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();

                InputStream stream = conn.getInputStream();

                //XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                //factory.setNamespaceAware(false);

                XmlPullParser xpp = Xml.newPullParser();
                xpp.setInput( stream , null);

                while (xpp.next() != XmlPullParser.END_DOCUMENT) {
                    Log.i(ACTIVITY_NAME, "In while");
                    if (xpp.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    if (xpp.getName().equals("temperature")) {
                        ct = xpp.getAttributeValue(null, "value");
                        publishProgress(25);
                        mint = xpp.getAttributeValue(null, "min");
                        publishProgress(50);
                        maxt = xpp.getAttributeValue(null, "max");
                        publishProgress(75);

                    }
                    if (xpp.getName().equals("speed")){
                        wind = xpp.getAttributeValue(null,"value");

                    }

                    if (xpp.getName().equals("weather")) {
                        iconName = xpp.getAttributeValue(null, "icon");
                        String iconFile = iconName + ".png";
                        if (fileExistance(iconFile)) {
                            FileInputStream inputStream = null;
                            try {
                                inputStream = new FileInputStream(getBaseContext().getFileStreamPath(iconFile));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            icon = BitmapFactory.decodeStream(inputStream);
                            Log.i(ACTIVITY_NAME, "Image already exists");
                        } else {
                            //Bitmap image  = HTTPUtils.getImage(ImageURL));

                            URL iconUrl = new URL("http://openweathermap.org/img/w/" + iconName + ".png");
                            icon = getImage(iconUrl);
                            FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                            icon.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                            outputStream.flush();
                            outputStream.close();
                            Log.i(ACTIVITY_NAME, "Add a new image");
                        }
                    }
                }

            }catch(MalformedURLException urlEX){
                urlEX.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            publishProgress(100);
            Log.i(ACTIVITY_NAME, "In doInBackground");
            return "Done!";
        }


        @Override
        protected void onProgressUpdate(Integer... values) {

            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
            Log.i(ACTIVITY_NAME, "In onProgressUpdate");

        }

        @Override
        protected void onPostExecute(String string) {

            String degree = Character.toString((char) 0x00B0);
            cT.setText(cT.getText()+ct+degree+"C");
            minT.setText(minT.getText()+mint+degree+"C");
            maxT.setText(maxT.getText()+maxt+degree+"C");
            wS.setText(wS.getText() +wind+"m/s");
            image.setImageBitmap(icon);
            progressBar.setVisibility(View.INVISIBLE);

        }
    }





}
