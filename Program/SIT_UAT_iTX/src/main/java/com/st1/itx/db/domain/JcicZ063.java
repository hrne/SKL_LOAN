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
 * JcicZ063 變更還款方案結案通知資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ063`")
public class JcicZ063 implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -5399277839699033662L;

@EmbeddedId
  private JcicZ063Id jcicZ063Id;

  // 交易代碼
  /* A:新增;C:異動 */
  @Column(name = "`TranKey`", length = 1)
  private String tranKey;

  // 債務人IDN
  @Column(name = "`CustId`", length = 10, insertable = false, updatable = false)
  private String custId;

  // 報送單位代號
  /* 三位文數字 */
  @Column(name = "`SubmitKey`", length = 3, insertable = false, updatable = false)
  private String submitKey;

  // 原前置協商申請日
  @Column(name = "`RcDate`", insertable = false, updatable = false)
  private int rcDate = 0;

  // 申請變更還款條件日
  @Column(name = "`ChangePayDate`", insertable = false, updatable = false)
  private int changePayDate = 0;

  // 變更還款條件結案日期
  @Column(name = "`ClosedDate`", insertable = false, updatable = false)
  private int closedDate = 0;

  // 結案原因
  /* A:資料key值報送錯誤，本行結案;B:協商不成立;C:更新變更還款條件 */
  @Column(name = "`ClosedResult`", length = 1)
  private String closedResult;

  // 轉出JCIC文字檔日期
  @Column(name = "`OutJcicTxtDate`")
  private int outJcicTxtDate = 0;

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


  public JcicZ063Id getJcicZ063Id() {
    return this.jcicZ063Id;
  }

  public void setJcicZ063Id(JcicZ063Id jcicZ063Id) {
    this.jcicZ063Id = jcicZ063Id;
  }

/**
	* 交易代碼<br>
	* A:新增;C:異動
	* @return String
	*/
  public String getTranKey() {
    return this.tranKey == null ? "" : this.tranKey;
  }

/**
	* 交易代碼<br>
	* A:新增;C:異動
  *
  * @param tranKey 交易代碼
	*/
  public void setTranKey(String tranKey) {
    this.tranKey = tranKey;
  }

/**
	* 債務人IDN<br>
	* 
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 債務人IDN<br>
	* 
  *
  * @param custId 債務人IDN
	*/
  public void setCustId(String custId) {
    this.custId = custId;
  }

/**
	* 報送單位代號<br>
	* 三位文數字
	* @return String
	*/
  public String getSubmitKey() {
    return this.submitKey == null ? "" : this.submitKey;
  }

/**
	* 報送單位代號<br>
	* 三位文數字
  *
  * @param submitKey 報送單位代號
	*/
  public void setSubmitKey(String submitKey) {
    this.submitKey = submitKey;
  }

/**
	* 原前置協商申請日<br>
	* 
	* @return Integer
	*/
  public int getRcDate() {
    return StaticTool.bcToRoc(this.rcDate);
  }

/**
	* 原前置協商申請日<br>
	* 
  *
  * @param rcDate 原前置協商申請日
  * @throws LogicException when Date Is Warn	*/
  public void setRcDate(int rcDate) throws LogicException {
    this.rcDate = StaticTool.rocToBc(rcDate);
  }

/**
	* 申請變更還款條件日<br>
	* 
	* @return Integer
	*/
  public int getChangePayDate() {
    return StaticTool.bcToRoc(this.changePayDate);
  }

/**
	* 申請變更還款條件日<br>
	* 
  *
  * @param changePayDate 申請變更還款條件日
  * @throws LogicException when Date Is Warn	*/
  public void setChangePayDate(int changePayDate) throws LogicException {
    this.changePayDate = StaticTool.rocToBc(changePayDate);
  }

/**
	* 變更還款條件結案日期<br>
	* 
	* @return Integer
	*/
  public int getClosedDate() {
    return StaticTool.bcToRoc(this.closedDate);
  }

/**
	* 變更還款條件結案日期<br>
	* 
  *
  * @param closedDate 變更還款條件結案日期
  * @throws LogicException when Date Is Warn	*/
  public void setClosedDate(int closedDate) throws LogicException {
    this.closedDate = StaticTool.rocToBc(closedDate);
  }

/**
	* 結案原因<br>
	* A:資料key值報送錯誤，本行結案;B:協商不成立;C:更新變更還款條件
	* @return String
	*/
  public String getClosedResult() {
    return this.closedResult == null ? "" : this.closedResult;
  }

/**
	* 結案原因<br>
	* A:資料key值報送錯誤，本行結案;B:協商不成立;C:更新變更還款條件
  *
  * @param closedResult 結案原因
	*/
  public void setClosedResult(String closedResult) {
    this.closedResult = closedResult;
  }

/**
	* 轉出JCIC文字檔日期<br>
	* 
	* @return Integer
	*/
  public int getOutJcicTxtDate() {
    return StaticTool.bcToRoc(this.outJcicTxtDate);
  }

/**
	* 轉出JCIC文字檔日期<br>
	* 
  *
  * @param outJcicTxtDate 轉出JCIC文字檔日期
  * @throws LogicException when Date Is Warn	*/
  public void setOutJcicTxtDate(int outJcicTxtDate) throws LogicException {
    this.outJcicTxtDate = StaticTool.rocToBc(outJcicTxtDate);
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
    return "JcicZ063 [jcicZ063Id=" + jcicZ063Id + ", tranKey=" + tranKey
           + ", closedResult=" + closedResult + ", outJcicTxtDate=" + outJcicTxtDate + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
           + "]";
  }
}
