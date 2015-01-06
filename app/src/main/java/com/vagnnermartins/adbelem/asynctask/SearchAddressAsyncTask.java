package com.vagnnermartins.adbelem.asynctask;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.vagnnermartins.adbelem.callback.Callback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vagnnermartins on 29/12/14.
 */
public class SearchAddressAsyncTask extends AsyncTask<Void, Void, List<Address>> {

    private Context context;
    private Callback callback;
    private Exception exception;
    private LatLng latLng;
    private int maxResults;
    private String endereco;

    public SearchAddressAsyncTask(Context context, LatLng latLng, Callback callback,  int maxResults){
        this.context = context;
        this.callback = callback;
        this.latLng = latLng;
        this.maxResults = maxResults;
    }

    public SearchAddressAsyncTask(Context context, String endereco, Callback callback, int maxResults){
        this.context = context;
        this.callback = callback;
        this.endereco = endereco;
        this.maxResults = maxResults;
    }

    @Override
    protected List<Address> doInBackground(Void... arg) {
        List<Address> list = new ArrayList<>();
        try {
            if(latLng != null){
                list = new Geocoder(context).getFromLocation(latLng.latitude, latLng.longitude, maxResults);
            }else{
                list = new Geocoder(context).getFromLocationName(endereco, maxResults);
            }
        } catch (Exception e) {
            exception = e;
        }
        return list;
    }

    @Override
    protected void onPostExecute(List<Address> result) {
        if(result != null && !result.isEmpty() &&exception == null){
            callback.onReturn(exception, result);
        }else{
            doInBackground();
        }
    }
}
