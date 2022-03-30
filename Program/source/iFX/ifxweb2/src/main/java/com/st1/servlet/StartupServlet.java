package com.st1.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Properties;

import javax.crypto.NoSuchPaddingException;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;

import com.st1.util.EncrypAES;
import com.st1.util.MySpring;
import com.st1.ifx.config.SetLogBackConfig;
import com.st1.ifx.filter.*;

/**
 * Servlet implementation class StartupServlet
 */
// @WebServlet(name="StartupServlet",loadOnStartup=1)
public class StartupServlet extends HttpServlet {
	static final Logger logger = LoggerFactory.getLogger(StartupServlet.class);
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StartupServlet() {
		super();
		logger.info("startupservlet constructor");
	}

	public void init() {
		logger.info("hello ifx");
		logger.info(getServletName() + ": inited");
		InitialContext ic = null;
		String serverName = "";
		String cellname = "";
		String nodename = "";
		String moduleName = "";
		String applicationName = "";
		String portNumber = "55552";
		String localAddr = "";
		String otherAddr = "";
		String otherMqAddr = "";

		try {
			String temp1 = ManagementFactory.getRuntimeMXBean().getName();
			logger.info("getRuntimeMXBean..:" + temp1);
			ic = new javax.naming.InitialContext();
			serverName = ic.lookup("servername").toString();
			cellname = ic.lookup("thisNode/cell/cellname").toString();
			nodename = ic.lookup("thisNode/nodename").toString();
			moduleName = ic.lookup("java:module/ModuleName").toString();
			applicationName = ic.lookup("java:app/AppName").toString();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			logger.error("get name error..:" + e1);
		}
		logger.info("TEST2");

		logger.info("servername..:" + FilterUtils.escape(serverName));
		logger.info("cellname..:" + FilterUtils.escape(cellname));
		logger.info("nodename..:" + FilterUtils.escape(nodename));
		logger.info(FilterUtils.escape("moduleName..:" + moduleName));
		logger.info(FilterUtils.escape("applicationName..:" + applicationName));

		logger.info("DEF portNumber..:" + portNumber);
		portNumber = System.getProperty("ifx_PortNumber");
		logger.info("getProperty ifx_PortNumber..:" + FilterUtils.escape(portNumber));
		// 因為好像沒法 try catch 故增加此段
		if (portNumber.isEmpty()) {
			portNumber = "55552";
		}

		logger.info(FilterUtils.escape("setProperty portNumber..:" + portNumber));
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		logger.info("after loggerContext");
		System.setProperty("log_path", portNumber);

		// 儲存在Global變數
		GlobalValues.setMqServerPort(portNumber);

		localAddr = System.getProperty("ifx_LocalAddr");
		otherAddr = System.getProperty("ifx_OtherAddr");
		otherMqAddr = System.getProperty("ifx_OtherMqAddr");
		logger.info("getProperty ifx_LocalAddr..:" + FilterUtils.escape(localAddr));
		logger.info("getProperty ifx_OtherAddr..:" + FilterUtils.escape(otherAddr));
		logger.info("getProperty ifx_OtherMqAddr..:" + FilterUtils.escape(otherMqAddr));
		GlobalValues.setLocalAddr(localAddr);
		GlobalValues.setOtherAddr(otherAddr);
		GlobalValues.setOtherMqAddr(otherMqAddr);

		// RollingFileAppender rfAppender = new RollingFileAppender();
		// rfAppender.setContext(loggerContext);
		// FixedWindowRollingPolicy rollingPolicy = new FixedWindowRollingPolicy();
		// rollingPolicy.setContext(loggerContext);
		// rollingPolicy.stop();
		// rfAppender.stop();
		// rollingPolicy.start();
		// rfAppender.start();

		ContextInitializer ci = new ContextInitializer(loggerContext);

		loggerContext.reset(); // 移除在開發變兩個?? 但open不會..
		logger.info("loggerContext reset!!");
		try {

			// I prefer autoConfig() over JoranConfigurator.doConfigure() so I wouldn't
			// need to find the file myself.
			ci.autoConfig();
			SetLogBackConfig setlog = new SetLogBackConfig();
			setlog.contextInitialized(null);
			logger.info("ContextInitializer autoConfig!!");
		} catch (Exception e) {
			// StatusPrinter will try to log this
			logger.info("error3" + e);
		}
		displayEncoding();
		logger.info("hello ifx1");
		displayEnv();
		logger.info("hello ifx2");
		displaySysProps();
		logger.info("hello ifx3");
		// String ifxHome = System.getenv("ifx-home");
		String fxworkFile = System.getProperty("ifx_fxworkfile");
		logger.info(FilterUtils.escape("fxworkFile:" + fxworkFile));
		String fxtxWrite = System.getProperty("ifx_fxtxwrite");
		logger.info(FilterUtils.escape("fxtxwrite:" + fxtxWrite));
		GlobalValues.setIfxFolder(fxworkFile, fxtxWrite);

		/*
		 * fxworkfile 檔案 -> ifxHome fxtxlog log 和 暫存檔案 ifx_tita docs temp 等等 log fxpara
		 * 參數 sna webServerEnv
		 */

		// GlobalValues.setIfxFolder(this.getInitParameter("ifx-folder"));
		// fmt.Env.setFmtFolder(GlobalValues.combine("fmt"));

		// GlobalValues.proxy = this.getInitParameter("proxy");
		// GlobalValues.menu = this.getInitParameter("menu");
		// GlobalValues.tranFolder = this.getInitParameter("tran-folder");
		//
		// ////logger.info("IFX MqServer M System started");
		// (new PdfServer()).start();
		// ////PdfThread = (new PdfServer());
		// try {
		// PdfServer.mainPDF();
		// } catch (DocumentException e1) {
		// // TODO Auto-generated catch block
		// StringWriter errors = new StringWriter();
		// e1.printStackTrace(new PrintWriter(errors));
		// logger.error(errors.toString());
		// }
		// ////PdfThread.start();

		try {
			logger.info("do GlobalValues.readEvn!");
			GlobalValues.readEvn();
			// GlobalValues.buildMenu();
		} catch (Exception e) {
			logger.error("GlobalValues.readEvn error:" + e.getMessage());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}

		try {
			// 在啟動war時,對加密功能設定 keygen 等等..
			EncrypAES.EncrypSet();
		} catch (NoSuchAlgorithmException e1) {
			StringWriter errors = new StringWriter();
			e1.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} catch (NoSuchPaddingException e1) {
			StringWriter errors = new StringWriter();
			e1.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}

		try {
			logger.info("do GlobalValues.generateJS!");
			GlobalValues.generateJS(getWebPath());
			// GlobalValues.buildMenu();
		} catch (Exception e) {
			logger.error("GlobalValues.generateJS error:" + e.getMessage());
		}

		GlobalValues.systemStarted = true;
		log("IFX System started");

		log("test spring");
		WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(getServletContext());

		// HelpListService helpService = ctx.getBean("helpListService",
		// HelpListService.class);
		// HelpList helpList = helpService.findById(23L);
		// logger.info(helpList);

		MySpring.getTranListBuilder().preload();
		GlobalValues.loadApplicationProps();

//		logger.info("Load AStarLoadUtils!!");
//		AStarLoadUtils.init();

	}

