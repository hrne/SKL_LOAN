package com.st1.ifx.sample;

public class TestEcho {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// ApplicationContext ctx = new
		// ClassPathXmlApplicationContext("app-context-int-echo.xml");
		// JobLaunchRequest request = new JobLaunchRequest("job-echo",
		// Collections.singletonMap("p1", (new Date()).toString()));
		// Message<JobLaunchRequest> msg = MessageBuilder.withPayload(request).build();
		// MessageChannel channel = ctx.getBean("job-requests", MessageChannel.class);
		// channel.send(msg);

		String a = "11111111111111111111111111";
		char word = "XT511".charAt(1);
		int number = word; // 將取得的字元轉換成Unicode碼
		int newnumber = 0;
		newnumber = (number - 65);

		System.out.println(number);
		System.out.println(newnumber);
		System.out.println(a.charAt(newnumber));
	}

}
