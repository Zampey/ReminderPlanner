package com.example.planner.calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.DB.DatabaseController;
import com.example.planner.MainActivity;
import com.example.planner.R;
import com.example.planner.model.ReminderModel;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {
    MaterialCalendarView calendarView;
    private DatabaseController dbc;

    public CalendarFragment(DatabaseController dbc) {
        this.dbc =dbc;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dbc.databaseOutput();
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.calendar_fragment, container, false);
        //variables initialization
        Calendar calendar = Calendar.getInstance();
        CalendarDay currentDay = CalendarDay.from(calendar);
        AppCompatButton returnToCurrentMonthButton = rootView.findViewById(R.id.return_to_current_month);

        // Find and configure views within the fragment layout
        calendarView = rootView.findViewById(R.id.calendarView);
        // Add any necessary configuration or event handling for the calendar view
        calendarView.addDecorator(new CurrentDayDecorator(this.getContext()));
        calendarView.setSelectedDate(currentDay);

        //checking for showing back button in calendar
        CalendarDay currentMonth = CalendarDay.today();
        if (isMoreThan3MonthsAway(currentMonth)) {
            returnToCurrentMonthButton.setVisibility(View.VISIBLE);
        } else {
            returnToCurrentMonthButton.setVisibility(View.GONE);
        }

        // add listener for displayed month change
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                if (isMoreThan3MonthsAway(date)) {
                    returnToCurrentMonthButton.setVisibility(View.VISIBLE);
                } else {
                    returnToCurrentMonthButton.setVisibility(View.GONE);
                }
            }
        });

        //add listener for "back to actual month" button
        returnToCurrentMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set actual month as displayed
                calendarView.setCurrentDate(CalendarDay.today());
                //set the back button as invisible
                returnToCurrentMonthButton.setVisibility(View.GONE);
            }
        });
        //Vytvořte instanci EventDecorator s použitím seznamu dat
        EventDecorator eventDecorator = new EventDecorator(requireContext(), getHighlightedDates());
        // Přidejte dekoraci do kalendáře
        calendarView.addDecorator(eventDecorator);


        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                if (selected) {
                    // Zde získáme seznam připomínek pro vybraný den
                    List<ReminderModel> remindersForDay = dbc.getRemindersForDay(date);
                    RecyclerView recyclerView = rootView.findViewById(R.id.calendarRecyclerView);
                    CalendarRecyclerViewAdapter adapter = new CalendarRecyclerViewAdapter(remindersForDay);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    Log.d("TAG", "onDateSelected: "+ remindersForDay);

                }
            }
        });


        // Return the root view
        return rootView;
    }

    private List<CalendarDay> getHighlightedDates() {
        List<ReminderModel> reminders = dbc.getAllReminders();
        List<CalendarDay> datesToHighlight = new ArrayList<>();

        for (ReminderModel reminder : reminders) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            try {
                Date date = sdf.parse(reminder.getDate());
                calendar.setTime(date);
                CalendarDay calendarDay = CalendarDay.from(calendar);
                datesToHighlight.add(calendarDay);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return datesToHighlight;
    }

    private boolean isMoreThan3MonthsAway(CalendarDay date) {
        CalendarDay currentMonth = CalendarDay.today();
        int monthsDiff = (date.getYear() - currentMonth.getYear()) * 12 + (date.getMonth() - currentMonth.getMonth());
        return Math.abs(monthsDiff) >= 3;
    }


}