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
 * ClOtherRights 擔保品他項權利檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`ClOtherRights`")
public class ClOtherRights implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 1726836396543591466L;

@EmbeddedId
  private ClOtherRightsId clOtherRightsId;

  // 擔保品代號1
  /* 擔保品代號檔CdCl */
  @Column(name = "`ClCode1`", insertable = false, updatable = false)
  private int clCode1 = 0;

  // 擔保品代號2
  /* 擔保品代號檔CdCl */
  @Column(name = "`ClCode2`", insertable = false, updatable = false)
  private int clCode2 = 0;

  // 擔保品編號
  @Column(name = "`ClNo`", insertable = false, updatable = false)
  private int clNo = 0;

  // 他項權利登記次序
  /* ex：0002-000資料轉換:固定為9999-nnn同一擔保品由001開始續編 */
  @Column(name = "`Seq`", length = 8, insertable = false, updatable = false)
  private String seq;

  // 縣市
  /* 資料轉換/ELOAN上送:無 */
  @Column(name = "`City`", length = 4)
  private String city;

  // 其他縣市
  /* 自行輸入資料轉換:無 */
  @Column(name = "`OtherCity`", length = 40)
  private String otherCity;

  // 地政
  /* CdCode.LandOfficeCode資料轉換:無 */
  @Column(name = "`LandAdm`", length = 2)
  private String landAdm;

  // 其他地政
  /* 自行輸入資料轉換:無 */
  @Column(name = "`OtherLandAdm`", length = 40)
  private String otherLandAdm;

  // 收件年
  /* 資料轉換:無 */
  @Column(name = "`RecYear`")
  private int recYear = 0;

  // 收件字
  /* 資料轉換:無 */
  @Column(name = "`RecWord`", length = 3)
  private String recWord;

  // 其他收件字
  /* 自行輸入資料轉換:無 */
  @Column(name = "`OtherRecWord`", length = 40)
  private String otherRecWord;

  // 收件號
  /* 資料轉換:無 */
  @Column(name = "`RecNumber`", length = 6)
  private String recNumber;

  // 權利價值說明
  /* 資料轉換/ELOAN上送:無印錄清償證明時維護CdCode.ClRightsNote01 本金02 債權本金03 共同擔保本金04 共同擔保債權本金 */
  @Column(name = "`RightsNote`", length = 2)
  private String rightsNote;

  // 擔保債權總金額
  /* 資料轉換:擔保品設定金額 */
  @Column(name = "`SecuredTotal`")
  private BigDecimal securedTotal = new BigDecimal("0");

  // 領取記號
  /* 0:未領取1:已領取 */
  @Column(name = "`ReceiveFg`")
  private int receiveFg = 0;

  // 篩選資料日期
  /* 印錄清償證明篩選資料時upd */
  @Column(name = "`ChoiceDate`")
  private int choiceDate = 0;

  // 篩選戶號
  /* 印錄清償證明篩選資料時upd */
  @Column(name = "`ReceiveCustNo`")
  private int receiveCustNo = 0;

  // 清償序號
  /* 印錄清償證明篩選資料時upd */
  @Column(name = "`CloseNo`")
  private int closeNo = 0;

  // 擔保債權確定日期
  /* 資料轉換:擔保債權確定日期 */
  @Column(name = "`SecuredDate`")
  private int securedDate = 0;

  // 建物坐落地號
  /* 資料轉換:無 */
  @Column(name = "`Location`", length = 40)
  private String location;

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


  public ClOtherRightsId getClOtherRightsId() {
    return this.clOtherRightsId;
  }

  public void setClOtherRightsId(ClOtherRightsId clOtherRightsId) {
    this.clOtherRightsId = clOtherRightsId;
  }

/**
	* 擔保品代號1<br>
	* 擔保品代號檔CdCl
	* @return Integer
	*/
  public int getClCode1() {
    return this.clCode1;
  }

/**
	* 擔保品代號1<br>
	* 擔保品代號檔CdCl
  *
  * @param clCode1 擔保品代號1
	*/
  public void setClCode1(int clCode1) {
    this.clCode1 = clCode1;
  }

/**
	* 擔保品代號2<br>
	* 擔保品代號檔CdCl
	* @return Integer
	*/
  public int getClCode2() {
    return this.clCode2;
  }

/**
	* 擔保品代號2<br>
	* 擔保品代號檔CdCl
  *
  * @param clCode2 擔保品代號2
	*/
  public void setClCode2(int clCode2) {
    this.clCode2 = clCode2;
  }

