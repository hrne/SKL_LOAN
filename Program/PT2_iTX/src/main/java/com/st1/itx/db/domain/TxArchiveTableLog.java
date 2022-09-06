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

/**
 * TxArchiveTableLog 歷史封存表紀錄檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxArchiveTableLog`")
public class TxArchiveTableLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4326021505569836973L;

	@EmbeddedId
	private TxArchiveTableLogId txArchiveTableLogId;

	// 分類
	/* 5YTX:已結清並領取清償證明五年之交易明細 */
	@Column(name = "`Type`", length = 4, insertable = false, updatable = false)
	private String type;

	// 搬運來源環境
	/* ONLINE:連線環境HISTORY:歷史環境 */
	@Column(name = "`DataFrom`", length = 7, insertable = false, updatable = false)
	private String dataFrom;

	// 搬運目標環境
	/* ONLINE:連線環境HISTORY:歷史環境 */
	@Column(name = "`DataTo`", length = 7, insertable = false, updatable = false)
	private String dataTo;

	// 執行日期
	@Column(name = "`ExecuteDate`", insertable = false, updatable = false)
	private int executeDate = 0;

	// 資料表名稱
	@Column(name = "`TableName`", length = 30, insertable = false, updatable = false)
	private String tableName;

	// 執行批次
	@Column(name = "`BatchNo`", insertable = false, updatable = false)
	private int batchNo = 0;

	// 是否成功
	/* 1:是0:否 */
	@Column(name = "`Result`")
	private int result = 0;

	// 戶號
	@Column(name = "`CustNo`", insertable = false, updatable = false)
	private int custNo = 0;

	// 額度
	@Column(name = "`FacmNo`", insertable = false, updatable = false)
	private int facmNo = 0;

	// 撥款序號
	@Column(name = "`BormNo`", insertable = false, updatable = false)
	private int bormNo = 0;

	// 執行結果說明
	@Column(name = "`Description`", length = 200)
	private String description;

	// 搬運資料筆數
	@Column(name = "`Records`")
	private int records = 0;

	// 已自連線環境刪除
	/* 1:是0:否 */
	@Column(name = "`IsDeleted`")
	private int isDeleted = 0;

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

	public TxArchiveTableLogId getTxArchiveTableLogId() {
		return this.txArchiveTableLogId;
	}

	public void setTxArchiveTableLogId(TxArchiveTableLogId txArchiveTableLogId) {
		this.txArchiveTableLogId = txArchiveTableLogId;
	}

	/**
	 * 分類<br>
	 * 5YTX:已結清並領取清償證明五年之交易明細
	 * 
	 * @return String
	 */
	public String getType() {
		return this.type == null ? "" : this.type;
	}

	/**
	 * 分類<br>
	 * 5YTX:已結清並領取清償證明五年之交易明細
	 *
	 * @param type 分類
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 搬運來源環境<br>
	 * ONLINE:連線環境 HISTORY:歷史環境
	 * 
	 * @return String
	 */
	public String getDataFrom() {
		return this.dataFrom == null ? "" : this.dataFrom;
	}

	/**
	 * 搬運來源環境<br>
	 * ONLINE:連線環境 HISTORY:歷史環境
	 *
	 * @param dataFrom 搬運來源環境
	 */
	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}

	/**
	 * 搬運目標環境<br>
	 * ONLINE:連線環境 HISTORY:歷史環境
	 * 
	 * @return String
	 */
	public String getDataTo() {
		return this.dataTo == null ? "" : this.dataTo;
	}

	/**
	 * 搬運目標環境<br>
	 * ONLINE:連線環境 HISTORY:歷史環境
	 *
	 * @param dataTo 搬運目標環境
	 */
	public void setDataTo(String dataTo) {
		this.dataTo = dataTo;
	}

	/**
	 * 執行日期<br>
	 * 
	 * @return Integer
	 */
	public int getExecuteDate() {
		return this.executeDate;
	}

	/**
	 * 執行日期<br>
	 * 
	 *
	 * @param executeDate 執行日期
	 */
	public void setExecuteDate(int executeDate) {
		this.executeDate = executeDate;
	}

	/**
	 * 資料表名稱<br>
	 * 
	 * @return String
	 */
	public String getTableName() {
		return this.tableName == null ? "" : this.tableName;
	}

	/**
	 * 資料表名稱<br>
	 * 
	 *
	 * @param tableName 資料表名稱
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * 執行批次<br>
	 * 
	 * @return Integer
	 */
	public int getBatchNo() {
		return this.batchNo;
	}

	/**
	 * 執行批次<br>
	 * 
	 *
	 * @param batchNo 執行批次
	 */
	public void setBatchNo(int batchNo) {
		this.batchNo = batchNo;
	}

	/**
	 * 是否成功<br>
	 * 1:是 0:否
	 * 
	 * @return Integer
	 */
	public int getResult() {
		return this.result;
	}

	/**
	 * 是否成功<br>
	 * 1:是 0:否
	 *
	 * @param result 是否成功
	 */
	public void setResult(int result) {
		this.result = result;
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
	 * 
	 * @return Integer
	 */
	public int getFacmNo() {
		return this.facmNo;
	}

	/**
	 * 額度<br>
	 * 
	 *
	 * @param facmNo 額度
	 */
	public void setFacmNo(int facmNo) {
		this.facmNo = facmNo;
	}

	/**
	 * 撥款序號<br>
	 * 
	 * @return Integer
	 */
	public int getBormNo() {
		return this.bormNo;
	}

	/**
	 * 撥款序號<br>
	 * 
	 *
	 * @param bormNo 撥款序號
	 */
	public void setBormNo(int bormNo) {
		this.bormNo = bormNo;
	}

	/**
	 * 執行結果說明<br>
	 * 
	 * @return String
	 */
	public String getDescription() {
		return this.description == null ? "" : this.description;
	}

	/**
	 * 執行結果說明<br>
	 * 
	 *
	 * @param description 執行結果說明
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 搬運資料筆數<br>
	 * 
	 * @return Integer
	 */
	public int getRecords() {
		return this.records;
	}

	/**
	 * 搬運資料筆數<br>
	 * 
	 *
	 * @param records 搬運資料筆數
	 */
	public void setRecords(int records) {
		this.records = records;
	}

	/**
	 * 已自連線環境刪除<br>
	 * 1:是 0:否
	 * 
	 * @return Integer
	 */
	public int getIsDeleted() {
		return this.isDeleted;
	}

	/**
	 * 已自連線環境刪除<br>
	 * 1:是 0:否
	 *
	 * @param isDeleted 已自連線環境刪除
	 */
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
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
		return "TxArchiveTableLog [txArchiveTableLogId=" + txArchiveTableLogId + ", result=" + result + ", description=" + description + ", records=" + records + ", isDeleted=" + isDeleted
				+ ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
