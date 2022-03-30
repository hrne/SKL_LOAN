package com.st1.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.st1.bean.FileReposBean;
import com.st1.def.menu.Menu;
import com.st1.def.menu.MenuItem;
import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.filter.SafeClose;
import com.st1.ifx.menu.TranListBuilder;
import com.st1.ifx.service.CodeListService;
import com.st1.ifx.service.HelpListService;
import com.st1.ifx.service.TxcdService;
import com.st1.msw.HttpClientDemo;
import com.st1.util.MySpring;
import com.st1.util.PoorManFile;
import com.st1.util.PoorManJson;
import com.st1.util.PoorManUtil;

public class GlobalValues {
	static final Logger logger = LoggerFactory.getLogger(GlobalValues.class);

	public static String SESSION_SYSVAR_MAP = "_sysvar.map";
	public static String SESSION_SYSVAR = "_sysvar";
	public static String SESSION_UID = "_uid";
	public static String SESSION_USER_INFO = "_user_info_";
	public static String SESSION_USER_SCRFILE_PREFIX = "_user_scr.prefix";
	public static String SESSION_USER_C12 = "_user_c12_text";
	public static String SESSION_USER_ATTACH = "_user_attach_text";
	public static String SESSION_USER_TRANMAP = "_user_tran_map";
	public static String ifxFolder;
	public static String ifxWriter;
	public static String proxy;
	public static String runtimeFolder;
	public static String callshellFolder;
	public static String hostLogFolder;
	public static String TRAN_DOC_TMP = "_tran_doc_tmp_";
	public static String tranFolder;
	// 各交易說明的網頁
	public static String explainFolder;
	public static String formFolder;
	public static String menuPath;
	public static String swiftFolder;
	// 供 主動訊息HostTran放檔案使用
	public static String notifyputFolder;
	public static String notifyputbakFolder;
	// 供 主動訊息HostTran放Import檔案 到inbox
	public static String notifyputFolderim;
	public static String notifyputhostFolderim;
	// #中鋼網銀電文超長時使用 XW130?
	public static String swift_mergeFolder;
	// #補送中鋼網銀表單 海清檔案資料夾
	public static String csteelxmlFolder;
	private static String swiftUnsoFolder;
	// swift & 報表 路徑
	public static String REPOSITORY_ROOT = "";
	// online 報表路徑
	public static String ROPOSITORY_ROOT = "";
	// 下載路徑
	public static String DOWNFILE_ROOT = "";
	// 本機開啟 MqtestServer 的PORT號
	// 註: HostTran 內有需求判斷此欄位 "putFromqToTita()"
	public static String mqServerPort;
	// 本機位置
	public static String localAddr;
	// 其他機器位置
	public static String otherAddr;
	public static String otherMqAddr;
	public static String tranEnvJson;
	public static String sealUrl = null;
	public static String fmtType = "1";
	public static String goMqtype = "0";
	public static String fourcofile = null;
	public static String SESSION_LAST_HOSTSEQ = "_last_host_seq";
	public static String applicationVersion = "v0.0";
	public static String jsVersionPath = null;
	public static String jsVersion = null;
	// 新增 Help.js的版號
	public static String helpjsVersionPath = null;
	public static String helpjsVersion = null;
	// send mail Ip Address
	public static String mailIpAddress = null;
	// SNA_IP_PORT
	public static String sna_Ipport_txcd = null;
	public static String twnbconfigFilePath = null;

	private static String envFile = "env.txt";
	private static String menuHtml;
	private static String fileRepos;

	// 中文轉數字
	public static HashMap<Integer, String> fourcoCode;
	// 數字轉中文
	public static HashMap<String, String> codeFourco;

	public static boolean systemStarted = false;
	public static boolean bR6 = true;
	public static boolean printerEnabled = false;
	public static boolean journalWanted = false;

	// DDHHMMSS+00000001
	public static int snaHeadDS = 16;
	public static int minTotaLen = 70;
	public static int helpCacheMinutes = 60 * 12; // 12 hours

	public static JSONObject j_Ipport_txcd = null;

	private static Menu menu;

	public static LinkedHashMap<String, String> sendIp = null;

	public static String getSwiftUnsoFolder() {
		return swiftUnsoFolder;
	}

	public static String getSna_Ipport_txcd() {
		return sna_Ipport_txcd;
	}

