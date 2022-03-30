package com.st1.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.filter.SafeClose;
import com.st1.util.PoorManFile;
import com.st1.util.PoorManUtil;

public class SaveDoc {
	private static final String DOC_EXT = ".zip";
	private static final String REPORT_EXT = ".rpt";
	private static final String ROPORT_EXT = ".ropt";
	private static final String PRINT_LOG_EXT = ".log";
	static final Logger logger = LoggerFactory.getLogger(SaveDoc.class);

	@SuppressWarnings("rawtypes")
	Map map = new HashMap<String, Object>();

	@SuppressWarnings("unchecked")
	public String save(String day, String brno, String txtno, String docJson) {
		try {
			String folder = GlobalValues.getDocFolder(day, brno);
			// HashMap<String, Object> map = new
			// ObjectMapper().readValue(docJson,
			// HashMap.class);
			// HashMap data = (HashMap) map.get("docs");
			// Iterator iter = data.keySet().iterator();
			// while (iter.hasNext()) {
			// String k = (String) iter.next();
			// String v = (String) data.get(k);
			// logger.info(k + "=" + v);
			// }

			String filePath = folder + File.separator + txtno + DOC_EXT;
			writeFile(filePath, docJson);
			map.put("status", "ok");

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			map.put("status", "nok");
			map.put("message", e.getMessage());
		}
		return toJSON(map);
	}

	public String toJSON(Map<String, Object> map) {
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

	private void writeFile(String filePath, String content) throws Exception {
		FileOutputStream fos = null;
		GZIPOutputStream gz = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(FilterUtils.filter(filePath));
			gz = new GZIPOutputStream(fos);
			// ZipOutputStream
			oos = new ObjectOutputStream(gz);
			// oos.writeBytes(content);
			oos.writeUTF(content);
			oos.flush();
		} finally {
			SafeClose.close(oos);
			SafeClose.close(gz);
			SafeClose.close(fos);
		}
	}

