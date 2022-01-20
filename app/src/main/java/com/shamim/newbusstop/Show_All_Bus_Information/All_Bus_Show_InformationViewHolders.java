package com.shamim.newbusstop.Show_All_Bus_Information;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shamim.newbusstop.HistoryViewActivity;
import com.shamim.newbusstop.R;


/**
 * Created by manel on 10/10/2017.
 */

public class All_Bus_Show_InformationViewHolders extends RecyclerView.ViewHolder{

    private TextView bus_name_all_bus,start_From_bus,end_From_bus;
    private ImageView show_all_bus_fragment_image;

    public All_Bus_Show_InformationViewHolders(@NonNull View itemView) {
        super(itemView);

        bus_name_all_bus =  itemView.findViewById(R.id.bus_name_all_bus_info);
        start_From_bus =  itemView.findViewById(R.id.start_From_bus_info);
        end_From_bus =  itemView.findViewById(R.id.end_From_bus);
        show_all_bus_fragment_image =  itemView.findViewById(R.id.show_all_bus_fragment_image);
    }



}
