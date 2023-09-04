package com.st1.itx.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
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
 * PfInsCheck 房貸獎勵保費檢核檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`PfInsCheck`")
public class PfInsCheck implements Serializable {


  @EmbeddedId
  private PfInsCheckId pfInsCheckId;

  // 類別
  /* 0:換算業績、業務報酬1:介紹獎金、協辦獎金2:介紹人加碼獎勵津貼 */
  @Column(name = "`Kind`", insertable = false, updatable = false)
  private int kind = 0;

  // 戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 徵審系統案號(eLoan案件編號)
  @Column(name = "`CreditSysNo`")
  private int creditSysNo = 0;

  // 借款人身份證字號
  @Column(name = "`CustId`", length = 10)
  private String custId;

  // 借款書申請日
  @Column(name = "`ApplDate`")
  private int applDate = 0;

  // 承保日
  @Column(name = "`InsDate`")
  private int insDate = 0;

  // 保單號碼
  @Column(name = "`InsNo`", length = 15)
  private String insNo;

  // 檢核結果(Y/N)
  @Column(name = "`CheckResult`", length = 1)
  private String checkResult;

  // 檢核工作月
  /* 檢核結果已為Y者不再變動，N則篩選借款書申請日三個月內者再次檢核 */
  @Column(name = "`CheckWorkMonth`")
  private int checkWorkMonth = 0;

  // 回應訊息1
  @Column(name = "`ReturnMsg`", length = 2000)
  private String returnMsg;

  // 回應訊息2
  @Column(name = "`ReturnMsg2`", length = 2000)
  private String returnMsg2;

  // 回應訊息3
  @Column(name = "`ReturnMsg3`", length = 2000)
  private String returnMsg3;

  // 業績工作月
  @Column(name = "`PerfWorkMonth`")
  private int perfWorkMonth = 0;

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


  public PfInsCheckId getPfInsCheckId() {
    return this.pfInsCheckId;
  }

