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
 * InsuRenew 火險單續保檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`InsuRenew`")
public class InsuRenew implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -3654277774403173324L;

@EmbeddedId
  private InsuRenewId insuRenewId;

  // 擔保品-代號1
  @Column(name = "`ClCode1`", insertable = false, updatable = false)
  private int clCode1 = 0;

  // 擔保品-代號2
  @Column(name = "`ClCode2`", insertable = false, updatable = false)
  private int clCode2 = 0;

  // 擔保品編號
  @Column(name = "`ClNo`", insertable = false, updatable = false)
  private int clNo = 0;

  // 原保單號碼
  @Column(name = "`PrevInsuNo`", length = 17, insertable = false, updatable = false)
  private String prevInsuNo;

  // 批單號碼
  /* 修改時需填入 */
  @Column(name = "`EndoInsuNo`", length = 17, insertable = false, updatable = false)
  private String endoInsuNo;

  // 火險單年月
  /* 原火險到期年月 */
  @Column(name = "`InsuYearMonth`")
  private int insuYearMonth = 0;

  // 借款人戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 保險單號碼
  @Column(name = "`NowInsuNo`", length = 17)
  private String nowInsuNo;

  // 原始保險單號碼
  @Column(name = "`OrigInsuNo`", length = 17)
  private String origInsuNo;

  // 是否續保
  /* 0.1.自保2.續保 */
  @Column(name = "`RenewCode`")
  private int renewCode = 0;

  // 保險公司
  @Column(name = "`InsuCompany`", length = 2)
  private String insuCompany;

  // 保險類別
  /* CdCode:InsuTypeCode01:住宅火險地震險02:火險03:地震險04:汽車全險05:綜合營造險06:動產火險07:其他 */
  @Column(name = "`InsuTypeCode`", length = 2)
  private String insuTypeCode;

  // 繳款方式
  /* CdCode:RepayCode1:匯款轉帳2:銀行扣款3:員工扣薪4:支票5:特約金6:人事特約金7:定存特約8:劃撥存款 */
  @Column(name = "`RepayCode`")
  private int repayCode = 0;

  // 火災險保險金額
  @Column(name = "`FireInsuCovrg`")
  private BigDecimal fireInsuCovrg = new BigDecimal("0");

  // 地震險保險金額
  @Column(name = "`EthqInsuCovrg`")
  private BigDecimal ethqInsuCovrg = new BigDecimal("0");

  // 火災險保費
  @Column(name = "`FireInsuPrem`")
  private BigDecimal fireInsuPrem = new BigDecimal("0");

  // 地震險保費
  @Column(name = "`EthqInsuPrem`")
  private BigDecimal ethqInsuPrem = new BigDecimal("0");

  // 保險起日
  @Column(name = "`InsuStartDate`")
  private int insuStartDate = 0;

  // 保險迄日
  @Column(name = "`InsuEndDate`")
  private int insuEndDate = 0;

  // 總保費
  @Column(name = "`TotInsuPrem`")
  private BigDecimal totInsuPrem = new BigDecimal("0");

  // 會計日期
  /* 繳款會計日 */
  @Column(name = "`AcDate`")
  private int acDate = 0;

  // 經辦
  @Column(name = "`TitaTlrNo`", length = 6)
  private String titaTlrNo;

  // 交易序號
  @Column(name = "`TitaTxtNo`", length = 8)
  private String titaTxtNo;

  // 入通知檔
  /* Y:已入N:未入(通知作業後新增)null:待通知 */
  @Column(name = "`NotiTempFg`", length = 1)
  private String notiTempFg;

  // 處理代碼
  /* 0:正常1:借支2:催收4:結案 */
  @Column(name = "`StatusCode`")
  private int statusCode = 0;

  // 轉催收日
  @Column(name = "`OvduDate`")
  private int ovduDate = 0;

  // 轉催編號
  @Column(name = "`OvduNo`")
  private BigDecimal ovduNo = new BigDecimal("0");

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


  public InsuRenewId getInsuRenewId() {
    return this.insuRenewId;
  }

  public void setInsuRenewId(InsuRenewId insuRenewId) {
    this.insuRenewId = insuRenewId;
  }

/**
	* 擔保品-代號1<br>
	* 
	* @return Integer
	*/
  public int getClCode1() {
    return this.clCode1;
  }

/**
	* 擔保品-代號1<br>
	* 
  *
  * @param clCode1 擔保品-代號1
	*/
  public void setClCode1(int clCode1) {
    this.clCode1 = clCode1;
  }

/**
	* 擔保品-代號2<br>
	* 
	* @return Integer
	*/
  public int getClCode2() {
    return this.clCode2;
  }

/**
	* 擔保品-代號2<br>
	* 
  *
  * @param clCode2 擔保品-代號2
	*/
  public void setClCode2(int clCode2) {
    this.clCode2 = clCode2;
  }

/**
	* 擔保品編號<br>
	* 
	* @return Integer
	*/
  public int getClNo() {
    return this.clNo;
  }

/**
	* 擔保品編號<br>
	* 
  *
  * @param clNo 擔保品編號
	*/
  public void setClNo(int clNo) {
    this.clNo = clNo;
  }

