
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.common.primitives.Ints;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
//import com.itextpdf.text.BaseColor;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.Image;
//import com.itextpdf.text.PageSize;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.Rectangle;
//import com.itextpdf.text.pdf.BaseFont;
//import com.itextpdf.text.pdf.ColumnText;
//import com.itextpdf.text.pdf.PdfArray;
//import com.itextpdf.text.pdf.PdfContentByte;
//import com.itextpdf.text.pdf.PdfDictionary;
//import com.itextpdf.text.pdf.PdfImportedPage;
//import com.itextpdf.text.pdf.PdfName;
//import com.itextpdf.text.pdf.PdfObject;
//import com.itextpdf.text.pdf.PdfReader;
//import com.itextpdf.text.pdf.PdfStamper;
//import com.itextpdf.text.pdf.PdfWriter;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BankRemit;
import com.st1.itx.db.domain.BankRemitId;
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.dump.HexDump;
import com.st1.itx.util.format.ConvertUpMoney;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.format.HostFormatter;
import com.st1.itx.util.mail.MailService;
import com.st1.itx.util.parse.Parse;

import oracle.sql.DATE;

@SuppressWarnings("unused")
public class test {
	public static int b = 0;

	private static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

	private static String countLen(String s) {
		int len = 0;
		for (int i = 0; i < s.length(); ++i) {
			if (isChinese(s.charAt(i)) || !IsPrintableAsciiChar(s.charAt(i))) {
				len = len + 2;

//				System.out.println(s.charAt(i));
			} else {
				len++;
//				System.out.println(s.charAt(i));
			}
		}
		return len + "";
	}

	private static boolean IsPrintableAsciiChar(char ch) {
		if (32 <= ch && ch <= 126)
			return true;
		return false;
	}

	private static void tt(int a, String... ib) {
		System.out.println(a++);
	}

	private static void cpRate(int igal, int iamt) {
		BigDecimal gal = new BigDecimal(igal);
		BigDecimal amt = new BigDecimal(iamt);
		if (igal == 0) {
			;
		} else {
			amt = amt.multiply(new BigDecimal(1000));
			amt = amt.divide(gal, 1, 1);
			System.out.print(amt);
		}
	}

	private static String asciiToHex(String asciiStr) {
		char[] chars = asciiStr.toCharArray();
		StringBuilder hex = new StringBuilder();
		for (char ch : chars) {
			hex.append(Integer.toHexString((int) ch));
		}
		return hex.toString();
	}

	// 1122轉{0x11,0x12}
	public static byte[] hexString2Bytes(String s) {
		byte[] bytes;
		bytes = new byte[s.length() / 2];

		for (int i = 0; i < bytes.length; i++) {
			// 十六轉十進制
			bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
		}

		return bytes;
	}

