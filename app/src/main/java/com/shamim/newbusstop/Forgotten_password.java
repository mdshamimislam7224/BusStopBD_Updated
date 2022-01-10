package com.shamim.newbusstop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.shamim.newbusstop.Internet_Connection.Network_Change_Listener;

public class Forgotten_password extends AppCompatActivity {
    EditText email;
    Button send,back;
    FirebaseAuth firebaseAuth;
    Network_Change_Listener connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        connection = new Network_Change_Listener();

        setContentView(R.layout.activity_forgotten_password);
        email = findViewById(R.id.email);
        send = findViewById(R.id.send);
        back = findViewById(R.id.back);

        firebaseAuth=FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intToMain = new Intent(Forgotten_password.this, Home.class);
                startActivity(intToMain);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Email = email.getText().toString();
                if(TextUtils.isEmpty(Email)){

                    Toast.makeText(Forgotten_password.this, "Please Enter Your Register Email", Toast.LENGTH_SHORT).show();

                }
                else{

                    firebaseAuth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                            }
                            else {
                                Toast.makeText(Forgotten_password.this, "Please Enter Your Correct Email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


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
    protected void onStop() {
        unregisterReceiver(connection);
        super.onStop();
    }
}