package com.st1.itx.util.common.data;

import java.util.LinkedHashMap;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("smsVo")
@Scope("prototype")
public class SmsVo extends LinkedHashMap<String, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6931341581042408107L;

	/**
	 * M 來源系統ID,預設值 Loan
	 */
	private String srcSystemID = "BPM"; // 待申請通過後改為LOAN

	/**
	 * M 交易名稱
	 */
	private String txnID = "ESB00517";

	/**
	 * M 簡訊資料
	 */
	private String smsData = "";

	/**
	 * ****************************************************** 回覆參數
	 */

	/*
	 * connect success 0.no 1.yes
	 */
	private boolean successFlag = false;

	/**
	 * response
	 */
	private String msgRs = "";

	/**
	 * response
	 */
	private String success = "";

	/**
	 * response
	 */
	private String message = "";

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the msgRs
	 */
	public String getMsgRs() {
		return msgRs;
	}

	/**
	 * @return the smsData
	 */
	public String getSmsData() {
		return smsData;
	}

	/**
	 * @return the srcSystemID
	 */
	public String getSrcSystemID() {
		return srcSystemID;
	}

	/**
	 * @return the success
	 */
	public String getSuccess() {
		return success;
	}

	/**
	 * @return the txnID
	 */
	public String getTxnID() {
		return txnID;
	}

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return successFlag;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @param msgRs the msgRs to set
	 */
	public void setMsgRs(String msgRs) {
		this.msgRs = msgRs;
	}

	/**
	 * @param smsDataVo the smsData to set
	 * @throws Exception 
	 */
	public void setSmsData(SmsDataVo smsDataVo) throws Exception {
		this.smsData = smsDataVo.getJsonString();
	}

	/**
	 * @param srcSystemID the srcSystemID to set
	 */
	public void setSrcSystemID(String srcSystemID) {
		this.srcSystemID = srcSystemID;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.successFlag = success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(String success) {
		this.success = success;
	}

	/**
	 * @param txnID the txnID to set
	 */
	public void setTxnID(String txnID) {
		this.txnID = txnID;
	}

}
