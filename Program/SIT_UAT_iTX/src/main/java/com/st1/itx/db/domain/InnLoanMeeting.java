package com.st1.itx.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.Id;
import javax.persistence.Column;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * InnLoanMeeting 放審會記錄檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`InnLoanMeeting`")
public class InnLoanMeeting implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9165099170255043405L;

// 放審會流水號
	@Id
	@Column(name = "`MeetNo`")
	private int meetNo = 0;

	// 日期
	@Column(name = "`MeetingDate`")
	private int meetingDate = 0;

	// 戶別
	@Column(name = "`CustCode`", length = 1)
	private String custCode;

	// 金額
	@Column(name = "`Amount`")
	private BigDecimal amount = new BigDecimal("0");

	// 議題
	@Column(name = "`Issue`", length = 50)
	private String issue;

	// 備註
	@Column(name = "`Remark`", length = 500)
	private String remark;

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

	/**
	 * 放審會流水號<br>
	 * 
	 * @return Integer
	 */
	public int getMeetNo() {
		return this.meetNo;
	}

	/**
	 * 放審會流水號<br>
	 * 
	 *
	 * @param meetNo 放審會流水號
	 */
	public void setMeetNo(int meetNo) {
		this.meetNo = meetNo;
	}

	/**
	 * 日期<br>
	 * 
	 * @return Integer
	 */
	public int getMeetingDate() {
		return StaticTool.bcToRoc(this.meetingDate);
	}

	/**
	 * 日期<br>
	 * 
	 *
	 * @param meetingDate 日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setMeetingDate(int meetingDate) throws LogicException {
		this.meetingDate = StaticTool.rocToBc(meetingDate);
	}

	/**
	 * 戶別<br>
	 * 
	 * @return String
	 */
	public String getCustCode() {
		return this.custCode == null ? "" : this.custCode;
	}

	/**
	 * 戶別<br>
	 * 
	 *
	 * @param custCode 戶別
	 */
	public void setCustCode(String custCode) {
		this.custCode = custCode;
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
	 * 議題<br>
	 * 
	 * @return String
	 */
	public String getIssue() {
		return this.issue == null ? "" : this.issue;
	}

	/**
	 * 議題<br>
	 * 
	 *
	 * @param issue 議題
	 */
	public void setIssue(String issue) {
		this.issue = issue;
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
		return "InnLoanMeeting [meetNo=" + meetNo + ", meetingDate=" + meetingDate + ", custCode=" + custCode + ", amount=" + amount + ", issue=" + issue + ", remark=" + remark + ", createDate="
				+ createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
