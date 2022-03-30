package com.st1.ifx.swiftauto;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.st1.ifx.domain.SwiftUnsolicitedMsg;
import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.hcomm.fmt.HostFormatter;
import com.st1.servlet.GlobalValues;
import com.st1.util.MySpring;
import com.st1.util.PoorManFile;
import com.st1.ifx.service.SwiftUnsoMsgService;

// TotaToPrn 不是thread-safe 所以不可以同時有兩個thread使用TotaToPrn(主因
// 是BicQuery時, 只有一組中心使用者ID, 主機序號必須累加 否則會有CE999錯誤)
// 未了避免控制上困擾加上系統也無須同時處理多份swift電文, 所以就一個一個來吧
// 使用spring integration要特別注意這點:
// swift電文 ---> socker listeer --->寫入特定目錄(folderX), 檔名: 日期+時間(yyyyMMddHHmmssSSS.swift)
// spring integration 掃描folderX, 得到一堆.swift檔案 (swiftFiles)
// foreach file in swiftFiles --> 
//    process TotaToPrn(file content)---> save to swift repos

@Component
@Scope("prototype")
public class TotaToPrn {
	static final Logger logger = LoggerFactory.getLogger(TotaToPrn.class);

	class PrnTag {
		String tag;
		String lines = "";
		String name = "[]";
		SwiftTag swiftTag;

		void append(String line) {
			if (this.lines.length() == 0)
				this.lines = line;
			else
				this.lines += (OUTPUT_NEWLINE + line);
		}

		@Override
		public String toString() {
			return "PrnTag [tag=" + tag + ", name=" + name + ", lines=" + lines + "]";
		}

	}

	private SwiftAutoConfig swiftAutoConfig;
	protected SwiftUnsoMsgService swiftUnsoMsgService = MySpring.getSwiftUnsoMsgService();

	@Autowired
	public void setSwiftAutoConfig(SwiftAutoConfig swiftAutoConfig) {
		this.swiftAutoConfig = swiftAutoConfig;
	}

	// static String SWIFT_NEWLINE = "\r\n";
	static String OUTPUT_NEWLINE = "\n";
	// 給平台判斷使用之檔案名稱 O-TYPE(1) O-TEMP(5)
	String swit_header = "";
	String file_name = "";
	// String mtName;
	MessageType mt;
	String swiftPart;
	String totaLabel;
	// for DB
	String brn;
	String dept;
	String ftbsdy;
	String msgtyp;
	String msgstatus_ck;
	String osn;
	String entSeq;
	// EOI 檔案NAME
	String refno;
	// HashMap<String, String> labelMap;
	Map<String, String> fieldMap;
	List<PrnTag> prnTagList;
	String linePrefix = "   ";
	private static final Map<String, String> cdkey = createCdkey();

	private static Map<String, String> createCdkey() {
		Map<String, String> myMap = new HashMap<String, String>();
		myMap.put("C", "Credit");
		myMap.put("D", "Debit");
		return myMap;
	}

	public void setLinePrefix(String linePrefix) {
		logger.info("setLinePrefix:" + linePrefix);
		this.linePrefix = linePrefix;
	}

	//
	// public static TotaToPrn fromTota(String mtName, String tota) {
	// TotaToPrn oPrn = new TotaToPrn();
	// oPrn.mtName = mtName;
	// oPrn.mtLines = tota;
	// return oPrn;
	// }
	public void fromTota(String tota) {
		logger.info("IN fromTota:" + tota);
		swit_header = tota.substring(0, 6);
		tota = tota.substring(6);
		// 在解析之前,先讀取去電 來電記號
		// O-TYPE(1) + O-TEMP(5)
		logger.info("swit_header:" + swit_header);
		// 1 SND , 2 RCV
		if (isReceive()) {
			file_name = "SWIFT-AUTO";
		} else if (isGoEoiReceive()) {
			file_name = "SWIFT-AUTOE";
		} else if (isGoEoiSand()) {
			file_name = "SWIFT-AUTOES";
		} else {
			file_name = "SWIFT-AUTOS";
		}

		logger.info("file_name:" + file_name);

		int index = tota.indexOf("{1:");
		this.totaLabel = tota.substring(0, index);
		// this.parseTotaLabel();
		// 依電文格式解析(非swift電文部分)
		this.parseTota(tota);
		// 中心下來是分開前面500,但沒關係,反正是用搜尋的 "{1:"
		this.swiftPart = tota.substring(index);
		// swift電文部分
		this.parseSwiftPart();

	}

	IncomingMessage incomingMessage;

