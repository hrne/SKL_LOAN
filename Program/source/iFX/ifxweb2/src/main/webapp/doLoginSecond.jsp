<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<%@page import="com.st1.ifx.hcomm.app.ImsLoginSock"%>
<%@page import="com.st1.ifx.hcomm.app.SessionMap"%>
<%@page import="com.st1.ifx.service.JournalService"%>
<%@page import="com.st1.ifx.filter.FilterUtils"%>
<%@page import="com.st1.servlet.GlobalValues"%>
<%@page import="com.st1.util.MySpring"%>
<%@page import="com.st1.msw.UserPub"%>
<%@page import="com.st1.msw.UserInfo"%>

<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>

<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>

<%@page import="org.apache.commons.text.StringEscapeUtils"%>

<%
	final Logger logger = LoggerFactory.getLogger("/doLoginSecond.jsp");

	boolean checkDupLogin = true;
	String message = "";
	String user = StringEscapeUtils.escapeXml10(request.getParameter("user"));
	user = user.toUpperCase();//小潘改user id轉大寫
	String pwd = request.getParameter("pwd");
	String addr = request.getRemoteAddr(); //127.0.0.1
	
	String authNo = request.getParameter("AuthNo");
	String authItem = request.getParameter("AuthItem");
	String agentNo = request.getParameter("AgentNo");
	String agentItem = request.getParameter("AgentItem");
	
	if(user == null)
	  user = (String) request.getAttribute("user");
	if(pwd == null)
	  pwd = (String) request.getAttribute("pwd");
	if(authNo == null)
	  authNo = (String) request.getAttribute("AuthNo");
	if(authItem == null)
	  authItem = (String) request.getAttribute("AuthItem");
	if(agentNo == null)
	  agentNo = (String) request.getAttribute("AgentNo");
	if(agentItem == null)
	  agentItem = (String) request.getAttribute("AgentItem");
	
	String ip = request.getHeader("X-Forwarded-For");
	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
	    ip = request.getHeader("Proxy-Client-IP");
	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
	    ip = request.getHeader("WL-Proxy-Client-IP");
	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
	    ip = request.getHeader("HTTP_CLIENT_IP");
	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
	    ip = request.getHeader("HTTP_X_FORWARDED_FOR");
	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
	    ip = request.getRemoteAddr();
	  
	//Login login = new Login(com.st1.servlet.GlobalValues.proxy, user,	pwd, addr);
	logger.info(FilterUtils.escape("user   : " + user));
	logger.info(FilterUtils.escape("addr:" + addr));

	ImsLoginSock login = MySpring.getLoginHostBean();
	login.setUser(user);
	login.setPwd(pwd);
	login.setAuthNo(authNo);
	login.setAuthItem(authItem);
	login.setAgentNo(agentNo);
	login.setAgentItem(agentItem);
	login.setIp(ip);
	
	login.setAddress(addr);

	//move to GlobalValues	
	//login.setLogFolder(GlobalValues.hostLogFolder);
	
	
	boolean ok = login.perform();
	
	
	logger.info("ok:" + ok);
	if (ok) {
		UserPub pub = UserPub.getInstance();
		String oldSessionId = UserPub.checkUser(user);
		if (oldSessionId != null) {
			if (checkDupLogin) {
				logger.info("duplicate login, old sessionId:" + FilterUtils.escape(oldSessionId));
				session.setAttribute("error1", "登入異常(Session Error:E001 DUPLOGIN)");
				session.setAttribute("currentSession", session.getId());
				session.setAttribute("oldSession", oldSessionId);
				session.setAttribute("user", user);
				response.sendRedirect("duplicateLogin.jsp");
				return;
			}
		}
		// check winname
		String clientWinName = StringEscapeUtils.escapeXml10(request.getParameter("winname"));
		String sessionWinName = (String) session.getAttribute("WINDOW_NAME");
		logger.info("client  Win Name:" + FilterUtils.escape(clientWinName));
		logger.info("session Win Name:" + FilterUtils.escape(sessionWinName));
		/*
		if (sessionWinName == null) {
			session.setAttribute("WINDOW_NAME", clientWinName);
			logger.info(FilterUtils.escape("first time login with WINDOW_NAME:" + clientWinName));
		} else {
			if (!(clientWinName.equals(sessionWinName))) {
				// different window, can't login
				if (checkDupLogin) {
					logger.info(FilterUtils.escape("can't login, session alredy contains WINDOW_NAME:" + session.getAttribute("WINDOW_NAME")));
					request.setAttribute("error", "E003-此瀏覽器已登入本系統!(WNAME)");

					request.getRequestDispatcher("errorLogin.jsp").forward(request, response);

					return;
				}
			} else {
				// same winname
				logger.info(FilterUtils.escape("same winname:" + clientWinName));
			}
		}
		*/

		message = "User login successfully ";

		// append user define var
		SessionMap sessionMap = login.getSession();
		HashMap<String, String> m = GlobalValues.getUserDefVarMap();
		Iterator<String> iter = m.keySet().iterator();
		while (iter.hasNext()) {
			String k = iter.next();
			String v = m.get(k);
			sessionMap.put(k, v);
		}

		/*	
		 // same session new user, when use Control+N, or new tab of same ie window
		 String lastUser = (String) session.getAttribute(GlobalValues.SESSION_UID);
		 if(lastUser!=null) {
		 logger.info("same session, difference user, last:"+ lastUser  + ", new user:"+ user);
		 session.setAttribute("error1", "登入異常(Session E002 SAMESESS)");
		 session.setAttribute("currentSession", session.getId());
		 session.setAttribute("user", user);
		 response.sendRedirect("duplicateLogin.jsp");
		 return;
		 }
		 */
		String json = login.getSessionAsJSON();

		logger.info("json:" + FilterUtils.escape(json));
		//session.setMaxInactiveInterval(30);
		session.setAttribute(GlobalValues.SESSION_SYSVAR, json);
		session.setAttribute(GlobalValues.SESSION_UID, user);

		// store last host seq
		session.setAttribute(GlobalValues.SESSION_LAST_HOSTSEQ, (String) sessionMap.get("TXTNO"));

		String tlrno = (String) sessionMap.get("TLRNO");
		String brn = (String) sessionMap.get("BRN");
		String name = (String) sessionMap.get("EMPNM");
		String level = (String) sessionMap.get("LEVEL"); // 上一版為SLEVEL
		String busDate = (String) sessionMap.get("DATE");
		//柯  新增主管對交易的權限。
		String dapKnd = (String) sessionMap.get("DAPKND");
		String oapKnd = (String) sessionMap.get("OAPKND");
		String cldept = (String) sessionMap.get("CLDEPT");

		String pswd = (String) sessionMap.get("PSWD");
		UserInfo u = new UserInfo(brn, tlrno, level, name, session.getId(), dapKnd, oapKnd, cldept, pswd);

		/*
		JournalService jnlService = MySpring.getJournalService();
		int lastSeq = jnlService.getLastSeq(busDate, brn, tlrno);
		u.setLastJnlSeq(lastSeq);
		System.out.printf("tlr:%s last jnl seq:%d\n", tlrno, lastSeq);
		 */

		session.setAttribute(GlobalValues.SESSION_USER_INFO, u);

		// TranListBuilder.java改從sesionMap讀取所需變數資料
		//session.setAttribute(GlobalValues.SESSION_USER_ATTACH, sessionMap.get("ATTACH_TEXT"));
		//session.setAttribute(GlobalValues.SESSION_USER_C12, sessionMap.get("C12_TEXT"));

		session.setAttribute(GlobalValues.SESSION_SYSVAR_MAP, sessionMap);

		// check ifx-core2.js 	Ifx.prototype.getTmpFilename
		String scrPrefix = "scr" + "/" + sessionMap.get("DATE") + "_" + sessionMap.get("BRN") + "_" + sessionMap.get("TLRNO") + "_";
		logger.info("scr prefix:" + FilterUtils.escape(scrPrefix));
		session.setAttribute(GlobalValues.SESSION_USER_SCRFILE_PREFIX, scrPrefix);

		logger.info("session Id dologin:" + session.getId());
		logger.info("KE1:" + GlobalValues.SESSION_USER_INFO);
		logger.info("KE2:" + session.getAttribute(GlobalValues.SESSION_USER_INFO));
		response.sendRedirect("getCache.jsp");
		//response.sendRedirect("env/step1.jsp");
		//response.sendRedirect("easy_main.jsp");

	} else { // login failed
		message = login.getErrmsg();
		if (message == null) {
			message = "主機無回應";
		}

		session.setAttribute("error", message);
		session.setAttribute("user", user);

		//response.sendRedirect("login.jsp");
		request.getRequestDispatcher("login.jsp").forward(request, response);
		return;
	}
%>