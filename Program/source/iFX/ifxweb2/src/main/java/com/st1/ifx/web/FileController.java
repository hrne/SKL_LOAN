package com.st1.ifx.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.filter.SafeClose;
import com.st1.msw.UserInfo;
import com.st1.servlet.GlobalValues;

@Controller
@RequestMapping("file/*")
public class FileController extends MyAjaxBase {
	private static final Logger logger = LoggerFactory.getLogger(FileController.class);

	@Value("${iTXInFolder}")
	String iTXInFolder = "";

	@Value("${ifx_fxtxwrite}")
	String ifxWrite = "";

	@RequestMapping(value = "Upload", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> fileUpload(@RequestPart("file") List<MultipartFile> files, HttpSession session) {
		logger.info("UploadFile...");

		Map<String, Object> map = new LinkedHashMap<String, Object>();
		List<String> fileli = new ArrayList<String>();
		String info = "";
		int count = 0, total = 0;

		StopWatch watch = new StopWatch();
		watch.start();

		UserInfo userInfo = (UserInfo) session.getAttribute(GlobalValues.SESSION_USER_INFO);
		logger.info("UploadFile.." + "FullId = " + userInfo.getBrno() + userInfo.getId());
		SimpleDateFormat dayformat = new SimpleDateFormat("yyyyMMdd");
//		String todayPath = "/home/weblogic/itxDoc/itxWrite/upload/" + dayformat.format(new Date()) + File.separator + userInfo.getId() + File.separator;
		String todayPath = iTXInFolder + dayformat.format(new Date()) + File.separator + userInfo.getId() + File.separator;

		File todayFile = new File(FilterUtils.escape(todayPath));

		if (!todayFile.exists()) {
			todayFile.mkdirs();
			todayFile.setExecutable(true, false);
			todayFile.setWritable(true, false);
			todayFile.setReadable(true, false);
		}

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");

		BufferedOutputStream stream = null;
		FileOutputStream fileOutStream = null;

		total = files.size();
		try {
			String fileName = "";
			logger.info("File Size : " + total);
			for (MultipartFile fileT : files) {
				logger.info(fileT.getOriginalFilename() + "");
				logger.info(fileT.isEmpty() + "");
				if (!fileT.isEmpty()) {
//					UUID uuid = UUID.nameUUIDFromBytes(fileT.getOriginalFilename().getBytes("UTF-8"));
//					fileName = uuid.toString().replaceAll("-", "");
					fileli.add(fileT.getOriginalFilename());

					fileName = FilterUtils.escape(todayPath) + fileT.getOriginalFilename();

					byte[] bytes = fileT.getBytes();
					fileOutStream = new FileOutputStream(new File(fileName));
					stream = new BufferedOutputStream(fileOutStream);
					stream.write(bytes);
					stream.flush();
					SafeClose.close(stream);
					SafeClose.close(fileOutStream);
					count++;
				}
			}
		} catch (Throwable t) {
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			logger.warn(errors.toString());
			info = "<span style=\"color:red; font-weight:bold;\"><br> 上傳至暫存區 總共: " + total + " <br>" + "傳送 : " + count + " \n</span><br>" + errors.toString();
//			return new ResponseEntity<String>("<span style=\"color:red; font-weight:bold;\"><br> 上傳至暫存區 總共: " + total + " <br>" + "傳送 : " + count + " \n</span><br>" + errors.toString(),
//					responseHeaders, HttpStatus.CREATED);
		} finally {
			SafeClose.close(stream);
			SafeClose.close(fileOutStream);
		}

		if (info.isEmpty()) {
			info = "<span style=\"color:green; font-weight:bold;\">上傳至暫存區檔案數: " + count + " </span>";
			map.put("status", true);
		} else
			map.put("status", false);

		map.put("content", info);
		map.put("fileNames", fileli);

		watch.stop();
		logger.info("SwiftUpload Total Execution Time : " + watch.getTotalTimeMillis());
		return makeJsonResponse(map);
//		return new ResponseEntity<String>("<span style=\"color:green; font-weight:bold;\">上傳至暫存區檔案數: " + count + " </span>", responseHeaders, HttpStatus.CREATED);
	}

	// 設定交易傳送IP
	@RequestMapping(value = "sendIp", method = { POST, GET }) // produces="text/plain;charset=UTF-8
	@ResponseBody
	public ResponseEntity<String> sendIp(@RequestParam String _d, HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");

		String resop = "";

		File ipF = new File(ifxWrite + File.separator + "sendIp" + File.separator + "sendIp.txt");

		InputStreamReader read = null;
		BufferedReader br = null;

		FileOutputStream fout = null;
		OutputStreamWriter osw = null;
		BufferedWriter tsfw = null;

		try {
			logger.info("sendIp set " + _d);
			LinkedHashMap<String, String> map = new ObjectMapper().readValue(_d, LinkedHashMap.class);
			read = new InputStreamReader(new FileInputStream(ipF), "UTF-8");
			br = new BufferedReader(read);
			while (br.ready())
				resop += br.readLine();

			if (map.get("funcd").equals("get"))
				resop = new ObjectMapper().writeValueAsString(GlobalValues.sendIp);
			else {
				resop = "<span style=\"color:green; font-weight:bold;\">導入成功...<br/>";
				for (Entry<String, String> s : map.entrySet())
					resop += s.getKey() + " => " + s.getValue() + "<br/>";
				resop += "</span>";

				if (ipF.exists())
					ipF.delete();

				fout = new FileOutputStream(ipF, true);
				osw = new OutputStreamWriter(fout, "UTF-8");
				tsfw = new BufferedWriter(osw);

				GlobalValues.sendIp = (LinkedHashMap<String, String>) map.clone();
				tsfw.write(new ObjectMapper().writeValueAsString(map));
				tsfw.flush();
			}
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			GlobalValues.sendIp = null;
			logger.warn(errors.toString());
			resop = "Error!!導入或讀取失敗";
		} finally {
			SafeClose.close(tsfw);
			SafeClose.close(osw);
			SafeClose.close(fout);
			SafeClose.close(br);
			SafeClose.close(read);
		}

		return new ResponseEntity<String>(resop, responseHeaders, HttpStatus.CREATED);
	}

	// String 按照長度切割 潘
	private HashMap<Integer, String> splitStringByLength(String src, int length) {

		if (null == src || src.equals("") || length <= 0) {
			return null;
		}

		int n = (src.length() + length - 1) / length; // 獲取整個字符串可以被切割成字符子串的個数

		HashMap<Integer, String> m = new HashMap<Integer, String>();

		for (int i = 0; i < n; i++) {
			if (i < (n - 1)) {
				m.put(i, src.substring(i * length, (i + 1) * length));
			} else {
				m.put(i, src.substring(i * length));
			}
		}
		for (Object key : m.keySet()) {
			logger.info(key + " : " + m.get(key));
		}
		return m;
	}
}
