package com.android.babbler.Participant;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.babbler.Adapters.SessionAdapter;
import com.android.babbler.DataClasses.Session;
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
public class ParticipantSessionsFragment extends Fragment {

    private  SessionAdapter itemsAdapter;
    private  ListView listView;
    private  ArrayList<Session> sessions=new ArrayList<Session>();
    public ParticipantSessionsFragment() {
        // Required empty public constructor
    }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            final View view=inflater.inflate(R.layout.fragment_participant_sessions, container, false);
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        //get the returned array
                        JSONArray AllSession = new JSONArray(response);
                        sessions.clear();
                        for (int i = 0; i < AllSession.length(); i++) {

                            JSONObject sessionJSONObject = AllSession.getJSONObject(i);
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
                        if(sessions.size()>0){
                            listView = view.findViewById(R.id.participant_sessions_list_view);
                            itemsAdapter = new SessionAdapter(getActivity(), sessions);
                            listView.setAdapter(itemsAdapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    SessionFragment sessionFragment = new SessionFragment(sessions.get(position),true);
                                    android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.fragment_container, sessionFragment);
                                    fragmentTransaction.addToBackStack("sessionFragment");
                                    fragmentTransaction.commit();
                                }
                            });
                        }
                        else{
                            ListView search_list_view=view.findViewById(R.id.participant_sessions_list_view);
                            search_list_view.setVisibility(View.GONE);
                            TextView textView=view.findViewById(R.id.tvP);
                            textView.setText("You're not participated in any session yet");
                            textView.setTextColor(getResources().getColor(R.color.error));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            //request the data in POST method with the defined url and response listener
            User participant=User.getInstance();
            ParticipantSessionsRequest request = new ParticipantSessionsRequest(participant.getM_id(), responseListener);
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);


            return view;
    }
    private class ParticipantSessionsRequest extends StringRequest {

        private static final String URL = "http://mohamadka.000webhostapp.com/ParticipantSessions.php";
        private Map<String, String> params;

        public ParticipantSessionsRequest(int uID, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);
            params = new HashMap<>();
            params.put("uID", uID+"");
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }

}