	private void parseSwiftPart() {
		incomingMessage = new IncomingMessage(this.swiftPart);
		incomingMessage.parse();
		logger.info("mt:" + incomingMessage.getMt());
	}

	private void parseTota(String tota) {
		String totaFmtFile = file_name + ".tom";
		HostFormatter formatter = new HostFormatter(totaFmtFile);
		fieldMap = formatter.parse(false, tota);
	}

	// private void parseTotaLabel() {
	// // 5050026200053876DU0001001H52001074000 505020110104950
	// System.out.println(this.totaLabel);
	// labelMap = new HashMap<String, String>();
	// labelMap.put("KEY", totaLabel.substring(0, 16));
	// labelMap.put("UNKNOWN", totaLabel.substring(16, 40));
	// labelMap.put("BRNO", totaLabel.substring(40, 44));
	// labelMap.put("DATE", totaLabel.substring(44, 52));
	// labelMap.put("MTYPE", totaLabel.substring(52, 55));
	// System.out.printf("key:%s, brno:%s, date:%s, mt:%s\n",
	// labelMap.get("KEY"), labelMap.get("BRNO"),
	// labelMap.get("DATE"), labelMap.get("MTYPE"));
	// }

	public String generate() {
		return generate(GlobalValues.REPOSITORY_ROOT);
	}

	public String generate(String saveToFolder) {
		logger.info("IN saveToFolder:" + FilterUtils.escape(saveToFolder));
		normalizeSwiftTota();

		// 將swift電文與mtxxx.txt合併
		mergeWithMessageTypeDefine();
		// {{#SWIFT_TEXT_PRT}}: 轉成swift所需格式
		String report = notPrettyPrinting();
		fieldMap.put("SWIFT_TEXT_PRT", report);
		// 套PFNX格式, 儲存報表
		logger.info("saveToFolder:" + FilterUtils.escape(saveToFolder));
		return saveFile(saveToFolder, generatePFNX());

	}

	// 將fieldMap之fld填入PFNX
	private String generatePFNX() {
		String pfnxFile = com.st1.ifx.hcomm.fmt.Env.getFmtFolder() + file_name + ".PFNX";
		logger.info(FilterUtils.escape("pfnxFile:" + pfnxFile));
		try {
			String pfnx = (new PoorManFile(pfnxFile)).read();
			prepareFields();
			return formatPfnx(pfnx, fieldMap);
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			return "error generating PFNX: " + e.getMessage();
		}

	}

	// 電文欄位與PFNX欄位轉換
	private void prepareFields() {
		logger.info("do prepareFields!");
		this.brn = returnString(((String) fieldMap.get("BRNO")), true);
		this.ftbsdy = returnString((String) fieldMap.get("FTBSDY"), true);
		// String date = labelMap.get("DATE");
		this.msgtyp = returnString((String) fieldMap.get("MSGTYP"), true);

		// 統一osn
		String osnEntseq = "";
		if (isReceive()) {
			this.osn = returnString((String) fieldMap.get("OSN"), true);
			osnEntseq = this.osn;
		} else {
			this.entSeq = returnString((String) fieldMap.get("ENTSEQ"), true);
			osnEntseq = this.entSeq;
		}
		String keyName = String.format("%s-%s-%s-%s", this.brn, ftbsdy, this.msgtyp, osnEntseq);
		logger.info("keyName:" + keyName);
		fieldMap.put("KEYNAME", keyName);
		fieldMap.put("MSGTYPX", mt.name);
		// MSGPRX
		fieldMap.put("MSGPRX", getMsgPrx((String) fieldMap.get("MSGPR")));
		fieldMap.put("CHOICEX", getChoicex((String) fieldMap.get("CHOICE")));
		String pde = returnString((String) fieldMap.get("PDE"), false);
		String pdm = returnString((String) fieldMap.get("PDM"), false);
		String dupx = getDupx(pde, pdm);
		fieldMap.put("DUPX", dupx);
		String auth = returnString((String) fieldMap.get("AUTH"), false);
		fieldMap.put("AUTHX", getAuthx(auth));
		String fisc = returnString((String) fieldMap.get("FISC"), false);
		String nckind = returnString((String) fieldMap.get("NCKIND"), false);
		if (Strings.isNullOrEmpty(fisc)) {
			fisc = "";
		}
		fieldMap.put("FISCX", getFiscx(fisc, msgtyp));
		if (isReceive()) {
			fieldMap.put("QUEUENAME", getQueuename(returnString((String) fieldMap.get("RCVSTA"), false), true));
		} else {
			fieldMap.put("QUEUENAME", getQueuename(returnString((String) fieldMap.get("SNDSTA"), false), false));
			fieldMap.put("NCKINDX", getNckindx(nckind));
		}
		/** 取得for檔案名稱的編號 **/
		this.refno = returnString((String) fieldMap.get("REFNO"), true);

	}

