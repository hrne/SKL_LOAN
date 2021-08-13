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
 * JcicRel 聯徵授信「同一關係企業及集團企業」資料報送檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicRel`")
public class JcicRel implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -245390749155818214L;

@EmbeddedId
  private JcicRelId jcicRelId;

  // 資料年月日
  /* 會計日YYYYMMDD，本表每周報送 */
  @Column(name = "`DataYMD`", insertable = false, updatable = false)
  private int dataYMD = 0;

  // 總行代號
  /* 金融機構總機構之代號，三位數字 */
  @Column(name = "`BankItem`", length = 3, insertable = false, updatable = false)
  private String bankItem;

  // 分行代號
  /* 金融機構分支機構之代號，四位數字 */
  @Column(name = "`BranchItem`", length = 4, insertable = false, updatable = false)
  private String branchItem;

  // 客戶填表年月
  /* 以'YYYMM'(民國)表示 */
  @Column(name = "`RelYM`")
  private int relYM = 0;

  // 報送時機
  /* A：新貸B：續貸C：更新 */
  @Column(name = "`TranCode`", length = 1, insertable = false, updatable = false)
  private String tranCode;

  // 授信企業統編
  /* 授信戶營利事業統一編號 */
  @Column(name = "`CustId`", length = 8, insertable = false, updatable = false)
  private String custId;

  // 空白
  /* 空白 */
  @Column(name = "`Filler6`", length = 1)
  private String filler6;

  // 關係企業統編
  /* 關係企業營利事業統一編號 */
  @Column(name = "`RelId`", length = 8, insertable = false, updatable = false)
  private String relId;

  // 空白
  /* 空白 */
  @Column(name = "`Filler8`", length = 1)
  private String filler8;

  // 關係企業關係代號
  /* 左靠A:有控制與從屬關係B:相互投資關係C:董事長（或代表公司董事、執行業務股東）或總經理與他公司董事長（或代表公司董事、執行業務股東）或總經理為同一人或具有配偶關係 */
  @Column(name = "`RelationCode`", length = 3)
  private String relationCode;

  // 空白
  /* 空白 */
  @Column(name = "`Filler10`", length = 5)
  private String filler10;

  // 結束註記碼
  /* 結束註記碼，請填代碼'Z' */
  @Column(name = "`EndCode`", length = 1)
  private String endCode;

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


  public JcicRelId getJcicRelId() {
    return this.jcicRelId;
  }

  public void setJcicRelId(JcicRelId jcicRelId) {
    this.jcicRelId = jcicRelId;
  }

/**
	* 資料年月日<br>
	* 會計日YYYYMMDD，本表每周報送
	* @return Integer
	*/
  public int getDataYMD() {
    return this.dataYMD;
  }

/**
	* 資料年月日<br>
	* 會計日YYYYMMDD，本表每周報送
  *
  * @param dataYMD 資料年月日
	*/
  public void setDataYMD(int dataYMD) {
    this.dataYMD = dataYMD;
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
	* 客戶填表年月<br>
	* 以'YYYMM'(民國)表示
	* @return Integer
	*/
  public int getRelYM() {
    return this.relYM;
  }

/**
	* 客戶填表年月<br>
	* 以'YYYMM'(民國)表示
  *
  * @param relYM 客戶填表年月
	*/
  public void setRelYM(int relYM) {
    this.relYM = relYM;
  }

/**
	* 報送時機<br>
	* A：新貸
B：續貸
C：更新
	* @return String
	*/
  public String getTranCode() {
    return this.tranCode == null ? "" : this.tranCode;
  }

/**
	* 報送時機<br>
	* A：新貸
B：續貸
C：更新
  *
  * @param tranCode 報送時機
	*/
  public void setTranCode(String tranCode) {
    this.tranCode = tranCode;
  }

/**
	* 授信企業統編<br>
	* 授信戶營利事業統一編號
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 授信企業統編<br>
	* 授信戶營利事業統一編號
  *
  * @param custId 授信企業統編
	*/
  public void setCustId(String custId) {
    this.custId = custId;
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
	* 關係企業統編<br>
	* 關係企業營利事業統一編號
	* @return String
	*/
  public String getRelId() {
    return this.relId == null ? "" : this.relId;
  }

/**
	* 關係企業統編<br>
	* 關係企業營利事業統一編號
  *
  * @param relId 關係企業統編
	*/
  public void setRelId(String relId) {
    this.relId = relId;
  }

/**
	* 空白<br>
	* 空白
	* @return String
	*/
  public String getFiller8() {
    return this.filler8 == null ? "" : this.filler8;
  }

/**
	* 空白<br>
	* 空白
  *
  * @param filler8 空白
	*/
  public void setFiller8(String filler8) {
    this.filler8 = filler8;
  }

/**
	* 關係企業關係代號<br>
	* 左靠
A:有控制與從屬關係
B:相互投資關係
C:董事長（或代表公司董事、執行業務股東）或總經理與他公司董事長（或代表公司董事、執行業務股東）或總經理為同一人或具有配偶關係
	* @return String
	*/
  public String getRelationCode() {
    return this.relationCode == null ? "" : this.relationCode;
  }

/**
	* 關係企業關係代號<br>
	* 左靠
A:有控制與從屬關係
B:相互投資關係
C:董事長（或代表公司董事、執行業務股東）或總經理與他公司董事長（或代表公司董事、執行業務股東）或總經理為同一人或具有配偶關係
  *
  * @param relationCode 關係企業關係代號
	*/
  public void setRelationCode(String relationCode) {
    this.relationCode = relationCode;
  }

/**
	* 空白<br>
	* 空白
	* @return String
	*/
  public String getFiller10() {
    return this.filler10 == null ? "" : this.filler10;
  }

/**
	* 空白<br>
	* 空白
  *
  * @param filler10 空白
	*/
  public void setFiller10(String filler10) {
    this.filler10 = filler10;
  }

/**
	* 結束註記碼<br>
	* 結束註記碼，請填代碼'Z'
	* @return String
	*/
  public String getEndCode() {
    return this.endCode == null ? "" : this.endCode;
  }

/**
	* 結束註記碼<br>
	* 結束註記碼，請填代碼'Z'
  *
  * @param endCode 結束註記碼
	*/
  public void setEndCode(String endCode) {
    this.endCode = endCode;
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
    return "JcicRel [jcicRelId=" + jcicRelId + ", relYM=" + relYM
           + ", filler6=" + filler6 + ", filler8=" + filler8 + ", relationCode=" + relationCode + ", filler10=" + filler10 + ", endCode=" + endCode
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
