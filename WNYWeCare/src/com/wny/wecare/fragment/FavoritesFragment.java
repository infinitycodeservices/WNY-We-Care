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

import android.app.ListFragment;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.wny.wecare.R;
import com.wny.wecare.handler.JSONParser;

public class FavoritesFragment extends ListFragment {
	
	// Get UserID from sharedprefs
	String uid = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("uid", "");
	
	private ListView mListView;
	
	// Setting the URL for the Select favorites
	String url_search_favorites = "http://www.infinitycodeservices.com/get_favorite.php";
	
	// Create array to store comments
	ArrayList<Map<String, String>> favoritesArray = new ArrayList<Map<String,  String>>();
	
	// Create JSON Parser object
	JSONParser jParser = new JSONParser();
	
	public FavoritesFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        
        mListView =(ListView)rootView.findViewById(android.R.id.list);
        
        userFavorites();

		ListAdapter adapter = new SimpleAdapter(rootView.getContext(), (List<? extends Map<String, ?>>) favoritesArray,
				R.layout.favorite_item, new String[] { "AgencyID", "AgencyName" }, new int[] { R.id.favid, R.id.favname });

		// updating listview
		setListAdapter(adapter);
        
        return rootView;
    }
	
public void userFavorites()	{
		
		// Building parameters for the search
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("UserID", uid));

		// Getting JSON string from URL
		JSONArray json = jParser.getJSONFromUrl(url_search_favorites, params);

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
				favoritesArray.add(map);
				
			}
			catch (JSONException e) {
				e.printStackTrace();
				
			}
			
		};
		
	}
}
