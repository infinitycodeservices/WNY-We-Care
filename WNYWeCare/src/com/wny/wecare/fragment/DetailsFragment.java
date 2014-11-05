package com.wny.wecare.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wny.wecare.MainActivity;
import com.wny.wecare.R;

public class DetailsFragment extends Fragment implements OnClickListener {
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
         
        return rootView;
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	
}
