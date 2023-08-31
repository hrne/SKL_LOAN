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
 * LaLgtp AS400土地明細資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`LaLgtp`")
public class LaLgtp implements Serializable {


  @EmbeddedId
  private LaLgtpId laLgtpId;

  // 營業單位別
  @Column(name = "`Cusbrh`", insertable = false, updatable = false)
  private int cusbrh = 0;

  // 押品別１
  @Column(name = "`Gdrid1`", insertable = false, updatable = false)
  private int gdrid1 = 0;

  // 押品別２
  @Column(name = "`Gdrid2`", insertable = false, updatable = false)
  private int gdrid2 = 0;

  // 押品號碼
  @Column(name = "`Gdrnum`", insertable = false, updatable = false)
  private int gdrnum = 0;

  // 序號
  @Column(name = "`Lgtseq`", insertable = false, updatable = false)
  private int lgtseq = 0;

  // 註記
  @Column(name = "`Gdrmrk`", length = 20)
  private String gdrmrk;

  // 擔保品群組編號
  @Column(name = "`Gdrnum2`")
  private BigDecimal gdrnum2 = new BigDecimal("0");

  // 押品狀況碼
  @Column(name = "`Grtsts`")
  private int grtsts = 0;

  // 提供人CIFKEY
  @Column(name = "`Lgtcif`")
  private int lgtcif = 0;

  // 核准金額
  @Column(name = "`Lgtiam`")
  private BigDecimal lgtiam = new BigDecimal("0");

  // 鑑價公司
  @Column(name = "`Lgtiid`", length = 20)
  private String lgtiid;

  // 地目代號
  @Column(name = "`Lgtory`", length = 20)
  private String lgtory;

  // 前次移轉金額
  @Column(name = "`Lgtpta`")
  private BigDecimal lgtpta = new BigDecimal("0");

  // 設定金額
  @Column(name = "`Lgtsam`")
  private BigDecimal lgtsam = new BigDecimal("0");

  // 代償後謄本
  @Column(name = "`Lgtsat`")
  private int lgtsat = 0;

  // 縣市
  @Column(name = "`Lgtcty`", length = 20)
  private String lgtcty;

  // 鄉鎮區
  @Column(name = "`Lgttwn`", length = 20)
  private String lgttwn;

  // 段
  @Column(name = "`Lgtsgm`", length = 200)
  private String lgtsgm;

  // 小段
  @Column(name = "`Lgtssg`", length = 200)
  private String lgtssg;

  // 地號１
  @Column(name = "`Lgtnm1`")
  private int lgtnm1 = 0;

  // 地號２
  @Column(name = "`Lgtnm2`")
  private int lgtnm2 = 0;

  // 面積（坪）
  @Column(name = "`Lgtsqm`")
  private BigDecimal lgtsqm = new BigDecimal("0");

  // 土地增值稅
  @Column(name = "`Lgttax`")
  private BigDecimal lgttax = new BigDecimal("0");

  // 土增稅年月
  @Column(name = "`Lgttay`")
  private int lgttay = 0;

  // 使用地類別
  @Column(name = "`Lgttyp`", length = 20)
  private String lgttyp;

  // 移轉年度
  @Column(name = "`Lgttyr`")
  private int lgttyr = 0;

  // 鑑價單價／坪
  @Column(name = "`Lgtunt`")
  private BigDecimal lgtunt = new BigDecimal("0");

  // 使用分區
  @Column(name = "`Lgtuse`", length = 20)
  private String lgtuse;

  // 公告土地現值
  @Column(name = "`Lgtval`")
  private BigDecimal lgtval = new BigDecimal("0");

  // 土地現值年月
  @Column(name = "`Lgtvym`")
  private int lgtvym = 0;

  // Fieldupdateaccessidentifier
  @Column(name = "`Updateident`")
  private int updateident = 0;

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


  public LaLgtpId getLaLgtpId() {
    return this.laLgtpId;
  }

