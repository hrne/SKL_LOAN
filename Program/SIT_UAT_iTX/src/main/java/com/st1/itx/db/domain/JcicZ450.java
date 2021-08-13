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
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ450 前置調解債務人繳款資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ450`")
public class JcicZ450 implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -8922772726558611934L;

@EmbeddedId
  private JcicZ450Id jcicZ450Id;

  // 交易代碼
  /* A:新增;C:異動;D:刪除 */
  @Column(name = "`TranKey`", length = 1)
  private String tranKey;

  // 債務人IDN
  @Column(name = "`CustId`", length = 10, insertable = false, updatable = false)
  private String custId;

  // 報送單位代號
  /* 三位文數字 */
  @Column(name = "`SubmitKey`", length = 3, insertable = false, updatable = false)
  private String submitKey;

  // 調解申請日
  @Column(name = "`ApplyDate`", insertable = false, updatable = false)
  private int applyDate = 0;

  // 受理調解機構代號
  /* 三位文數字法院名稱代號表(CdCode.CourtCode)或郵遞區號 */
  @Column(name = "`CourtCode`", length = 3, insertable = false, updatable = false)
  private String courtCode;

  // 繳款日期
  @Column(name = "`PayDate`", insertable = false, updatable = false)
  private int payDate = 0;

  // 本次繳款金額
  @Column(name = "`PayAmt`")
  private int payAmt = 0;

  // 累計實際還款金額
  @Column(name = "`SumRepayActualAmt`")
  private int sumRepayActualAmt = 0;

  // 截至目前累計應還款金額
  @Column(name = "`SumRepayShouldAmt`")
  private int sumRepayShouldAmt = 0;

  // 債權結案註記
  /* Y:債務全數清償N:債務尚未全數清償 */
  @Column(name = "`PayStatus`", length = 1)
  private String payStatus;

  // 轉JCIC文字檔日期
  @Column(name = "`OutJcicTxtDate`")
  private int outJcicTxtDate = 0;

  // 流水號
  @Column(name = "`Ukey`", length = 32)
  private String ukey;

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


  public JcicZ450Id getJcicZ450Id() {
    return this.jcicZ450Id;
  }

  public void setJcicZ450Id(JcicZ450Id jcicZ450Id) {
    this.jcicZ450Id = jcicZ450Id;
  }

/**
	* 交易代碼<br>
	* A:新增;C:異動;D:刪除
	* @return String
	*/
  public String getTranKey() {
    return this.tranKey == null ? "" : this.tranKey;
  }

/**
	* 交易代碼<br>
	* A:新增;C:異動;D:刪除
  *
  * @param tranKey 交易代碼
	*/
  public void setTranKey(String tranKey) {
    this.tranKey = tranKey;
  }

/**
	* 債務人IDN<br>
	* 
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 債務人IDN<br>
	* 
  *
  * @param custId 債務人IDN
	*/
  public void setCustId(String custId) {
    this.custId = custId;
  }

/**
	* 報送單位代號<br>
	* 三位文數字
	* @return String
	*/
  public String getSubmitKey() {
    return this.submitKey == null ? "" : this.submitKey;
  }

/**
	* 報送單位代號<br>
	* 三位文數字
  *
  * @param submitKey 報送單位代號
	*/
  public void setSubmitKey(String submitKey) {
    this.submitKey = submitKey;
  }

/**
	* 調解申請日<br>
	* 
	* @return Integer
	*/
  public int getApplyDate() {
    return StaticTool.bcToRoc(this.applyDate);
  }

/**
	* 調解申請日<br>
	* 
  *
  * @param applyDate 調解申請日
  * @throws LogicException when Date Is Warn	*/
  public void setApplyDate(int applyDate) throws LogicException {
    this.applyDate = StaticTool.rocToBc(applyDate);
  }

/**
	* 受理調解機構代號<br>
	* 三位文數字
法院名稱代號表(CdCode.CourtCode)或郵遞區號
	* @return String
	*/
  public String getCourtCode() {
    return this.courtCode == null ? "" : this.courtCode;
  }

/**
	* 受理調解機構代號<br>
	* 三位文數字
法院名稱代號表(CdCode.CourtCode)或郵遞區號
  *
  * @param courtCode 受理調解機構代號
	*/
  public void setCourtCode(String courtCode) {
    this.courtCode = courtCode;
  }

/**
	* 繳款日期<br>
	* 
	* @return Integer
	*/
  public int getPayDate() {
    return StaticTool.bcToRoc(this.payDate);
  }

/**
	* 繳款日期<br>
	* 
  *
  * @param payDate 繳款日期
  * @throws LogicException when Date Is Warn	*/
  public void setPayDate(int payDate) throws LogicException {
    this.payDate = StaticTool.rocToBc(payDate);
  }

/**
	* 本次繳款金額<br>
	* 
	* @return Integer
	*/
  public int getPayAmt() {
    return this.payAmt;
  }

/**
	* 本次繳款金額<br>
	* 
  *
  * @param payAmt 本次繳款金額
	*/
  public void setPayAmt(int payAmt) {
    this.payAmt = payAmt;
  }

/**
	* 累計實際還款金額<br>
	* 
	* @return Integer
	*/
  public int getSumRepayActualAmt() {
    return this.sumRepayActualAmt;
  }

/**
	* 累計實際還款金額<br>
	* 
  *
  * @param sumRepayActualAmt 累計實際還款金額
	*/
  public void setSumRepayActualAmt(int sumRepayActualAmt) {
    this.sumRepayActualAmt = sumRepayActualAmt;
  }

/**
	* 截至目前累計應還款金額<br>
	* 
	* @return Integer
	*/
  public int getSumRepayShouldAmt() {
    return this.sumRepayShouldAmt;
  }

/**
	* 截至目前累計應還款金額<br>
	* 
  *
  * @param sumRepayShouldAmt 截至目前累計應還款金額
	*/
  public void setSumRepayShouldAmt(int sumRepayShouldAmt) {
    this.sumRepayShouldAmt = sumRepayShouldAmt;
  }

/**
	* 債權結案註記<br>
	* Y:債務全數清償
N:債務尚未全數清償
	* @return String
	*/
  public String getPayStatus() {
    return this.payStatus == null ? "" : this.payStatus;
  }

/**
	* 債權結案註記<br>
	* Y:債務全數清償
N:債務尚未全數清償
  *
  * @param payStatus 債權結案註記
	*/
  public void setPayStatus(String payStatus) {
    this.payStatus = payStatus;
  }

/**
	* 轉JCIC文字檔日期<br>
	* 
	* @return Integer
	*/
  public int getOutJcicTxtDate() {
    return StaticTool.bcToRoc(this.outJcicTxtDate);
  }

/**
	* 轉JCIC文字檔日期<br>
	* 
  *
  * @param outJcicTxtDate 轉JCIC文字檔日期
  * @throws LogicException when Date Is Warn	*/
  public void setOutJcicTxtDate(int outJcicTxtDate) throws LogicException {
    this.outJcicTxtDate = StaticTool.rocToBc(outJcicTxtDate);
  }

/**
	* 流水號<br>
	* 
	* @return String
	*/
  public String getUkey() {
    return this.ukey == null ? "" : this.ukey;
  }

/**
	* 流水號<br>
	* 
  *
  * @param ukey 流水號
	*/
  public void setUkey(String ukey) {
    this.ukey = ukey;
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
    return "JcicZ450 [jcicZ450Id=" + jcicZ450Id + ", tranKey=" + tranKey
           + ", payAmt=" + payAmt + ", sumRepayActualAmt=" + sumRepayActualAmt + ", sumRepayShouldAmt=" + sumRepayShouldAmt + ", payStatus=" + payStatus + ", outJcicTxtDate=" + outJcicTxtDate + ", ukey=" + ukey
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
