package com.wny.wecare.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wny.wecare.CustomOnItemSelectedListener;
import com.wny.wecare.MainActivity;
import com.wny.wecare.R;
import com.wny.wecare.handler.JSONParser;

public class HomeFragment extends Fragment implements OnItemSelectedListener, OnClickListener{

	private OnFragmentInteractionListener mListener;

	private Spinner spinner;
	private Button btnSubmit;
	private String selection;

	// JSON Node names

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_AGENCY = "agency";
	private static final String TAG_AID = "AgencyID";
	private static final String TAG_NAME = "AgencyName";

	private static final String[] list={"Alden", "Amherst", "Angola",
		"Blasdell", "Boston", "Bowmansville", "Buffalo", "Cheektowaga", "Clarence",
		"Depew", "Derby", "East Aurora", "Eden", "Elma", "Getzville", "Gowanda",
		"Grand Island", "Holland", "Irving", "Kenmore", "Lackawanna", "Lake View",
		"Lancaster", "Lawtons", "North Collins", "Orchard Park", "Snyder", "Springville",
		"Tonawanda", "West Seneca", "Williamsville"};

	// Setup ArrayList from main activity to store results
	ArrayList<HashMap<String, String>> resultsList = null;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	// products JSONArray
	JSONArray agency = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_home, container, false);

		btnSubmit = (Button) rootView.findViewById(R.id.town_search);
		btnSubmit.setOnClickListener(this);
		spinner =(Spinner)rootView.findViewById(R.id.spinner);
		spinner.setOnItemSelectedListener(this);

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);

		return rootView;

	}

	// Check if parent activity implements the interface
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener =
					(OnFragmentInteractionListener)  activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	//add items into spinner dynamically
	/*public void addItemsOnSpinner2(View v) {
		addItemsOnSpinner2(v);
		final List<String> list = new ArrayList<String>();
		list.add("list 1");
		list.add("list 2");
		list.add("list 3");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list);

		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

	}*/

	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.town_search:
			String town = (String) spinner.getSelectedItem().toString();
			agencySearch(town);
			mListener.onFragmentButton();
			break;
		case R.id.zip_search:
			String strZip = v.findViewById(R.id.txt_zip).toString();
			int zip = Integer.parseInt(strZip);
			agencySearch(zip);
			mListener.onFragmentButton();
			break;
			/*case R.id.btn_location:
			String strZip = v.findViewById(R.id.txt_zip).toString();
			int zip = Integer.parseInt(strZip);
			agencySearch(zip);
			mListener.onFragmentButton();
			break;*/
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent,
			View v, int position, long id) {

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

	public void agencySearch(String tsearch)	{
		// Setting the URL for the Search by Town
		String url_search_agency = "http://www.infinitycodeservices.com/get_agency_by_city.php";
		// Building parameters for the search
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("town", tsearch));

		// Getting JSON string from URL
		JSONObject json = jParser.makeHttpRequest(url_search_agency, "GET", params);

		// Check your log cat for JSON reponse
		Log.d("Search Results: ", json.toString());

		try {
			// Checking for SUCCESS TAG
			int success = json.getInt(TAG_SUCCESS);

			if (success == 1) {
				// products found
				// Getting Array of Results
				agency = json.getJSONArray(TAG_AGENCY);

				// looping through All results
				for (int i = 0; i < agency.length(); i++) {
					JSONObject c = agency.getJSONObject(i);

					// Storing each json item in variable
					String id = c.getString(TAG_AID);
					String name = c.getString(TAG_NAME);

					// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();

					// adding each child node to HashMap key => value
					map.put(TAG_AID, id);
					map.put(TAG_NAME, name);

					// adding HashList to ArrayList
					resultsList.add(map);
				}
			}

			MainActivity.setResultsList(resultsList);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void agencySearch(int zsearch)	{
		// Setting the URL for the Search by Zip
		String url_search_agency = "http://www.infinitycodeservices.com/get_agency_by_zip.php";
		// Building parameters for the search
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("zip", Integer.toString(zsearch)));

		// Getting JSON string from URL
		JSONObject json = jParser.makeHttpRequest(url_search_agency, "GET", params);

		// Check your log cat for JSON reponse
		Log.d("Search Results: ", json.toString());

		try {
			// Checking for SUCCESS TAG
			int success = json.getInt(TAG_SUCCESS);

			if (success == 1) {
				// products found
				// Getting Array of Results
				agency = json.getJSONArray(TAG_AGENCY);

				// looping through All results
				for (int i = 0; i < agency.length(); i++) {
					JSONObject c = agency.getJSONObject(i);

					// Storing each json item in variable
					String id = c.getString(TAG_AID);
					String name = c.getString(TAG_NAME);

					// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();

					// adding each child node to HashMap key => value
					map.put(TAG_AID, id);
					map.put(TAG_NAME, name);

					// adding HashList to ArrayList
					resultsList.add(map);
				}
			}

			MainActivity.setResultsList(resultsList);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		};

	}


}




