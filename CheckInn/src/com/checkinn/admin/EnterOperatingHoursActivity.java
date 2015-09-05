package com.checkinn.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.checkinn.Constant;
import com.checkinn.R;
import com.checkinn.Utils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class EnterOperatingHoursActivity extends Activity implements OnClickListener {

	private Spinner spinnerCafe;
	protected String assignedCafe="";
	protected ArrayList<String> listOfCafe;
	private Button btnAssign;
	private ProgressDialog pd;
	private EditText etDate;
	private EditText etFrom;
	private EditText etTo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_operating_hours);
		init();
		fetchCafeData();
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

		etFrom = (EditText) findViewById(R.id.etFrom);
		etFrom.setFocusable(false);
		etFrom.setFocusableInTouchMode(false);
		etFrom.setOnClickListener(this);

		etTo = (EditText) findViewById(R.id.etTo);
		etTo.setFocusable(false);
		etTo.setFocusableInTouchMode(false);
		etTo.setOnClickListener(this);

		
		btnAssign = (Button)findViewById(R.id.btnSubmit);
		btnAssign.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				pd = Utils.showProgressDialog(EnterOperatingHoursActivity.this);
				ParseObject parseObject = new ParseObject(Constant.CAFE);
				parseObject.put("cafe_name", ""+assignedCafe);
				parseObject.put("is_holiday", true);
				parseObject.put("is_valid", true);
				
				String schedule_date = "";
				if(!etDate.getText().toString().equals(""))
				schedule_date = etDate.getText().toString()+" 00:00";  
				
				
				SimpleDateFormat  format = new SimpleDateFormat("dd-MM-yyyy HH:mm");  
				Date s_date = null;
				try {  
				    s_date = format.parse(schedule_date);  
				} catch (java.text.ParseException e) {
					e.printStackTrace();
				}
				if(s_date!=null)
					parseObject.put("schedule_date",s_date );
				
				
				String from_date = "";
				if(!etFrom.getText().toString().equals(""))
				 from_date = etFrom.getText().toString();  
				SimpleDateFormat  from_format = new SimpleDateFormat("HH:mm");  
				Date f_date = null;
				try {  
				    f_date = from_format.parse(from_date);  
				} catch (java.text.ParseException e) {
					e.printStackTrace();
				}
				if(f_date!=null)
					parseObject.put("start_time",f_date );
				
				String to_date = "";
				if(!etTo.getText().toString().equals(""))
				 to_date = etTo.getText().toString();  
				SimpleDateFormat  to_format = new SimpleDateFormat("HH:mm");  
				Date t_date = null;
				try {  
					t_date = to_format.parse(to_date);  
				} catch (java.text.ParseException e) {
					e.printStackTrace();
				}
				if(t_date!=null)
					parseObject.put("end_time",t_date );
				
				
				parseObject.saveInBackground(new SaveCallback() {
					
					@Override
					public void done(ParseException e) {
						pd.cancel();
						if(e==null){
							
						}else{
							Toast.makeText(EnterOperatingHoursActivity.this, "Error in adding Operating hours", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
			
			
		});

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
					SpinnerAdapter<String> adapter = new SpinnerAdapter<String>(EnterOperatingHoursActivity.this,android.R.layout.simple_list_item_1,listOfCafe);
					spinnerCafe.setAdapter(adapter);
				}else{
					Toast.makeText(EnterOperatingHoursActivity.this, "error in retrieving cafe", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.etDate:
			showDialog(999);
			break;
		case R.id.etFrom:
			showDialog(888);
			break;
		case R.id.etTo:
			showDialog(777);
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

	private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker d, int year, int month, int day) {
			etDate.setText(day + "-" +(month+1) + "-" + year);
		}
	};

	private TimePickerDialog.OnTimeSetListener fromTimeListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			etFrom.setText(hourOfDay + ":" + minute);
		}
	};	

	private TimePickerDialog.OnTimeSetListener toTimeListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			etTo.setText(hourOfDay + ":" + minute);
		}
	};


}
