package com.shamim.newbusstop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shamim.newbusstop.Internet_Connection.Network_Change_Listener;
import com.shamim.newbusstop.drawer_layout.Add_Bus_Stoppage_Of_Bus_Admin;
import com.shamim.newbusstop.drawer_layout.Add_Information_Of_Bus_Admin;
import com.shamim.newbusstop.drawer_layout.Contact_customer;
import com.shamim.newbusstop.drawer_layout.all_bus;
import com.shamim.newbusstop.drawer_layout.customer_profile;
import com.shamim.newbusstop.drawer_layout.traveling_history;

import java.io.IOException;
import java.util.Map;

public class Admin_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private SupportMapFragment mapFragment;
    private DrawerLayout drawer_layout_admin;
    Network_Change_Listener connection;

   private TextView add_bus_name_admin_text;
    private Uri filepath;
    private StorageReference buses_Image_From_Admin;
    DatabaseReference addBus_info_for_Admin;
    DatabaseReference  bus_Name_and_Image_DRef_for_Retrive;
    FirebaseAuth mAuth;
    String  customerId="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);


        Toolbar toolbar = findViewById(R.id.admin_toolbar);
        setSupportActionBar(toolbar);


        drawer_layout_admin = findViewById(R.id.drawer_layout_admin);
        add_bus_name_admin_text = findViewById(R.id.add_bus_name_admin_txt);



        //For Image Location
        buses_Image_From_Admin  = FirebaseStorage.getInstance().getReference("Buses Image From Admin");


        mAuth = FirebaseAuth.getInstance();
        customerId =mAuth.getCurrentUser().getUid();
     bus_Name_and_Image_DRef_for_Retrive = FirebaseDatabase.getInstance().getReference("Bus Stop BD").child("Bus Information From Admin").child("Bus Image And Name").child(customerId);



        Retrive_bus_Name_and_Image();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer_layout_admin, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout_admin.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.drawerlayout_view_admin);
        navigationView.setNavigationItemSelectedListener(this);

    }



    @Override
    public void onBackPressed() {
        if (drawer_layout_admin.isDrawerOpen(GravityCompat.START))
        {
            drawer_layout_admin.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.addbus_admin:
                getSupportFragmentManager().beginTransaction().replace(R.id.admin_layout,
                        new Add_Information_Of_Bus_Admin()).commit();
                break;


            case R.id.addbus_stoppage_admin:
                getSupportFragmentManager().beginTransaction().replace(R.id.admin_layout,
                        new Add_Bus_Stoppage_Of_Bus_Admin()).commit();
                break;

            case R.id.allbus_admin:
                Intent showallbus = new Intent(Admin_Activity.this, Show_All_Busses_Recycler_View .class);
                startActivity(showallbus);
                /*getSupportFragmentManager().beginTransaction().replace(R.id.test3,
                        new all_bus()).commit();*/
                break;
            case R.id.setting_customer:
                Intent otpsend = new Intent(Admin_Activity.this, Otp_Send_Activity.class);
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
            case R.id.logout_admin:

                SharedPreferences sharedPreferences = getSharedPreferences("BusBD_Info", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putBoolean("Admin_isLogged",false);
                edit.commit();
                Intent intentlogout= new Intent(Admin_Activity.this,Home.class);
                intentlogout.putExtra("login", "admin");
                startActivity(intentlogout);
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                finish();
                break;

        }

        drawer_layout_admin.closeDrawer(GravityCompat.START);
        return true;


    }


    private void Retrive_bus_Name_and_Image() {



        bus_Name_and_Image_DRef_for_Retrive.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){

                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("buses_name_from_admin")!=null){
                        add_bus_name_admin_text.setVisibility(View.VISIBLE);
                        add_bus_name_admin_text.setText(map.get("buses_name_from_admin").toString());
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

    }


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
}