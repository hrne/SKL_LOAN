package com.st1.itx.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLEncoder;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.data.Manufacture;
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
				response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + URLEncoder.encode(saveName, "UTF-8"));
				response.setContentType("application/force-download");
				// "attachment;filename*=UTF-8''"+URLEncoder.encode("时间都去哪儿了.mp3", "UTF-8");
//				response.setHeader("Content-Disposition", "attachment; filename=" + saveName);
			} else {
				response.setHeader("Content-Disposition",
						"inline; name=\"" + titleName + "\"; filename*=UTF-8''" + URLEncoder.encode(saveName.replaceAll(".pdf", "") + "_" + titleName + ".pdf", "UTF-8"));
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
		}
	}
}