/**
	* 擔保品編號<br>
	* 
	* @return Integer
	*/
  public int getClNo() {
    return this.clNo;
  }

/**
	* 擔保品編號<br>
	* 
  *
  * @param clNo 擔保品編號
	*/
  public void setClNo(int clNo) {
    this.clNo = clNo;
  }

/**
	* 他項權利登記次序<br>
	* ex：0002-000
資料轉換:固定為9999-nnn同一擔保品由001開始續編
	* @return String
	*/
  public String getSeq() {
    return this.seq == null ? "" : this.seq;
  }

/**
	* 他項權利登記次序<br>
	* ex：0002-000
資料轉換:固定為9999-nnn同一擔保品由001開始續編
  *
  * @param seq 他項權利登記次序
	*/
  public void setSeq(String seq) {
    this.seq = seq;
  }

/**
	* 縣市<br>
	* 資料轉換/ELOAN上送:無
	* @return String
	*/
  public String getCity() {
    return this.city == null ? "" : this.city;
  }

/**
	* 縣市<br>
	* 資料轉換/ELOAN上送:無
  *
  * @param city 縣市
	*/
  public void setCity(String city) {
    this.city = city;
  }

/**
	* 其他縣市<br>
	* 自行輸入
資料轉換:無
	* @return String
	*/
  public String getOtherCity() {
    return this.otherCity == null ? "" : this.otherCity;
  }

/**
	* 其他縣市<br>
	* 自行輸入
資料轉換:無
  *
  * @param otherCity 其他縣市
	*/
  public void setOtherCity(String otherCity) {
    this.otherCity = otherCity;
  }

/**
	* 地政<br>
	* CdCode.LandOfficeCode
資料轉換:無
	* @return String
	*/
  public String getLandAdm() {
    return this.landAdm == null ? "" : this.landAdm;
  }

/**
	* 地政<br>
	* CdCode.LandOfficeCode
資料轉換:無
  *
  * @param landAdm 地政
	*/
  public void setLandAdm(String landAdm) {
    this.landAdm = landAdm;
  }

/**
	* 其他地政<br>
	* 自行輸入
資料轉換:無
	* @return String
	*/
  public String getOtherLandAdm() {
    return this.otherLandAdm == null ? "" : this.otherLandAdm;
  }

/**
	* 其他地政<br>
	* 自行輸入
資料轉換:無
  *
  * @param otherLandAdm 其他地政
	*/
  public void setOtherLandAdm(String otherLandAdm) {
    this.otherLandAdm = otherLandAdm;
  }

/**
	* 收件年<br>
	* 資料轉換:無
	* @return Integer
	*/
  public int getRecYear() {
    return this.recYear;
  }

/**
	* 收件年<br>
	* 資料轉換:無
  *
  * @param recYear 收件年
	*/
  public void setRecYear(int recYear) {
    this.recYear = recYear;
  }

/**
	* 收件字<br>
	* 資料轉換:無
	* @return String
	*/
  public String getRecWord() {
    return this.recWord == null ? "" : this.recWord;
  }

/**
	* 收件字<br>
	* 資料轉換:無
  *
  * @param recWord 收件字
	*/
  public void setRecWord(String recWord) {
    this.recWord = recWord;
  }

/**
	* 其他收件字<br>
	* 自行輸入
資料轉換:無
	* @return String
	*/
  public String getOtherRecWord() {
    return this.otherRecWord == null ? "" : this.otherRecWord;
  }

/**
	* 其他收件字<br>
	* 自行輸入
資料轉換:無
  *
  * @param otherRecWord 其他收件字
	*/
  public void setOtherRecWord(String otherRecWord) {
    this.otherRecWord = otherRecWord;
  }

/**
	* 收件號<br>
	* 資料轉換:無
	* @return String
	*/
  public String getRecNumber() {
    return this.recNumber == null ? "" : this.recNumber;
  }

/**
	* 收件號<br>
	* 資料轉換:無
  *
  * @param recNumber 收件號
	*/
  public void setRecNumber(String recNumber) {
    this.recNumber = recNumber;
  }

/**
	* 權利價值說明<br>
	* 資料轉換/ELOAN上送:無
印錄清償證明時維護
CdCode.ClRightsNote
01 本金
02 債權本金
03 共同擔保本金
04 共同擔保債權本金
	* @return String
	*/
  public String getRightsNote() {
    return this.rightsNote == null ? "" : this.rightsNote;
  }

