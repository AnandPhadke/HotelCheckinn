package com.checkinn;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.ProgressDialog;
import android.content.Context;

public class Utils {

	
	
	public static ProgressDialog showProgressDialog(Context c){
		return ProgressDialog.show(c, "Cafetria",
			    "Loading...", false,false);
	}
	public static Calendar DateToCalendar(Date date){ 
		  Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-700"));
		  cal.setTime(date);
		  return cal;
		}
}
