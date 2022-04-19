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
 * JcicZ067 更生債務人繳款資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ067`")
public class JcicZ067 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6525372433708078957L;

	@EmbeddedId
	private JcicZ067Id jcicZ067Id;

	// 交易代碼
	/* A:新增;C:異動 */
	@Column(name = "`TranKey`", length = 1)
	private String tranKey;

	// 債務人IDN
	/* 目前可輸入四碼 */
	@Column(name = "`CustId`", length = 10, insertable = false, updatable = false)
	private String custId;

	// 報送單位代號
	@Column(name = "`SubmitKey`", length = 3, insertable = false, updatable = false)
	private String submitKey;

	// 款項統一收付申請日
	@Column(name = "`ApplyDate`", insertable = false, updatable = false)
	private int applyDate = 0;

	// 繳款日期
	@Column(name = "`PayDate`", insertable = false, updatable = false)
	private int payDate = 0;

	// 本日繳款金額
	@Column(name = "`PayAmt`")
	private BigDecimal payAmt = new BigDecimal("0");

	// 累計繳款金額
	@Column(name = "`TotalPayAmt`")
	private BigDecimal totalPayAmt = new BigDecimal("0");

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

	public JcicZ067Id getJcicZ067Id() {
		return this.jcicZ067Id;
	}

	public void setJcicZ067Id(JcicZ067Id jcicZ067Id) {
		this.jcicZ067Id = jcicZ067Id;
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
	 * 債務人IDN<br>
	 * 目前可輸入四碼
	 * 
	 * @return String
	 */
	public String getCustId() {
		return this.custId == null ? "" : this.custId;
	}

	/**
	 * 債務人IDN<br>
	 * 目前可輸入四碼
	 *
	 * @param custId 債務人IDN
	 */
	public void setCustId(String custId) {
		this.custId = custId;
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
	 * 繳款日期<br>
	 * 
	 * @return Integer
	 */
	public int getPayDate() {
		return StaticTool.bcToRoc(this.payDate);
	}

	/**
	 * 繳款日期<br>
	 * 
	 *
	 * @param payDate 繳款日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setPayDate(int payDate) throws LogicException {
		this.payDate = StaticTool.rocToBc(payDate);
	}

	/**
	 * 本日繳款金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPayAmt() {
		return this.payAmt;
	}

	/**
	 * 本日繳款金額<br>
	 * 
	 *
	 * @param payAmt 本日繳款金額
	 */
	public void setPayAmt(BigDecimal payAmt) {
		this.payAmt = payAmt;
	}

	/**
	 * 累計繳款金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getTotalPayAmt() {
		return this.totalPayAmt;
	}

	/**
	 * 累計繳款金額<br>
	 * 
	 *
	 * @param totalPayAmt 累計繳款金額
	 */
	public void setTotalPayAmt(BigDecimal totalPayAmt) {
		this.totalPayAmt = totalPayAmt;
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
		return "JcicZ067 [jcicZ067Id=" + jcicZ067Id + ", tranKey=" + tranKey + ", payAmt=" + payAmt + ", totalPayAmt=" + totalPayAmt + ", outJcicTxtDate=" + outJcicTxtDate + ", createDate="
				+ createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
