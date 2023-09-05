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
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * PfItDetail 介紹人業績明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`PfItDetail`")
public class PfItDetail implements Serializable {


  // 序號
  @Id
  @Column(name = "`LogNo`")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "`PfItDetail_SEQ`")
  @SequenceGenerator(name = "`PfItDetail_SEQ`", sequenceName = "`PfItDetail_SEQ`", allocationSize = 1)
  private Long logNo = 0L;

  // 業績日期
  /* 業績日期=系統營業日1.撥貸(計件代碼變更)，為撥款日期；計件代碼變更不可跨工作月2.部分償還、提前結案，為會計日3.保費檢核追回，為該工作月的業績止日 */
  @Column(name = "`PerfDate`")
  private int perfDate = 0;

  // 戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 撥款序號
  @Column(name = "`BormNo`")
  private int bormNo = 0;

  // 還款類別
  /* 0:撥款(計件代碼變更)2:部分償還3:提前結案4:人工維護(跨工作月新增)5:保費檢核追回 */
  @Column(name = "`RepayType`")
  private int repayType = 0;

  // 撥款日
  @Column(name = "`DrawdownDate`")
  private int drawdownDate = 0;

  // 商品代碼
  /* FacMain.ProdNo 商品代碼 */
  @Column(name = "`ProdCode`", length = 5)
  private String prodCode;

  // 計件代碼
  @Column(name = "`PieceCode`", length = 1)
  private String pieceCode;

  // 是否計件
  /* Y/N，追回時為扣除追回後重算後之是否計件 */
  @Column(name = "`CntingCode`", length = 1)
  private String cntingCode;

  // 撥款金額/追回金額
  /* 撥款為正值，部分償還、提前結案為負值，計件代碼變更、保費檢核追回為零 */
  @Column(name = "`DrawdownAmt`")
  private BigDecimal drawdownAmt = new BigDecimal("0");

  // 單位代號
  /* CdEmp.CenterCode 單位代號 */
  @Column(name = "`UnitCode`", length = 6)
  private String unitCode;

  // 區部代號
  /* CdBcm.District 區部 */
  @Column(name = "`DistCode`", length = 6)
  private String distCode;

  // 部室代號
  /* CdBcm.DeptCode 部室代號 */
  @Column(name = "`DeptCode`", length = 6)
  private String deptCode;

  // 介紹人
  /* FacMain.Introducer 介紹人 */
  @Column(name = "`Introducer`", length = 8)
  private String introducer;

  // 處經理代號
  /* CdBcm.UnitManager 處經理代號 */
  @Column(name = "`UnitManager`", length = 8)
  private String unitManager;

  // 區經理代號
  /* CdBcm.DistManager 區經理代號 */
  @Column(name = "`DistManager`", length = 8)
  private String distManager;

  // 部經理代號
  /* CdBcm.DeptManager 部經理代號 */
  @Column(name = "`DeptManager`", length = 8)
  private String deptManager;

  // 件數
  /* 介紹人-件數不會有小數點，追回時為扣除追回後重算後之筆數[是否計件]為Y,件數為1;若為N,件數為0 */
  @Column(name = "`PerfCnt`")
  private BigDecimal perfCnt = new BigDecimal("0");

  // 換算業績
  /* 調整後金額 */
  @Column(name = "`PerfEqAmt`")
  private BigDecimal perfEqAmt = new BigDecimal("0");

  // 業務報酬
  /* 調整後金額 */
  @Column(name = "`PerfReward`")
  private BigDecimal perfReward = new BigDecimal("0");

  // 業績金額
  /* 調整後金額 */
  @Column(name = "`PerfAmt`")
  private BigDecimal perfAmt = new BigDecimal("0");

  // 工作月
  @Column(name = "`WorkMonth`")
  private int workMonth = 0;

  // 工作季
  @Column(name = "`WorkSeason`")
  private int workSeason = 0;

  // 保費檢核日
  /* 保費檢核時放入 */
  @Column(name = "`RewardDate`")
  private int rewardDate = 0;

  // 產出媒體檔日期
  @Column(name = "`MediaDate`")
  private int mediaDate = 0;

  // 產出媒體檔記號
  /* 3.保費檢核結果為Y時已追回撥款，還款不用追回(不產媒體) */
  @Column(name = "`MediaFg`")
  private int mediaFg = 0;

  // 調整記號
  /* 0:未調整(BS996業績重算時,只重算未調整)1:同工作月調整   2:跨工作月調整(新增) */
  @Column(name = "`AdjRange`")
  private int adjRange = 0;

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
	* 序號<br>
	* 
	* @return Long
	*/
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long getLogNo() {
    return this.logNo;
  }

