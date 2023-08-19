package com.example.planner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.DB.DatabaseController;
import com.example.planner.model.DaySchedule;
import com.example.planner.model.ReminderModel;
import com.example.planner.schedule.RecyclerViewAdapter;
import com.example.planner.schedule.ReminderHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private DatabaseController dbc;
    public HomeFragment(DatabaseController dbc) {
        this.dbc = dbc;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        ArrayList<ReminderModel> reminderList = dbc.getNextSevenDays();
        //list s připomínkami
        List<ArrayList<ReminderModel>> remindersLists = ReminderHelper.groupRemindersByDay(reminderList);

        // Vytvoření seznamu dat pro nadpisy v RecyclerView
        List<String> dates = new ArrayList<>();
        Calendar today = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        for (int i = 0; i < 7; i++) {
            dates.add((i == 0) ? "Dnes" : (i == 1) ? "Zítra" : sdf.format(today.getTime()));
            today.add(Calendar.DATE, 1);
        }
        List<DaySchedule> dayScheduleList = new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            DaySchedule ds = new DaySchedule(dates.get(i),remindersLists.get(i));
            dayScheduleList.add(ds);
        }

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(dayScheduleList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);


        // Return the root view
        return rootView;
    }
}