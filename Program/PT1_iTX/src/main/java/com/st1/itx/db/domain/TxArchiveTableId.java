package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TxArchiveTable 歷史封存表設定檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class TxArchiveTableId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -4326972753657010148L;

// 分類
  /* 5YTX: 已結清並領取清償證明五年之交易明細 */
  @Column(name = "`Type`", length = 4)
  private String type = " ";

  // 資料表名稱
  @Column(name = "`TableName`", length = 30)
  private String tableName = " ";

  public TxArchiveTableId() {
  }

  public TxArchiveTableId(String type, String tableName) {
    this.type = type;
    this.tableName = tableName;
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


  @Override
  public int hashCode() {
    return Objects.hash(type, tableName);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    TxArchiveTableId txArchiveTableId = (TxArchiveTableId) obj;
    return type.equals(txArchiveTableId.type) && tableName.equals(txArchiveTableId.tableName);
  }

  @Override
  public String toString() {
    return "TxArchiveTableId [type=" + type + ", tableName=" + tableName + "]";
  }
}
