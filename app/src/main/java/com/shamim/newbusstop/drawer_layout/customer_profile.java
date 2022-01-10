package com.shamim.newbusstop.drawer_layout;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shamim.newbusstop.Customer_MapsActivity;
import com.shamim.newbusstop.Forgotten_password;
import com.shamim.newbusstop.Home;
import com.shamim.newbusstop.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class customer_profile extends Fragment implements View.OnClickListener {
    ImageView customer_profile_image,arraw_back_image;
    TextView customer_profile_name,customer_profile_name_text,customer_profile_email_text,customer_profile_phone_text,toolbar_text_id;
    EditText customer_profile_name_edittext,customer_profile_email_edittext,customer_profile_phone_edittext;
    Button customer_change_password_btn,customer_edit_profile_btn,customer_update_profile_btn;
    DatabaseReference profileRef;
    String customerId="", fullname,email,phone,profileImage;
    FirebaseAuth mAuth;
    Uri resultUri;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_customer_drawerlayout, container, false);


        customer_profile_image= view.findViewById(R.id.customer_profile_image);
        customer_profile_name = view.findViewById(R.id.customer_profile_name);
        customer_profile_name_text = view.findViewById(R.id.customer_profile_name_text);
        customer_profile_email_text = view.findViewById(R.id.customer_profile_email_text);
        customer_profile_phone_text = view.findViewById(R.id.customer_profile_phone_text);
        customer_profile_name_edittext = view.findViewById(R.id.customer_profile_name_edittext);
        customer_profile_email_edittext = view.findViewById(R.id.customer_profile_email_edittext);
        customer_profile_phone_edittext = view.findViewById(R.id.customer_profile_phone_edittext);
        customer_change_password_btn = view.findViewById(R.id.customer_change_password_btn);
        customer_edit_profile_btn = view.findViewById(R.id.customer_edit_profile_btn);
        arraw_back_image = view.findViewById(R.id.arraw_back_image);
        customer_update_profile_btn = view.findViewById(R.id.customer_update_profile_btn);
        toolbar_text_id= view.findViewById(R.id.toolbar_text_id);

        toolbar_text_id.setText("Profile Deatil");




        mAuth = FirebaseAuth.getInstance();
        customerId = mAuth.getCurrentUser().getUid();
        profileRef = FirebaseDatabase.getInstance().getReference("Bus Stop BD").child("Registration").child("Customer").child(customerId);



        customer_change_password_btn.setOnClickListener(this);
        customer_edit_profile_btn.setOnClickListener(this);
        arraw_back_image.setOnClickListener(this);
        customer_update_profile_btn.setOnClickListener(this);




        RetriveData();

        return view;


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_customer:
                Intent intent = new Intent(getActivity(), customer_profile.class);
                startActivity(intent);
                break;

            case R.id.allbus_customer:


                break;

            case R.id. arraw_back_image:
                Intent intentarraw = new Intent(getActivity(), Customer_MapsActivity.class);
                startActivity(intentarraw);
                getActivity().finish();
                break;

            case R.id. customer_edit_profile_btn:

                customer_profile_name_edittext.setVisibility(View.VISIBLE);
                customer_profile_email_edittext.setVisibility(View.VISIBLE);
                customer_profile_phone_edittext.setVisibility(View.VISIBLE);
                customer_update_profile_btn.setVisibility(View.VISIBLE);
                customer_edit_profile_btn.setVisibility(View.GONE);
                customer_profile_name_text.setVisibility(View.GONE);
                customer_profile_email_text.setVisibility(View.GONE);
                customer_profile_phone_text.setVisibility(View.GONE);
                break;

            case R.id.  customer_update_profile_btn:
                Update();

                break;
            case R.id. customer_change_password_btn:
                Intent intentchangepass = new Intent(getActivity(), Forgotten_password.class);
                startActivity(intentchangepass);
                getActivity().finish();
                break;


        }

    }
    public  void  RetriveData(){
        profileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){

                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("fullname")!=null){
                        fullname =map.get("fullname").toString();
                        customer_profile_name_text.setText(fullname);
                        customer_profile_name.setText(fullname);
                    }
                    if(map.get("email")!=null){
                        email=map.get("email").toString();
                        customer_profile_email_text.setText(email);
                    }
                    if(map.get("phone")!=null){
                        phone=map.get("phone").toString();
                        customer_profile_phone_text.setText(map.get("phone").toString());
                    }
                    if(map.get("profileImageurl")!=null){
                        profileImage=map.get("profileImageurl").toString();
                        Glide.with(getActivity().getApplication()).load(profileImage).into(customer_profile_image);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
/*
        profileRef= FirebaseDatabase.getInstance().getReference("Bus Stop BD").child("Registration").child("Customer").child(customerId);

        profileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fullname=snapshot.child("fullname").getValue().toString();
                String email=snapshot.child("email").getValue().toString();
                String phone=snapshot.child("phone").getValue().toString();

                customer_profile_name_text.setText(fullname);
                customer_profile_email_text.setText(email);
                customer_profile_phone_text.setText(phone);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

    }


    private void Update() {
        fullname = customer_profile_name_edittext.getText().toString();
        phone = customer_profile_phone_edittext.getText().toString();
        email= customer_profile_email_edittext.getText().toString();

       if(fullname.isEmpty() & phone.isEmpty() & email.isEmpty())
       {
           Toast.makeText(getContext(), "Please Fillup the all field", Toast.LENGTH_SHORT).show();
       }
        else
       {
           Map userInfo = new HashMap();
           userInfo.put("fullname", fullname);
           userInfo.put("email", email);
           userInfo.put("phone", phone);
           profileRef.updateChildren(userInfo).addOnSuccessListener(new OnSuccessListener() {
               @Override
               public void onSuccess(Object o) {
                   Toast.makeText(getContext(), "Successfully", Toast.LENGTH_SHORT).show();
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Toast.makeText(getContext(), "unSuccessfully", Toast.LENGTH_SHORT).show();

               }
           });

           if(resultUri != null) {

               StorageReference filePath = FirebaseStorage.getInstance().getReference().child("Profile_Image").child("Customer").child(customerId);
               Bitmap bitmap = null;
               try {
                   bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplication().getContentResolver(), resultUri);
               }
               catch (IOException e) {
                   e.printStackTrace();
               }

               ByteArrayOutputStream baos = new ByteArrayOutputStream();
               bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
               byte[] data = baos.toByteArray();
               UploadTask uploadTask = filePath.putBytes(data);

               uploadTask.addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       getActivity().finish();
                       return;
                   }
               });
               uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                   @Override
                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       Uri downloadUrl = taskSnapshot.getUploadSessionUri();

                       Map newImage = new HashMap();
                       newImage.put("profileImageUrl", downloadUrl.toString());
                       profileRef.updateChildren(newImage);

                       getActivity().finish();
                       return;
                   }
               });
           }else{
               getActivity().finish();
           }


       }




    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            customer_profile_image.setImageURI(resultUri);
        }
    }



}
