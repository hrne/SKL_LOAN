<%@page import="org.apache.commons.text.StringEscapeUtils"%>
<%@page import="com.st1.ifx.filter.FilterUtils"%>
<%@page import="com.st1.ifx.filter.SafeClose"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="java.util.regex.Pattern"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.io.*,java.net.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>My JSP 'log.jsp' starting page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<c:set var="script" value="${pageContext.request.contextPath}/script" />
<script src="<%=request.getContextPath()%>/script/jquery.js"></script>
<script src="<%=request.getContextPath()%>/script/jqueryui/js/jquery-ui-1.10.3.custom.min.js"></script>
<script>
	
<%OutputStream output = null;
			InputStream in = null;

			String jspVar = null;
			String jspVarp = null;

			if (StringEscapeUtils.escapeXml10(request
					.getParameter("hiddenTextBox")) != null) {
				jspVar = StringEscapeUtils.escapeXml10(request
						.getParameter("hiddenTextBox"));
				jspVarp = StringEscapeUtils.escapeXml10(request
						.getParameter("hiddenPathBox"));

				//out.println("Jsp Var : " + jspVar);
				String logpath = jspVarp + "/";
				String filename = jspVar; //檔案名
				filename = new String(filename.getBytes("ISO-8859-1"), "Big5");
				//out.println("logpath:"+logpath);
				File file = new File(FilterUtils.filter(logpath + filename));
				if (file.exists()) { //檢驗檔案是否存在
					String regex = "[`~!@#$%^&*()\\+\\=\\{}|:\"?><【】\\/r\\/n]";
					Pattern pa = Pattern.compile(regex);
					Matcher ma = pa.matcher(filename);
					if (ma.find()) {
						filename = ma.replaceAll("");
					}
					try {
						response.setHeader(
								"Content-Disposition",
								"attachment; filename=\""
										+ URLEncoder.encode(filename, "UTF-8")
										+ "\"");

						output = response.getOutputStream();
						in = new FileInputStream(file);
						byte[] resultBuff = new byte[0];
						byte[] buff = new byte[1024];
						int k = -1;
						while ((k = in.read(buff, 0, buff.length)) > -1) {
							byte[] tbuff = new byte[resultBuff.length + k]; // temp buffer size = bytes already read + bytes last read
							System.arraycopy(resultBuff, 0, tbuff, 0,
									resultBuff.length); // copy previous bytes
							System.arraycopy(buff, 0, tbuff, resultBuff.length,
									k); // copy current lot
							resultBuff = tbuff; // call the temp buffer as your result buff
						}

						output.write(resultBuff);
						// byte[] b = new byte[2048];
						//int len;
						//
						// while((len = in.read(b))>0){
						//   output.write(b,0,len);
						// }
						output.flush();
						out.clear();
						out = pageContext.pushBody();
					} catch (Exception ex) {
						out.println("Webfilelog Error 81");
					} finally {
						SafeClose.close(in);
						SafeClose.close(output);
					}

				} else {
					out.println(filename + " : 此檔案不存在");
					out.println("<br/>");
				}
			}%>
	
</script>

</head>

<body>
	weblog讀取檔案測試
	<br> 檔案
	<br>
	<button onclick="myFunction2()">Click me</button>
	<script>
		function myFunction2() {
			document.myForm2.hiddenTextBox.value = $('#lognnn').val();
			document.myForm2.hiddenPathBox.value = $('#logpath').val();
			document.forms['myForm2'].submit();
		};
	</script>
	<input id="lognnn" value="2016-05-17_105858.log" style="width: 300px;" />
	<br>
	<input id="logpath" value="/fxtxlog/log/" style="width: 300px;" />
	<br>
	<!--柯 myForm只是給通知報表取檔案-->
	<form id="myForm2" name="myForm2" method="post">
		<input type="hidden" name="hiddenTextBox"><br> <input type="hidden" name="hiddenPathBox"><br>
	</form>
</body>
</html>
