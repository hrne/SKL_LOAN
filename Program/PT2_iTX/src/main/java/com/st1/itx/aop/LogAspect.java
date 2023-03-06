package com.st1.itx.aop;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

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
import com.st1.itx.db.service.TxApLogService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.log.SysLogger;

@Aspect
public class LogAspect extends SysLogger {

	@Autowired
	private TxApLogService txApLogService;

	@Autowired
	private DateUtil dateUtil;

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

		if (!titaVo.isAplogOn() && !titaVo.isTxcdSpecial())
			return;

		if (titaVo.isRim() && !titaVo.getTxCode().equals(titaVo.getAplogRim()) && !titaVo.isTxcdSpecial())
			return;

		TxApLog txApLog = new TxApLog();
		txApLog.setEntdy(dateUtil.getNowIntegerForBC());
		txApLog.setUserID(titaVo.getTlrNo());
//		txApLog.setIDNumber              ();
		txApLog.setIDName(titaVo.getEmpNm());

		int event = -1;
		if (titaVo.isFuncindCopy() || titaVo.isFuncindModify())
			event = 3;
		else if (titaVo.isFuncindNew())
			event = 1;
		else if (titaVo.isFuncindDel())
			event = 2;
		else if (titaVo.isFuncindInquire())
			event = 4;

		if (titaVo.isTxcdSpecial() && titaVo.getTxCode().equals("LC100")) {
			event = 8;
			titaVo.putParam(ContentName.txCodeNM, "登入");
		}
		if (titaVo.isTxcdSpecial() && titaVo.getTxCode().equals("LC101")) {
			event = 9;
			titaVo.putParam(ContentName.txCodeNM, "登出");
		}

		if (event == -1)
			return;

		txApLog.setActionEvent(event);
		txApLog.setUserIP(titaVo.getIp());
		txApLog.setSystemName("放款帳務系統");
		txApLog.setOperationName(titaVo.getTxCodeNM());
		txApLog.setProgramName(titaVo.getTxCode());
		txApLog.setMethodName("run");
		txApLog.setServerName(hostName);
		txApLog.setServerIP(url);
		txApLog.setInputDataforXMLorJson(titaVo.getJsonString());
		txApLog.setOutputDataforXMLorJson("");
		txApLog.setEnforcementResult(Objects.isNull(result) ? 1 : 0);
		txApLog.setMessage("");

		try {
			txApLogService.insert(txApLog);
		} catch (DBException e) {
			this.error(e.getErrorMsg());
		}
	}

	@AfterThrowing(pointcut = "execution(* com.st1.itx.trade..run(..))", throwing = "error")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
		this.info("logAfterThrowing() is running!");
		this.info("Exception : " + error);
	}
}
