package com.st1.itx.aop;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxApLog;
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.TxApLogListService;
import com.st1.itx.db.service.TxApLogService;
import com.st1.itx.db.service.TxTranCodeService;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.log.SysLogger;

@Aspect
public class LogAspect extends SysLogger {

	@Autowired
	TxApLogService txApLogService;

	@Autowired
	TxApLogListService txApLogListService;

	@Autowired
	TxTranCodeService txTranCodeService;

	@Autowired
	DateUtil dateUtil;

	@Value("${url}")
	private String url = "";

	private String hostName = "";

	@PostConstruct
	private void init() {
		this.info("LogAspect init...");
		try {
			hostName = InetAddress.getLocalHost().getHostName().toString();
		} catch (UnknownHostException e) {
			this.error("get hostName Fail....");

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}
	}

	@Before("execution(* com.st1.itx.trade..run(..))")
	public void logBefore(JoinPoint joinPoint) {
		this.info("logBefore() is running!");
		Object[] args = joinPoint.getArgs();

		for (int i = 0; i < args.length; i++)
			this.info("params[" + i + "]:" + args[i].toString());

//		logger.info("hijacked : " + joinPoint.getSignature().getName());
//		logger.info("******");
	}

	@After("execution(* com.st1.itx.trade..run(..))")
	public void logAfter(JoinPoint joinPoint) {
		this.info("logAfter() is running!");
		this.info("hijacked : " + joinPoint.getSignature().getName());
//		logger.info("******");
	}

	@AfterReturning(pointcut = "execution(* com.st1.itx.trade..run(..))", returning = "result")
	public void logAfterReturning(JoinPoint joinPoint, Object result) throws LogicException {
		this.info("logAfterReturning() is running!");
		this.info("Method returned value is : " + result);

		Object[] args = joinPoint.getArgs();

		TitaVo titaVo = (TitaVo) args[0];

//		TxApLogList txApLogList = txApLogListService.findById(titaVo.getTxCode());

//		if (txApLogList != null) {
		TxApLog txApLog = new TxApLog();
		txApLog.setEntdy(dateUtil.getNowIntegerForBC());
		txApLog.setTlrNo(titaVo.getTlrNo());
		txApLog.setAct("Query");
		txApLog.setActTime(dateUtil.getNowStringTime());
		txApLog.setIp(titaVo.getIp());
		txApLog.setSystemName("iTX");
		txApLog.setServerIp(url);
		txApLog.setServerName(hostName);

		TxTranCode txTranCode = txTranCodeService.findById(titaVo.getTxCode());
		txApLog.setActName(txTranCode == null ? titaVo.getTxCode() : txTranCode.getTranItem());

		txApLog.setPgName(joinPoint.getTarget().getClass().getName());
		txApLog.setMethodName("run");

		try {
			txApLog.setInParam(titaVo.getJsonString());
		} catch (LogicException e) {
			txApLog.setInParam("get TitaVo String Error...");
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

		if (result == null)
			txApLog.setResultStatus("E");
		else
			txApLog.setResultStatus("S");

		try {
			txApLogService.insert(txApLog);
		} catch (DBException e) {
			this.error(e.getErrorMsg());
		}
//		}

	}

	@AfterThrowing(pointcut = "execution(* com.st1.itx.trade..run(..))", throwing = "error")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
		this.info("logAfterThrowing() is running!");
		this.info("Exception : " + error);
	}
}
