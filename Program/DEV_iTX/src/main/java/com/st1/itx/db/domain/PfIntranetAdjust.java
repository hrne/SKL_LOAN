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
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * PfIntranetAdjust 內網報表業績調整檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`PfIntranetAdjust`")
public class PfIntranetAdjust implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4642824795348552655L;

// 序號
	@Id
	@Column(name = "`LogNo`")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "`PfIntranetAdjust_SEQ`")
	@SequenceGenerator(name = "`PfIntranetAdjust_SEQ`", sequenceName = "`PfIntranetAdjust_SEQ`", allocationSize = 1)
	private Long logNo = 0L;

	// 戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度編號
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 撥款序號
	@Column(name = "`BormNo`")
	private int bormNo = 0;

	// 業績日期
	@Column(name = "`PerfDate`")
	private int perfDate = 0;

	// 工作月
	@Column(name = "`WorkMonth`")
	private int workMonth = 0;

	// 工作季
	@Column(name = "`WorkSeason`")
	private int workSeason = 0;

	// 介紹人
	@Column(name = "`Introducer`", length = 6)
	private String introducer;

	// 房貸專員
	@Column(name = "`BsOfficer`", length = 6)
	private String bsOfficer;

	// 業績金額
	@Column(name = "`PerfAmt`")
	private BigDecimal perfAmt = new BigDecimal("0");

	// 業績件數
	@Column(name = "`PerfCnt`")
	private BigDecimal perfCnt = new BigDecimal("0");

	// 單位類別
	/* 1:全部 2:單位 3:區部 4:部室 5:區部＋部室 */
	@Column(name = "`UnitType`", length = 1)
	private String unitType;

	// 單位代號
	@Column(name = "`UnitCode`", length = 6)
	private String unitCode;

	// 區部代號
	@Column(name = "`DistCode`", length = 6)
	private String distCode;

	// 部室代號
	@Column(name = "`DeptCode`", length = 6)
	private String deptCode;

	// 累計達成加減金額
	@Column(name = "`SumAmt`")
	private BigDecimal sumAmt = new BigDecimal("0");

	// 累計達成加減件數
	@Column(name = "`SumCnt`")
	private BigDecimal sumCnt = new BigDecimal("0");

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
	 * 序號<br>
	 * 
	 * @return Long
	 */
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getLogNo() {
		return this.logNo;
	}

	/**
	 * 序號<br>
	 * 
	 *
	 * @param logNo 序號
	 */
	public void setLogNo(Long logNo) {
		this.logNo = logNo;
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
	 * 業績日期<br>
	 * 
	 * @return Integer
	 */
	public int getPerfDate() {
		return StaticTool.bcToRoc(this.perfDate);
	}

	/**
	 * 業績日期<br>
	 * 
	 *
	 * @param perfDate 業績日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setPerfDate(int perfDate) throws LogicException {
		this.perfDate = StaticTool.rocToBc(perfDate);
	}

	/**
	 * 工作月<br>
	 * 
	 * @return Integer
	 */
	public int getWorkMonth() {
		return this.workMonth;
	}

	/**
	 * 工作月<br>
	 * 
	 *
	 * @param workMonth 工作月
	 */
	public void setWorkMonth(int workMonth) {
		this.workMonth = workMonth;
	}

	/**
	 * 工作季<br>
	 * 
	 * @return Integer
	 */
	public int getWorkSeason() {
		return this.workSeason;
	}

	/**
	 * 工作季<br>
	 * 
	 *
	 * @param workSeason 工作季
	 */
	public void setWorkSeason(int workSeason) {
		this.workSeason = workSeason;
	}

	/**
	 * 介紹人<br>
	 * 
	 * @return String
	 */
	public String getIntroducer() {
		return this.introducer == null ? "" : this.introducer;
	}

	/**
	 * 介紹人<br>
	 * 
	 *
	 * @param introducer 介紹人
	 */
	public void setIntroducer(String introducer) {
		this.introducer = introducer;
	}

	/**
	 * 房貸專員<br>
	 * 
	 * @return String
	 */
	public String getBsOfficer() {
		return this.bsOfficer == null ? "" : this.bsOfficer;
	}

	/**
	 * 房貸專員<br>
	 * 
	 *
	 * @param bsOfficer 房貸專員
	 */
	public void setBsOfficer(String bsOfficer) {
		this.bsOfficer = bsOfficer;
	}

	/**
	 * 業績金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPerfAmt() {
		return this.perfAmt;
	}

	/**
	 * 業績金額<br>
	 * 
	 *
	 * @param perfAmt 業績金額
	 */
	public void setPerfAmt(BigDecimal perfAmt) {
		this.perfAmt = perfAmt;
	}

	/**
	 * 業績件數<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPerfCnt() {
		return this.perfCnt;
	}

	/**
	 * 業績件數<br>
	 * 
	 *
	 * @param perfCnt 業績件數
	 */
	public void setPerfCnt(BigDecimal perfCnt) {
		this.perfCnt = perfCnt;
	}

	/**
	 * 單位類別<br>
	 * 1:全部 2:單位 3:區部 4:部室 5:區部＋部室
	 * 
	 * @return String
	 */
	public String getUnitType() {
		return this.unitType == null ? "" : this.unitType;
	}

	/**
	 * 單位類別<br>
	 * 1:全部 2:單位 3:區部 4:部室 5:區部＋部室
	 *
	 * @param unitType 單位類別
	 */
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	/**
	 * 單位代號<br>
	 * 
	 * @return String
	 */
	public String getUnitCode() {
		return this.unitCode == null ? "" : this.unitCode;
	}

	/**
	 * 單位代號<br>
	 * 
	 *
	 * @param unitCode 單位代號
	 */
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	/**
	 * 區部代號<br>
	 * 
	 * @return String
	 */
	public String getDistCode() {
		return this.distCode == null ? "" : this.distCode;
	}

	/**
	 * 區部代號<br>
	 * 
	 *
	 * @param distCode 區部代號
	 */
	public void setDistCode(String distCode) {
		this.distCode = distCode;
	}

	/**
	 * 部室代號<br>
	 * 
	 * @return String
	 */
	public String getDeptCode() {
		return this.deptCode == null ? "" : this.deptCode;
	}

	/**
	 * 部室代號<br>
	 * 
	 *
	 * @param deptCode 部室代號
	 */
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	/**
	 * 累計達成加減金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getSumAmt() {
		return this.sumAmt;
	}

	/**
	 * 累計達成加減金額<br>
	 * 
	 *
	 * @param sumAmt 累計達成加減金額
	 */
	public void setSumAmt(BigDecimal sumAmt) {
		this.sumAmt = sumAmt;
	}

	/**
	 * 累計達成加減件數<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getSumCnt() {
		return this.sumCnt;
	}

	/**
	 * 累計達成加減件數<br>
	 * 
	 *
	 * @param sumCnt 累計達成加減件數
	 */
	public void setSumCnt(BigDecimal sumCnt) {
		this.sumCnt = sumCnt;
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
		return "PfIntranetAdjust [logNo=" + logNo + ", custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + ", perfDate=" + perfDate + ", workMonth=" + workMonth + ", workSeason="
				+ workSeason + ", introducer=" + introducer + ", bsOfficer=" + bsOfficer + ", perfAmt=" + perfAmt + ", perfCnt=" + perfCnt + ", unitType=" + unitType + ", unitCode=" + unitCode
				+ ", distCode=" + distCode + ", deptCode=" + deptCode + ", sumAmt=" + sumAmt + ", sumCnt=" + sumCnt + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate="
				+ lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
