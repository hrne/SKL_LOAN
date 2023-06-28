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
 * Lagdtp AS400不動產押品主檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`Lagdtp`")
public class Lagdtp implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -8436700720407899454L;

@EmbeddedId
  private LagdtpId lagdtpId;

  // 營業單位別
  @Column(name = "`Cusbrh`", insertable = false, updatable = false)
  private int cusbrh = 0;

  // 押品別1
  @Column(name = "`Gdrid1`", insertable = false, updatable = false)
  private int gdrid1 = 0;

  // 押品別2
  @Column(name = "`Gdrid2`", insertable = false, updatable = false)
  private int gdrid2 = 0;

  // 押品號碼
  @Column(name = "`Gdrnum`", insertable = false, updatable = false)
  private int gdrnum = 0;

  // 地區別
  @Column(name = "`Loclid`")
  private int loclid = 0;

  // 鑑價期限
  @Column(name = "`Gdtidt`")
  private int gdtidt = 0;

  // 他項存續期限
  @Column(name = "`Gdtrdt`")
  private int gdtrdt = 0;

  // 順位
  @Column(name = "`Gdtpty`")
  private int gdtpty = 0;

  // 前一順位金額
  @Column(name = "`Gdtp1a`")
  private BigDecimal gdtp1a = new BigDecimal("0");

  // 前一順位債權人
  @Column(name = "`Gdtp1m`", length = 200)
  private String gdtp1m;

  // 前二順位金額
  @Column(name = "`Gdtp2a`")
  private BigDecimal gdtp2a = new BigDecimal("0");

  // 前二順位債權人
  @Column(name = "`Gdtp2m`", length = 200)
  private String gdtp2m;

  // 前三順位金額
  @Column(name = "`Gdtp3a`")
  private BigDecimal gdtp3a = new BigDecimal("0");

  // 前三順位債權人
  @Column(name = "`Gdtp3m`", length = 200)
  private String gdtp3m;

  // 建物標示備註
  @Column(name = "`Gdttmr`", length = 200)
  private String gdttmr;

  // 核准額度
  @Column(name = "`Aplpam`")
  private BigDecimal aplpam = new BigDecimal("0");

  // 借款人戶號
  @Column(name = "`Lmsacn`")
  private int lmsacn = 0;

  // 額度編號
  @Column(name = "`Lmsapn`")
  private int lmsapn = 0;

  // 設定日期
  @Column(name = "`Gdtsdt`")
  private int gdtsdt = 0;

  // 不動產別
  @Column(name = "`Gdttyp`")
  private int gdttyp = 0;

  // 一押品多額度時應註明相同額度
  @Column(name = "`Gdtapn`")
  private int gdtapn = 0;

  // 評估淨值
  @Column(name = "`Estval`")
  private BigDecimal estval = new BigDecimal("0");

  // 出租評估淨值
  @Column(name = "`Rstval`")
  private BigDecimal rstval = new BigDecimal("0");

  // 評估總價
  @Column(name = "`Ettval`")
  private BigDecimal ettval = new BigDecimal("0");

  // 總增值稅
  @Column(name = "`Risval`")
  private BigDecimal risval = new BigDecimal("0");

  // 押租金
  @Column(name = "`Rntval`")
  private BigDecimal rntval = new BigDecimal("0");

  // 抵押權設定種類
  @Column(name = "`Mgttyp`", length = 20)
  private String mgttyp;

  // 是否檢附同意書
  @Column(name = "`Mtgagm`", length = 20)
  private String mtgagm;

  // 擔保品群組編號
  @Column(name = "`Gdrnum2`")
  private BigDecimal gdrnum2 = new BigDecimal("0");

  // 註記
  @Column(name = "`Gdrmrk`", length = 20)
  private String gdrmrk;

  // Field update / access identifier
  @Column(name = "`UpdateIdent`")
  private int updateIdent = 0;

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


  public LagdtpId getLagdtpId() {
    return this.lagdtpId;
  }

  public void setLagdtpId(LagdtpId lagdtpId) {
    this.lagdtpId = lagdtpId;
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
	* 押品別1<br>
	* 
	* @return Integer
	*/
  public int getGdrid1() {
    return this.gdrid1;
  }

/**
	* 押品別1<br>
	* 
  *
  * @param gdrid1 押品別1
	*/
  public void setGdrid1(int gdrid1) {
    this.gdrid1 = gdrid1;
  }

/**
	* 押品別2<br>
	* 
	* @return Integer
	*/
  public int getGdrid2() {
    return this.gdrid2;
  }

/**
	* 押品別2<br>
	* 
  *
  * @param gdrid2 押品別2
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
	* 地區別<br>
	* 
	* @return Integer
	*/
  public int getLoclid() {
    return this.loclid;
  }

/**
	* 地區別<br>
	* 
  *
  * @param loclid 地區別
	*/
  public void setLoclid(int loclid) {
    this.loclid = loclid;
  }

/**
	* 鑑價期限<br>
	* 
	* @return Integer
	*/
  public int getGdtidt() {
    return this.gdtidt;
  }

/**
	* 鑑價期限<br>
	* 
  *
  * @param gdtidt 鑑價期限
	*/
  public void setGdtidt(int gdtidt) {
    this.gdtidt = gdtidt;
  }

/**
	* 他項存續期限<br>
	* 
	* @return Integer
	*/
  public int getGdtrdt() {
    return this.gdtrdt;
  }

/**
	* 他項存續期限<br>
	* 
  *
  * @param gdtrdt 他項存續期限
	*/
  public void setGdtrdt(int gdtrdt) {
    this.gdtrdt = gdtrdt;
  }

/**
	* 順位<br>
	* 
	* @return Integer
	*/
  public int getGdtpty() {
    return this.gdtpty;
  }

/**
	* 順位<br>
	* 
  *
  * @param gdtpty 順位
	*/
  public void setGdtpty(int gdtpty) {
    this.gdtpty = gdtpty;
  }

/**
	* 前一順位金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getGdtp1a() {
    return this.gdtp1a;
  }

/**
	* 前一順位金額<br>
	* 
  *
  * @param gdtp1a 前一順位金額
	*/
  public void setGdtp1a(BigDecimal gdtp1a) {
    this.gdtp1a = gdtp1a;
  }

/**
	* 前一順位債權人<br>
	* 
	* @return String
	*/
  public String getGdtp1m() {
    return this.gdtp1m == null ? "" : this.gdtp1m;
  }

/**
	* 前一順位債權人<br>
	* 
  *
  * @param gdtp1m 前一順位債權人
	*/
  public void setGdtp1m(String gdtp1m) {
    this.gdtp1m = gdtp1m;
  }

/**
	* 前二順位金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getGdtp2a() {
    return this.gdtp2a;
  }

/**
	* 前二順位金額<br>
	* 
  *
  * @param gdtp2a 前二順位金額
	*/
  public void setGdtp2a(BigDecimal gdtp2a) {
    this.gdtp2a = gdtp2a;
  }

/**
	* 前二順位債權人<br>
	* 
	* @return String
	*/
  public String getGdtp2m() {
    return this.gdtp2m == null ? "" : this.gdtp2m;
  }

/**
	* 前二順位債權人<br>
	* 
  *
  * @param gdtp2m 前二順位債權人
	*/
  public void setGdtp2m(String gdtp2m) {
    this.gdtp2m = gdtp2m;
  }

/**
	* 前三順位金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getGdtp3a() {
    return this.gdtp3a;
  }

/**
	* 前三順位金額<br>
	* 
  *
  * @param gdtp3a 前三順位金額
	*/
  public void setGdtp3a(BigDecimal gdtp3a) {
    this.gdtp3a = gdtp3a;
  }

/**
	* 前三順位債權人<br>
	* 
	* @return String
	*/
  public String getGdtp3m() {
    return this.gdtp3m == null ? "" : this.gdtp3m;
  }

/**
	* 前三順位債權人<br>
	* 
  *
  * @param gdtp3m 前三順位債權人
	*/
  public void setGdtp3m(String gdtp3m) {
    this.gdtp3m = gdtp3m;
  }

/**
	* 建物標示備註<br>
	* 
	* @return String
	*/
  public String getGdttmr() {
    return this.gdttmr == null ? "" : this.gdttmr;
  }

/**
	* 建物標示備註<br>
	* 
  *
  * @param gdttmr 建物標示備註
	*/
  public void setGdttmr(String gdttmr) {
    this.gdttmr = gdttmr;
  }

/**
	* 核准額度<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAplpam() {
    return this.aplpam;
  }

/**
	* 核准額度<br>
	* 
  *
  * @param aplpam 核准額度
	*/
  public void setAplpam(BigDecimal aplpam) {
    this.aplpam = aplpam;
  }

/**
	* 借款人戶號<br>
	* 
	* @return Integer
	*/
  public int getLmsacn() {
    return this.lmsacn;
  }

/**
	* 借款人戶號<br>
	* 
  *
  * @param lmsacn 借款人戶號
	*/
  public void setLmsacn(int lmsacn) {
    this.lmsacn = lmsacn;
  }

/**
	* 額度編號<br>
	* 
	* @return Integer
	*/
  public int getLmsapn() {
    return this.lmsapn;
  }

/**
	* 額度編號<br>
	* 
  *
  * @param lmsapn 額度編號
	*/
  public void setLmsapn(int lmsapn) {
    this.lmsapn = lmsapn;
  }

/**
	* 設定日期<br>
	* 
	* @return Integer
	*/
  public int getGdtsdt() {
    return this.gdtsdt;
  }

/**
	* 設定日期<br>
	* 
  *
  * @param gdtsdt 設定日期
	*/
  public void setGdtsdt(int gdtsdt) {
    this.gdtsdt = gdtsdt;
  }

/**
	* 不動產別<br>
	* 
	* @return Integer
	*/
  public int getGdttyp() {
    return this.gdttyp;
  }

/**
	* 不動產別<br>
	* 
  *
  * @param gdttyp 不動產別
	*/
  public void setGdttyp(int gdttyp) {
    this.gdttyp = gdttyp;
  }

/**
	* 一押品多額度時應註明相同額度<br>
	* 
	* @return Integer
	*/
  public int getGdtapn() {
    return this.gdtapn;
  }

/**
	* 一押品多額度時應註明相同額度<br>
	* 
  *
  * @param gdtapn 一押品多額度時應註明相同額度
	*/
  public void setGdtapn(int gdtapn) {
    this.gdtapn = gdtapn;
  }

/**
	* 評估淨值<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getEstval() {
    return this.estval;
  }

/**
	* 評估淨值<br>
	* 
  *
  * @param estval 評估淨值
	*/
  public void setEstval(BigDecimal estval) {
    this.estval = estval;
  }

/**
	* 出租評估淨值<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getRstval() {
    return this.rstval;
  }

/**
	* 出租評估淨值<br>
	* 
  *
  * @param rstval 出租評估淨值
	*/
  public void setRstval(BigDecimal rstval) {
    this.rstval = rstval;
  }

/**
	* 評估總價<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getEttval() {
    return this.ettval;
  }

/**
	* 評估總價<br>
	* 
  *
  * @param ettval 評估總價
	*/
  public void setEttval(BigDecimal ettval) {
    this.ettval = ettval;
  }

/**
	* 總增值稅<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getRisval() {
    return this.risval;
  }

/**
	* 總增值稅<br>
	* 
  *
  * @param risval 總增值稅
	*/
  public void setRisval(BigDecimal risval) {
    this.risval = risval;
  }

/**
	* 押租金<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getRntval() {
    return this.rntval;
  }

/**
	* 押租金<br>
	* 
  *
  * @param rntval 押租金
	*/
  public void setRntval(BigDecimal rntval) {
    this.rntval = rntval;
  }

/**
	* 抵押權設定種類<br>
	* 
	* @return String
	*/
  public String getMgttyp() {
    return this.mgttyp == null ? "" : this.mgttyp;
  }

/**
	* 抵押權設定種類<br>
	* 
  *
  * @param mgttyp 抵押權設定種類
	*/
  public void setMgttyp(String mgttyp) {
    this.mgttyp = mgttyp;
  }

/**
	* 是否檢附同意書<br>
	* 
	* @return String
	*/
  public String getMtgagm() {
    return this.mtgagm == null ? "" : this.mtgagm;
  }

/**
	* 是否檢附同意書<br>
	* 
  *
  * @param mtgagm 是否檢附同意書
	*/
  public void setMtgagm(String mtgagm) {
    this.mtgagm = mtgagm;
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
	* Field update / access identifier<br>
	* 
	* @return Integer
	*/
  public int getUpdateIdent() {
    return this.updateIdent;
  }

/**
	* Field update / access identifier<br>
	* 
  *
  * @param updateIdent Field update / access identifier
	*/
  public void setUpdateIdent(int updateIdent) {
    this.updateIdent = updateIdent;
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
    return "Lagdtp [lagdtpId=" + lagdtpId + ", loclid=" + loclid + ", gdtidt=" + gdtidt
           + ", gdtrdt=" + gdtrdt + ", gdtpty=" + gdtpty + ", gdtp1a=" + gdtp1a + ", gdtp1m=" + gdtp1m + ", gdtp2a=" + gdtp2a + ", gdtp2m=" + gdtp2m
           + ", gdtp3a=" + gdtp3a + ", gdtp3m=" + gdtp3m + ", gdttmr=" + gdttmr + ", aplpam=" + aplpam + ", lmsacn=" + lmsacn + ", lmsapn=" + lmsapn
           + ", gdtsdt=" + gdtsdt + ", gdttyp=" + gdttyp + ", gdtapn=" + gdtapn + ", estval=" + estval + ", rstval=" + rstval + ", ettval=" + ettval
           + ", risval=" + risval + ", rntval=" + rntval + ", mgttyp=" + mgttyp + ", mtgagm=" + mtgagm + ", gdrnum2=" + gdrnum2 + ", gdrmrk=" + gdrmrk
           + ", updateIdent=" + updateIdent + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
