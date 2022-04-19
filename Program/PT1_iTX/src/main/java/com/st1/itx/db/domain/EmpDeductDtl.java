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
 * EmpDeductDtl 員工扣薪明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`EmpDeductDtl`")
public class EmpDeductDtl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7322691085386636677L;

	@EmbeddedId
	private EmpDeductDtlId empDeductDtlId;

	// 入帳日期
	@Column(name = "`EntryDate`", insertable = false, updatable = false)
	private int entryDate = 0;

	// 戶號
	@Column(name = "`CustNo`", insertable = false, updatable = false)
	private int custNo = 0;

	// 入帳扣款別
	/* CdCode:RepayType1.期款2.部分償還3.結案4.帳管費5.火險費6.契變手續費7.法務費9.其他 */
	@Column(name = "`AchRepayCode`", insertable = false, updatable = false)
	private int achRepayCode = 0;

	// 業績年月
	@Column(name = "`PerfMonth`", insertable = false, updatable = false)
	private int perfMonth = 0;

	// 流程別
	@Column(name = "`ProcCode`", length = 1, insertable = false, updatable = false)
	private String procCode;

	// 扣款代碼
	/* CdCode:PerfRepayCode1:扣薪件;2:特約件;3:滯繳件;4:人事特約件;5:房貸扣薪件 */
	@Column(name = "`RepayCode`", length = 1, insertable = false, updatable = false)
	private String repayCode;

	// 科目
	@Column(name = "`AcctCode`", length = 12, insertable = false, updatable = false)
	private String acctCode;

	// 額度編號
	@Column(name = "`FacmNo`", insertable = false, updatable = false)
	private int facmNo = 0;

	// 撥款編號
	@Column(name = "`BormNo`", insertable = false, updatable = false)
	private int bormNo = 0;

	// 員工代號
	@Column(name = "`EmpNo`", length = 6)
	private String empNo;

	// 統一編號
	@Column(name = "`CustId`", length = 10)
	private String custId;

	// 交易金額(實扣金額)
	@Column(name = "`TxAmt`")
	private BigDecimal txAmt = new BigDecimal("0");

	// 失敗原因
	@Column(name = "`ErrMsg`", length = 20)
	private String errMsg;

	// 會計日期
	/* 入帳時更新 */
	@Column(name = "`Acdate`")
	private int acdate = 0;

	// 經辦
	/* 入帳時更新 */
	@Column(name = "`TitaTlrNo`", length = 6)
	private String titaTlrNo;

	// 交易序號
	/* 入帳時更新 */
	@Column(name = "`TitaTxtNo`", length = 8)
	private String titaTxtNo;

	// 批次號碼
	/* 入帳時更新 */
	@Column(name = "`BatchNo`", length = 6)
	private String batchNo;

	// 應扣金額
	@Column(name = "`RepayAmt`")
	private BigDecimal repayAmt = new BigDecimal("0");

	// 離職代碼
	@Column(name = "`ResignCode`", length = 2)
	private String resignCode;

	// 部室代號
	@Column(name = "`DeptCode`", length = 6)
	private String deptCode;

	// 單位代號
	@Column(name = "`UnitCode`", length = 6)
	private String unitCode;

	// 計息起日
	@Column(name = "`IntStartDate`")
	private int intStartDate = 0;

	// 計息迄日
	@Column(name = "`IntEndDate`")
	private int intEndDate = 0;

	// 職務代號
	@Column(name = "`PositCode`", length = 2)
	private String positCode;

	// 本金
	@Column(name = "`Principal`")
	private BigDecimal principal = new BigDecimal("0");

	// 利息
	@Column(name = "`Interest`")
	private BigDecimal interest = new BigDecimal("0");

	// 累溢短收
	@Column(name = "`SumOvpayAmt`")
	private BigDecimal sumOvpayAmt = new BigDecimal("0");

	// jason格式紀錄欄
	/* 暫收抵繳 TempAmt欠繳本金 ShortPri欠繳利息 ShortInt違約金 Breach火險單號碼 InsuNo(split by ,) */
	@Column(name = "`JsonFields`", length = 300)
	private String jsonFields;

	// 當期利息
	@Column(name = "`CurrIntAmt`")
	private BigDecimal currIntAmt = new BigDecimal("0");

	// 當期本金
	@Column(name = "`CurrPrinAmt`")
	private BigDecimal currPrinAmt = new BigDecimal("0");

	// 媒體日期
	/* 員工扣薪媒體檔 */
	@Column(name = "`MediaDate`")
	private int mediaDate = 0;

	// 媒體別
	/* 1:ACH新光2:ACH他行3:郵局4:15日5:非15日 */
	@Column(name = "`MediaKind`", length = 1)
	private String mediaKind;

	// 媒體序號
	/* 員工扣薪媒體檔 */
	@Column(name = "`MediaSeq`")
	private int mediaSeq = 0;

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

	public EmpDeductDtlId getEmpDeductDtlId() {
		return this.empDeductDtlId;
	}

	public void setEmpDeductDtlId(EmpDeductDtlId empDeductDtlId) {
		this.empDeductDtlId = empDeductDtlId;
	}

	/**
	 * 入帳日期<br>
	 * 
	 * @return Integer
	 */
	public int getEntryDate() {
		return StaticTool.bcToRoc(this.entryDate);
	}

	/**
	 * 入帳日期<br>
	 * 
	 *
	 * @param entryDate 入帳日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setEntryDate(int entryDate) throws LogicException {
		this.entryDate = StaticTool.rocToBc(entryDate);
	}

	/**
	 * 戶號<br>
	 * 
	 * @return Integer
	 */
	public int getCustNo() {
		return this.custNo;
	}

	/**
	 * 戶號<br>
	 * 
	 *
	 * @param custNo 戶號
	 */
	public void setCustNo(int custNo) {
		this.custNo = custNo;
	}

	/**
	 * 入帳扣款別<br>
	 * CdCode:RepayType 1.期款 2.部分償還 3.結案 4.帳管費 5.火險費 6.契變手續費 7.法務費 9.其他
	 * 
	 * @return Integer
	 */
	public int getAchRepayCode() {
		return this.achRepayCode;
	}

	/**
	 * 入帳扣款別<br>
	 * CdCode:RepayType 1.期款 2.部分償還 3.結案 4.帳管費 5.火險費 6.契變手續費 7.法務費 9.其他
	 *
	 * @param achRepayCode 入帳扣款別
	 */
	public void setAchRepayCode(int achRepayCode) {
		this.achRepayCode = achRepayCode;
	}

	/**
	 * 業績年月<br>
	 * 
	 * @return Integer
	 */
	public int getPerfMonth() {
		return this.perfMonth;
	}

	/**
	 * 業績年月<br>
	 * 
	 *
	 * @param perfMonth 業績年月
	 */
	public void setPerfMonth(int perfMonth) {
		this.perfMonth = perfMonth;
	}

	/**
	 * 流程別<br>
	 * 
	 * @return String
	 */
	public String getProcCode() {
		return this.procCode == null ? "" : this.procCode;
	}

	/**
	 * 流程別<br>
	 * 
	 *
	 * @param procCode 流程別
	 */
	public void setProcCode(String procCode) {
		this.procCode = procCode;
	}

	/**
	 * 扣款代碼<br>
	 * CdCode:PerfRepayCode 1:扣薪件;2:特約件;3:滯繳件;4:人事特約件;5:房貸扣薪件
	 * 
	 * @return String
	 */
	public String getRepayCode() {
		return this.repayCode == null ? "" : this.repayCode;
	}

	/**
	 * 扣款代碼<br>
	 * CdCode:PerfRepayCode 1:扣薪件;2:特約件;3:滯繳件;4:人事特約件;5:房貸扣薪件
	 *
	 * @param repayCode 扣款代碼
	 */
	public void setRepayCode(String repayCode) {
		this.repayCode = repayCode;
	}

	/**
	 * 科目<br>
	 * 
	 * @return String
	 */
	public String getAcctCode() {
		return this.acctCode == null ? "" : this.acctCode;
	}

	/**
	 * 科目<br>
	 * 
	 *
	 * @param acctCode 科目
	 */
	public void setAcctCode(String acctCode) {
		this.acctCode = acctCode;
	}

	/**
	 * 額度編號<br>
	 * 
	 * @return Integer
	 */
	public int getFacmNo() {
		return this.facmNo;
	}

	/**
	 * 額度編號<br>
	 * 
	 *
	 * @param facmNo 額度編號
	 */
	public void setFacmNo(int facmNo) {
		this.facmNo = facmNo;
	}

	/**
	 * 撥款編號<br>
	 * 
	 * @return Integer
	 */
	public int getBormNo() {
		return this.bormNo;
	}

	/**
	 * 撥款編號<br>
	 * 
	 *
	 * @param bormNo 撥款編號
	 */
	public void setBormNo(int bormNo) {
		this.bormNo = bormNo;
	}

	/**
	 * 員工代號<br>
	 * 
	 * @return String
	 */
	public String getEmpNo() {
		return this.empNo == null ? "" : this.empNo;
	}

	/**
	 * 員工代號<br>
	 * 
	 *
	 * @param empNo 員工代號
	 */
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	/**
	 * 統一編號<br>
	 * 
	 * @return String
	 */
	public String getCustId() {
		return this.custId == null ? "" : this.custId;
	}

	/**
	 * 統一編號<br>
	 * 
	 *
	 * @param custId 統一編號
	 */
	public void setCustId(String custId) {
		this.custId = custId;
	}

	/**
	 * 交易金額(實扣金額)<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getTxAmt() {
		return this.txAmt;
	}

	/**
	 * 交易金額(實扣金額)<br>
	 * 
	 *
	 * @param txAmt 交易金額(實扣金額)
	 */
	public void setTxAmt(BigDecimal txAmt) {
		this.txAmt = txAmt;
	}

	/**
	 * 失敗原因<br>
	 * 
	 * @return String
	 */
	public String getErrMsg() {
		return this.errMsg == null ? "" : this.errMsg;
	}

	/**
	 * 失敗原因<br>
	 * 
	 *
	 * @param errMsg 失敗原因
	 */
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	/**
	 * 會計日期<br>
	 * 入帳時更新
	 * 
	 * @return Integer
	 */
	public int getAcdate() {
		return StaticTool.bcToRoc(this.acdate);
	}

	/**
	 * 會計日期<br>
	 * 入帳時更新
	 *
	 * @param acdate 會計日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setAcdate(int acdate) throws LogicException {
		this.acdate = StaticTool.rocToBc(acdate);
	}

	/**
	 * 經辦<br>
	 * 入帳時更新
	 * 
	 * @return String
	 */
	public String getTitaTlrNo() {
		return this.titaTlrNo == null ? "" : this.titaTlrNo;
	}

	/**
	 * 經辦<br>
	 * 入帳時更新
	 *
	 * @param titaTlrNo 經辦
	 */
	public void setTitaTlrNo(String titaTlrNo) {
		this.titaTlrNo = titaTlrNo;
	}

	/**
	 * 交易序號<br>
	 * 入帳時更新
	 * 
	 * @return String
	 */
	public String getTitaTxtNo() {
		return this.titaTxtNo == null ? "" : this.titaTxtNo;
	}

	/**
	 * 交易序號<br>
	 * 入帳時更新
	 *
	 * @param titaTxtNo 交易序號
	 */
	public void setTitaTxtNo(String titaTxtNo) {
		this.titaTxtNo = titaTxtNo;
	}

	/**
	 * 批次號碼<br>
	 * 入帳時更新
	 * 
	 * @return String
	 */
	public String getBatchNo() {
		return this.batchNo == null ? "" : this.batchNo;
	}

	/**
	 * 批次號碼<br>
	 * 入帳時更新
	 *
	 * @param batchNo 批次號碼
	 */
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	/**
	 * 應扣金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getRepayAmt() {
		return this.repayAmt;
	}

	/**
	 * 應扣金額<br>
	 * 
	 *
	 * @param repayAmt 應扣金額
	 */
	public void setRepayAmt(BigDecimal repayAmt) {
		this.repayAmt = repayAmt;
	}

	/**
	 * 離職代碼<br>
	 * 
	 * @return String
	 */
	public String getResignCode() {
		return this.resignCode == null ? "" : this.resignCode;
	}

	/**
	 * 離職代碼<br>
	 * 
	 *
	 * @param resignCode 離職代碼
	 */
	public void setResignCode(String resignCode) {
		this.resignCode = resignCode;
	}

	/**
	 * 部室代號<br>
	 * 
	 * @return String
	 */
	public String getDeptCode() {
		return this.deptCode == null ? "" : this.deptCode;
	}

	/**
	 * 部室代號<br>
	 * 
	 *
	 * @param deptCode 部室代號
	 */
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	/**
	 * 單位代號<br>
	 * 
	 * @return String
	 */
	public String getUnitCode() {
		return this.unitCode == null ? "" : this.unitCode;
	}

	/**
	 * 單位代號<br>
	 * 
	 *
	 * @param unitCode 單位代號
	 */
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	/**
	 * 計息起日<br>
	 * 
	 * @return Integer
	 */
	public int getIntStartDate() {
		return StaticTool.bcToRoc(this.intStartDate);
	}

	/**
	 * 計息起日<br>
	 * 
	 *
	 * @param intStartDate 計息起日
	 * @throws LogicException when Date Is Warn
	 */
	public void setIntStartDate(int intStartDate) throws LogicException {
		this.intStartDate = StaticTool.rocToBc(intStartDate);
	}

	/**
	 * 計息迄日<br>
	 * 
	 * @return Integer
	 */
	public int getIntEndDate() {
		return StaticTool.bcToRoc(this.intEndDate);
	}

	/**
	 * 計息迄日<br>
	 * 
	 *
	 * @param intEndDate 計息迄日
	 * @throws LogicException when Date Is Warn
	 */
	public void setIntEndDate(int intEndDate) throws LogicException {
		this.intEndDate = StaticTool.rocToBc(intEndDate);
	}

	/**
	 * 職務代號<br>
	 * 
	 * @return String
	 */
	public String getPositCode() {
		return this.positCode == null ? "" : this.positCode;
	}

	/**
	 * 職務代號<br>
	 * 
	 *
	 * @param positCode 職務代號
	 */
	public void setPositCode(String positCode) {
		this.positCode = positCode;
	}

	/**
	 * 本金<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPrincipal() {
		return this.principal;
	}

	/**
	 * 本金<br>
	 * 
	 *
	 * @param principal 本金
	 */
	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}

	/**
	 * 利息<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getInterest() {
		return this.interest;
	}

	/**
	 * 利息<br>
	 * 
	 *
	 * @param interest 利息
	 */
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	/**
	 * 累溢短收<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getSumOvpayAmt() {
		return this.sumOvpayAmt;
	}

	/**
	 * 累溢短收<br>
	 * 
	 *
	 * @param sumOvpayAmt 累溢短收
	 */
	public void setSumOvpayAmt(BigDecimal sumOvpayAmt) {
		this.sumOvpayAmt = sumOvpayAmt;
	}

	/**
	 * jason格式紀錄欄<br>
	 * 暫收抵繳 TempAmt 欠繳本金 ShortPri 欠繳利息 ShortInt 違約金 Breach 火險單號碼 InsuNo(split by ,)
	 * 
	 * @return String
	 */
	public String getJsonFields() {
		return this.jsonFields == null ? "" : this.jsonFields;
	}

	/**
	 * jason格式紀錄欄<br>
	 * 暫收抵繳 TempAmt 欠繳本金 ShortPri 欠繳利息 ShortInt 違約金 Breach 火險單號碼 InsuNo(split by ,)
	 *
	 * @param jsonFields jason格式紀錄欄
	 */
	public void setJsonFields(String jsonFields) {
		this.jsonFields = jsonFields;
	}

	/**
	 * 當期利息<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCurrIntAmt() {
		return this.currIntAmt;
	}

	/**
	 * 當期利息<br>
	 * 
	 *
	 * @param currIntAmt 當期利息
	 */
	public void setCurrIntAmt(BigDecimal currIntAmt) {
		this.currIntAmt = currIntAmt;
	}

	/**
	 * 當期本金<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCurrPrinAmt() {
		return this.currPrinAmt;
	}

	/**
	 * 當期本金<br>
	 * 
	 *
	 * @param currPrinAmt 當期本金
	 */
	public void setCurrPrinAmt(BigDecimal currPrinAmt) {
		this.currPrinAmt = currPrinAmt;
	}

	/**
	 * 媒體日期<br>
	 * 員工扣薪媒體檔
	 * 
	 * @return Integer
	 */
	public int getMediaDate() {
		return StaticTool.bcToRoc(this.mediaDate);
	}

	/**
	 * 媒體日期<br>
	 * 員工扣薪媒體檔
	 *
	 * @param mediaDate 媒體日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setMediaDate(int mediaDate) throws LogicException {
		this.mediaDate = StaticTool.rocToBc(mediaDate);
	}

	/**
	 * 媒體別<br>
	 * 1:ACH新光 2:ACH他行 3:郵局 4:15日 5:非15日
	 * 
	 * @return String
	 */
	public String getMediaKind() {
		return this.mediaKind == null ? "" : this.mediaKind;
	}

	/**
	 * 媒體別<br>
	 * 1:ACH新光 2:ACH他行 3:郵局 4:15日 5:非15日
	 *
	 * @param mediaKind 媒體別
	 */
	public void setMediaKind(String mediaKind) {
		this.mediaKind = mediaKind;
	}

	/**
	 * 媒體序號<br>
	 * 員工扣薪媒體檔
	 * 
	 * @return Integer
	 */
	public int getMediaSeq() {
		return this.mediaSeq;
	}

	/**
	 * 媒體序號<br>
	 * 員工扣薪媒體檔
	 *
	 * @param mediaSeq 媒體序號
	 */
	public void setMediaSeq(int mediaSeq) {
		this.mediaSeq = mediaSeq;
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
		return "EmpDeductDtl [empDeductDtlId=" + empDeductDtlId + ", empNo=" + empNo + ", custId=" + custId + ", txAmt=" + txAmt + ", errMsg=" + errMsg + ", acdate=" + acdate + ", titaTlrNo="
				+ titaTlrNo + ", titaTxtNo=" + titaTxtNo + ", batchNo=" + batchNo + ", repayAmt=" + repayAmt + ", resignCode=" + resignCode + ", deptCode=" + deptCode + ", unitCode=" + unitCode
				+ ", intStartDate=" + intStartDate + ", intEndDate=" + intEndDate + ", positCode=" + positCode + ", principal=" + principal + ", interest=" + interest + ", sumOvpayAmt=" + sumOvpayAmt
				+ ", jsonFields=" + jsonFields + ", currIntAmt=" + currIntAmt + ", currPrinAmt=" + currPrinAmt + ", mediaDate=" + mediaDate + ", mediaKind=" + mediaKind + ", mediaSeq=" + mediaSeq
				+ ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