	/** 判斷是否是要換個EOI路徑和檔名 **/
	private boolean isGoEoi() {
		if (swit_header.startsWith("3") || swit_header.startsWith("4")) {
			logger.info("GoEoi!");
			return true;
		}
		logger.info("not GoEoi!");
		return false;
	}

	private boolean isGoEoiSand() {
		if (swit_header.startsWith("3")) {
			logger.info("isGoEoiSand!");
			return true;
		}
		return false;
	}

	private boolean isGoEoiReceive() {
		if (swit_header.startsWith("4")) {
			logger.info("isGoEoiReceive!");
			return true;
		}
		return false;
	}

	/** 判斷是否是來電,去電 **/
	private boolean isReceive() {
		if (swit_header.startsWith("2") || swit_header.startsWith("4")) {
			logger.info("isReceive!");
			return true;
		}
		logger.info("not Receive!");
		return false;
	}

	/** 判斷是否是去電ack? nak? **/
	private boolean isNak() {
		String sndsta = (String) fieldMap.get("SNDSTA");
		if (!sndsta.isEmpty() && sndsta.equals("09")) {
			return true;
		}
		return false;
	}

	private String getChoicex(String Choice) {
		if (Choice.equals("1"))
			return "Original Printing";
		else if (Choice.equals("2"))
			return "****** Copy Printing 補印 ******";
		else
			return "";
	}

	private String getAuthx(String auth) {
		if (auth.equals("0"))
			return "Unknown";
		else if (auth.equals("1"))
			return "Success";
		else if (auth.equals("2"))
			return "Fail";
		else if (auth.equals("3"))
			return "Unknown";
		else
			return auth;
	}

	private String getNckindx(String nckind) {
		if (nckind.trim().equals("A")) {
			return "**SWIFT**";
		} else if (nckind.trim().equals("M")) {
			return "**M 人工**";
		} else {
			return "";
		}
	}

	private String getFiscx(String fisc, String msgtyp) {
		if (!fisc.equals("0") && !(msgtyp.trim().equals("104") || msgtyp.trim().equals("192") || msgtyp.trim().equals("198"))) {
			return "FIN Copy Service : TWP";
		} else {
			return "";
		}
	}

	private String getDupx(String pde, String pdm) {
		String result = "";
		if (pde.equals("1")) {
			result = "*****PDE注意電文可能重複，切勿重複解款或處理******";
		} else if (pdm.equals("1")) {
			result = "*****PDM注意電文可能重複，切勿重複解款或處理******";
		}
		return result;
	}

	private String getQueuename(String queuename, boolean isReceive) {
		Locale.setDefault(Locale.TRADITIONAL_CHINESE);
		if (queuename.equals("00")) {
			return "ALL";
		} else if (queuename.equals("01")) {
			if (isReceive) {
				return "已收妥之電文";
			}
			return "ALTER";
		} else if (queuename.equals("02")) {
			if (isReceive) {
				return "已被解檔之電文";
			}
			return "待VERIFY";
		} else if (queuename.equals("03")) {
			if (isReceive) {
				return "已成功轉入各業務主檔";
			}
			return "待RELEASE";
		} else if (queuename.equals("04")) {
			if (isReceive) {
				return "轉入各業務主檔失敗";
			}
			return "CANCEL";
		} else if (queuename.equals("05")) {
			if (isReceive) {
				return "已被轉送聯行";
			}
			return "";
		} else if (queuename.equals("06")) {
			if (isReceive) {
				return "被轉送來之電文";
			}
			return "";
		} else if (queuename.equals("07")) {
			return "待SWIFT回應";
		} else if (queuename.equals("08")) {
			return "SWIFT ACK";
		} else if (queuename.equals("09")) {
			if (isReceive) {
				return "無法自動分派";
			}
			return "SWIFT NAK";
		} else if (queuename.equals("10")) {
			if (isReceive) {
				return "尚未收妥之電文";
			}
			return "待轉TELEX";
		} else if (queuename.equals("11")) {
			if (isReceive) {
				return "尚未收妥無法分派之電文";
			}
			return "已轉TELEX";
		} else if (queuename.equals("12")) {
			if (isReceive) {
				return "無法自動分派之電文已被解檔";
			}
			return "";
		} else if (queuename.equals("13")) {
			if (isReceive) {
				return "不處理之電文";
			}
			return "AIR MAIL";
		} else if (queuename.equals("14")) {
			return "修改過的 AIR MAIL";
		} else if (queuename.equals("99")) {
			return "ERROR";
		} else {
			return "";
		}
	}

