package com.example.planner.calendar;

import android.content.Context;
import android.graphics.Color;

import com.example.planner.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;

import androidx.core.content.ContextCompat;

public class CurrentDayDecorator implements DayViewDecorator {

    private final CalendarDay currentDay;
    private final Drawable backgroundDrawable;
    private final Calendar calendar = Calendar.getInstance();

    public CurrentDayDecorator(Context context) {
        this.currentDay = CalendarDay.from(calendar);
        this.backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.current_day_background);

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        CalendarDay currentDay = CalendarDay.today();
        int currentMonth = currentDay.getMonth();
        int currentDayOfMonth = currentDay.getDay();

        int month = day.getMonth();
        int dayOfMonth = day.getDay();

        return month == currentMonth && dayOfMonth == currentDayOfMonth;
    }

    @Override
    public void decorate(DayViewFacade view) {
        //view.addSpan(new ImageSpan(backgroundDrawable));
        view.addSpan(new ForegroundColorSpan(Color.WHITE));
        view.addSpan(new StyleSpan(Typeface.BOLD));
    }
}

