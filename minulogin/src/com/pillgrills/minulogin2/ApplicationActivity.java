package com.pillgrills.minulogin2;

import java.io.ByteArrayOutputStream;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ApplicationActivity extends ActionBarActivity {
	private ImageButton user_image;
	private EditText user_description;
	private TextView user_name;
	private Bitmap photo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userprofile_screen);
		user_description = (EditText) findViewById(R.id.editText1);
		user_name = (TextView) findViewById(R.id.textView1);
		user_name.setText(getIntent().getStringExtra("USER_NAME"));
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
		Intent intent = new Intent(this, MapsActivity.class);
		intent.putExtra("USER_DESCRIPTION", user_description.getText()
				.toString());
		intent.putExtra("USER_NAME", user_name.getText().toString());

		// Convert to byte array
		if (photo != null) {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();

			photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] byteArray = stream.toByteArray();

			intent.putExtra("USER_PHOTO", byteArray);
		}

		startActivity(intent);
		finish();
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

}
