package com.wny.wecare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionDefaultAudience;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.OnErrorListener;
import com.wny.wecare.handler.JSONParser;


public class LoginActivity extends FragmentActivity implements OnClickListener {

	// url to create new user
	private static String url_create_user = "http://infinitycodeservices.com/create_user.php";

	public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
			"[a-zA-Z0-9+._%-+]{1,256}" +
					"@" +
					"[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" +
					"(" +
					"." +
					"[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" +
					")+"
			);

	// Progress Dialog
	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();
	EditText inputEmail;
	Button btnLogin;

	public String strUid;
	private String TAG = "MainActivity";
	private Session.StatusCallback statusCallback = new SessionStatusCallback();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);

		Session session = new Session(getApplicationContext());
		Session.setActiveSession(session);
		session.openForRead(new Session.OpenRequest(LoginActivity.this).setDefaultAudience(SessionDefaultAudience.ONLY_ME).setPermissions(Arrays.asList("public_profile", "user_friends", "email")).setCallback(statusCallback));
		Session.setActiveSession(session);

		// Edit Text
		inputEmail = (EditText) findViewById(R.id.email);

		// Create button
		Button btnCreateUser = (Button) findViewById(R.id.btnEmail);

		// button click event
		btnCreateUser.setOnClickListener(new View.OnClickListener() {




			@Override
			public void onClick(View view) {
				String email=inputEmail.getText().toString();
				if(checkEmail(email)) {
					Toast.makeText(LoginActivity.this,"Valid Email Addresss", Toast.LENGTH_SHORT).show();
					
					new CreateNewUser().execute();

					startActivity(new Intent(LoginActivity.this, MainActivity.class));
					finish();

					// creating new user in background thread 
					
				}       	
				else
					Toast.makeText(LoginActivity.this,"Invalid Email Addresss", Toast.LENGTH_SHORT).show();
			}
		});

		onSessionStateChange();
	}

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange();
		}
	}

	private void onSessionStateChange() {
		Session session = Session.getActiveSession();
		if(session != null){
			if (session.isOpened()){

				Request getMe = Request.newMeRequest(session, new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if(user != null){
							finish();
						}
					}
				});
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		if (Session.getActiveSession() != null || Session.getActiveSession().isOpened()){
			Intent i = new Intent(LoginActivity.this,MainActivity.class);
			startActivity(i);


			LoginButton fbbutton = (LoginButton)
					findViewById(R.id.fbbutton);
			fbbutton.setOnErrorListener(new OnErrorListener() {

				@Override
				public void onError(FacebookException error) {
					Log.i(TAG, "Error " + error.getMessage());

				}

			});



		}

	}
	private boolean checkEmail(String email) {
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
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
			pDialog.setMessage("Logging In..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating User
		 * */
		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub

			// Setup ArrayList from main activity to store results
			ArrayList<Map<String, String>> emailList = new ArrayList<Map<String,  String>>();

			String email = ((EditText) findViewById(R.id.email)).getText().toString().trim();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("Email", email));

			// Creating JSON Parser object
			JSONParser jParser = new JSONParser();

			// Getting JSON string from URL
			JSONArray json = jParser.getJSONFromUrl(url_create_user, params);

			for (int i = 0; i < json.length(); i++)	{
				HashMap<String, String> map = new HashMap<String, String>();

				try	{
					JSONObject c = (JSONObject) json.get(i);
					//Fill map
					Iterator iter = c.keys();
					while(iter.hasNext())	{
						String currentKey = (String) iter.next();
						map.put(currentKey, c.getString(currentKey));
					}
					emailList.add(map);

				}
				catch (JSONException e) {
					e.printStackTrace();

				}

			};
			
			strUid = emailList.get(0).get("UserID");
			MainActivity.setUserID(strUid);
			
			/******* Create SharedPreferences *******/
			SharedPreferences pref = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = pref.edit();
			/**************** Storing data as KEY/VALUE pair *******************/
			editor.putString("uid", strUid);
			editor.putString("email", inputEmail.getText().toString());
			// Save the changes in SharedPreferences
			editor.commit();
			/**************** Get SharedPreferences data *******************/
			pref.getString("email", null);
			
			

			return null;

		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * *
		 */
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			//pDialog.dismiss();
			if (file_url != null) {
				Toast.makeText(LoginActivity.this, file_url, Toast.LENGTH_LONG).show();
			}

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}


}
