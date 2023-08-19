package com.example.planner.model;

import java.util.ArrayList;
import java.util.List;

public class DaySchedule {
    private String date;
    private List<ReminderModel> reminders;

    public DaySchedule(String date, List<ReminderModel> reminders) {
        this.date = date;
        this.reminders = reminders;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ReminderModel> getReminders() {
        return reminders;
    }

    public void setReminders(List<ReminderModel> reminders) {
        this.reminders = reminders;
    }
}
