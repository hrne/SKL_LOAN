package com.st1.itx.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
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
 * SlipMedia2022 傳票媒體檔2022年格式<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`SlipMedia2022`")
public class SlipMedia2022 implements Serializable {


  @EmbeddedId
  private SlipMedia2022Id slipMedia2022Id;

  // 帳冊別
  /* 000:全公司300:OIU全公司 */
  @Column(name = "`AcBookCode`", length = 3)
  private String acBookCode;

  // 傳票號碼
  /* F10+民國年+月份(1碼)+日期+3碼序號*3碼序號，從CdGSeq取號 */
  @Column(name = "`MediaSlipNo`", length = 12, insertable = false, updatable = false)
  private String mediaSlipNo;

  // 傳票明細序號
  /* 以相同傳票號碼(MediaSlipNo)編立流水號 */
  @Column(name = "`Seq`", insertable = false, updatable = false)
  private int seq = 0;

  // 傳票日期
  /* 一般情況都是會計日期(營業日)僅月底提存時，會是月底日期(日曆日) */
  @Column(name = "`AcDate`")
  private int acDate = 0;

  // 傳票批號
  @Column(name = "`BatchNo`")
  private int batchNo = 0;

  // 上傳核心序號
  /* 相關說明有二:1.關帳時,由L6101連動,以AcClose.CoreSeqNo放在titaVo.MediaSeq傳入2.上傳EBS Webservice此欄位使用於ETL批號(GROUP_ID) */
  @Column(name = "`MediaSeq`")
  private int mediaSeq = 0;

  // 科目代號
  @Column(name = "`AcNoCode`", length = 11)
  private String acNoCode;

  // 子目代號
  @Column(name = "`AcSubCode`", length = 5)
  private String acSubCode;

  // 部門代號
  @Column(name = "`DeptCode`", length = 6)
  private String deptCode;

  // 借貸別
  /* D:借C:貸 */
  @Column(name = "`DbCr`", length = 1)
  private String dbCr;

  // 金額
  @Column(name = "`TxAmt`")
  private BigDecimal txAmt = new BigDecimal("0");

  // 傳票摘要
  @Column(name = "`SlipRmk`", length = 80)
  private String slipRmk;

  // 會計科目銷帳碼
  @Column(name = "`ReceiveCode`", length = 15)
  private String receiveCode;

  // 成本月份
  @Column(name = "`CostMonth`", length = 2)
  private String costMonth;

  // 保單號碼
  @Column(name = "`InsuNo`", length = 10)
  private String insuNo;

  // 業務員代號
  @Column(name = "`SalesmanCode`", length = 12)
  private String salesmanCode;

  // 薪碼
  @Column(name = "`SalaryCode`", length = 2)
  private String salaryCode;

  // 幣別
  /* NTD:台幣(NT) */
  @Column(name = "`CurrencyCode`", length = 3)
  private String currencyCode;

  // 區隔帳冊
  /* 00A:傳統帳冊101:分紅帳冊201:利變年金帳冊203:利變萬能帳冊204:利變壽險帳冊30A:OIU傳統帳冊301:OIU利變年金303:OIU利變萬能304:OIU利變壽險 */
  @Column(name = "`AcSubBookCode`", length = 3)
  private String acSubBookCode;

  // 成本單位
  /* 暫訂作法為IFRS4準則下比照「部門代號」欄位提供，但現行提供通路單位(2碼)者需對應回6碼的單位代號；IFRS17準則下提供規則待訂 */
  @Column(name = "`CostUnit`", length = 6)
  private String CostUnit;

  // 通路別
  /* 核心(團終養)需提供，其他系統不用提供(空值) */
  @Column(name = "`SalesChannelType`", length = 2)
  private String salesChannelType;

  // 會計準則類型
  /* 1:IFRS42:IFRS17 */
  @Column(name = "`IfrsType`", length = 1)
  private String ifrsType;

  // 關係人ID
  /* 預留欄位，各系統暫不需提供資料 */
  @Column(name = "`RelationId`", length = 10)
  private String relationId;

  // 關聯方代號
  /* 預留欄位，各系統暫不需提供資料 */
  @Column(name = "`RelateCode`", length = 3)
  private String relateCode;

  // IFRS17群組
  /* 核心(團終養)於IFRS17準則下需提供，其他系統不用提供(空值) */
  @Column(name = "`Ifrs17Group`", length = 9)
  private String ifrs17Group;

  // 是否為最新
  /* Y:是N:否 */
  @Column(name = "`LatestFlag`", length = 1)
  private String latestFlag;

  // 是否已傳輸
  /* Y:是N:否 */
  @Column(name = "`TransferFlag`", length = 1)
  private String transferFlag;

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

