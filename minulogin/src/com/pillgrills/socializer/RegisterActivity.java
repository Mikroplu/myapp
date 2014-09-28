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

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
	String name, pass;
	String returnString = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_screen);
		name = null;
		pass = null;
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
		name = username_field.getText().toString();
		pass = password_field.getText().toString();
		
		
		String password_again = passwordAgain_field.getText().toString();

		if (checkIfValidForRegistration(name, pass, password_again) && checkIfInternet()) {
			new SendData().execute("");
		}
		if(!checkIfInternet()){
			Toast.makeText(getApplicationContext(),
					"njetu internet!", 2).show();
		}

	}

	public boolean checkIfValidForRegistration(String username,
			String password, String password_again) {
		if (!password.equals("") && !password_again.equals("")
				&& !username.equals("")) {
			if (password.length() > 6) {
				if (password.equals(password_again)) {
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
			Toast.makeText(getApplicationContext(), "Some fields are empty", 2)
					.show();
			return false;
		}
	}

	private class SendData extends AsyncTask<String, Void, String> {
		HttpPost httppost;
		HttpResponse response;
		HttpClient httpclient;
		List<NameValuePair> nameValuePairs;
		InputStream inputStream;

		@Override
		protected String doInBackground(String... params) {
			try {
				httpclient = new DefaultHttpClient();
				httppost = new HttpPost("http://mikroplu.co.nf/Register.php");
				// Add your data
				nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("username", name
						.trim()));
				nameValuePairs.add(new BasicNameValuePair("password", pass
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
			if (returnString.equals("OK")) {
				Toast.makeText(getApplicationContext(), "New user added", 4)
						.show();
			} else if (returnString.equals("NO")) {
				Toast.makeText(getApplicationContext(), "User already exists",
						4).show();
			} else {
				Toast.makeText(getApplicationContext(), "Something went wrong",
						4).show();
			}
		}

		String convertStreamToString(java.io.InputStream is) {
			java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
		}
	}

	boolean checkIfInternet() {
		ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conMgr.getActiveNetworkInfo();
		if (i == null)
			return false;
		if (!i.isConnected())
			return false;
		if (!i.isAvailable())
			return false;
		return true;
	}
}
