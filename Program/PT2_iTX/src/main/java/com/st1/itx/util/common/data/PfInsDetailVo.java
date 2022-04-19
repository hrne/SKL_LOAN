package com.st1.itx.util.common.data;

import java.math.BigDecimal;

public class PfInsDetailVo {

	String policy_no; // 保單號碼
	String po_status_code; // 狀況
	String custName; // 要保人
	int beginDate; // 始期
	BigDecimal face_amt; // 保額
	BigDecimal highest_loan; // 已貸金額
	BigDecimal loan_amt; // 可貸金額
	BigDecimal exchage_val; // 匯率
	BigDecimal mode_prem_year; // 年繳化保費(NTD)
	String insurance_type_3; // 保險型態
	BigDecimal rvl_values; // 帳戶價值
	String currency_1; // 幣別
	String names_i; // 被保人
	int application_date; // 承保日

	public String getPolicy_no() {
		return policy_no;
	}

	@Override
	public String toString() {
		return "PfInsDetailVo [policy_no=" + policy_no + ", po_status_code=" + po_status_code + ", custName=" + custName + ", beginDate=" + beginDate + ", face_amt=" + face_amt + ", highest_loan="
				+ highest_loan + ", loan_amt=" + loan_amt + ", exchage_val=" + exchage_val + ", mode_prem_year=" + mode_prem_year + ", insurance_type_3=" + insurance_type_3 + ", rvl_values="
				+ rvl_values + ", currency_1=" + currency_1 + ", names_i=" + names_i + ", application_date=" + application_date + "]";
	}

	public void setPolicy_no(String policy_no) {
		this.policy_no = policy_no;
	}

	public String getPo_status_code() {
		return po_status_code;
	}

	public void setPo_status_code(String po_status_code) {
		this.po_status_code = po_status_code;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public int getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(int beginDate) {
		this.beginDate = beginDate;
	}

	public BigDecimal getFace_amt() {
		return face_amt;
	}

	public void setFace_amt(BigDecimal face_amt) {
		this.face_amt = face_amt;
	}

	public BigDecimal getHighest_loan() {
		return highest_loan;
	}

	public void setHighest_loan(BigDecimal highest_loan) {
		this.highest_loan = highest_loan;
	}

	public BigDecimal getLoan_amt() {
		return loan_amt;
	}

	public void setLoan_amt(BigDecimal loan_amt) {
		this.loan_amt = loan_amt;
	}

	public BigDecimal getExchage_val() {
		return exchage_val;
	}

	public void setExchage_val(BigDecimal exchage_val) {
		this.exchage_val = exchage_val;
	}

	public BigDecimal getMode_prem_year() {
		return mode_prem_year;
	}

	public void setMode_prem_year(BigDecimal mode_prem_year) {
		this.mode_prem_year = mode_prem_year;
	}

	public String getInsurance_type_3() {
		return insurance_type_3;
	}

	public void setInsurance_type_3(String insurance_type_3) {
		this.insurance_type_3 = insurance_type_3;
	}

	public BigDecimal getRvl_values() {
		return rvl_values;
	}

	public void setRvl_values(BigDecimal rvl_values) {
		this.rvl_values = rvl_values;
	}

	public String getCurrency_1() {
		return currency_1;
	}

	public void setCurrency_1(String currency_1) {
		this.currency_1 = currency_1;
	}

	public String getNames_i() {
		return names_i;
	}

	public void setNames_i(String names_i) {
		this.names_i = names_i;
	}

	public int getApplication_date() {
		return application_date;
	}

	public void setApplication_date(int application_date) {
		this.application_date = application_date;
	}

}
