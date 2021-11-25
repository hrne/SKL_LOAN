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
 * PfReward 介紹、協辦獎金明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`PfReward`")
public class PfReward implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -2080162054487347593L;

// 序號
  @Id
  @Column(name = "`LogNo`")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "`PfReward_SEQ`")
  @SequenceGenerator(name = "`PfReward_SEQ`", sequenceName = "`PfReward_SEQ`", allocationSize = 1)
  private Long logNo = 0L;

  // 業績日期
  /* 1.撥貸(計件代碼變更)，為撥款日期2.部分償還、提前結案，為會計日 */
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
  /* 0.撥款(計件代碼變更) 2.部分償還 3.提前結案 */
  @Column(name = "`RepayType`")
  private int repayType = 0;

  // 計件代碼
  @Column(name = "`PieceCode`", length = 1)
  private String pieceCode;

  // 商品代碼
  /* FacMain.ProdNo 商品代碼 */
  @Column(name = "`ProdCode`", length = 5)
  private String prodCode;

  // 介紹人員編
  @Column(name = "`Introducer`", length = 6)
  private String introducer;

  // 協辦人員編
  @Column(name = "`Coorgnizer`", length = 6)
  private String coorgnizer;

  // 晤談一員編
  @Column(name = "`InterviewerA`", length = 6)
  private String interviewerA;

  // 晤談二員編
  @Column(name = "`InterviewerB`", length = 6)
  private String interviewerB;

  // 介紹人介紹獎金
  /* 薪碼:Q2 */
  @Column(name = "`IntroducerBonus`")
  private BigDecimal introducerBonus = new BigDecimal("0");

  // 介紹獎金轉檔日
  /* 暫不用 */
  @Column(name = "`IntroducerBonusDate`")
  private int introducerBonusDate = 0;

  // 介紹人加碼獎勵津貼
  /* 薪碼:Q1 */
  @Column(name = "`IntroducerAddBonus`")
  private BigDecimal introducerAddBonus = new BigDecimal("0");

  // 獎勵津貼轉檔日
  /* 暫不用 */
  @Column(name = "`IntroducerAddBonusDate`")
  private int introducerAddBonusDate = 0;

  // 協辦人員協辦獎金
  /* 薪碼:Q2 */
  @Column(name = "`CoorgnizerBonus`")
  private BigDecimal coorgnizerBonus = new BigDecimal("0");

  // 協辦獎金轉檔日
  /* 暫不用 */
  @Column(name = "`CoorgnizerBonusDate`")
  private int coorgnizerBonusDate = 0;

  // 工作月
  @Column(name = "`WorkMonth`")
  private int workMonth = 0;

  // 工作季
  @Column(name = "`WorkSeason`")
  private int workSeason = 0;

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
	* 1.撥貸(計件代碼變更)，為撥款日期
2.部分償還、提前結案，為會計日
	* @return Integer
	*/
  public int getPerfDate() {
    return StaticTool.bcToRoc(this.perfDate);
  }

/**
	* 業績日期<br>
	* 1.撥貸(計件代碼變更)，為撥款日期
2.部分償還、提前結案，為會計日
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
	* 0.撥款(計件代碼變更) 2.部分償還 3.提前結案
	* @return Integer
	*/
  public int getRepayType() {
    return this.repayType;
  }

