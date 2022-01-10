package com.shamim.newbusstop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class Adapter_For_Show_All_Buss extends FirebaseRecyclerAdapter<model_class_buss,Adapter_For_Show_All_Buss.ViewHolder_For_Show_All_Buss> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public Adapter_For_Show_All_Buss(@NonNull FirebaseRecyclerOptions<model_class_buss> options) {
        super(options);
    }




    @Override
    protected void onBindViewHolder(@NonNull ViewHolder_For_Show_All_Buss holder, int position, @NonNull model_class_buss model)
    {
        holder.single_row_bus_Name.setText(model.getBuses_name_from_admin());
        holder.single_row_BusStand_Name_Start.setText(model.getBus_stand_start());
        holder.single_row_BusStand_Name_End.setText(model.getBus_stand_end());
        Glide.with(holder.single_row_image.getContext()).load(model.getBuses_ImageUrl()).into(holder.single_row_image);

    }

    @NonNull
    @Override
    public ViewHolder_For_Show_All_Buss onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_for_allbusses_recycler,parent,false);


        return new ViewHolder_For_Show_All_Buss(view);
    }







    public class  ViewHolder_For_Show_All_Buss extends RecyclerView.ViewHolder
    {
        ImageView single_row_image;
        TextView single_row_bus_Name,single_row_BusStand_Name_Start,single_row_BusStand_Name_End;
        public ViewHolder_For_Show_All_Buss(@NonNull View itemView) {

            super(itemView);
            single_row_image =itemView.findViewById(R.id.single_row_image);
            single_row_bus_Name =itemView.findViewById(R.id.single_row_bus_Name);
            single_row_BusStand_Name_Start =itemView.findViewById(R.id.single_row_BusStand_Name_Start);
            single_row_BusStand_Name_End =itemView.findViewById(R.id.single_row_BusStand_Name_End);


        }
    }
}
