package com.android.babbler.Participant;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SessionFragment extends Fragment {

    private Session selectedSession;
    private  boolean isParticipated=false;
    public SessionFragment() {

    }
    @SuppressLint("ValidFragment")
    public SessionFragment(Session session,boolean isParticipated) {
        // Required empty public constructor
        super();
        this.selectedSession=session;
        this.isParticipated=isParticipated;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_session, container, false);

        ImageView image=view.findViewById(R.id.sessionImage);
        if(selectedSession.getImageID()>0)image.setImageResource(selectedSession.getImageID());
        else{
            image.setImageResource(R.drawable.b);
        }

        TextView title=view.findViewById(R.id.tvTitle);
        title.setText(selectedSession.getSessionTitle());

        TextView description=view.findViewById(R.id.tvDescription);
        description.setText(selectedSession.getSessionDescription());


        TextView Date=view.findViewById(R.id.tvDate);
        Date.setText(selectedSession.getSessionDate());

        TextView time=view.findViewById(R.id.tvTime);
        time.setText(selectedSession.getSessionTime());

        TextView duration=view.findViewById(R.id.tvDuration);
        duration.setText(selectedSession.getSessionDuration()+" min");

        TextView language=view.findViewById(R.id.tvLanguage);
        language.setText(selectedSession.getSessionLanguage());

        Button btn_session_participants=view.findViewById(R.id.btn_session_participants);
        btn_session_participants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                SessionParticipantsFragment sessionParticipantsFragment = new SessionParticipantsFragment(selectedSession.getSessionID());
                fragmentTransaction.replace(R.id.fragment_container, sessionParticipantsFragment);
                fragmentTransaction.addToBackStack("sessionParticipantsFragment");
                fragmentTransaction.commit();
            }
        });
        Button btn=view.findViewById(R.id.btnJoin);

        if(isParticipated){
            btn.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.card_item_error, null));
            btn.setText("Unjoin Session");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder confirm=new AlertDialog.Builder(getContext());
                    confirm.setMessage("Are you sure you want to leave this session?").setCancelable(false)
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
                                                    Toast.makeText(getContext(), "You're unjoined", Toast.LENGTH_SHORT).show();

                                                else  Toast.makeText(getContext(), "Error!", Toast.LENGTH_LONG).show();
                                                getActivity().getSupportFragmentManager().popBackStack();

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };
                                    //request the data in POST method with the defined url and response listener
                                    UnjoinSessionRequest request = new UnjoinSessionRequest(User.getInstance().getM_id(),
                                            selectedSession.getSessionID(), responseListener);
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
                    alert.setTitle("Unjoin Session");
                    alert.show();
                }
            });
        }
        else{
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonResponse = new JSONObject(response);
                                                boolean success = jsonResponse.getBoolean("success");
                                                if(success)
                                                    Toast.makeText(getContext(), "Successfully Joined", Toast.LENGTH_SHORT).show();

                                                else  Toast.makeText(getContext(), "Error!", Toast.LENGTH_LONG).show();
                                                getActivity().getSupportFragmentManager().popBackStack();

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };
                                    //request the data in POST method with the defined url and response listener
                                    JoinSessionRequest request = new JoinSessionRequest(User.getInstance().getM_id(),
                                            selectedSession.getSessionID(), responseListener);
                                    RequestQueue queue = Volley.newRequestQueue(getContext());
                                    queue.add(request);

                }
            });
        }
        return view;
    }
    private class JoinSessionRequest extends StringRequest {

        private static final String URL = "http://mohamadka.000webhostapp.com/JoinSession.php";
        private Map<String, String> params;

        public JoinSessionRequest(int uID,int sID, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);
            params = new HashMap<>();
            params.put("uID", uID+"");
            params.put("sID", sID+"");
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }
    private class UnjoinSessionRequest extends StringRequest {

        private static final String URL = "http://mohamadka.000webhostapp.com/UnjoinSession.php";
        private Map<String, String> params;

        public UnjoinSessionRequest(int uID,int sID, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);
            params = new HashMap<>();
            params.put("uID", uID+"");
            params.put("sID", sID+"");
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }
}
