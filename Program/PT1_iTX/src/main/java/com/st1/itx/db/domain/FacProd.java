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
 * FacProd 商品參數主檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`FacProd`")
public class FacProd implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7731356101590960957L;

// 商品代碼
	@Id
	@Column(name = "`ProdNo`", length = 5)
	private String prodNo = " ";

	// 商品名稱
	@Column(name = "`ProdName`", length = 60)
	private String prodName;

	// 商品生效日期
	@Column(name = "`StartDate`")
	private int startDate = 0;

	// 商品截止日期
	/* 0:無期限，非必輸欄位 */
	@Column(name = "`EndDate`")
	private int endDate = 0;

	// 商品狀態
	/* 共用代碼檔0:正常 1:停用 */
	@Column(name = "`StatusCode`", length = 1)
	private String statusCode;

	// 是否為協議商品
	/* Y:是 N:否 */
	@Column(name = "`AgreementFg`", length = 1)
	private String agreementFg;

	// 企金可使用記號
	/* Y:是 N:否 */
	@Column(name = "`EnterpriseFg`", length = 1)
	private String enterpriseFg;

	// 幣別
	@Column(name = "`CurrencyCode`", length = 3)
	private String currencyCode;

	// 指標利率代碼
	/* 共用代碼檔(CdCode.BaseRate)01: 保單分紅利率 02: 郵政儲金利率99: 自訂利率 */
	@Column(name = "`BaseRateCode`", length = 2)
	private String baseRateCode;

	// 商品加碼利率
	@Column(name = "`ProdIncr`")
	private BigDecimal prodIncr = new BigDecimal("0");

	// 利率下限
	@Column(name = "`LowLimitRate`")
	private BigDecimal lowLimitRate = new BigDecimal("0");

	// 加減碼是否依合約
	/* Y:是 N:否 */
	@Column(name = "`IncrFlag`", length = 1)
	private String incrFlag;

	// 利率區分
	/* 共用代碼檔1: 機動 2: 固動 3: 定期機動 */
	@Column(name = "`RateCode`", length = 1)
	private String rateCode;

	// 政府優惠房貸
	/* Y:是 N:否 */
	@Column(name = "`GovOfferFlag`", length = 1)
	private String govOfferFlag;

	// 理財型房貸
	/* Y:是 N:否 */
	@Column(name = "`FinancialFlag`", length = 1)
	private String financialFlag;

	// 員工優惠貸款
	/* Y:是 N:否 */
	@Column(name = "`EmpFlag`", length = 1)
	private String empFlag;

	// 是否限制清償
	/* Y:是 N:否 */
	@Column(name = "`BreachFlag`", length = 1)
	private String breachFlag;

	// 違約適用方式
	/* 共用代碼檔001:綁約[按年分段]002:綁約[按月分段]003:依核准額度004:依撥款金額 (原6)005:依提前償還金額 */
	@Column(name = "`BreachCode`", length = 3)
	private String breachCode;

	// 違約金收取方式
	/* 共用代碼檔1:即時收取2:領清償證明時收取 */
	@Column(name = "`BreachGetCode`", length = 1)
	private String breachGetCode;

	// 限制清償期限
	@Column(name = "`ProhibitMonth`")
	private int prohibitMonth = 0;

	// 違約金百分比
	@Column(name = "`BreachPercent`")
	private BigDecimal breachPercent = new BigDecimal("0");

	// 違約金分段月數
	@Column(name = "`BreachDecreaseMonth`")
	private int breachDecreaseMonth = 0;

	// 分段遞減百分比
	@Column(name = "`BreachDecrease`")
	private BigDecimal breachDecrease = new BigDecimal("0");

	// 還款起算比例%
	@Column(name = "`BreachStartPercent`")
	private int breachStartPercent = 0;

	// IFRS階梯商品別
	/* N = 非階梯式A = 固定階梯B = 浮動階梯 */
	@Column(name = "`Ifrs9StepProdCode`", length = 1)
	private String ifrs9StepProdCode;

	// IFRS產品別
	/* 共用代碼檔 */
	@Column(name = "`Ifrs9ProdCode`", length = 2)
	private String ifrs9ProdCode;

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
	 * 商品代碼<br>
	 * 
	 * @return String
	 */
	public String getProdNo() {
		return this.prodNo == null ? "" : this.prodNo;
	}

	/**
	 * 商品代碼<br>
	 * 
	 *
	 * @param prodNo 商品代碼
	 */
	public void setProdNo(String prodNo) {
		this.prodNo = prodNo;
	}

	/**
	 * 商品名稱<br>
	 * 
	 * @return String
	 */
	public String getProdName() {
		return this.prodName == null ? "" : this.prodName;
	}

	/**
	 * 商品名稱<br>
	 * 
	 *
	 * @param prodName 商品名稱
	 */
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	/**
	 * 商品生效日期<br>
	 * 
	 * @return Integer
	 */
	public int getStartDate() {
		return StaticTool.bcToRoc(this.startDate);
	}

	/**
	 * 商品生效日期<br>
	 * 
	 *
	 * @param startDate 商品生效日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setStartDate(int startDate) throws LogicException {
		this.startDate = StaticTool.rocToBc(startDate);
	}

	/**
	 * 商品截止日期<br>
	 * 0:無期限，非必輸欄位
	 * 
	 * @return Integer
	 */
	public int getEndDate() {
		return StaticTool.bcToRoc(this.endDate);
	}

	/**
	 * 商品截止日期<br>
	 * 0:無期限，非必輸欄位
	 *
	 * @param endDate 商品截止日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setEndDate(int endDate) throws LogicException {
		this.endDate = StaticTool.rocToBc(endDate);
	}

	/**
	 * 商品狀態<br>
	 * 共用代碼檔 0:正常 1:停用
	 * 
	 * @return String
	 */
	public String getStatusCode() {
		return this.statusCode == null ? "" : this.statusCode;
	}

	/**
	 * 商品狀態<br>
	 * 共用代碼檔 0:正常 1:停用
	 *
	 * @param statusCode 商品狀態
	 */
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * 是否為協議商品<br>
	 * Y:是 N:否
	 * 
	 * @return String
	 */
	public String getAgreementFg() {
		return this.agreementFg == null ? "" : this.agreementFg;
	}

	/**
	 * 是否為協議商品<br>
	 * Y:是 N:否
	 *
	 * @param agreementFg 是否為協議商品
	 */
	public void setAgreementFg(String agreementFg) {
		this.agreementFg = agreementFg;
	}

	/**
	 * 企金可使用記號<br>
	 * Y:是 N:否
	 * 
	 * @return String
	 */
	public String getEnterpriseFg() {
		return this.enterpriseFg == null ? "" : this.enterpriseFg;
	}

	/**
	 * 企金可使用記號<br>
	 * Y:是 N:否
	 *
	 * @param enterpriseFg 企金可使用記號
	 */
	public void setEnterpriseFg(String enterpriseFg) {
		this.enterpriseFg = enterpriseFg;
	}

	/**
	 * 幣別<br>
	 * 
	 * @return String
	 */
	public String getCurrencyCode() {
		return this.currencyCode == null ? "" : this.currencyCode;
	}

	/**
	 * 幣別<br>
	 * 
	 *
	 * @param currencyCode 幣別
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	/**
	 * 指標利率代碼<br>
	 * 共用代碼檔(CdCode.BaseRate) 01: 保單分紅利率 02: 郵政儲金利率 99: 自訂利率
	 * 
	 * @return String
	 */
	public String getBaseRateCode() {
		return this.baseRateCode == null ? "" : this.baseRateCode;
	}

	/**
	 * 指標利率代碼<br>
	 * 共用代碼檔(CdCode.BaseRate) 01: 保單分紅利率 02: 郵政儲金利率 99: 自訂利率
	 *
	 * @param baseRateCode 指標利率代碼
	 */
	public void setBaseRateCode(String baseRateCode) {
		this.baseRateCode = baseRateCode;
	}

	/**
	 * 商品加碼利率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getProdIncr() {
		return this.prodIncr;
	}

	/**
	 * 商品加碼利率<br>
	 * 
	 *
	 * @param prodIncr 商品加碼利率
	 */
	public void setProdIncr(BigDecimal prodIncr) {
		this.prodIncr = prodIncr;
	}

	/**
	 * 利率下限<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getLowLimitRate() {
		return this.lowLimitRate;
	}

	/**
	 * 利率下限<br>
	 * 
	 *
	 * @param lowLimitRate 利率下限
	 */
	public void setLowLimitRate(BigDecimal lowLimitRate) {
		this.lowLimitRate = lowLimitRate;
	}

	/**
	 * 加減碼是否依合約<br>
	 * Y:是 N:否
	 * 
	 * @return String
	 */
	public String getIncrFlag() {
		return this.incrFlag == null ? "" : this.incrFlag;
	}

	/**
	 * 加減碼是否依合約<br>
	 * Y:是 N:否
	 *
	 * @param incrFlag 加減碼是否依合約
	 */
	public void setIncrFlag(String incrFlag) {
		this.incrFlag = incrFlag;
	}

	/**
	 * 利率區分<br>
	 * 共用代碼檔 1: 機動 2: 固動 3: 定期機動
	 * 
	 * @return String
	 */
	public String getRateCode() {
		return this.rateCode == null ? "" : this.rateCode;
	}

	/**
	 * 利率區分<br>
	 * 共用代碼檔 1: 機動 2: 固動 3: 定期機動
	 *
	 * @param rateCode 利率區分
	 */
	public void setRateCode(String rateCode) {
		this.rateCode = rateCode;
	}

	/**
	 * 政府優惠房貸<br>
	 * Y:是 N:否
	 * 
	 * @return String
	 */
	public String getGovOfferFlag() {
		return this.govOfferFlag == null ? "" : this.govOfferFlag;
	}

	/**
	 * 政府優惠房貸<br>
	 * Y:是 N:否
	 *
	 * @param govOfferFlag 政府優惠房貸
	 */
	public void setGovOfferFlag(String govOfferFlag) {
		this.govOfferFlag = govOfferFlag;
	}

	/**
	 * 理財型房貸<br>
	 * Y:是 N:否
	 * 
	 * @return String
	 */
	public String getFinancialFlag() {
		return this.financialFlag == null ? "" : this.financialFlag;
	}

	/**
	 * 理財型房貸<br>
	 * Y:是 N:否
	 *
	 * @param financialFlag 理財型房貸
	 */
	public void setFinancialFlag(String financialFlag) {
		this.financialFlag = financialFlag;
	}

	/**
	 * 員工優惠貸款<br>
	 * Y:是 N:否
	 * 
	 * @return String
	 */
	public String getEmpFlag() {
		return this.empFlag == null ? "" : this.empFlag;
	}

	/**
	 * 員工優惠貸款<br>
	 * Y:是 N:否
	 *
	 * @param empFlag 員工優惠貸款
	 */
	public void setEmpFlag(String empFlag) {
		this.empFlag = empFlag;
	}

	/**
	 * 是否限制清償<br>
	 * Y:是 N:否
	 * 
	 * @return String
	 */
	public String getBreachFlag() {
		return this.breachFlag == null ? "" : this.breachFlag;
	}

	/**
	 * 是否限制清償<br>
	 * Y:是 N:否
	 *
	 * @param breachFlag 是否限制清償
	 */
	public void setBreachFlag(String breachFlag) {
		this.breachFlag = breachFlag;
	}

	/**
	 * 違約適用方式<br>
	 * 共用代碼檔 001:綁約[按年分段] 002:綁約[按月分段] 003:依核准額度 004:依撥款金額 (原6) 005:依提前償還金額
	 * 
	 * @return String
	 */
	public String getBreachCode() {
		return this.breachCode == null ? "" : this.breachCode;
	}

	/**
	 * 違約適用方式<br>
	 * 共用代碼檔 001:綁約[按年分段] 002:綁約[按月分段] 003:依核准額度 004:依撥款金額 (原6) 005:依提前償還金額
	 *
	 * @param breachCode 違約適用方式
	 */
	public void setBreachCode(String breachCode) {
		this.breachCode = breachCode;
	}

	/**
	 * 違約金收取方式<br>
	 * 共用代碼檔 1:即時收取 2:領清償證明時收取
	 * 
	 * @return String
	 */
	public String getBreachGetCode() {
		return this.breachGetCode == null ? "" : this.breachGetCode;
	}

	/**
	 * 違約金收取方式<br>
	 * 共用代碼檔 1:即時收取 2:領清償證明時收取
	 *
	 * @param breachGetCode 違約金收取方式
	 */
	public void setBreachGetCode(String breachGetCode) {
		this.breachGetCode = breachGetCode;
	}

	/**
	 * 限制清償期限<br>
	 * 
	 * @return Integer
	 */
	public int getProhibitMonth() {
		return this.prohibitMonth;
	}

	/**
	 * 限制清償期限<br>
	 * 
	 *
	 * @param prohibitMonth 限制清償期限
	 */
	public void setProhibitMonth(int prohibitMonth) {
		this.prohibitMonth = prohibitMonth;
	}

	/**
	 * 違約金百分比<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getBreachPercent() {
		return this.breachPercent;
	}

	/**
	 * 違約金百分比<br>
	 * 
	 *
	 * @param breachPercent 違約金百分比
	 */
	public void setBreachPercent(BigDecimal breachPercent) {
		this.breachPercent = breachPercent;
	}

	/**
	 * 違約金分段月數<br>
	 * 
	 * @return Integer
	 */
	public int getBreachDecreaseMonth() {
		return this.breachDecreaseMonth;
	}

	/**
	 * 違約金分段月數<br>
	 * 
	 *
	 * @param breachDecreaseMonth 違約金分段月數
	 */
	public void setBreachDecreaseMonth(int breachDecreaseMonth) {
		this.breachDecreaseMonth = breachDecreaseMonth;
	}

	/**
	 * 分段遞減百分比<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getBreachDecrease() {
		return this.breachDecrease;
	}

	/**
	 * 分段遞減百分比<br>
	 * 
	 *
	 * @param breachDecrease 分段遞減百分比
	 */
	public void setBreachDecrease(BigDecimal breachDecrease) {
		this.breachDecrease = breachDecrease;
	}

	/**
	 * 還款起算比例%<br>
	 * 
	 * @return Integer
	 */
	public int getBreachStartPercent() {
		return this.breachStartPercent;
	}

	/**
	 * 還款起算比例%<br>
	 * 
	 *
	 * @param breachStartPercent 還款起算比例%
	 */
	public void setBreachStartPercent(int breachStartPercent) {
		this.breachStartPercent = breachStartPercent;
	}

	/**
	 * IFRS階梯商品別<br>
	 * N = 非階梯式 A = 固定階梯 B = 浮動階梯
	 * 
	 * @return String
	 */
	public String getIfrs9StepProdCode() {
		return this.ifrs9StepProdCode == null ? "" : this.ifrs9StepProdCode;
	}

	/**
	 * IFRS階梯商品別<br>
	 * N = 非階梯式 A = 固定階梯 B = 浮動階梯
	 *
	 * @param ifrs9StepProdCode IFRS階梯商品別
	 */
	public void setIfrs9StepProdCode(String ifrs9StepProdCode) {
		this.ifrs9StepProdCode = ifrs9StepProdCode;
	}

	/**
	 * IFRS產品別<br>
	 * 共用代碼檔
	 * 
	 * @return String
	 */
	public String getIfrs9ProdCode() {
		return this.ifrs9ProdCode == null ? "" : this.ifrs9ProdCode;
	}

	/**
	 * IFRS產品別<br>
	 * 共用代碼檔
	 *
	 * @param ifrs9ProdCode IFRS產品別
	 */
	public void setIfrs9ProdCode(String ifrs9ProdCode) {
		this.ifrs9ProdCode = ifrs9ProdCode;
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
		return "FacProd [prodNo=" + prodNo + ", prodName=" + prodName + ", startDate=" + startDate + ", endDate=" + endDate + ", statusCode=" + statusCode + ", agreementFg=" + agreementFg
				+ ", enterpriseFg=" + enterpriseFg + ", currencyCode=" + currencyCode + ", baseRateCode=" + baseRateCode + ", prodIncr=" + prodIncr + ", lowLimitRate=" + lowLimitRate + ", incrFlag="
				+ incrFlag + ", rateCode=" + rateCode + ", govOfferFlag=" + govOfferFlag + ", financialFlag=" + financialFlag + ", empFlag=" + empFlag + ", breachFlag=" + breachFlag + ", breachCode="
				+ breachCode + ", breachGetCode=" + breachGetCode + ", prohibitMonth=" + prohibitMonth + ", breachPercent=" + breachPercent + ", breachDecreaseMonth=" + breachDecreaseMonth
				+ ", breachDecrease=" + breachDecrease + ", breachStartPercent=" + breachStartPercent + ", ifrs9StepProdCode=" + ifrs9StepProdCode + ", ifrs9ProdCode=" + ifrs9ProdCode
				+ ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
