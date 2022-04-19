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
 * FinReportQuality 客戶財務報表.財報品質<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`FinReportQuality`")
public class FinReportQuality implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6162426767505518743L;

	@EmbeddedId
	private FinReportQualityId finReportQualityId;

	// 客戶識別碼
	@Column(name = "`CustUKey`", length = 32, insertable = false, updatable = false)
	private String custUKey;

	// 識別碼
	@Column(name = "`UKey`", length = 32, insertable = false, updatable = false)
	private String uKey;

	// 年度財務報表類型
	/* 0:否,1:是 */
	@Column(name = "`ReportType`", length = 1)
	private String reportType;

	// 會計師查核意見
	@Column(name = "`Opinion`", length = 1)
	private String opinion;

	// 是否經會計師查核
	/* 0:否,1:是 */
	@Column(name = "`IsCheck`", length = 1)
	private String isCheck;

	// 近兩年是否曾換會計師
	/* 0:否,1:是 */
	@Column(name = "`IsChange`", length = 1)
	private String isChange;

	// 會計師事務所類型
	@Column(name = "`OfficeType`", length = 1)
	private String officeType;

	// 會計師懲戒紀錄
	/* 0:否,1:是 */
	@Column(name = "`PunishRecord`", length = 1)
	private String punishRecord;

	// 更換會計師原因
	@Column(name = "`ChangeReason`", length = 1)
	private String changeReason;

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

	public FinReportQualityId getFinReportQualityId() {
		return this.finReportQualityId;
	}

	public void setFinReportQualityId(FinReportQualityId finReportQualityId) {
		this.finReportQualityId = finReportQualityId;
	}

	/**
	 * 客戶識別碼<br>
	 * 
	 * @return String
	 */
	public String getCustUKey() {
		return this.custUKey == null ? "" : this.custUKey;
	}

	/**
	 * 客戶識別碼<br>
	 * 
	 *
	 * @param custUKey 客戶識別碼
	 */
	public void setCustUKey(String custUKey) {
		this.custUKey = custUKey;
	}

	/**
	 * 識別碼<br>
	 * 
	 * @return String
	 */
	public String getUKey() {
		return this.uKey == null ? "" : this.uKey;
	}

	/**
	 * 識別碼<br>
	 * 
	 *
	 * @param uKey 識別碼
	 */
	public void setUKey(String uKey) {
		this.uKey = uKey;
	}

	/**
	 * 年度財務報表類型<br>
	 * 0:否,1:是
	 * 
	 * @return String
	 */
	public String getReportType() {
		return this.reportType == null ? "" : this.reportType;
	}

	/**
	 * 年度財務報表類型<br>
	 * 0:否,1:是
	 *
	 * @param reportType 年度財務報表類型
	 */
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	/**
	 * 會計師查核意見<br>
	 * 
	 * @return String
	 */
	public String getOpinion() {
		return this.opinion == null ? "" : this.opinion;
	}

	/**
	 * 會計師查核意見<br>
	 * 
	 *
	 * @param opinion 會計師查核意見
	 */
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	/**
	 * 是否經會計師查核<br>
	 * 0:否,1:是
	 * 
	 * @return String
	 */
	public String getIsCheck() {
		return this.isCheck == null ? "" : this.isCheck;
	}

	/**
	 * 是否經會計師查核<br>
	 * 0:否,1:是
	 *
	 * @param isCheck 是否經會計師查核
	 */
	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}

	/**
	 * 近兩年是否曾換會計師<br>
	 * 0:否,1:是
	 * 
	 * @return String
	 */
	public String getIsChange() {
		return this.isChange == null ? "" : this.isChange;
	}

	/**
	 * 近兩年是否曾換會計師<br>
	 * 0:否,1:是
	 *
	 * @param isChange 近兩年是否曾換會計師
	 */
	public void setIsChange(String isChange) {
		this.isChange = isChange;
	}

	/**
	 * 會計師事務所類型<br>
	 * 
	 * @return String
	 */
	public String getOfficeType() {
		return this.officeType == null ? "" : this.officeType;
	}

	/**
	 * 會計師事務所類型<br>
	 * 
	 *
	 * @param officeType 會計師事務所類型
	 */
	public void setOfficeType(String officeType) {
		this.officeType = officeType;
	}

	/**
	 * 會計師懲戒紀錄<br>
	 * 0:否,1:是
	 * 
	 * @return String
	 */
	public String getPunishRecord() {
		return this.punishRecord == null ? "" : this.punishRecord;
	}

	/**
	 * 會計師懲戒紀錄<br>
	 * 0:否,1:是
	 *
	 * @param punishRecord 會計師懲戒紀錄
	 */
	public void setPunishRecord(String punishRecord) {
		this.punishRecord = punishRecord;
	}

	/**
	 * 更換會計師原因<br>
	 * 
	 * @return String
	 */
	public String getChangeReason() {
		return this.changeReason == null ? "" : this.changeReason;
	}

	/**
	 * 更換會計師原因<br>
	 * 
	 *
	 * @param changeReason 更換會計師原因
	 */
	public void setChangeReason(String changeReason) {
		this.changeReason = changeReason;
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
		return "FinReportQuality [finReportQualityId=" + finReportQualityId + ", reportType=" + reportType + ", opinion=" + opinion + ", isCheck=" + isCheck + ", isChange=" + isChange
				+ ", officeType=" + officeType + ", punishRecord=" + punishRecord + ", changeReason=" + changeReason + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate="
				+ lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
