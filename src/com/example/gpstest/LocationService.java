package com.example.gpstest;

import java.util.Date;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocationService extends Service {
	public static final String BROADCAST_ACTION = "LocationService";
	public LocationManager locationManager;
	public MyLocationListener listener;
	
	public Location previousBestLocation = null;
	
	private static final String TAG = "myLogs";
	
	private boolean gpsEnable, networkEnable;

	Intent intent;
	int counter = 0;
	
	NotificationManager nm;

	@Override
	public void onCreate() {
		super.onCreate();
		intent = new Intent(BROADCAST_ACTION);
	}

	@Override
	public void onStart(Intent intent, int startId) {      
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		listener = new MyLocationListener();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4*1000, 10, listener);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {    
		super.onDestroy();
		Log.d(TAG,"STOP_SERVICE");
		locationManager.removeUpdates(listener);        
	} 

	public class MyLocationListener implements LocationListener {

		public void onLocationChanged (final Location location) {
			showLocation(location);
		}

		public void onProviderDisabled (String provider) {
			checkEnabled();
		}


		public void onProviderEnabled (String provider) {
			checkEnabled();
	    	showLocation(locationManager.getLastKnownLocation(provider));
		}


		public void onStatusChanged (String provider, int status, Bundle extras) {
			if (provider.equals(LocationManager.GPS_PROVIDER)) {
	    		Log.d(TAG, "GPS status: " + String.valueOf(status));
	    	}
	    	else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
	    		Log.d(TAG, "NETWORK status: " + String.valueOf(status));
	    	}
		}
		
		private void showLocation (Location location) {
			if (location == null) return;
		    if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
		    	Log.d(TAG, "GPS: " + formatLocation(location));
		    }
		    else if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
		    	Log.d(TAG, "NETWORK: " + formatLocation(location));
		    }
		}

		private String formatLocation(Location location) {
			if (location == null) return "";
		    return String.format(
		        "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT",
		        location.getLatitude(), location.getLongitude(), new Date(location.getTime()));
		}

		private void checkEnabled() {
		    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) != gpsEnable) {
		    	Log.d(TAG, "GPS enabled: " + locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
		    	gpsEnable = !gpsEnable;
		    }

		    if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) != networkEnable) {
		    	Log.d(TAG, "NETWORK enabled: " + locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
		    	networkEnable = !networkEnable;
		    }
		}

	}
}