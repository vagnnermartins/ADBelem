package com.vagnnermartins.adbelem.receive;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.vagnnermartins.adbelem.R;
import com.vagnnermartins.adbelem.parse.EventParse;
import com.vagnnermartins.adbelem.util.DataUtil;

import java.util.Calendar;
import java.util.Date;

public class EventAlarmReceiver extends BroadcastReceiver {

    public static final String TITLE = "title";
    public static final String DATE = "date";
    public static final String ID = "id";

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra(TITLE);
        Date date = new Date();
        long dateMessage = intent.getLongExtra(DATE, date.getTime());
        date.setTime(dateMessage);
        int id = intent.getIntExtra(ID, 0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(getMessage(date))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(getMessage(date)))
                        .setDefaults(Notification.DEFAULT_ALL);
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(id, mBuilder.build());
    }

    private static String getMessage(Date date) {
        return DataUtil.formatDateToString(date) + " às " + DataUtil.transformDateToSting(date, "HH:mm");
    }
}
