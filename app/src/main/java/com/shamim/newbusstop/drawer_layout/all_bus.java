package com.shamim.newbusstop.drawer_layout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.shamim.newbusstop.R;
public class all_bus extends Fragment {
    private RecyclerView recyclerview_from_show_all_bus_fragemnt;
    TextView toolbar_text_id;
    ImageView arraw_back_image;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_bus_drawerlayout, container, false);

        toolbar_text_id= view.findViewById(R.id.toolbar_text_id);
        arraw_back_image= view.findViewById(R.id.arraw_back_image);
        recyclerview_from_show_all_bus_fragemnt=view.findViewById(R.id.recyclerview_from_show_all_bus_fragemnt);

        toolbar_text_id.setText(" Information For Traveling");


        return view;





    }

}
