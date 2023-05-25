package com.st1.itx.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
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
 * Lahgtp AS400建物明細資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`Lahgtp`")
public class Lahgtp implements Serializable {


  @EmbeddedId
  private LahgtpId lahgtpId;

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

  // 序號
  @Column(name = "`Lgtseq`", insertable = false, updatable = false)
  private int lgtseq = 0;

  // 提供人CIFKEY
  @Column(name = "`Lgtcif`")
  private int lgtcif = 0;

  // 門牌號碼
  @Column(name = "`Lgtadr`", length = 200)
  private String lgtadr;

  // 主建物建號
  @Column(name = "`Hgtmhn`")
  private int hgtmhn = 0;

  // 主建物(坪)
  @Column(name = "`Hgtmhs`")
  private BigDecimal hgtmhs = new BigDecimal("0");

  // 公設(坪)
  @Column(name = "`Hgtpsm`")
  private BigDecimal hgtpsm = new BigDecimal("0");

  // 車位(坪)
  @Column(name = "`Hgtcam`")
  private BigDecimal hgtcam = new BigDecimal("0");

  // 鑑價公司
  @Column(name = "`Lgtiid`", length = 20)
  private String lgtiid;

  // 鑑價單價/坪
  @Column(name = "`Lgtunt`")
  private BigDecimal lgtunt = new BigDecimal("0");

  // 核准金額
  @Column(name = "`Lgtiam`")
  private BigDecimal lgtiam = new BigDecimal("0");

  // 設定金額
  @Column(name = "`Lgtsam`")
  private BigDecimal lgtsam = new BigDecimal("0");

  // 代償後謄本
  @Column(name = "`Lgtsat`")
  private int lgtsat = 0;

  // 押品狀況碼
  @Column(name = "`Grtsts`")
  private int grtsts = 0;

  // 建物結構
  @Column(name = "`Hgtstr`", length = 20)
  private String hgtstr;

  // 建造年份
  @Column(name = "`Hgtcdt`")
  private int hgtcdt = 0;

  // 樓層數
  @Column(name = "`Hgtflr`")
  private int hgtflr = 0;

  // 屋頂結構
  @Column(name = "`Hgtrof`", length = 20)
  private String hgtrof;

  // 賣方姓名
  @Column(name = "`Salnam`", length = 200)
  private String salnam;

  // 賣方ID
  @Column(name = "`Salid1`", length = 200)
  private String salid1;

  // 停車位形式
  @Column(name = "`Hgtcap`", length = 20)
  private String hgtcap;

  // 主要用途
  @Column(name = "`Hgtgus`", length = 20)
  private String hgtgus;

  // 附屬建物用途
  @Column(name = "`Hgtaus`", length = 20)
  private String hgtaus;

  // 所在樓層
  @Column(name = "`Hgtfor`", length = 20)
  private String hgtfor;

  // 建築完成日
  @Column(name = "`Hgtcpe`")
  private int hgtcpe = 0;

  // 附屬建物(坪)
  @Column(name = "`Hgtads`")
  private BigDecimal hgtads = new BigDecimal("0");

  // 縣市名稱
  @Column(name = "`Hgtad1`", length = 20)
  private String hgtad1;

  // 鄉鎮市區名稱
  @Column(name = "`Hgtad2`", length = 200)
  private String hgtad2;

  // 街路巷弄
  @Column(name = "`Hgtad3`", length = 200)
  private String hgtad3;

  // 房屋所有權取得日
  @Column(name = "`Hgtgtd`")
  private int hgtgtd = 0;

  // 買賣契約價格
  @Column(name = "`Buyamt`")
  private BigDecimal buyamt = new BigDecimal("0");

  // 買賣契約日期
  @Column(name = "`Buydat`")
  private int buydat = 0;

  // 擔保品群組編號
  @Column(name = "`Gdrnum2`")
  private BigDecimal gdrnum2 = new BigDecimal("0");

  // 註記
  @Column(name = "`Gdrmrk`", length = 20)
  private String gdrmrk;

  // 主建物建號2
  @Column(name = "`Hgtmhn2`")
  private int hgtmhn2 = 0;

  // 獨立產權註記
  @Column(name = "`Hgtcip`", length = 20)
  private String hgtcip;

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


  public LahgtpId getLahgtpId() {
    return this.lahgtpId;
  }

