package com.st1.ifx.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.st1.ifx.repos.MyFileUtl;
import com.st1.ifx.repos.SwiftRepository;
import com.st1.msw.UserInfo;
import com.st1.servlet.GlobalValues;

@Controller
@RequestMapping("repos/*")
public class ReposController {

	@RequestMapping(value = "hi")
	public ModelAndView test(HttpServletResponse response) throws IOException {
		System.out.println("hello");
		return new ModelAndView("home");
	}

	@RequestMapping(value = "swift/autoprint/{dt}", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> getSwiftFolderForAutoPrint(@PathVariable String dt, HttpSession session) {
		return listSwiftFolder(dt, session, "0.rptsf");
	}

	@RequestMapping(value = "swift/{dt}", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> getSwiftFolder(@PathVariable String dt, HttpSession session) {
		return listSwiftFolder(dt, session, ".rptsf");
	}

	@SuppressWarnings("unchecked")
	private ResponseEntity<String> listSwiftFolder(String dt, HttpSession session, String suffix) {
		HashMap map = new HashMap();
		UserInfo userInfo = (UserInfo) session.getAttribute(GlobalValues.SESSION_USER_INFO);
		if (userInfo == null) {
			map.put("status", false);
			map.put("message", "E999 invalid session, not logged");
			return makeJsonResponse(map);
		}

		System.out.println("hello:" + dt);
		String brno = "";
		if (userInfo != null) {
			brno = userInfo.getBrno();
			System.out.println("hi " + brno);
		}
		SwiftRepository repos = new SwiftRepository();
		List<File> files = repos.getSwiftFolder(brno, dt, suffix);
		List<String> names = new ArrayList<String>();
		for (File f : files) {
			names.add(MyFileUtl.removeExtension(f));
		}

		map.put("status", true);
		map.put("message", "hello");
		map.put("processTime", new Date());
		map.put("dt", dt);
		map.put("brno", brno);
		map.put("files", names);
		map.put("size", names.size());
		return makeJsonResponse(map);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "swift/{dt}/{filename}", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> getSwiftFile(@PathVariable String dt, @PathVariable String filename, HttpSession session) {
		UserInfo userInfo = (UserInfo) session.getAttribute(GlobalValues.SESSION_USER_INFO);
		if (userInfo == null)
			throw new IllegalArgumentException("invalid session");

		System.out.println("getting swift " + dt + "/" + filename);
		String brno = "";
		if (userInfo != null) {
			brno = userInfo.getBrno();
			System.out.println("hi " + brno);
		}
		SwiftRepository repos = new SwiftRepository();
		File file = repos.getSwiftFile(brno, dt, filename + ".rptsf");
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
}
