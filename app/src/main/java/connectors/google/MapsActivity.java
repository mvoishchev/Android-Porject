package connectors.google;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.location.LocationManager;
import android.provider.Settings;
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
import android.location.Location;
import android.view.View;
import com.google.gson.Gson;

import t4.csc413.smartchef.NavBaseActivity;
import t4.csc413.smartchef.R;


/**
 * This Activity deals with Google Map creation
 * Activity Extends NavBaseActivity
 * NavBaseActivity extends FragmentActivity
 * This allows MapsActivity to work as a Fragment as well
 */
public class MapsActivity extends NavBaseActivity implements OnMapReadyCallback, OnMapClickListener, GoogleMap.OnMyLocationButtonClickListener {

    private GoogleMap mMap;
    private LocationManager locMan;
    boolean barsDisplayed;
    boolean restarantDisplayed;
    // NavBar
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    /**
     * OnCreate design the map and Naw Drawer
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        barsDisplayed=false;
        restarantDisplayed=false;

        // Nav Drawer
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);
    }


    /**
     *
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     * It will ensure that Location Services are enabled
     * And pan the camera to users current location
     * And send volley request to Google Places
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {       //Map creation with default marker in SF
        mMap = googleMap;                               //create an object of googleMap type
        mMap.setMyLocationEnabled(true);                //enables myLocation button
        this.checkLocationSetting();        //check location settings on the device

    //Pan the camera to Users Current Location
        mMap.setOnMapClickListener(this);
        mMap.setOnMyLocationButtonClickListener(this);
        //mMap.startUserSearch(userSearch);
        locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //get last location
        Location lastLoc = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        double lat=lastLoc.getLatitude();   //Get Current Lat
        double lng=lastLoc.getLongitude();  //Get Current Longitude
        LatLng currentMarker = new LatLng(lat,lng);
        CameraPosition userMarkerPosition = new CameraPosition.Builder()    //change camera on the map
                .target(currentMarker)      // Sets the center of the map to to the new created marker
                .zoom(15)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(userMarkerPosition));  //move camera to marker

        updatePlaces(lat, lng,1);   //Send Request to updatePlaces
    }

    /**
     * In this method activity checks if Location Services  are Enabled
     * Based on the Android Manifest Permissions
     * set Two boolean values for network and gps
     * If disabled Dialog Box is displayed to allow the user
     * enable location services in Phone settings
     */
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

    /**
     * onMapClick method changes the CameraPosition to mapClick position
     * @param latLng
     * And send a request to find nearBy Supermarkets
     * Recording in the log about the changes on the map
     */
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
        updatePlaces(lat,lng,1);

