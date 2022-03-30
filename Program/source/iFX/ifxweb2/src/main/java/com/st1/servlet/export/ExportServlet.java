package com.st1.servlet.export;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.MimeUtility;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.st1.ifx.filter.FilterUtils;
import com.st1.util.PoorManUtil;

/**
 * Servlet implementation class ExportServlet
 */
@WebServlet("/ExportServlet")
public class ExportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static final Logger logger = LoggerFactory.getLogger(ExportServlet.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ExportServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ExcelUtil excelUtil = new ExcelUtil();
		Gson gson = new Gson();
		// JsonParser jsonParser = new JsonParser();
		// JsonArray cols = null;
		String[][] cols = null;
		try {
			OutputStream outputStream = response.getOutputStream();
			String oper = request.getParameter("oper");
			String data = URLDecoder.decode(request.getParameter("data"), "UTF-8");

			String title = URLDecoder.decode(request.getParameter("title"), "UTF-8");

			logger.info(FilterUtils.escape("oper:" + oper));
			logger.info(FilterUtils.escape("title:" + title));
			logger.info(FilterUtils.escape("data:\n" + data));

			if (data != null && data.length() > 0) {
				// cols = jsonParser.parse(data).getAsJsonArray();
				cols = gson.fromJson(data, String[][].class);
			}

			if (oper != null && oper.length() > 0 && oper.equals("excel")) {
				title = title.replaceAll("\\s+", "");
				String filename = title + "_" + PoorManUtil.getNow() + ".xls";

				String regex = "[`~!@#$%^&*()\\+\\=\\{}|:\"?><【】\\/r\\/n]";
				Pattern pa = Pattern.compile(regex);
				Matcher ma = pa.matcher(filename);
				if (ma.find()) {
					filename = ma.replaceAll("");
				}

				String ua = request.getHeader("User-Agent").toLowerCase();

				boolean isChrome = (ua != null && ua.indexOf("chrome/") != -1 && ua.indexOf("edge/") == -1);

				if (isChrome)
					filename = MimeUtility.encodeWord(filename, "utf-8", "Q"); // chrome
				else
					filename = URLEncoder.encode(filename, "utf-8");

				//
				// response.setContentType("application/excel;charset=UTF-8");
				response.setContentType("application/msexcel;charset=utf-8");
				response.setCharacterEncoding("utf-8");

				response.setHeader("Content-disposition", "attachment;filename=" + filename);

				// response.setHeader("Content-disposition",
				// "filename=" + filename);
				response.setHeader("Cache-Control", "private");
				response.setHeader("Connection", "close");
				excelUtil.export(outputStream, cols);
			}
			outputStream.close();
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
	}

	// private String encodeFileName(HttpServletRequest request, String fileName)
	// throws UnsupportedEncodingException {
	// String userAgent = request.getHeader("user-agent");
	// String encodedFileName;
	// // IE
	// if (StringUtils.contains(userAgent, "MSIE")) {
	// encodedFileName = URLEncoder.encode(fileName, "utf-8");
	// }
	// else { // chrome
	// encodedFileName = MimeUtility.encodeWord(fileName, "utf-8", "Q");
	// }
	// return encodedFileName;
	// }
}
