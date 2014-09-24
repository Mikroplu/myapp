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

import android.app.Dialog;
import android.app.ProgressDialog;
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

public class MainActivity extends ActionBarActivity {
	EditText username_field;
	EditText password_field;
	private ProgressDialog prgDialog;
	String name = "", pass = "";
	byte[] data;
	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	InputStream inputStream;
	private boolean loggedin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen);

		username_field = (EditText) findViewById(R.id.username);
		password_field = (EditText) findViewById(R.id.password);
		new GetData().execute("");
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

	public void sendToRegisterActivity(View view) {
		Intent intent = new Intent(this, RegisterActivity.class);
		if (!username_field.getText().toString().equals("")) {
			intent.putExtra("USERNAME_FIELD_VALUE", username_field.getText()
					.toString());
		} else {
			intent.putExtra("USERNAME_FIELD_VALUE", "");
		}
		startActivity(intent);
		finish();
	}

	public void sendToApplicationActivity(View view) {
		new GetData().execute("");
		
		
		
		
		
		Intent intent = new Intent(this, ApplicationActivity.class);
		intent.putExtra("USER_NAME", username_field.getText().toString());
		startActivity(intent);
		finish();

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0:
			prgDialog = new ProgressDialog(this);
			prgDialog.setMessage("Downloading Mp3 file. Please wait...");
			prgDialog.setIndeterminate(false);
			prgDialog.setMax(100);
			prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			prgDialog.setCancelable(false);
			prgDialog.show();
			return prgDialog;
		default:
			return null;
		}
	}

	private class GetData extends AsyncTask<String, Void, String> {
		String name = "Jaanus";
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
				httppost = new HttpPost("http://mikroplu.co.nf/Login.php");
				// Add your data
				nameValuePairs = new ArrayList<NameValuePair>(1);
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
			//Log.d("DATABASE",returnString);
			Toast.makeText(getApplicationContext(), returnString, 4).show();

		}

		String convertStreamToString(java.io.InputStream is) {
			java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
		}
	}

}
