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

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends ActionBarActivity {

	EditText username_field;
	EditText password_field;
	EditText passwordAgain_field;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_screen);

		username_field = (EditText) findViewById(R.id.username);
		password_field = (EditText) findViewById(R.id.password);
		passwordAgain_field = (EditText) findViewById(R.id.password2);

		Intent intent = getIntent();
		String username = intent.getStringExtra("USERNAME_FIELD_VALUE");
		username_field.setText(username);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
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

	public void registerUser(View view) {
		String username = username_field.getText().toString();
		String password = password_field.getText().toString();
		String password_again = passwordAgain_field.getText().toString();

		Log.d("PASSWORDS", "_" + username + "_");
		Log.d("PASSWORDS", "_" + password + "_");
		Log.d("PASSWORDS", "_" + password_again + "_");

		if (checkIfValidForRegistration(username, password, password_again)) {
			// TODO

		}

	}

	public boolean checkIfValidForRegistration(String username,
			String password, String password_again) {
		if (!password.equals("") && !password_again.equals("")
				&& !username.equals("")) {
			if (password.length() > 6) {
				if (password.equals(password_again)) {
					Toast.makeText(getApplicationContext(),
							"Successfully registered!", 2).show();
					return true;
				} else {
					Toast.makeText(getApplicationContext(),
							"Passwords don't match", 2).show();
					return false;
				}
			} else {
				Toast.makeText(getApplicationContext(), "Password too short", 2)
						.show();
				return false;
			}
		} else {
			Toast.makeText(getApplicationContext(),
					"One of the fields is missing", 2).show();
			return false;
		}
	}

	private class SendData extends AsyncTask<String, Void, String> {
		String name = "Jaanus";
		String pass = "12345";
		HttpPost httppost;
		HttpResponse response;
		HttpClient httpclient;
		List<NameValuePair> nameValuePairs;
		InputStream inputStream;
		String returnString;

		@Override
		protected String doInBackground(String... params) {
			try {
				httpclient = new DefaultHttpClient();
				httppost = new HttpPost("http://mikroplu.co.nf/Register.php");
				// Add your data
				nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("username", name
						.trim()));
				nameValuePairs.add(new BasicNameValuePair("username", name
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
			// Log.d("DATABASE",returnString);
			Toast.makeText(getApplicationContext(), returnString, 4).show();

		}

		String convertStreamToString(java.io.InputStream is) {
			java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
		}
	}
}
