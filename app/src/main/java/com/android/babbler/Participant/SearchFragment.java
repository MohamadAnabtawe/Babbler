package com.android.babbler.Participant;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.babbler.DataClasses.Category;
import com.android.babbler.Participant.SearchResultsFragment;
import com.android.babbler.R;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private static final String URL = "http://mohamadka.000webhostapp.com/AllCategories.php";
    private ArrayList<Category> categories=new ArrayList<Category>();
    private static ArrayList<String> categoryNames =new ArrayList<String>();
    private Spinner LanguageSpinner;
    private String selectedLanguage;
    private Spinner CategorySpinner;
    private String selectedCategory;
    private ArrayAdapter<String> CategoryAdapter;
    private ArrayAdapter<String> LanguageAdapter;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_search, container, false);
        LanguageSpinner = view.findViewById(R.id.search_languages_spinner);
        LanguageAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item,getResources().getStringArray(R.array.languages));

        LanguageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        LanguageSpinner.setAdapter(LanguageAdapter);
        LanguageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLanguage=(String)adapterView.getItemAtPosition(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedLanguage=(String)adapterView.getItemAtPosition(0);
            }
        });
        CategorySpinner = view.findViewById(R.id.search_categories_spinner);
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
                        //add the session to the sessions array list
                        categories.add(new Category(cID,cName,cParent,cIndex));
                        categoryNames.add(cName);

                    }
                    CategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    CategorySpinner.setAdapter(CategoryAdapter);
                    CategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            selectedCategory=categories.get(i).getcID();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            selectedCategory=categories.get(0).getcID();
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

        Button search_btn=view.findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchResultsFragment searchResultsFragment = new SearchResultsFragment(selectedCategory,selectedLanguage);
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, searchResultsFragment);
                fragmentTransaction.addToBackStack("searchResults");
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}
