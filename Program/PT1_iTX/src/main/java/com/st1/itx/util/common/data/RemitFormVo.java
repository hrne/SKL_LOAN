package com.st1.itx.util.common.data;

import java.util.LinkedHashMap;
import java.util.Objects;

public class RemitFormVo extends LinkedHashMap<String, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 260435140071333399L;
	// 報表代號
	private String reportCode = "Remit";
	// 報表說明
	private String reportItem = "國內匯款申請書(兼取款憑條)";
	// 申請日期(民國年)
	private int applyDay;
	// 取款金額記號:1.同匯款金額 2.同匯款金額及手續費
	private int amtFg;
	// 取款帳號
	private String withdrawAccount;
	// 銀行記號:1.跨行 2.聯行 3.國庫 4.同業 5.證券 6.票券
	private int bankFg;
	// 收款行-銀行
	private String receiveBank;
	// 收款行-分行
	private String receiveBranch;
	// 財金費
	private int fiscFeeAmt;
	// 手續費
	private int normalFeeAmt;
	// 收款人-帳號
	private String receiveAccount;
	// 收款人-戶名
	private String receiveName;
	// 匯款代理人
	private String agentName;
	// 匯款代理人身份證號碼
	private String agentId;
	// 匯款人代理人電話
	private String agentTel;
	// 匯款金額
	private int remitAmt;
	// 匯款人名稱
	private String remitName;
	// 匯款人統一編號
	private String remitId;
	// 匯款人電話
	private String remitTel;
	// 附言
	private String note;

	/**
	 * @return the applyDay
	 */
	public int getApplyDay() {
		return applyDay;
	}

	/**
	 * @param applyDay the applyDay to set
	 */
	public void setApplyDay(int applyDay) {
		this.applyDay = applyDay;
	}

	/**
	 * @return the amtFg
	 */
	public int getAmtFg() {
		return amtFg;
	}

	/**
	 * @param amtFg the amtFg to set
	 */
	public void setAmtFg(int amtFg) {
		this.amtFg = amtFg;
	}

	/**
	 * @return the withdrawAccount
	 */
	public String getWithdrawAccount() {
		return withdrawAccount;
	}

	/**
	 * @param withdrawAccount the withdrawAccount to set
	 */
	public void setWithdrawAccount(String withdrawAccount) {
		this.withdrawAccount = withdrawAccount;
	}

	/**
	 * @return the bankFg
	 */
	public int getBankFg() {
		return bankFg;
	}

	/**
	 * @param bankFg the bankFg to set
	 */
	public void setBankFg(int bankFg) {
		this.bankFg = bankFg;
	}

	/**
	 * @return the receiveBank
	 */
	public String getReceiveBank() {
		return receiveBank;
	}

	/**
	 * @param receiveBank the receiveBank to set
	 */
	public void setReceiveBank(String receiveBank) {
		this.receiveBank = receiveBank;
	}

	/**
	 * @return the receiveBranch
	 */
	public String getReceiveBranch() {
		return receiveBranch;
	}

	/**
	 * @param receiveBranch the receiveBranch to set
	 */
	public void setReceiveBranch(String receiveBranch) {
		this.receiveBranch = receiveBranch;
	}

	/**
	 * @return the fiscFeeAmt
	 */
	public int getFiscFeeAmt() {
		return fiscFeeAmt;
	}

	/**
	 * @param fiscFeeAmt the fiscFeeAmt to set
	 */
	public void setFiscFeeAmt(int fiscFeeAmt) {
		this.fiscFeeAmt = fiscFeeAmt;
	}

	/**
	 * @return the normalFeeAmt
	 */
	public int getNormalFeeAmt() {
		return normalFeeAmt;
	}

	/**
	 * @param normalFeeAmt the normalFeeAmt to set
	 */
	public void setNormalFeeAmt(int normalFeeAmt) {
		this.normalFeeAmt = normalFeeAmt;
	}

	/**
	 * @return the receiveAccount
	 */
	public String getReceiveAccount() {
		return receiveAccount;
	}

	/**
	 * @param receiveAccount the receiveAccount to set
	 */
	public void setReceiveAccount(String receiveAccount) {
		this.receiveAccount = receiveAccount;
	}

	/**
	 * @return the receiveName
	 */
	public String getReceiveName() {
		return receiveName;
	}

	/**
	 * @param receiveName the receiveName to set
	 */
	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}

	/**
	 * @return the agentName
	 */
	public String getAgentName() {
		return agentName;
	}

	/**
	 * @param agentName the agentName to set
	 */
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	/**
	 * @return the agentId
	 */
	public String getAgentId() {
		return agentId;
	}

	/**
	 * @param agentId the agentId to set
	 */
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	/**
	 * @return the agentTel
	 */
	public String getAgentTel() {
		return agentTel;
	}

	/**
	 * @param agentTel the agentTel to set
	 */
	public void setAgentTel(String agentTel) {
		this.agentTel = agentTel;
	}

	/**
	 * @return the remitAmt
	 */
	public int getRemitAmt() {
		return remitAmt;
	}

	/**
	 * @param remitAmt the remitAmt to set
	 */
	public void setRemitAmt(int remitAmt) {
		this.remitAmt = remitAmt;
	}

	/**
	 * @return the remitName
	 */
	public String getRemitName() {
		return remitName;
	}

	/**
	 * @param remitName the remitName to set
	 */
	public void setRemitName(String remitName) {
		this.remitName = remitName;
	}

	/**
	 * @return the remitId
	 */
	public String getRemitId() {
		return remitId;
	}

	/**
	 * @param remitId the remitId to set
	 */
	public void setRemitId(String remitId) {
		this.remitId = remitId;
	}

	/**
	 * @return the remitTel
	 */
	public String getRemitTel() {
		return remitTel;
	}

	/**
	 * @param remitTel the remitTel to set
	 */
	public void setRemitTel(String remitTel) {
		this.remitTel = remitTel;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the reportCode
	 */
	public String getReportCode() {
		return reportCode;
	}

	/**
	 * @param reportCode the reportCode to set
	 */
	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	/**
	 * @return the reportItem
	 */
	public String getReportItem() {
		return reportItem;
	}

	/**
	 * @param reportItem the reportItem to set
	 */
	public void setReportItem(String reportItem) {
		this.reportItem = reportItem;
	}

	public void setNewPageFg() {
		this.put("newPageFg", "1");
	}

	public boolean isNewPage() {
		return Objects.isNull(this.get("newPageFg")) ? false : "1".equals(this.get("newPageFg"));
	}

}