	private String getMsgPrx(String msgpr) {
		Locale.setDefault(Locale.TRADITIONAL_CHINESE);
		if (msgpr.isEmpty()) {
			return "";
		}
		msgpr = msgpr.trim().toUpperCase();
		String result = msgpr;
		if (msgpr.toUpperCase(Locale.TAIWAN).equals("U3")) {
			result = "Urgent Non-Delivery Warning and Delivery Notification";
		} else if (msgpr.toUpperCase(Locale.TAIWAN).equals("U1")) {
			result = "Urgent Non-Delivery Warning";
		} else if (msgpr.toUpperCase(Locale.TAIWAN).equals("N2")) {
			result = "Normal Delivery Notification";
		} else if (msgpr.toUpperCase(Locale.TAIWAN).equals("N")) {
			result = "Normal";
		}
		return result;
	}

	private String formatPfnx(String lines, Map<String, String> flds) {
		// /\{\{(#.+?)}{2}/g;
		logger.info("in formatPfnx.");
		Pattern pattern = Pattern.compile("\\{\\{(#.+?)}}");
		Matcher matcher = pattern.matcher(lines);
		while (matcher.find()) {
			// System.out.println(matcher.groupCount());
			// System.out.println(matcher.group(0));
			// System.out.println(matcher.group(1));
			// System.out.println("");
			String fullName = matcher.group(0);
			String name = matcher.group(1);
			String value = "";
			name = name.substring(1);
			if (flds.containsKey(name))
				value = (String) flds.get(name);
			else if (name.endsWith("!")) {
				value = getSpecialValue(name);
			} else {
				value = "?" + name + "?";
			}
			lines = lines.replace(fullName, value);
		}

		return lines;
	}

	// 特殊變數欄位 (小寫, 驚嘆號結尾, now!, date!)
	private String getSpecialValue(String name) {
		if (name.equalsIgnoreCase("now!")) {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			Calendar c1 = Calendar.getInstance(); // today
			return sdf.format(c1.getTime());
		} else if (name.equalsIgnoreCase("date!")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c1 = Calendar.getInstance(); // today
			return sdf.format(c1.getTime());
		}

		return "";
	}

