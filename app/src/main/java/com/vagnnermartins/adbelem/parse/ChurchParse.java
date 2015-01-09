package com.vagnnermartins.adbelem.parse;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

/**
 * Created by vagnnermartins on 26/12/14.
 */
@ParseClassName("Church")
public class ChurchParse extends ParseObject {

    private static final String NAME = "name";
    private static final String SECTOR = "sector";
    private static final String ADDRESS = "address";
    private static final String LATLNG = "latLng";
    private static final String WEBSITE = "website";
    private static final String FONE = "fone";
    private static final String FATHER = "father";
    private static final String PASTOR = "pastor";
    private static final String ENABLE = "enable";
    private static final String FOLLOW = "follow";

    public static void findAllSectors(FindCallback<ChurchParse> callback){
        ParseQuery query = ParseQuery.getQuery(ChurchParse.class);
        query.whereEqualTo(ENABLE, true);
        query.orderByAscending(SECTOR);
        query.whereEqualTo(FATHER, null);
        query.findInBackground(callback);
    }

    public static void findAllCongregations(FindCallback<ChurchParse> callback){
        ParseQuery query = ParseQuery.getQuery(ChurchParse.class);
        query.whereEqualTo(ENABLE, true);
        query.orderByAscending(SECTOR);
        query.whereExists(FATHER);
        query.findInBackground(callback);
    }

    public static void findChurchByLocation(FindCallback<ChurchParse> callback, LatLng location, double distance){
        ParseQuery query = ParseQuery.getQuery(ChurchParse.class);
        query.whereEqualTo(ENABLE, true);
        query.whereWithinKilometers(LATLNG, new ParseGeoPoint(location.latitude, location.longitude), distance);
        query.findInBackground(callback);
    }

    public static void findCongregationsByChurch(FindCallback<ChurchParse> callback, ChurchParse father){
        ParseQuery<ChurchParse> query = ParseQuery.getQuery(ChurchParse.class);
        query.whereEqualTo(ENABLE, true);
        ParseQuery<ChurchParse> innerQuery = ParseQuery.getQuery(ChurchParse.class);
        innerQuery.whereEqualTo("objectId", father.getObjectId());
        query.whereMatchesQuery(FATHER, innerQuery);
        query.findInBackground(callback);
    }

    public static void saveChurchInLocal(ChurchParse church){
        church.pinInBackground();
    }

    public static void deleteChurchInLocal(ChurchParse church){
        church.unpinInBackground();
    }

    public static void findMyChurchesInLocal(FindCallback<ChurchParse> callback){
        ParseQuery query = ParseQuery.getQuery(ChurchParse.class);
        query.fromLocalDatastore();
        query.orderByAscending(SECTOR);
        query.findInBackground(callback);
    }

    public int getSector(){
        return getInt(SECTOR);
    }
    public String getName(){
        return getString(NAME);
    }
    public String getAddress(){
        return getString(ADDRESS);
    }
    public ParseGeoPoint getLatLng(){
        return getParseGeoPoint(LATLNG);
    }
    public String getWebSite(){
        return getString(WEBSITE);
    }
    public String getFone(){
        return getString(FONE);
    }
    public boolean isEnabled(){
        return getBoolean(ENABLE);
    }
    public ChurchParse getFather(){
        return (ChurchParse) getParseObject(FATHER);
    }
    public String getPastor(){
        return getString(PASTOR);
    }
    public boolean isFollow(){
        return getBoolean(FOLLOW);
    }
    public void setFollow(boolean isFollow){
        put(FOLLOW, isFollow);
    }

    @Override
    public String toString() {
        return getSector() + " - " + getName() + getAddress();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChurchParse)) return false;

        ChurchParse that = (ChurchParse) o;

        if (!getObjectId().equals(that.getObjectId())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getObjectId().hashCode();
    }
}