	public static void setSna_Ipport_txcd(String sna_Ipport_txcd) {
		if (GlobalValues.sna_Ipport_txcd == null) {
			GlobalValues.sna_Ipport_txcd = sna_Ipport_txcd;
		}
	}

	public static String getIfxFolder() {
		return ifxFolder;
	}

	public static String getMqServerPort() {
		return mqServerPort;
	}

	public static void setMqServerPort(String mqServerPort) {
		GlobalValues.mqServerPort = mqServerPort;
	}

	public static String getLocalAddr() {
		return localAddr;
	}

	public static void setLocalAddr(String localAddr) {
		GlobalValues.localAddr = localAddr;
	}

	public static String getOtherAddr() {
		return otherAddr;
	}

	public static void setOtherAddr(String otherAddr) {
		GlobalValues.otherAddr = otherAddr;
	}

	public static String getOtherMqAddr() {
		return otherMqAddr;
	}

	public static void setOtherMqAddr(String otherMqAddr) {
		GlobalValues.otherMqAddr = otherMqAddr;
	}

	public static void setIfxFolder(String fxworkFile, String fxtxWrite) {
		ifxFolder = fxworkFile;
		ifxWriter = fxtxWrite;

		runtimeFolder = ifxFolder + File.separator + "runtime";

		REPOSITORY_ROOT = ifxWriter + File.separator + "repos" + File.separator + "report";
		ROPOSITORY_ROOT = ifxWriter + File.separator + "repos" + File.separator + "report_online";
		DOWNFILE_ROOT = ifxWriter + File.separator + "repos" + File.separator + "data";
		tranFolder = runtimeFolder + File.separator + "tran" + File.separator;
		explainFolder = runtimeFolder + File.separator + "explain" + File.separator;
		formFolder = runtimeFolder + File.separator + "form" + File.separator; // PDF套表
		// menuPath = runtimeFolder + "/menu/menu2.xml";

		// hostLogFolder = runtimeFolder + File.separator + "Log";
		hostLogFolder = ifxWriter + File.separator + "log";
		(new File(FilterUtils.filter(hostLogFolder))).mkdirs();

		// 資料夾先創且權限要先開
		callshellFolder = ifxFolder + File.separator + "callshell";

		swiftFolder = runtimeFolder + File.separator + "swift" + File.separator + "mt" + File.separator;

		String versionpath = ifxFolder + File.separator + "webServerEnv" + File.separator;
		jsVersionPath = versionpath + "js-version.txt";
		helpjsVersionPath = versionpath + "helpjs-version.txt";

		twnbconfigFilePath = versionpath + "TBConvert.conf";
		logger.info("twnbconfigFilePath:" + FilterUtils.escape(twnbconfigFilePath));

		System.out.println("VersionTask!! GlovalValues!");
		readJsVersion();
		readHelpjsVersion(false);// 新增 Help.js的版號 //更新四角號碼
		File ipF = null;
		InputStreamReader read = null;
		BufferedReader br = null;
		try {
			ipF = new File("/home/weblogic/ifxDoc/ifxwriter/sendIp/sendIp.txt");
			read = new InputStreamReader(new FileInputStream(ipF), "UTF-8");
			br = new BufferedReader(read);
			String resop = "";
			while (br.ready())
				resop += br.readLine();

			sendIp = new ObjectMapper().readValue(resop, new TypeReference<Map<String, String>>() {
			});
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} finally {
			SafeClose.close(br);
			SafeClose.close(read);
		}
	}

