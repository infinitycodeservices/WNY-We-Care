package com.wny.wecare.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wny.wecare.MainActivity;
import com.wny.wecare.R;
import com.wny.wecare.handler.JSONParser;

public class DialogFragment extends FragmentActivity implements OnClickListener{

	private TextView text;      
	private Button startBtn;  

	// Create JSON Parser object
	JSONParser jParser = new JSONParser();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_details);


		text = (TextView)findViewById(R.id.rate);

		startBtn = (Button) findViewById(R.id.cmntButton);
		startBtn.setOnClickListener(this);
	}


	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void onClick(View v) {

		if (v == startBtn) {
			CustomDialogFragment dialog = new CustomDialogFragment();
			dialog.show(getSupportFragmentManager(), "dialog");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@SuppressLint("ValidFragment")
	public class CustomDialogFragment extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Dialog dialog = new Dialog(getActivity());

			dialog.getWindow().getAttributes().windowAnimations = R.style.Animation_CustomDialog;

			dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

			dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
			dialog.setContentView(R.layout.dialog_custom);

			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

			dialog.setCanceledOnTouchOutside(false);

			final TextView message = (TextView) dialog.findViewById(R.id.message);
			message.setText(text.getText());

			dialog.findViewById(R.id.positive_button).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
				}
			});

			dialog.findViewById(R.id.close_button).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// Build parameters to create feedback
					/*String uid = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("uid", "");;
					String comments = MainActivity.getDetailsID();
					String feedbk = (String) message.getText();
					String rating = dialog.findViewById(R.id.rating).toString();
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("AgencyID", comments));
					params.add(new BasicNameValuePair("UserID", uid));
					params.add(new BasicNameValuePair("Comment", feedbk));
					params.add(new BasicNameValuePair("Star", rating));

					// Write to table
					JSONArray json = jParser.getJSONFromUrl("http://www.infinitycodeservices.com/add_feedback.php", params);
					if(json != null)	{
						Toast.makeText(getActivity(), "Successfully added feedback", Toast.LENGTH_LONG).show();
					} else	{
						Toast.makeText(getActivity(), "Feedback not added", Toast.LENGTH_LONG).show();
					}*/
					dismiss();
				}
			});
			return dialog;
		}

		protected void dismiss() {
			// TODO Auto-generated method stub

		}

		public void show(FragmentManager supportFragmentManager, String string) {
			// TODO Auto-generated method stub

		}
	}


	public Context getActivity() {
		// TODO Auto-generated method stub
		return null;
	}
}


