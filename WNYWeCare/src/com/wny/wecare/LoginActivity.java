package com.wny.wecare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wny.wecare.handler.JSONParser;


public class LoginActivity extends Activity implements OnClickListener {

	// Progress Dialog
	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();
	EditText inputEmail;
	Button btnLogin;

	public String strUid;

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

			    //SAVE
			    SharedPreferences ui = getSharedPreferences("UserInfo", MODE_PRIVATE);
			    SharedPreferences.Editor edUi = ui.edit();
			    edUi.putString("uid", strUid);
			    edUi.putString("email", email);
			    edUi.commit();

			    startActivity(new Intent(LoginActivity.this, MainActivity.class));
			    finish();

			    // Returns Toast "Login success!"
			    //return json.getString(TAG_MESSAGE);
			
            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(LoginActivity.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
}


