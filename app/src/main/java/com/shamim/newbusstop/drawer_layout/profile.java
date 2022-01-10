package com.shamim.newbusstop.drawer_layout;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shamim.newbusstop.Home;
import com.shamim.newbusstop.R;

public class profile extends Fragment implements View.OnClickListener {
    ImageView profile_image;
    TextView profile_fullname,profile_email,profile_phone,profile_license,profile_nid,profile_company;
    Button profile_edit, profile_change_password, profile_back;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_drawerlayout, container, false);


        profile_fullname = view.findViewById(R.id.profile_fullname);
        profile_email = view.findViewById(R.id.profile_email);
        profile_phone = view.findViewById(R.id.profile_phone);
        profile_license = view.findViewById(R.id.profile_license);
        profile_nid = view.findViewById(R.id.profile_nid);
        profile_company = view.findViewById(R.id.profile_company);
        profile_edit = view.findViewById(R.id.profile_edit);
        profile_change_password = view.findViewById(R.id.profile_change_password);


        profile_back = view.findViewById(R.id.profile_back);
        profile_image = view.findViewById(R.id.profile_image);
        profile_edit.setOnClickListener(this);
        profile_change_password.setOnClickListener(this);
        profile_back.setOnClickListener(this);
        return view;





    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_change_password:
                Intent intent = new Intent(getActivity(), Home.class);
                startActivity(intent);
                break;


            case R.id.profile_edit:

                break;

            case R.id.profile_back:
                Intent profile_back = new Intent(getActivity(), Home.class);
                startActivity(profile_back);

                break;
        }

    }


}
