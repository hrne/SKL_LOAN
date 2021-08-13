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

/**
 * JcicB093 聯徵動產及貴重物品擔保品明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicB093`")
public class JcicB093 implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 179525591747722926L;

@EmbeddedId
  private JcicB093Id jcicB093Id;

  // 資料日期
  @Column(name = "`DataYM`", insertable = false, updatable = false)
  private int dataYM = 0;

  // 資料別
  /* 93:動產及貴重物品擔保品明細檔資料 */
  @Column(name = "`DataType`", length = 2)
  private String dataType;

  // 總行代號
  /* 金融機構總機構之代號，三位數字 */
  @Column(name = "`BankItem`", length = 3)
  private String bankItem;

  // 分行代號
  /* 金融機構分支機構之代號，四位數字 */
  @Column(name = "`BranchItem`", length = 4)
  private String branchItem;

  // 空白
  /* 空白 */
  @Column(name = "`Filler4`", length = 2)
  private String filler4;

  // 擔保品控制編碼
  /* Key,左靠右補空白 */
  @Column(name = "`ClActNo`", length = 50, insertable = false, updatable = false)
  private String clActNo;

  // 擔保品類別
  /* 擔保品類別代號．附件9 */
  @Column(name = "`ClTypeJCIC`", length = 2)
  private String clTypeJCIC;

  // 擔保品所有權人或代表人IDN/BAN
  /* 左靠，身份證或統一證號 */
  @Column(name = "`OwnerId`", length = 10)
  private String ownerId;

  // 鑑估值
  /* 右靠，左補0，單位新台幣千元，最新鑑價金額 */
  @Column(name = "`EvaAmt`")
  private int evaAmt = 0;

  // 鑑估日期
  /* 擔保品鑑估年月，以YYYYMM(民國年)表示；重新鑑價，請填報最新鑑價日期 */
  @Column(name = "`EvaDate`")
  private int evaDate = 0;

  // 可放款值
  /* 右靠，左補0，單位新台幣千元，鑑估值 * 可貸放成數 */
  @Column(name = "`LoanLimitAmt`")
  private int loanLimitAmt = 0;

  // 設定日期
  /* 動產設定年月，以YYYYMM(民國年)表示 */
  @Column(name = "`SettingDate`")
  private int settingDate = 0;

  // 本行本月設定金額
  /* 右靠，左補0，單位新台幣千元，本月新增設定總金額，如無則填0 */
  @Column(name = "`MonthSettingAmt`")
  private int monthSettingAmt = 0;

  // 本月設定抵押順位
  /* 本月如有設定，填報本月設定之設定順位 */
  @Column(name = "`SettingSeq`")
  private int settingSeq = 0;

  // 本行累計已設定總金額
  /* 右靠，左補0，單位新台幣千元，至本月底該動產本行已設定總金額 */
  @Column(name = "`SettingAmt`")
  private int settingAmt = 0;

  // 其他債權人前已設定金額
  /* 右靠，左補0，單位新台幣千元，如無則填0 */
  @Column(name = "`PreSettingAmt`")
  private int preSettingAmt = 0;

  // 處分價格
  /* 八位數字，右靠，左補0，單位新台幣千元，填報該擔保品至本月底前已被處分之價格金額，如無則填0 */
  @Column(name = "`DispPrice`")
  private int dispPrice = 0;

  // 權利到期年月
  /* YYYYMM(民國年)，如無到期年月，請填99999 */
  @Column(name = "`IssueEndDate`")
  private int issueEndDate = 0;

  // 是否有保險
  /* Y=是，N=否 */
  @Column(name = "`InsuFg`", length = 1)
  private String insuFg;

  // 空白
  /* 備用 */
  @Column(name = "`Filler19`", length = 17)
  private String filler19;

  // 資料所屬年月
  /* 請填報本筆授信資料所屬年月(民國年) */
  @Column(name = "`JcicDataYM`")
  private int jcicDataYM = 0;

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


  public JcicB093Id getJcicB093Id() {
    return this.jcicB093Id;
  }

  public void setJcicB093Id(JcicB093Id jcicB093Id) {
    this.jcicB093Id = jcicB093Id;
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
	* 資料別<br>
	* 93:動產及貴重物品擔保品明細檔資料
	* @return String
	*/
  public String getDataType() {
    return this.dataType == null ? "" : this.dataType;
  }

/**
	* 資料別<br>
	* 93:動產及貴重物品擔保品明細檔資料
  *
  * @param dataType 資料別
	*/
  public void setDataType(String dataType) {
    this.dataType = dataType;
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
	* 空白<br>
	* 空白
	* @return String
	*/
  public String getFiller4() {
    return this.filler4 == null ? "" : this.filler4;
  }

/**
	* 空白<br>
	* 空白
  *
  * @param filler4 空白
	*/
  public void setFiller4(String filler4) {
    this.filler4 = filler4;
  }

/**
	* 擔保品控制編碼<br>
	* Key,左靠右補空白
	* @return String
	*/
  public String getClActNo() {
    return this.clActNo == null ? "" : this.clActNo;
  }

/**
	* 擔保品控制編碼<br>
	* Key,左靠右補空白
  *
  * @param clActNo 擔保品控制編碼
	*/
  public void setClActNo(String clActNo) {
    this.clActNo = clActNo;
  }

/**
	* 擔保品類別<br>
	* 擔保品類別代號．附件9
	* @return String
	*/
  public String getClTypeJCIC() {
    return this.clTypeJCIC == null ? "" : this.clTypeJCIC;
  }

/**
	* 擔保品類別<br>
	* 擔保品類別代號．附件9
  *
  * @param clTypeJCIC 擔保品類別
	*/
  public void setClTypeJCIC(String clTypeJCIC) {
    this.clTypeJCIC = clTypeJCIC;
  }

/**
	* 擔保品所有權人或代表人IDN/BAN<br>
	* 左靠，身份證或統一證號
	* @return String
	*/
  public String getOwnerId() {
    return this.ownerId == null ? "" : this.ownerId;
  }

/**
	* 擔保品所有權人或代表人IDN/BAN<br>
	* 左靠，身份證或統一證號
  *
  * @param ownerId 擔保品所有權人或代表人IDN/BAN
	*/
  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

/**
	* 鑑估值<br>
	* 右靠，左補0，單位新台幣千元，最新鑑價金額
	* @return Integer
	*/
  public int getEvaAmt() {
    return this.evaAmt;
  }

/**
	* 鑑估值<br>
	* 右靠，左補0，單位新台幣千元，最新鑑價金額
  *
  * @param evaAmt 鑑估值
	*/
  public void setEvaAmt(int evaAmt) {
    this.evaAmt = evaAmt;
  }

/**
	* 鑑估日期<br>
	* 擔保品鑑估年月，以YYYYMM(民國年)表示；重新鑑價，請填報最新鑑價日期
	* @return Integer
	*/
  public int getEvaDate() {
    return this.evaDate;
  }

/**
	* 鑑估日期<br>
	* 擔保品鑑估年月，以YYYYMM(民國年)表示；重新鑑價，請填報最新鑑價日期
  *
  * @param evaDate 鑑估日期
	*/
  public void setEvaDate(int evaDate) {
    this.evaDate = evaDate;
  }

/**
	* 可放款值<br>
	* 右靠，左補0，單位新台幣千元，鑑估值 * 可貸放成數
	* @return Integer
	*/
  public int getLoanLimitAmt() {
    return this.loanLimitAmt;
  }

/**
	* 可放款值<br>
	* 右靠，左補0，單位新台幣千元，鑑估值 * 可貸放成數
  *
  * @param loanLimitAmt 可放款值
	*/
  public void setLoanLimitAmt(int loanLimitAmt) {
    this.loanLimitAmt = loanLimitAmt;
  }

/**
	* 設定日期<br>
	* 動產設定年月，以YYYYMM(民國年)表示
	* @return Integer
	*/
  public int getSettingDate() {
    return this.settingDate;
  }

/**
	* 設定日期<br>
	* 動產設定年月，以YYYYMM(民國年)表示
  *
  * @param settingDate 設定日期
	*/
  public void setSettingDate(int settingDate) {
    this.settingDate = settingDate;
  }

/**
	* 本行本月設定金額<br>
	* 右靠，左補0，單位新台幣千元，本月新增設定總金額，如無則填0
	* @return Integer
	*/
  public int getMonthSettingAmt() {
    return this.monthSettingAmt;
  }

/**
	* 本行本月設定金額<br>
	* 右靠，左補0，單位新台幣千元，本月新增設定總金額，如無則填0
  *
  * @param monthSettingAmt 本行本月設定金額
	*/
  public void setMonthSettingAmt(int monthSettingAmt) {
    this.monthSettingAmt = monthSettingAmt;
  }

/**
	* 本月設定抵押順位<br>
	* 本月如有設定，填報本月設定之設定順位
	* @return Integer
	*/
  public int getSettingSeq() {
    return this.settingSeq;
  }

/**
	* 本月設定抵押順位<br>
	* 本月如有設定，填報本月設定之設定順位
  *
  * @param settingSeq 本月設定抵押順位
	*/
  public void setSettingSeq(int settingSeq) {
    this.settingSeq = settingSeq;
  }

/**
	* 本行累計已設定總金額<br>
	* 右靠，左補0，單位新台幣千元，至本月底該動產本行已設定總金額
	* @return Integer
	*/
  public int getSettingAmt() {
    return this.settingAmt;
  }

/**
	* 本行累計已設定總金額<br>
	* 右靠，左補0，單位新台幣千元，至本月底該動產本行已設定總金額
  *
  * @param settingAmt 本行累計已設定總金額
	*/
  public void setSettingAmt(int settingAmt) {
    this.settingAmt = settingAmt;
  }

/**
	* 其他債權人前已設定金額<br>
	* 右靠，左補0，單位新台幣千元，如無則填0
	* @return Integer
	*/
  public int getPreSettingAmt() {
    return this.preSettingAmt;
  }

/**
	* 其他債權人前已設定金額<br>
	* 右靠，左補0，單位新台幣千元，如無則填0
  *
  * @param preSettingAmt 其他債權人前已設定金額
	*/
  public void setPreSettingAmt(int preSettingAmt) {
    this.preSettingAmt = preSettingAmt;
  }

/**
	* 處分價格<br>
	* 八位數字，右靠，左補0，單位新台幣千元，填報該擔保品至本月底前已被處分之價格金額，如無則填0
	* @return Integer
	*/
  public int getDispPrice() {
    return this.dispPrice;
  }

/**
	* 處分價格<br>
	* 八位數字，右靠，左補0，單位新台幣千元，填報該擔保品至本月底前已被處分之價格金額，如無則填0
  *
  * @param dispPrice 處分價格
	*/
  public void setDispPrice(int dispPrice) {
    this.dispPrice = dispPrice;
  }

/**
	* 權利到期年月<br>
	* YYYYMM(民國年)，如無到期年月，請填99999
	* @return Integer
	*/
  public int getIssueEndDate() {
    return this.issueEndDate;
  }

/**
	* 權利到期年月<br>
	* YYYYMM(民國年)，如無到期年月，請填99999
  *
  * @param issueEndDate 權利到期年月
	*/
  public void setIssueEndDate(int issueEndDate) {
    this.issueEndDate = issueEndDate;
  }

/**
	* 是否有保險<br>
	* Y=是，N=否
	* @return String
	*/
  public String getInsuFg() {
    return this.insuFg == null ? "" : this.insuFg;
  }

/**
	* 是否有保險<br>
	* Y=是，N=否
  *
  * @param insuFg 是否有保險
	*/
  public void setInsuFg(String insuFg) {
    this.insuFg = insuFg;
  }

/**
	* 空白<br>
	* 備用
	* @return String
	*/
  public String getFiller19() {
    return this.filler19 == null ? "" : this.filler19;
  }

/**
	* 空白<br>
	* 備用
  *
  * @param filler19 空白
	*/
  public void setFiller19(String filler19) {
    this.filler19 = filler19;
  }

/**
	* 資料所屬年月<br>
	* 請填報本筆授信資料所屬年月(民國年)
	* @return Integer
	*/
  public int getJcicDataYM() {
    return this.jcicDataYM;
  }

/**
	* 資料所屬年月<br>
	* 請填報本筆授信資料所屬年月(民國年)
  *
  * @param jcicDataYM 資料所屬年月
	*/
  public void setJcicDataYM(int jcicDataYM) {
    this.jcicDataYM = jcicDataYM;
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
    return "JcicB093 [jcicB093Id=" + jcicB093Id + ", dataType=" + dataType + ", bankItem=" + bankItem + ", branchItem=" + branchItem + ", filler4=" + filler4
           + ", clTypeJCIC=" + clTypeJCIC + ", ownerId=" + ownerId + ", evaAmt=" + evaAmt + ", evaDate=" + evaDate + ", loanLimitAmt=" + loanLimitAmt + ", settingDate=" + settingDate
           + ", monthSettingAmt=" + monthSettingAmt + ", settingSeq=" + settingSeq + ", settingAmt=" + settingAmt + ", preSettingAmt=" + preSettingAmt + ", dispPrice=" + dispPrice + ", issueEndDate=" + issueEndDate
           + ", insuFg=" + insuFg + ", filler19=" + filler19 + ", jcicDataYM=" + jcicDataYM + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
