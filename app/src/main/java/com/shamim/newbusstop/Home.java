package com.shamim.newbusstop;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.navigation.NavigationView;
import com.shamim.newbusstop.Internet_Connection.Network_Change_Listener;
import com.shamim.newbusstop.Inventory_Spinner_NearBy_Place.Data_NearBy_Place;
import com.shamim.newbusstop.Nearby_Places.GetNearby_Place;
import com.shamim.newbusstop.drawer_layout.contact;
import com.shamim.newbusstop.drawer_layout.login;
import com.shamim.newbusstop.drawer_layout.register;
import com.shamim.newbusstop.drawer_layout.setting;

import java.io.File;
import java.util.Arrays;
import java.util.Locale;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    //for map variable
    Network_Change_Listener connection;
    private GoogleMap mMap;
    private String destination, requestService;
    private LatLng destinationLatLng;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Spinner Spinner_Nearby_Place;
    private Near_Place_Search_Adapter nearby_place_search_adapter;
    private Marker CurrentUserLocationMarker;
    private static final int Request_User_Location_code = 99;
    private double latitude, longitude;
    private int proximityRadius = 10000;
    private DrawerLayout drawerLayout;
    String TAG = "Home Activity";
    CardView search;
    Button bt_Find_NearBy_Place;
    View map;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private LinearLayout spinnerLinearLayout_with_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        /*sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
           String usertype= sharedPref.getString("userType","NA");
           String drivertype = sharedPref.getString("userType","NA");
        Toast.makeText(this,usertype, Toast.LENGTH_SHORT).show();
        Toast.makeText(this,drivertype, Toast.LENGTH_SHORT).show();*/

        connection = new Network_Change_Listener();
        search = findViewById(R.id.search);
        Spinner_Nearby_Place=findViewById(R.id.spinner_Nearby_Place);
        spinnerLinearLayout_with_button=findViewById(R.id.spinnerLinearLayout_with_button);
        bt_Find_NearBy_Place=findViewById(R.id.bt_Find_NearBy_Place);
       // view= findViewById(R.id.view);
        map= findViewById(R.id.map);

        nearby_place_search_adapter = new Near_Place_Search_Adapter(Home.this, Data_NearBy_Place.getNearByPlace());
        Spinner_Nearby_Place.setAdapter(nearby_place_search_adapter);

        preferences = getSharedPreferences("BusBD_Info", Context.MODE_PRIVATE);
        editor = preferences.edit();

        Boolean User_isLogged = preferences.getBoolean("User_isLogged", false);
        Boolean Driver_islogged = preferences.getBoolean("Driver_isLogged", false);
        Boolean Admin_islogged = preferences.getBoolean("Admin_isLogged", false);


        //Start--here use SharedPreferences  for user login

        if (User_isLogged) {
            Intent intent = new Intent(Home.this, Customer_MapsActivity.class);
            startActivity(intent);
            finish();
        }
        else if (Driver_islogged)
        {
            Intent intent = new Intent(Home.this, Driver_maps_Activity.class);
            startActivity(intent);
            finish();
        }
        else if (Admin_islogged)
        {
            Intent intent = new Intent(Home.this, Admin_Activity.class);
            startActivity(intent);
            finish();
        }
        // End--  here use SharedPreferences  for user login




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CheckUserLocationPermission();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        //For Used Drawerlayout (ToolBar)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.drawerlayout_view);
        navigationView.setNavigationItemSelectedListener(this);

        Log.d(TAG, "Navigation botton");



        Places.initialize(getApplicationContext(), "mykey", Locale.US);
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        assert autocompleteFragment != null;
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull com.google.android.libraries.places.api.model.Place place)
            {
                destination = place.getName().toString();
                destinationLatLng = place.getLatLng();
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.d(TAG,"Place Search Error:=="+status.getStatusMessage());

            }
        } );


        // Start Near By Search Place
        bt_Find_NearBy_Place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hospital = "hospital", school = "school", restaurant = "restaurant"
                        ,mosque="mosque",atm="atm",gas="gas";
                Object transferData[] = new Object[2];
                GetNearby_Place getNearby_place = new GetNearby_Place();

                mMap.clear();

                int i = Spinner_Nearby_Place.getSelectedItemPosition();

                if (i == 0) {
                    Toast.makeText(Home.this, "Select the Option", Toast.LENGTH_SHORT).show();
                      }
                else if (i==1)
                {
                    String urlmosque=getUrl( latitude,longitude,mosque);
                    transferData[0]=mMap;
                    transferData[1]=urlmosque;
                    getNearby_place.execute(transferData);
                    Toast.makeText(Home.this, "Searching Nearby Mosque...", Toast.LENGTH_SHORT).show();
                }

                else if (i==2)
                {
                    String urlhospital=getUrl( latitude,longitude,hospital);
                    transferData[0]=mMap;
                    transferData[1]=urlhospital;
                    getNearby_place.execute(transferData);
                    Toast.makeText(Home.this, "Searching Nearby Hospital...", Toast.LENGTH_SHORT).show();
                }

                else if (i==3)
                {
                    String urlatm=getUrl( latitude,longitude,atm);
                    transferData[0]=mMap;
                    transferData[1]=urlatm;
                    getNearby_place.execute(transferData);
                    Toast.makeText(Home.this, "Searching Nearby ATM BOOTH...", Toast.LENGTH_SHORT).show();
                }

                else if (i==4)
                {
                    String urlgas=getUrl( latitude,longitude,gas);
                    transferData[0]=mMap;
                    transferData[1]=urlgas;
                    getNearby_place.execute(transferData);
                    Toast.makeText(Home.this, "Searching Nearby Gas Station...", Toast.LENGTH_SHORT).show();
                }

                else if (i==5)
                {
                    String urlrestaurant=getUrl( latitude,longitude,restaurant);
                    transferData[0]=mMap;
                    transferData[1]=urlrestaurant;
                    getNearby_place.execute(transferData);
                    Toast.makeText(Home.this, "Searching Nearby Restaurant...", Toast.LENGTH_SHORT).show();
                }

                else if (i==6)
                {
                    String urlschool=getUrl( latitude,longitude,school);
                    transferData[0]=mMap;
                    transferData[1]=urlschool;
                    getNearby_place.execute(transferData);
                    Toast.makeText(Home.this, "Searching Nearby School...", Toast.LENGTH_SHORT).show();
                }
                else

                {
                    Toast.makeText(Home.this, "Invalid Search", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {
        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleURL.append("location=" + latitude + "," + longitude);
        googleURL.append("&radius=" + proximityRadius);
        googleURL.append("&type=" + nearbyPlace);
        googleURL.append("&sensor=true");
        googleURL.append("&key=" + "AIzaSyC2RdC7u5jC6jqsF9aRFuQFvkyoy9Iox-Y");

        Log.d("GoogleMapsActivity", "url = " + googleURL.toString());

        return googleURL.toString();
    }

    //For Drawerlayout Method
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "Navigation drawer");
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.home_alluser:
                Intent intent = new Intent(Home.this, Home.class);
                startActivity(intent);
                break;

            case R.id.allbus:
                Intent showallbus = new Intent(Home.this, Show_All_Busses_Recycler_View .class);
                startActivity(showallbus);
                break;
            case R.id.setting:
                search.setVisibility(View.GONE);
                map.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_Container,
                        new setting()).commit();
                break;
            case R.id.login:
                search.setVisibility(View.GONE);
                map.setVisibility(View.GONE);
                spinnerLinearLayout_with_button.setVisibility(View.GONE);
               // map_search_Nearbybtn_Hide.setVisibility(View.GONE);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                login loginFragment = new login();
                transaction.replace(R.id.fragment_Container, loginFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                //transaction.remove(loginFragment);




                break;
            case R.id.register:
                search.setVisibility(View.GONE);
                map.setVisibility(View.GONE);
                spinnerLinearLayout_with_button.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_Container,
                        new register()).commit();


                break;

            case R.id.contact:
                search.setVisibility(View.GONE);
                map.setVisibility(View.GONE);
                spinnerLinearLayout_with_button.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_Container,
                        new contact()).commit();

                break;
            case R.id.exit:
                finish();
                break;

            case R.id.share:
                Intent sharingInent = new Intent(Intent.ACTION_SEND);
                sharingInent.setType("text/plain");
                String shareBody="https://mdshamimislam487.blogspot.com/";
                String shareSubject="Your Subject here";

                sharingInent.putExtra(Intent.EXTRA_TEXT,shareBody);
                sharingInent.putExtra(Intent.EXTRA_SUBJECT,shareSubject);
                startActivity(Intent.createChooser(sharingInent,"Share Using"));
                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //End  Drawerlayout Method

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(locationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        lastLocation = location;
        if (CurrentUserLocationMarker != null) {
            CurrentUserLocationMarker.remove();
        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("User Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        CurrentUserLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(12));
        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();

            mMap.setMyLocationEnabled(true);
        }
    }

    public Boolean CheckUserLocationPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        Request_User_Location_code);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_code);
            }
            return false;

        } else {
            return true;
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Request_User_Location_code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "Permission Denied.....", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    //For create new client
    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    /*public void search_image_Botton(View view) {
        String hospital = "hospital", school = "school", restaurant = "restaurant";
        Object transferData[] = new Object[2];
        GetNearby_Place getNearbyPlaces = new GetNearby_Place();


        switch (view.getId()) {
            case R.id.search_image_Botton:
                EditText addressField = (EditText) findViewById(R.id.Location_search);
                String address = addressField.getText().toString();

                List<Address> addressList = null;
                MarkerOptions userMarkerOptions = new MarkerOptions();

                if (!TextUtils.isEmpty(address)) {
                    Geocoder geocoder = new Geocoder(this);

                    try {
                        addressList = geocoder.getFromLocationName(address, 1);

                        if (addressList != null) {
                            for (int i = 0; i < addressList.size(); i++) {
                                Address userAddress = addressList.get(i);
                                LatLng latLng = new LatLng(userAddress.getLatitude(), userAddress.getLongitude());

                                userMarkerOptions.position(latLng);
                                userMarkerOptions.title(address);
                                userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                                mMap.addMarker(userMarkerOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                            }
                        } else {
                            Toast.makeText(this, "Location not found...", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "please write any location name...", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }**/


    @Override
    protected void onStart() {

        IntentFilter filter= new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connection,filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(connection);
        super.onStop();
    }

    @Override
    protected void onResume() {

        search.setVisibility(View.VISIBLE);
        map.setVisibility(View.VISIBLE);
        super.onResume();
    }



    }




