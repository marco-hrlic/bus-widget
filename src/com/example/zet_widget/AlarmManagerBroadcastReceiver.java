package com.example.zet_widget;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;
import android.widget.RemoteViews;



/**
  * 
  * This Broadcast Receiver is used to update the widget via the alarmManager
  * 
  */
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
	
	Time last_time_updated;
	
	@SuppressLint("Wakelock")
	@Override
	 public void onReceive(Context context, Intent intent) {
		
		//Get the widget id's
		int ids[] = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context,ExampleAppWidgetProvider.class));
		
		
		
		Log.d("AlarmManagerBroadcastreciver", "onRecieve Method - start");
		
		//If any widgets placed...
		if(ids.length > 0){
		
			PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
			PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "bTimeWidget");
			//Acquire the lock
			wl.acquire();
			
			//Get the remote view
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
	  
		
			//Updating the button listener!
			Log.d("AlarmManagerBroadcastreciver", "onRecieve Method - updating the button listener");
			Intent intent2 = new Intent(context, SettingsActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent2, 0);
			views.setOnClickPendingIntent(R.id.button, pendingIntent);
			
			
			//Get the preferences
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
				
			//Update the Naziv linije
			Log.d("AlarmManagerBroadcastreciver", "onRecieve Method - updating pref_linija");
			String syncConnPref = sharedPref.getString("pref_linija", "202");
			views.setTextViewText(R.id.nazivlinije, syncConnPref);
		
			//String smjer = sharedPreg.getString'
			//views.setTextViewText(R.id.direction,"" );
			
			//Update the last time update textView
			Log.d("AlarmManagerBroadcastreciver", "onRecieve Method - updating lasttime");
			Time last_time_updated = new Time();
			last_time_updated.setToNow();
			views.setTextViewText(R.id.lasttime, Integer.toString(last_time_updated.hour) + ":" 
					+ Integer.toString(last_time_updated.minute) + ":" 
					+ Integer.toString(last_time_updated.second));
		
			
			
			//Parsing the xml file and getting back the result
			
			String smjer = sharedPref.getString("pref_smjer", "");
			String data = XmlParser.parseXml(last_time_updated, syncConnPref, smjer, context);
			
			if (data != null) views.setTextViewText(R.id.widgetlabel, data); else
				views.setTextViewText(R.id.widgetlabel, "error");
			
			
			
			
			
			
			
			//get the widget
			ComponentName thiswidget = new ComponentName(context, ExampleAppWidgetProvider.class);
			//get the widget manager
			AppWidgetManager manager = AppWidgetManager.getInstance(context);
	
			Log.d("AlarmManagerBroadcastreciver", "onRecieve Method - updating the widget");
			manager.updateAppWidget(thiswidget, views);
	  
			//Release the lock
			wl.release(); 
			
		} 
		//if no widgets placed...
		else { 
			
			Log.d("AlarmManagerBroadcastreciver", "onRecieve Method - no widget placed/n**Alarm cancled");
			//cancel the alarm
			Intent intent3 = new Intent(context, AlarmManagerBroadcastReceiver.class);
			PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent3, 0);
			AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			alarmManager.cancel(sender);
		}
		
	 }
	
	
	
	
}
