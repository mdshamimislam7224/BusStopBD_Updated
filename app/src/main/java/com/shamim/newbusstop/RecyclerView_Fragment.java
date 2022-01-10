package com.shamim.newbusstop;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.shamim.newbusstop.Internet_Connection.Network_Change_Listener;


public class RecyclerView_Fragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String admin_user_Uid;
    FirebaseAuth mAuth;

    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    Adapter_For_Show_All_Buss adapter_for_show_all_buss;
    Network_Change_Listener connection;

    public RecyclerView_Fragment() {
    }


    public static RecyclerView_Fragment newInstance(String param1, String param2) {
        RecyclerView_Fragment fragment = new RecyclerView_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connection= new Network_Change_Listener();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.recycler_view_fragment, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);

        mAuth = FirebaseAuth.getInstance();
        admin_user_Uid = mAuth.getCurrentUser().getUid();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        admin_user_Uid = FirebaseAuth.getInstance().getUid();


        FirebaseRecyclerOptions<model_class_buss> options =
                new FirebaseRecyclerOptions.Builder<model_class_buss>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Bus Stop BD").child("Bus Information From Admin"), model_class_buss.class)
                        .build();

        adapter_for_show_all_buss= new Adapter_For_Show_All_Buss(options);
        recyclerView.setAdapter(adapter_for_show_all_buss);

        return view;
    }

    @Override
    public void onStart() {
        IntentFilter filter= new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getContext().registerReceiver(connection,filter);
        super.onStart();
        adapter_for_show_all_buss.startListening();
    }

    @Override
   public void onStop() {
        getContext().unregisterReceiver(connection);
        super.onStop();
        adapter_for_show_all_buss.stopListening();
    }


}