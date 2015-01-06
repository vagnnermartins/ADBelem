package com.vagnnermartins.adbelem.enums;

import com.vagnnermartins.adbelem.R;

import java.util.Calendar;

/**
 * Created by vagnnermartins on 28/12/14.
 */
public enum DayOfTheWeekEnum {

    SUNDAY(Calendar.SUNDAY, R.string.sunday),
    MONDAY(Calendar.MONDAY, R.string.monday),
    TUESDAY(Calendar.TUESDAY, R.string.tuesday),
    WEDNESDAY(Calendar.WEDNESDAY, R.string.wednesday),
    THURSDAY(Calendar.THURSDAY, R.string.thursday),
    FRIDAY(Calendar.FRIDAY, R.string.friday),
    SATURDAY(Calendar.SATURDAY, R.string.saturday);

    private int value;
    private int day;

    private DayOfTheWeekEnum(int value, int day) {
        this.value = value;
        this.day = day;
    }

    public int getValue() {
        return value;
    }

    public int getDay() {
        return day;
    }
}
