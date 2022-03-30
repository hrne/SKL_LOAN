package com.st1.ifx.hcomm.app;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.st1.ifx.dataVo.TitaVo;
import com.st1.ifx.dataVo.TotaVo;
import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.hcomm.fmt.FormatUtil;
import com.st1.ifx.hcomm.fmt.HostFormatter;
import com.st1.ifx.hcomm.sock.SockSender;
import com.st1.servlet.GlobalValues;
import com.st1.sklwebservice.WsSKLAuthentication;
import com.st1.sklwebservice.WsSKLAuthenticationSoap;
import com.st1.util.EncrypAES;
import com.st1.util.MySpring;
import com.st1.util.getContent.ContentName;

@Component("imsLoginSock")
@Scope("prototype")
public class ImsLoginSock extends ImsTran {
	static final Logger logger = LoggerFactory.getLogger(ImsLoginSock.class);

	@Autowired
	private SimpleJournal simpleJournal;

	@Value("${sna_adUrl}")
	private String adUrl = "";

	private String ip = "";

	String msgId = "";

	String proxy;
	String user;
	String password;
	String authNo;
	String authItem;
	String agentNo;
	String agentItem;

	String address;
	SessionMap sessionMap;
	String errmsg;

	String txcode = "LC100";
	String key;

	String logfile;

	boolean adStatus = true;

	public ImsLoginSock() {
		logger.info("ImsLogin empyty constructor");
	}

	public String hello() {
		return "hello";
	}

	public static void setHi(String hi) {
		logger.info("setHi " + hi);
	}

	public ImsLoginSock(String proxy, String user, String password, String address) {
		// init(proxy, user, password, address);
	}

	public SessionMap getSession() {
		return sessionMap;
	}

	public String getSessionAsJSON() throws Exception {
		StringWriter writer = new StringWriter();
		ObjectMapper m = new ObjectMapper();
		m.writeValue(writer, sessionMap);
		return writer.toString();
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> rimLc013() {
		logger.info("Rim LC013");
		return (List<Map<String, String>>) this.perform_Lc013().get("occursList");
	}

	public boolean perform() {
		try {
			sessionMap = SessionMap.newSessionMap();

			boolean ok = perform_c1200();
			if (ok) {
				// save session
				try {
					sessionMap.storeSession(this.user);
				} catch (IOException e) {
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					logger.error(errors.toString());
				}
			}
			logger.info("HostTran journalWanted:" + GlobalValues.journalWanted);
			if (GlobalValues.journalWanted) {
				simpleJournal.setEntDay(sessionMap.get("FDATE")); // ENTDD更改成FDATE
				// 有無需要其他資料?
				// simpleJournal.setTxno(sessionMap.get("TXTNO"));
				simpleJournal.updateJournal(ok, msgId, errmsg, "");
			}

			return ok;
		} catch (Exception ex) {
			errmsg = ex.getMessage();
			logger.error(ex.getMessage());
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());

		}
		return false;
	}

