package com.sonu.advancesonu.main;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class VenueExploreActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {

    GoogleMap mGoogleMap;
    Spinner mSprVenueType;
    List<HashMap<String, String>> finalList=new ArrayList<HashMap<String, String>>();

    public String inPlc,type;
    String[] mVenueType = null;
    String[] mVenueTypeName = null;

    double mLatitude = 0;
    double mLongitude = 0;

    HashMap<String, String> mMarkerVenueLink = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_explore);

        Bundle bun = getIntent().getExtras();
        inPlc = bun.getString("userplace");
        type = bun.getString("type");

       // mVenueType = getResources().getStringArray(R.array.venue_type);

        // Array of venue type names
       // mVenueTypeName = getResources().getStringArray(R.array.venue_type_name);

        // Creating an array adapter with an array of Venue types
        // to populate the spinner
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mVenueTypeName);

        // Getting reference to the Spinner
       // mSprVenueType = (Spinner) findViewById(R.id.spr_venue_type);

        // Setting adapter on Spinner to set venue types
        //mSprVenueType.setAdapter(adapter);

        //Button btnFind;

        // Getting reference to Find Button
        //btnFind = (Button) findViewById(R.id.btn_find);
        StringBuilder sb = new StringBuilder("https://api.foursquare.com/v2/venues/explore?");
        sb.append("near=" + inPlc);
        sb.append("&client_id=DPFWRMIBQN4MCRC4ND4NEWGG1FZ4NW2NJPJOIXOAMUUZGUSK");
        sb.append("&client_secret=MXJG4PPWU0R2GPDTUIOCEXEZKRAQG5SWLOUKLXY0TFUODM3N");
        sb.append("&v=20180206");
        sb.append("&query=" + type);
        sb.append("&limit=20");

        //String sArr[]={sb.toString(),sb2.toString()};
        // Creating a new non-ui thread task to download Google venue json data
        VenueExploreActivity.VenuesTask venuesTask = new VenueExploreActivity.VenuesTask();

        // Invokes the "doInBackground()" method of the class VenueTask
        venuesTask.execute(sb.toString());


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
            fragment.getMapAsync(VenueExploreActivity.this);

           /* btnFind.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    int selectedPosition = mSprVenueType.getSelectedItemPosition();
                    type = mVenueType[selectedPosition];


                    StringBuilder sb = new StringBuilder("https://api.foursquare.com/v2/venues/explore?");
                    sb.append("near=" + inPlc);
                    sb.append("&client_id=DPFWRMIBQN4MCRC4ND4NEWGG1FZ4NW2NJPJOIXOAMUUZGUSK");
                    sb.append("&client_secret=MXJG4PPWU0R2GPDTUIOCEXEZKRAQG5SWLOUKLXY0TFUODM3N");
                    sb.append("&v=20180206");
                    sb.append("&query=" + type);
                    sb.append("&limit=20");

                    //String sArr[]={sb.toString(),sb2.toString()};
                    // Creating a new non-ui thread task to download Google venue json data
                    VenueExploreActivity.VenuesTask venuesTask = new VenueExploreActivity.VenuesTask();

                    // Invokes the "doInBackground()" method of the class VenueTask
                    venuesTask.execute(sb.toString());


                }
            });*/

        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);


            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception downloading", e.toString());
        } finally {
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
            Toast.makeText(VenueExploreActivity.this, "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();
            return;
        }
        //mGoogleMap.setMyLocationEnabled(true);


        // Getting LocationManager object from System Service LOCATION_SERVICE
        //LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        //  String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location From GPS
        // Location location = locationManager.getLastKnownLocation(provider);

        //          if (location != null) {
        //            onLocationChanged(location);
        //      }

//            locationManager.requestLocationUpdates(provider, 20000, 0, (android.location.LocationListener) this);
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {
                //String s="v";
                if(mMarkerVenueLink.get(arg0.getTitle()).equals("v")) {
                    Intent intent = new Intent(getBaseContext(), VenueDetailsActivity.class);
                    //String title = mMarkerVenueLink.get(arg0.getTitle());
                    String venueid= mMarkerVenueLink.get(arg0.getId());
                    //Toast.makeText(XtestActivity.this, venueid, Toast.LENGTH_LONG).show();
                    //intent.putExtra("venuename", title);
                    intent.putExtra("venueID",venueid);

                    // Starting the Venue Details Activity
                    startActivity(intent);

                }
                else if (mMarkerVenueLink.get(arg0.getTitle()).equals("p")){
                    Intent intent = new Intent(getBaseContext(), PlaceDetailsActivity.class);
                    String reference = mMarkerVenueLink.get(arg0.getId());
                    intent.putExtra("reference", reference);

                    // Starting the Place Details Activity
                    startActivity(intent);
                }
            }
        });

    }

    private class VenuesTask extends AsyncTask<String, Integer, String> {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
                //data[1] = downloadUrl(url[1]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {
            VenueExploreActivity.ParserTask parserTask = new VenueExploreActivity.ParserTask();

            // Start parsing the Google venues in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }
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
            VenueExploreActivity.ParserTask2 parserTask = new ParserTask2();

            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> venues = null;
            FoursquareJsonParser venueJsonParser = new FoursquareJsonParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                /** Getting the parsed data as a List construct */
                venues = venueJsonParser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return venues;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {
            if (list != null) {
                // Clears all the existing markers
                mGoogleMap.clear();
                MarkerOptions markerOpt = new MarkerOptions();
                HashMap<String, String> phmVenue = list.get(0);

                // Getting latitude of the venue
                double plat = Double.parseDouble(phmVenue.get("lat"));

                // Getting longitude of the venue
                double plng = Double.parseDouble(phmVenue.get("lng"));
                mLatitude = plat;
                mLongitude = plng;
                Toast.makeText(VenueExploreActivity.this, plat+"   "+plng, Toast.LENGTH_LONG).show();
                LatLng platLng = new LatLng(plat, plng);

                // Setting the position for the marker
                markerOpt.position(platLng);

                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(platLng));
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                StringBuilder sb2 = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                sb2.append("location=" + mLatitude + "," + mLongitude);
                sb2.append("&radius=5000");
                sb2.append("&types=" + type.toLowerCase().replace(' ','_'));
                sb2.append("&sensor=true");
                sb2.append("&key=AIzaSyCPI8Ad2SIMfw-PGBbYOz4FFfgKl3awYqg");
                // Creating a new non-ui thread task to download Google venue json data
                VenueExploreActivity.PlacesTask placesTask = new VenueExploreActivity.PlacesTask();

                // Invokes the "doInBackground()" method of the class VenueTask
                finalList.clear();
                for (int i=0;i<list.size();i++){
                    finalList.add(list.get(i));
                }
                placesTask.execute(sb2.toString());
            }
        }
    }

    private class ParserTask2 extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                /** Getting the parsed data as a List construct */
                places = placeJsonParser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {

            // Clears all the existing markers
            mGoogleMap.clear();

            for (int i = 0; i < list.size(); i++) {
                finalList.add(list.get(i));
            }
            if(finalList.isEmpty()){
                Toast.makeText(VenueExploreActivity.this, "No Result found", Toast.LENGTH_LONG).show();
            }
            Set<HashMap<String, String>> finalset=new LinkedHashSet<HashMap<String, String>>(finalList);

            for (Iterator<HashMap<String, String>> i = finalset.iterator(); i.hasNext();) {

                HashMap<String, String> temp=i.next();
                if(temp.get("flag").equals("p")){
                    // Creating a marker
                    MarkerOptions markerOptions = new MarkerOptions();

                    // Getting a place from the places list
                    HashMap<String, String> hmPlace = temp;

                    // Getting latitude of the place
                    double lat = Double.parseDouble(hmPlace.get("lat"));

                    // Getting longitude of the place
                    double lng = Double.parseDouble(hmPlace.get("lng"));

                    // Getting name
                    String name = hmPlace.get("place_name");

                    // Getting vicinity
                    String vicinity = hmPlace.get("vicinity");

                    LatLng latLng = new LatLng(lat, lng);

                    // Setting the position for the marker
                    markerOptions.position(latLng);

                    // Setting the title for the marker.
                    //This will be displayed on taping the marker
                    markerOptions.title(name + " : " + vicinity);

                    // Placing a marker on the touched position
                    Marker m = mGoogleMap.addMarker(markerOptions);

                    // Linking Marker id and place reference
                    mMarkerVenueLink.put(m.getId(), hmPlace.get("reference"));
                    mMarkerVenueLink.put(m.getTitle(), hmPlace.get("flag"));

                }
                else {
                    // Creating a marker
                    MarkerOptions markerOptions = new MarkerOptions();

                    // Getting a venue from the venues list
                    HashMap<String, String> hmVenue = temp;

                    // Getting latitude of the venue
                    double lat = Double.parseDouble(hmVenue.get("lat"));

                    // Getting longitude of the venue
                    double lng = Double.parseDouble(hmVenue.get("lng"));

                    // Getting name
                    String name = hmVenue.get("venue_name");

                    // Getting vicinity
                    String address = hmVenue.get("Address");

                    LatLng latLng = new LatLng(lat, lng);

                    // Setting the position for the marker
                    markerOptions.position(latLng);

                    // Setting the title for the marker.
                    //This will be displayed on taping the marker
                    markerOptions.title(name + " : " + address);

                    // Placing a marker on the touched position
                    Marker m = mGoogleMap.addMarker(markerOptions);

                    // Linking Marker id and venue reference
                    mMarkerVenueLink.put(m.getId(), hmVenue.get("venueid"));
                    mMarkerVenueLink.put(m.getTitle(), hmVenue.get("flag"));
                }
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
