package com.st1.ifx.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.pdf.codec.Base64;
import com.st1.bean.FileReposBean;
import com.st1.ifx.domain.CodeList;
import com.st1.ifx.domain.Journal;
import com.st1.ifx.domain.LocalRim;
import com.st1.ifx.domain.MsgBox;
import com.st1.ifx.domain.Ovr;
import com.st1.ifx.domain.OvrScreen;
import com.st1.ifx.domain.Ticker;
import com.st1.ifx.domain.TranDoc;
import com.st1.ifx.domain.TranDocBuf;
import com.st1.ifx.domain.TranDocLog;
import com.st1.ifx.etc.Pair;
import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.filter.SafeClose;
import com.st1.ifx.filter.Validator;
import com.st1.ifx.hcomm.app.SessionMap;
import com.st1.ifx.menu.TranItem;
import com.st1.ifx.menu.TranListBuilder;
import com.st1.ifx.service.CodeListService;
import com.st1.ifx.service.HelpListService;
import com.st1.ifx.service.JournalService;
import com.st1.ifx.service.LocalRimService;
import com.st1.ifx.service.MsgService;
import com.st1.ifx.service.OvrService;
import com.st1.ifx.service.TickerService;
import com.st1.ifx.service.TranDocLogService;
import com.st1.ifx.service.TranDocService;
import com.st1.ifx.service.TxcdService;
import com.st1.ifx.service.UserPubService;
import com.st1.msw.MswCenter;
import com.st1.msw.UserInfo;
import com.st1.servlet.GlobalValues;
import com.st1.util.FolderZiper;
import com.st1.util.MySpring;
import com.st1.util.PoorManFile;

@Controller
@RequestMapping("hnd/*")
public class MyAjaxController extends MyAjaxBase {
	static final Logger logger = LoggerFactory.getLogger(MyAjaxController.class);

	@Autowired
	private HttpServletRequest request;

	@Autowired
	HttpServletResponse response;

	@Autowired
	private FileReposBean fileReposBean;

	// http://localhost:8080/ifxweb2/mvc/hnd/helpList/GXDEF/IRTDEF/IRTKD/IRTKDX
	@RequestMapping(value = "helpList/{help}/{segment}/{value}/{label}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, List<Pair<String, String>>> getHelp(@PathVariable String help, @PathVariable String segment, @PathVariable String value, @PathVariable String label) {
		logger.info(FilterUtils.escape("getting help list...., help:" + help + ", segment:" + segment));
		logger.info(FilterUtils.escape("value:" + value + ", label:" + label));

		StopWatch watch = new StopWatch();
		watch.start();

		HelpListService helpListService = MySpring.getHelpListService();
		List<Pair<String, String>> pairs = helpListService.findList(help, segment, value, label);
		Map<String, List<Pair<String, String>>> map = new HashMap<String, List<Pair<String, String>>>();
		map.put("result", pairs);

		watch.stop();
		logger.info("Total execution time to get help list in millis: " + watch.getTotalTimeMillis());

		logger.debug(new Gson().toJson(map));
		return map;
	}

	// http://localhost:8080/ifxweb/mvc/hnd/helpList/AXDEF/CURDEF16?index=2
	@RequestMapping(value = "helpList/{help}/{segment}", method = RequestMethod.GET)
	@ResponseBody
	public String[] getValues(@PathVariable String help, @PathVariable String segment, @RequestParam int index) {
		logger.info(FilterUtils.escape("getting help list...., help:" + help + ", segment:" + segment));
		logger.info(FilterUtils.escape("index:" + index));

		StopWatch watch = new StopWatch();
		watch.start();
		HelpListService helpListService = MySpring.getHelpListService();
		String[] result = helpListService.findValues(help, segment, index);
		watch.stop();
		logger.info("Total execution time to getValues in millis: " + watch.getTotalTimeMillis());
		logger.debug(new Gson().toJson(result));
		return result;
	}

	// http://localhost:8080/ifxweb2/mvc/hnd/menu/html?key=yyyymmdd
	@RequestMapping(value = "menu/html", method = RequestMethod.GET)
	public ResponseEntity<String> getMenuHtml(@RequestParam String key, HttpSession session, HttpServletResponse response) {
		// response.setHeader("Cache-Control", "no-cache");
		logger.info("getting menu html");
		// Pair<String, Map<String, TranItem>> pair = getUserMenuInfo(session);
		// String output = pair.first;
		// out.println("\n\nvar _tranMap = " + output + ";\n\n");

		TranListBuilder builder = MySpring.getTranListBuilder();
		String output = builder.getMenuHtml();

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");

		// disable cache, if cache is enabled, browser will not get menu html at
		// startup,
		// Calendar inOneMonth = Calendar.getInstance();
		// inOneMonth.add(Calendar.HOUR, 12);
		// response.setDateHeader("Expires", inOneMonth.getTimeInMillis());

		logger.info("Menu Html");
		logger.info(output);

		return new ResponseEntity<String>(output, responseHeaders, HttpStatus.CREATED);

	}

	// http://localhost:8080/ifxweb2/mvc/hnd/menu/json?key=yyyymmdd
	@RequestMapping(value = "menu/json", method = RequestMethod.GET)
	public ResponseEntity<String> getMenuJson(@RequestParam String key, HttpSession session, HttpServletResponse response) {
		logger.info("getting menu json...");
		// Map<String, TranItem> tranMap = (Map<String, TranItem>) session
		// .getAttribute(GlobalValues.SESSION_USER_TRANMAP);
		// if (tranMap == null) {
		// logger.info("get user menu for json");
		// getUserMenuInfo(session);
		// tranMap = (Map<String, TranItem>) session
		// .getAttribute(GlobalValues.SESSION_USER_TRANMAP);
		// }
		// String output = new Gson().toJson(tranMap);
		//
		TranListBuilder builder = MySpring.getTranListBuilder();
		String output = builder.getMenuJson();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=utf-8");

		// Calendar inOneMonth = Calendar.getInstance();
		// inOneMonth.add(Calendar.HOUR, 12);
		// response.setDateHeader("Expires", inOneMonth.getTimeInMillis());

		logger.info("return json:\n" + output);
		return new ResponseEntity<String>(output, responseHeaders, HttpStatus.CREATED);
	}

	@SuppressWarnings({ "unused" })
	private Pair<String, Map<String, TranItem>> getUserMenuInfo(HttpSession session) {
		StopWatch watch = new StopWatch();
		watch.start();

		UserInfo userInfo = (UserInfo) session.getAttribute(GlobalValues.SESSION_USER_INFO);
		logger.info("user:" + userInfo.getId() + " getting global menu html");
		// String attachText = (String) session
		// .getAttribute(GlobalValues.SESSION_USER_ATTACH);
		// String c12Text = (String) session
		// .getAttribute(GlobalValues.SESSION_USER_C12);

		// SessionMap sessionMap = (SessionMap) session
		// .getAttribute(GlobalValues.SESSION_SYSVAR_MAP);
		// HashMap map = new HashMap();
		// map.put("TXGRP", sessionMap.get("TXGRP"));
		// map.put("LEVEL", sessionMap.get("LEVEL"));
		// map.put("FXLVL", sessionMap.get("FXLVL"));

		// TranListBuilder builder = MySpring.getTranListBuilder();
		// Pair<String, Map<String, TranItem>> pair = builder.buildOnce();
		// Pair<String, Map<String, TranItem>> pair = builder.build(
		// userInfo.getBrno(), userInfo.getId(), map, null, null);

		// session.setAttribute(GlobalValues.SESSION_USER_TRANMAP, pair.second);

		watch.stop();
		logger.info("user:" + userInfo.getId() + " takes " + watch.getTotalTimeMillis() + " for global menu html");
		return null;
	}