  public void setPfInsCheckId(PfInsCheckId pfInsCheckId) {
    this.pfInsCheckId = pfInsCheckId;
  }

/**
	* 類別<br>
	* 0:換算業績、業務報酬
1:介紹獎金、協辦獎金
2:介紹人加碼獎勵津貼
	* @return Integer
	*/
  public int getKind() {
    return this.kind;
  }

/**
	* 類別<br>
	* 0:換算業績、業務報酬
1:介紹獎金、協辦獎金
2:介紹人加碼獎勵津貼
  *
  * @param kind 類別
	*/
  public void setKind(int kind) {
    this.kind = kind;
  }

/**
	* 戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 戶號<br>
	* 
  *
  * @param custNo 戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 額度編號<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度編號<br>
	* 
  *
  * @param facmNo 額度編號
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 徵審系統案號(eLoan案件編號)<br>
	* 
	* @return Integer
	*/
  public int getCreditSysNo() {
    return this.creditSysNo;
  }

/**
	* 徵審系統案號(eLoan案件編號)<br>
	* 
  *
  * @param creditSysNo 徵審系統案號(eLoan案件編號)
	*/
  public void setCreditSysNo(int creditSysNo) {
    this.creditSysNo = creditSysNo;
  }

/**
	* 借款人身份證字號<br>
	* 
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 借款人身份證字號<br>
	* 
  *
  * @param custId 借款人身份證字號
	*/
  public void setCustId(String custId) {
    this.custId = custId;
  }

/**
	* 借款書申請日<br>
	* 
	* @return Integer
	*/
  public int getApplDate() {
    return StaticTool.bcToRoc(this.applDate);
  }

/**
	* 借款書申請日<br>
	* 
  *
  * @param applDate 借款書申請日
  * @throws LogicException when Date Is Warn	*/
  public void setApplDate(int applDate) throws LogicException {
    this.applDate = StaticTool.rocToBc(applDate);
  }

/**
	* 承保日<br>
	* 
	* @return Integer
	*/
  public int getInsDate() {
    return StaticTool.bcToRoc(this.insDate);
  }

/**
	* 承保日<br>
	* 
  *
  * @param insDate 承保日
  * @throws LogicException when Date Is Warn	*/
  public void setInsDate(int insDate) throws LogicException {
    this.insDate = StaticTool.rocToBc(insDate);
  }

/**
	* 保單號碼<br>
	* 
	* @return String
	*/
  public String getInsNo() {
    return this.insNo == null ? "" : this.insNo;
  }

/**
	* 保單號碼<br>
	* 
  *
  * @param insNo 保單號碼
	*/
  public void setInsNo(String insNo) {
    this.insNo = insNo;
  }

/**
	* 檢核結果(Y/N)<br>
	* 
	* @return String
	*/
  public String getCheckResult() {
    return this.checkResult == null ? "" : this.checkResult;
  }

/**
	* 檢核結果(Y/N)<br>
	* 
  *
  * @param checkResult 檢核結果(Y/N)
	*/
  public void setCheckResult(String checkResult) {
    this.checkResult = checkResult;
  }

/**
	* 檢核工作月<br>
	* 檢核結果已為Y者不再變動，N則篩選借款書申請日三個月內者再次檢核
	* @return Integer
	*/
  public int getCheckWorkMonth() {
    return this.checkWorkMonth;
  }

/**
	* 檢核工作月<br>
	* 檢核結果已為Y者不再變動，N則篩選借款書申請日三個月內者再次檢核
  *
  * @param checkWorkMonth 檢核工作月
	*/
  public void setCheckWorkMonth(int checkWorkMonth) {
    this.checkWorkMonth = checkWorkMonth;
  }

/**
	* 回應訊息1<br>
	* 
	* @return String
	*/
  public String getReturnMsg() {
    return this.returnMsg == null ? "" : this.returnMsg;
  }

/**
	* 回應訊息1<br>
	* 
  *
  * @param returnMsg 回應訊息1
	*/
  public void setReturnMsg(String returnMsg) {
    this.returnMsg = returnMsg;
  }

/**
	* 回應訊息2<br>
	* 
	* @return String
	*/
  public String getReturnMsg2() {
    return this.returnMsg2 == null ? "" : this.returnMsg2;
  }

/**
	* 回應訊息2<br>
	* 
  *
  * @param returnMsg2 回應訊息2
	*/
  public void setReturnMsg2(String returnMsg2) {
    this.returnMsg2 = returnMsg2;
  }

/**
	* 回應訊息3<br>
	* 
	* @return String
	*/
  public String getReturnMsg3() {
    return this.returnMsg3 == null ? "" : this.returnMsg3;
  }

/**
	* 回應訊息3<br>
	* 
  *
  * @param returnMsg3 回應訊息3
	*/
  public void setReturnMsg3(String returnMsg3) {
    this.returnMsg3 = returnMsg3;
  }

/**
	* 業績工作月<br>
	* 
	* @return Integer
	*/
  public int getPerfWorkMonth() {
    return this.perfWorkMonth;
  }

/**
	* 業績工作月<br>
	* 
  *
  * @param perfWorkMonth 業績工作月
	*/
  public void setPerfWorkMonth(int perfWorkMonth) {
    this.perfWorkMonth = perfWorkMonth;
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
    return "PfInsCheck [pfInsCheckId=" + pfInsCheckId + ", creditSysNo=" + creditSysNo + ", custId=" + custId + ", applDate=" + applDate
           + ", insDate=" + insDate + ", insNo=" + insNo + ", checkResult=" + checkResult + ", checkWorkMonth=" + checkWorkMonth + ", returnMsg=" + returnMsg + ", returnMsg2=" + returnMsg2
           + ", returnMsg3=" + returnMsg3 + ", perfWorkMonth=" + perfWorkMonth + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
           + "]";
  }
}