	private String saveFile(String saveToFolder, String content) {
		logger.info("in saveFile!");
		String brnofile = this.brn;
		logger.info("saveFile brno:" + this.brn);
		this.dept = returnString((String) fieldMap.get("DEPT"), true);
		logger.info("saveFile dept:" + dept);
		// 只有1058要分科
		/*
		 * 1進口科 2出口科 3匯兌科 4會計科 5電信科 6國業科
		 */
		if (this.brn.equals("1058") && !this.dept.isEmpty() && !this.dept.equals("0")) {
			brnofile += this.dept;
			logger.info("brno change to:" + brnofile);
		}
		// String msgtyp = ((String) fieldMap.get("MSGTYP")).trim();
		// String brno = ((String) fieldMap.get("BRNO")).trim();
		String keyName = (String) fieldMap.get("KEYNAME");

		SimpleDateFormat sdf = new SimpleDateFormat("HHmmssSSS");
		String now = sdf.format(new Date());
		String filename = String.format("%s-%s.rptsf", keyName, now);

		// 建立"今天的目錄"
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		String today = sdf2.format(new Date());
		logger.info("saveToFolder:" + FilterUtils.escape(saveToFolder));
		logger.info("brno:" + brnofile);
		logger.info("today:" + today);
		String folder = combinePaths(saveToFolder, brnofile, today, "_swift");

		try {
			File todayFile = new File(folder);
			// 判斷是否存在"今日資料夾"
			// 不存在就搜尋全部該分行向下的全部未列印過電文稿(0.rptsf),並"移動"檔案到今日資料夾內
			if (!todayFile.exists()) {
				todayFile.mkdirs();
				todayFile.setExecutable(true, false);
				todayFile.setWritable(true, false);
				todayFile.setReadable(true, false);
				// SwiftRepository repos = new SwiftRepository();
				// List<File> notprintfiles = repos.getAllswiftFolder(brnofile, ".rptsf");
				// logger.info("Not print files size:"+notprintfiles.size());
				// for(File npf:notprintfiles){
				// logger.info("Nprint file:"+npf.getPath());
				// try {
				// FileUtils.moveFile(npf, new File(todayFile.getPath(), npf.getName()));
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// StringWriter errors = new StringWriter();
				// e.printStackTrace(new PrintWriter(errors));
				// logger.error(errors.toString());
				// }
				// }
				// 更新資料庫中未列印的電文稿 (換日期)
				logger.info("do updateNotprint!!!");
				swiftUnsoMsgService.updateNotprint();
				// swiftUnsoMsgService.updateNotprint(); 暫時 潘
			}
		} catch (SecurityException ex) {
			logger.error("SecurityException error!");
			logger.error(ex.getMessage());
		}
		// 轉到別的Eoi路徑,並調整檔名 (3 OR 4)
		// 電文種類 分行代號 發電日期 - 交易編號 .TXT
		// 範例：MT103105820151223-ALLH5W00001.TXT
		if (isGoEoi()) {
			folder = swiftAutoConfig.copytoEoiFolder;
			filename = "MT" + this.msgtyp + this.brn + this.ftbsdy + "-" + this.refno + ".txt";
		}

		logger.info("folder:" + FilterUtils.escape(folder));
		logger.info("filename:" + FilterUtils.escape(filename));

		// 報表檔案完整路徑
		String filePath = combinePaths(folder, filename);
		logger.info("filePath:" + FilterUtils.escape(filePath));

		try {
			File newTextFile = new File(filePath);
			FileUtils.writeStringToFile(newTextFile, content.toString(), "UTF-8");
			logger.info("content:" + FilterUtils.escape(content));
			logger.info("newTextFile.getPath:" + newTextFile.getPath());
			logger.info("after fileWriter.write.!");
			logger.info("swift report (auto-print) save to {}", filePath);

			if (!isGoEoi()) {
				// 存實體檔的時候另外存db2
				logger.info("begin swiftUnsoMsgService!" + this.msgtyp + "_" + this.msgstatus_ck);
				// 20170626 add msgStatus
				// 20180310 測試:先去db檢查是否有重複的資料
				swiftUnsoMsgService.save(SwiftUnsolicitedMsg.fromfile(brnofile, today, this.ftbsdy, this.msgtyp, this.osn, this.entSeq, filename, filePath, newTextFile.length(), this.msgstatus_ck));
				logger.info("after swiftUnsoMsgService!");
				String copyto = FilenameUtils.concat(swiftAutoConfig.copytoFolder, FilenameUtils.getPath(newTextFile.getPath()));
				logger.info("copyto " + copyto);
				File copytotmp = new File(copyto);
				FileUtils.copyFileToDirectory(newTextFile, copytotmp, true);
				copytotmp.setExecutable(true, false);
				copytotmp.setWritable(true, false);
				copytotmp.setReadable(true, false);
			}
			return filePath;
		} catch (IOException ex) {
			// System.err.println(ex.getMessage());
			logger.error("ex:" + ex.getMessage());
		} finally {
			logger.info("FileWriter finally!!!");
		}
		logger.info("End....!");
		return null;

	}

	// swift列印格式
	private String notPrettyPrinting() {
		logger.info("in notPrettyPrinting!");
		List<String> arr = new ArrayList<String>();
		// 有要列印這行?
		// arr.add("{4:");

		// 來電需要前面加入*** 記號
		if (isReceive()) {
			setLinePrefix("***");
			this.msgstatus_ck = "R";
		} else if (isNak()) { // NAK需加入前置記號
			setLinePrefix("NAK");
			this.msgstatus_ck = "N";
		} else {
			this.msgstatus_ck = "A";
		}

		for (PrnTag t : prnTagList) {
			// System.out.println(t);
			String x = ":" + t.tag + ":";
			if (t.swiftTag != null) {
				arr.add(String.format("%-5s%s", x, t.swiftTag.name));
			} else { // 程式跑到這邊應該是mtxxx.txt與來電電文版本不對 部分tag定義, 所以直接列印收到的電文內容
				arr.add(x);
			}
			arr.add(addSpaces(t.lines));
		}

		// 有要列印這行?
		// arr.add("-}");
		if (incomingMessage.resultMap.get("b5") != null) {
			arr.add(incomingMessage.resultMap.get("b5") + "  Checksum Trailer");
		} else {
			arr.add("  Checksum Trailer");
		}
		if (use_old_format) {
			arr.add(incomingMessage.resultMap.get("s"));
			arr.add(incomingMessage.resultMap.get("trn"));
		}
		StringBuilder sb = new StringBuilder();
		for (String s : arr) {
			sb.append(s).append(OUTPUT_NEWLINE); // 原本兩個,移除 + OUTPUT_NEWLINE
		}

		// 如果每一行都需要加前置字串
		if (linePrefix != null && linePrefix.length() > 0) {
			String[] ss = sb.toString().split(OUTPUT_NEWLINE);
			sb = new StringBuilder();
			for (String s : ss) {
				sb.append(linePrefix).append(s).append(OUTPUT_NEWLINE);
			}
		}

		logger.info("====================");
		logger.info(sb.toString());

		return sb.toString();
	}

