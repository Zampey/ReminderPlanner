package com.example.planner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.example.planner.DB.DatabaseController;
import com.example.planner.DB.ReminderDbHelper;
import com.example.planner.calendar.CalendarFragment;
import com.example.planner.dialogs.CreationDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity  {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout fragmentContainer;
    private FloatingActionButton addReminder;
    private static final int ALARM_INTERVAL = 30*60*10000; // 30 minut v milisekundách
    DatabaseController dbc = new DatabaseController( new ReminderDbHelper(this));

    private HomeFragment homeFragment;
    //private SearchFragment searchFragment;
    private CalendarFragment calendarFragment;
    //private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAlarm();
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        addReminder = findViewById(R.id.add_fab);
        fragmentContainer = findViewById(R.id.fragmentContainer);
        //bottonNavBar settings
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);

       homeFragment = new HomeFragment(dbc);
       //searchFragment = new SearchFragment();
       calendarFragment = new CalendarFragment(dbc);
       //settingsFragment = new SettingsFragment();

        switchToFragment(homeFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.miHome:
                    switchToFragment(homeFragment);
                    return true;
                /*case R.id.miSearch:
                    switchToFragment(searchFragment);
                    return true;*/
                case R.id.miCalendar:
                    switchToFragment(calendarFragment);
                    return true;
                /*case R.id.miSetting:
                    switchToFragment(settingsFragment);
                    return true;*/
            }
            return false;
        });

        addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreationDialog();
            }
        });
    }

    private void switchToFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    public void openCreationDialog(){
        CreationDialog creationDialog = new CreationDialog(dbc);
        creationDialog.show(getSupportFragmentManager(), "creation dialog");
    }



    private void setAlarm() {
        Intent alarmIntent = new Intent(this, MyAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long currentTimeMillis = System.currentTimeMillis();
        long alarmTimeMillis = currentTimeMillis + ALARM_INTERVAL;

        // Nastavte alarm na spuštění každých 30 minut
        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                alarmTimeMillis,
                ALARM_INTERVAL,
                pendingIntent
        );
    }


}