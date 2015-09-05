package com.checkinn.student;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.checkinn.Constant;
import com.checkinn.R;
import com.checkinn.Utils;
import com.checkinn.session_manager.UserSession;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ActivityEnterAvaialibility extends Activity implements
		OnClickListener {

	private EditText etDate;
	private EditText etFrom;
	private EditText etTo;
	private Button btnSubmit;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_availability);
		setTitle("Enter Availability");
		init();
	}

	private void init() {
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

		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(this);
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

		case R.id.btnSubmit:
			btnSubmit.setOnClickListener(null);
			pd = Utils.showProgressDialog(this);
			if(TextUtils.isEmpty(etDate.getText().toString()) && etFrom.getText().toString().equals("") && etTo.getText().toString().equals("") ){
				pd.cancel();
				Toast.makeText(ActivityEnterAvaialibility.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
				
			}else{
				submitAvailibility();	
			}
			btnSubmit.setOnClickListener(this);
			break;
		default:
			break;
		}
	}

	private void submitAvailibility() {
		ParseUser user = null;
		try {
			user = ParseUser.logIn(UserSession.getInstance(this).getUsername(),UserSession.getInstance(this).getPassword());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(user==null){
			pd.cancel();
			Toast.makeText(ActivityEnterAvaialibility.this, "User session vanishes logout and then login again", Toast.LENGTH_SHORT).show();
			return;
		}
		user.put(Constant.A_DATE, etDate.getText().toString());
		user.put(Constant.A_FROM, etFrom.getText().toString());
		user.put(Constant.A_TO, etTo.getText().toString());
		user.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				pd.cancel();
				if(e ==null){
					Toast.makeText(ActivityEnterAvaialibility.this, "Availibility added successfully", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(ActivityEnterAvaialibility.this, "Error in update availibility", Toast.LENGTH_SHORT).show();
				}
			}
		});
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
