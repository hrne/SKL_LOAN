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
 * CollLaw 法催紀錄法務進度檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CollLaw`")
public class CollLaw implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1716858389376828917L;

	@EmbeddedId
	private CollLawId collLawId;

	// 案件種類
	/* 1:法催2:債協 */
	@Column(name = "`CaseCode`", length = 1, insertable = false, updatable = false)
	private String caseCode;

	// 擔保品代號1
	/* 2021/5/7，新增*資料轉換時只轉主要擔保品 */
	@Column(name = "`ClCode1`")
	private int clCode1 = 0;

	// 擔保品代號2
	/* 2021/5/7，新增*資料轉換時只轉主要擔保品 */
	@Column(name = "`ClCode2`")
	private int clCode2 = 0;

	// 擔保品編號
	/* 2021/5/7，新增*資料轉換時只轉主要擔保品 */
	@Column(name = "`ClNo`")
	private int clNo = 0;

	// 借款人戶號
	@Column(name = "`CustNo`", insertable = false, updatable = false)
	private int custNo = 0;

	// 額度編號
	@Column(name = "`FacmNo`", insertable = false, updatable = false)
	private int facmNo = 0;

	// 作業日期
	@Column(name = "`AcDate`", insertable = false, updatable = false)
	private int acDate = 0;

	// 經辦
	@Column(name = "`TitaTlrNo`", length = 6, insertable = false, updatable = false)
	private String titaTlrNo;

	// 交易序號
	@Column(name = "`TitaTxtNo`", length = 8, insertable = false, updatable = false)
	private String titaTxtNo;

	// 記錄日期
	@Column(name = "`RecordDate`")
	private int recordDate = 0;

	// 法務進度
	/* CdCode共用代碼檔 */
	@Column(name = "`LegalProg`", length = 3)
	private String legalProg;

	// 金額
	@Column(name = "`Amount`")
	private BigDecimal amount = new BigDecimal("0");

	// 其他記錄選項
	/* 下拉選單1.支付命令確定-借款人2.支付命令確定-保證人3.本票裁定確定4.拍賣抵押物裁定確定5.拍賣質物裁定確定6.全部勝訴判決7.一部勝訴判決 */
	@Column(name = "`Remark`", length = 1)
	private String remark;

	// 其他紀錄內容
	@Column(name = "`Memo`", length = 500)
	private String memo;

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

	public CollLawId getCollLawId() {
		return this.collLawId;
	}

	public void setCollLawId(CollLawId collLawId) {
		this.collLawId = collLawId;
	}

	/**
	 * 案件種類<br>
	 * 1:法催 2:債協
	 * 
	 * @return String
	 */
	public String getCaseCode() {
		return this.caseCode == null ? "" : this.caseCode;
	}

	/**
	 * 案件種類<br>
	 * 1:法催 2:債協
	 *
	 * @param caseCode 案件種類
	 */
	public void setCaseCode(String caseCode) {
		this.caseCode = caseCode;
	}

	/**
	 * 擔保品代號1<br>
	 * 2021/5/7，新增 資料轉換時只轉主要擔保品
	 * 
	 * @return Integer
	 */
	public int getClCode1() {
		return this.clCode1;
	}

	/**
	 * 擔保品代號1<br>
	 * 2021/5/7，新增 資料轉換時只轉主要擔保品
	 *
	 * @param clCode1 擔保品代號1
	 */
	public void setClCode1(int clCode1) {
		this.clCode1 = clCode1;
	}

	/**
	 * 擔保品代號2<br>
	 * 2021/5/7，新增 資料轉換時只轉主要擔保品
	 * 
	 * @return Integer
	 */
	public int getClCode2() {
		return this.clCode2;
	}

	/**
	 * 擔保品代號2<br>
	 * 2021/5/7，新增 資料轉換時只轉主要擔保品
	 *
	 * @param clCode2 擔保品代號2
	 */
	public void setClCode2(int clCode2) {
		this.clCode2 = clCode2;
	}

	/**
	 * 擔保品編號<br>
	 * 2021/5/7，新增 資料轉換時只轉主要擔保品
	 * 
	 * @return Integer
	 */
	public int getClNo() {
		return this.clNo;
	}

	/**
	 * 擔保品編號<br>
	 * 2021/5/7，新增 資料轉換時只轉主要擔保品
	 *
	 * @param clNo 擔保品編號
	 */
	public void setClNo(int clNo) {
		this.clNo = clNo;
	}

	/**
	 * 借款人戶號<br>
	 * 
	 * @return Integer
	 */
	public int getCustNo() {
		return this.custNo;
	}

	/**
	 * 借款人戶號<br>
	 * 
	 *
	 * @param custNo 借款人戶號
	 */
	public void setCustNo(int custNo) {
		this.custNo = custNo;
	}

	/**
	 * 額度編號<br>
	 * 
	 * @return Integer
	 */
	public int getFacmNo() {
		return this.facmNo;
	}

	/**
	 * 額度編號<br>
	 * 
	 *
	 * @param facmNo 額度編號
	 */
	public void setFacmNo(int facmNo) {
		this.facmNo = facmNo;
	}

	/**
	 * 作業日期<br>
	 * 
	 * @return Integer
	 */
	public int getAcDate() {
		return StaticTool.bcToRoc(this.acDate);
	}

	/**
	 * 作業日期<br>
	 * 
	 *
	 * @param acDate 作業日期
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
	 * @return String
	 */
	public String getTitaTxtNo() {
		return this.titaTxtNo == null ? "" : this.titaTxtNo;
	}

	/**
	 * 交易序號<br>
	 * 
	 *
	 * @param titaTxtNo 交易序號
	 */
	public void setTitaTxtNo(String titaTxtNo) {
		this.titaTxtNo = titaTxtNo;
	}

	/**
	 * 記錄日期<br>
	 * 
	 * @return Integer
	 */
	public int getRecordDate() {
		return StaticTool.bcToRoc(this.recordDate);
	}

	/**
	 * 記錄日期<br>
	 * 
	 *
	 * @param recordDate 記錄日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setRecordDate(int recordDate) throws LogicException {
		this.recordDate = StaticTool.rocToBc(recordDate);
	}

	/**
	 * 法務進度<br>
	 * CdCode共用代碼檔
	 * 
	 * @return String
	 */
	public String getLegalProg() {
		return this.legalProg == null ? "" : this.legalProg;
	}

	/**
	 * 法務進度<br>
	 * CdCode共用代碼檔
	 *
	 * @param legalProg 法務進度
	 */
	public void setLegalProg(String legalProg) {
		this.legalProg = legalProg;
	}

	/**
	 * 金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAmount() {
		return this.amount;
	}

	/**
	 * 金額<br>
	 * 
	 *
	 * @param amount 金額
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * 其他記錄選項<br>
	 * 下拉選單 1.支付命令確定-借款人 2.支付命令確定-保證人 3.本票裁定確定 4.拍賣抵押物裁定確定 5.拍賣質物裁定確定 6.全部勝訴判決
	 * 7.一部勝訴判決
	 * 
	 * @return String
	 */
	public String getRemark() {
		return this.remark == null ? "" : this.remark;
	}

	/**
	 * 其他記錄選項<br>
	 * 下拉選單 1.支付命令確定-借款人 2.支付命令確定-保證人 3.本票裁定確定 4.拍賣抵押物裁定確定 5.拍賣質物裁定確定 6.全部勝訴判決
	 * 7.一部勝訴判決
	 *
	 * @param remark 其他記錄選項
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 其他紀錄內容<br>
	 * 
	 * @return String
	 */
	public String getMemo() {
		return this.memo == null ? "" : this.memo;
	}

	/**
	 * 其他紀錄內容<br>
	 * 
	 *
	 * @param memo 其他紀錄內容
	 */
	public void setMemo(String memo) {
		this.memo = memo;
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
		return "CollLaw [collLawId=" + collLawId + ", clCode1=" + clCode1 + ", clCode2=" + clCode2 + ", clNo=" + clNo + ", recordDate=" + recordDate + ", legalProg=" + legalProg + ", amount=" + amount
				+ ", remark=" + remark + ", memo=" + memo + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
