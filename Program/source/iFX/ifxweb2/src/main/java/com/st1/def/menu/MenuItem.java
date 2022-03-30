package com.st1.def.menu;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MenuItem {
	static final Logger logger = LoggerFactory.getLogger(MenuItem.class);

	String code;
	String name;
	String auth;
	String pass;
	String cor;
	String type;

	public MenuItem() {
	}

	public MenuItem(String code, String name, String type, String auth, String pass, String cor) {
		this.code = code;
		this.name = name;
		this.type = type;
		this.auth = auth;
		this.pass = pass;
		this.cor = cor;

	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isSwift() {
		if (this.type != null && this.type.equals("swift"))
			return true;
		else
			return false;
	}

	@Override
	public String toString() {
		return "code:" + code + ", name:" + name;
	}

	public String toJson() {
		return toJson(this);
	}

	private static String toJson(Object obj) {
		String result = null;
		ObjectMapper m = new ObjectMapper();
		try {
			result = m.writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			throw new RuntimeException(e);
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			throw new RuntimeException(e);
		}
		return result;
	}
}