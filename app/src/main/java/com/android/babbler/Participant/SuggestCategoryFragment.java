package com.android.babbler.Participant;


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
public class SuggestCategoryFragment extends Fragment {

    TextView category;
    public SuggestCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_suggest_category, container, false);
        category=view.findViewById(R.id.catReq);
        Button cancel=view.findViewById(R.id.catReqCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        Button confirm=view.findViewById(R.id.catReqConfirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String category_name = category.getText().toString();
                final int user_id = User.getInstance().getM_id();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Toast.makeText(getActivity(), "Category suggestion was sent,"+"\n"+"   waiting for admin approval",
                                        Toast.LENGTH_LONG).show();
                                getActivity().getSupportFragmentManager().popBackStack();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage("Category suggestion failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                CategoryRequest categoryRequest = new CategoryRequest(category_name, user_id, responseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(categoryRequest);
            }
        });
        return view;
    }
    private class CategoryRequest extends StringRequest {
        private static final String SUGGEST_CATEGORY_REQUEST_URL = "https://mohamadka.000webhostapp.com/SuggestCategory.php";
        private Map<String, String> params;

        public CategoryRequest(String category, int user_id, Response.Listener<String> listener) {
            super(Method.POST, SUGGEST_CATEGORY_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("category", category);
            params.put ("user_id", user_id +"");
        }
        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }

}
