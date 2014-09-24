package com.pillgrills.minulogin2;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Messenger;
import android.location.*;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MapsActivity extends ActionBarActivity implements
		android.location.LocationListener {

	GoogleMap map;
	String descript;
	String name;
	LatLng mylocation;
	Marker marker = null;
	SupportMapFragment fm;
	Location location;
	Messenger mService = null;
	boolean mIsBound;
	Intent intent;
	LocationManager locationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maps_screen);

		// Initializing variables
		// ****************************************************

		try {
			intent = getIntent();
			descript = intent.getStringExtra("USER_DESCRIPTION");
			name = intent.getStringExtra("USER_NAME");
			fm = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);
			map = fm.getMap();
			locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			mylocation = new LatLng(location.getLatitude(),
					location.getLongitude());
			marker = map.addMarker(new MarkerOptions().position(mylocation)
					.title(descript));

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"Error at initializing variables:__" + e.toString(), 1).show();
		}

		// ****************************************************************************

		if (location != null) {
			onLocationChanged(location);
		}

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				20000, 0, this);

	}

	public void sendToApplicationActivity(View view) {
		Intent intent = new Intent(this, ApplicationActivity.class);
		startActivity(intent);
		finish();

	}

	@Override
	public void onLocationChanged(Location location) {

		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		mylocation = new LatLng(latitude, longitude);

		// For debugging
		String a = String.valueOf(latitude);
		String b = String.valueOf(longitude);
		Log.d("LOCATION", "Latitude: " + a + " Longitude: " + b);

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		//TODO
	}

	public void showMarker(View view) {
		if (marker == null) {
			marker = map.addMarker(new MarkerOptions().position(mylocation)
					.title("description"));
		}
	}

	public void removeMarker(View view) {
		if (marker != null) {
			marker.remove();
			marker = null;
		}
	}

}
