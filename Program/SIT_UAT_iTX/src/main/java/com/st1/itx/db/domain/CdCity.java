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
 * CdCity 地區別代碼檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdCity`")
public class CdCity implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 8929313936006139450L;

// 縣市代碼(地區別)
  /* 3:基隆市5:台北市10:新北市15:桃園市17:新竹市20:新竹縣25:苗栗縣35:台中市40:彰化縣45:南投縣50:雲林縣54:嘉義市55:嘉義縣65:台南市70:高雄市80:屏東縣95:宜蘭縣90:花蓮縣85:台東縣97:澎湖縣 */
  @Id
  @Column(name = "`CityCode`", length = 2)
  private String cityCode = " ";

  // 縣市名稱(地區別)
  @Column(name = "`CityItem`", length = 10)
  private String cityItem;

  // 單位代號
  @Column(name = "`UnitCode`", length = 6)
  private String unitCode;

  // 催收人員
  @Column(name = "`AccCollPsn`", length = 6)
  private String accCollPsn;

  // 法務人員
  @Column(name = "`LegalPsn`", length = 6)
  private String legalPsn;

  // 利率加減碼
  @Column(name = "`IntRateIncr`")
  private BigDecimal intRateIncr = new BigDecimal("0");

  // 利率上限
  @Column(name = "`IntRateCeiling`")
  private BigDecimal intRateCeiling = new BigDecimal("0");

  // 利率下限
  @Column(name = "`IntRateFloor`")
  private BigDecimal intRateFloor = new BigDecimal("0");

  // 聯徵用縣市代碼
  /* C:基隆市A:台北市F:新北市H:桃園市O:新竹市J:新竹縣K:苗栗縣B:台中市N:彰化縣M:南投縣P:雲林縣I:嘉義市Q:嘉義縣D:台南市E:高雄市T:屏東縣V:台東縣U:花蓮縣G:宜蘭縣W:金門縣X:澎湖縣Z:連江縣 */
  @Column(name = "`JcicCityCode`", length = 0)
  private String jcicCityCode;

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
	* 縣市代碼(地區別)<br>
	* 3:基隆市
5:台北市
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
	* 3:基隆市
5:台北市
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
	* 縣市名稱(地區別)<br>
	* 
	* @return String
	*/
  public String getCityItem() {
    return this.cityItem == null ? "" : this.cityItem;
  }

/**
	* 縣市名稱(地區別)<br>
	* 
  *
  * @param cityItem 縣市名稱(地區別)
	*/
  public void setCityItem(String cityItem) {
    this.cityItem = cityItem;
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
	* 催收人員<br>
	* 
	* @return String
	*/
  public String getAccCollPsn() {
    return this.accCollPsn == null ? "" : this.accCollPsn;
  }

/**
	* 催收人員<br>
	* 
  *
  * @param accCollPsn 催收人員
	*/
  public void setAccCollPsn(String accCollPsn) {
    this.accCollPsn = accCollPsn;
  }

/**
	* 法務人員<br>
	* 
	* @return String
	*/
  public String getLegalPsn() {
    return this.legalPsn == null ? "" : this.legalPsn;
  }

/**
	* 法務人員<br>
	* 
  *
  * @param legalPsn 法務人員
	*/
  public void setLegalPsn(String legalPsn) {
    this.legalPsn = legalPsn;
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
	* 聯徵用縣市代碼<br>
	* C:基隆市
A:台北市
F:新北市
H:桃園市
O:新竹市
J:新竹縣
K:苗栗縣
B:台中市
N:彰化縣
M:南投縣
P:雲林縣
I:嘉義市
Q:嘉義縣
D:台南市
E:高雄市
T:屏東縣
V:台東縣
U:花蓮縣
G:宜蘭縣
W:金門縣
X:澎湖縣
Z:連江縣
	* @return String
	*/
  public String getJcicCityCode() {
    return this.jcicCityCode == null ? "" : this.jcicCityCode;
  }

/**
	* 聯徵用縣市代碼<br>
	* C:基隆市
A:台北市
F:新北市
H:桃園市
O:新竹市
J:新竹縣
K:苗栗縣
B:台中市
N:彰化縣
M:南投縣
P:雲林縣
I:嘉義市
Q:嘉義縣
D:台南市
E:高雄市
T:屏東縣
V:台東縣
U:花蓮縣
G:宜蘭縣
W:金門縣
X:澎湖縣
Z:連江縣
  *
  * @param jcicCityCode 聯徵用縣市代碼
	*/
  public void setJcicCityCode(String jcicCityCode) {
    this.jcicCityCode = jcicCityCode;
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
    return "CdCity [cityCode=" + cityCode + ", cityItem=" + cityItem + ", unitCode=" + unitCode + ", accCollPsn=" + accCollPsn + ", legalPsn=" + legalPsn + ", intRateIncr=" + intRateIncr
           + ", intRateCeiling=" + intRateCeiling + ", intRateFloor=" + intRateFloor + ", jcicCityCode=" + jcicCityCode + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
