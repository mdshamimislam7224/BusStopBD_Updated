package com.shamim.newbusstop;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shamim.newbusstop.Internet_Connection.Network_Change_Listener;
import com.shamim.newbusstop.drawer_layout.Contact_customer;
import com.shamim.newbusstop.drawer_layout.all_bus;
import com.shamim.newbusstop.drawer_layout.customer_profile;
import com.shamim.newbusstop.drawer_layout.traveling_history;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Customer_MapsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private GoogleMap mMap;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    private FusedLocationProviderClient mFusedLocationClient;

    private Button mLogout, mRequest, mSettings, mHistory;
    private DrawerLayout drawer_layout_customer;
    private Marker current_passenger_Marker;


    private LatLng pickupLocation;

    private Boolean requestBol = false;
    private Boolean request_Gps;

    private Marker pickupMarker;

    private SupportMapFragment mapFragment;
    private String customerId = "", fullname, email, phone, profileImage;

    private String destination, requestService;

    private LatLng destinationLatLng;

    private LinearLayout mDriverInfo;

    private ImageView mDriverProfileImage;
    DatabaseReference profileRef_layoutheader;

    private TextView mDriverName, mDriverPhone, mDriverBus;

    private RadioGroup mRadioGroup;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private RatingBar mRatingBar;
    private GoogleApiClient mGoogleApiClient;
    private String TAG = "Customer_Map_Activity";
    Network_Change_Listener connection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer__maps_activity);
        connection = new Network_Change_Listener();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Customer_MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Location_Request_code);
        } else {
            mapFragment.getMapAsync(this);
        }

        CheckGps();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        destinationLatLng = new LatLng(0.0, 0.0);
        mDriverInfo = (LinearLayout) findViewById(R.id.driverInfo);
        mDriverProfileImage = (ImageView) findViewById(R.id.driverProfileImage);
        mDriverName = (TextView) findViewById(R.id.driverName);
        mDriverPhone = (TextView) findViewById(R.id.driverPhone);
        mDriverBus = (TextView) findViewById(R.id.driverBus);
        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mRequest = (Button) findViewById(R.id.mRequest);

        Toolbar toolbar = findViewById(R.id.customer_toolbar);
        setSupportActionBar(toolbar);
        drawer_layout_customer = findViewById(R.id.drawer_layout_customer);

        mAuth = FirebaseAuth.getInstance();
        customerId = mAuth.getCurrentUser().getUid();
        profileRef_layoutheader = FirebaseDatabase.getInstance().getReference("Bus Stop BD").child("Registration").child("Customer").child(customerId);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer_layout_customer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout_customer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.drawerlayout_view_customer);
        navigationView.setNavigationItemSelectedListener(this);


        Log.d(TAG, "Navigation botton");


        mRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (requestBol) {
                    endRide();

                } else {
                    int selectId = mRadioGroup.getCheckedRadioButtonId();

                    final RadioButton radioButton = (RadioButton) findViewById(selectId);

                    if (radioButton.getText() == null) {
                        Toast.makeText(Customer_MapsActivity.this, "Please Select The Service", Toast.LENGTH_LONG).show();
                    }

                    requestService = radioButton.getText().toString();

                    requestBol = true;

                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Bus Stop BD").child("CustomerRequest");

                    GeoFire geoFire = new GeoFire(ref);
                    geoFire.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()));

                    pickupLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    pickupMarker = mMap.addMarker(new MarkerOptions().position(pickupLocation).title("Pickup Here").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pickup)));
                    mRequest.setText("Getting your Driver....");

                    getClosestDriver();

                }
            }

        });


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
            public void onPlaceSelected(@NonNull com.google.android.libraries.places.api.model.Place place) {
                destination = place.getName().toString();
                destinationLatLng = place.getLatLng();
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.d(TAG, "Place Search Error:==");

            }
        });


        updateNavHeader();


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "Navigation drawer");
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.home_customer:
                Intent intent = new Intent(Customer_MapsActivity.this, Customer_MapsActivity.class);
                startActivity(intent);
                break;

            case R.id.allbus_customer:
                getSupportFragmentManager().beginTransaction().replace(R.id.test33,
                        new all_bus()).commit();
                break;
            case R.id.setting_customer:
                Intent otpsend = new Intent(Customer_MapsActivity.this, Otp_Send_Activity.class);
                startActivity(otpsend);
                break;
            case R.id.login:

                break;
            case R.id.customer_travel_history:
                getSupportFragmentManager().beginTransaction().replace(R.id.test33,
                        new traveling_history()).commit();

                break;

            case R.id.contact_customer_with_Bus_owner:
                getSupportFragmentManager().beginTransaction().replace(R.id.test33,
                        new Contact_customer()).commit();
                break;
            case R.id.exit:
                finish();
                break;

            case R.id.share:
                Toast.makeText(this, "Share this app", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile_customer:

                getSupportFragmentManager().beginTransaction().replace(R.id.test33,
                        new customer_profile()).commit();
                Toast.makeText(this, "Share this app", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout_customer:

                SharedPreferences sharedPreferences = getSharedPreferences("BusBD_Info", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putBoolean("User_isLogged", false);
                edit.commit();
                Intent intentlogout = new Intent(Customer_MapsActivity.this, Home.class);
                intentlogout.putExtra("login", "customer");
                startActivity(intentlogout);
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                finish();

                break;

        }

        drawer_layout_customer.closeDrawer(GravityCompat.START);
        return true;


    }

    public void updateNavHeader() {

        final NavigationView navigationView = (NavigationView) findViewById(R.id.drawerlayout_view_customer);
        View headerView = navigationView.getHeaderView(0);
        final TextView navUsername = headerView.findViewById(R.id.drawer_layout_name_customer);
        final TextView navUserphone = headerView.findViewById(R.id.drawer_layout_number_customer);
        final TextView navUserMail = headerView.findViewById(R.id.drawer_layout_email_customer);
        final ImageView navUserPhot = headerView.findViewById(R.id.drawer_layout_image_customer);

        profileRef_layoutheader.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("fullname") != null) {
                        fullname = map.get("fullname").toString();
                        navUsername.setText(fullname);
                    }
                    if (map.get("email") != null) {
                        email = map.get("email").toString();
                        navUserMail.setText(email);
                    }
                    if (map.get("phone") != null) {
                        phone = map.get("phone").toString();
                        navUserphone.setText(map.get("phone").toString());
                    }
                    if (map.get("profileImageurl") != null) {
                        profileImage = map.get("profileImageurl").toString();
                        Glide.with(getApplication()).load(profileImage).into(navUserPhot);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    private int radius = 1;
    private Boolean driverFound = false;
    private String driverFoundID;
    GeoQuery geoQuery;

    private void getClosestDriver() {
        Log.d(TAG, " getClosestDriver() ");
        DatabaseReference driverLocation = FirebaseDatabase.getInstance().getReference("Bus Stop BD").child("Available").child("DriverAvailable");
        Log.d(TAG, " driverLocation==" + driverLocation);
        GeoFire geoFire = new GeoFire(driverLocation);

        //bydefult
        //geoQuery = geoFire.queryAtLocation(new GeoLocation(23.707310, 90.415480), radius);
        geoQuery = geoFire.queryAtLocation(new GeoLocation(pickupLocation.latitude, pickupLocation.longitude), radius);

        Log.d(TAG, " geoFire==" + geoFire);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                Log.d(TAG, "Key=" + key + "  Location=" + location.latitude);

                /**
                 search the details of that bus
                 BusDetails Database ref:
                 Find Bus details using the bus key
                 check if the bus is full or not
                 if not full get driver details
                 Send driver notifaction
                 */


                if (!driverFound && requestBol) {
                    DatabaseReference mCustomerDatabase = FirebaseDatabase.getInstance().getReference("Bus Stop BD").child("Registration").child("Driver").child(key);
                    mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                                Map<String, Object> driverMap = (Map<String, Object>) snapshot.getValue();

                                if (driverFound) {
                                    return;
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d(TAG, "onCancelled");
                        }
                    });
                    Log.d(TAG, "DriverFound==" + driverFound);
                    driverFound = true;
                    driverFoundID = key;
                }

                DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference("Bus Stop BD").child("Registration").child("Driver").child(driverFoundID).child("CustomerRequest");
                String customerID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                HashMap map = new HashMap();
                map.put("customerRideId", customerID);
                map.put("destination", destination);
                map.put("destinationLat", destinationLatLng.latitude);
                map.put("destinationLng", destinationLatLng.longitude);
                driverRef.updateChildren(map);

                getDriverLocation();
                getDriverInfo();
                getHasRideEnded();
                mRequest.setText("Looking for Driver Location....");

            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if (!driverFound) {

                    Log.d(TAG, "Driver not found + radius==" + driverFound + "Radius==" + radius);
                    radius++;
                    getClosestDriver();
                }

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    private Marker mDriverMarker;
    private DatabaseReference driverLocationRef;
    private ValueEventListener driverLocationRefListener;


    private void getDriverLocation() {
        driverLocationRef = FirebaseDatabase.getInstance().getReference("Bus Stop BD").child("DriversWorking").child(driverFoundID).child("l");

        Log.d(TAG, "driverLocationRef  of getDriverLocation()" + driverLocationRef);
        driverLocationRefListener = driverLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && requestBol) {

                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    double locationLat = 0;
                    double locationLng = 0;
                    mRequest.setText("Driver Found: ");
                    if (map.get(0) != null) {
                        locationLat = Double.parseDouble(map.get(0).toString());
                    }
                    if (map.get(1) != null) {
                        locationLng = Double.parseDouble(map.get(1).toString());
                    }
                    LatLng driverLatLng = new LatLng(locationLat, locationLng);
                    if (mDriverMarker != null) {
                        mDriverMarker.remove();
                    }


                    Location loc1 = new Location("");
                    loc1.setLatitude(pickupLocation.latitude);
                    loc1.setLongitude(pickupLocation.longitude);

                    Location loc2 = new Location("");
                    loc2.setLatitude(driverLatLng.latitude);
                    loc2.setLongitude(driverLatLng.longitude);

                    float distance = loc1.distanceTo(loc2);

                    if (distance < 300) {

                        mRequest.setText("Cencel Request");

                        /*mRequest.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                endRide();
                                return;
                            }
                        });*/
                    }
                    else {
                        mRequest.setText("Driver Found: " + String.valueOf(distance));
                        mDriverMarker = mMap.addMarker(new MarkerOptions().position(driverLatLng).title("your driver").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_bus)));
                        ;

                    }

                    mDriverMarker = mMap.addMarker(new MarkerOptions().position(driverLatLng).title("your driver").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_bus)));
                    ;

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private DatabaseReference driveHasEndedRef;
    private ValueEventListener driveHasEndedRefListener;

    private void getHasRideEnded() {
        driveHasEndedRef = FirebaseDatabase.getInstance().getReference("Bus Stop BD").child("Registration").child("Driver").child(driverFoundID).child("CustomerRequest").child("customerRideId");
        driveHasEndedRefListener = driveHasEndedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                } else {
                    endRide();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void endRide() {
        requestBol = false;

        if (driverFoundID != null) {
            DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference("Bus Stop BD").child("Registration").child("Driver").child(driverFoundID).child("CustomerRequest");
            driverRef.removeValue();
            geoQuery.removeAllListeners();
            driverLocationRef.removeEventListener(driverLocationRefListener);
            driveHasEndedRef.removeEventListener(driveHasEndedRefListener);
            driverFoundID = null;

        }
        /*else {
            Toast.makeText(Customer_MapsActivity.this, "Driver is not Available Online", Toast.LENGTH_SHORT).show();
            mRequest.setText("Request for The Bus");
            return;
        }*/

        driverFound = false;
        radius = 1;
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Bus Stop BD").child("CustomerRequest");

        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(userId);

        if (pickupMarker != null) {
            pickupMarker.remove();
        }
        if (mDriverMarker != null) {
            mDriverMarker.remove();
        }
        mRequest.setText("Request for The Bus");
        mDriverInfo.setVisibility(View.GONE);
        mDriverName.setText("");
        mDriverPhone.setText("");
        mDriverBus.setText("Destination: --");
        mDriverProfileImage.setImageResource(R.mipmap.ic_default_user);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Customer_MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, Location_Request_code);
        }
        buildGoogleApiClient();

        mMap.setMyLocationEnabled(true);

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                CheckGps();
                return true;
            }
        });
    }

    private void CheckGps()
    {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(3000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest)
                .setAlwaysShow(true);

        Task<LocationSettingsResponse> locationSettingsResponseTask = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        locationSettingsResponseTask.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(Customer_MapsActivity.this, "Gps is already On", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {
                    if (e.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                        try {
                            resolvableApiException.startResolutionForResult(Customer_MapsActivity.this, 101);
                        } catch (IntentSender.SendIntentException sendIntentException) {
                            sendIntentException.printStackTrace();
                        }
                    }

                    if (e.getStatusCode() == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE) {
                        Toast.makeText(Customer_MapsActivity.this, "Setting not available", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        mLastLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());


        if (current_passenger_Marker != null) {
            current_passenger_Marker.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions();

        current_passenger_Marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Your Current Location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pickup)));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //what is the work this
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(Customer_MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, Location_Request_code);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    final int Location_Request_code = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Location_Request_code: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    mapFragment.getMapAsync(this);

                } else {
                    Toast.makeText(getApplicationContext(), "Please provide the permission", Toast.LENGTH_LONG).show();
                }
                break;


            }


        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(Customer_MapsActivity.this, "Now Gps is On", Toast.LENGTH_SHORT).show();


            }
            if (resultCode==RESULT_CANCELED)
            {
                Toast.makeText(Customer_MapsActivity.this, "Denied Gps On", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }




    private void getDriverInfo() {
        mDriverInfo.setVisibility(View.VISIBLE);
        DatabaseReference mCustomerDatabase = FirebaseDatabase.getInstance().getReference("Bus Stop BD").child("Registration").child("Driver").child(driverFoundID);
        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("fullname") != null) {
                        mDriverName.setText(map.get("fullname").toString());
                    }
                    if (map.get("phone") != null) {
                        mDriverPhone.setText(map.get("phone").toString());
                    }
                    if (map.get("company") != null) {
                        mDriverBus.setText(map.get("company").toString());
                    }
                    if (map.get("profileImageurl") != null) {
                        Glide.with(getApplication()).load(map.get("profileImageurl").toString()).into(mDriverProfileImage);
                    }
                    int ratingSum = 0;
                    float ratingsTotal = 0;
                    float ratingsAvg = 0;
                    for (DataSnapshot child : dataSnapshot.child("rating").getChildren()) {
                        ratingSum = ratingSum + Integer.valueOf(child.getValue().toString());
                        ratingsTotal++;
                    }
                    if (ratingsTotal != 0) {
                        ratingsAvg = ratingSum / ratingsTotal;
                        mRatingBar.setRating(ratingsAvg);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    @Override
    protected void onStart() {
        IntentFilter filter= new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connection,filter);
        super.onStart();
    }
    @Override
    protected void onResume() {
        super.onResume();
        CheckGps();
    }



    @Override
    protected void onStop() {
        unregisterReceiver(connection);
        super.onStop();
    }


}