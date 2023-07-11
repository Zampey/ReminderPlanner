package com.example.planner;

import android.app.Dialog;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.planner.DB.DatabaseController;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CreationDialog extends AppCompatDialogFragment {
    private EditText date, title, description;
    private CheckBox month, week, days5, days3, day1;
    private final DatabaseController dbc;
    ArrayList<CalendarDay> listOfDates = new ArrayList<>();
    public CreationDialog(DatabaseController dbc) {
        this.dbc = dbc;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.creation_dialog,null);
        builder.setView(view)
                .setNegativeButton("Zrušit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("Vytvořit", new DialogInterface.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //initiating inputs
                date = view.findViewById(R.id.creation_dialog_date);
                title = view.findViewById(R.id.creation_dialog_title);
                description = view.findViewById(R.id.creation_dialog_description);
                month = view.findViewById(R.id.checkbox1);
                week = view.findViewById(R.id.checkbox2);
                days5 = view.findViewById(R.id.checkbox3);
                days3 = view.findViewById(R.id.checkbox4);
                day1 = view.findViewById(R.id.checkbox5);
                dataTransfer(title.getText(),description.getText(),date.getText(),month.isChecked(),week.isChecked(),days5.isChecked(),days3.isChecked(),day1.isChecked());
            }
        });
        return builder.create();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void dataTransfer(Editable title, Editable description, Editable date, boolean month, boolean week, boolean days5, boolean days3, boolean day1){
        CalendarDay endDate = dateFormatter(date);
        listOfDates.add(CalendarDay.from(endDate.getDate()));
        Calendar c = Calendar.getInstance();
        if (month){
            c.setTime(endDate.getDate());
            c.add(c.DATE, -30);
            listOfDates.add(CalendarDay.from(c.getTime()));
        }
        if (week){
            c.setTime(endDate.getDate());
            c.add(c.DATE, -7);
            listOfDates.add(CalendarDay.from(c.getTime()));
        }
        if (days5){
            c.setTime(endDate.getDate());
            c.add(c.DATE, -5);
            listOfDates.add(CalendarDay.from(c.getTime()));
        }
        if (days3){
            c.setTime(endDate.getDate());
            c.add(c.DATE, -3);
            listOfDates.add(CalendarDay.from(c.getTime()));
        }
        if (day1){
            c.setTime(endDate.getDate());
            c.add(c.DATE, -1);
            listOfDates.add(CalendarDay.from(c.getTime()));
        }
        dbc.saveReminder(title.toString(),description.toString(),listOfDates);
        dbc.databaseOutput();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CalendarDay dateFormatter(Editable dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date dateHolder = null;
        try {
            dateHolder = dateFormat.parse(dateString.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateHolder);

        int yearHolder = calendar.get(Calendar.YEAR);
        int monthHolder = calendar.get(Calendar.MONTH);
        int dayHolder = calendar.get(Calendar.DAY_OF_MONTH);

        CalendarDay calendarDay = CalendarDay.from(yearHolder, monthHolder, dayHolder);
        return calendarDay;
    }
}
