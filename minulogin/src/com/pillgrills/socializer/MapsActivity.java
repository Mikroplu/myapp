package com.pillgrills.socializer;

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

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MapsActivity extends ActionBarActivity {

	GoogleMap map;
	String descript;
	String name;
	LatLng mylocation = null;
	Marker marker = null;
	SupportMapFragment fm;
	Messenger mService = null;
	boolean mIsBound;
	Intent intent;
	LocationManager manager;
	LocationListener listener;
	int testingcounter = 0;
	String returnString;
	String latitude;
	String longitude;
	String user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maps_screen);

		// Initializing variables
		// ****************************************************

		try {

			user = getIntent().getStringExtra("USERNAME");

			Toast.makeText(getApplicationContext(),
					getIntent().getStringExtra("USERNAME"), 1).show();

			// Getting googlemaps fragment
			fm = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);

			// Initializing googlemaps using fragment
			map = fm.getMap();

			// Initializing manager
			manager = (LocationManager) this
					.getSystemService(Context.LOCATION_SERVICE);

			// If lastknownlocaiton is available make that our current location
			Location location = manager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);

			if (location != null) {
				mylocation = new LatLng(location.getLatitude(),
						location.getLongitude());
			}

			// Creating listener
			listener = new LocationListener() {

				@Override
				public void onStatusChanged(String provider, int status,
						Bundle extras) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onProviderEnabled(String provider) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onProviderDisabled(String provider) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onLocationChanged(Location location) {
					mylocation = new LatLng(location.getLatitude(),
							location.getLongitude());
					new GetData().execute("");
					

				}
			};

			// Telling manager to start listening for location updates
			manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
					listener);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"Error at initializing variables:__" + e.toString(), 1)
					.show();
		}

		// ****************************************************************************

	}

	public void sendToApplicationActivity(View view) {
		// Back button
		Intent intent = new Intent(this, ApplicationActivity.class);
		intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
		startActivity(intent);
		finish();

	}

	// This method gets called OnLocationChanged, and will put marker on your
	// changed location while removing previous marker
	public void updateMyLocation() {

		if (marker == null) {
			marker = map.addMarker(new MarkerOptions().position(mylocation)
					.title(user));
		}
		if (marker != null) {
			marker.remove();
			marker = map.addMarker(new MarkerOptions().position(mylocation)
					.title(user));

		}

	}

	public void shareLocation(View view) {
		new GetData().execute("");
	}

	private class GetData extends AsyncTask<String, Void, String> {
		HttpPost httppost;
		HttpResponse response;
		HttpClient httpclient;
		List<NameValuePair> nameValuePairs;
		InputStream inputStream;

		@Override
		protected String doInBackground(String... params) {
			try {
				latitude = String.valueOf(mylocation.latitude);
				longitude = String.valueOf(mylocation.longitude);
				user = getIntent().getStringExtra("USERNAME");

				httpclient = new DefaultHttpClient();
				httppost = new HttpPost("http://mikroplu.co.nf/Locations.php");
				// Add your data
				nameValuePairs = new ArrayList<NameValuePair>(3);

				nameValuePairs.add(new BasicNameValuePair("username", user
						.trim()));

				nameValuePairs.add(new BasicNameValuePair("latitude", latitude
						.trim()));

				nameValuePairs.add(new BasicNameValuePair("longitude",
						longitude.trim()));

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
			if (returnString != null) {
				
				updateMyLocation();
				updateUserLocations();

				Log.d("LOCATION", returnString);

			} else {
				Toast.makeText(getApplicationContext(), "Something went wrong",
						4).show();
				// TODO errorhandling
			}
		}

		String convertStreamToString(java.io.InputStream is) {
			java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
		}

	}

	public void updateUserLocations() {
		if (returnString != null) {
			map.clear();
			String data = returnString;
			String[] array = data.split("/");
			for (String input : array) {
				String[] rida = input.split(" ");
				String name = rida[0];
				Double latitude = Double.parseDouble(rida[1]);
				Double longitude = Double.parseDouble(rida[2]);

				map.addMarker(new MarkerOptions().position(
						new LatLng(latitude, longitude)).title(name));

			}
		} else {
			Toast.makeText(getApplicationContext(), "Something went wrong", 4)
					.show();
			// TODO errorhandling
		}

	}

}
