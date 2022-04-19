package com.st1.itx.db.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * RepayActChangeLog 還款帳號變更(含還款方式)紀錄檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`RepayActChangeLog`")
public class RepayActChangeLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7437159469743570140L;

// 序號
	@Id
	@Column(name = "`LogNo`")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "`RepayActChangeLog_SEQ`")
	@SequenceGenerator(name = "`RepayActChangeLog_SEQ`", sequenceName = "`RepayActChangeLog_SEQ`", allocationSize = 1)
	private Long logNo = 0L;

	// 戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度
	/* 兩個額度共用同一扣款帳號則ACH授權記錄檔只有第一個額度送出授權，但授權帳號檔會寫兩筆 */
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 繳款方式
	/* 共用代碼檔 RepayCode1: 匯款轉帳2: 銀行扣款3: 員工扣薪4: 支票5: 特約金6: 人事特約金7: 定存特約8: 劃撥存款 */
	@Column(name = "`RepayCode`")
	private int repayCode = 0;

	// 扣款銀行
	@Column(name = "`RepayBank`", length = 3)
	private String repayBank;

	// 郵局存款別
	/* CdCode:PostDepCode存簿：P劃撥：G */
	@Column(name = "`PostDepCode`", length = 1)
	private String postDepCode;

	// 扣款帳號
	/* 變更扣款帳號時授權成功才會更新 */
	@Column(name = "`RepayAcct`", length = 14)
	private String repayAcct;

	// 狀態碼
	/* 空白:未授權0:授權成功 1:停止使用 2.取消授權 8.授權失敗9:已送出授權 */
	@Column(name = "`Status`", length = 1)
	private String status;

	// 登放日期
	@Column(name = "`RelDy`")
	private int relDy = 0;

	// 登放序號
	/* 單位別(4)+經辦(6)+交易序號(8) */
	@Column(name = "`RelTxseq`", length = 18)
	private String relTxseq;

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
	 * 序號<br>
	 * 
	 * @return Long
	 */
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getLogNo() {
		return this.logNo;
	}

	/**
	 * 序號<br>
	 * 
	 *
	 * @param logNo 序號
	 */
	public void setLogNo(Long logNo) {
		this.logNo = logNo;
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
	 * 額度<br>
	 * 兩個額度共用同一扣款帳號則ACH授權記錄檔只有第一個額度送出授權，但授權帳號檔會寫兩筆
	 * 
	 * @return Integer
	 */
	public int getFacmNo() {
		return this.facmNo;
	}

	/**
	 * 額度<br>
	 * 兩個額度共用同一扣款帳號則ACH授權記錄檔只有第一個額度送出授權，但授權帳號檔會寫兩筆
	 *
	 * @param facmNo 額度
	 */
	public void setFacmNo(int facmNo) {
		this.facmNo = facmNo;
	}

	/**
	 * 繳款方式<br>
	 * 共用代碼檔 RepayCode 1: 匯款轉帳 2: 銀行扣款 3: 員工扣薪 4: 支票 5: 特約金 6: 人事特約金 7: 定存特約 8: 劃撥存款
	 * 
	 * @return Integer
	 */
	public int getRepayCode() {
		return this.repayCode;
	}

	/**
	 * 繳款方式<br>
	 * 共用代碼檔 RepayCode 1: 匯款轉帳 2: 銀行扣款 3: 員工扣薪 4: 支票 5: 特約金 6: 人事特約金 7: 定存特約 8: 劃撥存款
	 *
	 * @param repayCode 繳款方式
	 */
	public void setRepayCode(int repayCode) {
		this.repayCode = repayCode;
	}

	/**
	 * 扣款銀行<br>
	 * 
	 * @return String
	 */
	public String getRepayBank() {
		return this.repayBank == null ? "" : this.repayBank;
	}

	/**
	 * 扣款銀行<br>
	 * 
	 *
	 * @param repayBank 扣款銀行
	 */
	public void setRepayBank(String repayBank) {
		this.repayBank = repayBank;
	}

	/**
	 * 郵局存款別<br>
	 * CdCode:PostDepCode 存簿：P劃撥：G
	 * 
	 * @return String
	 */
	public String getPostDepCode() {
		return this.postDepCode == null ? "" : this.postDepCode;
	}

	/**
	 * 郵局存款別<br>
	 * CdCode:PostDepCode 存簿：P劃撥：G
	 *
	 * @param postDepCode 郵局存款別
	 */
	public void setPostDepCode(String postDepCode) {
		this.postDepCode = postDepCode;
	}

	/**
	 * 扣款帳號<br>
	 * 變更扣款帳號時授權成功才會更新
	 * 
	 * @return String
	 */
	public String getRepayAcct() {
		return this.repayAcct == null ? "" : this.repayAcct;
	}

	/**
	 * 扣款帳號<br>
	 * 變更扣款帳號時授權成功才會更新
	 *
	 * @param repayAcct 扣款帳號
	 */
	public void setRepayAcct(String repayAcct) {
		this.repayAcct = repayAcct;
	}

	/**
	 * 狀態碼<br>
	 * 空白:未授權 0:授權成功 1:停止使用 2.取消授權 8.授權失敗 9:已送出授權
	 * 
	 * @return String
	 */
	public String getStatus() {
		return this.status == null ? "" : this.status;
	}

	/**
	 * 狀態碼<br>
	 * 空白:未授權 0:授權成功 1:停止使用 2.取消授權 8.授權失敗 9:已送出授權
	 *
	 * @param status 狀態碼
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 登放日期<br>
	 * 
	 * @return Integer
	 */
	public int getRelDy() {
		return StaticTool.bcToRoc(this.relDy);
	}

	/**
	 * 登放日期<br>
	 * 
	 *
	 * @param relDy 登放日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setRelDy(int relDy) throws LogicException {
		this.relDy = StaticTool.rocToBc(relDy);
	}

	/**
	 * 登放序號<br>
	 * 單位別(4)+經辦(6)+交易序號(8)
	 * 
	 * @return String
	 */
	public String getRelTxseq() {
		return this.relTxseq == null ? "" : this.relTxseq;
	}

	/**
	 * 登放序號<br>
	 * 單位別(4)+經辦(6)+交易序號(8)
	 *
	 * @param relTxseq 登放序號
	 */
	public void setRelTxseq(String relTxseq) {
		this.relTxseq = relTxseq;
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
		return "RepayActChangeLog [logNo=" + logNo + ", custNo=" + custNo + ", facmNo=" + facmNo + ", repayCode=" + repayCode + ", repayBank=" + repayBank + ", postDepCode=" + postDepCode
				+ ", repayAcct=" + repayAcct + ", status=" + status + ", relDy=" + relDy + ", relTxseq=" + relTxseq + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate="
				+ lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
