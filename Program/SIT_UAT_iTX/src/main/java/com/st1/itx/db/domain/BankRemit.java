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
 * BankRemit 撥款匯款檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`BankRemit`")
public class BankRemit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1985173344155262907L;

	@EmbeddedId
	private BankRemitId bankRemitId;

	// 會計日期
	@Column(name = "`AcDate`", insertable = false, updatable = false)
	private int acDate = 0;

	// 經辦
	@Column(name = "`TitaTlrNo`", length = 6, insertable = false, updatable = false)
	private String titaTlrNo;

	// 交易序號
	@Column(name = "`TitaTxtNo`", length = 8, insertable = false, updatable = false)
	private String titaTxtNo;

	// 整批批號
	/* "LN + 傳票批號 + 匯款批號 */
	@Column(name = "`BatchNo`", length = 6)
	private String batchNo;

	// 撥款方式
	/*
	 * CdCode:DrawdownCode201:撥款(整批匯款)02:撥款(單筆匯款)04:退款台新(存款憑條)05:退款他行(整批匯款)11:退款新光(
	 * 存款憑條)
	 */
	@Column(name = "`DrawdownCode`")
	private int drawdownCode = 0;

	// 狀態
	/* CdCode:DrawdownStatus0:正常1:產檔後修正2:產檔後訂正3.未放行(ActFg=1時顯示用) */
	@Column(name = "`StatusCode`")
	private int statusCode = 0;

	// 匯款銀行
	@Column(name = "`RemitBank`", length = 3)
	private String remitBank;

	// 匯款分行
	@Column(name = "`RemitBranch`", length = 4)
	private String remitBranch;

	// 匯款帳號
	@Column(name = "`RemitAcctNo`", length = 14)
	private String remitAcctNo;

	// 收款戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度編號
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 撥款序號
	@Column(name = "`BormNo`")
	private int bormNo = 0;

	// 收款戶名
	@Column(name = "`CustName`", length = 100)
	private String custName;

	// 附言
	@Column(name = "`Remark`", length = 100)
	private String remark;

	// 幣別
	@Column(name = "`CurrencyCode`", length = 3)
	private String currencyCode;

	// 匯款金額
	@Column(name = "`RemitAmt`")
	private BigDecimal remitAmt = new BigDecimal("0");

	// AML回應碼
	/* CdCode:AmlCheckItem0.非可疑名單/已完成名單確認1.需審查/確認2.為凍結名單/未確定名單 */
	@Column(name = "`AmlRsp`", length = 1)
	private String amlRsp;

	// 交易進行記號
	/* 1STEP TX -&amp;gt; 0 (from eloan)2STEP TX -&amp;gt; 1 2 */
	@Column(name = "`ActFg`")
	private int actFg = 0;

	// 產檔後修正後內容
	/* jason 格式 */
	@Column(name = "`ModifyContent`", length = 500)
	private String modifyContent;

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

	public BankRemitId getBankRemitId() {
		return this.bankRemitId;
	}

	public void setBankRemitId(BankRemitId bankRemitId) {
		this.bankRemitId = bankRemitId;
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
	 * 整批批號<br>
	 * "LN + 傳票批號 + 匯款批號
	 * 
	 * @return String
	 */
	public String getBatchNo() {
		return this.batchNo == null ? "" : this.batchNo;
	}

	/**
	 * 整批批號<br>
	 * "LN + 傳票批號 + 匯款批號
	 *
	 * @param batchNo 整批批號
	 */
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	/**
	 * 撥款方式<br>
	 * CdCode:DrawdownCode2 01:撥款(整批匯款) 02:撥款(單筆匯款) 04:退款台新(存款憑條) 05:退款他行(整批匯款)
	 * 11:退款新光(存款憑條)
	 * 
	 * @return Integer
	 */
	public int getDrawdownCode() {
		return this.drawdownCode;
	}

	/**
	 * 撥款方式<br>
	 * CdCode:DrawdownCode2 01:撥款(整批匯款) 02:撥款(單筆匯款) 04:退款台新(存款憑條) 05:退款他行(整批匯款)
	 * 11:退款新光(存款憑條)
	 *
	 * @param drawdownCode 撥款方式
	 */
	public void setDrawdownCode(int drawdownCode) {
		this.drawdownCode = drawdownCode;
	}

	/**
	 * 狀態<br>
	 * CdCode:DrawdownStatus 0:正常 1:產檔後修正 2:產檔後訂正 3.未放行(ActFg=1時顯示用)
	 * 
	 * @return Integer
	 */
	public int getStatusCode() {
		return this.statusCode;
	}

	/**
	 * 狀態<br>
	 * CdCode:DrawdownStatus 0:正常 1:產檔後修正 2:產檔後訂正 3.未放行(ActFg=1時顯示用)
	 *
	 * @param statusCode 狀態
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
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
	 * 匯款分行<br>
	 * 
	 * @return String
	 */
	public String getRemitBranch() {
		return this.remitBranch == null ? "" : this.remitBranch;
	}

	/**
	 * 匯款分行<br>
	 * 
	 *
	 * @param remitBranch 匯款分行
	 */
	public void setRemitBranch(String remitBranch) {
		this.remitBranch = remitBranch;
	}

	/**
	 * 匯款帳號<br>
	 * 
	 * @return String
	 */
	public String getRemitAcctNo() {
		return this.remitAcctNo == null ? "" : this.remitAcctNo;
	}

	/**
	 * 匯款帳號<br>
	 * 
	 *
	 * @param remitAcctNo 匯款帳號
	 */
	public void setRemitAcctNo(String remitAcctNo) {
		this.remitAcctNo = remitAcctNo;
	}

	/**
	 * 收款戶號<br>
	 * 
	 * @return Integer
	 */
	public int getCustNo() {
		return this.custNo;
	}

	/**
	 * 收款戶號<br>
	 * 
	 *
	 * @param custNo 收款戶號
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
	 * 收款戶名<br>
	 * 
	 * @return String
	 */
	public String getCustName() {
		return this.custName == null ? "" : this.custName;
	}

	/**
	 * 收款戶名<br>
	 * 
	 *
	 * @param custName 收款戶名
	 */
	public void setCustName(String custName) {
		this.custName = custName;
	}

	/**
	 * 附言<br>
	 * 
	 * @return String
	 */
	public String getRemark() {
		return this.remark == null ? "" : this.remark;
	}

	/**
	 * 附言<br>
	 * 
	 *
	 * @param remark 附言
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 幣別<br>
	 * 
	 * @return String
	 */
	public String getCurrencyCode() {
		return this.currencyCode == null ? "" : this.currencyCode;
	}

	/**
	 * 幣別<br>
	 * 
	 *
	 * @param currencyCode 幣別
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	/**
	 * 匯款金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getRemitAmt() {
		return this.remitAmt;
	}

	/**
	 * 匯款金額<br>
	 * 
	 *
	 * @param remitAmt 匯款金額
	 */
	public void setRemitAmt(BigDecimal remitAmt) {
		this.remitAmt = remitAmt;
	}

	/**
	 * AML回應碼<br>
	 * CdCode:AmlCheckItem 0.非可疑名單/已完成名單確認 1.需審查/確認 2.為凍結名單/未確定名單
	 * 
	 * @return String
	 */
	public String getAmlRsp() {
		return this.amlRsp == null ? "" : this.amlRsp;
	}

	/**
	 * AML回應碼<br>
	 * CdCode:AmlCheckItem 0.非可疑名單/已完成名單確認 1.需審查/確認 2.為凍結名單/未確定名單
	 *
	 * @param amlRsp AML回應碼
	 */
	public void setAmlRsp(String amlRsp) {
		this.amlRsp = amlRsp;
	}

	/**
	 * 交易進行記號<br>
	 * 1STEP TX -&amp;gt; 0 (from eloan) 2STEP TX -&amp;gt; 1 2
	 * 
	 * @return Integer
	 */
	public int getActFg() {
		return this.actFg;
	}

	/**
	 * 交易進行記號<br>
	 * 1STEP TX -&amp;gt; 0 (from eloan) 2STEP TX -&amp;gt; 1 2
	 *
	 * @param actFg 交易進行記號
	 */
	public void setActFg(int actFg) {
		this.actFg = actFg;
	}

	/**
	 * 產檔後修正後內容<br>
	 * jason 格式
	 * 
	 * @return String
	 */
	public String getModifyContent() {
		return this.modifyContent == null ? "" : this.modifyContent;
	}

	/**
	 * 產檔後修正後內容<br>
	 * jason 格式
	 *
	 * @param modifyContent 產檔後修正後內容
	 */
	public void setModifyContent(String modifyContent) {
		this.modifyContent = modifyContent;
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
		return "BankRemit [bankRemitId=" + bankRemitId + ", batchNo=" + batchNo + ", drawdownCode=" + drawdownCode + ", statusCode=" + statusCode + ", remitBank=" + remitBank + ", remitBranch="
				+ remitBranch + ", remitAcctNo=" + remitAcctNo + ", custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + ", custName=" + custName + ", remark=" + remark
				+ ", currencyCode=" + currencyCode + ", remitAmt=" + remitAmt + ", amlRsp=" + amlRsp + ", actFg=" + actFg + ", modifyContent=" + modifyContent + ", createDate=" + createDate
				+ ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
