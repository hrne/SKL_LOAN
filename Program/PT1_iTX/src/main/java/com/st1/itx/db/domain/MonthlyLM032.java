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
 * MonthlyLM032 逾期案件滾動率明細月報工作檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`MonthlyLM032`")
public class MonthlyLM032 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5380144383499332286L;

	@EmbeddedId
	private MonthlyLM032Id monthlyLM032Id;

	// 前期資料年月
	@Column(name = "`ADTYMT`", insertable = false, updatable = false)
	private int aDTYMT = 0;

	// 前期擔保品代號1
	@Column(name = "`GDRID1`")
	private int gDRID1 = 0;

	// 前期逾期期數
	@Column(name = "`W08PPR`")
	private int w08PPR = 0;

	// 前期戶號
	@Column(name = "`LMSACN`", insertable = false, updatable = false)
	private int lMSACN = 0;

	// 前期額度號碼
	@Column(name = "`LMSAPN`", insertable = false, updatable = false)
	private int lMSAPN = 0;

	// 前期本金餘額
	@Column(name = "`W08LBL`")
	private BigDecimal w08LBL = new BigDecimal("0");

	// 前期逾期天數
	@Column(name = "`W08DLY`")
	private int w08DLY = 0;

	// 當期戶況
	@Column(name = "`STATUS`", length = 2)
	private String sTATUS;

	// 當期資料年月
	@Column(name = "`ADTYMT01`")
	private int aDTYMT01 = 0;

	// 當期擔保品代號1
	@Column(name = "`GDRID101`")
	private int gDRID101 = 0;

	// 當期逾期期數
	@Column(name = "`W08PPR01`")
	private int w08PPR01 = 0;

	// 當期戶號
	@Column(name = "`LMSACN01`")
	private int lMSACN01 = 0;

	// 當期額度號碼
	@Column(name = "`LMSAPN01`")
	private int lMSAPN01 = 0;

	// 當期本金餘額
	@Column(name = "`W08LBL01`")
	private BigDecimal w08LBL01 = new BigDecimal("0");

	// 當期逾期天數
	@Column(name = "`W08DLY01`")
	private int w08DLY01 = 0;

	// 當期業務科目
	@Column(name = "`ACTACT`", length = 3)
	private String aCTACT;

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

	public MonthlyLM032Id getMonthlyLM032Id() {
		return this.monthlyLM032Id;
	}

	public void setMonthlyLM032Id(MonthlyLM032Id monthlyLM032Id) {
		this.monthlyLM032Id = monthlyLM032Id;
	}

	/**
	 * 前期資料年月<br>
	 * 
	 * @return Integer
	 */
	public int getADTYMT() {
		return this.aDTYMT;
	}

	/**
	 * 前期資料年月<br>
	 * 
	 *
	 * @param aDTYMT 前期資料年月
	 */
	public void setADTYMT(int aDTYMT) {
		this.aDTYMT = aDTYMT;
	}

	/**
	 * 前期擔保品代號1<br>
	 * 
	 * @return Integer
	 */
	public int getGDRID1() {
		return this.gDRID1;
	}

	/**
	 * 前期擔保品代號1<br>
	 * 
	 *
	 * @param gDRID1 前期擔保品代號1
	 */
	public void setGDRID1(int gDRID1) {
		this.gDRID1 = gDRID1;
	}

	/**
	 * 前期逾期期數<br>
	 * 
	 * @return Integer
	 */
	public int getW08PPR() {
		return this.w08PPR;
	}

	/**
	 * 前期逾期期數<br>
	 * 
	 *
	 * @param w08PPR 前期逾期期數
	 */
	public void setW08PPR(int w08PPR) {
		this.w08PPR = w08PPR;
	}

	/**
	 * 前期戶號<br>
	 * 
	 * @return Integer
	 */
	public int getLMSACN() {
		return this.lMSACN;
	}

	/**
	 * 前期戶號<br>
	 * 
	 *
	 * @param lMSACN 前期戶號
	 */
	public void setLMSACN(int lMSACN) {
		this.lMSACN = lMSACN;
	}

	/**
	 * 前期額度號碼<br>
	 * 
	 * @return Integer
	 */
	public int getLMSAPN() {
		return this.lMSAPN;
	}

	/**
	 * 前期額度號碼<br>
	 * 
	 *
	 * @param lMSAPN 前期額度號碼
	 */
	public void setLMSAPN(int lMSAPN) {
		this.lMSAPN = lMSAPN;
	}

	/**
	 * 前期本金餘額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getW08LBL() {
		return this.w08LBL;
	}

	/**
	 * 前期本金餘額<br>
	 * 
	 *
	 * @param w08LBL 前期本金餘額
	 */
	public void setW08LBL(BigDecimal w08LBL) {
		this.w08LBL = w08LBL;
	}

	/**
	 * 前期逾期天數<br>
	 * 
	 * @return Integer
	 */
	public int getW08DLY() {
		return this.w08DLY;
	}

	/**
	 * 前期逾期天數<br>
	 * 
	 *
	 * @param w08DLY 前期逾期天數
	 */
	public void setW08DLY(int w08DLY) {
		this.w08DLY = w08DLY;
	}

	/**
	 * 當期戶況<br>
	 * 
	 * @return String
	 */
	public String getSTATUS() {
		return this.sTATUS == null ? "" : this.sTATUS;
	}

	/**
	 * 當期戶況<br>
	 * 
	 *
	 * @param sTATUS 當期戶況
	 */
	public void setSTATUS(String sTATUS) {
		this.sTATUS = sTATUS;
	}

	/**
	 * 當期資料年月<br>
	 * 
	 * @return Integer
	 */
	public int getADTYMT01() {
		return this.aDTYMT01;
	}

	/**
	 * 當期資料年月<br>
	 * 
	 *
	 * @param aDTYMT01 當期資料年月
	 */
	public void setADTYMT01(int aDTYMT01) {
		this.aDTYMT01 = aDTYMT01;
	}

	/**
	 * 當期擔保品代號1<br>
	 * 
	 * @return Integer
	 */
	public int getGDRID101() {
		return this.gDRID101;
	}

	/**
	 * 當期擔保品代號1<br>
	 * 
	 *
	 * @param gDRID101 當期擔保品代號1
	 */
	public void setGDRID101(int gDRID101) {
		this.gDRID101 = gDRID101;
	}

	/**
	 * 當期逾期期數<br>
	 * 
	 * @return Integer
	 */
	public int getW08PPR01() {
		return this.w08PPR01;
	}

	/**
	 * 當期逾期期數<br>
	 * 
	 *
	 * @param w08PPR01 當期逾期期數
	 */
	public void setW08PPR01(int w08PPR01) {
		this.w08PPR01 = w08PPR01;
	}

	/**
	 * 當期戶號<br>
	 * 
	 * @return Integer
	 */
	public int getLMSACN01() {
		return this.lMSACN01;
	}

	/**
	 * 當期戶號<br>
	 * 
	 *
	 * @param lMSACN01 當期戶號
	 */
	public void setLMSACN01(int lMSACN01) {
		this.lMSACN01 = lMSACN01;
	}

	/**
	 * 當期額度號碼<br>
	 * 
	 * @return Integer
	 */
	public int getLMSAPN01() {
		return this.lMSAPN01;
	}

	/**
	 * 當期額度號碼<br>
	 * 
	 *
	 * @param lMSAPN01 當期額度號碼
	 */
	public void setLMSAPN01(int lMSAPN01) {
		this.lMSAPN01 = lMSAPN01;
	}

	/**
	 * 當期本金餘額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getW08LBL01() {
		return this.w08LBL01;
	}

	/**
	 * 當期本金餘額<br>
	 * 
	 *
	 * @param w08LBL01 當期本金餘額
	 */
	public void setW08LBL01(BigDecimal w08LBL01) {
		this.w08LBL01 = w08LBL01;
	}

	/**
	 * 當期逾期天數<br>
	 * 
	 * @return Integer
	 */
	public int getW08DLY01() {
		return this.w08DLY01;
	}

	/**
	 * 當期逾期天數<br>
	 * 
	 *
	 * @param w08DLY01 當期逾期天數
	 */
	public void setW08DLY01(int w08DLY01) {
		this.w08DLY01 = w08DLY01;
	}

	/**
	 * 當期業務科目<br>
	 * 
	 * @return String
	 */
	public String getACTACT() {
		return this.aCTACT == null ? "" : this.aCTACT;
	}

	/**
	 * 當期業務科目<br>
	 * 
	 *
	 * @param aCTACT 當期業務科目
	 */
	public void setACTACT(String aCTACT) {
		this.aCTACT = aCTACT;
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
		return "MonthlyLM032 [monthlyLM032Id=" + monthlyLM032Id + ", gDRID1=" + gDRID1 + ", w08PPR=" + w08PPR + ", w08LBL=" + w08LBL + ", w08DLY=" + w08DLY + ", sTATUS=" + sTATUS + ", aDTYMT01="
				+ aDTYMT01 + ", gDRID101=" + gDRID101 + ", w08PPR01=" + w08PPR01 + ", lMSACN01=" + lMSACN01 + ", lMSAPN01=" + lMSAPN01 + ", w08LBL01=" + w08LBL01 + ", w08DLY01=" + w08DLY01
				+ ", aCTACT=" + aCTACT + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
