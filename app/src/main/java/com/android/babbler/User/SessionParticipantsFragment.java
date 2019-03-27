package com.android.babbler.User;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.babbler.DataClasses.User;
import com.android.babbler.R;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SessionParticipantsFragment extends Fragment {

    private int selectedSessionID;
    private SessionParticipantsAdapter itemsAdapter;
    public SessionParticipantsFragment() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public SessionParticipantsFragment(int sessionID) {
        // Required empty public constructor
        this.selectedSessionID=sessionID;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view= inflater.inflate(R.layout.fragment_session_participants, container, false);
        LinearLayout layout=view.findViewById(R.id.start_session_layout);
        //if the user is not moderator hide the start session button
        if(!User.getInstance().getM_role().equals("moderator")) layout.setVisibility(View.GONE);
        else {
            layout.setVisibility(View.VISIBLE);
            //click listener for the start button
            Button start=view.findViewById(R.id.s_start);
            start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Make sure the Skype for Android client is installed.
                        if (!isSkypeClientInstalled(getContext())) {
                            goToMarket(getContext());
                            return;
                        }
                        Intent myIntent = new Intent(Intent.ACTION_VIEW);

                        // Restrict the Intent to being handled by the Skype for Android client only.
                        myIntent.setComponent(new ComponentName("com.skype.raider", "com.skype.raider.Main"));
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        // Initiate the Intent. It should never fail because you've already established the
                        // presence of its handler (although there is an extremely minute window where that
                        // handler can go away).
                        getContext().startActivity(myIntent);

                    }
            });
        }
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray AllParticipants = new JSONArray(response);
                    final ArrayList<User> users=new ArrayList<User>();
                     for (int i = 0; i < AllParticipants.length(); i++) {
                         //get the participant's information
                        JSONObject participantJSONObject = AllParticipants.getJSONObject(i);
                        int user_id = participantJSONObject.getInt("id");
                        String user_role = participantJSONObject.getString("role");
                        String user_name = participantJSONObject.getString("name");
                        String user_email = participantJSONObject.getString("email");
                        String user_birthday = participantJSONObject.getString("bdate");
                        String user_phone_number = participantJSONObject.getString("phone");
                        String user_address = participantJSONObject.getString("address");
                        int user_imageID=participantJSONObject.getInt("ImageID");
                        users.add(new User(user_id, user_role, user_name,user_email, user_birthday, user_phone_number,user_address, user_imageID));
                    }
                    if(users.size()>0) {
                        ListView listView = view.findViewById(R.id.session_participants_list_view);
                        itemsAdapter = new SessionParticipantsAdapter(getActivity(), users);
                        listView.setAdapter(itemsAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                UserProfileFragment participantProfileFragment = new UserProfileFragment(users.get(position));
                                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

                                if(User.getInstance().getM_role().equals("moderator"))
                                    fragmentTransaction.replace(R.id.moderator_fragment_container, participantProfileFragment);
                                else if(User.getInstance().getM_role().equals("participant"))
                                    fragmentTransaction.replace(R.id.fragment_container, participantProfileFragment);

                                fragmentTransaction.addToBackStack("participantProfileFragment");
                                fragmentTransaction.commit();
                            }
                        });
                    }
                    else{
                        TextView tv=view.findViewById(R.id.tv1);
                        tv.setTextColor(getResources().getColor(R.color.error));
                        tv.setText("There are no participants yet!");
                    }
                } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        };
        SessionParticipantsRequest participantsRequest = new SessionParticipantsRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(participantsRequest);


        return view;
    }

    //custom adapter
    private class SessionParticipantsAdapter extends ArrayAdapter<User> {

        ArrayList<User> participants;

        public SessionParticipantsAdapter(Activity context, ArrayList<User> participants)
        {
            super(context,0,participants);
            this.participants=participants;
        }

        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            // Check if the existing view is being reused, otherwise inflate the view
            View listItemView = convertView;
            if(listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(
                        R.layout.users_list_item, parent, false);
            }

            /**Get the {@link User} object located at this position in the list
             */
            final User currentUser=getItem(position);


            TextView email=listItemView.findViewById(R.id.p_email);
            email.setText(currentUser.getM_email());


            TextView name=listItemView.findViewById(R.id.p_name);
            name.setText(currentUser.getM_name());


            ImageView participant_img=listItemView.findViewById(R.id.p_img);
            //if there is an Image change it, else keep the default image
            if(currentUser.getM_image()>0)participant_img.setImageResource(currentUser.getM_image());


            ImageView delete=listItemView.findViewById(R.id.deleteParticipant);
            //if the user is not moderator hide the delete button
            if(!User.getInstance().getM_role().equals("moderator")){
                delete.setVisibility(View.GONE);
            }
            else {
                delete.setVisibility(View.VISIBLE);
                //click listener for delete button
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder confirm = new AlertDialog.Builder(getContext());
                        confirm.setMessage("Are you sure you want to delete this participant?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonResponse = new JSONObject(response);
                                                    boolean success = jsonResponse.getBoolean("success");
                                                    if (success) {
                                                        participants.remove(participants.get(position));
                                                        itemsAdapter.notifyDataSetChanged();
                                                        Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                                    } else
                                                        Toast.makeText(getContext(), "Error!", Toast.LENGTH_LONG).show();

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        };
                                        //request the data in POST method with the defined url and response listener
                                        DeleteRequest request = new DeleteRequest(currentUser.getM_id(), responseListener);
                                        RequestQueue queue = Volley.newRequestQueue(getContext());
                                        queue.add(request);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }

                                });
                        AlertDialog alert = confirm.create();
                        alert.setTitle("Delete Participant");
                        alert.show();
                    }
                });

            }
            return listItemView;

        }

        private class DeleteRequest extends StringRequest {

            private static final String URL = "http://mohamadka.000webhostapp.com/UnjoinSession.php";
            private Map<String, String> params;

            public DeleteRequest(int participantID, Response.Listener<String> listener) {
                super(Method.POST, URL, listener, null);
                params = new HashMap<>();
                params.put("sID", selectedSessionID+"");
                params.put("uID", participantID+"");
            }

            @Override
            public Map<String, String> getParams() {
                return params;
            }
        }


    }

    private class SessionParticipantsRequest extends StringRequest {

        private static final String LOGIN_REQUEST_URL = "https://mohamadka.000webhostapp.com/SessionParticipants.php";
        private Map<String, String> params;

        public SessionParticipantsRequest(Response.Listener<String> listener) {
            super(Method.POST, LOGIN_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("sID", selectedSessionID+"");
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }

    /**
     * Determine whether the Skype for Android client is installed on this device.
     */
    public boolean isSkypeClientInstalled(Context myContext) {
        PackageManager myPackageMgr = myContext.getPackageManager();
        try {
            myPackageMgr.getPackageInfo("com.skype.raider", PackageManager.GET_ACTIVITIES);
        }
        catch (PackageManager.NameNotFoundException e) {
            return (false);
        }
        return (true);
    }

    /**
     * Install the Skype client through the market: URI scheme.
     */
    public void goToMarket(Context myContext) {
        Uri marketUri = Uri.parse("market://details?id=com.skype.raider");
        Intent myIntent = new Intent(Intent.ACTION_VIEW, marketUri);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myContext.startActivity(myIntent);

        return;
    }

}
