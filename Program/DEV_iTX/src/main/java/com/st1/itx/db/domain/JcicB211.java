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
 * JcicB211 聯徵每日授信餘額變動資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicB211`")
public class JcicB211 implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -4316957813523620681L;

@EmbeddedId
  private JcicB211Id jcicB211Id;

  // 資料日期
  @Column(name = "`DataYMD`", insertable = false, updatable = false)
  private int dataYMD = 0;

  // 總行代號
  /* Key,金融機構總機構之代號，三位數字 */
  @Column(name = "`BankItem`", length = 3, insertable = false, updatable = false)
  private String bankItem;

  // 分行代號
  /* Key,金融機構分支機構之代號，四位數字 */
  @Column(name = "`BranchItem`", length = 4, insertable = false, updatable = false)
  private String branchItem;

  // 交易代碼
  /* A 新增 C 異動 D刪除 ;1. 新增資料時填A;2. 修改非key值欄位值時填C; 3. 刪除原報送資料時填D;4. 若為更改Key欄位值，請先以一筆D刪除原報送資料，再以A代碼報送一筆新增(異動後)資料 */
  @Column(name = "`TranCode`", length = 1)
  private String tranCode;

  // 授信戶IDN/BAN
  /* Key,左靠，身份證或統一證號 */
  @Column(name = "`CustId`", length = 10, insertable = false, updatable = false)
  private String custId;

  // 交易屬性
  /* A:本筆撥款 B:本筆還款 */
  @Column(name = "`SubTranCode`", length = 1)
  private String subTranCode;

  // 交易日期
  /* Key,以'YYYMMDD'(民國年)表示 */
  @Column(name = "`AcDate`", insertable = false, updatable = false)
  private int acDate = 0;

  // 本筆撥款／還款帳號
  /* Key, */
  @Column(name = "`AcctNo`", length = 50, insertable = false, updatable = false)
  private String acctNo;

  // 交易內容檔序號
  /* Key,避免資料重複 */
  @Column(name = "`BorxNo`", insertable = false, updatable = false)
  private int borxNo = 0;

  // 本筆撥款／還款金額
  /* 右靠左補0，單位新台幣千元，不足千元以一千元計，填1 */
  @Column(name = "`TxAmt`")
  private BigDecimal txAmt = new BigDecimal("0");

  // 本筆撥款／還款餘額
  /* 右靠左補0，單位新台幣千元，不足千元以一千元計，填1 */
  @Column(name = "`LoanBal`")
  private BigDecimal loanBal = new BigDecimal("0");

  // 本筆還款後之還款紀錄
  /* 參照附件八還款紀錄代號表 */
  @Column(name = "`RepayCode`", length = 1)
  private String repayCode;

  // 本筆還款後之債權結案註記
  @Column(name = "`NegStatus`", length = 3)
  private String negStatus;

  // 科目別
  /* 請參考附件三授信科目代號表 */
  @Column(name = "`AcctCode`", length = 1)
  private String acctCode;

  // 科目別註記
  /* 以 S 註記為十足擔保之授信，辦理保證或承兌業務發生墊款時，以M註記為已墊款有擔保之授信，以N註記為已墊款無擔保之授信，如無前述狀況請填X;若該擔保品於當月處理完畢，則當月份本欄位。屬部分擔保及副擔保之授信，本欄位亦請田X，但仍需將該部分擔保品或副擔保品相關資料填報於第45、46、47欄位;另辦理留學生就學貸款(授信科目Z)業務時，以R註記為留學生就學貸款，以X註記為高級中等以上學校就學貸款，請參考附件三之一科目別註記代號表 */
  @Column(name = "`SubAcctCode`", length = 1)
  private String subAcctCode;

  // 呆帳轉銷年月
  /* 以YYYMM(民國)表示，請填報該筆呆帳之轉銷年月；第12欄科目別填報B：呆帳者，本欄位為必要填報項目(且每月皆需報送)；本欄位如無資料請填空白，勿填0。 */
  @Column(name = "`BadDebtDate`")
  private int badDebtDate = 0;

  // 個人消費性貸款註記
  /* 個人貸款屬消費性貸款者請填Y，屬於非消費性貸款(如非法人組織貸款)請填N，非屬個人貸款填X */
  @Column(name = "`ConsumeFg`", length = 1)
  private String consumeFg;

  // 融資業務分類
  /* 法人特殊融資分為五類 A 專案融資 B 商品融資 C標的融資 D 收益型商用不動產融資 E 高風險商用不動產融資 K 非屬前述特殊融資之其他一般法人金融貸款個人(含非法人組織)金融貸款分類為L 購買住宅貸款(非自用) M 購買住宅貸款(自用) N 一般房屋抵押貸款(房屋修繕貸款) O 由信用卡衍生之小額信用貸款 R 由現金卡衍生之小額信用貸款 S 創業貸款 T 所營事業營運周轉金貸款 U 建築用融資貸款 V 代墊投標保證金貸款 W 農業用途貸款 X 參與都市更新更新計畫貸款 Y 企業員工認購股票(或可轉換公司債)貸款 ,Z 其他個人金融貸款 ,1 個人投資理財貸款 */
  @Column(name = "`FinCode`", length = 1)
  private String finCode;

  // 用途別
  /* 用途別請參照附件五 (1:購置不動產 2:購置動產 3:企業投資 4:週轉金) */
  @Column(name = "`UsageCode`", length = 1)
  private String usageCode;

  // 空白
  /* 備用 */
  @Column(name = "`Filler18`", length = 130)
  private String filler18;

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


  public JcicB211Id getJcicB211Id() {
    return this.jcicB211Id;
  }

  public void setJcicB211Id(JcicB211Id jcicB211Id) {
    this.jcicB211Id = jcicB211Id;
  }

/**
	* 資料日期<br>
	* 
	* @return Integer
	*/
  public int getDataYMD() {
    return this.dataYMD;
  }

/**
	* 資料日期<br>
	* 
  *
  * @param dataYMD 資料日期
	*/
  public void setDataYMD(int dataYMD) {
    this.dataYMD = dataYMD;
  }

/**
	* 總行代號<br>
	* Key,金融機構總機構之代號，三位數字
	* @return String
	*/
  public String getBankItem() {
    return this.bankItem == null ? "" : this.bankItem;
  }

/**
	* 總行代號<br>
	* Key,金融機構總機構之代號，三位數字
  *
  * @param bankItem 總行代號
	*/
  public void setBankItem(String bankItem) {
    this.bankItem = bankItem;
  }

/**
	* 分行代號<br>
	* Key,金融機構分支機構之代號，四位數字
	* @return String
	*/
  public String getBranchItem() {
    return this.branchItem == null ? "" : this.branchItem;
  }

/**
	* 分行代號<br>
	* Key,金融機構分支機構之代號，四位數字
  *
  * @param branchItem 分行代號
	*/
  public void setBranchItem(String branchItem) {
    this.branchItem = branchItem;
  }

/**
	* 交易代碼<br>
	* A 新增 C 異動 D刪除 ;
1. 新增資料時填A;
2. 修改非key值欄位值時填C; 
3. 刪除原報送資料時填D;
4. 若為更改Key欄位值，請先以一筆D刪除原報送資料，再以A代碼報送一筆新增(異動後)資料
	* @return String
	*/
  public String getTranCode() {
    return this.tranCode == null ? "" : this.tranCode;
  }

/**
	* 交易代碼<br>
	* A 新增 C 異動 D刪除 ;
1. 新增資料時填A;
2. 修改非key值欄位值時填C; 
3. 刪除原報送資料時填D;
4. 若為更改Key欄位值，請先以一筆D刪除原報送資料，再以A代碼報送一筆新增(異動後)資料
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
	* 交易屬性<br>
	* A:本筆撥款 B:本筆還款
	* @return String
	*/
  public String getSubTranCode() {
    return this.subTranCode == null ? "" : this.subTranCode;
  }

/**
	* 交易屬性<br>
	* A:本筆撥款 B:本筆還款
  *
  * @param subTranCode 交易屬性
	*/
  public void setSubTranCode(String subTranCode) {
    this.subTranCode = subTranCode;
  }

/**
	* 交易日期<br>
	* Key,以'YYYMMDD'(民國年)表示
	* @return Integer
	*/
  public int getAcDate() {
    return this.acDate;
  }

/**
	* 交易日期<br>
	* Key,以'YYYMMDD'(民國年)表示
  *
  * @param acDate 交易日期
	*/
  public void setAcDate(int acDate) {
    this.acDate = acDate;
  }

/**
	* 本筆撥款／還款帳號<br>
	* Key,
	* @return String
	*/
  public String getAcctNo() {
    return this.acctNo == null ? "" : this.acctNo;
  }

/**
	* 本筆撥款／還款帳號<br>
	* Key,
  *
  * @param acctNo 本筆撥款／還款帳號
	*/
  public void setAcctNo(String acctNo) {
    this.acctNo = acctNo;
  }

/**
	* 交易內容檔序號<br>
	* Key,避免資料重複
	* @return Integer
	*/
  public int getBorxNo() {
    return this.borxNo;
  }

/**
	* 交易內容檔序號<br>
	* Key,避免資料重複
  *
  * @param borxNo 交易內容檔序號
	*/
  public void setBorxNo(int borxNo) {
    this.borxNo = borxNo;
  }

/**
	* 本筆撥款／還款金額<br>
	* 右靠左補0，單位新台幣千元，不足千元以一千元計，填1
	* @return BigDecimal
	*/
  public BigDecimal getTxAmt() {
    return this.txAmt;
  }

/**
	* 本筆撥款／還款金額<br>
	* 右靠左補0，單位新台幣千元，不足千元以一千元計，填1
  *
  * @param txAmt 本筆撥款／還款金額
	*/
  public void setTxAmt(BigDecimal txAmt) {
    this.txAmt = txAmt;
  }

/**
	* 本筆撥款／還款餘額<br>
	* 右靠左補0，單位新台幣千元，不足千元以一千元計，填1
	* @return BigDecimal
	*/
  public BigDecimal getLoanBal() {
    return this.loanBal;
  }

/**
	* 本筆撥款／還款餘額<br>
	* 右靠左補0，單位新台幣千元，不足千元以一千元計，填1
  *
  * @param loanBal 本筆撥款／還款餘額
	*/
  public void setLoanBal(BigDecimal loanBal) {
    this.loanBal = loanBal;
  }

/**
	* 本筆還款後之還款紀錄<br>
	* 參照附件八還款紀錄代號表
	* @return String
	*/
  public String getRepayCode() {
    return this.repayCode == null ? "" : this.repayCode;
  }

/**
	* 本筆還款後之還款紀錄<br>
	* 參照附件八還款紀錄代號表
  *
  * @param repayCode 本筆還款後之還款紀錄
	*/
  public void setRepayCode(String repayCode) {
    this.repayCode = repayCode;
  }

/**
	* 本筆還款後之債權結案註記<br>
	* 
	* @return String
	*/
  public String getNegStatus() {
    return this.negStatus == null ? "" : this.negStatus;
  }

/**
	* 本筆還款後之債權結案註記<br>
	* 
  *
  * @param negStatus 本筆還款後之債權結案註記
	*/
  public void setNegStatus(String negStatus) {
    this.negStatus = negStatus;
  }

/**
	* 科目別<br>
	* 請參考附件三授信科目代號表
	* @return String
	*/
  public String getAcctCode() {
    return this.acctCode == null ? "" : this.acctCode;
  }

/**
	* 科目別<br>
	* 請參考附件三授信科目代號表
  *
  * @param acctCode 科目別
	*/
  public void setAcctCode(String acctCode) {
    this.acctCode = acctCode;
  }

/**
	* 科目別註記<br>
	* 以 S 註記為十足擔保之授信，辦理保證或承兌業務發生墊款時，以M註記為已墊款有擔保之授信，以N註記為已墊款無擔保之授信，如無前述狀況請填X;若該擔保品於當月處理完畢，則當月份本欄位。屬部分擔保及副擔保之授信，本欄位亦請田X，但仍需將該部分擔保品或副擔保品相關資料填報於第45、46、47欄位;另辦理留學生就學貸款(授信科目Z)業務時，以R註記為留學生就學貸款，以X註記為高級中等以上學校就學貸款，請參考附件三之一科目別註記代號表
	* @return String
	*/
  public String getSubAcctCode() {
    return this.subAcctCode == null ? "" : this.subAcctCode;
  }

/**
	* 科目別註記<br>
	* 以 S 註記為十足擔保之授信，辦理保證或承兌業務發生墊款時，以M註記為已墊款有擔保之授信，以N註記為已墊款無擔保之授信，如無前述狀況請填X;若該擔保品於當月處理完畢，則當月份本欄位。屬部分擔保及副擔保之授信，本欄位亦請田X，但仍需將該部分擔保品或副擔保品相關資料填報於第45、46、47欄位;另辦理留學生就學貸款(授信科目Z)業務時，以R註記為留學生就學貸款，以X註記為高級中等以上學校就學貸款，請參考附件三之一科目別註記代號表
  *
  * @param subAcctCode 科目別註記
	*/
  public void setSubAcctCode(String subAcctCode) {
    this.subAcctCode = subAcctCode;
  }

/**
	* 呆帳轉銷年月<br>
	* 以YYYMM(民國)表示，請填報該筆呆帳之轉銷年月；第12欄科目別填報B：呆帳者，本欄位為必要填報項目(且每月皆需報送)；本欄位如無資料請填空白，勿填0。
	* @return Integer
	*/
  public int getBadDebtDate() {
    return this.badDebtDate;
  }

/**
	* 呆帳轉銷年月<br>
	* 以YYYMM(民國)表示，請填報該筆呆帳之轉銷年月；第12欄科目別填報B：呆帳者，本欄位為必要填報項目(且每月皆需報送)；本欄位如無資料請填空白，勿填0。
  *
  * @param badDebtDate 呆帳轉銷年月
	*/
  public void setBadDebtDate(int badDebtDate) {
    this.badDebtDate = badDebtDate;
  }

/**
	* 個人消費性貸款註記<br>
	* 個人貸款屬消費性貸款者請填Y，屬於非消費性貸款(如非法人組織貸款)請填N，非屬個人貸款填X
	* @return String
	*/
  public String getConsumeFg() {
    return this.consumeFg == null ? "" : this.consumeFg;
  }

/**
	* 個人消費性貸款註記<br>
	* 個人貸款屬消費性貸款者請填Y，屬於非消費性貸款(如非法人組織貸款)請填N，非屬個人貸款填X
  *
  * @param consumeFg 個人消費性貸款註記
	*/
  public void setConsumeFg(String consumeFg) {
    this.consumeFg = consumeFg;
  }

/**
	* 融資業務分類<br>
	* 法人特殊融資分為五類 A 專案融資 B 商品融資 C標的融資 D 收益型商用不動產融資 E 高風險商用不動產融資 K 非屬前述特殊融資之其他一般法人金融貸款
個人(含非法人組織)金融貸款分類為L 購買住宅貸款(非自用) M 購買住宅貸款(自用) N 一般房屋抵押貸款(房屋修繕貸款) O 由信用卡衍生之小額信用貸款 R 由現金卡衍生之小額信用貸款 S 創業貸款 T 所營事業營運周轉金貸款 U 建築用融資貸款 V 代墊投標保證金貸款 W 農業用途貸款 X 參與都市更新更新計畫貸款 Y 企業員工認購股票(或可轉換公司債)貸款 ,Z 其他個人金融貸款 ,1 個人投資理財貸款
	* @return String
	*/
  public String getFinCode() {
    return this.finCode == null ? "" : this.finCode;
  }

/**
	* 融資業務分類<br>
	* 法人特殊融資分為五類 A 專案融資 B 商品融資 C標的融資 D 收益型商用不動產融資 E 高風險商用不動產融資 K 非屬前述特殊融資之其他一般法人金融貸款
個人(含非法人組織)金融貸款分類為L 購買住宅貸款(非自用) M 購買住宅貸款(自用) N 一般房屋抵押貸款(房屋修繕貸款) O 由信用卡衍生之小額信用貸款 R 由現金卡衍生之小額信用貸款 S 創業貸款 T 所營事業營運周轉金貸款 U 建築用融資貸款 V 代墊投標保證金貸款 W 農業用途貸款 X 參與都市更新更新計畫貸款 Y 企業員工認購股票(或可轉換公司債)貸款 ,Z 其他個人金融貸款 ,1 個人投資理財貸款
  *
  * @param finCode 融資業務分類
	*/
  public void setFinCode(String finCode) {
    this.finCode = finCode;
  }

/**
	* 用途別<br>
	* 用途別請參照附件五 (1:購置不動產 2:購置動產 3:企業投資 4:週轉金)
	* @return String
	*/
  public String getUsageCode() {
    return this.usageCode == null ? "" : this.usageCode;
  }

/**
	* 用途別<br>
	* 用途別請參照附件五 (1:購置不動產 2:購置動產 3:企業投資 4:週轉金)
  *
  * @param usageCode 用途別
	*/
  public void setUsageCode(String usageCode) {
    this.usageCode = usageCode;
  }

/**
	* 空白<br>
	* 備用
	* @return String
	*/
  public String getFiller18() {
    return this.filler18 == null ? "" : this.filler18;
  }

/**
	* 空白<br>
	* 備用
  *
  * @param filler18 空白
	*/
  public void setFiller18(String filler18) {
    this.filler18 = filler18;
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
    return "JcicB211 [jcicB211Id=" + jcicB211Id + ", tranCode=" + tranCode + ", subTranCode=" + subTranCode
           + ", txAmt=" + txAmt + ", loanBal=" + loanBal + ", repayCode=" + repayCode
           + ", negStatus=" + negStatus + ", acctCode=" + acctCode + ", subAcctCode=" + subAcctCode + ", badDebtDate=" + badDebtDate + ", consumeFg=" + consumeFg + ", finCode=" + finCode
           + ", usageCode=" + usageCode + ", filler18=" + filler18 + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
           + "]";
  }
}
