package com.android.babbler.User;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.babbler.Adapters.MessageAdapter;
import com.android.babbler.DataClasses.Message;
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
public class ReceivedMessagesFragment extends Fragment {

    private MessageAdapter itemsAdapter;
    private ListView listView;
    private ArrayList<Message> messages=new ArrayList<Message>();

    public ReceivedMessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=  inflater.inflate(R.layout.fragment_received_messages, container, false);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //get the returned array
                    JSONArray AllSession = new JSONArray(response);
                    messages.clear();
                    for (int i = 0; i < AllSession.length(); i++) {

                        JSONObject sessionJSONObject = AllSession.getJSONObject(i);
                        int messageID = sessionJSONObject.getInt("messageID");
                        int senderID = sessionJSONObject.getInt("senderID");
                        int receiverID = sessionJSONObject.getInt("receiverID");
                        String subject = sessionJSONObject.getString("subject");
                        String content = sessionJSONObject.getString("content");
                        String time = sessionJSONObject.getString("time");
                        String date = sessionJSONObject.getString("date");
                        messages.add(new Message(messageID, senderID, receiverID, subject, content, time, date));

                    }
                    if(messages.size()>0) {
                        listView = view.findViewById(R.id.received_msg_list_view);
                        itemsAdapter = new MessageAdapter(getActivity(), messages);
                        listView.setAdapter(itemsAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                MessageFragment messageFragment = new MessageFragment(messages.get(position));
                                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                switch (User.getInstance().getM_role()){
                                    case "participant":
                                        fragmentTransaction.replace(R.id.fragment_container, messageFragment);
                                        break;
                                    case "manager":
                                        fragmentTransaction.replace(R.id.manager_fragment_container, messageFragment);
                                        break;
                                    case "moderator":
                                        fragmentTransaction.replace(R.id.moderator_fragment_container, messageFragment);
                                        break;
                                }
                                fragmentTransaction.addToBackStack("messageFragment");
                                fragmentTransaction.commit();
                            }
                        });
                    }
                    else{
                        ListView lv=view.findViewById(R.id.received_msg_list_view);
                        lv.setVisibility(View.GONE);
                        TextView textView=view.findViewById(R.id.recv_tv);
                        textView.setText("You didn't receive any message yet");
                        textView.setTextColor(getResources().getColor(R.color.error));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        //request the data in POST method with the defined url and response listener
        ReceivedMessagesRequest request = new ReceivedMessagesRequest(User.getInstance().getM_id(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);

        return view;
    }
    private class ReceivedMessagesRequest extends StringRequest {

        private static final String URL = "http://mohamadka.000webhostapp.com/ReceivedMessages.php";
        private Map<String, String> params;

        public ReceivedMessagesRequest( int uID, Response.Listener<String> listener) {
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
