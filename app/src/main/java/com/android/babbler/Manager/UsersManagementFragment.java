package com.android.babbler.Manager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.babbler.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersManagementFragment extends Fragment {


    public UsersManagementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       final View view= inflater.inflate(R.layout.fragment_users_management, container, false);
        Button managers=view.findViewById(R.id.manage_managers);
        managers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                ManagersListFragment managersListFragment=new ManagersListFragment();
                fragmentTransaction.replace(R.id.manager_fragment_container,managersListFragment);
                fragmentTransaction.addToBackStack("managersListFragment");
                fragmentTransaction.commit();
            }
        });

        Button moderators=view.findViewById(R.id.manage_moderators);
        moderators.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                ModeratorsListFragment moderatorsListFragment=new ModeratorsListFragment();
                fragmentTransaction.replace(R.id.manager_fragment_container,moderatorsListFragment);
                fragmentTransaction.addToBackStack("moderatorsListFragment");
                fragmentTransaction.commit();
            }
        });

        Button participants=view.findViewById(R.id.manage_participants);
        participants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                ParticipantsListFragment participantsListFragment=new ParticipantsListFragment();
                fragmentTransaction.replace(R.id.manager_fragment_container,participantsListFragment);
                fragmentTransaction.addToBackStack("participantsListFragment");
                fragmentTransaction.commit();
            }
        });

       return view;
    }

}
