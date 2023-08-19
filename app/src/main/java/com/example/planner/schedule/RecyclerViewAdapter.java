package com.example.planner.schedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.R;
import com.example.planner.model.DaySchedule;
import com.example.planner.model.ReminderModel;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<DaySchedule> dayScheduleList;

    public RecyclerViewAdapter(List<DaySchedule> dayScheduleList) {
        this.dayScheduleList = dayScheduleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.headerTextView.setText(dayScheduleList.get(position).getDate());

        List<ReminderModel> reminders = dayScheduleList.get(position).getReminders();
        ReminderItemAdapter reminderItemAdapter = new ReminderItemAdapter(reminders);
        holder.reminderRecyclerView.setAdapter(reminderItemAdapter);
    }

    @Override
    public int getItemCount() {
        return dayScheduleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView headerTextView;
        RecyclerView reminderRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            headerTextView = itemView.findViewById(R.id.headerTextView);
            reminderRecyclerView = itemView.findViewById(R.id.reminderRecyclerView);
            reminderRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }
}