/**
	* 還款類別<br>
	* 0.撥款(計件代碼變更) 2.部分償還 3.提前結案
  *
  * @param repayType 還款類別
	*/
  public void setRepayType(int repayType) {
    this.repayType = repayType;
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
	* 介紹人員編<br>
	* 
	* @return String
	*/
  public String getIntroducer() {
    return this.introducer == null ? "" : this.introducer;
  }

/**
	* 介紹人員編<br>
	* 
  *
  * @param introducer 介紹人員編
	*/
  public void setIntroducer(String introducer) {
    this.introducer = introducer;
  }

/**
	* 協辦人員編<br>
	* 
	* @return String
	*/
  public String getCoorgnizer() {
    return this.coorgnizer == null ? "" : this.coorgnizer;
  }

/**
	* 協辦人員編<br>
	* 
  *
  * @param coorgnizer 協辦人員編
	*/
  public void setCoorgnizer(String coorgnizer) {
    this.coorgnizer = coorgnizer;
  }

/**
	* 晤談一員編<br>
	* 
	* @return String
	*/
  public String getInterviewerA() {
    return this.interviewerA == null ? "" : this.interviewerA;
  }

/**
	* 晤談一員編<br>
	* 
  *
  * @param interviewerA 晤談一員編
	*/
  public void setInterviewerA(String interviewerA) {
    this.interviewerA = interviewerA;
  }

/**
	* 晤談二員編<br>
	* 
	* @return String
	*/
  public String getInterviewerB() {
    return this.interviewerB == null ? "" : this.interviewerB;
  }

/**
	* 晤談二員編<br>
	* 
  *
  * @param interviewerB 晤談二員編
	*/
  public void setInterviewerB(String interviewerB) {
    this.interviewerB = interviewerB;
  }

/**
	* 介紹人介紹獎金<br>
	* 薪碼:Q2
	* @return BigDecimal
	*/
  public BigDecimal getIntroducerBonus() {
    return this.introducerBonus;
  }

/**
	* 介紹人介紹獎金<br>
	* 薪碼:Q2
  *
  * @param introducerBonus 介紹人介紹獎金
	*/
  public void setIntroducerBonus(BigDecimal introducerBonus) {
    this.introducerBonus = introducerBonus;
  }

/**
	* 介紹獎金轉檔日<br>
	* 暫不用
	* @return Integer
	*/
  public int getIntroducerBonusDate() {
    return StaticTool.bcToRoc(this.introducerBonusDate);
  }

/**
	* 介紹獎金轉檔日<br>
	* 暫不用
  *
  * @param introducerBonusDate 介紹獎金轉檔日
  * @throws LogicException when Date Is Warn	*/
  public void setIntroducerBonusDate(int introducerBonusDate) throws LogicException {
    this.introducerBonusDate = StaticTool.rocToBc(introducerBonusDate);
  }

/**
	* 介紹人加碼獎勵津貼<br>
	* 薪碼:Q1
	* @return BigDecimal
	*/
  public BigDecimal getIntroducerAddBonus() {
    return this.introducerAddBonus;
  }

/**
	* 介紹人加碼獎勵津貼<br>
	* 薪碼:Q1
  *
  * @param introducerAddBonus 介紹人加碼獎勵津貼
	*/
  public void setIntroducerAddBonus(BigDecimal introducerAddBonus) {
    this.introducerAddBonus = introducerAddBonus;
  }

/**
	* 獎勵津貼轉檔日<br>
	* 暫不用
	* @return Integer
	*/
  public int getIntroducerAddBonusDate() {
    return StaticTool.bcToRoc(this.introducerAddBonusDate);
  }

/**
	* 獎勵津貼轉檔日<br>
	* 暫不用
  *
  * @param introducerAddBonusDate 獎勵津貼轉檔日
  * @throws LogicException when Date Is Warn	*/
  public void setIntroducerAddBonusDate(int introducerAddBonusDate) throws LogicException {
    this.introducerAddBonusDate = StaticTool.rocToBc(introducerAddBonusDate);
  }

/**
	* 協辦人員協辦獎金<br>
	* 薪碼:Q2
	* @return BigDecimal
	*/
  public BigDecimal getCoorgnizerBonus() {
    return this.coorgnizerBonus;
  }

/**
	* 協辦人員協辦獎金<br>
	* 薪碼:Q2
  *
  * @param coorgnizerBonus 協辦人員協辦獎金
	*/
  public void setCoorgnizerBonus(BigDecimal coorgnizerBonus) {
    this.coorgnizerBonus = coorgnizerBonus;
  }

/**
	* 協辦獎金轉檔日<br>
	* 暫不用
	* @return Integer
	*/
  public int getCoorgnizerBonusDate() {
    return StaticTool.bcToRoc(this.coorgnizerBonusDate);
  }

/**
	* 協辦獎金轉檔日<br>
	* 暫不用
  *
  * @param coorgnizerBonusDate 協辦獎金轉檔日
  * @throws LogicException when Date Is Warn	*/
  public void setCoorgnizerBonusDate(int coorgnizerBonusDate) throws LogicException {
    this.coorgnizerBonusDate = StaticTool.rocToBc(coorgnizerBonusDate);
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
    return "PfReward [logNo=" + logNo + ", perfDate=" + perfDate + ", custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + ", repayType=" + repayType
           + ", pieceCode=" + pieceCode + ", prodCode=" + prodCode + ", introducer=" + introducer + ", coorgnizer=" + coorgnizer + ", interviewerA=" + interviewerA + ", interviewerB=" + interviewerB
           + ", introducerBonus=" + introducerBonus + ", introducerBonusDate=" + introducerBonusDate + ", introducerAddBonus=" + introducerAddBonus + ", introducerAddBonusDate=" + introducerAddBonusDate + ", coorgnizerBonus=" + coorgnizerBonus + ", coorgnizerBonusDate=" + coorgnizerBonusDate
           + ", workMonth=" + workMonth + ", workSeason=" + workSeason + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
           + "]";
  }
}
