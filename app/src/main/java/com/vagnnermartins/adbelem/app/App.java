package com.vagnnermartins.adbelem.app;

import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;

import com.gc.materialdesign.widgets.SnackBar;
import com.parse.Parse;
import com.parse.ParseObject;
import com.vagnnermartins.adbelem.R;
import com.vagnnermartins.adbelem.constants.Keys;
import com.vagnnermartins.adbelem.parse.ChurchParse;
import com.vagnnermartins.adbelem.parse.EventParse;
import com.vagnnermartins.adbelem.parse.WorshipParse;
import com.vagnnermartins.adbelem.util.ConnectionDetectorUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by vagnnermartins on 26/12/14.
 */
public class App extends Application {

    public List<ChurchParse> listSectors;
    public List<ChurchParse> listCongragations;
    public List<ChurchParse> allChurches;
    public List<ChurchParse> myChurches;
    public ChurchParse selectedChurch;
    public Map<String, List<ChurchParse>> mapCongragations;
    public Map<String, List<WorshipParse>> mapWorship;
    public Map<String, List<EventParse>> mapEvents;
    private List<AsyncTask<?, ?, ?>> tasks;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        initParse();
        allChurches = new ArrayList<>();
        mapWorship = new HashMap<>();
        mapCongragations = new HashMap<>();
        mapEvents = new HashMap<>();
        tasks = new ArrayList<>();
    }

    private void initParse() {
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(ChurchParse.class);
        ParseObject.registerSubclass(WorshipParse.class);
        ParseObject.registerSubclass(EventParse.class);
        Parse.initialize(this, Keys.APP_ID, Keys.CLIENT_KEY);
    }

    public boolean isInternetConnection(Activity activity){
        ConnectionDetectorUtils cd = new ConnectionDetectorUtils(this);
        if (!cd.isConnectingToInternet()) {
            new SnackBar(activity, activity.getString(R.string.exception_erro_err_internet_disconnected)).show();
            return false;
        }
        return true;
    }

    public void registerTask(AsyncTask task){
        if(task != null){
            tasks.add(task);
        }
    }

    public void unregisterTask(AsyncTask task){
        if(task != null){
            task.cancel(true);
            tasks.remove(task);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterAllTasks();
    }

    private void unregisterAllTasks() {
        for(AsyncTask task : tasks){
            task.cancel(true);
            tasks.remove(task);
        }
    }
}
