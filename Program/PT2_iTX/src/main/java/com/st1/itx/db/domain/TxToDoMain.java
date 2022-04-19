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

/**
 * TxToDoMain 應處理清單主檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxToDoMain`")
public class TxToDoMain implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

// 項目
	@Id
	@Column(name = "`ItemCode`", length = 6)
	private String itemCode = " ";

	// 項目中文
	@Column(name = "`ItemDesc`", length = 30)
	private String itemDesc;

	// 前日留存
	/* Y-留存前日未處理資料 */
	@Column(name = "`YdReserveFg`", length = 1)
	private String ydReserveFg;

	// 處理功能
	/* Y-有自動處理功能，執行後明細檔狀態改為已處理C-連結或啟動原交易，執行後明細檔狀態改為已處理M-人工自行處理，明細檔狀態不會變動 */
	@Column(name = "`AutoFg`", length = 1)
	private String autoFg;

	// 刪除功能
	/* Y-有刪除功能 */
	@Column(name = "`DeleteFg`", length = 1)
	private String deleteFg;

	// 保留功能
	/* Y-有保留功能 */
	@Column(name = "`ReserveFg`", length = 1)
	private String reserveFg;

	// 關帳檢核
	/* Y-關帳檢核有未處理則不許關帳 */
	@Column(name = "`AcClsCheck`", length = 1)
	private String acClsCheck;

	// 連結查詢交易
	/* 按&amp;lt;筆數&amp;gt;數字鍵，連結的查詢交易 */
	@Column(name = "`ChainInqTxcd`", length = 5)
	private String chainInqTxcd;

	// 連結處理交易
	/* 按&amp;lt;處理交易&amp;gt;鍵，連結的處理交易 */
	@Column(name = "`ChainUpdTxcd`", length = 5)
	private String chainUpdTxcd;

	// 執行交易
	/* 實際功能的執行交易 */
	@Column(name = "`ExcuteTxcd`", length = 5)
	private String excuteTxcd;

	// 訂正功能
	/* Y-有訂正功能 */
	@Column(name = "`EraseFg`", length = 1)
	private String eraseFg;

	// 昨日留存筆數
	@Column(name = "`YdReserveCnt`")
	private int ydReserveCnt = 0;

	// 本日新增筆數
	@Column(name = "`TdNewCnt`")
	private int tdNewCnt = 0;

	// 本日處理筆數
	@Column(name = "`TdProcessCnt`")
	private int tdProcessCnt = 0;

	// 本日刪除筆數
	@Column(name = "`TdDeleteCNT`")
	private int tdDeleteCNT = 0;

	// 保留筆數
	@Column(name = "`ReserveCnt`")
	private int reserveCnt = 0;

	// 未處理筆數
	@Column(name = "`UnProcessCnt`")
	private int unProcessCnt = 0;

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
	 * 項目<br>
	 * 
	 * @return String
	 */
	public String getItemCode() {
		return this.itemCode == null ? "" : this.itemCode;
	}

	/**
	 * 項目<br>
	 * 
	 *
	 * @param itemCode 項目
	 */
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	/**
	 * 項目中文<br>
	 * 
	 * @return String
	 */
	public String getItemDesc() {
		return this.itemDesc == null ? "" : this.itemDesc;
	}

	/**
	 * 項目中文<br>
	 * 
	 *
	 * @param itemDesc 項目中文
	 */
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	/**
	 * 前日留存<br>
	 * Y-留存前日未處理資料
	 * 
	 * @return String
	 */
	public String getYdReserveFg() {
		return this.ydReserveFg == null ? "" : this.ydReserveFg;
	}

	/**
	 * 前日留存<br>
	 * Y-留存前日未處理資料
	 *
	 * @param ydReserveFg 前日留存
	 */
	public void setYdReserveFg(String ydReserveFg) {
		this.ydReserveFg = ydReserveFg;
	}

	/**
	 * 處理功能<br>
	 * Y-有自動處理功能，執行後明細檔狀態改為已處理 C-連結或啟動原交易，執行後明細檔狀態改為已處理 M-人工自行處理，明細檔狀態不會變動
	 * 
	 * @return String
	 */
	public String getAutoFg() {
		return this.autoFg == null ? "" : this.autoFg;
	}

	/**
	 * 處理功能<br>
	 * Y-有自動處理功能，執行後明細檔狀態改為已處理 C-連結或啟動原交易，執行後明細檔狀態改為已處理 M-人工自行處理，明細檔狀態不會變動
	 *
	 * @param autoFg 處理功能
	 */
	public void setAutoFg(String autoFg) {
		this.autoFg = autoFg;
	}

	/**
	 * 刪除功能<br>
	 * Y-有刪除功能
	 * 
	 * @return String
	 */
	public String getDeleteFg() {
		return this.deleteFg == null ? "" : this.deleteFg;
	}

	/**
	 * 刪除功能<br>
	 * Y-有刪除功能
	 *
	 * @param deleteFg 刪除功能
	 */
	public void setDeleteFg(String deleteFg) {
		this.deleteFg = deleteFg;
	}

	/**
	 * 保留功能<br>
	 * Y-有保留功能
	 * 
	 * @return String
	 */
	public String getReserveFg() {
		return this.reserveFg == null ? "" : this.reserveFg;
	}

	/**
	 * 保留功能<br>
	 * Y-有保留功能
	 *
	 * @param reserveFg 保留功能
	 */
	public void setReserveFg(String reserveFg) {
		this.reserveFg = reserveFg;
	}

	/**
	 * 關帳檢核<br>
	 * Y-關帳檢核有未處理則不許關帳
	 * 
	 * @return String
	 */
	public String getAcClsCheck() {
		return this.acClsCheck == null ? "" : this.acClsCheck;
	}

	/**
	 * 關帳檢核<br>
	 * Y-關帳檢核有未處理則不許關帳
	 *
	 * @param acClsCheck 關帳檢核
	 */
	public void setAcClsCheck(String acClsCheck) {
		this.acClsCheck = acClsCheck;
	}

	/**
	 * 連結查詢交易<br>
	 * 按&amp;lt;筆數&amp;gt;數字鍵，連結的查詢交易
	 * 
	 * @return String
	 */
	public String getChainInqTxcd() {
		return this.chainInqTxcd == null ? "" : this.chainInqTxcd;
	}

	/**
	 * 連結查詢交易<br>
	 * 按&amp;lt;筆數&amp;gt;數字鍵，連結的查詢交易
	 *
	 * @param chainInqTxcd 連結查詢交易
	 */
	public void setChainInqTxcd(String chainInqTxcd) {
		this.chainInqTxcd = chainInqTxcd;
	}

	/**
	 * 連結處理交易<br>
	 * 按&amp;lt;處理交易&amp;gt;鍵，連結的處理交易
	 * 
	 * @return String
	 */
	public String getChainUpdTxcd() {
		return this.chainUpdTxcd == null ? "" : this.chainUpdTxcd;
	}

	/**
	 * 連結處理交易<br>
	 * 按&amp;lt;處理交易&amp;gt;鍵，連結的處理交易
	 *
	 * @param chainUpdTxcd 連結處理交易
	 */
	public void setChainUpdTxcd(String chainUpdTxcd) {
		this.chainUpdTxcd = chainUpdTxcd;
	}

	/**
	 * 執行交易<br>
	 * 實際功能的執行交易
	 * 
	 * @return String
	 */
	public String getExcuteTxcd() {
		return this.excuteTxcd == null ? "" : this.excuteTxcd;
	}

	/**
	 * 執行交易<br>
	 * 實際功能的執行交易
	 *
	 * @param excuteTxcd 執行交易
	 */
	public void setExcuteTxcd(String excuteTxcd) {
		this.excuteTxcd = excuteTxcd;
	}

	/**
	 * 訂正功能<br>
	 * Y-有訂正功能
	 * 
	 * @return String
	 */
	public String getEraseFg() {
		return this.eraseFg == null ? "" : this.eraseFg;
	}

	/**
	 * 訂正功能<br>
	 * Y-有訂正功能
	 *
	 * @param eraseFg 訂正功能
	 */
	public void setEraseFg(String eraseFg) {
		this.eraseFg = eraseFg;
	}

	/**
	 * 昨日留存筆數<br>
	 * 
	 * @return Integer
	 */
	public int getYdReserveCnt() {
		return this.ydReserveCnt;
	}

	/**
	 * 昨日留存筆數<br>
	 * 
	 *
	 * @param ydReserveCnt 昨日留存筆數
	 */
	public void setYdReserveCnt(int ydReserveCnt) {
		this.ydReserveCnt = ydReserveCnt;
	}

	/**
	 * 本日新增筆數<br>
	 * 
	 * @return Integer
	 */
	public int getTdNewCnt() {
		return this.tdNewCnt;
	}

	/**
	 * 本日新增筆數<br>
	 * 
	 *
	 * @param tdNewCnt 本日新增筆數
	 */
	public void setTdNewCnt(int tdNewCnt) {
		this.tdNewCnt = tdNewCnt;
	}

	/**
	 * 本日處理筆數<br>
	 * 
	 * @return Integer
	 */
	public int getTdProcessCnt() {
		return this.tdProcessCnt;
	}

	/**
	 * 本日處理筆數<br>
	 * 
	 *
	 * @param tdProcessCnt 本日處理筆數
	 */
	public void setTdProcessCnt(int tdProcessCnt) {
		this.tdProcessCnt = tdProcessCnt;
	}

	/**
	 * 本日刪除筆數<br>
	 * 
	 * @return Integer
	 */
	public int getTdDeleteCNT() {
		return this.tdDeleteCNT;
	}

	/**
	 * 本日刪除筆數<br>
	 * 
	 *
	 * @param tdDeleteCNT 本日刪除筆數
	 */
	public void setTdDeleteCNT(int tdDeleteCNT) {
		this.tdDeleteCNT = tdDeleteCNT;
	}

	/**
	 * 保留筆數<br>
	 * 
	 * @return Integer
	 */
	public int getReserveCnt() {
		return this.reserveCnt;
	}

	/**
	 * 保留筆數<br>
	 * 
	 *
	 * @param reserveCnt 保留筆數
	 */
	public void setReserveCnt(int reserveCnt) {
		this.reserveCnt = reserveCnt;
	}

	/**
	 * 未處理筆數<br>
	 * 
	 * @return Integer
	 */
	public int getUnProcessCnt() {
		return this.unProcessCnt;
	}

	/**
	 * 未處理筆數<br>
	 * 
	 *
	 * @param unProcessCnt 未處理筆數
	 */
	public void setUnProcessCnt(int unProcessCnt) {
		this.unProcessCnt = unProcessCnt;
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
		return "TxToDoMain [itemCode=" + itemCode + ", itemDesc=" + itemDesc + ", ydReserveFg=" + ydReserveFg + ", autoFg=" + autoFg + ", deleteFg=" + deleteFg + ", reserveFg=" + reserveFg
				+ ", acClsCheck=" + acClsCheck + ", chainInqTxcd=" + chainInqTxcd + ", chainUpdTxcd=" + chainUpdTxcd + ", excuteTxcd=" + excuteTxcd + ", eraseFg=" + eraseFg + ", ydReserveCnt="
				+ ydReserveCnt + ", tdNewCnt=" + tdNewCnt + ", tdProcessCnt=" + tdProcessCnt + ", tdDeleteCNT=" + tdDeleteCNT + ", reserveCnt=" + reserveCnt + ", unProcessCnt=" + unProcessCnt
				+ ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