  // 回應錯誤代碼
  @Column(name = "`ErrorCode`", length = 3)
  private String errorCode;

  // 回應錯誤訊息
  @Column(name = "`ErrorMsg`", length = 2000)
  private String errorMsg;


  public SlipMedia2022Id getSlipMedia2022Id() {
    return this.slipMedia2022Id;
  }

  public void setSlipMedia2022Id(SlipMedia2022Id slipMedia2022Id) {
    this.slipMedia2022Id = slipMedia2022Id;
  }

/**
	* 帳冊別<br>
	* 000:全公司
300:OIU全公司
	* @return String
	*/
  public String getAcBookCode() {
    return this.acBookCode == null ? "" : this.acBookCode;
  }

/**
	* 帳冊別<br>
	* 000:全公司
300:OIU全公司
  *
  * @param acBookCode 帳冊別
	*/
  public void setAcBookCode(String acBookCode) {
    this.acBookCode = acBookCode;
  }

/**
	* 傳票號碼<br>
	* F10+民國年+月份(1碼)+日期+3碼序號
*3碼序號，從CdGSeq取號
	* @return String
	*/
  public String getMediaSlipNo() {
    return this.mediaSlipNo == null ? "" : this.mediaSlipNo;
  }

/**
	* 傳票號碼<br>
	* F10+民國年+月份(1碼)+日期+3碼序號
*3碼序號，從CdGSeq取號
  *
  * @param mediaSlipNo 傳票號碼
	*/
  public void setMediaSlipNo(String mediaSlipNo) {
    this.mediaSlipNo = mediaSlipNo;
  }

/**
	* 傳票明細序號<br>
	* 以相同傳票號碼(MediaSlipNo)編立流水號
	* @return Integer
	*/
  public int getSeq() {
    return this.seq;
  }

/**
	* 傳票明細序號<br>
	* 以相同傳票號碼(MediaSlipNo)編立流水號
  *
  * @param seq 傳票明細序號
	*/
  public void setSeq(int seq) {
    this.seq = seq;
  }

/**
	* 傳票日期<br>
	* 一般情況都是會計日期(營業日)
僅月底提存時，會是月底日期(日曆日)
	* @return Integer
	*/
  public int getAcDate() {
    return StaticTool.bcToRoc(this.acDate);
  }

/**
	* 傳票日期<br>
	* 一般情況都是會計日期(營業日)
僅月底提存時，會是月底日期(日曆日)
  *
  * @param acDate 傳票日期
  * @throws LogicException when Date Is Warn	*/
  public void setAcDate(int acDate) throws LogicException {
    this.acDate = StaticTool.rocToBc(acDate);
  }

/**
	* 傳票批號<br>
	* 
	* @return Integer
	*/
  public int getBatchNo() {
    return this.batchNo;
  }

/**
	* 傳票批號<br>
	* 
  *
  * @param batchNo 傳票批號
	*/
  public void setBatchNo(int batchNo) {
    this.batchNo = batchNo;
  }

/**
	* 上傳核心序號<br>
	* 相關說明有二:
1.關帳時,由L6101連動,以AcClose.CoreSeqNo放在titaVo.MediaSeq傳入
2.上傳EBS Webservice此欄位使用於ETL批號(GROUP_ID)
	* @return Integer
	*/
  public int getMediaSeq() {
    return this.mediaSeq;
  }

/**
	* 上傳核心序號<br>
	* 相關說明有二:
1.關帳時,由L6101連動,以AcClose.CoreSeqNo放在titaVo.MediaSeq傳入
2.上傳EBS Webservice此欄位使用於ETL批號(GROUP_ID)
  *
  * @param mediaSeq 上傳核心序號
	*/
  public void setMediaSeq(int mediaSeq) {
    this.mediaSeq = mediaSeq;
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
	* 部門代號<br>
	* 
	* @return String
	*/
  public String getDeptCode() {
    return this.deptCode == null ? "" : this.deptCode;
  }

/**
	* 部門代號<br>
	* 
  *
  * @param deptCode 部門代號
	*/
  public void setDeptCode(String deptCode) {
    this.deptCode = deptCode;
  }

/**
	* 借貸別<br>
	* D:借
C:貸
	* @return String
	*/
  public String getDbCr() {
    return this.dbCr == null ? "" : this.dbCr;
  }

/**
	* 借貸別<br>
	* D:借
C:貸
  *
  * @param dbCr 借貸別
	*/
  public void setDbCr(String dbCr) {
    this.dbCr = dbCr;
  }

/**
	* 金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getTxAmt() {
    return this.txAmt;
  }

/**
	* 金額<br>
	* 
  *
  * @param txAmt 金額
	*/
  public void setTxAmt(BigDecimal txAmt) {
    this.txAmt = txAmt;
  }

/**
	* 傳票摘要<br>
	* 
	* @return String
	*/
  public String getSlipRmk() {
    return this.slipRmk == null ? "" : this.slipRmk;
  }

/**
	* 傳票摘要<br>
	* 
  *
  * @param slipRmk 傳票摘要
	*/
  public void setSlipRmk(String slipRmk) {
    this.slipRmk = slipRmk;
  }

/**
	* 會計科目銷帳碼<br>
	* 
	* @return String
	*/
  public String getReceiveCode() {
    return this.receiveCode == null ? "" : this.receiveCode;
  }

/**
	* 會計科目銷帳碼<br>
	* 
  *
  * @param receiveCode 會計科目銷帳碼
	*/
  public void setReceiveCode(String receiveCode) {
    this.receiveCode = receiveCode;
  }

/**
	* 成本月份<br>
	* 
	* @return String
	*/
  public String getCostMonth() {
    return this.costMonth == null ? "" : this.costMonth;
  }

/**
	* 成本月份<br>
	* 
  *
  * @param costMonth 成本月份
	*/
  public void setCostMonth(String costMonth) {
    this.costMonth = costMonth;
  }

/**
	* 保單號碼<br>
	* 
	* @return String
	*/
  public String getInsuNo() {
    return this.insuNo == null ? "" : this.insuNo;
  }

/**
	* 保單號碼<br>
	* 
  *
  * @param insuNo 保單號碼
	*/
  public void setInsuNo(String insuNo) {
    this.insuNo = insuNo;
  }

/**
	* 業務員代號<br>
	* 
	* @return String
	*/
  public String getSalesmanCode() {
    return this.salesmanCode == null ? "" : this.salesmanCode;
  }

/**
	* 業務員代號<br>
	* 
  *
  * @param salesmanCode 業務員代號
	*/
  public void setSalesmanCode(String salesmanCode) {
    this.salesmanCode = salesmanCode;
  }

/**
	* 薪碼<br>
	* 
	* @return String
	*/
  public String getSalaryCode() {
    return this.salaryCode == null ? "" : this.salaryCode;
  }

/**
	* 薪碼<br>
	* 
  *
  * @param salaryCode 薪碼
	*/
  public void setSalaryCode(String salaryCode) {
    this.salaryCode = salaryCode;
  }

/**
	* 幣別<br>
	* NTD:台幣(NT)
	* @return String
	*/
  public String getCurrencyCode() {
    return this.currencyCode == null ? "" : this.currencyCode;
  }

/**
	* 幣別<br>
	* NTD:台幣(NT)
  *
  * @param currencyCode 幣別
	*/
  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

/**
	* 區隔帳冊<br>
	* 00A:傳統帳冊
101:分紅帳冊
201:利變年金帳冊
203:利變萬能帳冊
204:利變壽險帳冊
30A:OIU傳統帳冊
301:OIU利變年金
303:OIU利變萬能
304:OIU利變壽險
	* @return String
	*/
  public String getAcSubBookCode() {
    return this.acSubBookCode == null ? "" : this.acSubBookCode;
  }

/**
	* 區隔帳冊<br>
	* 00A:傳統帳冊
101:分紅帳冊
201:利變年金帳冊
203:利變萬能帳冊
204:利變壽險帳冊
30A:OIU傳統帳冊
301:OIU利變年金
303:OIU利變萬能
304:OIU利變壽險
  *
  * @param acSubBookCode 區隔帳冊
	*/
  public void setAcSubBookCode(String acSubBookCode) {
    this.acSubBookCode = acSubBookCode;
  }

/**
	* 成本單位<br>
	* 暫訂作法為IFRS4準則下比照「部門代號」欄位提供，但現行提供通路單位(2碼)者需對應回6碼的單位代號；IFRS17準則下提供規則待訂
	* @return String
	*/
  public String getCostUnit() {
    return this.CostUnit == null ? "" : this.CostUnit;
  }

/**
	* 成本單位<br>
	* 暫訂作法為IFRS4準則下比照「部門代號」欄位提供，但現行提供通路單位(2碼)者需對應回6碼的單位代號；IFRS17準則下提供規則待訂
  *
  * @param CostUnit 成本單位
	*/
  public void setCostUnit(String CostUnit) {
    this.CostUnit = CostUnit;
  }

/**
	* 通路別<br>
	* 核心(團終養)需提供，其他系統不用提供(空值)
	* @return String
	*/
  public String getSalesChannelType() {
    return this.salesChannelType == null ? "" : this.salesChannelType;
  }

/**
	* 通路別<br>
	* 核心(團終養)需提供，其他系統不用提供(空值)
  *
  * @param salesChannelType 通路別
	*/
  public void setSalesChannelType(String salesChannelType) {
    this.salesChannelType = salesChannelType;
  }

/**
	* 會計準則類型<br>
	* 1:IFRS4
2:IFRS17
	* @return String
	*/
  public String getIfrsType() {
    return this.ifrsType == null ? "" : this.ifrsType;
  }

/**
	* 會計準則類型<br>
	* 1:IFRS4
2:IFRS17
  *
  * @param ifrsType 會計準則類型
	*/
  public void setIfrsType(String ifrsType) {
    this.ifrsType = ifrsType;
  }

/**
	* 關係人ID<br>
	* 預留欄位，各系統暫不需提供資料
	* @return String
	*/
  public String getRelationId() {
    return this.relationId == null ? "" : this.relationId;
  }

/**
	* 關係人ID<br>
	* 預留欄位，各系統暫不需提供資料
  *
  * @param relationId 關係人ID
	*/
  public void setRelationId(String relationId) {
    this.relationId = relationId;
  }

/**
	* 關聯方代號<br>
	* 預留欄位，各系統暫不需提供資料
	* @return String
	*/
  public String getRelateCode() {
    return this.relateCode == null ? "" : this.relateCode;
  }

/**
	* 關聯方代號<br>
	* 預留欄位，各系統暫不需提供資料
  *
  * @param relateCode 關聯方代號
	*/
  public void setRelateCode(String relateCode) {
    this.relateCode = relateCode;
  }

/**
	* IFRS17群組<br>
	* 核心(團終養)於IFRS17準則下需提供，其他系統不用提供(空值)
	* @return String
	*/
  public String getIfrs17Group() {
    return this.ifrs17Group == null ? "" : this.ifrs17Group;
  }

/**
	* IFRS17群組<br>
	* 核心(團終養)於IFRS17準則下需提供，其他系統不用提供(空值)
  *
  * @param ifrs17Group IFRS17群組
	*/
  public void setIfrs17Group(String ifrs17Group) {
    this.ifrs17Group = ifrs17Group;
  }

/**
	* 是否為最新<br>
	* Y:是
N:否
	* @return String
	*/
  public String getLatestFlag() {
    return this.latestFlag == null ? "" : this.latestFlag;
  }

/**
	* 是否為最新<br>
	* Y:是
N:否
  *
  * @param latestFlag 是否為最新
	*/
  public void setLatestFlag(String latestFlag) {
    this.latestFlag = latestFlag;
  }

/**
	* 是否已傳輸<br>
	* Y:是
N:否
	* @return String
	*/
  public String getTransferFlag() {
    return this.transferFlag == null ? "" : this.transferFlag;
  }

/**
	* 是否已傳輸<br>
	* Y:是
N:否
  *
  * @param transferFlag 是否已傳輸
	*/
  public void setTransferFlag(String transferFlag) {
    this.transferFlag = transferFlag;
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

/**
	* 回應錯誤代碼<br>
	* 
	* @return String
	*/
  public String getErrorCode() {
    return this.errorCode == null ? "" : this.errorCode;
  }

/**
	* 回應錯誤代碼<br>
	* 
  *
  * @param errorCode 回應錯誤代碼
	*/
  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

/**
	* 回應錯誤訊息<br>
	* 
	* @return String
	*/
  public String getErrorMsg() {
    return this.errorMsg == null ? "" : this.errorMsg;
  }

/**
	* 回應錯誤訊息<br>
	* 
  *
  * @param errorMsg 回應錯誤訊息
	*/
  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }


  @Override
  public String toString() {
    return "SlipMedia2022 [slipMedia2022Id=" + slipMedia2022Id + ", acBookCode=" + acBookCode + ", acDate=" + acDate + ", batchNo=" + batchNo + ", mediaSeq=" + mediaSeq
           + ", acNoCode=" + acNoCode + ", acSubCode=" + acSubCode + ", deptCode=" + deptCode + ", dbCr=" + dbCr + ", txAmt=" + txAmt + ", slipRmk=" + slipRmk
           + ", receiveCode=" + receiveCode + ", costMonth=" + costMonth + ", insuNo=" + insuNo + ", salesmanCode=" + salesmanCode + ", salaryCode=" + salaryCode + ", currencyCode=" + currencyCode
           + ", acSubBookCode=" + acSubBookCode + ", CostUnit=" + CostUnit + ", salesChannelType=" + salesChannelType + ", ifrsType=" + ifrsType + ", relationId=" + relationId + ", relateCode=" + relateCode
           + ", ifrs17Group=" + ifrs17Group + ", latestFlag=" + latestFlag + ", transferFlag=" + transferFlag + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + ", errorCode=" + errorCode + ", errorMsg=" + errorMsg + "]";
  }
}
