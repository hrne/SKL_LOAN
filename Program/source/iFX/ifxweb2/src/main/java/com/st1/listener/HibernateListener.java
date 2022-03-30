package com.st1.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.st1.dao.SessionManager;

/**
 * Application Lifecycle Listener implementation class HibernateListener
 * 
 */
// @WebListener
public class HibernateListener implements ServletContextListener {

	/**
	 * Default constructor.
	 */
	public HibernateListener() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		SessionManager.getSessionFactory(); // Just call the static initializer
											// of that class
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		SessionManager.getSessionFactory().close(); // Free all resources

	}

}
