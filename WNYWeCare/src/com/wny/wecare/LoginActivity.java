package com.wny.wecare;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.wny.wecare.handler.JSONParser;


public class LoginActivity extends Activity implements OnClickListener {

	// Progress Dialog
	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();
	EditText inputEmail;
	Button btnLogin;

	// url to create new user
	private static String url_create_user = "http://infinitycodeservices.com/create_user.php";

	// JSON Node names
		private static final String TAG_SUCCESS = "success";

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.login_activity);

			// Edit Text
			inputEmail = (EditText) findViewById(R.id.email);
			

			// Create button
			Button btnCreateUser = (Button) findViewById(R.id.btnEmail);

			// button click event
			btnCreateUser.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					// creating new user in background thread
					new CreateNewUser().execute();
				}
			});
		}

		/**
		 * Background Async Task to Create new User
		 * */
		class CreateNewUser extends AsyncTask<String, String, String> {

			/**
			 * Before starting background thread Show Progress Dialog
			 * */
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(LoginActivity.this);
				pDialog.setMessage("Creating User..");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
			}

			/**
			 * Creating User
			 * */
			protected String doInBackground(String... args) {
				String email = inputEmail.getText().toString();
				

				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("email", email));
				

				// getting JSON Object
				// Note that create User url accepts POST method
				JSONObject json = jsonParser.makeHttpRequest(url_create_user,
						"POST", params);
				
				// check log cat fro response
				Log.d("Create Response", json.toString());

				// check for success tag
				try {
					int success = json.getInt(TAG_SUCCESS);

					if (success == 1) {
						// successfully created user
						Intent i = new Intent(getApplicationContext(), MainActivity.class);
						startActivity(i);
						
						// closing this screen
						finish();
					} else {
						// failed to create User
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				return null;
			}

			/**
			 * After completing background task Dismiss the progress dialog
			 * **/
			protected void onPostExecute(String file_url) {
				// dismiss the dialog once done
				pDialog.dismiss();
			}

		}

		@Override
		public void onConnectionFailed(ConnectionResult arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onConnected(Bundle arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onConnectionSuspended(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	}
