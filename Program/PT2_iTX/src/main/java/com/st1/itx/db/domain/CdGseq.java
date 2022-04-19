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
 * CdGseq 編號編碼檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdGseq`")
public class CdGseq implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 3411840878158705775L;

@EmbeddedId
  private CdGseqId cdGseqId;

  // 編號日期
  /* 年度編號時月日為0，月份編號時日為0 */
  @Column(name = "`GseqDate`", insertable = false, updatable = false)
  private int gseqDate = 0;

  // 編號方式
  /* 0:不分1:年度編號2:月份編號3:日編號 */
  @Column(name = "`GseqCode`", insertable = false, updatable = false)
  private int gseqCode = 0;

  // 業務類別
  /* 業務自行編制   例：L2-業務作業 */
  @Column(name = "`GseqType`", length = 2, insertable = false, updatable = false)
  private String gseqType;

  // 交易種類
  /* 業務自行編制   例：GseqType="L2"0001:戶號0002:案件申請編號 */
  @Column(name = "`GseqKind`", length = 4, insertable = false, updatable = false)
  private String gseqKind;

  // 有效值
  /* 例：有效值=999 , 流水號999時 , 下一個為001 */
  @Column(name = "`Offset`")
  private int offset = 0;

  // 流水號
  @Column(name = "`SeqNo`")
  private int seqNo = 0;

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


  public CdGseqId getCdGseqId() {
    return this.cdGseqId;
  }

  public void setCdGseqId(CdGseqId cdGseqId) {
    this.cdGseqId = cdGseqId;
  }

/**
	* 編號日期<br>
	* 年度編號時月日為0，月份編號時日為0
	* @return Integer
	*/
  public int getGseqDate() {
    return this.gseqDate;
  }

/**
	* 編號日期<br>
	* 年度編號時月日為0，月份編號時日為0
  *
  * @param gseqDate 編號日期
	*/
  public void setGseqDate(int gseqDate) {
    this.gseqDate = gseqDate;
  }

/**
	* 編號方式<br>
	* 0:不分
1:年度編號
2:月份編號
3:日編號
	* @return Integer
	*/
  public int getGseqCode() {
    return this.gseqCode;
  }

/**
	* 編號方式<br>
	* 0:不分
1:年度編號
2:月份編號
3:日編號
  *
  * @param gseqCode 編號方式
	*/
  public void setGseqCode(int gseqCode) {
    this.gseqCode = gseqCode;
  }

/**
	* 業務類別<br>
	* 業務自行編制   例：L2-業務作業
	* @return String
	*/
  public String getGseqType() {
    return this.gseqType == null ? "" : this.gseqType;
  }

/**
	* 業務類別<br>
	* 業務自行編制   例：L2-業務作業
  *
  * @param gseqType 業務類別
	*/
  public void setGseqType(String gseqType) {
    this.gseqType = gseqType;
  }

/**
	* 交易種類<br>
	* 業務自行編制   
例：
GseqType="L2"
0001:戶號
0002:案件申請編號
	* @return String
	*/
  public String getGseqKind() {
    return this.gseqKind == null ? "" : this.gseqKind;
  }

/**
	* 交易種類<br>
	* 業務自行編制   
例：
GseqType="L2"
0001:戶號
0002:案件申請編號
  *
  * @param gseqKind 交易種類
	*/
  public void setGseqKind(String gseqKind) {
    this.gseqKind = gseqKind;
  }

/**
	* 有效值<br>
	* 例：有效值=999 , 流水號999時 , 下一個為001
	* @return Integer
	*/
  public int getOffset() {
    return this.offset;
  }

/**
	* 有效值<br>
	* 例：有效值=999 , 流水號999時 , 下一個為001
  *
  * @param offset 有效值
	*/
  public void setOffset(int offset) {
    this.offset = offset;
  }

/**
	* 流水號<br>
	* 
	* @return Integer
	*/
  public int getSeqNo() {
    return this.seqNo;
  }

/**
	* 流水號<br>
	* 
  *
  * @param seqNo 流水號
	*/
  public void setSeqNo(int seqNo) {
    this.seqNo = seqNo;
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
    return "CdGseq [cdGseqId=" + cdGseqId + ", offset=" + offset + ", seqNo=" + seqNo
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
