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
 * InnDocRecord 檔案借閱檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`InnDocRecord`")
public class InnDocRecord implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -8354633614084907051L;

@EmbeddedId
  private InnDocRecordId innDocRecordId;

  // 借款人戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 額度號碼
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 申請序號
  /* 依戶號編流水號 */
  @Column(name = "`ApplSeq`", length = 3, insertable = false, updatable = false)
  private String applSeq;

  // 登放記號
  /* CdCode.ActFgX1:登錄2:放行3:審核4:審核放行 */
  @Column(name = "`TitaActFg`", length = 1)
  private String titaActFg;

  // 申請或歸還
  /* CdCode.ApplCode1:申請2:歸還 */
  @Column(name = "`ApplCode`", length = 1)
  private String applCode;

  // 借閱人
  @Column(name = "`ApplEmpNo`", length = 6)
  private String applEmpNo;

  // 管理人
  @Column(name = "`KeeperEmpNo`", length = 6)
  private String keeperEmpNo;

  // 用途
  /* CdCode.UsageCodeX01:清償02:法拍03:增貸04:展期05:撥款06:查閱07:重估08:其他09:查核10:補發11:部分塗銷12:領還暫不領 */
  @Column(name = "`UsageCode`", length = 2)
  private String usageCode;

  // 正本/影本
  /* 1.正本 2.影本 3.正本及影本 */
  @Column(name = "`CopyCode`", length = 1)
  private String copyCode;

  // 借閱日期
  @Column(name = "`ApplDate`")
  private int applDate = 0;

  // 歸還日期
  @Column(name = "`ReturnDate`")
  private int returnDate = 0;

  // 歸還人
  @Column(name = "`ReturnEmpNo`", length = 6)
  private String returnEmpNo;

  // 備註
  /* 2022/8/17:長度由60放大為100 */
  @Column(name = "`Remark`", length = 100)
  private String remark;

  // 借閱項目
  /* 1:重要袋2:普通袋3:重要袋&amp;普通袋4:其他 */
  @Column(name = "`ApplObj`", length = 1)
  private String applObj;

  // 登錄日期
  @Column(name = "`TitaEntDy`")
  private int titaEntDy = 0;

  // 登錄經辦
  @Column(name = "`TitaTlrNo`", length = 6)
  private String titaTlrNo;

  // 登錄交易序號
  @Column(name = "`TitaTxtNo`")
  private int titaTxtNo = 0;

  // jason格式紀錄欄
  @Column(name = "`JsonFields`", length = 300)
  private String jsonFields;

  // 額度備註
  /* 2022/8/17新增:額度號碼=0時必須輸入,紀錄有哪些額度借閱檔案 */
  @Column(name = "`FacmNoMemo`", length = 20)
  private String facmNoMemo;

  // 借閱人姓名
  /* 轉舊資料使用 */
  @Column(name = "`ApplEmpName`", length = 30)
  private String applEmpName;

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


  public InnDocRecordId getInnDocRecordId() {
    return this.innDocRecordId;
  }

  public void setInnDocRecordId(InnDocRecordId innDocRecordId) {
    this.innDocRecordId = innDocRecordId;
  }

/**
	* 借款人戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 借款人戶號<br>
	* 
  *
  * @param custNo 借款人戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 額度號碼<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度號碼<br>
	* 
  *
  * @param facmNo 額度號碼
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 申請序號<br>
	* 依戶號編流水號
	* @return String
	*/
  public String getApplSeq() {
    return this.applSeq == null ? "" : this.applSeq;
  }

/**
	* 申請序號<br>
	* 依戶號編流水號
  *
  * @param applSeq 申請序號
	*/
  public void setApplSeq(String applSeq) {
    this.applSeq = applSeq;
  }

/**
	* 登放記號<br>
	* CdCode.ActFgX
1:登錄
2:放行
3:審核
4:審核放行
	* @return String
	*/
  public String getTitaActFg() {
    return this.titaActFg == null ? "" : this.titaActFg;
  }

/**
	* 登放記號<br>
	* CdCode.ActFgX
1:登錄
2:放行
3:審核
4:審核放行
  *
  * @param titaActFg 登放記號
	*/
  public void setTitaActFg(String titaActFg) {
    this.titaActFg = titaActFg;
  }

