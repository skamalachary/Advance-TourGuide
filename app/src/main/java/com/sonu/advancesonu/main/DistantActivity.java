package com.sonu.advancesonu.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.foursquare.api.types.Venue;
import com.foursquare.placepicker.PlacePicker;
import com.foursquare.placepicker.PlacePickerSdk;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.sonu.advancesonu.R;

import java.util.HashMap;


public class DistantActivity extends AppCompatActivity {

    private static final String CONSUMER_KEY="WYTAI0PYUHH2YTEZ5OKRQ403NX2SBZCTQP5PXUMQVCLCOVPK";
    private static final String CONSUMER_SECRET="NOQ5QOCR15T430WS0S3YMJQO3TLHDIWFSMXYKCLKQV42XDUA";
    private static final String MAPBOX_ACCESS_TOKEN = "sk.eyJ1Ijoic2thbWFsYWNoYXJ5IiwiYSI6ImNsM2w5ZXlsbzBlaTYzZXJzczVhN3J0ZG0ifQ.un35A2oo79GC2YX9ukhCuQ";
    public String inPlace;
    MapView mapView;
    private MapboxMap mapboxMap;
    double lat = 0;
    double lng = 0;
    private LocationServices locationServices;
    private static final int PERMISSIONS_LOCATION = 0;

    HashMap<String, String> mMarkerPlaceLink = new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle nbundle=getIntent().getExtras();
        inPlace=nbundle.getString("userplace");
        // Setup Permissions
        locationServices = LocationServices.getLocationServices(DistantActivity.this);
        if (!locationServices.areLocationPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_LOCATION);
        }

        MapboxAccountManager.start(this, MAPBOX_ACCESS_TOKEN);
        PlacePickerSdk.with(new PlacePickerSdk.Builder(this)
                .consumer(CONSUMER_KEY, CONSUMER_SECRET)
                .imageLoader(new PlacePickerSdk.ImageLoader() {
                    @Override
                    public void loadImage(Context context, ImageView v, String url) {
                        Glide.with(context)
                                .load(url)
                                .placeholder(R.drawable.category_none)
                                .dontAnimate()
                                .into(v);
                    }
                })
                .build());

        setContentView(R.layout.activity_distant);

        Button findPlace = (Button) findViewById(R.id.btnPlacePick);
        findPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPlace();
            }
        });
        Button findTopPicks = (Button) findViewById(R.id.btnTopPicks);
        findTopPicks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(DistantActivity.this,DistantStartActivity .class);
                Bundle bundle=new Bundle();
                bundle.putString("inPlace",inPlace);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        getClosestPlace();
    }

    private void getClosestPlace() {
        PlacePickerSdk.get().getCurrentPlace(new PlacePickerSdk.CurrentPlaceResult() {
            @Override
            public void success(Venue venue, boolean confident) {
                lat = venue.getLocation().getLat();
                lng = venue.getLocation().getLng();


                mapView.getMapAsync(new OnMapReadyCallback() {

                    @Override
                    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                        DistantActivity.this.mapboxMap = mapboxMap;
                        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));
                        mapboxMap.setMyLocationEnabled(true);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                            }
                        }, 500);

                    } // End onMapReady
                });
            }
            @Override
            public void fail() {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == PlacePicker.PLACE_PICKED_RESULT_CODE) {
            Venue place = data.getParcelableExtra(PlacePicker.EXTRA_PLACE);
            mapboxMap.clear();
            mapboxMap.addMarker(new MarkerViewOptions()
                    .position(new LatLng(place.getLocation().getLat(), place.getLocation().getLng()))
                    .title("NAME: "+place.getName()+"\n"+"ADDRESS: "+place.getLocation().getAddress()));
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLocation().getLat(), place.getLocation().getLng()), 15));

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void pickPlace() {
        /*Intent intent = new Intent(this, PlacePickerActivity.class);
        String TAG = PlacePicker.class.getSimpleName();
        intent.putExtra(TAG + ".EXTRA_HEADER_BACKGROUND_RESOURCE", R.color.colorPrimary);
        startActivityForResult(intent, 9001);*/
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}