/**
	* 序號<br>
	* 
  *
  * @param logNo 序號
	*/
  public void setLogNo(Long logNo) {
    this.logNo = logNo;
  }

/**
	* 業績日期<br>
	* 業績日期=系統營業日
1.撥貸(計件代碼變更)，為撥款日期；計件代碼變更不可跨工作月
2.部分償還、提前結案，為會計日
3.保費檢核追回，為該工作月的業績止日
	* @return Integer
	*/
  public int getPerfDate() {
    return StaticTool.bcToRoc(this.perfDate);
  }

/**
	* 業績日期<br>
	* 業績日期=系統營業日
1.撥貸(計件代碼變更)，為撥款日期；計件代碼變更不可跨工作月
2.部分償還、提前結案，為會計日
3.保費檢核追回，為該工作月的業績止日
  *
  * @param perfDate 業績日期
  * @throws LogicException when Date Is Warn	*/
  public void setPerfDate(int perfDate) throws LogicException {
    this.perfDate = StaticTool.rocToBc(perfDate);
  }

/**
	* 戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 戶號<br>
	* 
  *
  * @param custNo 戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 額度編號<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度編號<br>
	* 
  *
  * @param facmNo 額度編號
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 撥款序號<br>
	* 
	* @return Integer
	*/
  public int getBormNo() {
    return this.bormNo;
  }

/**
	* 撥款序號<br>
	* 
  *
  * @param bormNo 撥款序號
	*/
  public void setBormNo(int bormNo) {
    this.bormNo = bormNo;
  }

/**
	* 還款類別<br>
	* 0:撥款(計件代碼變更)
2:部分償還
3:提前結案
4:人工維護(跨工作月新增)
5:保費檢核追回
	* @return Integer
	*/
  public int getRepayType() {
    return this.repayType;
  }

/**
	* 還款類別<br>
	* 0:撥款(計件代碼變更)
2:部分償還
3:提前結案
4:人工維護(跨工作月新增)
5:保費檢核追回
  *
  * @param repayType 還款類別
	*/
  public void setRepayType(int repayType) {
    this.repayType = repayType;
  }

/**
	* 撥款日<br>
	* 
	* @return Integer
	*/
  public int getDrawdownDate() {
    return StaticTool.bcToRoc(this.drawdownDate);
  }

/**
	* 撥款日<br>
	* 
  *
  * @param drawdownDate 撥款日
  * @throws LogicException when Date Is Warn	*/
  public void setDrawdownDate(int drawdownDate) throws LogicException {
    this.drawdownDate = StaticTool.rocToBc(drawdownDate);
  }

/**
	* 商品代碼<br>
	* FacMain.ProdNo 商品代碼
	* @return String
	*/
  public String getProdCode() {
    return this.prodCode == null ? "" : this.prodCode;
  }

/**
	* 商品代碼<br>
	* FacMain.ProdNo 商品代碼
  *
  * @param prodCode 商品代碼
	*/
  public void setProdCode(String prodCode) {
    this.prodCode = prodCode;
  }

/**
	* 計件代碼<br>
	* 
	* @return String
	*/
  public String getPieceCode() {
    return this.pieceCode == null ? "" : this.pieceCode;
  }

/**
	* 計件代碼<br>
	* 
  *
  * @param pieceCode 計件代碼
	*/
  public void setPieceCode(String pieceCode) {
    this.pieceCode = pieceCode;
  }

/**
	* 是否計件<br>
	* Y/N，追回時為扣除追回後重算後之是否計件
	* @return String
	*/
  public String getCntingCode() {
    return this.cntingCode == null ? "" : this.cntingCode;
  }

/**
	* 是否計件<br>
	* Y/N，追回時為扣除追回後重算後之是否計件
  *
  * @param cntingCode 是否計件
	*/
  public void setCntingCode(String cntingCode) {
    this.cntingCode = cntingCode;
  }

/**
	* 撥款金額/追回金額<br>
	* 撥款為正值，部分償還、提前結案為負值，計件代碼變更、保費檢核追回為零
	* @return BigDecimal
	*/
  public BigDecimal getDrawdownAmt() {
    return this.drawdownAmt;
  }

