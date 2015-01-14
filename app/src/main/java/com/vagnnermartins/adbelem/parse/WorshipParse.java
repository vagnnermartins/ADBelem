package com.vagnnermartins.adbelem.parse;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.vagnnermartins.adbelem.enums.DayOfTheWeekEnum;

import java.util.Calendar;

/**
 * Created by vagnnermartins on 28/12/14.
 */
@ParseClassName("Worship")
public class WorshipParse extends ParseObject{

    private static final String NAME = "name";
    private static final String DAY = "day";
    private static final String TIME = "time";
    private static final String CHURCH = "church";
    private static final String STATUS = "status";

    public static void findWorshipByChurch(ChurchParse church, FindCallback<WorshipParse> callback){
        ParseQuery query = ParseQuery.getQuery(WorshipParse.class);
        query.whereEqualTo(CHURCH, church);
        query.whereEqualTo(STATUS, true);
        query.orderByAscending(DAY);
        query.findInBackground(callback);
    }

    public String getName(){
        return getString(NAME);
    }

    public DayOfTheWeekEnum getDay(){
        return checkDayOfTheWeek(getInt(DAY));
    }

    public String getTime(){
        return getString(TIME);
    }

    private DayOfTheWeekEnum checkDayOfTheWeek(int day) {
        DayOfTheWeekEnum result;
        switch (day){
            case Calendar.SUNDAY:
                result = DayOfTheWeekEnum.SUNDAY;
                break;
            case Calendar.MONDAY:
                result = DayOfTheWeekEnum.MONDAY;
                break;
            case Calendar.TUESDAY:
                result = DayOfTheWeekEnum.TUESDAY;
                break;
            case Calendar.WEDNESDAY:
                result = DayOfTheWeekEnum.WEDNESDAY;
                break;
            case Calendar.THURSDAY:
                result = DayOfTheWeekEnum.THURSDAY;
                break;
            case Calendar.FRIDAY:
                result = DayOfTheWeekEnum.FRIDAY;
                break;
            default:
                result = DayOfTheWeekEnum.SATURDAY;
        }
        return result;
    }
}
