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

/**
 * ClNoMap 擔保品編號新舊對照檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`ClNoMap`")
public class ClNoMap implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 7740711368547243587L;

@EmbeddedId
  private ClNoMapId clNoMapId;

  // 原擔保品代號1
  /* 原擔保品 */
  @Column(name = "`GDRID1`", insertable = false, updatable = false)
  private int gDRID1 = 0;

  // 原擔保品代號2
  /* 原擔保品 */
  @Column(name = "`GDRID2`", insertable = false, updatable = false)
  private int gDRID2 = 0;

  // 原擔保品編號
  /* 原擔保品 */
  @Column(name = "`GDRNUM`", insertable = false, updatable = false)
  private int gDRNUM = 0;

  // 原擔保品序號
  /* 原擔保品 */
  @Column(name = "`LGTSEQ`", insertable = false, updatable = false)
  private int lGTSEQ = 0;

  // 最新擔保品代號1
  /* 經過唯一性處理後之最新擔保品若原擔保品未被唯一性處理為一筆,此欄位擺0 */
  @Column(name = "`MainGDRID1`")
  private int mainGDRID1 = 0;

  // 最新擔保品代號2
  /* 經過唯一性處理後之最新擔保品若原擔保品未被唯一性處理為一筆,此欄位擺0 */
  @Column(name = "`MainGDRID2`")
  private int mainGDRID2 = 0;

  // 最新擔保品編號
  /* 經過唯一性處理後之最新擔保品若原擔保品未被唯一性處理為一筆,此欄位擺0 */
  @Column(name = "`MainGDRNUM`")
  private int mainGDRNUM = 0;

  // 最新擔保品序號
  /* 經過唯一性處理後之最新擔保品若原擔保品未被唯一性處理為一筆,此欄位擺0 */
  @Column(name = "`MainLGTSEQ`")
  private int mainLGTSEQ = 0;

  // 新擔保品代號1
  /* 與原擔保品對應之新擔保品 */
  @Column(name = "`ClCode1`")
  private int clCode1 = 0;

  // 新擔保品代號2
  /* 與原擔保品對應之新擔保品 */
  @Column(name = "`ClCode2`")
  private int clCode2 = 0;

  // 新擔保品編號
  /* 與原擔保品對應之新擔保品 */
  @Column(name = "`ClNo`")
  private int clNo = 0;

  // 轉換結果
  /* 共用代碼檔0:未轉入,資料不完整(其他原因)1:唯一性處理後,此筆作為最新擔保品轉入2:唯一性處理後,此筆不轉入,僅由最新擔保品轉入3:單筆擔保品直接轉入4:未轉入,資料不完整(擔保品提供人)5:未轉入,資料不完整(門牌地址)6:未轉入,資料不完整(主建號)7:未轉入,資料不完整(地號) */
  @Column(name = "`TfStatus`")
  private int tfStatus = 0;

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


  public ClNoMapId getClNoMapId() {
    return this.clNoMapId;
  }

  public void setClNoMapId(ClNoMapId clNoMapId) {
    this.clNoMapId = clNoMapId;
  }

/**
	* 原擔保品代號1<br>
	* 原擔保品
	* @return Integer
	*/
  public int getGDRID1() {
    return this.gDRID1;
  }

/**
	* 原擔保品代號1<br>
	* 原擔保品
  *
  * @param gDRID1 原擔保品代號1
	*/
  public void setGDRID1(int gDRID1) {
    this.gDRID1 = gDRID1;
  }

/**
	* 原擔保品代號2<br>
	* 原擔保品
	* @return Integer
	*/
  public int getGDRID2() {
    return this.gDRID2;
  }

/**
	* 原擔保品代號2<br>
	* 原擔保品
  *
  * @param gDRID2 原擔保品代號2
	*/
  public void setGDRID2(int gDRID2) {
    this.gDRID2 = gDRID2;
  }

/**
	* 原擔保品編號<br>
	* 原擔保品
	* @return Integer
	*/
  public int getGDRNUM() {
    return this.gDRNUM;
  }

/**
	* 原擔保品編號<br>
	* 原擔保品
  *
  * @param gDRNUM 原擔保品編號
	*/
  public void setGDRNUM(int gDRNUM) {
    this.gDRNUM = gDRNUM;
  }

/**
	* 原擔保品序號<br>
	* 原擔保品
	* @return Integer
	*/
  public int getLGTSEQ() {
    return this.lGTSEQ;
  }

/**
	* 原擔保品序號<br>
	* 原擔保品
  *
  * @param lGTSEQ 原擔保品序號
	*/
  public void setLGTSEQ(int lGTSEQ) {
    this.lGTSEQ = lGTSEQ;
  }

/**
	* 最新擔保品代號1<br>
	* 經過唯一性處理後之最新擔保品
若原擔保品未被唯一性處理為一筆,此欄位擺0
	* @return Integer
	*/
  public int getMainGDRID1() {
    return this.mainGDRID1;
  }

