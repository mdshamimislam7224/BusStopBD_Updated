package com.shamim.newbusstop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shamim.newbusstop.Internet_Connection.Network_Change_Listener;

public class Login_activity extends AppCompatActivity implements View.OnClickListener {
    TextView liginHere;
    EditText login_email, login_password;
    Button login_forgotten_password, login_donot_account_user, login_donot_account_admin, login_donot_account_driver, login_donot_account_subadmin, user_login,
            admin_login, subAdmin_login, driver_login;
    FirebaseAuth firebaseAuth;
    DatabaseReference userTypeRef;
    String customerId = "", userType, email, phone, profileImage;
    FirebaseAuth mAuth;
    String login;
    String TAG = "Login_Activity";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Network_Change_Listener connection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        connection = new Network_Change_Listener();

        firebaseAuth = FirebaseAuth.getInstance();
        preferences = getSharedPreferences("BusBD_Info", Context.MODE_PRIVATE);
        editor = preferences.edit();

        mAuth = FirebaseAuth.getInstance();
        //customerId = mAuth.getCurrentUser().getUid();
        userTypeRef = FirebaseDatabase.getInstance().getReference("Bus Stop BD").child("Registration").child("Customer");


        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        login_forgotten_password = findViewById(R.id.login_forgotten);
        login_donot_account_user = findViewById(R.id.login_not_account_user);
        login_donot_account_driver = findViewById(R.id.login_not_account_driver);
        login_donot_account_admin = findViewById(R.id.login_not_account_admin);
        login_donot_account_subadmin = findViewById(R.id.login_not_account_subadmin);

        user_login = findViewById(R.id.user_login);
        admin_login = findViewById(R.id.admin_login);
        subAdmin_login = findViewById(R.id.subadmin_login);
        driver_login = findViewById(R.id.driver_login);
        liginHere = findViewById(R.id.login_here);

        Intent intent = getIntent();
        login = intent.getStringExtra("login");

        if (login.equals("customer")) {
            user_login.setVisibility(View.VISIBLE);
            driver_login.setVisibility(View.GONE);
            admin_login.setVisibility(View.GONE);
            subAdmin_login.setVisibility(View.GONE);
            login_donot_account_driver.setVisibility(View.GONE);
            login_donot_account_admin.setVisibility(View.GONE);
            login_donot_account_subadmin.setVisibility(View.GONE);

        } else if (login.equals("driver")) {
            user_login.setVisibility(View.GONE);
            driver_login.setVisibility(View.VISIBLE);
            admin_login.setVisibility(View.GONE);
            subAdmin_login.setVisibility(View.GONE);
            login_donot_account_driver.setVisibility(View.VISIBLE);
            login_donot_account_user.setVisibility(View.GONE);
            login_donot_account_admin.setVisibility(View.GONE);
            login_donot_account_subadmin.setVisibility(View.GONE);

        } else if (login.equals("admin")) {
            user_login.setVisibility(View.GONE);
            driver_login.setVisibility(View.GONE);
            admin_login.setVisibility(View.VISIBLE);
            subAdmin_login.setVisibility(View.GONE);

            login_donot_account_driver.setVisibility(View.GONE);
            login_donot_account_user.setVisibility(View.GONE);
            login_donot_account_admin.setVisibility(View.VISIBLE);
            login_donot_account_subadmin.setVisibility(View.GONE);


        } else if (login.equals("subadmin")) {
            user_login.setVisibility(View.GONE);
            driver_login.setVisibility(View.GONE);
            admin_login.setVisibility(View.GONE);
            subAdmin_login.setVisibility(View.VISIBLE);

            login_donot_account_driver.setVisibility(View.GONE);
            login_donot_account_user.setVisibility(View.GONE);
            login_donot_account_admin.setVisibility(View.GONE);
            login_donot_account_subadmin.setVisibility(View.VISIBLE);

        }

        login_forgotten_password.setOnClickListener(this);
        login_donot_account_user.setOnClickListener(this);
        login_donot_account_driver.setOnClickListener(this);
        login_donot_account_admin.setOnClickListener(this);
        login_donot_account_subadmin.setOnClickListener(this);
        user_login.setOnClickListener(this);
        admin_login.setOnClickListener(this);
        subAdmin_login.setOnClickListener(this);
        driver_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.login_forgotten:

                Intent login_forgotten_password = new Intent(this, Forgotten_password.class);
                startActivity(login_forgotten_password);

                break;

            case R.id.user_login:

                User_sign();

                break;
            case R.id.driver_login:
                Driver_sign();

                break;
            case R.id.admin_login:
                AdminsignIn();
                break;

            case R.id.subadmin_login:
                SubAdminsignIn();
                break;

            case R.id.login_not_account_user:

                Intent intent = new Intent(this, Registration.class);
                intent.putExtra("user_type", "user");
                startActivity(intent);

