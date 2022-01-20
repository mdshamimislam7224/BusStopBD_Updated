package com.shamim.newbusstop.drawer_layout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shamim.newbusstop.Customer_MapsActivity;
import com.shamim.newbusstop.Forgotten_password;
import com.shamim.newbusstop.History_Recycleview.HistoryAdapter;
import com.shamim.newbusstop.History_Recycleview.HistoryObject;
import com.shamim.newbusstop.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class traveling_history extends Fragment implements View.OnClickListener {
    private String  userId,customerOrDriver;
    TextView toolbar_text_id;
    ImageView arraw_back_image;
    private RecyclerView mHistoryRecyclerView;
    private RecyclerView.Adapter mHistoryAdapter;
    private RecyclerView.LayoutManager mHistoryLayoutManager;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.traveling_history_layout, container, false);

        toolbar_text_id= view.findViewById(R.id.toolbar_text_id);
        arraw_back_image= view.findViewById(R.id.arraw_back_image);

       toolbar_text_id.setText("Travel For Information All Bus");

        mHistoryRecyclerView = (RecyclerView) view.findViewById(R.id.historyRecyclerView);
        mHistoryRecyclerView.setNestedScrollingEnabled(false);
        mHistoryRecyclerView.setHasFixedSize(true);
        mHistoryLayoutManager = new LinearLayoutManager(getContext());
        mHistoryRecyclerView.setLayoutManager(mHistoryLayoutManager);
        mHistoryAdapter = new HistoryAdapter(getDataSetHistory(), getContext());
        mHistoryRecyclerView.setAdapter(mHistoryAdapter);
        userId= FirebaseAuth.getInstance().getCurrentUser().getUid();



        getUserHistoryIds();

        arraw_back_image.setOnClickListener(this);
        return view;



    }

    private void getUserHistoryIds()
    {
        DatabaseReference userHistoryDatabase = FirebaseDatabase.getInstance().getReference("Bus Stop BD").child("History").child("Customer");
        userHistoryDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot history : dataSnapshot.getChildren()){
                        FetchRideInformation(history.getKey());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void FetchRideInformation(final String rideKey)
    {
        DatabaseReference historyDatabase = FirebaseDatabase.getInstance().getReference("Bus Stop BD").child("History").child("Customer").child(rideKey);
        historyDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String rideID =dataSnapshot.getKey();
                    Long timestamp = 0L;

                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if (child.getKey().equals("timestamp"))
                        {
                            timestamp = Long.valueOf(child.getValue().toString());

                        }
                    }


                    HistoryObject obj = new HistoryObject(rideID,getDate(timestamp));
                    resultsHistory.add(obj);
                    mHistoryAdapter.notifyDataSetChanged();

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private String getDate(Long timestamp)
    {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp*1000);
        String date = DateFormat.format("dd-MM-yyyy hh:mm", cal).toString();
        return date;
    }



    private ArrayList resultsHistory = new ArrayList<HistoryObject>();
    private ArrayList<HistoryObject> getDataSetHistory()
    {
        return resultsHistory;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.arraw_back_image:

                Intent login_forgotten_password = new Intent(getContext(), Customer_MapsActivity.class);
                startActivity(login_forgotten_password);
                getActivity().finish();

                break;

        }
    }
}