	static String SPACES5 = "     "; // tag名稱之長度為5

	private String addSpaces(String lines) {
		String[] ss = lines.split(OUTPUT_NEWLINE);
		for (int i = 0; i < ss.length; i++) {
			ss[i] = SPACES5 + ss[i];
		}

		return myJoin(ss, OUTPUT_NEWLINE); // 原本兩個,移除 + OUTPUT_NEWLINE
	}

	public static String myJoin(String[] aArr, String sSep) {
		StringBuilder sbStr = new StringBuilder();
		for (int i = 0, il = aArr.length; i < il; i++) {
			if (i > 0)
				sbStr.append(sSep);
			sbStr.append(aArr[i]);
		}
		return sbStr.toString();
	}

	private void mergeWithMessageTypeDefine() {
		logger.info("IN mergeWithMessageTypeDefine");
		logger.info("swiftFolder: {}", FilterUtils.escape(GlobalValues.swiftFolder));
		String srcFileName = combinePaths(GlobalValues.swiftFolder, "mt" + incomingMessage.getMt() + ".txt");
		logger.info("srcFileName: {}", srcFileName);
		mt = MessageType.fromFile(srcFileName);

		// loop, get SwiftTag by tag id
		for (PrnTag t : prnTagList) {
			t.swiftTag = mt.findByTagName(t.tag);
		}

		// 柯:常駐程式同步與業務面相同(都印bic)
		// loop 2, process bic query
		processBicQuery();

		// loop 3, process customize format
		processCustFormat();

	}

	private void processCustFormat() {
		for (PrnTag t : prnTagList) {
			if (t.swiftTag != null) {
				if (t.swiftTag.formatFrom != null) // 此tag之內容需要特別處理
					t.lines = doFormat(t.swiftTag.formatFrom, t.lines);
			}
		}
	}

	private void processBicQuery() {
		for (PrnTag t : prnTagList) {
			if (t.swiftTag != null) {
				String bicCode = tryGetBic(t);
				if (bicCode != null && bicCode.length() > 0) {
					sendHost(t, bicCode);
					// sendHost(t, bicCode); 暫時 潘
				}
			}
		}
	}

	@Autowired
	BicQuery bicQuery;

	private void sendHost(PrnTag t, String bicCode) {
		logger.info("IN sendHost");
		logger.info("bicCode:" + bicCode);
		bicQuery.setBrno(swiftAutoConfig.bicQueryBrno);
		bicQuery.setTlrno(swiftAutoConfig.bicQueryTlrno);
		bicQuery.setMsgType(incomingMessage.getMt());
		bicQuery.setDest(bicCode);

		if (bicQuery.perform()) {
			// 查詢成功
			Map<String, String> m = bicQuery.getResultMap();
			String bkName1 = (String) m.get("BKNAME1");
			String bkName2 = (String) m.get("BKNAME2");
			String bkAddr1 = (String) m.get("BKADDR1");
			String bkAddr2 = (String) m.get("BKADDR2");
			// 目前還沒有列印格式, 就隨便依序亂擺吧
			// 移除 >>記號
			if (bkName1.trim().length() > 0) {
				t.append("**" + bkName1);
			}
			if (bkName2.trim().length() > 0) {
				t.append("**" + bkName2);
			}
			if (bkAddr1.trim().length() > 0) {
				t.append("**" + bkAddr1);
			}
			if (bkAddr2.trim().length() > 0) {
				t.append("**" + bkAddr2);
			}
		} // else 查詢失敗, 失敗就算了, 重傳問題更多
	}

	private String tryGetBic(PrnTag t) {
		String bic = null;
		String[] ss;

		String searchTag = t.tag + ","; // ## swift.bicquery.type.1,2,3 最後面一定要加逗號

		try {
			if ((swiftAutoConfig.bicQueryType1 + ",").indexOf(searchTag) != -1) {
				// bic在第一行, 如41A
				ss = t.lines.split(OUTPUT_NEWLINE);
				bic = ss[0];
			} else if ((swiftAutoConfig.bicQueryType2 + ",").indexOf(searchTag) != -1) {
				// bic在非/開頭那一行, 如5x家族
				ss = t.lines.split(OUTPUT_NEWLINE);
				for (String s : ss) {
					if (!s.startsWith("/")) {
						bic = s;
						break;
					}
				}
			} else if ((swiftAutoConfig.bicQueryType3 + ",").indexOf(searchTag) != -1) {
				// 字串最後一個/之後為BIC, 如94F, 94H, 95P
				int i = t.lines.lastIndexOf("/");
				if (i != -1) {
					bic = t.lines.substring(i + 1);
				}
			}
		} catch (Exception ex) {
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			bic = null;
		}
		return bic;
	}