/**
	* 最新擔保品代號1<br>
	* 經過唯一性處理後之最新擔保品
若原擔保品未被唯一性處理為一筆,此欄位擺0
  *
  * @param mainGDRID1 最新擔保品代號1
	*/
  public void setMainGDRID1(int mainGDRID1) {
    this.mainGDRID1 = mainGDRID1;
  }

/**
	* 最新擔保品代號2<br>
	* 經過唯一性處理後之最新擔保品
若原擔保品未被唯一性處理為一筆,此欄位擺0
	* @return Integer
	*/
  public int getMainGDRID2() {
    return this.mainGDRID2;
  }

/**
	* 最新擔保品代號2<br>
	* 經過唯一性處理後之最新擔保品
若原擔保品未被唯一性處理為一筆,此欄位擺0
  *
  * @param mainGDRID2 最新擔保品代號2
	*/
  public void setMainGDRID2(int mainGDRID2) {
    this.mainGDRID2 = mainGDRID2;
  }

/**
	* 最新擔保品編號<br>
	* 經過唯一性處理後之最新擔保品
若原擔保品未被唯一性處理為一筆,此欄位擺0
	* @return Integer
	*/
  public int getMainGDRNUM() {
    return this.mainGDRNUM;
  }

/**
	* 最新擔保品編號<br>
	* 經過唯一性處理後之最新擔保品
若原擔保品未被唯一性處理為一筆,此欄位擺0
  *
  * @param mainGDRNUM 最新擔保品編號
	*/
  public void setMainGDRNUM(int mainGDRNUM) {
    this.mainGDRNUM = mainGDRNUM;
  }

/**
	* 最新擔保品序號<br>
	* 經過唯一性處理後之最新擔保品
若原擔保品未被唯一性處理為一筆,此欄位擺0
	* @return Integer
	*/
  public int getMainLGTSEQ() {
    return this.mainLGTSEQ;
  }

/**
	* 最新擔保品序號<br>
	* 經過唯一性處理後之最新擔保品
若原擔保品未被唯一性處理為一筆,此欄位擺0
  *
  * @param mainLGTSEQ 最新擔保品序號
	*/
  public void setMainLGTSEQ(int mainLGTSEQ) {
    this.mainLGTSEQ = mainLGTSEQ;
  }

/**
	* 新擔保品代號1<br>
	* 與原擔保品對應之新擔保品
	* @return Integer
	*/
  public int getClCode1() {
    return this.clCode1;
  }

/**
	* 新擔保品代號1<br>
	* 與原擔保品對應之新擔保品
  *
  * @param clCode1 新擔保品代號1
	*/
  public void setClCode1(int clCode1) {
    this.clCode1 = clCode1;
  }

/**
	* 新擔保品代號2<br>
	* 與原擔保品對應之新擔保品
	* @return Integer
	*/
  public int getClCode2() {
    return this.clCode2;
  }

/**
	* 新擔保品代號2<br>
	* 與原擔保品對應之新擔保品
  *
  * @param clCode2 新擔保品代號2
	*/
  public void setClCode2(int clCode2) {
    this.clCode2 = clCode2;
  }

/**
	* 新擔保品編號<br>
	* 與原擔保品對應之新擔保品
	* @return Integer
	*/
  public int getClNo() {
    return this.clNo;
  }

/**
	* 新擔保品編號<br>
	* 與原擔保品對應之新擔保品
  *
  * @param clNo 新擔保品編號
	*/
  public void setClNo(int clNo) {
    this.clNo = clNo;
  }

/**
	* 轉換結果<br>
	* 共用代碼檔
0:未轉入,資料不完整(其他原因)
1:唯一性處理後,此筆作為最新擔保品轉入
2:唯一性處理後,此筆不轉入,僅由最新擔保品轉入
3:單筆擔保品直接轉入
4:未轉入,資料不完整(擔保品提供人)
5:未轉入,資料不完整(門牌地址)
6:未轉入,資料不完整(主建號)
7:未轉入,資料不完整(地號)
	* @return Integer
	*/
  public int getTfStatus() {
    return this.tfStatus;
  }

/**
	* 轉換結果<br>
	* 共用代碼檔
0:未轉入,資料不完整(其他原因)
1:唯一性處理後,此筆作為最新擔保品轉入
2:唯一性處理後,此筆不轉入,僅由最新擔保品轉入
3:單筆擔保品直接轉入
4:未轉入,資料不完整(擔保品提供人)
5:未轉入,資料不完整(門牌地址)
6:未轉入,資料不完整(主建號)
7:未轉入,資料不完整(地號)
  *
  * @param tfStatus 轉換結果
	*/
  public void setTfStatus(int tfStatus) {
    this.tfStatus = tfStatus;
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
    return "ClNoMap [clNoMapId=" + clNoMapId + ", mainGDRID1=" + mainGDRID1 + ", mainGDRID2=" + mainGDRID2
           + ", mainGDRNUM=" + mainGDRNUM + ", mainLGTSEQ=" + mainLGTSEQ + ", clCode1=" + clCode1 + ", clCode2=" + clCode2 + ", clNo=" + clNo + ", tfStatus=" + tfStatus
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
