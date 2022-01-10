package com.shamim.newbusstop;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shamim.newbusstop.Internet_Connection.Network_Change_Listener;

import java.io.IOException;

public class Registration extends AppCompatActivity implements View.OnClickListener {
    TextView spinerID_text;
    EditText fullname, email, phone, password, password1, driving_License, trade_license_of_bus_Company, nid,road_permit_license;
    ImageView profileImage;
    Spinner spinnerId_Bus_name;
    String TAG = "Driver Activity";
    Button cancel, driver_singup, back,user_signup,admin_signup,subadmin_singup,choose_the_image;
    private Uri filepath;
    private FirebaseUser firebaseUser;
    Network_Change_Listener connection;

    FirebaseAuth firebaseAuth;
    DatabaseReference driver_Database_Ref,
            custome_Database_Ref, admin_Database_Ref,
            subadmin_Database_Ref;
    StorageReference driver_Image_Database_Ref, Customer_Image_Database_Ref, Admin_Image_Database_Ref, Subadmin_Image_Database_Ref;
    String usertype="";
    SharedPreferences sharedPreferences;
    //spinner
    String [] busName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        connection = new Network_Change_Listener();

        sharedPreferences =getSharedPreferences("For_Login_SharedPreferences_file", Context.MODE_PRIVATE);

        Intent intent = getIntent();
        usertype=intent.getStringExtra("user_type");

        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        password1 = findViewById(R.id.password1);
        driving_License = findViewById(R.id.driving_License);
        nid = findViewById(R.id.nid);
        spinnerId_Bus_name = findViewById(R.id.spinnerId_Bus_name);
        spinerID_text= findViewById(R.id.spinerID_text);
        trade_license_of_bus_Company =findViewById(R.id. trade_license_of_bus_Company);
        cancel = findViewById(R.id.cancel);
        back = findViewById(R.id.back);
        road_permit_license=findViewById(R.id.road_permit_license);
        driver_singup = findViewById(R.id. driver_singup);
        user_signup = findViewById(R.id. user_signup);
        admin_signup = findViewById(R.id. admin_signup);
        profileImage = findViewById(R.id.profile_image);
        subadmin_singup = findViewById(R.id.subadmin_singup);
        choose_the_image= findViewById(R.id.choose_the_image);

