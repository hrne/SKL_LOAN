package com.st1.ifx.dataVo;

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
import com.st1.util.getContent.ContentName;

/**
 * ToTaVo feedback data structure
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("totaVo")
@Scope("prototype")
public class TotaVo extends LinkedHashMap<String, Object> {

	private static final long serialVersionUID = -3395642179667569163L;

	private boolean isHtmlContent = false;

	private List<LinkedHashMap<String, String>> occursList = new ArrayList<LinkedHashMap<String, String>>();

	public TotaVo() {
	}

	public TotaVo(TitaVo titaVo) {
		Date date = new Date();
		SimpleDateFormat fd = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat ft = new SimpleDateFormat("HHmmssSSS");
		this.putParam(ContentName.brno, titaVo.getBrno());
		this.putParam(ContentName.tlrno, titaVo.getTlrNo());
		this.putParam(ContentName.txtNo, titaVo.getTxtNo());
		this.putParam(ContentName.txrSeq, titaVo.getBrno() + titaVo.getTlrNo() + titaVo.getTxtNo());

		this.putParam(ContentName.caldy, fd.format(date));
		this.putParam(ContentName.caltm, ft.format(date).subSequence(0, 8));
		this.putParam(ContentName.txcd, titaVo.getTxcd());
		this.putParam(ContentName.msgId, titaVo.getTxCode());
	}

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

	/**
	 * get Json String
	 * 
	 * @return String this
	 * @throws JsonProcessingException if convert fail
	 */
	public String toJsonString() throws JsonProcessingException {
		if (this.isEmpty())
			return "";
		return new ObjectMapper().writeValueAsString(this);
	}

	/**
	 * Brno 分行
	 * 
	 * @return String
	 */
	public String getBrNo() {
		return this.get(ContentName.brno) == null ? "" : (String) this.get(ContentName.brno);
	}

	/**
	 * TlrNo 交易員編號
	 * 
	 * @return String
	 */
	public String getTlrNo() {
		return this.get(ContentName.tlrno) == null ? "" : (String) this.get(ContentName.tlrno);
	}

	/**
	 * get txtNo len 8
	 * 
	 * @return String
	 */
	public String getTxtNo() {
		return this.get(ContentName.txtNo) == null ? "" : (String) this.get(ContentName.txtNo);
	}

	/**
	 * 交易日曆日
	 * 
	 * @return String
	 */
	public String getCalDy() {
		return this.get(ContentName.caldy) == null ? "" : (String) this.get(ContentName.caldy);
	}

	/**
	 * 交易時間
	 * 
	 * @return String
	 */
	public String getCalTm() {
		return this.get(ContentName.caltm) == null ? "" : (String) this.get(ContentName.caltm);
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
	 * 結束記號
	 * 
	 * @return String
	 */
	public String getMsgEnd() {
		return this.get(ContentName.msgEnd) == null ? "" : (String) this.get(ContentName.msgEnd);
	}

	/**
	 * get if TXRSUT is E
	 * 
	 * @return isError true else false
	 */
	public boolean isError() {
		return this.get(ContentName.txrsut).equals("E");
	}

	/**
	 * get if TXRSUT is W
	 * 
	 * @return isError true else false
	 */
	public boolean isWarn() {
		return this.get(ContentName.txrsut).equals("W");
	}

	/**
	 * get Txrsut Good S Error E
	 * 
	 * @return String
	 */
	public String getTxrsut() {
		return this.get(ContentName.txrsut) == null ? "" : (String) this.get(ContentName.txrsut);
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
	 * get MLDRY
	 * 
	 * @return String
	 */
	public String getMlDry() {
		return this.get(ContentName.mldry) == null ? "" : (String) this.get(ContentName.mldry);
	}

	/**
	 * get MrKey
	 * 
	 * @return String
	 */
	public String getMrkey() {
		return this.get(ContentName.mrKey) == null ? "" : (String) this.get(ContentName.mrKey);
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
		this.putParam(ContentName.txrsut, "E");
		this.putParam(ContentName.errorMsg, msg);
	}

	/**
	 * 
	 * @return ArrayList LinkedHashMap
	 */
	public List<LinkedHashMap<String, String>> getOccursList() {
		return this.occursList;
	}

	public boolean isHtmlContent() {
		return isHtmlContent;
	}

}
