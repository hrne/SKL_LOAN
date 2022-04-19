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
 * RptRelationSelf 報表用_金控利害關係人_關係人資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`RptRelationSelf`")
public class RptRelationSelf implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7908221808328356406L;

	@EmbeddedId
	private RptRelationSelfId rptRelationSelfId;

	// CusId
	@Column(name = "`CusId`", length = 20, insertable = false, updatable = false)
	private String cusId;

	// CusName
	@Column(name = "`CusName`", length = 30)
	private String cusName;

	// STSCD
	@Column(name = "`STSCD`", length = 2, insertable = false, updatable = false)
	private String sTSCD;

	// CusCCD
	@Column(name = "`CusCCD`", length = 1)
	private String cusCCD;

	// CusSCD
	@Column(name = "`CusSCD`", length = 2, insertable = false, updatable = false)
	private String cusSCD;

	// LAW001
	@Column(name = "`LAW001`", length = 1)
	private String lAW001;

	// LAW002
	@Column(name = "`LAW002`", length = 1)
	private String lAW002;

	// LAW003
	@Column(name = "`LAW003`", length = 1)
	private String lAW003;

	// LAW004
	@Column(name = "`LAW004`", length = 1)
	private String lAW004;

	// LAW005
	@Column(name = "`LAW005`", length = 1)
	private String lAW005;

	// LAW006
	@Column(name = "`LAW006`", length = 1)
	private String lAW006;

	// LAW007
	@Column(name = "`LAW007`", length = 1)
	private String lAW007;

	// LAW008
	@Column(name = "`LAW008`", length = 1)
	private String lAW008;

	// LAW009
	@Column(name = "`LAW009`", length = 1)
	private String lAW009;

	// LAW010
	@Column(name = "`LAW010`", length = 1)
	private String lAW010;

	// Mark
	@Column(name = "`Mark`", length = 10)
	private String mark;

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

	public RptRelationSelfId getRptRelationSelfId() {
		return this.rptRelationSelfId;
	}

	public void setRptRelationSelfId(RptRelationSelfId rptRelationSelfId) {
		this.rptRelationSelfId = rptRelationSelfId;
	}

	/**
	 * CusId<br>
	 * 
	 * @return String
	 */
	public String getCusId() {
		return this.cusId == null ? "" : this.cusId;
	}

	/**
	 * CusId<br>
	 * 
	 *
	 * @param cusId CusId
	 */
	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	/**
	 * CusName<br>
	 * 
	 * @return String
	 */
	public String getCusName() {
		return this.cusName == null ? "" : this.cusName;
	}

	/**
	 * CusName<br>
	 * 
	 *
	 * @param cusName CusName
	 */
	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	/**
	 * STSCD<br>
	 * 
	 * @return String
	 */
	public String getSTSCD() {
		return this.sTSCD == null ? "" : this.sTSCD;
	}

	/**
	 * STSCD<br>
	 * 
	 *
	 * @param sTSCD STSCD
	 */
	public void setSTSCD(String sTSCD) {
		this.sTSCD = sTSCD;
	}

	/**
	 * CusCCD<br>
	 * 
	 * @return String
	 */
	public String getCusCCD() {
		return this.cusCCD == null ? "" : this.cusCCD;
	}

	/**
	 * CusCCD<br>
	 * 
	 *
	 * @param cusCCD CusCCD
	 */
	public void setCusCCD(String cusCCD) {
		this.cusCCD = cusCCD;
	}

	/**
	 * CusSCD<br>
	 * 
	 * @return String
	 */
	public String getCusSCD() {
		return this.cusSCD == null ? "" : this.cusSCD;
	}

	/**
	 * CusSCD<br>
	 * 
	 *
	 * @param cusSCD CusSCD
	 */
	public void setCusSCD(String cusSCD) {
		this.cusSCD = cusSCD;
	}

	/**
	 * LAW001<br>
	 * 
	 * @return String
	 */
	public String getLAW001() {
		return this.lAW001 == null ? "" : this.lAW001;
	}

	/**
	 * LAW001<br>
	 * 
	 *
	 * @param lAW001 LAW001
	 */
	public void setLAW001(String lAW001) {
		this.lAW001 = lAW001;
	}

	/**
	 * LAW002<br>
	 * 
	 * @return String
	 */
	public String getLAW002() {
		return this.lAW002 == null ? "" : this.lAW002;
	}

	/**
	 * LAW002<br>
	 * 
	 *
	 * @param lAW002 LAW002
	 */
	public void setLAW002(String lAW002) {
		this.lAW002 = lAW002;
	}

	/**
	 * LAW003<br>
	 * 
	 * @return String
	 */
	public String getLAW003() {
		return this.lAW003 == null ? "" : this.lAW003;
	}

	/**
	 * LAW003<br>
	 * 
	 *
	 * @param lAW003 LAW003
	 */
	public void setLAW003(String lAW003) {
		this.lAW003 = lAW003;
	}

	/**
	 * LAW004<br>
	 * 
	 * @return String
	 */
	public String getLAW004() {
		return this.lAW004 == null ? "" : this.lAW004;
	}

	/**
	 * LAW004<br>
	 * 
	 *
	 * @param lAW004 LAW004
	 */
	public void setLAW004(String lAW004) {
		this.lAW004 = lAW004;
	}

	/**
	 * LAW005<br>
	 * 
	 * @return String
	 */
	public String getLAW005() {
		return this.lAW005 == null ? "" : this.lAW005;
	}

	/**
	 * LAW005<br>
	 * 
	 *
	 * @param lAW005 LAW005
	 */
	public void setLAW005(String lAW005) {
		this.lAW005 = lAW005;
	}

	/**
	 * LAW006<br>
	 * 
	 * @return String
	 */
	public String getLAW006() {
		return this.lAW006 == null ? "" : this.lAW006;
	}

	/**
	 * LAW006<br>
	 * 
	 *
	 * @param lAW006 LAW006
	 */
	public void setLAW006(String lAW006) {
		this.lAW006 = lAW006;
	}

	/**
	 * LAW007<br>
	 * 
	 * @return String
	 */
	public String getLAW007() {
		return this.lAW007 == null ? "" : this.lAW007;
	}

	/**
	 * LAW007<br>
	 * 
	 *
	 * @param lAW007 LAW007
	 */
	public void setLAW007(String lAW007) {
		this.lAW007 = lAW007;
	}

	/**
	 * LAW008<br>
	 * 
	 * @return String
	 */
	public String getLAW008() {
		return this.lAW008 == null ? "" : this.lAW008;
	}

	/**
	 * LAW008<br>
	 * 
	 *
	 * @param lAW008 LAW008
	 */
	public void setLAW008(String lAW008) {
		this.lAW008 = lAW008;
	}

	/**
	 * LAW009<br>
	 * 
	 * @return String
	 */
	public String getLAW009() {
		return this.lAW009 == null ? "" : this.lAW009;
	}

	/**
	 * LAW009<br>
	 * 
	 *
	 * @param lAW009 LAW009
	 */
	public void setLAW009(String lAW009) {
		this.lAW009 = lAW009;
	}

	/**
	 * LAW010<br>
	 * 
	 * @return String
	 */
	public String getLAW010() {
		return this.lAW010 == null ? "" : this.lAW010;
	}

	/**
	 * LAW010<br>
	 * 
	 *
	 * @param lAW010 LAW010
	 */
	public void setLAW010(String lAW010) {
		this.lAW010 = lAW010;
	}

	/**
	 * Mark<br>
	 * 
	 * @return String
	 */
	public String getMark() {
		return this.mark == null ? "" : this.mark;
	}

	/**
	 * Mark<br>
	 * 
	 *
	 * @param mark Mark
	 */
	public void setMark(String mark) {
		this.mark = mark;
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
		return "RptRelationSelf [rptRelationSelfId=" + rptRelationSelfId + ", cusName=" + cusName + ", cusCCD=" + cusCCD + ", lAW001=" + lAW001 + ", lAW002=" + lAW002 + ", lAW003=" + lAW003
				+ ", lAW004=" + lAW004 + ", lAW005=" + lAW005 + ", lAW006=" + lAW006 + ", lAW007=" + lAW007 + ", lAW008=" + lAW008 + ", lAW009=" + lAW009 + ", lAW010=" + lAW010 + ", mark=" + mark
				+ ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
