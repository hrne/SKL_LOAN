package com.st1.ifx.hcomm.fmt;

import java.util.HashMap;

@SuppressWarnings("rawtypes")
public class PoorMap extends HashMap {
	private static final long serialVersionUID = -220068098755672461L;

	public String getValueAt(String key) {
		if (this.containsKey(key)) {
			return (String) super.get(key);
		}
		String altKey = "H." + key;
		if (this.containsKey(altKey)) {
			return (String) this.get(altKey);
		}
		altKey = "B." + key;
		if (this.containsKey(altKey)) {
			return (String) this.get(altKey);
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	public void setValueAt(String key, String value) {
		super.put(key, value);
	}

	public void setHeaderValueAt(String key, String value) {
		setValueAt("H." + key.toUpperCase(), value);
	}

	public void setBasicValueAt(String key, String value) {
		setValueAt("B." + key.toUpperCase(), value);
	}

}
