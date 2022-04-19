package com.st1.itx.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.EmbeddedId;
import javax.persistence.Column;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * NegAppr01 最大債權撥付資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`NegAppr01`")
public class NegAppr01 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5525079924631880640L;

	@EmbeddedId
	private NegAppr01Id negAppr01Id;

	// 會計日期
	@Column(name = "`AcDate`", insertable = false, updatable = false)
	private int acDate = 0;

	// 經辦
	@Column(name = "`TitaTlrNo`", length = 6, insertable = false, updatable = false)
	private String titaTlrNo;

	// 交易序號
	@Column(name = "`TitaTxtNo`", insertable = false, updatable = false)
	private int titaTxtNo = 0;

	// 債權機構代號
	@Column(name = "`FinCode`", length = 10, insertable = false, updatable = false)
	private String finCode;

	// 戶號
	/* 保貸戶須建立客戶主檔 */
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 案件序號
	@Column(name = "`CaseSeq`")
	private int caseSeq = 0;

	// 案件種類
	/* 共用代碼檔1:債協2:調解3:更生4:清算 */
	@Column(name = "`CaseKindCode`", length = 1)
	private String caseKindCode;

	// 撥付金額
	@Column(name = "`ApprAmt`")
	private BigDecimal apprAmt = new BigDecimal("0");

	// 累計撥付金額
	@Column(name = "`AccuApprAmt`")
	private BigDecimal accuApprAmt = new BigDecimal("0");

	// 撥付比例
	@Column(name = "`AmtRatio`")
	private BigDecimal amtRatio = new BigDecimal("0");

	// 製檔日期
	/* L5707最大債權撥付產檔時寫入會計日 */
	@Column(name = "`ExportDate`")
	private int exportDate = 0;

	// 撥付日期
	/* L5708最大債權撥付出帳時寫入會計日 */
	@Column(name = "`ApprDate`")
	private int apprDate = 0;

	// 提兌日
	/* L5709最大債權撥付回覆檔檢核時寫入會計日 */
	@Column(name = "`BringUpDate`")
	private int bringUpDate = 0;

	// 匯款銀行
	@Column(name = "`RemitBank`", length = 7)
	private String remitBank;

	// 匯款帳號
	@Column(name = "`RemitAcct`", length = 16)
	private String remitAcct;

	// 資料傳送單位
	@Column(name = "`DataSendUnit`", length = 8)
	private String dataSendUnit;

	// 撥付傳票日
	/* L5708最大債權撥付出帳時寫入會計日 */
	@Column(name = "`ApprAcDate`")
	private int apprAcDate = 0;

	// 回應代碼
	@Column(name = "`ReplyCode`", length = 4)
	private String replyCode;

	// Batch交易序號
	/* BatchTx01存入 BatchTx04用找到指定的NegAppr01 */
	@Column(name = "`BatchTxtNo`", length = 10)
	private String batchTxtNo;

	// 建檔日期時間
	@CreatedDate
	@Column(name = "`CreateDate`")
	private java.sql.Timestamp createDate;

	// 建檔人員
	@Column(name = "`CreateEmpNo`", length = 6)
	private String createEmpNo;

	// 最後更新日期時間
	@LastModifiedDate
	@Column(name = "`LastUpdate`")
	private java.sql.Timestamp lastUpdate;

	// 最後更新人員
	@Column(name = "`LastUpdateEmpNo`", length = 6)
	private String lastUpdateEmpNo;

	public NegAppr01Id getNegAppr01Id() {
		return this.negAppr01Id;
	}

	public void setNegAppr01Id(NegAppr01Id negAppr01Id) {
		this.negAppr01Id = negAppr01Id;
	}

	/**
	 * 會計日期<br>
	 * 
	 * @return Integer
	 */
	public int getAcDate() {
		return StaticTool.bcToRoc(this.acDate);
	}

	/**
	 * 會計日期<br>
	 * 
	 *
	 * @param acDate 會計日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setAcDate(int acDate) throws LogicException {
		this.acDate = StaticTool.rocToBc(acDate);
	}

	/**
	 * 經辦<br>
	 * 
	 * @return String
	 */
	public String getTitaTlrNo() {
		return this.titaTlrNo == null ? "" : this.titaTlrNo;
	}

	/**
	 * 經辦<br>
	 * 
	 *
	 * @param titaTlrNo 經辦
	 */
	public void setTitaTlrNo(String titaTlrNo) {
		this.titaTlrNo = titaTlrNo;
	}

	/**
	 * 交易序號<br>
	 * 
	 * @return Integer
	 */
	public int getTitaTxtNo() {
		return this.titaTxtNo;
	}

	/**
	 * 交易序號<br>
	 * 
	 *
	 * @param titaTxtNo 交易序號
	 */
	public void setTitaTxtNo(int titaTxtNo) {
		this.titaTxtNo = titaTxtNo;
	}

	/**
	 * 債權機構代號<br>
	 * 
	 * @return String
	 */
	public String getFinCode() {
		return this.finCode == null ? "" : this.finCode;
	}

	/**
	 * 債權機構代號<br>
	 * 
	 *
	 * @param finCode 債權機構代號
	 */
	public void setFinCode(String finCode) {
		this.finCode = finCode;
	}

	/**
	 * 戶號<br>
	 * 保貸戶須建立客戶主檔
	 * 
	 * @return Integer
	 */
	public int getCustNo() {
		return this.custNo;
	}

	/**
	 * 戶號<br>
	 * 保貸戶須建立客戶主檔
	 *
	 * @param custNo 戶號
	 */
	public void setCustNo(int custNo) {
		this.custNo = custNo;
	}

	/**
	 * 案件序號<br>
	 * 
	 * @return Integer
	 */
	public int getCaseSeq() {
		return this.caseSeq;
	}

	/**
	 * 案件序號<br>
	 * 
	 *
	 * @param caseSeq 案件序號
	 */
	public void setCaseSeq(int caseSeq) {
		this.caseSeq = caseSeq;
	}

	/**
	 * 案件種類<br>
	 * 共用代碼檔 1:債協 2:調解 3:更生 4:清算
	 * 
	 * @return String
	 */
	public String getCaseKindCode() {
		return this.caseKindCode == null ? "" : this.caseKindCode;
	}

	/**
	 * 案件種類<br>
	 * 共用代碼檔 1:債協 2:調解 3:更生 4:清算
	 *
	 * @param caseKindCode 案件種類
	 */
	public void setCaseKindCode(String caseKindCode) {
		this.caseKindCode = caseKindCode;
	}

	/**
	 * 撥付金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getApprAmt() {
		return this.apprAmt;
	}

	/**
	 * 撥付金額<br>
	 * 
	 *
	 * @param apprAmt 撥付金額
	 */
	public void setApprAmt(BigDecimal apprAmt) {
		this.apprAmt = apprAmt;
	}

	/**
	 * 累計撥付金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAccuApprAmt() {
		return this.accuApprAmt;
	}

	/**
	 * 累計撥付金額<br>
	 * 
	 *
	 * @param accuApprAmt 累計撥付金額
	 */
	public void setAccuApprAmt(BigDecimal accuApprAmt) {
		this.accuApprAmt = accuApprAmt;
	}

	/**
	 * 撥付比例<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAmtRatio() {
		return this.amtRatio;
	}

	/**
	 * 撥付比例<br>
	 * 
	 *
	 * @param amtRatio 撥付比例
	 */
	public void setAmtRatio(BigDecimal amtRatio) {
		this.amtRatio = amtRatio;
	}

	/**
	 * 製檔日期<br>
	 * L5707最大債權撥付產檔時寫入會計日
	 * 
	 * @return Integer
	 */
	public int getExportDate() {
		return StaticTool.bcToRoc(this.exportDate);
	}

	/**
	 * 製檔日期<br>
	 * L5707最大債權撥付產檔時寫入會計日
	 *
	 * @param exportDate 製檔日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setExportDate(int exportDate) throws LogicException {
		this.exportDate = StaticTool.rocToBc(exportDate);
	}

	/**
	 * 撥付日期<br>
	 * L5708最大債權撥付出帳時寫入會計日
	 * 
	 * @return Integer
	 */
	public int getApprDate() {
		return StaticTool.bcToRoc(this.apprDate);
	}

	/**
	 * 撥付日期<br>
	 * L5708最大債權撥付出帳時寫入會計日
	 *
	 * @param apprDate 撥付日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setApprDate(int apprDate) throws LogicException {
		this.apprDate = StaticTool.rocToBc(apprDate);
	}

	/**
	 * 提兌日<br>
	 * L5709最大債權撥付回覆檔檢核時寫入會計日
	 * 
	 * @return Integer
	 */
	public int getBringUpDate() {
		return StaticTool.bcToRoc(this.bringUpDate);
	}

	/**
	 * 提兌日<br>
	 * L5709最大債權撥付回覆檔檢核時寫入會計日
	 *
	 * @param bringUpDate 提兌日
	 * @throws LogicException when Date Is Warn
	 */
	public void setBringUpDate(int bringUpDate) throws LogicException {
		this.bringUpDate = StaticTool.rocToBc(bringUpDate);
	}

	/**
	 * 匯款銀行<br>
	 * 
	 * @return String
	 */
	public String getRemitBank() {
		return this.remitBank == null ? "" : this.remitBank;
	}

	/**
	 * 匯款銀行<br>
	 * 
	 *
	 * @param remitBank 匯款銀行
	 */
	public void setRemitBank(String remitBank) {
		this.remitBank = remitBank;
	}

	/**
	 * 匯款帳號<br>
	 * 
	 * @return String
	 */
	public String getRemitAcct() {
		return this.remitAcct == null ? "" : this.remitAcct;
	}

	/**
	 * 匯款帳號<br>
	 * 
	 *
	 * @param remitAcct 匯款帳號
	 */
	public void setRemitAcct(String remitAcct) {
		this.remitAcct = remitAcct;
	}

	/**
	 * 資料傳送單位<br>
	 * 
	 * @return String
	 */
	public String getDataSendUnit() {
		return this.dataSendUnit == null ? "" : this.dataSendUnit;
	}

	/**
	 * 資料傳送單位<br>
	 * 
	 *
	 * @param dataSendUnit 資料傳送單位
	 */
	public void setDataSendUnit(String dataSendUnit) {
		this.dataSendUnit = dataSendUnit;
	}

	/**
	 * 撥付傳票日<br>
	 * L5708最大債權撥付出帳時寫入會計日
	 * 
	 * @return Integer
	 */
	public int getApprAcDate() {
		return StaticTool.bcToRoc(this.apprAcDate);
	}

	/**
	 * 撥付傳票日<br>
	 * L5708最大債權撥付出帳時寫入會計日
	 *
	 * @param apprAcDate 撥付傳票日
	 * @throws LogicException when Date Is Warn
	 */
	public void setApprAcDate(int apprAcDate) throws LogicException {
		this.apprAcDate = StaticTool.rocToBc(apprAcDate);
	}

	/**
	 * 回應代碼<br>
	 * 
	 * @return String
	 */
	public String getReplyCode() {
		return this.replyCode == null ? "" : this.replyCode;
	}

	/**
	 * 回應代碼<br>
	 * 
	 *
	 * @param replyCode 回應代碼
	 */
	public void setReplyCode(String replyCode) {
		this.replyCode = replyCode;
	}

	/**
	 * Batch交易序號<br>
	 * BatchTx01存入 BatchTx04用找到指定的NegAppr01
	 * 
	 * @return String
	 */
	public String getBatchTxtNo() {
		return this.batchTxtNo == null ? "" : this.batchTxtNo;
	}

	/**
	 * Batch交易序號<br>
	 * BatchTx01存入 BatchTx04用找到指定的NegAppr01
	 *
	 * @param batchTxtNo Batch交易序號
	 */
	public void setBatchTxtNo(String batchTxtNo) {
		this.batchTxtNo = batchTxtNo;
	}

	/**
	 * 建檔日期時間<br>
	 * 
	 * @return java.sql.Timestamp
	 */
	public java.sql.Timestamp getCreateDate() {
		return this.createDate;
	}

	/**
	 * 建檔日期時間<br>
	 * 
	 *
	 * @param createDate 建檔日期時間
	 */
	public void setCreateDate(java.sql.Timestamp createDate) {
		this.createDate = createDate;
	}

	/**
	 * 建檔人員<br>
	 * 
	 * @return String
	 */
	public String getCreateEmpNo() {
		return this.createEmpNo == null ? "" : this.createEmpNo;
	}

	/**
	 * 建檔人員<br>
	 * 
	 *
	 * @param createEmpNo 建檔人員
	 */
	public void setCreateEmpNo(String createEmpNo) {
		this.createEmpNo = createEmpNo;
	}

	/**
	 * 最後更新日期時間<br>
	 * 
	 * @return java.sql.Timestamp
	 */
	public java.sql.Timestamp getLastUpdate() {
		return this.lastUpdate;
	}

	/**
	 * 最後更新日期時間<br>
	 * 
	 *
	 * @param lastUpdate 最後更新日期時間
	 */
	public void setLastUpdate(java.sql.Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * 最後更新人員<br>
	 * 
	 * @return String
	 */
	public String getLastUpdateEmpNo() {
		return this.lastUpdateEmpNo == null ? "" : this.lastUpdateEmpNo;
	}

	/**
	 * 最後更新人員<br>
	 * 
	 *
	 * @param lastUpdateEmpNo 最後更新人員
	 */
	public void setLastUpdateEmpNo(String lastUpdateEmpNo) {
		this.lastUpdateEmpNo = lastUpdateEmpNo;
	}

	@Override
	public String toString() {
		return "NegAppr01 [negAppr01Id=" + negAppr01Id + ", custNo=" + custNo + ", caseSeq=" + caseSeq + ", caseKindCode=" + caseKindCode + ", apprAmt=" + apprAmt + ", accuApprAmt=" + accuApprAmt
				+ ", amtRatio=" + amtRatio + ", exportDate=" + exportDate + ", apprDate=" + apprDate + ", bringUpDate=" + bringUpDate + ", remitBank=" + remitBank + ", remitAcct=" + remitAcct
				+ ", dataSendUnit=" + dataSendUnit + ", apprAcDate=" + apprAcDate + ", replyCode=" + replyCode + ", batchTxtNo=" + batchTxtNo + ", createDate=" + createDate + ", createEmpNo="
				+ createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
