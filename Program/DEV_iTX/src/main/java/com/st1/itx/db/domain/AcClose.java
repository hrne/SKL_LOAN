package com.st1.itx.db.domain;

import java.io.Serializable;
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
 * AcClose 會計業務關帳控制檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`AcClose`")
public class AcClose implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1022890788761738004L;

	@EmbeddedId
	private AcCloseId acCloseId;

	// 會計日期
	@Column(name = "`AcDate`", insertable = false, updatable = false)
	private int acDate = 0;

	// 單位別
	@Column(name = "`BranchNo`", length = 4, insertable = false, updatable = false)
	private String branchNo;

	// 業務類別
	/* 01:撥款匯款 (含暫收退還且非退票)02:支票繳款 03:債協09:放款 */
	@Column(name = "`SecNo`", length = 2, insertable = false, updatable = false)
	private String secNo;

	// 關帳狀態
	/* 0：開帳1：關帳2：關帳取消 */
	@Column(name = "`ClsFg`")
	private int clsFg = 0;

	// 業務批號
	/* 預設為01，關帳後再開帳則＋１ */
	@Column(name = "`BatNo`")
	private int batNo = 0;

	// 業務關帳次數
	/* 預設為00，關帳時+1、關帳取消則-1 */
	@Column(name = "`ClsNo`")
	private int clsNo = 0;

	// 傳票號碼
	/* 每日由1起編 */
	@Column(name = "`SlipNo`")
	private int slipNo = 0;

	// 上傳核心序號
	/* 只更新特定筆(09:放款)預設為000，產生上傳媒體(02:支票繳款，09:放款)關帳時＋１ */
	@Column(name = "`CoreSeqNo`")
	private int coreSeqNo = 0;

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

	public AcCloseId getAcCloseId() {
		return this.acCloseId;
	}

	public void setAcCloseId(AcCloseId acCloseId) {
		this.acCloseId = acCloseId;
	}

	/**
	 * 會計日期<br>
	 * 
	 * @return Integer
	 */
	public int getAcDate() {
		return StaticTool.bcToRoc(this.acDate);
	}

	/**
	 * 會計日期<br>
	 * 
	 *
	 * @param acDate 會計日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setAcDate(int acDate) throws LogicException {
		this.acDate = StaticTool.rocToBc(acDate);
	}

	/**
	 * 單位別<br>
	 * 
	 * @return String
	 */
	public String getBranchNo() {
		return this.branchNo == null ? "" : this.branchNo;
	}

	/**
	 * 單位別<br>
	 * 
	 *
	 * @param branchNo 單位別
	 */
	public void setBranchNo(String branchNo) {
		this.branchNo = branchNo;
	}

	/**
	 * 業務類別<br>
	 * 01:撥款匯款 (含暫收退還且非退票) 02:支票繳款 03:債協 09:放款
	 * 
	 * @return String
	 */
	public String getSecNo() {
		return this.secNo == null ? "" : this.secNo;
	}

	/**
	 * 業務類別<br>
	 * 01:撥款匯款 (含暫收退還且非退票) 02:支票繳款 03:債協 09:放款
	 *
	 * @param secNo 業務類別
	 */
	public void setSecNo(String secNo) {
		this.secNo = secNo;
	}

	/**
	 * 關帳狀態<br>
	 * 0：開帳 1：關帳 2：關帳取消
	 * 
	 * @return Integer
	 */
	public int getClsFg() {
		return this.clsFg;
	}

	/**
	 * 關帳狀態<br>
	 * 0：開帳 1：關帳 2：關帳取消
	 *
	 * @param clsFg 關帳狀態
	 */
	public void setClsFg(int clsFg) {
		this.clsFg = clsFg;
	}

	/**
	 * 業務批號<br>
	 * 預設為01，關帳後再開帳則＋１
	 * 
	 * @return Integer
	 */
	public int getBatNo() {
		return this.batNo;
	}

	/**
	 * 業務批號<br>
	 * 預設為01，關帳後再開帳則＋１
	 *
	 * @param batNo 業務批號
	 */
	public void setBatNo(int batNo) {
		this.batNo = batNo;
	}

	/**
	 * 業務關帳次數<br>
	 * 預設為00，關帳時+1、關帳取消則-1
	 * 
	 * @return Integer
	 */
	public int getClsNo() {
		return this.clsNo;
	}

	/**
	 * 業務關帳次數<br>
	 * 預設為00，關帳時+1、關帳取消則-1
	 *
	 * @param clsNo 業務關帳次數
	 */
	public void setClsNo(int clsNo) {
		this.clsNo = clsNo;
	}

	/**
	 * 傳票號碼<br>
	 * 每日由1起編
	 * 
	 * @return Integer
	 */
	public int getSlipNo() {
		return this.slipNo;
	}

	/**
	 * 傳票號碼<br>
	 * 每日由1起編
	 *
	 * @param slipNo 傳票號碼
	 */
	public void setSlipNo(int slipNo) {
		this.slipNo = slipNo;
	}

	/**
	 * 上傳核心序號<br>
	 * 只更新特定筆(09:放款) 預設為000，產生上傳媒體(02:支票繳款，09:放款)關帳時＋１
	 * 
	 * @return Integer
	 */
	public int getCoreSeqNo() {
		return this.coreSeqNo;
	}

	/**
	 * 上傳核心序號<br>
	 * 只更新特定筆(09:放款) 預設為000，產生上傳媒體(02:支票繳款，09:放款)關帳時＋１
	 *
	 * @param coreSeqNo 上傳核心序號
	 */
	public void setCoreSeqNo(int coreSeqNo) {
		this.coreSeqNo = coreSeqNo;
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
		return "AcClose [acCloseId=" + acCloseId + ", clsFg=" + clsFg + ", batNo=" + batNo + ", clsNo=" + clsNo + ", slipNo=" + slipNo + ", coreSeqNo=" + coreSeqNo + ", createDate=" + createDate
				+ ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
