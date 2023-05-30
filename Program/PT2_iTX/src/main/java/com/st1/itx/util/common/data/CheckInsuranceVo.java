package com.st1.itx.util.common.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("checkInsuranceVo")
@Scope("prototype")
public class CheckInsuranceVo extends LinkedHashMap<String, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -785343870608521943L;

	/**
	 * M 來源系統ID,預設值 Loan
	 */
	private String SrcSystemID = "LOAN";

	/**
	 * M 交易名稱
	 */
	private String TxnID = "ESB00060";

	/**
	 * M 保戶身份證字號
	 */
	private String CustId = "";

	/**
	 * ****************************************************** 回覆參數
	 */

	/*
	 * connect success 0.no 1.yes
	 */
	private boolean success = false;

	/**
	 * response
	 */
	private String msgRs = "";

	/**
	 * 回覆detail
	 */
	private List<HashMap<String, String>> detail = new ArrayList<HashMap<String, String>>();

	/**
	 * @return the srcSystemID
	 */
	public String getSrcSystemID() {
		return SrcSystemID;
	}

	/**
	 * @param srcSystemID the srcSystemID to set
	 */
	public void setSrcSystemID(String srcSystemID) {
		SrcSystemID = srcSystemID;
	}

	/**
	 * @return the txnID
	 */
	public String getTxnID() {
		return TxnID;
	}

	/**
	 * @param txnID the txnID to set
	 */
	public void setTxnID(String txnID) {
		TxnID = txnID;
	}

	/**
	 * @return the custId
	 */
	public String getCustId() {
		return CustId;
	}

	/**
	 * @param custId the custId to set
	 */
	public void setCustId(String custId) {
		CustId = custId;
	}

	/**
	 * @return the msgRs 保單號碼 policy_no=1096709812 狀況 po_status_code=P 要保人
	 *         names_o=李四3 始期 issue_date=2020-02-12+08:00 保額 face_amt=1000 已貸金額
	 *         highest_loan=0 可貸金額 匯率 exchage_val=1 年繳化保費(NTD) mode_prem_year=4060
	 *         保險型態 insurance_type_3=C 帳戶價值 rvl_values=0 幣別 currency_1=NTD 被保人
	 *         names_i=張三3 承保日 application_date=2020-02-25+08:00 collect_po_type=3
	 *         loan_amt=0 medical_ind_bpm= available=Y
	 */
	public String getMsgRs() {
		return msgRs;
	}

	/**
	 * @param msgRs the msgRs to set
	 */
	public void setMsgRs(String msgRs) {
		this.msgRs = msgRs;
	}

	/**
	 * @return the detail
	 */
	public List<HashMap<String, String>> getDetail() {
		return detail;
	}

	/**
	 * @param detail the detail to set
	 */
//	              保單號碼	policy_no=1096709812
//			狀況	po_status_code=P
//			要保人	names_o=李四3
//			始期	issue_date=2020-02-12+08:00
//			保額	face_amt=1000
//			已貸金額	highest_loan=0
//			可貸金額	 loan_bal : if (highest_loan == null) -> 0 else -> [loan_amt] + [highest_loan]
//			匯率	exchage_val=1
//			年繳化保費(NTD)	mode_prem_year=4060
//			保險型態	insurance_type_3=C
//			帳戶價值	rvl_values=0
//			幣別	currency_1=NTD
//			被保人	names_i=張三3
//			承保日	application_date=2020-02-25+08:00
//			collect_po_type=3
//			loan_amt=0
//			medical_ind_bpm=
//			available=Y

	public void setDetail(List<HashMap<String, String>> detail) {
		this.detail = detail;
	}

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

}
