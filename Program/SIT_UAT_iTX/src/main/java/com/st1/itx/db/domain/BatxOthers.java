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
 * BatxOthers 其他還款來源檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`BatxOthers`")
public class BatxOthers implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -5914198499020906921L;

@EmbeddedId
  private BatxOthersId batxOthersId;

  // 會計日期
  @Column(name = "`AcDate`", insertable = false, updatable = false)
  private int acDate = 0;

  // 整批批號
  /* 不同櫃員登錄時，抓取總帳檔當日最新之BATX批號+1 */
  @Column(name = "`BatchNo`", length = 6, insertable = false, updatable = false)
  private String batchNo;

  // 明細序號
  @Column(name = "`DetailSeq`", insertable = false, updatable = false)
  private int detailSeq = 0;

  // 來源
  /* 05.法院扣薪06.理賠金07.代收款-債權協商09.其他11.匯款轉帳預先作業 */
  @Column(name = "`RepayCode`")
  private int repayCode = 0;

  // 還款類別
  /* 1:期款2:部分償還3:結案4:帳管費5:火險費6:契變手續費7:法務費9:其他 */
  @Column(name = "`RepayType`")
  private int repayType = 0;

  // 來源會計科目
  /* 8+5+2 */
  @Column(name = "`RepayAcCode`", length = 15)
  private String repayAcCode;

  // 入帳日期
  @Column(name = "`EntryDate`")
  private int entryDate = 0;

  // 金額
  @Column(name = "`RepayAmt`")
  private BigDecimal repayAmt = new BigDecimal("0");

  // 來源統編
  @Column(name = "`RepayId`", length = 10)
  private String repayId;

  // 來源戶名
  @Column(name = "`RepayName`", length = 100)
  private String repayName;

  // 借款人戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度號碼
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 借款人戶名
  @Column(name = "`CustNm`", length = 100)
  private String custNm;

  // 銷帳碼
  @Column(name = "`RvNo`", length = 12)
  private String rvNo;

  // 摘要
  @Column(name = "`Note`", length = 100)
  private String note;

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


  public BatxOthersId getBatxOthersId() {
    return this.batxOthersId;
  }

  public void setBatxOthersId(BatxOthersId batxOthersId) {
    this.batxOthersId = batxOthersId;
  }

/**
	* 會計日期<br>
	* 
	* @return Integer
	*/
  public int getAcDate() {
    return StaticTool.bcToRoc(this.acDate);
  }

/**
	* 會計日期<br>
	* 
  *
  * @param acDate 會計日期
  * @throws LogicException when Date Is Warn	*/
  public void setAcDate(int acDate) throws LogicException {
    this.acDate = StaticTool.rocToBc(acDate);
  }

/**
	* 整批批號<br>
	* 不同櫃員登錄時，抓取總帳檔當日最新之BATX批號+1
	* @return String
	*/
  public String getBatchNo() {
    return this.batchNo == null ? "" : this.batchNo;
  }

/**
	* 整批批號<br>
	* 不同櫃員登錄時，抓取總帳檔當日最新之BATX批號+1
  *
  * @param batchNo 整批批號
	*/
  public void setBatchNo(String batchNo) {
    this.batchNo = batchNo;
  }

/**
	* 明細序號<br>
	* 
	* @return Integer
	*/
  public int getDetailSeq() {
    return this.detailSeq;
  }

/**
	* 明細序號<br>
	* 
  *
  * @param detailSeq 明細序號
	*/
  public void setDetailSeq(int detailSeq) {
    this.detailSeq = detailSeq;
  }

/**
	* 來源<br>
	* 05.法院扣薪
06.理賠金
07.代收款-債權協商
09.其他
11.匯款轉帳預先作業
	* @return Integer
	*/
  public int getRepayCode() {
    return this.repayCode;
  }

/**
	* 來源<br>
	* 05.法院扣薪
06.理賠金
07.代收款-債權協商
09.其他
11.匯款轉帳預先作業
  *
  * @param repayCode 來源
	*/
  public void setRepayCode(int repayCode) {
    this.repayCode = repayCode;
  }

