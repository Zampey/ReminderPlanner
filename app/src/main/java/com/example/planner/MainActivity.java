package com.example.planner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.example.planner.DB.DatabaseController;
import com.example.planner.calendar.CalendarFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout fragmentContainer;
    private FloatingActionButton addReminder;

    DatabaseController dbc = new DatabaseController(this);

    private HomeFragment homeFragment;
    //private SearchFragment searchFragment;
    private CalendarFragment calendarFragment;
    //private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setup database controller


        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        addReminder = findViewById(R.id.add_fab);
        fragmentContainer = findViewById(R.id.fragmentContainer);
        //bottonNavBar settings
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);

       homeFragment = new HomeFragment();
       //searchFragment = new SearchFragment();
       calendarFragment = new CalendarFragment();
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
}