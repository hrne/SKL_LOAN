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
import javax.persistence.Id;
import javax.persistence.Column;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * TxTranCode 交易控制檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxTranCode`")
public class TxTranCode implements Serializable {


  // 交易代號
  @Id
  @Column(name = "`TranNo`", length = 5)
  private String tranNo = " ";

  // 交易名稱
  @Column(name = "`TranItem`", length = 40)
  private String tranItem;

  // 交易說明
  @Column(name = "`Desc`", length = 60)
  private String desc;

  // 交易類別
  /* 1.查詢 2.維護 */
  @Column(name = "`TypeFg`")
  private int typeFg = 0;

  // 訂正記號
  /* 0.不可訂正 1.可訂正 */
  @Column(name = "`CancelFg`")
  private int cancelFg = 0;

  // 修正記號
  /* 0.不可修正 1.可修正 */
  @Column(name = "`ModifyFg`")
  private int modifyFg = 0;

  // 主選單類別
  @Column(name = "`MenuNo`", length = 2)
  private String menuNo;

  // 子選單類別
  @Column(name = "`SubMenuNo`", length = 2)
  private String subMenuNo;

  // 掛入選單記號
  /* 0.否 1.是 */
  @Column(name = "`MenuFg`")
  private int menuFg = 0;

  // 登錄需提交記號
  /* 0.否 1.是 (限二段式交易使用) 2.自動 */
  @Column(name = "`SubmitFg`")
  private int submitFg = 0;

  // 狀態
  /* 1.正常 2.停用 */
  @Column(name = "`Status`")
  private int status = 0;

  // 結清戶個資控管記號
  /* 0.不控管 1.控管 */
  @Column(name = "`CustDataCtrlFg`")
  private int custDataCtrlFg = 0;

  // 顯示顧客控管警訊記號
  /* 0.不顯示 1.顯示 */
  @Column(name = "`CustRmkFg`")
  private int custRmkFg = 0;

  // 入口交易提示訊息
  @Column(name = "`ChainTranMsg`", length = 200)
  private String chainTranMsg;

  // 敏感性資料記錄記號
  /* 0-NONE1-YES */
  @Column(name = "`ApLogFlag`")
  private int apLogFlag = 0;

  // 敏感性資料記錄Rim記號
  @Column(name = "`ApLogRim`", length = 5)
  private String apLogRim;

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
	* 交易代號<br>
	* 
	* @return String
	*/
  public String getTranNo() {
    return this.tranNo == null ? "" : this.tranNo;
  }

/**
	* 交易代號<br>
	* 
  *
  * @param tranNo 交易代號
	*/
  public void setTranNo(String tranNo) {
    this.tranNo = tranNo;
  }

/**
	* 交易名稱<br>
	* 
	* @return String
	*/
  public String getTranItem() {
    return this.tranItem == null ? "" : this.tranItem;
  }

/**
	* 交易名稱<br>
	* 
  *
  * @param tranItem 交易名稱
	*/
  public void setTranItem(String tranItem) {
    this.tranItem = tranItem;
  }

/**
	* 交易說明<br>
	* 
	* @return String
	*/
  public String getDesc() {
    return this.desc == null ? "" : this.desc;
  }

/**
	* 交易說明<br>
	* 
  *
  * @param desc 交易說明
	*/
  public void setDesc(String desc) {
    this.desc = desc;
  }

/**
	* 交易類別<br>
	* 1.查詢 2.維護
	* @return Integer
	*/
  public int getTypeFg() {
    return this.typeFg;
  }

/**
	* 交易類別<br>
	* 1.查詢 2.維護
  *
  * @param typeFg 交易類別
	*/
  public void setTypeFg(int typeFg) {
    this.typeFg = typeFg;
  }

/**
	* 訂正記號<br>
	* 0.不可訂正 1.可訂正
	* @return Integer
	*/
  public int getCancelFg() {
    return this.cancelFg;
  }

/**
	* 訂正記號<br>
	* 0.不可訂正 1.可訂正
  *
  * @param cancelFg 訂正記號
	*/
  public void setCancelFg(int cancelFg) {
    this.cancelFg = cancelFg;
  }

/**
	* 修正記號<br>
	* 0.不可修正 1.可修正
	* @return Integer
	*/
  public int getModifyFg() {
    return this.modifyFg;
  }

/**
	* 修正記號<br>
	* 0.不可修正 1.可修正
  *
  * @param modifyFg 修正記號
	*/
  public void setModifyFg(int modifyFg) {
    this.modifyFg = modifyFg;
  }

/**
	* 主選單類別<br>
	* 
	* @return String
	*/
  public String getMenuNo() {
    return this.menuNo == null ? "" : this.menuNo;
  }

/**
	* 主選單類別<br>
	* 
  *
  * @param menuNo 主選單類別
	*/
  public void setMenuNo(String menuNo) {
    this.menuNo = menuNo;
  }

/**
	* 子選單類別<br>
	* 
	* @return String
	*/
  public String getSubMenuNo() {
    return this.subMenuNo == null ? "" : this.subMenuNo;
  }

/**
	* 子選單類別<br>
	* 
  *
  * @param subMenuNo 子選單類別
	*/
  public void setSubMenuNo(String subMenuNo) {
    this.subMenuNo = subMenuNo;
  }

/**
	* 掛入選單記號<br>
	* 0.否 1.是
	* @return Integer
	*/
  public int getMenuFg() {
    return this.menuFg;
  }

/**
	* 掛入選單記號<br>
	* 0.否 1.是
  *
  * @param menuFg 掛入選單記號
	*/
  public void setMenuFg(int menuFg) {
    this.menuFg = menuFg;
  }

/**
	* 登錄需提交記號<br>
	* 0.否 1.是 (限二段式交易使用) 2.自動
	* @return Integer
	*/
  public int getSubmitFg() {
    return this.submitFg;
  }

/**
	* 登錄需提交記號<br>
	* 0.否 1.是 (限二段式交易使用) 2.自動
  *
  * @param submitFg 登錄需提交記號
	*/
  public void setSubmitFg(int submitFg) {
    this.submitFg = submitFg;
  }

/**
	* 狀態<br>
	* 1.正常 2.停用
	* @return Integer
	*/
  public int getStatus() {
    return this.status;
  }

/**
	* 狀態<br>
	* 1.正常 2.停用
  *
  * @param status 狀態
	*/
  public void setStatus(int status) {
    this.status = status;
  }

/**
	* 結清戶個資控管記號<br>
	* 0.不控管 1.控管
	* @return Integer
	*/
  public int getCustDataCtrlFg() {
    return this.custDataCtrlFg;
  }

/**
	* 結清戶個資控管記號<br>
	* 0.不控管 1.控管
  *
  * @param custDataCtrlFg 結清戶個資控管記號
	*/
  public void setCustDataCtrlFg(int custDataCtrlFg) {
    this.custDataCtrlFg = custDataCtrlFg;
  }

/**
	* 顯示顧客控管警訊記號<br>
	* 0.不顯示 1.顯示
	* @return Integer
	*/
  public int getCustRmkFg() {
    return this.custRmkFg;
  }

/**
	* 顯示顧客控管警訊記號<br>
	* 0.不顯示 1.顯示
  *
  * @param custRmkFg 顯示顧客控管警訊記號
	*/
  public void setCustRmkFg(int custRmkFg) {
    this.custRmkFg = custRmkFg;
  }

/**
	* 入口交易提示訊息<br>
	* 
	* @return String
	*/
  public String getChainTranMsg() {
    return this.chainTranMsg == null ? "" : this.chainTranMsg;
  }

/**
	* 入口交易提示訊息<br>
	* 
  *
  * @param chainTranMsg 入口交易提示訊息
	*/
  public void setChainTranMsg(String chainTranMsg) {
    this.chainTranMsg = chainTranMsg;
  }

/**
	* 敏感性資料記錄記號<br>
	* 0-NONE
1-YES
	* @return Integer
	*/
  public int getApLogFlag() {
    return this.apLogFlag;
  }

/**
	* 敏感性資料記錄記號<br>
	* 0-NONE
1-YES
  *
  * @param apLogFlag 敏感性資料記錄記號
	*/
  public void setApLogFlag(int apLogFlag) {
    this.apLogFlag = apLogFlag;
  }

/**
	* 敏感性資料記錄Rim記號<br>
	* 
	* @return String
	*/
  public String getApLogRim() {
    return this.apLogRim == null ? "" : this.apLogRim;
  }

/**
	* 敏感性資料記錄Rim記號<br>
	* 
  *
  * @param apLogRim 敏感性資料記錄Rim記號
	*/
  public void setApLogRim(String apLogRim) {
    this.apLogRim = apLogRim;
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
    return "TxTranCode [tranNo=" + tranNo + ", tranItem=" + tranItem + ", desc=" + desc + ", typeFg=" + typeFg + ", cancelFg=" + cancelFg + ", modifyFg=" + modifyFg
           + ", menuNo=" + menuNo + ", subMenuNo=" + subMenuNo + ", menuFg=" + menuFg + ", submitFg=" + submitFg + ", status=" + status + ", custDataCtrlFg=" + custDataCtrlFg
           + ", custRmkFg=" + custRmkFg + ", chainTranMsg=" + chainTranMsg + ", apLogFlag=" + apLogFlag + ", apLogRim=" + apLogRim + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
