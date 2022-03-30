<%@page import="com.st1.servlet.GlobalValues"%>
<%@page import="com.st1.msw.UserInfo"%>
<%@page import="com.st1.util.PoorManUtil"%>
<%@page import="com.st1.servlet.SaveDoc"%>
<%@page import="com.st1.ifx.filter.FilterUtils"%>
<%@page import="org.apache.commons.text.StringEscapeUtils"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@page import="java.util.Locale"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	final Logger logger = LoggerFactory.getLogger("IFX-savedoc");
	Locale.setDefault(Locale.TRADITIONAL_CHINESE);
	request.setCharacterEncoding("UTF-8");
	response.setContentType("text/json");
	String result = null;
	String brno, tlrno, txtno, docs;
	tlrno = ""; //must init
	SaveDoc saveDoc = new SaveDoc();
	String cmd = StringEscapeUtils.escapeXml10(request.getParameter("cmd"));
	brno = StringEscapeUtils.escapeXml10(request.getParameter("brno"));
	txtno = StringEscapeUtils.escapeXml10(request.getParameter("txtno"));
	cmd = cmd.toUpperCase();
	logger.info("cmd:" + FilterUtils.escape(cmd));
	logger.info("brno:" + FilterUtils.escape(brno));
	logger.info("txtno:" + FilterUtils.escape(txtno));
	String day = PoorManUtil.getToday();
	String dt = StringEscapeUtils.escapeXml10(request.getParameter("dt"));
	if (cmd.toUpperCase(Locale.TAIWAN).equals("SAVE")) {
		docs = StringEscapeUtils.escapeXml10(request.getParameter("docs"));
		logger.info("docs:" + FilterUtils.escape(docs));
		result = saveDoc.save(day, brno, txtno, docs);
	} else if (cmd.equals("SEARCH")) {
		if (brno == null || brno.length() == 0) {
			UserInfo u = (UserInfo) session.getAttribute(GlobalValues.SESSION_USER_INFO);
			brno = u.getBrno();
			logger.info("using session brno:" + brno);
		}
		result = saveDoc.searchDoc(dt, brno, txtno);
	} else if (cmd.equals("INCR")) {
		if (brno == null || brno.length() == 0) {
			UserInfo u = (UserInfo) session.getAttribute(GlobalValues.SESSION_USER_INFO);
			brno = u.getBrno();
			tlrno = u.getId();
			logger.info("using session brno:" + brno + ",tlrno:" + tlrno);
		}
		String formId = StringEscapeUtils.escapeXml10(request.getParameter("form"));
		result = saveDoc.updatePrintLog(dt, brno, txtno, formId, tlrno);
	} else if (cmd.equals("FETCH")) {
		String form = StringEscapeUtils.escapeXml10(request.getParameter("form"));
		result = saveDoc.fecthDoc(day, brno, txtno, form);
	} else if (cmd.equals("REPORT")) {

		//if(brno==null || brno.length() == 0) {
		UserInfo u = (UserInfo) session.getAttribute(GlobalValues.SESSION_USER_INFO);
		brno = u.getBrno();
		logger.info("using session brno:" + brno);
		//}
		String filename = StringEscapeUtils.escapeXml10(request.getParameter("filename"));
		String startline = StringEscapeUtils.escapeXml10(request.getParameter("startline"));
		String scount = StringEscapeUtils.escapeXml10(request.getParameter("scount"));
		String gowhere = StringEscapeUtils.escapeXml10(request.getParameter("gowhere"));
		logger.info("gowhere:" + FilterUtils.escape(gowhere));
		if (gowhere.equals("online")) {
			tlrno = u.getId();
		} else {
			tlrno = "";
		}
		result = saveDoc.readFileline(dt, brno, tlrno, filename, startline, scount);
	} else if (cmd.equals("REPORTPAGE")) {

		//if(brno==null || brno.length() == 0) {
		UserInfo u = (UserInfo) session.getAttribute(GlobalValues.SESSION_USER_INFO);
		brno = StringEscapeUtils.escapeXml10(u.getBrno());
		//brno = StringEscapeUtils.escapeXml10(request.getParameter("brno"));//潘 CodeView 會是空值
		logger.info("using session brno:" + brno);
		//}
		String filename = StringEscapeUtils.escapeXml10(request.getParameter("filename"));
		String pagenum = StringEscapeUtils.escapeXml10(request.getParameter("pagenum"));
		String gowhere = StringEscapeUtils.escapeXml10(request.getParameter("gowhere"));
		logger.info(FilterUtils.escape("gowhere:" + gowhere));
		if (gowhere.equals("online")) {
			//tlrno = u.getId();
			tlrno = StringEscapeUtils.escapeXml10(request.getParameter("filetlrno"));
		} else {
			tlrno = "";
		}
		result = saveDoc.readFilepage(dt, brno, tlrno, filename, pagenum);
	} else if (cmd.equals("REPORTPAGEALL")) {

		//if(brno==null || brno.length() == 0) {
		UserInfo u = (UserInfo) session.getAttribute(GlobalValues.SESSION_USER_INFO);
		brno = u.getBrno();
		//filebrno
		//brno = StringEscapeUtils.escapeXml10(request.getParameter("brno"));//潘 CodeView 會是空值
		logger.info("using session brno:" + brno);
		//}
		String filename = StringEscapeUtils.escapeXml10(request.getParameter("filename"));
		String gowhere = StringEscapeUtils.escapeXml10(request.getParameter("gowhere"));
		logger.info("gowhere:" + FilterUtils.escape(gowhere));
		if (gowhere.equals("online")) {
			//tlrno = u.getId();
			tlrno = StringEscapeUtils.escapeXml10(request.getParameter("filetlrno"));
		} else {
			tlrno = "";
		}
		result = saveDoc.readFilepageall(dt, brno, tlrno, filename);
	} else if (cmd.equals("RSWIFTOVER")) {
		String filename = StringEscapeUtils.escapeXml10(request.getParameter("filename"));
		logger.info(FilterUtils.escape("filename:" + filename));
		result = saveDoc.readSwiftOver(filename);
	}
	logger.info(FilterUtils.escape(result));
	out.print(result);
%>