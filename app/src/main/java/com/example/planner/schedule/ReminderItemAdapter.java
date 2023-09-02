package com.example.planner.schedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.DB.DatabaseController;
import com.example.planner.DB.ReminderDbHelper;
import com.example.planner.MainActivity;
import com.example.planner.R;
import com.example.planner.model.ReminderModel;

import java.util.List;

public class ReminderItemAdapter extends RecyclerView.Adapter<ReminderItemAdapter.ViewHolder> {
    private DatabaseController dbc;
    private List<ReminderModel> reminders;

    public ReminderItemAdapter(List<ReminderModel> reminders, DatabaseController dbc) {
        this.reminders = reminders;
        this.dbc = dbc;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReminderModel reminder = reminders.get(position);
        holder.viewTitle.setText(reminder.getTitle());
        holder.viewDescription.setText(reminder.getDescription());
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbc.deleteWholeReminder(reminder.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView viewTitle;
        TextView viewDescription;
        ImageView deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewTitle = itemView.findViewById(R.id.titleTextView);
            viewDescription = itemView.findViewById(R.id.descriptionTextView);
            deleteBtn = itemView.findViewById(R.id.deleteRemBtn);
        }
    }
}

