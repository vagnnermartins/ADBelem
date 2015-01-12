package com.vagnnermartins.adbelem.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.vagnnermartins.adbelem.parse.EventParse;
import com.vagnnermartins.adbelem.receive.EventAlarmReceiver;

import java.util.Calendar;

/**
 * Created by vagnnermartins on 11/01/15.
 */
public class AlarmUtil {

    public static void scheduledEventNotification(Context context, EventParse event) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 4);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int id = (int) event.getDate().getTime();
        Intent intent = new Intent(context, EventAlarmReceiver.class);
        intent.putExtra(EventAlarmReceiver.TITLE, event.getName());
        intent.putExtra(EventAlarmReceiver.DATE, event.getDate().getTime());
        intent.putExtra(EventAlarmReceiver.ID, id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, event.getDate().getTime(), pendingIntent);
    }

    public static void cancelScheduleEventNotification(Context context, EventParse event){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, EventAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) event.getDate().getTime(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }
}
