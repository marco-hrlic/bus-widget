package com.example.zet_widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;
import android.widget.RemoteViews;




public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener{
	
	// Listener for preference changed
	OnSharedPreferenceChangeListener listener;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d("SettingsActivity"," onCreate Method");
		
		// Display the fragment as the main content.
		getFragmentManager().beginTransaction()
		.replace(android.R.id.content, new SettingsFragment())
		.commit();
	
		// Set up the pref_change_listener method
		prefListener();
		
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);    
	        
	}
		
	
	@Override
	protected void onResume(){
		super.onResume();
		
		Log.d("SettingsActivity"," onResume Method - start");
		//Register the listener
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Log.d("SettingsActivity"," onResume Method - registerOnSharedPreferenceChangeListener");
		prefs.registerOnSharedPreferenceChangeListener(listener);      
	}
	
	
	@Override	 
	protected void onStop(){
		
		//Cancel the listener
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Log.d("SettingsActivity"," onStop Method - unregisterOnSharedPreferenceChangeListener");
		prefs.unregisterOnSharedPreferenceChangeListener(listener);
		
		super.onStop();
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		
		Log.d("SettingsActivity"," onPause Method - start");
		//Cancel the listener
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Log.d("SettingsActivity"," onPause Method - unregisterOnSharedPreferenceChangeListener");
		prefs.unregisterOnSharedPreferenceChangeListener(listener);
       
	}

		
	
		
	/*
	 *  This method sets up a preference_change_listener to be called onCreate
	 */
	public void prefListener(){
		
		Log.d("SettingsActivity"," prefListener Method");
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		
		listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			
			public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
				Log.d("SettingsActivity", "onSharedPreferenceChanged Method");
					
				//Get context and the remoteViews
				Context context = getApplicationContext();
				RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);	
							
				//Get the preferences
				SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
				
				
				//get the widget
				ComponentName thiswidget = new ComponentName(context, ExampleAppWidgetProvider.class);
				//get the widget manager
				AppWidgetManager manager = AppWidgetManager.getInstance(context);
				
				
				
				/**************************************/
				
				if (key.equals("pref_linija") || key.equals("pref_smjer")){
					//Update the Naziv linije
					Log.d("SettingsActivity","onSharedPreferenceChanged - updating naziv_linije");
					String syncConnPref = sharedPref.getString("pref_linija", "");
					views.setTextViewText(R.id.nazivlinije, syncConnPref.trim());
					

					
					Log.d("SettingsActivity","onSharedPreferenceChanged - updating naziv_linije - done");
					
					
					String smjer = sharedPref.getString("pref_smjer", "202");
					Time last_time_updated = new Time();
					last_time_updated.setToNow();
					String data = XmlParser.parseXml(last_time_updated, syncConnPref, smjer, context);
					if (data != null) views.setTextViewText(R.id.widgetlabel, data);  else
						views.setTextViewText(R.id.widgetlabel, "error");
		
				
					
				}
			
				views.setTextViewText(R.id.direction, SettingsFragment.smjer);
				
				/**************************************/
				
/*
				if (key.equals("pref_update_interval")){
					//update the update_interval
					Log.d("SettingsActivity","onSharedPreferenceChanged - updating update_interval");
				
					//Create the intent, and pendingIntent
					Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
					PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
					
					//First cancel the current alarm
					//Get the alarmManager
					AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
					//Cancel it!
					alarmManager.cancel(pi);
	
					// get the update period from the settings in seconds
					int updatePeriod = Integer.valueOf(sharedPref.getString("pref_update_interval", "60"));
				
					//set the repeating alarm!
					alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000*updatePeriod , pi);

					Log.d("SettingsActivity","onSharedPreferenceChanged - updating update_interval - done\n**updatePeriod = "
							+ Integer.toString(updatePeriod) + " sec");

				
				}
				*/
				
				/**************************************/
				
				//update the widget
				manager.updateAppWidget(thiswidget, views);
				Log.d("SettingsActivity","onSharedPreferenceChanged - widget updated");
				
			}
		};

		prefs.registerOnSharedPreferenceChangeListener(listener);
	}


	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		
	}

	
	
	
}
