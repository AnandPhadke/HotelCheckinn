package com.checkinn.admin;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
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

public class CancelShiftActivity extends Activity {

	protected ArrayList<UserObject> array;
	private ProgressDialog pd;
	protected Spinner spinnerUserList;
	private LinearLayout llAvailable;
	private TextView tvNotAvailable;
	private TextView tvDate;
	private TextView tvFrom;
	private TextView tvTo;
	protected UserObject selectedUser;
	private Button btnCancel;
	protected ArrayList<String> listOfCafe;
	protected Spinner spinnerCafe;
	protected String assignedCafe="";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cancel_shift);
		init();
		fetchAvailableUser();
		fetchCafeData();
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
					}
					SpinnerAdapter<String> adapter = new SpinnerAdapter<String>(CancelShiftActivity.this,android.R.layout.simple_list_item_1,listOfCafe);
					spinnerCafe.setAdapter(adapter);
				}else{
					Toast.makeText(CancelShiftActivity.this, "error in retrieving cafe", Toast.LENGTH_SHORT).show();
				}
			}
		});
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
		tvTo = (TextView)findViewById(R.id.tvTo);	
		
		
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
					}else{
						tvNotAvailable.setVisibility(View.GONE);
						llAvailable.setVisibility(View.VISIBLE);
						tvDate.setText(selectedUser.a_date);
						tvFrom.setText(selectedUser.a_from);
						tvTo.setText(selectedUser.a_to);
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

		
		
		btnCancel = (Button)findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				cancelShift();
			}
		});
	}


	protected void cancelShift() {

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
			Toast.makeText(CancelShiftActivity.this, "Something is wrong logout then login again", Toast.LENGTH_SHORT).show();
			return;
		}
		user.put("assign_cafe", "");
		user.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				pd.cancel();
				if(e ==null){
					Toast.makeText(CancelShiftActivity.this, "Successfully canceled shift", Toast.LENGTH_SHORT).show();
					ParseQuery<ParseInstallation> query = ParseInstallation.getQuery(); 
					query.whereEqualTo("username", selectedUser.userName);    
					ParsePush push = new ParsePush();
					push.setQuery(query);
					push.setMessage( "Admin canceled your shift");
					push.sendInBackground();
					
				}else{
					Toast.makeText(CancelShiftActivity.this, "Error in canceling shift", Toast.LENGTH_SHORT).show();
				}
			}
		});
	
		
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery(Constant.CAFE_SCHEDULE);
		query.whereEqualTo("username", ""+selectedUser.userName.toString());
		query.whereEqualTo("cafe_name", ""+assignedCafe.toString());
		ParseObject p1 = null;
		try {
			p1 = query.getFirst();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(p1!=null)
			p1.deleteInBackground();
		
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
					SpinnerAdapter<UserObject> adapter = new SpinnerAdapter<UserObject>(CancelShiftActivity.this,android.R.layout.simple_list_item_1,array);
					spinnerUserList.setAdapter(adapter);
					
					

					
				}else{
					Toast.makeText(CancelShiftActivity.this, "error in retrieving users", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
