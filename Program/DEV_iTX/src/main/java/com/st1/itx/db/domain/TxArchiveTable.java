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
 * TxArchiveTable 歷史封存表設定檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxArchiveTable`")
public class TxArchiveTable implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -8064281921164174016L;

@EmbeddedId
  private TxArchiveTableId txArchiveTableId;

  // 分類
  /* 5YTX: 已結清並領取清償證明五年之交易明細 */
  @Column(name = "`Type`", length = 4, insertable = false, updatable = false)
  private String type;

  // 資料表名稱
  @Column(name = "`TableName`", length = 30, insertable = false, updatable = false)
  private String tableName;

  // 啟用記號
  /* 1:啟用0:停用 */
  @Column(name = "`Enabled`")
  private int enabled = 0;

  // 搬運條件
  /* 1:戶號2:戶號-額度3:戶號-額度-撥款序號 */
  @Column(name = "`Conditions`")
  private int conditions = 0;

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


  public TxArchiveTableId getTxArchiveTableId() {
    return this.txArchiveTableId;
  }

  public void setTxArchiveTableId(TxArchiveTableId txArchiveTableId) {
    this.txArchiveTableId = txArchiveTableId;
  }

/**
	* 分類<br>
	* 5YTX: 已結清並領取清償證明五年之交易明細
	* @return String
	*/
  public String getType() {
    return this.type == null ? "" : this.type;
  }

/**
	* 分類<br>
	* 5YTX: 已結清並領取清償證明五年之交易明細
  *
  * @param type 分類
	*/
  public void setType(String type) {
    this.type = type;
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
	* 啟用記號<br>
	* 1:啟用
0:停用
	* @return Integer
	*/
  public int getEnabled() {
    return this.enabled;
  }

/**
	* 啟用記號<br>
	* 1:啟用
0:停用
  *
  * @param enabled 啟用記號
	*/
  public void setEnabled(int enabled) {
    this.enabled = enabled;
  }

/**
	* 搬運條件<br>
	* 1:戶號
2:戶號-額度
3:戶號-額度-撥款序號
	* @return Integer
	*/
  public int getConditions() {
    return this.conditions;
  }

/**
	* 搬運條件<br>
	* 1:戶號
2:戶號-額度
3:戶號-額度-撥款序號
  *
  * @param conditions 搬運條件
	*/
  public void setConditions(int conditions) {
    this.conditions = conditions;
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
    return "TxArchiveTable [txArchiveTableId=" + txArchiveTableId + ", enabled=" + enabled + ", conditions=" + conditions + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