        busName= getResources().getStringArray(R.array.Bus_Name);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,busName);
        spinnerId_Bus_name.setAdapter(adapter);


        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();


        if (usertype.equals("user") )
        {
            driving_License.setVisibility(View.GONE);
            admin_signup.setVisibility(View.GONE);
            driver_singup.setVisibility(View.GONE);
            nid.setVisibility(View.GONE);
            trade_license_of_bus_Company.setVisibility(View.GONE);
            spinnerId_Bus_name.setVisibility(View.GONE);
            road_permit_license.setVisibility(View.GONE);
            spinerID_text.setVisibility(View.GONE);



        }
        else if(usertype.equals("driver"))
        {
            admin_signup.setVisibility(View.GONE);
            user_signup.setVisibility(View.GONE);
            subadmin_singup.setVisibility(View.GONE);
            trade_license_of_bus_Company.setVisibility(View.GONE);
            road_permit_license.setVisibility(View.GONE);
            spinerID_text.setVisibility(View.VISIBLE);
        }
        else if(usertype.equals("admin"))
        {
            subadmin_singup.setVisibility(View.GONE);
            user_signup.setVisibility(View.GONE);
            driver_singup.setVisibility(View.GONE);
            driving_License.setVisibility(View.GONE);
            trade_license_of_bus_Company.setVisibility(View.VISIBLE);
            road_permit_license.setVisibility(View.VISIBLE);
            spinerID_text.setVisibility(View.VISIBLE);


        }
        else if(usertype.equals("subadmin"))
        {
            admin_signup.setVisibility(View.GONE);
            user_signup.setVisibility(View.GONE);
            driver_singup.setVisibility(View.GONE);
            driving_License.setVisibility(View.GONE);
            trade_license_of_bus_Company.setVisibility(View.GONE);
            spinerID_text.setVisibility(View.VISIBLE);

        }

        firebaseAuth = FirebaseAuth.getInstance();

        //register
        driver_Database_Ref = FirebaseDatabase.getInstance().getReference("Bus Stop BD").child("Registration").child("Driver");
        custome_Database_Ref = FirebaseDatabase.getInstance().getReference("Bus Stop BD").child("Registration").child("Customer");
        admin_Database_Ref = FirebaseDatabase.getInstance().getReference("Bus Stop BD").child("Registration").child("Admin");
        subadmin_Database_Ref = FirebaseDatabase.getInstance().getReference("Bus Stop BD").child("Registration").child("SubAdmin");

        //image
        driver_Image_Database_Ref = FirebaseStorage.getInstance().getReference("Profile_Image").child("Driver").child(firebaseAuth.getUid());
        Customer_Image_Database_Ref = FirebaseStorage.getInstance().getReference("Profile_Image").child("Customer").child(firebaseAuth.getUid());
        Admin_Image_Database_Ref = FirebaseStorage.getInstance().getReference("Profile_Image").child("Admin").child(firebaseAuth.getUid());
        Subadmin_Image_Database_Ref = FirebaseStorage.getInstance().getReference("Profile_Image").child("SUB Admin").child(firebaseAuth.getUid());

        cancel.setOnClickListener(this);
        driver_singup.setOnClickListener(this);
        profileImage.setOnClickListener(this);
        user_signup.setOnClickListener(this);
        admin_signup.setOnClickListener(this);
        subadmin_singup.setOnClickListener(this);
        choose_the_image.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.cancel:

               final Intent cancel = new Intent(this, Home.class);
                startActivity(cancel);

                break;

            case R.id.driver_singup:


                final String Fullname = fullname.getText().toString();
                final String Email = email.getText().toString();
                String emailmatch = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                final String Phone = phone.getText().toString();
                final String Password = password.getText().toString();
                String Password1 = password1.getText().toString();
                final String License = driving_License.getText().toString();
                final String NID = nid.getText().toString();
               final String Bus_name = spinnerId_Bus_name.getSelectedItem().toString();

                if (profileImage == null) {
                    Toast.makeText(Registration.this, "Please Insert Your Image", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Image");
                }

                else if (Fullname.isEmpty()) {
                    Toast.makeText(Registration.this, "Please Enter your Fullname", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Full Name is Empty" + " " + Fullname);
                } else if (Email.isEmpty()) {
                    Toast.makeText(Registration.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Eamil Null" + " " + Email);
                }
                else if (!Email.matches(emailmatch))
                {
                    Toast.makeText(Registration.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }

                else if (Phone.isEmpty()) {
                    Toast.makeText(Registration.this, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Phone" + " " + Phone);
                } else if (Phone.charAt(0) != '+' || Phone.charAt(1) != '8' || Phone.charAt(2) != '8' || Phone.charAt(3) != '0') {

                    Toast.makeText(Registration.this, "Must Be First 4 Digit=+880", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "First Position Number is" + " " + Phone);
                } else if (Phone.charAt(4) != '1') {
                    Toast.makeText(Registration.this, "5th Position Digit is = 1 ", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "5th Position Number is" + " " + Phone);
                } else if (Phone.charAt(5) != '9' && Phone.charAt(5) != '8' &&
                        Phone.charAt(5) != '7' && Phone.charAt(5) != '3' &&
                        Phone.charAt(5) != '4' && Phone.charAt(5) != '5'
                        && Phone.charAt(5) != '6') {
                    Toast.makeText(Registration.this, "6th Position Digit is = 3 or 4 or 5 or 6 or 7 or 8 or 9 ", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "6th Position Number is" + " " + Phone);

                } else if (Phone.length() != 14) {
                    Toast.makeText(Registration.this, "Number Must be 14 length with +880", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Phone Number Not =14" + " " + Phone);
                } else if (Password.isEmpty()) {
                    Toast.makeText(Registration.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Password" + " " + Password);
                } else if (Password1.isEmpty()) {
                    Toast.makeText(Registration.this, "Please Enter Your Conform Password", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Password1" + " " + Password1);
                } else if (!Password1.equals(Password)) {
                    Toast.makeText(Registration.this, "Please Enter Same Password", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Password  Password" + " " + Password + " " + Password1);
                } else if (Password.length() < 7) {
                    Toast.makeText(Registration.this, "Password Must be 6 grater than", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Password Length Not Grater then 7" + " " + Password);
                } else if (License.isEmpty()) {
                    Toast.makeText(Registration.this, "Please Enter Your License", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "License" + " " + License);
                } else if (NID.isEmpty()) {
                    Toast.makeText(Registration.this, "Please Enter Your NID Number", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "NID" + " " + NID);
                } else if (NID.length() != 10) {
                    Toast.makeText(Registration.this, "NID Card Number Must be 10", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "NID Card Number Must be 10" + " " + NID);
                } else if (Bus_name.equals("Select The Option")) {
                    Toast.makeText(Registration.this, "Please Choose Your Bus Name", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Bus_name" + " " + spinnerId_Bus_name);
                }
                else {
                    final Loading loading = new Loading(Registration.this);

                    firebaseAuth.createUserWithEmailAndPassword(Email, Password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull final Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        UploadDriverInformaion();

                                        loading.startLoading();


                                        Intent driver = new Intent(Registration.this, Login_activity.class);
                                        driver.putExtra("login", "driver");
                                        startActivity(driver);

                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                loading.dismissDialog();
                                            }
                                        },5000);


                                        Toast.makeText(Registration.this, "Registration is Complete ", Toast.LENGTH_SHORT).show();


                                    }

                                    else {
                                        Toast.makeText(Registration.this, "Already This Email has Registered ", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });
                }



                break;

            case R.id.choose_the_image:
                choose_image();

                break;

            case R.id.user_signup:
                final String User_Fullname = fullname.getText().toString();
                final String User_Email = email.getText().toString();
                String User_emailmatch = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                final String User_Phone = phone.getText().toString();
                final String User_Password = password.getText().toString();
                String User_Password1 = password1.getText().toString();

                if (profileImage == null) {
                    Toast.makeText(Registration.this, "Please Insert Your Image", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Image");
                }

                else if (User_Fullname.isEmpty()) {
                    Toast.makeText(Registration.this, "Please Enter your Fullname", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Full Name is Empty" + " " + User_Fullname);
                } else if (User_Email.isEmpty()) {
                    Toast.makeText(Registration.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Eamil Null" + " " + User_Email);
                }
                else if (!User_Email.matches(User_emailmatch))
                {
                    Toast.makeText(Registration.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }

                else if (User_Phone.isEmpty()) {
                    Toast.makeText(Registration.this, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Phone" + " " + User_Phone);
                } else if (User_Phone.charAt(0) != '+' ||User_Phone.charAt(1) != '8' || User_Phone.charAt(2) != '8' || User_Phone.charAt(3) != '0') {

                    Toast.makeText(Registration.this, "Must Be First 4 Digit=+880", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "First Position Number is" + " " + User_Phone);
                } else if (User_Phone.charAt(4) != '1') {
                    Toast.makeText(Registration.this, "5th Position Digit is = 1 ", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "5th Position Number is" + " " + User_Phone);
                } else if (User_Phone.charAt(5) != '9' && User_Phone.charAt(5) != '8' &&
                        User_Phone.charAt(5) != '7' && User_Phone.charAt(5) != '3' &&
                        User_Phone.charAt(5) != '4' && User_Phone.charAt(5) != '5'
                        && User_Phone.charAt(5) != '6') {
                    Toast.makeText(Registration.this, "6th Position Digit is = 3 or 4 or 5 or 6 or 7 or 8 or 9 ", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "6th Position Number is" + " " + User_Phone);

                } else if (User_Phone.length() != 14) {
                    Toast.makeText(Registration.this, "Number Must be 14 length with +880", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Phone Number Not =14" + " " + User_Phone);
                } else if (User_Password.isEmpty()) {
                    Toast.makeText(Registration.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Password" + " " + User_Password);
                } else if (User_Password1.isEmpty()) {
                    Toast.makeText(Registration.this, "Please Enter Your Conform Password", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Password1" + " " + User_Password1);
                } else if (!User_Password1.equals(User_Password)) {
                    Toast.makeText(Registration.this, "Please Enter Same Password", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Password  Password" + " " + User_Password + " " + User_Password1);
                } else if (User_Password.length() < 7) {
                    Toast.makeText(Registration.this, "Password Must be 6 grater than", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Password Length Not Grater then 7" + " " + User_Password);
                }
                else {

                    final Loading loading = new Loading(Registration.this);

                    Log.d(TAG,"else");
                    //user_image();


                    firebaseAuth.createUserWithEmailAndPassword(User_Email, User_Password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull final Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        loading.startLoading();

                                        UploadCustomerInformaion();



                                        Intent customer = new Intent(Registration.this, Login_activity.class);
                                        customer.putExtra("login", "customer");
                                        startActivity(customer);

                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                loading.dismissDialog();
                                            }
                                        },5000);


                                        Toast.makeText(Registration.this, "Registration is Complete ", Toast.LENGTH_SHORT).show();



                                    }
                                    else {
                                        Toast.makeText(Registration.this, "Already This Email has Registered ", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });
                }
                break;

            case R.id.subadmin_singup:

                final String SubAdmin_Fullname = fullname.getText().toString();
                final String SubAdmin_Email = email.getText().toString();
                String SubAdmin_emailmatch = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                final String SubAdmin_Phone = phone.getText().toString();
                final String SubAdmin_Password = password.getText().toString();
                String SubAdmin_Password1 = password1.getText().toString();
                final String SubAdmin_NID = nid.getText().toString();
                final String SubAdmin_Company = spinnerId_Bus_name.getSelectedItem().toString();

                if (profileImage == null) {
                    Toast.makeText(Registration.this, "Please Insert Your Image", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Image");
                }

                else if (SubAdmin_Fullname.isEmpty()) {
                    Toast.makeText(Registration.this, "Please Enter your Fullname", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Full Name is Empty" + " " + SubAdmin_Fullname);
                } else if (SubAdmin_Email.isEmpty()) {
                    Toast.makeText(Registration.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Eamil Null" + " " + SubAdmin_Email);
                }
                else if (!SubAdmin_Email.matches(SubAdmin_emailmatch))
                {
                    Toast.makeText(Registration.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }

                else if (SubAdmin_Phone.isEmpty()) {
                    Toast.makeText(Registration.this, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Phone" + " " +SubAdmin_Phone);
                } else if (SubAdmin_Phone.charAt(0) != '+' || SubAdmin_Phone.charAt(1) != '8' || SubAdmin_Phone.charAt(2) != '8' || SubAdmin_Phone.charAt(3) != '0') {

                    Toast.makeText(Registration.this, "Must Be First 4 Digit=+880", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "First Position Number is" + " " + SubAdmin_Phone);
                } else if (SubAdmin_Phone.charAt(4) != '1') {
                    Toast.makeText(Registration.this, "5th Position Digit is = 1 ", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "5th Position Number is" + " " + SubAdmin_Phone);
                } else if (SubAdmin_Phone.charAt(5) != '9' && SubAdmin_Phone.charAt(5) != '8' &&
                        SubAdmin_Phone.charAt(5) != '7' && SubAdmin_Phone.charAt(5) != '3' &&
                        SubAdmin_Phone.charAt(5) != '4' && SubAdmin_Phone.charAt(5) != '5'
                        && SubAdmin_Phone.charAt(5) != '6') {
                    Toast.makeText(Registration.this, "6th Position Digit is = 3 or 4 or 5 or 6 or 7 or 8 or 9 ", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "6th Position Number is" + " " + SubAdmin_Phone);

                } else if (SubAdmin_Phone.length() != 14) {
                    Toast.makeText(Registration.this, "Number Must be 14 length with +880", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Phone Number Not =14" + " " + SubAdmin_Phone);
                } else if (SubAdmin_Password.isEmpty()) {
                    Toast.makeText(Registration.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Password" + " " + SubAdmin_Password);
                } else if (SubAdmin_Password1.isEmpty()) {
                    Toast.makeText(Registration.this, "Please Enter Your Conform Password", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Password1" + " " + SubAdmin_Password1);
                } else if (!SubAdmin_Password1.equals(SubAdmin_Password)) {
                    Toast.makeText(Registration.this, "Please Enter Same Password", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Password  Password" + " " + SubAdmin_Password + " " + SubAdmin_Password1);
                } else if (SubAdmin_Password.length() < 7) {
                    Toast.makeText(Registration.this, "Password Must be 6 grater than", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Password Length Not Grater then 7" + " " + SubAdmin_Password);
                }  else if (SubAdmin_NID.isEmpty()) {
                    Toast.makeText(Registration.this, "Please Enter Your NID Number", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "NID" + " " + SubAdmin_NID);
                } else if (SubAdmin_NID.length() != 10) {
                    Toast.makeText(Registration.this, "NID Card Number Must be 10", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "NID Card Number Must be 10" + " " + SubAdmin_NID);
                }
                else if (SubAdmin_Company.equals("Select The Option")) {
                    Toast.makeText(Registration.this, "Please Choose Your Bus Name", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Bus_name" + " " + spinnerId_Bus_name);
                }

                else {

                    final Loading loading = new Loading(Registration.this);


                    Log.d(TAG,"else");


                    firebaseAuth.createUserWithEmailAndPassword(SubAdmin_Email, SubAdmin_Password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull final Task<AuthResult> task) {
                                    if (task.isSuccessful()) {


                                        UploadsubadminInformaion();
                                        loading.startLoading();

                                        Intent subadmin = new Intent(Registration.this, Login_activity.class);
                                        subadmin.putExtra("login", "subadmin");
                                        startActivity(subadmin);


                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                loading.dismissDialog();
                                            }
                                        }, 5000);


                                        Toast.makeText(Registration.this, "Registration is Complete ", Toast.LENGTH_SHORT).show();

                                    }

                                    else
                                        {
                                        Toast.makeText(Registration.this, "Already This Email has Registered ", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });
                }

                break;

            case R.id.admin_signup:
                final String Admin_Fullname = fullname.getText().toString();
                final String Admin_Email = email.getText().toString();
               final String Admin_emailmatch = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                final String Admin_Phone = phone.getText().toString();
                final String Admin_Password = password.getText().toString();
               final String Admin_Password1 = password1.getText().toString();
                final String Admin_NID = nid.getText().toString();
                final String Road_permit_license = road_permit_license.getText().toString();
                final String Admin_Company = spinnerId_Bus_name.getSelectedItem().toString();

                if (profileImage == null) {
                    Toast.makeText(Registration.this, "Please Insert Your Image", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Image");
                }

                else if (Admin_Fullname.isEmpty()) {
                    Toast.makeText(Registration.this, "Please Enter your Fullname", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Full Name is Empty" + " " + Admin_Fullname);
                } else if (Admin_Email.isEmpty()) {
                    Toast.makeText(Registration.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Eamil Null" + " " + Admin_Email);
                }
                else if (!Admin_Email.matches(Admin_emailmatch))
                {
                    Toast.makeText(Registration.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }

                else if (Admin_Phone.isEmpty()) {
                    Toast.makeText(Registration.this, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Phone" + " " +Admin_Phone);
                } else if (Admin_Phone.charAt(0) != '+' || Admin_Phone.charAt(1) != '8' || Admin_Phone.charAt(2) != '8' || Admin_Phone.charAt(3) != '0') {

                    Toast.makeText(Registration.this, "Must Be First 4 Digit=+880", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "First Position Number is" + " " + Admin_Phone);
                } else if (Admin_Phone.charAt(4) != '1') {
                    Toast.makeText(Registration.this, "5th Position Digit is = 1 ", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "5th Position Number is" + " " + Admin_Phone);
                } else if (Admin_Phone.charAt(5) != '9' && Admin_Phone.charAt(5) != '8' &&
                        Admin_Phone.charAt(5) != '7' && Admin_Phone.charAt(5) != '3' &&
                        Admin_Phone.charAt(5) != '4' && Admin_Phone.charAt(5) != '5'
                        && Admin_Phone.charAt(5) != '6') {
                    Toast.makeText(Registration.this, "6th Position Digit is = 3 or 4 or 5 or 6 or 7 or 8 or 9 ", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "6th Position Number is" + " " + Admin_Phone);

                } else if (Admin_Phone.length() != 14) {
                    Toast.makeText(Registration.this, "Number Must be 14 length with +880", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Phone Number Not =14" + " " + Admin_Phone);
                } else if (Admin_Password.isEmpty()) {
                    Toast.makeText(Registration.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Password" + " " + Admin_Password);
                } else if (Admin_Password1.isEmpty()) {
                    Toast.makeText(Registration.this, "Please Enter Your Conform Password", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Password1" + " " + Admin_Password1);
                } else if (!Admin_Password1.equals(Admin_Password)) {
                    Toast.makeText(Registration.this, "Please Enter Same Password", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Password  Password" + " " + Admin_Password + " " + Admin_Password1);
                } else if (Admin_Password.length() < 7) {
                    Toast.makeText(Registration.this, "Password Must be 6 grater than", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Password Length Not Grater then 7" + " " + Admin_Password);
                }  else if (Admin_NID.isEmpty()) {
                    Toast.makeText(Registration.this, "Please Enter Your NID Number", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "NID" + " " + Admin_NID);
                } else if (Admin_NID.length() != 10) {
                    Toast.makeText(Registration.this, "NID Card Number Must be 10", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "NID Card Number Must be 10" + " " + Admin_NID);
                }
                else if (Admin_Company.equals("Select The Option")) {

                    Toast.makeText(Registration.this, "Please Choose Your Bus Name", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Admin_Company" + " " + spinnerId_Bus_name);
                }
                else if (Road_permit_license.isEmpty()) {
                    Toast.makeText(Registration.this, "Please Enter Your Road Permit License", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Road_permit_license" + " " + Road_permit_license);
                }

                else {

                    final Loading loading = new Loading(Registration.this);


                    Log.d(TAG,"else");


                    firebaseAuth.createUserWithEmailAndPassword(Admin_Email, Admin_Password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull final Task<AuthResult> task) {
                                    if (task.isSuccessful()) {


                                        loading.startLoading();

                                        UploadAdminInformaion();


                                        Intent admin = new Intent(Registration.this, Login_activity.class);
                                        admin.putExtra("login","admin");
                                        startActivity(admin);


                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                loading.dismissDialog();
                                            }
                                        }, 5000);


                                        Toast.makeText(Registration.this, "Registration is Complete ", Toast.LENGTH_SHORT).show();



                                    }
                                    else {
                                        Toast.makeText(Registration.this, "Already This Email has Registered ", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });
                }

                break;


        }




    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void UploadDriverInformaion() {

        if (filepath!= null)
        {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Image Uploading");
            dialog.show();
            final StorageReference uploder = driver_Image_Database_Ref.child(System.currentTimeMillis() + "."+
                    getFileExtension(filepath));

            uploder.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            uploder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Model_class_Registration model =new Model_class_Registration(
                                            fullname.getText().toString(),
                                            email.getText().toString(),
                                            phone.getText().toString(),
                                            password.getText().toString(),
                                            driving_License.getText().toString(),
                                            nid.getText().toString(),
                                            spinnerId_Bus_name.getSelectedItem().toString(),
                                            usertype,
                                            uri.toString()
                                            );
                                    driver_Database_Ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(model);

                                    Toast.makeText(Registration.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });

                            profileImage.setImageResource(R.drawable.ic_launcher_background);
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    final float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.setMessage("Uploaded:" + (int) percent + "%");
                        }
                    },8000);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    },8000);


                    Toast.makeText(Registration.this, "Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            Toast.makeText(this, "Please Select Image", Toast.LENGTH_SHORT).show();
        }

    }



    private void UploadCustomerInformaion() {

        if (filepath!= null)
        {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Image Uploading");
            dialog.show();
            final StorageReference uploder = Customer_Image_Database_Ref.child(System.currentTimeMillis() + "."+
                    getFileExtension(filepath));

            uploder.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            uploder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {


                                    Model_class_Registration model =new Model_class_Registration();

                                    model.setFullname(fullname.getText().toString());
                                    model.setEmail(email.getText().toString());
                                    model.setPhone(phone.getText().toString());
                                    model.setPassword(password.getText().toString());
                                    model.setUserType(usertype);
                                    model.setProfileImageurl(uri.toString());



                                    custome_Database_Ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(model);
                                    Toast.makeText(Registration.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });

                            profileImage.setImageResource(R.drawable.ic_launcher_background);
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    dialog.setMessage("Uploaded:" + (int) percent + "%");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(Registration.this, "Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            Toast.makeText(this, "Please Select Image", Toast.LENGTH_SHORT).show();
        }

    }


    private void UploadAdminInformaion() {

        if (filepath == null)
        {
            Toast.makeText(this, "Please Select Image", Toast.LENGTH_SHORT).show();

        }
        else
        {


            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Image Uploading");
            dialog.show();
            final StorageReference uploder = Admin_Image_Database_Ref.child(System.currentTimeMillis() + "."+
                    getFileExtension(filepath));

            uploder.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            uploder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Model_class_Registration model =new Model_class_Registration(
                                            fullname.getText().toString(),
                                            email.getText().toString(),
                                            phone.getText().toString(),
                                            password.getText().toString(),
                                            nid.getText().toString(),
                                            road_permit_license.getText().toString(),
                                            spinnerId_Bus_name.getSelectedItem().toString(),
                                            trade_license_of_bus_Company.getText().toString(),
                                            usertype,
                                            uri.toString()
                                    );
                                    admin_Database_Ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(model);
                                    Toast.makeText(Registration.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });

                            profileImage.setImageResource(R.drawable.ic_launcher_background);
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    dialog.setMessage("Uploaded:" + (int) percent + "%");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(Registration.this, "Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });


        }

    }


    private void UploadsubadminInformaion() {

        if (filepath!= null)
        {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Image Uploading");
            dialog.show();
            final StorageReference uploder = Subadmin_Image_Database_Ref.child(System.currentTimeMillis() + "."+
                    getFileExtension(filepath));

            uploder.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            uploder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Model_class_Registration model =new Model_class_Registration(
                                            fullname.getText().toString(),
                                            email.getText().toString(),
                                            phone.getText().toString(),
                                            password.getText().toString(),
                                            nid.getText().toString(),
                                            spinnerId_Bus_name.getSelectedItem().toString(),                                            usertype,
                                            uri.toString()
                                    );
                                    subadmin_Database_Ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(model);
                                    Toast.makeText(Registration.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });

                            profileImage.setImageResource(R.drawable.ic_launcher_background);
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    dialog.setMessage("Uploaded:" + (int) percent + "%");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(Registration.this, "Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            Toast.makeText(this, "Please Select Image", Toast.LENGTH_SHORT).show();
        }

    }



    private void choose_image()
    {
        Intent choose = new Intent();
        choose.setType("image/*");
        choose.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(choose,"Select Image"),1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==1  && resultCode == RESULT_OK
                &&  data != null && data.getData() != null )
        {
            Log.d(TAG,"image"+requestCode+" "+resultCode);

           filepath = data.getData();


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
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