/**
	* 撥款金額/追回金額<br>
	* 撥款為正值，部分償還、提前結案為負值，計件代碼變更、保費檢核追回為零
  *
  * @param drawdownAmt 撥款金額/追回金額
	*/
  public void setDrawdownAmt(BigDecimal drawdownAmt) {
    this.drawdownAmt = drawdownAmt;
  }

/**
	* 單位代號<br>
	* CdEmp.CenterCode 單位代號
	* @return String
	*/
  public String getUnitCode() {
    return this.unitCode == null ? "" : this.unitCode;
  }

/**
	* 單位代號<br>
	* CdEmp.CenterCode 單位代號
  *
  * @param unitCode 單位代號
	*/
  public void setUnitCode(String unitCode) {
    this.unitCode = unitCode;
  }

/**
	* 區部代號<br>
	* CdBcm.District 區部
	* @return String
	*/
  public String getDistCode() {
    return this.distCode == null ? "" : this.distCode;
  }

/**
	* 區部代號<br>
	* CdBcm.District 區部
  *
  * @param distCode 區部代號
	*/
  public void setDistCode(String distCode) {
    this.distCode = distCode;
  }

/**
	* 部室代號<br>
	* CdBcm.DeptCode 部室代號
	* @return String
	*/
  public String getDeptCode() {
    return this.deptCode == null ? "" : this.deptCode;
  }

/**
	* 部室代號<br>
	* CdBcm.DeptCode 部室代號
  *
  * @param deptCode 部室代號
	*/
  public void setDeptCode(String deptCode) {
    this.deptCode = deptCode;
  }

/**
	* 介紹人<br>
	* FacMain.Introducer 介紹人
	* @return String
	*/
  public String getIntroducer() {
    return this.introducer == null ? "" : this.introducer;
  }

/**
	* 介紹人<br>
	* FacMain.Introducer 介紹人
  *
  * @param introducer 介紹人
	*/
  public void setIntroducer(String introducer) {
    this.introducer = introducer;
  }

/**
	* 處經理代號<br>
	* CdBcm.UnitManager 處經理代號
	* @return String
	*/
  public String getUnitManager() {
    return this.unitManager == null ? "" : this.unitManager;
  }

/**
	* 處經理代號<br>
	* CdBcm.UnitManager 處經理代號
  *
  * @param unitManager 處經理代號
	*/
  public void setUnitManager(String unitManager) {
    this.unitManager = unitManager;
  }

/**
	* 區經理代號<br>
	* CdBcm.DistManager 區經理代號
	* @return String
	*/
  public String getDistManager() {
    return this.distManager == null ? "" : this.distManager;
  }

/**
	* 區經理代號<br>
	* CdBcm.DistManager 區經理代號
  *
  * @param distManager 區經理代號
	*/
  public void setDistManager(String distManager) {
    this.distManager = distManager;
  }

/**
	* 部經理代號<br>
	* CdBcm.DeptManager 部經理代號
	* @return String
	*/
  public String getDeptManager() {
    return this.deptManager == null ? "" : this.deptManager;
  }

/**
	* 部經理代號<br>
	* CdBcm.DeptManager 部經理代號
  *
  * @param deptManager 部經理代號
	*/
  public void setDeptManager(String deptManager) {
    this.deptManager = deptManager;
  }

/**
	* 件數<br>
	* 介紹人-件數不會有小數點，追回時為扣除追回後重算後之筆數
[是否計件]為Y,件數為1;若為N,件數為0
	* @return BigDecimal
	*/
  public BigDecimal getPerfCnt() {
    return this.perfCnt;
  }

/**
	* 件數<br>
	* 介紹人-件數不會有小數點，追回時為扣除追回後重算後之筆數
[是否計件]為Y,件數為1;若為N,件數為0
  *
  * @param perfCnt 件數
	*/
  public void setPerfCnt(BigDecimal perfCnt) {
    this.perfCnt = perfCnt;
  }

/**
	* 換算業績<br>
	* 調整後金額
	* @return BigDecimal
	*/
  public BigDecimal getPerfEqAmt() {
    return this.perfEqAmt;
  }

/**
	* 換算業績<br>
	* 調整後金額
  *
  * @param perfEqAmt 換算業績
	*/
  public void setPerfEqAmt(BigDecimal perfEqAmt) {
    this.perfEqAmt = perfEqAmt;
  }

/**
	* 業務報酬<br>
	* 調整後金額
	* @return BigDecimal
	*/
  public BigDecimal getPerfReward() {
    return this.perfReward;
  }

