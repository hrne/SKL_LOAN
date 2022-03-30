package com.st1.ifx.swiftauto;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.hcomm.app.ImsTranBase;
import com.st1.ifx.hcomm.fmt.Env;
import com.st1.ifx.hcomm.fmt.HostFormatter;
import com.st1.ifx.hcomm.sock.SockSender;
import com.st1.servlet.GlobalValues;
import com.st1.util.PoorManUtil;

@Component
public class BicQuery extends ImsTranBase {
	static final Logger logger = LoggerFactory.getLogger(BicQuery.class);

	@Autowired
	private ApplicationContext applicationContext;

	// implements ApplicationContextAware
	// @Override
	// public void setApplicationContext(ApplicationContext ctx)
	// throws BeansException {
	// this.applicationContext = ctx;
	//
	// }
	private SockSender createSockSender() {
		return this.applicationContext.getBean(SockSender.class);
	}

	String txcode = "XWR01";
	String key;
	String errmsg;
	String tlrno;
	String brno;
	String msgType;
	String dest;

	long lastSeq = 1;

	Map<String, String> resultMap;

	String logfile;

	// SockSender sockSender;
	@Override
	public boolean perform() {
		logfile = getLogFileName(this.brno + this.tlrno);
		logger.info("logfile:[" + FilterUtils.escape(logfile) + "]");

		SockSender sockSender = createSockSender();

		boolean ok = false;
		try {
			while (true) {
				String tita = buildTita() + "$PRINT"; // -> PRINT 不要發錯誤訊息回來
				writeLog(logfile, txcode, key, tita, "TITA");
				logger.info("tita:" + tita);
				String output = "";
				/*
				 * String output = sockSender.send(tita, HostTran.gettitaMsgTxcd(tita),
				 * HostTran.gettitaMsgId(tita), HostTran.gettitaMsgIdisUpdate(tita)); // wait
				 * modify to LinkedHashMap apan
				 */
				// 這邊是否有頭? msg len offset之類的
				logger.info("output:" + output);
				writeLog(logfile, txcode, key, output, "TOTA");
				int LngOffset = sockSender.getMsglngOffset();
				ok = processOutput(output, LngOffset);
				if (!ok && msgId.equals("CE999")) {
					logger.info("_T:" + (String) resultMap.get("_T"));
					String sSeq = ((String) resultMap.get("_T")).substring(16).trim();
					lastSeq = Long.parseLong(sSeq);
					logger.info("change seq to " + lastSeq);
					logger.info("send again");
					continue;
				}
				break;
			}
			return ok;
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			errmsg = ex.getMessage();
			return false;

		}
	}

	String msgId = "";

	private boolean processOutput(String tota, int LngOffset) {
		if (tota.length() < GlobalValues.minTotaLen) {
			logger.error("output length is too short:" + tota.length());
			return false;
		}
		logger.info("org tota:" + tota);
		logger.info("snaHeadDS:" + GlobalValues.snaHeadDS);
		tota = tota.substring(GlobalValues.snaHeadDS);
		logger.info("tmp tota:" + tota);
		tota = tota.substring(LngOffset);
		logger.info("LngOffset:" + LngOffset);
		logger.info("end tota:" + tota);
		HostFormatter formatter = new HostFormatter("XWR01_XWR01.tom");
		resultMap = formatter.parse(true, tota);
		String txrsut = (String) resultMap.get("TXRSUT");
		msgId = (String) resultMap.get("MSGID");

		logger.info("txrsut:" + txrsut + ",msgId:" + msgId);
		if (txrsut.equals("E")) {
			errmsg = msgId + ":" + (String) resultMap.get("_T");
			logger.info("XWR01, errmsg:" + errmsg);
			return false;
		}
		return true;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String buildTita() throws JsonProcessingException {
		this.key = tlrno + "00000000";
		LinkedHashMap map = new LinkedHashMap();
		map.put("KINBR", brno);
		map.put("TLRNO", tlrno);
		map.put("TXTNO", String.valueOf(++lastSeq));
		map.put("ENTDY", "00");
		map.put("TXCD", this.txcode);

		map.put("EMPNOT", brno + tlrno);
		map.put("EMPNOS", "");

		map.put("CALDY", "00000000");
		map.put("CALTM", PoorManUtil.getNowwithFormat("HHmmssSSS").substring(0, 8));

		map.put("MTTPSEQ", "00");
		map.put("TOTAFG", "0");
		map.put("OBUFG", "1");

		map.put("ACBRNO", brno);
		map.put("RBRNO", brno);
		map.put("FBRNO", brno);

		map.put("RELCD", "1");
		map.put("ACTFG", "0");
		map.put("FILLER_FINAL", "  0000");

		map.put("DEST", dest);
		map.put("MSGTYPE", msgType);
		logger.info("building " + this.txcode + " tita......");
		HostFormatter formatter = new HostFormatter("XWR01.tim");
		return formatter.format(true, map);
	}

	public String getTxcode() {
		return txcode;
	}

	public void setTxcode(String txcode) {
		this.txcode = txcode;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getBrno() {
		return brno;
	}

	public void setBrno(String brno) {
		this.brno = brno;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public Map<String, String> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<String, String> resultMap) {
		this.resultMap = resultMap;
	}

	public String getTlrno() {
		return tlrno;
	}

	public void setTlrno(String tlrno) {
		this.tlrno = tlrno;
	}

}
