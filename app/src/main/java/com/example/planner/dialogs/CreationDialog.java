package com.example.planner.dialogs;

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
import com.example.planner.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreationDialog extends AppCompatDialogFragment {
    private EditText date, title, description;
    private CheckBox month, week, days5, days3, day1;
    private final DatabaseController dbc;

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

            @RequiresApi(api = Build.VERSION_CODES.O)
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
        AlertDialog ad = builder.create();
        ad.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return ad;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void dataTransfer(Editable title, Editable description, Editable date, boolean month, boolean week, boolean days5, boolean days3, boolean day1) {
        ArrayList<String> listOfDates = new ArrayList<>();
        // Přeformátujeme datum do CalendarDay
        CalendarDay endDate = dateFormatter(date.toString());

        if (endDate != null) {
            // Inicializace listu dat
            listOfDates.clear();
            listOfDates.add(datePrepare(endDate.getDate().toString()));

            // Odečtení dnů podle zaškrtnutých checkboxů
            Calendar c = Calendar.getInstance();
            if (month) {
                c.setTime(endDate.getDate());
                c.add(Calendar.DATE, -30);
                listOfDates.add(datePrepare(CalendarDay.from(c).getDate().toString()));
            }
            if (week) {
                c.setTime(endDate.getDate());
                c.add(Calendar.DATE, -7);
                listOfDates.add(datePrepare(CalendarDay.from(c).getDate().toString()));

            }
            if (days5) {
                c.setTime(endDate.getDate());
                c.add(Calendar.DATE, -5);
                listOfDates.add(datePrepare(CalendarDay.from(c).getDate().toString()));
            }
            if (days3) {
                c.setTime(endDate.getDate());
                c.add(Calendar.DATE, -3);
                listOfDates.add(datePrepare(CalendarDay.from(c).getDate().toString()));
            }
            if (day1) {
                c.setTime(endDate.getDate());
                c.add(Calendar.DATE, -1);
                listOfDates.add(datePrepare(CalendarDay.from(c).getDate().toString()));
            }

            // Předáme zformátované CalendarDay instance do saveReminder metody
            dbc.saveReminder(datePrepare(endDate.getDate().toString()), title.toString(), description.toString(), listOfDates);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public CalendarDay dateFormatter(String dateString) {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        Date dateHolder = null;
        try {
            dateHolder = inputDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dateHolder != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateHolder);
            return CalendarDay.from(calendar);
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String datePrepare(String userDateStr) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        try {
            LocalDateTime userDate = LocalDateTime.parse(userDateStr, inputFormatter);
            return outputFormatter.format(userDate);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Chyba při analýze data: " + e.getMessage());
        }
    }
}
