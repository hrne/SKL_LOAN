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
 * JcicB680 貸款餘額扣除擔保品鑑估值之金額資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicB680`")
public class JcicB680 implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -51704531979674736L;

@EmbeddedId
  private JcicB680Id jcicB680Id;

  // 資料日期
  @Column(name = "`DataYM`", insertable = false, updatable = false)
  private int dataYM = 0;

  // 總行代號
  /* 金融機構總機構之代號，三位數字 */
  @Column(name = "`BankItem`", length = 3)
  private String bankItem;

  // 分行代號
  /* 金融機構分支機構之代號，四位數字 */
  @Column(name = "`BranchItem`", length = 4)
  private String branchItem;

  // 交易代碼
  /* A新增 C異動 D刪除 */
  @Column(name = "`TranCode`", length = 1, insertable = false, updatable = false)
  private String tranCode;

  // 授信戶IDN/BAN
  /* Key,左靠，身份證或統一證號 */
  @Column(name = "`CustId`", length = 10, insertable = false, updatable = false)
  private String custId;

  // 上欄IDN或BAN錯誤註記
  /* 第4欄授信戶IDN/BAN經邏輯檢查無誤者，本欄位空白，否則以 * 填報 */
  @Column(name = "`CustIdErr`", length = 1)
  private String custIdErr;

  // 借款人戶號
  /* 戶號(KEY) */
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 額度編號
  /* 呆帳明細資料(KEY)，其他彙計資料放0 */
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 撥款序號
  /* 呆帳明細資料(KEY)，其他彙計資料放0 */
  @Column(name = "`BormNo`", insertable = false, updatable = false)
  private int bormNo = 0;

  // 空白
  /* 空白 */
  @Column(name = "`Filler6`", length = 40)
  private String filler6;

  // 貸款餘額扣除擔保品鑑估值之金額
  /* 右靠左補0，單位新台幣千元，本欄位金額請四捨五入後填報，不足千元者以一千元計。為該授信戶於上述總分行機構辦理之擔保放款餘額加上部分擔保、副擔保貸款餘額扣除所提供之擔保品鑑估值後所得之金額，上開所得之金額如小於0，則金額報送"0"。 */
  @Column(name = "`Amt`")
  private BigDecimal amt = new BigDecimal("0");

  // 資料所屬年月
  /* 請填報本筆授信資料所屬年月 */
  @Column(name = "`JcicDataYM`")
  private int jcicDataYM = 0;

  // 空白
  /* 空白 */
  @Column(name = "`Filler9`", length = 54)
  private String filler9;

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


  public JcicB680Id getJcicB680Id() {
    return this.jcicB680Id;
  }

  public void setJcicB680Id(JcicB680Id jcicB680Id) {
    this.jcicB680Id = jcicB680Id;
  }

/**
	* 資料日期<br>
	* 
	* @return Integer
	*/
  public int getDataYM() {
    return this.dataYM;
  }

/**
	* 資料日期<br>
	* 
  *
  * @param dataYM 資料日期
	*/
  public void setDataYM(int dataYM) {
    this.dataYM = dataYM;
  }

/**
	* 總行代號<br>
	* 金融機構總機構之代號，三位數字
	* @return String
	*/
  public String getBankItem() {
    return this.bankItem == null ? "" : this.bankItem;
  }

/**
	* 總行代號<br>
	* 金融機構總機構之代號，三位數字
  *
  * @param bankItem 總行代號
	*/
  public void setBankItem(String bankItem) {
    this.bankItem = bankItem;
  }

/**
	* 分行代號<br>
	* 金融機構分支機構之代號，四位數字
	* @return String
	*/
  public String getBranchItem() {
    return this.branchItem == null ? "" : this.branchItem;
  }

/**
	* 分行代號<br>
	* 金融機構分支機構之代號，四位數字
  *
  * @param branchItem 分行代號
	*/
  public void setBranchItem(String branchItem) {
    this.branchItem = branchItem;
  }

/**
	* 交易代碼<br>
	* A新增 C異動 D刪除
	* @return String
	*/
  public String getTranCode() {
    return this.tranCode == null ? "" : this.tranCode;
  }

/**
	* 交易代碼<br>
	* A新增 C異動 D刪除
  *
  * @param tranCode 交易代碼
	*/
  public void setTranCode(String tranCode) {
    this.tranCode = tranCode;
  }

/**
	* 授信戶IDN/BAN<br>
	* Key,左靠，身份證或統一證號
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 授信戶IDN/BAN<br>
	* Key,左靠，身份證或統一證號
  *
  * @param custId 授信戶IDN/BAN
	*/
  public void setCustId(String custId) {
    this.custId = custId;
  }

/**
	* 上欄IDN或BAN錯誤註記<br>
	* 第4欄授信戶IDN/BAN經邏輯檢查無誤者，本欄位空白，否則以 * 填報
	* @return String
	*/
  public String getCustIdErr() {
    return this.custIdErr == null ? "" : this.custIdErr;
  }

/**
	* 上欄IDN或BAN錯誤註記<br>
	* 第4欄授信戶IDN/BAN經邏輯檢查無誤者，本欄位空白，否則以 * 填報
  *
  * @param custIdErr 上欄IDN或BAN錯誤註記
	*/
  public void setCustIdErr(String custIdErr) {
    this.custIdErr = custIdErr;
  }

/**
	* 借款人戶號<br>
	* 戶號(KEY)
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 借款人戶號<br>
	* 戶號(KEY)
  *
  * @param custNo 借款人戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 額度編號<br>
	* 呆帳明細資料(KEY)，其他彙計資料放0
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度編號<br>
	* 呆帳明細資料(KEY)，其他彙計資料放0
  *
  * @param facmNo 額度編號
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 撥款序號<br>
	* 呆帳明細資料(KEY)，其他彙計資料放0
	* @return Integer
	*/
  public int getBormNo() {
    return this.bormNo;
  }

/**
	* 撥款序號<br>
	* 呆帳明細資料(KEY)，其他彙計資料放0
  *
  * @param bormNo 撥款序號
	*/
  public void setBormNo(int bormNo) {
    this.bormNo = bormNo;
  }

/**
	* 空白<br>
	* 空白
	* @return String
	*/
  public String getFiller6() {
    return this.filler6 == null ? "" : this.filler6;
  }

/**
	* 空白<br>
	* 空白
  *
  * @param filler6 空白
	*/
  public void setFiller6(String filler6) {
    this.filler6 = filler6;
  }

/**
	* 貸款餘額扣除擔保品鑑估值之金額<br>
	* 右靠左補0，單位新台幣千元，本欄位金額請四捨五入後填報，不足千元者以一千元計。為該授信戶於上述總分行機構辦理之擔保放款餘額加上部分擔保、副擔保貸款餘額扣除所提供之擔保品鑑估值後所得之金額，上開所得之金額如小於0，則金額報送"0"。
	* @return BigDecimal
	*/
  public BigDecimal getAmt() {
    return this.amt;
  }

/**
	* 貸款餘額扣除擔保品鑑估值之金額<br>
	* 右靠左補0，單位新台幣千元，本欄位金額請四捨五入後填報，不足千元者以一千元計。為該授信戶於上述總分行機構辦理之擔保放款餘額加上部分擔保、副擔保貸款餘額扣除所提供之擔保品鑑估值後所得之金額，上開所得之金額如小於0，則金額報送"0"。
  *
  * @param amt 貸款餘額扣除擔保品鑑估值之金額
	*/
  public void setAmt(BigDecimal amt) {
    this.amt = amt;
  }

/**
	* 資料所屬年月<br>
	* 請填報本筆授信資料所屬年月
	* @return Integer
	*/
  public int getJcicDataYM() {
    return this.jcicDataYM;
  }

/**
	* 資料所屬年月<br>
	* 請填報本筆授信資料所屬年月
  *
  * @param jcicDataYM 資料所屬年月
	*/
  public void setJcicDataYM(int jcicDataYM) {
    this.jcicDataYM = jcicDataYM;
  }

/**
	* 空白<br>
	* 空白
	* @return String
	*/
  public String getFiller9() {
    return this.filler9 == null ? "" : this.filler9;
  }

/**
	* 空白<br>
	* 空白
  *
  * @param filler9 空白
	*/
  public void setFiller9(String filler9) {
    this.filler9 = filler9;
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
    return "JcicB680 [jcicB680Id=" + jcicB680Id + ", bankItem=" + bankItem + ", branchItem=" + branchItem + ", custIdErr=" + custIdErr
           + ", filler6=" + filler6 + ", amt=" + amt + ", jcicDataYM=" + jcicDataYM
           + ", filler9=" + filler9 + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
