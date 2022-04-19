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

/**
 * LoanSyndItem 聯貸案費用檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`LoanSyndItem`")
public class LoanSyndItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4302287181076433446L;

	@EmbeddedId
	private LoanSyndItemId loanSyndItemId;

	// 聯貸案編號
	@Column(name = "`SyndNo`", insertable = false, updatable = false)
	private int syndNo = 0;

	// 聯貸案序號
	@Column(name = "`SyndSeq`", insertable = false, updatable = false)
	private int syndSeq = 0;

	// 項別
	@Column(name = "`Item`", length = 10)
	private String item;

	// 金額
	@Column(name = "`SyndAmt`")
	private BigDecimal syndAmt = new BigDecimal("0");

	// 備註
	@Column(name = "`SyndMark`", length = 40)
	private String syndMark;

	// 已銷金額
	@Column(name = "`SyndBal`")
	private BigDecimal syndBal = new BigDecimal("0");

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

	public LoanSyndItemId getLoanSyndItemId() {
		return this.loanSyndItemId;
	}

	public void setLoanSyndItemId(LoanSyndItemId loanSyndItemId) {
		this.loanSyndItemId = loanSyndItemId;
	}

	/**
	 * 聯貸案編號<br>
	 * 
	 * @return Integer
	 */
	public int getSyndNo() {
		return this.syndNo;
	}

	/**
	 * 聯貸案編號<br>
	 * 
	 *
	 * @param syndNo 聯貸案編號
	 */
	public void setSyndNo(int syndNo) {
		this.syndNo = syndNo;
	}

	/**
	 * 聯貸案序號<br>
	 * 
	 * @return Integer
	 */
	public int getSyndSeq() {
		return this.syndSeq;
	}

	/**
	 * 聯貸案序號<br>
	 * 
	 *
	 * @param syndSeq 聯貸案序號
	 */
	public void setSyndSeq(int syndSeq) {
		this.syndSeq = syndSeq;
	}

	/**
	 * 項別<br>
	 * 
	 * @return String
	 */
	public String getItem() {
		return this.item == null ? "" : this.item;
	}

	/**
	 * 項別<br>
	 * 
	 *
	 * @param item 項別
	 */
	public void setItem(String item) {
		this.item = item;
	}

	/**
	 * 金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getSyndAmt() {
		return this.syndAmt;
	}

	/**
	 * 金額<br>
	 * 
	 *
	 * @param syndAmt 金額
	 */
	public void setSyndAmt(BigDecimal syndAmt) {
		this.syndAmt = syndAmt;
	}

	/**
	 * 備註<br>
	 * 
	 * @return String
	 */
	public String getSyndMark() {
		return this.syndMark == null ? "" : this.syndMark;
	}

	/**
	 * 備註<br>
	 * 
	 *
	 * @param syndMark 備註
	 */
	public void setSyndMark(String syndMark) {
		this.syndMark = syndMark;
	}

	/**
	 * 已銷金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getSyndBal() {
		return this.syndBal;
	}

	/**
	 * 已銷金額<br>
	 * 
	 *
	 * @param syndBal 已銷金額
	 */
	public void setSyndBal(BigDecimal syndBal) {
		this.syndBal = syndBal;
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
		return "LoanSyndItem [loanSyndItemId=" + loanSyndItemId + ", item=" + item + ", syndAmt=" + syndAmt + ", syndMark=" + syndMark + ", syndBal=" + syndBal + ", createDate=" + createDate
				+ ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
