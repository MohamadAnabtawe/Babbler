package com.android.babbler.User;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.babbler.DataClasses.User;
import com.android.babbler.LoginRegister.LoginActivity;
import com.android.babbler.Manager.ManagerMainActivity;
import com.android.babbler.Moderator.ModeratorMainActivity;
import com.android.babbler.Participant.UserMainActivity;
import com.android.babbler.R;
import com.android.babbler.DataClasses.Message;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {

    private Message selectedMessage;
    private User sender;
    private Button reply;

    public MessageFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public MessageFragment(Message selectedMessage) {
        this.selectedMessage=selectedMessage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_message, container, false);
        reply=view.findViewById(R.id.reply_msg);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            //get the user's information
                            int user_id = jsonResponse.getInt("ID");
                            String user_role = jsonResponse.getString("role");
                            String user_name = jsonResponse.getString("name");
                            String user_email = jsonResponse.getString("email");
                            String user_phone_number = jsonResponse.getString("phone");
                            String user_birthday = jsonResponse.getString("bdate");
                            String user_address = jsonResponse.getString("address");
                            int user_imageID=jsonResponse.getInt("ImageID");
                            TextView from=view.findViewById(R.id.msg_sender);
                            from.setText("From: "+user_email);
                            sender=new User(user_id,user_role,user_name,user_email,user_phone_number,
                                    user_birthday,user_address,user_imageID);
                            reply.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    SendMessageFragment sendMessageFragment = new SendMessageFragment(sender);
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
                        }
                    } catch(JSONException e) {
                        e.printStackTrace();
                    }
                }
        };

        UserRequest loginRequest = new UserRequest(selectedMessage.getSenderID(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(loginRequest);

        TextView date_time=view.findViewById(R.id.msg_time_date);
        date_time.setText(selectedMessage.getDate()+", "+selectedMessage.getTime());

        TextView subject=view.findViewById(R.id.msg_subject);
        subject.setText(selectedMessage.getSubject());


        TextView content=view.findViewById(R.id.msg_content);
        content.setText(selectedMessage.getContent());

        Button delete=view.findViewById(R.id.delete_msg);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder confirm=new android.app.AlertDialog.Builder(getContext());
                confirm.setMessage("Are you sure you want to delete this message?").setCancelable(false)
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
                                                Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                                getActivity().getSupportFragmentManager().popBackStack();
                                            }
                                            else  Toast.makeText(getContext(), "Error!", Toast.LENGTH_LONG).show();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                //request the data in POST method with the defined url and response listener
                                DeleteRequest request = new DeleteRequest(selectedMessage.getMessageID(), responseListener);
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
                android.app.AlertDialog alert=confirm.create();
                alert.setTitle("Delete Message");
                alert.show();

            }
        });

        if(User.getInstance().getM_id()==selectedMessage.getSenderID())
            reply.setVisibility(View.GONE);


        return view;
    }
    private class UserRequest extends StringRequest {
        private static final String URL = "https://mohamadka.000webhostapp.com/UserByID.php";
        private Map<String, String> params;

        public UserRequest(int uID, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);
            params = new HashMap<>();
            params.put("uID", uID + "");

        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }

    }
    private class DeleteRequest extends StringRequest {

        private static final String URL = "http://mohamadka.000webhostapp.com/DeleteMessage.php";
        private Map<String, String> params;

        public DeleteRequest( int messageID, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);
            params = new HashMap<>();
            params.put("messageID", messageID+"");
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }

}
