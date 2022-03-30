package com.st1.ifx.dataVo;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.st1.ifx.hcomm.fmt.FormatUtil;
import com.st1.util.PoorManUtil;
import com.st1.util.getContent.ContentName;;;

@Component("titaVo")
@Scope("prototype")
public class TitaVo extends LinkedHashMap<String, String> {
	static final Logger logger = LoggerFactory.getLogger(TitaVo.class);

	private static final long serialVersionUID = 3280668073933350149L;

	private String orgTitaVO;

	private String orgTita;

	public TitaVo() {
		;
	}

	/**
	 * @param tita for String
	 * @return titaVo Object
	 */
	public TitaVo(String titaJson, String tita) throws JsonParseException, JsonMappingException, IOException {
		this.setOrgTitaVO(titaJson);
		this.setOrgTita(tita);
		this.putAll(new ObjectMapper().configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true).readValue(titaJson, TitaVo.class));
	}

	public String voToString() {
		try {
			return new ObjectMapper().configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true).writeValueAsString(this);
		} catch (JsonProcessingException e) {
			logger.warn("TitaVo to String Error!!!");
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.warn(errors.toString());
			return "";
		}
	}

	/**
	 * @return HCODE = 0 false, HCODE = 1 true
	 */
	public boolean isHcode() {
		return this.get("HCODE").equals("0") ? false : true;
	}

	public boolean isUpdate() {
		String txcdHead = this.get(ContentName.txcd);
		txcdHead = txcdHead.substring(2, 3);
		if (txcdHead.toUpperCase().equals("R") || txcdHead.equals("9") || txcdHead.equals("0"))
			return false;
		return true;
	}

	/**
	 * 
	 * @return an String 交易序號
	 */
	public String getTxtNo() {
		return this.get(ContentName.txtNo) == null ? "" : this.get(ContentName.txtNo);
	}

	/**
	 * 
	 * @return an Integer ACTFG code
	 */
	public int getActfg() {
		return Integer.parseInt(this.get(ContentName.actfg));
	}

	/**
	 * 
	 * @return an String TXCD
	 */
	public String getTxcd() {
		return this.get(ContentName.txcd);
	}

	public String getMsgId() {
		String msgId = this.get(ContentName.txcd);
		msgId = msgId.substring(0, 2);
		return msgId == null ? "" : msgId;
	}

	/**
	 * 
	 * @return an String TXCODE
	 */
	public String getTxCode() {
		return this.get(ContentName.txCode);
	}

	public void setTxCode(String txCode) {
		this.put(ContentName.txCode, txCode);
	}

	/**
	 * 
	 * @return an String 交易員
	 */
	public String getTlrNo() {
		return this.get(ContentName.tlrno) == null ? "" : this.get(ContentName.tlrno);
	}

	/**
	 * 
	 * @return an String BRNO
	 */
	public String getBrno() {
		return this.get(ContentName.kinbr);
	}

	public String getRbrno() {
		return this.get(ContentName.rBrno);
	}

	public String getFbrno() {
		return this.get(ContentName.fBrno);
	}

	public String getAcbrno() {
		return this.get(ContentName.acBrno);
	}

	public String getCifkey() {
		return this.get(ContentName.cifKey);
	}

	public String getCurcd() {
		return this.get(ContentName.curcd);
	}

	public String getTxAmt() {
		return this.get(ContentName.txAmt);
	}

	public String getBatchNo() {
		return this.get(ContentName.batchNo);
	}

	public String getPbrno() {
		return this.get(ContentName.pBrno);
	}

	public void setPbrno(String pBrno) {
		this.put(ContentName.pBrno, pBrno);
	}

	public String getBrTlrNo() {
		return this.get(ContentName.brTlrno);
	}

	public void setBrTlrNo(String brTlrno) {
		this.put(ContentName.brTlrno, brTlrno);
	}

	public String getMrKey() {
		return this.get(ContentName.mrKey);
	}

	public String getRqsp() {
		return this.get(ContentName.rqsp);
	}

	public void setRqsp(String rqsp) {
		this.put(ContentName.rqsp, rqsp);
	}

	public String getSupno() {
		return this.get(ContentName.supno);
	}

	public void setSupno(String supno) {
		this.put(ContentName.supno, supno);
	}

	public String getLevel() {
		return this.get(ContentName.level);
	}

	public void setLevel(String level) {
		this.put(ContentName.level, level);
	}

	public String getOrgTitaVO() {
		return orgTitaVO;
	}

	public void setOrgTitaVO(String orgTitaVO) {
		this.orgTitaVO = orgTitaVO;
	}

	public String getOrgTita() {
		return this.orgTita;
	}

	public void setOrgTita(String tita) {
		this.orgTita = tita;
	}

	public void setIp(String ip) {
		this.put("IP", ip);
	}

	public void putSequenceToTita(int seq) {
		String s = FormatUtil.pad9(Integer.toString(seq), 8);
		this.put(ContentName.txtNo, s);
	}

	public void putSequenceZeroToTita() {
		String s = FormatUtil.pad9("0", 8);
		this.put(ContentName.txtNo, s);
	}

	public void putHeadTimeAndSerialNum() {
		AtomicInteger atomNext = new AtomicInteger(0);
		atomNext.compareAndSet(99999999, 0); // 如果到底了 就歸零
		int hosttranc = atomNext.incrementAndGet();
		this.put(ContentName.headTime, PoorManUtil.getNowwithFormat("ddHHmmss"));
		this.put(ContentName.SerialNum, String.format("%08d", hosttranc));
	}
}
