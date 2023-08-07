package com.example.planner.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.R;
import com.example.planner.model.ReminderModel;

import java.util.ArrayList;
import java.util.List;

public class DateReminderListAdapter extends RecyclerView.Adapter<DateReminderListAdapter.DateReminderViewHolder> {

    private Context context;
    private List<String> dates;
    private List<ArrayList<ReminderModel>> remindersLists;

    public DateReminderListAdapter(Context context, List<String> dates, List<ArrayList<ReminderModel>> remindersLists) {
        this.context = context;
        this.dates = dates;
        this.remindersLists = remindersLists;
    }

    @NonNull
    @Override
    public DateReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_date_reminder_list, parent, false);
        return new DateReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateReminderViewHolder holder, int position) {
        String date = dates.get(position);
        ArrayList<ReminderModel> reminders = remindersLists.get(position);

        holder.dateTextView.setText(date);

        ReminderListAdapter adapter = new ReminderListAdapter(context, reminders);
        holder.reminderListView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public static class DateReminderViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        ListView reminderListView;

        public DateReminderViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            reminderListView = itemView.findViewById(R.id.reminderListView);
        }
    }
}
