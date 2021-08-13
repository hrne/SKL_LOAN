package com.st1.test;

import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestNettyMain {

	public static AbstractXmlApplicationContext ac;

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		ac = new ClassPathXmlApplicationContext("classpath:spring-testnetty.xml");
		NettyClient nc = ac.getBean(NettyClient.class);

//		ns.startNettyServer();
//		Thread.sleep(3000);
//		while(true) {
//			ns.sendMessage(null,"hello".getBytes());
//			Thread.sleep(500);
//			ns.sendMessage(null,"hello".getBytes());
//			Thread.sleep(500);
//			ns.sendMessage(null,"hello".getBytes());
//			Thread.sleep(500);
//			ns.sendMessage(null,"hello".getBytes());
//			Thread.sleep(500);
//			ns.sendMessage(null,"中文三四無六其把就時中文三四無六其把就時中文三四無六其把就時中文三四無六其把就時中文三四無六其把就時中文三四無六其把就時中文三四無六其把就時中文三四無六其把就時中文三四無六其把就時中文三四無六其把就時中文三四無六其把就時中文三四無六其把就時中文三四無六其把就時中文三四無六其把就時中文三四無六其把就時".getBytes());
//			Thread.sleep(500);
//		}

		nc.startNettyClient();
		Thread.sleep(3000);
		while (true) {
			nc.sendMessage("hello".getBytes());
			Thread.sleep(500);
			nc.sendMessage("hello".getBytes());
			Thread.sleep(500);
			nc.sendMessage("hello".getBytes());
			Thread.sleep(500);
			nc.sendMessage("hello".getBytes());
			Thread.sleep(500);
			nc.sendMessage("中文三四無六其把就時中文三四無六其把就時中文三四無六其把就時中文三四無六其把就時中文三四無六其把就時中文三四無六其把就時中文三四無六其把就時中文三四無六其把就時中文三四無六其把就時中文三四無六其把就時中文三四無六其把就時中文三四無六其把就時中文三四無六其把就時中文三四無六其把就時中文三四無六其把就時".getBytes());
			Thread.sleep(500);
		}

//		System.out.println("main function");
	}

}