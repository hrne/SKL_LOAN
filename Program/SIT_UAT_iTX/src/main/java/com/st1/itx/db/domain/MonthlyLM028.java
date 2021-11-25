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
 * MonthlyLM028 LM028預估現金流量月報工作檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`MonthlyLM028`")
public class MonthlyLM028 implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 1963833765496219433L;

@EmbeddedId
  private MonthlyLM028Id monthlyLM028Id;

  // 戶況
  @Column(name = "`LMSSTS`")
  private int lMSSTS = 0;

  // 企金別
  @Column(name = "`CUSENT`")
  private int cUSENT = 0;

  // 營業單位別
  @Column(name = "`CUSBRH`", length = 4)
  private String cUSBRH;

  // 借款人戶號
  @Column(name = "`LMSACN`", insertable = false, updatable = false)
  private int lMSACN = 0;

  // 額度編號
  @Column(name = "`LMSAPN`", insertable = false, updatable = false)
  private int lMSAPN = 0;

  // 撥款序號
  @Column(name = "`LMSASQ`", insertable = false, updatable = false)
  private int lMSASQ = 0;

  // 利率
  @Column(name = "`IRTRAT`")
  private BigDecimal iRTRAT = new BigDecimal("0");

  // 繳息週期
  @Column(name = "`LMSISC`")
  private int lMSISC = 0;

  // 扣款銀行
  @Column(name = "`LMSPBK`", length = 3)
  private String lMSPBK;

  // 貸款期間－月
  @Column(name = "`APLMON`")
  private int aPLMON = 0;

  // 貸款期間－日
  @Column(name = "`APLDAY`")
  private int aPLDAY = 0;

  // 放款餘額
  @Column(name = "`LMSLBL`")
  private BigDecimal lMSLBL = new BigDecimal("0");

  // 利率區分
  @Column(name = "`AILIRT`", length = 1)
  private String aILIRT;

  // 郵局存款別
  @Column(name = "`POSCDE`", length = 1)
  private String pOSCDE;

  // 應繳日
  @Column(name = "`LMSPDY`")
  private int lMSPDY = 0;

  // 首次調整週期
  @Column(name = "`IRTFSC`")
  private int iRTFSC = 0;

  // 基本利率代碼
  @Column(name = "`IRTBCD`", length = 2)
  private String iRTBCD;

  // 利率1
  @Column(name = "`IRTRATYR1`")
  private BigDecimal iRTRATYR1 = new BigDecimal("0");

  // 利率2
  @Column(name = "`IRTRATYR2`")
  private BigDecimal iRTRATYR2 = new BigDecimal("0");

  // 利率3
  @Column(name = "`IRTRATYR3`")
  private BigDecimal iRTRATYR3 = new BigDecimal("0");

  // 利率4
  @Column(name = "`IRTRATYR4`")
  private BigDecimal iRTRATYR4 = new BigDecimal("0");

  // 利率5
  @Column(name = "`IRTRATYR5`")
  private BigDecimal iRTRATYR5 = new BigDecimal("0");

  // 押品別１
  @Column(name = "`GDRID1`")
  private int gDRID1 = 0;

  // 押品別２
  @Column(name = "`GDRID2`")
  private int gDRID2 = 0;

  // 撥款日-年
  @Column(name = "`YYYY`")
  private int yYYY = 0;

  // 撥款日-月
  @Column(name = "`MONTH`")
  private int mONTH = 0;

  // 撥款日-日
  @Column(name = "`DAY`")
  private int dAY = 0;

  // 到期日碼
  @Column(name = "`W08CDE`")
  private int w08CDE = 0;

  // 是否為關係人
  @Column(name = "`RELATION`", length = 1)
  private String rELATION;

  // 制度別
  @Column(name = "`DPTLVL`", length = 1)
  private String dPTLVL;

  // 資金來源
  @Column(name = "`ACTFSC`", length = 1)
  private String aCTFSC;

  // 最新利率
  @Column(name = "`LIRTRATYR`")
  private BigDecimal lIRTRATYR = new BigDecimal("0");

  // 最新利率生效起日
  @Column(name = "`LIRTDAY`")
  private int lIRTDAY = 0;

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


  public MonthlyLM028Id getMonthlyLM028Id() {
    return this.monthlyLM028Id;
  }

  public void setMonthlyLM028Id(MonthlyLM028Id monthlyLM028Id) {
    this.monthlyLM028Id = monthlyLM028Id;
  }

