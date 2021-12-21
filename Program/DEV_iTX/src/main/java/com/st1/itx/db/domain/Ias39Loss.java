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
 * Ias39Loss 特殊客觀減損狀況檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`Ias39Loss`")
public class Ias39Loss implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8991295825134026401L;

	@EmbeddedId
	private Ias39LossId ias39LossId;

	// 戶號
	@Column(name = "`CustNo`", insertable = false, updatable = false)
	private int custNo = 0;

	// 額度編號
	@Column(name = "`FacmNo`", insertable = false, updatable = false)
	private int facmNo = 0;

	// 發生日期
	@Column(name = "`MarkDate`", insertable = false, updatable = false)
	private int markDate = 0;

	// 註記碼
	/* 1重整2破產9其他 */
	@Column(name = "`MarkCode`")
	private int markCode = 0;

	// 原因代碼
	/* 01個別評價99其他 add 2021/12/2 */
	@Column(name = "`MarkRsn`")
	private int markRsn = 0;

	// 原因說明
	/* add 2021/12/2 */
	@Column(name = "`MarkRsnDesc`", length = 500)
	private String markRsnDesc;

	// 客觀減損條件
	/* 1~5, add 2021/12/2 */
	@Column(name = "`LosCod`")
	private int losCod = 0;

	// 起始日期
	@Column(name = "`StartDate`")
	private int startDate = 0;

	// 終止日期
	@Column(name = "`EndDate`")
	private int endDate = 0;

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

	public Ias39LossId getIas39LossId() {
		return this.ias39LossId;
	}

	public void setIas39LossId(Ias39LossId ias39LossId) {
		this.ias39LossId = ias39LossId;
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
	 * 額度編號<br>
	 * 
	 * @return Integer
	 */
	public int getFacmNo() {
		return this.facmNo;
	}

	/**
	 * 額度編號<br>
	 * 
	 *
	 * @param facmNo 額度編號
	 */
	public void setFacmNo(int facmNo) {
		this.facmNo = facmNo;
	}

	/**
	 * 發生日期<br>
	 * 
	 * @return Integer
	 */
	public int getMarkDate() {
		return StaticTool.bcToRoc(this.markDate);
	}

	/**
	 * 發生日期<br>
	 * 
	 *
	 * @param markDate 發生日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setMarkDate(int markDate) throws LogicException {
		this.markDate = StaticTool.rocToBc(markDate);
	}

	/**
	 * 註記碼<br>
	 * 1重整2破產9其他
	 * 
	 * @return Integer
	 */
	public int getMarkCode() {
		return this.markCode;
	}

	/**
	 * 註記碼<br>
	 * 1重整2破產9其他
	 *
	 * @param markCode 註記碼
	 */
	public void setMarkCode(int markCode) {
		this.markCode = markCode;
	}

	/**
	 * 原因代碼<br>
	 * 01個別評價99其他 add 2021/12/2
	 * 
	 * @return Integer
	 */
	public int getMarkRsn() {
		return this.markRsn;
	}

	/**
	 * 原因代碼<br>
	 * 01個別評價99其他 add 2021/12/2
	 *
	 * @param markRsn 原因代碼
	 */
	public void setMarkRsn(int markRsn) {
		this.markRsn = markRsn;
	}

	/**
	 * 原因說明<br>
	 * add 2021/12/2
	 * 
	 * @return String
	 */
	public String getMarkRsnDesc() {
		return this.markRsnDesc == null ? "" : this.markRsnDesc;
	}

	/**
	 * 原因說明<br>
	 * add 2021/12/2
	 *
	 * @param markRsnDesc 原因說明
	 */
	public void setMarkRsnDesc(String markRsnDesc) {
		this.markRsnDesc = markRsnDesc;
	}

	/**
	 * 客觀減損條件<br>
	 * 1~5, add 2021/12/2
	 * 
	 * @return Integer
	 */
	public int getLosCod() {
		return this.losCod;
	}

	/**
	 * 客觀減損條件<br>
	 * 1~5, add 2021/12/2
	 *
	 * @param losCod 客觀減損條件
	 */
	public void setLosCod(int losCod) {
		this.losCod = losCod;
	}

	/**
	 * 起始日期<br>
	 * 
	 * @return Integer
	 */
	public int getStartDate() {
		return StaticTool.bcToRoc(this.startDate);
	}

	/**
	 * 起始日期<br>
	 * 
	 *
	 * @param startDate 起始日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setStartDate(int startDate) throws LogicException {
		this.startDate = StaticTool.rocToBc(startDate);
	}

	/**
	 * 終止日期<br>
	 * 
	 * @return Integer
	 */
	public int getEndDate() {
		return StaticTool.bcToRoc(this.endDate);
	}

	/**
	 * 終止日期<br>
	 * 
	 *
	 * @param endDate 終止日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setEndDate(int endDate) throws LogicException {
		this.endDate = StaticTool.rocToBc(endDate);
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
		return "Ias39Loss [ias39LossId=" + ias39LossId + ", markCode=" + markCode + ", markRsn=" + markRsn + ", markRsnDesc=" + markRsnDesc + ", losCod=" + losCod + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
