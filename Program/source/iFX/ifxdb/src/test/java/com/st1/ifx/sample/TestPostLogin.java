package com.st1.ifx.sample;

import com.st1.ifx.menu.Attach;
import com.st1.ifx.menu.C1200;
import com.st1.ifx.menu.SimpleMapper;

public class TestPostLogin {
//	ATTACH_TEXT=1                              00005050ABAB001000004505061100190
//	C12_TEXT=300100000000000000000000000 蔡坤能     5052610
	
	static String attachText = "1                              00005050ABAB001000004505061100190";
	static String c12Text = "300100000000000000000000000 蔡坤能     5052610";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("IN  TestPostLogin main!");
		Attach attach = SimpleMapper.parse(attachText, Attach.class);
		if(attach!=null) 
			System.out.println(attach);
		C1200 c12 = SimpleMapper.parse(c12Text, C1200.class);
		if(c12!=null)
			System.out.println(c12);
		
		
	}

}
