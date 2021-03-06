package com.example.zet_widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;



public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d("MainActivity", "onCreate");
		
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		Log.d("MainActivity", "onCreateOptionsMenu");
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/*
	 * Start the settings when selected in the action bar
	 */
	public boolean onOptionsItemSelected(MenuItem item){
		// Display the fragment as the main content.
	
		Log.d("MainActivity", "onOptionsItemSelected");
		
		Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
		startActivity(intent);
		return true;
		
	}

	
	
}
