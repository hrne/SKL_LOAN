package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ451 前置調解延期繳款資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ451Id implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -4589010433761442247L;

// 報送單位代號
  /* 三位文數字 */
  @Column(name = "`SubmitKey`", length = 3)
  private String submitKey = " ";

  // 債務人IDN
  @Column(name = "`CustId`", length = 10)
  private String custId = " ";

  // 調解申請日
  @Column(name = "`ApplyDate`")
  private int applyDate = 0;

  // 受理調解機構代號
  /* 三位文數字法院名稱代號表(CdCode.CourtCode)或郵遞區號 */
  @Column(name = "`CourtCode`", length = 3)
  private String courtCode = " ";

  // 延期繳款年月
  /* YYYYMMDD */
  @Column(name = "`DelayYM`")
  private int delayYM = 0;

  public JcicZ451Id() {
  }

  public JcicZ451Id(String submitKey, String custId, int applyDate, String courtCode, int delayYM) {
    this.submitKey = submitKey;
    this.custId = custId;
    this.applyDate = applyDate;
    this.courtCode = courtCode;
    this.delayYM = delayYM;
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
	* 
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 債務人IDN<br>
	* 
  *
  * @param custId 債務人IDN
	*/
  public void setCustId(String custId) {
    this.custId = custId;
  }

/**
	* 調解申請日<br>
	* 
	* @return Integer
	*/
  public int getApplyDate() {
    return  StaticTool.bcToRoc(this.applyDate);
  }

/**
	* 調解申請日<br>
	* 
  *
  * @param applyDate 調解申請日
  * @throws LogicException when Date Is Warn	*/
  public void setApplyDate(int applyDate) throws LogicException {
    this.applyDate = StaticTool.rocToBc(applyDate);
  }

/**
	* 受理調解機構代號<br>
	* 三位文數字
法院名稱代號表(CdCode.CourtCode)或郵遞區號
	* @return String
	*/
  public String getCourtCode() {
    return this.courtCode == null ? "" : this.courtCode;
  }

/**
	* 受理調解機構代號<br>
	* 三位文數字
法院名稱代號表(CdCode.CourtCode)或郵遞區號
  *
  * @param courtCode 受理調解機構代號
	*/
  public void setCourtCode(String courtCode) {
    this.courtCode = courtCode;
  }

/**
	* 延期繳款年月<br>
	* YYYYMMDD
	* @return Integer
	*/
  public int getDelayYM() {
    return this.delayYM;
  }

/**
	* 延期繳款年月<br>
	* YYYYMMDD
  *
  * @param delayYM 延期繳款年月
	*/
  public void setDelayYM(int delayYM) {
    this.delayYM = delayYM;
  }


  @Override
  public int hashCode() {
    return Objects.hash(submitKey, custId, applyDate, courtCode, delayYM);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    JcicZ451Id jcicZ451Id = (JcicZ451Id) obj;
    return submitKey.equals(jcicZ451Id.submitKey) && custId.equals(jcicZ451Id.custId) && applyDate == jcicZ451Id.applyDate && courtCode == jcicZ451Id.courtCode && delayYM == jcicZ451Id.delayYM;
  }

  @Override
  public String toString() {
    return "JcicZ451Id [submitKey=" + submitKey + ", custId=" + custId + ", applyDate=" + applyDate + ", courtCode=" + courtCode + ", delayYM=" + delayYM + "]";
  }
}