/**
	* 業務報酬<br>
	* 調整後金額
  *
  * @param perfReward 業務報酬
	*/
  public void setPerfReward(BigDecimal perfReward) {
    this.perfReward = perfReward;
  }

/**
	* 業績金額<br>
	* 調整後金額
	* @return BigDecimal
	*/
  public BigDecimal getPerfAmt() {
    return this.perfAmt;
  }

/**
	* 業績金額<br>
	* 調整後金額
  *
  * @param perfAmt 業績金額
	*/
  public void setPerfAmt(BigDecimal perfAmt) {
    this.perfAmt = perfAmt;
  }

/**
	* 工作月<br>
	* 
	* @return Integer
	*/
  public int getWorkMonth() {
    return this.workMonth;
  }

/**
	* 工作月<br>
	* 
  *
  * @param workMonth 工作月
	*/
  public void setWorkMonth(int workMonth) {
    this.workMonth = workMonth;
  }

/**
	* 工作季<br>
	* 
	* @return Integer
	*/
  public int getWorkSeason() {
    return this.workSeason;
  }

/**
	* 工作季<br>
	* 
  *
  * @param workSeason 工作季
	*/
  public void setWorkSeason(int workSeason) {
    this.workSeason = workSeason;
  }

/**
	* 保費檢核日<br>
	* 保費檢核時放入
	* @return Integer
	*/
  public int getRewardDate() {
    return StaticTool.bcToRoc(this.rewardDate);
  }

/**
	* 保費檢核日<br>
	* 保費檢核時放入
  *
  * @param rewardDate 保費檢核日
  * @throws LogicException when Date Is Warn	*/
  public void setRewardDate(int rewardDate) throws LogicException {
    this.rewardDate = StaticTool.rocToBc(rewardDate);
  }

/**
	* 產出媒體檔日期<br>
	* 
	* @return Integer
	*/
  public int getMediaDate() {
    return StaticTool.bcToRoc(this.mediaDate);
  }

/**
	* 產出媒體檔日期<br>
	* 
  *
  * @param mediaDate 產出媒體檔日期
  * @throws LogicException when Date Is Warn	*/
  public void setMediaDate(int mediaDate) throws LogicException {
    this.mediaDate = StaticTool.rocToBc(mediaDate);
  }

/**
	* 產出媒體檔記號<br>
	* 3.保費檢核結果為Y時已追回撥款，還款不用追回(不產媒體)
	* @return Integer
	*/
  public int getMediaFg() {
    return this.mediaFg;
  }

/**
	* 產出媒體檔記號<br>
	* 3.保費檢核結果為Y時已追回撥款，還款不用追回(不產媒體)
  *
  * @param mediaFg 產出媒體檔記號
	*/
  public void setMediaFg(int mediaFg) {
    this.mediaFg = mediaFg;
  }

/**
	* 調整記號<br>
	* 0:未調整(BS996業績重算時,只重算未調整)
1:同工作月調整   
2:跨工作月調整(新增)
	* @return Integer
	*/
  public int getAdjRange() {
    return this.adjRange;
  }

/**
	* 調整記號<br>
	* 0:未調整(BS996業績重算時,只重算未調整)
1:同工作月調整   
2:跨工作月調整(新增)
  *
  * @param adjRange 調整記號
	*/
  public void setAdjRange(int adjRange) {
    this.adjRange = adjRange;
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
    return "PfItDetail [logNo=" + logNo + ", perfDate=" + perfDate + ", custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + ", repayType=" + repayType
           + ", drawdownDate=" + drawdownDate + ", prodCode=" + prodCode + ", pieceCode=" + pieceCode + ", cntingCode=" + cntingCode + ", drawdownAmt=" + drawdownAmt + ", unitCode=" + unitCode
           + ", distCode=" + distCode + ", deptCode=" + deptCode + ", introducer=" + introducer + ", unitManager=" + unitManager + ", distManager=" + distManager + ", deptManager=" + deptManager
           + ", perfCnt=" + perfCnt + ", perfEqAmt=" + perfEqAmt + ", perfReward=" + perfReward + ", perfAmt=" + perfAmt + ", workMonth=" + workMonth + ", workSeason=" + workSeason
           + ", rewardDate=" + rewardDate + ", mediaDate=" + mediaDate + ", mediaFg=" + mediaFg + ", adjRange=" + adjRange + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
