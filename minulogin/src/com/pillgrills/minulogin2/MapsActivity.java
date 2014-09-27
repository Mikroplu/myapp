package com.pillgrills.minulogin2;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
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
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Messenger;
import android.location.*;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MapsActivity extends ActionBarActivity {

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
	String returnString= null;

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
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

			// Getting GPS status
			boolean isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// Getting network status
			boolean isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// No network provider is enabled
			}

			else {
				LocationListener locationListener = (LocationListener) new MyLocationListener();
				locationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, 5000, 10,
						locationListener);

				location = locationManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

				mylocation = new LatLng(location.getLatitude(),
						location.getLongitude());

				marker = map.addMarker(new MarkerOptions().position(mylocation)
						.title(descript));

			}

		} catch (NullPointerException e) {

			Toast.makeText(getApplicationContext(),
					"Error at initializing variables:__" + e.toString(), 1)
					.show();
		}

		// ****************************************************************************

	}

	public void sendToApplicationActivity(View view) {
		Intent intent = new Intent(this, ApplicationActivity.class);
		startActivity(intent);
		finish();

	}

	public void showMarker(View view) {
		//TODO
		if (marker == null) {
			marker = map.addMarker(new MarkerOptions().position(mylocation)
					.title("description"));
			
		}
		//TODO
		new PostLocation().execute("");
		
	}

	public void removeMarker(View view) {
		if (marker != null) {
			marker.remove();
			marker = null;
		}
	}

	public class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location arg0) {
			mylocation = new LatLng(arg0.getLatitude(), arg0.getLongitude());

		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub

		}

	}
	
	
	private class PostLocation extends AsyncTask<String, Void, String> {
		HttpPost httppost;
		HttpResponse response;
		HttpClient httpclient;
		List<NameValuePair> nameValuePairs;
		InputStream inputStream;

		@Override
		protected String doInBackground(String... params) {
			try {
				String latitude =  String.valueOf(mylocation.latitude);
				String longitude = String.valueOf(mylocation.longitude);
				String username = "Priit";
				httpclient = new DefaultHttpClient();
				httppost = new HttpPost("http://mikroplu.co.nf/Addlocation.php");
				// Add your data
				nameValuePairs = new ArrayList<NameValuePair>(3);
				nameValuePairs.add(new BasicNameValuePair("username", username
						.trim()));
				nameValuePairs.add(new BasicNameValuePair("latitude", latitude
						.trim()));
				nameValuePairs.add(new BasicNameValuePair("longitude", longitude
						.trim()));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				response = httpclient.execute(httppost);
				inputStream = response.getEntity().getContent();
				returnString = convertStreamToString(inputStream);
				inputStream.close();
			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("DATABASE", returnString);
			Toast.makeText(getApplicationContext(), returnString, 2).show();
		}

		String convertStreamToString(java.io.InputStream is) {
			java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
		}
	}

}
