package com.checkinn.objects;

public class UserObject {
	public String userName="";
	public String password="";
	public String userID="";
	public String cafeName="";
	public String performanceRating="";
	public String totalSwapRequestPlaced="";
	public String totalShiftsCancelled="";
	public String attendancePercentage="";
	
	
	public boolean is_admin;
	public String a_date="";
	public String a_from="";
	public String a_to="";
	public String objectId="";
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return userName;
	}
	
}
