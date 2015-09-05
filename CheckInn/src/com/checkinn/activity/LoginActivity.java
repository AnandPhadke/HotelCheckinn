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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.checkinn.Constant;
import com.checkinn.R;
import com.checkinn.Utils;
import com.checkinn.dashboard.AdminDashboardActivity;
import com.checkinn.dashboard.StudentDashboardActivity;
import com.checkinn.session_manager.UserSession;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends Activity implements OnClickListener {

	
	private Button btnSignup;
	private Button btnLogin;
	private String usernametxt="";
	private String passwordtxt="";
	private EditText etUsername;
	private EditText etPassword;
	private ProgressDialog progressDialog;
	private CheckBox checkBox;
	private boolean isRememberMe;
	private TextView tvChangePassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getActionBar().setTitle("Login");
		btnSignup = (Button)findViewById(R.id.btnSignup);
		btnSignup.setOnClickListener(this);
		btnLogin = (Button)findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);
		etUsername = (EditText)findViewById(R.id.etUsername);
		etPassword = (EditText)findViewById(R.id.etPassword);
		checkBox =(CheckBox)findViewById(R.id.cbRememberMe);
		tvChangePassword=(TextView)findViewById(R.id.tvChangePassword);
		tvChangePassword.setOnClickListener(this);
	        

	        isRememberMe = UserSession.getInstance(this).isRememberme();
	        
	        
	        if (isRememberMe) {
	        	etUsername.setText(UserSession.getInstance(this).getUsername());
	        	etPassword.setText(UserSession.getInstance(this).getPassword());
	        	checkBox.setChecked(true);
	        	Intent intent;
				if(UserSession.getInstance(this).isAdmin()){
					//intent = new Intent(LoginActivity.this,HomeBaseActivity.class);
					//Toast.makeText(LoginActivity.this, "Admin dashboard comming soon ...", Toast.LENGTH_SHORT).show();
					 intent = new Intent(LoginActivity.this,AdminDashboardActivity.class);
						startActivity(intent);
				}else{
					intent = new Intent(LoginActivity.this,StudentDashboardActivity.class);
					startActivity(intent);
					
				}
				finish();
	        	//login(UserSession.getInstance(this).getUsername(), UserSession.getInstance(this).getPassword());
	        }
		
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				
			}
		});
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btnSignup:
			intent = new Intent(this,SignupActivity.class);
			startActivity(intent);
			break;
			
		case R.id.btnLogin:
			
			usernametxt = etUsername.getText().toString();
			passwordtxt = etPassword.getText().toString();
			if(TextUtils.isEmpty(usernametxt) || TextUtils.isEmpty(passwordtxt)){
				Toast.makeText(this, "Please insert all fields", Toast.LENGTH_SHORT).show();
				return;
			}
			UserSession.getInstance(this).setUsername(usernametxt);
			UserSession.getInstance(this).setPassword(passwordtxt);
			if (checkBox.isChecked()) {
				UserSession.getInstance(this).setRememberme(true);
            } else {
               //UserSession.getInstance(this).clearSession();
            }
			
			login(usernametxt,passwordtxt);
			break;
		case R.id.tvChangePassword:
			intent = new Intent(this,ChangePasswordActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	private void login(String userName,String password) {
	
		progressDialog = Utils.showProgressDialog(LoginActivity.this);
		// Send data to Parse.com for verification
		ParseUser.logInInBackground(usernametxt, passwordtxt,
				new LogInCallback() {
					public void done(ParseUser user, ParseException e) {
						progressDialog.dismiss();
						if (user != null) {
							Log.v(getClass().getName(),user.get("employee_name").toString());
							// If user exist and authenticated, send user to Welcome.class
//							Intent intent = new Intent(
//									LoginSignupActivity.this,
//									Welcome.class);
//							startActivity(intent);
							UserSession.getInstance(LoginActivity.this).setEmloyeeName(user.get(Constant.USER_NAME).toString());
							UserSession.getInstance(LoginActivity.this).setUserId(user.get(Constant.USER_ID).toString());
							UserSession.getInstance(LoginActivity.this).setAdmin(user.getBoolean(Constant.IS_MANAGER));
							UserSession.getInstance(LoginActivity.this).setSession(true);
							
							Toast.makeText(getApplicationContext(),
									"Successfully Logged in",
									Toast.LENGTH_LONG).show();
							
							Intent intent;
							if(user.getBoolean(Constant.IS_MANAGER)){
								//intent = new Intent(LoginActivity.this,HomeBaseActivity.class);
							//	Toast.makeText(LoginActivity.this, "Admin dashboard comming soon ...", Toast.LENGTH_SHORT).show();
								 intent = new Intent(LoginActivity.this,AdminDashboardActivity.class);
								startActivity(intent);
								finish();
							}else{
								intent = new Intent(LoginActivity.this,StudentDashboardActivity.class);
								startActivity(intent);
								finish();
							}
							
						} else {
							Toast.makeText(
									getApplicationContext(),
									"No such user exist, please signup",
									Toast.LENGTH_LONG).show();
						}
					}
				});

	}
}
