package com.sonu.advancesonu.main;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sonu.advancesonu.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;


public class XtestActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {

    GoogleMap mGoogleMap;
    public String inPlc;
    String venueID;

    double mLatitude = 0;
    double mLongitude = 0;

    HashMap<String, String> mMarkerPlaceLink = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xtest);

        Bundle bun=getIntent().getExtras();
        inPlc=bun.getString("userplace");

        TextView tvTop= (TextView) findViewById(R.id.tv_Top);

        // Getting reference to Find Button
        //btnFind = (Button) findViewById(R.id.btn_Top);


        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());


        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        } else { // Google Play Services are available

            // Getting reference to the SupportMapFragment
            SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            // Getting Google Map
            fragment.getMapAsync(XtestActivity.this);

           }
    }

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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;

        // Enabling MyLocation in Google Map
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(XtestActivity.this, "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();
            return;
        }
        // mGoogleMap.setMyLocationEnabled(true);



        // Getting LocationManager object from System Service LOCATION_SERVICE
        //LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        //Criteria criteria = new Criteria();

        // Getting the name of the best provider
        //String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location From GPS
        //Location location = locationManager.getLastKnownLocation(provider);

//            if(location!=null){
        //              onLocationChanged(location);
        //        }

        //      locationManager.requestLocationUpdates(provider, 20000, 0, (LocationListener) this);
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {
                Intent intent = new Intent(getBaseContext(), VenueDetailsActivity.class);
                //String title = mMarkerPlaceLink.get(arg0.getTitle());
                String venueid= mMarkerPlaceLink.get(arg0.getId());
                //Toast.makeText(XtestActivity.this, venueid, Toast.LENGTH_LONG).show();
                //intent.putExtra("venuename", title);
                intent.putExtra("venueID",venueid);

                // Starting the Place Details Activity
                startActivity(intent);
            }
        });

        StringBuilder sb = new StringBuilder("https://api.foursquare.com/v2/venues/explore?");
        sb.append("near="+inPlc);
        sb.append("&client_id=DPFWRMIBQN4MCRC4ND4NEWGG1FZ4NW2NJPJOIXOAMUUZGUSK");
        sb.append("&client_secret=MXJG4PPWU0R2GPDTUIOCEXEZKRAQG5SWLOUKLXY0TFUODM3N");
        sb.append("&v=20180206");
        sb.append("&limit=10");


        // Creating a new non-ui thread task to download Google place json data
        XtestActivity.PlacesTask placesTask = new XtestActivity.PlacesTask();

        // Invokes the "doInBackground()" method of the class PlaceTask
        placesTask.execute(sb.toString());


    }

    private class PlacesTask extends AsyncTask<String, Integer, String> {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {
            XtestActivity.ParserTask parserTask = new XtestActivity.ParserTask();
            //Toast.makeText(XtestActivity.this, "Http Query Successful", Toast.LENGTH_LONG).show();
            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParserTask
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            FoursquareJsonParser placeJsonParser = new FoursquareJsonParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                /** Getting the parsed data as a List construct */
                places = placeJsonParser.parse(jObject);
                //Toast.makeText(XtestActivity.this, "Parsing Done", Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {

            if(list!=null) {
                // Clears all the existing markers
                mGoogleMap.clear();
                MarkerOptions markerOpt = new MarkerOptions();
                HashMap<String, String> phmPlace = list.get(0);

                // Getting latitude of the place
                double plat = Double.parseDouble(phmPlace.get("lat"));

                // Getting longitude of the place
                double plng = Double.parseDouble(phmPlace.get("lng"));

                LatLng platLng = new LatLng(plat, plng);

                // Setting the position for the marker
                markerOpt.position(platLng);

                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(platLng));
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                for (int i = 0; i < list.size(); i++) {

                    // Creating a marker
                    MarkerOptions markerOptions = new MarkerOptions();

                    // Getting a place from the places list
                    HashMap<String, String> hmPlace = list.get(i);

                    // Getting latitude of the place
                    double lat = Double.parseDouble(hmPlace.get("lat"));

                    // Getting longitude of the place
                    double lng = Double.parseDouble(hmPlace.get("lng"));

                    // Getting name
                    String name = hmPlace.get("venue_name");

                    // Getting vicinity
                    String address = hmPlace.get("Address");

                    LatLng latLng = new LatLng(lat, lng);

                    // Setting the position for the marker
                    markerOptions.position(latLng);

                    // Setting the title for the marker.
                    //This will be displayed on taping the marker
                    markerOptions.title(name + " : " + address);

                    // Placing a marker on the touched position
                    Marker m = mGoogleMap.addMarker(markerOptions);

                    // Linking Marker id and place reference
                    mMarkerPlaceLink.put(m.getId(), hmPlace.get("venueid"));


                }

                //Toast.makeText(XtestActivity.this, "Parsing Completed", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(XtestActivity.this, "The Entered location cannot be found.Please Enter a valid Location.", Toast.LENGTH_LONG).show();
                Intent i=new Intent(XtestActivity.this, DistantStartActivity.class);
                startActivity(i);
            }
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        LatLng latLng = new LatLng(mLatitude, mLongitude);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));

    }
    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
}
