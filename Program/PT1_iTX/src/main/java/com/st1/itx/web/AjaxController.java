package com.st1.itx.web;

import java.io.ByteArrayInputStream;
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
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
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

import com.google.gson.Gson;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxAttachment;
import com.st1.itx.db.domain.TxFile;
import com.st1.itx.db.service.TxAttachmentService;
import com.st1.itx.db.service.TxFileService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.data.Manufacture;
import com.st1.itx.util.filter.SafeClose;
import com.st1.itx.util.log.SysLogger;
import com.st1.itx.util.menu.MenuBuilder;
import com.st1.itx.util.parse.ZLibUtils;

@Controller
@RequestMapping("hnd/*")
public class AjaxController extends SysLogger {

	@Autowired
	private MenuBuilder menuBuilder;

	@Autowired
	private TxFileService txFileService;

	@PostConstruct
	public void init() {
		this.mustInfo("AjaxController Init....");
	}

	@RequestMapping(value = "menu2/jsonp", method = RequestMethod.GET)
	public ResponseEntity<String> getMenuJson2(@RequestParam String authNo, HttpSession session, HttpServletResponse response) {
		ThreadVariable.setObject(ContentName.loggerFg, true);
		Map<String, Object> map = new HashMap<String, Object>();

		if (authNo.trim().isEmpty())
			map.put("data", menuBuilder.buildRootMenu());
		else
			map.put("data", menuBuilder.buildMenu(authNo));

		map.put("status", true);
		return makeJsonResponse(map, true);
	}

	@RequestMapping(value = "txcd/jsonp", method = RequestMethod.GET)
	public ResponseEntity<String> getTxcdList(@RequestParam String authNo, @RequestParam String term, HttpSession session, HttpServletResponse response) {
		ThreadVariable.setObject(ContentName.loggerFg, true);
		Map<String, Object> map = new HashMap<String, Object>();

		if (!term.trim().isEmpty())
			map.put("data", menuBuilder.buildAutoCP(authNo, term));

		map.put("status", true);
		return makeJsonResponse(map, true);
	}

	@RequestMapping(value = "download/file/{fileNo}")
	public void getFile(@PathVariable String fileNo, HttpServletResponse response) throws Exception {
		ThreadVariable.setObject(ContentName.loggerFg, true);
		this.mustInfo("getFile FileNo : [" + fileNo + "]");

		Long fileNoL = Long.parseLong(fileNo);

		ZLibUtils zLibUtils = MySpring.getBean("zLibUtils", ZLibUtils.class);

		TxAttachmentService txAttachmentService = MySpring.getBean("txAttachmentService", TxAttachmentService.class);
		TxAttachment txAttachment = txAttachmentService.findById(fileNoL);

		InputStream inputStream = null;
		try {
			if (txAttachment != null) {
				response.addHeader("Access-Control-Allow-Origin", "*");
				response.setHeader("Content-Disposition", "attachment;filename*=UTF-8'zh_TW'" + URLEncoder.encode(txAttachment.getFileItem().trim(), "UTF-8"));
				response.setContentType("application/force-download");

				inputStream = new ByteArrayInputStream(zLibUtils.unCompress7z(txAttachment.getFileData()));
				IOUtils.copy(inputStream, response.getOutputStream());
				response.flushBuffer();
			} else
				this.mustInfo("txAttachment is Null");
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		} finally {
			ThreadVariable.clearThreadLocal();
			SafeClose.close(inputStream);

			zLibUtils = null;
			txAttachmentService = null;
			txAttachment = null;
		}
	}

