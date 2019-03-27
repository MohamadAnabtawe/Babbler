package com.android.babbler.User;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.babbler.DataClasses.User;
import com.android.babbler.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment {


    public MessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_messages, container, false);
        Button sent = view.findViewById(R.id.sent_msg);
        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                SentMessagesFragment sentMessagesFragment =new SentMessagesFragment();
                switch (User.getInstance().getM_role()){
                    case "participant":
                        fragmentTransaction.replace(R.id.fragment_container, sentMessagesFragment);
                        break;
                    case "manager":
                        fragmentTransaction.replace(R.id.manager_fragment_container, sentMessagesFragment);
                        break;
                    case "moderator":
                        fragmentTransaction.replace(R.id.moderator_fragment_container, sentMessagesFragment);
                        break;
                }
                fragmentTransaction.addToBackStack("sentMessagesFragment");
                fragmentTransaction.commit();
            }
        });

        Button received = view.findViewById(R.id.recv_msg);
        received.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                ReceivedMessagesFragment receivedMessagesFragment =new ReceivedMessagesFragment();
                switch (User.getInstance().getM_role()){
                    case "participant":
                        fragmentTransaction.replace(R.id.fragment_container, receivedMessagesFragment);
                        break;
                    case "manager":
                        fragmentTransaction.replace(R.id.manager_fragment_container, receivedMessagesFragment);
                        break;
                    case "moderator":
                        fragmentTransaction.replace(R.id.moderator_fragment_container, receivedMessagesFragment);
                        break;
                }
                fragmentTransaction.addToBackStack("receivedMessagesFragment");
                fragmentTransaction.commit();
            }
        });
        return view;
    }

}