/**
	* 戶況<br>
	* 
	* @return Integer
	*/
  public int getLMSSTS() {
    return this.lMSSTS;
  }

/**
	* 戶況<br>
	* 
  *
  * @param lMSSTS 戶況
	*/
  public void setLMSSTS(int lMSSTS) {
    this.lMSSTS = lMSSTS;
  }

/**
	* 企金別<br>
	* 
	* @return Integer
	*/
  public int getCUSENT() {
    return this.cUSENT;
  }

/**
	* 企金別<br>
	* 
  *
  * @param cUSENT 企金別
	*/
  public void setCUSENT(int cUSENT) {
    this.cUSENT = cUSENT;
  }

/**
	* 營業單位別<br>
	* 
	* @return String
	*/
  public String getCUSBRH() {
    return this.cUSBRH == null ? "" : this.cUSBRH;
  }

/**
	* 營業單位別<br>
	* 
  *
  * @param cUSBRH 營業單位別
	*/
  public void setCUSBRH(String cUSBRH) {
    this.cUSBRH = cUSBRH;
  }

/**
	* 借款人戶號<br>
	* 
	* @return Integer
	*/
  public int getLMSACN() {
    return this.lMSACN;
  }

/**
	* 借款人戶號<br>
	* 
  *
  * @param lMSACN 借款人戶號
	*/
  public void setLMSACN(int lMSACN) {
    this.lMSACN = lMSACN;
  }

/**
	* 額度編號<br>
	* 
	* @return Integer
	*/
  public int getLMSAPN() {
    return this.lMSAPN;
  }

/**
	* 額度編號<br>
	* 
  *
  * @param lMSAPN 額度編號
	*/
  public void setLMSAPN(int lMSAPN) {
    this.lMSAPN = lMSAPN;
  }

/**
	* 撥款序號<br>
	* 
	* @return Integer
	*/
  public int getLMSASQ() {
    return this.lMSASQ;
  }

/**
	* 撥款序號<br>
	* 
  *
  * @param lMSASQ 撥款序號
	*/
  public void setLMSASQ(int lMSASQ) {
    this.lMSASQ = lMSASQ;
  }

/**
	* 利率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getIRTRAT() {
    return this.iRTRAT;
  }

/**
	* 利率<br>
	* 
  *
  * @param iRTRAT 利率
	*/
  public void setIRTRAT(BigDecimal iRTRAT) {
    this.iRTRAT = iRTRAT;
  }

/**
	* 繳息週期<br>
	* 
	* @return Integer
	*/
  public int getLMSISC() {
    return this.lMSISC;
  }

/**
	* 繳息週期<br>
	* 
  *
  * @param lMSISC 繳息週期
	*/
  public void setLMSISC(int lMSISC) {
    this.lMSISC = lMSISC;
  }

/**
	* 扣款銀行<br>
	* 
	* @return String
	*/
  public String getLMSPBK() {
    return this.lMSPBK == null ? "" : this.lMSPBK;
  }

/**
	* 扣款銀行<br>
	* 
  *
  * @param lMSPBK 扣款銀行
	*/
  public void setLMSPBK(String lMSPBK) {
    this.lMSPBK = lMSPBK;
  }

/**
	* 貸款期間－月<br>
	* 
	* @return Integer
	*/
  public int getAPLMON() {
    return this.aPLMON;
  }

/**
	* 貸款期間－月<br>
	* 
  *
  * @param aPLMON 貸款期間－月
	*/
  public void setAPLMON(int aPLMON) {
    this.aPLMON = aPLMON;
  }

/**
	* 貸款期間－日<br>
	* 
	* @return Integer
	*/
  public int getAPLDAY() {
    return this.aPLDAY;
  }

/**
	* 貸款期間－日<br>
	* 
  *
  * @param aPLDAY 貸款期間－日
	*/
  public void setAPLDAY(int aPLDAY) {
    this.aPLDAY = aPLDAY;
  }

/**
	* 放款餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLMSLBL() {
    return this.lMSLBL;
  }

/**
	* 放款餘額<br>
	* 
  *
  * @param lMSLBL 放款餘額
	*/
  public void setLMSLBL(BigDecimal lMSLBL) {
    this.lMSLBL = lMSLBL;
  }

/**
	* 利率區分<br>
	* 
	* @return String
	*/
  public String getAILIRT() {
    return this.aILIRT == null ? "" : this.aILIRT;
  }

