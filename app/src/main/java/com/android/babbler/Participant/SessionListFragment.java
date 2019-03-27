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
public class SessionListFragment extends Fragment {

    private  SessionAdapter itemsAdapter;
    private  ListView listView;
    private  ArrayList<Session> sessions=new ArrayList<Session>();
    public SessionListFragment()
    {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view=inflater.inflate(R.layout.fragment_session_list, container, false);

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
                    if(sessions.size()==0){
                        ListView lv=view.findViewById(R.id.session_list_view);
                        lv.setVisibility(View.GONE);
                        TextView textView=view.findViewById(R.id.tvlist);
                        textView.setText("Sorry, we don't have any available sessions yet, please try again later...");
                        textView.setTextColor(getResources().getColor(R.color.error));
                        TextView tv=view.findViewById(R.id.requestCategory);
                        tv.setVisibility(View.GONE);
                    }
                    else {
                        listView = view.findViewById(R.id.session_list_view);
                        itemsAdapter = new SessionAdapter(getActivity(), sessions);
                        listView.setAdapter(itemsAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                SessionFragment sessionFragment = new SessionFragment(sessions.get(position), false);
                                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_container, sessionFragment);
                                fragmentTransaction.addToBackStack("session");
                                fragmentTransaction.commit();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

       //request the data in POST method with the defined url and response listener
        MySessionsRequest request = new MySessionsRequest(User.getInstance().getM_id(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);

        TextView requestCategory = view.findViewById(R.id.requestCategory);
        requestCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SuggestCategoryFragment suggestCategoryFragment = new SuggestCategoryFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, suggestCategoryFragment);
                fragmentTransaction.addToBackStack("suggestCategoryFragment");
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    private class MySessionsRequest extends StringRequest {

        private static final String URL = "http://mohamadka.000webhostapp.com/AllSessions.php";
        private Map<String, String> params;

        public MySessionsRequest( int uID, Response.Listener<String> listener) {
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
