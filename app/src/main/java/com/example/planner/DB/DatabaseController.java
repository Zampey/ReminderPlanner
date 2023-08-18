package com.example.planner.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.planner.model.ReminderModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseController{
    private Context context;
    private ReminderDbHelper reminderDbHelper;
    public DatabaseController(Context context) {
        this.context = context;
        this.reminderDbHelper = new ReminderDbHelper(context);
    }

    public void saveReminder(String title, String description, ArrayList<String> datesList){
        // getting writable DB
        SQLiteDatabase db = reminderDbHelper.getWritableDatabase();
        // preparing values for insert into "reminders"
        ContentValues reminderValues = new ContentValues();
        reminderValues.put("title", title);
        reminderValues.put("description", description);
        // inserting into "reminders", and getting ID
        long reminderId = db.insert("reminders", null, reminderValues);

        if (reminderId != -1) {
            for (String date : datesList) {
                // preparing values for insert into "reminder_dates"
                ContentValues reminderDateValues = new ContentValues();
                reminderDateValues.put("reminder_id", reminderId);
                reminderDateValues.put("date", date);
                // inserting into "reminder_dates"
                long reminderDateId = db.insert("reminder_dates", null, reminderDateValues);
                if (reminderDateId == -1) {
                    // inserting fail when inserting dates
                    Log.e("Database", "Failed to insert reminder date.");
                }
            }
        } else {
            // inserting fail when inserting reminder
            Log.e("Database", "Failed to insert reminder.");
        }
        db.close();
    }

    @SuppressLint("Range")
    public void databaseOutput(){
        SQLiteDatabase db = reminderDbHelper.getReadableDatabase();

        // SQL query
        String query = "SELECT reminders.id, reminders.title, reminders.description, reminder_dates.date " +
                "FROM reminders " +
                "LEFT JOIN reminder_dates ON reminders.id = reminder_dates.reminder_id";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // getting values from cursor
                Log.d("Database",String.valueOf(cursor.getInt(cursor.getColumnIndex("id"))));
                Log.d("Database",cursor.getString(cursor.getColumnIndex("title")));
                Log.d("Database",cursor.getString(cursor.getColumnIndex("description")));
                Log.d("Database",cursor.getString(cursor.getColumnIndex("date")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    public void databaseReset(){
        // getting writable DB
        SQLiteDatabase db = reminderDbHelper.getWritableDatabase();
        // deletion of all lines in both DBs
        db.delete("reminders", null, null);
        db.delete("reminder_dates", null, null);
        db.close();
    }
    @SuppressLint("Range")
    public ArrayList<ReminderModel> getNextSevenDays() {
        ArrayList<ReminderModel> reminderList = new ArrayList<>();
        // Získání aktuálního data
        Calendar currentDate = Calendar.getInstance();

        // Přeformátování aktuálního data do správného formátu "yyyy-MM-dd HH:mm:ss"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateStr = sdf.format(currentDate.getTime());

        // Přidání 7 dnů k aktuálnímu datu pro vytvoření koncového data
        currentDate.add(Calendar.DAY_OF_MONTH, 7);
        String endDateStr = sdf.format(currentDate.getTime());

        // Získání databáze pro čtení
        SQLiteDatabase db = reminderDbHelper.getReadableDatabase();

        // Příprava poddotazu pro nalezení posledních dat pro každou připomínku
        String subquery = "SELECT reminder_id, MAX(date) AS last_date FROM reminder_dates GROUP BY reminder_id";

        // Příprava podmínky pro získání záznamů s posledním datem do 7 dnů od aktuálního data
        String selection = "last_dates.last_date >= ? AND last_dates.last_date <= ?";
        String[] selectionArgs = {currentDateStr, endDateStr};

        // Dotaz pro získání připomínek s posledním datem v následujících 7 dnech
        String query = "SELECT reminders.id, reminders.title, reminders.description, last_dates.last_date AS date " +
                "FROM reminders " +
                "INNER JOIN (" + subquery + ") AS last_dates ON reminders.id = last_dates.reminder_id " +
                "WHERE " + selection +
                " ORDER BY last_dates.last_date";
        Log.d("Database", "SQL dotaz: " + query);
        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                // Získání hodnot z kurzoru
                int reminderId = cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                //přidání připomínky do listu
                ReminderModel reminderModel = new ReminderModel(title,description,date);
                reminderList.add(reminderModel);

                // Výpis hodnot pomocí logování nebo do konzole
                Log.d("Database", "Reminder ID: " + reminderId);
                Log.d("Database", "Title: " + title);
                Log.d("Database", "Description: " + description);
                Log.d("Database", "Date: " + date);
            } while (cursor.moveToNext());
        }
        // Uzavření kurzoru a databáze
        cursor.close();
        db.close();
        return reminderList;
    }
    @SuppressLint("Range")
    public List<ReminderModel> getAllReminders(){
        SQLiteDatabase db = reminderDbHelper.getReadableDatabase();
        List<ReminderModel> reminders = new ArrayList<>();
        String query = "SELECT reminders.id, title, description, date " +
                "FROM reminders " +
                "JOIN reminder_dates ON reminders.id = reminder_dates.reminder_id";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String date = cursor.getString(cursor.getColumnIndex("date"));

                ReminderModel reminder = new ReminderModel(title, description, date);
                reminders.add(reminder);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return reminders;
    }

    public void insertTestReminder(Context context) {
        // Vytvoření instance ReminderDbHelper
        ReminderDbHelper dbHelper = new ReminderDbHelper(context);

        // Získání databáze pro zápis
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Získání aktuálního data
        Calendar currentDate = Calendar.getInstance();

        // Přeformátování aktuálního data do správného formátu "yyyy-MM-dd HH:mm:ss"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateStr = sdf.format(currentDate.getTime());

        // Připrava hodnot pro vložení do tabulky "reminders"
        String title = "Testovací připomínka";
        String description = "Toto je testovací popisek připomínky.";

        ContentValues reminderValues = new ContentValues();
        reminderValues.put("title", title);
        reminderValues.put("description", description);

        // Vložení připomínky do tabulky "reminders" a získání jejího ID
        long reminderId = db.insert("reminders", null, reminderValues);

        // Příprava hodnot pro vložení do tabulky "reminder_dates"
        ContentValues reminderDateValues = new ContentValues();
        reminderDateValues.put("reminder_id", reminderId);
        reminderDateValues.put("date", currentDateStr);

        // Vložení data připomínky do tabulky "reminder_dates"
        long reminderDateId = db.insert("reminder_dates", null, reminderDateValues);

        // Uzavření databáze
        db.close();

        Log.d("TestDatabaseInsert", "Testovací připomínka byla úspěšně vložena do databáze.");
    }
    @SuppressLint("Range")
    public List<ReminderModel> getRemindersForDay(CalendarDay calendarDay) {
        List<ReminderModel> reminders = new ArrayList<>();

        SQLiteDatabase db = reminderDbHelper.getReadableDatabase();

        // Získat dnešní datum ve formátu "YYYY-MM-DD"
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String todayDate = dateFormat.format(calendarDay.getDate());

        // SQL dotaz, který načte poslední připomínky s jejich posledními datumy, které odpovídají dnešnímu dni
        String query = "SELECT r.id, r.title, r.description, MAX(rd.date) AS max_date " +
                "FROM reminders r " +
                "JOIN reminder_dates rd ON r.id = rd.reminder_id " +
                "WHERE rd.date = ? " +
                "GROUP BY r.id, r.title, r.description";

        Cursor cursor = db.rawQuery(query, new String[]{todayDate});

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String date = cursor.getString(cursor.getColumnIndex("max_date"));

                ReminderModel reminder = new ReminderModel(title, description, date);
                reminders.add(reminder);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return reminders;
    }
}
