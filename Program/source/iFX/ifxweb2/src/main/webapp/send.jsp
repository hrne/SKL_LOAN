<%@page import="java.io.IOException"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.StringWriter"%>

<%@page import="java.util.Enumeration"%>
<%@page import="java.util.List"%>

<%@page import="org.apache.commons.text.StringEscapeUtils"%>

<%@page import="com.st1.ifx.hcomm.HostTran"%>
<%@page import="com.st1.util.MySpring"%>
<%@page import="com.st1.servlet.GlobalValues"%>
<%@page import="com.st1.msw.UserInfo"%>
<%@page import="com.st1.ifx.filter.FilterUtils"%>
<%@page import="com.st1.ifx.dataVo.TitaVo"%>

<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@ page language="java" contentType="application/json; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	final Logger logger = LoggerFactory.getLogger("IFX-Systeam");
	response.setContentType("text/json");
	String result = send(session, request);
	
	out.print(result);
	logger.info("leave send");
%>

<%!static String send(HttpSession session, HttpServletRequest request) {
		final Logger logger = LoggerFactory.getLogger("Send.jsp");
		String txcd = StringEscapeUtils.escapeXml11(request.getParameter("txcd"));
		String pbrno = StringEscapeUtils.escapeXml11(request.getParameter("pbrno"));
		String key = StringEscapeUtils.escapeXml11(request.getParameter("key"));
		String tita = request.getParameter("tita");
		String titasix = request.getParameter("titaRsix");
		String rim = StringEscapeUtils.escapeXml11(request.getParameter("rim"));
		String text = StringEscapeUtils.escapeXml11(request.getParameter("text"));
		String msgMode = StringEscapeUtils.escapeXml11(request.getParameter("msgMode"));
		String origSeq = StringEscapeUtils.escapeXml11(request.getParameter("origSeq"));
		/*潘 resv json格式的雙引號會被替換掉 不能用escapeXml11*/
		//String resv = StringEscapeUtils.escapeXml11(request.getParameter("resv"));
		String resv = request.getParameter("resv");
		String mq = StringEscapeUtils.escapeXml11(request.getParameter("mq"));
		String updatedType = StringEscapeUtils.escapeXml11(request.getParameter("updTx"));
		String sJnlId = StringEscapeUtils.escapeXml11(request.getParameter("jnlId"));
		String rqsp = StringEscapeUtils.escapeXml11(request.getParameter("rqsp"));
		String supno = StringEscapeUtils.escapeXml11(request.getParameter("supno"));
		String level = StringEscapeUtils.escapeXml11(request.getParameter("level"));

		String hostOvrMode = StringEscapeUtils.escapeXml11(request.getParameter("hostOvrMode"));
		String psbkReturnMode = StringEscapeUtils.escapeXml11(request.getParameter("psbkReturnMode"));
		Enumeration<String> requestParameters = request.getParameterNames();
		
		String ip = request.getHeader("X-Forwarded-For");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }

		while (requestParameters.hasMoreElements()) {
			String paramName = (String) requestParameters.nextElement();
			logger.info("Request Paramter Name: " + paramName + ", Value - " + request.getParameter(paramName));
		}

		if (psbkReturnMode.equals("1"))
			rim = "1";

		HostTran hosttran = MySpring.getHostTranBean();
		TitaVo titaVo = null;

		try {
			if (rim.equals("1"))
				titaVo = new TitaVo(tita, text);
			else
				titaVo = new TitaVo(titasix, tita);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.warn(errors.toString());
		}

		hosttran.setSession(session);
		hosttran.setKey(key);
		hosttran.setbRim(rim.equals("1"));
		hosttran.setUpdatedType(updatedType);
		hosttran.setJnlId(Long.parseLong(sJnlId));

		hosttran.setMsgMode(Integer.parseInt(msgMode));
		hosttran.setOrigSeq(Integer.parseInt(origSeq));

		titaVo.put("rim", rim);
		titaVo.setTxCode(txcd);
		titaVo.setBrTlrNo(key);
		titaVo.setRqsp(rqsp.trim());
		titaVo.setSupno(supno.trim());
		titaVo.setLevel(level.trim());
		titaVo.setPbrno(pbrno);
		titaVo.setIp(ip);

		hosttran.setHostOvrMode(Integer.parseInt(hostOvrMode));		
		hosttran.send(tita, text, resv, mq, titaVo);
		return hosttran.getTotaJson();
	}%>