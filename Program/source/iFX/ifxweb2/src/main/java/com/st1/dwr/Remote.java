package com.st1.dwr;

import java.util.Date;

public class Remote {
	public String getData(int index) {
		Date dt = new Date();
		System.out.println(dt.toString());
		return dt.toString();
	}

	public String putPerson(Person p) {
		return "Hello " + p.getName();
	}
}
