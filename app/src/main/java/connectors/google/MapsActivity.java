package connectors.google;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.Marker;
import android.location.Location;
import android.widget.EditText;

import com.google.gson.Gson;


import t4.csc413.smartchef.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnMapClickListener, GoogleMap.OnMyLocationButtonClickListener {

    private GoogleMap mMap;
    private EditText userSearch;
    //instance variables for Marker icon drawable resources
    private int userIcon, foodIcon, drinkIcon, shopIcon, otherIcon;
    //location manager
    private LocationManager locMan;

    //user marker
    private Marker userMarker;

    //places of interest
    private Marker[] placeMarkers;
    //max
    private final int MAX_PLACES = 20;//most returned from google
    //marker options
    private MarkerOptions[] places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //updatePlaces();
        //userSearch=(EditText) findViewById(R.id.userSearch);
        //userSearch = (EditText) userSearch.getText();

    }


    /**'
     *
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {       //Map creation with default marker in SF
        mMap = googleMap;                               //create an object of googleMap type
        mMap.setMyLocationEnabled(true);                //enables myLocation button
        this.checkLocationSetting();        //check location settings on the device

        // Add a marker in San Francisco and move the camera
        LatLng sanFrancisco = new LatLng(37.7833, -122.4167);   //SF coordinates
        mMap.addMarker(new MarkerOptions().position(sanFrancisco).title("Marker in San Francisco"));    //new marker creation
        CameraPosition cameraPosition = new CameraPosition.Builder()    //change camera on the map
                .target(sanFrancisco)      // Sets the center of the map to San Francisco
                .zoom(12)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));  //move camera to marker

        mMap.setOnMapClickListener(this);
        mMap.setOnMyLocationButtonClickListener(this);
        //mMap.startUserSearch(userSearch);
    }

    public void checkLocationSetting() {    //method to check location settings and enable them if necessary
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE); //// Get Location Manager
        boolean gps_enabled = false;    //and check for GPS & Network location services
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);   //call to see status of GPS
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);   //call to see status of Network Provider
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) { //if both are disabled call to intent to go to location settings

            AlertDialog.Builder builder = new AlertDialog.Builder(this);    //dialog box to alert the user to current status of location settings
            builder.setTitle("Location Services Not Active");               //
            builder.setMessage("Please enable Location Services and GPS");  //*

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing
                    dialog.dismiss();
                }
            });

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { //listener to check if user click OK on the message box
                public void onClick(DialogInterface dialogInterface, int i) {       //onClick method that directs user to location setting
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });

            Dialog alertDialog = builder.create();          //Dialog box creation
            alertDialog.setCanceledOnTouchOutside(true);   //box does disappear
            alertDialog.show();                             //*
        }
    }


    @Override
    public void onMapClick(LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title("Your specified marker"));
        CameraPosition userMarkerPosition = new CameraPosition.Builder()    //change camera on the map
                .target(latLng)      // Sets the center of the map to to the new created marker
                .zoom(15)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(userMarkerPosition));  //move camera to marker
        double lat= latLng.latitude;
        double lng=latLng.longitude;
        updatePlaces(lat,lng);

        Log.v("MyMapActivity", "location changed");
        Log.i("MAP", "Marker");
    }


    @Override
    public boolean onMyLocationButtonClick() {
        checkLocationSetting();
        locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //get last location
        Location lastLoc = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        double lat=lastLoc.getLatitude();
        double lng=lastLoc.getLongitude();
        LatLng currentMarker = new LatLng(lat,lng);
        CameraPosition userMarkerPosition = new CameraPosition.Builder()    //change camera on the map
                .target(currentMarker)      // Sets the center of the map to to the new created marker
                .zoom(15)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(userMarkerPosition));  //move camera to marker

        updatePlaces(lat,lng);
        return true;
    }


    public void updatePlaces(double mapLat,double mapLng) {
        //get location manager
        checkLocationSetting();
        //locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //get last location
        //Location lastLoc = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        double lat = mapLat;
        double lng = mapLng;
        //create LatLng
        //build places query string
        String supermarketSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + lat + "," + lng + "&radius=1000&types=grocery_or_supermarket&key=AIzaSyBrpZ3JtyykGKAdjdSdM5Tu9vIM9L-O4co";
        //execute query

        volleyRequest(supermarketSearchStr);
    }


    void volleyRequest(String placesUrl) {

        RequestQueue queue = Volley.newRequestQueue(this);

        //Request a string response from the provided URL.
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, placesUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringRequest) {
                        // Display the first 500 characters of the response string.
                        //Log.i("REQUEST RESPOSNE", "THE RESPONSE IS: " + stringRequest);
                        GooglePlacesData ListOfPlaces = new Gson().fromJson(stringRequest, GooglePlacesData.class);
                        for (int i = 0; i < ListOfPlaces.results.size(); i++) {
                            //Log.i("REQUEST RESPOSNE", "First lat and lng: " + ListOfPlaces.results.get(i).geometry.location.lat +
                            //        " , " + ListOfPlaces.results.get(i).geometry.location.lng);
                            Log.i("REQUEST RESPOSNE", "The name of the responses: " + ListOfPlaces.results.get(i).name +
                                    "And the Andress: " + ListOfPlaces.results.get(i).vicinity);
                            MarkerOptions placeMarker = new MarkerOptions().position(new LatLng(ListOfPlaces.results.get(i).geometry.location.lat,ListOfPlaces.results.get(i).geometry.location.lng))
                                    .title(ListOfPlaces.results.get(i).name);
                            placeMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            mMap.addMarker(placeMarker);
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("REQUEST RESPOSNE", "Gracefully failed");
            }
        });
        queue.add(stringRequest);
    }

}