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
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * TxFile 輸出檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxFile`")
public class TxFile implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -543688493705681173L;

// 檔案序號
  @Id
  @Column(name = "`FileNo`")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "`TxFile_SEQ`")
  @SequenceGenerator(name = "`TxFile_SEQ`", sequenceName = "`TxFile_SEQ`", allocationSize = 1)
  private Long fileNo = 0L;

  // 檔案日期
  @Column(name = "`FileDate`")
  private int fileDate = 0;

  // 檔案編號
  @Column(name = "`FileCode`", length = 40)
  private String fileCode;

  // 檔案名稱
  @Column(name = "`FileItem`", length = 80)
  private String fileItem;

  // 檔案型態
  /* 1.PDF2.EXCEL3.TXT4.DBF5.CSV6.套印 */
  @Column(name = "`FileType`")
  private int fileType = 0;

  // 輸出檔名
  @Column(name = "`FileOutput`", length = 80)
  private String fileOutput;

  // 輸出格式
  /* 1.Utf8 2.Big5 */
  @Column(name = "`FileFormat`")
  private int fileFormat = 0;

  // 檔案內容
  @Column(name = "`FileData`", length = 3000)
  private String fileData;

  // 單位
  @Column(name = "`BrNo`", length = 4)
  private String brNo;

  // 簽核記號
  /* 0.不需簽核 1.需簽核 */
  @Column(name = "`SignCode`", length = 1)
  private String signCode;

  // 簽核科組別
  @Column(name = "`GroupNo`", length = 1)
  private String groupNo;

  // 簽核經辦
  @Column(name = "`TlrNo`", length = 6)
  private String tlrNo;

  // 簽核主管
  @Column(name = "`SupNo`", length = 6)
  private String supNo;

  // 批號
  @Column(name = "`BatchNo`", length = 50)
  private String batchNo;

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
	* 檔案序號<br>
	* 
	* @return Long
	*/
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long getFileNo() {
    return this.fileNo;
  }

/**
	* 檔案序號<br>
	* 
  *
  * @param fileNo 檔案序號
	*/
  public void setFileNo(Long fileNo) {
    this.fileNo = fileNo;
  }

/**
	* 檔案日期<br>
	* 
	* @return Integer
	*/
  public int getFileDate() {
    return StaticTool.bcToRoc(this.fileDate);
  }

/**
	* 檔案日期<br>
	* 
  *
  * @param fileDate 檔案日期
  * @throws LogicException when Date Is Warn	*/
  public void setFileDate(int fileDate) throws LogicException {
    this.fileDate = StaticTool.rocToBc(fileDate);
  }

/**
	* 檔案編號<br>
	* 
	* @return String
	*/
  public String getFileCode() {
    return this.fileCode == null ? "" : this.fileCode;
  }

/**
	* 檔案編號<br>
	* 
  *
  * @param fileCode 檔案編號
	*/
  public void setFileCode(String fileCode) {
    this.fileCode = fileCode;
  }

/**
	* 檔案名稱<br>
	* 
	* @return String
	*/
  public String getFileItem() {
    return this.fileItem == null ? "" : this.fileItem;
  }

/**
	* 檔案名稱<br>
	* 
  *
  * @param fileItem 檔案名稱
	*/
  public void setFileItem(String fileItem) {
    this.fileItem = fileItem;
  }

/**
	* 檔案型態<br>
	* 1.PDF
2.EXCEL
3.TXT
4.DBF
5.CSV
6.套印
	* @return Integer
	*/
  public int getFileType() {
    return this.fileType;
  }

/**
	* 檔案型態<br>
	* 1.PDF
2.EXCEL
3.TXT
4.DBF
5.CSV
6.套印
  *
  * @param fileType 檔案型態
	*/
  public void setFileType(int fileType) {
    this.fileType = fileType;
  }

/**
	* 輸出檔名<br>
	* 
	* @return String
	*/
  public String getFileOutput() {
    return this.fileOutput == null ? "" : this.fileOutput;
  }

/**
	* 輸出檔名<br>
	* 
  *
  * @param fileOutput 輸出檔名
	*/
  public void setFileOutput(String fileOutput) {
    this.fileOutput = fileOutput;
  }

/**
	* 輸出格式<br>
	* 1.Utf8 2.Big5
	* @return Integer
	*/
  public int getFileFormat() {
    return this.fileFormat;
  }

/**
	* 輸出格式<br>
	* 1.Utf8 2.Big5
  *
  * @param fileFormat 輸出格式
	*/
  public void setFileFormat(int fileFormat) {
    this.fileFormat = fileFormat;
  }

/**
	* 檔案內容<br>
	* 
	* @return String
	*/
  public String getFileData() {
    return this.fileData == null ? "" : this.fileData;
  }

/**
	* 檔案內容<br>
	* 
  *
  * @param fileData 檔案內容
	*/
  public void setFileData(String fileData) {
    this.fileData = fileData;
  }

/**
	* 單位<br>
	* 
	* @return String
	*/
  public String getBrNo() {
    return this.brNo == null ? "" : this.brNo;
  }

/**
	* 單位<br>
	* 
  *
  * @param brNo 單位
	*/
  public void setBrNo(String brNo) {
    this.brNo = brNo;
  }

/**
	* 簽核記號<br>
	* 0.不需簽核 1.需簽核
	* @return String
	*/
  public String getSignCode() {
    return this.signCode == null ? "" : this.signCode;
  }

/**
	* 簽核記號<br>
	* 0.不需簽核 1.需簽核
  *
  * @param signCode 簽核記號
	*/
  public void setSignCode(String signCode) {
    this.signCode = signCode;
  }

/**
	* 簽核科組別<br>
	* 
	* @return String
	*/
  public String getGroupNo() {
    return this.groupNo == null ? "" : this.groupNo;
  }

/**
	* 簽核科組別<br>
	* 
  *
  * @param groupNo 簽核科組別
	*/
  public void setGroupNo(String groupNo) {
    this.groupNo = groupNo;
  }

/**
	* 簽核經辦<br>
	* 
	* @return String
	*/
  public String getTlrNo() {
    return this.tlrNo == null ? "" : this.tlrNo;
  }

/**
	* 簽核經辦<br>
	* 
  *
  * @param tlrNo 簽核經辦
	*/
  public void setTlrNo(String tlrNo) {
    this.tlrNo = tlrNo;
  }

/**
	* 簽核主管<br>
	* 
	* @return String
	*/
  public String getSupNo() {
    return this.supNo == null ? "" : this.supNo;
  }

/**
	* 簽核主管<br>
	* 
  *
  * @param supNo 簽核主管
	*/
  public void setSupNo(String supNo) {
    this.supNo = supNo;
  }

/**
	* 批號<br>
	* 
	* @return String
	*/
  public String getBatchNo() {
    return this.batchNo == null ? "" : this.batchNo;
  }

/**
	* 批號<br>
	* 
  *
  * @param batchNo 批號
	*/
  public void setBatchNo(String batchNo) {
    this.batchNo = batchNo;
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
    return "TxFile [fileNo=" + fileNo + ", fileDate=" + fileDate + ", fileCode=" + fileCode + ", fileItem=" + fileItem + ", fileType=" + fileType + ", fileOutput=" + fileOutput
           + ", fileFormat=" + fileFormat + ", fileData=" + fileData + ", brNo=" + brNo + ", signCode=" + signCode + ", groupNo=" + groupNo + ", tlrNo=" + tlrNo
           + ", supNo=" + supNo + ", batchNo=" + batchNo + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
           + "]";
  }
}