        Log.v("MyMapActivity", "location changed");
        Log.i("MAP", "Marker");
    }

    /**
     * The boolean Listener to the default method of the Current location button
     * Checks location setting to ensure they are enabled
     * And pans the camera to current position
     * Sends out the request to find nearBy supermarkets by updatePlaces
     * @return
     */
    @Override
    public boolean onMyLocationButtonClick() {
        mMap.clear();
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

        updatePlaces(lat, lng, 1);

        return true;
    }

    /**
     * Bar Search method
     * Based on the button click
     * Gets current location of user
     * And send updatePlaces to find nearBy Bars
     * based on the GooglePlaces API
     * @param view
     */
    public void barSearch(View view) {

        if (barsDisplayed ){
            mMap.clear();
            barsDisplayed=false;

        }else{

            Location lastLoc = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            double lat = lastLoc.getLatitude();
            double lng = lastLoc.getLongitude();
            updatePlaces(lat, lng, 2);
            LatLng currentMarker = new LatLng(lat, lng);
            CameraPosition userMarkerPosition = new CameraPosition.Builder()    //change camera on the map
                    .target(currentMarker)      // Sets the center of the map to to the new created marker
                    .zoom(15)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(userMarkerPosition));  //move camera to marker
            barsDisplayed=true;
        }
    }

    /**
     * Restauraunt Search method
     * Based on the button click
     * Gets current location of user
     * And send updatePlaces to find nearBy restaurants
     * based on the GooglePlaces API
     * @param view
     */
    public void restaurantSearch(View view) {

        if(restarantDisplayed){
            mMap.clear();
            restarantDisplayed=false;

        }else{

            Location lastLoc = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            double lat = lastLoc.getLatitude();
            double lng = lastLoc.getLongitude();
            updatePlaces(lat, lng, 3);
            LatLng currentMarker = new LatLng(lat, lng);
            CameraPosition userMarkerPosition = new CameraPosition.Builder()    //change camera on the map
                    .target(currentMarker)      // Sets the center of the map to to the new created marker
                    .zoom(15)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(userMarkerPosition));  //move camera to marker
            restarantDisplayed=true;
        }

    }

    /**
     * updatePlaces method to distinguish which Url to use
     * to send a request to Places API
     * pass the coordinates fo the mapPosition
     * and generate a volleyRequest for Places API
     * @param mapLat
     * @param mapLng
     * @param style
     */
    public void updatePlaces(double mapLat,double mapLng, int style) {
        //get location manager
        checkLocationSetting();
        double lat = mapLat;
        double lng = mapLng;

        //build places query string
        String supermarketSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + lat + "," + lng + "&radius=1000&types=grocery_or_supermarket&key=AIzaSyBrpZ3JtyykGKAdjdSdM5Tu9vIM9L-O4co";
        String barSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + lat + "," + lng + "&radius=1000&types=bar|night_club&key=AIzaSyBrpZ3JtyykGKAdjdSdM5Tu9vIM9L-O4co";
        String restaurauntSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + lat + "," + lng + "&radius=1000&types=cafe|restaurant&key=AIzaSyBrpZ3JtyykGKAdjdSdM5Tu9vIM9L-O4co";

        //execute query
        if (style==1) { //supermarket search
            volleyRequest(supermarketSearchStr);
        }   else if (style==2){ //bar search
                volleyRequestForBar(barSearchStr);
            } else if (style==3){   //restaurant search
                    volleyRequestForRestauraunt(restaurauntSearchStr);
                }
    }

    /**
     * Execution of string URL request for supermarkets
     * generate a new volley request
     * Generate onResponse parsing from Json response
     * GooglePlacesData is where the values of the response separated
     * @param placesUrl
     */
    void volleyRequest(String placesUrl) {

        RequestQueue queue = Volley.newRequestQueue(this);

        //Request a string response from the provided URL.
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, placesUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringRequest) {
                        GooglePlacesData ListOfPlaces = new Gson().fromJson(stringRequest, GooglePlacesData.class);
                        for (int i = 0; i < ListOfPlaces.results.size(); i++) { //Placing markers on the map of the objects that were returned
                            Log.i("REQUEST RESPONSE", "The name of the responses: " + ListOfPlaces.results.get(i).name +    //Record log values
                                    "And the Address: " + ListOfPlaces.results.get(i).vicinity);
                            MarkerOptions placeMarker = new MarkerOptions().position(new LatLng(ListOfPlaces.results.get(i).geometry.location.lat,ListOfPlaces.results.get(i).geometry.location.lng))
                                    .title(ListOfPlaces.results.get(i).name)
                                    .snippet(ListOfPlaces.results.get(i).vicinity);
                                    placeMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.shopping));
                                    mMap.addMarker(placeMarker);

                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("REQUEST RESPONSE", "Gracefully failed");
            }
        });
        queue.add(stringRequest);
    }

    /**
     * Execution of string URL request for Bars
     * generate a new volley request
     * Generate onResponse parsing from Json response
     * GooglePlacesData is where the values of the response separated
     * @param placesUrl
     */
    void volleyRequestForBar(String placesUrl) {

        RequestQueue queue = Volley.newRequestQueue(this);

        //Request a string response from the provided URL.
        final StringRequest stringRequestForBars = new StringRequest(Request.Method.GET, placesUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringRequestForBars) {
                        GooglePlacesData ListOfPlaces = new Gson().fromJson(stringRequestForBars, GooglePlacesData.class);
                        mMap.clear();
                        for (int i = 0; i < ListOfPlaces.results.size(); i++) {
                            Log.i("REQUEST RESPONSE", "The name of the responses: " + ListOfPlaces.results.get(i).name +    //Record log values
                                    "And the Address: " + ListOfPlaces.results.get(i).vicinity);
                            MarkerOptions placeMarker = new MarkerOptions().position(new LatLng(ListOfPlaces.results.get(i).geometry.location.lat, ListOfPlaces.results.get(i).geometry.location.lng))
                                    .title(ListOfPlaces.results.get(i).name)
                                    .snippet(ListOfPlaces.results.get(i).vicinity);
                            placeMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.baricon));
                            mMap.addMarker(placeMarker);
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("REQUEST RESPONSE", "Gracefully failed");
            }
        });
        queue.add(stringRequestForBars);
    }

    /**
     * Execution of string URL request for Restaurant
     * generate a new volley request
     * Generate onResponse parsing from Json response
     * GooglePlacesData is where the values of the response separated
     * @param placesUrl
     */
    void volleyRequestForRestauraunt(String placesUrl) {

        RequestQueue queue = Volley.newRequestQueue(this);

        //Request a string response from the provided URL.
        final StringRequest stringRequestForRestauraunt = new StringRequest(Request.Method.GET, placesUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringRequestForRestauraunt) {

                        GooglePlacesData ListOfPlaces = new Gson().fromJson(stringRequestForRestauraunt, GooglePlacesData.class);
                        mMap.clear();
                        for (int i = 0; i < ListOfPlaces.results.size(); i++) {
                            Log.i("REQUEST RESPONSE", "The name of the responses: " + ListOfPlaces.results.get(i).name +    //Record log values
                                    "And the Address: " + ListOfPlaces.results.get(i).vicinity);
                            MarkerOptions placeMarker = new MarkerOptions().position(new LatLng(ListOfPlaces.results.get(i).geometry.location.lat, ListOfPlaces.results.get(i).geometry.location.lng))
                                    .title(ListOfPlaces.results.get(i).name)
                                    .snippet(ListOfPlaces.results.get(i).vicinity);
                            placeMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.restauranticon));
                            mMap.addMarker(placeMarker);
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("REQUEST RESPONSE", "Gracefully failed");
            }
        });
        queue.add(stringRequestForRestauraunt);
    }

}