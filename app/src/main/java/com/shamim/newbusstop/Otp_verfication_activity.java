package com.shamim.newbusstop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.shamim.newbusstop.Internet_Connection.Network_Change_Listener;

import java.util.concurrent.TimeUnit;

public class Otp_verfication_activity extends AppCompatActivity implements View.OnClickListener {
    TextView resend_otp;
    EditText inputcode1, inputcode2, inputcode3, inputcode4, inputcode5, inputcode6;
    Button verify_message_btn;
    String verficationId, mobile_Number;
    String TAG = "Otp_Verfication";

    FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    Network_Change_Listener connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verfication_activity);

        connection = new Network_Change_Listener();

        inputcode1 = findViewById(R.id.inputcode1);
        inputcode2 = findViewById(R.id.inputcode2);
        inputcode3 = findViewById(R.id.inputcode3);
        inputcode4 = findViewById(R.id.inputcode4);
        inputcode5 = findViewById(R.id.inputcode5);
        inputcode6 = findViewById(R.id.inputcode6);
        verify_message_btn = findViewById(R.id.verify_message_btn);
        resend_otp = findViewById(R.id.resend_otp);

        mAuth = FirebaseAuth.getInstance();


        verficationId = getIntent().getStringExtra("verficationID");
        mobile_Number = getIntent().getStringExtra("mobile");

        Log.d(TAG, "Sms verfication==" + verficationId);
        Log.d(TAG, "Mobile verfication==" + mobile_Number);

        mCallBack();
        setupOTPInput();

        verify_message_btn.setOnClickListener(this);
        resend_otp.setOnClickListener(this);


    }


    private void setupOTPInput() {
        inputcode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputcode2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputcode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputcode3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputcode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputcode4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputcode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputcode5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputcode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputcode6.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.resend_otp:
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber(mobile_Number)       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(Otp_verfication_activity.this)  // Activity (for callback binding)
                                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);

                break;

            case R.id.verify_message_btn:

                if (inputcode1.getText().toString().trim().isEmpty()
                        || inputcode2.getText().toString().trim().isEmpty()
                        || inputcode3.getText().toString().trim().isEmpty()
                        || inputcode4.getText().toString().trim().isEmpty()
                        || inputcode5.getText().toString().trim().isEmpty()
                        || inputcode6.getText().toString().trim().isEmpty()

                ) {
                    Toast.makeText(Otp_verfication_activity.this, "Please Enter Your Valid Code", Toast.LENGTH_SHORT).show();
                }
                else {


                String smsCode =
                        inputcode1.getText().toString() +
                                inputcode2.getText().toString() +
                                inputcode3.getText().toString() +
                                inputcode4.getText().toString() +
                                inputcode5.getText().toString() +
                                inputcode6.getText().toString();


                Log.d(TAG, "Sms verfication==" + smsCode);


                 if (verficationId != null) {

                     PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verficationId, smsCode);


                     FirebaseAuth.getInstance().signInWithCredential(credential)
                             .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                 @Override
                                 public void onComplete(@NonNull Task<AuthResult> task) {
                                  if (task.isSuccessful())
                                  {



                                      Intent customer = new Intent(Otp_verfication_activity.this, Login_activity.class);
                                      customer.putExtra("login", "customer");
                                      startActivity(customer);
                                      Toast.makeText(Otp_verfication_activity.this, "Login Here", Toast.LENGTH_SHORT).show();
                                  }
                                  else
                                  {
                                      Toast.makeText(Otp_verfication_activity.this, "Code Invalid", Toast.LENGTH_SHORT).show();

                                  }
                                 }
                             });



                 }


                }


                    break;
                }
        }


        public void mCallBack ()
        {

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
                    Intent sendintent = new Intent(Otp_verfication_activity.this, Otp_verfication_activity.class);

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