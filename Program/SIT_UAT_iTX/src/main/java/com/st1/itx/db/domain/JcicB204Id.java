package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * JcicB204 聯徵授信餘額日報檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicB204Id implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -3527557387525069300L;

// 資料日期
  @Column(name = "`DataYMD`")
  private int dataYMD = 0;

  // 總行代號
  /* Key,金融機構總機構之代號，三位數字 */
  @Column(name = "`BankItem`", length = 3)
  private String bankItem = " ";

  // 分行代號
  /* Key,金融機構分支機構之代號，四位數字 */
  @Column(name = "`BranchItem`", length = 4)
  private String branchItem = " ";

  // 新增核准額度日期／清償日期／額度到期或解約日期
  /* Key,以'YYYMMDD'(民國)表示 */
  @Column(name = "`DataDate`")
  private int dataDate = 0;

  // 額度控制編碼／帳號
  /* Key,左靠，填報本筆撥款帳號且為一號到底;若僅核准額度尚未動撥，本欄請填授信額度檔之第6欄'本階共用額度控制編碼'。 */
  @Column(name = "`AcctNo`", length = 50)
  private String acctNo = " ";

  // 授信戶IDN/BAN
  /* Key,左靠，身份證或統一證號 */
  @Column(name = "`CustId`", length = 10)
  private String custId = " ";

  // 科目別
  /* Key,請參考附件三授信科目代號表 */
  @Column(name = "`AcctCode`", length = 1)
  private String acctCode = " ";

  // 科目別註記
  /* Key,以 S 註記為十足擔保之授信，辦理保證或承兌業務發生墊款時，以M註記為已墊款有擔保之授信，以N註記為已墊款無擔保之授信...請參考附件三之一科目別註記代號表;如無前述狀況請填X */
  @Column(name = "`SubAcctCode`", length = 1)
  private String subAcctCode = " ";

  // 交易別
  /* L:新增授信額度P:每筆撥款清償後額度未解約(第10欄清償金額需大於0)D:刪除A:每筆撥款清償後額度解約(第10欄清償金額需大於0)B:額度到期或解約(第10欄清償金額需等於0) */
  @Column(name = "`SubTranCode`", length = 1)
  private String subTranCode = " ";

  // 1~7欄資料值相同之交易序號
  /* Key … */
  @Column(name = "`SeqNo`", length = 1)
  private String seqNo = " ";

  public JcicB204Id() {
  }

  public JcicB204Id(int dataYMD, String bankItem, String branchItem, int dataDate, String acctNo, String custId, String acctCode, String subAcctCode, String subTranCode, String seqNo) {
    this.dataYMD = dataYMD;
    this.bankItem = bankItem;
    this.branchItem = branchItem;
    this.dataDate = dataDate;
    this.acctNo = acctNo;
    this.custId = custId;
    this.acctCode = acctCode;
    this.subAcctCode = subAcctCode;
    this.subTranCode = subTranCode;
    this.seqNo = seqNo;
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
	* 新增核准額度日期／清償日期／額度到期或解約日期<br>
	* Key,以'YYYMMDD'(民國)表示
	* @return Integer
	*/
  public int getDataDate() {
    return this.dataDate;
  }

/**
	* 新增核准額度日期／清償日期／額度到期或解約日期<br>
	* Key,以'YYYMMDD'(民國)表示
  *
  * @param dataDate 新增核准額度日期／清償日期／額度到期或解約日期
	*/
  public void setDataDate(int dataDate) {
    this.dataDate = dataDate;
  }

/**
	* 額度控制編碼／帳號<br>
	* Key,左靠，填報本筆撥款帳號且為一號到底;若僅核准額度尚未動撥，本欄請填授信額度檔之第6欄'本階共用額度控制編碼'。
	* @return String
	*/
  public String getAcctNo() {
    return this.acctNo == null ? "" : this.acctNo;
  }

/**
	* 額度控制編碼／帳號<br>
	* Key,左靠，填報本筆撥款帳號且為一號到底;若僅核准額度尚未動撥，本欄請填授信額度檔之第6欄'本階共用額度控制編碼'。
  *
  * @param acctNo 額度控制編碼／帳號
	*/
  public void setAcctNo(String acctNo) {
    this.acctNo = acctNo;
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
	* 科目別<br>
	* Key,請參考附件三授信科目代號表
	* @return String
	*/
  public String getAcctCode() {
    return this.acctCode == null ? "" : this.acctCode;
  }

/**
	* 科目別<br>
	* Key,請參考附件三授信科目代號表
  *
  * @param acctCode 科目別
	*/
  public void setAcctCode(String acctCode) {
    this.acctCode = acctCode;
  }

/**
	* 科目別註記<br>
	* Key,以 S 註記為十足擔保之授信，辦理保證或承兌業務發生墊款時，以M註記為已墊款有擔保之授信，以N註記為已墊款無擔保之授信...
請參考附件三之一科目別註記代號表;
如無前述狀況請填X
	* @return String
	*/
  public String getSubAcctCode() {
    return this.subAcctCode == null ? "" : this.subAcctCode;
  }

/**
	* 科目別註記<br>
	* Key,以 S 註記為十足擔保之授信，辦理保證或承兌業務發生墊款時，以M註記為已墊款有擔保之授信，以N註記為已墊款無擔保之授信...
請參考附件三之一科目別註記代號表;
如無前述狀況請填X
  *
  * @param subAcctCode 科目別註記
	*/
  public void setSubAcctCode(String subAcctCode) {
    this.subAcctCode = subAcctCode;
  }

/**
	* 交易別<br>
	* L:新增授信額度
P:每筆撥款清償後額度未解約(第10欄清償金額需大於0)
D:刪除
A:每筆撥款清償後額度解約(第10欄清償金額需大於0)
B:額度到期或解約(第10欄清償金額需等於0)
	* @return String
	*/
  public String getSubTranCode() {
    return this.subTranCode == null ? "" : this.subTranCode;
  }

/**
	* 交易別<br>
	* L:新增授信額度
P:每筆撥款清償後額度未解約(第10欄清償金額需大於0)
D:刪除
A:每筆撥款清償後額度解約(第10欄清償金額需大於0)
B:額度到期或解約(第10欄清償金額需等於0)
  *
  * @param subTranCode 交易別
	*/
  public void setSubTranCode(String subTranCode) {
    this.subTranCode = subTranCode;
  }

/**
	* 1~7欄資料值相同之交易序號<br>
	* Key …
	* @return String
	*/
  public String getSeqNo() {
    return this.seqNo == null ? "" : this.seqNo;
  }

/**
	* 1~7欄資料值相同之交易序號<br>
	* Key …
  *
  * @param seqNo 1~7欄資料值相同之交易序號
	*/
  public void setSeqNo(String seqNo) {
    this.seqNo = seqNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(dataYMD, bankItem, branchItem, dataDate, acctNo, custId, acctCode, subAcctCode, subTranCode, seqNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    JcicB204Id jcicB204Id = (JcicB204Id) obj;
    return dataYMD == jcicB204Id.dataYMD && bankItem.equals(jcicB204Id.bankItem) && branchItem.equals(jcicB204Id.branchItem) && dataDate == jcicB204Id.dataDate && acctNo.equals(jcicB204Id.acctNo) && custId.equals(jcicB204Id.custId) && acctCode.equals(jcicB204Id.acctCode) && subAcctCode.equals(jcicB204Id.subAcctCode) && subTranCode.equals(jcicB204Id.subTranCode) && seqNo.equals(jcicB204Id.seqNo);
  }

  @Override
  public String toString() {
    return "JcicB204Id [dataYMD=" + dataYMD + ", bankItem=" + bankItem + ", branchItem=" + branchItem + ", dataDate=" + dataDate + ", acctNo=" + acctNo + ", custId=" + custId + ", acctCode=" + acctCode + ", subAcctCode=" + subAcctCode + ", subTranCode=" + subTranCode + ", seqNo=" + seqNo + "]";
  }
}
