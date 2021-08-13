package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * JcicRel 聯徵授信「同一關係企業及集團企業」資料報送檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicRelId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 8565324161978244876L;

// 資料年月日
  /* 會計日YYYYMMDD，本表每周報送 */
  @Column(name = "`DataYMD`")
  private int dataYMD = 0;

  // 總行代號
  /* 金融機構總機構之代號，三位數字 */
  @Column(name = "`BankItem`", length = 3)
  private String bankItem = " ";

  // 分行代號
  /* 金融機構分支機構之代號，四位數字 */
  @Column(name = "`BranchItem`", length = 4)
  private String branchItem = " ";

  // 授信企業統編
  /* 授信戶營利事業統一編號 */
  @Column(name = "`CustId`", length = 8)
  private String custId = " ";

  // 關係企業統編
  /* 關係企業營利事業統一編號 */
  @Column(name = "`RelId`", length = 8)
  private String relId = " ";

  // 報送時機
  /* A：新貸B：續貸C：更新 */
  @Column(name = "`TranCode`", length = 1)
  private String tranCode = " ";

  public JcicRelId() {
  }

  public JcicRelId(int dataYMD, String bankItem, String branchItem, String custId, String relId, String tranCode) {
    this.dataYMD = dataYMD;
    this.bankItem = bankItem;
    this.branchItem = branchItem;
    this.custId = custId;
    this.relId = relId;
    this.tranCode = tranCode;
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


  @Override
  public int hashCode() {
    return Objects.hash(dataYMD, bankItem, branchItem, custId, relId, tranCode);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    JcicRelId jcicRelId = (JcicRelId) obj;
    return dataYMD == jcicRelId.dataYMD && bankItem.equals(jcicRelId.bankItem) && branchItem.equals(jcicRelId.branchItem) && custId.equals(jcicRelId.custId) && relId.equals(jcicRelId.relId) && tranCode.equals(jcicRelId.tranCode);
  }

  @Override
  public String toString() {
    return "JcicRelId [dataYMD=" + dataYMD + ", bankItem=" + bankItem + ", branchItem=" + branchItem + ", custId=" + custId + ", relId=" + relId + ", tranCode=" + tranCode + "]";
  }
}