	// TODO TEST!!
	private String moneyFormatter(String s) {
		logger.info("moneyFormatter:" + s);
		String sign = ","; // 柯:Swift的金額欄位是相反的
		String threesign = ".";
		String nStr = s;
		String x1;
		String x2;
		nStr += "";
		String[] x = nStr.split(sign);
		x1 = x[0];
		x2 = x.length > 1 ? sign + x[1] : "";
		String re = "(\\d+)(\\d{3}.*)";
		Pattern pattern = Pattern.compile(re);
		Matcher matcher = pattern.matcher(x1);
		while (matcher.find()) {
			x1 = matcher.group(1) + threesign + matcher.group(2);
			matcher = pattern.matcher(x1);
		}
		logger.info("moneyFormatter:x1" + x1);
		logger.info("moneyFormatter:x2" + x2);
		return x1 + x2;
	}
	// Tag 61 – Statement Line 6!n[4!n]2a[1!a]15d1!a3!c16x[//16x]

	private String doFormat(String formatName, String s) {
		if (formatName.equalsIgnoreCase("valueDate")) {
			return formatValueDate(s);
		} else if (formatName.equalsIgnoreCase("statementLine")) {
			// TODO change
			return formatStatementline(s);
		}
		return s;
	}

	// TODO 日期有需求時再優化 (swift-print.js)
	private String formatValueDate(String s) {
		logger.info("formatValueDate:" + s);
		List<String> list = new ArrayList<String>();
		if (s.charAt(0) == 'C' || s.charAt(0) == 'D') {
			list.add(String.format("%s%s", "Debit/Credit           :  ", cdkey.get(s.substring(0, 1))));
			s = s.substring(1);
		}
		String date = s.substring(0, 6);
		String cur = s.substring(6, 9);
		String amt = s.substring(9);
		amt = moneyFormatter(amt);

		list.add(String.format("%s%s", "Date                   :  ", date));
		list.add(String.format("%s%s(%s)", "Currency               :  ", cur + "   ", SwiftCur.getMoneyName(cur)));
		list.add(String.format("%s#%s#", "Amount                 :  ", amt));
		list.add(s);
		return myJoin(list.toArray(new String[0]), OUTPUT_NEWLINE);
	}

	private String formatStatementline(String s) {
		logger.info("formatStatementline:" + s);
		List<String> list = new ArrayList<String>();
		String laststr = "";
		String vdate = "";
		String entry = "";
		String mark = "";
		String mount = "";
		String funds = "";
		String ident = "";
		String customerReference = "";
		String bankReference = "";

		vdate = s.substring(0, 6); // 6!n Value Date (YYMMDD)
		laststr = s.substring(6);
		logger.info("laststr:" + laststr);
		if (Character.isDigit(laststr.charAt(0))) {
			entry = laststr.substring(0, 4); // [4!n] Entry Date (MMDD)
			laststr = laststr.substring(4);
		}
		// 2a Debit/Credit Mark
		String ckmarkone = laststr.substring(0, 1).toUpperCase();
		String ckmarktwo = laststr.substring(0, 2).toUpperCase();
		if (ckmarktwo.toUpperCase(Locale.TAIWAN).equals("RC") || ckmarktwo.toUpperCase(Locale.TAIWAN).equals("RD")) {
			mark = ckmarktwo;
			laststr = laststr.substring(2);
		} else if (ckmarkone.toUpperCase(Locale.TAIWAN).equals("C") || ckmarkone.toUpperCase(Locale.TAIWAN).equals("D")) {
			mark = ckmarkone;
			laststr = laststr.substring(1);
		}

		// [1!a] Funds Code (3rd character of the currency code, if needed)
		if (!Character.isDigit(laststr.charAt(0))) {
			funds = Character.toString(laststr.charAt(0));
			laststr = laststr.substring(1);
		}

		String[] tmpmoney = untilEnglish(laststr);
		// 15d Amount
		mount = tmpmoney[0];
		mount = moneyFormatter(mount);
		// 1!a3!c Transaction Type Identification Code
		ident = tmpmoney[1].substring(0, 4);
		// Reference
		laststr = tmpmoney[1].substring(4);
		String[] tmpline = laststr.split(OUTPUT_NEWLINE);
		String[] tmptext = tmpline[0].split("//");
		// 16x Customer Reference
		customerReference = tmptext[0];
		// [//16x] Bank Reference
		if (tmptext.length > 1) {
			// FIRST LINE
			bankReference = tmptext[1];
		}
		list.add(String.format("%6s%s%4s%s%2s%s%4s%s#%s#", vdate, "   ", entry, "   ", mark, "   ", ident, "   ", mount));
		// list.add(String.format("%s%6s%s%4s", "Value : ", vdate," Entry : ",entry));
		// list.add(String.format("%s%2s%s%4s%s#%s#", "Debit/Credit : ", mark," Code :
		// ",ident," Amount : ", mount));
		list.add(String.format("%s%s", "Customer Reference : ", customerReference));
		list.add(String.format("%s%s", "Bank Reference     : ", bankReference));
		// all string
		// it's work
		if (tmpline.length > 1) {
			list.add(tmpline[1]);
		}
		return myJoin(list.toArray(new String[0]), OUTPUT_NEWLINE);
	}

