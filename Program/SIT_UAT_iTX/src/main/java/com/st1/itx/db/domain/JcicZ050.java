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
 * JcicZ050 債務人繳款資料檔案<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ050`")
public class JcicZ050 implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -178229929282474355L;

@EmbeddedId
  private JcicZ050Id jcicZ050Id;

  // 交易代碼
  /* A:新增;C:異動;D:刪除 */
  @Column(name = "`TranKey`", length = 1)
  private String tranKey;

  // 報送單位代號
  /* 三位文數字 */
  @Column(name = "`SubmitKey`", length = 3, insertable = false, updatable = false)
  private String submitKey;

  // 債務人IDN
  @Column(name = "`CustId`", length = 10, insertable = false, updatable = false)
  private String custId;

  // 協商申請日
  @Column(name = "`RcDate`", insertable = false, updatable = false)
  private int rcDate = 0;

  // 繳款日期
  /* 債務人實際繳款日期;若當日有多次還款，應累計當日繳款金額合併報送，不同繳款日期，應分別報送繳款紀錄。 */
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
  /* Y:債務全數清償;N:債務尚未全數清償; */
  @Column(name = "`Status`", length = 1)
  private String status;

  // 轉出JCIC文字檔日期
  @Column(name = "`OutJcicTxtDate`")
  private int outJcicTxtDate = 0;

  // 流水號
  @Column(name = "`Ukey`", length = 32)
  private String ukey;

  // 進入第二階梯還款年月
  /* JcicZ050消費者債務清理條例書上沒有此欄位新光提供的資料有故保留之 */
  @Column(name = "`SecondRepayYM`")
  private int secondRepayYM = 0;

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


  public JcicZ050Id getJcicZ050Id() {
    return this.jcicZ050Id;
  }

  public void setJcicZ050Id(JcicZ050Id jcicZ050Id) {
    this.jcicZ050Id = jcicZ050Id;
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
	* 協商申請日<br>
	* 
	* @return Integer
	*/
  public int getRcDate() {
    return StaticTool.bcToRoc(this.rcDate);
  }

/**
	* 協商申請日<br>
	* 
  *
  * @param rcDate 協商申請日
  * @throws LogicException when Date Is Warn	*/
  public void setRcDate(int rcDate) throws LogicException {
    this.rcDate = StaticTool.rocToBc(rcDate);
  }

/**
	* 繳款日期<br>
	* 債務人實際繳款日期;
若當日有多次還款，應累計當日繳款金額合併報送，不同繳款日期，應分別報送繳款紀錄。
	* @return Integer
	*/
  public int getPayDate() {
    return StaticTool.bcToRoc(this.payDate);
  }

/**
	* 繳款日期<br>
	* 債務人實際繳款日期;
若當日有多次還款，應累計當日繳款金額合併報送，不同繳款日期，應分別報送繳款紀錄。
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
	* Y:債務全數清償;
N:債務尚未全數清償;
	* @return String
	*/
  public String getStatus() {
    return this.status == null ? "" : this.status;
  }

/**
	* 債權結案註記<br>
	* Y:債務全數清償;
N:債務尚未全數清償;
  *
  * @param status 債權結案註記
	*/
  public void setStatus(String status) {
    this.status = status;
  }

/**
	* 轉出JCIC文字檔日期<br>
	* 
	* @return Integer
	*/
  public int getOutJcicTxtDate() {
    return StaticTool.bcToRoc(this.outJcicTxtDate);
  }

/**
	* 轉出JCIC文字檔日期<br>
	* 
  *
  * @param outJcicTxtDate 轉出JCIC文字檔日期
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
	* 進入第二階梯還款年月<br>
	* JcicZ050消費者債務清理條例
書上沒有此欄位
新光提供的資料有
故保留之
	* @return Integer
	*/
  public int getSecondRepayYM() {
    return this.secondRepayYM;
  }

/**
	* 進入第二階梯還款年月<br>
	* JcicZ050消費者債務清理條例
書上沒有此欄位
新光提供的資料有
故保留之
  *
  * @param secondRepayYM 進入第二階梯還款年月
	*/
  public void setSecondRepayYM(int secondRepayYM) {
    this.secondRepayYM = secondRepayYM;
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
    return "JcicZ050 [jcicZ050Id=" + jcicZ050Id + ", tranKey=" + tranKey + ", payAmt=" + payAmt
           + ", sumRepayActualAmt=" + sumRepayActualAmt + ", sumRepayShouldAmt=" + sumRepayShouldAmt + ", status=" + status + ", outJcicTxtDate=" + outJcicTxtDate + ", ukey=" + ukey + ", secondRepayYM=" + secondRepayYM
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
