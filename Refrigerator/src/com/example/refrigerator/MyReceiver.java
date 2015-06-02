package com.example.refrigerator;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
	String name;
	String buyYear, buyMonth, buyDay;
	int alarmCode;
	
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        //Intent service1 = new Intent(context, MyAlarmService.class);
        //context.startService(service1);
    	
    	name = intent.getExtras().getString("foodName");
    	buyYear = intent.getExtras().getString("buyYear");
    	buyMonth = intent.getExtras().getString("buyMonth");
    	buyDay = intent.getExtras().getString("buyDay");
    	alarmCode = intent.getExtras().getInt("alarmCode");

        Intent intent1 = new Intent(context.getApplicationContext(), MainActivity.class);


        NotificationManager notifier = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notify = new Notification(R.drawable.ic_launcher, "alarm!",
                System.currentTimeMillis());

        PendingIntent pendingNotificationIntent = PendingIntent.getActivity( context.getApplicationContext(), alarmCode, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        notify.setLatestEventInfo(context, "AlarmApp", name, pendingNotificationIntent);

        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notify.vibrate = new long[] { 200, 200, 500, 300 };

        notifier.notify(alarmCode, notify);

        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