  public void setLaLgtpId(LaLgtpId laLgtpId) {
    this.laLgtpId = laLgtpId;
  }

/**
	* 營業單位別<br>
	* 
	* @return Integer
	*/
  public int getCusbrh() {
    return this.cusbrh;
  }

/**
	* 營業單位別<br>
	* 
  *
  * @param cusbrh 營業單位別
	*/
  public void setCusbrh(int cusbrh) {
    this.cusbrh = cusbrh;
  }

/**
	* 押品別１<br>
	* 
	* @return Integer
	*/
  public int getGdrid1() {
    return this.gdrid1;
  }

/**
	* 押品別１<br>
	* 
  *
  * @param gdrid1 押品別１
	*/
  public void setGdrid1(int gdrid1) {
    this.gdrid1 = gdrid1;
  }

/**
	* 押品別２<br>
	* 
	* @return Integer
	*/
  public int getGdrid2() {
    return this.gdrid2;
  }

/**
	* 押品別２<br>
	* 
  *
  * @param gdrid2 押品別２
	*/
  public void setGdrid2(int gdrid2) {
    this.gdrid2 = gdrid2;
  }

/**
	* 押品號碼<br>
	* 
	* @return Integer
	*/
  public int getGdrnum() {
    return this.gdrnum;
  }

/**
	* 押品號碼<br>
	* 
  *
  * @param gdrnum 押品號碼
	*/
  public void setGdrnum(int gdrnum) {
    this.gdrnum = gdrnum;
  }

/**
	* 序號<br>
	* 
	* @return Integer
	*/
  public int getLgtseq() {
    return this.lgtseq;
  }

/**
	* 序號<br>
	* 
  *
  * @param lgtseq 序號
	*/
  public void setLgtseq(int lgtseq) {
    this.lgtseq = lgtseq;
  }

/**
	* 註記<br>
	* 
	* @return String
	*/
  public String getGdrmrk() {
    return this.gdrmrk == null ? "" : this.gdrmrk;
  }

/**
	* 註記<br>
	* 
  *
  * @param gdrmrk 註記
	*/
  public void setGdrmrk(String gdrmrk) {
    this.gdrmrk = gdrmrk;
  }

/**
	* 擔保品群組編號<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getGdrnum2() {
    return this.gdrnum2;
  }

/**
	* 擔保品群組編號<br>
	* 
  *
  * @param gdrnum2 擔保品群組編號
	*/
  public void setGdrnum2(BigDecimal gdrnum2) {
    this.gdrnum2 = gdrnum2;
  }

/**
	* 押品狀況碼<br>
	* 
	* @return Integer
	*/
  public int getGrtsts() {
    return this.grtsts;
  }

/**
	* 押品狀況碼<br>
	* 
  *
  * @param grtsts 押品狀況碼
	*/
  public void setGrtsts(int grtsts) {
    this.grtsts = grtsts;
  }

/**
	* 提供人CIFKEY<br>
	* 
	* @return Integer
	*/
  public int getLgtcif() {
    return this.lgtcif;
  }

/**
	* 提供人CIFKEY<br>
	* 
  *
  * @param lgtcif 提供人CIFKEY
	*/
  public void setLgtcif(int lgtcif) {
    this.lgtcif = lgtcif;
  }

/**
	* 核准金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLgtiam() {
    return this.lgtiam;
  }

/**
	* 核准金額<br>
	* 
  *
  * @param lgtiam 核准金額
	*/
  public void setLgtiam(BigDecimal lgtiam) {
    this.lgtiam = lgtiam;
  }

/**
	* 鑑價公司<br>
	* 
	* @return String
	*/
  public String getLgtiid() {
    return this.lgtiid == null ? "" : this.lgtiid;
  }

/**
	* 鑑價公司<br>
	* 
  *
  * @param lgtiid 鑑價公司
	*/
  public void setLgtiid(String lgtiid) {
    this.lgtiid = lgtiid;
  }

/**
	* 地目代號<br>
	* 
	* @return String
	*/
  public String getLgtory() {
    return this.lgtory == null ? "" : this.lgtory;
  }

/**
	* 地目代號<br>
	* 
  *
  * @param lgtory 地目代號
	*/
  public void setLgtory(String lgtory) {
    this.lgtory = lgtory;
  }

/**
	* 前次移轉金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLgtpta() {
    return this.lgtpta;
  }

/**
	* 前次移轉金額<br>
	* 
  *
  * @param lgtpta 前次移轉金額
	*/
  public void setLgtpta(BigDecimal lgtpta) {
    this.lgtpta = lgtpta;
  }

/**
	* 設定金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLgtsam() {
    return this.lgtsam;
  }

/**
	* 設定金額<br>
	* 
  *
  * @param lgtsam 設定金額
	*/
  public void setLgtsam(BigDecimal lgtsam) {
    this.lgtsam = lgtsam;
  }

/**
	* 代償後謄本<br>
	* 
	* @return Integer
	*/
  public int getLgtsat() {
    return this.lgtsat;
  }

/**
	* 代償後謄本<br>
	* 
  *
  * @param lgtsat 代償後謄本
	*/
  public void setLgtsat(int lgtsat) {
    this.lgtsat = lgtsat;
  }

/**
	* 縣市<br>
	* 
	* @return String
	*/
  public String getLgtcty() {
    return this.lgtcty == null ? "" : this.lgtcty;
  }

/**
	* 縣市<br>
	* 
  *
  * @param lgtcty 縣市
	*/
  public void setLgtcty(String lgtcty) {
    this.lgtcty = lgtcty;
  }

/**
	* 鄉鎮區<br>
	* 
	* @return String
	*/
  public String getLgttwn() {
    return this.lgttwn == null ? "" : this.lgttwn;
  }

/**
	* 鄉鎮區<br>
	* 
  *
  * @param lgttwn 鄉鎮區
	*/
  public void setLgttwn(String lgttwn) {
    this.lgttwn = lgttwn;
  }

/**
	* 段<br>
	* 
	* @return String
	*/
  public String getLgtsgm() {
    return this.lgtsgm == null ? "" : this.lgtsgm;
  }

/**
	* 段<br>
	* 
  *
  * @param lgtsgm 段
	*/
  public void setLgtsgm(String lgtsgm) {
    this.lgtsgm = lgtsgm;
  }

/**
	* 小段<br>
	* 
	* @return String
	*/
  public String getLgtssg() {
    return this.lgtssg == null ? "" : this.lgtssg;
  }

/**
	* 小段<br>
	* 
  *
  * @param lgtssg 小段
	*/
  public void setLgtssg(String lgtssg) {
    this.lgtssg = lgtssg;
  }

/**
	* 地號１<br>
	* 
	* @return Integer
	*/
  public int getLgtnm1() {
    return this.lgtnm1;
  }

/**
	* 地號１<br>
	* 
  *
  * @param lgtnm1 地號１
	*/
  public void setLgtnm1(int lgtnm1) {
    this.lgtnm1 = lgtnm1;
  }

/**
	* 地號２<br>
	* 
	* @return Integer
	*/
  public int getLgtnm2() {
    return this.lgtnm2;
  }

/**
	* 地號２<br>
	* 
  *
  * @param lgtnm2 地號２
	*/
  public void setLgtnm2(int lgtnm2) {
    this.lgtnm2 = lgtnm2;
  }

/**
	* 面積（坪）<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLgtsqm() {
    return this.lgtsqm;
  }

/**
	* 面積（坪）<br>
	* 
  *
  * @param lgtsqm 面積（坪）
	*/
  public void setLgtsqm(BigDecimal lgtsqm) {
    this.lgtsqm = lgtsqm;
  }

/**
	* 土地增值稅<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLgttax() {
    return this.lgttax;
  }

/**
	* 土地增值稅<br>
	* 
  *
  * @param lgttax 土地增值稅
	*/
  public void setLgttax(BigDecimal lgttax) {
    this.lgttax = lgttax;
  }

/**
	* 土增稅年月<br>
	* 
	* @return Integer
	*/
  public int getLgttay() {
    return this.lgttay;
  }

/**
	* 土增稅年月<br>
	* 
  *
  * @param lgttay 土增稅年月
	*/
  public void setLgttay(int lgttay) {
    this.lgttay = lgttay;
  }

/**
	* 使用地類別<br>
	* 
	* @return String
	*/
  public String getLgttyp() {
    return this.lgttyp == null ? "" : this.lgttyp;
  }

/**
	* 使用地類別<br>
	* 
  *
  * @param lgttyp 使用地類別
	*/
  public void setLgttyp(String lgttyp) {
    this.lgttyp = lgttyp;
  }

/**
	* 移轉年度<br>
	* 
	* @return Integer
	*/
  public int getLgttyr() {
    return this.lgttyr;
  }

/**
	* 移轉年度<br>
	* 
  *
  * @param lgttyr 移轉年度
	*/
  public void setLgttyr(int lgttyr) {
    this.lgttyr = lgttyr;
  }

/**
	* 鑑價單價／坪<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLgtunt() {
    return this.lgtunt;
  }

/**
	* 鑑價單價／坪<br>
	* 
  *
  * @param lgtunt 鑑價單價／坪
	*/
  public void setLgtunt(BigDecimal lgtunt) {
    this.lgtunt = lgtunt;
  }

/**
	* 使用分區<br>
	* 
	* @return String
	*/
  public String getLgtuse() {
    return this.lgtuse == null ? "" : this.lgtuse;
  }

/**
	* 使用分區<br>
	* 
  *
  * @param lgtuse 使用分區
	*/
  public void setLgtuse(String lgtuse) {
    this.lgtuse = lgtuse;
  }

/**
	* 公告土地現值<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLgtval() {
    return this.lgtval;
  }

/**
	* 公告土地現值<br>
	* 
  *
  * @param lgtval 公告土地現值
	*/
  public void setLgtval(BigDecimal lgtval) {
    this.lgtval = lgtval;
  }

/**
	* 土地現值年月<br>
	* 
	* @return Integer
	*/
  public int getLgtvym() {
    return this.lgtvym;
  }

/**
	* 土地現值年月<br>
	* 
  *
  * @param lgtvym 土地現值年月
	*/
  public void setLgtvym(int lgtvym) {
    this.lgtvym = lgtvym;
  }

/**
	* Fieldupdateaccessidentifier<br>
	* 
	* @return Integer
	*/
  public int getUpdateident() {
    return this.updateident;
  }

/**
	* Fieldupdateaccessidentifier<br>
	* 
  *
  * @param updateident Fieldupdateaccessidentifier
	*/
  public void setUpdateident(int updateident) {
    this.updateident = updateident;
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
    return "LaLgtp [laLgtpId=" + laLgtpId + ", gdrmrk=" + gdrmrk
           + ", gdrnum2=" + gdrnum2 + ", grtsts=" + grtsts + ", lgtcif=" + lgtcif + ", lgtiam=" + lgtiam + ", lgtiid=" + lgtiid + ", lgtory=" + lgtory
           + ", lgtpta=" + lgtpta + ", lgtsam=" + lgtsam + ", lgtsat=" + lgtsat + ", lgtcty=" + lgtcty + ", lgttwn=" + lgttwn + ", lgtsgm=" + lgtsgm
           + ", lgtssg=" + lgtssg + ", lgtnm1=" + lgtnm1 + ", lgtnm2=" + lgtnm2 + ", lgtsqm=" + lgtsqm + ", lgttax=" + lgttax + ", lgttay=" + lgttay
           + ", lgttyp=" + lgttyp + ", lgttyr=" + lgttyr + ", lgtunt=" + lgtunt + ", lgtuse=" + lgtuse + ", lgtval=" + lgtval + ", lgtvym=" + lgtvym
           + ", updateident=" + updateident + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
