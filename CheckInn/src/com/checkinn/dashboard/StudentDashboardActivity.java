package com.checkinn.dashboard;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.checkinn.Constant;
import com.checkinn.R;
import com.checkinn.Utils;
import com.checkinn.activity.HomeBaseActivity;
import com.checkinn.session_manager.UserSession;
import com.checkinn.student.ActivityEnterAvaialibility;
import com.checkinn.student.ScheduleActivity;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class StudentDashboardActivity extends HomeBaseActivity {

	private TextView tvSchedule;
	private TextView tvEnterAvailability;
	private TextView tvCancelShift;
	private ProgressDialog pd;

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sideMenuTitles = getResources().getStringArray(R.array.side_menu_user);
		setContentView(R.layout.activity_student_dashboard);
		 ParseAnalytics.trackAppOpened(getIntent());
	      //  PushService.setDefaultPushCallback(this,AdminDashboardActivity.class);
	       // ParseInstallation.getCurrentInstallation().saveInBackground();
	        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
	        installation.put("username", UserSession.getInstance(this).getUsername());
	        installation.saveInBackground();
		
		init();

	}

	private void init() {
		tvSchedule = (TextView) findViewById(R.id.tvSchedule);
		tvSchedule.setOnClickListener(this);
		tvEnterAvailability = (TextView) findViewById(R.id.tvEnterAvailability);
		tvCancelShift = (TextView) findViewById(R.id.tvCancelShift);
		tvEnterAvailability.setOnClickListener(this);
		tvCancelShift.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tvSchedule:
			startActivity(new Intent(this, ScheduleActivity.class));
			break;

		case R.id.tvEnterAvailability:
			startActivity(new Intent(this, ActivityEnterAvaialibility.class));
			break;
			
		case R.id.tvCancelShift:
			AlertDialog.Builder build = new AlertDialog.Builder(this);
			build.setTitle("Cancel shift").setMessage("Are you sure you want to cancel shift")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() 
					{
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							dialog.dismiss();
							cancelShift();
						}
					}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
					{
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							dialog.dismiss();
						}
					});
			AlertDialog dialog=build.create();		
			dialog.show();
		default:
			break;
		}
	}
	
	
	private void cancelShift() {
		pd = Utils.showProgressDialog(this);
		ParseUser user = null;
		try {
			user = ParseUser.logIn(UserSession.getInstance(this).getUsername(),UserSession.getInstance(this).getPassword());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			pd.cancel();
			e.printStackTrace();
		}
		if(user ==null){
			pd.cancel();
			Toast.makeText(StudentDashboardActivity.this, "User session vanishes logout and then login again", Toast.LENGTH_SHORT).show();
			return;
		
		}
		user.put(Constant.A_DATE, "");
		user.put(Constant.A_FROM, "");
		user.put(Constant.A_TO, "");
		user.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				pd.cancel();
				if(e ==null){
					Toast.makeText(StudentDashboardActivity.this, "Availibility cancelled", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(StudentDashboardActivity.this, "Error in update availibility", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

}
