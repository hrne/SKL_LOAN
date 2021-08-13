package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * JcicB201 聯徵授信餘額月報資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicB201Id implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -6803270984784236141L;

// 資料年月
  @Column(name = "`DataYM`")
  private int dataYM = 0;

  // 總行代號
  /* Key,金融機構總機構之代號，三位數字 */
  @Column(name = "`BankItem`", length = 3)
  private String bankItem = " ";

  // 分行代號
  /* Key,金融機構分支機構之代號，四位數字 */
  @Column(name = "`BranchItem`", length = 4)
  private String branchItem = " ";

  // 交易代碼
  /* A 新增 C 異動 D刪除 ;每月正常報送資料(尚未上線時)僅限填A 代碼;發函更正(已上線)授信資料時，依下列狀況填A C D代碼：1. 新增資料時填A;2. 修改非key值欄位值時填C; 3. 刪除原報送資料時填D;4. 若為更改Key欄位值，請先以一筆D刪除原報送資料，再以A代碼報送一筆新增(異動後)資料 */
  @Column(name = "`TranCode`", length = 1)
  private String tranCode = " ";

  // 帳號屬性註記
  /* A 本月新增帳號(非上月轉換而來)C 本月轉換帳號(如填報C，請務必填報帳號轉換檔)X 舊有帳號 */
  @Column(name = "`SubTranCode`", length = 1)
  private String subTranCode = " ";

  // 本筆撥款帳號
  /* Key,左靠，填報本筆授信撥款帳號或交易序號，該帳號在同一金融機構內需為唯一(不可重複)，且一號到底(每月同筆撥款帳號均為相同號碼);若帳號無法定義至每筆撥款，如循環信用之存融、現金卡放款、透支戶及理財型房貸…等，則可定義為對應該筆撥款額度項下之帳號(可為契約或批覆書號碼)前揭循環信用業務若有另產生放款帳號，則以該放款帳號填報，若無產生放款帳號，則請填報原存款帳號。如為票券公司可以 成交單號 報送本欄位。若有部分轉催、部分轉呆或金融機構合併或轉籍之狀況，其帳號均不可重複，若其帳號無法一號到底需轉換帳號時，請於第3.2欄填報C(本月轉換帳號)，並務必另填報帳號轉換檔。 */
  @Column(name = "`AcctNo`", length = 50)
  private String acctNo = " ";

  // 本筆撥款帳號序號
  /* 1=關係人5位以內2=關係人5位以上記在第2筆 */
  @Column(name = "`SeqNo`")
  private int seqNo = 0;

  public JcicB201Id() {
  }

  public JcicB201Id(int dataYM, String bankItem, String branchItem, String tranCode, String subTranCode, String acctNo, int seqNo) {
    this.dataYM = dataYM;
    this.bankItem = bankItem;
    this.branchItem = branchItem;
    this.tranCode = tranCode;
    this.subTranCode = subTranCode;
    this.acctNo = acctNo;
    this.seqNo = seqNo;
  }

/**
	* 資料年月<br>
	* 
	* @return Integer
	*/
  public int getDataYM() {
    return this.dataYM;
  }

/**
	* 資料年月<br>
	* 
  *
  * @param dataYM 資料年月
	*/
  public void setDataYM(int dataYM) {
    this.dataYM = dataYM;
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
每月正常報送資料(尚未上線時)僅限填A 代碼;發函更正(已上線)授信資料時，依下列狀況填A C D代碼：
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
每月正常報送資料(尚未上線時)僅限填A 代碼;發函更正(已上線)授信資料時，依下列狀況填A C D代碼：
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
	* 帳號屬性註記<br>
	* A 本月新增帳號(非上月轉換而來)
C 本月轉換帳號(如填報C，請務必填報帳號轉換檔)
X 舊有帳號
	* @return String
	*/
  public String getSubTranCode() {
    return this.subTranCode == null ? "" : this.subTranCode;
  }

/**
	* 帳號屬性註記<br>
	* A 本月新增帳號(非上月轉換而來)
C 本月轉換帳號(如填報C，請務必填報帳號轉換檔)
X 舊有帳號
  *
  * @param subTranCode 帳號屬性註記
	*/
  public void setSubTranCode(String subTranCode) {
    this.subTranCode = subTranCode;
  }

/**
	* 本筆撥款帳號<br>
	* Key,左靠，填報本筆授信撥款帳號或交易序號，該帳號在同一金融機構內需為唯一(不可重複)，且一號到底(每月同筆撥款帳號均為相同號碼);若帳號無法定義至每筆撥款，如循環信用之存融、現金卡放款、透支戶及理財型房貸…等，則可定義為對應該筆撥款額度項下之帳號(可為契約或批覆書號碼)前揭循環信用業務若有另產生放款帳號，則以該放款帳號填報，若無產生放款帳號，則請填報原存款帳號。如為票券公司可以 成交單號 報送本欄位。若有部分轉催、部分轉呆或金融機構合併或轉籍之狀況，其帳號均不可重複，若其帳號無法一號到底需轉換帳號時，請於第3.2欄填報C(本月轉換帳號)，並務必另填報帳號轉換檔。
	* @return String
	*/
  public String getAcctNo() {
    return this.acctNo == null ? "" : this.acctNo;
  }

/**
	* 本筆撥款帳號<br>
	* Key,左靠，填報本筆授信撥款帳號或交易序號，該帳號在同一金融機構內需為唯一(不可重複)，且一號到底(每月同筆撥款帳號均為相同號碼);若帳號無法定義至每筆撥款，如循環信用之存融、現金卡放款、透支戶及理財型房貸…等，則可定義為對應該筆撥款額度項下之帳號(可為契約或批覆書號碼)前揭循環信用業務若有另產生放款帳號，則以該放款帳號填報，若無產生放款帳號，則請填報原存款帳號。如為票券公司可以 成交單號 報送本欄位。若有部分轉催、部分轉呆或金融機構合併或轉籍之狀況，其帳號均不可重複，若其帳號無法一號到底需轉換帳號時，請於第3.2欄填報C(本月轉換帳號)，並務必另填報帳號轉換檔。
  *
  * @param acctNo 本筆撥款帳號
	*/
  public void setAcctNo(String acctNo) {
    this.acctNo = acctNo;
  }

/**
	* 本筆撥款帳號序號<br>
	* 1=關係人5位以內
2=關係人5位以上記在第2筆
	* @return Integer
	*/
  public int getSeqNo() {
    return this.seqNo;
  }

/**
	* 本筆撥款帳號序號<br>
	* 1=關係人5位以內
2=關係人5位以上記在第2筆
  *
  * @param seqNo 本筆撥款帳號序號
	*/
  public void setSeqNo(int seqNo) {
    this.seqNo = seqNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(dataYM, bankItem, branchItem, tranCode, subTranCode, acctNo, seqNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    JcicB201Id jcicB201Id = (JcicB201Id) obj;
    return dataYM == jcicB201Id.dataYM && bankItem.equals(jcicB201Id.bankItem) && branchItem.equals(jcicB201Id.branchItem) && tranCode.equals(jcicB201Id.tranCode) && subTranCode.equals(jcicB201Id.subTranCode) && acctNo.equals(jcicB201Id.acctNo) && seqNo == jcicB201Id.seqNo;
  }

  @Override
  public String toString() {
    return "JcicB201Id [dataYM=" + dataYM + ", bankItem=" + bankItem + ", branchItem=" + branchItem + ", tranCode=" + tranCode + ", subTranCode=" + subTranCode + ", acctNo=" + acctNo + ", seqNo=" + seqNo + "]";
  }
}
