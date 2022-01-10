package com.shamim.newbusstop.drawer_layout;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shamim.newbusstop.Admin_Activity;
import com.shamim.newbusstop.R;
import com.shamim.newbusstop.model_class_buss;

import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class Add_Bus_Stoppage_Of_Bus_Admin extends Fragment implements View.OnClickListener {
    @Nullable
    DatabaseReference addBus_info_for_Admin, addBus_Stand_info_from_Admin;

    ImageView bus_Image_from_admin;
    LinearLayout add_Fieldlayout;
    Button bus_stoppage_submit_Btn, add_fied_information_Btn, choose_Image_bus_from_admin;
    EditText bus_Company_name, bus_stoppage_Name_Start, bus_stoppage_Name_End, busTotal_Bus;
    String admin_user_Uid = null;
    Uri filepath;
    private StorageReference buses_Image_From_Admin;
    FirebaseAuth mAuth;
    String customerId = null;
    ProgressDialog dialog;
    ArrayList<model_class_buss> bus_Stoppage_Info_array;
     model_class_buss bus_Info,busStandIdInfo;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_bus_stoppage_admin, container, false);

        add_Fieldlayout = view.findViewById(R.id.Add_fieldlayout_list);
        bus_stoppage_submit_Btn = view.findViewById(R.id.bus_stoppage_submit_Btn);
        choose_Image_bus_from_admin = view.findViewById(R.id.choose_Image_bus_from_admin);
        bus_Company_name = view.findViewById(R.id.bus_Company_name);
        bus_stoppage_Name_Start = view.findViewById(R.id.bus_stoppage_Name_Start);
        bus_stoppage_Name_End = view.findViewById(R.id.bus_stoppage_Name_End);
        busTotal_Bus = view.findViewById(R.id.busTotal_Bus);
        bus_Image_from_admin = view.findViewById(R.id.bus_Image_from_admin);
        add_fied_information_Btn = view.findViewById(R.id.add_fied_information_Btn);

        dialog = new ProgressDialog(getContext());




        bus_stoppage_submit_Btn.setOnClickListener(this);
        choose_Image_bus_from_admin.setOnClickListener(this);
        add_fied_information_Btn.setOnClickListener(this);

        return view;


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.choose_Image_bus_from_admin:

                Intent choose = new Intent();
                choose.setType("image/*");
                choose.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(choose, "Select Image"), 1);

                break;
            case R.id.add_fied_information_Btn:

                addView();

                break;

            case R.id.bus_stoppage_submit_Btn:

                addBus_Stand_info_from_Admin = FirebaseDatabase.getInstance().getReference("Bus Stop BD").child("Bus Stoppage");
                addBus_info_for_Admin = FirebaseDatabase.getInstance().getReference("Bus Stop BD").child("Bus Information From Admin");
                buses_Image_From_Admin = FirebaseStorage.getInstance().getReference("Buses Image From Admin");


                mAuth = FirebaseAuth.getInstance();
                customerId = mAuth.getCurrentUser().getUid();
                admin_user_Uid = FirebaseAuth.getInstance().getUid();




                if (filepath != null) {

                    final String bus_Name = bus_Company_name.getText().toString().trim();
                    final String stoppage_Name_Start = bus_stoppage_Name_Start.getText().toString();
                    final String stoppage_Name_End = bus_stoppage_Name_End.getText().toString().trim();
                    final String bus_Total = busTotal_Bus.getText().toString();

                   if(bus_Name.isEmpty() || stoppage_Name_Start.isEmpty() || stoppage_Name_End.isEmpty()
                            || bus_Total.isEmpty()) {

                       Toast.makeText(getContext(), "Please Fill up All Field", Toast.LENGTH_SHORT).show();

                   }
                   else
                       {
                            busStandIdInfo = new model_class_buss();
                            bus_Info = new model_class_buss();
                           bus_Stoppage_Info_array = new ArrayList<>();

                           if (add_Fieldlayout.getChildCount() != 0) {

                           for(int i=1; i<add_Fieldlayout.getChildCount();i++){

                               View view = add_Fieldlayout.getChildAt(i);

                               EditText busStoppage_Name = (EditText) view.findViewById(R.id.bus_stop_info);
                               EditText bus_Stoppage_Rent = (EditText) view.findViewById(R.id.bus_Seat_rent);


                               if (!busStoppage_Name.getText().toString().equals("") || !bus_Stoppage_Rent.getText().toString().equals("")) {

                                   busStandIdInfo.setBusStand_Name(busStoppage_Name.getText().toString());
                                   busStandIdInfo.setBus_Stand_Rent(bus_Stoppage_Rent.getText().toString());

                               }
                               else
                               {
                                   Toast.makeText(getActivity(), "Please Insert Bus Stoppage and Rent", Toast.LENGTH_SHORT).show();
                               }

                               bus_Stoppage_Info_array.add(busStandIdInfo);
                           }

                           if (bus_Stoppage_Info_array.size()!=0) {

                               Insertdata();
                           }
                           else
                           {
                               Toast.makeText(getActivity(), "Please correctly Enter data", Toast.LENGTH_SHORT).show();
                           }



                       }
                       else {
                           Toast.makeText(getContext(), "Please Add your Bus Stoppage", Toast.LENGTH_SHORT).show();


                       }



                       }
                   }


                else {
                    Toast.makeText(getContext(), "Please Insert Image", Toast.LENGTH_SHORT).show();
                }


                break;
        }


    }



    private void Insertdata()
    {
        dialog.setTitle("Uploading");
        dialog.show();

        final StorageReference uploder = buses_Image_From_Admin.child(System.currentTimeMillis() + "." +
                getFileExtension(filepath));

        uploder.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final int busTotal = Integer.parseInt(busTotal_Bus.getText().toString());
                                bus_Info.setBuses_name_from_admin(bus_Company_name.getText().toString());
                                bus_Info.setBus_stand_start(bus_stoppage_Name_Start.getText().toString());
                                bus_Info.setBus_stand_end(bus_stoppage_Name_End.getText().toString().trim());
                                bus_Info.setTotal_Amount_Buses(busTotal);
                                bus_Info.setBuses_ImageUrl(uri.toString());

                                addBus_info_for_Admin.child(customerId).setValue(bus_Info);
                                addBus_Stand_info_from_Admin.child(customerId).setValue(bus_Stoppage_Info_array);
                                Toast.makeText(getActivity(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                bus_Stoppage_Info_array.clear();
                                Intent goAdmin = new Intent(getActivity(),Admin_Activity.class);
                                startActivity(goAdmin);
                                getActivity().finish();

                            }
                        });

                        bus_Image_from_admin.setImageResource(R.drawable.add_information_bus_admin);
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
                }, 8000);

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
                }, 8000);


                Toast.makeText(getContext(), "Uploading Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addView() {

        final View fieldViewer = getLayoutInflater().inflate(R.layout.add_field_bus_info, null, false);


        ImageView imageClose = (ImageView) fieldViewer.findViewById(R.id.bus_stop_iteam_remove);


        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(fieldViewer);
            }
        });


        add_Fieldlayout.addView(fieldViewer);

    }

    private void removeView(View view) {

        add_Fieldlayout.removeView(view);



    }

    //for Image
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            filepath = data.getData();


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filepath);
                bus_Image_from_admin.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    //Image Uploading
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


}
