package com.shamim.newbusstop.drawer_layout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shamim.newbusstop.R;
import com.shamim.newbusstop.model_class_buss;

import java.util.ArrayList;

public class Add_Information_Of_Bus_Admin extends Fragment  {
    @Nullable
    DatabaseReference addBus_info_for_Admin,addBus_Stand_for_Admin;

    EditText bus_road_permit_license,bus_license,bus_stop_start,bus_stop_end,bus_Seat,bus_stop_info;
    LinearLayout add_Fieldlayout;
    Button add_fied_information_Btn,bus_information_submit_Btn;
    long busStand_Id;
    ArrayList<model_class_buss> bussArrayList;
    int index;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_bus_info_admin, container, false);

        bus_road_permit_license= view.findViewById(R.id.bus_road_permit_license);
        bus_Seat= view.findViewById(R.id.bus_Seat);
        bus_stop_info= view.findViewById(R.id.bus_stop_info);
        add_Fieldlayout= view.findViewById(R.id.Add_fieldlayout_list);
        add_fied_information_Btn= view.findViewById(R.id.add_fied_information_Btn);
        bus_information_submit_Btn= view.findViewById(R.id.all_buses_id_submit);


        addBus_info_for_Admin = FirebaseDatabase.getInstance().getReference("Bus Stop BD").child("Bus Information From Admin");
        addBus_Stand_for_Admin = FirebaseDatabase.getInstance().getReference("Bus Stop BD").child("Bus Stoppage");

        addBus_info_for_Admin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    busStand_Id =(snapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        bus_information_submit_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Bus_road_permit_license = bus_road_permit_license.getText().toString().trim();
                String bus_sit= bus_Seat.getText().toString();
              final   int Bus_Seat= Integer.parseInt(bus_sit);
                //String Add_bus_name = add_Fieldlayout.toString();



                if (Bus_road_permit_license.isEmpty() && bus_sit.isEmpty())
                {
                    Toast.makeText(getContext(), "Please Fill up the all Field", Toast.LENGTH_SHORT).show();
                }
                else if (add_Fieldlayout.getChildCount() == 0) {

                    Toast.makeText(getContext(), "Please Add Bus Stop Name and Rent", Toast.LENGTH_SHORT).show();

                }

                else
                {
                    String admin_user_Uid = FirebaseAuth.getInstance().getUid();
                    model_class_buss add_bus_information_from_admin = new model_class_buss();

                    ArrayList<model_class_buss> businfoarray = new ArrayList<>();


                    for(int i=1;i<add_Fieldlayout.getChildCount();i++){

                        View view = add_Fieldlayout.getChildAt(i);

                        EditText editTextName = (EditText)view.findViewById(R.id.bus_stop_info);
                        EditText spinnerTeam = (EditText) view.findViewById(R.id.bus_Seat_rent);

                        bussArrayList = new ArrayList<>();

                        if(!editTextName.getText().toString().equals("") && !spinnerTeam.getText().toString().equals("")){


                            //busStandIdInfo.setBusStand_Name(editTextName.getText().toString());
                            //busStandIdInfo.setBus_Stand_Rent(spinnerTeam.getText().toString());


                        }
                        else
                        {
                            Toast.makeText(getContext(), "Please Insert Bus Stand Name And Rent", Toast.LENGTH_SHORT).show();
                        }



                        //businfoarray.add(busStandIdInfo);

                    }


                    add_bus_information_from_admin.setAdminUser_uid(admin_user_Uid);
                    add_bus_information_from_admin.setRoad_permit_license(Bus_road_permit_license);

                    String busKey= addBus_info_for_Admin.push().getKey();

                    addBus_info_for_Admin.child(busKey).setValue(add_bus_information_from_admin);

                    addBus_Stand_for_Admin.child(admin_user_Uid).setValue(businfoarray);




                }




            }
        });


        return view;





    }


    private void addView() {

        final View fieldViewer = getLayoutInflater().inflate(R.layout.add_field_bus_info,null,false);


        ImageView imageClose = (ImageView)fieldViewer.findViewById(R.id.bus_stop_iteam_remove);



        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(fieldViewer);
            }
        });


        add_Fieldlayout.addView(fieldViewer);

    }
    private void removeView(View view){

        add_Fieldlayout.removeView(view);

    }



}
