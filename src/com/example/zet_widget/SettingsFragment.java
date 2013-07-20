package com.example.zet_widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;



public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener{
	
	OnSharedPreferenceChangeListener listener;
	public static String smjer;
	public static boolean flagStartSmjer = true, flagStartGrad = true, flagStartLinija = true;

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SettingsFragment"," onCreate Method - Loading the fragment layout");
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        fragmentUpdate(getActivity().getApplicationContext(), 
        		PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()), "");
        prefListener();
    }



	
	
	public void fragmentUpdate(Context context, SharedPreferences sharedPref, String key){
		Log.d("***", "in fragment Update");
		
		ListPreference list_linija = (ListPreference)findPreference("pref_linija");
		ListPreference list_update = (ListPreference)findPreference("pref_update_interval");
		ListPreference list_smjer = (ListPreference)findPreference("pref_smjer");
		ListPreference list_grad = (ListPreference)findPreference("pref_grad");
		
		String grad = sharedPref.getString("pref_grad", "Odaberi grad");
		list_grad.setSummary(grad);
		
		if(list_grad.getValue().equalsIgnoreCase("Zagreb")){
			
			list_linija.setEntries(R.array.pref_listArray_linije_zagreb);
			list_linija.setEntryValues(R.array.pref_listArray_values_linije_zagreb);
			
			if(key.equalsIgnoreCase("pref_grad") || flagStartGrad){
				list_linija.setValue("202");
				flagStartGrad = false;
			}
			
		}
		else if(list_grad.getValue().equalsIgnoreCase("Rijeka")){
			list_linija.setEntries(R.array.pref_listArray_linije_rijeka);
			list_linija.setEntryValues(R.array.pref_listArray_values_linije_rijeka);
			
			if(key.equalsIgnoreCase("pref_grad") || flagStartGrad){
				list_linija.setValue("1");
				flagStartGrad = false;
			}
			
		}
		else if(list_grad.getValue().equalsIgnoreCase("Pula")){
			list_linija.setEntries(R.array.pref_listArray_linije_pula);
			list_linija.setEntryValues(R.array.pref_listArray_values_linije_pula);
			if(key.equalsIgnoreCase("pref_grad") || flagStartGrad){
				list_linija.setValue("2a");
				flagStartGrad = false;
			}
			
		}
		
		
		//setting smjer preferences
		list_smjer.setEntries(Direction.getDirectionsDouble((String) list_linija.getEntry()));
		list_smjer.setEntryValues(Direction.getDirectionsSingle((String) list_linija.getEntry()));
		
		
		if(flagStartSmjer || key.equals("pref_linija")){ 
			list_smjer.setValue((Direction.getDirection((String) list_linija.getEntry())));
			flagStartSmjer = false;
		}
			
		
		String summary_pref_linija = sharedPref.getString("pref_linija", "Odaber liniju");
		String summary_pref_update_interval = sharedPref.getString("pref_update_interval", "");
		
		list_linija.setSummary(summary_pref_linija);
		list_update.setSummary(summary_pref_update_interval + " sekundi");
		list_smjer.setSummary(list_smjer.getEntry());
		smjer = Direction.removePrefix((String) list_smjer.getEntry());	
		
	}
	 
	@Override 
	public void onStop(){
		//Cancel the listener
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		Log.d("SettingsFragment"," onStop Method - unregisterOnSharedPreferenceChangeListener");
		prefs.unregisterOnSharedPreferenceChangeListener(listener);
		
		
		
		//Update the alarm! and set the broadcast to update the widget!
		Log.d("eee", "Updating from fragment");
		Context context = getActivity().getApplicationContext();
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		
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
		
		
		super.onStop();
		
	}
	
	
	@Override
	public void onStart(){
		//Register the listener
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		Log.d("SettingsFragment"," onStart Method - registerOnSharedPreferenceChangeListener");
		prefs.registerOnSharedPreferenceChangeListener(listener);
		super.onStart();
	}
	
	public void prefListener(){
		
		Log.d("fragment"," prefListener Method");
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		prefs.registerOnSharedPreferenceChangeListener(listener);
		
		listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			@Override
			public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
				fragmentUpdate(getActivity().getApplicationContext(), prefs, key);
	
			}
		};

		prefs.registerOnSharedPreferenceChangeListener(listener);
	}


	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
		String key) {
	// TODO Auto-generated method stub
	//fragmentUpdate(getActivity().getApplicationContext(), sharedPreferences, "");
	Log.d("fragmenttttt"," prefListener Method");
	}
	
}
