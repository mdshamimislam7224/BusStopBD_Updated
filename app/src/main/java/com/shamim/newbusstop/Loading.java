package com.shamim.newbusstop;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

class Loading {
     Activity activity;
   AlertDialog alertDialog;
    Loading(Activity myactivity){
        activity=myactivity;

    }
    void  startLoading()
    {
       AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog,null));
        builder.setCancelable(false);
        alertDialog =builder.create();
        alertDialog.show();

    }
    void dismissDialog()
    {
        alertDialog.dismiss();
    }

}
