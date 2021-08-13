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
 * CdAcCode 會計科子細目設定檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdAcCode`")
public class CdAcCode implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -4822747890271629333L;

@EmbeddedId
  private CdAcCodeId cdAcCodeId;

  // 科目代號
  @Column(name = "`AcNoCode`", length = 11, insertable = false, updatable = false)
  private String acNoCode;

  // 子目代號
  @Column(name = "`AcSubCode`", length = 5, insertable = false, updatable = false)
  private String acSubCode;

  // 細目代號
  @Column(name = "`AcDtlCode`", length = 2, insertable = false, updatable = false)
  private String acDtlCode;

  // 科子細目名稱
  @Column(name = "`AcNoItem`", length = 40)
  private String acNoItem;

  // 業務科目代號
  @Column(name = "`AcctCode`", length = 3)
  private String acctCode;

  // 業務科目名稱
  @Column(name = "`AcctItem`", length = 20)
  private String acctItem;

  // 科子目級別
  /* 0:可入帳科目1:下編子細目 */
  @Column(name = "`ClassCode`")
  private int classCode = 0;

  // 帳冊別記號
  /* 0:不細分1:細分2:中介 */
  @Column(name = "`AcBookFlag`")
  private int acBookFlag = 0;

  // 借貸別
  /* D:借(1,5,6,9)C:貸(2,3,4,8) */
  @Column(name = "`DbCr`", length = 1)
  private String dbCr;

  // 業務科目記號
  /* 0:非業務科目（可經由[其他傳票輸入]交易入帳）1:資負明細科目（放款、催收款項...等，不可經由[其他傳票輸入]交易入帳)※資負明細科目1.列入[資負明細月報]項目2.可由[未銷帳餘額明細]查詢未銷明細3.列入[資負明細每日餘額檔(含所屬會科、帳冊別及利率)](資負明細業務科目)4.列入[會計餘額檢核表(會計檔餘額、銷帳檔餘額、業務檔餘額)]項目 */
  @Column(name = "`AcctFlag`")
  private int acctFlag = 0;

  // 銷帳科目記號
  /* 0－非銷帳科目1－會計銷帳科目(銷帳編號由系統自編,ex:應收利息)2－業務銷帳科目(銷帳編號由業務自編,ex:暫付及待結轉帳項－火險保費) */
  @Column(name = "`ReceivableFlag`")
  private int receivableFlag = 0;

  // 日結餘額檢查記號
  /* 0－不檢查1－不過餘額(借貸分由放款及核心系統出帳)2－應&amp;gt;=03－應=04－應&amp;lt;=0 */
  @Column(name = "`ClsChkFlag`")
  private int clsChkFlag = 0;

  // 放款部使用記號
  /* 0:可以使用1:不可使用 */
  @Column(name = "`InuseFlag`")
  private int inuseFlag = 0;

  // 舊科目代號
  @Column(name = "`AcNoCodeOld`", length = 8)
  private String acNoCodeOld;

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


  public CdAcCodeId getCdAcCodeId() {
    return this.cdAcCodeId;
  }

  public void setCdAcCodeId(CdAcCodeId cdAcCodeId) {
    this.cdAcCodeId = cdAcCodeId;
  }

/**
	* 科目代號<br>
	* 
	* @return String
	*/
  public String getAcNoCode() {
    return this.acNoCode == null ? "" : this.acNoCode;
  }

/**
	* 科目代號<br>
	* 
  *
  * @param acNoCode 科目代號
	*/
  public void setAcNoCode(String acNoCode) {
    this.acNoCode = acNoCode;
  }

/**
	* 子目代號<br>
	* 
	* @return String
	*/
  public String getAcSubCode() {
    return this.acSubCode == null ? "" : this.acSubCode;
  }

/**
	* 子目代號<br>
	* 
  *
  * @param acSubCode 子目代號
	*/
  public void setAcSubCode(String acSubCode) {
    this.acSubCode = acSubCode;
  }

/**
	* 細目代號<br>
	* 
	* @return String
	*/
  public String getAcDtlCode() {
    return this.acDtlCode == null ? "" : this.acDtlCode;
  }

/**
	* 細目代號<br>
	* 
  *
  * @param acDtlCode 細目代號
	*/
  public void setAcDtlCode(String acDtlCode) {
    this.acDtlCode = acDtlCode;
  }

/**
	* 科子細目名稱<br>
	* 
	* @return String
	*/
  public String getAcNoItem() {
    return this.acNoItem == null ? "" : this.acNoItem;
  }

/**
	* 科子細目名稱<br>
	* 
  *
  * @param acNoItem 科子細目名稱
	*/
  public void setAcNoItem(String acNoItem) {
    this.acNoItem = acNoItem;
  }

/**
	* 業務科目代號<br>
	* 
	* @return String
	*/
  public String getAcctCode() {
    return this.acctCode == null ? "" : this.acctCode;
  }

/**
	* 業務科目代號<br>
	* 
  *
  * @param acctCode 業務科目代號
	*/
  public void setAcctCode(String acctCode) {
    this.acctCode = acctCode;
  }

/**
	* 業務科目名稱<br>
	* 
	* @return String
	*/
  public String getAcctItem() {
    return this.acctItem == null ? "" : this.acctItem;
  }

/**
	* 業務科目名稱<br>
	* 
  *
  * @param acctItem 業務科目名稱
	*/
  public void setAcctItem(String acctItem) {
    this.acctItem = acctItem;
  }

/**
	* 科子目級別<br>
	* 0:可入帳科目
1:下編子細目
	* @return Integer
	*/
  public int getClassCode() {
    return this.classCode;
  }

/**
	* 科子目級別<br>
	* 0:可入帳科目
1:下編子細目
  *
  * @param classCode 科子目級別
	*/
  public void setClassCode(int classCode) {
    this.classCode = classCode;
  }

/**
	* 帳冊別記號<br>
	* 0:不細分
1:細分
2:中介
	* @return Integer
	*/
  public int getAcBookFlag() {
    return this.acBookFlag;
  }

/**
	* 帳冊別記號<br>
	* 0:不細分
1:細分
2:中介
  *
  * @param acBookFlag 帳冊別記號
	*/
  public void setAcBookFlag(int acBookFlag) {
    this.acBookFlag = acBookFlag;
  }

/**
	* 借貸別<br>
	* D:借(1,5,6,9)
C:貸(2,3,4,8)
	* @return String
	*/
  public String getDbCr() {
    return this.dbCr == null ? "" : this.dbCr;
  }

/**
	* 借貸別<br>
	* D:借(1,5,6,9)
C:貸(2,3,4,8)
  *
  * @param dbCr 借貸別
	*/
  public void setDbCr(String dbCr) {
    this.dbCr = dbCr;
  }

/**
	* 業務科目記號<br>
	* 0:非業務科目（可經由[其他傳票輸入]交易入帳）
1:資負明細科目（放款、催收款項...等，不可經由[其他傳票輸入]交易入帳)
※資負明細科目
1.列入[資負明細月報]項目
2.可由[未銷帳餘額明細]查詢未銷明細
3.列入[資負明細每日餘額檔(含所屬會科、帳冊別及利率)](資負明細業務科目)
4.列入[會計餘額檢核表(會計檔餘額、銷帳檔餘額、業務檔餘額)]項目
	* @return Integer
	*/
  public int getAcctFlag() {
    return this.acctFlag;
  }

/**
	* 業務科目記號<br>
	* 0:非業務科目（可經由[其他傳票輸入]交易入帳）
1:資負明細科目（放款、催收款項...等，不可經由[其他傳票輸入]交易入帳)
※資負明細科目
1.列入[資負明細月報]項目
2.可由[未銷帳餘額明細]查詢未銷明細
3.列入[資負明細每日餘額檔(含所屬會科、帳冊別及利率)](資負明細業務科目)
4.列入[會計餘額檢核表(會計檔餘額、銷帳檔餘額、業務檔餘額)]項目
  *
  * @param acctFlag 業務科目記號
	*/
  public void setAcctFlag(int acctFlag) {
    this.acctFlag = acctFlag;
  }

/**
	* 銷帳科目記號<br>
	* 0－非銷帳科目
1－會計銷帳科目(銷帳編號由系統自編,ex:應收利息)
2－業務銷帳科目(銷帳編號由業務自編,ex:暫付及待結轉帳項－火險保費)
	* @return Integer
	*/
  public int getReceivableFlag() {
    return this.receivableFlag;
  }

/**
	* 銷帳科目記號<br>
	* 0－非銷帳科目
1－會計銷帳科目(銷帳編號由系統自編,ex:應收利息)
2－業務銷帳科目(銷帳編號由業務自編,ex:暫付及待結轉帳項－火險保費)
  *
  * @param receivableFlag 銷帳科目記號
	*/
  public void setReceivableFlag(int receivableFlag) {
    this.receivableFlag = receivableFlag;
  }

/**
	* 日結餘額檢查記號<br>
	* 0－不檢查
1－不過餘額(借貸分由放款及核心系統出帳)
2－應&amp;gt;=0
3－應=0
4－應&amp;lt;=0
	* @return Integer
	*/
  public int getClsChkFlag() {
    return this.clsChkFlag;
  }

/**
	* 日結餘額檢查記號<br>
	* 0－不檢查
1－不過餘額(借貸分由放款及核心系統出帳)
2－應&amp;gt;=0
3－應=0
4－應&amp;lt;=0
  *
  * @param clsChkFlag 日結餘額檢查記號
	*/
  public void setClsChkFlag(int clsChkFlag) {
    this.clsChkFlag = clsChkFlag;
  }

/**
	* 放款部使用記號<br>
	* 0:可以使用
1:不可使用
	* @return Integer
	*/
  public int getInuseFlag() {
    return this.inuseFlag;
  }

/**
	* 放款部使用記號<br>
	* 0:可以使用
1:不可使用
  *
  * @param inuseFlag 放款部使用記號
	*/
  public void setInuseFlag(int inuseFlag) {
    this.inuseFlag = inuseFlag;
  }

/**
	* 舊科目代號<br>
	* 
	* @return String
	*/
  public String getAcNoCodeOld() {
    return this.acNoCodeOld == null ? "" : this.acNoCodeOld;
  }

/**
	* 舊科目代號<br>
	* 
  *
  * @param acNoCodeOld 舊科目代號
	*/
  public void setAcNoCodeOld(String acNoCodeOld) {
    this.acNoCodeOld = acNoCodeOld;
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
    return "CdAcCode [cdAcCodeId=" + cdAcCodeId + ", acNoItem=" + acNoItem + ", acctCode=" + acctCode + ", acctItem=" + acctItem
           + ", classCode=" + classCode + ", acBookFlag=" + acBookFlag + ", dbCr=" + dbCr + ", acctFlag=" + acctFlag + ", receivableFlag=" + receivableFlag + ", clsChkFlag=" + clsChkFlag
           + ", inuseFlag=" + inuseFlag + ", acNoCodeOld=" + acNoCodeOld + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
           + "]";
  }
}
