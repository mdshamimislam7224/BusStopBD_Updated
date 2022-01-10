package com.shamim.newbusstop.drawer_layout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shamim.newbusstop.Bus_Model_Class;
import com.shamim.newbusstop.Model_class_Registration;
import com.shamim.newbusstop.R;

import java.util.ArrayList;
import java.util.List;

public class Contact_customer extends Fragment {
    ImageSlider mainSlider;
    TextView bus_name_contact1;
    Spinner contactspinner;
    Button showdata_spinner_contact;
    ListView businfoListView;

    DatabaseReference contactRef, contactRefspinner;
    private ValueEventListener mListener;
    ArrayList<String> arrayList, listviewarraylist;
    ArrayAdapter<String> adapter;
        FirebaseListAdapter listviewadapter;
        String TAG = "Contact_customer";

    public List<Bus_Model_Class> mDatalist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_drawerlayout, container, false);

        contactspinner = view.findViewById(R.id.contactspinner);
        businfoListView = view.findViewById(R.id.businfoListView);
        showdata_spinner_contact = view.findViewById(R.id.showdata_spinner_contact);


        mainSlider = view.findViewById(R.id.image_slider);

        arrayList = new ArrayList<String>();
        listviewarraylist = new ArrayList<String>();

        String customerUid = FirebaseAuth.getInstance().getUid();


        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, arrayList);
        Query query = FirebaseDatabase.getInstance().getReference("Bus Stop BD").child("Registration").child("Admin").child(customerUid);

        FirebaseListOptions <Model_class_Registration> options = new FirebaseListOptions.Builder<Model_class_Registration>()
                .setLayout(R.layout.contact_item)
                .setQuery(query,Model_class_Registration.class).build();

        listviewadapter = new FirebaseListAdapter(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Object model, int position) {

                TextView email = v.findViewById(R.id.contact_email);
                TextView phone = v.findViewById(R.id.contact_phone);
                TextView road_permit_License = v.findViewById(R.id.contact_road_permit_license);

                Model_class_Registration class_registration = (Model_class_Registration) model;

                email.setText(class_registration.getEmail().toString());
                phone.setText(class_registration.getPhone().toString());
                road_permit_License.setText(class_registration.getRoad_permit_License().toString());



            }
        };
        businfoListView.setAdapter(listviewadapter);

        //listviewadapter.startListening();
        contactspinner.setAdapter(adapter);
        //businfoListView.setAdapter(listviewadapter);




        contactRef = FirebaseDatabase.getInstance().getReference("Bus Stop BD").child("Registration").child("Admin");
        contactRefspinner = FirebaseDatabase.getInstance().getReference("Bus Stop BD").child("Registration").child("Admin");


        mDatalist = new ArrayList<>();

        final List<SlideModel> remoteimage = new ArrayList<>();

        contactRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {

                    remoteimage.add(new SlideModel(data.child("profileImageurl").getValue().toString(), data.child("email").getValue().toString(), ScaleTypes.FIT));
                    mainSlider.setImageList(remoteimage, ScaleTypes.FIT);
                    mainSlider.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onItemSelected(int i) {

                            if (i < 1) {
                                switch (i) {
                                    case 0:
                                        Toast.makeText(getContext(), "Pic1", Toast.LENGTH_SHORT).show();
                                    case 1:
                                        Toast.makeText(getContext(), "Pic2", Toast.LENGTH_SHORT).show();
                                    case 2:
                                        Toast.makeText(getContext(), "Pic3", Toast.LENGTH_SHORT).show();


                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Rectrive_data_Spinner();
        //show_data_Spinner();

        showdata_spinner_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              final String Spinner_data = contactspinner.getSelectedItem().toString();
                //final String Spinner_data = String.valueOf(contactspinner.getSelectedItemPosition());
                if (Spinner_data != null) {

                    final Model_class_Registration busNameModel = new Model_class_Registration();


                    contactRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot data : snapshot.getChildren()) {

                            int index =0;
                            if (index <0)
                            {
                              switch (index)

                              {

                              }

                            }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                else {
                    Toast.makeText(getContext(), "Please Select The Bus", Toast.LENGTH_SHORT).show();
                }


            }
        });

        return view;


    }

    public void Rectrive_data_Spinner() {
        mListener = contactRefspinner.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    arrayList.add(data.child("bus_name").getValue(String.class));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void show_data_Spinner() {
        mListener = contactRefspinner.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("Spinner_value=" + " ", String.valueOf(snapshot.getValue()));
                arrayList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                   // listviewarraylist.add(data.getValue(String.class).toString());
                    listviewarraylist.add(data.child("bus_name").getValue(String.class));

                    listviewadapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}

