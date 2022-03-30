<%@page import="com.st1.ifx.hcomm.app.ImsLoginSock"%>
<%@page import="com.st1.util.MySpring"%>
<%@page import="com.st1.ifx.service.JournalService"%>
<%@page import="com.st1.msw.UserPub"%>
<%@page import="com.st1.msw.UserInfo"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.st1.ifx.hcomm.app.SessionMap"%>
<%@page import="java.util.HashMap"%>
<%@page import="org.apache.commons.text.StringEscapeUtils"%>
<%@page import="com.st1.servlet.GlobalValues"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String message = "";
	String user = StringEscapeUtils.escapeXml10(request.getParameter("user"));
	String password = StringEscapeUtils.escapeXml10(request.getParameter("password"));
	String addr = "127.0.0.1";
	System.out.println("client:" + request.getRemoteAddr());

	//Login login = new Login(com.st1.servlet.GlobalValues.proxy, user,
	//		password, addr);

	ImsLoginSock login = MySpring.getLoginHostBean();
	login.setUser(user);
	login.setPassword(password);
	login.setAddress(addr);

	// move to GlobalValues	
	//	login.setLogFolder(GlobalValues.hostLogFolder);
	boolean ok = login.perform();

	if (ok) {
		// check winname
		String clientWinName = StringEscapeUtils.escapeXml10(request.getParameter("winname"));
		String sessionWinName = (String) session.getAttribute("WINDOW_NAME");
		System.out.println("client Win Name:" + clientWinName);
		System.out.println("session Win Name:" + sessionWinName);
		if (sessionWinName == null) {
			session.setAttribute("WINDOW_NAME", clientWinName);
			System.out.println("first time login with WINDOW_NAME:" + clientWinName);
		} else {
			if (!(clientWinName.equals(sessionWinName))) {
				// different window, can't login
				System.out.println("can't login, session alredy contains WINDOW_NAME:" + session.getAttribute("WINDOW_NAME"));
				request.setAttribute("error", "E003-此瀏覽器已登入本系統!(WNAME)");
				request.getRequestDispatcher("errorLogin.jsp").forward(request, response);
				return;
			} else {
				// same winname
				System.out.println("same winname:" + clientWinName);
			}
		}

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

		UserPub pub = UserPub.getInstance();
		String oldSessionId = UserPub.checkUser(user);
		if (oldSessionId != null) {

			System.out.println("duplicate login, old sessionId:" + oldSessionId);

			session.setAttribute("error1", "登入異常(Session Error:E001 DUPLOGIN)");
			session.setAttribute("currentSession", session.getId());
			session.setAttribute("oldSession", oldSessionId);
			session.setAttribute("user", user);
			response.sendRedirect("duplicateLogin.jsp");

			return;
		}
		/*	
		 // same session new user, when use Control+N, or new tab of same ie window
		 String lastUser = (String) session.getAttribute(GlobalValues.SESSION_UID);
		 if(lastUser!=null) {
		 System.out.println("same session, difference user, last:"+ lastUser  + ", new user:"+ user);
		 session.setAttribute("error1", "登入異常(Session E002 SAMESESS)");
		 session.setAttribute("currentSession", session.getId());
		 session.setAttribute("user", user);
		 response.sendRedirect("duplicateLogin.jsp");
		 return;
		 }
		 */
		String json = login.getSessionAsJSON();

		System.out.println("json:" + json);
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
		String pswd = (String) sessionMap.get("PSWD"); // 潘2017/12/07

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
		System.out.println("scr prefix:" + scrPrefix);
		session.setAttribute(GlobalValues.SESSION_USER_SCRFILE_PREFIX, scrPrefix);
		response.sendRedirect("env/step1.jsp");
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