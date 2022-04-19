package com.st1.itx.util.common.data;

import java.math.BigDecimal;

import javax.persistence.Entity;

@Entity
public class PfDetailVo {

	/*-------------------  計算輸入爛  -----------------*/

	// 借款人戶號
	private int custNo = 0;

	// 額度編號
	private int facmNo = 0;

	// 撥款序號
	private int bormNo = 0;

	// 交易內容檔序號
	private int borxNo = 0;

	// 還款類別
	/* 0.撥款 1.計件代碼變更 2.部分償還 3.提前結案 */
	private int repayType = 0;

	// 計件代碼
	private String pieceCode;

	// 撥款金額/追回金額
	private BigDecimal drawdownAmt = new BigDecimal("0");

	// 計件代碼2
	private String pieceCodeSecond;

	// 計件代碼2金額
	private BigDecimal pieceCodeSecondAmt = new BigDecimal("0");

	// 撥款日期
	private int drawdownDate = 0;

	// 已攤還期數
	private int repaidPeriod = 0;

	/*------------ 試算(不更新業績檔) -----------*/
	// 是否為試匴
	private boolean isTrial = false;

	/*------------ 重新計算業績輸入爛 (BS996 use) -----------*/
	// 業績日期
	private int perfDate = 0;

	// 是否以最新的員工資料檔更新介紹人所屬資料欄Y/N
	/* 單位、區部、部室及處經理代號、區經理代號、部經理代號 */
	private String empResetFg;

	@Override
	public String toString() {
		return "PfDetailVo [custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + ", borxNo=" + borxNo + ", repayType=" + repayType + ", pieceCode=" + pieceCode + ", drawdownAmt="
				+ drawdownAmt + ", pieceCodeSecond=" + pieceCodeSecond + ", pieceCodeSecondAmt=" + pieceCodeSecondAmt + ", drawdownDate=" + drawdownDate + ", repaidPeriod=" + repaidPeriod
				+ ", isTrial=" + isTrial + ", perfDate=" + perfDate + ", empResetFg=" + empResetFg + "]";
	}

	public int getCustNo() {
		return custNo;
	}

	public void setCustNo(int custNo) {
		this.custNo = custNo;
	}

	public int getFacmNo() {
		return facmNo;
	}

	public void setFacmNo(int facmNo) {
		this.facmNo = facmNo;
	}

	public int getBormNo() {
		return bormNo;
	}

	public void setBormNo(int bormNo) {
		this.bormNo = bormNo;
	}

	public int getBorxNo() {
		return borxNo;
	}

	public void setBorxNo(int borxNo) {
		this.borxNo = borxNo;
	}

	public String getPieceCode() {
		return pieceCode;
	}

	public void setPieceCode(String pieceCode) {
		this.pieceCode = pieceCode;
	}

	public int getRepayType() {
		return repayType;
	}

	public void setRepayType(int repayType) {
		this.repayType = repayType;
	}

	public BigDecimal getDrawdownAmt() {
		return drawdownAmt;
	}

	public void setDrawdownAmt(BigDecimal drawdownAmt) {
		this.drawdownAmt = drawdownAmt;
	}

	public String getPieceCodeSecond() {
		return pieceCodeSecond;
	}

	public void setPieceCodeSecond(String pieceCodeSecond) {
		this.pieceCodeSecond = pieceCodeSecond;
	}

	public BigDecimal getPieceCodeSecondAmt() {
		return pieceCodeSecondAmt;
	}

	public void setPieceCodeSecondAmt(BigDecimal pieceCodeSecondAmt) {
		this.pieceCodeSecondAmt = pieceCodeSecondAmt;
	}

	public int getDrawdownDate() {
		return drawdownDate;
	}

	public void setDrawdownDate(int drawdownDate) {
		this.drawdownDate = drawdownDate;
	}

	public int getRepaidPeriod() {
		return repaidPeriod;
	}

	public void setRepaidPeriod(int repaidPeriod) {
		this.repaidPeriod = repaidPeriod;
	}

	public boolean isTrial() {
		return isTrial;
	}

	public void setTrial(boolean isTrial) {
		this.isTrial = isTrial;
	}

	public int getPerfDate() {
		return perfDate;
	}

	public void setPerfDate(int perfDate) {
		this.perfDate = perfDate;
	}

	public String getEmpResetFg() {
		return empResetFg;
	}

	public void setEmpResetFg(String empResetFg) {
		this.empResetFg = empResetFg;
	}

}
