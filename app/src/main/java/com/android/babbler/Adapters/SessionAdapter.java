package com.android.babbler.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.babbler.Participant.SessionListFragment;
import com.android.babbler.R;
import com.android.babbler.DataClasses.Session;

import java.util.ArrayList;

import static android.app.PendingIntent.getActivity;


public class SessionAdapter extends ArrayAdapter<Session> {

    public SessionAdapter(Activity context, ArrayList<Session> sessionList)
    {
        super(context,0,sessionList);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.session_list, parent, false);
        }

        // Get the {@link session} object located at this position in the list
        Session currentSession=getItem(position);

        // Find the TextView in the session_list.xml layout with the ID title_tv
        TextView title_tv=listItemView.findViewById(R.id.title_tv);
        title_tv.setText(currentSession.getSessionTitle());

        // Find the TextView in the session_list.xml layout with the ID time_tv
        TextView time_tv=listItemView.findViewById(R.id.time_tv);
        time_tv.setText(currentSession.getSessionTime());

        // Find the TextView in the session_list.xml layout with the ID language_tv
        TextView language_tv=listItemView.findViewById(R.id.language_tv);
        language_tv.setText(currentSession.getSessionLanguage());

        // Find the TextView in the session_list.xml layout with the ID date_tv
        TextView date_tv=listItemView.findViewById(R.id.date_tv);
        date_tv.setText(currentSession.getSessionDate());

       // Find the ImageView in the session_list.xml layout with the ID s_img
        de.hdodenhof.circleimageview.CircleImageView session_img=listItemView.findViewById(R.id.m_img);
        //if there is an Image change it, else keep the default image
        if(currentSession.getImageID()>0)session_img.setImageResource(currentSession.getImageID());
        else{
            session_img.setImageResource(R.drawable.b);
        }
        return listItemView;

    }

}