                break;
            case R.id.login_not_account_driver:

                Intent intent1 = new Intent(this, Registration.class);
                intent1.putExtra("user_type", "driver");
                startActivity(intent1);

                break;
            case R.id.login_not_account_admin:

                Intent admin = new Intent(this, Registration.class);
                admin.putExtra("user_type", "admin");
                startActivity(admin);

                break;
            case R.id.login_not_account_subadmin:

                Intent subadmin = new Intent(this, Registration.class);
                subadmin.putExtra("user_type", "subadmin");
                startActivity(subadmin);

                break;

        }

    }

    private void User_sign() {

        final String Email = login_email.getText().toString();
        final String Password = login_password.getText().toString();
        String emailmatch = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (Email.isEmpty()) {
            Toast.makeText(this, "Please Enter Your Valid Email", Toast.LENGTH_SHORT).show();
        } else if (!Email.matches(emailmatch)) {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
        } else if (Password.isEmpty()) {
            Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();

        } else {
            final Loading loading = new Loading(Login_activity.this);

            firebaseAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        Log.d(TAG, " Email===" + task);

                        loading.startLoading();

                        String customerUid = task.getResult().getUser().getUid();

                        Toast.makeText(Login_activity.this, "User==" + customerUid, Toast.LENGTH_SHORT).show();

                        FirebaseDatabase customerDatabaseRef = FirebaseDatabase.getInstance();

                        customerDatabaseRef.getReference("Bus Stop BD").child("Registration").child("Customer").child(customerUid).child("userType")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        String userCheck = snapshot.getValue(String.class);

                                        Log.d(TAG, "Check User ==" + userCheck);

                                        if (userCheck != null) {

                                            if (userCheck.equals("user")) {

                                                preferences = getSharedPreferences("BusBD_Info", Context.MODE_PRIVATE);
                                                editor = preferences.edit();
                                                editor.putString("emailkey", Email);
                                                editor.putString("passwordkey", Password);
                                                editor.putBoolean("User_isLogged", true);
                                                editor.commit();

                                                Intent drivermap = new Intent(Login_activity.this, Customer_MapsActivity.class);
                                                startActivity(drivermap);
                                                Toast.makeText(Login_activity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        loading.dismissDialog();
                                                    }
                                                }, 5000);
                                            } else {

                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        loading.dismissDialog();
                                                    }
                                                }, 5000);

                                                Toast.makeText(Login_activity.this, "Please Choose your Right User ", Toast.LENGTH_SHORT).show();
                                            }

                                        } else {
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    loading.dismissDialog();
                                                }
                                            }, 5000);
                                            Toast.makeText(Login_activity.this, "Please Choose your Right User ", Toast.LENGTH_SHORT).show();

                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                loading.dismissDialog();
                                            }
                                        }, 5000);
                                        Toast.makeText(Login_activity.this, "ERRor=" + error, Toast.LENGTH_SHORT).show();

                                    }
                                });


                    } else {
                        Toast.makeText(Login_activity.this, "Wrong Email Or Password", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }


    private void Driver_sign() {

        final String Email = login_email.getText().toString();
        final String Password = login_password.getText().toString();
        String emailmatch = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (Email.isEmpty()) {
            Toast.makeText(this, "Please Enter Your Valid Email", Toast.LENGTH_SHORT).show();
        } else if (!Email.matches(emailmatch)) {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
        } else if (Password.isEmpty()) {
            Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();

        } else {
            final Loading loading = new Loading(Login_activity.this);

            Log.d(TAG, "driver_login");
            firebaseAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    Log.d(TAG, "driver_login" + task);

                    if (task.isSuccessful()) {

                        loading.startLoading();

                        String customerUid = task.getResult().getUser().getUid();

                        FirebaseDatabase customerDatabaseRef = FirebaseDatabase.getInstance();

                        customerDatabaseRef.getReference("Bus Stop BD").child("Registration").child("Driver").child(customerUid).child("userType")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String userCheck = snapshot.getValue(String.class);

                                        Log.d(TAG, "Check User ==" + userCheck);

                                        if (userCheck != null) {

                                            if (userCheck.equals("driver")) {

                                                preferences = getSharedPreferences("BusBD_Info", Context.MODE_PRIVATE);
                                                editor = preferences.edit();
                                                editor.putString("emailkey", Email);
                                                editor.putString("passwordkey", Password);
                                                editor.putBoolean("Driver_isLogged", true);
                                                editor.commit();


                                                Intent drivermap = new Intent(Login_activity.this, Driver_maps_Activity.class);
                                                startActivity(drivermap);
                                                Toast.makeText(Login_activity.this, "Login Successful", Toast.LENGTH_SHORT).show();


                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        loading.dismissDialog();
                                                    }
                                                }, 5000);
                                            } else {
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        loading.dismissDialog();
                                                    }
                                                }, 5000);

                                                Toast.makeText(Login_activity.this, "Please Choose your Right User ", Toast.LENGTH_SHORT).show();
                                            }

                                        } else {
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    loading.dismissDialog();
                                                }
                                            }, 5000);
                                            Toast.makeText(Login_activity.this, "Please Choose your Right User ", Toast.LENGTH_SHORT).show();


                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                loading.dismissDialog();
                                            }
                                        }, 5000);
                                        Toast.makeText(Login_activity.this, "ERROR== " + error, Toast.LENGTH_SHORT).show();


                                    }
                                });

                    } else {


                        Toast.makeText(Login_activity.this, "Email or Password Wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    public void AdminsignIn() {

        final String Email = login_email.getText().toString();
        final String Password = login_password.getText().toString();
        String emailmatch = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (Email.isEmpty()) {
            Toast.makeText(this, "Please Enter Your Valid Email", Toast.LENGTH_SHORT).show();
        } else if (!Email.matches(emailmatch)) {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
        } else if (Password.isEmpty()) {
            Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();

        } else {

            final Loading loading = new Loading(Login_activity.this);

            firebaseAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        loading.startLoading();

                        String customerUid = task.getResult().getUser().getUid();

                        FirebaseDatabase customerDatabaseRef = FirebaseDatabase.getInstance();

                        customerDatabaseRef.getReference("Bus Stop BD").child("Registration").child("Admin").child(customerUid).child("userType")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        String userCheck = snapshot.getValue(String.class);

                                        Log.d(TAG, "Check User ==" + userCheck);

                                        if (userCheck != null) {

                                            if (userCheck.equals("admin")) {

                                                preferences = getSharedPreferences("BusBD_Info", Context.MODE_PRIVATE);
                                                editor = preferences.edit();
                                                editor.putString("emailkey", Email);
                                                editor.putString("passwordkey", Password);
                                                editor.putBoolean("Admin_isLogged", true);
                                                editor.commit();


                                                Intent adminmap = new Intent(Login_activity.this, Admin_Activity.class);
                                                startActivity(adminmap);
                                                Toast.makeText(Login_activity.this, "Login Successful", Toast.LENGTH_SHORT).show();


                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        loading.dismissDialog();
                                                    }
                                                }, 5000);
                                            } else {
                                                Toast.makeText(Login_activity.this, "Invalid Email", Toast.LENGTH_SHORT).show();

                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        loading.dismissDialog();
                                                    }
                                                }, 5000);
                                            }
                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                        //here check user check thhen sharedPreference.


                    } else {


                        Toast.makeText(Login_activity.this, "Email or Password Wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }


    public void SubAdminsignIn() {

        final String Email = login_email.getText().toString();
        final String Password = login_password.getText().toString();
        String emailmatch = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (Email.isEmpty()) {
            Toast.makeText(this, "Please Enter Your Valid Email", Toast.LENGTH_SHORT).show();
        } else if (!Email.matches(emailmatch)) {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
        } else if (Password.isEmpty()) {
            Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();

        } else {

            final Loading loading = new Loading(Login_activity.this);

            Log.d(TAG, "driver_login");
            firebaseAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    Log.d(TAG, "driver_login" + task);
                    if (task.isSuccessful()) {

                        loading.startLoading();

                        String customerUid = task.getResult().getUser().getUid();

                        FirebaseDatabase customerDatabaseRef = FirebaseDatabase.getInstance();

                        customerDatabaseRef.getReference("Bus Stop BD").child("Registration").child("SubAdmin").child(customerUid).child("userType")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String userCheck = snapshot.getValue(String.class);

                                        Log.d(TAG, "Check User ==" + userCheck);

                                        if (userCheck != null) {

                                            if (userCheck.equals("subadmin")) {

                                                preferences = getSharedPreferences("BusBD_Info", Context.MODE_PRIVATE);
                                                editor = preferences.edit();
                                                editor.putString("emailkey", Email);
                                                editor.putString("passwordkey", Password);
                                                editor.putBoolean("SubAdmin_isLogged", true);
                                                editor.commit();


                                                Intent subadminmap = new Intent(Login_activity.this, Driver_maps_Activity.class);
                                                startActivity(subadminmap);
                                                Toast.makeText(Login_activity.this, "Login Successful", Toast.LENGTH_SHORT).show();


                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        loading.dismissDialog();
                                                    }
                                                }, 5000);
                                            } else {
                                                Toast.makeText(Login_activity.this, "Invalid Email", Toast.LENGTH_SHORT).show();

                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        loading.dismissDialog();
                                                    }
                                                }, 5000);
                                            }


                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                        //here check user check thhen sharedPreference.


                    } else {


                        Toast.makeText(Login_activity.this, "Email or Password Wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connection, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(connection);
        super.onStop();
    }
}

