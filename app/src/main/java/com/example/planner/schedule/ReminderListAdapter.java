package com.example.planner.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.planner.R;
import com.example.planner.model.ReminderModel;

import java.util.ArrayList;

public class ReminderListAdapter extends ArrayAdapter<ReminderModel> {

    private ArrayList<ReminderModel> reminders;
    private LayoutInflater inflater;

    public ReminderListAdapter(Context context, ArrayList<ReminderModel> reminders) {
        super(context, 0, reminders);
        this.reminders = reminders;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_reminder, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = convertView.findViewById(R.id.titleTextView);
            holder.descriptionTextView = convertView.findViewById(R.id.descriptionTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ReminderModel reminder = reminders.get(position);

        // Nastavení hodnot do TextViews
        holder.titleTextView.setText(reminder.getTitle());
        holder.descriptionTextView.setText(reminder.getDescription());

        return convertView;
    }

    private static class ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
    }
}
