package com.wny.wecare.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wny.wecare.MainActivity;
import com.wny.wecare.R;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ResultsFragment extends ListFragment {

	private OnFragmentInteractionListener mListener;
	
	ArrayList<Map<String, String>> resultsList = new ArrayList<Map<String, String>>();
	
	// Setup ArrayList<Map> to populate listView
	List<Map> data = new ArrayList<Map>();
	
	private ListView mListView;
	
	private GoogleMap map;
	
	//Create map bounds for zoom
	LatLngBounds.Builder builder = new LatLngBounds.Builder();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//Get search results from stored ArrayList
		resultsList = MainActivity.getResultsList();
				
		// Load required fields from resultsList
		for (int i = 0; i < resultsList.size(); i++)	{
			Map dmap = new HashMap();
			dmap.put("agencyID", resultsList.get(i).get("AgencyID"));
			dmap.put("agencyName", resultsList.get(i).get("AgencyName"));
			dmap.put("address", resultsList.get(i).get("Address1") + " " + 
					resultsList.get(i).get("City") + " " + resultsList.get(i).get("State") + ", " +
					resultsList.get(i).get("Zip"));
			data.add(dmap);
		}

		View rootView = inflater.inflate(R.layout.fragment_results, container, false);

		//Build listView from results
		mListView =(ListView)rootView.findViewById(android.R.id.list);

		ListAdapter adapter = new SimpleAdapter(rootView.getContext(), (List<? extends Map<String, ?>>) data,
				R.layout.custom_row_view, new String[] { "agencyID", "agencyName",
		"address" }, new int[] { R.id.ragid, R.id.ragname,
			R.id.ragaddr });

		// updating listview
		setListAdapter(adapter);

		//Setup embedded Google Map fragment
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		
		//Add markers to the map from resultsList
		for (int i = 0; i < resultsList.size(); i++)	{
			Double latitude = Double.parseDouble(resultsList.get(i).get("Lat"));
			Double longitude = Double.parseDouble(resultsList.get(i).get("Lng"));
			String mname = resultsList.get(i).get("AgencyName");
			LatLng posit = new LatLng(latitude, longitude);
			Marker m = map.addMarker(new MarkerOptions()
			.position(posit)
			.title(mname));
			builder.include(m.getPosition());
		}
		
		map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
		    @Override
		    public void onMapLoaded() {
		    	LatLngBounds bounds = builder.build();
		        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
		    }
		});

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
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {    

		int new_position = position;
		String detailID = data.get(new_position).get("agencyID").toString();
		Toast.makeText(v.getContext(), 
                "AgencyID: " + detailID, Toast.LENGTH_LONG).show();
		mListener.onDetailsButton();

	}
	
	public void onListFragmentItemClick(int position)	{
		
	}
	

}
