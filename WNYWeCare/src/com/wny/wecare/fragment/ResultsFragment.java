package com.wny.wecare.fragment;

import java.util.ArrayList;
import java.util.HashMap;

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
	
	ArrayList<HashMap<String, String>> resultsList = null;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_home, container, false);
		
		resultsList = MainActivity.getResultsList();
		
		ListView lv =(ListView) getActivity().findViewById(R.id.listView2);
		
		ArrayAdapter adapter = new ArrayAdapter(
                getActivity(), R.layout.fragment_results, resultsList);
        // updating listview
        lv.setAdapter(adapter);
        
        return rootView;
    
	}

}
