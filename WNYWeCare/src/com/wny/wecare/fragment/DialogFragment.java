package com.wny.wecare.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.wny.wecare.R;

public class DialogFragment extends FragmentActivity implements OnClickListener{

	private TextView text;      
	private Button startBtn;   

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_details);


		text = (TextView)findViewById(R.id.rate);

		startBtn = (Button) findViewById(R.id.rate_button);
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
			Dialog dialog = new Dialog(getActivity());

			dialog.getWindow().getAttributes().windowAnimations = R.style.Animation_CustomDialog;

			dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

			dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
			dialog.setContentView(R.layout.dialog_custom);

			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

			dialog.setCanceledOnTouchOutside(false);

			TextView message = (TextView) dialog.findViewById(R.id.message);
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


