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
 * PfBsDetail 房貸專員業績明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`PfBsDetail`")
public class PfBsDetail implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 437220263775811807L;

// 序號
  @Id
  @Column(name = "`LogNo`")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "`PfBsDetail_SEQ`")
  @SequenceGenerator(name = "`PfBsDetail_SEQ`", sequenceName = "`PfBsDetail_SEQ`", allocationSize = 1)
  private Long logNo = 0L;

  // 業績日期
  /* 業績日期=系統營業日1.撥貸(計件代碼變更)，為撥款日期；計件代碼變更不可跨工作月2.部分償還、提前結案，為會計日 */
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
  /* 0.撥款 1.計件代碼變更 2.部分償還 3.提前結案 4.人工增減業績 */
  @Column(name = "`RepayType`")
  private int repayType = 0;

  // 房貸專員
  /* FacMain.BusinessOfficer 放款業務專員 */
  @Column(name = "`BsOfficer`", length = 6)
  private String bsOfficer;

  // 部室代號
  /* 應以PfBsOfficer(房貸專員業績目標檔)計算業績 */
  @Column(name = "`DeptCode`", length = 6)
  private String deptCode;

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

  // 撥款金額/追回金額
  /* 撥款為正值，部分償還、提前結案為負值，計件代碼變更為零 */
  @Column(name = "`DrawdownAmt`")
  private BigDecimal drawdownAmt = new BigDecimal("0");

  // 件數
  @Column(name = "`PerfCnt`")
  private BigDecimal perfCnt = new BigDecimal("0");

  // 業績金額
  @Column(name = "`PerfAmt`")
  private BigDecimal perfAmt = new BigDecimal("0");

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
	* 業績日期=系統營業日
1.撥貸(計件代碼變更)，為撥款日期；計件代碼變更不可跨工作月
2.部分償還、提前結案，為會計日
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
	* 0.撥款 1.計件代碼變更 2.部分償還 3.提前結案 4.人工增減業績
	* @return Integer
	*/
  public int getRepayType() {
    return this.repayType;
  }

/**
	* 還款類別<br>
	* 0.撥款 1.計件代碼變更 2.部分償還 3.提前結案 4.人工增減業績
  *
  * @param repayType 還款類別
	*/
  public void setRepayType(int repayType) {
    this.repayType = repayType;
  }

/**
	* 房貸專員<br>
	* FacMain.BusinessOfficer 放款業務專員
	* @return String
	*/
  public String getBsOfficer() {
    return this.bsOfficer == null ? "" : this.bsOfficer;
  }

/**
	* 房貸專員<br>
	* FacMain.BusinessOfficer 放款業務專員
  *
  * @param bsOfficer 房貸專員
	*/
  public void setBsOfficer(String bsOfficer) {
    this.bsOfficer = bsOfficer;
  }

/**
	* 部室代號<br>
	* 應以PfBsOfficer(房貸專員業績目標檔)計算業績
	* @return String
	*/
  public String getDeptCode() {
    return this.deptCode == null ? "" : this.deptCode;
  }

/**
	* 部室代號<br>
	* 應以PfBsOfficer(房貸專員業績目標檔)計算業績
  *
  * @param deptCode 部室代號
	*/
  public void setDeptCode(String deptCode) {
    this.deptCode = deptCode;
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
	* 撥款金額/追回金額<br>
	* 撥款為正值，部分償還、提前結案為負值，計件代碼變更為零
	* @return BigDecimal
	*/
  public BigDecimal getDrawdownAmt() {
    return this.drawdownAmt;
  }

/**
	* 撥款金額/追回金額<br>
	* 撥款為正值，部分償還、提前結案為負值，計件代碼變更為零
  *
  * @param drawdownAmt 撥款金額/追回金額
	*/
  public void setDrawdownAmt(BigDecimal drawdownAmt) {
    this.drawdownAmt = drawdownAmt;
  }

/**
	* 件數<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getPerfCnt() {
    return this.perfCnt;
  }

/**
	* 件數<br>
	* 
  *
  * @param perfCnt 件數
	*/
  public void setPerfCnt(BigDecimal perfCnt) {
    this.perfCnt = perfCnt;
  }

/**
	* 業績金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getPerfAmt() {
    return this.perfAmt;
  }

/**
	* 業績金額<br>
	* 
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
    return "PfBsDetail [logNo=" + logNo + ", perfDate=" + perfDate + ", custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + ", repayType=" + repayType
           + ", bsOfficer=" + bsOfficer + ", deptCode=" + deptCode + ", drawdownDate=" + drawdownDate + ", prodCode=" + prodCode + ", pieceCode=" + pieceCode + ", drawdownAmt=" + drawdownAmt
           + ", perfCnt=" + perfCnt + ", perfAmt=" + perfAmt + ", workMonth=" + workMonth + ", workSeason=" + workSeason + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