/**
	* 利率區分<br>
	* 
  *
  * @param aILIRT 利率區分
	*/
  public void setAILIRT(String aILIRT) {
    this.aILIRT = aILIRT;
  }

/**
	* 郵局存款別<br>
	* 
	* @return String
	*/
  public String getPOSCDE() {
    return this.pOSCDE == null ? "" : this.pOSCDE;
  }

/**
	* 郵局存款別<br>
	* 
  *
  * @param pOSCDE 郵局存款別
	*/
  public void setPOSCDE(String pOSCDE) {
    this.pOSCDE = pOSCDE;
  }

/**
	* 應繳日<br>
	* 
	* @return Integer
	*/
  public int getLMSPDY() {
    return this.lMSPDY;
  }

/**
	* 應繳日<br>
	* 
  *
  * @param lMSPDY 應繳日
	*/
  public void setLMSPDY(int lMSPDY) {
    this.lMSPDY = lMSPDY;
  }

/**
	* 首次調整週期<br>
	* 
	* @return Integer
	*/
  public int getIRTFSC() {
    return this.iRTFSC;
  }

/**
	* 首次調整週期<br>
	* 
  *
  * @param iRTFSC 首次調整週期
	*/
  public void setIRTFSC(int iRTFSC) {
    this.iRTFSC = iRTFSC;
  }

/**
	* 基本利率代碼<br>
	* 
	* @return String
	*/
  public String getIRTBCD() {
    return this.iRTBCD == null ? "" : this.iRTBCD;
  }

/**
	* 基本利率代碼<br>
	* 
  *
  * @param iRTBCD 基本利率代碼
	*/
  public void setIRTBCD(String iRTBCD) {
    this.iRTBCD = iRTBCD;
  }

/**
	* 利率1<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getIRTRATYR1() {
    return this.iRTRATYR1;
  }

/**
	* 利率1<br>
	* 
  *
  * @param iRTRATYR1 利率1
	*/
  public void setIRTRATYR1(BigDecimal iRTRATYR1) {
    this.iRTRATYR1 = iRTRATYR1;
  }

/**
	* 利率2<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getIRTRATYR2() {
    return this.iRTRATYR2;
  }

/**
	* 利率2<br>
	* 
  *
  * @param iRTRATYR2 利率2
	*/
  public void setIRTRATYR2(BigDecimal iRTRATYR2) {
    this.iRTRATYR2 = iRTRATYR2;
  }

/**
	* 利率3<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getIRTRATYR3() {
    return this.iRTRATYR3;
  }

/**
	* 利率3<br>
	* 
  *
  * @param iRTRATYR3 利率3
	*/
  public void setIRTRATYR3(BigDecimal iRTRATYR3) {
    this.iRTRATYR3 = iRTRATYR3;
  }

/**
	* 利率4<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getIRTRATYR4() {
    return this.iRTRATYR4;
  }

/**
	* 利率4<br>
	* 
  *
  * @param iRTRATYR4 利率4
	*/
  public void setIRTRATYR4(BigDecimal iRTRATYR4) {
    this.iRTRATYR4 = iRTRATYR4;
  }

/**
	* 利率5<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getIRTRATYR5() {
    return this.iRTRATYR5;
  }

/**
	* 利率5<br>
	* 
  *
  * @param iRTRATYR5 利率5
	*/
  public void setIRTRATYR5(BigDecimal iRTRATYR5) {
    this.iRTRATYR5 = iRTRATYR5;
  }

/**
	* 押品別１<br>
	* 
	* @return Integer
	*/
  public int getGDRID1() {
    return this.gDRID1;
  }

/**
	* 押品別１<br>
	* 
  *
  * @param gDRID1 押品別１
	*/
  public void setGDRID1(int gDRID1) {
    this.gDRID1 = gDRID1;
  }

/**
	* 押品別２<br>
	* 
	* @return Integer
	*/
  public int getGDRID2() {
    return this.gDRID2;
  }

/**
	* 押品別２<br>
	* 
  *
  * @param gDRID2 押品別２
	*/
  public void setGDRID2(int gDRID2) {
    this.gDRID2 = gDRID2;
  }

/**
	* 撥款日-年<br>
	* 
	* @return Integer
	*/
  public int getYYYY() {
    return this.yYYY;
  }

/**
	* 撥款日-年<br>
	* 
  *
  * @param yYYY 撥款日-年
	*/
  public void setYYYY(int yYYY) {
    this.yYYY = yYYY;
  }

