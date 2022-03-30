package com.st1.ifx.web;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.st1.ifx.domain.ConsoleLog;
import com.st1.ifx.domain.SwiftUnsolicitedMsg;
import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.service.ConsoleLogService;
import com.st1.ifx.service.SwiftUnsoMsgService;
import com.st1.util.PoorManUtil;

@Controller
@RequestMapping("rpt/*")
public class ReportController_temp extends MyAjaxBase {
	private static final Logger logger = LoggerFactory.getLogger(ReportController_temp.class);

	@Autowired
	private ConsoleLogService consoleLogService;

	@RequestMapping(value = "log/{brno}/{dt}/{pageNumber}", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ModelAndView viewSysLog(Model model, @PathVariable String brno, @PathVariable String dt, @PathVariable Integer pageNumber) throws Exception {
		// if(dt.length() == 0) dt = PoorManUtil.getToday();

		java.sql.Date date = PoorManUtil.stringToSqldate(dt.replaceAll("\\D+", ""));
		Page<ConsoleLog> page = consoleLogService.getConsoleLog(brno, date, pageNumber);

		int current = page.getNumber() + 1;
		int begin = Math.max(1, current - 5);
		int end = Math.min(begin + 10, page.getTotalPages());

		model.addAttribute("brno", brno);
		model.addAttribute("dt", dt);
		model.addAttribute("consoleLog", page);
		model.addAttribute("consoleLogList", page.getContent());
		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);

		return new ModelAndView("console/log");
	}

	@Autowired
	private SwiftUnsoMsgService swiftUnsoService;

	// http://localhost:8080/ifxweb2/mvc/rpt/swift/poll/brn
	// http://localhost:8080/ifxweb2/mvc/rpt/swift/poll/brn/?date=yyyyMMdd&type=0
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "swift/poll/{brn}", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> swiftPoll(@PathVariable String brn, @RequestParam(required = false) String date, @RequestParam(required = false) String type) {
		if (date == null) {
			date = PoorManUtil.getToday();
		}
		if (type == null)
			type = "0"; // 0:未印 , 1:全部

		logger.info(FilterUtils.escape("polling swift unso msg, brn:" + brn + "date:" + date + "type:" + type));

		HashMap m = new HashMap();

		StopWatch watch = new StopWatch();
		watch.start();
		try {
			List<SwiftUnsolicitedMsg> list = swiftUnsoService.get(brn, date);
			//
			logger.info("Total execution time to getValues in millis: " + watch.getTotalTimeMillis());

			// TODO convert to client format
			m.put("list", list);

		} catch (Exception ex) {
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			m.put("errmsg", errors.toString());
		}
		return makeJsonResponse(m);

	}

}
