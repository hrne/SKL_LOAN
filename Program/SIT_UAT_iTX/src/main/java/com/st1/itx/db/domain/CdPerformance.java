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
 * CdPerformance 業績件數及金額核算標準設定檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdPerformance`")
public class CdPerformance implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 9011543980368024209L;

@EmbeddedId
  private CdPerformanceId cdPerformanceId;

  // 工作年月
  /* 2021/4/12 新增 */
  @Column(name = "`WorkMonth`", insertable = false, updatable = false)
  private int workMonth = 0;

  // 計件代碼
  /* ~~~(綁約2年以下或未綁約)~~~A: 新貸件B: 新貸件(同押品,數額度之額度一以外)C: 原額度內—動支件D: 新增額度—新貸件(指有增加設定抵押權者)E: 展期件~~~(有綁約2年(含)以上)~~~1: 新貸件2: 新貸件(同押品,數額度之額度一以外)3: 原額度內—動支件4: 新增額度—新貸件(指有增加設定抵押權者)5: 展期件~~~(無關綁約)~~~6: 原額度內—6個月內動支件(還款後6個月內再動支者)7: 服務件8: 特殊件9: 固特利契轉 */
  @Column(name = "`PieceCode`", length = 1, insertable = false, updatable = false)
  private String pieceCode;

  // 介紹單位_件數
  /* 0=不計件數例: 1=1件 , 2=2件 */
  @Column(name = "`UnitCnt`")
  private BigDecimal unitCnt = new BigDecimal("0");

  // 介紹單位_計件金額門檻
  /* 介紹單位_件數&amp;gt;0時有值例: 600000=1件(60萬以上) */
  @Column(name = "`UnitAmtCond`")
  private BigDecimal unitAmtCond = new BigDecimal("0");

  // 介紹單位_撥款業績_比例
  /* 輸入比例例: 0=無 , 1=全部計算 */
  @Column(name = "`UnitPercent`")
  private BigDecimal unitPercent = new BigDecimal("0");

  // 介紹人_介紹獎金_比例
  /* 輸入比例例: 0=無 , 0.001=1/1000*撥款金額 */
  @Column(name = "`IntrodPerccent`")
  private BigDecimal introdPerccent = new BigDecimal("0");

  // 介紹人_介紹獎金_門檻(新增額度)
  /* 例: 0=無限制 , 500000=撥款金額達50萬以上者 */
  @Column(name = "`IntrodAmtCond`")
  private BigDecimal introdAmtCond = new BigDecimal("0");

  // 介紹人_換算業績率(分母)
  /* 例: 0=無 , 10000=以每一萬元計算獎金金額 */
  @Column(name = "`IntrodPfEqBase`")
  private BigDecimal introdPfEqBase = new BigDecimal("0");

  // 介紹人_換算業績率(分子)
  /* 介紹人_換算業績金額基準=0時必須為0例: 0=無 , 35=以每一萬元計算35元業績獎金 */
  @Column(name = "`IntrodPfEqAmt`")
  private BigDecimal introdPfEqAmt = new BigDecimal("0");

  // 介紹人_業務報酬率(分母)
  /* 例: 0=無 , 10000=以每一萬元計算獎金金額 */
  @Column(name = "`IntrodRewardBase`")
  private BigDecimal introdRewardBase = new BigDecimal("0");

  // 介紹人_業務報酬率(分子)
  /* 介紹人_二階(或三階承攬)業務報酬_金額基準=0時必須為0例: 0=無 , 12.5=以每一萬元計算12.5元業績獎金 */
  @Column(name = "`IntrodReward`")
  private BigDecimal introdReward = new BigDecimal("0");

  // 房貸專員_件數
  /* 0=不計件數例: 1=1件 , 0.1=0.1件 */
  @Column(name = "`BsOffrCnt`")
  private BigDecimal bsOffrCnt = new BigDecimal("0");

  // 房貸專員_件數上限
  /* 0=無上限例: 1=最高1件 */
  @Column(name = "`BsOffrCntLimit`")
  private BigDecimal bsOffrCntLimit = new BigDecimal("0");

  // 房貸專員_計件金額門檻
  /* 例: 0=無限制 , 600000=1件(60萬以上) */
  @Column(name = "`BsOffrAmtCond`")
  private BigDecimal bsOffrAmtCond = new BigDecimal("0");

  // 房貸專員_計件金額門檻(動支、增貸)
  /* 房貸專員_件數&amp;gt;0時有值例: 100000=10萬計0.1件,最高1件 */
  @Column(name = "`BsOffrCntAmt`")
  private BigDecimal bsOffrCntAmt = new BigDecimal("0");

  // 房貸專員_撥款業績_比例
  /* 輸入比例例: 0=無 , 1=全部計算 , 0.5=撥款金額*1/2 */
  @Column(name = "`BsOffrPerccent`")
  private BigDecimal bsOffrPerccent = new BigDecimal("0");

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


  public CdPerformanceId getCdPerformanceId() {
    return this.cdPerformanceId;
  }

  public void setCdPerformanceId(CdPerformanceId cdPerformanceId) {
    this.cdPerformanceId = cdPerformanceId;
  }

/**
	* 工作年月<br>
	* 2021/4/12 新增
	* @return Integer
	*/
  public int getWorkMonth() {
    return this.workMonth;
  }

/**
	* 工作年月<br>
	* 2021/4/12 新增
  *
  * @param workMonth 工作年月
	*/
  public void setWorkMonth(int workMonth) {
    this.workMonth = workMonth;
  }

/**
	* 計件代碼<br>
	* ~~~(綁約2年以下或未綁約)~~~
A: 新貸件
B: 新貸件(同押品,數額度之額度一以外)
C: 原額度內—動支件
D: 新增額度—新貸件(指有增加設定抵押權者)
E: 展期件
~~~(有綁約2年(含)以上)~~~
1: 新貸件
2: 新貸件(同押品,數額度之額度一以外)
3: 原額度內—動支件
4: 新增額度—新貸件(指有增加設定抵押權者)
5: 展期件
~~~(無關綁約)~~~
6: 原額度內—6個月內動支件(還款後6個月內再動支者)
7: 服務件
8: 特殊件
9: 固特利契轉
	* @return String
	*/
  public String getPieceCode() {
    return this.pieceCode == null ? "" : this.pieceCode;
  }

/**
	* 計件代碼<br>
	* ~~~(綁約2年以下或未綁約)~~~
A: 新貸件
B: 新貸件(同押品,數額度之額度一以外)
C: 原額度內—動支件
D: 新增額度—新貸件(指有增加設定抵押權者)
E: 展期件
~~~(有綁約2年(含)以上)~~~
1: 新貸件
2: 新貸件(同押品,數額度之額度一以外)
3: 原額度內—動支件
4: 新增額度—新貸件(指有增加設定抵押權者)
5: 展期件
~~~(無關綁約)~~~
6: 原額度內—6個月內動支件(還款後6個月內再動支者)
7: 服務件
8: 特殊件
9: 固特利契轉
  *
  * @param pieceCode 計件代碼
	*/
  public void setPieceCode(String pieceCode) {
    this.pieceCode = pieceCode;
  }

/**
	* 介紹單位_件數<br>
	* 0=不計件數
例: 1=1件 , 2=2件
	* @return BigDecimal
	*/
  public BigDecimal getUnitCnt() {
    return this.unitCnt;
  }

/**
	* 介紹單位_件數<br>
	* 0=不計件數
例: 1=1件 , 2=2件
  *
  * @param unitCnt 介紹單位_件數
	*/
  public void setUnitCnt(BigDecimal unitCnt) {
    this.unitCnt = unitCnt;
  }

/**
	* 介紹單位_計件金額門檻<br>
	* 介紹單位_件數&amp;gt;0時有值
例: 600000=1件(60萬以上)
	* @return BigDecimal
	*/
  public BigDecimal getUnitAmtCond() {
    return this.unitAmtCond;
  }

/**
	* 介紹單位_計件金額門檻<br>
	* 介紹單位_件數&amp;gt;0時有值
例: 600000=1件(60萬以上)
  *
  * @param unitAmtCond 介紹單位_計件金額門檻
	*/
  public void setUnitAmtCond(BigDecimal unitAmtCond) {
    this.unitAmtCond = unitAmtCond;
  }

/**
	* 介紹單位_撥款業績_比例<br>
	* 輸入比例
例: 0=無 , 1=全部計算
	* @return BigDecimal
	*/
  public BigDecimal getUnitPercent() {
    return this.unitPercent;
  }

/**
	* 介紹單位_撥款業績_比例<br>
	* 輸入比例
例: 0=無 , 1=全部計算
  *
  * @param unitPercent 介紹單位_撥款業績_比例
	*/
  public void setUnitPercent(BigDecimal unitPercent) {
    this.unitPercent = unitPercent;
  }

/**
	* 介紹人_介紹獎金_比例<br>
	* 輸入比例
例: 0=無 , 0.001=1/1000*撥款金額
	* @return BigDecimal
	*/
  public BigDecimal getIntrodPerccent() {
    return this.introdPerccent;
  }

/**
	* 介紹人_介紹獎金_比例<br>
	* 輸入比例
例: 0=無 , 0.001=1/1000*撥款金額
  *
  * @param introdPerccent 介紹人_介紹獎金_比例
	*/
  public void setIntrodPerccent(BigDecimal introdPerccent) {
    this.introdPerccent = introdPerccent;
  }

/**
	* 介紹人_介紹獎金_門檻(新增額度)<br>
	* 例: 0=無限制 , 500000=撥款金額達50萬以上者
	* @return BigDecimal
	*/
  public BigDecimal getIntrodAmtCond() {
    return this.introdAmtCond;
  }

/**
	* 介紹人_介紹獎金_門檻(新增額度)<br>
	* 例: 0=無限制 , 500000=撥款金額達50萬以上者
  *
  * @param introdAmtCond 介紹人_介紹獎金_門檻(新增額度)
	*/
  public void setIntrodAmtCond(BigDecimal introdAmtCond) {
    this.introdAmtCond = introdAmtCond;
  }

/**
	* 介紹人_換算業績率(分母)<br>
	* 例: 0=無 , 10000=以每一萬元計算獎金金額
	* @return BigDecimal
	*/
  public BigDecimal getIntrodPfEqBase() {
    return this.introdPfEqBase;
  }

/**
	* 介紹人_換算業績率(分母)<br>
	* 例: 0=無 , 10000=以每一萬元計算獎金金額
  *
  * @param introdPfEqBase 介紹人_換算業績率(分母)
	*/
  public void setIntrodPfEqBase(BigDecimal introdPfEqBase) {
    this.introdPfEqBase = introdPfEqBase;
  }

/**
	* 介紹人_換算業績率(分子)<br>
	* 介紹人_換算業績金額基準=0時必須為0
例: 0=無 , 35=以每一萬元計算35元業績獎金
	* @return BigDecimal
	*/
  public BigDecimal getIntrodPfEqAmt() {
    return this.introdPfEqAmt;
  }

/**
	* 介紹人_換算業績率(分子)<br>
	* 介紹人_換算業績金額基準=0時必須為0
例: 0=無 , 35=以每一萬元計算35元業績獎金
  *
  * @param introdPfEqAmt 介紹人_換算業績率(分子)
	*/
  public void setIntrodPfEqAmt(BigDecimal introdPfEqAmt) {
    this.introdPfEqAmt = introdPfEqAmt;
  }

/**
	* 介紹人_業務報酬率(分母)<br>
	* 例: 0=無 , 10000=以每一萬元計算獎金金額
	* @return BigDecimal
	*/
  public BigDecimal getIntrodRewardBase() {
    return this.introdRewardBase;
  }

/**
	* 介紹人_業務報酬率(分母)<br>
	* 例: 0=無 , 10000=以每一萬元計算獎金金額
  *
  * @param introdRewardBase 介紹人_業務報酬率(分母)
	*/
  public void setIntrodRewardBase(BigDecimal introdRewardBase) {
    this.introdRewardBase = introdRewardBase;
  }

/**
	* 介紹人_業務報酬率(分子)<br>
	* 介紹人_二階(或三階承攬)業務報酬_金額基準=0時必須為0
例: 0=無 , 12.5=以每一萬元計算12.5元業績獎金
	* @return BigDecimal
	*/
  public BigDecimal getIntrodReward() {
    return this.introdReward;
  }

/**
	* 介紹人_業務報酬率(分子)<br>
	* 介紹人_二階(或三階承攬)業務報酬_金額基準=0時必須為0
例: 0=無 , 12.5=以每一萬元計算12.5元業績獎金
  *
  * @param introdReward 介紹人_業務報酬率(分子)
	*/
  public void setIntrodReward(BigDecimal introdReward) {
    this.introdReward = introdReward;
  }

/**
	* 房貸專員_件數<br>
	* 0=不計件數
例: 1=1件 , 0.1=0.1件
	* @return BigDecimal
	*/
  public BigDecimal getBsOffrCnt() {
    return this.bsOffrCnt;
  }

/**
	* 房貸專員_件數<br>
	* 0=不計件數
例: 1=1件 , 0.1=0.1件
  *
  * @param bsOffrCnt 房貸專員_件數
	*/
  public void setBsOffrCnt(BigDecimal bsOffrCnt) {
    this.bsOffrCnt = bsOffrCnt;
  }

/**
	* 房貸專員_件數上限<br>
	* 0=無上限
例: 1=最高1件
	* @return BigDecimal
	*/
  public BigDecimal getBsOffrCntLimit() {
    return this.bsOffrCntLimit;
  }

/**
	* 房貸專員_件數上限<br>
	* 0=無上限
例: 1=最高1件
  *
  * @param bsOffrCntLimit 房貸專員_件數上限
	*/
  public void setBsOffrCntLimit(BigDecimal bsOffrCntLimit) {
    this.bsOffrCntLimit = bsOffrCntLimit;
  }

/**
	* 房貸專員_計件金額門檻<br>
	* 例: 0=無限制 , 600000=1件(60萬以上)
	* @return BigDecimal
	*/
  public BigDecimal getBsOffrAmtCond() {
    return this.bsOffrAmtCond;
  }

/**
	* 房貸專員_計件金額門檻<br>
	* 例: 0=無限制 , 600000=1件(60萬以上)
  *
  * @param bsOffrAmtCond 房貸專員_計件金額門檻
	*/
  public void setBsOffrAmtCond(BigDecimal bsOffrAmtCond) {
    this.bsOffrAmtCond = bsOffrAmtCond;
  }

/**
	* 房貸專員_計件金額門檻(動支、增貸)<br>
	* 房貸專員_件數&amp;gt;0時有值
例: 100000=10萬計0.1件,最高1件
	* @return BigDecimal
	*/
  public BigDecimal getBsOffrCntAmt() {
    return this.bsOffrCntAmt;
  }

/**
	* 房貸專員_計件金額門檻(動支、增貸)<br>
	* 房貸專員_件數&amp;gt;0時有值
例: 100000=10萬計0.1件,最高1件
  *
  * @param bsOffrCntAmt 房貸專員_計件金額門檻(動支、增貸)
	*/
  public void setBsOffrCntAmt(BigDecimal bsOffrCntAmt) {
    this.bsOffrCntAmt = bsOffrCntAmt;
  }

/**
	* 房貸專員_撥款業績_比例<br>
	* 輸入比例
例: 0=無 , 1=全部計算 , 0.5=撥款金額*1/2
	* @return BigDecimal
	*/
  public BigDecimal getBsOffrPerccent() {
    return this.bsOffrPerccent;
  }

/**
	* 房貸專員_撥款業績_比例<br>
	* 輸入比例
例: 0=無 , 1=全部計算 , 0.5=撥款金額*1/2
  *
  * @param bsOffrPerccent 房貸專員_撥款業績_比例
	*/
  public void setBsOffrPerccent(BigDecimal bsOffrPerccent) {
    this.bsOffrPerccent = bsOffrPerccent;
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
    return "CdPerformance [cdPerformanceId=" + cdPerformanceId + ", unitCnt=" + unitCnt + ", unitAmtCond=" + unitAmtCond + ", unitPercent=" + unitPercent + ", introdPerccent=" + introdPerccent
           + ", introdAmtCond=" + introdAmtCond + ", introdPfEqBase=" + introdPfEqBase + ", introdPfEqAmt=" + introdPfEqAmt + ", introdRewardBase=" + introdRewardBase + ", introdReward=" + introdReward + ", bsOffrCnt=" + bsOffrCnt
           + ", bsOffrCntLimit=" + bsOffrCntLimit + ", bsOffrAmtCond=" + bsOffrAmtCond + ", bsOffrCntAmt=" + bsOffrCntAmt + ", bsOffrPerccent=" + bsOffrPerccent + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
