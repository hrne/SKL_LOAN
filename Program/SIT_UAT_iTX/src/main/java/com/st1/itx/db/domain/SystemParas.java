package com.st1.itx.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.Id;
import javax.persistence.Column;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * SystemParas 系統參數設定檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`SystemParas`")
public class SystemParas implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5916746765955218455L;

// 業務類型
	/* LN */
	@Id
	@Column(name = "`BusinessType`", length = 2)
	private String businessType = " ";

	// 違約寬限天數(營業日)
	/* L6501維護預設值:5原系統為:6? */
	@Column(name = "`GraceDays`")
	private int graceDays = 0;

	// ACH授權提出一日一批
	/* L6501維護Y:一日一批N:一日多批預設值:Y */
	@Column(name = "`AchAuthOneTime`", length = 1)
	private String achAuthOneTime;

	// ACH扣款方式
	/* L6501維護1:特定日2:連續日 */
	@Column(name = "`AchDeductFlag`")
	private int achDeductFlag = 0;

	// ACH扣款特定日1
	/* L6501維護預設值:1 */
	@Column(name = "`AchDeductDD1`")
	private int achDeductDD1 = 0;

	// ACH扣款特定日2
	/* L6501維護預設值:10 */
	@Column(name = "`AchDeductDD2`")
	private int achDeductDD2 = 0;

	// ACH扣款特定日3
	/* L6501維護預設值:20 */
	@Column(name = "`AchDeductDD3`")
	private int achDeductDD3 = 0;

	// ACH扣款特定日4
	/* L6501維護預設值:0 */
	@Column(name = "`AchDeductDD4`")
	private int achDeductDD4 = 0;

	// ACH扣款特定日5
	/* L6501維護預設值:0 */
	@Column(name = "`AchDeductDD5`")
	private int achDeductDD5 = 0;

	// ACH特定日二扣營業日差
	/* L6501維護預設值:5 */
	@Column(name = "`AchSecondDeductDays`")
	private int achSecondDeductDays = 0;

	// ACH連續日扣款方式
	/* L6501維護1.連續扣款扣五個營業日(寬限期內)2.每次均重算至當日為止應繳期數之期金、延滯息&amp;違約金合計預設值:1 */
	@Column(name = "`AchDeductMethod`")
	private int achDeductMethod = 0;

	// POST扣款方式
	/* L6501維護1:特定日2:連續日 */
	@Column(name = "`PostDeductFlag`")
	private int postDeductFlag = 0;

	// ACH扣款特定日1
	/* L6501維護預設值:1 */
	@Column(name = "`PostDeductDD1`")
	private int postDeductDD1 = 0;

	// ACH扣款特定日2
	/* L6501維護預設值:10 */
	@Column(name = "`PostDeductDD2`")
	private int postDeductDD2 = 0;

	// ACH扣款特定日3
	/* L6501維護預設值:20 */
	@Column(name = "`PostDeductDD3`")
	private int postDeductDD3 = 0;

	// ACH扣款特定日4
	/* L6501維護預設值:0 */
	@Column(name = "`PostDeductDD4`")
	private int postDeductDD4 = 0;

	// ACH扣款特定日5
	/* L6501維護預設值:0 */
	@Column(name = "`PostDeductDD5`")
	private int postDeductDD5 = 0;

	// ACH特定日二扣營業日差
	/* L6501維護預設值:5 */
	@Column(name = "`PostSecondDeductDays`")
	private int postSecondDeductDays = 0;

	// ACH連續日扣款方式
	/* L6501維護1.連續扣款扣五個營業日(寬限期內)2.每次均重算至當日為止應繳期數之期金、延滯息&amp;違約金合計預設值:1 */
	@Column(name = "`PostDeductMethod`")
	private int postDeductMethod = 0;

	// 放款部收款專戶戶號
	/* L6501維護預設值:0610940 */
	@Column(name = "`LoanDeptCustNo`")
	private int loanDeptCustNo = 0;

	// 前置協商收款專戶戶號
	/* L6501維護預設值:0601776 */
	@Column(name = "`NegDeptCustNo`")
	private int negDeptCustNo = 0;

	// 業績追回之部分還款金額條件
	/* L6501維護追回繳納1期但未繳足3期期款即結清（含部分還款達60萬之案件)，未曾繳款者則同時追回房貸專員業績預設值:600,000 */
	@Column(name = "`PerfBackRepayAmt`")
	private BigDecimal perfBackRepayAmt = new BigDecimal("0");

	// 業績追回之起期數
	/* L6501維護預設值:1 追回[繳納1期]但[未繳足3期]期款即結清（含部分還款達60萬之案件)，未曾繳款者則同時追回房貸專員業績 */
	@Column(name = "`PerfBackPeriodS`")
	private int perfBackPeriodS = 0;

	// 業績追回之止期數
	/* L6501維護預設值:3 追回[繳納1期]但[未繳足3期]期款即結清（含部分還款達60萬之案件)，未曾繳款者則同時追回房貸專員業績 */
	@Column(name = "`PerfBackPeriodE`")
	private int perfBackPeriodE = 0;

	// 業績追回通知員工代碼清單
	/* L6501維護 add 2021/12/6 by 智誠預設空白 */
	@Column(name = "`EmpNoList`", length = 150)
	private String empNoList;

	// 短期擔保放款年限之起
	/* L6501維護預設值:0 */
	@Column(name = "`AcctCode310A`")
	private int acctCode310A = 0;

	// 短期擔保放款年限之止
	/* L6501維護預設值:1 */
	@Column(name = "`AcctCode310B`")
	private int acctCode310B = 0;

	// 中期擔保放款年限之起
	/* L6501維護預設值:1 */
	@Column(name = "`AcctCode320A`")
	private int acctCode320A = 0;

	// 中期擔保放款年限之止
	/* L6501維護預設值:7 */
	@Column(name = "`AcctCode320B`")
	private int acctCode320B = 0;

	// 長期擔保放款年限之起
	/* L6501維護預設值:7 */
	@Column(name = "`AcctCode330A`")
	private int acctCode330A = 0;

	// 長期擔保放款年限之止
	/* L6501維護預設值:99 */
	@Column(name = "`AcctCode330B`")
	private int acctCode330B = 0;

	// 減免金額限額
	/* L6501維護預設值:200 */
	@Column(name = "`ReduceAmtLimit`")
	private int reduceAmtLimit = 0;

	// 單筆預收期數
	/* L6501維護預設值:0 */
	@Column(name = "`PreRepayTerms`")
	private int preRepayTerms = 0;

	// 批次預收期數
	@Column(name = "`PreRepayTermsBatch`")
	private int preRepayTermsBatch = 0;

	// 回收時可短繳本金金額之百分比
	@Column(name = "`ShortPrinPercent`")
	private int shortPrinPercent = 0;

	// 回收時可短繳利息金額之百分比
	@Column(name = "`ShortIntPercent`")
	private int shortIntPercent = 0;

	// AML檢查記號
	/* 0:系統自動檢核 1:需人工確認 2:不檢核(測試套用) */
	@Column(name = "`AmlFg`")
	private int amlFg = 0;

	// AML網址
	@Column(name = "`AmlUrl`", length = 50)
	private String amlUrl;

	// 業績日期
	/* 工作日業績結算時更新(排程15:30執行) */
	@Column(name = "`PerfDate`")
	private int perfDate = 0;

	// 帳冊別
	/* 000：全公司 */
	@Column(name = "`AcBookCode`", length = 3)
	private String acBookCode;

	// 區隔帳冊
	/* 00A：傳統帳冊 */
	@Column(name = "`AcSubBookCode`", length = 3)
	private String acSubBookCode;

	// 帳冊別帳務調整日期
	/* L6709帳冊別目標金額維護交易時更新 */
	@Column(name = "`AcBookAdjDate`")
	private int acBookAdjDate = 0;

	// EBS啟用記號
	/* Y:啟用,N:不啟用 */
	@Column(name = "`EbsFg`", length = 1)
	private String ebsFg;

	// EBS網址
	/* 傳票媒體檔傳遞至會計資訊系統(EBS),服務協定為RESTful */
	@Column(name = "`EbsUrl`", length = 100)
	private String ebsUrl;

	// EBS認證
	/* username:password */
	@Column(name = "`EbsAuth`", length = 100)
	private String ebsAuth;

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

	/**
	 * 業務類型<br>
	 * LN
	 * 
	 * @return String
	 */
	public String getBusinessType() {
		return this.businessType == null ? "" : this.businessType;
	}

	/**
	 * 業務類型<br>
	 * LN
	 *
	 * @param businessType 業務類型
	 */
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	/**
	 * 違約寬限天數(營業日)<br>
	 * L6501維護 預設值:5 原系統為:6?
	 * 
	 * @return Integer
	 */
	public int getGraceDays() {
		return this.graceDays;
	}

	/**
	 * 違約寬限天數(營業日)<br>
	 * L6501維護 預設值:5 原系統為:6?
	 *
	 * @param graceDays 違約寬限天數(營業日)
	 */
	public void setGraceDays(int graceDays) {
		this.graceDays = graceDays;
	}

	/**
	 * ACH授權提出一日一批<br>
	 * L6501維護 Y:一日一批 N:一日多批 預設值:Y
	 * 
	 * @return String
	 */
	public String getAchAuthOneTime() {
		return this.achAuthOneTime == null ? "" : this.achAuthOneTime;
	}

	/**
	 * ACH授權提出一日一批<br>
	 * L6501維護 Y:一日一批 N:一日多批 預設值:Y
	 *
	 * @param achAuthOneTime ACH授權提出一日一批
	 */
	public void setAchAuthOneTime(String achAuthOneTime) {
		this.achAuthOneTime = achAuthOneTime;
	}

	/**
	 * ACH扣款方式<br>
	 * L6501維護 1:特定日 2:連續日
	 * 
	 * @return Integer
	 */
	public int getAchDeductFlag() {
		return this.achDeductFlag;
	}

	/**
	 * ACH扣款方式<br>
	 * L6501維護 1:特定日 2:連續日
	 *
	 * @param achDeductFlag ACH扣款方式
	 */
	public void setAchDeductFlag(int achDeductFlag) {
		this.achDeductFlag = achDeductFlag;
	}

	/**
	 * ACH扣款特定日1<br>
	 * L6501維護 預設值:1
	 * 
	 * @return Integer
	 */
	public int getAchDeductDD1() {
		return this.achDeductDD1;
	}

	/**
	 * ACH扣款特定日1<br>
	 * L6501維護 預設值:1
	 *
	 * @param achDeductDD1 ACH扣款特定日1
	 */
	public void setAchDeductDD1(int achDeductDD1) {
		this.achDeductDD1 = achDeductDD1;
	}

	/**
	 * ACH扣款特定日2<br>
	 * L6501維護 預設值:10
	 * 
	 * @return Integer
	 */
	public int getAchDeductDD2() {
		return this.achDeductDD2;
	}

	/**
	 * ACH扣款特定日2<br>
	 * L6501維護 預設值:10
	 *
	 * @param achDeductDD2 ACH扣款特定日2
	 */
	public void setAchDeductDD2(int achDeductDD2) {
		this.achDeductDD2 = achDeductDD2;
	}

	/**
	 * ACH扣款特定日3<br>
	 * L6501維護 預設值:20
	 * 
	 * @return Integer
	 */
	public int getAchDeductDD3() {
		return this.achDeductDD3;
	}

	/**
	 * ACH扣款特定日3<br>
	 * L6501維護 預設值:20
	 *
	 * @param achDeductDD3 ACH扣款特定日3
	 */
	public void setAchDeductDD3(int achDeductDD3) {
		this.achDeductDD3 = achDeductDD3;
	}

	/**
	 * ACH扣款特定日4<br>
	 * L6501維護 預設值:0
	 * 
	 * @return Integer
	 */
	public int getAchDeductDD4() {
		return this.achDeductDD4;
	}

	/**
	 * ACH扣款特定日4<br>
	 * L6501維護 預設值:0
	 *
	 * @param achDeductDD4 ACH扣款特定日4
	 */
	public void setAchDeductDD4(int achDeductDD4) {
		this.achDeductDD4 = achDeductDD4;
	}

	/**
	 * ACH扣款特定日5<br>
	 * L6501維護 預設值:0
	 * 
	 * @return Integer
	 */
	public int getAchDeductDD5() {
		return this.achDeductDD5;
	}

	/**
	 * ACH扣款特定日5<br>
	 * L6501維護 預設值:0
	 *
	 * @param achDeductDD5 ACH扣款特定日5
	 */
	public void setAchDeductDD5(int achDeductDD5) {
		this.achDeductDD5 = achDeductDD5;
	}

	/**
	 * ACH特定日二扣營業日差<br>
	 * L6501維護 預設值:5
	 * 
	 * @return Integer
	 */
	public int getAchSecondDeductDays() {
		return this.achSecondDeductDays;
	}

	/**
	 * ACH特定日二扣營業日差<br>
	 * L6501維護 預設值:5
	 *
	 * @param achSecondDeductDays ACH特定日二扣營業日差
	 */
	public void setAchSecondDeductDays(int achSecondDeductDays) {
		this.achSecondDeductDays = achSecondDeductDays;
	}

	/**
	 * ACH連續日扣款方式<br>
	 * L6501維護 1.連續扣款扣五個營業日(寬限期內) 2.每次均重算至當日為止應繳期數之期金、延滯息&amp;違約金合計 預設值:1
	 * 
	 * @return Integer
	 */
	public int getAchDeductMethod() {
		return this.achDeductMethod;
	}

	/**
	 * ACH連續日扣款方式<br>
	 * L6501維護 1.連續扣款扣五個營業日(寬限期內) 2.每次均重算至當日為止應繳期數之期金、延滯息&amp;違約金合計 預設值:1
	 *
	 * @param achDeductMethod ACH連續日扣款方式
	 */
	public void setAchDeductMethod(int achDeductMethod) {
		this.achDeductMethod = achDeductMethod;
	}

	/**
	 * POST扣款方式<br>
	 * L6501維護 1:特定日 2:連續日
	 * 
	 * @return Integer
	 */
	public int getPostDeductFlag() {
		return this.postDeductFlag;
	}

	/**
	 * POST扣款方式<br>
	 * L6501維護 1:特定日 2:連續日
	 *
	 * @param postDeductFlag POST扣款方式
	 */
	public void setPostDeductFlag(int postDeductFlag) {
		this.postDeductFlag = postDeductFlag;
	}

	/**
	 * ACH扣款特定日1<br>
	 * L6501維護 預設值:1
	 * 
	 * @return Integer
	 */
	public int getPostDeductDD1() {
		return this.postDeductDD1;
	}

	/**
	 * ACH扣款特定日1<br>
	 * L6501維護 預設值:1
	 *
	 * @param postDeductDD1 ACH扣款特定日1
	 */
	public void setPostDeductDD1(int postDeductDD1) {
		this.postDeductDD1 = postDeductDD1;
	}

	/**
	 * ACH扣款特定日2<br>
	 * L6501維護 預設值:10
	 * 
	 * @return Integer
	 */
	public int getPostDeductDD2() {
		return this.postDeductDD2;
	}

	/**
	 * ACH扣款特定日2<br>
	 * L6501維護 預設值:10
	 *
	 * @param postDeductDD2 ACH扣款特定日2
	 */
	public void setPostDeductDD2(int postDeductDD2) {
		this.postDeductDD2 = postDeductDD2;
	}

	/**
	 * ACH扣款特定日3<br>
	 * L6501維護 預設值:20
	 * 
	 * @return Integer
	 */
	public int getPostDeductDD3() {
		return this.postDeductDD3;
	}

	/**
	 * ACH扣款特定日3<br>
	 * L6501維護 預設值:20
	 *
	 * @param postDeductDD3 ACH扣款特定日3
	 */
	public void setPostDeductDD3(int postDeductDD3) {
		this.postDeductDD3 = postDeductDD3;
	}

	/**
	 * ACH扣款特定日4<br>
	 * L6501維護 預設值:0
	 * 
	 * @return Integer
	 */
	public int getPostDeductDD4() {
		return this.postDeductDD4;
	}

	/**
	 * ACH扣款特定日4<br>
	 * L6501維護 預設值:0
	 *
	 * @param postDeductDD4 ACH扣款特定日4
	 */
	public void setPostDeductDD4(int postDeductDD4) {
		this.postDeductDD4 = postDeductDD4;
	}

	/**
	 * ACH扣款特定日5<br>
	 * L6501維護 預設值:0
	 * 
	 * @return Integer
	 */
	public int getPostDeductDD5() {
		return this.postDeductDD5;
	}

	/**
	 * ACH扣款特定日5<br>
	 * L6501維護 預設值:0
	 *
	 * @param postDeductDD5 ACH扣款特定日5
	 */
	public void setPostDeductDD5(int postDeductDD5) {
		this.postDeductDD5 = postDeductDD5;
	}

	/**
	 * ACH特定日二扣營業日差<br>
	 * L6501維護 預設值:5
	 * 
	 * @return Integer
	 */
	public int getPostSecondDeductDays() {
		return this.postSecondDeductDays;
	}

	/**
	 * ACH特定日二扣營業日差<br>
	 * L6501維護 預設值:5
	 *
	 * @param postSecondDeductDays ACH特定日二扣營業日差
	 */
	public void setPostSecondDeductDays(int postSecondDeductDays) {
		this.postSecondDeductDays = postSecondDeductDays;
	}

	/**
	 * ACH連續日扣款方式<br>
	 * L6501維護 1.連續扣款扣五個營業日(寬限期內) 2.每次均重算至當日為止應繳期數之期金、延滯息&amp;違約金合計 預設值:1
	 * 
	 * @return Integer
	 */
	public int getPostDeductMethod() {
		return this.postDeductMethod;
	}

	/**
	 * ACH連續日扣款方式<br>
	 * L6501維護 1.連續扣款扣五個營業日(寬限期內) 2.每次均重算至當日為止應繳期數之期金、延滯息&amp;違約金合計 預設值:1
	 *
	 * @param postDeductMethod ACH連續日扣款方式
	 */
	public void setPostDeductMethod(int postDeductMethod) {
		this.postDeductMethod = postDeductMethod;
	}

	/**
	 * 放款部收款專戶戶號<br>
	 * L6501維護 預設值:0610940
	 * 
	 * @return Integer
	 */
	public int getLoanDeptCustNo() {
		return this.loanDeptCustNo;
	}

	/**
	 * 放款部收款專戶戶號<br>
	 * L6501維護 預設值:0610940
	 *
	 * @param loanDeptCustNo 放款部收款專戶戶號
	 */
	public void setLoanDeptCustNo(int loanDeptCustNo) {
		this.loanDeptCustNo = loanDeptCustNo;
	}

	/**
	 * 前置協商收款專戶戶號<br>
	 * L6501維護 預設值:0601776
	 * 
	 * @return Integer
	 */
	public int getNegDeptCustNo() {
		return this.negDeptCustNo;
	}

	/**
	 * 前置協商收款專戶戶號<br>
	 * L6501維護 預設值:0601776
	 *
	 * @param negDeptCustNo 前置協商收款專戶戶號
	 */
	public void setNegDeptCustNo(int negDeptCustNo) {
		this.negDeptCustNo = negDeptCustNo;
	}

	/**
	 * 業績追回之部分還款金額條件<br>
	 * L6501維護 追回繳納1期但未繳足3期期款即結清（含部分還款達60萬之案件)，未曾繳款者則同時追回房貸專員業績 預設值:600,000
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPerfBackRepayAmt() {
		return this.perfBackRepayAmt;
	}

	/**
	 * 業績追回之部分還款金額條件<br>
	 * L6501維護 追回繳納1期但未繳足3期期款即結清（含部分還款達60萬之案件)，未曾繳款者則同時追回房貸專員業績 預設值:600,000
	 *
	 * @param perfBackRepayAmt 業績追回之部分還款金額條件
	 */
	public void setPerfBackRepayAmt(BigDecimal perfBackRepayAmt) {
		this.perfBackRepayAmt = perfBackRepayAmt;
	}

	/**
	 * 業績追回之起期數<br>
	 * L6501維護 預設值:1 追回[繳納1期]但[未繳足3期]期款即結清（含部分還款達60萬之案件)，未曾繳款者則同時追回房貸專員業績
	 * 
	 * @return Integer
	 */
	public int getPerfBackPeriodS() {
		return this.perfBackPeriodS;
	}

	/**
	 * 業績追回之起期數<br>
	 * L6501維護 預設值:1 追回[繳納1期]但[未繳足3期]期款即結清（含部分還款達60萬之案件)，未曾繳款者則同時追回房貸專員業績
	 *
	 * @param perfBackPeriodS 業績追回之起期數
	 */
	public void setPerfBackPeriodS(int perfBackPeriodS) {
		this.perfBackPeriodS = perfBackPeriodS;
	}

	/**
	 * 業績追回之止期數<br>
	 * L6501維護 預設值:3 追回[繳納1期]但[未繳足3期]期款即結清（含部分還款達60萬之案件)，未曾繳款者則同時追回房貸專員業績
	 * 
	 * @return Integer
	 */
	public int getPerfBackPeriodE() {
		return this.perfBackPeriodE;
	}

	/**
	 * 業績追回之止期數<br>
	 * L6501維護 預設值:3 追回[繳納1期]但[未繳足3期]期款即結清（含部分還款達60萬之案件)，未曾繳款者則同時追回房貸專員業績
	 *
	 * @param perfBackPeriodE 業績追回之止期數
	 */
	public void setPerfBackPeriodE(int perfBackPeriodE) {
		this.perfBackPeriodE = perfBackPeriodE;
	}

	/**
	 * 業績追回通知員工代碼清單<br>
	 * L6501維護 add 2021/12/6 by 智誠 預設空白
	 * 
	 * @return String
	 */
	public String getEmpNoList() {
		return this.empNoList == null ? "" : this.empNoList;
	}

	/**
	 * 業績追回通知員工代碼清單<br>
	 * L6501維護 add 2021/12/6 by 智誠 預設空白
	 *
	 * @param empNoList 業績追回通知員工代碼清單
	 */
	public void setEmpNoList(String empNoList) {
		this.empNoList = empNoList;
	}

	/**
	 * 短期擔保放款年限之起<br>
	 * L6501維護 預設值:0
	 * 
	 * @return Integer
	 */
	public int getAcctCode310A() {
		return this.acctCode310A;
	}

	/**
	 * 短期擔保放款年限之起<br>
	 * L6501維護 預設值:0
	 *
	 * @param acctCode310A 短期擔保放款年限之起
	 */
	public void setAcctCode310A(int acctCode310A) {
		this.acctCode310A = acctCode310A;
	}

	/**
	 * 短期擔保放款年限之止<br>
	 * L6501維護 預設值:1
	 * 
	 * @return Integer
	 */
	public int getAcctCode310B() {
		return this.acctCode310B;
	}

	/**
	 * 短期擔保放款年限之止<br>
	 * L6501維護 預設值:1
	 *
	 * @param acctCode310B 短期擔保放款年限之止
	 */
	public void setAcctCode310B(int acctCode310B) {
		this.acctCode310B = acctCode310B;
	}

	/**
	 * 中期擔保放款年限之起<br>
	 * L6501維護 預設值:1
	 * 
	 * @return Integer
	 */
	public int getAcctCode320A() {
		return this.acctCode320A;
	}

	/**
	 * 中期擔保放款年限之起<br>
	 * L6501維護 預設值:1
	 *
	 * @param acctCode320A 中期擔保放款年限之起
	 */
	public void setAcctCode320A(int acctCode320A) {
		this.acctCode320A = acctCode320A;
	}

	/**
	 * 中期擔保放款年限之止<br>
	 * L6501維護 預設值:7
	 * 
	 * @return Integer
	 */
	public int getAcctCode320B() {
		return this.acctCode320B;
	}

	/**
	 * 中期擔保放款年限之止<br>
	 * L6501維護 預設值:7
	 *
	 * @param acctCode320B 中期擔保放款年限之止
	 */
	public void setAcctCode320B(int acctCode320B) {
		this.acctCode320B = acctCode320B;
	}

	/**
	 * 長期擔保放款年限之起<br>
	 * L6501維護 預設值:7
	 * 
	 * @return Integer
	 */
	public int getAcctCode330A() {
		return this.acctCode330A;
	}

	/**
	 * 長期擔保放款年限之起<br>
	 * L6501維護 預設值:7
	 *
	 * @param acctCode330A 長期擔保放款年限之起
	 */
	public void setAcctCode330A(int acctCode330A) {
		this.acctCode330A = acctCode330A;
	}

	/**
	 * 長期擔保放款年限之止<br>
	 * L6501維護 預設值:99
	 * 
	 * @return Integer
	 */
	public int getAcctCode330B() {
		return this.acctCode330B;
	}

	/**
	 * 長期擔保放款年限之止<br>
	 * L6501維護 預設值:99
	 *
	 * @param acctCode330B 長期擔保放款年限之止
	 */
	public void setAcctCode330B(int acctCode330B) {
		this.acctCode330B = acctCode330B;
	}

	/**
	 * 減免金額限額<br>
	 * L6501維護 預設值:200
	 * 
	 * @return Integer
	 */
	public int getReduceAmtLimit() {
		return this.reduceAmtLimit;
	}

	/**
	 * 減免金額限額<br>
	 * L6501維護 預設值:200
	 *
	 * @param reduceAmtLimit 減免金額限額
	 */
	public void setReduceAmtLimit(int reduceAmtLimit) {
		this.reduceAmtLimit = reduceAmtLimit;
	}

	/**
	 * 單筆預收期數<br>
	 * L6501維護 預設值:0
	 * 
	 * @return Integer
	 */
	public int getPreRepayTerms() {
		return this.preRepayTerms;
	}

	/**
	 * 單筆預收期數<br>
	 * L6501維護 預設值:0
	 *
	 * @param preRepayTerms 單筆預收期數
	 */
	public void setPreRepayTerms(int preRepayTerms) {
		this.preRepayTerms = preRepayTerms;
	}

	/**
	 * 批次預收期數<br>
	 * 
	 * @return Integer
	 */
	public int getPreRepayTermsBatch() {
		return this.preRepayTermsBatch;
	}

	/**
	 * 批次預收期數<br>
	 * 
	 *
	 * @param preRepayTermsBatch 批次預收期數
	 */
	public void setPreRepayTermsBatch(int preRepayTermsBatch) {
		this.preRepayTermsBatch = preRepayTermsBatch;
	}

	/**
	 * 回收時可短繳本金金額之百分比<br>
	 * 
	 * @return Integer
	 */
	public int getShortPrinPercent() {
		return this.shortPrinPercent;
	}

	/**
	 * 回收時可短繳本金金額之百分比<br>
	 * 
	 *
	 * @param shortPrinPercent 回收時可短繳本金金額之百分比
	 */
	public void setShortPrinPercent(int shortPrinPercent) {
		this.shortPrinPercent = shortPrinPercent;
	}

	/**
	 * 回收時可短繳利息金額之百分比<br>
	 * 
	 * @return Integer
	 */
	public int getShortIntPercent() {
		return this.shortIntPercent;
	}

	/**
	 * 回收時可短繳利息金額之百分比<br>
	 * 
	 *
	 * @param shortIntPercent 回收時可短繳利息金額之百分比
	 */
	public void setShortIntPercent(int shortIntPercent) {
		this.shortIntPercent = shortIntPercent;
	}

	/**
	 * AML檢查記號<br>
	 * 0:系統自動檢核 1:需人工確認 2:不檢核(測試套用)
	 * 
	 * @return Integer
	 */
	public int getAmlFg() {
		return this.amlFg;
	}

	/**
	 * AML檢查記號<br>
	 * 0:系統自動檢核 1:需人工確認 2:不檢核(測試套用)
	 *
	 * @param amlFg AML檢查記號
	 */
	public void setAmlFg(int amlFg) {
		this.amlFg = amlFg;
	}

	/**
	 * AML網址<br>
	 * 
	 * @return String
	 */
	public String getAmlUrl() {
		return this.amlUrl == null ? "" : this.amlUrl;
	}

	/**
	 * AML網址<br>
	 * 
	 *
	 * @param amlUrl AML網址
	 */
	public void setAmlUrl(String amlUrl) {
		this.amlUrl = amlUrl;
	}

	/**
	 * 業績日期<br>
	 * 工作日業績結算時更新(排程15:30執行)
	 * 
	 * @return Integer
	 */
	public int getPerfDate() {
		return StaticTool.bcToRoc(this.perfDate);
	}

	/**
	 * 業績日期<br>
	 * 工作日業績結算時更新(排程15:30執行)
	 *
	 * @param perfDate 業績日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setPerfDate(int perfDate) throws LogicException {
		this.perfDate = StaticTool.rocToBc(perfDate);
	}

	/**
	 * 帳冊別<br>
	 * 000：全公司
	 * 
	 * @return String
	 */
	public String getAcBookCode() {
		return this.acBookCode == null ? "" : this.acBookCode;
	}

	/**
	 * 帳冊別<br>
	 * 000：全公司
	 *
	 * @param acBookCode 帳冊別
	 */
	public void setAcBookCode(String acBookCode) {
		this.acBookCode = acBookCode;
	}

	/**
	 * 區隔帳冊<br>
	 * 00A：傳統帳冊
	 * 
	 * @return String
	 */
	public String getAcSubBookCode() {
		return this.acSubBookCode == null ? "" : this.acSubBookCode;
	}

	/**
	 * 區隔帳冊<br>
	 * 00A：傳統帳冊
	 *
	 * @param acSubBookCode 區隔帳冊
	 */
	public void setAcSubBookCode(String acSubBookCode) {
		this.acSubBookCode = acSubBookCode;
	}

	/**
	 * 帳冊別帳務調整日期<br>
	 * L6709帳冊別目標金額維護交易時更新
	 * 
	 * @return Integer
	 */
	public int getAcBookAdjDate() {
		return StaticTool.bcToRoc(this.acBookAdjDate);
	}

	/**
	 * 帳冊別帳務調整日期<br>
	 * L6709帳冊別目標金額維護交易時更新
	 *
	 * @param acBookAdjDate 帳冊別帳務調整日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setAcBookAdjDate(int acBookAdjDate) throws LogicException {
		this.acBookAdjDate = StaticTool.rocToBc(acBookAdjDate);
	}

	/**
	 * EBS啟用記號<br>
	 * Y:啟用,N:不啟用
	 * 
	 * @return String
	 */
	public String getEbsFg() {
		return this.ebsFg == null ? "" : this.ebsFg;
	}

	/**
	 * EBS啟用記號<br>
	 * Y:啟用,N:不啟用
	 *
	 * @param ebsFg EBS啟用記號
	 */
	public void setEbsFg(String ebsFg) {
		this.ebsFg = ebsFg;
	}

	/**
	 * EBS網址<br>
	 * 傳票媒體檔傳遞至會計資訊系統(EBS),服務協定為RESTful
	 * 
	 * @return String
	 */
	public String getEbsUrl() {
		return this.ebsUrl == null ? "" : this.ebsUrl;
	}

	/**
	 * EBS網址<br>
	 * 傳票媒體檔傳遞至會計資訊系統(EBS),服務協定為RESTful
	 *
	 * @param ebsUrl EBS網址
	 */
	public void setEbsUrl(String ebsUrl) {
		this.ebsUrl = ebsUrl;
	}

	/**
	 * EBS認證<br>
	 * username:password
	 * 
	 * @return String
	 */
	public String getEbsAuth() {
		return this.ebsAuth == null ? "" : this.ebsAuth;
	}

	/**
	 * EBS認證<br>
	 * username:password
	 *
	 * @param ebsAuth EBS認證
	 */
	public void setEbsAuth(String ebsAuth) {
		this.ebsAuth = ebsAuth;
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
		return "SystemParas [businessType=" + businessType + ", graceDays=" + graceDays + ", achAuthOneTime=" + achAuthOneTime + ", achDeductFlag=" + achDeductFlag + ", achDeductDD1=" + achDeductDD1
				+ ", achDeductDD2=" + achDeductDD2 + ", achDeductDD3=" + achDeductDD3 + ", achDeductDD4=" + achDeductDD4 + ", achDeductDD5=" + achDeductDD5 + ", achSecondDeductDays="
				+ achSecondDeductDays + ", achDeductMethod=" + achDeductMethod + ", postDeductFlag=" + postDeductFlag + ", postDeductDD1=" + postDeductDD1 + ", postDeductDD2=" + postDeductDD2
				+ ", postDeductDD3=" + postDeductDD3 + ", postDeductDD4=" + postDeductDD4 + ", postDeductDD5=" + postDeductDD5 + ", postSecondDeductDays=" + postSecondDeductDays
				+ ", postDeductMethod=" + postDeductMethod + ", loanDeptCustNo=" + loanDeptCustNo + ", negDeptCustNo=" + negDeptCustNo + ", perfBackRepayAmt=" + perfBackRepayAmt + ", perfBackPeriodS="
				+ perfBackPeriodS + ", perfBackPeriodE=" + perfBackPeriodE + ", empNoList=" + empNoList + ", acctCode310A=" + acctCode310A + ", acctCode310B=" + acctCode310B + ", acctCode320A="
				+ acctCode320A + ", acctCode320B=" + acctCode320B + ", acctCode330A=" + acctCode330A + ", acctCode330B=" + acctCode330B + ", reduceAmtLimit=" + reduceAmtLimit + ", preRepayTerms="
				+ preRepayTerms + ", preRepayTermsBatch=" + preRepayTermsBatch + ", shortPrinPercent=" + shortPrinPercent + ", shortIntPercent=" + shortIntPercent + ", amlFg=" + amlFg + ", amlUrl="
				+ amlUrl + ", perfDate=" + perfDate + ", acBookCode=" + acBookCode + ", acSubBookCode=" + acSubBookCode + ", acBookAdjDate=" + acBookAdjDate + ", ebsFg=" + ebsFg + ", ebsUrl=" + ebsUrl
				+ ", ebsAuth=" + ebsAuth + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
