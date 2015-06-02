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

    	name = intent.getExtras().getString("foodName");
    	buyYear = intent.getExtras().getString("buyYear");
    	buyMonth = intent.getExtras().getString("buyMonth");
    	buyDay = intent.getExtras().getString("buyDay");
    	alarmCode = intent.getExtras().getInt("alarmCode");

        Intent intent1 = new Intent(context.getApplicationContext(), MainActivity.class);


        NotificationManager notifier = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent pendingNotificationIntent = PendingIntent.getActivity( context.getApplicationContext(), alarmCode, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        
        Notification notify = new Notification.Builder(context)
        .setWhen(System.currentTimeMillis())
        .setTicker("자도냉 유통기한이 다 됐습니다.")
        .setContentTitle("자도냉 유통기한 알림")
        .setStyle(new Notification.BigTextStyle()
         .bigText(buyYear+"년"+buyMonth+"월"+buyDay+"일에 구매한\n" + name + "의 유통기한이 만료되었습니다."))
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentIntent(pendingNotificationIntent)
        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
        .setAutoCancel(true).build();

        notifier.notify(alarmCode, notify);

        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