/**
	* 原保單號碼<br>
	* 
	* @return String
	*/
  public String getPrevInsuNo() {
    return this.prevInsuNo == null ? "" : this.prevInsuNo;
  }

/**
	* 原保單號碼<br>
	* 
  *
  * @param prevInsuNo 原保單號碼
	*/
  public void setPrevInsuNo(String prevInsuNo) {
    this.prevInsuNo = prevInsuNo;
  }

/**
	* 批單號碼<br>
	* 修改時需填入
	* @return String
	*/
  public String getEndoInsuNo() {
    return this.endoInsuNo == null ? "" : this.endoInsuNo;
  }

/**
	* 批單號碼<br>
	* 修改時需填入
  *
  * @param endoInsuNo 批單號碼
	*/
  public void setEndoInsuNo(String endoInsuNo) {
    this.endoInsuNo = endoInsuNo;
  }

/**
	* 火險單年月<br>
	* 原火險到期年月
	* @return Integer
	*/
  public int getInsuYearMonth() {
    return this.insuYearMonth;
  }

/**
	* 火險單年月<br>
	* 原火險到期年月
  *
  * @param insuYearMonth 火險單年月
	*/
  public void setInsuYearMonth(int insuYearMonth) {
    this.insuYearMonth = insuYearMonth;
  }

/**
	* 借款人戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 借款人戶號<br>
	* 
  *
  * @param custNo 借款人戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 額度<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度<br>
	* 
  *
  * @param facmNo 額度
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 保險單號碼<br>
	* 
	* @return String
	*/
  public String getNowInsuNo() {
    return this.nowInsuNo == null ? "" : this.nowInsuNo;
  }

/**
	* 保險單號碼<br>
	* 
  *
  * @param nowInsuNo 保險單號碼
	*/
  public void setNowInsuNo(String nowInsuNo) {
    this.nowInsuNo = nowInsuNo;
  }

/**
	* 原始保險單號碼<br>
	* 
	* @return String
	*/
  public String getOrigInsuNo() {
    return this.origInsuNo == null ? "" : this.origInsuNo;
  }

/**
	* 原始保險單號碼<br>
	* 
  *
  * @param origInsuNo 原始保險單號碼
	*/
  public void setOrigInsuNo(String origInsuNo) {
    this.origInsuNo = origInsuNo;
  }

/**
	* 是否續保<br>
	* 0.
1.自保
2.續保
	* @return Integer
	*/
  public int getRenewCode() {
    return this.renewCode;
  }

/**
	* 是否續保<br>
	* 0.
1.自保
2.續保
  *
  * @param renewCode 是否續保
	*/
  public void setRenewCode(int renewCode) {
    this.renewCode = renewCode;
  }

/**
	* 保險公司<br>
	* 
	* @return String
	*/
  public String getInsuCompany() {
    return this.insuCompany == null ? "" : this.insuCompany;
  }

/**
	* 保險公司<br>
	* 
  *
  * @param insuCompany 保險公司
	*/
  public void setInsuCompany(String insuCompany) {
    this.insuCompany = insuCompany;
  }

/**
	* 保險類別<br>
	* CdCode:InsuTypeCode
01:住宅火險地震險
02:火險
03:地震險
04:汽車全險
05:綜合營造險
06:動產火險
07:其他
	* @return String
	*/
  public String getInsuTypeCode() {
    return this.insuTypeCode == null ? "" : this.insuTypeCode;
  }

/**
	* 保險類別<br>
	* CdCode:InsuTypeCode
01:住宅火險地震險
02:火險
03:地震險
04:汽車全險
05:綜合營造險
06:動產火險
07:其他
  *
  * @param insuTypeCode 保險類別
	*/
  public void setInsuTypeCode(String insuTypeCode) {
    this.insuTypeCode = insuTypeCode;
  }

/**
	* 繳款方式<br>
	* CdCode:RepayCode
1:匯款轉帳
2:銀行扣款
3:員工扣薪
4:支票
5:特約金
6:人事特約金
7:定存特約
8:劃撥存款
	* @return Integer
	*/
  public int getRepayCode() {
    return this.repayCode;
  }

/**
	* 繳款方式<br>
	* CdCode:RepayCode
1:匯款轉帳
2:銀行扣款
3:員工扣薪
4:支票
5:特約金
6:人事特約金
7:定存特約
8:劃撥存款
  *
  * @param repayCode 繳款方式
	*/
  public void setRepayCode(int repayCode) {
    this.repayCode = repayCode;
  }

/**
	* 火災險保險金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getFireInsuCovrg() {
    return this.fireInsuCovrg;
  }

/**
	* 火災險保險金額<br>
	* 
  *
  * @param fireInsuCovrg 火災險保險金額
	*/
  public void setFireInsuCovrg(BigDecimal fireInsuCovrg) {
    this.fireInsuCovrg = fireInsuCovrg;
  }

