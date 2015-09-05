package com.checkinn.admin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.checkinn.Constant;
import com.checkinn.R;
import com.checkinn.Utils;
import com.checkinn.objects.UserObject;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ActivityAssignShift extends Activity implements OnClickListener {
	
	private Spinner spinnerUserList;
	private Spinner spinnerCafe;
	private ProgressDialog pd;
	private LinearLayout llAvailable;
	private TextView tvNotAvailable;
	private TextView tvDate;
	private TextView tvFrom;
	private TextView tvTo;
	protected ArrayList<UserObject> array;
	protected ArrayList<String> listOfCafe;
	protected String assignedCafe="";
	private Button btnAssign;
	protected UserObject selectedUser;
	protected int fromTime;
	protected int toTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Assign shift");
		setContentView(R.layout.activity_assign_shift);
		init();
		fetchAvailableUser();
		fetchCafeData();
	}

	private void init() {
		spinnerCafe= (Spinner)findViewById(R.id.spinnerCafe);
		spinnerCafe.setPrompt("Select cafe");
		spinnerUserList = (Spinner)findViewById(R.id.spinnerUserList);
		spinnerUserList.setPrompt("Select user");
		llAvailable = (LinearLayout)findViewById(R.id.llAvailable);
        tvNotAvailable= (TextView)findViewById(R.id.tvNotAvailable);
        tvDate = (TextView)findViewById(R.id.tvDate);
        tvFrom = (TextView)findViewById(R.id.tvFrom);
        tvFrom.setOnClickListener(this);
		tvTo = (TextView)findViewById(R.id.tvTo);
		tvTo.setOnClickListener(this);
		//spinnerUserList.set
		
		spinnerUserList.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
					selectedUser = array.get(pos);
					if (selectedUser==null){
						return;
					}
					if(selectedUser.a_date==null ||selectedUser.a_date.equals("")){
						tvNotAvailable.setVisibility(View.VISIBLE);
						llAvailable.setVisibility(View.GONE);
						btnAssign.setVisibility(View.GONE);
					}else{
						tvNotAvailable.setVisibility(View.GONE);
						llAvailable.setVisibility(View.VISIBLE);
						tvDate.setText(selectedUser.a_date);
						tvFrom.setText(selectedUser.a_from);
						try {
						fromTime =Integer.parseInt(selectedUser.a_from.split(":")[0]);
						tvTo.setText(selectedUser.a_to);
						toTime =Integer.parseInt(selectedUser.a_to.split(":")[0]);
						} catch (Exception e) {
						}
						btnAssign.setVisibility(View.VISIBLE);
					}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		spinnerCafe.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				assignedCafe = listOfCafe.get(pos);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		btnAssign = (Button)findViewById(R.id.btnAssign);
		btnAssign.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				assignShift();
			}
		});
	}

	private void assignShift() {
		pd = Utils.showProgressDialog(this);
		ParseUser user = null;
		try {
			Log.v(getClass().getName(), "username"+selectedUser.userName);
			ParseQuery<ParseObject> query = ParseQuery.getQuery("live_users");
			query.whereEqualTo("username", ""+selectedUser.userName.toString());
			ParseObject p= query.getFirst();
			//ParseUser.getQuery().get(selectedUser.objectId);
			
			user = ParseUser.logIn(p.getString("username"),p.getString("password"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(user==null){
			pd.cancel();
			Toast.makeText(ActivityAssignShift.this, "Something is wrong logout then login again", Toast.LENGTH_SHORT).show();
			return;
		}
		user.put("assign_cafe", assignedCafe);
		user.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				
				if(e ==null){
					
					ParseQuery<ParseInstallation> query = ParseInstallation.getQuery(); 
					query.whereEqualTo("username", selectedUser.userName);    
					ParsePush push = new ParsePush();
					push.setQuery(query);
					push.setMessage( "Admin assigned "+assignedCafe+" cafe to you");
					push.sendInBackground();
					
				}else{
					Toast.makeText(ActivityAssignShift.this, "Error in assign shift", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		
		ParseObject parseObject = new ParseObject(Constant.CAFE_SCHEDULE);
		parseObject.put("username", ""+selectedUser.userName);
		parseObject.put("cafe_name",""+assignedCafe);
		
		if(!tvDate.getText().toString().equals("")){
			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			Date dateSchedule = null;
			try {
				dateSchedule = formatter.parse(tvDate.getText().toString()+" 00:00");
			} catch (java.text.ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			parseObject.put("schedule_date",dateSchedule );
		}
		if(!tvFrom.getText().toString().equals("")){
			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			Date timeFrom = null;
			try {
				timeFrom = formatter.parse(tvDate.getText().toString()+" "+tvFrom.getText().toString());
			} catch (java.text.ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			parseObject.put("start_time", timeFrom);
		}
		if(!tvTo.getText().toString().equals("")){
			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			Date timeFrom = null;
			try {
				timeFrom = formatter.parse(tvDate.getText().toString()+" "+tvTo.getText().toString());
			} catch (java.text.ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			parseObject.put("end_time", timeFrom);
		}
		parseObject.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				pd.cancel();
				if(e==null){
					Toast.makeText(ActivityAssignShift.this, "Successfully assign cafe!!!", Toast.LENGTH_SHORT).show();	
				}
				
			}
		});
		
		
		
		
		
		
	}

	private void fetchAvailableUser() {
		pd = Utils.showProgressDialog(this);
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.findInBackground(new FindCallback<ParseUser>() {
			
			@Override
			public void done(List<ParseUser> objects, ParseException e) {
				pd.cancel();
				if(e==null){
					 array = new ArrayList<UserObject>();
					 array.clear();
					for (ParseUser parseUser : objects) {
						
						
						Log.v("assign shift", "username   :   "+parseUser.getUsername());
						Log.v("assign shift", "date   :   "+parseUser.getString("a_date"));
						if(!parseUser.getBoolean("is_admin")){
							UserObject userObject = new UserObject();
							userObject.a_date = parseUser.getString("a_date");
							userObject.a_from = parseUser.getString("a_from");
							userObject.a_to = parseUser.getString("a_to");
							userObject.userName =parseUser.getUsername();
							userObject.objectId = parseUser.getObjectId();
							//userObject.password= parseUser.getPa
							array.add(userObject);
						}
					}
					SpinnerAdapter<UserObject> adapter = new SpinnerAdapter<UserObject>(ActivityAssignShift.this,android.R.layout.simple_list_item_1,array);
					spinnerUserList.setAdapter(adapter);
					
					

					
				}else{
					Toast.makeText(ActivityAssignShift.this, "error in retrieving users", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	
	
	private void fetchCafeData() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery(Constant.CAFE);
		query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				pd.cancel();
				if(e==null){
					listOfCafe = new ArrayList<String>();
					for (ParseObject parseObject : objects) {
							listOfCafe.add(parseObject.getString("cafe_name"));
							HashSet hs = new HashSet(listOfCafe);
							listOfCafe.clear();
							listOfCafe.addAll(hs);
					}
					SpinnerAdapter<String> adapter = new SpinnerAdapter<String>(ActivityAssignShift.this,android.R.layout.simple_list_item_1,listOfCafe);
					spinnerCafe.setAdapter(adapter);
				}else{
					Toast.makeText(ActivityAssignShift.this, "error in retrieving cafe", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvFrom:
			showDialog(888);
			break;
		case R.id.tvTo:
			showDialog(777);
			break;
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Calendar c = Calendar.getInstance();
		// TODO Auto-generated method stub
		if (id == 888) {
			return new TimePickerDialog(this, fromTimeListener,
					c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
		}
		if (id == 777) {
			return new TimePickerDialog(this, toTimeListener,
					c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
		}
		return null;
	}

	

	private TimePickerDialog.OnTimeSetListener fromTimeListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			if(hourOfDay >= fromTime && hourOfDay<= toTime){
				tvFrom.setText(hourOfDay + ":" + minute);
			}else{
				Toast.makeText(ActivityAssignShift.this, "Pick time within range only ", Toast.LENGTH_SHORT).show();
			}
			
			
			
		}
	};	

	private TimePickerDialog.OnTimeSetListener toTimeListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			if(hourOfDay >= fromTime && hourOfDay<= toTime){
				tvTo.setText(hourOfDay + ":" + minute);
			}else{
				Toast.makeText(ActivityAssignShift.this, "Pick time within range only ", Toast.LENGTH_SHORT).show();
			}
			
			
		}
	};


}
