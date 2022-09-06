package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TxArchiveTableLog 歷史封存表紀錄檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class TxArchiveTableLogId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5050163324965259963L;

// 分類
	/* 5YTX:已結清並領取清償證明五年之交易明細 */
	@Column(name = "`Type`", length = 4)
	private String type = " ";

	// 搬運來源環境
	/* ONLINE:連線環境HISTORY:歷史環境 */
	@Column(name = "`DataFrom`", length = 7)
	private String dataFrom = " ";

	// 搬運目標環境
	/* ONLINE:連線環境HISTORY:歷史環境 */
	@Column(name = "`DataTo`", length = 7)
	private String dataTo = " ";

	// 執行日期
	@Column(name = "`ExecuteDate`")
	private int executeDate = 0;

	// 資料表名稱
	@Column(name = "`TableName`", length = 30)
	private String tableName = " ";

	// 執行批次
	@Column(name = "`BatchNo`")
	private int batchNo = 0;

	// 戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 撥款序號
	@Column(name = "`BormNo`")
	private int bormNo = 0;

	public TxArchiveTableLogId() {
	}

	public TxArchiveTableLogId(String type, String dataFrom, String dataTo, int executeDate, String tableName, int batchNo, int custNo, int facmNo, int bormNo) {
		this.type = type;
		this.dataFrom = dataFrom;
		this.dataTo = dataTo;
		this.executeDate = executeDate;
		this.tableName = tableName;
		this.batchNo = batchNo;
		this.custNo = custNo;
		this.facmNo = facmNo;
		this.bormNo = bormNo;
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

	@Override
	public int hashCode() {
		return Objects.hash(type, dataFrom, dataTo, executeDate, tableName, batchNo, custNo, facmNo, bormNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		TxArchiveTableLogId txArchiveTableLogId = (TxArchiveTableLogId) obj;
		return type.equals(txArchiveTableLogId.type) && dataFrom.equals(txArchiveTableLogId.dataFrom) && dataTo.equals(txArchiveTableLogId.dataTo) && executeDate == txArchiveTableLogId.executeDate
				&& tableName.equals(txArchiveTableLogId.tableName) && batchNo == txArchiveTableLogId.batchNo && custNo == txArchiveTableLogId.custNo && facmNo == txArchiveTableLogId.facmNo
				&& bormNo == txArchiveTableLogId.bormNo;
	}

	@Override
	public String toString() {
		return "TxArchiveTableLogId [type=" + type + ", dataFrom=" + dataFrom + ", dataTo=" + dataTo + ", executeDate=" + executeDate + ", tableName=" + tableName + ", batchNo=" + batchNo
				+ ", custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + "]";
	}
}
