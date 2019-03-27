package com.android.babbler.Moderator;


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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.babbler.DataClasses.Session;
import com.android.babbler.DataClasses.User;
import com.android.babbler.R;
import com.android.babbler.User.SessionParticipantsFragment;
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
public class ModeratorSessionListFragment extends Fragment {

    User member= User.getInstance();
    private ModeratorSessionAdapter itemsAdapter;
    private ListView listView;
    private ArrayList<Session> sessions=new ArrayList<Session>();

    public ModeratorSessionListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_moderator_session_list, container, false);
        LinearLayout add=view.findViewById(R.id.addSession);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateSessionFragment createSessionFragment = new CreateSessionFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.moderator_fragment_container, createSessionFragment);
                fragmentTransaction.addToBackStack("createSessionFragment");
                fragmentTransaction.commit();
            }
        });
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //get the returned array
                    JSONArray MySessions = new JSONArray(response);
                    sessions.clear();
                    for (int i = 0; i < MySessions.length(); i++) {

                        JSONObject sessionJSONObject = MySessions.getJSONObject(i);
                        int sessionID = sessionJSONObject.getInt("sID");
                        int moderatorID = sessionJSONObject.getInt("mID");
                        String category = sessionJSONObject.getString("cID");
                        String sessionTitle = sessionJSONObject.getString("sTitle");
                        String sessionDescription = sessionJSONObject.getString("sDescription");
                        String sessionDate = sessionJSONObject.getString("sDate");
                        String sessionTime = sessionJSONObject.getString("sTime");
                        int sessionDuration = sessionJSONObject.getInt("sDuration");
                        int maxParticipants = sessionJSONObject.getInt("maxParticipants");
                        int NumOfParticipants = sessionJSONObject.getInt("sNumOfParticipants");
                        String sessionLanguage = sessionJSONObject.getString("sLanguage");
                        int imageSrc = sessionJSONObject.getInt("sImageID");
                        //add the session to the sessions array list
                        sessions.add(new Session(sessionID, moderatorID, category, sessionTitle, sessionDescription, sessionDate, sessionTime, sessionDuration, maxParticipants, NumOfParticipants, sessionLanguage, imageSrc));

                    }
                    if(sessions.size()>0) {
                        listView = view.findViewById(R.id.moderator_session_list);
                        itemsAdapter = new ModeratorSessionAdapter(getActivity(), sessions);
                        listView.setAdapter(itemsAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                UpdateSessionFragment updateSessionFragment = new UpdateSessionFragment(sessions.get(position));
                                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.moderator_fragment_container, updateSessionFragment);
                                fragmentTransaction.addToBackStack("updateSessionFragment");
                                fragmentTransaction.commit();
                            }
                        });
                    }
                    else{
                        TextView tv=view.findViewById(R.id.tv2);
                        tv.setTextColor(getResources().getColor(R.color.error));
                        tv.setText("You don't have any sessions yet!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        //request the data in POST method with the defined url and response listener
        MySessionsRequest request = new MySessionsRequest(member.getM_id(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);

        return view;
    }

    private class MySessionsRequest extends StringRequest {

        private static final String URL = "http://mohamadka.000webhostapp.com/MySessions.php";
        private Map<String, String> params;

        public MySessionsRequest( int mID, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);
            params = new HashMap<>();
            params.put("mID", mID+"");
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }

    //custom adapter
    private class ModeratorSessionAdapter extends ArrayAdapter<Session> {

        ArrayList<Session> Mysessions;

        public ModeratorSessionAdapter(Activity context, ArrayList<Session> sessionList)
        {
            super(context,0,sessionList);
            this.Mysessions=sessionList;
        }

        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            // Check if the existing view is being reused, otherwise inflate the view
            View listItemView = convertView;
            if(listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(
                        R.layout.moderator_sessions_list, parent, false);
            }

            /**Get the {@link Session} object located at this position in the list
             */
            final Session currentSession=getItem(position);


            TextView title_tv=listItemView.findViewById(R.id.title_tv);
            title_tv.setText(currentSession.getSessionTitle());


            TextView time_tv=listItemView.findViewById(R.id.time_tv);
            time_tv.setText(currentSession.getSessionTime());


            ImageView session_img=listItemView.findViewById(R.id.m_img);
            //if there is an Image change it, else keep the default image
            if(currentSession.getImageID()>0)session_img.setImageResource(currentSession.getImageID());
            else{
                session_img.setImageResource(R.drawable.b);
            }

            //on click got to list participants of the current session
            ImageView sessionParticipants=listItemView.findViewById(R.id.sessionParticipants);
            sessionParticipants.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.support.v4.app.FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                    SessionParticipantsFragment sessionParticipantsFragment = new SessionParticipantsFragment(currentSession.getSessionID());
                    fragmentTransaction.replace(R.id.moderator_fragment_container, sessionParticipantsFragment);
                    fragmentTransaction.addToBackStack("sessionParticipantsFragment");
                    fragmentTransaction.commit();
                }
            });
            //click listener for delete button
            ImageView delete=listItemView.findViewById(R.id.deleteSession);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder confirm=new AlertDialog.Builder(getContext());
                    confirm.setMessage("Are you sure you want to delete this session?").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonResponse = new JSONObject(response);
                                                boolean success = jsonResponse.getBoolean("success");
                                                if(success)
                                                {
                                                    Mysessions.remove(sessions.get(position));
                                                    itemsAdapter.notifyDataSetChanged();
                                                    Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                                }
                                                else  Toast.makeText(getContext(), "Error!", Toast.LENGTH_LONG).show();

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };
                                    //request the data in POST method with the defined url and response listener
                                    DeleteRequest request = new DeleteRequest(currentSession.getSessionID(), responseListener);
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
                    AlertDialog alert=confirm.create();
                    alert.setTitle("Delete Session");
                    alert.show();

                }
            });
            return listItemView;

        }
        private class DeleteRequest extends StringRequest {

            private static final String URL = "http://mohamadka.000webhostapp.com/DeleteSession.php";
            private Map<String, String> params;

            public DeleteRequest( int sessionID, Response.Listener<String> listener) {
                super(Method.POST, URL, listener, null);
                params = new HashMap<>();
                params.put("sID", sessionID+"");
            }

            @Override
            public Map<String, String> getParams() {
                return params;
            }
        }

    }

}
