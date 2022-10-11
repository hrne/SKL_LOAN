package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * CdCityRate 地區利率檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CdCityRateId implements Serializable {


  // 生效年月
  /* 西元年月 */
  @Column(name = "`EffectYYMM`")
  private int effectYYMM = 0;

  // 縣市代碼(地區別)
  /* 03:基隆市05:台北市10:新北市15:桃園市17:新竹市20:新竹縣25:苗栗縣35:台中市40:彰化縣45:南投縣50:雲林縣54:嘉義市55:嘉義縣65:台南市70:高雄市80:屏東縣95:宜蘭縣90:花蓮縣85:台東縣97:澎湖縣 */
  @Column(name = "`CityCode`", length = 2)
  private String cityCode = " ";

  public CdCityRateId() {
  }

  public CdCityRateId(int effectYYMM, String cityCode) {
    this.effectYYMM = effectYYMM;
    this.cityCode = cityCode;
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


  @Override
  public int hashCode() {
    return Objects.hash(effectYYMM, cityCode);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    CdCityRateId cdCityRateId = (CdCityRateId) obj;
    return effectYYMM == cdCityRateId.effectYYMM && cityCode.equals(cdCityRateId.cityCode);
  }

  @Override
  public String toString() {
    return "CdCityRateId [effectYYMM=" + effectYYMM + ", cityCode=" + cityCode + "]";
  }
}
