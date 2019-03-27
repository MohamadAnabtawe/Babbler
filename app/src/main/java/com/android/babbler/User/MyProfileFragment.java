package com.android.babbler.User;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.babbler.DataClasses.User;
import com.android.babbler.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {

    TextView name,email,password,birthday,phone,address;
    ImageView ProfileImage;
    Button changePassword;
    User user= User.getInstance();

    public MyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_my_profile, container, false);
        name=view.findViewById(R.id.tvName);
        name.setText(user.getM_name());
        email=view.findViewById(R.id.tvEmail);
        email.setText(user.getM_email());
        birthday=view.findViewById(R.id.tvBdate);
        birthday.setText(user.getM_birthday());
        phone=view.findViewById(R.id.tvPhone);
        phone.setText(user.getM_phone_number());
        address=view.findViewById(R.id.tvAddress);
        address.setText(user.getM_address());
        changePassword=view.findViewById(R.id.changePass);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),ChangePasswordActivity.class));
            }
        });
        return view;
    }

}
