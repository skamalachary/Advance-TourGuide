package com.sonu.advancesonu.main;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sonu.advancesonu.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class VenueDetailsActivity extends AppCompatActivity {

    WebView mWvVenueDetails;
    TextView cityField, detailsField, currentTemperatureField, humidity_field, pressure_field, weatherIcon, updatedField;

    Typeface weatherFont;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_details);


        mWvVenueDetails = (WebView) findViewById(R.id.wv_venue_details);

        mWvVenueDetails.getSettings().setUseWideViewPort(false);

        // Getting place reference from the map
        //String name = getIntent().getStringExtra("venuename");
        //String name1=name.substring(0,name.indexOf(':'));
        String venueID=getIntent().getStringExtra("venueID");

        StringBuilder sb = new StringBuilder("https://api.foursquare.com/v2/venues/");
        sb.append(venueID);
        sb.append("?client_id=DPFWRMIBQN4MCRC4ND4NEWGG1FZ4NW2NJPJOIXOAMUUZGUSK");
        sb.append("&client_secret=MXJG4PPWU0R2GPDTUIOCEXEZKRAQG5SWLOUKLXY0TFUODM3N");
        sb.append("&v=20180207");


        // Creating a new non-ui thread task to download Google place details
        VenueDetailsActivity.VenuesTask venuesTask = new VenueDetailsActivity.VenuesTask();

        // Invokes the "doInBackground()" method of the class VenueTask
        venuesTask.execute(sb.toString());


        weatherFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/weathericons-regular-webfont.ttf");

        cityField = (TextView)findViewById(R.id.city_field);
        updatedField = (TextView)findViewById(R.id.updated_field);
        detailsField = (TextView)findViewById(R.id.details_field);
        currentTemperatureField = (TextView)findViewById(R.id.current_temperature_field);
        //humidity_field = (TextView)findViewById(R.id.humidity_field);
        //pressure_field = (TextView)findViewById(R.id.pressure_field);
        weatherIcon = (TextView)findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);



    };


    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);


            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();
            br.close();

        }catch(Exception e){
            Log.d("Exception downloading", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }


    /** A class, to download Google Venue Details */
    private class VenuesTask extends AsyncTask<String, Integer, String> {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result){
            VenueDetailsActivity.ParserTask parserTask = new VenueDetailsActivity.ParserTask();

            // Start parsing the Google place details in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }
    }


    /** A class to parse the Google Venue Details in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, HashMap<String, String>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected HashMap<String, String> doInBackground(String... jsonData) {

            HashMap<String, String> hVenueDetails = null;
            VenueDetailsJSONParser placeDetailsJsonParser = new VenueDetailsJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                // Start parsing Google place details in JSON format
                hVenueDetails = placeDetailsJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return hVenueDetails;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(HashMap<String, String> hVenueDetails){


            String name = hVenueDetails.get("name");
            String icon = hVenueDetails.get("icon");
            String address = hVenueDetails.get("address");
            String lat = hVenueDetails.get("lat");
            String lng = hVenueDetails.get("lng");
            //String formatted_address = hVenueDetails.get("formatted_address");
            //String formatted_phone = hVenueDetails.get("formatted_phone");
            String city = hVenueDetails.get("city");
            String rating = hVenueDetails.get("rating");
            //String international_phone_number = hVenueDetails.get("international_phone_number");
            String url = hVenueDetails.get("url");


            String mimeType = "text/html";
            String encoding = "utf-8";

            String data = 	"<html>"+
                    "<body><img style='float:left' src="+icon+" /><h1><center>"+name+"</center></h1>" +
                    "<br style='clear:both' />" +
                    "<hr  />"+
                    "<p>Location : " + lat + "," + lng + "</p>" +
                    "<p>Address : " + address + "</p>" +
                    "<p>City : " + city + "</p>" +
                    "<p>Rating : " + rating + "</p>" +
                    "<p>URL  : <a href='" + url + "'>" + url + "</p>" +
                    "</body></html>";


                // Setting the data in WebView
            mWvVenueDetails.loadDataWithBaseURL("", data, mimeType, encoding, "");
            Function.placeIdTask asyncTask =new Function.placeIdTask(new Function.AsyncResponse() {
                public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure, String weather_updatedOn, String weather_iconText, String sun_rise) {

                    cityField.setText(weather_city);
                    updatedField.setText(weather_updatedOn);
                    detailsField.setText(weather_description);
                    currentTemperatureField.setText(weather_temperature+"C");
                    //humidity_field.setText("Humidity: "+weather_humidity);
                    //pressure_field.setText("Pressure: "+weather_pressure);
                    weatherIcon.setText(Html.fromHtml(weather_iconText));

                }
            });
            // you can paste your city "latitude" and "longitude" here
            asyncTask.execute(lat, lng); //  asyncTask.execute("Latitude", "Longitude")

        }
    }
}
