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
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * ReltMain 借款戶關係人/關係企業主檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`ReltMain`")
public class ReltMain implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 1373285610123497469L;

@EmbeddedId
  private ReltMainId reltMainId;

  // eLoan案件編號
  @Column(name = "`CaseNo`", insertable = false, updatable = false)
  private int caseNo = 0;

  // 借戶人戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 關係人客戶識別碼
  @Column(name = "`ReltUKey`", length = 32, insertable = false, updatable = false)
  private String reltUKey;

  // 關係
  /* 共用代碼檔(CustRelationType)01 本人02 配偶03 祖(外祖)父母04 父母05 兄弟姊妹06 子女07 孫(外孫)子女08 有控制與從屬關係09 相互投資關係10 董事長11 董事12 監察人99 其他 */
  @Column(name = "`ReltCode`", length = 2)
  private String reltCode;

  // 備註類型
  /* 1 持股比例2 被持股比例3 持有股份4 出資額5 關係人9 其它 */
  @Column(name = "`RemarkType`", length = 1)
  private String remarkType;

  // 備註
  @Column(name = "`Reltmark`", length = 100)
  private String reltmark;

  // 最新案件記號
  @Column(name = "`FinalFg`", length = 1)
  private String finalFg;

  // 申請日期
  @Column(name = "`ApplDate`")
  private int applDate = 0;

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


  public ReltMainId getReltMainId() {
    return this.reltMainId;
  }

  public void setReltMainId(ReltMainId reltMainId) {
    this.reltMainId = reltMainId;
  }

/**
	* eLoan案件編號<br>
	* 
	* @return Integer
	*/
  public int getCaseNo() {
    return this.caseNo;
  }

/**
	* eLoan案件編號<br>
	* 
  *
  * @param caseNo eLoan案件編號
	*/
  public void setCaseNo(int caseNo) {
    this.caseNo = caseNo;
  }

/**
	* 借戶人戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 借戶人戶號<br>
	* 
  *
  * @param custNo 借戶人戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 關係人客戶識別碼<br>
	* 
	* @return String
	*/
  public String getReltUKey() {
    return this.reltUKey == null ? "" : this.reltUKey;
  }

/**
	* 關係人客戶識別碼<br>
	* 
  *
  * @param reltUKey 關係人客戶識別碼
	*/
  public void setReltUKey(String reltUKey) {
    this.reltUKey = reltUKey;
  }

/**
	* 關係<br>
	* 共用代碼檔(CustRelationType)
01 本人
02 配偶
03 祖(外祖)父母
04 父母
05 兄弟姊妹
06 子女
07 孫(外孫)子女
08 有控制與從屬關係
09 相互投資關係
10 董事長
11 董事
12 監察人
99 其他
	* @return String
	*/
  public String getReltCode() {
    return this.reltCode == null ? "" : this.reltCode;
  }

/**
	* 關係<br>
	* 共用代碼檔(CustRelationType)
01 本人
02 配偶
03 祖(外祖)父母
04 父母
05 兄弟姊妹
06 子女
07 孫(外孫)子女
08 有控制與從屬關係
09 相互投資關係
10 董事長
11 董事
12 監察人
99 其他
  *
  * @param reltCode 關係
	*/
  public void setReltCode(String reltCode) {
    this.reltCode = reltCode;
  }

/**
	* 備註類型<br>
	* 1 持股比例
2 被持股比例
3 持有股份
4 出資額
5 關係人
9 其它
	* @return String
	*/
  public String getRemarkType() {
    return this.remarkType == null ? "" : this.remarkType;
  }

/**
	* 備註類型<br>
	* 1 持股比例
2 被持股比例
3 持有股份
4 出資額
5 關係人
9 其它
  *
  * @param remarkType 備註類型
	*/
  public void setRemarkType(String remarkType) {
    this.remarkType = remarkType;
  }

/**
	* 備註<br>
	* 
	* @return String
	*/
  public String getReltmark() {
    return this.reltmark == null ? "" : this.reltmark;
  }

/**
	* 備註<br>
	* 
  *
  * @param reltmark 備註
	*/
  public void setReltmark(String reltmark) {
    this.reltmark = reltmark;
  }

/**
	* 最新案件記號<br>
	* 
	* @return String
	*/
  public String getFinalFg() {
    return this.finalFg == null ? "" : this.finalFg;
  }

/**
	* 最新案件記號<br>
	* 
  *
  * @param finalFg 最新案件記號
	*/
  public void setFinalFg(String finalFg) {
    this.finalFg = finalFg;
  }

/**
	* 申請日期<br>
	* 
	* @return Integer
	*/
  public int getApplDate() {
    return StaticTool.bcToRoc(this.applDate);
  }

/**
	* 申請日期<br>
	* 
  *
  * @param applDate 申請日期
  * @throws LogicException when Date Is Warn	*/
  public void setApplDate(int applDate) throws LogicException {
    this.applDate = StaticTool.rocToBc(applDate);
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
    return "ReltMain [reltMainId=" + reltMainId + ", reltCode=" + reltCode + ", remarkType=" + remarkType + ", reltmark=" + reltmark
           + ", finalFg=" + finalFg + ", applDate=" + applDate + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
           + "]";
  }
}
