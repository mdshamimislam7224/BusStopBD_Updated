package com.shamim.newbusstop.drawer_layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shamim.newbusstop.Forgotten_password;
import com.shamim.newbusstop.R;
public class setting extends Fragment implements View.OnClickListener {
    Button setting_change_password;
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.setting_drawerlayout, container, false);

            setting_change_password = view.findViewById(R.id.setting_change_password);

            setting_change_password.setOnClickListener(this);
            return view;





        }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.setting_change_password:
                Intent setting_change_password = new Intent(getActivity(), Forgotten_password.class);
                startActivity(setting_change_password);
                break;
        }
    }
}
