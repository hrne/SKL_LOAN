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

/**
 * MlaundryParas 疑似洗錢樣態條件設定檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`MlaundryParas`")
public class MlaundryParas implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8889661918021663285L;

// 業務類型
	/* LN */
	@Id
	@Column(name = "`BusinessType`", length = 2)
	private String businessType = " ";

	// 洗錢樣態一金額合計超過
	/* L8201維護 : 金額合計超過 */
	@Column(name = "`Factor1TotLimit`")
	private BigDecimal factor1TotLimit = new BigDecimal("0");

	// 洗錢樣態二次數
	/* L8201維護 : 次數 */
	@Column(name = "`Factor2Count`")
	private int factor2Count = 0;

	// 洗錢樣態二單筆起始金額
	/* L8201維護 : 單筆起始金額 */
	@Column(name = "`Factor2AmtStart`")
	private BigDecimal factor2AmtStart = new BigDecimal("0");

	// 洗錢樣態二單筆迄止金額
	/* L8201維護 : 單筆迄止金額 */
	@Column(name = "`Factor2AmtEnd`")
	private BigDecimal factor2AmtEnd = new BigDecimal("0");

	// 洗錢樣態三金額合計超過
	/* L8201維護 : 金額合計超過 */
	@Column(name = "`Factor3TotLimit`")
	private BigDecimal factor3TotLimit = new BigDecimal("0");

	// 統計期間天數
	/* L8201維護 : 統計期間天數 */
	@Column(name = "`FactorDays`")
	private int factorDays = 0;

	// 樣態三統計期間天數
	/* L8201維護 : 統計期間天數 */
	@Column(name = "`FactorDays3`")
	private int factorDays3 = 0;

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
	 * 業務類型<br>
	 * LN
	 * 
	 * @return String
	 */
	public String getBusinessType() {
		return this.businessType == null ? "" : this.businessType;
	}

	/**
	 * 業務類型<br>
	 * LN
	 *
	 * @param businessType 業務類型
	 */
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	/**
	 * 洗錢樣態一金額合計超過<br>
	 * L8201維護 : 金額合計超過
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getFactor1TotLimit() {
		return this.factor1TotLimit;
	}

	/**
	 * 洗錢樣態一金額合計超過<br>
	 * L8201維護 : 金額合計超過
	 *
	 * @param factor1TotLimit 洗錢樣態一金額合計超過
	 */
	public void setFactor1TotLimit(BigDecimal factor1TotLimit) {
		this.factor1TotLimit = factor1TotLimit;
	}

	/**
	 * 洗錢樣態二次數<br>
	 * L8201維護 : 次數
	 * 
	 * @return Integer
	 */
	public int getFactor2Count() {
		return this.factor2Count;
	}

	/**
	 * 洗錢樣態二次數<br>
	 * L8201維護 : 次數
	 *
	 * @param factor2Count 洗錢樣態二次數
	 */
	public void setFactor2Count(int factor2Count) {
		this.factor2Count = factor2Count;
	}

	/**
	 * 洗錢樣態二單筆起始金額<br>
	 * L8201維護 : 單筆起始金額
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getFactor2AmtStart() {
		return this.factor2AmtStart;
	}

	/**
	 * 洗錢樣態二單筆起始金額<br>
	 * L8201維護 : 單筆起始金額
	 *
	 * @param factor2AmtStart 洗錢樣態二單筆起始金額
	 */
	public void setFactor2AmtStart(BigDecimal factor2AmtStart) {
		this.factor2AmtStart = factor2AmtStart;
	}

	/**
	 * 洗錢樣態二單筆迄止金額<br>
	 * L8201維護 : 單筆迄止金額
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getFactor2AmtEnd() {
		return this.factor2AmtEnd;
	}

	/**
	 * 洗錢樣態二單筆迄止金額<br>
	 * L8201維護 : 單筆迄止金額
	 *
	 * @param factor2AmtEnd 洗錢樣態二單筆迄止金額
	 */
	public void setFactor2AmtEnd(BigDecimal factor2AmtEnd) {
		this.factor2AmtEnd = factor2AmtEnd;
	}

	/**
	 * 洗錢樣態三金額合計超過<br>
	 * L8201維護 : 金額合計超過
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getFactor3TotLimit() {
		return this.factor3TotLimit;
	}

	/**
	 * 洗錢樣態三金額合計超過<br>
	 * L8201維護 : 金額合計超過
	 *
	 * @param factor3TotLimit 洗錢樣態三金額合計超過
	 */
	public void setFactor3TotLimit(BigDecimal factor3TotLimit) {
		this.factor3TotLimit = factor3TotLimit;
	}

	/**
	 * 統計期間天數<br>
	 * L8201維護 : 統計期間天數
	 * 
	 * @return Integer
	 */
	public int getFactorDays() {
		return this.factorDays;
	}

	/**
	 * 統計期間天數<br>
	 * L8201維護 : 統計期間天數
	 *
	 * @param factorDays 統計期間天數
	 */
	public void setFactorDays(int factorDays) {
		this.factorDays = factorDays;
	}

	/**
	 * 樣態三統計期間天數<br>
	 * L8201維護 : 統計期間天數
	 * 
	 * @return Integer
	 */
	public int getFactorDays3() {
		return this.factorDays3;
	}

	/**
	 * 樣態三統計期間天數<br>
	 * L8201維護 : 統計期間天數
	 *
	 * @param factorDays3 樣態三統計期間天數
	 */
	public void setFactorDays3(int factorDays3) {
		this.factorDays3 = factorDays3;
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
		return "MlaundryParas [businessType=" + businessType + ", factor1TotLimit=" + factor1TotLimit + ", factor2Count=" + factor2Count + ", factor2AmtStart=" + factor2AmtStart + ", factor2AmtEnd="
				+ factor2AmtEnd + ", factor3TotLimit=" + factor3TotLimit + ", factorDays=" + factorDays + ", factorDays3=" + factorDays3 + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
				+ ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