	// http://localhost:8080/ifxweb2/mvc/hnd/menu2/menu
	// http://localhost:8080/ifxweb2/mvc/hnd/menu2/menu=A
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "menu2/jsonp", method = RequestMethod.GET)
	public ResponseEntity<String> getMenuJson2(@RequestParam String menu, @RequestParam(required = false) String callback, HttpSession session, HttpServletResponse response) {

		logger.info("getMenuJson2!!");
		String attachText = (String) session.getAttribute(GlobalValues.SESSION_USER_ATTACH);
		String c12Text = (String) session.getAttribute(GlobalValues.SESSION_USER_C12);
		SessionMap sessionMap = (SessionMap) session.getAttribute(GlobalValues.SESSION_SYSVAR_MAP);
		UserInfo userInfo = (UserInfo) session.getAttribute(GlobalValues.SESSION_USER_INFO);

		HttpHeaders responseHeaders = new HttpHeaders();
		if (userInfo == null) {
			responseHeaders.add("Content-Type", "application/json; charset=utf-8");
			logger.warn("getMenuJson2 must login");
			return new ResponseEntity<String>("", responseHeaders, HttpStatus.CREATED);
			// throw new RuntimeException("must login");
		}
		HashMap<String, String> c12Map = new HashMap<String, String>();
		c12Map.put("TXGRP", sessionMap.get("TXGRP"));
		c12Map.put("LEVEL", sessionMap.get("LEVEL"));
		c12Map.put("FXLVL", sessionMap.get("FXLVL"));
		c12Map.put("DAPKND", sessionMap.get("DAPKND"));
		c12Map.put("OAPKND", sessionMap.get("OAPKND"));

		String brno = userInfo.getBrno();
		String tlrno = userInfo.getId();

		// String brno="5067";
		// String tlrno="29";
		// String attachText = null;
		// String c12Text = null;
		// HashMap c12Map = new HashMap();
		// c12Map.put("TXGRP","00");
		// c12Map.put("LEVEL","3");
		// c12Map.put("FXLVL","1");

		TranListBuilder builder = MySpring.getTranListBuilder();
		List<String> resultList = builder.buildMenu2(menu, brno, tlrno, c12Map, attachText, c12Text);
		String output = new Gson().toJson(resultList);

		if (callback != null && callback.length() > 0) {
			responseHeaders.add("Content-Type", "application/javascript; charset=utf-8");
			output = "callback(" + output + ")";
		} else {
			responseHeaders.add("Content-Type", "application/json; charset=utf-8");
		}
		// Calendar inOneMonth = Calendar.getInstance();
		// inOneMonth.add(Calendar.HOUR, 12);
		// response.setDateHeader("Expires", inOneMonth.getTimeInMillis());

		logger.info("return json:\n" + output);
		return new ResponseEntity<String>(output, responseHeaders, HttpStatus.CREATED);
	}

	// http://localhost:8080/ifxweb2/mvc/hnd/txcd/term=
	@RequestMapping(value = "txcd/jsonp", method = RequestMethod.GET)
	public ResponseEntity<String> getTxcdList(@RequestParam String term, @RequestParam(required = false) String callback, HttpSession session, HttpServletResponse response) {
		TxcdService txcdService = MySpring.getBean("txcdService", TxcdService.class);
		logger.info("getting txcd list:" + FilterUtils.escape(term));
		StopWatch watch = new StopWatch();
		watch.start();
		SessionMap sessionMap = (SessionMap) session.getAttribute(GlobalValues.SESSION_SYSVAR_MAP);
		List<String> resultList = new ArrayList<String>();
		// 前端term不能小於兩位
		if (checkLoginodbu(sessionMap, term)) {
			List<String> list = txcdService.findTxcdterm(2, term);
			resultList = list;
			logger.info("result ok!");
		} else {
			logger.info("checkLoginodbu false!");
		}

		String output = new Gson().toJson(resultList);

		HttpHeaders responseHeaders = new HttpHeaders();
		if (callback != null && callback.length() > 0) {
			responseHeaders.add("Content-Type", "application/javascript; charset=utf-8");
			output = "callback(" + output + ")";
		} else {
			responseHeaders.add("Content-Type", "application/json; charset=utf-8");
		}
		// Calendar inOneMonth = Calendar.getInstance();
		// inOneMonth.add(Calendar.HOUR, 12);
		// response.setDateHeader("Expires", inOneMonth.getTimeInMillis());

		logger.info(FilterUtils.escape("return json:\n" + output));
		watch.stop();
		logger.info("Total execution time to get txcd list(getTxcdList) in millis: " + watch.getTotalTimeMillis());
		return new ResponseEntity<String>(output, responseHeaders, HttpStatus.CREATED);
	}

	// http://localhost:8080/ifxweb2/mvc/hnd/swift/mt/mt110
	@RequestMapping(value = "swift/mt/{name}", method = RequestMethod.GET)
	public ResponseEntity<String> getSwiftMT(@PathVariable String name, HttpSession session, HttpServletResponse response) {
		// response.setHeader("Cache-Control", "no-cache");
		logger.info("getting swift mt " + FilterUtils.escape(name));
		String filePath = GlobalValues.swiftFolder + name + ".txt";
		logger.info("mt path:" + FilterUtils.escape(filePath));
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
		String x = "";
		try {
			PoorManFile poor = new PoorManFile(filePath);
			x = poor.read();

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			x = "無法下載SWIFT電文格式檔:" + name;
		}
		logger.info(FilterUtils.escape(x));
		return new ResponseEntity<String>(x, responseHeaders, HttpStatus.CREATED);
	}

	// http://localhost:8080/ifxweb2/mvc/hnd/codeList/all/FCDEF/CURDEF
	@RequestMapping(value = "codeList/all/{help}/{segment}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> getAllKeys(@PathVariable String help, @PathVariable String segment) {
		logger.info(FilterUtils.escape("getting all help:" + help + ", segment:" + segment));

		StopWatch watch = new StopWatch();
		watch.start();
		CodeListService codeListService = MySpring.getCodeListService();
		List<CodeList> tmp = codeListService.findBySegment(help, segment);
		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		// 更改成 回覆 key 和 value
		for (CodeList cc : tmp) {
			HashMap<String, String> cm = new HashMap<String, String>();
			cm.put("key", cc.getXey());
			cm.put("content", cc.getContent());
			result.add(cm);
		}
		watch.stop();
		logger.info("Total execution time to getValues in millis: " + watch.getTotalTimeMillis());

		logger.debug(new Gson().toJson(result));

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(new Gson().toJson(result), responseHeaders, HttpStatus.CREATED);
	}

	// http://localhost:8080/ifxweb2/mvc/hnd/codeList/FCDEF/CURDEF?key=TWD
	@RequestMapping(value = "codeList/{help}/{segment}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> getCodeByKey(@PathVariable String help, @PathVariable String segment, @RequestParam String key) {
		logger.info("getting content:" + FilterUtils.escape(help) + ", segment:" + FilterUtils.escape(segment) + ", key:" + FilterUtils.escape(key));

		StopWatch watch = new StopWatch();
		watch.start();
		CodeListService codeListService = MySpring.getCodeListService();
		CodeList tmp = codeListService.findByKey(help, segment, key);
		// HashMap<String, String> result = new HashMap<String, String>();
		watch.stop();
		logger.info("Total execution time to getValues in millis: " + watch.getTotalTimeMillis());

		logger.info("tmp:" + tmp.getContent());

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(tmp.getContent(), responseHeaders, HttpStatus.CREATED);
	}

	// http://localhost:8080/ifxweb2/mvc/hnd/rim/BYE/CC
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "rim/{tableName}/{key}", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> getLocalRim(@PathVariable String tableName, @PathVariable String key) {
		logger.info("getting local rim for tableName:" + FilterUtils.escape(tableName) + ", key:" + FilterUtils.escape(key));

		StopWatch watch = new StopWatch();
		watch.start();
		boolean retval = false;
		String message;
		HashMap map = new HashMap();
		LocalRimService localRimService = MySpring.getLocalRimService();
		LocalRim result = localRimService.find(tableName, key);

		if (result != null) {
			message = result.getData();
			retval = true;
		} else {
			message = String.format(TTError.TT001, tableName, key);
		}
		map.put("retval", retval);
		map.put("host", "1"); // web server
		map.put("message", message);

		watch.stop();
		logger.info("Total execution time to getValues in millis: " + watch.getTotalTimeMillis());

		return makeJsonResponse(map);
	}

	// http://localhost:8080/ifxweb2/mvc/hnd/rim/BYE/CC
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "rimNEW/{tableName}/{key}", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> getLocalRimNOCACHE(@PathVariable String tableName, @PathVariable String key) {
		logger.info(FilterUtils.escape("getting local rim for tableName:" + tableName + ", key:" + key));

		StopWatch watch = new StopWatch();
		// logger.info("after watch");
		watch.start();
		// logger.info("after watch.start()");
		boolean retval = false;
		String message;
		HashMap map = new HashMap();
		// logger.info("after HashMap");
		LocalRimService localRimService = MySpring.getLocalRimService();
		// logger.info("after localRimService");
		LocalRim result = localRimService.findnocache(tableName, key);
		// logger.info("after getLocalRimNOCACHE - findnocache");

		if (result != null) {
			message = result.getData();
			retval = true;
		} else {
			message = String.format(TTError.TT001, tableName, key);
		}
		map.put("retval", retval);
		map.put("host", "1"); // web server
		map.put("message", message);

		watch.stop();
		logger.info("Total execution time to getValues in millis: " + watch.getTotalTimeMillis());

		return makeJsonResponse(map);
	}

	// http://localhost:8080/ifxweb2/mvc/hnd/rim/BYE/CC
	// http://localhost:8080/ifxweb2/mvc/hnd/rim/SWIFT?m=TAIPEI
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "rim/{tableName}", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> matchLocalRim(@PathVariable String tableName, @RequestParam String m) {
		logger.info(FilterUtils.escape("matching for tableName:" + tableName + ", m:" + m));

		StopWatch watch = new StopWatch();
		watch.start();
		boolean retval = false;
		HashMap map = new HashMap();
		LocalRimService localRimService = MySpring.getLocalRimService();
		List<LocalRim> result = null;
		if (m.equals("*")) {
			result = localRimService.findAll(tableName);
		} else {
			result = localRimService.findLike2(tableName, m);
		}
		retval = true;
		map.put("retval", retval);
		map.put("result", result);
		map.put("records", result == null ? 0 : result.size());
		watch.stop();
		logger.info("Total execution time to getValues in millis: " + watch.getTotalTimeMillis());

		return makeJsonResponse(map);
	}

	// http://localhost:8080/ifxweb2/mvc/hnd/jnl/resv/20131029/5050/20/00001693
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "jnl/resv/{busdate}/{brn}/{tlrno}/{txno}", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> getResv(@PathVariable String busdate, @PathVariable String brn, @PathVariable String tlrno, @PathVariable String txno) {
		logger.info("getting RESV for {}/{}/{}/{}", FilterUtils.escape(busdate), FilterUtils.escape(brn), FilterUtils.escape(tlrno), FilterUtils.escape(txno));

		StopWatch watch = new StopWatch();
		watch.start();

		JournalService service = MySpring.getJournalService();
		List<Journal> jnlList = service.findByF4(busdate, brn, tlrno, txno);
		watch.stop();
		logger.info("Total execution time to getValues in millis: " + watch.getTotalTimeMillis());
		Journal jnl = null;
		if (jnlList != null && jnlList.size() > 0) {
			jnl = jnlList.get(0);
		}
		HashMap map = new HashMap();

		if (jnl != null) {
			if (jnlList.size() > 1) {
				logger.info(FilterUtils.escape("j_Msgid():" + jnl.getMsgid() + ",j_Txcode():" + jnl.getTxcode()));
				logger.error("jnlList.size:" + FilterUtils.escape(jnlList.size()) + ",TODO!! something wrong resv:" + FilterUtils.escape(String.format("%s/%s/%s/%s", busdate, brn, tlrno, txno)));
			}

			map.put("retval", true);
			map.put("text", jnl.getTitaResv());
		} else {
			map.put("retval", false);
			map.put("text", String.format("無法取得此筆資料:%s/%s/%s/%s", busdate, brn, tlrno, txno));
		}

		return makeJsonResponse(map);
	}

	// http://localhost:8080/ifxweb2/mvc/hnd/jnl/tita/21
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "jnl/tita/{id}", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> getTitaAndResv(@PathVariable String id) {
		logger.info("getting Journal Tita for {}", FilterUtils.escape(id));

		id = id != null ? id.trim() : "";
		String busDate = id.length() >= 8 ? id.substring(0, 8) : "";
		String brn = id.length() >= 14 ? id.substring(8, 12) : "";
		String tlrNo = id.length() >= 18 ? id.substring(12, 18) : "";
		String txNo = id.length() >= 26 ? id.substring(18) : "";

		logger.info("busDate :[" + busDate + "], brn :[" + brn + "], tlrNo :[" + tlrNo + "], txNo :[" + txNo + "]");

		StopWatch watch = new StopWatch();
		watch.start();
		JournalService service = MySpring.getJournalService();
//		Journal jnl = service.get(Long.parseLong(id));

		List<Journal> jnlLi = service.findByF4(busDate, brn, tlrNo, txNo);
		Journal jnl = jnlLi == null || jnlLi.isEmpty() ? null : jnlLi.get(0);
		logger.info(jnl != null && jnlLi.size() > 1 ? "jnlLi > 1" : "Normal One");

		watch.stop();
		logger.info("Total execution time to getValues in millis: " + watch.getTotalTimeMillis());

		HashMap map = new HashMap();
		if (jnl != null) {
			map.put("success", true);
			map.put("brn", jnl.getBrn());
			map.put("tlrno", jnl.getTlrno());
			map.put("txno", jnl.getTxno());
			map.put("mrkey", jnl.getMrkey());
			map.put("busdate", jnl.getBusdate());
			map.put("tita", jnl.getTita());
			map.put("resv", jnl.getTitaResv());
			map.put("jnlId", jnl.getId());
		} else {
			map.put("success", false);
			map.put("errmsg", String.format("無法取得此筆資料:%s", id));
		}

		return makeJsonResponse(map);
	}

	// http://localhost:8080/ifxweb2/mvc/hnd/war/file/...
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "web/file", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> getFileReport(@RequestParam String _d) {
		logger.info("FileReport scan, _d:{}", FilterUtils.escape(_d));
		Gson gson = new Gson();
		HashMap<String, String> m = new HashMap<String, String>();
		m = (HashMap<String, String>) gson.fromJson(_d, m.getClass());
		String filename = m.get("filename").trim();
		String root = m.get("root").trim();
		logger.info("get filename: " + FilterUtils.escape(filename));
		String strNum = "";
		FileReader fr = null;
		BufferedReader br = null;
		HashMap map = new HashMap();
		map.put("success", false);

		map.put("form", "HOSTFILE");
		map.put("prompt", "A4");
		map.put("content", "");
		if (root.equals("ifx")) {
			root = GlobalValues.ifxFolder;
		} else if (root.equals("ifw")) {
			root = GlobalValues.ifxWriter;
		}
		logger.info(FilterUtils.escape("get filename path: " + root + "/" + filename));
		map.put("errmsg", "讀取錯誤:" + root + "/" + filename);

		try { // 之後要換FTP專屬路徑
			fr = new FileReader(FilterUtils.filter(root + "/" + filename));
			br = new BufferedReader(fr); // 在finally
			StringBuffer sb = new StringBuffer();
			boolean nofirst = false;
			while ((strNum = br.readLine()) != null) {
				if (nofirst) {
					sb.append('\n');
				} else {
					nofirst = true;
				}
				sb.append(strNum);
			}
			map.put("success", true);
			map.put("errmsg", "");
			map.put("content", sb.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.info("FileNotFoundException:" + e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("IOException:" + e);
		} finally {
			SafeClose.close(br);
			SafeClose.close(fr);
			logger.info("--- File End ---");
		}

		return makeJsonResponse(map);
	}

	// SINOSTEEL 補送中鋼網銀表單
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "web/csteellist", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> csteelxmlFilelist(@RequestParam String _d) {
		logger.info("csteelxml list scan, _d:{}", FilterUtils.escape(_d));
		Gson gson = new Gson();
		HashMap<String, String> m = new HashMap<String, String>();
		m = (HashMap<String, String>) gson.fromJson(_d, m.getClass());
		String namelike = m.get("namelike").trim();
		logger.info(FilterUtils.escape("namelike:" + namelike));
		StopWatch watch = new StopWatch();
		logger.info("Total execution time to getValues in millis: " + watch.getTotalTimeMillis());
		HashMap map = new HashMap();

		watch.start();
		map.put("success", true);
		map.put("form", "CSTEELLIST");
		List<File> files = GlobalValues.getcsteelxmlFiles();
		map.put("list", convertToFilesListResponse(files, namelike));

		watch.stop();

		return makeJsonResponse(map);

	}

	// 下載中鋼xml檔案
	// 傳遞的變數有點(,)有斜線(/)都無法傳遞,故在此補齊路徑
	@RequestMapping(value = "web/csteeldownload/{filename}/{ext}")
	public void getCsteelxmlFile(@PathVariable String filename, @PathVariable String ext, HttpSession session, HttpServletResponse response) throws Exception {
		filename = FilenameUtils.removeExtension(filename);
		InputStream inputStream = null;
		try {
			String root = GlobalValues.csteelxmlFolder;
			File fileToDownload = new File(FilterUtils.filter(root + File.separator + filename + "." + ext));

			logger.info("get Csteel xml filepath:" + fileToDownload.getPath());
			logger.info("filename:" + filename);
			inputStream = new FileInputStream(fileToDownload);
			response.setContentType("application/force-download");
			// if (ext != null && ext.trim().length() > 0) {
			// filename = filename + "." + ext.trim();
			// }

			response.setHeader("Content-Disposition", "attachment; filename=" + filename + "." + ext);
			IOUtils.copy(inputStream, response.getOutputStream());
			response.flushBuffer();
		} catch (Exception e) {
			logger.error("getYangmeiFile error:" + e.getMessage());
			// e.printStackTrace();
		} finally {
			SafeClose.close(inputStream);
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "web/yangmeilist", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> yangmeiFilelist(@RequestParam String _d) {
		logger.info("Yangmei list scan, _d:{}", FilterUtils.escape(_d));
		Gson gson = new Gson();
		HashMap<String, String> m = new HashMap<String, String>();
		m = (HashMap<String, String>) gson.fromJson(_d, m.getClass());
		String day = m.get("day").trim();
		String namelike = m.get("namelike").trim();

		logger.info("day:" + FilterUtils.escape(day));
		logger.info("namelike:" + FilterUtils.escape(namelike));
		StopWatch watch = new StopWatch();
		logger.info("Total execution time to getValues in millis: " + watch.getTotalTimeMillis());
		HashMap map = new HashMap();

		watch.start();
		map.put("success", true);
		map.put("form", "YANGMEI");
		List<File> files = GlobalValues.getYangmeiFiles(day);
		map.put("list", convertToFilesListResponse(files, namelike));

		watch.stop();

		return makeJsonResponse(map);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object convertToFilesListResponse(List<File> files, String namelike) {
		logger.info("convertToFilesListResponse!");

		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

		if (files.isEmpty()) {
			logger.info("no this dir!!");
			return null;
		}

		logger.info("files size:" + files.size());
		String tempFile = "";
		String tempFiletime = "";
		String tempFiledate = "";
		File cf = null;
		HashMap m;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sda = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < files.size(); i++) {
			m = new HashMap();
			cf = files.get(i);
			tempFile = cf.getName();
			tempFiletime = sdf.format(cf.lastModified());
			tempFiledate = sda.format(cf.lastModified());
			logger.info("name:" + tempFile);
			if (cf.isFile()) {
				m.put("filepath_f", cf.getAbsolutePath());
				m.put("filename_f", tempFile);

				tempFile = FilenameUtils.removeExtension(tempFile);
				m.put("filename", tempFile);
				m.put("filepath", cf.getPath());
				m.put("filetime", tempFiletime);
				m.put("filedate", tempFiledate);
				m.put("filetype", "file");

				if (namelike.isEmpty()) {
					result.add(m);
					logger.info("file");
				} else {
					if (tempFile.startsWith(namelike)) {
						result.add(m);
						logger.info("startsWith file");
					} else {
						logger.info(FilterUtils.escape("no start with:" + namelike + ",no add."));
					}
				}

			} else if (cf.isDirectory()) {
				m.put("filename", tempFile);
				m.put("filetype", "direct");
				logger.info("name:" + tempFile + ":direct");
				result.add(m);
			}
		}
		return result;
	}

	// 下載央媒檔案
	// 傳遞的變數有點(,)有斜線(/)都無法傳遞,故在此補齊路徑
	@RequestMapping(value = "yangmei/download/{dt}/{filename}/{ext}")
	public void getYangmeiFile(@PathVariable String dt, @PathVariable String filename, @PathVariable String ext, HttpSession session, HttpServletResponse response) throws Exception {
		filename = FilenameUtils.removeExtension(filename);
		InputStream inputStream = null;
		try {
			String root = GlobalValues.getYangmeiFolder();
			File fileToDownload = new File(FilterUtils.filter(root + File.separator + dt + File.separator + filename + "." + ext));

			logger.info("get Yangmei filepath:" + fileToDownload.getPath());
			logger.info("filename:" + filename);
			inputStream = new FileInputStream(fileToDownload);
			response.setContentType("application/force-download");
			// if (ext != null && ext.trim().length() > 0) {
			// filename = filename + "." + ext.trim();
			// }

			response.setHeader("Content-Disposition", "attachment; filename=" + filename + "." + ext);
			IOUtils.copy(inputStream, response.getOutputStream());
			response.flushBuffer();
		} catch (Exception e) {
			logger.error("getYangmeiFile error");
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} finally {
			SafeClose.close(inputStream);
		}
	}

	// 打包下載央媒檔案
	// 傳遞的變數有點(,)有斜線(/)都無法傳遞,故在此補齊路徑
	@RequestMapping(value = "yangmei/downloadlist/{dt}")
	public void getYangmeidtZip(@PathVariable String dt, HttpSession session, HttpServletResponse response) throws Exception {
		logger.info("getYangmeidtZip, dt:{}", FilterUtils.escape(dt));
		String root = GlobalValues.getYangmeiFolder();
		String fileToZip = root + File.separator + dt;
		String folderToZip = fileToZip + ".zip";
		InputStream inputStream = null;
		try {
			logger.info("getYangmeidtZip, folderToZip:{}", FilterUtils.escape(folderToZip));
			File zipToDownload = FolderZiper.zipFolder(fileToZip, folderToZip);
			logger.info("getYangmeidtZip, zipToDownload:{}", zipToDownload.exists());
			inputStream = new FileInputStream(zipToDownload);
			response.setContentType("application/force-download");
			// 檔名自取
			response.setHeader("Content-Disposition", "attachment; filename=" + dt + ".zip");
			IOUtils.copy(inputStream, response.getOutputStream());
			response.flushBuffer();
		} catch (Exception e) {
			logger.error("getYangmeidtZip error:" + e.getMessage());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} finally {
			SafeClose.close(inputStream);
		}
	}

	// http://localhost:8080/ifxweb2/web/fomr/~~
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "web/form/{ext}/{formid}", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> getWebForm(@PathVariable String ext, @PathVariable String formid) {
		logger.info("getting Web Form name:" + FilterUtils.escape(formid) + "." + FilterUtils.escape(ext));
		HashMap map = new HashMap();
		logger.info(FilterUtils.escape("form path:" + GlobalValues.getFormPath(formid, ext))); // *.form
		// or
		// *.pdf?
		File file = new File(FilterUtils.filter(GlobalValues.getFormPath(formid, ext)));

		// Path existingFile = Paths.get(GlobalValues.getFormPath(formid));
		// 初始值
		logger.info("讀取form 路徑:" + file.getPath());
		map.put("success", false);
		map.put("formid", formid);

		byte[] bytes = null;
		if (!file.exists()) {
			logger.info("查無檔案");
			return makeJsonResponse(map);
		}
		StopWatch watch = new StopWatch();
		watch.start();
		bytes = readContentIntoByteArray(file);
		String bytestring = Base64.encodeBytes(bytes);
		logger.info("temppage bytes.length:" + bytes.length);
		watch.stop();
		logger.info("Total execution time to getWebForm in millis: " + watch.getTotalTimeMillis());
		map.put("success", true);
		map.put("formbyte", bytestring);

		return makeJsonResponse(map);
	}

	private static byte[] readContentIntoByteArray(File file) {
		FileInputStream fileInputStream = null;
		byte[] bFile = new byte[(int) file.length()];
		try {
			logger.info("readContentIntoByteArray ...");
			// convert file into array of bytes
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bFile);
			// 關閉 log
			// for (int i = 0; i < bFile.length; i++) {
			// System.out.print((char) bFile[i]);
			// }
		} catch (Exception e) {
			logger.error("readContentIntoByteArray:");
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			bFile = null;
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					logger.error("IOException:" + e);
				}
			}
			logger.info("--- readContentIntoByteArray End ---");
		}
		return bFile;
	}

	// http://localhost:8080/ifxweb2/mvc/hnd/war/file/...
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "special/code/four", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> getFourCode(@RequestParam String _d) {
		HashMap map = new HashMap();
		String result = "";
		String value = "";
		map.put("success", false);
		map.put("result", "");
		if (_d.isEmpty()) {
			logger.info("FourCode no input");
			return makeJsonResponse(map);
		}
		logger.info("FourCode:" + FilterUtils.escape(_d));
		// 有兩個特殊 四字節的字 會取錯誤
		// http://zangweiren.blog.51cto.com/412366/94392
		logger.info("FourCode length:" + _d.length());
		logger.info("FourCode true length:" + _d.codePointCount(0, _d.length()));
		// logger.info("FourCode length:" + _d.substring(0, 1));
		// logger.info("FourCode length:" + _d.substring(0, 1).length());
		int j = 0;
		for (int i = 0; i < _d.codePointCount(0, _d.length()); i++) {
			logger.info("FourCode:" + _d.codePointAt(j));
			logger.info("Exist ?" + GlobalValues.fourcoCode.containsKey(_d.codePointAt(j)));
			value = GlobalValues.fourcoCode.get(_d.codePointAt(j));
			logger.info("out:" + value);
			if (value != null) {
				result += value;
			} else {
				result += "????";
			}
			// 如果是兩碼的，則需要多加1位
			if (Character.charCount(_d.codePointAt(j)) != 1) {
				j++;
			}
			j++;
		}
		map.put("success", true);
		map.put("result", result);
		logger.info("FourCode result:" + result);
		return makeJsonResponse(map);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "special/code/refour", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> getRefourCode(@RequestParam String _d) {
		HashMap map = new HashMap();
		String result = "";
		String value = "";
		map.put("success", false);
		map.put("result", "");
		if (_d.isEmpty()) {
			logger.info("reFourCode no input");
			return makeJsonResponse(map);
		}
		logger.info("reFourCode:" + FilterUtils.escape(_d));
		logger.info("reFourCode length:" + _d.length());
		int j = 0;
		String tempfour = "";
		// 每隔四個數字轉回中文
		for (int i = 0; i < _d.length(); i += 4) {
			j = i + 4;
			tempfour = _d.substring(i, j);
			logger.info(FilterUtils.escape("reFourCode:" + tempfour));
			logger.info("Exist ?" + GlobalValues.codeFourco.containsKey(tempfour));
			value = GlobalValues.codeFourco.get(tempfour);
			logger.info("out:" + value);
			if (value != null) {
				result += value;
			} else {
				result += "?";
			}
		}
		map.put("success", true);
		map.put("result", result);
		logger.info("reFourCode result:" + result);
		return makeJsonResponse(map);
	}

	// 測試轉碼
	// http://localhost:8080/ifxweb2/mvc/hnd/special/confirm/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "special/confirm", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> confirmCode(@RequestParam String _d) {
		logger.info("comfirmCode:" + FilterUtils.escape(_d));
		HashMap map = new HashMap();
		map.put("success", true);
		if (_d.isEmpty()) {
			logger.info("comfirmCode no input");
			return makeJsonResponse(map);
		}
		byte[] data = null;
		String result = null;
		try {
			data = _d.getBytes("cp937");
			result = new String(data, "cp937");
		} catch (UnsupportedEncodingException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}

		if (!_d.equals(result)) {
			map.put("success", false);
			// 把錯誤的字秀出來給使用者看
			for (int i = 0; i < _d.length(); i++) {
				String temp = _d.substring(i, i + 1);
				if (result != null) {
					if (!temp.equals(result.substring(i, i + 1))) {
						map.put("errorstr", temp);
					}
				}
			}
		}

		logger.info("Total execution time to getValues in millis: ");

		return makeJsonResponse(map);
	}

	// http://localhost:8080/ifxweb2/mvc/hnd/jnl/scan?txno=
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "jnl/scan", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> journalScan(@RequestParam String _d) {
		logger.info("Journal scan, _d:{}", FilterUtils.escape(_d));
		Gson gson = new Gson();
		HashMap<String, String> m = new HashMap<String, String>();
		m = (HashMap<String, String>) gson.fromJson(_d, m.getClass());
		StopWatch watch = new StopWatch();
		watch.start();

		JournalService service = MySpring.getJournalService();
		List<Journal> list = service.findByCriteriaQuery(m);
		watch.stop();
		logger.info("Total execution time to getValues in millis: " + watch.getTotalTimeMillis());
		logger.info("Total records:" + list.size());
		HashMap map = new HashMap();
		map.put("success", true);
		map.put("form", "JSCAN");
		map.put("list", convertToJnlScanResponse(list));

		return makeJsonResponse(map);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object convertToJnlScanResponse(List<Journal> list) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");

		SimpleDateFormat busDateFormat = new SimpleDateFormat("yyyyMMdd");
		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		for (Journal j : list) {
			HashMap m = new HashMap();
			m.put("id", j.getId());
			m.put("jnlDate", df.format(j.getJnlDate()));
			m.put("jnlTime", tf.format(j.getJnlTime()));
			try {
				m.put("busDate", df.format(busDateFormat.parse(j.getBusdate())));
			} catch (ParseException e) {
				m.put("busDate", j.getBusdate());
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
			}
			m.put("txno", j.getTxno());
			m.put("mrkey", j.getMrkey());
			m.put("currency", j.getCurrency());
			m.put("txamt", j.getTxamt());
			m.put("txcode", j.getTxcode());
			m.put("cifkey", j.getCifkey());
			m.put("fbrno", j.getFbrno());
			m.put("pbrno", j.getPbrno());
			m.put("acbrno", j.getAcbrno());
			m.put("brn", j.getBrn());
			m.put("tlrno", j.getTlrno());
			m.put("overd", j.getOvred());
			m.put("rqsp", j.getRqsp());
			m.put("supno", j.getSupno());
			result.add(m);

		}
		return result;
	}

	// @Autowired
	// private TranDocService tranDocService;
	//
	// @Autowired
	// private TranDocLogService tranDocLogService;
	//
	// @Autowired
	// private TxcdService txcdService;

	// http://localhost:8080/ifxweb2/mvc/hnd/doc/save?jnlId....
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "doc/save", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> saveDoc(@RequestParam String _d, HttpSession session) {

		TranDocService tranDocService = MySpring.getTranDocService();

		// TranDocLogService tranDocLogService = MySpring.getBean("tranDocLogService",
		// TranDocLogService.class);

		// TxcdService txcdService = MySpring.getBean("txcdService", TxcdService.class);

		String jnlId, docName, docPrompt, docParameter, content, srhCifkey, srhMrkey, srhTemp, srhCurrency, srhTxamt;
		HashMap m = new HashMap();
		try {
			Gson gson = new Gson();
			HashMap<String, String> dataMap = new HashMap<String, String>();
			Type type = new TypeToken<HashMap<String, String>>() {
			}.getType();
			dataMap = (HashMap<String, String>) gson.fromJson(_d, type);
			jnlId = dataMap.get("jnlId");
			docName = "" + dataMap.get("docName");
			docPrompt = "" + dataMap.get("docPrompt").trim();// 過長...
			docParameter = "" + dataMap.get("docParameter");
			content = "" + dataMap.get("content");
			srhCifkey = "" + dataMap.get("srhCifkey");
			srhMrkey = "" + dataMap.get("srhMrkey");
			srhTemp = "" + dataMap.get("srhTemp");
			srhCurrency = "" + dataMap.get("srhCurrency");
			srhTxamt = "" + dataMap.get("srhTxamt");

			StopWatch watch = new StopWatch();
			watch.start();

			logger.info("save doc, jnlId:{}, docName:{}, docPrompt:{},docParameter:{},\ncontent:{}", FilterUtils.escape(jnlId), FilterUtils.escape(docName), FilterUtils.escape(docPrompt),
					FilterUtils.escape(docParameter), FilterUtils.escape(content));
			logger.info("start tranDocService");
			logger.info("session:" + FilterUtils.escape(session.toString()));
			logger.info("TRAN_DOC_TMP:" + session.getAttribute(GlobalValues.TRAN_DOC_TMP));
			TranDoc tmpTranDoc = (TranDoc) session.getAttribute(GlobalValues.TRAN_DOC_TMP);
			tmpTranDoc.setSrhCifkey(srhCifkey);
			tmpTranDoc.setSrhMrkey(srhMrkey);
			tmpTranDoc.setSrhTemp(srhTemp);
			tmpTranDoc.setSrhCurrency(srhCurrency);
			tmpTranDoc.setSrhTxamt(srhTxamt);
			// tmpTranDoc.setk....._d.
			logger.info("middle tranDocService");
			logger.info(FilterUtils.escape("tmpTranDoc:" + tmpTranDoc));
			TranDoc doc = tranDocService.save(tmpTranDoc, Long.parseLong(jnlId), docName, docPrompt, docParameter, content);
			logger.info("after tranDocService");
			m.put("success", true);
			m.put("errmsg", "");
			m.put("docId", doc.getDocId());

			watch.stop();
			logger.info("Total execution time to getValues in millis: " + watch.getTotalTimeMillis());

		} catch (Exception ex) {
			logger.error("saveDoc:" + ex.getMessage());
			m.put("success", false);
			m.put("errmsg", ex.getMessage());
		}
		return makeJsonResponse(m);

	}

	// http://localhost:8080/ifxweb2/mvc/hnd/doc/list/2
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "doc/list/{jnlId}", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> getDocList(@PathVariable String jnlId) {
		TranDocService tranDocService = MySpring.getTranDocService();

		logger.info("getting Dup Docs for {}", FilterUtils.escape(jnlId));
		HashMap m = new HashMap();
		StopWatch watch = new StopWatch();
		watch.start();
		try {
			List<TranDoc> list = tranDocService.findByJnlId(Long.parseLong(jnlId), true);
			m.put("success", true);
			m.put("list", makeSlimDocList(list));
			logger.info("Total execution time to getValues in millis: " + watch.getTotalTimeMillis());
		} catch (Exception ex) {
			logger.error("getDocList:" + ex.getMessage());
			m.put("success", false);
			m.put("errmsg", ex.getMessage());
		}
		return makeJsonResponse(m);

	}

	// http://localhost:8080/ifxweb2/mvc/hnd/doc/listfake/2
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "doc/listfake/{fakejnlId}", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> getfakeDocList(@PathVariable String fakejnlId) {
		TranDocService tranDocService = MySpring.getTranDocService();
		logger.info("getting Dup Docs for {}", FilterUtils.escape(fakejnlId));
		HashMap m = new HashMap();
		StopWatch watch = new StopWatch();
		watch.start();
		try {
			JournalService service = MySpring.getJournalService();
			List<Journal> jnlList = service.findByF4(fakejnlId.substring(0, 8), fakejnlId.substring(8, 12), fakejnlId.substring(12, 14), fakejnlId.substring(14));
			Journal jnl = null;
			if (jnlList != null && jnlList.size() > 0)
				jnl = jnlList.get(0);
			List<TranDoc> list = null;
			if (jnl != null) {
				fakejnlId = jnl.getId().toString();
				list = tranDocService.findByJnlId(Long.parseLong(fakejnlId), true);

				m.put("success", true);
				m.put("list", makeSlimDocList(list));
				logger.info("Total execution time to getValues in millis: " + watch.getTotalTimeMillis());
			} else {
				m.put("success", false);
				m.put("errmsg", "查無此交易序號");
			}
		} catch (Exception ex) {
			logger.error("getfakeDocList:" + ex.getMessage());
			m.put("success", false);
			m.put("errmsg", ex.getMessage());
		}
		return makeJsonResponse(m);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<HashMap> makeSlimDocList(List<TranDoc> list) {
		List<HashMap> outputList = new ArrayList<HashMap>();
		for (TranDoc d : list) {
			HashMap m = new HashMap();
			m.put("docId", d.getDocId());
			m.put("docName", d.getDocName());
			m.put("docPrompt", d.getDocPrompt());
			outputList.add(m);
		}
		return outputList;
	}

	// http://localhost:8080/ifxweb2/mvc/hnd/doc/get/41
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "doc/get/{docId}", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> getDoc(@PathVariable String docId, @RequestParam(required = false, defaultValue = "0") String _addLog, HttpSession session) {
		TranDocService tranDocService = MySpring.getTranDocService();
		TranDocLogService tranDocLogService = MySpring.getBean("tranDocLogService", TranDocLogService.class);
		logger.info("getting Dup Doc for DocID:{}, addLog:{} ", FilterUtils.escape(docId), FilterUtils.escape(_addLog));
		HashMap m = new HashMap();
		Long count;
		TranDoc tranDoc;
		StopWatch watch = new StopWatch();
		watch.start();
		UserInfo userInfo = (UserInfo) session.getAttribute(GlobalValues.SESSION_USER_INFO);
		try {
			tranDoc = tranDocService.getByDocId(Long.parseLong(docId));
			if (_addLog.equals("1")) {
				addTranDocLog(session, Long.parseLong(docId));
			}
			// 使用分行分開計算次數
			// count = tranDocLogService.getDupCounts(Long.parseLong(docId));
			count = tranDocLogService.getDupbrnCounts(Long.parseLong(docId), userInfo.getBrno());
			m.put("success", true);
			m.put("doc", convertToSlimDoc(tranDoc, count));
			logger.info("Total execution time to getValues in millis: " + watch.getTotalTimeMillis());
		} catch (Exception ex) {
			logger.error("getDoc:" + ex.getMessage());
			m.put("success", false);
			m.put("errmsg", ex.getMessage());
		}
		return makeJsonResponse(m);

	}

	// http://localhost:8080/ifxweb2/mvc/hnd/doc/get/41
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "doc/addlog/{docId}", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> addDoclog(@PathVariable String docId, HttpSession session) {
		logger.info("add Dup Doc log for DocID:{} ", FilterUtils.escape(docId));
		HashMap m = new HashMap();
		StopWatch watch = new StopWatch();
		watch.start();
		m.put("success", true);
		try {
			addTranDocLog(session, Long.parseLong(docId));
			logger.info("Total execution time to getValues in millis: " + watch.getTotalTimeMillis());
		} catch (Exception ex) {
			logger.error("addDoclog:" + ex.getMessage());
			m.put("success", false);
			m.put("errmsg", ex.getMessage());
		}
		return makeJsonResponse(m);

	}

	private TranDocLog addTranDocLog(HttpSession session, long docId) {
		TranDocLogService tranDocLogService = MySpring.getBean("tranDocLogService", TranDocLogService.class);
		UserInfo userInfo = (UserInfo) session.getAttribute(GlobalValues.SESSION_USER_INFO);
		// String who = userInfo.getBrno() + userInfo.getId();
		TranDocLog log = new TranDocLog();
		log.setDocId(docId);
		log.touch();
		log.setPrintBrno(userInfo.getBrno());
		log.setPrintTlrno(userInfo.getId());
		return tranDocLogService.save(log);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private HashMap convertToSlimDoc(TranDoc tranDoc, Long dupCount) {
		HashMap m = new HashMap();
		m.put("docId", tranDoc.getDocId());
		m.put("form", tranDoc.getDocName());
		m.put("prompt", tranDoc.getDocPrompt());
		m.put("parameter", tranDoc.getDocParameter());
		m.put("dups", dupCount);
		List<String> list = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		for (TranDocBuf b : tranDoc.getBuffers()) {
			// list.add(b.getBuffer());
			sb.append(b.getBuffer());
		}
		m.put("content", sb.toString());
		return m;
	}

	// http://localhost:8080/ifxweb2/mvc/hnd/doc/scan?
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "doc/scan", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> trandocScan(@RequestParam String _d) {
		logger.info("trandoc scan, _d:{}", FilterUtils.escape(_d));
		Gson gson = new Gson();
		HashMap<String, String> m = new HashMap<String, String>();
		m = (HashMap<String, String>) gson.fromJson(_d, m.getClass());
		StopWatch watch = new StopWatch();
		watch.start();

		TranDocService service = MySpring.getTranDocService();
		logger.info("after service.");
		List<TranDoc> list = null;
		logger.info("m.get(special):" + FilterUtils.escape(m.get("special")));

		if (m.get("special").equals("true")) {
			logger.info("findByCriteriaQuerylast!");
			list = service.findByCriteriaQuerylast(m); // 可能之後都是使用此版本
		} else {
			list = service.findByCriteriaQuery(m); // 原本的正常版本
		}

		logger.info("after list:" + list.size());
		watch.stop();
		logger.info("Total execution time to getValues in millis: " + watch.getTotalTimeMillis());
		logger.info("Total records:" + list.size());
		HashMap map = new HashMap();
		logger.info("after map");
		map.put("success", true);
		logger.info("after success");
		map.put("form", "TSCAN");
		logger.info("after form");
		if (m.get("special").equals("true")) {
			map.put("list", convertToTranScanResponsetest(list));
		} else {
			map.put("list", convertToTranScanResponse(list));
		}
		logger.info(FilterUtils.escape("list records:" + map.get("list")));

		return makeJsonResponse(map);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object convertToTranScanResponse(List<TranDoc> list) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		logger.info("after df");
		SimpleDateFormat busDateFormat = new SimpleDateFormat("yyyyMMdd");
		logger.info("after busDateFormat");
		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		logger.info("after result:" + list.size());

		for (int i = 0; i < list.size(); i++) {
			HashMap m = new HashMap();
			m.put("id", list.get(i).getDocId());
			m.put("jnlid", list.get(i).getJnlId());
			try {
				m.put("busDate", df.format(busDateFormat.parse(list.get(i).getSrhBusdate())));
			} catch (Exception e) {
				m.put("busDate", list.get(i).getSrhBusdate());
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
			}

			m.put("kinbr", list.get(i).getSrhKinbr());
			m.put("rbrno", list.get(i).getSrhRbrno());
			m.put("fbrno", list.get(i).getSrhFbrno());
			m.put("acbrno", list.get(i).getSrhAcbrno());
			m.put("pbrno", list.get(i).getSrhPbrno());
			m.put("cifkey", list.get(i).getSrhCifkey());
			m.put("batno", list.get(i).getSrhBatno());
			m.put("docname", list.get(i).getDocName());
			m.put("mrkey", list.get(i).getSrhMrkey());
			m.put("prompt", list.get(i).getDocPrompt());
			m.put("txcode", list.get(i).getSrhTxcode());
			result.add(m);
			logger.info(FilterUtils.escape("result m:" + m));
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes" })
	private Object convertToTranScanResponsetest(List list) {
		logger.info("in convertToTranScanResponsetest!");
		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		logger.info("list:" + list.size());
		try {
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Object[] values = (Object[]) iter.next();
				HashMap<String, String> m = new HashMap<String, String>();
				for (int i = 0; i < values.length; i++) {
					m.put(Integer.toString(i), values[i].toString());
				}
				result.add(m);
			}
		} catch (Exception e) {
			logger.error("Exception:" + e);
		}

		logger.info("result:" + result.size());
		return result;
	}

	@Autowired
	private OvrService ovrService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "screen/save", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> saveScreen(@RequestParam String _d) {
		HashMap m = new HashMap();
		logger.info(FilterUtils.escape(_d));
		try {
			StopWatch watch = new StopWatch();
			watch.start();

			Gson gson = new Gson();
			HashMap<String, String> dataMap = new HashMap<String, String>();
			Type type = new TypeToken<HashMap<String, String>>() {
			}.getType();
			dataMap = (HashMap<String, String>) gson.fromJson(_d, type);
			String brn = dataMap.get("brn");
			String tlrno = dataMap.get("tlrno");
			String txcd = dataMap.get("txcd");
			String rqsp = dataMap.get("rqsp");
			String content = dataMap.get("content");

			logger.info("saveScreen, brn:{}, tlr:{}, txcd:{}", FilterUtils.escape(brn), FilterUtils.escape(tlrno), FilterUtils.escape(txcd));
			Ovr ovr = new Ovr();
			ovr.setBrn(brn);
			ovr.setTlrno(tlrno);
			ovr.setTxcd(txcd);
			ovr.setRqsp(rqsp.substring(0, 4));
			ovr.setRqspMessage(rqsp.substring(4));
			ovrService.save(ovr, content);

			m.put("success", true);
			m.put("errmsg", "");
			m.put("id", ovr.getId());

			watch.stop();
			logger.info("Total execution time to getValues in millis: " + watch.getTotalTimeMillis());

		} catch (Exception ex) {
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			logger.warn(errors.toString());

			m.put("success", false);
			m.put("errmsg", errors.toString());
		}
		return makeJsonResponse(m);
	}

	// http://localhost:8080/ifxweb2/mvc/hnd/screen/get/12
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "screen/get/{id}", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> getScreen(@PathVariable String id) {
		logger.info("getting Screen id:{} ", FilterUtils.escape(id));
		HashMap m = new HashMap();
		Ovr ovr = null;
		StopWatch watch = new StopWatch();
		watch.start();
		try {
			ovr = ovrService.get(Long.parseLong(id), false);
			logger.info("ovr:{}", FilterUtils.escape(ovr));
			m.put("success", true);

			StringBuilder sb = new StringBuilder();
			for (OvrScreen scr : ovr.getBuffers()) {
				sb.append(scr.getBuf());
			}
			m.put("buffer", sb.toString());
			logger.info("Total execution time to getValues in millis: " + watch.getTotalTimeMillis());
		} catch (Exception ex) {
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			m.put("success", false);
			m.put("errmsg", errors.toString());
		}
		return makeJsonResponse(m);
	}

	@Autowired
	private NewsTickerTask newsTicker;

	// http://localhost:8080/ifxweb2/mvc/hnd/tickers
	@RequestMapping(value = "tickers", method = RequestMethod.GET)
	public ResponseEntity<String> getTickers(@RequestParam(required = false) String callback, HttpSession session, HttpServletResponse response) {

		UserInfo userInfo = (UserInfo) session.getAttribute(GlobalValues.SESSION_USER_INFO);
		String brno;
		if (userInfo == null) {
			// logger.info("must login");
			return null;
			// throw new RuntimeException("must login");
		} else {
			brno = userInfo.getBrno();
			if (brno == null) {
				// logger.error("must login");
				return null;
				// throw new RuntimeException("must login");
			}
		}
		logger.info("getting tickers:" + brno);
		SessionMap sessionMap = (SessionMap) session.getAttribute(GlobalValues.SESSION_SYSVAR_MAP);
		String fdate = sessionMap.get("FDATE");
		logger.info("fdate:" + fdate);

		String[] result = tickerService.findValidTicker(brno, fdate);
		// String[] news = newsTicker.getNews();
		String[] news = new String[0];
		String[] ss = new String[result.length + news.length];
		System.arraycopy(result, 0, ss, 0, result.length);
		System.arraycopy(news, 0, ss, result.length, news.length);
		String output = new Gson().toJson(ss);

		HttpHeaders responseHeaders = new HttpHeaders();
		if (callback != null && callback.length() > 0) {
			responseHeaders.add("Content-Type", "application/javascript; charset=utf-8");
			output = "callback(" + output + ")";
		} else {
			responseHeaders.add("Content-Type", "application/json; charset=utf-8");
		}

		logger.info("return json:\n" + FilterUtils.escape(output));
		return new ResponseEntity<String>(output, responseHeaders, HttpStatus.CREATED);
	}

	@Autowired
	private TickerService tickerService;

	// http://localhost:8080/ifxweb2/mvc/hnd/tickers/add
	@RequestMapping(value = "tickers/add", method = RequestMethod.GET)
	public ResponseEntity<String> addTicker(@RequestParam String content, @RequestParam String validPeriod, @RequestParam(required = false) String callback, HttpSession session,
			HttpServletResponse response) {
		logger.info("adding  ticker, content:" + FilterUtils.escape(content));
		String result = "OK";
		Ticker ticker = new Ticker();
		ticker.setContent(content);
		ticker.setStopTime(Long.parseLong(validPeriod));
		tickerService.save(ticker);

		String output = new Gson().toJson(result);

		HttpHeaders responseHeaders = new HttpHeaders();
		if (callback != null && callback.length() > 0) {
			responseHeaders.add("Content-Type", "application/javascript; charset=utf-8");
			output = "callback(" + output + ")";
		} else {
			responseHeaders.add("Content-Type", "application/json; charset=utf-8");
		}

		logger.info("return json:\n" + output);
		return new ResponseEntity<String>(output, responseHeaders, HttpStatus.CREATED);
	}

	/* 潘 新增跑馬燈查詢--Start */
	// http://localhost:8080/ifxweb2/mvc/hnd/tickers/scan
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "tickers/scan", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> tickersScan(@RequestParam String _d) {
		logger.info("Tickers scan, _d:{}", FilterUtils.escape(_d));
		TickerService tickerServiceP;
		Gson gson = new Gson();

		HashMap<String, String> m = new HashMap<String, String>();
		m = (HashMap<String, String>) gson.fromJson(_d, m.getClass());

		tickerServiceP = MySpring.getTickerService();
		List<Ticker> list = tickerServiceP.findStopTime(m);

		HashMap map = new HashMap();
		map.put("success", true);
		map.put("form", "TSCAN");
		map.put("list", convertToTickerScanResponse(list));
		logger.info(FilterUtils.escape("list records:" + map.get("list")));

		return makeJsonResponse(map);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object convertToTickerScanResponse(List<Ticker> list) {
		String stopDate, stopTime;

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat tf2 = new SimpleDateFormat("HH:mm");
		SimpleDateFormat tf3 = new SimpleDateFormat("HHmm");

		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		for (Ticker j : list) {
			HashMap m = new HashMap();
			m.put("id", j.getId());
			m.put("date", df.format(j.getDated()));
			m.put("time", tf.format(j.getTime()));
			m.put("brno", j.getBrno());
			m.put("content", j.getContent());
			stopDate = j.getStopTime().toString().substring(0, 8);
			stopTime = j.getStopTime().toString().substring(8, 12);

			logger.info("StopDate : " + stopDate + "StopTime : " + stopTime);
			try {
				m.put("stopDate", df.format(df2.parse(stopDate)));
				m.put("stopTime", tf2.format(tf3.parse(stopTime)));
			} catch (ParseException e) {
				logger.info("FormatDate Error...");
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
			}
			m.put("tickno", j.getTickno());

			logger.info(FilterUtils.escape(m.toString()));
			result.add(m);
		}
		return result;
	}

	/* 潘 新增跑馬燈查詢--End */

	@Autowired
	private MsgService msgService;

	@RequestMapping(value = "msgbox", method = RequestMethod.GET)
	public ResponseEntity<String> getMsgBox(@RequestParam(required = false) String option, HttpSession session, HttpServletResponse response) throws ParseException {
		logger.info("getting msgs");

		UserInfo userInfo = (UserInfo) session.getAttribute(GlobalValues.SESSION_USER_INFO);
		HttpHeaders responseHeaders = new HttpHeaders();

		responseHeaders.add("Content-Type", "application/json; charset=utf-8");

		if (userInfo == null) {
			logger.warn("getMsgBox must login");
			return new ResponseEntity<String>("", responseHeaders, HttpStatus.CREATED);
			// throw new RuntimeException("must login");
		}
		String brno = userInfo.getBrno();

		String tlrno = userInfo.getId();
		String tmpcontent = "";
		if (option == null || option.length() == 0)
			option = "1";

		SessionMap sessionMap = (SessionMap) session.getAttribute(GlobalValues.SESSION_SYSVAR_MAP);
		String fdate = sessionMap.get("FDATE");
		logger.info("fdate:" + fdate);

		List<MsgBox> list = msgService.get(brno, tlrno, Integer.valueOf(option), fdate);
		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		for (MsgBox b : list) {
			HashMap<String, String> m = new HashMap<String, String>();
			m.put("id", b.getId().toString());

			String s = (Integer.parseInt(b.getRcvDate().toString().replaceAll("-", "").trim()) - 19110000) + "";
			SimpleDateFormat sdfSource = new SimpleDateFormat("yyyMMdd");
			Date date = sdfSource.parse(s);
			SimpleDateFormat sdfDestination = new SimpleDateFormat("yyy/MM/dd");

			m.put("rcvdate", sdfDestination.format(date));
			m.put("rcvtime", b.getRcvTime().toString());
			m.put("msgno", b.getMsgno());
			tmpcontent = b.getContent();
			m.put("message", tmpcontent);
			String viewdate = (b.getViewDate() != null && b.getViewTime() != null)
					? (Integer.parseInt(b.getViewDate().toString().replaceAll("-", "").trim()) - 19110000) + " " + b.getViewTime().toString()
					: "";
			m.put("viewdate", viewdate);
			result.add(m);
		}
		String output = new Gson().toJson(result);

		logger.info("return json:\n" + FilterUtils.escape(output));
		// 原本是 HttpStatus.OK 會被瀏覽器CACHE->改成HttpStatus.CREATED 後就不會
		return new ResponseEntity<String>(output, responseHeaders, HttpStatus.CREATED);
	}

	@RequestMapping(value = "msgbox/update", method = RequestMethod.POST)
	public ResponseEntity<String> updateMsgBox(@RequestParam String id, HttpSession session, HttpServletResponse response) {
		logger.info("update msgs:" + FilterUtils.escape(id));

		UserInfo userInfo = (UserInfo) session.getAttribute(GlobalValues.SESSION_USER_INFO);

		String tlrno = userInfo.getId();
		if (tlrno == null) {
			logger.warn("updateMsgBox must login");
			// throw new RuntimeException("must login");
		}
		MsgBox b = msgService.updateMsgBox(Long.parseLong(id));

		String r = (b.getViewDate() != null && b.getViewTime() != null) ? b.getViewDate().toString() + " " + b.getViewTime().toString() : "";
		String output = new Gson().toJson(r);
		logger.info("msgbox updated, return json:" + output);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(output, responseHeaders, HttpStatus.CREATED);
	}

	// http://localhost:8080/ifxweb2/mvc/hnd/msgbox/talkTo/505029?msg=this is a
	// book
	@RequestMapping(value = "msgbox/talkTo/{who}", method = RequestMethod.GET)
	@ResponseBody
	public String systemTalkTo(@PathVariable String who, @RequestParam String msg) {
		logger.info(FilterUtils.escape("talkTo " + who + "," + msg));
		MswCenter mswCenter = MswCenter.getInstance();
		mswCenter.systemTalk("host", who, msg);
		return "GOOD";

	}

	@RequestMapping(value = "tempScrList", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> serchScrTemp(@RequestParam String _d, HttpSession session, HttpServletResponse response) {
		Gson gson = new Gson();
		Map<?, ?> parm = gson.fromJson(_d, HashMap.class);
		String scrPeth = parm.get("brTlrNo").toString();
		String txCode = parm.get("txCode").toString();

		List<String> fileNmLi = fileReposBean.listFile(scrPeth, txCode);

		Map<String, Object> m = new LinkedHashMap<String, Object>();
		m.put("status", "S");
		m.put("tempScrList", fileNmLi);
		return makeJsonResponse(m);
	}

	@RequestMapping(value = "suspendedTran", method = { RequestMethod.POST, RequestMethod.GET }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String suspendedAction(@RequestParam String cmd, @RequestParam String filename, @RequestParam String content) {
		String ua = request.getHeader("User-Agent").toLowerCase();
		boolean br = (ua != null && ua.indexOf("chrome/") != -1);

		try {
			filename = URLDecoder.decode(filename, "UTF-8");
			content = URLDecoder.decode(content, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
		logger.info(FilterUtils.escape("suspend, cmd:" + cmd));
		logger.info(FilterUtils.escape("file:" + filename));

		String output = null;
		if (cmd.equals("put")) {
			output = fileReposBean.write(filename, content);
		} else if (cmd.equals("get")) {
			output = fileReposBean.read(filename);
		} else if (cmd.equals("delete")) {
			output = fileReposBean.delete(filename);
		}

		logger.info("output:" + output);
		response.setCharacterEncoding("utf-8");

		return output;
	}

	@RequestMapping(value = "dosys/sysvar", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> changeSysVar(@RequestParam String _d, HttpSession session) {
		logger.info("changeSysVar...." + _d);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		Type type = new TypeToken<LinkedHashMap<String, String>>() {
		}.getType();
		Map<String, String> dataMap = gson.fromJson(_d, type);

		SessionMap sessionMap = (SessionMap) session.getAttribute(GlobalValues.SESSION_SYSVAR_MAP);

		sessionMap.put(dataMap.get("name"), dataMap.get("value"));

		try {
			session.setAttribute(GlobalValues.SESSION_SYSVAR_MAP, sessionMap);
			session.setAttribute(GlobalValues.SESSION_SYSVAR, getSessionAsJSON(sessionMap));
			map.put("success", true);
			map.put("msg", "已更新sysvar...");
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());

		}

		return makeJsonResponse(map);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "dosys/action", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> dosysAction(@RequestParam String _d, HttpSession session) {
		String what;
		String shellname;
		String para;
		HashMap m = new HashMap();
		try {
			Gson gson = new Gson();
			HashMap<String, String> dataMap = new HashMap<String, String>();
			Type type = new TypeToken<HashMap<String, String>>() {
			}.getType();
			dataMap = (HashMap<String, String>) gson.fromJson(_d, type);
			what = dataMap.get("what");
			shellname = dataMap.get("shellname");
			para = dataMap.get("para");
			logger.info("dosysAction:" + FilterUtils.escape(what));
			StopWatch watch = new StopWatch();
			watch.start();
			SessionMap sessionMap = (SessionMap) session.getAttribute(GlobalValues.SESSION_SYSVAR_MAP);
			String sessionasjson = "";
			if (what.equals("ERTXTNO")) {
				sessionMap.put("ERTXTNO", "");
				try {
					sessionasjson = getSessionAsJSON(sessionMap);
				} catch (Exception e) {
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					logger.error(errors.toString());
				}
				logger.info("getSessionAsJSON :" + sessionasjson);
				// 更新 map
				session.setAttribute(GlobalValues.SESSION_SYSVAR_MAP, sessionMap);
				session.setAttribute(GlobalValues.SESSION_SYSVAR, sessionasjson);
			} else if (what.equals("RUNSHELL")) { // 11.10已測試成功
				// call shell的資料夾
				String scriptName = GlobalValues.callshellFolder + File.separator + shellname;
				// 參數
				if (!para.isEmpty()) {
					scriptName += " " + para;
				}
				logger.info("in RUNSHELL! call scriptName :" + FilterUtils.escape(scriptName));
				Runtime rt = Runtime.getRuntime();
				Process process = null;
				try {
					scriptName = Validator.removeSpecialCharacters(scriptName);
					process = rt.exec(scriptName);
					if (process != null)
						process.waitFor();
				} catch (Exception e) {
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					logger.error(errors.toString());
				}
			} else if (what.equals("RUNSHELL2")) { // 待測試
				logger.info("in RUNSHELL2!");
				// call shell的資料夾

				String scriptName = GlobalValues.callshellFolder + File.separator + shellname;
				// 參數
				try {
					Runtime rt = Runtime.getRuntime();
					Process process = null;
					if (!para.isEmpty()) {
						logger.info("in command2!");
						String[] command2 = { Validator.removeSpecialCharacters("sh"), Validator.removeSpecialCharacters(scriptName), Validator.removeSpecialCharacters(para) };
						process = rt.exec(command2);
					} else {
						logger.info("in command1!");
						String[] command1 = { Validator.removeSpecialCharacters("sh"), Validator.removeSpecialCharacters(scriptName) };
						process = rt.exec(command1);
					}

					if (process != null)
						process.waitFor();
				} catch (Exception e) {
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					logger.error(errors.toString());
				}
			}

			m.put("success", true);
			m.put("msg", "已動作:" + what);

			watch.stop();
			logger.info("Total execution time to getValues in millis: " + watch.getTotalTimeMillis());

		} catch (Exception ex) {
			m.put("success", false);
			m.put("msg", ex.getMessage());
		}
		return makeJsonResponse(m);

	}

	// 檢查 登入權限是否有
	private boolean checkLoginodbu(SessionMap sessionMap, String t) {

		// 柯:增加 try catch 測試 NullPointerException
		// logger.info("in checkLoginodbu!");
		String dapknd = null;
		String oapknd = null;
		int newnumber = 0;
		try { // 柯 增加 null檢查
			dapknd = sessionMap.get("DAPKND");
			oapknd = sessionMap.get("OAPKND");
			char word = t.charAt(1); // 取得輸txcd的第二碼
			switch (word) {
			case '0':
				word = 'A';
				break;
			case '1':
				word = 'B';
				break;
			case '2':
				word = 'C';
				break;
			case '3':
				word = 'D';
				break;
			case '4':
				word = 'E';
				break;
			case '5':
				word = 'F';
				break;
			case '6':
				word = 'G';
				break;
			case '7':
				word = 'H';
				break;
			case '8':
				word = 'I';
				break;
			case '9':
				word = 'J';
				break;
			}
			int number = word; // 將取得的字元轉換成Unicode碼
			newnumber = (number - 65); // 不+ 1 從0開始
			if (oapknd.charAt(newnumber) == dapknd.charAt(newnumber) && dapknd.charAt(newnumber) == '0') {
				return false;
			} else {
				return true;
			}
		} catch (StringIndexOutOfBoundsException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			return false;
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			return false;
		}

	}

	// 柯: for clean value
	public String getSessionAsJSON(SessionMap sessionMap) throws Exception {
		StringWriter writer = new StringWriter();
		ObjectMapper m = new ObjectMapper();
		m.writeValue(writer, sessionMap);
		return writer.toString();
	}

	/**
	 * 共用取得檔案清單
	 * 
	 * @param _d
	 * @return
	 */
	// mvc/hnd/web/all_sFilelist
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "web/all_sFilelist", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> all_sFilelist(@RequestParam String _d) {
		logger.info("all_sFilelist list scan, _d:{}", FilterUtils.escape(_d));
		Gson gson = new Gson();
		HashMap<String, String> m = new HashMap<String, String>();
		m = (HashMap<String, String>) gson.fromJson(_d, m.getClass());

		// 萬用字原是點和*
		// 日期
		String fday = m.get("f_day").trim();
		// 分行
		String fbrno = m.get("f_brno").trim();
		// 櫃員
		String ftlrno = m.get("f_tlrno").trim();
		// 檔名
		String fname = m.get("f_name").trim();
		// 檔名其他
		// String fother = m.get("f_other").trim();
		// 附檔名
		String fext = m.get("f_ext").trim();

		String file_rexp = "^" + fday + "-" + fbrno + "-" + ftlrno + "-" + fname + "\\." + fext;

		logger.info(FilterUtils.escape("file_rexp: " + file_rexp));

		/*
		 * 
		 * 下傳檔案命名規則
		 * 
		 * REMOTE_FILE='/fxworkfile/import/inbox/&YYMMDD&-&BR&-&TELR&-檔案名稱(X08)_自由運用.
		 * ftp'
		 * 
		 * 例如:20170603-1058-11-FILENAME.ftp
		 * 
		 * 
		 * 查詢時,櫃員代號特別處理
		 * 
		 * 20170603-1058-11-FILENAME.ftp -> 僅11 的櫃員代號可下載 20170603-1058-1X-FILENAME.ftp
		 * -> 1* 的櫃員代號可下載 20170603-1058-XX-FILENAME.ftp -> 1058分行的櫃員代號可下載
		 */
		StopWatch watch = new StopWatch();
		logger.info("Total execution time to getValues in millis: " + watch.getTotalTimeMillis());
		HashMap map = new HashMap();

		watch.start();
		List<File> files = GlobalValues.getFtpFiles(file_rexp);
		map.put("list", convertToFilesListResponse(files, ""));
		map.put("success", true);
		map.put("form", "FTPFILELIST");
		watch.stop();

		return makeJsonResponse(map);

	}

	// 依照路徑下載任何檔案
	// 目前不限定目錄，故要注意資安問題
	// 點 斜線 井字號無法傳送
	@RequestMapping(value = "filedownload/path/{fpath}")
	public void getFiledownloadShare(@PathVariable String fpath, HttpSession session, HttpServletResponse response) throws Exception {
		logger.info("getFiledownloadShare, fpath:{}", FilterUtils.escape(fpath));
		int dotPos;
		String filep = fpath.replace('$', '.');
		List<String> fileptmp = new ArrayList<String>(Arrays.asList(filep.split("&")));
		filep = Joiner.on(File.separator).join(fileptmp);

		logger.info("getFiledownloadShare, filep:{}", filep);
		InputStream inputStream = null;
		FileInputStream fis = null;
		try {
			File fileToDownload = new File(filep);
			logger.info("getFiledownloadShare, fileToDownload:{}", fileToDownload.exists());
			// if(fileToDownload.exists() &&
			// fileToDownload.getParentFile().equals("ftp")){
			if (fileToDownload.exists()) {
				fis = new FileInputStream(fileToDownload);
				inputStream = fis;
				dotPos = fileToDownload.getName().lastIndexOf('.');
				response.setContentType("application/force-download");
				// response.setHeader("Content-Disposition", "attachment; filename=" +
				// fileToDownload.getName());
				response.setHeader("Content-Disposition", "attachment; filename=" + fileToDownload.getName().substring(0, dotPos) + ".txt");
				IOUtils.copy(inputStream, response.getOutputStream());
				response.flushBuffer();
			}
		} catch (Exception e) {
			logger.error("getFiledownloadShare error");
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} finally {
			SafeClose.close(inputStream);
			SafeClose.close(fis);
		}
	}

	@RequestMapping(value = "usrpub/alluser", method = RequestMethod.GET)
	@ResponseBody
	public String userPuballuser() {
		logger.info("userPuballuser!");
		UserPubService userPubService = MySpring.getUserPubService();
		Long count = userPubService.findAllcount("USER");
		logger.info("findAllcount :" + count);
		return count.toString();
	}

	@RequestMapping(value = "keepAlive", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> keepAlive(@RequestParam String _d) {
		Map<String, Object> m = new LinkedHashMap<String, Object>();
		m.put("msg", "keepAlive is OK");
		return makeJsonResponse(m);
	}

}