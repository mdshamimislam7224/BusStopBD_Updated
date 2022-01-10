package com.shamim.newbusstop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shamim.newbusstop.Inventory_Spinner_NearBy_Place.NearBy_Place_Class;

import java.util.List;

public class Near_Place_Search_Adapter extends BaseAdapter
{
    private Context context;
    private List<NearBy_Place_Class> place_List;

    public Near_Place_Search_Adapter(Context context, List<NearBy_Place_Class> place_List) {
        this.context = context;
        this.place_List = place_List;
    }



    @Override
    public int getCount() {
        return place_List !=null ? place_List.size() :0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rootView = LayoutInflater.from(context)
                .inflate(R.layout.item_nearby_place_search,parent,false);

        TextView txtname =rootView.findViewById(R.id.spinner_Nearby_Place_Name);
        ImageView image =rootView.findViewById(R.id.spinner_Nearby_Place_Image);

        txtname.setText(place_List.get(position).getName());
        image.setImageResource(place_List.get(position).getImage());


        return rootView;
    }
}
