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
 * JcicZ047 金融機構無擔保債務協議資料檔案<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ047`")
public class JcicZ047 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8936994456724808866L;

	@EmbeddedId
	private JcicZ047Id jcicZ047Id;

	// 交易代碼
	/* A:新增C:異動D:刪除 */
	@Column(name = "`TranKey`", length = 1)
	private String tranKey;

	// 報送單位代號
	/* 三位文數字 */
	@Column(name = "`SubmitKey`", length = 3, insertable = false, updatable = false)
	private String submitKey;

	// 債務人IDN
	@Column(name = "`CustId`", length = 10, insertable = false, updatable = false)
	private String custId;

	// 協商申請日
	@Column(name = "`RcDate`", insertable = false, updatable = false)
	private int rcDate = 0;

	// 期數
	@Column(name = "`Period`")
	private int period = 0;

	// 利率
	/* XX.XX */
	@Column(name = "`Rate`")
	private BigDecimal rate = new BigDecimal("0");

	// 依民法第323條計算之信用貸款債務總金額
	/* 目前只輸入9碼 */
	@Column(name = "`Civil323ExpAmt`")
	private int civil323ExpAmt = 0;

	// 信用貸款債務簽約總金額
	/* 目前只輸入9碼 */
	@Column(name = "`ExpLoanAmt`")
	private int expLoanAmt = 0;

	// 依民法第323條計算之現金卡債務總金額
	/* 目前只輸入9碼 */
	@Column(name = "`Civil323CashAmt`")
	private int civil323CashAmt = 0;

	// 現金卡債務簽約總金額
	/* 目前只輸入9碼 */
	@Column(name = "`CashCardAmt`")
	private int cashCardAmt = 0;

	// 依民法第323條計算之信用卡債務總金額
	/* 目前只輸入9碼 */
	@Column(name = "`Civil323CreditAmt`")
	private int civil323CreditAmt = 0;

	// 信用卡債務簽約總金額
	/* 目前只輸入9碼 */
	@Column(name = "`CreditCardAmt`")
	private int creditCardAmt = 0;

	// 依民法第323條計算之債務總金額
	/* 目前只輸入9碼 */
	@Column(name = "`Civil323Amt`")
	private BigDecimal civil323Amt = new BigDecimal("0");

	// 簽約總債務金額
	/* 目前只輸入9碼 */
	@Column(name = "`TotalAmt`")
	private BigDecimal totalAmt = new BigDecimal("0");

	// 協議完成日
	@Column(name = "`PassDate`")
	private int passDate = 0;

	// 面談日期
	@Column(name = "`InterviewDate`")
	private int interviewDate = 0;

	// 簽約完成日期
	@Column(name = "`SignDate`")
	private int signDate = 0;

	// 前置協商註記訊息揭露期限
	@Column(name = "`LimitDate`")
	private int limitDate = 0;

	// 首期應繳款日
	@Column(name = "`FirstPayDate`")
	private int firstPayDate = 0;

	// 月付金
	@Column(name = "`MonthPayAmt`")
	private int monthPayAmt = 0;

	// 繳款帳號
	@Column(name = "`PayAccount`", length = 20)
	private String payAccount;

	// 最大債權金融機構聲請狀送達地址
	/* 38個中文字全形 */
	@Column(name = "`PostAddr`", length = 38)
	private String postAddr;

	// 屬二階段還款方案之階段註記
	/* 1:屬二階段還款,第一階段2:屬二階段還款,第二階段單格空白:非屬二階段還款 */
	@Column(name = "`GradeType`", length = 1)
	private String gradeType;

	// 第一階段最後一期應繳金額
	/* 屬二階段還款方案之階段註記=1,則本欄位為必填 */
	@Column(name = "`PayLastAmt`")
	private int payLastAmt = 0;

	// 第二段期數
	@Column(name = "`Period2`")
	private int period2 = 0;

	// 第二階段利率
	@Column(name = "`Rate2`")
	private BigDecimal rate2 = new BigDecimal("0");

	// 第二階段協商方案估計月付金
	@Column(name = "`MonthPayAmt2`")
	private int monthPayAmt2 = 0;

	// 第二階段最後一期應繳金額
	@Column(name = "`PayLastAmt2`")
	private int payLastAmt2 = 0;

	// 轉出JCIC文字檔日期
	@Column(name = "`OutJcicTxtDate`")
	private int outJcicTxtDate = 0;

	// 流水號
	@Column(name = "`Ukey`", length = 32)
	private String ukey;

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

	// 實際報送日期
	@Column(name = "`ActualFilingDate`")
	private int actualFilingDate = 0;

	// 實際報送記號
	@Column(name = "`ActualFilingMark`", length = 3)
	private String actualFilingMark;

	public JcicZ047Id getJcicZ047Id() {
		return this.jcicZ047Id;
	}

	public void setJcicZ047Id(JcicZ047Id jcicZ047Id) {
		this.jcicZ047Id = jcicZ047Id;
	}

	/**
	 * 交易代碼<br>
	 * A:新增 C:異動 D:刪除
	 * 
	 * @return String
	 */
	public String getTranKey() {
		return this.tranKey == null ? "" : this.tranKey;
	}

	/**
	 * 交易代碼<br>
	 * A:新增 C:異動 D:刪除
	 *
	 * @param tranKey 交易代碼
	 */
	public void setTranKey(String tranKey) {
		this.tranKey = tranKey;
	}

	/**
	 * 報送單位代號<br>
	 * 三位文數字
	 * 
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
	 * 協商申請日<br>
	 * 
	 * @return Integer
	 */
	public int getRcDate() {
		return StaticTool.bcToRoc(this.rcDate);
	}

	/**
	 * 協商申請日<br>
	 * 
	 *
	 * @param rcDate 協商申請日
	 * @throws LogicException when Date Is Warn
	 */
	public void setRcDate(int rcDate) throws LogicException {
		this.rcDate = StaticTool.rocToBc(rcDate);
	}

	/**
	 * 期數<br>
	 * 
	 * @return Integer
	 */
	public int getPeriod() {
		return this.period;
	}

	/**
	 * 期數<br>
	 * 
	 *
	 * @param period 期數
	 */
	public void setPeriod(int period) {
		this.period = period;
	}

	/**
	 * 利率<br>
	 * XX.XX
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getRate() {
		return this.rate;
	}

	/**
	 * 利率<br>
	 * XX.XX
	 *
	 * @param rate 利率
	 */
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	/**
	 * 依民法第323條計算之信用貸款債務總金額<br>
	 * 目前只輸入9碼
	 * 
	 * @return Integer
	 */
	public int getCivil323ExpAmt() {
		return this.civil323ExpAmt;
	}

	/**
	 * 依民法第323條計算之信用貸款債務總金額<br>
	 * 目前只輸入9碼
	 *
	 * @param civil323ExpAmt 依民法第323條計算之信用貸款債務總金額
	 */
	public void setCivil323ExpAmt(int civil323ExpAmt) {
		this.civil323ExpAmt = civil323ExpAmt;
	}

	/**
	 * 信用貸款債務簽約總金額<br>
	 * 目前只輸入9碼
	 * 
	 * @return Integer
	 */
	public int getExpLoanAmt() {
		return this.expLoanAmt;
	}

	/**
	 * 信用貸款債務簽約總金額<br>
	 * 目前只輸入9碼
	 *
	 * @param expLoanAmt 信用貸款債務簽約總金額
	 */
	public void setExpLoanAmt(int expLoanAmt) {
		this.expLoanAmt = expLoanAmt;
	}

	/**
	 * 依民法第323條計算之現金卡債務總金額<br>
	 * 目前只輸入9碼
	 * 
	 * @return Integer
	 */
	public int getCivil323CashAmt() {
		return this.civil323CashAmt;
	}

	/**
	 * 依民法第323條計算之現金卡債務總金額<br>
	 * 目前只輸入9碼
	 *
	 * @param civil323CashAmt 依民法第323條計算之現金卡債務總金額
	 */
	public void setCivil323CashAmt(int civil323CashAmt) {
		this.civil323CashAmt = civil323CashAmt;
	}

	/**
	 * 現金卡債務簽約總金額<br>
	 * 目前只輸入9碼
	 * 
	 * @return Integer
	 */
	public int getCashCardAmt() {
		return this.cashCardAmt;
	}

	/**
	 * 現金卡債務簽約總金額<br>
	 * 目前只輸入9碼
	 *
	 * @param cashCardAmt 現金卡債務簽約總金額
	 */
	public void setCashCardAmt(int cashCardAmt) {
		this.cashCardAmt = cashCardAmt;
	}

	/**
	 * 依民法第323條計算之信用卡債務總金額<br>
	 * 目前只輸入9碼
	 * 
	 * @return Integer
	 */
	public int getCivil323CreditAmt() {
		return this.civil323CreditAmt;
	}

	/**
	 * 依民法第323條計算之信用卡債務總金額<br>
	 * 目前只輸入9碼
	 *
	 * @param civil323CreditAmt 依民法第323條計算之信用卡債務總金額
	 */
	public void setCivil323CreditAmt(int civil323CreditAmt) {
		this.civil323CreditAmt = civil323CreditAmt;
	}

	/**
	 * 信用卡債務簽約總金額<br>
	 * 目前只輸入9碼
	 * 
	 * @return Integer
	 */
	public int getCreditCardAmt() {
		return this.creditCardAmt;
	}

	/**
	 * 信用卡債務簽約總金額<br>
	 * 目前只輸入9碼
	 *
	 * @param creditCardAmt 信用卡債務簽約總金額
	 */
	public void setCreditCardAmt(int creditCardAmt) {
		this.creditCardAmt = creditCardAmt;
	}

	/**
	 * 依民法第323條計算之債務總金額<br>
	 * 目前只輸入9碼
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCivil323Amt() {
		return this.civil323Amt;
	}

	/**
	 * 依民法第323條計算之債務總金額<br>
	 * 目前只輸入9碼
	 *
	 * @param civil323Amt 依民法第323條計算之債務總金額
	 */
	public void setCivil323Amt(BigDecimal civil323Amt) {
		this.civil323Amt = civil323Amt;
	}

	/**
	 * 簽約總債務金額<br>
	 * 目前只輸入9碼
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getTotalAmt() {
		return this.totalAmt;
	}

	/**
	 * 簽約總債務金額<br>
	 * 目前只輸入9碼
	 *
	 * @param totalAmt 簽約總債務金額
	 */
	public void setTotalAmt(BigDecimal totalAmt) {
		this.totalAmt = totalAmt;
	}

	/**
	 * 協議完成日<br>
	 * 
	 * @return Integer
	 */
	public int getPassDate() {
		return StaticTool.bcToRoc(this.passDate);
	}

	/**
	 * 協議完成日<br>
	 * 
	 *
	 * @param passDate 協議完成日
	 * @throws LogicException when Date Is Warn
	 */
	public void setPassDate(int passDate) throws LogicException {
		this.passDate = StaticTool.rocToBc(passDate);
	}

	/**
	 * 面談日期<br>
	 * 
	 * @return Integer
	 */
	public int getInterviewDate() {
		return StaticTool.bcToRoc(this.interviewDate);
	}

	/**
	 * 面談日期<br>
	 * 
	 *
	 * @param interviewDate 面談日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setInterviewDate(int interviewDate) throws LogicException {
		this.interviewDate = StaticTool.rocToBc(interviewDate);
	}

	/**
	 * 簽約完成日期<br>
	 * 
	 * @return Integer
	 */
	public int getSignDate() {
		return StaticTool.bcToRoc(this.signDate);
	}

	/**
	 * 簽約完成日期<br>
	 * 
	 *
	 * @param signDate 簽約完成日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setSignDate(int signDate) throws LogicException {
		this.signDate = StaticTool.rocToBc(signDate);
	}

	/**
	 * 前置協商註記訊息揭露期限<br>
	 * 
	 * @return Integer
	 */
	public int getLimitDate() {
		return StaticTool.bcToRoc(this.limitDate);
	}

	/**
	 * 前置協商註記訊息揭露期限<br>
	 * 
	 *
	 * @param limitDate 前置協商註記訊息揭露期限
	 * @throws LogicException when Date Is Warn
	 */
	public void setLimitDate(int limitDate) throws LogicException {
		this.limitDate = StaticTool.rocToBc(limitDate);
	}

	/**
	 * 首期應繳款日<br>
	 * 
	 * @return Integer
	 */
	public int getFirstPayDate() {
		return StaticTool.bcToRoc(this.firstPayDate);
	}

	/**
	 * 首期應繳款日<br>
	 * 
	 *
	 * @param firstPayDate 首期應繳款日
	 * @throws LogicException when Date Is Warn
	 */
	public void setFirstPayDate(int firstPayDate) throws LogicException {
		this.firstPayDate = StaticTool.rocToBc(firstPayDate);
	}

	/**
	 * 月付金<br>
	 * 
	 * @return Integer
	 */
	public int getMonthPayAmt() {
		return this.monthPayAmt;
	}

	/**
	 * 月付金<br>
	 * 
	 *
	 * @param monthPayAmt 月付金
	 */
	public void setMonthPayAmt(int monthPayAmt) {
		this.monthPayAmt = monthPayAmt;
	}

	/**
	 * 繳款帳號<br>
	 * 
	 * @return String
	 */
	public String getPayAccount() {
		return this.payAccount == null ? "" : this.payAccount;
	}

	/**
	 * 繳款帳號<br>
	 * 
	 *
	 * @param payAccount 繳款帳號
	 */
	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount;
	}

	/**
	 * 最大債權金融機構聲請狀送達地址<br>
	 * 38個中文字全形
	 * 
	 * @return String
	 */
	public String getPostAddr() {
		return this.postAddr == null ? "" : this.postAddr;
	}

	/**
	 * 最大債權金融機構聲請狀送達地址<br>
	 * 38個中文字全形
	 *
	 * @param postAddr 最大債權金融機構聲請狀送達地址
	 */
	public void setPostAddr(String postAddr) {
		this.postAddr = postAddr;
	}

	/**
	 * 屬二階段還款方案之階段註記<br>
	 * 1:屬二階段還款,第一階段 2:屬二階段還款,第二階段 單格空白:非屬二階段還款
	 * 
	 * @return String
	 */
	public String getGradeType() {
		return this.gradeType == null ? "" : this.gradeType;
	}

	/**
	 * 屬二階段還款方案之階段註記<br>
	 * 1:屬二階段還款,第一階段 2:屬二階段還款,第二階段 單格空白:非屬二階段還款
	 *
	 * @param gradeType 屬二階段還款方案之階段註記
	 */
	public void setGradeType(String gradeType) {
		this.gradeType = gradeType;
	}

	/**
	 * 第一階段最後一期應繳金額<br>
	 * 屬二階段還款方案之階段註記=1,則本欄位為必填
	 * 
	 * @return Integer
	 */
	public int getPayLastAmt() {
		return this.payLastAmt;
	}

	/**
	 * 第一階段最後一期應繳金額<br>
	 * 屬二階段還款方案之階段註記=1,則本欄位為必填
	 *
	 * @param payLastAmt 第一階段最後一期應繳金額
	 */
	public void setPayLastAmt(int payLastAmt) {
		this.payLastAmt = payLastAmt;
	}

	/**
	 * 第二段期數<br>
	 * 
	 * @return Integer
	 */
	public int getPeriod2() {
		return this.period2;
	}

	/**
	 * 第二段期數<br>
	 * 
	 *
	 * @param period2 第二段期數
	 */
	public void setPeriod2(int period2) {
		this.period2 = period2;
	}

	/**
	 * 第二階段利率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getRate2() {
		return this.rate2;
	}

	/**
	 * 第二階段利率<br>
	 * 
	 *
	 * @param rate2 第二階段利率
	 */
	public void setRate2(BigDecimal rate2) {
		this.rate2 = rate2;
	}

	/**
	 * 第二階段協商方案估計月付金<br>
	 * 
	 * @return Integer
	 */
	public int getMonthPayAmt2() {
		return this.monthPayAmt2;
	}

	/**
	 * 第二階段協商方案估計月付金<br>
	 * 
	 *
	 * @param monthPayAmt2 第二階段協商方案估計月付金
	 */
	public void setMonthPayAmt2(int monthPayAmt2) {
		this.monthPayAmt2 = monthPayAmt2;
	}

	/**
	 * 第二階段最後一期應繳金額<br>
	 * 
	 * @return Integer
	 */
	public int getPayLastAmt2() {
		return this.payLastAmt2;
	}

	/**
	 * 第二階段最後一期應繳金額<br>
	 * 
	 *
	 * @param payLastAmt2 第二階段最後一期應繳金額
	 */
	public void setPayLastAmt2(int payLastAmt2) {
		this.payLastAmt2 = payLastAmt2;
	}

	/**
	 * 轉出JCIC文字檔日期<br>
	 * 
	 * @return Integer
	 */
	public int getOutJcicTxtDate() {
		return StaticTool.bcToRoc(this.outJcicTxtDate);
	}

	/**
	 * 轉出JCIC文字檔日期<br>
	 * 
	 *
	 * @param outJcicTxtDate 轉出JCIC文字檔日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setOutJcicTxtDate(int outJcicTxtDate) throws LogicException {
		this.outJcicTxtDate = StaticTool.rocToBc(outJcicTxtDate);
	}

	/**
	 * 流水號<br>
	 * 
	 * @return String
	 */
	public String getUkey() {
		return this.ukey == null ? "" : this.ukey;
	}

	/**
	 * 流水號<br>
	 * 
	 *
	 * @param ukey 流水號
	 */
	public void setUkey(String ukey) {
		this.ukey = ukey;
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
	 * 實際報送日期<br>
	 * 
	 * @return Integer
	 */
	public int getActualFilingDate() {
		return StaticTool.bcToRoc(this.actualFilingDate);
	}

	/**
	 * 實際報送日期<br>
	 * 
	 *
	 * @param actualFilingDate 實際報送日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setActualFilingDate(int actualFilingDate) throws LogicException {
		this.actualFilingDate = StaticTool.rocToBc(actualFilingDate);
	}

	/**
	 * 實際報送記號<br>
	 * 
	 * @return String
	 */
	public String getActualFilingMark() {
		return this.actualFilingMark == null ? "" : this.actualFilingMark;
	}

	/**
	 * 實際報送記號<br>
	 * 
	 *
	 * @param actualFilingMark 實際報送記號
	 */
	public void setActualFilingMark(String actualFilingMark) {
		this.actualFilingMark = actualFilingMark;
	}

	@Override
	public String toString() {
		return "JcicZ047 [jcicZ047Id=" + jcicZ047Id + ", tranKey=" + tranKey + ", period=" + period + ", rate=" + rate + ", civil323ExpAmt=" + civil323ExpAmt + ", expLoanAmt=" + expLoanAmt
				+ ", civil323CashAmt=" + civil323CashAmt + ", cashCardAmt=" + cashCardAmt + ", civil323CreditAmt=" + civil323CreditAmt + ", creditCardAmt=" + creditCardAmt + ", civil323Amt="
				+ civil323Amt + ", totalAmt=" + totalAmt + ", passDate=" + passDate + ", interviewDate=" + interviewDate + ", signDate=" + signDate + ", limitDate=" + limitDate + ", firstPayDate="
				+ firstPayDate + ", monthPayAmt=" + monthPayAmt + ", payAccount=" + payAccount + ", postAddr=" + postAddr + ", gradeType=" + gradeType + ", payLastAmt=" + payLastAmt + ", period2="
				+ period2 + ", rate2=" + rate2 + ", monthPayAmt2=" + monthPayAmt2 + ", payLastAmt2=" + payLastAmt2 + ", outJcicTxtDate=" + outJcicTxtDate + ", ukey=" + ukey + ", createDate="
				+ createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + ", actualFilingDate=" + actualFilingDate + ", actualFilingMark="
				+ actualFilingMark + "]";
	}
}
