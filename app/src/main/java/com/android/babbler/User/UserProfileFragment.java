package com.android.babbler.User;


import android.annotation.SuppressLint;
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
public class UserProfileFragment extends Fragment {

    private User participant;

    public UserProfileFragment() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public UserProfileFragment(User participant) {
        this.participant=participant;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_user_profile, container, false);
        TextView name=view.findViewById(R.id.tvName);
        name.setText(participant.getM_name());

        TextView email=view.findViewById(R.id.tvEmail);
        email.setText(participant.getM_email());

        TextView birthday=view.findViewById(R.id.tvBdate);
        birthday.setText(participant.getM_birthday());

        TextView phone=view.findViewById(R.id.tvPhone);
        phone.setText(participant.getM_phone_number());

        TextView address=view.findViewById(R.id.tvAddress);
        address.setText(participant.getM_address());

        if(participant.getM_image()>0){
            de.hdodenhof.circleimageview.CircleImageView image=view.findViewById(R.id.participantProfileImage);
            image.setImageResource(participant.getM_image());
        }

        ImageView send_msg=view.findViewById(R.id.user_send_msg);
        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                SendMessageFragment sendMessageFragment =new SendMessageFragment(participant);
                switch (User.getInstance().getM_role()){
                    case "participant":
                        fragmentTransaction.replace(R.id.fragment_container, sendMessageFragment);
                        break;
                    case "manager":
                        fragmentTransaction.replace(R.id.manager_fragment_container, sendMessageFragment);
                        break;
                    case "moderator":
                        fragmentTransaction.replace(R.id.moderator_fragment_container, sendMessageFragment);
                        break;
                }
                fragmentTransaction.addToBackStack("sendMessageFragment");
                fragmentTransaction.commit();
            }
        });
        return view;
    }

}
