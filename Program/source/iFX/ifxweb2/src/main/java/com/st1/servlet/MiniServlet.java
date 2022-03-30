package com.st1.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.service.CodeListService;
import com.st1.util.MySpring;
import com.st1.util.PoorManFile;

/**
 * Servlet implementation class MiniServlet
 */
// @WebServlet("/MiniServlet")
public class MiniServlet extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(MiniServlet.class);

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MiniServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Locale.setDefault(Locale.TRADITIONAL_CHINESE);

		String url = request.getRequestURL().toString();
		url = url.toLowerCase();
		logger.info("mini url:" + FilterUtils.escape(url));
		String content = "";
		if (url.toUpperCase(Locale.TAIWAN).endsWith("HELP.JS")) {
			logger.info("in help.js");
			if (GlobalValues.helpCacheMinutes > 0) {
				logger.info("help.js will expired in " + GlobalValues.helpCacheMinutes + " minutes");
				Calendar inOneMonth = Calendar.getInstance();
				inOneMonth.add(Calendar.MINUTE, GlobalValues.helpCacheMinutes);
				// Expires 刷新(強制讀取)無效,只有在不刷新重新讀取時有效
				// 測試ok
				response.setDateHeader("Expires", inOneMonth.getTimeInMillis());
			} else {
				logger.info("help.js no-cahce");
			}
			// content = getJsByPath(url);
			content = buildHelp();
		} else {
			Calendar inOneMonth = Calendar.getInstance();
			inOneMonth.add(Calendar.YEAR, 1); // 12小時改成1年,經測試更改畫面js檔,快取會變(依據中間13位時間)
			response.setDateHeader("Expires", inOneMonth.getTimeInMillis());

			content = getJBP(url);
		}

		// explain是 html網頁,故調整此段
		if (url.indexOf("mini/explain/") > 0) {
			response.setContentType("text/html;charset=UTF-8");
		} else {
			response.setContentType("text/javascript");
		}

		response.getWriter().write(content);
		response.flushBuffer();
		// response.getWriter().write("function test() { alert('peek-a-boo');}
		// test();");
	}

	private String buildHelp() {
		CodeListService service = MySpring.getCodeListService();
		return service.buildJS();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	static Map<String, String> jsMap = new HashMap<String, String>();

	public static void add(String key, String js) {
		jsMap.put(key, js);
	}

	private String getJBP(String u) {

		int pos;
		logger.info(FilterUtils.escape("in getJsByPath:" + u));
		if ((pos = u.indexOf("mini/tran/")) > 0) {
			String txcode = u.substring(pos + "mini/tran/".length());
			txcode = txcode.substring(0, txcode.indexOf("."));
			txcode = txcode.toUpperCase(Locale.TAIWAN);
			logger.debug("mini txcode:" + txcode);
			// logger.info("mini txcode"+txcode);
			String filepath = GlobalValues.getTranFilePath(txcode);
			String x = "";
			String pfns = "";
			try {
				PoorManFile poor = new PoorManFile(filepath);
				x = poor.read();
				pfns = GlobalValues.getTranPfns(txcode);
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.warn(errors.toString());
			}

			return x + "\n\n" + pfns;
		} else if ((pos = u.indexOf("mini/explain/")) > 0) {
			// 各交易說明訊息
			String exname = u.substring(pos + "mini/explain/".length());
			logger.debug("mini exname:" + exname);
			// 檔案路徑為 : ifxfolder\runtime\explain
			String filepath = GlobalValues.getExplainFilePath(exname);
			String x = "";
			PoorManFile poor = null;

			// 預設 ERROR.html為有問題時的網頁
			File filef = new File(FilterUtils.filter(filepath));
			if (!filef.exists()) {
				filepath = GlobalValues.getExplainFilePath("ERROR");
			}

			try {
				poor = new PoorManFile(filepath);
				x = poor.read();
				logger.info("x len:" + x.length());
			} catch (Exception e) {
				logger.error("mini exname:" + e.getMessage());
			}

			return x;
		} else if (u.endsWith("help.js")) {
			logger.debug("return help.js");
			return jsMap.get("help");
		} else if (u.endsWith("errors.js")) {
			logger.debug("return errors.js");
			return jsMap.get("errors");
		} else {
			logger.info(FilterUtils.escape("unknown key:" + u));
			return "function test() { alert('peek-a-boo');} test();";
		}
	}

	public static long getVersion(String txcode) {
		String filepath = GlobalValues.getTranFilePath(txcode);
		long lastModified = (new File(FilterUtils.filter(filepath))).lastModified();
		return lastModified;
	}
}
