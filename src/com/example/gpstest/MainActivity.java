package com.example.gpstest;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;


public class MainActivity extends Activity {
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Intent service = new Intent(context, LocationService.class);
		    context.startService(service);
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
    public void onClickStart(View v) {
        startService(new Intent(this, LocationService.class));
      }
      
      public void onClickStop(View v) {
        stopService(new Intent(this, LocationService.class));
      }
	
	@Override
	  protected void onResume() {
	    super.onResume();
	    Intent intent = new Intent(this, LocationService.class);
	    startService(intent);
	    registerReceiver(receiver, new IntentFilter(LocationService.BROADCAST_ACTION));
	  }
	
	  @Override
	  protected void onPause() {
	    super.onPause();
	    unregisterReceiver(receiver);
	  }
		 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onClickLocationSettings(View view) {
	    startActivity(new Intent( android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	}
}