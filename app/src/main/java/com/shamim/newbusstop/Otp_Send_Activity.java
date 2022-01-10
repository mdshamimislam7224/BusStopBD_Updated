package com.shamim.newbusstop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shamim.newbusstop.Internet_Connection.Network_Change_Listener;

import java.util.concurrent.TimeUnit;

public class Otp_Send_Activity extends AppCompatActivity implements View.OnClickListener {
    TextView mobile_number;
    Button send_message_btn;
    FirebaseAuth mAuth;
    final String TAG = "Otp_SEND";
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    Network_Change_Listener connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_send);
        connection = new Network_Change_Listener();

        numbergetshow();
        mobile_number = findViewById(R.id.mobile_number);
        send_message_btn = findViewById(R.id.send_message_btn);
        mAuth = FirebaseAuth.getInstance();

        mCallback();


        send_message_btn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_message_btn:

                /*PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+88" +mobile_number_edtText.getText().toString(),
                        60, TimeUnit.SECONDS
                        ,Otp_Send_Activity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
                        {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                            }

                            @Override
                            public void onCodeSent(@NonNull String  verficationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                Intent sendintent = new Intent(Otp_Send_Activity.this, Otp_verfication_activity.class);
                                sendintent.putExtra("mobile",mobile_number_edtText.getText().toString());
                                sendintent.putExtra("verficationID",verficationID);
                                startActivity(sendintent);


                            }
                        }
                );*/

                numberget();
                break;
        }
    }


    public void numberget() {

        String customerUid = FirebaseAuth.getInstance().getUid();

        FirebaseDatabase customerDatabaseRef = FirebaseDatabase.getInstance();

        customerDatabaseRef.getReference("Bus Stop BD").child("Registration").child("Customer").child(customerUid).child("phone")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String phone_number = snapshot.getValue(String.class);
                        Log.i("just_get_number==", phone_number);

                        if (phone_number != null) {
                            mobile_number.setText(phone_number);
                            PhoneAuthOptions options =
                                    PhoneAuthOptions.newBuilder(mAuth)
                                            .setPhoneNumber(phone_number)       // Phone number to verify
                                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                            .setActivity(Otp_Send_Activity.this)                 // Activity (for callback binding)
                                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                            .build();
                            PhoneAuthProvider.verifyPhoneNumber(options);

                        } else {
                            Toast.makeText(Otp_Send_Activity.this, "Number Invalid", Toast.LENGTH_SHORT).show();


                        }


                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void numbergetshow() {

        String customerUid = FirebaseAuth.getInstance().getUid();

        FirebaseDatabase customerDatabaseRef = FirebaseDatabase.getInstance();

        customerDatabaseRef.getReference("Bus Stop BD").child("Registration").child("Customer").child(customerUid).child("phone")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String phone_number = snapshot.getValue(String.class);
                        Log.i("just_get_number==", phone_number);

                        if (phone_number != null) {
                            mobile_number.setText(phone_number);


                        } else {
                            Toast.makeText(Otp_Send_Activity.this, "Number Invalid", Toast.LENGTH_SHORT).show();


                        }


                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    public void mCallback() {

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);
                Intent sendintent = new Intent(Otp_Send_Activity.this, Otp_verfication_activity.class);
                sendintent.putExtra("mobile", mobile_number.getText().toString());
                sendintent.putExtra("verficationID", verificationId);
                startActivity(sendintent);

                // Save verification ID and resending token so we can use them later


                // ...
            }
        };
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