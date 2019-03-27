package com.android.babbler.Moderator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.babbler.DataClasses.Category;
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
public class CreateSessionFragment extends Fragment {

    private static final String URL = "http://mohamadka.000webhostapp.com/AllCategories.php";
    private ArrayList<Category> categories = new ArrayList<Category>();
    private static ArrayList<String> categoryNames = new ArrayList<String>();
    private Spinner CategorySpinner;
    private String selectedCategory;
    private ArrayAdapter<String> CategoryAdapter;
    private Spinner LanguageSpinner;
    private String selectedLanguage;
    private ArrayAdapter<String> LanguageAdapter;
    private int SessionImageID;

    public CreateSessionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_create_session, container, false);
        Button cancel = view.findViewById(R.id.sCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        /* fill the language spinner*/
        LanguageSpinner = view.findViewById(R.id.lang_spinner);
        LanguageAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item, getResources().getStringArray(R.array.languages));

        LanguageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        LanguageSpinner.setAdapter(LanguageAdapter);
        LanguageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLanguage = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedLanguage = (String) adapterView.getItemAtPosition(0);
            }
        });

        /* fill the categories spinner*/
        CategorySpinner = view.findViewById(R.id.sCategory);
        CategoryAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item, categoryNames);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //get the returned array
                    JSONArray AllCategories = new JSONArray(response);
                    categories.clear();
                    categoryNames.clear();
                    for (int i = 0; i < AllCategories.length(); i++) {

                        JSONObject categoryJSONObject = AllCategories.getJSONObject(i);
                        String cID = categoryJSONObject.getString("cID");
                        String cName = categoryJSONObject.getString("cName");
                        String cParent = categoryJSONObject.getString("cParent");
                        int cIndex = categoryJSONObject.getInt("cIndex");
                        categories.add(new Category(cID, cName, cParent, cIndex));
                        categoryNames.add(cName);

                    }
                    CategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    CategorySpinner.setAdapter(CategoryAdapter);
                    CategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            selectedCategory = categories.get(i).getcID();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            selectedCategory = categories.get(0).getcID();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        //request the data in POST method with the defined url and response listener
        StringRequest request = new StringRequest(StringRequest.Method.POST, URL, responseListener, null);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);

        /*on confirm send create session request*/
        Button confirm = view.findViewById(R.id.sConfirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send request
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Toast.makeText(getActivity(), "Session Added Successfully",
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
                EditText et_title = view.findViewById(R.id.sTitle);
                EditText et_description = view.findViewById(R.id.sDescription);
                EditText et_date = view.findViewById(R.id.sDate);
                EditText et_maxParticipants = view.findViewById(R.id.sMax);
                EditText et_time = view.findViewById(R.id.sTime);
                EditText et_duration = view.findViewById(R.id.sDuration);
                //get the string content of the edit texts
                String title = et_title.getText().toString();
                String description = et_description.getText().toString();
                String date = et_date.getText().toString();
                String maxParticipants = et_maxParticipants.getText().toString();
                String time = et_time.getText().toString();
                String duration = et_duration.getText().toString();
                User moderator = User.getInstance();
                int mID = moderator.getM_id();

                //check if any of the fields is null
                if (title.isEmpty() || description.isEmpty() || date.isEmpty() || maxParticipants.isEmpty() || time.isEmpty() || duration.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Please fill all the fields")
                            .setNegativeButton("OK", null)
                            .create()
                            .show();
                } else {
                    SessionImageID();
                    int sDuration = Integer.parseInt(duration);
                    int sMax = Integer.parseInt(maxParticipants);
                    //request the data in POST method with the defined url and response listener
                    CreateSessionRequest createSessionRequest = new CreateSessionRequest(mID, selectedCategory, title, description, date, time,
                            sDuration, sMax, selectedLanguage, SessionImageID, responseListener);

                    RequestQueue Queue = Volley.newRequestQueue(getActivity());
                    Queue.add(createSessionRequest);
                }
            }
        });

        return view;
    }


    private class CreateSessionRequest extends StringRequest {
        private static final String URL = "https://mohamadka.000webhostapp.com/CreateSession.php";
        private Map<String, String> params;

        public CreateSessionRequest(int mID, String cID, String sTitle, String sDescription, String sDate, String sTime, int sDuration,
                                    int maxParticipants, String sLanguage, int sImageID, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);
            params = new HashMap<>();
            params.put("mID", mID + "");
            params.put("cID", cID);
            params.put("sTitle", sTitle);
            params.put("sDescription", sDescription);
            params.put("sDate", sDate);
            params.put("sTime", sTime);
            params.put("sDuration", sDuration + "");
            params.put("maxParticipants", maxParticipants + "");
            params.put("sLanguage", sLanguage);
            params.put("sImageID", sImageID + "");
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }

    }

    private void SessionImageID() {
        char c1 = selectedCategory.charAt(0) ,c2=' ';
        if(selectedCategory.length()>1) c2 = selectedCategory.charAt(1);
        switch (c1) {
            //Nature
            case '1':
                switch (c2) {
                    //Animals
                    case '1':
                    SessionImageID=R.drawable.animals;
                    break;

                    //Food & Beverages
                    case '2':
                        SessionImageID=R.drawable.food;
                    break;
                }
                break;
            default:
                SessionImageID=R.drawable.nature;
                break;

            //Health
            case '2':
                SessionImageID=R.drawable.health;
                break;

            //Sport
            case '3':
                SessionImageID=R.drawable.sport;
                break;

            //Entertainment
            case '4':
                SessionImageID=R.drawable.entertainment;
                break;

            //Art
            case '5':
                SessionImageID=R.drawable.art;
                break;

            //Education
            case '6':
                SessionImageID=R.drawable.education;
                break;

            //Technology
            case '7':
                SessionImageID=R.drawable.technology;
                break;

            //Network
            case '8':
                SessionImageID=R.drawable.social_media;
                break;

            //Social
            case '9':
                switch (c2) {

                    //Politics
                    case '1':
                        SessionImageID=R.drawable.politics;
                        break;

                    //Economy
                    case '2':
                        SessionImageID=R.drawable.economy;
                        break;

                    default:
                        SessionImageID=R.drawable.social;
                        break;
                }
                break;
        }

    }

}
