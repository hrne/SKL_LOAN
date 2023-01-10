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
 * BankRemit 撥款匯款檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`BankRemit`")
public class BankRemit implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -363471675307099233L;

@EmbeddedId
  private BankRemitId bankRemitId;

  // 會計日期
  @Column(name = "`AcDate`", insertable = false, updatable = false)
  private int acDate = 0;

  // 經辦
  @Column(name = "`TitaTlrNo`", length = 6, insertable = false, updatable = false)
  private String titaTlrNo;

  // 交易序號
  @Column(name = "`TitaTxtNo`", length = 8, insertable = false, updatable = false)
  private String titaTxtNo;

  // 流水號
  /* 舊資料有多筆同一筆會計日期+經辦+交易序號有多筆 */
  @Column(name = "`Seq`", insertable = false, updatable = false)
  private int seq = 0;

  // 整批批號
  /* "LN + 傳票批號 + 匯款批號 */
  @Column(name = "`BatchNo`", length = 6)
  private String batchNo;

  // 撥款方式
  /* CdCode.DrawdownCode201:撥款(整批匯款)02:撥款(單筆匯款)04:退款台新(存款憑條)05:退款他行(整批匯款)11:退款新光(存款憑條) */
  @Column(name = "`DrawdownCode`")
  private int drawdownCode = 0;

  // 狀態
  /* CdCode.DrawdownStatus0:正常1:未放行(ActFg=1時顯示用)2:產檔後訂正3:產檔後修正4:產檔後改單筆匯款5:被修正或訂正(顯示用) */
  @Column(name = "`StatusCode`")
  private int statusCode = 0;

  // 匯款銀行
  @Column(name = "`RemitBank`", length = 3)
  private String remitBank;

  // 匯款分行
  @Column(name = "`RemitBranch`", length = 4)
  private String remitBranch;

  // 匯款帳號
  @Column(name = "`RemitAcctNo`", length = 14)
  private String remitAcctNo;

  // 收款戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 撥款序號
  @Column(name = "`BormNo`")
  private int bormNo = 0;

  // 收款戶名
  @Column(name = "`CustName`", length = 100)
  private String custName;

  // 收款人ID
  @Column(name = "`CustId`", length = 10)
  private String custId;

  // 收款人出生日期
  @Column(name = "`CustBirthday`")
  private int custBirthday = 0;

  // 收款人性別
  @Column(name = "`CustGender`", length = 1)
  private String custGender;

  // 附言
  @Column(name = "`Remark`", length = 100)
  private String remark;

  // 幣別
  @Column(name = "`CurrencyCode`", length = 3)
  private String currencyCode;

  // 匯款金額
  @Column(name = "`RemitAmt`")
  private BigDecimal remitAmt = new BigDecimal("0");

  // AML回應碼
  /* CdCode.AmlCheckItem0:非可疑名單/已完成名單確認1:需審查/確認2:為凍結名單/未確定名單 */
  @Column(name = "`AmlRsp`", length = 1)
  private String amlRsp;

  // 交易進行記號
  /* 0:1STEP TX (from eloan)1/2:2STEP TX */
  @Column(name = "`ActFg`")
  private int actFg = 0;

  // 產檔後修正後內容
  /* json 格式 */
  @Column(name = "`ModifyContent`", length = 500)
  private String modifyContent;

  // 付款狀況碼
  /* CdCode.PayCodeI:建檔O:確認C:建檔取消A:已開票D:支票到期B:退件S:請款單位擋匯T:網銀擋匯W:匯款途中F:匯款失敗H:支票兌領/匯款成功R:退匯/退回情況改變成作廢L:支票掛失V:支票作廢J:空白票遺失Z:逾一年未兌領Y:逾二年未兌領P:繳款成功X:付款沖回Q:AML相似名單確認中U:禁止交易(AML) */
  @Column(name = "`PayCode`", length = 1)
  private String payCode;

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


  public BankRemitId getBankRemitId() {
    return this.bankRemitId;
  }

  public void setBankRemitId(BankRemitId bankRemitId) {
    this.bankRemitId = bankRemitId;
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
	* 經辦<br>
	* 
	* @return String
	*/
  public String getTitaTlrNo() {
    return this.titaTlrNo == null ? "" : this.titaTlrNo;
  }

/**
	* 經辦<br>
	* 
  *
  * @param titaTlrNo 經辦
	*/
  public void setTitaTlrNo(String titaTlrNo) {
    this.titaTlrNo = titaTlrNo;
  }

/**
	* 交易序號<br>
	* 
	* @return String
	*/
  public String getTitaTxtNo() {
    return this.titaTxtNo == null ? "" : this.titaTxtNo;
  }

/**
	* 交易序號<br>
	* 
  *
  * @param titaTxtNo 交易序號
	*/
  public void setTitaTxtNo(String titaTxtNo) {
    this.titaTxtNo = titaTxtNo;
  }

/**
	* 流水號<br>
	* 舊資料有多筆同一筆會計日期+經辦+交易序號有多筆
	* @return Integer
	*/
  public int getSeq() {
    return this.seq;
  }

/**
	* 流水號<br>
	* 舊資料有多筆同一筆會計日期+經辦+交易序號有多筆
  *
  * @param seq 流水號
	*/
  public void setSeq(int seq) {
    this.seq = seq;
  }

/**
	* 整批批號<br>
	* "LN + 傳票批號 + 匯款批號
	* @return String
	*/
  public String getBatchNo() {
    return this.batchNo == null ? "" : this.batchNo;
  }

/**
	* 整批批號<br>
	* "LN + 傳票批號 + 匯款批號
  *
  * @param batchNo 整批批號
	*/
  public void setBatchNo(String batchNo) {
    this.batchNo = batchNo;
  }

/**
	* 撥款方式<br>
	* CdCode.DrawdownCode2
01:撥款(整批匯款)
02:撥款(單筆匯款)
04:退款台新(存款憑條)
05:退款他行(整批匯款)
11:退款新光(存款憑條)
	* @return Integer
	*/
  public int getDrawdownCode() {
    return this.drawdownCode;
  }

/**
	* 撥款方式<br>
	* CdCode.DrawdownCode2
01:撥款(整批匯款)
02:撥款(單筆匯款)
04:退款台新(存款憑條)
05:退款他行(整批匯款)
11:退款新光(存款憑條)
  *
  * @param drawdownCode 撥款方式
	*/
  public void setDrawdownCode(int drawdownCode) {
    this.drawdownCode = drawdownCode;
  }

/**
	* 狀態<br>
	* CdCode.DrawdownStatus
0:正常
1:未放行(ActFg=1時顯示用)
2:產檔後訂正
3:產檔後修正
4:產檔後改單筆匯款
5:被修正或訂正(顯示用)
	* @return Integer
	*/
  public int getStatusCode() {
    return this.statusCode;
  }

/**
	* 狀態<br>
	* CdCode.DrawdownStatus
0:正常
1:未放行(ActFg=1時顯示用)
2:產檔後訂正
3:產檔後修正
4:產檔後改單筆匯款
5:被修正或訂正(顯示用)
  *
  * @param statusCode 狀態
	*/
  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

/**
	* 匯款銀行<br>
	* 
	* @return String
	*/
  public String getRemitBank() {
    return this.remitBank == null ? "" : this.remitBank;
  }

/**
	* 匯款銀行<br>
	* 
  *
  * @param remitBank 匯款銀行
	*/
  public void setRemitBank(String remitBank) {
    this.remitBank = remitBank;
  }

/**
	* 匯款分行<br>
	* 
	* @return String
	*/
  public String getRemitBranch() {
    return this.remitBranch == null ? "" : this.remitBranch;
  }

/**
	* 匯款分行<br>
	* 
  *
  * @param remitBranch 匯款分行
	*/
  public void setRemitBranch(String remitBranch) {
    this.remitBranch = remitBranch;
  }

/**
	* 匯款帳號<br>
	* 
	* @return String
	*/
  public String getRemitAcctNo() {
    return this.remitAcctNo == null ? "" : this.remitAcctNo;
  }

/**
	* 匯款帳號<br>
	* 
  *
  * @param remitAcctNo 匯款帳號
	*/
  public void setRemitAcctNo(String remitAcctNo) {
    this.remitAcctNo = remitAcctNo;
  }

/**
	* 收款戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 收款戶號<br>
	* 
  *
  * @param custNo 收款戶號
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
	* 收款戶名<br>
	* 
	* @return String
	*/
  public String getCustName() {
    return this.custName == null ? "" : this.custName;
  }

/**
	* 收款戶名<br>
	* 
  *
  * @param custName 收款戶名
	*/
  public void setCustName(String custName) {
    this.custName = custName;
  }

/**
	* 收款人ID<br>
	* 
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 收款人ID<br>
	* 
  *
  * @param custId 收款人ID
	*/
  public void setCustId(String custId) {
    this.custId = custId;
  }

/**
	* 收款人出生日期<br>
	* 
	* @return Integer
	*/
  public int getCustBirthday() {
    return StaticTool.bcToRoc(this.custBirthday);
  }

/**
	* 收款人出生日期<br>
	* 
  *
  * @param custBirthday 收款人出生日期
  * @throws LogicException when Date Is Warn	*/
  public void setCustBirthday(int custBirthday) throws LogicException {
    this.custBirthday = StaticTool.rocToBc(custBirthday);
  }

/**
	* 收款人性別<br>
	* 
	* @return String
	*/
  public String getCustGender() {
    return this.custGender == null ? "" : this.custGender;
  }

/**
	* 收款人性別<br>
	* 
  *
  * @param custGender 收款人性別
	*/
  public void setCustGender(String custGender) {
    this.custGender = custGender;
  }

/**
	* 附言<br>
	* 
	* @return String
	*/
  public String getRemark() {
    return this.remark == null ? "" : this.remark;
  }

/**
	* 附言<br>
	* 
  *
  * @param remark 附言
	*/
  public void setRemark(String remark) {
    this.remark = remark;
  }

/**
	* 幣別<br>
	* 
	* @return String
	*/
  public String getCurrencyCode() {
    return this.currencyCode == null ? "" : this.currencyCode;
  }

/**
	* 幣別<br>
	* 
  *
  * @param currencyCode 幣別
	*/
  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

/**
	* 匯款金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getRemitAmt() {
    return this.remitAmt;
  }

/**
	* 匯款金額<br>
	* 
  *
  * @param remitAmt 匯款金額
	*/
  public void setRemitAmt(BigDecimal remitAmt) {
    this.remitAmt = remitAmt;
  }

/**
	* AML回應碼<br>
	* CdCode.AmlCheckItem
0:非可疑名單/已完成名單確認
1:需審查/確認
2:為凍結名單/未確定名單
	* @return String
	*/
  public String getAmlRsp() {
    return this.amlRsp == null ? "" : this.amlRsp;
  }

/**
	* AML回應碼<br>
	* CdCode.AmlCheckItem
0:非可疑名單/已完成名單確認
1:需審查/確認
2:為凍結名單/未確定名單
  *
  * @param amlRsp AML回應碼
	*/
  public void setAmlRsp(String amlRsp) {
    this.amlRsp = amlRsp;
  }

/**
	* 交易進行記號<br>
	* 0:1STEP TX (from eloan)
1/2:2STEP TX
	* @return Integer
	*/
  public int getActFg() {
    return this.actFg;
  }

/**
	* 交易進行記號<br>
	* 0:1STEP TX (from eloan)
1/2:2STEP TX
  *
  * @param actFg 交易進行記號
	*/
  public void setActFg(int actFg) {
    this.actFg = actFg;
  }

/**
	* 產檔後修正後內容<br>
	* json 格式
	* @return String
	*/
  public String getModifyContent() {
    return this.modifyContent == null ? "" : this.modifyContent;
  }

/**
	* 產檔後修正後內容<br>
	* json 格式
  *
  * @param modifyContent 產檔後修正後內容
	*/
  public void setModifyContent(String modifyContent) {
    this.modifyContent = modifyContent;
  }

/**
	* 付款狀況碼<br>
	* CdCode.PayCode
I:建檔
O:確認
C:建檔取消
A:已開票
D:支票到期
B:退件
S:請款單位擋匯
T:網銀擋匯
W:匯款途中
F:匯款失敗
H:支票兌領/匯款成功
R:退匯/退回情況改變成作廢
L:支票掛失
V:支票作廢
J:空白票遺失
Z:逾一年未兌領
Y:逾二年未兌領
P:繳款成功
X:付款沖回
Q:AML相似名單確認中
U:禁止交易(AML)
	* @return String
	*/
  public String getPayCode() {
    return this.payCode == null ? "" : this.payCode;
  }

/**
	* 付款狀況碼<br>
	* CdCode.PayCode
I:建檔
O:確認
C:建檔取消
A:已開票
D:支票到期
B:退件
S:請款單位擋匯
T:網銀擋匯
W:匯款途中
F:匯款失敗
H:支票兌領/匯款成功
R:退匯/退回情況改變成作廢
L:支票掛失
V:支票作廢
J:空白票遺失
Z:逾一年未兌領
Y:逾二年未兌領
P:繳款成功
X:付款沖回
Q:AML相似名單確認中
U:禁止交易(AML)
  *
  * @param payCode 付款狀況碼
	*/
  public void setPayCode(String payCode) {
    this.payCode = payCode;
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
    return "BankRemit [bankRemitId=" + bankRemitId + ", batchNo=" + batchNo + ", drawdownCode=" + drawdownCode
           + ", statusCode=" + statusCode + ", remitBank=" + remitBank + ", remitBranch=" + remitBranch + ", remitAcctNo=" + remitAcctNo + ", custNo=" + custNo + ", facmNo=" + facmNo
           + ", bormNo=" + bormNo + ", custName=" + custName + ", custId=" + custId + ", custBirthday=" + custBirthday + ", custGender=" + custGender + ", remark=" + remark
           + ", currencyCode=" + currencyCode + ", remitAmt=" + remitAmt + ", amlRsp=" + amlRsp + ", actFg=" + actFg + ", modifyContent=" + modifyContent + ", payCode=" + payCode
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
