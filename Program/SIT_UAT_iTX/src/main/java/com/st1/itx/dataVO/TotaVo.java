package com.st1.itx.dataVO;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.eum.ContentName;

/**
 * ToTaVo feedback data structure
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("totaVo")
@Scope("prototype")
public class TotaVo extends LinkedHashMap<String, Object> {
	private static final Logger logger = LoggerFactory.getLogger(TotaVo.class);

	private static final long serialVersionUID = -3395642179667569163L;

	private boolean isHtmlContent = false;

	private List<LinkedHashMap<String, String>> occursList = new ArrayList<LinkedHashMap<String, String>>();

	/**
	 * Associates the specified value with the specified key in this map. If the map
	 * previously contained a mapping for the key, the old value is replaced.
	 *
	 * @param key   key with which the specified value is to be associated
	 * @param param value to be associated with the specified key
	 */
	public void putParam(String key, Object param) {
		if (param instanceof Integer)
			this.put(key, new BigDecimal((Integer) (param == null ? 0 : param)).toPlainString());
		else if (param instanceof Double)
			this.put(key, new BigDecimal((Double) (param == null ? 0 : param)).toPlainString());
		else if (param instanceof Float)
			this.put(key, new BigDecimal((Float) (param == null ? 0 : param)).toPlainString());
		else if (param instanceof Long)
			this.put(key, new BigDecimal((Long) (param == null ? 0 : param)).toPlainString());
		else if (param instanceof BigDecimal)
			this.put(key, ((BigDecimal) (param == null ? 0 : param)).toPlainString());
		else
			this.put(key, param == null ? "" : param);
	}

	public TotaVo() {
		this.putParam(ContentName.sendTo, "iFX");
		this.putParam(ContentName.msgLen, "");
		this.putParam(ContentName.txrseq, "");
		this.putParam(ContentName.brno, "");
		this.putParam(ContentName.tlrno, "");
		this.putParam(ContentName.txtno, "");
		this.putParam(ContentName.caldy, "");
		this.putParam(ContentName.caltm, "");
		this.putParam(ContentName.msgEnd, "1");
		this.putParam(ContentName.txrsut, "S");
		this.putParam(ContentName.txcd, "");
		this.putParam(ContentName.msgId, "");
		this.putParam(ContentName.mldry, "");
		this.putParam(ContentName.mrKey, "");
		this.putParam(ContentName.filler, "");
		this.put("occursList", this.occursList);
	}

	/**
	 * init set txcd and msgid
	 * 
	 * @param titaVo titaVo
	 */
	public void init(TitaVo titaVo) {
		Date date = new Date();
		SimpleDateFormat fd = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat ft = new SimpleDateFormat("HHmmssSSS");
		this.putParam(ContentName.msgLen, "");
		this.putParam(ContentName.brno, titaVo.getBrno());
		this.putParam(ContentName.tlrno, titaVo.getTlrNo());
		this.putParam(ContentName.txtno, titaVo.getTxtNo());
		this.putParam(ContentName.txrseq, titaVo.getBrno() + titaVo.getTlrNo() + titaVo.getTxtNo());

		this.putParam(ContentName.caldy, fd.format(date));
		this.putParam(ContentName.caltm, ft.format(date).subSequence(0, 8));
		this.putParam(ContentName.txcd, titaVo.getTxcd());
		this.putParam(ContentName.msgId, titaVo.getTxCode());
	}

	/**
	 * get Json String
	 * 
	 * @return String this
	 * @throws LogicException if convert fail throw LogicException
	 */
	public String getJsonString() throws LogicException {
		try {
			if (this.isEmpty())
				return "";
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());

			throw new LogicException("CE000", "TempVo to String 轉換失敗 : " + this);
		}
	}

	/**
	 * get MsgEnd 2 || 3 = true
	 * 
	 * @return boolean isReentry true else false
	 */
	public boolean isReentry() {
		return this.get(ContentName.msgEnd).equals("2") || this.get(ContentName.msgEnd).equals("3");
	}

	/**
	 * Set Reentry Auto
	 */
	public void setMsgEndToAuto() {
		this.putParam(ContentName.msgEnd, "2");
	}

	/**
	 * Set Reentry Manual
	 */
	public void setMsgEndToEnter() {
		this.putParam(ContentName.msgEnd, "3");
	}

	/**
	 * Set TXRSUT to error
	 * 
	 * @return isError true else false
	 */
	public boolean isError() {
		return this.get(ContentName.txrsut).equals("E");
	}

	public boolean isWarn() {
		return this.get(ContentName.txrsut).equals("W");
	}

	public String getTxrsut() {
		return this.get(ContentName.txrsut) == null ? "" : (String) this.get(ContentName.txrsut);
	}

	/**
	 * Get TXCD
	 * 
	 * @return String txcd
	 */
	public String getTxcd() {
		return (String) this.get(ContentName.txcd);
	}

	/**
	 * get msgId
	 * 
	 * @return String msgId
	 */
	public String getMsgId() {
		return this.get(ContentName.msgId) == null ? "" : (String) this.get(ContentName.msgId);
	}

	/**
	 * add list for search
	 * 
	 * @param map OccursEntity
	 */
	public void addOccursList(LinkedHashMap<String, String> map) {
		this.occursList.add(map);
	}

	/**
	 * 
	 * @return ArrayList LinkedHashMap
	 */
	public List<LinkedHashMap<String, String>> getOccursList() {
		return this.occursList;
	}

	/**
	 * set Txrsut to Error
	 */
	public void setTxrsutE() {
		this.putParam(ContentName.txrsut, "E");
	}

	/**
	 * set MsgId
	 * 
	 * @param msgId MsgId
	 */
	public void setMsgId(String msgId) {
		this.putParam(ContentName.msgId, msgId);
	}

	/**
	 * set ErrorMsgId
	 * 
	 * @param eMsgId String
	 */
	public void setErrorMsgId(String eMsgId) {
		this.putParam(ContentName.msgId, eMsgId);
	}

	/**
	 * getErrorMsg
	 * 
	 * @return String ErrorMsg
	 */
	public String getErrorMsg() {
		return (String) (this.get(ContentName.errorMsg) == null ? "" : this.get(ContentName.errorMsg));
	}

	/**
	 * set ErrorMsg
	 * 
	 * @param msg String
	 */
	public void setErrorMsg(String msg) {
		this.putParam(ContentName.errorMsg, msg);
	}

	/**
	 * get sendTo location
	 * 
	 * @return String sendTo location
	 */
	public String getSendTo() {
		return (String) this.get(ContentName.sendTo);
	}

	/**
	 * set msg send to iFX
	 */
	public void setSendToiFX() {
		this.putParam(ContentName.sendTo, "iFX");
	}

	/**
	 * set this Tota Object is Warning Msg
	 * 
	 * @param msg String msg
	 */
	public void setWarnMsg(String msg) {
		this.putParam(ContentName.txrsut, "W");
		this.putParam(ContentName.warnMsg, msg);
	}

	/**
	 * set EC TitaVo
	 * 
	 * @param titaVo EC TitaVo
	 */
	public void setEcTitaVo(TitaVo titaVo) {
		TitaVo tita = (TitaVo) titaVo.clone();
		tita.putParam(ContentName.msgLen, "");
		tita.putParam(ContentName.brno, tita.getBrno());
		tita.putParam(ContentName.msgEnd, "1");
		tita.putParam(ContentName.txrsut, "S");
		tita.putParam(ContentName.msgId, tita.getTxcd());
		tita.putParam(ContentName.mldry, "");
		tita.putParam(ContentName.filler, "");
		this.put("EC", tita);
	}

	/**
	 * get EC TitaVo
	 * 
	 * @return EC TitaVo
	 */
	public TitaVo getEcTitaVo() {
		return (TitaVo) this.get("EC");
	}

	public boolean isHtmlContent() {
		return isHtmlContent;
	}

	public void setHtmlContent(String msg) {
		this.putParam(ContentName.noticeMsg, msg);
		this.isHtmlContent = true;
		// 固定MSGID
		this.setMsgId("NO001");
	}

}
