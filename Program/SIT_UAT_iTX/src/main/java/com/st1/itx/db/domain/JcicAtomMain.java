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
 * JcicAtomMain 債務匯入資料功能主檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicAtomMain`")
public class JcicAtomMain implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -8756352532492346652L;

// 功能代碼
  @Id
  @Column(name = "`FunctionCode`", length = 6)
  private String functionCode = " ";

  // 功能類別
  @Column(name = "`DataType`", length = 45)
  private String dataType;

  // 功能說明
  @Column(name = "`Remark`", length = 300)
  private String remark;

  // 查詢點數
  @Column(name = "`SearchPoint`", length = 3)
  private String searchPoint;

  // 輸入鍵值
  @Column(name = "`FunctionKey`", length = 200)
  private String functionKey;

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
	* 功能代碼<br>
	* 
	* @return String
	*/
  public String getFunctionCode() {
    return this.functionCode == null ? "" : this.functionCode;
  }

/**
	* 功能代碼<br>
	* 
  *
  * @param functionCode 功能代碼
	*/
  public void setFunctionCode(String functionCode) {
    this.functionCode = functionCode;
  }

/**
	* 功能類別<br>
	* 
	* @return String
	*/
  public String getDataType() {
    return this.dataType == null ? "" : this.dataType;
  }

/**
	* 功能類別<br>
	* 
  *
  * @param dataType 功能類別
	*/
  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

/**
	* 功能說明<br>
	* 
	* @return String
	*/
  public String getRemark() {
    return this.remark == null ? "" : this.remark;
  }

/**
	* 功能說明<br>
	* 
  *
  * @param remark 功能說明
	*/
  public void setRemark(String remark) {
    this.remark = remark;
  }

/**
	* 查詢點數<br>
	* 
	* @return String
	*/
  public String getSearchPoint() {
    return this.searchPoint == null ? "" : this.searchPoint;
  }

/**
	* 查詢點數<br>
	* 
  *
  * @param searchPoint 查詢點數
	*/
  public void setSearchPoint(String searchPoint) {
    this.searchPoint = searchPoint;
  }

/**
	* 輸入鍵值<br>
	* 
	* @return String
	*/
  public String getFunctionKey() {
    return this.functionKey == null ? "" : this.functionKey;
  }

/**
	* 輸入鍵值<br>
	* 
  *
  * @param functionKey 輸入鍵值
	*/
  public void setFunctionKey(String functionKey) {
    this.functionKey = functionKey;
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
    return "JcicAtomMain [functionCode=" + functionCode + ", dataType=" + dataType + ", remark=" + remark + ", searchPoint=" + searchPoint + ", functionKey=" + functionKey + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
