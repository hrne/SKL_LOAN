package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * CdBaseRate 指標利率檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CdBaseRateId implements Serializable {


  // 幣別
  /* 共用代碼檔TWD:新台幣 */
  @Column(name = "`CurrencyCode`", length = 3)
  private String currencyCode = " ";

  // 利率代碼
  /* 共用代碼檔從 L6301 維護代碼01:保單分紅利率02:郵政儲金利率03:台北金融業拆款定盤利率 */
  @Column(name = "`BaseRateCode`", length = 2)
  private String baseRateCode = " ";

  // 生效日期
  @Column(name = "`EffectDate`")
  private int effectDate = 0;

  public CdBaseRateId() {
  }

  public CdBaseRateId(String currencyCode, String baseRateCode, int effectDate) {
    this.currencyCode = currencyCode;
    this.baseRateCode = baseRateCode;
    this.effectDate = effectDate;
  }

/**
	* 幣別<br>
	* 共用代碼檔
TWD:新台幣
	* @return String
	*/
  public String getCurrencyCode() {
    return this.currencyCode == null ? "" : this.currencyCode;
  }

/**
	* 幣別<br>
	* 共用代碼檔
TWD:新台幣
  *
  * @param currencyCode 幣別
	*/
  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

/**
	* 利率代碼<br>
	* 共用代碼檔從 L6301 維護代碼
01:保單分紅利率
02:郵政儲金利率
03:台北金融業拆款定盤利率
	* @return String
	*/
  public String getBaseRateCode() {
    return this.baseRateCode == null ? "" : this.baseRateCode;
  }

/**
	* 利率代碼<br>
	* 共用代碼檔從 L6301 維護代碼
01:保單分紅利率
02:郵政儲金利率
03:台北金融業拆款定盤利率
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
    return  StaticTool.bcToRoc(this.effectDate);
  }

/**
	* 生效日期<br>
	* 
  *
  * @param effectDate 生效日期
  * @throws LogicException when Date Is Warn	*/
  public void setEffectDate(int effectDate) throws LogicException {
    this.effectDate = StaticTool.rocToBc(effectDate);
  }


  @Override
  public int hashCode() {
    return Objects.hash(currencyCode, baseRateCode, effectDate);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    CdBaseRateId cdBaseRateId = (CdBaseRateId) obj;
    return currencyCode.equals(cdBaseRateId.currencyCode) && baseRateCode.equals(cdBaseRateId.baseRateCode) && effectDate == cdBaseRateId.effectDate;
  }

  @Override
  public String toString() {
    return "CdBaseRateId [currencyCode=" + currencyCode + ", baseRateCode=" + baseRateCode + ", effectDate=" + effectDate + "]";
  }
}