/**
	* 權利價值說明<br>
	* 資料轉換/ELOAN上送:無
印錄清償證明時維護
CdCode.ClRightsNote
01 本金
02 債權本金
03 共同擔保本金
04 共同擔保債權本金
  *
  * @param rightsNote 權利價值說明
	*/
  public void setRightsNote(String rightsNote) {
    this.rightsNote = rightsNote;
  }

/**
	* 擔保債權總金額<br>
	* 資料轉換:擔保品設定金額
	* @return BigDecimal
	*/
  public BigDecimal getSecuredTotal() {
    return this.securedTotal;
  }

/**
	* 擔保債權總金額<br>
	* 資料轉換:擔保品設定金額
  *
  * @param securedTotal 擔保債權總金額
	*/
  public void setSecuredTotal(BigDecimal securedTotal) {
    this.securedTotal = securedTotal;
  }

/**
	* 領取記號<br>
	* 0:未領取
1:已領取
	* @return Integer
	*/
  public int getReceiveFg() {
    return this.receiveFg;
  }

/**
	* 領取記號<br>
	* 0:未領取
1:已領取
  *
  * @param receiveFg 領取記號
	*/
  public void setReceiveFg(int receiveFg) {
    this.receiveFg = receiveFg;
  }

/**
	* 篩選資料日期<br>
	* 印錄清償證明篩選資料時upd
	* @return Integer
	*/
  public int getChoiceDate() {
    return StaticTool.bcToRoc(this.choiceDate);
  }

/**
	* 篩選資料日期<br>
	* 印錄清償證明篩選資料時upd
  *
  * @param choiceDate 篩選資料日期
  * @throws LogicException when Date Is Warn	*/
  public void setChoiceDate(int choiceDate) throws LogicException {
    this.choiceDate = StaticTool.rocToBc(choiceDate);
  }

/**
	* 篩選戶號<br>
	* 印錄清償證明篩選資料時upd
	* @return Integer
	*/
  public int getReceiveCustNo() {
    return this.receiveCustNo;
  }

/**
	* 篩選戶號<br>
	* 印錄清償證明篩選資料時upd
  *
  * @param receiveCustNo 篩選戶號
	*/
  public void setReceiveCustNo(int receiveCustNo) {
    this.receiveCustNo = receiveCustNo;
  }

/**
	* 清償序號<br>
	* 印錄清償證明篩選資料時upd
	* @return Integer
	*/
  public int getCloseNo() {
    return this.closeNo;
  }

/**
	* 清償序號<br>
	* 印錄清償證明篩選資料時upd
  *
  * @param closeNo 清償序號
	*/
  public void setCloseNo(int closeNo) {
    this.closeNo = closeNo;
  }

/**
	* 擔保債權確定日期<br>
	* 資料轉換:擔保債權確定日期
	* @return Integer
	*/
  public int getSecuredDate() {
    return StaticTool.bcToRoc(this.securedDate);
  }

/**
	* 擔保債權確定日期<br>
	* 資料轉換:擔保債權確定日期
  *
  * @param securedDate 擔保債權確定日期
  * @throws LogicException when Date Is Warn	*/
  public void setSecuredDate(int securedDate) throws LogicException {
    this.securedDate = StaticTool.rocToBc(securedDate);
  }

/**
	* 建物坐落地號<br>
	* 資料轉換:無
	* @return String
	*/
  public String getLocation() {
    return this.location == null ? "" : this.location;
  }

/**
	* 建物坐落地號<br>
	* 資料轉換:無
  *
  * @param location 建物坐落地號
	*/
  public void setLocation(String location) {
    this.location = location;
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
    return "ClOtherRights [clOtherRightsId=" + clOtherRightsId + ", city=" + city + ", otherCity=" + otherCity
           + ", landAdm=" + landAdm + ", otherLandAdm=" + otherLandAdm + ", recYear=" + recYear + ", recWord=" + recWord + ", otherRecWord=" + otherRecWord + ", recNumber=" + recNumber
           + ", rightsNote=" + rightsNote + ", securedTotal=" + securedTotal + ", receiveFg=" + receiveFg + ", choiceDate=" + choiceDate + ", receiveCustNo=" + receiveCustNo + ", closeNo=" + closeNo
           + ", securedDate=" + securedDate + ", location=" + location + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
           + "]";
  }
}
