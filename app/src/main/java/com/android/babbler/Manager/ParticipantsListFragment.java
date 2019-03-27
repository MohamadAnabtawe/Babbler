package com.android.babbler.Manager;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.babbler.DataClasses.User;
import com.android.babbler.R;
import com.android.babbler.User.UserProfileFragment;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ParticipantsListFragment extends Fragment {

    private static final String URL = "http://mohamadka.000webhostapp.com/AllParticipants.php";
    private ParticipantsAdapter itemsAdapter;
    public ParticipantsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_participants_list, container, false);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray AllParticipants = new JSONArray(response);
                    final ArrayList<User> users=new ArrayList<User>();
                    for (int i = 0; i < AllParticipants.length(); i++) {
                        //get the manager's information
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
                        ListView listView = view.findViewById(R.id.participants_list_view);
                        itemsAdapter = new ParticipantsAdapter(getActivity(), users);
                        listView.setAdapter(itemsAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                UserProfileFragment userProfileFragment = new UserProfileFragment(users.get(position));
                                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.manager_fragment_container, userProfileFragment);
                                fragmentTransaction.addToBackStack("userProfileFragment");
                                fragmentTransaction.commit();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        StringRequest request = new StringRequest(StringRequest.Method.POST,URL, responseListener, null);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
        return view;
    }

    //custom adapter
    private class ParticipantsAdapter extends ArrayAdapter<User> {

        ArrayList<User> participants;

        public ParticipantsAdapter(Activity context, ArrayList<User> participants) {
            super(context, 0, participants);
            this.participants = participants;
        }

        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            // Check if the existing view is being reused, otherwise inflate the view
            View listItemView = convertView;
            if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(
                        R.layout.users_list_item, parent, false);
            }

            /**Get the {@link User} object located at this position in the list
             */
            final User currentUser = getItem(position);


            TextView email = listItemView.findViewById(R.id.p_email);
            email.setText(currentUser.getM_email());


            TextView name = listItemView.findViewById(R.id.p_name);
            name.setText(currentUser.getM_name());


            ImageView participant_img = listItemView.findViewById(R.id.p_img);
            //if there is an Image change it, else keep the default image
            if (currentUser.getM_image() > 0)
                participant_img.setImageResource(currentUser.getM_image());


            ImageView delete = listItemView.findViewById(R.id.deleteParticipant);
            //click listener for delete button
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder confirm = new AlertDialog.Builder(getContext());
                    confirm.setMessage("Are you sure you want to delete this manager?").setCancelable(false)
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
                                    DeleteUserRequest request = new DeleteUserRequest(currentUser.getM_id(), responseListener);
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

            return listItemView;

        }
    }

}
