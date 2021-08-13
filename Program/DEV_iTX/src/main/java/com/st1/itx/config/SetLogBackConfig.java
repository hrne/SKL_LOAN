package com.st1.itx.config;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.LoggerFactory;

import com.st1.itx.util.filter.FilterUtils;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

public class SetLogBackConfig implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			if (System.getProperty("iTXPort") != null && System.getProperty("iTXPort").equals("7005"))
				this.load(System.getProperty("itx_Config") + "/logback_7005.xml");
			else
				this.load(System.getProperty("itx_Config") + "/logback.xml");
		} catch (Throwable t) {
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			System.out.println(errors.toString());
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	private void load(String externalConfigFileLocation) throws IOException, JoranException {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

		File externalConfigFile = new File(FilterUtils.filter(externalConfigFileLocation));
		if (!externalConfigFile.exists()) {
			throw new IOException("Logback External Config File Parameter does not reference a file that exists");
		} else {
			if (!externalConfigFile.isFile()) {
				throw new IOException("Logback External Config File Parameter exists, but does not reference a file");
			} else {
				if (!externalConfigFile.canRead()) {
					throw new IOException("Logback External Config File exists and is a file, but cannot be read.");
				} else {
					JoranConfigurator configurator = new JoranConfigurator();
					configurator.setContext(lc);
					lc.reset();
					configurator.doConfigure(externalConfigFileLocation);
					StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
				}
			}
		}
	}

}
