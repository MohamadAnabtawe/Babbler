package com.android.babbler.Participant;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.babbler.DataClasses.User;
import com.android.babbler.R;
import com.android.babbler.DataClasses.Session;
import com.android.babbler.Adapters.SessionAdapter;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultsFragment extends Fragment {

    private String categoryID;
    private String language;
    private SessionAdapter itemsAdapter;
    private ListView listView;
    private ArrayList<Session> searchResults=new ArrayList<Session>();
    private static final int N=2;
    private ArrayList<Session> AllSolutions=new ArrayList<Session>();
    public SearchResultsFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public SearchResultsFragment(String categoryID,String language) {
        // Required empty public constructor
        this.categoryID=categoryID;
        this.language=language;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_search_results, container, false);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //get the returned array
                    JSONArray SessionsByLanguage = new JSONArray(response);
                    ArrayList<Session> sessions=new ArrayList<Session>();
                    for (int i = 0; i < SessionsByLanguage.length(); i++) {
                        JSONObject sessionJSONObject = SessionsByLanguage.getJSONObject(i);
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
                        ListView search_list_view=view.findViewById(R.id.search_list_view);
                        search_list_view.setVisibility(View.GONE);
                        TextView textView=view.findViewById(R.id.s_r);
                        textView.setText("Sorry, we don't have available sessions in this language yet, please try again later...");
                        textView.setTextColor(getResources().getColor(R.color.error));
                    }
                    else {
                        searchResults.clear();
                        AllSolutions.clear();
                        pareto_optimal_solutions(sessions);
                        listView = view.findViewById(R.id.search_list_view);
                        itemsAdapter = new SessionAdapter(getActivity(), searchResults);
                        listView.setAdapter(itemsAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                SessionFragment sessionFragment = new SessionFragment(searchResults.get(position),false);
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
        SearchRequest request = new SearchRequest(User.getInstance().getM_id(),language,responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);

        return view;
    }

    /**
     * implements the algorithm of pareto optimal solutions, by calculating the best matching
     * sessions both in time and category, after getting all the solutions we pick the best N solutions
     */
    private void pareto_optimal_solutions(ArrayList<Session> sessions){
        /*epsilon: is the maximum distance from the line "time"="distance" (x=y)
          d: is the distance of point from the line*/
        double epsilon=20,d,time;
        /* distance between categories */
        int distance;
        pareto_line(sessions);
        //if number of all the solutions is less or equal to N then we the result will be all the solutions
        if(AllSolutions.size()<=N){
            searchResults=AllSolutions;
            return;
        }
        //else we calculate and return the best N solutions
        while(searchResults.size()<N){
            for(int i=0;i<AllSolutions.size();i++){
                time=time(AllSolutions.get(i).getSessionTime(),AllSolutions.get(i).getSessionDate());
                distance=distance(categoryID,AllSolutions.get(i).getCategory());
                //the distance between the point (x,y) and the line y=x is |x-y|/sqrt(2)
                d=Math.abs(time-distance)/Math.sqrt(2);
                if(d<=epsilon) {
                    searchResults.add(AllSolutions.get(i));
                    if(searchResults.size()==N)return;
                }
            }
            //delete all the sessions that we already added to the search result
            for(int j=0;j<searchResults.size();j++)
                AllSolutions.remove(searchResults.get(j));
            epsilon+=20;
        }
    }

    /**
     *
     * update the list of all optimal solutions (solutions on the pareto line)
     */
    private void pareto_line(ArrayList<Session> sessions){
        boolean isOptimal;
        for(int i=0;i<sessions.size();i++)
        {
            isOptimal=true;
            for(int j=0;j<sessions.size();j++){
                //if this solution is dominated by other solution, then it's not optimal
                if(i!=j && dominated(sessions.get(i),sessions.get(j))) {
                    isOptimal=false;
                    break;
                }
            }
            //if this solution is not dominated by any others then it's on the pareto line
            if(isOptimal)AllSolutions.add(sessions.get(i));
        }
    }

    /**
     *
     * @param id1 : the first category id
     * @param id2 : the second category id
     * @return distance: the distance between the tow categories
     */
    private int distance(String id1,String id2){
        int len,num1,num2;
        String result="";
        /*we compare the first len digits from left to right where len is the smallest length of id
         * for example: id1="123", id2="231134" we compare the difference between 123 and 231
         * and the result in this example is 112*/
        len= (id1.length()<id2.length()) ? id1.length():id2.length();
        for(int i=0;i<len;i++){
            num1=id1.charAt(i)-'0';
            num2=id2.charAt(i)-'0';
            //calculate the difference in the digits
            result+= Math.abs(num1-num2);
        }
        return Integer.parseInt(result);
    }

    /**
     *
     * @param Time : the time of the category in 24 format (xx:xx)
     * @param Date : the date of the category in dd-MM-yyyy format (we take only the first tow numbers which represent the day)
     * @return time: an double representation of the time (example: 14:20 today -> 14.20 in double)
     */
    private double time(String Time,String Date){
        int diff=0;
        double time;
        String currentDate;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy");
        currentDate=mdformat.format(calendar.getTime());
        //convert day and month into integer
        int currentDay=Integer.parseInt(currentDate.substring(0,2));
        int sessionDay=Integer.parseInt(Date.substring(0,2));
        int currentMonth=Integer.parseInt(currentDate.substring(3,5));
        int sessionMonth=Integer.parseInt(Date.substring(3,5));
        /*calculate the difference in days (between the session day and the current day)
        if the current month is different (we only look at sessions in span of a week) then we subtract the day from 31
        which is the maximum number of days in a month*/
        if(currentMonth != sessionMonth)diff=31-currentDay+sessionDay;
        else{
            diff=sessionDay-currentDay;
        }
        //for each day we add 24 in our example 14:20 in the next day will be represented by the number 14.20+24*1 =38.20
        time=Double.parseDouble(Time.substring(0,2)+"."+Time.substring(3,5));
        time+=diff*24;
        return time;
    }

    /**
     * @param A first session
     * @param B second session
     * @return true if A is dominated by B and false else
     * */
    private boolean dominated(Session A,Session B){
        int distanceA,distanceB;
        double timeA,timeB;
        //calculate the time and the distance of session A and B from the searched category
        timeA=time(A.getSessionTime(),A.getSessionDate());
        distanceA=distance(categoryID,A.getCategory());
        timeB=time(B.getSessionTime(),B.getSessionDate());
        distanceB=distance(categoryID,B.getCategory());
        //check if A dominates B
        if(timeB<=timeA && distanceB<=distanceA)
            if(timeB<timeA||distanceB<distanceA)return true;
        return false;
    }

    private class SearchRequest extends StringRequest {

        private static final String REQUEST_URL = "https://mohamadka.000webhostapp.com/SessionByLanguage.php";
        private Map<String, String> params;

        public SearchRequest(int uID,String language, Response.Listener<String> listener) {
            super(Method.POST, REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("sLanguage", language);
            params.put("uID", uID+"");
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }
}
