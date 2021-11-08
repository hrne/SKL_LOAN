package com.st1.itx.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.data.Manufacture;
import com.st1.itx.util.filter.FilterUtils;
import com.st1.itx.util.log.SysLogger;

@Controller
@RequestMapping("hnd/*")
public class AjaxController extends SysLogger {
	@PostConstruct
	public void init() {
		this.info("AjaxController Init....");
	}

	@RequestMapping(value = "download/file/{sno}/{fileType}/{name}")
	public void getFile(@PathVariable String sno, @PathVariable String fileType, String name, HttpServletResponse response) throws Exception {
		ThreadVariable.setObject(ContentName.loggerFg, true);
		this.info("getFile...");

		fileType = fileType == null ? "" : fileType.trim();

		TitaVo titaVo = new TitaVo();
		titaVo.putParam("fileno", sno);

		Manufacture manufacture = MySpring.getBean("manufacture", Manufacture.class);
		manufacture.setTitaVo(titaVo);

		manufacture.exec();
		String fileName = manufacture.getFilename() + manufacture.getExt();
		String saveName = manufacture.getSavename();
		String titleName = manufacture.getTitleName().isEmpty() ? "空白" : manufacture.getTitleName();

		InputStream inputStream = null;
		try {
			File file = new File(fileName);
			this.info("get filepath : " + file.getPath());

			inputStream = new FileInputStream(file);
			response.addHeader("Access-Control-Allow-Origin", "*");

			if (!"1".equals(fileType)) {
				response.setHeader("Content-Disposition", "attachment;filename*=UTF-8'zh_TW'" + URLEncoder.encode(saveName, "UTF-8"));
				response.setContentType("application/force-download");
				// "attachment;filename*=UTF-8''"+URLEncoder.encode("时间都去哪儿了.mp3", "UTF-8");
//				response.setHeader("Content-Disposition", "attachment; filename=" + saveName);
			} else {
				response.setHeader("Content-Disposition",
						"inline; name=\"" + URLEncoder.encode(titleName, "UTF-8") + "\"; filename*=UTF-8''" + saveName.replaceAll(".pdf", "") + "_" + titleName + ".pdf");
				response.setContentType("application/pdf;");
			}
			IOUtils.copy(inputStream, response.getOutputStream());
			response.flushBuffer();
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		} finally {
			inputStream.close();
			ThreadVariable.clearThreadLocal();
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "printReport" }, method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> findCodeList(@RequestParam String _d, HttpSession session, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		Map<String, String> _m = this.stringToMap(_d);
		ThreadVariable.setObject(ContentName.loggerFg, "1".equals(_m.get("Log")) ? true : false);

		this.info("printReport Active!");
		this.info("Param : " + _d);

		long reportNo = Long.parseLong(_m.get("reportNo").trim());
		String printer = _m.get("printer");
		int pageNo = 0;

		MakeReport makeReport = MySpring.getBean("makeReport", MakeReport.class);

		List<List<Map<String, ?>>> pLi = new ArrayList<List<Map<String, ?>>>();
		while (true) {
			try {
				Map<String, ?> p = makeReport.toPrint(reportNo, pageNo++, printer);
				if (p.get("printJson") != null)
					pLi.add((List<Map<String, ?>>) p.get("printJson"));
				if (!"1".equals(p.get("morePage")) || pageNo > 100)
					break;
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error(errors.toString());
				map.put("success", false);
				break;
			}
		}

		if (map.get("success") == null)
			map.put("success", true);
		map.put("pageNo", pageNo);
		map.put("printList", pLi);
		ResponseEntity<String> result = null;
		try {
			result = makeJsonResponse(map, true);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> stringToMap(String _d) {
		Gson gson = new Gson();
		Map<String, String> m = new LinkedHashMap<String, String>();
		m = gson.fromJson(_d, m.getClass());

		return m;
	}

	private ResponseEntity<String> makeJsonResponse(Map<String, ?> m, boolean isOtherNet) throws Exception {

		Gson gson = new Gson();
		String output = gson.toJson(m);

		HttpHeaders responseHeaders = new HttpHeaders();
		if (isOtherNet)
			responseHeaders.add("Access-Control-Allow-Origin", "*");
		responseHeaders.add("Content-Type", "application/json; charset=utf-8");

		this.info("return json: " + FilterUtils.escape(output));
		ThreadVariable.clearThreadLocal();
		return new ResponseEntity<String>(output, responseHeaders, HttpStatus.CREATED);
	}
}
