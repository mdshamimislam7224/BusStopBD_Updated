package com.shamim.newbusstop.Internet_Connection;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import com.shamim.newbusstop.R;

public class Network_Change_Listener extends BroadcastReceiver {

    AlertDialog dialog;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Communication.isconnectedInternet(context)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View layout_dialog = LayoutInflater.from(context).inflate(R.layout.internet_check_layout, null);
            builder.setView(layout_dialog);

            AppCompatButton internet_checkBtn = layout_dialog.findViewById(R.id.internet_checkBtn);

            dialog = builder.create();
            dialog.show();
            dialog.setCancelable(false);
            dialog.getWindow().setGravity(Gravity.CENTER);


            internet_checkBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    onReceive(context, intent);

                }
            });
        }


    }
}
