package com.st1.itx.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
//import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.st1.itx.main.opMain;
//import com.st1.itx.util.MySpring;

public class MainSocketServer {
	private static final Logger logger = LoggerFactory.getLogger(MainSocketServer.class);

	public String portNumber;

	public ServerSocket servSock;

	public Thread t1;

	public Executor service;

	public void serverInit() {
		try {
			servSock = new ServerSocket(Integer.parseInt(portNumber));
			t1 = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						serverExecutor();
					} catch (Exception e) {
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						logger.error(errors.toString());
					}
				}
			});
			t1.start();
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
	}

	public void serverExecutor() throws Exception {
		logger.info("ready port : " + portNumber);
		this.service = Executors.newCachedThreadPool();

		while (true) {
//			Socket cloneSock = servSock.accept();
			logger.info("client connected");
//			opMain main = MySpring.getBean("mainApp", opMain.class);
//			main.setCloneSock(cloneSock);
//			service.execute(main);
		}
	}

	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}

	public String getPortNumber() {
		return this.portNumber;
	}
}
