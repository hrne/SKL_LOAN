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
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * CdWorkMonth 放款業績工作月對照檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdWorkMonth`")
public class CdWorkMonth implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5148936569655338550L;

	@EmbeddedId
	private CdWorkMonthId cdWorkMonthId;

	// 業績年度
	@Column(name = "`Year`", insertable = false, updatable = false)
	private int year = 0;

	// 工作月份
	/* 13個月 */
	@Column(name = "`Month`", insertable = false, updatable = false)
	private int month = 0;

	// 開始日期
	@Column(name = "`StartDate`")
	private int startDate = 0;

	// 終止日期
	@Column(name = "`EndDate`")
	private int endDate = 0;

	// 獎金發放日
	/* 11/24 by 智誠 */
	@Column(name = "`BonusDate`")
	private int bonusDate = 0;

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

	public CdWorkMonthId getCdWorkMonthId() {
		return this.cdWorkMonthId;
	}

	public void setCdWorkMonthId(CdWorkMonthId cdWorkMonthId) {
		this.cdWorkMonthId = cdWorkMonthId;
	}

	/**
	 * 業績年度<br>
	 * 
	 * @return Integer
	 */
	public int getYear() {
		return this.year;
	}

	/**
	 * 業績年度<br>
	 * 
	 *
	 * @param year 業績年度
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * 工作月份<br>
	 * 13個月
	 * 
	 * @return Integer
	 */
	public int getMonth() {
		return this.month;
	}

	/**
	 * 工作月份<br>
	 * 13個月
	 *
	 * @param month 工作月份
	 */
	public void setMonth(int month) {
		this.month = month;
	}

	/**
	 * 開始日期<br>
	 * 
	 * @return Integer
	 */
	public int getStartDate() {
		return StaticTool.bcToRoc(this.startDate);
	}

	/**
	 * 開始日期<br>
	 * 
	 *
	 * @param startDate 開始日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setStartDate(int startDate) throws LogicException {
		this.startDate = StaticTool.rocToBc(startDate);
	}

	/**
	 * 終止日期<br>
	 * 
	 * @return Integer
	 */
	public int getEndDate() {
		return StaticTool.bcToRoc(this.endDate);
	}

	/**
	 * 終止日期<br>
	 * 
	 *
	 * @param endDate 終止日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setEndDate(int endDate) throws LogicException {
		this.endDate = StaticTool.rocToBc(endDate);
	}

	/**
	 * 獎金發放日<br>
	 * 11/24 by 智誠
	 * 
	 * @return Integer
	 */
	public int getBonusDate() {
		return StaticTool.bcToRoc(this.bonusDate);
	}

	/**
	 * 獎金發放日<br>
	 * 11/24 by 智誠
	 *
	 * @param bonusDate 獎金發放日
	 * @throws LogicException when Date Is Warn
	 */
	public void setBonusDate(int bonusDate) throws LogicException {
		this.bonusDate = StaticTool.rocToBc(bonusDate);
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
		return "CdWorkMonth [cdWorkMonthId=" + cdWorkMonthId + ", startDate=" + startDate + ", endDate=" + endDate + ", bonusDate=" + bonusDate + ", createDate=" + createDate + ", createEmpNo="
				+ createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
