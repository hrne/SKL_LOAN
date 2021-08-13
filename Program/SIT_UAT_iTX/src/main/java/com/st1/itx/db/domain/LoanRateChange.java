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
 * LoanRateChange 放款利率變動檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`LoanRateChange`")
public class LoanRateChange implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 481039425176200957L;

	@EmbeddedId
	private LoanRateChangeId loanRateChangeId;

	// 借款人戶號
	@Column(name = "`CustNo`", insertable = false, updatable = false)
	private int custNo = 0;

	// 額度編號
	@Column(name = "`FacmNo`", insertable = false, updatable = false)
	private int facmNo = 0;

	// 撥款序號
	@Column(name = "`BormNo`", insertable = false, updatable = false)
	private int bormNo = 0;

	// 生效日期
	@Column(name = "`EffectDate`", insertable = false, updatable = false)
	private int effectDate = 0;

	// 狀態
	/* 0: 正常 */
	@Column(name = "`Status`")
	private int status = 0;

	// 利率區分
	/* 共用代碼檔1: 機動 2: 固動 3: 定期機動 */
	@Column(name = "`RateCode`", length = 1)
	private String rateCode;

	// 商品代碼
	@Column(name = "`ProdNo`", length = 5)
	private String prodNo;

	// 指標利率代碼
	/* 共用代碼檔01: 保單分紅利率02: 中華郵政二年期定儲機動利率99: 自訂利率 */
	@Column(name = "`BaseRateCode`", length = 2)
	private String baseRateCode;

	// 加減碼是否依合約
	/* Y:是 N:否 */
	@Column(name = "`IncrFlag`", length = 1)
	private String incrFlag;

	// 加碼利率
	@Column(name = "`RateIncr`")
	private BigDecimal rateIncr = new BigDecimal("0");

	// 個別加碼利率
	@Column(name = "`IndividualIncr`")
	private BigDecimal individualIncr = new BigDecimal("0");

	// 適用利率
	@Column(name = "`FitRate`")
	private BigDecimal fitRate = new BigDecimal("0");

	// 備註
	@Column(name = "`Remark`", length = 60)
	private String remark;

	// 交易序號-會計日期
	@Column(name = "`AcDate`")
	private int acDate = 0;

	// 交易序號-櫃員別
	@Column(name = "`TellerNo`", length = 6)
	private String tellerNo;

	// 交易序號-流水號
	@Column(name = "`TxtNo`", length = 8)
	private String txtNo;

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

	public LoanRateChangeId getLoanRateChangeId() {
		return this.loanRateChangeId;
	}

	public void setLoanRateChangeId(LoanRateChangeId loanRateChangeId) {
		this.loanRateChangeId = loanRateChangeId;
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
	 * 撥款序號<br>
	 * 
	 * @return Integer
	 */
	public int getBormNo() {
		return this.bormNo;
	}

	/**
	 * 撥款序號<br>
	 * 
	 *
	 * @param bormNo 撥款序號
	 */
	public void setBormNo(int bormNo) {
		this.bormNo = bormNo;
	}

	/**
	 * 生效日期<br>
	 * 
	 * @return Integer
	 */
	public int getEffectDate() {
		return StaticTool.bcToRoc(this.effectDate);
	}

	/**
	 * 生效日期<br>
	 * 
	 *
	 * @param effectDate 生效日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setEffectDate(int effectDate) throws LogicException {
		this.effectDate = StaticTool.rocToBc(effectDate);
	}

	/**
	 * 狀態<br>
	 * 0: 正常
	 * 
	 * @return Integer
	 */
	public int getStatus() {
		return this.status;
	}

	/**
	 * 狀態<br>
	 * 0: 正常
	 *
	 * @param status 狀態
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 利率區分<br>
	 * 共用代碼檔 1: 機動 2: 固動 3: 定期機動
	 * 
	 * @return String
	 */
	public String getRateCode() {
		return this.rateCode == null ? "" : this.rateCode;
	}

	/**
	 * 利率區分<br>
	 * 共用代碼檔 1: 機動 2: 固動 3: 定期機動
	 *
	 * @param rateCode 利率區分
	 */
	public void setRateCode(String rateCode) {
		this.rateCode = rateCode;
	}

	/**
	 * 商品代碼<br>
	 * 
	 * @return String
	 */
	public String getProdNo() {
		return this.prodNo == null ? "" : this.prodNo;
	}

	/**
	 * 商品代碼<br>
	 * 
	 *
	 * @param prodNo 商品代碼
	 */
	public void setProdNo(String prodNo) {
		this.prodNo = prodNo;
	}

	/**
	 * 指標利率代碼<br>
	 * 共用代碼檔 01: 保單分紅利率 02: 中華郵政二年期定儲機動利率 99: 自訂利率
	 * 
	 * @return String
	 */
	public String getBaseRateCode() {
		return this.baseRateCode == null ? "" : this.baseRateCode;
	}

	/**
	 * 指標利率代碼<br>
	 * 共用代碼檔 01: 保單分紅利率 02: 中華郵政二年期定儲機動利率 99: 自訂利率
	 *
	 * @param baseRateCode 指標利率代碼
	 */
	public void setBaseRateCode(String baseRateCode) {
		this.baseRateCode = baseRateCode;
	}

	/**
	 * 加減碼是否依合約<br>
	 * Y:是 N:否
	 * 
	 * @return String
	 */
	public String getIncrFlag() {
		return this.incrFlag == null ? "" : this.incrFlag;
	}

	/**
	 * 加減碼是否依合約<br>
	 * Y:是 N:否
	 *
	 * @param incrFlag 加減碼是否依合約
	 */
	public void setIncrFlag(String incrFlag) {
		this.incrFlag = incrFlag;
	}

	/**
	 * 加碼利率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getRateIncr() {
		return this.rateIncr;
	}

	/**
	 * 加碼利率<br>
	 * 
	 *
	 * @param rateIncr 加碼利率
	 */
	public void setRateIncr(BigDecimal rateIncr) {
		this.rateIncr = rateIncr;
	}

	/**
	 * 個別加碼利率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getIndividualIncr() {
		return this.individualIncr;
	}

	/**
	 * 個別加碼利率<br>
	 * 
	 *
	 * @param individualIncr 個別加碼利率
	 */
	public void setIndividualIncr(BigDecimal individualIncr) {
		this.individualIncr = individualIncr;
	}

	/**
	 * 適用利率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getFitRate() {
		return this.fitRate;
	}

	/**
	 * 適用利率<br>
	 * 
	 *
	 * @param fitRate 適用利率
	 */
	public void setFitRate(BigDecimal fitRate) {
		this.fitRate = fitRate;
	}

	/**
	 * 備註<br>
	 * 
	 * @return String
	 */
	public String getRemark() {
		return this.remark == null ? "" : this.remark;
	}

	/**
	 * 備註<br>
	 * 
	 *
	 * @param remark 備註
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 交易序號-會計日期<br>
	 * 
	 * @return Integer
	 */
	public int getAcDate() {
		return StaticTool.bcToRoc(this.acDate);
	}

	/**
	 * 交易序號-會計日期<br>
	 * 
	 *
	 * @param acDate 交易序號-會計日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setAcDate(int acDate) throws LogicException {
		this.acDate = StaticTool.rocToBc(acDate);
	}

	/**
	 * 交易序號-櫃員別<br>
	 * 
	 * @return String
	 */
	public String getTellerNo() {
		return this.tellerNo == null ? "" : this.tellerNo;
	}

	/**
	 * 交易序號-櫃員別<br>
	 * 
	 *
	 * @param tellerNo 交易序號-櫃員別
	 */
	public void setTellerNo(String tellerNo) {
		this.tellerNo = tellerNo;
	}

	/**
	 * 交易序號-流水號<br>
	 * 
	 * @return String
	 */
	public String getTxtNo() {
		return this.txtNo == null ? "" : this.txtNo;
	}

	/**
	 * 交易序號-流水號<br>
	 * 
	 *
	 * @param txtNo 交易序號-流水號
	 */
	public void setTxtNo(String txtNo) {
		this.txtNo = txtNo;
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
		return "LoanRateChange [loanRateChangeId=" + loanRateChangeId + ", status=" + status + ", rateCode=" + rateCode + ", prodNo=" + prodNo + ", baseRateCode=" + baseRateCode + ", incrFlag="
				+ incrFlag + ", rateIncr=" + rateIncr + ", individualIncr=" + individualIncr + ", fitRate=" + fitRate + ", remark=" + remark + ", acDate=" + acDate + ", tellerNo=" + tellerNo
				+ ", txtNo=" + txtNo + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
