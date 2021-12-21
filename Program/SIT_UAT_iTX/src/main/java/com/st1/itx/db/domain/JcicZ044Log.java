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
 * JcicZ044Log 請求同意債務清償方案通知資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ044Log`")
public class JcicZ044Log implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6266425617476139727L;

	@EmbeddedId
	private JcicZ044LogId jcicZ044LogId;

	// 流水號
	@Column(name = "`Ukey`", length = 32, insertable = false, updatable = false)
	private String ukey;

	// 交易序號
	@Column(name = "`TxSeq`", length = 18, insertable = false, updatable = false)
	private String txSeq;

	// 交易代碼
	/* A:新增，C:異動，X:補件 */
	@Column(name = "`TranKey`", length = 1)
	private String tranKey;

	// 負債主因
	/*
	 * 11:投資或創業失敗12:因消費而積欠債務13:遭逢重大傷病或災變14:個人與家庭收入減少(含失業、減薪)15:收入穩定但支付超過能力可負擔之費用、
	 * 如昂貴教育、補習費或購置汽車、不動產16:收入穩定但因自宅貸款月付金提高，導致無法負荷17:被詐騙集團詐騙18:為他人作保證或遭他人倒帳
	 */
	@Column(name = "`DebtCode`", length = 2)
	private String debtCode;

	// 無擔保金融債務協商總金額
	@Column(name = "`NonGageAmt`")
	private int nonGageAmt = 0;

	// 期數
	@Column(name = "`Period`")
	private int period = 0;

	// 利率
	@Column(name = "`Rate`")
	private BigDecimal rate = new BigDecimal("0");

	// 協商方案估計月付金
	@Column(name = "`MonthPayAmt`")
	private int monthPayAmt = 0;

	// 最近年度綜合所得總額
	@Column(name = "`ReceYearIncome`")
	private int receYearIncome = 0;

	// 最近年度別
	@Column(name = "`ReceYear`")
	private int receYear = 0;

	// 前二年度綜合所得總額
	@Column(name = "`ReceYear2Income`")
	private int receYear2Income = 0;

	// 前二年度別
	@Column(name = "`ReceYear2`")
	private int receYear2 = 0;

	// 目前每月收入
	@Column(name = "`CurrentMonthIncome`")
	private int currentMonthIncome = 0;

	// 生活支出總額
	@Column(name = "`LivingCost`")
	private int livingCost = 0;

	// 目前主要所得來源公司名稱
	@Column(name = "`CompName`", length = 40)
	private String compName;

	// 目前主要所得公司統編
	@Column(name = "`CompId`", length = 8)
	private String compId;

	// 債務人名下車輛數量
	@Column(name = "`CarCnt`")
	private int carCnt = 0;

	// 債務人名下建物筆數
	@Column(name = "`HouseCnt`")
	private int houseCnt = 0;

	// 債務人名下土地筆數
	@Column(name = "`LandCnt`")
	private int landCnt = 0;

	// 撫養子女數
	@Column(name = "`ChildCnt`")
	private int childCnt = 0;

	// 撫養子女責任比率
	@Column(name = "`ChildRate`")
	private BigDecimal childRate = new BigDecimal("0");

	// 撫養父母人數
	@Column(name = "`ParentCnt`")
	private int parentCnt = 0;

	// 撫養父母責任比率
	@Column(name = "`ParentRate`")
	private BigDecimal parentRate = new BigDecimal("0");

	// 其他法定撫養人數
	@Column(name = "`MouthCnt`")
	private int mouthCnt = 0;

	// 其他法定撫養人之責任比率
	@Column(name = "`MouthRate`")
	private BigDecimal mouthRate = new BigDecimal("0");

	// 屬二階段還款方案之階段註記
	/* :非屬二階段還款1:屬二階段還款方案之第一階段2:屬二階段還款方案之第二階段 */
	@Column(name = "`GradeType`", length = 1)
	private String gradeType;

	// 第一階段最後一期應繳金額
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

	public JcicZ044LogId getJcicZ044LogId() {
		return this.jcicZ044LogId;
	}

	public void setJcicZ044LogId(JcicZ044LogId jcicZ044LogId) {
		this.jcicZ044LogId = jcicZ044LogId;
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
	 * 交易序號<br>
	 * 
	 * @return String
	 */
	public String getTxSeq() {
		return this.txSeq == null ? "" : this.txSeq;
	}

	/**
	 * 交易序號<br>
	 * 
	 *
	 * @param txSeq 交易序號
	 */
	public void setTxSeq(String txSeq) {
		this.txSeq = txSeq;
	}

	/**
	 * 交易代碼<br>
	 * A:新增，C:異動，X:補件
	 * 
	 * @return String
	 */
	public String getTranKey() {
		return this.tranKey == null ? "" : this.tranKey;
	}

	/**
	 * 交易代碼<br>
	 * A:新增，C:異動，X:補件
	 *
	 * @param tranKey 交易代碼
	 */
	public void setTranKey(String tranKey) {
		this.tranKey = tranKey;
	}

	/**
	 * 負債主因<br>
	 * 11:投資或創業失敗 12:因消費而積欠債務 13:遭逢重大傷病或災變 14:個人與家庭收入減少(含失業、減薪)
	 * 15:收入穩定但支付超過能力可負擔之費用、如昂貴教育、補習費或購置汽車、不動產 16:收入穩定但因自宅貸款月付金提高，導致無法負荷 17:被詐騙集團詐騙
	 * 18:為他人作保證或遭他人倒帳
	 * 
	 * @return String
	 */
	public String getDebtCode() {
		return this.debtCode == null ? "" : this.debtCode;
	}

	/**
	 * 負債主因<br>
	 * 11:投資或創業失敗 12:因消費而積欠債務 13:遭逢重大傷病或災變 14:個人與家庭收入減少(含失業、減薪)
	 * 15:收入穩定但支付超過能力可負擔之費用、如昂貴教育、補習費或購置汽車、不動產 16:收入穩定但因自宅貸款月付金提高，導致無法負荷 17:被詐騙集團詐騙
	 * 18:為他人作保證或遭他人倒帳
	 *
	 * @param debtCode 負債主因
	 */
	public void setDebtCode(String debtCode) {
		this.debtCode = debtCode;
	}

	/**
	 * 無擔保金融債務協商總金額<br>
	 * 
	 * @return Integer
	 */
	public int getNonGageAmt() {
		return this.nonGageAmt;
	}

	/**
	 * 無擔保金融債務協商總金額<br>
	 * 
	 *
	 * @param nonGageAmt 無擔保金融債務協商總金額
	 */
	public void setNonGageAmt(int nonGageAmt) {
		this.nonGageAmt = nonGageAmt;
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
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getRate() {
		return this.rate;
	}

	/**
	 * 利率<br>
	 * 
	 *
	 * @param rate 利率
	 */
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	/**
	 * 協商方案估計月付金<br>
	 * 
	 * @return Integer
	 */
	public int getMonthPayAmt() {
		return this.monthPayAmt;
	}

	/**
	 * 協商方案估計月付金<br>
	 * 
	 *
	 * @param monthPayAmt 協商方案估計月付金
	 */
	public void setMonthPayAmt(int monthPayAmt) {
		this.monthPayAmt = monthPayAmt;
	}

	/**
	 * 最近年度綜合所得總額<br>
	 * 
	 * @return Integer
	 */
	public int getReceYearIncome() {
		return this.receYearIncome;
	}

	/**
	 * 最近年度綜合所得總額<br>
	 * 
	 *
	 * @param receYearIncome 最近年度綜合所得總額
	 */
	public void setReceYearIncome(int receYearIncome) {
		this.receYearIncome = receYearIncome;
	}

	/**
	 * 最近年度別<br>
	 * 
	 * @return Integer
	 */
	public int getReceYear() {
		return this.receYear;
	}

	/**
	 * 最近年度別<br>
	 * 
	 *
	 * @param receYear 最近年度別
	 */
	public void setReceYear(int receYear) {
		this.receYear = receYear;
	}

	/**
	 * 前二年度綜合所得總額<br>
	 * 
	 * @return Integer
	 */
	public int getReceYear2Income() {
		return this.receYear2Income;
	}

	/**
	 * 前二年度綜合所得總額<br>
	 * 
	 *
	 * @param receYear2Income 前二年度綜合所得總額
	 */
	public void setReceYear2Income(int receYear2Income) {
		this.receYear2Income = receYear2Income;
	}

	/**
	 * 前二年度別<br>
	 * 
	 * @return Integer
	 */
	public int getReceYear2() {
		return this.receYear2;
	}

	/**
	 * 前二年度別<br>
	 * 
	 *
	 * @param receYear2 前二年度別
	 */
	public void setReceYear2(int receYear2) {
		this.receYear2 = receYear2;
	}

	/**
	 * 目前每月收入<br>
	 * 
	 * @return Integer
	 */
	public int getCurrentMonthIncome() {
		return this.currentMonthIncome;
	}

	/**
	 * 目前每月收入<br>
	 * 
	 *
	 * @param currentMonthIncome 目前每月收入
	 */
	public void setCurrentMonthIncome(int currentMonthIncome) {
		this.currentMonthIncome = currentMonthIncome;
	}

	/**
	 * 生活支出總額<br>
	 * 
	 * @return Integer
	 */
	public int getLivingCost() {
		return this.livingCost;
	}

	/**
	 * 生活支出總額<br>
	 * 
	 *
	 * @param livingCost 生活支出總額
	 */
	public void setLivingCost(int livingCost) {
		this.livingCost = livingCost;
	}

	/**
	 * 目前主要所得來源公司名稱<br>
	 * 
	 * @return String
	 */
	public String getCompName() {
		return this.compName == null ? "" : this.compName;
	}

	/**
	 * 目前主要所得來源公司名稱<br>
	 * 
	 *
	 * @param compName 目前主要所得來源公司名稱
	 */
	public void setCompName(String compName) {
		this.compName = compName;
	}

	/**
	 * 目前主要所得公司統編<br>
	 * 
	 * @return String
	 */
	public String getCompId() {
		return this.compId == null ? "" : this.compId;
	}

	/**
	 * 目前主要所得公司統編<br>
	 * 
	 *
	 * @param compId 目前主要所得公司統編
	 */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/**
	 * 債務人名下車輛數量<br>
	 * 
	 * @return Integer
	 */
	public int getCarCnt() {
		return this.carCnt;
	}

	/**
	 * 債務人名下車輛數量<br>
	 * 
	 *
	 * @param carCnt 債務人名下車輛數量
	 */
	public void setCarCnt(int carCnt) {
		this.carCnt = carCnt;
	}

	/**
	 * 債務人名下建物筆數<br>
	 * 
	 * @return Integer
	 */
	public int getHouseCnt() {
		return this.houseCnt;
	}

	/**
	 * 債務人名下建物筆數<br>
	 * 
	 *
	 * @param houseCnt 債務人名下建物筆數
	 */
	public void setHouseCnt(int houseCnt) {
		this.houseCnt = houseCnt;
	}

	/**
	 * 債務人名下土地筆數<br>
	 * 
	 * @return Integer
	 */
	public int getLandCnt() {
		return this.landCnt;
	}

	/**
	 * 債務人名下土地筆數<br>
	 * 
	 *
	 * @param landCnt 債務人名下土地筆數
	 */
	public void setLandCnt(int landCnt) {
		this.landCnt = landCnt;
	}

	/**
	 * 撫養子女數<br>
	 * 
	 * @return Integer
	 */
	public int getChildCnt() {
		return this.childCnt;
	}

	/**
	 * 撫養子女數<br>
	 * 
	 *
	 * @param childCnt 撫養子女數
	 */
	public void setChildCnt(int childCnt) {
		this.childCnt = childCnt;
	}

	/**
	 * 撫養子女責任比率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getChildRate() {
		return this.childRate;
	}

	/**
	 * 撫養子女責任比率<br>
	 * 
	 *
	 * @param childRate 撫養子女責任比率
	 */
	public void setChildRate(BigDecimal childRate) {
		this.childRate = childRate;
	}

	/**
	 * 撫養父母人數<br>
	 * 
	 * @return Integer
	 */
	public int getParentCnt() {
		return this.parentCnt;
	}

	/**
	 * 撫養父母人數<br>
	 * 
	 *
	 * @param parentCnt 撫養父母人數
	 */
	public void setParentCnt(int parentCnt) {
		this.parentCnt = parentCnt;
	}

	/**
	 * 撫養父母責任比率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getParentRate() {
		return this.parentRate;
	}

	/**
	 * 撫養父母責任比率<br>
	 * 
	 *
	 * @param parentRate 撫養父母責任比率
	 */
	public void setParentRate(BigDecimal parentRate) {
		this.parentRate = parentRate;
	}

	/**
	 * 其他法定撫養人數<br>
	 * 
	 * @return Integer
	 */
	public int getMouthCnt() {
		return this.mouthCnt;
	}

	/**
	 * 其他法定撫養人數<br>
	 * 
	 *
	 * @param mouthCnt 其他法定撫養人數
	 */
	public void setMouthCnt(int mouthCnt) {
		this.mouthCnt = mouthCnt;
	}

	/**
	 * 其他法定撫養人之責任比率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getMouthRate() {
		return this.mouthRate;
	}

	/**
	 * 其他法定撫養人之責任比率<br>
	 * 
	 *
	 * @param mouthRate 其他法定撫養人之責任比率
	 */
	public void setMouthRate(BigDecimal mouthRate) {
		this.mouthRate = mouthRate;
	}

	/**
	 * 屬二階段還款方案之階段註記<br>
	 * :非屬二階段還款 1:屬二階段還款方案之第一階段 2:屬二階段還款方案之第二階段
	 * 
	 * @return String
	 */
	public String getGradeType() {
		return this.gradeType == null ? "" : this.gradeType;
	}

	/**
	 * 屬二階段還款方案之階段註記<br>
	 * :非屬二階段還款 1:屬二階段還款方案之第一階段 2:屬二階段還款方案之第二階段
	 *
	 * @param gradeType 屬二階段還款方案之階段註記
	 */
	public void setGradeType(String gradeType) {
		this.gradeType = gradeType;
	}

	/**
	 * 第一階段最後一期應繳金額<br>
	 * 
	 * @return Integer
	 */
	public int getPayLastAmt() {
		return this.payLastAmt;
	}

	/**
	 * 第一階段最後一期應繳金額<br>
	 * 
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
		return "JcicZ044Log [jcicZ044LogId=" + jcicZ044LogId + ", tranKey=" + tranKey + ", debtCode=" + debtCode + ", nonGageAmt=" + nonGageAmt + ", period=" + period + ", rate=" + rate
				+ ", monthPayAmt=" + monthPayAmt + ", receYearIncome=" + receYearIncome + ", receYear=" + receYear + ", receYear2Income=" + receYear2Income + ", receYear2=" + receYear2
				+ ", currentMonthIncome=" + currentMonthIncome + ", livingCost=" + livingCost + ", compName=" + compName + ", compId=" + compId + ", carCnt=" + carCnt + ", houseCnt=" + houseCnt
				+ ", landCnt=" + landCnt + ", childCnt=" + childCnt + ", childRate=" + childRate + ", parentCnt=" + parentCnt + ", parentRate=" + parentRate + ", mouthCnt=" + mouthCnt + ", mouthRate="
				+ mouthRate + ", gradeType=" + gradeType + ", payLastAmt=" + payLastAmt + ", period2=" + period2 + ", rate2=" + rate2 + ", monthPayAmt2=" + monthPayAmt2 + ", payLastAmt2="
				+ payLastAmt2 + ", outJcicTxtDate=" + outJcicTxtDate + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo="
				+ lastUpdateEmpNo + "]";
	}
}
