package com.checkinn;

import android.app.Application;

import com.checkinn.dashboard.AdminDashboardActivity;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;
import com.parse.PushService;
//changes made
public class CheckInnApplication extends Application {
	 private static final String YOUR_APPLICATION_ID = "nxJK2NwJrFkEO3P2DhQlDIk2xYPdirqlID3vAQ8r";
		private static final String YOUR_CLIENT_KEY = "eNY8BFSkXDSn9RPysalil3sQRa529ws1FsPqUmuc";
	@Override
	public void onCreate() {
		super.onCreate();
		 // Add your initialization code here
        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
        PushService.setDefaultPushCallback(this, AdminDashboardActivity.class);
	}
}
