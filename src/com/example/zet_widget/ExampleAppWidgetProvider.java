package com.example.zet_widget;

/* This class is responsible for handling the widget
 *
 */



import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;




public class ExampleAppWidgetProvider extends AppWidgetProvider {

	/*
	 *  onDeleted Method
	 */
	@Override
	public void onDeleted(Context context, int[] appWidgetIds){
		
		Log.d("ExampleAppWidgetProvider", "onDelete Method");
		
		Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
		
		super.onDeleted(context,appWidgetIds);
		
	}
	
	
	/*
	 *  onEnabled Method
	 *  setup update with the alarmManager, also set the update interval here!
	 */
	
	@Override
	public void onEnabled(Context context){
		super.onEnabled(context);
		Log.d("ExampleAppWidgetProvider", "onEnabled Method");
		
		AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		
		
		//!!need to get the update time from the preferences!
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		//get the update period from the settings in seconds
		
		
		// !!!!!!!
		int updatePeriod = Integer.valueOf(prefs.getString("pref_update_interval", "5"));
		Log.d("ExampleAppWodgetProvider", Integer.toString(updatePeriod));
		
		//cancel the alarm if already running just in case
		am.cancel(pi);
		
		//set the alarm
		Log.d("ExampleAppWidgetProvider", "update_interval ");
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() , 1000*updatePeriod , pi);
		
		
	
	
	}
	
	
	
	
	/*
	 *  onDisabled Method. 
	 *  Disable the alarm so there are no wake_clock leaks!!!
	 */
	@Override
	public void onDisabled(Context context){
		
		Log.d("ExampleAppWidgetProvider", "onDisabled Method");
		
		Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
		
		
		super.onDisabled(context);
	}
	
	
	
	/*
	 *  onAppWidgetOptionsChanged Method
	 */
	@Override
	 public void onAppWidgetOptionsChanged(Context context,
	   AppWidgetManager appWidgetManager, int appWidgetId,
	   Bundle newOptions) {
		
		Log.d("ExampleAppWidgetProvider", "onAppWidgetOptionsChanged Method");
		
	  //Do some operation here, once you see that the widget has change its size or position.

	 }
	
	
	/*
	 *   onUpdate Method	
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	  
		Log.d("ExampleAppWidgetProvider", "onUpdate Method");
	  
		

	 }

}

