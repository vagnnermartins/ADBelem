package com.vagnnermartins.adbelem.parse;

import android.content.Context;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.vagnnermartins.adbelem.util.AlarmUtil;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by vagnnermartins on 04/01/15.
 */
@ParseClassName("Events")
public class EventParse extends ParseObject implements Serializable{

    private static final String NAME = "name";
    private static final String DATE = "date";
    private static final String CHURCH = "church";
    private static final String FOLLOW = "follow";

    public static void findEventsByChurch(FindCallback<EventParse> callback, ChurchParse church){
        ParseQuery query = ParseQuery.getQuery(EventParse.class);
        query.whereGreaterThanOrEqualTo(DATE, new Date());
        query.whereEqualTo(CHURCH, church);
        query.include(CHURCH);
        query.findInBackground(callback);
    }

    public static void findMyEventsInLocal(FindCallback<EventParse> callback){
        ParseQuery query = ParseQuery.getQuery(EventParse.class);
        query.fromLocalDatastore();
        query.whereGreaterThanOrEqualTo(DATE, new Date());
        query.orderByAscending(DATE);
        query.include(CHURCH);
        query.findInBackground(callback);
    }

    public static void saveEventInLocal(Context context, EventParse event){
        AlarmUtil.scheduledEventNotification(context, event);
        event.pinInBackground();
    }

    public static void deleteEventInLocal(Context context, EventParse event){
        AlarmUtil.cancelScheduleEventNotification(context, event);
        event.unpinInBackground();
    }


    public String getName(){
        return getString(NAME);
    }
    public Date getDate(){
        return getDate(DATE);
    }
    public ChurchParse getChurch(){
        return (ChurchParse) getParseObject(CHURCH);
    }
    public boolean isFollow(){
        return getBoolean(FOLLOW);
    }
    public void setFollow(boolean isFollow){
        put(FOLLOW, isFollow);
    }
    @Override
    public String toString() {
        return getName();
    }
}
