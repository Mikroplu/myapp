package com.pillgrills.socializer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ApplicationActivity extends ActionBarActivity {
	private ImageButton user_image;
	private EditText user_description;
	private TextView user_name;
	private Bitmap photo;
	private LocationManager manager;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Toast.makeText(getApplicationContext(), getIntent().getStringExtra("USERNAME"), 1).show();
		setContentView(R.layout.userprofile_screen);
		manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		user_description = (EditText) findViewById(R.id.editText1);
		user_name = (TextView) findViewById(R.id.textView1);
		user_image = (ImageButton) findViewById(R.id.imageButton1);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void sendToMainActivity(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	public void sendToMapsActivity(View view) {
		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			buildAlertMessageNoGps();
		}

		if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			
			Intent intent = new Intent(this, MapsActivity.class);
			intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
			startActivity(intent);
			finish();
		}
	}

	public void startCamera(View view) {
		Intent cameraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent, 1888);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1888 && resultCode == RESULT_OK) {
			photo = (Bitmap) data.getExtras().get("data");
			user_image.setImageBitmap(photo);

		}
	}

	private void buildAlertMessageNoGps() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"Your GPS seems to be disabled, do you want to enable it?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(
									@SuppressWarnings("unused") final DialogInterface dialog,
									@SuppressWarnings("unused") final int id) {
								startActivity(new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,
							@SuppressWarnings("unused") final int id) {
						dialog.cancel();
					}
				});
		final AlertDialog alert = builder.create();
		alert.show();
	}

}
