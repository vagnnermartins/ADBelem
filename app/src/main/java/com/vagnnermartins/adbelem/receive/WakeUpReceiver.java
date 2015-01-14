package com.vagnnermartins.adbelem.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.vagnnermartins.adbelem.parse.ChurchParse;
import com.vagnnermartins.adbelem.parse.EventParse;
import com.vagnnermartins.adbelem.util.AlarmUtil;

import java.util.List;

public class WakeUpReceiver extends BroadcastReceiver {

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        EventParse.findMyEventsInLocal(onFindMyEventsInLocal());
    }

    private FindCallback<EventParse> onFindMyEventsInLocal() {
        return new FindCallback<EventParse>() {
            @Override
            public void done(List<EventParse> result, ParseException e) {
                AlarmUtil.scheduleEventsNotification(context, result);
            }
        };
    }
}
