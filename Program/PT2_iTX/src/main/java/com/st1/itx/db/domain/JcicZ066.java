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
 * JcicZ066 受理更生款項統一收付通知<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ066`")
public class JcicZ066 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2240680072212979326L;

	@EmbeddedId
	private JcicZ066Id jcicZ066Id;

	// 交易代碼
	/* A:新增;C:異動 */
	@Column(name = "`TranKey`", length = 1)
	private String tranKey;

	// 報送單位代號
	@Column(name = "`SubmitKey`", length = 3, insertable = false, updatable = false)
	private String submitKey;

	// 債務人IDN
	@Column(name = "`CustId`", length = 10, insertable = false, updatable = false)
	private String custId;

	// 款項統一收付申請日
	@Column(name = "`ApplyDate`", insertable = false, updatable = false)
	private int applyDate = 0;

	// 生效日期
	@Column(name = "`StartDate`")
	private int startDate = 0;

	// 本分配表首繳日
	@Column(name = "`PayDate`", insertable = false, updatable = false)
	private int payDate = 0;

	// 債權金融機構代號
	@Column(name = "`BankId`", length = 10, insertable = false, updatable = false)
	private String bankId;

	// 參與分配債權金額
	@Column(name = "`AllotAmt`")
	private BigDecimal allotAmt = new BigDecimal("0");

	// 債權比例
	@Column(name = "`OwnPercentage`")
	private BigDecimal ownPercentage = new BigDecimal("0");

	// 轉JCIC文字檔日期
	@Column(name = "`OutJcicTxtDate`")
	private int outJcicTxtDate = 0;

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

	public JcicZ066Id getJcicZ066Id() {
		return this.jcicZ066Id;
	}

	public void setJcicZ066Id(JcicZ066Id jcicZ066Id) {
		this.jcicZ066Id = jcicZ066Id;
	}

	/**
	 * 交易代碼<br>
	 * A:新增;C:異動
	 * 
	 * @return String
	 */
	public String getTranKey() {
		return this.tranKey == null ? "" : this.tranKey;
	}

	/**
	 * 交易代碼<br>
	 * A:新增;C:異動
	 *
	 * @param tranKey 交易代碼
	 */
	public void setTranKey(String tranKey) {
		this.tranKey = tranKey;
	}

	/**
	 * 報送單位代號<br>
	 * 
	 * @return String
	 */
	public String getSubmitKey() {
		return this.submitKey == null ? "" : this.submitKey;
	}

	/**
	 * 報送單位代號<br>
	 * 
	 *
	 * @param submitKey 報送單位代號
	 */
	public void setSubmitKey(String submitKey) {
		this.submitKey = submitKey;
	}

	/**
	 * 債務人IDN<br>
	 * 
	 * @return String
	 */
	public String getCustId() {
		return this.custId == null ? "" : this.custId;
	}

	/**
	 * 債務人IDN<br>
	 * 
	 *
	 * @param custId 債務人IDN
	 */
	public void setCustId(String custId) {
		this.custId = custId;
	}

	/**
	 * 款項統一收付申請日<br>
	 * 
	 * @return Integer
	 */
	public int getApplyDate() {
		return StaticTool.bcToRoc(this.applyDate);
	}

	/**
	 * 款項統一收付申請日<br>
	 * 
	 *
	 * @param applyDate 款項統一收付申請日
	 * @throws LogicException when Date Is Warn
	 */
	public void setApplyDate(int applyDate) throws LogicException {
		this.applyDate = StaticTool.rocToBc(applyDate);
	}

	/**
	 * 生效日期<br>
	 * 
	 * @return Integer
	 */
	public int getStartDate() {
		return StaticTool.bcToRoc(this.startDate);
	}

	/**
	 * 生效日期<br>
	 * 
	 *
	 * @param startDate 生效日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setStartDate(int startDate) throws LogicException {
		this.startDate = StaticTool.rocToBc(startDate);
	}

	/**
	 * 本分配表首繳日<br>
	 * 
	 * @return Integer
	 */
	public int getPayDate() {
		return StaticTool.bcToRoc(this.payDate);
	}

	/**
	 * 本分配表首繳日<br>
	 * 
	 *
	 * @param payDate 本分配表首繳日
	 * @throws LogicException when Date Is Warn
	 */
	public void setPayDate(int payDate) throws LogicException {
		this.payDate = StaticTool.rocToBc(payDate);
	}

	/**
	 * 債權金融機構代號<br>
	 * 
	 * @return String
	 */
	public String getBankId() {
		return this.bankId == null ? "" : this.bankId;
	}

	/**
	 * 債權金融機構代號<br>
	 * 
	 *
	 * @param bankId 債權金融機構代號
	 */
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	/**
	 * 參與分配債權金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAllotAmt() {
		return this.allotAmt;
	}

	/**
	 * 參與分配債權金額<br>
	 * 
	 *
	 * @param allotAmt 參與分配債權金額
	 */
	public void setAllotAmt(BigDecimal allotAmt) {
		this.allotAmt = allotAmt;
	}

	/**
	 * 債權比例<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getOwnPercentage() {
		return this.ownPercentage;
	}

	/**
	 * 債權比例<br>
	 * 
	 *
	 * @param ownPercentage 債權比例
	 */
	public void setOwnPercentage(BigDecimal ownPercentage) {
		this.ownPercentage = ownPercentage;
	}

	/**
	 * 轉JCIC文字檔日期<br>
	 * 
	 * @return Integer
	 */
	public int getOutJcicTxtDate() {
		return StaticTool.bcToRoc(this.outJcicTxtDate);
	}

	/**
	 * 轉JCIC文字檔日期<br>
	 * 
	 *
	 * @param outJcicTxtDate 轉JCIC文字檔日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setOutJcicTxtDate(int outJcicTxtDate) throws LogicException {
		this.outJcicTxtDate = StaticTool.rocToBc(outJcicTxtDate);
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
		return "JcicZ066 [jcicZ066Id=" + jcicZ066Id + ", tranKey=" + tranKey + ", startDate=" + startDate + ", allotAmt=" + allotAmt + ", ownPercentage=" + ownPercentage + ", outJcicTxtDate="
				+ outJcicTxtDate + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
