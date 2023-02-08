package com.st1.ifx.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Time;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.st1.ifx.domain.MsgBox;
import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.service.CodeListService;
import com.st1.ifx.service.MsgService;
import com.st1.msw.MswCenter;
import com.st1.servlet.GlobalValues;
import com.st1.util.Base64;
import com.st1.util.MySpring;

@Controller
@RequestMapping("msw/*")
public class MswController {
	static final Logger logger = LoggerFactory.getLogger(MswController.class);

	@RequestMapping(value = "sendTo", method = { RequestMethod.POST, RequestMethod.GET })
	public void mswSendTo(@RequestParam String p0, @RequestParam String p1, @RequestParam String sessionId, @RequestParam String p2, @RequestParam String p3, @RequestParam String methodName,
			@RequestParam String statusCode) {
		// @RequestParam String _d
		logger.info("MswController!!!!!");
		// Gson gson = new Gson();
		// HashMap<String, String> m = new HashMap<String, String>();
		// m = (HashMap<String, String>) gson.fromJson(_d, m.getClass());

		// logger.info(FilterUtils.escape("m.get(sessionId):" + m.get("sessionId")));
		// logger.info(FilterUtils.escape("m.get(methodName):" + m.get("methodName")));
		// logger.info(FilterUtils.escape("m.get(statusCode):" + m.get("statusCode")));
		// logger.info(FilterUtils.escape("m.get(p):" + m.get("p")));
		logger.info(FilterUtils.escape("sessionId:" + sessionId));
		logger.info(FilterUtils.escape("methodName:" + methodName));
		logger.info(FilterUtils.escape("statusCode:" + statusCode));
		Object[] op = new Object[4];
		try {
			if (p0 != null && !p0.isEmpty()) {
				logger.info(FilterUtils.escape("p0:" + p0));
				op[0] = Base64.decodeToObject(p0);
			}
			if (p1 != null && !p1.isEmpty()) {
				logger.info(FilterUtils.escape("p1:" + p1));
				op[1] = Base64.decodeToObject(p1);
			}
			if (p2 != null && !p2.isEmpty()) {
				logger.info(FilterUtils.escape("p2:" + p2));
				op[2] = Base64.decodeToObject(p2);
			}
			if (p3 != null && !p3.isEmpty()) {
				logger.info(FilterUtils.escape("p3:" + p3));
				op[3] = Base64.decodeToObject(p3);
			}
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} catch (ClassNotFoundException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}

		MswCenter _ms = MswCenter.getInstance();
		logger.info("before mswSendTo!");
		_ms.sendTo(sessionId, methodName, Integer.parseInt(statusCode), op);
	}

	@RequestMapping(value = "talk/{to}/{message}", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public void sendTalkto(@PathVariable String to, @PathVariable String message) {
		logger.info(FilterUtils.escape("sendTalkto...., to:" + to + ", message:" + message));
		MswCenter _ms = MswCenter.getInstance();
		_ms.talk(to, message);

	}

	// 通知測試ok
	@RequestMapping(value = "systemTalk/{talker}/{to}/{message}", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public void sendSystemTalk(@PathVariable String talker, @PathVariable String to, @PathVariable String message) {
		message = message.replaceAll("_", " ").replaceAll("\\@", ".");
		logger.info(FilterUtils.escape("sendSystemTalk....,talker:" + talker + "to:" + to + ", message:" + message));

		String vDate = message.substring(0, 8);
		String vTime = message.substring(8, 12);

		MsgService msgService = MySpring.getMsgService();
		MsgBox msgBox = new MsgBox();

		java.util.Date today = new java.util.Date();
		long t = today.getTime();

		msgBox.setBrno("0000");
		msgBox.setContent(message.substring(17));
		msgBox.setDone('N');
		msgBox.setMsgno("00000");
		msgBox.setRcvDate(new java.sql.Date(t));
		msgBox.setRcvTime(new Time(t));
		msgBox.setTlrno(to);
		msgBox.setValidTime(Long.parseLong(vDate + vTime));
		msgBox.setViewDate(null);
		msgBox.setViewTime(null);
		logger.info("befor Apan save");
		msgService.save(msgBox);

		MswCenter _ms = MswCenter.getInstance();
		_ms.systemTalk(talker, to, message.substring(17));

	}

	// 更新版號,直接清除快取,並同步版號 (清兩次沒差)
	@RequestMapping(value = "upVersion/{what}/{version}", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public void sendUpVersion(@PathVariable String what, @PathVariable String version) {
		logger.info(FilterUtils.escape("sendUpVersion....,what:" + what + "version:" + version));
		// TODO UPDATE VERSION
		if (what.equals("h")) {
			CodeListService service = MySpring.getCodeListService();
			logger.info("update webhdb successfully");
			service.evict();
			GlobalValues.upelpjsVersion(version);
		}
	}

}
