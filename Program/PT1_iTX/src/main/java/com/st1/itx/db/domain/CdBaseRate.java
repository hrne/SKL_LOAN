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
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * CdBaseRate 指標利率檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdBaseRate`")
public class CdBaseRate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CdBaseRateId cdBaseRateId;

	// 幣別
	/* 共用代碼檔TWD:新台幣 */
	@Column(name = "`CurrencyCode`", length = 3, insertable = false, updatable = false)
	private String currencyCode;

	// 利率代碼
	/* 共用代碼檔從 L6301 維護代碼01:保單分紅利率02:郵政儲金利率03:台北金融業拆款定盤利率 */
	@Column(name = "`BaseRateCode`", length = 2, insertable = false, updatable = false)
	private String baseRateCode;

	// 生效日期
	@Column(name = "`EffectDate`", insertable = false, updatable = false)
	private int effectDate = 0;

	// 利率
	@Column(name = "`BaseRate`")
	private BigDecimal baseRate = new BigDecimal("0");

	// 備註
	@Column(name = "`Remark`", length = 40)
	private String remark;

	// 生效記號
	/* 0:已放行(生效日期&amp;gt;日曆日:未生效 else 已生效)1:已使用(不可刪除)2:未放行 */
	@Column(name = "`EffectFlag`")
	private int effectFlag = 0;

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

	public CdBaseRateId getCdBaseRateId() {
		return this.cdBaseRateId;
	}

	public void setCdBaseRateId(CdBaseRateId cdBaseRateId) {
		this.cdBaseRateId = cdBaseRateId;
	}

	/**
	 * 幣別<br>
	 * 共用代碼檔 TWD:新台幣
	 * 
	 * @return String
	 */
	public String getCurrencyCode() {
		return this.currencyCode == null ? "" : this.currencyCode;
	}

	/**
	 * 幣別<br>
	 * 共用代碼檔 TWD:新台幣
	 *
	 * @param currencyCode 幣別
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	/**
	 * 利率代碼<br>
	 * 共用代碼檔從 L6301 維護代碼 01:保單分紅利率 02:郵政儲金利率 03:台北金融業拆款定盤利率
	 * 
	 * @return String
	 */
	public String getBaseRateCode() {
		return this.baseRateCode == null ? "" : this.baseRateCode;
	}

	/**
	 * 利率代碼<br>
	 * 共用代碼檔從 L6301 維護代碼 01:保單分紅利率 02:郵政儲金利率 03:台北金融業拆款定盤利率
	 *
	 * @param baseRateCode 利率代碼
	 */
	public void setBaseRateCode(String baseRateCode) {
		this.baseRateCode = baseRateCode;
	}

	/**
	 * 生效日期<br>
	 * 
	 * @return Integer
	 */
	public int getEffectDate() {
		return StaticTool.bcToRoc(this.effectDate);
	}

	/**
	 * 生效日期<br>
	 * 
	 *
	 * @param effectDate 生效日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setEffectDate(int effectDate) throws LogicException {
		this.effectDate = StaticTool.rocToBc(effectDate);
	}

	/**
	 * 利率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getBaseRate() {
		return this.baseRate;
	}

	/**
	 * 利率<br>
	 * 
	 *
	 * @param baseRate 利率
	 */
	public void setBaseRate(BigDecimal baseRate) {
		this.baseRate = baseRate;
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
	 * 生效記號<br>
	 * 0:已放行(生效日期&amp;gt;日曆日:未生效 else 已生效) 1:已使用(不可刪除) 2:未放行
	 * 
	 * @return Integer
	 */
	public int getEffectFlag() {
		return this.effectFlag;
	}

	/**
	 * 生效記號<br>
	 * 0:已放行(生效日期&amp;gt;日曆日:未生效 else 已生效) 1:已使用(不可刪除) 2:未放行
	 *
	 * @param effectFlag 生效記號
	 */
	public void setEffectFlag(int effectFlag) {
		this.effectFlag = effectFlag;
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
		return "CdBaseRate [cdBaseRateId=" + cdBaseRateId + ", baseRate=" + baseRate + ", remark=" + remark + ", effectFlag=" + effectFlag + ", createDate=" + createDate + ", createEmpNo="
				+ createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
