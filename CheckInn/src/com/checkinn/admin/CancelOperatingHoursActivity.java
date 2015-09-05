package com.checkinn.admin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.checkinn.Constant;
import com.checkinn.R;
import com.checkinn.Utils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class CancelOperatingHoursActivity extends Activity implements OnClickListener {

	private Spinner spinnerCafe;
	protected String assignedCafe="";
	protected ArrayList<String> listOfCafe;
	private Button btnCancel;
	private ProgressDialog pd;
	private EditText etDate;
	//protected Date cancelDate;
	protected Calendar cancelCalenderDate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cancel_operating_hours);
		init();
		fetchCafeData();
	}
	private void fetchCafeData() {
		pd = Utils.showProgressDialog(this);
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
					SpinnerAdapter<String> adapter = new SpinnerAdapter<String>(CancelOperatingHoursActivity.this,android.R.layout.simple_list_item_1,listOfCafe);
					spinnerCafe.setAdapter(adapter);
				}else{
					Toast.makeText(CancelOperatingHoursActivity.this, "error in retrieving cafe", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	private void init() {
		spinnerCafe= (Spinner)findViewById(R.id.spinnerCafe);
		spinnerCafe.setPrompt("Select cafe");
		spinnerCafe.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				assignedCafe = listOfCafe.get(pos);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});

		etDate = (EditText) findViewById(R.id.etDate);
		etDate.setFocusable(false);
		etDate.setFocusableInTouchMode(false);
		etDate.setOnClickListener(this);
		
		btnCancel = (Button)findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				pd = Utils.showProgressDialog(CancelOperatingHoursActivity.this);
				ParseQuery<ParseObject> query = ParseQuery.getQuery(Constant.CAFE);
				query.whereEqualTo("cafe_name", ""+assignedCafe);
				query.findInBackground(new FindCallback<ParseObject>() {

					private String TAG="btnCancel";

					@Override
					public void done(List<ParseObject> objects, ParseException e) {
						Log.v(TAG, ""+objects.size());
						pd.cancel();
						if(e==null){
							for (ParseObject parseObject : objects) {
								Date date = parseObject.getDate("schedule_date");
								Log.v(TAG, "date >> "+date+"   cancelDate >>  "+cancelCalenderDate.getTime().toString());
								if(cancelCalenderDate==null ){
									break;
								}
								if(date!=null){
									
									//if(date.compareTo(cancelDate)== 0)
									Calendar cal2 = Calendar.getInstance();
									cal2.setTime(date);
									boolean sameDay = cancelCalenderDate.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
											cancelCalenderDate.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)&&cancelCalenderDate.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) ;
									
									Log.v(TAG,"cancel Date === date "+sameDay);
									if(sameDay)
									{
										parseObject.deleteInBackground();
									}
								}
								
							}
						}else
						{
							
						}
					}
				});
			}
		});
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.etDate:
			showDialog(999);
			break;
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Calendar c = Calendar.getInstance();
		// TODO Auto-generated method stub
		if (id == 999) {

			return new DatePickerDialog(this, myDateListener,
					c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DATE));
		}
		return null;
	}
	
	private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker d, int year, int month, int day) {
			etDate.setText(day + "-" +(month+1) + "-" + year);
			Log.v("date","date selected   >>  "+day + "-" +(month+1) + "-" + year);
				cancelCalenderDate = Calendar.getInstance();
				cancelCalenderDate.set(year, month, day);
				//cancelDate = c.getTime();
		}
	};

	}
