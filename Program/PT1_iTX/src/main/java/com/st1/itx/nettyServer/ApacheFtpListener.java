package com.st1.itx.nettyServer;

import com.st1.itx.util.MySpring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ApacheFtpListener implements ServletContextListener {
	private final Logger logger = LoggerFactory.getLogger(ApacheFtpListener.class);
	private final String SERVER_NAME = "FTP-SERVER";

	private ApacheFtpServer apacheFtpServer;

	// 容器關閉時調用方法stop ftpServer
	public void contextDestroyed(ServletContextEvent sce) {

		apacheFtpServer.stop();
		sce.getServletContext().removeAttribute(SERVER_NAME);
		logger.info("Apache Ftp server is stoped!");

	}

	// 容器初始化調用方法start ftpServer
	public void contextInitialized(ServletContextEvent sce) {

		apacheFtpServer = (ApacheFtpServer) MySpring.getBean("apacheFtpServer");

		sce.getServletContext().setAttribute(SERVER_NAME, apacheFtpServer);
		try {
			apacheFtpServer.start();
			logger.info("Apache Ftp server is started!");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Apache Ftp server start failed!", e);
		}

	}

}