	@RequestMapping(value = "download/file/{sno}/{fileType}/{name}")
	public void getFile(@PathVariable String sno, @PathVariable String fileType, String name, HttpServletResponse response) throws Exception {
		ThreadVariable.setObject(ContentName.loggerFg, true);
		this.mustInfo("getFile...");

		fileType = fileType == null ? "" : fileType.trim();
		if (fileType.equals("7")) {
			InputStream inputStream = null;
			ZLibUtils zLibUtils = MySpring.getBean("zLibUtils", ZLibUtils.class);
			TxFile txFile = txFileService.findById(Long.parseLong(Objects.isNull(sno) || sno.isEmpty() ? "0" : sno));
			try {
				if (Objects.isNull(txFile))
					return;

				response.addHeader("Access-Control-Allow-Origin", "*");
				response.setHeader("Content-Disposition", "attachment;filename*=UTF-8'zh_TW'" + URLEncoder.encode(txFile.getFileItem(), "UTF-8"));
				response.setContentType("application/force-download");

				inputStream = new ByteArrayInputStream(zLibUtils.unCompress7z(txFile.getFileZip()));
				IOUtils.copy(inputStream, response.getOutputStream());
				response.flushBuffer();
				return;
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error(errors.toString());
				return;
			} finally {
				txFile = null;
				zLibUtils = null;
				SafeClose.close(inputStream);
				ThreadVariable.clearThreadLocal();
			}
		}

		TitaVo titaVo = new TitaVo();
		titaVo.putParam("fileno", sno);

		Manufacture manufacture = MySpring.getBean("manufacture", Manufacture.class);
		manufacture.setTitaVo(titaVo);

		try {
			manufacture.exec();
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}
		
		String fileName = manufacture.getFilename() + manufacture.getExt();
		String saveName = manufacture.getSavename();
		String titleName = manufacture.getTitleName().isEmpty() ? "??????" : manufacture.getTitleName();

		InputStream inputStream = null;
		try {
			File file = new File(fileName);
			this.mustInfo("get filepath : " + file.getPath());

			inputStream = new FileInputStream(file);
			response.addHeader("Access-Control-Allow-Origin", "*");

			if (!"1".equals(fileType)) {

				response.setHeader("Content-Disposition", "attachment;filename*=UTF-8'zh_TW'" + URLEncoder.encode(saveName, "UTF-8"));
				response.setContentType("application/force-download");
				// "attachment;filename*=UTF-8''"+URLEncoder.encode("?????????????????????.mp3", "UTF-8");
//				response.setHeader("Content-Disposition", "attachment; filename=" + saveName);
			} else {
				response.setHeader("Content-Disposition", "inline; name=\"" + URLEncoder.encode(titleName, "UTF-8") + "\"; filename*=UTF-8''"
						+ URLEncoder.encode(saveName.replaceAll(".pdf", ""), "UTF-8") + "_" + URLEncoder.encode(titleName + ".pdf", "UTF-8"));
				response.setContentType("application/pdf;");
			}
			IOUtils.copy(inputStream, response.getOutputStream());
			response.flushBuffer();
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		} finally {
			SafeClose.close(inputStream);
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

		this.mustInfo("printReport Active!");
		this.mustInfo("Param : " + _d);

		long reportNo = Long.parseLong(Objects.isNull(_m.get("reportNo")) ? "0" : _m.get("reportNo").trim());
//		String printer = Objects.isNull(_m.get("printer")) ? "" : _m.get("printer").trim();
		String localIp = Objects.isNull(_m.get("localIp")) ? "" : _m.get("localIp").trim();
		String prt = "";
		String prtIp = "";
		int pageNo = 1;

		MakeReport makeReport = MySpring.getBean("makeReport", MakeReport.class);

		List<List<Map<String, ?>>> pLi = new ArrayList<List<Map<String, ?>>>();
		while (true) {
			try {
				Map<String, ?> p = makeReport.toPrint(reportNo, pageNo, localIp);
				if (p.get("printJson") != null)
					pLi.add((List<Map<String, ?>>) p.get("printJson"));

				this.mustInfo("morePage : " + "1".equals(p.get("morePage").toString()));
				this.mustInfo("pageNo   : " + pageNo);

				if (pageNo == 1) {
					prt = (String) p.get("Printer");
					prtIp = (String) p.get("ServerIp");
				}

				if (!"1".equals(p.get("morePage").toString()) || pageNo > 100)
					break;

				pageNo++;
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error(errors.toString());
				map.put("success", false);
				map.put("msg", e.getMessage());
				break;
			}
		}

		if (map.get("success") == null)
			map.put("success", true);

		if (Objects.isNull(prtIp) || prtIp.isEmpty()) {
			map.put("success", false);
			map.put("msg", "????????????????????????IP");
		}

		if (Objects.isNull(prt) || prt.isEmpty()) {
			map.put("success", false);
			String msg = Objects.isNull(map.get("msg")) ? "" : (String) map.get("msg");
			map.put("msg", msg + "??????????????????");
		}

		map.put("ServerIp", prtIp);
		map.put("Printer", prt);
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

	private ResponseEntity<String> makeJsonResponse(Map<String, ?> m, boolean isOtherNet) {

		Gson gson = null;
		String output = null;
		HttpHeaders responseHeaders = null;
		try {
			gson = new Gson();
			output = gson.toJson(m);
			responseHeaders = new HttpHeaders();
			if (isOtherNet)
				responseHeaders.add("Access-Control-Allow-Origin", "*");
			responseHeaders.add("Content-Type", "application/json; charset=utf-8");
			return new ResponseEntity<String>(output, responseHeaders, HttpStatus.CREATED);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			return new ResponseEntity<String>("", responseHeaders, HttpStatus.CREATED);
		} finally {
			ThreadVariable.clearThreadLocal();
		}
	}
}
