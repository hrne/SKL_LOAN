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
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * TxTeller 使用者檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxTeller`")
public class TxTeller implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -1475350139473068903L;

// 使用者代號
  @Id
  @Column(name = "`TlrNo`", length = 6)
  private String tlrNo = " ";

  // 使用單位
  @Column(name = "`BrNo`", length = 4)
  private String brNo;

  // AD記號
  @Column(name = "`AdFg`")
  private int adFg = 0;

  // 使用者姓名
  @Column(name = "`TlrItem`", length = 20)
  private String tlrItem;

  // 權限等級
  /* 1.主管 3.經辦 */
  @Column(name = "`LevelFg`")
  private int levelFg = 0;

  // 授權等級
  /* 經辦:0;主管:1-9 */
  @Column(name = "`AllowFg`")
  private int allowFg = 0;

  // 狀態
  /* 0.未啟用 1:啟用 9.停用 */
  @Column(name = "`Status`")
  private int status = 0;

  // 課組別
  @Column(name = "`GroupNo`", length = 1)
  private String groupNo;

  // 權限編號
  @Column(name = "`AuthNo`", length = 6)
  private String authNo;

  // 本、次日日期
  @Column(name = "`Entdy`")
  private int entdy = 0;

  // 報表使用資料庫
  /* 0.onLine1.onDay2.onMon3.onHist */
  @Column(name = "`ReportDb`")
  private int reportDb = 0;

  // 登入記號
  @Column(name = "`LogonFg`")
  private int logonFg = 0;

  // LOG記號
  @Column(name = "`LoggerFg`")
  private int loggerFg = 0;

  // 最後交易序號
  @Column(name = "`TxtNo`")
  private int txtNo = 0;

  // 上交日
  @Column(name = "`LtxDate`")
  private int ltxDate = 0;

  // 上交時
  @Column(name = "`LtxTime`")
  private int ltxTime = 0;

  // 備註
  @Column(name = "`Desc`", length = 60)
  private String desc;

  // 上次登入日期
  @Column(name = "`LastDate`")
  private int lastDate = 0;

  // 上次登入時間
  @Column(name = "`LastTime`")
  private int lastTime = 0;

  // AML定審高風險處理記號
  /* 限放款審查課設定0.否1是 */
  @Column(name = "`AmlHighFg`", length = 1)
  private String amlHighFg;

  // 權限編號1
  /* 停止使用,改記錄於TxTellerAuth */
  @Column(name = "`AuthNo1`", length = 6)
  private String authNo1;

  // 權限編號2
  /* 停止使用,改記錄於TxTellerAuth */
  @Column(name = "`AuthNo2`", length = 6)
  private String authNo2;

  // 權限編號3
  /* 停止使用,改記錄於TxTellerAuth */
  @Column(name = "`AuthNo3`", length = 6)
  private String authNo3;

  // 權限編號4
  /* 停止使用,改記錄於TxTellerAuth */
  @Column(name = "`AuthNo4`", length = 6)
  private String authNo4;

  // 權限編號5
  /* 停止使用,改記錄於TxTellerAuth */
  @Column(name = "`AuthNo5`", length = 6)
  private String authNo5;

  // 權限編號6
  /* 停止使用,改記錄於TxTellerAuth */
  @Column(name = "`AuthNo6`", length = 6)
  private String authNo6;

  // 權限編號7
  /* 停止使用,改記錄於TxTellerAuth */
  @Column(name = "`AuthNo7`", length = 6)
  private String authNo7;

  // 權限編號8
  /* 停止使用,改記錄於TxTellerAuth */
  @Column(name = "`AuthNo8`", length = 6)
  private String authNo8;

  // 權限編號9
  /* 停止使用,改記錄於TxTellerAuth */
  @Column(name = "`AuthNo9`", length = 6)
  private String authNo9;

  // 權限編號10
  /* 停止使用,改記錄於TxTellerAuth */
  @Column(name = "`AuthNo10`", length = 6)
  private String authNo10;

  // 站別
  @Column(name = "`Station`", length = 3)
  private String station;

  // 最後維護時間
  @Column(name = "`MntDate`")
  private java.sql.Timestamp mntDate;

  // 最後維護人員
  @Column(name = "`MntEmpNo`", length = 6)
  private String mntEmpNo;

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
	* 使用者代號<br>
	* 
	* @return String
	*/
  public String getTlrNo() {
    return this.tlrNo == null ? "" : this.tlrNo;
  }

/**
	* 使用者代號<br>
	* 
  *
  * @param tlrNo 使用者代號
	*/
  public void setTlrNo(String tlrNo) {
    this.tlrNo = tlrNo;
  }

/**
	* 使用單位<br>
	* 
	* @return String
	*/
  public String getBrNo() {
    return this.brNo == null ? "" : this.brNo;
  }

/**
	* 使用單位<br>
	* 
  *
  * @param brNo 使用單位
	*/
  public void setBrNo(String brNo) {
    this.brNo = brNo;
  }

/**
	* AD記號<br>
	* 
	* @return Integer
	*/
  public int getAdFg() {
    return this.adFg;
  }

/**
	* AD記號<br>
	* 
  *
  * @param adFg AD記號
	*/
  public void setAdFg(int adFg) {
    this.adFg = adFg;
  }

/**
	* 使用者姓名<br>
	* 
	* @return String
	*/
  public String getTlrItem() {
    return this.tlrItem == null ? "" : this.tlrItem;
  }

/**
	* 使用者姓名<br>
	* 
  *
  * @param tlrItem 使用者姓名
	*/
  public void setTlrItem(String tlrItem) {
    this.tlrItem = tlrItem;
  }

/**
	* 權限等級<br>
	* 1.主管 3.經辦
	* @return Integer
	*/
  public int getLevelFg() {
    return this.levelFg;
  }

/**
	* 權限等級<br>
	* 1.主管 3.經辦
  *
  * @param levelFg 權限等級
	*/
  public void setLevelFg(int levelFg) {
    this.levelFg = levelFg;
  }

/**
	* 授權等級<br>
	* 經辦:0;主管:1-9
	* @return Integer
	*/
  public int getAllowFg() {
    return this.allowFg;
  }

/**
	* 授權等級<br>
	* 經辦:0;主管:1-9
  *
  * @param allowFg 授權等級
	*/
  public void setAllowFg(int allowFg) {
    this.allowFg = allowFg;
  }

/**
	* 狀態<br>
	* 0.未啟用 1:啟用 9.停用
	* @return Integer
	*/
  public int getStatus() {
    return this.status;
  }

/**
	* 狀態<br>
	* 0.未啟用 1:啟用 9.停用
  *
  * @param status 狀態
	*/
  public void setStatus(int status) {
    this.status = status;
  }

/**
	* 課組別<br>
	* 
	* @return String
	*/
  public String getGroupNo() {
    return this.groupNo == null ? "" : this.groupNo;
  }

/**
	* 課組別<br>
	* 
  *
  * @param groupNo 課組別
	*/
  public void setGroupNo(String groupNo) {
    this.groupNo = groupNo;
  }

/**
	* 權限編號<br>
	* 
	* @return String
	*/
  public String getAuthNo() {
    return this.authNo == null ? "" : this.authNo;
  }

/**
	* 權限編號<br>
	* 
  *
  * @param authNo 權限編號
	*/
  public void setAuthNo(String authNo) {
    this.authNo = authNo;
  }

/**
	* 本、次日日期<br>
	* 
	* @return Integer
	*/
  public int getEntdy() {
    return StaticTool.bcToRoc(this.entdy);
  }

/**
	* 本、次日日期<br>
	* 
  *
  * @param entdy 本、次日日期
  * @throws LogicException when Date Is Warn	*/
  public void setEntdy(int entdy) throws LogicException {
    this.entdy = StaticTool.rocToBc(entdy);
  }

/**
	* 報表使用資料庫<br>
	* 0.onLine
1.onDay
2.onMon
3.onHist
	* @return Integer
	*/
  public int getReportDb() {
    return this.reportDb;
  }

/**
	* 報表使用資料庫<br>
	* 0.onLine
1.onDay
2.onMon
3.onHist
  *
  * @param reportDb 報表使用資料庫
	*/
  public void setReportDb(int reportDb) {
    this.reportDb = reportDb;
  }

/**
	* 登入記號<br>
	* 
	* @return Integer
	*/
  public int getLogonFg() {
    return this.logonFg;
  }

/**
	* 登入記號<br>
	* 
  *
  * @param logonFg 登入記號
	*/
  public void setLogonFg(int logonFg) {
    this.logonFg = logonFg;
  }

/**
	* LOG記號<br>
	* 
	* @return Integer
	*/
  public int getLoggerFg() {
    return this.loggerFg;
  }

/**
	* LOG記號<br>
	* 
  *
  * @param loggerFg LOG記號
	*/
  public void setLoggerFg(int loggerFg) {
    this.loggerFg = loggerFg;
  }

/**
	* 最後交易序號<br>
	* 
	* @return Integer
	*/
  public int getTxtNo() {
    return this.txtNo;
  }

/**
	* 最後交易序號<br>
	* 
  *
  * @param txtNo 最後交易序號
	*/
  public void setTxtNo(int txtNo) {
    this.txtNo = txtNo;
  }

/**
	* 上交日<br>
	* 
	* @return Integer
	*/
  public int getLtxDate() {
    return StaticTool.bcToRoc(this.ltxDate);
  }

/**
	* 上交日<br>
	* 
  *
  * @param ltxDate 上交日
  * @throws LogicException when Date Is Warn	*/
  public void setLtxDate(int ltxDate) throws LogicException {
    this.ltxDate = StaticTool.rocToBc(ltxDate);
  }

/**
	* 上交時<br>
	* 
	* @return Integer
	*/
  public int getLtxTime() {
    return this.ltxTime;
  }

/**
	* 上交時<br>
	* 
  *
  * @param ltxTime 上交時
	*/
  public void setLtxTime(int ltxTime) {
    this.ltxTime = ltxTime;
  }

/**
	* 備註<br>
	* 
	* @return String
	*/
  public String getDesc() {
    return this.desc == null ? "" : this.desc;
  }

/**
	* 備註<br>
	* 
  *
  * @param desc 備註
	*/
  public void setDesc(String desc) {
    this.desc = desc;
  }

/**
	* 上次登入日期<br>
	* 
	* @return Integer
	*/
  public int getLastDate() {
    return StaticTool.bcToRoc(this.lastDate);
  }

/**
	* 上次登入日期<br>
	* 
  *
  * @param lastDate 上次登入日期
  * @throws LogicException when Date Is Warn	*/
  public void setLastDate(int lastDate) throws LogicException {
    this.lastDate = StaticTool.rocToBc(lastDate);
  }

/**
	* 上次登入時間<br>
	* 
	* @return Integer
	*/
  public int getLastTime() {
    return this.lastTime;
  }

/**
	* 上次登入時間<br>
	* 
  *
  * @param lastTime 上次登入時間
	*/
  public void setLastTime(int lastTime) {
    this.lastTime = lastTime;
  }

/**
	* AML定審高風險處理記號<br>
	* 限放款審查課設定0.否1是
	* @return String
	*/
  public String getAmlHighFg() {
    return this.amlHighFg == null ? "" : this.amlHighFg;
  }

/**
	* AML定審高風險處理記號<br>
	* 限放款審查課設定0.否1是
  *
  * @param amlHighFg AML定審高風險處理記號
	*/
  public void setAmlHighFg(String amlHighFg) {
    this.amlHighFg = amlHighFg;
  }

/**
	* 權限編號1<br>
	* 停止使用,改記錄於TxTellerAuth
	* @return String
	*/
  public String getAuthNo1() {
    return this.authNo1 == null ? "" : this.authNo1;
  }

/**
	* 權限編號1<br>
	* 停止使用,改記錄於TxTellerAuth
  *
  * @param authNo1 權限編號1
	*/
  public void setAuthNo1(String authNo1) {
    this.authNo1 = authNo1;
  }

/**
	* 權限編號2<br>
	* 停止使用,改記錄於TxTellerAuth
	* @return String
	*/
  public String getAuthNo2() {
    return this.authNo2 == null ? "" : this.authNo2;
  }

/**
	* 權限編號2<br>
	* 停止使用,改記錄於TxTellerAuth
  *
  * @param authNo2 權限編號2
	*/
  public void setAuthNo2(String authNo2) {
    this.authNo2 = authNo2;
  }

/**
	* 權限編號3<br>
	* 停止使用,改記錄於TxTellerAuth
	* @return String
	*/
  public String getAuthNo3() {
    return this.authNo3 == null ? "" : this.authNo3;
  }

/**
	* 權限編號3<br>
	* 停止使用,改記錄於TxTellerAuth
  *
  * @param authNo3 權限編號3
	*/
  public void setAuthNo3(String authNo3) {
    this.authNo3 = authNo3;
  }

/**
	* 權限編號4<br>
	* 停止使用,改記錄於TxTellerAuth
	* @return String
	*/
  public String getAuthNo4() {
    return this.authNo4 == null ? "" : this.authNo4;
  }

/**
	* 權限編號4<br>
	* 停止使用,改記錄於TxTellerAuth
  *
  * @param authNo4 權限編號4
	*/
  public void setAuthNo4(String authNo4) {
    this.authNo4 = authNo4;
  }

/**
	* 權限編號5<br>
	* 停止使用,改記錄於TxTellerAuth
	* @return String
	*/
  public String getAuthNo5() {
    return this.authNo5 == null ? "" : this.authNo5;
  }

/**
	* 權限編號5<br>
	* 停止使用,改記錄於TxTellerAuth
  *
  * @param authNo5 權限編號5
	*/
  public void setAuthNo5(String authNo5) {
    this.authNo5 = authNo5;
  }

/**
	* 權限編號6<br>
	* 停止使用,改記錄於TxTellerAuth
	* @return String
	*/
  public String getAuthNo6() {
    return this.authNo6 == null ? "" : this.authNo6;
  }

/**
	* 權限編號6<br>
	* 停止使用,改記錄於TxTellerAuth
  *
  * @param authNo6 權限編號6
	*/
  public void setAuthNo6(String authNo6) {
    this.authNo6 = authNo6;
  }

/**
	* 權限編號7<br>
	* 停止使用,改記錄於TxTellerAuth
	* @return String
	*/
  public String getAuthNo7() {
    return this.authNo7 == null ? "" : this.authNo7;
  }

/**
	* 權限編號7<br>
	* 停止使用,改記錄於TxTellerAuth
  *
  * @param authNo7 權限編號7
	*/
  public void setAuthNo7(String authNo7) {
    this.authNo7 = authNo7;
  }

/**
	* 權限編號8<br>
	* 停止使用,改記錄於TxTellerAuth
	* @return String
	*/
  public String getAuthNo8() {
    return this.authNo8 == null ? "" : this.authNo8;
  }

/**
	* 權限編號8<br>
	* 停止使用,改記錄於TxTellerAuth
  *
  * @param authNo8 權限編號8
	*/
  public void setAuthNo8(String authNo8) {
    this.authNo8 = authNo8;
  }

/**
	* 權限編號9<br>
	* 停止使用,改記錄於TxTellerAuth
	* @return String
	*/
  public String getAuthNo9() {
    return this.authNo9 == null ? "" : this.authNo9;
  }

/**
	* 權限編號9<br>
	* 停止使用,改記錄於TxTellerAuth
  *
  * @param authNo9 權限編號9
	*/
  public void setAuthNo9(String authNo9) {
    this.authNo9 = authNo9;
  }

/**
	* 權限編號10<br>
	* 停止使用,改記錄於TxTellerAuth
	* @return String
	*/
  public String getAuthNo10() {
    return this.authNo10 == null ? "" : this.authNo10;
  }

/**
	* 權限編號10<br>
	* 停止使用,改記錄於TxTellerAuth
  *
  * @param authNo10 權限編號10
	*/
  public void setAuthNo10(String authNo10) {
    this.authNo10 = authNo10;
  }

/**
	* 站別<br>
	* 
	* @return String
	*/
  public String getStation() {
    return this.station == null ? "" : this.station;
  }

/**
	* 站別<br>
	* 
  *
  * @param station 站別
	*/
  public void setStation(String station) {
    this.station = station;
  }

/**
	* 最後維護時間<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getMntDate() {
    return this.mntDate;
  }

/**
	* 最後維護時間<br>
	* 
  *
  * @param mntDate 最後維護時間
	*/
  public void setMntDate(java.sql.Timestamp mntDate) {
    this.mntDate = mntDate;
  }

/**
	* 最後維護人員<br>
	* 
	* @return String
	*/
  public String getMntEmpNo() {
    return this.mntEmpNo == null ? "" : this.mntEmpNo;
  }

/**
	* 最後維護人員<br>
	* 
  *
  * @param mntEmpNo 最後維護人員
	*/
  public void setMntEmpNo(String mntEmpNo) {
    this.mntEmpNo = mntEmpNo;
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
    return "TxTeller [tlrNo=" + tlrNo + ", brNo=" + brNo + ", adFg=" + adFg + ", tlrItem=" + tlrItem + ", levelFg=" + levelFg + ", allowFg=" + allowFg
           + ", status=" + status + ", groupNo=" + groupNo + ", authNo=" + authNo + ", entdy=" + entdy + ", reportDb=" + reportDb + ", logonFg=" + logonFg
           + ", loggerFg=" + loggerFg + ", txtNo=" + txtNo + ", ltxDate=" + ltxDate + ", ltxTime=" + ltxTime + ", desc=" + desc + ", lastDate=" + lastDate
           + ", lastTime=" + lastTime + ", amlHighFg=" + amlHighFg + ", authNo1=" + authNo1 + ", authNo2=" + authNo2 + ", authNo3=" + authNo3 + ", authNo4=" + authNo4
           + ", authNo5=" + authNo5 + ", authNo6=" + authNo6 + ", authNo7=" + authNo7 + ", authNo8=" + authNo8 + ", authNo9=" + authNo9 + ", authNo10=" + authNo10
           + ", station=" + station + ", mntDate=" + mntDate + ", mntEmpNo=" + mntEmpNo + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