	// {0x11,0x12}轉1122
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		char[] buffer = new char[2];
		for (int i = 0; i < src.length; i++) {
			buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
			buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
			stringBuilder.append(buffer);
		}
		return stringBuilder.toString();
	}

	public static byte[] charToByte(char c) {
		byte[] b = new byte[2];
		b[0] = (byte) ((c & 0xFF00) >> 8);
		b[1] = (byte) (c & 0xFF);
		return b;
	}

	public static void main(String[] args) throws InterruptedException, IOException, NoSuchMethodException, ScriptException, LogicException {

		byte[] msg1 = "010010001IQ001p00000101000100000074 0000000000000000000000~#0000000000000000000000000000000000                         ".getBytes("CP937");
		byte[] msg2 = { 0x0f, 0x0f, 0x0f };
		byte[] msg3 = hexString2Bytes(String.format("%06d", msg1.length));
		byte[] msg4 = { 0x01 };
		byte[] msg5 = hexString2Bytes(String.format("%06d", 0));
		byte[] msg6 = { 0x0f };
		byte[] msg7 = { 0x0f };

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(msg2);
		baos.write(msg3);
		baos.write(msg4);
		baos.write(msg5);
		baos.write(msg6);
		baos.write(msg7);
		baos.write(msg1);
		byte[] c = baos.toByteArray();
		System.out.println(HexDump.dumpHexString("".getBytes("UTF-8")));
		System.out.println("123456".substring(0, 5));
		System.out.println(countLen(""));
		System.out.println(new String("".getBytes("UTF-8"), "UTF-8"));
		String s = new String("".getBytes("UTF-8"), "UTF-8");
		byte[] n = { (byte) 0xB1, (byte) 0x69 };
		System.out.println(HexDump.dumpHexString("張".getBytes("UNICODE")));
		System.out.println(new String(n, "BIG5"));

		System.out.println(
				"00000000  000C310                      000000000000000000000~#000000000000000000000000000012340000                                                    496423      ".substring(90, 94));

//		.replaceAll("{", "{\"").replaceAll(",", "\",\"").replaceAll(":", "\":\"").replaceAll("}\",\"{", ",")
//		MailService mail = new MailService();
//		mail.init();
//		mail.setParams("AdamTest", "gto0410124@msn.com", "doom167.abc@msa.hinet.net", "測試頭", "測試身體");
//		mail.exec();

//		System.out.println(p.IntegerToString(3, 8));

//		System.arraycopy(s3, 0, s2, s2.length - 2, 2);

//		System.out.println(isChinese(c[0]));

//		System.out.println(parse.IntegerToSqlDateO(new DateUtil().getNowIntegerForBC(), new DateUtil().getNowIntegerTime()));
//
//		System.out.println(new DateUtil().getNowIntegerForBC());
//		DateUtil dateUtil = new DateUtil();
//		dateUtil.setDate_1(1081205);
//		dateUtil.setDate_2(1281120);
//		dateUtil.dateDiffSp();
//
//		System.out.println(dateUtil.getYears());
//		System.out.println(dateUtil.getMons());
//		System.out.println(dateUtil.getDays());

//      System.out.println("1234567890".substring(0,5));
//		String s = "[{\"sendTo\":\"iFX\",\"MSGLEN\":\"\",\"BRNO\":\"\",\"TLRNO\":\"\",\"TXTNO\":\"00000000\",\"CALDY\":\"20191022\",\"CALTM\":\"183750\",\"MSGEND\":\"\",\"TXRSUT\":\"S\",\"MSGID\":\"XT95J\",\"MLDRY\":\"\",\"MRKEY\":\"\",\"FILLER\":\"\",\"occursList\":[{\"ODEFFLG\":\"N\",\"OISIN\":\"20151012TEST\",\"OISINNM\":\"20151012TEST 5DAYS                 \",\"OBDTYPE\":\"我B\",\"OCURCD\":\"1\",\"OISKPID\":\"G00007     \",\"OISPRICE\":\"100.0\",\"OCPRATE\":\"5.0\",\"OFXFL\":\"1\",\"OCALMT\":\"1\",\"ORATING1\":\"BB+  \",\"ORATING2\":\"     \",\"ORATING3\":\"     \",\"OISEDY\":\"20140510\",\"OMATDY\":\"20150529\",\"ODRPAMT\":\"3000000.0\",\"ODRPAMT_USD\":\"0\",\"OTAXATION\":\" \",\"OLINTDY\":\"20150710\",\"ONINTDY\":\"20150810\"},{\"ODEFFLG\":\"Y\",\"OISIN\":\"31395BJB0   \",\"OISINNM\":\"DD0007                             \",\"OBDTYPE\":\"11\",\"OCURCD\":\"1\",\"OISKPID\":\"080105     \",\"OISPRICE\":\"120.0\",\"OCPRATE\":\"6.25\",\"OFXFL\":\"1\",\"OCALMT\":\"1\",\"ORATING1\":\"AAA  \",\"ORATING2\":\"AA   \",\"ORATING3\":\"AA   \",\"OISEDY\":\"20140310\",\"OMATDY\":\"0\",\"ODRPAMT\":\"1.1E7\",\"ODRPAMT_USD\":\"0\",\"OTAXATION\":\" \",\"OLINTDY\":\"20150610\",\"ONINTDY\":\"20150910\"},{\"ODEFFLG\":\" \",\"OISIN\":\"APAN01234567\",\"OISINNM\":\"APAN01234567YY                     \",\"OBDTYPE\":\"12\",\"OCURCD\":\"1\",\"OISKPID\":\"G00007     \",\"OISPRICE\":\"99.26352\",\"OCPRATE\":\"2.25\",\"OFXFL\":\"1\",\"OCALMT\":\"4\",\"ORATING1\":\"     \",\"ORATING2\":\"     \",\"ORATING3\":\"     \",\"OISEDY\":\"20160215\",\"OMATDY\":\"20180228\",\"ODRPAMT\":\"2000000.0\",\"ODRPAMT_USD\":\"0\",\"OTAXATION\":\" \",\"OLINTDY\":\"20170815\",\"ONINTDY\":\"20180215\"},{\"ODEFFLG\":\"N\",\"OISIN\":\"APANCAD     \",\"OISINNM\":\"APANCAD TEST                       \",\"OBDTYPE\":\"12\",\"OCURCD\":\"9\",\"OISKPID\":\"C001111    \",\"OISPRICE\":\"98.852\",\"OCPRATE\":\"2.25\",\"OFXFL\":\"1\",\"OCALMT\":\"5\",\"ORATING1\":\"     \",\"ORATING2\":\"     \",\"ORATING3\":\"     \",\"OISEDY\":\"20140630\",\"OMATDY\":\"0\",\"ODRPAMT\":\"0.0\",\"ODRPAMT_USD\":\"0\",\"OTAXATION\":\" \",\"OLINTDY\":\"20150728\",\"ONINTDY\":\"20160128\"},{\"ODEFFLG\":\"N\",\"OISIN\":\"APANCNY     \",\"OISINNM\":\"APANCNY                            \",\"OBDTYPE\":\"12\",\"OCURCD\":\"30\",\"OISKPID\":\"G00017     \",\"OISPRICE\":\"102.0\",\"OCPRATE\":\"3.0\",\"OFXFL\":\"1\",\"OCALMT\":\"1\",\"ORATING1\":\"     \",\"ORATING2\":\"     \",\"ORATING3\":\"     \",\"OISEDY\":\"20150701\",\"OMATDY\":\"20161130\",\"ODRPAMT\":\"2000000.0\",\"ODRPAMT_USD\":\"0\",\"OTAXATION\":\" \",\"OLINTDY\":\"20161001\",\"ONINTDY\":\"20170101\"},{\"ODEFFLG\":\" \",\"OISIN\":\"APANTEST    \",\"OISINNM\":\"APANTEST                           \",\"OBDTYPE\":\"11\",\"OCURCD\":\"1\",\"OISKPID\":\"C001111    \",\"OISPRICE\":\"100.0\",\"OCPRATE\":\"2.875\",\"OFXFL\":\"2\",\"OCALMT\":\"1\",\"ORATING1\":\"     \",\"ORATING2\":\"Aa   \",\"ORATING3\":\"AA+  \",\"OISEDY\":\"20160327\",\"OMATDY\":\"20170331\",\"ODRPAMT\":\"1.013E7\",\"ODRPAMT_USD\":\"0\",\"OTAXATION\":\" \",\"OLINTDY\":\"20160327\",\"ONINTDY\":\"20170327\"},{\"ODEFFLG\":\" \",\"OISIN\":\"APANTESTAUD \",\"OISINNM\":\"AUSTRALIA GOVIE 4.75 2027/04/21    \",\"OBDTYPE\":\"12\",\"OCURCD\":\"5\",\"OISKPID\":\"G00014     \",\"OISPRICE\":\"98.598\",\"OCPRATE\":\"4.75\",\"OFXFL\":\"1\",\"OCALMT\":\"6\",\"ORATING1\":\"     \",\"ORATING2\":\"     \",\"ORATING3\":\"     \",\"OISEDY\":\"20111021\",\"OMATDY\":\"20161230\",\"ODRPAMT\":\"100000.0\",\"ODRPAMT_USD\":\"0\",\"OTAXATION\":\" \",\"OLINTDY\":\"20161021\",\"ONINTDY\":\"20170421\"},{\"ODEFFLG\":\" \",\"OISIN\":\"APANTESTU   \",\"OISINNM\":\"APANTESTU123                       \",\"OBDTYPE\":\"12\",\"OCURCD\":\"1\",\"OISKPID\":\"G00014     \",\"OISPRICE\":\"102.0\",\"OCPRATE\":\"2.0\",\"OFXFL\":\"1\",\"OCALMT\":\"1\",\"ORATING1\":\"     \",\"ORATING2\":\"     \",\"ORATING3\":\"     \",\"OISEDY\":\"20170306\",\"OMATDY\":\"20170531\",\"ODRPAMT\":\"50000.0\",\"ODRPAMT_USD\":\"0\",\"OTAXATION\":\" \",\"OLINTDY\":\"20170406\",\"ONINTDY\":\"20170506\"},{\"ODEFFLG\":\"N\",\"OISIN\":\"DTEST001    \",\"OISINNM\":\"DD00001                            \",\"OBDTYPE\":\"14\",\"OCURCD\":\"1\",\"OISKPID\":\"080106     \",\"OISPRICE\":\"100.0\",\"OCPRATE\":\"5.0\",\"OFXFL\":\"1\",\"OCALMT\":\"1\",\"ORATING1\":\"     \",\"ORATING2\":\"AAAAA\",\"ORATING3\":\"     \",\"OISEDY\":\"20140324\",\"OMATDY\":\"0\",\"ODRPAMT\":\"5.33E8\",\"ODRPAMT_USD\":\"0\",\"OTAXATION\":\" \",\"OLINTDY\":\"20150424\",\"ONINTDY\":\"20150724\"},{\"ODEFFLG\":\"N\",\"OISIN\":\"DTEST002    \",\"OISINNM\":\"DD00002                            \",\"OBDTYPE\":\"11\",\"OCURCD\":\"2\",\"OISKPID\":\"080401     \",\"OISPRICE\":\"100.0\",\"OCPRATE\":\"2.0\",\"OFXFL\":\"2\",\"OCALMT\":\"5\",\"ORATING1\":\"     \",\"ORATING2\":\"     \",\"ORATING3\":\"     \",\"OISEDY\":\"20140324\",\"OMATDY\":\"0\",\"ODRPAMT\":\"2.7E7\",\"ODRPAMT_USD\":\"0\",\"OTAXATION\":\" \",\"OLINTDY\":\"20150624\",\"ONINTDY\":\"20150924\"},{\"ODEFFLG\":\"N\",\"OISIN\":\"DTEST003    \",\"OISINNM\":\"DD00003                            \",\"OBDTYPE\":\"13\",\"OCURCD\":\"1\",\"OISKPID\":\"080105     \",\"OISPRICE\":\"110.0\",\"OCPRATE\":\"7.5\",\"OFXFL\":\"1\",\"OCALMT\":\"1\",\"ORATING1\":\"     \",\"ORATING2\":\"     \",\"ORATING3\":\"     \",\"OISEDY\":\"20150326\",\"OMATDY\":\"0\",\"ODRPAMT\":\"-2.77E7\",\"ODRPAMT_USD\":\"0\",\"OTAXATION\":\" \",\"OLINTDY\":\"20150626\",\"ONINTDY\":\"20150926\"},{\"ODEFFLG\":\"N\",\"OISIN\":\"DTEST004    \",\"OISINNM\":\"DD0004                             \",\"OBDTYPE\":\"15\",\"OCURCD\":\"1\",\"OISKPID\":\"080106     \",\"OISPRICE\":\"120.0\",\"OCPRATE\":\"5.0\",\"OFXFL\":\"1\",\"OCALMT\":\"2\",\"ORATING1\":\"     \",\"ORATING2\":\"     \",\"ORATING3\":\"     \",\"OISEDY\":\"20150305\",\"OMATDY\":\"0\",\"ODRPAMT\":\"1000000.0\",\"ODRPAMT_USD\":\"0\",\"OTAXATION\":\" \",\"OLINTDY\":\"20150605\",\"ONINTDY\":\"20150905\"},{\"ODEFFLG\":\"N\",\"OISIN\":\"DTEST005    \",\"OISINNM\":\"DD0005                             \",\"OBDTYPE\":\"14\",\"OCURCD\":\"14\",\"OISKPID\":\"080105     \",\"OISPRICE\":\"100.0\",\"OCPRATE\":\"4.0\",\"OFXFL\":\"1\",\"OCALMT\":\"1\",\"ORATING1\":\"A    \",\"ORATING2\":\"A    \",\"ORATING3\":\"AA   \",\"OISEDY\":\"20150310\",\"OMATDY\":\"0\",\"ODRPAMT\":\"5.2E7\",\"ODRPAMT_USD\":\"0\",\"OTAXATION\":\" \",\"OLINTDY\":\"20150610\",\"ONINTDY\":\"20150910\"},{\"ODEFFLG\":\"Y\",\"OISIN\":\"DTEST006    \",\"OISINNM\":\"DD0006                             \",\"OBDTYPE\":\"11\",\"OCURCD\":\"14\",\"OISKPID\":\"080105     \",\"OISPRICE\":\"100.0\",\"OCPRATE\":\"4.5\",\"OFXFL\":\"1\",\"OCALMT\":\"4\",\"ORATING1\":\"A    \",\"ORATING2\":\"A    \",\"ORATING3\":\"A    \",\"OISEDY\":\"20150310\",\"OMATDY\":\"20150630\",\"ODRPAMT\":\"4.6E7\",\"ODRPAMT_USD\":\"0\",\"OTAXATION\":\" \",\"OLINTDY\":\"20150610\",\"ONINTDY\":\"20150910\"},{\"ODEFFLG\":\"N\",\"OISIN\":\"DTEST007    \",\"OISINNM\":\"DD0007                             \",\"OBDTYPE\":\"11\",\"OCURCD\":\"14\",\"OISKPID\":\"080105     \",\"OISPRICE\":\"120.0\",\"OCPRATE\":\"5.1\",\"OFXFL\":\"1\",\"OCALMT\":\"1\",\"ORATING1\":\"AA   \",\"ORATING2\":\"A    \",\"ORATING3\":\"A    \",\"OISEDY\":\"20150310\",\"OMATDY\":\"20150630\",\"ODRPAMT\":\"7.0E7\",\"ODRPAMT_USD\":\"0\",\"OTAXATION\":\" \",\"OLINTDY\":\"20150610\",\"ONINTDY\":\"20150910\"},{\"ODEFFLG\":\"Y\",\"OISIN\":\"DTEST008    \",\"OISINNM\":\"DD00008                            \",\"OBDTYPE\":\"14\",\"OCURCD\":\"1\",\"OISKPID\":\"080105     \",\"OISPRICE\":\"100.0\",\"OCPRATE\":\"5.0\",\"OFXFL\":\"1\",\"OCALMT\":\"1\",\"ORATING1\":\"AAA  \",\"ORATING2\":\"AA   \",\"ORATING3\":\"AA   \",\"OISEDY\":\"20150408\",\"OMATDY\":\"0\",\"ODRPAMT\":\"1.685E8\",\"ODRPAMT_USD\":\"0\",\"OTAXATION\":\" \",\"OLINTDY\":\"20150708\",\"ONINTDY\":\"20151008\"},{\"ODEFFLG\":\"Y\",\"OISIN\":\"DTEST013    \",\"OISINNM\":\"DD0013                             \",\"OBDTYPE\":\"14\",\"OCURCD\":\"1\",\"OISKPID\":\"080105     \",\"OISPRICE\":\"100.0\",\"OCPRATE\":\"5.0\",\"OFXFL\":\"1\",\"OCALMT\":\"1\",\"ORATING1\":\"AA   \",\"ORATING2\":\"A    \",\"ORATING3\":\"A    \",\"OISEDY\":\"20150428\",\"OMATDY\":\"20161031\",\"ODRPAMT\":\"4.9E7\",\"ODRPAMT_USD\":\"0\",\"OTAXATION\":\" \",\"OLINTDY\":\"20160728\",\"ONINTDY\":\"20161028\"},{\"ODEFFLG\":\" \",\"OISIN\":\"ISINTEST0001\",\"OISINNM\":\"ISINTEST0001 123                   \",\"OBDTYPE\":\"15\",\"OCURCD\":\"1\",\"OISKPID\":\"G00017     \",\"OISPRICE\":\"100.0\",\"OCPRATE\":\"3.2\",\"OFXFL\":\"1\",\"OCALMT\":\"1\",\"ORATING1\":\"     \",\"ORATING2\":\"     \",\"ORATING3\":\"     \",\"OISEDY\":\"20070418\",\"OMATDY\":\"20161130\",\"ODRPAMT\":\"1000000.0\",\"ODRPAMT_USD\":\"0\",\"OTAXATION\":\" \",\"OLINTDY\":\"20161018\",\"ONINTDY\":\"20170418\"},{\"ODEFFLG\":\"N\",\"OISIN\":\"TESTMEL001  \",\"OISINNM\":\"TEST CHG OVERNIGHT                 \",\"OBDTYPE\":\"15\",\"OCURCD\":\"1\",\"OISKPID\":\"080125     \",\"OISPRICE\":\"100.0\",\"OCPRATE\":\"1.0\",\"OFXFL\":\"1\",\"OCALMT\":\"1\",\"ORATING1\":\"AAA  \",\"ORATING2\":\"AAA  \",\"ORATING3\":\"AA   \",\"OISEDY\":\"20110131\",\"OMATDY\":\"0\",\"ODRPAMT\":\"7.0E7\",\"ODRPAMT_USD\":\"0\",\"OTAXATION\":\" \",\"OLINTDY\":\"20150430\",\"ONINTDY\":\"20150730\"},{\"ODEFFLG\":\"N\",\"OISIN\":\"TESTMEL002  \",\"OISINNM\":\"TEST 002                           \",\"OBDTYPE\":\"15\",\"OCURCD\":\"1\",\"OISKPID\":\"080105     \",\"OISPRICE\":\"100.0\",\"OCPRATE\":\"1.0\",\"OFXFL\":\"1\",\"OCALMT\":\"1\",\"ORATING1\":\"AAA  \",\"ORATING2\":\"AAA  \",\"ORATING3\":\"AAA  \",\"OISEDY\":\"20110331\",\"OMATDY\":\"0\",\"ODRPAMT\":\"0.0\",\"ODRPAMT_USD\":\"0\",\"OTAXATION\":\" \",\"OLINTDY\":\"20150630\",\"ONINTDY\":\"20150930\"},{\"ODEFFLG\":\"N\",\"OISIN\":\"US912828G385\",\"OISINNM\":\"US 10Y T_NOTE 2.25 2024/11/15      \",\"OBDTYPE\":\"12\",\"OCURCD\":\"1\",\"OISKPID\":\"G00007     \",\"OISPRICE\":\"100.0\",\"OCPRATE\":\"2.25\",\"OFXFL\":\"1\",\"OCALMT\":\"1\",\"ORATING1\":\"Aaa  \",\"ORATING2\":\"aaa  \",\"ORATING3\":\"     \",\"OISEDY\":\"20141127\",\"OMATDY\":\"20161130\",\"ODRPAMT\":\"1.33999E7\",\"ODRPAMT_USD\":\"0\",\"OTAXATION\":\" \",\"OLINTDY\":\"20160515\",\"ONINTDY\":\"20170515\"},{\"ODEFFLG\":\" \",\"OISIN\":\"XS0286541890\",\"OISINNM\":\"HSBC-COSMOS CDO                    \",\"OBDTYPE\":\"15\",\"OCURCD\":\"1\",\"OISKPID\":\"HSBCHKHH   \",\"OISPRICE\":\"100.0\",\"OCPRATE\":\"0.583838\",\"OFXFL\":\"2\",\"OCALMT\":\"1\",\"ORATING1\":\"AA   \",\"ORATING2\":\"     \",\"ORATING3\":\"     \",\"OISEDY\":\"20070301\",\"OMATDY\":\"20190628\",\"ODRPAMT\":\"5000000.0\",\"ODRPAMT_USD\":\"0\",\"OTAXATION\":\" \",\"OLINTDY\":\"20180620\",\"ONINTDY\":\"20180620\"},{\"ODEFFLG\":\" \",\"OISIN\":\"XXXXXXXXXXXX\",\"OISINNM\":\"                                   \",\"OBDTYPE\":\"  \",\"OCURCD\":\"0\",\"OISKPID\":\"           \",\"OISPRICE\":\"0.0\",\"OCPRATE\":\"0.0\",\"OFXFL\":\" \",\"OCALMT\":\" \",\"ORATING1\":\"     \",\"ORATING2\":\"     \",\"ORATING3\":\"     \",\"OISEDY\":\"0\",\"OMATDY\":\"0\",\"ODRPAMT\":\"0.0\",\"ODRPAMT_USD\":\"0\",\"OTAXATION\":\" \",\"OLINTDY\":\"0\",\"ONINTDY\":\"0\"}]}]";
//		String filename = "[XT95J]債券基本資料查詢{}_20191125183752.xls";
//		String regex = "[`~!@#$%^&*()\\+\\=\\{}|:\"?><【】\\/r\\/n]";
//		Pattern pa = Pattern.compile(regex);
//		Matcher ma = pa.matcher(filename);
//		if (ma.find()) {
//			System.out.println(ma);
//		    filename = ma.replaceAll("");
//		}
//		System.out.println(filename.substring(4));

//		s = "abcd~!@#$%^&*)_+<>?\":}{|\\][\';/.,=-\\]！＠＃＄％︿＆＊（）";
//		HashMap<String, String> result_tim = new ObjectMapper().readValue(s, HashMap.class);
//		for (int i = 0; i < s.length(); ++i) {
//			System.out.println("" + (isChinese(s.charAt(i))));
//		}
//		Gson gson = new Gson();
//		ArrayList<LinkedHashMap> listFromGson = gson.fromJson(s, new TypeToken<ArrayList<LinkedHashMap>>() {
//		}.getType());
//		HostFormatter a = new HostFormatter("XT95J.tom");
//		ArrayList<LinkedHashMap> tt = gson.fromJson(new ObjectMapper().writeValueAsString(listFromGson.get(0).get("occursList")), new TypeToken<ArrayList<LinkedHashMap>>() {
//		}.getType());

//		String ss = "莼蒓";
//		System.out.println(listFromGson.get(1));

//		for (LinkedHashMap b : tt)
//			System.out.println(a.format(false, b));
//		System.out.println(a.format(false, tt.get(0)));
//		Date date = new Date();
//		SimpleDateFormat ft = new SimpleDateFormat("HHmmss");
//		Integer.parseInt(ft.format(date));
//		System.out.println(Integer.parseInt(ft.format(date)));
//		ApplicationContext context = new ClassPathXmlApplicationContext("spring/root.xml");
//		((AbstractApplicationContext) context).close();
//		NettyServer socketServer = new NettyServer();
//		InetSocketAddress address = new InetSocketAddress("127.0.0.1", 55688);
//		ChannelFuture future = socketServer.run(address);
//		Runtime.getRuntime().addShutdownHook(new Thread(){
//            @Override
//            public void run() {
//                socketServer.destroy();
//            }
//        });
//        future.channel().closeFuture().syncUninterruptibly();

//		String path = "C:\\SKL\\test\\tt.txt";
//		String text = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
//		System.out.println(text.getBytes("BIG5").length);
//		ScriptEngineManager manager = new ScriptEngineManager();
//		ScriptEngine engine = manager.getEngineByName("js");
//		String js = text
//				+ "\nvar loop=false;var form=[];var fl={};function fields(){var b={};getTranDef().rtns.forEach(function(c){if(c.type==\"form\")form=form.concat(c.name);c.fields.forEach(function(a){if(null!=a._[2]){b[a._[0]]=a._[1].toString()+','+a._[2].toString()+(a._[3]==0?'':'.'+a._[3].toString());}if(loop){fl[c.name]=a._[0];loop=false;}if(\"#LOOP\"==a._[0]){loop=true}})});return b}function tim(){var b=fields(),c={};getTranDef().tim.list.forEach(function(a){null!=b[a]&&(c[a]=b[a])});return JSON.stringify(c)};function tom(){var b=fields(),c={},d={};form.forEach(function(a){getTranDef().tom[a].forEach(function(x){if(x==fl[a])c[\"#LOOP\"]=\"\";if(null!=b[x]){c[x]=b[x]}});d[a]=c});return JSON.stringify(d)};";
//		engine.eval(js);
//		Invocable invocable = (Invocable) engine;
//
//		Map<String, Integer> fieldsId = new HashMap<String, Integer>();
//
//		String fields = (String) invocable.invokeFunction("tim");
//		String tom = (String) invocable.invokeFunction("tom");
//
//		System.out.println(fields);
//		System.out.println(tom);
//		
//		WriteTiTo te = new WriteTiTo();
//		System.out.println(te.writeTom("XT95J"));

//		result.getHeadre().put("d","4");

//		HashMap<String, HashMap<String, String>> result_tom = new ObjectMapper().readValue(tom, HashMap.class);
//		System.out.println(result_tom);
//		for (Entry<String, HashMap<String, String>> s : result_tom.entrySet()) {
//			System.out.println(s.getKey());
//			for (Entry<String, String> x : s.getValue().entrySet()) {
//				System.out.println(x.getKey());
//				System.out.println(x.getValue());
//			}
//				
//		}
//
//		ArrayList<ArrayList> varRtn = new ArrayList();
//		for (Object b : result_tim) {
//			HashMap<String, ArrayList> c = new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(b), HashMap.class);
//			varRtn.add(c.get("_"));
//		}
//
//		for (ArrayList c : varRtn) {
//			fieldsId.put((String) c.get(0), (Integer) c.get(2));
//			System.out.println(c.get(0));
//		}
//		ArrayList<String> tim_List = new ArrayList();
//		tim_List = (ArrayList<String>) result_tim.get("list");
	}

	public static String bb(Object bef, Object aft) throws JsonProcessingException {
		String b = new ObjectMapper().writeValueAsString(bef);
		String a = new ObjectMapper().writeValueAsString(aft);
		System.out.println(b);
		System.out.println(a);
		return b;
	}

	public static Object cc(Object c, String msg) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper.readValue(msg, c.getClass());

	}
}
