package com.st1.ifx.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.filter.SafeClose;
import com.st1.ifx.repos.MyFileUtl;
import com.st1.msw.UserInfo;
import com.st1.servlet.GlobalValues;

@Controller
@RequestMapping("ifxReport/*")
public class IfxReportController {
	static final Logger logger = LoggerFactory.getLogger(IfxReportController.class);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "get/{dt}/{app}", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> getSwiftFolderForAutoPrint(@PathVariable String dt, @PathVariable String app, HttpSession session) {
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
		String root = GlobalValues.REPOSITORY_ROOT;
		List<File> files = getFiles(root, brno, dt, app, ".rpt");
		List<String> names = new ArrayList<String>();
		for (File f : files) {
			names.add(MyFileUtl.removeExtension(f));
		}

		map.put("status", true);
		map.put("message", "hi");
		map.put("processTime", new Date());
		map.put("dt", dt);
		map.put("brno", brno);
		map.put("files", names);
		map.put("size", names.size());
		return makeJsonResponse(map);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "get/{dt}/{app}/{filename}", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> getFileContent(@PathVariable String dt, @PathVariable String app, @PathVariable String filename, HttpSession session) {
		UserInfo userInfo = (UserInfo) session.getAttribute(GlobalValues.SESSION_USER_INFO);
		if (userInfo == null)
			throw new IllegalArgumentException("invalid session");

		System.out.println("getting swift " + dt + "/" + filename);
		String brno = "";
		if (userInfo != null) {
			brno = userInfo.getBrno();
			System.out.println("hi " + brno);
		}
		String root = GlobalValues.REPOSITORY_ROOT;
		String filePath = combinePaths(root, brno, dt, app, filename + ".rpt");
		File file = new File(filePath);
		List<String> lines = MyFileUtl.readAll(file, true);
		HashMap map = new HashMap();
		map.put("status", lines == null ? false : true);
		map.put("message", "hello");
		map.put("processTime", new Date());
		map.put("dt", dt);
		map.put("brno", brno);
		map.put("file", filename);
		map.put("lines", lines);
		map.put("size", lines == null ? 0 : lines.size());
		return makeJsonResponse(map);
	}

	@RequestMapping(value = "download/{dt}/{app}/{filename}")
	public void getLogFile(@PathVariable String dt, @PathVariable String app, @PathVariable String filename, HttpSession session, HttpServletResponse response) throws Exception {

		UserInfo userInfo = (UserInfo) session.getAttribute(GlobalValues.SESSION_USER_INFO);
		if (userInfo == null)
			throw new IllegalArgumentException("invalid session");

		System.out.println("getting swift " + dt + "/" + filename);
		String brno = "";
		if (userInfo != null) {
			brno = userInfo.getBrno();
			System.out.println("hi " + brno);
		}
		InputStream inputStream = null;
		try {
			String root = GlobalValues.REPOSITORY_ROOT;
			String filePath = combinePaths(root, brno, dt, app, filename + ".rpt");
			File fileToDownload = new File(filePath);
			inputStream = new FileInputStream(fileToDownload);
			response.setContentType("application/force-download");
			response.setHeader("Content-Disposition", "attachment; filename=" + filename + ".rpt");
			IOUtils.copy(inputStream, response.getOutputStream());
			response.flushBuffer();
			inputStream.close();
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} finally {
			SafeClose.close(inputStream);
		}

	}

	private List<File> getFiles(String root, String brno, String dt, String app, String ext) {
		String folder = combinePaths(root, brno, dt, app);
		File[] files = new File(folder).listFiles(new EndsWithFilter(ext));
		if (files == null)
			return new ArrayList<File>();
		return Arrays.asList(files);
	}

	@SuppressWarnings("rawtypes")
	protected ResponseEntity<String> makeJsonResponse(HashMap m) {
		Gson gson = new Gson();
		String output = gson.toJson(m);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(output, responseHeaders, HttpStatus.CREATED);
	}

	class EndsWithFilter implements FilenameFilter {
		String ext;

		EndsWithFilter(String ext) {
			this.ext = ext;
		}

		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(ext);
		}

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
		return combined.getPath();
	}
}
