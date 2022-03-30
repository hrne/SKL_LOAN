package com.st1.ifx.web;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.st1.bean.FileReposBean;
import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.menu.TranItem;
import com.st1.ifx.menu.TranListBuilder;
import com.st1.msw.UserInfo;
import com.st1.msw.UserPub;
import com.st1.servlet.GlobalValues;
import com.st1.servlet.MiniServlet;
import com.st1.util.MySpring;

@Controller
@RequestMapping("tran/*")
public class TranController {
	private static final Logger logger = LoggerFactory.getLogger(TranController.class);

	@Autowired
	private FileReposBean fileReposBean;

	// http://localhost:8080/ifxweb2/mvc/tran/home
	@RequestMapping(value = "home", method = RequestMethod.GET)
	public String home() {
		logger.debug("go home");
		return "home";
	}

	// http://localhost:8080/ifxweb2/mvc/tran/swiftCode
	@RequestMapping(value = "swiftCode", method = RequestMethod.GET)
	public String swiftCode() {
		logger.debug("go swiftCode");
		return "swiftCode";
	}

	// http://localhost:8080/ifxweb2/mvc/tran/run/txcode=G0220&title=hello
	@RequestMapping(value = "run", method = RequestMethod.GET)
	public String run(Model uiModel, @RequestParam String txcode, @RequestParam(value = "id", required = false) String id, @RequestParam(required = false, defaultValue = "0") String hot,
			@RequestParam(required = false) String key, @RequestParam(required = false) String resend, @RequestParam(required = false) String chain, @RequestParam(required = false) String ovrScrFile,
			@RequestParam(value = "m", required = false) String mode, @RequestParam(value = "rim", required = false) String rim, @RequestParam(value = "fkey", required = false) String fkey,
			HttpSession session, HttpServletRequest request, HttpServletResponse response) {

		String t2 = "n/a";
		String sysvar = (String) session.getAttribute(GlobalValues.SESSION_SYSVAR);
		if (sysvar == null) {
			logger.warn("not login, please login");
			return "redirect:/index.jsp";
		}

		UserInfo userInfo = (UserInfo) session.getAttribute(GlobalValues.SESSION_USER_INFO);

		String brno = userInfo.getBrno();
		String tlrno = userInfo.getId();
		// 柯 增加這段for 不同台但被強制登出時,應該也自行登出
		UserPub pub = UserPub.getInstance();
		if (pub.getBySessionId(session.getId()) == null) {
			logger.info("no session in db! please login again");
			session.setAttribute(GlobalValues.SESSION_SYSVAR, null);
			return "redirect:/index.jsp";
		}

		try {
			txcode = txcode.toUpperCase();
			TranReq txReq = new TranReq(txcode, id, key, chain, ovrScrFile, mode);
			logger.debug("tx req:" + txReq);

			chain = chain == null ? "0" : chain;
			Map<String, String> changeMap = new HashMap<String, String>();
			changeMap.put("CHAIN", chain);
			changeMap.put("__tid", id);
			changeMap.put("__hot", hot);
			boolean tmpExists = false;
			String tmpTranFile = "";
			// no more temp tran
//			if (chain.equals("0")) {
			// String tmpTranFile = (String) session
			// .getAttribute(GlobalValues.SESSION_USER_SCRFILE_PREFIX)
			// + txcode + ".txt";
//			tmpTranFile = "scr/" + brno + tlrno + "/" + txcode + ".txt";
			tmpTranFile = "scr/" + brno + tlrno + "/";
			tmpExists = fileReposBean.exists(tmpTranFile, txcode);
			if (!tmpExists)
				tmpTranFile = "";
//			}

			txReq.setJsFileUrl(buildJsFileUrl(txcode));

			Gson gson = new Gson();
			// @SuppressWarnings("unchecked")
			// Map<String, TranItem> tranMap = (Map<String, TranItem>) session
			// .getAttribute(GlobalValues.SESSION_USER_TRANMAP);
			TranListBuilder builder = MySpring.getTranListBuilder();
			Map<String, TranItem> tranMap = builder.getTranMap();

			TranItem tranItem = null;
			if (ovrScrFile == null) { // screen copy mode 不用檢查權限
				// X9901更改交易代號後應該沒有用處
				List<String> specialTrans = Arrays.asList(new String[] { "X9901" });
				boolean dontCheck = true; // 暫時都不檢查權限
				tranItem = tranMap.get(txcode);
				if (tranItem == null) {
					if (specialTrans.contains(txcode) || dontCheck) {
						tranItem = new TranItem(txcode);
					} else {
						logger.warn("txcpde:" + FilterUtils.escape(txcode) + " is not authorized, or no such txcode");
						throw new RuntimeException(String.format("無權交易, 或交易代號:%s不存在", txcode));
					}
				}
				tranItem.merge(changeMap);
			}
			// //var _basePath = '${pageContext.request.contextPath}';
			StringBuilder jsBlk1 = new StringBuilder();
			// start 增加該var的修正日期
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long varversion = MiniServlet.getVersion(txcode);
			String vardate = sdf.format(new Date(varversion));
			appendVar(jsBlk1, "_varVersion", vardate);
			// end
			appendVar(jsBlk1, "_txcd", txcode);
			appendJson(jsBlk1, "_sysvar", sysvar);
			appendJson(jsBlk1, "_changeSysVar", changeMap != null ? gson.toJson(changeMap) : "");
			// appendJson(jsBlk1, "_tranAuth", tranItem != null ? gson.toJson(tranItem) :
			// "");
			appendJson(jsBlk1, "_tranAuth", gson.toJson(tranItem));
			appendVar(jsBlk1, "_chain", chain);

			// tmpExists = false;
			appendJson(jsBlk1, "_tmpfileExists", tmpExists ? "true" : "false");
			appendVar(jsBlk1, "_tmpfile", tmpTranFile);

			appendVar(jsBlk1, "_ovrScrFile", ovrScrFile == null ? "" : ovrScrFile);
			appendVar(jsBlk1, "_key", key == null ? "" : key);
			appendVar(jsBlk1, "_resend", resend == null ? "" : resend); // 柯 新增 for
			// viewjournal
			appendVar(jsBlk1, "_mode", mode == null ? "" : mode);
			appendVar(jsBlk1, "_rim", rim == null ? "" : rim);
			appendVar(jsBlk1, "_fkey", fkey == null ? "" : fkey);
			// appendJson(jsBlk1, "_tranEnv", GlobalValues.tranEnvJson);

			appendVar(jsBlk1, "_server_os", GlobalValues.bR6 ? "1" : "0");
			// 給前端使用的port
			appendVar(jsBlk1, "_mqserverport", GlobalValues.mqServerPort);

			// logger.debug("js:"+jsBlock1.toString());
			txReq.setJsBlock1(jsBlk1.toString());
			uiModel.addAttribute("txReq", txReq);

			return "tran";
		} catch (Exception ex) {
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			request.setAttribute("txcode", txcode);
			request.setAttribute("msg", "Tranontroller Fail 184..");
			return "forward:/error-notran.jsp";

		}

	}

	private String buildJsFileUrl(String txcode) {
		long version = MiniServlet.getVersion(txcode);
		String strVersion = String.format("?%d", version);
		String jsFile = String.format("mini/tran/%s.js%s", txcode, strVersion);
		logger.info("txcode:" + txcode + ", version:" + strVersion + "(date:" + new Date(version).toString() + ")");
		logger.info("jsFile:" + jsFile);
		return jsFile;
	}

	private void appendVar(StringBuilder sb, String name, String value) {
		sb.append(String.format("var %s='%s';\n", name, value));
	}

	private void appendJson(StringBuilder sb, String name, String value) {
		sb.append(String.format("var %s=%s;\n", name, value));
	}
}