	private void displaySysProps() {
		Properties props = System.getProperties();
		logger.info("hello ifx6");
		try {
			props.list(System.out);
		} catch (Exception e) {
			logger.info("displaySysProps error:" + e);
		}
		logger.info("hello ifx7");

	}

	private void displayEnv() {
		Map<String, String> env = System.getenv();
		logger.info("hello ifx4");
		for (String envName : env.keySet()) {
			logger.info("hello envName:" + envName + "," + env.keySet());
			System.out.format("%s=%s%n", envName, env.get(envName));
		}
		logger.info("hello ifx5");
	}

	private void displayEncoding() {
		String defaultCharacterEncoding = System.getProperty("file.encoding");
		logger.info("defaultCharacterEncoding by property: " + FilterUtils.escape(defaultCharacterEncoding));
		logger.info("defaultCharacterEncoding by code: " + getDefaultCharEncoding());
		logger.info("defaultCharacterEncoding by charSet: " + Charset.defaultCharset());

		// System.setProperty("file.encoding", "UTF-8");
		//
		// logger.info("defaultCharacterEncoding by property after updating
		// file.encoding
		// : "
		// + System.getProperty("file.encoding"));
		//
		// logger.info("defaultCharacterEncoding by code after updating file.encoding :
		// "
		// + getDefaultCharEncoding());
		//
		// logger.info("defaultCharacterEncoding by java.nio.Charset after updating
		// file.encoding : "
		// + Charset.defaultCharset());

	}

	private String getDefaultCharEncoding() {
		byte[] bArray = { 'w' };
		InputStream is = new ByteArrayInputStream(bArray);
		InputStreamReader reader = new InputStreamReader(is);
		String defaultCharacterEncoding = reader.getEncoding();
		return defaultCharacterEncoding;

	}

	private String getWebPath() {
		String dir = this.getServletContext().getRealPath("/script");
		logger.info("getWebPath dir:" + dir);
		// i(dir==null) {
		// try {
		// dir = getServletContext().getRequest().getSession().
		// getServletContext().getResource("/").getPath();
		// }
		// catch (MalformedURLException ex) {
		// StringWriter errors = new StringWriter();
		// e1.printStackTrace(new PrintWriter(errors));
		// logger.error(errors.toString());
		// }
		// }
		return dir;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cmd = request.getParameter("cmd");
		if (cmd == null)
			return;

		if (cmd.equalsIgnoreCase("reload")) {
			try {
				logger.info("reload help/error js/fourCode");
				GlobalValues.generateJS(getWebPath());
				PrintWriter out = response.getWriter();
				out.println("reload help/error.js ");
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
			}
		} else if (cmd.equalsIgnoreCase("list")) {
			response.sendRedirect("listEnv.jsp");
		}
	}

	public void destroy() {
		;
	}
}
