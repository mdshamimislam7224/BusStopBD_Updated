package com.shamim.newbusstop.drawer_layout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.shamim.newbusstop.Customer_MapsActivity;
import com.shamim.newbusstop.Forgotten_password;
import com.shamim.newbusstop.Home;
import com.shamim.newbusstop.Login_activity;
import com.shamim.newbusstop.R;

public class login extends Fragment implements View.OnClickListener {

    Button customer,driver,admin,subadmin,backToHome_chooser;
    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_drawerlayout, container, false);


        customer = view.findViewById(R.id.customer_login_chooser);
        driver = view.findViewById(R.id.driver_login_chooser);
        admin = view.findViewById(R.id.admin_login_chooser);
        subadmin = view.findViewById(R.id.subadmin_login_chooser);
        backToHome_chooser = view.findViewById(R.id.backToHome_chooser);




        customer.setOnClickListener(this);
        driver.setOnClickListener(this);
        admin.setOnClickListener(this);
        subadmin.setOnClickListener(this);
        backToHome_chooser.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.customer_login_chooser:
                Intent customer = new Intent(getActivity(), Login_activity.class);
                customer.putExtra("login", "customer");
                startActivity(customer);
                getActivity().finish();

                break;
            case R.id.driver_login_chooser:
                Intent driver = new Intent(getActivity(), Login_activity.class);
                driver.putExtra("login","driver");
                startActivity(driver);

                break;

            case R.id.subadmin_login_chooser:
                Intent subadmin = new Intent(getActivity(), Login_activity.class);
                subadmin.putExtra("login","subadmin");
                startActivity(subadmin);

                break;

            case R.id.admin_login_chooser:
                Intent admin = new Intent(getActivity(), Login_activity.class);
                admin.putExtra("login","admin");
                startActivity(admin);

                break;
            case R.id.backToHome_chooser:
                Intent backToHome = new Intent(getActivity(), Home.class);
                startActivity(backToHome);

        }

    }

}