	private String[] untilEnglish(String s) {
		String rtnstr = "";
		int indexi;
		for (indexi = 0; indexi < s.length(); indexi++) {
			char c = s.charAt(indexi);
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
				break;
			} else {
				rtnstr += c;
				continue;
			}
		}
		return new String[] { rtnstr, s.substring(indexi) };
	}

	private void normalizeSwiftTota() {
		prnTagList = new ArrayList<TotaToPrn.PrnTag>();
		String patternStr = "^:(\\d{2,3}[A-Z]?):"; // two or three digits and at most one
		// [A-Z]
		Pattern pattern = Pattern.compile(patternStr);
		PrnTag prnTag = null;

		String[] lines = getBlock4();
		String s;
		for (int i = 1; i < lines.length - 1; i++) { // we don't need first line
			// and last line
			s = lines[i];
			Matcher matcher = pattern.matcher(s);
			if (matcher.find()) { // match =>:32A:a long line
				prnTag = new PrnTag();
				prnTagList.add(prnTag);
				prnTag.tag = matcher.group(1); // group(1) => 32A
				prnTag.append(s.substring(matcher.group(0).length())); // group(0)=>
				// :32A:
			} else {
				// 目前swift之後續行
				if (prnTag != null)
					prnTag.append(s);
			}
		}
	}

	static final boolean use_old_format = false;

	private String[] getBlock4() {
		logger.info("getBlock4.use_old_format:" + use_old_format);
		String msgtyp = incomingMessage.getMt();
		logger.info("incomingMessage getMt():" + msgtyp);

		String swiftB4 = "";

		if (use_old_format) {
			swiftB4 = incomingMessage.resultMap.get("b4.1");
		} else {
			// new format
			swiftB4 = incomingMessage.resultMap.get("b4.0");
			swiftB4 = swiftB4.replaceAll("\r\n", "##");
		}
		// 電文代號是0開頭的 則把 {與}取代
		if (msgtyp.startsWith("0")) {
			logger.info("msgtyp go { and } logic!");
			// 與端末系統統一
			String headtemp = swiftB4.substring(0, 3);
			swiftB4 = swiftB4.substring(3, swiftB4.length());
			// logger.info("swiftB4 temp1->"+swiftB4);
			swiftB4 = swiftB4.replaceAll("\\{", "##:");
			swiftB4 = swiftB4.replaceAll("\\}", "");
			// logger.info("swiftB4 temp2->"+swiftB4);
			swiftB4 = headtemp + swiftB4;
			// logger.info("swiftB4 temp3->"+swiftB4);
			swiftB4 += "##}";
		}
		logger.info("swiftB4 temp4->" + swiftB4);
		return swiftB4.split("##");
		// return swiftB4.split(OUTPUT_NEWLINE);

	}

	// Pattern pattern;
	//
	// private void compilePattern() {
	// String patternStr = "^:(\\d{2}([A-Z])?):";
	// pattern = Pattern.compile(patternStr);
	// }

	// private void parseTotaTag(String s, PrnTag prnTag) {
	// Matcher matcher = pattern.matcher(s);
	// if (matcher.find()) {
	// System.out.println("\t" + matcher.group(0));
	// System.out.println("\t" + matcher.group(1));
	// } else {
	// }
	// }
	public static String returnString(String str, boolean trim) {
		if (Strings.isNullOrEmpty(str)) {
			return "";
		}
		if (trim) {
			return str.trim();
		}
		return str;
	}

	public static String combinePaths(String... paths) {
		if (paths.length == 0) {
			return "";
		}
		File combined = new File(FilterUtils.filter(paths[0]));
		int i = 1;
		while (i < paths.length) {
			combined = new File(combined, FilterUtils.filter(paths[i]));
			++i;
		}
		logger.info("combinePaths:" + combined.getPath());
		return combined.getPath();
	}
}
