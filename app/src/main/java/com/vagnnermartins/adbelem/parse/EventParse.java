package com.vagnnermartins.adbelem.parse;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;

/**
 * Created by vagnnermartins on 04/01/15.
 */
@ParseClassName("Events")
public class EventParse extends ParseObject{

    private static final String NAME = "name";
    private static final String DATE = "date";
    private static final String CHURCH = "church";

    public static void findEventsByChurch(FindCallback<EventParse> callback, ChurchParse church){
        ParseQuery query = ParseQuery.getQuery(EventParse.class);
        query.whereGreaterThanOrEqualTo(DATE, new Date());
        query.whereEqualTo(CHURCH, church);
        query.include(CHURCH);
        query.findInBackground(callback);
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
    @Override
    public String toString() {
        return getName();
    }
}