	public static void readJsVersion() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(FilterUtils.filter(jsVersionPath)));
			jsVersion = br.readLine();
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			jsVersion = PoorManUtil.getNow();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
			}
		}
		logger.info("jsVersion:[" + jsVersion + "]");
	}

	// 新增 Help.js的版號
	public static void readHelpjsVersion(Boolean updateone) {
		BufferedReader br = null;
		List<String> otherAddrs = new ArrayList<String>(Arrays.asList(otherAddr.split(";")));
		logger.info(FilterUtils.escape("ifx_otherAddr:" + otherAddr));
		logger.info("otherAddrs len:" + otherAddrs.size());
		String urlother = "";
		String urlother_result = "";
		try {
			br = new BufferedReader(new FileReader(FilterUtils.filter(helpjsVersionPath)));
			helpjsVersion = br.readLine();
			if (updateone) {
				logger.info("old helpjsVersion!" + helpjsVersion);
				PoorManFile poorFile = new PoorManFile(helpjsVersionPath);
				String[] helpver = helpjsVersion.trim().split("-");

				// 測..增加去空白
				int helpverint = Integer.parseInt(helpver[1]) + 1;
				String todayString = PoorManUtil.getToday();
				logger.info("today String!" + todayString);
				try {
					if (!todayString.equals(helpver[0])) {
						helpjsVersion = todayString + "-1";
					} else {
						helpjsVersion = helpver[0] + "-" + helpverint;
					}
					poorFile.write(helpjsVersion);
					logger.info("new helpjsVersion!:[" + helpjsVersion + "]");

				} catch (IOException e) {
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					logger.error(errors.toString());
				}

			} else {
				// 同步更新四角號碼&MENU選單&HELP下拉式選單
				try {
					logger.info("Update! Four Code:" + FilterUtils.escape(fourcofile));
					CodeListService codeservice = MySpring.getCodeListService();
					logger.info("after CodeListService.");
					if (codeservice != null) {
						logger.info("codeservice evict !!!");
						codeservice.evict();
					}
					HelpListService helpservice = MySpring.getHelpListService();
					logger.info("after HelpListService.");
					if (helpservice != null) {
						logger.info("helpservice evict !!!");
						helpservice.evict();
					}
					TxcdService txcdservice = MySpring.getTxcdService();
					logger.info("after getTxcdService.");
					if (txcdservice != null) {
						logger.info("txcdservice evict !!!");
						txcdservice.evict();
					}
					TranListBuilder builder = MySpring.getTranListBuilder();
					logger.info("after getTranListBuilder.");
					if (builder != null) {
						logger.info("builder evict !!!");
						builder.evict();
						logger.info("after builder evict !!!");
					}
					logger.info("before excelfourCodetoMap.");
					excelfourCodetoMap();
					logger.info("before tranpfnsEvict.");
					tranpfnsEvict();

				} catch (Exception e) {
					logger.error(" readHelpjsVersion-in Exception !!!" + e.getMessage());
				}
			}
		} catch (IOException e) {
			helpjsVersion = PoorManUtil.getNow();
			logger.error(" readHelpjsVersion-in IOException !!!" + e.getMessage());
		} finally {

			// 增加通知其他機台更新java快取版號
			logger.info("do for otherAddrs START");
			for (String othadd : otherAddrs) {
				logger.info("othadd:" + FilterUtils.escape(othadd));
				urlother = othadd + "/mvc/msw/upVersion/h/" + helpjsVersion;
				logger.info("urlother:" + FilterUtils.escape(urlother));
				urlother_result = HttpClientDemo.sendPost(urlother);
				logger.info(FilterUtils.escape(urlother_result));
			}
			logger.info("do for otherAddrs END");
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				logger.error(" readHelpjsVersion-in IOException finally!!!" + e.getMessage());
			}
		}

		logger.info("helpjsVersion:[" + helpjsVersion + "]");
	}

	// 更新 Help.js的版號
	public static void upelpjsVersion(String upVersion) {
		BufferedReader br = null;

		try {
			if (!upVersion.equals(helpjsVersion)) {
				br = new BufferedReader(new FileReader(FilterUtils.filter(helpjsVersionPath)));
				helpjsVersion = br.readLine();
				logger.info("upelpjsVersionold:old helpjsVersion!" + helpjsVersion);
				PoorManFile poorFile = new PoorManFile(helpjsVersionPath);

				poorFile.write(upVersion);
				logger.info("upelpjsVersionold:new helpjsVersion!:[" + helpjsVersion + "]");
			} else {
				logger.info("upelpjsVersionold:same helpjsVersion!:[" + helpjsVersion + "]");
			}

		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			helpjsVersion = PoorManUtil.getNow();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
			}
		}

		logger.info("upelpjsVersionold:helpjsVersion:[" + helpjsVersion + "]");
	}

	public static String combine(String p) {
		return ifxFolder + File.separator + p;
	}

	public static String getMenuPath() {
		return menuPath;
	}

	public static String getTranPath(String txcode, String ext) {
		return tranFolder + File.separator + txcode.substring(0, 2) + File.separator + txcode + ext;
	}

	public static String getExplainPath(String name, String ext) {
		return explainFolder + File.separator + name + ext;
	}

	public static String getFormPath(String formid, String ext) {
		return formFolder + File.separator + formid + "." + ext;
	}

	public static String getExplainFilePath(String exname) {
		return getExplainPath(exname, ".html");
	}

	public static String getTranFilePath(String txcode) {
		return getTranPath(txcode, ".js");
	}

	public static String getPfnFile(String name) {
		name = name.toUpperCase();
		String path = ifxFolder + File.separator + "PFN" + File.separator + name;
		if (!name.toUpperCase(Locale.TAIWAN).endsWith(".PFNX"))
			path += ".PFN";
		return path;
	}

	public static String getDocFolder(String day, String brno) {
		// String day = PoorManUtil.getToday();
		String folder = ifxWriter + File.separator + "docs" + File.separator + brno + File.separator + day;
		if (!(new File(FilterUtils.filter(folder))).exists()) {
			boolean success = (new File(FilterUtils.filter(folder))).mkdirs();
			if (!success) {
				logger.info("can't create folder:" + FilterUtils.escape(folder));
			}
		}
		return folder;
	}

	public static String getYangmeiFolder() {
		// String day = PoorManUtil.getToday();
		String folder = ifxWriter + File.separator + "repos" + File.separator + "yangmei";
		if (!(new File(FilterUtils.filter(folder))).exists()) {
			return null;
		}
		return folder;
	}

	public static String getFtpFolder() {
		// String day = PoorManUtil.getToday();
		String folder = ifxWriter + File.separator + "repos" + File.separator + "ftp";
		if (!(new File(FilterUtils.filter(folder))).exists()) {
			return null;
		}
		return folder;
	}

	public static String getReportFolder(String day, String brno, String type) {
		// String day = PoorManUtil.getToday();
		String folder = REPOSITORY_ROOT + File.separator + brno + File.separator + day;
		if (!type.isEmpty()) {
			folder += File.separator + type;
		}
		if (!(new File(FilterUtils.filter(folder))).exists()) {
			return null;
		}
		return folder;
	}

	public static String getRoportFolder(String day, String brno) {
		// String day = PoorManUtil.getToday();
		String folder = ROPOSITORY_ROOT + File.separator + brno + File.separator + day;
		if (!(new File(FilterUtils.filter(folder))).exists()) {
			return null;
		}
		return folder;
	}

	public static String getDocFilePath(String day, String brno, String txtno, String ext) {
		return getDocFolder(day, brno) + File.separator + txtno + ext;
	}

	public static List<File> getDocFiles(String day, String brno) {
		String folder = getDocFolder(day, brno);
		File dir = new File(FilterUtils.filter(folder));
		File[] files = dir.listFiles();
		return Arrays.asList(files);
	}

	public static List<File> getReportFiles(String day, String brno, String type) {
		String folder = getReportFolder(day, brno, type);
		logger.info("getReportFiles:" + FilterUtils.escape(folder));
		if (!folder.equals(null)) {
			File dir = new File(FilterUtils.filter(folder));
			File[] files = dir.listFiles();
			logger.info("files:" + files.length);
			return Arrays.asList(files);
		}
		return null;
	}

	public static List<File> getRoportFiles(String day, String brno) {
		String folder = getRoportFolder(day, brno);
		logger.info("getRoportFiles:" + FilterUtils.escape(folder));
		if (!folder.equals(null)) {
			File dir = new File(FilterUtils.filter(folder));
			File[] files = dir.listFiles();
			logger.info("files:" + files.length);
			return Arrays.asList(files);
		}
		return null;
	}

	public static List<File> getYangmeiFiles(String day) {
		String folder = getYangmeiFolder();
		folder += "/" + day;
		logger.info("getReportFiles:" + FilterUtils.escape(folder));
		if (!folder.equals(null)) {
			File dir = new File(FilterUtils.filter(folder));
			File[] files = dir.listFiles();
			logger.info("files:" + files.length);
			return Arrays.asList(files);
		}
		return null;
	}

	public static List<File> getFtpFiles(String file_rexp) {
		String folder = getFtpFolder();
		logger.info("getFtpFiles:" + FilterUtils.escape(folder));
		if (!folder.equals(null)) {
			File dir = new File(FilterUtils.filter(folder));
			ftpFilter filter = new ftpFilter(file_rexp);
			File[] files = dir.listFiles(filter);
			logger.info("files:" + files.length);
			return Arrays.asList(files);
		}
		return null;
	}

	public static List<File> getcsteelxmlFiles() {
		String folder = csteelxmlFolder;
		if (!(new File(FilterUtils.filter(folder))).exists()) {
			return null;
		}
		if (!folder.equals(null)) {
			File dir = new File(FilterUtils.filter(folder));
			CstellxmlFilter filter = new CstellxmlFilter(".xml");
			File[] files = dir.listFiles(filter);
			logger.info("files:" + files.length);
			return Arrays.asList(files);
		}
		return null;
	}

	public static File searchDoc(String day, String brno, final String txtno, final String ext) {
		String folder = getDocFolder(day, brno);
		File dir = new File(FilterUtils.filter(folder));
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.startsWith(txtno + ext);
			}
		};
		File[] files = dir.listFiles(filter);
		if (files.length > 0)
			return files[0];
		else
			return null;
	}

	public static String searchReport(String day, String brno, final String filename, final String ext) {
		// 分行+報表日期+業務+檔名
		String folder = REPOSITORY_ROOT + File.separator + brno + File.separator + day + File.separator + filename.substring(0, 2) + File.separator + filename + ext;
		logger.info(FilterUtils.escape("REP folder:" + folder));
		return folder;
	}

	public static String searchRoport(String day, String brno, String tlrno, final String filename, final String ext) {
		// 分行+報表日期+業務+檔名
		String folder = ROPOSITORY_ROOT + File.separator + brno + File.separator + day + File.separator + brno + tlrno + filename + ext;
		logger.info("ROP folder:" + FilterUtils.escape(folder));
		return folder;
	}

	private static String removeBOM(String content) {
		String UTF8_BOM = "\uFEFF";
		if (content.startsWith(UTF8_BOM)) {
			// logger.info("BOM found");
			content = content.replace(UTF8_BOM, "");
		} else {
			// logger.info("no BOM");
		}
		return content;
	}

	@Cacheable("gettranpfns")
	public static String getTranPfns(String txcode) throws Exception {
		logger.info("get pfns for:" + FilterUtils.escape(txcode));
		String pfnsFilePath = getTranPath(txcode, ".pfns");
		PoorManFile pfns = new PoorManFile(pfnsFilePath);
		String content = pfns.read();

		String separator = System.getProperty("line.separator");
		String[] ss = content.split(separator);

		HashMap<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < ss.length; i++) {
			String k = ss[i];
			if (k.trim().length() > 0) {
				PoorManFile pfn = new PoorManFile(getPfnFile(k));
				String v = pfn.read();
				v = removeBOM(v);
				// logger.info(v);
				map.put(k.toUpperCase(), v);
			}
		}

		return "var _pfns = " + convertToJSON(map) + ";";
	}

	@CacheEvict(value = "gettranpfns", allEntries = true)
	public static void tranpfnsEvict() {
		logger.info("evict gettranpfns");

	}

	public static String convertToJSON(HashMap<String, String> map) {

		StringWriter writer = new StringWriter();
		ObjectMapper m = new ObjectMapper();
		try {
			m.writeValue(writer, map);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
		return writer.toString();
	}

	public static String getEnvFilePath(String fileName) {
		return GlobalValues.ifxFolder + File.separator + fileName;
	}

	public static String getPropsFilePath(String fileName) {
		return GlobalValues.ifxFolder + "/runtime/props/" + fileName;
	}

	private static Properties envProps;
	private static HashMap<String, String> userDefVarMap = new HashMap<String, String>();

	public static HashMap<String, String> getUserDefVarMap() {
		return userDefVarMap;

	}

	private static HashMap<String, String> errorCoderMap = new HashMap<String, String>();

	public static HashMap<String, String> getErrorCodeMap() {
		return errorCoderMap;
	}

	public static void readEvn() {
		if (isWindows()) {
			bR6 = false;
		} else {
			bR6 = false;
		}
		logger.info("operation system is R6? " + bR6);

		String filePath = getEnvFilePath(envFile);
		logger.info("evn:" + FilterUtils.escape(filePath));
		envProps = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(FilterUtils.filter(filePath));
			envProps.load(fis);
			envProps.list(System.out);
			String rimCacheWanted = envProps.getProperty("rim_cache", "1");
			RimCaches.init(rimCacheWanted.trim().equals("1"));
			proxy = envProps.getProperty("proxy", "ERROR");
			logger.info("proxy:" + FilterUtils.escape(proxy));

			if (envProps.getProperty("journal", "0").equals("1")) {
				journalWanted = true;
				logger.info("write journal wanted");
			} else {
				logger.info("no journal wanted");
			}

			String printServiceUrl = envProps.getProperty("print_service", null);
			logger.info("print service url:" + FilterUtils.escape(printServiceUrl));
			String unknownFormAction = envProps.getProperty("unknown_form_action", "0");

			String sealurl = envProps.getProperty("seal_service", null);
			logger.info("seal url:" + FilterUtils.escape(sealurl));
			GlobalValues.sealUrl = sealurl;

			String helpCache = envProps.getProperty("help_cache_minutes", "720");
			GlobalValues.helpCacheMinutes = Integer.parseInt(helpCache);
			logger.info("helpCache:" + FilterUtils.escape(helpCache));

			Map<String, String> m = new HashMap<String, String>();
			m.put("printServiceUrl", printServiceUrl);
			m.put("unknownFormAction", unknownFormAction);
			tranEnvJson = new Gson().toJson(m);

			readPropToMap("sysvar", "var.txt", userDefVarMap);
			readPropToMap("error code map", "errors.txt", errorCoderMap);

			// set host format folder
			fmtType = envProps.getProperty("fmt_type", "1");

			// set go mq type
			goMqtype = envProps.getProperty("whant_go_mq", "0");
			logger.info("whant_go_mq:" + FilterUtils.escape(goMqtype));

			// 四角號碼xls檔名
			fourcofile = envProps.getProperty("four_co_file", null);
			logger.info("fourcofile:" + FilterUtils.escape(fourcofile));

			String fmtFolder = runtimeFolder + File.separator + "fmt" + fmtType + File.separator;
			logger.info("fmtType:" + FilterUtils.escape(fmtType));
			logger.info("fmtFolder:" + FilterUtils.escape(fmtFolder));
			com.st1.ifx.hcomm.fmt.Env.setFmtFolder(fmtFolder);
			com.st1.ifx.hcomm.fmt.Env.setLogFolder(hostLogFolder);

			mailIpAddress = envProps.getProperty("mail_IpAddress");
			logger.info("mailIpAddress ww:" + FilterUtils.escape(mailIpAddress));

			// 好像沒在用
			swiftUnsoFolder = envProps.getProperty("swift_unso_folder");
			logger.info("swift unso folder www:" + FilterUtils.escape(swiftUnsoFolder));
			(new File(FilterUtils.filter(swiftUnsoFolder))).mkdirs();

			// 補送中鋼網銀表單資料夾已存在
			logger.info("csteelXmlFolder!!!");
			csteelxmlFolder = envProps.getProperty("csteel_xml_folder");
			logger.info("csteel Xml Folder:" + FilterUtils.escape(csteelxmlFolder));

			notifyputFolder = envProps.getProperty("notify_file");
			logger.info("notify file:" + FilterUtils.escape(notifyputFolder));
			(new File(FilterUtils.filter(notifyputFolder))).mkdirs();

			notifyputbakFolder = envProps.getProperty("notify_bakfile");
			logger.info("notify bakfile:" + FilterUtils.escape(notifyputbakFolder));
			(new File(FilterUtils.filter(notifyputbakFolder))).mkdirs();

			notifyputFolderim = envProps.getProperty("notify_im_file");
			logger.info("notify im file:" + FilterUtils.escape(notifyputFolderim));
			(new File(FilterUtils.filter(notifyputFolderim))).mkdirs();

			notifyputhostFolderim = envProps.getProperty("notify_im_hostfile");
			logger.info(FilterUtils.escape("notify im host file:" + notifyputhostFolderim));
			(new File(FilterUtils.filter(notifyputhostFolderim))).mkdirs();

			// 開發機沒有設定這個參數?
			swift_mergeFolder = envProps.getProperty("swift_mergefolder");
			logger.info("swift_merge folder:" + FilterUtils.escape(swift_mergeFolder));
			(new File(FilterUtils.filter(swift_mergeFolder))).mkdirs();

		} catch (NullPointerException e) {
			/// NullPointerException for File 在path 為 null 時的錯誤
			logger.error("failed to load env propertites file path:" + e.getMessage());
		} catch (Exception e) {
			logger.error("failed to load env :" + e.getMessage());
		} finally {
			SafeClose.close(fis);
		}

	}

	public static boolean isWindows() {
		logger.info("isWindows:" + FilterUtils.escape(System.getProperty("os.name")));
		return System.getProperty("os.name").startsWith("Windows");
	}

	private static void readPropToMap(String prompt, String fileName, HashMap<String, String> map) {
		String path = getPropsFilePath(fileName);
		logger.info(FilterUtils.escape("read prop for " + prompt + " from:" + path));

		PoorManFile poor = new PoorManFile(path);
		String s;
		try {
			s = poor.read();
			String[] ss = s.split("\r\n");
			for (int i = 0; i < ss.length; i++) {
				if (ss[i].length() > 0 && ss[i].charAt(0) != ';') {
					int j = ss[i].indexOf("=");
					if (j > 0) {
						String k = ss[i].substring(0, j);
						String v = ss[i].substring(j + 1);
						logger.info(FilterUtils.escape(k + "=" + v));
						map.put(k, v);
					}
				}
			}
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}

	}

	public static void generateJS(String webRealPath) throws Exception {
		generateHelpJS(webRealPath);
		try {
			generateErrorCodeJS(webRealPath);
			excelfourCodetoMap();
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
	}

	private static void excelfourCodetoMap() throws Exception {
		String codeFile = getPropsFilePath(fourcofile);// "Four-Corner.xls"
		logger.info(FilterUtils.escape("generating excel four Corner to Map:" + codeFile));
		if (StringUtils.isEmpty(fourcofile)) {
			logger.warn("in getPropsFilePath, the fourCode file is null path:" + FilterUtils.escape(fourcofile));
			return;
		}
		FileInputStream fis = null;
		POIFSFileSystem fs = null;
		HSSFWorkbook book = null;

		try {
			fis = new FileInputStream(FilterUtils.filter(codeFile));
			fs = new POIFSFileSystem(fis);
			book = new HSSFWorkbook(fs);
			HSSFSheet sheet = book.getSheetAt(0);

			HSSFCell cell = null;
			String firstcode = null;
			String value = null;
			HashMap<Integer, String> datacode = new HashMap<Integer, String>();
			HashMap<String, String> codedata = new HashMap<String, String>();
			for (int j = 1; j < sheet.getPhysicalNumberOfRows(); j++) {
				HSSFRow row = sheet.getRow(j);
				// 只限定四個台灣字
				for (int i = 0; i < 6; i++) {
					// 這欄無用 (大陸字)
					if (i == 1) {
						continue;
					}
					cell = row.getCell(i);
					// 有些最前面是 空白
					value = cell.toString().trim();
					if (value.isEmpty()) {
						break;
					}
					if (i == 0) {
						firstcode = value;
					} else {
						// 只取第一個字
						datacode.put(value.codePointAt(0), firstcode);
						// 四角號碼轉回中文 不重複
						if (!codedata.containsKey(firstcode)) {
							codedata.put(firstcode, value);
						}
						// logger.info("fourCode:" + value.codePointAt(0) + "," +
						// firstcode);
					}
					// System.out.println(cell.getContents());
				}
			}
			// 給全域變數
			fourcoCode = datacode;
			codeFourco = codedata;
			logger.info("fourcoCode size:" + fourcoCode.size());
			logger.info("codeFourco size:" + codeFourco.size());
		} catch (Exception e) {
			logger.info("fourCode error:" + e);
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} finally {
			SafeClose.close(book);
			SafeClose.close(fs);
			SafeClose.close(fis);
		}
	}

	private static void generateErrorCodeJS(String webRealPath) throws Exception {
		// String jsFile = webRealPath + "/script/help/errors.js";
		// String jsFile = webRealPath + "/help/errors.js";
		String jsFile = getPropsFilePath("errors.js");
		logger.info("generating error code js:" + FilterUtils.escape(jsFile));
		String s = PoorManUtil.mapToJSON(errorCoderMap);
		logger.info(FilterUtils.escape(s));
		PoorManFile poor = new PoorManFile(jsFile);

		poor.write("var _ifxErrorCode = " + s);

		PoorManFile poor2 = new PoorManFile(jsFile);
		String errorJS = poor2.read();
		MiniServlet.add("errors", errorJS);
	}

	private static void generateHelpJS(String webRealPath) throws Exception {
		// String helpJS = webRealPath + "/script/help/Help.js";
		// String helpJS = webRealPath + "/help/Help.js";
		String helpJS = getPropsFilePath("Help.js");
		// 柯: 應該沒用到，註解
		// String helpXls = getPropsFilePath("help.xls");
		logger.info("helpJS:" + FilterUtils.escape(helpJS));
		// logger.info("helpXls:" + helpXls);
		logger.info("no generate help.js");
		// try {
		//
		// HelpFileGen2.generate(helpXls, helpJS, "BIG5", false);
		// } catch (Exception e) {
		// StringWriter errors = new StringWriter();
		// e.printStackTrace(new PrintWriter(errors));
		// logger.error(errors.toString());
		// }
		logger.info("using help:" + FilterUtils.escape(helpJS));
		PoorManFile poor = new PoorManFile(helpJS);
		String content = poor.read();
		MiniServlet.add("help", content);

	}

	// public static void buildMenu() throws Exception {
	// String filePath = GlobalValues.getMenuPath();
	// menu = MenuBuilder.fromXml(filePath);
	// menuHtml = MenuBuilder.toHtml(menu);
	// logger.info(menuHtml);
	// }

	public static Menu getMenu() {
		return menu;
	}

	public static String getMenuHtml() {
		return menuHtml;
	}

	public static MenuItem getTranAuth(String txcode) {
		return menu.getByCode(txcode);
	}

	public static String getTranAuthAsJson(String txcode) {
		// StringWriter writer = new StringWriter();
		// ObjectMapper m = new ObjectMapper();
		MenuItem item = getTranAuth(txcode);
		return PoorManJson.toJson(item);
		//
		// try {
		// m.writeValue(writer, item);
		// } catch (JsonGenerationException e) {
		// StringWriter errors = new StringWriter();
		// e.printStackTrace(new PrintWriter(errors));
		// logger.error(errors.toString());
		// } catch (JsonMappingException e) {
		// StringWriter errors = new StringWriter();
		// e.printStackTrace(new PrintWriter(errors));
		// logger.error(errors.toString());
		// } catch (IOException e) {
		// StringWriter errors = new StringWriter();
		// e.printStackTrace(new PrintWriter(errors));
		// logger.error(errors.toString());
		// }
		// return writer.toString();
	}

	public static void loadApplicationProps() {
		InputStream inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
		Properties props = new Properties();
		try {
			props.load(inStream);
			props.list(System.out);
			applicationVersion = props.getProperty("application.version", "n/a");

		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} finally {
			SafeClose.close(inStream);
		}

	}

	public static String getHostLogFolder() {
		return hostLogFolder;
	}

	// 補送中鋼網銀表單檔案篩選規則
	static class CstellxmlFilter implements FilenameFilter {
		private String type;

		public CstellxmlFilter(String type) {
			this.type = type;
		}

		// 檔名固定第六碼為F，第八碼為1，附檔名為.xml(?????F?1*.xml)
		public boolean accept(File dir, String name) {
			return name.endsWith(type) && name.matches("^.....F.1.*");
		}
	}

	// 檔案篩選規則
	static class ftpFilter implements FilenameFilter {
		private String patten;

		public ftpFilter(String patten) {
			this.patten = patten;
		}

		public boolean accept(File dir, String name) {
			return name.matches(patten);
		}
	}

	public static String[] parsingIpportBytxcd(String txcd) {
		String ip = null;
		String port = null;
		logger.info("parsingIpportBytxcd:" + FilterUtils.escape(txcd));
		try {
			if (j_Ipport_txcd == null) {
				j_Ipport_txcd = new JSONObject(sna_Ipport_txcd);
			}
			if (!j_Ipport_txcd.getJSONObject("IPPORT").isNull(txcd)) {
				ip = j_Ipport_txcd.getJSONObject("IPPORT").getJSONArray(txcd).get(0).toString();
				logger.info("parsingIpportBytxcd ip:" + ip);
				port = j_Ipport_txcd.getJSONObject("IPPORT").getJSONArray(txcd).get(1).toString();
				logger.info("parsingIpportBytxcd port :" + port);
			}
			logger.info("parsingIpportBytxcd out");
		} catch (JSONException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}

		return new String[] { ip, port };
	}

}
