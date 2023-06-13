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
 * CdCityRate 地區利率檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdCityRate`")
public class CdCityRate implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -4335954756561010150L;

@EmbeddedId
  private CdCityRateId cdCityRateId;

  // 生效年月
  /* 西元年月 */
  @Column(name = "`EffectYYMM`", insertable = false, updatable = false)
  private int effectYYMM = 0;

  // 縣市代碼(地區別)
  /* 03:基隆市05:台北市10:新北市15:桃園市17:新竹市20:新竹縣25:苗栗縣35:台中市40:彰化縣45:南投縣50:雲林縣54:嘉義市55:嘉義縣65:台南市70:高雄市80:屏東縣95:宜蘭縣90:花蓮縣85:台東縣97:澎湖縣 */
  @Column(name = "`CityCode`", length = 2, insertable = false, updatable = false)
  private String cityCode;

  // 利率加減碼
  @Column(name = "`IntRateIncr`")
  private BigDecimal intRateIncr = new BigDecimal("0");

  // 利率上限
  @Column(name = "`IntRateCeiling`")
  private BigDecimal intRateCeiling = new BigDecimal("0");

  // 利率下限
  @Column(name = "`IntRateFloor`")
  private BigDecimal intRateFloor = new BigDecimal("0");

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


  public CdCityRateId getCdCityRateId() {
    return this.cdCityRateId;
  }

  public void setCdCityRateId(CdCityRateId cdCityRateId) {
    this.cdCityRateId = cdCityRateId;
  }

/**
	* 生效年月<br>
	* 西元年月
	* @return Integer
	*/
  public int getEffectYYMM() {
    return this.effectYYMM;
  }

/**
	* 生效年月<br>
	* 西元年月
  *
  * @param effectYYMM 生效年月
	*/
  public void setEffectYYMM(int effectYYMM) {
    this.effectYYMM = effectYYMM;
  }

/**
	* 縣市代碼(地區別)<br>
	* 03:基隆市
05:台北市
10:新北市
15:桃園市
17:新竹市
20:新竹縣
25:苗栗縣
35:台中市
40:彰化縣
45:南投縣
50:雲林縣
54:嘉義市
55:嘉義縣
65:台南市
70:高雄市
80:屏東縣
95:宜蘭縣
90:花蓮縣
85:台東縣
97:澎湖縣
	* @return String
	*/
  public String getCityCode() {
    return this.cityCode == null ? "" : this.cityCode;
  }

/**
	* 縣市代碼(地區別)<br>
	* 03:基隆市
05:台北市
10:新北市
15:桃園市
17:新竹市
20:新竹縣
25:苗栗縣
35:台中市
40:彰化縣
45:南投縣
50:雲林縣
54:嘉義市
55:嘉義縣
65:台南市
70:高雄市
80:屏東縣
95:宜蘭縣
90:花蓮縣
85:台東縣
97:澎湖縣
  *
  * @param cityCode 縣市代碼(地區別)
	*/
  public void setCityCode(String cityCode) {
    this.cityCode = cityCode;
  }

/**
	* 利率加減碼<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getIntRateIncr() {
    return this.intRateIncr;
  }

/**
	* 利率加減碼<br>
	* 
  *
  * @param intRateIncr 利率加減碼
	*/
  public void setIntRateIncr(BigDecimal intRateIncr) {
    this.intRateIncr = intRateIncr;
  }

/**
	* 利率上限<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getIntRateCeiling() {
    return this.intRateCeiling;
  }

/**
	* 利率上限<br>
	* 
  *
  * @param intRateCeiling 利率上限
	*/
  public void setIntRateCeiling(BigDecimal intRateCeiling) {
    this.intRateCeiling = intRateCeiling;
  }

/**
	* 利率下限<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getIntRateFloor() {
    return this.intRateFloor;
  }

/**
	* 利率下限<br>
	* 
  *
  * @param intRateFloor 利率下限
	*/
  public void setIntRateFloor(BigDecimal intRateFloor) {
    this.intRateFloor = intRateFloor;
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
    return "CdCityRate [cdCityRateId=" + cdCityRateId + ", intRateIncr=" + intRateIncr + ", intRateCeiling=" + intRateCeiling + ", intRateFloor=" + intRateFloor + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
