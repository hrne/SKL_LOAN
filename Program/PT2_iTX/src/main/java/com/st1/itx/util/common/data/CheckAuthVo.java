package com.st1.itx.util.common.data;

import java.util.LinkedHashMap;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("checkAuthVo")
@Scope("prototype")
public class CheckAuthVo extends LinkedHashMap<String, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1254066878484726850L;

	// 經辦員編
	private String TlrNo;

	// 被代理人員編
	private String AgentNo;

	// 交易編號
	private String TranNo;

	// 使用被代理人維護權限員編 (舊版)
	private String AgenUpdtTlrNo;

	// 使用被代理人查詢權限員編 (舊版)
	private String AgenInqtTlrNo;

	// 維護資料權限
	private boolean CanUpdate;

	// 查詢資料權限
	private boolean CanInquiry;

	/**
	 * @return the tranNo
	 */
	public String getTranNo() {
		return TranNo;
	}

	/**
	 * @param tranNo the tranNo to set
	 */
	public void setTranNo(String tranNo) {
		TranNo = tranNo;
	}

	/**
	 * @return the tlrNo
	 */
	public String getTlrNo() {
		return TlrNo;
	}

	/**
	 * @param tlrNo the tlrNo to set
	 */
	public void setTlrNo(String tlrNo) {
		TlrNo = tlrNo;
	}

	/**
	 * @return the agenUpdtTlrNo
	 */
	public String getAgenUpdtTlrNo() {
		return AgenUpdtTlrNo;
	}

	/**
	 * @param agenUpdtTlrNo the agenUpdtTlrNo to set
	 */
	public void setAgenUpdtTlrNo(String agenUpdtTlrNo) {
		AgenUpdtTlrNo = agenUpdtTlrNo;
	}

	/**
	 * @return the agenInqtTlrNo
	 */
	public String getAgenInqtTlrNo() {
		return AgenInqtTlrNo;
	}

	/**
	 * @param agenInqtTlrNo the agenInqtTlrNo to set
	 */
	public void setAgenInqtTlrNo(String agenInqtTlrNo) {
		AgenInqtTlrNo = agenInqtTlrNo;
	}

	/**
	 * @return the canUpdate
	 */
	public boolean isCanUpdate() {
		return CanUpdate;
	}

	/**
	 * @param canUpdate the canUpdate to set
	 */
	public void setCanUpdate(boolean canUpdate) {
		CanUpdate = canUpdate;
	}

	/**
	 * @return the canInquiry
	 */
	public boolean isCanInquiry() {
		return CanInquiry;
	}

	/**
	 * @param canInquiry the canInquiry to set
	 */
	public void setCanInquiry(boolean canInquiry) {
		CanInquiry = canInquiry;
	}

	/**
	 * @return the agentNo
	 */
	public String getAgentNo() {
		return AgentNo;
	}

	/**
	 * @param agentNo the agentNo to set
	 */
	public void setAgentNo(String agentNo) {
		AgentNo = agentNo;
	}

}
