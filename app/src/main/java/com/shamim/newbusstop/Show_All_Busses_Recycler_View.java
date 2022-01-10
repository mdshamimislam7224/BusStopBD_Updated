package com.shamim.newbusstop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.shamim.newbusstop.Internet_Connection.Network_Change_Listener;
import com.shamim.newbusstop.drawer_layout.all_bus;

public class Show_All_Busses_Recycler_View extends AppCompatActivity {
    ConstraintLayout RecyclerView;
    Network_Change_Listener connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__all__busses__recycler__view);

        connection = new Network_Change_Listener();

        RecyclerView= findViewById(R.id.RecyclerView);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportFragmentManager().beginTransaction().replace(R.id.RecyclerView,new RecyclerView_Fragment()).commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem item=menu.findItem(R.id.search_item);
        SearchView searchView =(SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

               processsearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                processsearch(query);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void processsearch(String query) {

        FirebaseRecyclerOptions<model_class_buss> options =
                new FirebaseRecyclerOptions.Builder<model_class_buss>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Bus Stop BD").child("Bus Information From Admin"), model_class_buss.class)
                        .build();

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