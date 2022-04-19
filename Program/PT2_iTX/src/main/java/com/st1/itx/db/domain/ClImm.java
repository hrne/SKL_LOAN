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
 * ClImm 擔保品不動產檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`ClImm`")
public class ClImm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7997369176754068602L;

	@EmbeddedId
	private ClImmId clImmId;

	// 擔保品代號1
	/* 擔保品代號碼CdCl */
	@Column(name = "`ClCode1`", insertable = false, updatable = false)
	private int clCode1 = 0;

	// 擔保品代號2
	/* 擔保品代號碼CdCl */
	@Column(name = "`ClCode2`", insertable = false, updatable = false)
	private int clCode2 = 0;

	// 擔保品編號
	@Column(name = "`ClNo`", insertable = false, updatable = false)
	private int clNo = 0;

	// 評估淨值
	@Column(name = "`EvaNetWorth`")
	private BigDecimal evaNetWorth = new BigDecimal("0");

	// 土地增值稅
	@Column(name = "`LVITax`")
	private BigDecimal lVITax = new BigDecimal("0");

	// 出租評估淨值
	@Column(name = "`RentEvaValue`")
	private BigDecimal rentEvaValue = new BigDecimal("0");

	// 押租金
	@Column(name = "`RentPrice`")
	private BigDecimal rentPrice = new BigDecimal("0");

	// 權利種類
	/* 共用代碼檔1.抵押權2.地上權3.抵押權+地上權 */
	@Column(name = "`OwnershipCode`", length = 1)
	private String ownershipCode;

	// 抵押權註記
	/* 共用代碼檔0:最高限額抵押權1:普通抵押權 */
	@Column(name = "`MtgCode`", length = 1)
	private String mtgCode;

	// 最高限額抵押權之擔保債權種類-票據
	/* Y:是N:否 */
	@Column(name = "`MtgCheck`", length = 1)
	private String mtgCheck;

	// 最高限額抵押權之擔保債權種類-借款
	/* Y:是N:否 */
	@Column(name = "`MtgLoan`", length = 1)
	private String mtgLoan;

	// 最高限額抵押權之擔保債權種類-保證債務
	/* Y:是N:否 */
	@Column(name = "`MtgPledge`", length = 1)
	private String mtgPledge;

	// 檢附同意書
	/* Y:是N:否 */
	@Column(name = "`Agreement`", length = 1)
	private String agreement;

	// 鑑價公司
	@Column(name = "`EvaCompanyCode`", length = 2)
	private String evaCompanyCode;

	// 限制塗銷日期
	@Column(name = "`LimitCancelDate`")
	private int limitCancelDate = 0;

	// 擔保註記
	/* 共用代碼檔1:擔保2:副擔保 */
	@Column(name = "`ClCode`", length = 1)
	private String clCode;

	// 貸放成數(%)
	@Column(name = "`LoanToValue`")
	private BigDecimal loanToValue = new BigDecimal("0");

	// 其他債權人設定總額
	@Column(name = "`OtherOwnerTotal`")
	private BigDecimal otherOwnerTotal = new BigDecimal("0");

	// 代償後謄本
	/* 0:無1:有 */
	@Column(name = "`CompensationCopy`", length = 1)
	private String compensationCopy;

	// 建物標示備註
	@Column(name = "`BdRmk`", length = 60)
	private String bdRmk;

	// 最高抵押權確定事由
	/*
	 * 共用代碼檔1.擔保品遭查封(民事執行處)2.擔保品遭查封(行政執行處)3.本公司申請裁定拍賣抵押物4.擔保品經本公司聲請強制執行5.擔保品之查封經撤銷(
	 * 民事執行處)6.擔保品之查封經撤銷(行政執行處)
	 */
	@Column(name = "`MtgReasonCode`", length = 1)
	private String mtgReasonCode;

	// 收文日期
	@Column(name = "`ReceivedDate`")
	private int receivedDate = 0;

	// 收文案號
	@Column(name = "`ReceivedNo`", length = 20)
	private String receivedNo;

	// 撤銷日期
	@Column(name = "`CancelDate`")
	private int cancelDate = 0;

	// 撤銷案號
	@Column(name = "`CancelNo`", length = 20)
	private String cancelNo;

	// 設定狀態
	/* 1:設定2:解除 */
	@Column(name = "`SettingStat`", length = 1)
	private String settingStat;

	// 擔保品狀態
	/* 0:正常1:塗銷2:處分3:抵押權確定 */
	@Column(name = "`ClStat`", length = 1)
	private String clStat;

	// 設定日期
	@Column(name = "`SettingDate`")
	private int settingDate = 0;

	// 設定金額
	@Column(name = "`SettingAmt`")
	private BigDecimal settingAmt = new BigDecimal("0");

	// 擔保債權確定日期
	@Column(name = "`ClaimDate`")
	private int claimDate = 0;

	// 設定順位(1~9)
	@Column(name = "`SettingSeq`", length = 1)
	private String settingSeq;

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

	public ClImmId getClImmId() {
		return this.clImmId;
	}

	public void setClImmId(ClImmId clImmId) {
		this.clImmId = clImmId;
	}

	/**
	 * 擔保品代號1<br>
	 * 擔保品代號碼CdCl
	 * 
	 * @return Integer
	 */
	public int getClCode1() {
		return this.clCode1;
	}

	/**
	 * 擔保品代號1<br>
	 * 擔保品代號碼CdCl
	 *
	 * @param clCode1 擔保品代號1
	 */
	public void setClCode1(int clCode1) {
		this.clCode1 = clCode1;
	}

	/**
	 * 擔保品代號2<br>
	 * 擔保品代號碼CdCl
	 * 
	 * @return Integer
	 */
	public int getClCode2() {
		return this.clCode2;
	}

	/**
	 * 擔保品代號2<br>
	 * 擔保品代號碼CdCl
	 *
	 * @param clCode2 擔保品代號2
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
	 * 評估淨值<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getEvaNetWorth() {
		return this.evaNetWorth;
	}

	/**
	 * 評估淨值<br>
	 * 
	 *
	 * @param evaNetWorth 評估淨值
	 */
	public void setEvaNetWorth(BigDecimal evaNetWorth) {
		this.evaNetWorth = evaNetWorth;
	}

	/**
	 * 土地增值稅<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getLVITax() {
		return this.lVITax;
	}

	/**
	 * 土地增值稅<br>
	 * 
	 *
	 * @param lVITax 土地增值稅
	 */
	public void setLVITax(BigDecimal lVITax) {
		this.lVITax = lVITax;
	}

	/**
	 * 出租評估淨值<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getRentEvaValue() {
		return this.rentEvaValue;
	}

	/**
	 * 出租評估淨值<br>
	 * 
	 *
	 * @param rentEvaValue 出租評估淨值
	 */
	public void setRentEvaValue(BigDecimal rentEvaValue) {
		this.rentEvaValue = rentEvaValue;
	}

	/**
	 * 押租金<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getRentPrice() {
		return this.rentPrice;
	}

	/**
	 * 押租金<br>
	 * 
	 *
	 * @param rentPrice 押租金
	 */
	public void setRentPrice(BigDecimal rentPrice) {
		this.rentPrice = rentPrice;
	}

	/**
	 * 權利種類<br>
	 * 共用代碼檔 1.抵押權 2.地上權 3.抵押權+地上權
	 * 
	 * @return String
	 */
	public String getOwnershipCode() {
		return this.ownershipCode == null ? "" : this.ownershipCode;
	}

	/**
	 * 權利種類<br>
	 * 共用代碼檔 1.抵押權 2.地上權 3.抵押權+地上權
	 *
	 * @param ownershipCode 權利種類
	 */
	public void setOwnershipCode(String ownershipCode) {
		this.ownershipCode = ownershipCode;
	}

	/**
	 * 抵押權註記<br>
	 * 共用代碼檔 0:最高限額抵押權 1:普通抵押權
	 * 
	 * @return String
	 */
	public String getMtgCode() {
		return this.mtgCode == null ? "" : this.mtgCode;
	}

	/**
	 * 抵押權註記<br>
	 * 共用代碼檔 0:最高限額抵押權 1:普通抵押權
	 *
	 * @param mtgCode 抵押權註記
	 */
	public void setMtgCode(String mtgCode) {
		this.mtgCode = mtgCode;
	}

	/**
	 * 最高限額抵押權之擔保債權種類-票據<br>
	 * Y:是 N:否
	 * 
	 * @return String
	 */
	public String getMtgCheck() {
		return this.mtgCheck == null ? "" : this.mtgCheck;
	}

	/**
	 * 最高限額抵押權之擔保債權種類-票據<br>
	 * Y:是 N:否
	 *
	 * @param mtgCheck 最高限額抵押權之擔保債權種類-票據
	 */
	public void setMtgCheck(String mtgCheck) {
		this.mtgCheck = mtgCheck;
	}

	/**
	 * 最高限額抵押權之擔保債權種類-借款<br>
	 * Y:是 N:否
	 * 
	 * @return String
	 */
	public String getMtgLoan() {
		return this.mtgLoan == null ? "" : this.mtgLoan;
	}

	/**
	 * 最高限額抵押權之擔保債權種類-借款<br>
	 * Y:是 N:否
	 *
	 * @param mtgLoan 最高限額抵押權之擔保債權種類-借款
	 */
	public void setMtgLoan(String mtgLoan) {
		this.mtgLoan = mtgLoan;
	}

	/**
	 * 最高限額抵押權之擔保債權種類-保證債務<br>
	 * Y:是 N:否
	 * 
	 * @return String
	 */
	public String getMtgPledge() {
		return this.mtgPledge == null ? "" : this.mtgPledge;
	}

	/**
	 * 最高限額抵押權之擔保債權種類-保證債務<br>
	 * Y:是 N:否
	 *
	 * @param mtgPledge 最高限額抵押權之擔保債權種類-保證債務
	 */
	public void setMtgPledge(String mtgPledge) {
		this.mtgPledge = mtgPledge;
	}

	/**
	 * 檢附同意書<br>
	 * Y:是 N:否
	 * 
	 * @return String
	 */
	public String getAgreement() {
		return this.agreement == null ? "" : this.agreement;
	}

	/**
	 * 檢附同意書<br>
	 * Y:是 N:否
	 *
	 * @param agreement 檢附同意書
	 */
	public void setAgreement(String agreement) {
		this.agreement = agreement;
	}

	/**
	 * 鑑價公司<br>
	 * 
	 * @return String
	 */
	public String getEvaCompanyCode() {
		return this.evaCompanyCode == null ? "" : this.evaCompanyCode;
	}

	/**
	 * 鑑價公司<br>
	 * 
	 *
	 * @param evaCompanyCode 鑑價公司
	 */
	public void setEvaCompanyCode(String evaCompanyCode) {
		this.evaCompanyCode = evaCompanyCode;
	}

	/**
	 * 限制塗銷日期<br>
	 * 
	 * @return Integer
	 */
	public int getLimitCancelDate() {
		return StaticTool.bcToRoc(this.limitCancelDate);
	}

	/**
	 * 限制塗銷日期<br>
	 * 
	 *
	 * @param limitCancelDate 限制塗銷日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setLimitCancelDate(int limitCancelDate) throws LogicException {
		this.limitCancelDate = StaticTool.rocToBc(limitCancelDate);
	}

	/**
	 * 擔保註記<br>
	 * 共用代碼檔 1:擔保 2:副擔保
	 * 
	 * @return String
	 */
	public String getClCode() {
		return this.clCode == null ? "" : this.clCode;
	}

	/**
	 * 擔保註記<br>
	 * 共用代碼檔 1:擔保 2:副擔保
	 *
	 * @param clCode 擔保註記
	 */
	public void setClCode(String clCode) {
		this.clCode = clCode;
	}

	/**
	 * 貸放成數(%)<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getLoanToValue() {
		return this.loanToValue;
	}

	/**
	 * 貸放成數(%)<br>
	 * 
	 *
	 * @param loanToValue 貸放成數(%)
	 */
	public void setLoanToValue(BigDecimal loanToValue) {
		this.loanToValue = loanToValue;
	}

	/**
	 * 其他債權人設定總額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getOtherOwnerTotal() {
		return this.otherOwnerTotal;
	}

	/**
	 * 其他債權人設定總額<br>
	 * 
	 *
	 * @param otherOwnerTotal 其他債權人設定總額
	 */
	public void setOtherOwnerTotal(BigDecimal otherOwnerTotal) {
		this.otherOwnerTotal = otherOwnerTotal;
	}

	/**
	 * 代償後謄本<br>
	 * 0:無 1:有
	 * 
	 * @return String
	 */
	public String getCompensationCopy() {
		return this.compensationCopy == null ? "" : this.compensationCopy;
	}

	/**
	 * 代償後謄本<br>
	 * 0:無 1:有
	 *
	 * @param compensationCopy 代償後謄本
	 */
	public void setCompensationCopy(String compensationCopy) {
		this.compensationCopy = compensationCopy;
	}

	/**
	 * 建物標示備註<br>
	 * 
	 * @return String
	 */
	public String getBdRmk() {
		return this.bdRmk == null ? "" : this.bdRmk;
	}

	/**
	 * 建物標示備註<br>
	 * 
	 *
	 * @param bdRmk 建物標示備註
	 */
	public void setBdRmk(String bdRmk) {
		this.bdRmk = bdRmk;
	}

	/**
	 * 最高抵押權確定事由<br>
	 * 共用代碼檔 1.擔保品遭查封(民事執行處) 2.擔保品遭查封(行政執行處) 3.本公司申請裁定拍賣抵押物 4.擔保品經本公司聲請強制執行
	 * 5.擔保品之查封經撤銷(民事執行處) 6.擔保品之查封經撤銷(行政執行處)
	 * 
	 * @return String
	 */
	public String getMtgReasonCode() {
		return this.mtgReasonCode == null ? "" : this.mtgReasonCode;
	}

	/**
	 * 最高抵押權確定事由<br>
	 * 共用代碼檔 1.擔保品遭查封(民事執行處) 2.擔保品遭查封(行政執行處) 3.本公司申請裁定拍賣抵押物 4.擔保品經本公司聲請強制執行
	 * 5.擔保品之查封經撤銷(民事執行處) 6.擔保品之查封經撤銷(行政執行處)
	 *
	 * @param mtgReasonCode 最高抵押權確定事由
	 */
	public void setMtgReasonCode(String mtgReasonCode) {
		this.mtgReasonCode = mtgReasonCode;
	}

	/**
	 * 收文日期<br>
	 * 
	 * @return Integer
	 */
	public int getReceivedDate() {
		return StaticTool.bcToRoc(this.receivedDate);
	}

	/**
	 * 收文日期<br>
	 * 
	 *
	 * @param receivedDate 收文日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setReceivedDate(int receivedDate) throws LogicException {
		this.receivedDate = StaticTool.rocToBc(receivedDate);
	}

	/**
	 * 收文案號<br>
	 * 
	 * @return String
	 */
	public String getReceivedNo() {
		return this.receivedNo == null ? "" : this.receivedNo;
	}

	/**
	 * 收文案號<br>
	 * 
	 *
	 * @param receivedNo 收文案號
	 */
	public void setReceivedNo(String receivedNo) {
		this.receivedNo = receivedNo;
	}

	/**
	 * 撤銷日期<br>
	 * 
	 * @return Integer
	 */
	public int getCancelDate() {
		return StaticTool.bcToRoc(this.cancelDate);
	}

	/**
	 * 撤銷日期<br>
	 * 
	 *
	 * @param cancelDate 撤銷日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setCancelDate(int cancelDate) throws LogicException {
		this.cancelDate = StaticTool.rocToBc(cancelDate);
	}

	/**
	 * 撤銷案號<br>
	 * 
	 * @return String
	 */
	public String getCancelNo() {
		return this.cancelNo == null ? "" : this.cancelNo;
	}

	/**
	 * 撤銷案號<br>
	 * 
	 *
	 * @param cancelNo 撤銷案號
	 */
	public void setCancelNo(String cancelNo) {
		this.cancelNo = cancelNo;
	}

	/**
	 * 設定狀態<br>
	 * 1:設定 2:解除
	 * 
	 * @return String
	 */
	public String getSettingStat() {
		return this.settingStat == null ? "" : this.settingStat;
	}

	/**
	 * 設定狀態<br>
	 * 1:設定 2:解除
	 *
	 * @param settingStat 設定狀態
	 */
	public void setSettingStat(String settingStat) {
		this.settingStat = settingStat;
	}

	/**
	 * 擔保品狀態<br>
	 * 0:正常 1:塗銷 2:處分 3:抵押權確定
	 * 
	 * @return String
	 */
	public String getClStat() {
		return this.clStat == null ? "" : this.clStat;
	}

	/**
	 * 擔保品狀態<br>
	 * 0:正常 1:塗銷 2:處分 3:抵押權確定
	 *
	 * @param clStat 擔保品狀態
	 */
	public void setClStat(String clStat) {
		this.clStat = clStat;
	}

	/**
	 * 設定日期<br>
	 * 
	 * @return Integer
	 */
	public int getSettingDate() {
		return StaticTool.bcToRoc(this.settingDate);
	}

	/**
	 * 設定日期<br>
	 * 
	 *
	 * @param settingDate 設定日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setSettingDate(int settingDate) throws LogicException {
		this.settingDate = StaticTool.rocToBc(settingDate);
	}

	/**
	 * 設定金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getSettingAmt() {
		return this.settingAmt;
	}

	/**
	 * 設定金額<br>
	 * 
	 *
	 * @param settingAmt 設定金額
	 */
	public void setSettingAmt(BigDecimal settingAmt) {
		this.settingAmt = settingAmt;
	}

	/**
	 * 擔保債權確定日期<br>
	 * 
	 * @return Integer
	 */
	public int getClaimDate() {
		return StaticTool.bcToRoc(this.claimDate);
	}

	/**
	 * 擔保債權確定日期<br>
	 * 
	 *
	 * @param claimDate 擔保債權確定日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setClaimDate(int claimDate) throws LogicException {
		this.claimDate = StaticTool.rocToBc(claimDate);
	}

	/**
	 * 設定順位(1~9)<br>
	 * 
	 * @return String
	 */
	public String getSettingSeq() {
		return this.settingSeq == null ? "" : this.settingSeq;
	}

	/**
	 * 設定順位(1~9)<br>
	 * 
	 *
	 * @param settingSeq 設定順位(1~9)
	 */
	public void setSettingSeq(String settingSeq) {
		this.settingSeq = settingSeq;
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
		return "ClImm [clImmId=" + clImmId + ", evaNetWorth=" + evaNetWorth + ", lVITax=" + lVITax + ", rentEvaValue=" + rentEvaValue + ", rentPrice=" + rentPrice + ", ownershipCode=" + ownershipCode
				+ ", mtgCode=" + mtgCode + ", mtgCheck=" + mtgCheck + ", mtgLoan=" + mtgLoan + ", mtgPledge=" + mtgPledge + ", agreement=" + agreement + ", evaCompanyCode=" + evaCompanyCode
				+ ", limitCancelDate=" + limitCancelDate + ", clCode=" + clCode + ", loanToValue=" + loanToValue + ", otherOwnerTotal=" + otherOwnerTotal + ", compensationCopy=" + compensationCopy
				+ ", bdRmk=" + bdRmk + ", mtgReasonCode=" + mtgReasonCode + ", receivedDate=" + receivedDate + ", receivedNo=" + receivedNo + ", cancelDate=" + cancelDate + ", cancelNo=" + cancelNo
				+ ", settingStat=" + settingStat + ", clStat=" + clStat + ", settingDate=" + settingDate + ", settingAmt=" + settingAmt + ", claimDate=" + claimDate + ", settingSeq=" + settingSeq
				+ ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
