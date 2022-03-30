package com.st1.ifx.tmp;

import java.net.MalformedURLException;
import java.net.URL;

import com.st1.sklwebservice.WsSKLAuthentication;
import com.st1.sklwebservice.WsSKLAuthenticationSoap;

public class AdClient {

	public static void main() throws MalformedURLException {
		URL url = new URL("https://t-ws.skl.com.tw/RequirementWebService/wsSkl_Authentlication.asmx?wsdl");

		WsSKLAuthentication ss = new WsSKLAuthentication(url);

		WsSKLAuthenticationSoap port = ss.getWsSKLAuthenticationSoap();

		boolean is = port.adIsAuthenticated("Adam", "1qaz2wsx");
	}

}