/**
	* 申請或歸還<br>
	* CdCode.ApplCode
1:申請
2:歸還
	* @return String
	*/
  public String getApplCode() {
    return this.applCode == null ? "" : this.applCode;
  }

/**
	* 申請或歸還<br>
	* CdCode.ApplCode
1:申請
2:歸還
  *
  * @param applCode 申請或歸還
	*/
  public void setApplCode(String applCode) {
    this.applCode = applCode;
  }

/**
	* 借閱人<br>
	* 
	* @return String
	*/
  public String getApplEmpNo() {
    return this.applEmpNo == null ? "" : this.applEmpNo;
  }

/**
	* 借閱人<br>
	* 
  *
  * @param applEmpNo 借閱人
	*/
  public void setApplEmpNo(String applEmpNo) {
    this.applEmpNo = applEmpNo;
  }

/**
	* 管理人<br>
	* 
	* @return String
	*/
  public String getKeeperEmpNo() {
    return this.keeperEmpNo == null ? "" : this.keeperEmpNo;
  }

/**
	* 管理人<br>
	* 
  *
  * @param keeperEmpNo 管理人
	*/
  public void setKeeperEmpNo(String keeperEmpNo) {
    this.keeperEmpNo = keeperEmpNo;
  }

/**
	* 用途<br>
	* CdCode.UsageCodeX
01:清償
02:法拍
03:增貸
04:展期
05:撥款
06:查閱
07:重估
08:其他
09:查核
10:補發
11:部分塗銷
12:領還暫不領
	* @return String
	*/
  public String getUsageCode() {
    return this.usageCode == null ? "" : this.usageCode;
  }

/**
	* 用途<br>
	* CdCode.UsageCodeX
01:清償
02:法拍
03:增貸
04:展期
05:撥款
06:查閱
07:重估
08:其他
09:查核
10:補發
11:部分塗銷
12:領還暫不領
  *
  * @param usageCode 用途
	*/
  public void setUsageCode(String usageCode) {
    this.usageCode = usageCode;
  }

/**
	* 正本/影本<br>
	* 1.正本 2.影本 3.正本及影本
	* @return String
	*/
  public String getCopyCode() {
    return this.copyCode == null ? "" : this.copyCode;
  }

/**
	* 正本/影本<br>
	* 1.正本 2.影本 3.正本及影本
  *
  * @param copyCode 正本/影本
	*/
  public void setCopyCode(String copyCode) {
    this.copyCode = copyCode;
  }

/**
	* 借閱日期<br>
	* 
	* @return Integer
	*/
  public int getApplDate() {
    return StaticTool.bcToRoc(this.applDate);
  }

/**
	* 借閱日期<br>
	* 
  *
  * @param applDate 借閱日期
  * @throws LogicException when Date Is Warn	*/
  public void setApplDate(int applDate) throws LogicException {
    this.applDate = StaticTool.rocToBc(applDate);
  }

/**
	* 歸還日期<br>
	* 
	* @return Integer
	*/
  public int getReturnDate() {
    return StaticTool.bcToRoc(this.returnDate);
  }

/**
	* 歸還日期<br>
	* 
  *
  * @param returnDate 歸還日期
  * @throws LogicException when Date Is Warn	*/
  public void setReturnDate(int returnDate) throws LogicException {
    this.returnDate = StaticTool.rocToBc(returnDate);
  }

/**
	* 歸還人<br>
	* 
	* @return String
	*/
  public String getReturnEmpNo() {
    return this.returnEmpNo == null ? "" : this.returnEmpNo;
  }

/**
	* 歸還人<br>
	* 
  *
  * @param returnEmpNo 歸還人
	*/
  public void setReturnEmpNo(String returnEmpNo) {
    this.returnEmpNo = returnEmpNo;
  }

/**
	* 備註<br>
	* 2022/8/17:長度由60放大為100
	* @return String
	*/
  public String getRemark() {
    return this.remark == null ? "" : this.remark;
  }

/**
	* 備註<br>
	* 2022/8/17:長度由60放大為100
  *
  * @param remark 備註
	*/
  public void setRemark(String remark) {
    this.remark = remark;
  }