  public void setLahgtpId(LahgtpId lahgtpId) {
    this.lahgtpId = lahgtpId;
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
	* 門牌號碼<br>
	* 
	* @return String
	*/
  public String getLgtadr() {
    return this.lgtadr == null ? "" : this.lgtadr;
  }

/**
	* 門牌號碼<br>
	* 
  *
  * @param lgtadr 門牌號碼
	*/
  public void setLgtadr(String lgtadr) {
    this.lgtadr = lgtadr;
  }

/**
	* 主建物建號<br>
	* 
	* @return Integer
	*/
  public int getHgtmhn() {
    return this.hgtmhn;
  }

/**
	* 主建物建號<br>
	* 
  *
  * @param hgtmhn 主建物建號
	*/
  public void setHgtmhn(int hgtmhn) {
    this.hgtmhn = hgtmhn;
  }

/**
	* 主建物(坪)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getHgtmhs() {
    return this.hgtmhs;
  }

/**
	* 主建物(坪)<br>
	* 
  *
  * @param hgtmhs 主建物(坪)
	*/
  public void setHgtmhs(BigDecimal hgtmhs) {
    this.hgtmhs = hgtmhs;
  }

/**
	* 公設(坪)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getHgtpsm() {
    return this.hgtpsm;
  }

/**
	* 公設(坪)<br>
	* 
  *
  * @param hgtpsm 公設(坪)
	*/
  public void setHgtpsm(BigDecimal hgtpsm) {
    this.hgtpsm = hgtpsm;
  }

/**
	* 車位(坪)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getHgtcam() {
    return this.hgtcam;
  }

/**
	* 車位(坪)<br>
	* 
  *
  * @param hgtcam 車位(坪)
	*/
  public void setHgtcam(BigDecimal hgtcam) {
    this.hgtcam = hgtcam;
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
	* 鑑價單價/坪<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLgtunt() {
    return this.lgtunt;
  }

/**
	* 鑑價單價/坪<br>
	* 
  *
  * @param lgtunt 鑑價單價/坪
	*/
  public void setLgtunt(BigDecimal lgtunt) {
    this.lgtunt = lgtunt;
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
	* 建物結構<br>
	* 
	* @return String
	*/
  public String getHgtstr() {
    return this.hgtstr == null ? "" : this.hgtstr;
  }

/**
	* 建物結構<br>
	* 
  *
  * @param hgtstr 建物結構
	*/
  public void setHgtstr(String hgtstr) {
    this.hgtstr = hgtstr;
  }

/**
	* 建造年份<br>
	* 
	* @return Integer
	*/
  public int getHgtcdt() {
    return this.hgtcdt;
  }

/**
	* 建造年份<br>
	* 
  *
  * @param hgtcdt 建造年份
	*/
  public void setHgtcdt(int hgtcdt) {
    this.hgtcdt = hgtcdt;
  }

/**
	* 樓層數<br>
	* 
	* @return Integer
	*/
  public int getHgtflr() {
    return this.hgtflr;
  }

/**
	* 樓層數<br>
	* 
  *
  * @param hgtflr 樓層數
	*/
  public void setHgtflr(int hgtflr) {
    this.hgtflr = hgtflr;
  }

/**
	* 屋頂結構<br>
	* 
	* @return String
	*/
  public String getHgtrof() {
    return this.hgtrof == null ? "" : this.hgtrof;
  }

/**
	* 屋頂結構<br>
	* 
  *
  * @param hgtrof 屋頂結構
	*/
  public void setHgtrof(String hgtrof) {
    this.hgtrof = hgtrof;
  }

/**
	* 賣方姓名<br>
	* 
	* @return String
	*/
  public String getSalnam() {
    return this.salnam == null ? "" : this.salnam;
  }

/**
	* 賣方姓名<br>
	* 
  *
  * @param salnam 賣方姓名
	*/
  public void setSalnam(String salnam) {
    this.salnam = salnam;
  }

/**
	* 賣方ID<br>
	* 
	* @return String
	*/
  public String getSalid1() {
    return this.salid1 == null ? "" : this.salid1;
  }

/**
	* 賣方ID<br>
	* 
  *
  * @param salid1 賣方ID
	*/
  public void setSalid1(String salid1) {
    this.salid1 = salid1;
  }

/**
	* 停車位形式<br>
	* 
	* @return String
	*/
  public String getHgtcap() {
    return this.hgtcap == null ? "" : this.hgtcap;
  }

/**
	* 停車位形式<br>
	* 
  *
  * @param hgtcap 停車位形式
	*/
  public void setHgtcap(String hgtcap) {
    this.hgtcap = hgtcap;
  }

/**
	* 主要用途<br>
	* 
	* @return String
	*/
  public String getHgtgus() {
    return this.hgtgus == null ? "" : this.hgtgus;
  }

/**
	* 主要用途<br>
	* 
  *
  * @param hgtgus 主要用途
	*/
  public void setHgtgus(String hgtgus) {
    this.hgtgus = hgtgus;
  }

/**
	* 附屬建物用途<br>
	* 
	* @return String
	*/
  public String getHgtaus() {
    return this.hgtaus == null ? "" : this.hgtaus;
  }

/**
	* 附屬建物用途<br>
	* 
  *
  * @param hgtaus 附屬建物用途
	*/
  public void setHgtaus(String hgtaus) {
    this.hgtaus = hgtaus;
  }

/**
	* 所在樓層<br>
	* 
	* @return String
	*/
  public String getHgtfor() {
    return this.hgtfor == null ? "" : this.hgtfor;
  }

/**
	* 所在樓層<br>
	* 
  *
  * @param hgtfor 所在樓層
	*/
  public void setHgtfor(String hgtfor) {
    this.hgtfor = hgtfor;
  }

/**
	* 建築完成日<br>
	* 
	* @return Integer
	*/
  public int getHgtcpe() {
    return this.hgtcpe;
  }

/**
	* 建築完成日<br>
	* 
  *
  * @param hgtcpe 建築完成日
	*/
  public void setHgtcpe(int hgtcpe) {
    this.hgtcpe = hgtcpe;
  }

/**
	* 附屬建物(坪)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getHgtads() {
    return this.hgtads;
  }

/**
	* 附屬建物(坪)<br>
	* 
  *
  * @param hgtads 附屬建物(坪)
	*/
  public void setHgtads(BigDecimal hgtads) {
    this.hgtads = hgtads;
  }

/**
	* 縣市名稱<br>
	* 
	* @return String
	*/
  public String getHgtad1() {
    return this.hgtad1 == null ? "" : this.hgtad1;
  }

/**
	* 縣市名稱<br>
	* 
  *
  * @param hgtad1 縣市名稱
	*/
  public void setHgtad1(String hgtad1) {
    this.hgtad1 = hgtad1;
  }

/**
	* 鄉鎮市區名稱<br>
	* 
	* @return String
	*/
  public String getHgtad2() {
    return this.hgtad2 == null ? "" : this.hgtad2;
  }

/**
	* 鄉鎮市區名稱<br>
	* 
  *
  * @param hgtad2 鄉鎮市區名稱
	*/
  public void setHgtad2(String hgtad2) {
    this.hgtad2 = hgtad2;
  }

/**
	* 街路巷弄<br>
	* 
	* @return String
	*/
  public String getHgtad3() {
    return this.hgtad3 == null ? "" : this.hgtad3;
  }

/**
	* 街路巷弄<br>
	* 
  *
  * @param hgtad3 街路巷弄
	*/
  public void setHgtad3(String hgtad3) {
    this.hgtad3 = hgtad3;
  }

/**
	* 房屋所有權取得日<br>
	* 
	* @return Integer
	*/
  public int getHgtgtd() {
    return this.hgtgtd;
  }

/**
	* 房屋所有權取得日<br>
	* 
  *
  * @param hgtgtd 房屋所有權取得日
	*/
  public void setHgtgtd(int hgtgtd) {
    this.hgtgtd = hgtgtd;
  }

/**
	* 買賣契約價格<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getBuyamt() {
    return this.buyamt;
  }

/**
	* 買賣契約價格<br>
	* 
  *
  * @param buyamt 買賣契約價格
	*/
  public void setBuyamt(BigDecimal buyamt) {
    this.buyamt = buyamt;
  }

/**
	* 買賣契約日期<br>
	* 
	* @return Integer
	*/
  public int getBuydat() {
    return this.buydat;
  }

/**
	* 買賣契約日期<br>
	* 
  *
  * @param buydat 買賣契約日期
	*/
  public void setBuydat(int buydat) {
    this.buydat = buydat;
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
	* 主建物建號2<br>
	* 
	* @return Integer
	*/
  public int getHgtmhn2() {
    return this.hgtmhn2;
  }

/**
	* 主建物建號2<br>
	* 
  *
  * @param hgtmhn2 主建物建號2
	*/
  public void setHgtmhn2(int hgtmhn2) {
    this.hgtmhn2 = hgtmhn2;
  }

/**
	* 獨立產權註記<br>
	* 
	* @return String
	*/
  public String getHgtcip() {
    return this.hgtcip == null ? "" : this.hgtcip;
  }

/**
	* 獨立產權註記<br>
	* 
  *
  * @param hgtcip 獨立產權註記
	*/
  public void setHgtcip(String hgtcip) {
    this.hgtcip = hgtcip;
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
    return "Lahgtp [lahgtpId=" + lahgtpId + ", lgtcif=" + lgtcif
           + ", lgtadr=" + lgtadr + ", hgtmhn=" + hgtmhn + ", hgtmhs=" + hgtmhs + ", hgtpsm=" + hgtpsm + ", hgtcam=" + hgtcam + ", lgtiid=" + lgtiid
           + ", lgtunt=" + lgtunt + ", lgtiam=" + lgtiam + ", lgtsam=" + lgtsam + ", lgtsat=" + lgtsat + ", grtsts=" + grtsts + ", hgtstr=" + hgtstr
           + ", hgtcdt=" + hgtcdt + ", hgtflr=" + hgtflr + ", hgtrof=" + hgtrof + ", salnam=" + salnam + ", salid1=" + salid1 + ", hgtcap=" + hgtcap
           + ", hgtgus=" + hgtgus + ", hgtaus=" + hgtaus + ", hgtfor=" + hgtfor + ", hgtcpe=" + hgtcpe + ", hgtads=" + hgtads + ", hgtad1=" + hgtad1
           + ", hgtad2=" + hgtad2 + ", hgtad3=" + hgtad3 + ", hgtgtd=" + hgtgtd + ", buyamt=" + buyamt + ", buydat=" + buydat + ", gdrnum2=" + gdrnum2
           + ", gdrmrk=" + gdrmrk + ", hgtmhn2=" + hgtmhn2 + ", hgtcip=" + hgtcip + ", updateIdent=" + updateIdent + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
