package com.android.babbler.User;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.babbler.DataClasses.User;
import com.android.babbler.R;
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
public class SendMessageFragment extends Fragment {

    private User sendTo;

    public SendMessageFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public SendMessageFragment(User sendTo) {
       this.sendTo=sendTo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_send_message, container, false);

        TextView send_to=view.findViewById(R.id.send_to);
        send_to.setText(sendTo.getM_email());

        Button cancel=view.findViewById(R.id.cancel_send);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        Button send=view.findViewById(R.id.send_msg);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Toast.makeText(getActivity(), "Message Sent Successfully",
                                        Toast.LENGTH_LONG).show();
                                getActivity().getSupportFragmentManager().popBackStack();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage("Failed, please try again")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                EditText et_subject = view.findViewById(R.id.send_subject);
                EditText et_content = view.findViewById(R.id.send_content);

                //get the string content of the edit texts
                String subject = et_subject.getText().toString();
                String content = et_content.getText().toString();


                //check if any of the fields is null
                if (subject.isEmpty() || content.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Please fill all the fields")
                            .setNegativeButton("OK", null)
                            .create()
                            .show();
                } else {

                    //request the data in POST method with the defined url and response listener
                    SendMessageRequest createSessionRequest = new SendMessageRequest(User.getInstance().getM_id(),sendTo.getM_id(),
                            subject, content, responseListener);

                    RequestQueue Queue = Volley.newRequestQueue(getActivity());
                    Queue.add(createSessionRequest);
                }
            }
        });
        return view;
    }

    private class SendMessageRequest extends StringRequest {
        private static final String URL = "https://mohamadka.000webhostapp.com/SendMessage.php";
        private Map<String, String> params;

        public SendMessageRequest(int senderID, int receiverID, String subject, String content, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);
            params = new HashMap<>();
            params.put("senderID", senderID + "");
            params.put("receiverID", receiverID+"");
            params.put("subject", subject);
            params.put("content", content);
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }

    }

}
