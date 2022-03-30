package com.st1.ifx.web;

import java.security.SecureRandom;
import java.util.Random;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@Service
@WebService(serviceName = "simpleService")
public class SimpleWebServiceEndpoint extends SpringBeanAutowiringSupport {

	@WebMethod
	public void hello() {
		System.out.println("hello");
	}

	@WebMethod
	public String greeting(String name) {
		return "hello " + name;
	}

	private static final int maxRands = 16;

	@WebMethod
	public int next1() {
		return new SecureRandom().nextInt();
	}

	@WebMethod
	public int[] nextN(final int n) {
		final int k = (n > maxRands) ? maxRands : Math.abs(n);
		int[] rands = new int[k];
		Random r = new SecureRandom();
		for (int i = 0; i < k; i++)
			rands[i] = r.nextInt();
		return rands;
	}

	public static void main(String[] args) {
		final String url = "http://localhost:8888/simple";
		System.out.println("Publishing SimpleWebServiceEndpoint at endpoint " + url);
		javax.xml.ws.Endpoint.publish(url, new SimpleWebServiceEndpoint());
	}
}
