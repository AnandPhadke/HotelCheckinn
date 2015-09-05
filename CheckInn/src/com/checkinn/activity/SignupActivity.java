package com.checkinn.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.checkinn.Constant;
import com.checkinn.R;
import com.checkinn.Utils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class SignupActivity extends Activity implements OnClickListener {
		private Button btnSignup;
		private EditText etUsername;
		private EditText etPassword;
		private EditText etConfirmPassword;
		private EditText etEmployeeName;
		private EditText etUserId;
		private String usernametxt="";
		private String passwordtxt="";
		private String employeeName="";
		private String userId="";
		private ProgressDialog progressDialog;
		private String confirmPasswordtxt="";
		private CheckBox cbAdmin;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_signup);
			getActionBar().setTitle("Signup");
			btnSignup = (Button)findViewById(R.id.btnSignup);
			btnSignup.setOnClickListener(this);
			etUsername = (EditText)findViewById(R.id.etUsername);
			etPassword = (EditText)findViewById(R.id.etPassword);
			etConfirmPassword = (EditText)findViewById(R.id.etConfirmPassword);
			etEmployeeName = (EditText)findViewById(R.id.etEmployeeName);
			etUserId = (EditText)findViewById(R.id.etUserId);
			cbAdmin = (CheckBox)findViewById(R.id.cbAdmin);
			
			
			
			
		}

		@Override
		public void onClick(View v) {
			Intent intent;
			switch (v.getId()) {
			case R.id.btnSignup:
				progressDialog = Utils.showProgressDialog(this);
				// Retrieve the text entered from the EditText
				usernametxt = etUsername.getText().toString();
				passwordtxt = etPassword.getText().toString();
				employeeName = etEmployeeName.getText().toString();
				userId = etUserId.getText().toString();
				confirmPasswordtxt = etPassword.getText().toString();
				if(TextUtils.isEmpty(usernametxt) || TextUtils.isEmpty(passwordtxt)|| TextUtils.isEmpty(employeeName) || TextUtils.isEmpty(userId)|| TextUtils.isEmpty(confirmPasswordtxt)){
					Toast.makeText(this, "Please insert all fields", Toast.LENGTH_SHORT).show();
					return;
				}
				if(!passwordtxt.equals(confirmPasswordtxt)){
					Toast.makeText(this, "Password and confirm password should be same", Toast.LENGTH_SHORT).show();
					return;
				}

					// Save new user data into Parse.com Data Storage
				ParseUser user = new ParseUser();
					user.setUsername(usernametxt);
					user.setPassword(passwordtxt);
					user.put(Constant.USER_NAME, employeeName);
					user.put(Constant.USER_ID, userId);
					user.put(Constant.IS_MANAGER, cbAdmin.isChecked());
					
					user.signUpInBackground(new SignUpCallback() {
						
						@Override
						public void done(ParseException e) {
							progressDialog.dismiss();
							if (e == null) {
								// Show a simple Toast message upon successful registration
								
								ParseObject parseObject = new ParseObject("live_users");
								parseObject.put("username", usernametxt);
								parseObject.put("password", passwordtxt.toString());
								parseObject.saveInBackground(new SaveCallback() {
									
									@Override
									public void done(ParseException e) {
										
										if(e==null){
											Toast.makeText(getApplicationContext(),
													"Successfully Signed up",
													Toast.LENGTH_LONG).show();
											finish();
										}else{

											//Log.e(getClass().getName(), "exception   "+e.getMessage());
											Toast.makeText(getApplicationContext(),
													"Sign up Error", Toast.LENGTH_LONG)
													.show();
										
										}
										
										
									}
								});
								
							} else {
								

								Log.e(getClass().getName(), "exception   "+e.getMessage());
								Toast.makeText(getApplicationContext(),
										"Sign up Error", Toast.LENGTH_LONG)
										.show();
							
							}
						}
					});
					break;
				

			default:
				break;
			}
		}
}