	private TotaVo perform_Lc013() {
		List<TotaVo> totaVoLi = null;
		TotaVo totaVo = null;
		try {
			String tita = this.build_lc013();
			TitaVo titaVo = new TitaVo(tita, "");

			logfile = getLogFileName(titaVo.getTlrNo());
			writeLog(logfile, "LC013", key, tita, "TITA");

			/*
			 * 壓測 Timestamp startTime = new Timestamp(new Date().getTime()); int i = 0;
			 * while (true) { new Thread(new Runnable() {
			 * 
			 * @Override public void run() { SockSender sockSender =
			 * MySpring.getBean("sockSender", SockSender.class); try {
			 * sockSender.send(titaVo, titaVo.getTxcd()); } catch (Exception e2) {
			 * StringWriter errors = new StringWriter(); e2.printStackTrace(new
			 * PrintWriter(errors)); logger.error(errors.toString()); } } }).start();
			 * 
			 * new Thread(new Runnable() {
			 * 
			 * @Override public void run() { SockSender sockSender =
			 * MySpring.getBean("sockSender", SockSender.class); try {
			 * sockSender.send(titaVo, titaVo.getTxcd()); } catch (Exception e2) {
			 * StringWriter errors = new StringWriter(); e2.printStackTrace(new
			 * PrintWriter(errors)); logger.error(errors.toString()); } } }).start(); i +=
			 * 2; Thread.sleep(100);
			 * 
			 * Timestamp endTime = new Timestamp(new Date().getTime()); int hours = (int)
			 * ((endTime.getTime() - startTime.getTime()) / (1000 * 60 * 60)); int minutes =
			 * (int) (((endTime.getTime() - startTime.getTime()) / 1000 - hours * (60 * 60))
			 * / 60); int second = (int) ((endTime.getTime() - startTime.getTime()) / 1000 -
			 * hours * (60 * 60) - minutes * 60);
			 * 
			 * if (second >= 30) { logger.info("sendCount : [" + i + "]"); break; } }
			 */
			totaVoLi = sockSender.send(titaVo, titaVo.getTxcd());

			totaVo = totaVoLi.get(0);

			logger.info("LC013 : " + totaVo.toJsonString());

			try {
				// AD 驗證
				String adFg = (String) totaVo.get("AdFg");
				if (adFg != null && adFg.trim().equals("1")) {
					if (this.getAdUrl().equals("https://t-ws.skl.com.tw/RequirementWebService/wsSkl_Authentlication.asmx?wsdl"))
						;

					URL url = new URL(this.getAdUrl());

					WsSKLAuthentication ss = new WsSKLAuthentication(url);

					WsSKLAuthenticationSoap port = ss.getWsSKLAuthenticationSoap();

					this.setAdStatus(port.adIsAuthenticated(this.getUser().trim(), this.getPassword().trim()));

					logger.info("Ad驗證 : " + this.getAdUrl());
					logger.info("USER : " + this.getUser());
					logger.info("PWD  : " + this.getPassword());
					logger.info("Ad Is Good ? " + this.isAdStatus());
				}
			} catch (Exception e2) {
				this.setAdStatus(false);
				StringWriter errors = new StringWriter();
				e2.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
			}

		} catch (Exception ex) {
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
		return totaVo;
	}

	@SuppressWarnings("unused")
	private boolean perform_c1200() {
		sessionMap.touch();
		List<TotaVo> totaVoLi = null;

		try {
			String tita = build_c1200();
			TitaVo titaVo = new TitaVo(tita, "");
			if (GlobalValues.journalWanted)
				prelogJournal(tita);

			logfile = getLogFileName(titaVo.getTlrNo());
			logger.info("logfile:[" + FilterUtils.escape(logfile) + "]");

			writeLog(logfile, txcode, key, tita, "TITA");
			totaVoLi = sockSender.send(titaVo, titaVo.getTxcd());
		} catch (Exception ex) {
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());

			errmsg = "perform_c1200 fail.. 198";
			writeLog(logfile, txcode, key, errmsg, "ERROR");
			return false;
		}

		if (Objects.isNull(totaVoLi) || totaVoLi.size() == 0) {
			errmsg = "TotaVoLi Is Null Or Empty";
			writeLog(logfile, txcode, key, errmsg, "ERROR");
			return false;
		} else {
			try {
				String tota = totaVoLi.get(0).toJsonString();
				writeLog(logfile, txcode, key, tota, "TOTA");
				return process_c1200_tota(totaVoLi.get(0), key);
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());

				errmsg = "TotaVo Parse To String Error";
				writeLog(logfile, txcode, key, errmsg, "ERROR");
				return false;
			}
		}
	}

	private boolean process_c1200_tota(TotaVo totaVo, String key) throws JsonProcessingException {
		logger.info("process_c1200_tota:" + totaVo.toJsonString());

		String txrsut = totaVo.getTxrsut();
		msgId = totaVo.getMsgId();
		logger.info("txrsut:" + txrsut + ",msgId:" + msgId);

		if (totaVo.isError()) {
			errmsg = msgId + ":" + totaVo.getErrorMsg();
			logger.info("C1200, errmsg:" + errmsg);
			if (GlobalValues.journalWanted)
				simpleJournal.updateJournal(false, msgId, errmsg, "");
			return false;
		}

		sessionMap.put("BRN", totaVo.getBrNo());
		sessionMap.put("BNAM", totaVo.get("BRNAME"));
		sessionMap.put("CLASS", totaVo.get("CLASS"));
		sessionMap.put("BKNO", totaVo.get("BANKNO"));
		sessionMap.put("FXLVL", totaVo.get("FXLVL"));
		sessionMap.put("BCURCD", totaVo.get("BCURCD"));
		sessionMap.put("FXBRNO", totaVo.get("FXBRNO"));
		sessionMap.put("FINBRNO", totaVo.get("FINBRNO"));
		sessionMap.put("OBUBRNO", totaVo.get("OBUBRNO"));
		sessionMap.put("LEVEL", totaVo.get("LEVEL"));

		sessionMap.put("EMPNM", totaVo.get("NAME"));
		if (agentItem != null && !agentItem.trim().isEmpty())
			sessionMap.put("AgentItem", agentItem);
		else
			sessionMap.put("AgentItem", "");

		sessionMap.put("EMPNO", totaVo.get("EMPNO"));
		sessionMap.put("TXGRP", totaVo.get("TXGRP"));
		sessionMap.put("TXTNO", totaVo.getTxtNo());
		logger.info(FilterUtils.escape(this.user + " last host seq:" + totaVo.get("TXTNO")));

		sessionMap.put("NDAYMOD", totaVo.get("MODE"));
		sessionMap.put("DBTO", totaVo.get("DBTO"));

		String dt = (String) totaVo.get("TXDATE");
		if (dt.length() == 7)
			dt = "0" + dt;
		String bcDt = convertToBCDate(dt);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		sessionMap.put("DATE", dt);
		sessionMap.put("CDATE", convertToRocDate(sdf.format(new Date())));
		sessionMap.put("FDATE", bcDt);
		sessionMap.put("ENTDY", dt);
		sessionMap.put("TLRNO", user);

		sessionMap.put("SWFCD", totaVo.get("SWFCD"));
		sessionMap.put("LSNTAMT", totaVo.get("LSNTAMT"));
		sessionMap.put("LLSNTAMT", totaVo.get("LLSNTAMT"));
		// 下營業日
		sessionMap.put("NBSDY", totaVo.get("NBSDY"));
		// 下下營業日
		sessionMap.put("NNBSDY", totaVo.get("NNBSDY"));
		// 上營業日
		sessionMap.put("LBSDY", totaVo.get("LBSDY"));
		// 本月月底營業日
		sessionMap.put("MFBSDY", totaVo.get("MFBSDY"));
		sessionMap.put("CLDEPT", totaVo.get("CLDEPT"));
		sessionMap.put("CLDEPTNA", totaVo.get("CLDEPTNA"));
		sessionMap.put("DAPKND", totaVo.get("DAPKND"));
		sessionMap.put("OAPKND", totaVo.get("OAPKND"));
		// 管轄行
		sessionMap.put("MBRNO", totaVo.get("MBRNO"));
		sessionMap.put("ADNO", totaVo.get("ADNO"));
		sessionMap.put("DISFORM", totaVo.get("DISFORM"));
		try {
			String lsLogin = (String) totaVo.get("LSLOGIN");
			int year = Integer.parseInt(lsLogin.substring(0, 4));
			if (year > 1911) {
				year = year - 1911;
				lsLogin = Integer.toString(year) + lsLogin.substring(4);
			}
			lsLogin = lsLogin.substring(1);
			SimpleDateFormat sdfSource = new SimpleDateFormat("yyyMMddHHmmss");
			Date date = sdfSource.parse(lsLogin);
			SimpleDateFormat sdfDestination = new SimpleDateFormat("yyy/MM/dd, HH:mm:ss");
			sessionMap.put("LSLOGIN", sdfDestination.format(date));
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			sessionMap.put("LSLOGIN", "");
		}
		// 初始化 TIMEOUT 序號
		sessionMap.put("ERTXTNO", "");

		sessionMap.put("AUTHNO", this.getAuthNo());
		sessionMap.put("AUTHITEM", this.getAuthItem());
		sessionMap.put("AGENT", this.getAgentNo());

		// 使用者登入IP
		sessionMap.put("IP", this.getIp());

		if (errmsg != null)
			sessionMap.put("C12_WARN", errmsg);

		FormatUtil.dumpMap(sessionMap);

		// 儲存加密後密碼,FOR主管授權時重新輸入
		String sealUrl = GlobalValues.sealUrl;
		String adno = totaVo.get("ADNO").toString().trim();
		String pswd = totaVo.get("PSWD").toString().trim();

		sessionMap.put("PSWD", EncrypAES.userEncrytor(pswd));

		logger.info("adno:" + FilterUtils.escape(adno) + ",pswd len:" + pswd.length());
		logger.info("sealUrl:" + FilterUtils.escape(sealUrl));

		if (GlobalValues.journalWanted)
			simpleJournal.updateJournal(true, msgId, "", "");

		return true;
	}

	int seq;

	private String build_c1200() throws JsonProcessingException {
		this.key = user + "00000000";
		Map<String, String> map = initTitaLabel();
		map.put("TLRNO", user.substring(0, 6));
		map.put("EMPNOT", user.substring(0, 6));
		map.put("PSWD", password);
		map.put("AUTHNO", this.getAuthNo());
		map.put("AGENT", this.getAgentNo());
		map.put("IP", this.getIp());
		map.put(ContentName.txCode, "LC100");
		logger.info("building " + this.txcode + " tita......");
		HostFormatter formatter = new HostFormatter("C1200.tim");

		return formatter.format(true, map);
	}

	private String build_lc013() throws JsonProcessingException {
		Map<String, String> map = initTitaLabel();
		map.put("TLRNO", user);
		map.put("EMPNOT", user);
		map.put(ContentName.txCode, "LC013");

		HostFormatter formatter = new HostFormatter("LC013.tim");
		String tita = formatter.format(true, map);

		return tita;
	}

	private void prelogJournal(String tita) {
		// String txno = tita.substring(HostTran.hostSeqStart, HostTran.hostSeqEnd);
		simpleJournal.setBrn(user.substring(0, 4));
		simpleJournal.setTlrno(user.substring(4));
		simpleJournal.setTxcode(txcode);
		simpleJournal.setEntDay("00000000");
		// simpleJournal.setTxno("00000000");
		simpleJournal.prelogJournal(tita);
	}

	private Map<String, String> initTitaLabel() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("KINBR", user.substring(0, 4));
		// map.put("TLRNO", user.substring(4));
		map.put("TXTNO", "0");
		map.put("ENTDY", "0");
		map.put("TXCD", txcode);
		return map;
	}

	private static String convertToBCDate(String dt) {
		String s = Integer.toString(Integer.parseInt(dt) + 19110000);
		return FormatUtil.pad9(s, 8);
	}

	private static String convertToRocDate(String dt) {
		String s = Integer.toString(Integer.parseInt(dt) - 19110000);
		return FormatUtil.pad9(s, 8);
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAuthNo() {
		return authNo;
	}

	public void setAuthNo(String authNo) {
		this.authNo = authNo;
	}

	public String getAuthItem() {
		return authItem;
	}

	public void setAuthItem(String authItem) {
		this.authItem = authItem;
	}

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}

	public String getAgentItem() {
		return agentItem;
	}

	public void setAgentItem(String agentItem) {
		this.agentItem = agentItem;
	}

	public String getAdUrl() {
		return adUrl;
	}

	public void setAdUrl(String adUrl) {
		this.adUrl = adUrl;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public boolean isAdStatus() {
		return adStatus;
	}

	public void setAdStatus(boolean adStatus) {
		this.adStatus = adStatus;
	}

}
