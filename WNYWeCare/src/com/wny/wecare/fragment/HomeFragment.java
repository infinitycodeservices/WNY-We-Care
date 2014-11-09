package com.wny.wecare.fragment;

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
import android.app.Fragment;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.wny.wecare.MainActivity;
import com.wny.wecare.R;
import com.wny.wecare.handler.JSONParser;

public class HomeFragment extends Fragment implements OnItemSelectedListener, OnClickListener{

	private OnFragmentInteractionListener mListener;

	private Spinner spinner;
	private Button btnSubmit;
	private Button zipSubmit;
	private Button btnLocation;
	
	private static final String[] list={"Alden", "Amherst", "Angola",
		"Blasdell", "Boston", "Bowmansville", "Buffalo", "Cheektowaga", "Clarence",
		"Depew", "Derby", "East Aurora", "Eden", "Elma", "Getzville", "Gowanda",
		"Grand Island", "Holland", "Irving", "Kenmore", "Lackawanna", "Lake View",
		"Lancaster", "Lawtons", "North Collins", "Orchard Park", "Snyder", "Springville",
		"Tonawanda", "West Seneca", "Williamsville"};

	// Setup ArrayList from main activity to store results
	ArrayList<Map<String, String>> resultsList = new ArrayList<Map<String,  String>>();

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
		
		zipSubmit = (Button) rootView.findViewById(R.id.zip_search);
		zipSubmit.setOnClickListener(this);
		
		btnLocation = (Button) rootView.findViewById(R.id.btn_location);
		btnLocation.setOnClickListener(this);

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
			String strZip  = ((EditText) v.findViewById(R.id.txt_zip)).getText().toString().trim();
			int zip = Integer.parseInt(strZip);
			agencySearch(zip);
			mListener.onFragmentButton();
			break;
			case R.id.btn_location:
				 Geocoder geocoder;
			     String bestProvider;
			     List<Address> user = null;
			     double lat = 0;
			     double lng = 0;
			     int radius = 0;

			    LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

			     Criteria criteria = new Criteria();
			     bestProvider = lm.getBestProvider(criteria, false);
			     Location location = lm.getLastKnownLocation(bestProvider);

			     if (location == null){
			         Toast.makeText(getActivity(),"Location Not found",Toast.LENGTH_LONG).show();
			      }else{
			        geocoder = new Geocoder(getActivity());
			        try {
			            user = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
			        lat=(double)user.get(0).getLatitude();
			        lng=(double)user.get(0).getLongitude();
			        System.out.println(" DDD lat: " +lat+",  longitude: "+lng);

			        }catch (Exception e) {
			                e.printStackTrace();
			        }
			    }
			agencySearch(lat, lng, radius);
			mListener.onFragmentButton();
			break;
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
		params.add(new BasicNameValuePair("City", tsearch));

		// Getting JSON string from URL
		JSONArray json = jParser.getJSONFromUrl(url_search_agency, params);

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
				resultsList.add(map);
				
			}
			catch (JSONException e) {
				e.printStackTrace();
				
			}
			
		};
		
		MainActivity.setResultsList(resultsList);
		
	}

	public void agencySearch(int zsearch)	{
		// Setting the URL for the Search by Zip
		String url_search_agency = "http://www.infinitycodeservices.com/get_agency_by_zip.php";
		// Building parameters for the search
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("zip", Integer.toString(zsearch)));

		// Getting JSON string from URL
		JSONArray json = jParser.getJSONFromUrl(url_search_agency, params);

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
				resultsList.add(map);
				
			}
			catch (JSONException e) {
				e.printStackTrace();
				
			}
			
		};
		
		MainActivity.setResultsList(resultsList);

	}
	
	private void agencySearch(double lat, double lng, int radius) {
		// Setting the URL for the Search by Zip
				String url_search_agency = "http://www.infinitycodeservices.com/get_agency_by_zip.php";
				// Building parameters for the search
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("lat", Double.toString(lat)));
				params.add(new BasicNameValuePair("lng", Double.toString(lng)));
				params.add(new BasicNameValuePair("radius", Integer.toString(radius)));

				// Getting JSON string from URL
				JSONArray json = jParser.getJSONFromUrl(url_search_agency, params);

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
						resultsList.add(map);
						
					}
					catch (JSONException e) {
						e.printStackTrace();
						
					}
					
				};
				
				MainActivity.setResultsList(resultsList);
		
	}


}