/**
	* 地震險保險金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getEthqInsuCovrg() {
    return this.ethqInsuCovrg;
  }

/**
	* 地震險保險金額<br>
	* 
  *
  * @param ethqInsuCovrg 地震險保險金額
	*/
  public void setEthqInsuCovrg(BigDecimal ethqInsuCovrg) {
    this.ethqInsuCovrg = ethqInsuCovrg;
  }

/**
	* 火災險保費<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getFireInsuPrem() {
    return this.fireInsuPrem;
  }

/**
	* 火災險保費<br>
	* 
  *
  * @param fireInsuPrem 火災險保費
	*/
  public void setFireInsuPrem(BigDecimal fireInsuPrem) {
    this.fireInsuPrem = fireInsuPrem;
  }

/**
	* 地震險保費<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getEthqInsuPrem() {
    return this.ethqInsuPrem;
  }

/**
	* 地震險保費<br>
	* 
  *
  * @param ethqInsuPrem 地震險保費
	*/
  public void setEthqInsuPrem(BigDecimal ethqInsuPrem) {
    this.ethqInsuPrem = ethqInsuPrem;
  }

/**
	* 保險起日<br>
	* 
	* @return Integer
	*/
  public int getInsuStartDate() {
    return StaticTool.bcToRoc(this.insuStartDate);
  }

/**
	* 保險起日<br>
	* 
  *
  * @param insuStartDate 保險起日
  * @throws LogicException when Date Is Warn	*/
  public void setInsuStartDate(int insuStartDate) throws LogicException {
    this.insuStartDate = StaticTool.rocToBc(insuStartDate);
  }

/**
	* 保險迄日<br>
	* 
	* @return Integer
	*/
  public int getInsuEndDate() {
    return StaticTool.bcToRoc(this.insuEndDate);
  }

/**
	* 保險迄日<br>
	* 
  *
  * @param insuEndDate 保險迄日
  * @throws LogicException when Date Is Warn	*/
  public void setInsuEndDate(int insuEndDate) throws LogicException {
    this.insuEndDate = StaticTool.rocToBc(insuEndDate);
  }

/**
	* 總保費<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getTotInsuPrem() {
    return this.totInsuPrem;
  }

/**
	* 總保費<br>
	* 
  *
  * @param totInsuPrem 總保費
	*/
  public void setTotInsuPrem(BigDecimal totInsuPrem) {
    this.totInsuPrem = totInsuPrem;
  }

/**
	* 會計日期<br>
	* 繳款會計日
	* @return Integer
	*/
  public int getAcDate() {
    return StaticTool.bcToRoc(this.acDate);
  }

/**
	* 會計日期<br>
	* 繳款會計日
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
	* 入通知檔<br>
	* Y:已入
N:未入(通知作業後新增)
null:待通知
	* @return String
	*/
  public String getNotiTempFg() {
    return this.notiTempFg == null ? "" : this.notiTempFg;
  }

/**
	* 入通知檔<br>
	* Y:已入
N:未入(通知作業後新增)
null:待通知
  *
  * @param notiTempFg 入通知檔
	*/
  public void setNotiTempFg(String notiTempFg) {
    this.notiTempFg = notiTempFg;
  }

/**
	* 處理代碼<br>
	* 0:正常
1:借支
2:催收
4:結案
	* @return Integer
	*/
  public int getStatusCode() {
    return this.statusCode;
  }

/**
	* 處理代碼<br>
	* 0:正常
1:借支
2:催收
4:結案
  *
  * @param statusCode 處理代碼
	*/
  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

/**
	* 轉催收日<br>
	* 
	* @return Integer
	*/
  public int getOvduDate() {
    return this.ovduDate;
  }

/**
	* 轉催收日<br>
	* 
  *
  * @param ovduDate 轉催收日
	*/
  public void setOvduDate(int ovduDate) {
    this.ovduDate = ovduDate;
  }

/**
	* 轉催編號<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOvduNo() {
    return this.ovduNo;
  }

/**
	* 轉催編號<br>
	* 
  *
  * @param ovduNo 轉催編號
	*/
  public void setOvduNo(BigDecimal ovduNo) {
    this.ovduNo = ovduNo;
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
    return "InsuRenew [insuRenewId=" + insuRenewId + ", insuYearMonth=" + insuYearMonth
           + ", custNo=" + custNo + ", facmNo=" + facmNo + ", nowInsuNo=" + nowInsuNo + ", origInsuNo=" + origInsuNo + ", renewCode=" + renewCode + ", insuCompany=" + insuCompany
           + ", insuTypeCode=" + insuTypeCode + ", repayCode=" + repayCode + ", fireInsuCovrg=" + fireInsuCovrg + ", ethqInsuCovrg=" + ethqInsuCovrg + ", fireInsuPrem=" + fireInsuPrem + ", ethqInsuPrem=" + ethqInsuPrem
           + ", insuStartDate=" + insuStartDate + ", insuEndDate=" + insuEndDate + ", totInsuPrem=" + totInsuPrem + ", acDate=" + acDate + ", titaTlrNo=" + titaTlrNo + ", titaTxtNo=" + titaTxtNo
           + ", notiTempFg=" + notiTempFg + ", statusCode=" + statusCode + ", ovduDate=" + ovduDate + ", ovduNo=" + ovduNo + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
