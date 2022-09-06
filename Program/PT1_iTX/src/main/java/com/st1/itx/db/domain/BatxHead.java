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
 * BatxHead 整批入帳總數檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`BatxHead`")
public class BatxHead implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2578067811465026616L;

	@EmbeddedId
	private BatxHeadId batxHeadId;

	// 會計日期
	@Column(name = "`AcDate`", insertable = false, updatable = false)
	private int acDate = 0;

	// 批號
	/* BATX01、02… */
	@Column(name = "`BatchNo`", length = 6, insertable = false, updatable = false)
	private String batchNo;

	// 總金額
	@Column(name = "`BatxTotAmt`")
	private BigDecimal batxTotAmt = new BigDecimal("0");

	// 總筆數
	@Column(name = "`BatxTotCnt`")
	private int batxTotCnt = 0;

	// 未完筆數
	@Column(name = "`UnfinishCnt`")
	private int unfinishCnt = 0;

	// 作業狀態
	/* CdCode.BatchStatus0:待檢核1:檢核有誤2:檢核正常3:入帳未完4:入帳完成8:已刪除 */
	@Column(name = "`BatxExeCode`", length = 1)
	private String batxExeCode;

	// 整批作業狀態
	/* 0:正常1:整批處理中2.已整批訂正 */
	@Column(name = "`BatxStsCode`", length = 1)
	private String batxStsCode;

	// 經辦
	@Column(name = "`TitaTlrNo`", length = 6)
	private String titaTlrNo;

	// 交易代號
	/* L4200,L4210,L4450,BS020 */
	@Column(name = "`TitaTxCd`", length = 5)
	private String titaTxCd;

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

	public BatxHeadId getBatxHeadId() {
		return this.batxHeadId;
	}

	public void setBatxHeadId(BatxHeadId batxHeadId) {
		this.batxHeadId = batxHeadId;
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
	 * 批號<br>
	 * BATX01、02…
	 * 
	 * @return String
	 */
	public String getBatchNo() {
		return this.batchNo == null ? "" : this.batchNo;
	}

	/**
	 * 批號<br>
	 * BATX01、02…
	 *
	 * @param batchNo 批號
	 */
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	/**
	 * 總金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getBatxTotAmt() {
		return this.batxTotAmt;
	}

	/**
	 * 總金額<br>
	 * 
	 *
	 * @param batxTotAmt 總金額
	 */
	public void setBatxTotAmt(BigDecimal batxTotAmt) {
		this.batxTotAmt = batxTotAmt;
	}

	/**
	 * 總筆數<br>
	 * 
	 * @return Integer
	 */
	public int getBatxTotCnt() {
		return this.batxTotCnt;
	}

	/**
	 * 總筆數<br>
	 * 
	 *
	 * @param batxTotCnt 總筆數
	 */
	public void setBatxTotCnt(int batxTotCnt) {
		this.batxTotCnt = batxTotCnt;
	}

	/**
	 * 未完筆數<br>
	 * 
	 * @return Integer
	 */
	public int getUnfinishCnt() {
		return this.unfinishCnt;
	}

	/**
	 * 未完筆數<br>
	 * 
	 *
	 * @param unfinishCnt 未完筆數
	 */
	public void setUnfinishCnt(int unfinishCnt) {
		this.unfinishCnt = unfinishCnt;
	}

	/**
	 * 作業狀態<br>
	 * CdCode.BatchStatus 0:待檢核 1:檢核有誤 2:檢核正常 3:入帳未完 4:入帳完成 8:已刪除
	 * 
	 * @return String
	 */
	public String getBatxExeCode() {
		return this.batxExeCode == null ? "" : this.batxExeCode;
	}

	/**
	 * 作業狀態<br>
	 * CdCode.BatchStatus 0:待檢核 1:檢核有誤 2:檢核正常 3:入帳未完 4:入帳完成 8:已刪除
	 *
	 * @param batxExeCode 作業狀態
	 */
	public void setBatxExeCode(String batxExeCode) {
		this.batxExeCode = batxExeCode;
	}

	/**
	 * 整批作業狀態<br>
	 * 0:正常 1:整批處理中 2.已整批訂正
	 * 
	 * @return String
	 */
	public String getBatxStsCode() {
		return this.batxStsCode == null ? "" : this.batxStsCode;
	}

	/**
	 * 整批作業狀態<br>
	 * 0:正常 1:整批處理中 2.已整批訂正
	 *
	 * @param batxStsCode 整批作業狀態
	 */
	public void setBatxStsCode(String batxStsCode) {
		this.batxStsCode = batxStsCode;
	}

	/**
	 * 經辦<br>
	 * 
	 * @return String
	 */
	public String getTitaTlrNo() {
		return this.titaTlrNo == null ? "" : this.titaTlrNo;
	}

	/**
	 * 經辦<br>
	 * 
	 *
	 * @param titaTlrNo 經辦
	 */
	public void setTitaTlrNo(String titaTlrNo) {
		this.titaTlrNo = titaTlrNo;
	}

	/**
	 * 交易代號<br>
	 * L4200,L4210,L4450,BS020
	 * 
	 * @return String
	 */
	public String getTitaTxCd() {
		return this.titaTxCd == null ? "" : this.titaTxCd;
	}

	/**
	 * 交易代號<br>
	 * L4200,L4210,L4450,BS020
	 *
	 * @param titaTxCd 交易代號
	 */
	public void setTitaTxCd(String titaTxCd) {
		this.titaTxCd = titaTxCd;
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
		return "BatxHead [batxHeadId=" + batxHeadId + ", batxTotAmt=" + batxTotAmt + ", batxTotCnt=" + batxTotCnt + ", unfinishCnt=" + unfinishCnt + ", batxExeCode=" + batxExeCode + ", batxStsCode="
				+ batxStsCode + ", titaTlrNo=" + titaTlrNo + ", titaTxCd=" + titaTxCd + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
				+ ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
