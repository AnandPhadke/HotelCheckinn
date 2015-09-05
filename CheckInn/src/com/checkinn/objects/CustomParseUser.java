package com.checkinn.objects;

import com.parse.ParseUser;

public class CustomParseUser extends ParseUser {

	@Override
	public String toString() {
		super.toString();
		return getUsername();
	}
}
