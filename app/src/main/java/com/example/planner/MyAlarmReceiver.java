package com.example.planner;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.planner.DB.DatabaseController;
import com.example.planner.DB.ReminderDbHelper;
import com.example.planner.model.ReminderModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Calendar;
import java.util.List;

public class MyAlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "my_channel_id";
    private static final int NOTIFICATION_ID = 1;
    private DatabaseController dbc;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Zde provádíte akce, které chcete provést každých 30 minut
        dbc = new DatabaseController(new ReminderDbHelper(context));
        createNotification(context);
    }

    private void createNotification(Context context) {
        // Vytvoření notifikačního kanálu (jen pro Android 8.0 a novější)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel Name";
            String description = "Popis mého kanálu";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        List<ReminderModel> remindersForDay = dbc.getRemindersForDay(CalendarDay.today());
        if (remindersForDay.size()>0){

            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY); // Získání hodin v 24h formátu
            if (hour == 10 || hour == 17) {
                // Vytvoření notifikace
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle("Plánovač")
                        .setContentText("Čekají vás nějaké aktivity. Zkontrolujte je!");

                // Získání NotificationManager
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                // Zobrazení notifikace
                notificationManager.notify(NOTIFICATION_ID, builder.build());
            }

        }

    }
}