package com.android.babbler.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.babbler.DataClasses.Message;
import com.android.babbler.R;

import java.util.ArrayList;

public class MessageAdapter extends ArrayAdapter<Message> {
    public MessageAdapter(Activity context, ArrayList<Message> messageList)
    {
        super(context,0,messageList);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.message_list_item, parent, false);
        }

        Message currentMessage=getItem(position);

        TextView subject=listItemView.findViewById(R.id.subject_tv);
        subject.setText(currentMessage.getSubject());

        TextView time_tv=listItemView.findViewById(R.id.msg_time_tv);
        time_tv.setText(currentMessage.getTime());

        TextView date_tv=listItemView.findViewById(R.id.msg_date_tv);
        date_tv.setText(currentMessage.getDate());

        return listItemView;

    }
}
