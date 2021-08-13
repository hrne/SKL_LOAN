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
 * JcicAtomDetail 債務匯入資料功能明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicAtomDetail`")
public class JcicAtomDetail implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 7481761045971028124L;

@EmbeddedId
  private JcicAtomDetailId jcicAtomDetailId;

  // 功能代碼
  @Column(name = "`FunctionCode`", length = 6, insertable = false, updatable = false)
  private String functionCode;

  // 順序
  @Column(name = "`DataOrder`", insertable = false, updatable = false)
  private int dataOrder = 0;

  // 欄位名稱
  @Column(name = "`FiledName`", length = 50)
  private String filedName;

  // 格式與長度
  @Column(name = "`FiledType`", length = 20)
  private String filedType;

  // 中文說明
  @Column(name = "`Remark`", length = 300)
  private String remark;

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


  public JcicAtomDetailId getJcicAtomDetailId() {
    return this.jcicAtomDetailId;
  }

  public void setJcicAtomDetailId(JcicAtomDetailId jcicAtomDetailId) {
    this.jcicAtomDetailId = jcicAtomDetailId;
  }

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
	* 順序<br>
	* 
	* @return Integer
	*/
  public int getDataOrder() {
    return this.dataOrder;
  }

/**
	* 順序<br>
	* 
  *
  * @param dataOrder 順序
	*/
  public void setDataOrder(int dataOrder) {
    this.dataOrder = dataOrder;
  }

/**
	* 欄位名稱<br>
	* 
	* @return String
	*/
  public String getFiledName() {
    return this.filedName == null ? "" : this.filedName;
  }

/**
	* 欄位名稱<br>
	* 
  *
  * @param filedName 欄位名稱
	*/
  public void setFiledName(String filedName) {
    this.filedName = filedName;
  }

/**
	* 格式與長度<br>
	* 
	* @return String
	*/
  public String getFiledType() {
    return this.filedType == null ? "" : this.filedType;
  }

/**
	* 格式與長度<br>
	* 
  *
  * @param filedType 格式與長度
	*/
  public void setFiledType(String filedType) {
    this.filedType = filedType;
  }

/**
	* 中文說明<br>
	* 
	* @return String
	*/
  public String getRemark() {
    return this.remark == null ? "" : this.remark;
  }

/**
	* 中文說明<br>
	* 
  *
  * @param remark 中文說明
	*/
  public void setRemark(String remark) {
    this.remark = remark;
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
    return "JcicAtomDetail [jcicAtomDetailId=" + jcicAtomDetailId + ", filedName=" + filedName + ", filedType=" + filedType + ", remark=" + remark + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
