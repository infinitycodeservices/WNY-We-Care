package com.wny.wecare.fragment;

import java.util.ArrayList;
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

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ResultsFragment extends ListFragment {

	ArrayList<Map<String, String>> resultsList = new ArrayList<Map<String, String>>();
		
	private GoogleMap map;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//Get search results from stored ArrayList
		resultsList = MainActivity.getResultsList();

		View rootView = inflater.inflate(R.layout.fragment_results, container, false);

		/*//Build listView from results
		ListView lv =(ListView) getActivity().findViewById(android.R.id.list);

		ListAdapter adapter = new SimpleAdapter(getActivity(), resultsList,
				R.layout.custom_row_view, new String[] { "AgencyID", "AgencyName",
		"Address1" }, new int[] { R.id.text1, R.id.text2,
			R.id.text3 });

		// updating listview
		setListAdapter(adapter);*/

		//Setup embedded Google Map fragment
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		//Create map bounds for zoom
		LatLngBounds.Builder builder = new LatLngBounds.Builder();

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
		
		// Set map camera to fit bounds
		LatLngBounds bounds = builder.build();
		int padding = 0; // offset from edges of the map in pixels
		CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
		map.animateCamera(cu);

		return rootView;
	}
	
	public void onListFragmentItemClick(int position)	{
		
	}
	

}
