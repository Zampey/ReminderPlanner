package com.example.planner.schedule;

import com.example.planner.model.ReminderModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReminderHelper {

    // Metoda rozdělí připomínky do seznamů pro každý den následujících 7 dní
    public static List<ArrayList<ReminderModel>> groupRemindersByDay(ArrayList<ReminderModel> reminders) {
        // Vytvoříme seznam pro každý den
        List<ArrayList<ReminderModel>> remindersByDay = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            remindersByDay.add(new ArrayList<>());
        }

        // Získáme aktuální datum
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        // Projdeme všechny připomínky
        for (ReminderModel reminder : reminders) {
            // Získáme datum připomínky ve formátu yyyy-MM-dd HH:mm:ss
            String reminderDateStr = reminder.getDate();

            // Převedeme datum ze String na Calendar
            Calendar reminderDate = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            try {
                Date date = sdf.parse(reminderDateStr);
                reminderDate.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
                continue; // Pokud se nepodaří převést datum, přeskočíme tuto připomínku
            }

            // Rozdíl mezi aktuálním datem a datem připomínky v dnech
            int daysDifference = (int) ((reminderDate.getTimeInMillis() - today.getTimeInMillis()) / (1000 * 60 * 60 * 24));

            // Pokud je rozdíl větší než 0 a menší než 7, přidáme připomínku do příslušného seznamu
            if (daysDifference >= 0 && daysDifference < 7) {
                remindersByDay.get(daysDifference).add(reminder);
            }
        }

        return remindersByDay;
    }
}
