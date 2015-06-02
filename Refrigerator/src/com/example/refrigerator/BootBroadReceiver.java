package com.example.refrigerator;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class BootBroadReceiver extends BroadcastReceiver {

	String dbName = "MyDB";
	SQLiteDatabase database;
	String createColdTable = "create table coldTable (id integer primary key ,name text , buyyear text , buymonth text , buyday text , limityear text ,limitmonth text , limitday text, notifyCheck int default 1);";
	String createCoolTable = "create table coolTable (id integer primary key ,name text , buyyear text , buymonth text , buyday text , limityear text ,limitmonth text , limitday text, notifyCheck int default 1);";
	ArrayList<String> foodInfoList = null;
	ArrayList<ArrayList<String>> refrigeratorList = null;
	PendingIntent pendingIntent;
	int alarmCode = 0;
	int currentYear = 0, currentMonth = 0, currentDay = 0;
	int limitYear = 0, limitMonth = 0, limitDay = 0;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

			refrigeratorList = new ArrayList<ArrayList<String>>();

			database = context.openOrCreateDatabase(dbName,
					context.MODE_MULTI_PROCESS, null);

			createColdTable();
			selectColdData();
			
			createCoolTable();
			selectCoolData();
			
			setNotification(context);
		}
	}


	private void setNotification(Context context) {
		Calendar calendar = Calendar.getInstance();

		currentYear = calendar.get(Calendar.YEAR);
		currentMonth = calendar.get(Calendar.MONTH)+1;
		currentDay = calendar.get(Calendar.DAY_OF_MONTH);

		for (int i = 0; i < refrigeratorList.size(); i++) {
			if (refrigeratorList.get(i).get(7).toString().equals("1")) {
				limitYear = Integer.parseInt(refrigeratorList.get(i).get(4)
						.toString());
				limitMonth = Integer.parseInt(refrigeratorList.get(i)
						.get(5).toString());
				limitDay = Integer.parseInt(refrigeratorList.get(i).get(6)
						.toString());
				
				if (currentYear <= limitYear && currentMonth <= limitMonth
						&& currentDay <= limitDay) {
					
					calendar.set(Calendar.YEAR, limitYear);
					calendar.set(Calendar.MONTH, limitMonth - 1);
					calendar.set(Calendar.DAY_OF_MONTH, limitDay);

					calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
					calendar.set(Calendar.AM_PM, calendar.get(Calendar.AM_PM));
					calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 1);
					calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND));

					Intent myIntent = new Intent(context.getApplicationContext(),MyReceiver.class);

					myIntent.putExtra("foodName", refrigeratorList.get(i).get(0)
							.toString());
					myIntent.putExtra("buyYear", refrigeratorList.get(i).get(1)
							.toString());
					myIntent.putExtra("buyMonth", refrigeratorList.get(i).get(2)
							.toString());
					myIntent.putExtra("buyDay", refrigeratorList.get(i).get(3)
							.toString());
					myIntent.putExtra("alarmCode", alarmCode);

					pendingIntent = PendingIntent.getBroadcast(
							context.getApplicationContext(), alarmCode,
							myIntent, 0);

					AlarmManager alarmManager = (AlarmManager) context
							.getSystemService(context.ALARM_SERVICE);
					
					alarmManager.set(AlarmManager.RTC,
							calendar.getTimeInMillis(), pendingIntent);
					
					alarmCode++;
				} 
			}
		}
	}

	public void createColdTable() {
		try {
			database.execSQL(createColdTable);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void selectColdData() {
		String sql = "select * from coldTable";
		Cursor result = database.rawQuery(sql, null);
		result.moveToFirst();
		while (!result.isAfterLast()) {
			foodInfoList = new ArrayList<String>();
			foodInfoList.add(result.getString(1));
			foodInfoList.add(result.getString(2));
			foodInfoList.add(result.getString(3));
			foodInfoList.add(result.getString(4));
			foodInfoList.add(result.getString(5));
			foodInfoList.add(result.getString(6));
			foodInfoList.add(result.getString(7));
			foodInfoList.add(result.getString(8));
			refrigeratorList.add(foodInfoList);
			result.moveToNext();
		}
		result.close();
	}
	
	private void createCoolTable() {
		try {
			database.execSQL(createCoolTable);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void selectCoolData() {
		String sql = "select * from coolTable";
		Cursor result = database.rawQuery(sql, null);
		result.moveToFirst();
		while (!result.isAfterLast()) {
			foodInfoList = new ArrayList<String>();
			foodInfoList.add(result.getString(1));
			foodInfoList.add(result.getString(2));
			foodInfoList.add(result.getString(3));
			foodInfoList.add(result.getString(4));
			foodInfoList.add(result.getString(5));
			foodInfoList.add(result.getString(6));
			foodInfoList.add(result.getString(7));
			foodInfoList.add(result.getString(8));
			refrigeratorList.add(foodInfoList);
			result.moveToNext();
		}
		result.close();
	}
}
