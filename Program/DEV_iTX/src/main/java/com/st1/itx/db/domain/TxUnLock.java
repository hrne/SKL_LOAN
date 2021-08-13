package com.st1.itx.db.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.Id;
import javax.persistence.Column;

/**
 * TxUnLock 人工解除鎖定控制檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxUnLock`")
public class TxUnLock implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3116539838944367743L;

	// 鎖定序號
	@Id
	@Column(name = "`LockNo`")
	private Long lockNo = 0L;

	// 帳務日
	@Column(name = "`Entdy`")
	private int entdy = 0;

	// 戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 交易代號
	@Column(name = "`TranNo`", length = 5)
	private String tranNo;

	// 鎖定單位
	@Column(name = "`BrNo`", length = 4)
	private String brNo;

	// 鎖定櫃員
	@Column(name = "`TlrNo`", length = 6)
	private String tlrNo;

	// 解除鎖定單位
	@Column(name = "`BrNo2`", length = 4)
	private String brNo2;

	// 解除鎖定櫃員
	@Column(name = "`TlrNo2`", length = 6)
	private String tlrNo2;

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
	 * 鎖定序號<br>
	 * 
	 * @return Long
	 */
	public Long getLockNo() {
		return this.lockNo;
	}

	/**
	 * 鎖定序號<br>
	 * 
	 *
	 * @param lockNo 鎖定序號
	 */
	public void setLockNo(Long lockNo) {
		this.lockNo = lockNo;
	}

	/**
	 * 帳務日<br>
	 * 
	 * @return Integer
	 */
	public int getEntdy() {
		return this.entdy;
	}

	/**
	 * 帳務日<br>
	 * 
	 *
	 * @param entdy 帳務日
	 */
	public void setEntdy(int entdy) {
		this.entdy = entdy;
	}

	/**
	 * 戶號<br>
	 * 
	 * @return Integer
	 */
	public int getCustNo() {
		return this.custNo;
	}

	/**
	 * 戶號<br>
	 * 
	 *
	 * @param custNo 戶號
	 */
	public void setCustNo(int custNo) {
		this.custNo = custNo;
	}

	/**
	 * 交易代號<br>
	 * 
	 * @return String
	 */
	public String getTranNo() {
		return this.tranNo == null ? "" : this.tranNo;
	}

	/**
	 * 交易代號<br>
	 * 
	 *
	 * @param tranNo 交易代號
	 */
	public void setTranNo(String tranNo) {
		this.tranNo = tranNo;
	}

	/**
	 * 鎖定單位<br>
	 * 
	 * @return String
	 */
	public String getBrNo() {
		return this.brNo == null ? "" : this.brNo;
	}

	/**
	 * 鎖定單位<br>
	 * 
	 *
	 * @param brNo 鎖定單位
	 */
	public void setBrNo(String brNo) {
		this.brNo = brNo;
	}

	/**
	 * 鎖定櫃員<br>
	 * 
	 * @return String
	 */
	public String getTlrNo() {
		return this.tlrNo == null ? "" : this.tlrNo;
	}

	/**
	 * 鎖定櫃員<br>
	 * 
	 *
	 * @param tlrNo 鎖定櫃員
	 */
	public void setTlrNo(String tlrNo) {
		this.tlrNo = tlrNo;
	}

	/**
	 * 解除鎖定單位<br>
	 * 
	 * @return String
	 */
	public String getBrNo2() {
		return this.brNo2 == null ? "" : this.brNo2;
	}

	/**
	 * 解除鎖定單位<br>
	 * 
	 *
	 * @param brNo2 解除鎖定單位
	 */
	public void setBrNo2(String brNo2) {
		this.brNo2 = brNo2;
	}

	/**
	 * 解除鎖定櫃員<br>
	 * 
	 * @return String
	 */
	public String getTlrNo2() {
		return this.tlrNo2 == null ? "" : this.tlrNo2;
	}

	/**
	 * 解除鎖定櫃員<br>
	 * 
	 *
	 * @param tlrNo2 解除鎖定櫃員
	 */
	public void setTlrNo2(String tlrNo2) {
		this.tlrNo2 = tlrNo2;
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
		return "TxUnLock [lockNo=" + lockNo + ", entdy=" + entdy + ", custNo=" + custNo + ", tranNo=" + tranNo + ", brNo=" + brNo + ", tlrNo=" + tlrNo + ", brNo2=" + brNo2 + ", tlrNo2=" + tlrNo2
				+ ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
