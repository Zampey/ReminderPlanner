package com.example.planner.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.planner.DB.ReminderDbHelper;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;

public class DatabaseController{
    private Context context;
    public DatabaseController(Context context) {
        this.context = context;
    }

    public void saveReminder(String title, String description, ArrayList<CalendarDay> datesList){
        ReminderDbHelper dbHelper = new ReminderDbHelper(context);
        // getting writable DB
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // preparing values for insert into "reminders"
        ContentValues reminderValues = new ContentValues();
        reminderValues.put("title", title);
        reminderValues.put("description", description);
        // inserting into "reminders", and getting ID
        long reminderId = db.insert("reminders", null, reminderValues);

        if (reminderId != -1) {
            for (CalendarDay date : datesList) {
                // preparing values for insert into "reminder_dates"
                ContentValues reminderDateValues = new ContentValues();
                reminderDateValues.put("reminder_id", reminderId);
                reminderDateValues.put("date", date.getDate().toString());
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
        ReminderDbHelper dbHelper = new ReminderDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

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
                //@SuppressLint("Range") int reminderId = cursor.getInt(cursor.getColumnIndex("id"));
                //@SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
                //@SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
                //@SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    public void databaseReset(){
        // initiation of ReminderDbHelper
        ReminderDbHelper dbHelper = new ReminderDbHelper(context);
        // getting writable DB
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // deletion of all lines in both DBs
        db.delete("reminders", null, null);
        db.delete("reminder_dates", null, null);
        db.close();
    }
}
