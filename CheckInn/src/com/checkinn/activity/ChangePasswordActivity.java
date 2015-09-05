package com.checkinn.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.checkinn.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class ChangePasswordActivity extends Activity implements OnClickListener {
	private Button btnChangePassword;
	private EditText etOldPassword;
	private EditText etPassword;
	private EditText etConfirmPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		
		btnChangePassword = (Button)findViewById(R.id.btnChangePassword);
		btnChangePassword.setOnClickListener(this);
		etOldPassword = (EditText)findViewById(R.id.etOldPassword);
		etPassword = (EditText)findViewById(R.id.etPassword);
		etConfirmPassword = (EditText)findViewById(R.id.etConfirmPassword);
	}

	@Override
	public void onClick(View v) {
		ParseUser.requestPasswordResetInBackground("test@gmail.com",
		        new RequestPasswordResetCallback() {
		            public void done(ParseException e) {

		                if (e == null) {
		                    // An email was successfully sent with reset
		                    // instructions.
		                    Toast.makeText(getApplicationContext(), "An email is successfully sent with reset intructions", Toast.LENGTH_LONG).show();
		                } else {
		                    // Something went wrong. Look at the ParseException
		                    // to see what's up.
		                    Toast.makeText(getApplicationContext(), "Oops something went wrong!!!", Toast.LENGTH_LONG).show();
		                }
		            }
		        }
		    );		
	}
}
