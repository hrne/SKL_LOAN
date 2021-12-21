package com.st1.itx.db.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.EmbeddedId;
import javax.persistence.Column;

/**
 * BankRelationSelf 金控利害關係人_關係人員工資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`BankRelationSelf`")
public class BankRelationSelf implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1594293086907102341L;

	@EmbeddedId
	private BankRelationSelfId bankRelationSelfId;

	// 借款戶所屬公司名稱
	@Column(name = "`CustName`", length = 70, insertable = false, updatable = false)
	private String custName;

	// 借款戶統編
	@Column(name = "`CustId`", length = 11, insertable = false, updatable = false)
	private String custId;

	// 金控法第44條
	@Column(name = "`LAW001`", length = 1)
	private String lAW001;

	// 金控法第44條(列項)
	@Column(name = "`LAW002`", length = 1)
	private String lAW002;

	// 金控法第45條
	@Column(name = "`LAW003`", length = 1)
	private String lAW003;

	// 保險法(放款)
	@Column(name = "`LAW005`", length = 1)
	private String lAW005;

	// 準利害關係人
	@Column(name = "`LAW008`", length = 1)
	private String lAW008;

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

	public BankRelationSelfId getBankRelationSelfId() {
		return this.bankRelationSelfId;
	}

	public void setBankRelationSelfId(BankRelationSelfId bankRelationSelfId) {
		this.bankRelationSelfId = bankRelationSelfId;
	}

	/**
	 * 借款戶所屬公司名稱<br>
	 * 
	 * @return String
	 */
	public String getCustName() {
		return this.custName == null ? "" : this.custName;
	}

	/**
	 * 借款戶所屬公司名稱<br>
	 * 
	 *
	 * @param custName 借款戶所屬公司名稱
	 */
	public void setCustName(String custName) {
		this.custName = custName;
	}

	/**
	 * 借款戶統編<br>
	 * 
	 * @return String
	 */
	public String getCustId() {
		return this.custId == null ? "" : this.custId;
	}

	/**
	 * 借款戶統編<br>
	 * 
	 *
	 * @param custId 借款戶統編
	 */
	public void setCustId(String custId) {
		this.custId = custId;
	}

	/**
	 * 金控法第44條<br>
	 * 
	 * @return String
	 */
	public String getLAW001() {
		return this.lAW001 == null ? "" : this.lAW001;
	}

	/**
	 * 金控法第44條<br>
	 * 
	 *
	 * @param lAW001 金控法第44條
	 */
	public void setLAW001(String lAW001) {
		this.lAW001 = lAW001;
	}

	/**
	 * 金控法第44條(列項)<br>
	 * 
	 * @return String
	 */
	public String getLAW002() {
		return this.lAW002 == null ? "" : this.lAW002;
	}

	/**
	 * 金控法第44條(列項)<br>
	 * 
	 *
	 * @param lAW002 金控法第44條(列項)
	 */
	public void setLAW002(String lAW002) {
		this.lAW002 = lAW002;
	}

	/**
	 * 金控法第45條<br>
	 * 
	 * @return String
	 */
	public String getLAW003() {
		return this.lAW003 == null ? "" : this.lAW003;
	}

	/**
	 * 金控法第45條<br>
	 * 
	 *
	 * @param lAW003 金控法第45條
	 */
	public void setLAW003(String lAW003) {
		this.lAW003 = lAW003;
	}

	/**
	 * 保險法(放款)<br>
	 * 
	 * @return String
	 */
	public String getLAW005() {
		return this.lAW005 == null ? "" : this.lAW005;
	}

	/**
	 * 保險法(放款)<br>
	 * 
	 *
	 * @param lAW005 保險法(放款)
	 */
	public void setLAW005(String lAW005) {
		this.lAW005 = lAW005;
	}

	/**
	 * 準利害關係人<br>
	 * 
	 * @return String
	 */
	public String getLAW008() {
		return this.lAW008 == null ? "" : this.lAW008;
	}

	/**
	 * 準利害關係人<br>
	 * 
	 *
	 * @param lAW008 準利害關係人
	 */
	public void setLAW008(String lAW008) {
		this.lAW008 = lAW008;
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
		return "BankRelationSelf [bankRelationSelfId=" + bankRelationSelfId + ", lAW001=" + lAW001 + ", lAW002=" + lAW002 + ", lAW003=" + lAW003 + ", lAW005=" + lAW005 + ", lAW008=" + lAW008
				+ ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
