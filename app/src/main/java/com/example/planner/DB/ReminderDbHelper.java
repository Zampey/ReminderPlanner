package com.example.planner.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReminderDbHelper extends SQLiteOpenHelper {

    // define database scheme
    private static final String DATABASE_NAME = "reminder.db";
    private static final int DATABASE_VERSION = 1;

    public ReminderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // table for reminders
        String createReminderTable = "CREATE TABLE reminders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "description TEXT" +
                ")";
        db.execSQL(createReminderTable);

        // table for dates of every reminder
        String createReminderDateTable = "CREATE TABLE reminder_dates (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "reminder_id INTEGER," +
                "date TEXT," +
                "FOREIGN KEY (reminder_id) REFERENCES reminders(id)" +
                ")";
        db.execSQL(createReminderDateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // database actualization
        db.execSQL("DROP TABLE IF EXISTS reminder_dates");
        db.execSQL("DROP TABLE IF EXISTS reminders");
        onCreate(db);
    }
}
