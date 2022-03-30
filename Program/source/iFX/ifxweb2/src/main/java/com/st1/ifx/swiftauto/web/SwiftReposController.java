package com.st1.ifx.swiftauto.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.st1.ifx.domain.SwiftPrinter;
import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.repos.MyFileUtl;
import com.st1.ifx.repos.SwiftRepository;
import com.st1.ifx.repository.SwiftPrinterRepository;
import com.st1.msw.UserInfo;
import com.st1.servlet.GlobalValues;

@Controller
@RequestMapping("swiftrepos/*")
public class SwiftReposController {
	static final Logger logger = LoggerFactory.getLogger(SwiftReposController.class);

	@Autowired
	private SwiftPrinterRepository printerRepos;

	@RequestMapping(value = "hi")
	public ModelAndView test(HttpServletResponse response) throws IOException {
		logger.info("hello");
		return new ModelAndView("home");
	}

	@RequestMapping(value = "swift/autoprint/{dt}", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> getSwiftFolderForAutoPrint(@PathVariable String dt, HttpSession session) {
		return listSwiftFolder(dt, session, true);
	}

	@RequestMapping(value = "swift/{dt}", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> getSwiftFolder(@PathVariable String dt, HttpSession session) {
		return listSwiftFolder(dt, session, false);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ResponseEntity<String> listSwiftFolder(String dt, HttpSession session, boolean mustfirst) {
		HashMap map = new HashMap();
		UserInfo userInfo = (UserInfo) session.getAttribute(GlobalValues.SESSION_USER_INFO);
		if (userInfo == null) {
			map.put("status", false);
			map.put("message", "E999 invalid session, not logged");
			return makeJsonResponse(map);
		}

		String brno = "";
		if (userInfo != null) {
			brno = userInfo.getBrno();
		}
		// System.out.printf("list swift folder brno:%s, dt:%s\n", brno, dt);
		SwiftRepository repos = new SwiftRepository();
		// 資料從實體檔案清單
		// List<File> files = repos.getSwiftFolder(brno, dt, suffix);
		// System.out.printf("repos.getSwiftfromDb!");
		// 資料從db2
		List<HashMap> smapList = repos.getSwiftfromDb(brno, dt, mustfirst);
		// System.out.printf("getSwiftfromDb files size:" + files.size());
		List<String> names = new ArrayList<String>();
		for (HashMap f : smapList) {
			names.add(f.get("filepath_rm").toString());
		}

		map.put("status", true);
		map.put("message", "hello");
		map.put("processTime", new Date());
		map.put("dt", dt);
		map.put("brno", brno);
		map.put("files", names);
		map.put("printTo", getPrintTo(brno, smapList));
		map.put("size", names.size());
		return makeJsonResponse(map);
	}

	private String[] getPrintTo(String brno, List<HashMap> smapList) {
		List<String> printerList = new ArrayList<String>();
		String printTo = "";
		String srhMsgType = "";
		String srhMsgStatus = "";

		Optional<SwiftPrinter> sp = this.printerRepos.findById(brno);
		if (sp.isPresent()) {
			SwiftPrinter printerInfo = sp.get();

			for (HashMap smap : smapList) {
				srhMsgType = smap.get("msgtype").toString();
				srhMsgStatus = smap.get("msgstatus").toString();
				printTo = printerInfo.getPrinterByMsgType(srhMsgType, srhMsgStatus);
				printerList.add(printTo);
			}
			return printerList.toArray(new String[0]);
		} else {
			logger.warn("getPrintTo finbyid not found!! id = " + brno);
			for (HashMap smap : smapList) {
				srhMsgType = smap.get("msgtype").toString();
				srhMsgStatus = smap.get("msgstatus").toString();
				printTo = null;
				printerList.add(printTo);
			}
			return printerList.toArray(new String[0]);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "swift/{dt}/{filename}", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> getSwiftFile(@PathVariable String dt, @PathVariable String filename, HttpSession session) {
		UserInfo userInfo = (UserInfo) session.getAttribute(GlobalValues.SESSION_USER_INFO);
		if (userInfo == null)
			throw new IllegalArgumentException("invalid session");

		logger.info(FilterUtils.escape("getting swift " + dt + "/" + filename));
		String brno = "";
		if (userInfo != null) {
			brno = userInfo.getBrno();
			logger.info("hi " + brno);
		}
		SwiftRepository repos = new SwiftRepository();
		File file = repos.getSwiftFile(brno, dt, filename + ".rptsf");
		if (file == null) {
			throw new IllegalArgumentException("WARNING brno:" + brno + ",dt:" + dt + ",filename:" + filename + " is not exist!");
		}
		List<String> lines = MyFileUtl.readAll(file, true);
		repos.setSwiftFilePrintedAgain(brno, dt, filename + ".rptsf");
		HashMap map = new HashMap();
		map.put("status", lines == null ? false : true);
		map.put("message", "hello");
		map.put("processTime", new Date());
		map.put("dt", dt);
		map.put("brno", brno);
		map.put("file", filename);
		map.put("printer", getPrinterName(brno, filename));
		map.put("lines", lines);
		map.put("size", lines == null ? 0 : lines.size());
		return makeJsonResponse(map);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "swift/{brndept}/{dt}/{filename}", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> getSwiftFileFromdb(@PathVariable String brndept, @PathVariable String dt, @PathVariable String filename, HttpSession session) {
		UserInfo userInfo = (UserInfo) session.getAttribute(GlobalValues.SESSION_USER_INFO);
		if (userInfo == null)
			throw new IllegalArgumentException("invalid session");

		logger.info(FilterUtils.escape("getSwiftFileFromdb:" + brndept + "," + dt + "," + filename + ".rptsf"));

		SwiftRepository repos = new SwiftRepository();
		String filePath = repos.getSwiftFilePath(brndept, dt, filename + ".rptsf");
		logger.info("getSwiftFile filePath:" + filePath);
		File file = repos.getSwiftFile(filePath);
		if (file == null) {
			throw new IllegalArgumentException("WARNING filePath:" + filePath + " is not exist!");
		}
		List<String> lines = MyFileUtl.readAll(file, true);
		repos.setSwiftFilePrintedAgain(brndept, dt, filename + ".rptsf");
		HashMap map = new HashMap();
		map.put("status", lines == null ? false : true);
		map.put("message", "hello");
		map.put("processTime", new Date());
		map.put("dt", dt);
		map.put("brno", brndept);
		map.put("file", filename);
		map.put("printer", getPrinterName(brndept, filename));
		map.put("lines", lines);
		map.put("size", lines == null ? 0 : lines.size());
		return makeJsonResponse(map);
	}

	private String getPrinterName(String brno, String filename) {
		String[] ss = filename.split("-");
		if (ss.length < 1)
			return ""; // default printer
		String mt = ss[ss.length - 1];
		if (mt.equals("103+"))
			return "Super Printer";

		return "";
	}

	@SuppressWarnings("rawtypes")
	protected ResponseEntity<String> makeJsonResponse(HashMap m) {
		Gson gson = new Gson();
		String output = gson.toJson(m);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(output, responseHeaders, HttpStatus.CREATED);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "swift/printer/list", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> getSwiftPrinter(HttpSession session) {
		logger.info("in getSwiftPrinter!");
		UserInfo userInfo = (UserInfo) session.getAttribute(GlobalValues.SESSION_USER_INFO);
		if (userInfo == null)
			throw new IllegalArgumentException("invalid session");

		String brno = "";
		if (userInfo != null) {
			brno = userInfo.getBrno();
			logger.info("hi " + brno);
		}
		SwiftPrinter printer = null;
		Optional<SwiftPrinter> sp = printerRepos.findById(brno);
		if (sp.isPresent())
			printer = sp.get();

		HashMap m = new HashMap();
		m.put("defined", printer == null ? "no" : "yes");
		if (printer != null)
			m.put("printer-info", printer);
		m.put("mtFiles", getMtFiles());
		/*
		 * ResponseEntity<String> x = makeJsonResponse(m); logger.info(x.toString());
		 * logger.info(x.getBody()); 潘
		 */
		return makeJsonResponse(m);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "swift/printer/save", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> saveSwiftPrinter(@RequestParam String _d, HttpSession session) {
		logger.info("in saveSwiftPrinter!");
		UserInfo userInfo = (UserInfo) session.getAttribute(GlobalValues.SESSION_USER_INFO);
		if (userInfo == null)
			throw new IllegalArgumentException("invalid session");

		String brno = "";
		if (userInfo != null) {
			brno = userInfo.getBrno();
			logger.info("hi " + brno);
		}

		Gson gson = new Gson();
		HashMap<String, String> m = new HashMap<String, String>();
		m = (HashMap<String, String>) gson.fromJson(_d, m.getClass());

		SwiftPrinter printer = new SwiftPrinter();
		printer.setBrn(brno);
		printer.setPrimaryPrinter(m.get("priPrinter"));
		printer.setAckPrinter(m.get("ackPrinter"));
		printer.setNakPrinter(m.get("nakPrinter"));
		printer.setAltPrinter(m.get("altPrinter"));
		printer.setPriAllowip(m.get("priAllowip"));
		printer.setAltMsgList(m.get("altMsgList"));
		printerRepos.save(printer);

		HashMap m2 = new HashMap();
		m2.put("status", "good");
		return makeJsonResponse(m2);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "mt/list", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> getMtList() {

		String[] names = getMtFiles();

		HashMap m = new HashMap();
		m.put("status", "good");
		m.put("mt", names);
		return makeJsonResponse(m);
	}

	private String[] getMtFiles() {
		File directory = new File(FilterUtils.filter(GlobalValues.swiftFolder));
		logger.info(FilterUtils.escape(GlobalValues.swiftFolder));
		Collection<File> files = FileUtils.listFiles(directory, new WildcardFileFilter("mt*.txt"), null);
		// System.out.println(files.size());
		String[] names = new String[files.size() * 3];
		int i = 0;
		for (File f : files) {
			// 來電
			names[i++] = MyFileUtl.removeExtension(f.getName()) + "_R";
			// 去電ACK
			names[i++] = MyFileUtl.removeExtension(f.getName()) + "_A";
			// 去電NAK
			names[i++] = MyFileUtl.removeExtension(f.getName()) + "_N";
		}
		return names;
	}
}