/**
	* 還款類別<br>
	* 1:期款
2:部分償還
3:結案
4:帳管費
5:火險費
6:契變手續費
7:法務費
9:其他
	* @return Integer
	*/
  public int getRepayType() {
    return this.repayType;
  }

/**
	* 還款類別<br>
	* 1:期款
2:部分償還
3:結案
4:帳管費
5:火險費
6:契變手續費
7:法務費
9:其他
  *
  * @param repayType 還款類別
	*/
  public void setRepayType(int repayType) {
    this.repayType = repayType;
  }

/**
	* 來源會計科目<br>
	* 8+5+2
	* @return String
	*/
  public String getRepayAcCode() {
    return this.repayAcCode == null ? "" : this.repayAcCode;
  }

/**
	* 來源會計科目<br>
	* 8+5+2
  *
  * @param repayAcCode 來源會計科目
	*/
  public void setRepayAcCode(String repayAcCode) {
    this.repayAcCode = repayAcCode;
  }

/**
	* 入帳日期<br>
	* 
	* @return Integer
	*/
  public int getEntryDate() {
    return StaticTool.bcToRoc(this.entryDate);
  }

/**
	* 入帳日期<br>
	* 
  *
  * @param entryDate 入帳日期
  * @throws LogicException when Date Is Warn	*/
  public void setEntryDate(int entryDate) throws LogicException {
    this.entryDate = StaticTool.rocToBc(entryDate);
  }

/**
	* 金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getRepayAmt() {
    return this.repayAmt;
  }

/**
	* 金額<br>
	* 
  *
  * @param repayAmt 金額
	*/
  public void setRepayAmt(BigDecimal repayAmt) {
    this.repayAmt = repayAmt;
  }

/**
	* 來源統編<br>
	* 
	* @return String
	*/
  public String getRepayId() {
    return this.repayId == null ? "" : this.repayId;
  }

/**
	* 來源統編<br>
	* 
  *
  * @param repayId 來源統編
	*/
  public void setRepayId(String repayId) {
    this.repayId = repayId;
  }

/**
	* 來源戶名<br>
	* 
	* @return String
	*/
  public String getRepayName() {
    return this.repayName == null ? "" : this.repayName;
  }

/**
	* 來源戶名<br>
	* 
  *
  * @param repayName 來源戶名
	*/
  public void setRepayName(String repayName) {
    this.repayName = repayName;
  }

/**
	* 借款人戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 借款人戶號<br>
	* 
  *
  * @param custNo 借款人戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 額度號碼<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度號碼<br>
	* 
  *
  * @param facmNo 額度號碼
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 借款人戶名<br>
	* 
	* @return String
	*/
  public String getCustNm() {
    return this.custNm == null ? "" : this.custNm;
  }

/**
	* 借款人戶名<br>
	* 
  *
  * @param custNm 借款人戶名
	*/
  public void setCustNm(String custNm) {
    this.custNm = custNm;
  }

/**
	* 銷帳碼<br>
	* 
	* @return String
	*/
  public String getRvNo() {
    return this.rvNo == null ? "" : this.rvNo;
  }

/**
	* 銷帳碼<br>
	* 
  *
  * @param rvNo 銷帳碼
	*/
  public void setRvNo(String rvNo) {
    this.rvNo = rvNo;
  }

/**
	* 摘要<br>
	* 
	* @return String
	*/
  public String getNote() {
    return this.note == null ? "" : this.note;
  }

/**
	* 摘要<br>
	* 
  *
  * @param note 摘要
	*/
  public void setNote(String note) {
    this.note = note;
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
    return "BatxOthers [batxOthersId=" + batxOthersId + ", repayCode=" + repayCode + ", repayType=" + repayType + ", repayAcCode=" + repayAcCode
           + ", entryDate=" + entryDate + ", repayAmt=" + repayAmt + ", repayId=" + repayId + ", repayName=" + repayName + ", custNo=" + custNo + ", facmNo=" + facmNo
           + ", custNm=" + custNm + ", rvNo=" + rvNo + ", note=" + note + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
