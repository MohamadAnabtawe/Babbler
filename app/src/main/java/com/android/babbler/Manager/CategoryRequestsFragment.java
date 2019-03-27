package com.android.babbler.Manager;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.babbler.DataClasses.RequestCategory;
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


public class CategoryRequestsFragment extends Fragment {

    private static final String URL = "http://mohamadka.000webhostapp.com/CategoryRequests.php";
    private CategoryRequestAdapter itemsAdapter;
    private ListView listView;
    private ArrayList<RequestCategory> categoryRequests=new ArrayList<RequestCategory>();

    public CategoryRequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view= inflater.inflate(R.layout.fragment_category_requests, container, false);
        Button addCategory=view.findViewById(R.id.add_category);
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCategoryFragment addCategoryFragment = new AddCategoryFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.manager_fragment_container, addCategoryFragment);
                fragmentTransaction.addToBackStack("addCategoryFragment");
                fragmentTransaction.commit();
            }
        });
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //get the returned array
                    JSONArray AllRequests = new JSONArray(response);
                    categoryRequests.clear();
                    for (int i = 0; i < AllRequests.length(); i++) {
                        JSONObject requestsJSONObject = AllRequests.getJSONObject(i);
                        int rID = requestsJSONObject.getInt("rID");
                        int userID = requestsJSONObject.getInt("userID");
                        String category = requestsJSONObject.getString("category");
                        //add the session to the sessions array list
                        categoryRequests.add(new RequestCategory(rID, userID, category));

                    }
                    if(categoryRequests.size()>0){
                        listView = view.findViewById(R.id.requests_list_view);
                        itemsAdapter = new CategoryRequestAdapter(getActivity(), categoryRequests);
                        listView.setAdapter(itemsAdapter);
                    }
                    else{
                        TextView tv=view.findViewById(R.id.catReqTv);
                        tv.setTextColor(getResources().getColor(R.color.error));
                        tv.setText("There are no requests!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        //request the data in POST method with the defined url and response listener
        StringRequest request = new StringRequest(StringRequest.Method.POST,URL, responseListener, null);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
        return view;
    }
    private class CategoryRequestAdapter extends ArrayAdapter<RequestCategory> {

        private RequestCategory currentRequest;
        private TextView categoryName;
        private TextView rID;
        private ImageView addRequest;
        private ImageView deleteRequest;
        private ArrayList<RequestCategory> category_requests;
        public CategoryRequestAdapter(Activity context, ArrayList<RequestCategory> categoryRequests){
            super(context,0,categoryRequests);
            this.category_requests=categoryRequests;
        }

        public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
            // Check if the existing view is being reused, otherwise inflate the view
            View listItemView = convertView;
            if(listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(
                        R.layout.category_request_item, parent, false);
            }

            currentRequest=getItem(position);

            rID=listItemView.findViewById(R.id.requestID);
            rID.setText("request ID: "+currentRequest.getrID());

            categoryName=listItemView.findViewById(R.id.category_request_tv);
            categoryName.setText(currentRequest.getCategory());

            addRequest= listItemView.findViewById(R.id.addRequest);
            addRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddCategoryFragment addCategoryFragment = new AddCategoryFragment(currentRequest.getCategory());
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.manager_fragment_container, addCategoryFragment);
                    fragmentTransaction.addToBackStack("addCategoryFragment");
                    fragmentTransaction.commit();
                }
            });

            deleteRequest= listItemView.findViewById(R.id.deleteRequest);
            deleteRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if(success)
                                {
                                    category_requests.remove(categoryRequests.get(position));
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
                    DeleteRequest request = new DeleteRequest(currentRequest.getrID(), responseListener);
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    queue.add(request);
                }
            });

            return listItemView;

        }
        private class DeleteRequest extends StringRequest {

            private static final String URL = "http://mohamadka.000webhostapp.com/DeleteCategoryRequest.php";
            private Map<String, String> params;

            public DeleteRequest( int requestID, Response.Listener<String> listener) {
                super(Method.POST, URL, listener, null);
                params = new HashMap<>();
                params.put("rID", requestID+"");
            }

            @Override
            public Map<String, String> getParams() {
                return params;
            }
        }

    }

}