/**
	* 借閱項目<br>
	* 1:重要袋
2:普通袋
3:重要袋&amp;普通袋
4:其他
	* @return String
	*/
  public String getApplObj() {
    return this.applObj == null ? "" : this.applObj;
  }

/**
	* 借閱項目<br>
	* 1:重要袋
2:普通袋
3:重要袋&amp;普通袋
4:其他
  *
  * @param applObj 借閱項目
	*/
  public void setApplObj(String applObj) {
    this.applObj = applObj;
  }

/**
	* 登錄日期<br>
	* 
	* @return Integer
	*/
  public int getTitaEntDy() {
    return StaticTool.bcToRoc(this.titaEntDy);
  }

/**
	* 登錄日期<br>
	* 
  *
  * @param titaEntDy 登錄日期
  * @throws LogicException when Date Is Warn	*/
  public void setTitaEntDy(int titaEntDy) throws LogicException {
    this.titaEntDy = StaticTool.rocToBc(titaEntDy);
  }

/**
	* 登錄經辦<br>
	* 
	* @return String
	*/
  public String getTitaTlrNo() {
    return this.titaTlrNo == null ? "" : this.titaTlrNo;
  }

/**
	* 登錄經辦<br>
	* 
  *
  * @param titaTlrNo 登錄經辦
	*/
  public void setTitaTlrNo(String titaTlrNo) {
    this.titaTlrNo = titaTlrNo;
  }

/**
	* 登錄交易序號<br>
	* 
	* @return Integer
	*/
  public int getTitaTxtNo() {
    return this.titaTxtNo;
  }

/**
	* 登錄交易序號<br>
	* 
  *
  * @param titaTxtNo 登錄交易序號
	*/
  public void setTitaTxtNo(int titaTxtNo) {
    this.titaTxtNo = titaTxtNo;
  }

/**
	* jason格式紀錄欄<br>
	* 
	* @return String
	*/
  public String getJsonFields() {
    return this.jsonFields == null ? "" : this.jsonFields;
  }

/**
	* jason格式紀錄欄<br>
	* 
  *
  * @param jsonFields jason格式紀錄欄
	*/
  public void setJsonFields(String jsonFields) {
    this.jsonFields = jsonFields;
  }

/**
	* 額度備註<br>
	* 2022/8/17新增:額度號碼=0時必須輸入,紀錄有哪些額度借閱檔案
	* @return String
	*/
  public String getFacmNoMemo() {
    return this.facmNoMemo == null ? "" : this.facmNoMemo;
  }

/**
	* 額度備註<br>
	* 2022/8/17新增:額度號碼=0時必須輸入,紀錄有哪些額度借閱檔案
  *
  * @param facmNoMemo 額度備註
	*/
  public void setFacmNoMemo(String facmNoMemo) {
    this.facmNoMemo = facmNoMemo;
  }

/**
	* 借閱人姓名<br>
	* 轉舊資料使用
	* @return String
	*/
  public String getApplEmpName() {
    return this.applEmpName == null ? "" : this.applEmpName;
  }

/**
	* 借閱人姓名<br>
	* 轉舊資料使用
  *
  * @param applEmpName 借閱人姓名
	*/
  public void setApplEmpName(String applEmpName) {
    this.applEmpName = applEmpName;
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
    return "InnDocRecord [innDocRecordId=" + innDocRecordId + ", titaActFg=" + titaActFg + ", applCode=" + applCode + ", applEmpNo=" + applEmpNo
           + ", keeperEmpNo=" + keeperEmpNo + ", usageCode=" + usageCode + ", copyCode=" + copyCode + ", applDate=" + applDate + ", returnDate=" + returnDate + ", returnEmpNo=" + returnEmpNo
           + ", remark=" + remark + ", applObj=" + applObj + ", titaEntDy=" + titaEntDy + ", titaTlrNo=" + titaTlrNo + ", titaTxtNo=" + titaTxtNo + ", jsonFields=" + jsonFields
           + ", facmNoMemo=" + facmNoMemo + ", applEmpName=" + applEmpName + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
           + "]";
  }
}
