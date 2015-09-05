package com.checkinn.session_manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserSession {

	
	private static UserSession userSession;
	private static SharedPreferences loginPreferences;
	private static Editor loginPrefsEditor;
	
	  private UserSession() {
	  }

	  public static synchronized UserSession getInstance(Context context) {
			 
	    if (userSession == null) {
	    	userSession = new UserSession();
	    	loginPreferences = context.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
			 loginPrefsEditor = loginPreferences.edit();
	    }
	    return userSession;
	  }

	public boolean isRememberme() {
		return loginPreferences.getBoolean("saveLogin", false);
	}

	public void setRememberme(boolean isRememberme) {
		loginPrefsEditor.putBoolean("saveLogin", isRememberme);
		loginPrefsEditor.commit();
	}

	public String getUsername() {
		return loginPreferences.getString("username", "");
	}

	public void setUsername(String username) {
		 loginPrefsEditor.putString("username", username);
		 loginPrefsEditor.commit();
	}

	public String getPassword() {
	    return loginPreferences.getString("password", "");
	}

	public void setPassword(String password) {
		loginPrefsEditor.putString("password", password);
        loginPrefsEditor.commit();
	}
	  
	 public void clearSession(){
		 loginPrefsEditor.clear();
         loginPrefsEditor.commit();
	 }

	public String getEmloyeeName() {
	    return loginPreferences.getString("employee_name", "");
	}

	public void setEmloyeeName(String emloyeeName) {
		loginPrefsEditor.putString("employee_name", emloyeeName);
        loginPrefsEditor.commit();
	}

	public String getUserId() {
		 return loginPreferences.getString("user_id", "");
	}

	public void setUserId(String userId) {
		loginPrefsEditor.putString("user_id", userId);
        loginPrefsEditor.commit();
	}

	public boolean isAdmin() {
		return loginPreferences.getBoolean("is_admin", false);
	}

	public void setAdmin(boolean isAdmin) {
		loginPrefsEditor.putBoolean("is_admin", isAdmin);
		loginPrefsEditor.commit();
	}
	
	
	public boolean isSession() {
		return loginPreferences.getBoolean("is_session", false);
	}

	public void setSession(boolean isSession) {
		loginPrefsEditor.putBoolean("is_session", isSession);
		loginPrefsEditor.commit();
	}
	  
	  
}
