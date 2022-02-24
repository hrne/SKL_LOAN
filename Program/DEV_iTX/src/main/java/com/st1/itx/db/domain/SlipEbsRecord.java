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
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * SlipEbsRecord 傳票上傳EBS紀錄檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`SlipEbsRecord`")
public class SlipEbsRecord implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 7914676887631781648L;

// 上傳序號
  @Id
  @Column(name = "`UploadNo`")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "`SlipEbsRecord_SEQ`")
  @SequenceGenerator(name = "`SlipEbsRecord_SEQ`", sequenceName = "`SlipEbsRecord_SEQ`", allocationSize = 1)
  private Long uploadNo = 0L;

  // ETL批號
  /* ETL批號(匯出日期YYYYMMDD+3位流水號) */
  @Column(name = "`GroupId`", length = 11)
  private String groupId;

  // 上傳資料
  @Column(name = "`RequestData`", length = 3000)
  private String requestData;

  // 收到資料
  @Column(name = "`ResultData`", length = 3000)
  private String resultData;

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
	* 上傳序號<br>
	* 
	* @return Long
	*/
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long getUploadNo() {
    return this.uploadNo;
  }

/**
	* 上傳序號<br>
	* 
  *
  * @param uploadNo 上傳序號
	*/
  public void setUploadNo(Long uploadNo) {
    this.uploadNo = uploadNo;
  }

/**
	* ETL批號<br>
	* ETL批號(匯出日期YYYYMMDD+3位流水號)
	* @return String
	*/
  public String getGroupId() {
    return this.groupId == null ? "" : this.groupId;
  }

/**
	* ETL批號<br>
	* ETL批號(匯出日期YYYYMMDD+3位流水號)
  *
  * @param groupId ETL批號
	*/
  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

/**
	* 上傳資料<br>
	* 
	* @return String
	*/
  public String getRequestData() {
    return this.requestData == null ? "" : this.requestData;
  }

/**
	* 上傳資料<br>
	* 
  *
  * @param requestData 上傳資料
	*/
  public void setRequestData(String requestData) {
    this.requestData = requestData;
  }

/**
	* 收到資料<br>
	* 
	* @return String
	*/
  public String getResultData() {
    return this.resultData == null ? "" : this.resultData;
  }

/**
	* 收到資料<br>
	* 
  *
  * @param resultData 收到資料
	*/
  public void setResultData(String resultData) {
    this.resultData = resultData;
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
    return "SlipEbsRecord [uploadNo=" + uploadNo + ", groupId=" + groupId + ", requestData=" + requestData + ", resultData=" + resultData + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
