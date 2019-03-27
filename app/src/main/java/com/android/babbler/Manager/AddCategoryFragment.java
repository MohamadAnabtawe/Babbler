package com.android.babbler.Manager;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class AddCategoryFragment extends Fragment {

    private static final String URL = "http://mohamadka.000webhostapp.com/AllCategories.php";
    private ArrayList<Category> categories=new ArrayList<Category>();
    private static ArrayList<String> categoryNames =new ArrayList<String>();
    private Spinner CategorySpinner;
    private Category categoryParent;
    private ArrayAdapter<String> CategoryAdapter;
    private String categoryRequestName="";

    public AddCategoryFragment() {
    }

    @SuppressLint("ValidFragment")
    public AddCategoryFragment(String categoryRequestName) {
        this.categoryRequestName=categoryRequestName;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_add_category, container, false);

        Button cancel=view.findViewById(R.id.cCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        /* fill the categories spinner*/
        CategorySpinner = view.findViewById(R.id.cParent);
        CategoryAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item,categoryNames);
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
                        categories.add(new Category(cID,cName,cParent,cIndex));
                        categoryNames.add(cName);

                    }
                    CategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    CategorySpinner.setAdapter(CategoryAdapter);
                    CategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            categoryParent=categories.get(i);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            categoryParent=categories.get(0);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        //request the data in POST method with the defined url and response listener
        StringRequest request = new StringRequest(StringRequest.Method.POST,URL, responseListener, null);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);

        EditText categoryName=view.findViewById(R.id.cName);
        if(!categoryRequestName.isEmpty())categoryName.setText(categoryRequestName);


        /*on confirm send create session request*/
        Button confirm=view.findViewById(R.id.cConfirm);
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
                                Toast.makeText(getActivity(), "Category Added Successfully",
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
                EditText categoryName=view.findViewById(R.id.cName);
                EditText et_description = view.findViewById(R.id.sDescription);


                String cName=categoryName.getText().toString();

                String cParent=categoryParent.getcID();
                int parent_cIndex=categoryParent.getcIndex();
                String cID=cParent+parent_cIndex;

                //check if any of the fields is null
                if(cName.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Please type the category's name")
                            .setNegativeButton("OK", null)
                            .create()
                            .show();
                }
                else {
                    //request the data in POST method with the defined url and response listener
                    AddCategoryRequest createSessionRequest = new AddCategoryRequest(cID, cName,cParent,responseListener);

                    RequestQueue Queue = Volley.newRequestQueue(getActivity());
                    Queue.add(createSessionRequest);
                }
            }
        });

        return view;
    }

    private class AddCategoryRequest extends StringRequest {
        private static final String URL = "https://mohamadka.000webhostapp.com/CreateCategory.php";
        private Map<String, String> params;

        public AddCategoryRequest(String cID, String cName, String cParent, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);
            params = new HashMap<>();
            params.put("cID", cID);
            params.put("cName", cName);
            params.put("cParent", cParent);
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }

    }

}
