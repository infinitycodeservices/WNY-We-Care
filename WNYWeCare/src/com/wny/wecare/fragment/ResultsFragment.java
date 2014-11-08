package com.wny.wecare.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.wny.wecare.MainActivity;
import com.wny.wecare.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ResultsFragment extends Fragment {
	
	ArrayList<Map<String, String>> resultsList = new ArrayList<Map<String, String>>();
	private GoogleMap map;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_results, container, false);
		
		//Get search results from stored ArrayList
		resultsList = MainActivity.getResultsList();
		
		//Build listView from results
		ListView lv =(ListView) getActivity().findViewById(R.id.listView2);
		
		ArrayAdapter adapter = new ArrayAdapter(
                getActivity(), R.layout.fragment_results, resultsList);
        // updating listview
        lv.setAdapter(adapter);
        
        //Setup embedded Google Map fragment
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        
        return rootView;
    
	}

}