	private String readFile(String filePath) throws Exception {
		FileInputStream fis = null;
		GZIPInputStream gs = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream(filePath);
			gs = new GZIPInputStream(fis);
			// ZipInputStream
			ois = new ObjectInputStream(gs);
			String s = ois.readUTF();
			return s;
		} finally {
			SafeClose.close(ois);
			SafeClose.close(gs);
			SafeClose.close(fis);
		}
	}

	/**
	 * 報表頁次讀取 (輸出時去除每行第一位控制碼)
	 */
	@SuppressWarnings("unchecked")
	public String readFilepage(String day, String brno, String tlrno, String filePath, String page) {
		logger.info("in readFileline");
		// Maybe can change to RandomAccessFile
		logger.info(FilterUtils.escape(day));
		logger.info(FilterUtils.escape(brno));
		logger.info(FilterUtils.escape(tlrno));
		logger.info(FilterUtils.escape(filePath));
		logger.info(FilterUtils.escape(page));
		String str = "";
		int oreadpage = 0; // 已讀取頁次
		// 該頁的內容
		List<String> textlist = new ArrayList<String>();
		// 全部內容
		List<String> textlistall = new ArrayList<String>();
		String outstring = "";
		String file = null;
		FileInputStream ins = null;
		Reader r = null; // cooked reader
		BufferedReader br = null; // buffered for readLine()
		int pagenum = Integer.parseInt(page); // 頁次
		logger.info("Report報表讀取");
		logger.info("pagenum   :" + pagenum);
		logger.info(FilterUtils.escape("fil          :" + filePath));
		if (tlrno.isEmpty()) {
			logger.info("Go searchReport");
			file = GlobalValues.searchReport(day, brno, filePath, REPORT_EXT);
		} else {
			logger.info("Go searchRoport");
			file = GlobalValues.searchRoport(day, brno, tlrno, filePath, ROPORT_EXT);
		}
		logger.info("file path    :" + FilterUtils.escape(file));

		try {
			ins = new FileInputStream(FilterUtils.filter(file));
			logger.info("after FileInputStream");
			r = new InputStreamReader(ins, "UTF-8"); // leave charset out for default
			logger.info("after InputStreamReader");
			br = new BufferedReader(r);
			logger.info("after BufferedReader");
			map.put("fnext", "0"); // 預設沒有
			while ((str = br.readLine()) != null && oreadpage <= pagenum) {
				logger.info("str:" + str);
				if (str.substring(0, 1).equals("1")) {
					if (oreadpage + 1 > pagenum) {
						map.put("fnext", "1");
					} else {
						map.put("fnext", "0");
					}
					oreadpage++;
					logger.info("oreadpage:" + oreadpage);
				}
				if (oreadpage == pagenum) {
					logger.info("textlist add:" + str);
					textlist.add(str.substring(1));
				} else {
					textlistall.add(str.substring(1));
				}
			}
			logger.info("textlist.size():" + textlist.size());
			logger.info("textlistall.size():" + textlistall.size());
			if (textlist.size() == 0) {
				logger.info("該張報表不正常.返回報表全部內容.");
				textlist = textlistall;
			}
			outstring = Joiner.on("\n").join(textlist);
			logger.info("outstring:" + outstring);
			map.put("status", "ok");
			map.put("result", outstring);

		} catch (Exception e) {
			// TODO: handle exception
			logger.info("Report報表讀取錯誤!!!" + e);
			map.put("status", "nok");
			map.put("message", e.getMessage());
		} finally {
			SafeClose.close(br);
			SafeClose.close(r);
			SafeClose.close(ins);
		}
		return toJSON(map);
	}

	/**
	 * 報表全部讀取,for[全部列印]功能 (輸出時去除每行第一位控制碼)
	 */
	@SuppressWarnings("unchecked")
	public String readFilepageall(String day, String brno, String tlrno, String filePath) {
		logger.info("in readFileline");
		// Maybe can change to RandomAccessFile
		logger.info(FilterUtils.escape(day));
		logger.info(FilterUtils.escape(brno));
		logger.info(FilterUtils.escape(tlrno));
		logger.info(FilterUtils.escape(filePath));
		String str = "";
		List<String> textlist = new ArrayList<String>();
		String outstring = "";
		String file = null;
		FileInputStream ins = null;
		Reader r = null; // cooked reader
		BufferedReader br = null; // buffered for readLine()
		logger.info("Report all 報表讀取");
		logger.info("fil          :" + FilterUtils.escape(filePath));
		if (tlrno.isEmpty()) {
			logger.info("Go searchReport");
			file = GlobalValues.searchReport(day, brno, filePath, REPORT_EXT);
		} else {
			logger.info("Go searchRoport");
			file = GlobalValues.searchRoport(day, brno, tlrno, filePath, ROPORT_EXT);
		}
		logger.info("file path    :" + FilterUtils.escape(file));

		try {
			ins = new FileInputStream(FilterUtils.filter(file));
			logger.info("after FileInputStream");
			r = new InputStreamReader(ins, "UTF-8"); // leave charset out for default
			logger.info("after InputStreamReader");
			br = new BufferedReader(r);
			logger.info("after BufferedReader");
			boolean firstpage = true;
			while ((str = br.readLine()) != null) {
				logger.info("str:" + str);
				if (str.substring(0, 1).equals("1")) {
					if (firstpage) {
						firstpage = false;
					} else {
						textlist.add("{{formfeed}}");
					}
					textlist.add(str.substring(1));
				} else {
					textlist.add(str.substring(1));
				}
			}
			outstring = Joiner.on("\n").join(textlist);
			logger.info("outstring:" + outstring);
			map.put("fnext", "0");
			map.put("status", "prt");
			map.put("result", outstring);
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("Report all報表讀取錯誤!!!" + e);
			map.put("status", "nok");
			map.put("message", e.getMessage());
		} finally {
			SafeClose.close(br);
			SafeClose.close(r);
			SafeClose.close(ins);
		}
		return toJSON(map);
	}

	@SuppressWarnings("unchecked")
	public String readFileline(String day, String brno, String tlrno, String filePath, String startline, String count) {
		logger.info("in readFileline");
		// Maybe can change to RandomAccessFile
		logger.info(FilterUtils.escape(day));
		logger.info(FilterUtils.escape(brno));
		logger.info(FilterUtils.escape(tlrno));
		logger.info(FilterUtils.escape(filePath));
		logger.info(FilterUtils.escape(startline));
		logger.info(FilterUtils.escape(count));
		String str = "";
		int oreadline = 0; // 已讀取行數
		int omapline = 0; // 已maping 行數
		List<String> textlist = new ArrayList<String>();
		String outstring = "";
		String file = null;
		FileInputStream ins = null;
		Reader r = null; // cooked reader
		BufferedReader br = null; // buffered for readLine()
		int sline = Integer.parseInt(startline); // 開始行數
		int scount = Integer.parseInt(count); // 限制只讀取總行數
		logger.info("Report報表讀取");

		logger.info("start line   :" + FilterUtils.escape(startline));
		logger.info("need map line:" + FilterUtils.escape(count));
		logger.info("fil          :" + FilterUtils.escape(filePath));
		if (tlrno.isEmpty()) {
			logger.info("Go searchReport");
			file = GlobalValues.searchReport(day, brno, filePath, REPORT_EXT);
		} else {
			logger.info("Go searchRoport");
			file = GlobalValues.searchRoport(day, brno, tlrno, filePath, ROPORT_EXT);
		}
		logger.info(FilterUtils.escape("file path    :" + file));

		try {
			ins = new FileInputStream(FilterUtils.filter(file));
			logger.info("after FileInputStream");
			r = new InputStreamReader(ins, "UTF-8"); // leave charset out for default
			logger.info("after InputStreamReader");
			br = new BufferedReader(r);
			logger.info("after BufferedReader");
			map.put("fnext", "0"); // 預設沒有
			while ((str = br.readLine()) != null && omapline <= scount) {
				logger.info("str:" + str);
				oreadline++;
				if (oreadline <= sline) {
					logger.info("oreadline <= sline");
				} else {
					logger.info("else...");

					if (omapline < scount) {
						textlist.add(str);
					} else {
						map.put("fnext", "1"); // 還有續傳值
					}
					omapline++;

				}

			}
			outstring = Joiner.on("\n").join(textlist);
			logger.info("outstring:" + outstring);
			map.put("status", "ok");
			map.put("result", outstring);
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("Report報表讀取錯誤!!!" + e);
			map.put("status", "nok");
			map.put("message", e.getMessage());
		} finally {
			SafeClose.close(br);
			SafeClose.close(r);
			SafeClose.close(ins);
		}
		return toJSON(map);
	}

	private String readPrintLog(String day, String brno, String txtno) {
		String printLogFile = GlobalValues.getDocFilePath(day, brno, txtno, PRINT_LOG_EXT);
		File f = new File(FilterUtils.filter(printLogFile));
		if (f.exists()) {
			PoorManFile p = new PoorManFile(printLogFile);
			try {
				return p.read();
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
			}
		}
		return null;
	}

	private void appendPrintLog(String day, String brno, String txtno, String line) {
		String printLogFile = GlobalValues.getDocFilePath(day, brno, txtno, PRINT_LOG_EXT);
		PoorManFile p = new PoorManFile(printLogFile);
		try {
			p.append(line);
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}

	}

	@SuppressWarnings("unchecked")
	public String searchDoc(String day, String brno, String txtno) {
		File file = GlobalValues.searchDoc(day, brno, txtno, DOC_EXT);
		if (file == null) {
			map.put("status", "nok");
			map.put("message", txtno + " not found!");

		} else {
			try {
				logger.info(file.getPath());
				String printLog = readPrintLog(day, brno, txtno);
				if (printLog != null)
					map.put("printLog", printLog);

				map.put("status", "ok");
				map.put("result", readMap(file.getPath()));
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
				map.put("status", "nok");
				map.put("message", e.getMessage());
			}
		}
		return toJSON(map);
	}

	@SuppressWarnings("unchecked")
	public String updatePrintLog(String day, String brno, String txtno, String formId, String tlrno) {
		String logText = String.format("%s, %s,%s,%s\n", formId, brno, tlrno, PoorManUtil.getNowwithFormat("yyyy/MM/dd HH:mm:ss"));
		appendPrintLog(day, brno, txtno, logText);
		map.put("status", "ok");
		return toJSON(map);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String fecthDoc(String day, String brno, String txtno, String form) {
		File file = GlobalValues.searchDoc(day, brno, txtno, DOC_EXT);
		if (file == null) {
			map.put("status", "nok");
			map.put("message", txtno + " not found!");
		} else {
			try {
				logger.info(file.getPath());
				String docJson = readFile(file.getPath());
				HashMap<String, Object> jsonMap = new ObjectMapper().readValue(docJson, HashMap.class);
				HashMap data = (HashMap) jsonMap.get("docs");
				String r = (String) data.get(form);
				logger.info(r);
				map.put("status", "ok");
				map.put("form", r);

			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
				map.put("status", "nok");
				map.put("message", e.getMessage());
			}
		}
		logger.info(FilterUtils.escape(toJSON(map)));
		return toJSON(map);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap readMap(String filePath) throws Exception {
		String docJson = readFile(filePath);
		HashMap<String, Object> map = new ObjectMapper().readValue(docJson, HashMap.class);
		HashMap data = (HashMap) map.get("docs");
		return data;
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		GlobalValues.ifxFolder = "d:/ifxfolder";
		String brno = "5050";
		SaveDoc saveDoc = new SaveDoc();
		// Random generator = new Random();
		// String txno = brno + "-" + generator.nextInt(30000) + "-"
		// + generator.nextInt(20000);
		// String json =
		// "{ \"docs\" : { \"FM101\" : \"������H����076�����P�a�_��i\", \"I1000\" : \"��
		// �� �� �m�G �Ὤ���F���_���F�� 41.8 ���� (���y�������)\"}}";
		// String result = saveDoc.save(brno, txno, json);
		// logger.info("result:" + result);
		//
		// List<File> files = GlobalValues.getDocFiles(brno);
		// Iterator<File> iter = files.iterator();
		// while (iter.hasNext()) {
		// File f = iter.next();
		// logger.info(f.getName());
		// }
		String day = PoorManUtil.getToday();
		String searchTxtNo = "5050026100093175";
		String s = saveDoc.searchDoc(day, brno, searchTxtNo);
		logger.info(FilterUtils.escape(s));
		// SaveDoc saveDoc2 = new SaveDoc();
		// s = saveDoc2.fecthDoc(day, brno, searchTxtNo, "I1000");
		// logger.info(s);
	}

	public String readSwiftOver(String fileName) throws Exception {
		String filePath = GlobalValues.swift_mergeFolder + File.separator + fileName;
		logger.info(FilterUtils.escape("readSwiftOver filePath:" + filePath));
		BufferedReader br = null;
		FileInputStream fis = null;
		InputStreamReader isr = null;

		String line = "";
		String result = "";
		try {
			File f = new File(FilterUtils.filter(filePath));
			fis = new FileInputStream(f);
			// java.io.FileNotFoundException
			logger.info("BufferedReader BIG5..");
			isr = new InputStreamReader(fis, "BIG5");
			br = new BufferedReader(isr); // MFT只有BIG5
			// java.io.UnsupportedEncodingException
			// java.io.IOException
			logger.info("br.ready? " + br.ready());
			while ((line = br.readLine()) != null) {

				// 1.去除空行
				// 2.去除第三個字是小寫的tag ()
				// 3.去除空欄時的 冒號 + 4位空白
				if (line.isEmpty() || Character.isLowerCase(line.charAt(2)) || line.matches("^(..:    $|...:    $)")) {
					logger.info("List.del:" + line);
					continue;
				}
				logger.info("List.L:" + line);
				// 2.首列補 冒號 符合SWIFT規格
				result = result + ":" + line + "\r\n";

			}
			logger.info("replaceAll \\\\n.");
			// 3.把\\n取代成換行符號
			// JAVA將"\\\\"解析成"\\"給正規表達式，正規表達式再將"\\"解析成"\"
			// 所以一個反斜線，在正規表達式要寫成四個
			result = result.replaceAll("\\\\\\\\n", "\r\n");
			logger.info("after readLine .result:" + result);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			SafeClose.close(br);
			SafeClose.close(isr);
			SafeClose.close(fis);
		}
		return result;
	}
}
