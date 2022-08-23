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
 * JcicZ042 回報無擔保債權金額資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ042`")
public class JcicZ042 implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 7079092276261093051L;

@EmbeddedId
  private JcicZ042Id jcicZ042Id;

  // 交易代碼
  /* A:新增C:異動 */
  @Column(name = "`TranKey`", length = 1)
  private String tranKey;

  // 報送單位代號
  /* 三位文數字 */
  @Column(name = "`SubmitKey`", length = 3, insertable = false, updatable = false)
  private String submitKey;

  // 債務人IDN
  /* 身分證號 */
  @Column(name = "`CustId`", length = 10, insertable = false, updatable = false)
  private String custId;

  // 協商申請日
  @Column(name = "`RcDate`", insertable = false, updatable = false)
  private int rcDate = 0;

  // 最大債權金融機構代號
  /* 三位文數字 */
  @Column(name = "`MaxMainCode`", length = 3, insertable = false, updatable = false)
  private String maxMainCode;

  // 是否為本金融機構債務人
  /* N,Y */
  @Column(name = "`IsClaims`", length = 1)
  private String isClaims;

  // 本金融機構有擔保債權筆數
  @Column(name = "`GuarLoanCnt`")
  private int guarLoanCnt = 0;

  // 信用貸款對內本息餘額
  @Column(name = "`ExpLoanAmt`")
  private int expLoanAmt = 0;

  // 依民法第323條計算之信用貸款本息餘額
  @Column(name = "`Civil323ExpAmt`")
  private int civil323ExpAmt = 0;

  // 信用貸款最近一期繳款金額
  @Column(name = "`ReceExpAmt`")
  private int receExpAmt = 0;

  // 現金卡放款對內本息餘額
  @Column(name = "`CashCardAmt`")
  private int cashCardAmt = 0;

  // 依民法第323條計算之現金卡放款本息餘額
  @Column(name = "`Civil323CashAmt`")
  private int civil323CashAmt = 0;

  // 現金卡最近一期繳款金額
  @Column(name = "`ReceCashAmt`")
  private int receCashAmt = 0;

  // 信用卡對內本息餘額
  @Column(name = "`CreditCardAmt`")
  private int creditCardAmt = 0;

  // 依民法第323條計算之信用卡本息餘額
  @Column(name = "`Civil323CreditAmt`")
  private int civil323CreditAmt = 0;

  // 信用卡最近一期繳款金額
  @Column(name = "`ReceCreditAmt`")
  private int receCreditAmt = 0;

  // 信用貸款本金
  @Column(name = "`ReceExpPrin`")
  private int receExpPrin = 0;

  // 信用貸款利息
  @Column(name = "`ReceExpInte`")
  private int receExpInte = 0;

  // 信用貸款違約金
  @Column(name = "`ReceExpPena`")
  private int receExpPena = 0;

  // 信用貸款其他費用
  @Column(name = "`ReceExpOther`")
  private int receExpOther = 0;

  // 現金卡本金
  @Column(name = "`CashCardPrin`")
  private int cashCardPrin = 0;

  // 信金卡利息
  @Column(name = "`CashCardInte`")
  private int cashCardInte = 0;

  // 信金卡違約金
  @Column(name = "`CashCardPena`")
  private int cashCardPena = 0;

  // 現金卡其他費用
  @Column(name = "`CashCardOther`")
  private int cashCardOther = 0;

  // 信用卡本金
  @Column(name = "`CreditCardPrin`")
  private int creditCardPrin = 0;

  // 信用卡利息
  @Column(name = "`CreditCardInte`")
  private int creditCardInte = 0;

  // 信用卡違約金
  @Column(name = "`CreditCardPena`")
  private int creditCardPena = 0;

  // 信用卡其他費用
  @Column(name = "`CreditCardOther`")
  private int creditCardOther = 0;

  // 轉出JCIC文字檔日期
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

  // 實際報送日期
  @Column(name = "`ActualFilingDate`")
  private int actualFilingDate = 0;

  // 實際報送記號
  @Column(name = "`ActualFilingMark`", length = 3)
  private String actualFilingMark;


  public JcicZ042Id getJcicZ042Id() {
    return this.jcicZ042Id;
  }

  public void setJcicZ042Id(JcicZ042Id jcicZ042Id) {
    this.jcicZ042Id = jcicZ042Id;
  }

/**
	* 交易代碼<br>
	* A:新增
C:異動
	* @return String
	*/
  public String getTranKey() {
    return this.tranKey == null ? "" : this.tranKey;
  }

/**
	* 交易代碼<br>
	* A:新增
C:異動
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
	* 身分證號
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 債務人IDN<br>
	* 身分證號
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
	* 最大債權金融機構代號<br>
	* 三位文數字
	* @return String
	*/
  public String getMaxMainCode() {
    return this.maxMainCode == null ? "" : this.maxMainCode;
  }

/**
	* 最大債權金融機構代號<br>
	* 三位文數字
  *
  * @param maxMainCode 最大債權金融機構代號
	*/
  public void setMaxMainCode(String maxMainCode) {
    this.maxMainCode = maxMainCode;
  }

/**
	* 是否為本金融機構債務人<br>
	* N,Y
	* @return String
	*/
  public String getIsClaims() {
    return this.isClaims == null ? "" : this.isClaims;
  }

/**
	* 是否為本金融機構債務人<br>
	* N,Y
  *
  * @param isClaims 是否為本金融機構債務人
	*/
  public void setIsClaims(String isClaims) {
    this.isClaims = isClaims;
  }

/**
	* 本金融機構有擔保債權筆數<br>
	* 
	* @return Integer
	*/
  public int getGuarLoanCnt() {
    return this.guarLoanCnt;
  }

/**
	* 本金融機構有擔保債權筆數<br>
	* 
  *
  * @param guarLoanCnt 本金融機構有擔保債權筆數
	*/
  public void setGuarLoanCnt(int guarLoanCnt) {
    this.guarLoanCnt = guarLoanCnt;
  }

/**
	* 信用貸款對內本息餘額<br>
	* 
	* @return Integer
	*/
  public int getExpLoanAmt() {
    return this.expLoanAmt;
  }

/**
	* 信用貸款對內本息餘額<br>
	* 
  *
  * @param expLoanAmt 信用貸款對內本息餘額
	*/
  public void setExpLoanAmt(int expLoanAmt) {
    this.expLoanAmt = expLoanAmt;
  }

/**
	* 依民法第323條計算之信用貸款本息餘額<br>
	* 
	* @return Integer
	*/
  public int getCivil323ExpAmt() {
    return this.civil323ExpAmt;
  }

/**
	* 依民法第323條計算之信用貸款本息餘額<br>
	* 
  *
  * @param civil323ExpAmt 依民法第323條計算之信用貸款本息餘額
	*/
  public void setCivil323ExpAmt(int civil323ExpAmt) {
    this.civil323ExpAmt = civil323ExpAmt;
  }

/**
	* 信用貸款最近一期繳款金額<br>
	* 
	* @return Integer
	*/
  public int getReceExpAmt() {
    return this.receExpAmt;
  }

/**
	* 信用貸款最近一期繳款金額<br>
	* 
  *
  * @param receExpAmt 信用貸款最近一期繳款金額
	*/
  public void setReceExpAmt(int receExpAmt) {
    this.receExpAmt = receExpAmt;
  }

/**
	* 現金卡放款對內本息餘額<br>
	* 
	* @return Integer
	*/
  public int getCashCardAmt() {
    return this.cashCardAmt;
  }

/**
	* 現金卡放款對內本息餘額<br>
	* 
  *
  * @param cashCardAmt 現金卡放款對內本息餘額
	*/
  public void setCashCardAmt(int cashCardAmt) {
    this.cashCardAmt = cashCardAmt;
  }

/**
	* 依民法第323條計算之現金卡放款本息餘額<br>
	* 
	* @return Integer
	*/
  public int getCivil323CashAmt() {
    return this.civil323CashAmt;
  }

/**
	* 依民法第323條計算之現金卡放款本息餘額<br>
	* 
  *
  * @param civil323CashAmt 依民法第323條計算之現金卡放款本息餘額
	*/
  public void setCivil323CashAmt(int civil323CashAmt) {
    this.civil323CashAmt = civil323CashAmt;
  }

/**
	* 現金卡最近一期繳款金額<br>
	* 
	* @return Integer
	*/
  public int getReceCashAmt() {
    return this.receCashAmt;
  }

/**
	* 現金卡最近一期繳款金額<br>
	* 
  *
  * @param receCashAmt 現金卡最近一期繳款金額
	*/
  public void setReceCashAmt(int receCashAmt) {
    this.receCashAmt = receCashAmt;
  }

/**
	* 信用卡對內本息餘額<br>
	* 
	* @return Integer
	*/
  public int getCreditCardAmt() {
    return this.creditCardAmt;
  }

/**
	* 信用卡對內本息餘額<br>
	* 
  *
  * @param creditCardAmt 信用卡對內本息餘額
	*/
  public void setCreditCardAmt(int creditCardAmt) {
    this.creditCardAmt = creditCardAmt;
  }

/**
	* 依民法第323條計算之信用卡本息餘額<br>
	* 
	* @return Integer
	*/
  public int getCivil323CreditAmt() {
    return this.civil323CreditAmt;
  }

/**
	* 依民法第323條計算之信用卡本息餘額<br>
	* 
  *
  * @param civil323CreditAmt 依民法第323條計算之信用卡本息餘額
	*/
  public void setCivil323CreditAmt(int civil323CreditAmt) {
    this.civil323CreditAmt = civil323CreditAmt;
  }

/**
	* 信用卡最近一期繳款金額<br>
	* 
	* @return Integer
	*/
  public int getReceCreditAmt() {
    return this.receCreditAmt;
  }

/**
	* 信用卡最近一期繳款金額<br>
	* 
  *
  * @param receCreditAmt 信用卡最近一期繳款金額
	*/
  public void setReceCreditAmt(int receCreditAmt) {
    this.receCreditAmt = receCreditAmt;
  }

/**
	* 信用貸款本金<br>
	* 
	* @return Integer
	*/
  public int getReceExpPrin() {
    return this.receExpPrin;
  }

/**
	* 信用貸款本金<br>
	* 
  *
  * @param receExpPrin 信用貸款本金
	*/
  public void setReceExpPrin(int receExpPrin) {
    this.receExpPrin = receExpPrin;
  }

/**
	* 信用貸款利息<br>
	* 
	* @return Integer
	*/
  public int getReceExpInte() {
    return this.receExpInte;
  }

/**
	* 信用貸款利息<br>
	* 
  *
  * @param receExpInte 信用貸款利息
	*/
  public void setReceExpInte(int receExpInte) {
    this.receExpInte = receExpInte;
  }

/**
	* 信用貸款違約金<br>
	* 
	* @return Integer
	*/
  public int getReceExpPena() {
    return this.receExpPena;
  }

/**
	* 信用貸款違約金<br>
	* 
  *
  * @param receExpPena 信用貸款違約金
	*/
  public void setReceExpPena(int receExpPena) {
    this.receExpPena = receExpPena;
  }

/**
	* 信用貸款其他費用<br>
	* 
	* @return Integer
	*/
  public int getReceExpOther() {
    return this.receExpOther;
  }

/**
	* 信用貸款其他費用<br>
	* 
  *
  * @param receExpOther 信用貸款其他費用
	*/
  public void setReceExpOther(int receExpOther) {
    this.receExpOther = receExpOther;
  }

/**
	* 現金卡本金<br>
	* 
	* @return Integer
	*/
  public int getCashCardPrin() {
    return this.cashCardPrin;
  }

/**
	* 現金卡本金<br>
	* 
  *
  * @param cashCardPrin 現金卡本金
	*/
  public void setCashCardPrin(int cashCardPrin) {
    this.cashCardPrin = cashCardPrin;
  }

/**
	* 信金卡利息<br>
	* 
	* @return Integer
	*/
  public int getCashCardInte() {
    return this.cashCardInte;
  }

/**
	* 信金卡利息<br>
	* 
  *
  * @param cashCardInte 信金卡利息
	*/
  public void setCashCardInte(int cashCardInte) {
    this.cashCardInte = cashCardInte;
  }

/**
	* 信金卡違約金<br>
	* 
	* @return Integer
	*/
  public int getCashCardPena() {
    return this.cashCardPena;
  }

/**
	* 信金卡違約金<br>
	* 
  *
  * @param cashCardPena 信金卡違約金
	*/
  public void setCashCardPena(int cashCardPena) {
    this.cashCardPena = cashCardPena;
  }

/**
	* 現金卡其他費用<br>
	* 
	* @return Integer
	*/
  public int getCashCardOther() {
    return this.cashCardOther;
  }

/**
	* 現金卡其他費用<br>
	* 
  *
  * @param cashCardOther 現金卡其他費用
	*/
  public void setCashCardOther(int cashCardOther) {
    this.cashCardOther = cashCardOther;
  }

/**
	* 信用卡本金<br>
	* 
	* @return Integer
	*/
  public int getCreditCardPrin() {
    return this.creditCardPrin;
  }

/**
	* 信用卡本金<br>
	* 
  *
  * @param creditCardPrin 信用卡本金
	*/
  public void setCreditCardPrin(int creditCardPrin) {
    this.creditCardPrin = creditCardPrin;
  }

/**
	* 信用卡利息<br>
	* 
	* @return Integer
	*/
  public int getCreditCardInte() {
    return this.creditCardInte;
  }

/**
	* 信用卡利息<br>
	* 
  *
  * @param creditCardInte 信用卡利息
	*/
  public void setCreditCardInte(int creditCardInte) {
    this.creditCardInte = creditCardInte;
  }

/**
	* 信用卡違約金<br>
	* 
	* @return Integer
	*/
  public int getCreditCardPena() {
    return this.creditCardPena;
  }

/**
	* 信用卡違約金<br>
	* 
  *
  * @param creditCardPena 信用卡違約金
	*/
  public void setCreditCardPena(int creditCardPena) {
    this.creditCardPena = creditCardPena;
  }

/**
	* 信用卡其他費用<br>
	* 
	* @return Integer
	*/
  public int getCreditCardOther() {
    return this.creditCardOther;
  }

/**
	* 信用卡其他費用<br>
	* 
  *
  * @param creditCardOther 信用卡其他費用
	*/
  public void setCreditCardOther(int creditCardOther) {
    this.creditCardOther = creditCardOther;
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

/**
	* 實際報送日期<br>
	* 
	* @return Integer
	*/
  public int getActualFilingDate() {
    return StaticTool.bcToRoc(this.actualFilingDate);
  }

/**
	* 實際報送日期<br>
	* 
  *
  * @param actualFilingDate 實際報送日期
  * @throws LogicException when Date Is Warn	*/
  public void setActualFilingDate(int actualFilingDate) throws LogicException {
    this.actualFilingDate = StaticTool.rocToBc(actualFilingDate);
  }

/**
	* 實際報送記號<br>
	* 
	* @return String
	*/
  public String getActualFilingMark() {
    return this.actualFilingMark == null ? "" : this.actualFilingMark;
  }

/**
	* 實際報送記號<br>
	* 
  *
  * @param actualFilingMark 實際報送記號
	*/
  public void setActualFilingMark(String actualFilingMark) {
    this.actualFilingMark = actualFilingMark;
  }


  @Override
  public String toString() {
    return "JcicZ042 [jcicZ042Id=" + jcicZ042Id + ", tranKey=" + tranKey + ", isClaims=" + isClaims
           + ", guarLoanCnt=" + guarLoanCnt + ", expLoanAmt=" + expLoanAmt + ", civil323ExpAmt=" + civil323ExpAmt + ", receExpAmt=" + receExpAmt + ", cashCardAmt=" + cashCardAmt + ", civil323CashAmt=" + civil323CashAmt
           + ", receCashAmt=" + receCashAmt + ", creditCardAmt=" + creditCardAmt + ", civil323CreditAmt=" + civil323CreditAmt + ", receCreditAmt=" + receCreditAmt + ", receExpPrin=" + receExpPrin + ", receExpInte=" + receExpInte
           + ", receExpPena=" + receExpPena + ", receExpOther=" + receExpOther + ", cashCardPrin=" + cashCardPrin + ", cashCardInte=" + cashCardInte + ", cashCardPena=" + cashCardPena + ", cashCardOther=" + cashCardOther
           + ", creditCardPrin=" + creditCardPrin + ", creditCardInte=" + creditCardInte + ", creditCardPena=" + creditCardPena + ", creditCardOther=" + creditCardOther + ", outJcicTxtDate=" + outJcicTxtDate + ", ukey=" + ukey
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + ", actualFilingDate=" + actualFilingDate + ", actualFilingMark=" + actualFilingMark
           + "]";
  }
}