/**
	* 撥款日-月<br>
	* 
	* @return Integer
	*/
  public int getMONTH() {
    return this.mONTH;
  }

/**
	* 撥款日-月<br>
	* 
  *
  * @param mONTH 撥款日-月
	*/
  public void setMONTH(int mONTH) {
    this.mONTH = mONTH;
  }

/**
	* 撥款日-日<br>
	* 
	* @return Integer
	*/
  public int getDAY() {
    return this.dAY;
  }

/**
	* 撥款日-日<br>
	* 
  *
  * @param dAY 撥款日-日
	*/
  public void setDAY(int dAY) {
    this.dAY = dAY;
  }

/**
	* 到期日碼<br>
	* 
	* @return Integer
	*/
  public int getW08CDE() {
    return this.w08CDE;
  }

/**
	* 到期日碼<br>
	* 
  *
  * @param w08CDE 到期日碼
	*/
  public void setW08CDE(int w08CDE) {
    this.w08CDE = w08CDE;
  }

/**
	* 是否為關係人<br>
	* 
	* @return String
	*/
  public String getRELATION() {
    return this.rELATION == null ? "" : this.rELATION;
  }

/**
	* 是否為關係人<br>
	* 
  *
  * @param rELATION 是否為關係人
	*/
  public void setRELATION(String rELATION) {
    this.rELATION = rELATION;
  }

/**
	* 制度別<br>
	* 
	* @return String
	*/
  public String getDPTLVL() {
    return this.dPTLVL == null ? "" : this.dPTLVL;
  }

/**
	* 制度別<br>
	* 
  *
  * @param dPTLVL 制度別
	*/
  public void setDPTLVL(String dPTLVL) {
    this.dPTLVL = dPTLVL;
  }

/**
	* 資金來源<br>
	* 
	* @return String
	*/
  public String getACTFSC() {
    return this.aCTFSC == null ? "" : this.aCTFSC;
  }

/**
	* 資金來源<br>
	* 
  *
  * @param aCTFSC 資金來源
	*/
  public void setACTFSC(String aCTFSC) {
    this.aCTFSC = aCTFSC;
  }

/**
	* 最新利率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLIRTRATYR() {
    return this.lIRTRATYR;
  }

/**
	* 最新利率<br>
	* 
  *
  * @param lIRTRATYR 最新利率
	*/
  public void setLIRTRATYR(BigDecimal lIRTRATYR) {
    this.lIRTRATYR = lIRTRATYR;
  }

/**
	* 最新利率生效起日<br>
	* 
	* @return Integer
	*/
  public int getLIRTDAY() {
    return this.lIRTDAY;
  }

/**
	* 最新利率生效起日<br>
	* 
  *
  * @param lIRTDAY 最新利率生效起日
	*/
  public void setLIRTDAY(int lIRTDAY) {
    this.lIRTDAY = lIRTDAY;
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
    return "MonthlyLM028 [monthlyLM028Id=" + monthlyLM028Id + ", lMSSTS=" + lMSSTS + ", cUSENT=" + cUSENT + ", cUSBRH=" + cUSBRH
           + ", iRTRAT=" + iRTRAT + ", lMSISC=" + lMSISC + ", lMSPBK=" + lMSPBK + ", aPLMON=" + aPLMON + ", aPLDAY=" + aPLDAY + ", lMSLBL=" + lMSLBL
           + ", aILIRT=" + aILIRT + ", pOSCDE=" + pOSCDE + ", lMSPDY=" + lMSPDY + ", iRTFSC=" + iRTFSC + ", iRTBCD=" + iRTBCD + ", iRTRATYR1=" + iRTRATYR1
           + ", iRTRATYR2=" + iRTRATYR2 + ", iRTRATYR3=" + iRTRATYR3 + ", iRTRATYR4=" + iRTRATYR4 + ", iRTRATYR5=" + iRTRATYR5 + ", gDRID1=" + gDRID1 + ", gDRID2=" + gDRID2
           + ", yYYY=" + yYYY + ", mONTH=" + mONTH + ", dAY=" + dAY + ", w08CDE=" + w08CDE + ", rELATION=" + rELATION + ", dPTLVL=" + dPTLVL
           + ", aCTFSC=" + aCTFSC + ", lIRTRATYR=" + lIRTRATYR + ", lIRTDAY=" + lIRTDAY + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
