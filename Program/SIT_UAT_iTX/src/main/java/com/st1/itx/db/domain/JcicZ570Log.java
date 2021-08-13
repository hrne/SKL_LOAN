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
 * JcicZ570Log 受理更生款項統一收付通知資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ570Log`")
public class JcicZ570Log implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 3249716035451754693L;

@EmbeddedId
  private JcicZ570LogId jcicZ570LogId;

  // 流水號
  @Column(name = "`Ukey`", length = 32, insertable = false, updatable = false)
  private String ukey;

  // 交易序號
  @Column(name = "`TxSeq`", length = 18, insertable = false, updatable = false)
  private String txSeq;

  // 交易代碼
  /* A:新增;C:異動;X:補件;D:刪除(僅限補件之資料刪除) */
  @Column(name = "`TranKey`", length = 1)
  private String tranKey;

  // 更生方案認可裁定日
  @Column(name = "`AdjudicateDate`")
  private int adjudicateDate = 0;

  // 更生債權金融機構家數
  @Column(name = "`BankCount`")
  private int bankCount = 0;

  // 債權金融機構代號1
  @Column(name = "`Bank1`", length = 3)
  private String bank1;

  // 債權金融機構代號2
  @Column(name = "`Bank2`", length = 3)
  private String bank2;

  // 債權金融機構代號3
  @Column(name = "`Bank3`", length = 3)
  private String bank3;

  // 債權金融機構代號4
  @Column(name = "`Bank4`", length = 3)
  private String bank4;

  // 債權金融機構代號5
  @Column(name = "`Bank5`", length = 3)
  private String bank5;

  // 債權金融機構代號6
  @Column(name = "`Bank6`", length = 3)
  private String bank6;

  // 債權金融機構代號7
  @Column(name = "`Bank7`", length = 3)
  private String bank7;

  // 債權金融機構代號8
  @Column(name = "`Bank8`", length = 3)
  private String bank8;

  // 債權金融機構代號9
  @Column(name = "`Bank9`", length = 3)
  private String bank9;

  // 債權金融機構代號10
  @Column(name = "`Bank10`", length = 3)
  private String bank10;

  // 債權金融機構代號11
  @Column(name = "`Bank11`", length = 3)
  private String bank11;

  // 債權金融機構代號12
  @Column(name = "`Bank12`", length = 3)
  private String bank12;

  // 債權金融機構代號13
  @Column(name = "`Bank13`", length = 3)
  private String bank13;

  // 債權金融機構代號14
  @Column(name = "`Bank14`", length = 3)
  private String bank14;

  // 債權金融機構代號15
  @Column(name = "`Bank15`", length = 3)
  private String bank15;

  // 債權金融機構代號16
  @Column(name = "`Bank16`", length = 3)
  private String bank16;

  // 債權金融機構代號17
  @Column(name = "`Bank17`", length = 3)
  private String bank17;

  // 債權金融機構代號18
  @Column(name = "`Bank18`", length = 3)
  private String bank18;

  // 債權金融機構代號19
  @Column(name = "`Bank19`", length = 3)
  private String bank19;

  // 債權金融機構代號20
  @Column(name = "`Bank20`", length = 3)
  private String bank20;

  // 債權金融機構代號21
  @Column(name = "`Bank21`", length = 3)
  private String bank21;

  // 債權金融機構代號22
  @Column(name = "`Bank22`", length = 3)
  private String bank22;

  // 債權金融機構代號23
  @Column(name = "`Bank23`", length = 3)
  private String bank23;

  // 債權金融機構代號24
  @Column(name = "`Bank24`", length = 3)
  private String bank24;

  // 債權金融機構代號25
  @Column(name = "`Bank25`", length = 3)
  private String bank25;

  // 債權金融機構代號26
  @Column(name = "`Bank26`", length = 3)
  private String bank26;

  // 債權金融機構代號27
  @Column(name = "`Bank27`", length = 3)
  private String bank27;

  // 債權金融機構代號28
  @Column(name = "`Bank28`", length = 3)
  private String bank28;

  // 債權金融機構代號29
  @Column(name = "`Bank29`", length = 3)
  private String bank29;

  // 債權金融機構代號30
  @Column(name = "`Bank30`", length = 3)
  private String bank30;

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


  public JcicZ570LogId getJcicZ570LogId() {
    return this.jcicZ570LogId;
  }

  public void setJcicZ570LogId(JcicZ570LogId jcicZ570LogId) {
    this.jcicZ570LogId = jcicZ570LogId;
  }

/**
	* 流水號<br>
	* 
	* @return String
	*/
  public String getUkey() {
    return this.ukey == null ? "" : this.ukey;
  }

/**
	* 流水號<br>
	* 
  *
  * @param ukey 流水號
	*/
  public void setUkey(String ukey) {
    this.ukey = ukey;
  }

/**
	* 交易序號<br>
	* 
	* @return String
	*/
  public String getTxSeq() {
    return this.txSeq == null ? "" : this.txSeq;
  }

/**
	* 交易序號<br>
	* 
  *
  * @param txSeq 交易序號
	*/
  public void setTxSeq(String txSeq) {
    this.txSeq = txSeq;
  }

/**
	* 交易代碼<br>
	* A:新增;C:異動;X:補件;D:刪除(僅限補件之資料刪除)
	* @return String
	*/
  public String getTranKey() {
    return this.tranKey == null ? "" : this.tranKey;
  }

/**
	* 交易代碼<br>
	* A:新增;C:異動;X:補件;D:刪除(僅限補件之資料刪除)
  *
  * @param tranKey 交易代碼
	*/
  public void setTranKey(String tranKey) {
    this.tranKey = tranKey;
  }

/**
	* 更生方案認可裁定日<br>
	* 
	* @return Integer
	*/
  public int getAdjudicateDate() {
    return StaticTool.bcToRoc(this.adjudicateDate);
  }

/**
	* 更生方案認可裁定日<br>
	* 
  *
  * @param adjudicateDate 更生方案認可裁定日
  * @throws LogicException when Date Is Warn	*/
  public void setAdjudicateDate(int adjudicateDate) throws LogicException {
    this.adjudicateDate = StaticTool.rocToBc(adjudicateDate);
  }

/**
	* 更生債權金融機構家數<br>
	* 
	* @return Integer
	*/
  public int getBankCount() {
    return this.bankCount;
  }

/**
	* 更生債權金融機構家數<br>
	* 
  *
  * @param bankCount 更生債權金融機構家數
	*/
  public void setBankCount(int bankCount) {
    this.bankCount = bankCount;
  }

/**
	* 債權金融機構代號1<br>
	* 
	* @return String
	*/
  public String getBank1() {
    return this.bank1 == null ? "" : this.bank1;
  }

/**
	* 債權金融機構代號1<br>
	* 
  *
  * @param bank1 債權金融機構代號1
	*/
  public void setBank1(String bank1) {
    this.bank1 = bank1;
  }

/**
	* 債權金融機構代號2<br>
	* 
	* @return String
	*/
  public String getBank2() {
    return this.bank2 == null ? "" : this.bank2;
  }

/**
	* 債權金融機構代號2<br>
	* 
  *
  * @param bank2 債權金融機構代號2
	*/
  public void setBank2(String bank2) {
    this.bank2 = bank2;
  }

/**
	* 債權金融機構代號3<br>
	* 
	* @return String
	*/
  public String getBank3() {
    return this.bank3 == null ? "" : this.bank3;
  }

/**
	* 債權金融機構代號3<br>
	* 
  *
  * @param bank3 債權金融機構代號3
	*/
  public void setBank3(String bank3) {
    this.bank3 = bank3;
  }

/**
	* 債權金融機構代號4<br>
	* 
	* @return String
	*/
  public String getBank4() {
    return this.bank4 == null ? "" : this.bank4;
  }

/**
	* 債權金融機構代號4<br>
	* 
  *
  * @param bank4 債權金融機構代號4
	*/
  public void setBank4(String bank4) {
    this.bank4 = bank4;
  }

/**
	* 債權金融機構代號5<br>
	* 
	* @return String
	*/
  public String getBank5() {
    return this.bank5 == null ? "" : this.bank5;
  }

/**
	* 債權金融機構代號5<br>
	* 
  *
  * @param bank5 債權金融機構代號5
	*/
  public void setBank5(String bank5) {
    this.bank5 = bank5;
  }

/**
	* 債權金融機構代號6<br>
	* 
	* @return String
	*/
  public String getBank6() {
    return this.bank6 == null ? "" : this.bank6;
  }

/**
	* 債權金融機構代號6<br>
	* 
  *
  * @param bank6 債權金融機構代號6
	*/
  public void setBank6(String bank6) {
    this.bank6 = bank6;
  }

/**
	* 債權金融機構代號7<br>
	* 
	* @return String
	*/
  public String getBank7() {
    return this.bank7 == null ? "" : this.bank7;
  }

/**
	* 債權金融機構代號7<br>
	* 
  *
  * @param bank7 債權金融機構代號7
	*/
  public void setBank7(String bank7) {
    this.bank7 = bank7;
  }

/**
	* 債權金融機構代號8<br>
	* 
	* @return String
	*/
  public String getBank8() {
    return this.bank8 == null ? "" : this.bank8;
  }

/**
	* 債權金融機構代號8<br>
	* 
  *
  * @param bank8 債權金融機構代號8
	*/
  public void setBank8(String bank8) {
    this.bank8 = bank8;
  }

/**
	* 債權金融機構代號9<br>
	* 
	* @return String
	*/
  public String getBank9() {
    return this.bank9 == null ? "" : this.bank9;
  }

/**
	* 債權金融機構代號9<br>
	* 
  *
  * @param bank9 債權金融機構代號9
	*/
  public void setBank9(String bank9) {
    this.bank9 = bank9;
  }

/**
	* 債權金融機構代號10<br>
	* 
	* @return String
	*/
  public String getBank10() {
    return this.bank10 == null ? "" : this.bank10;
  }

/**
	* 債權金融機構代號10<br>
	* 
  *
  * @param bank10 債權金融機構代號10
	*/
  public void setBank10(String bank10) {
    this.bank10 = bank10;
  }

/**
	* 債權金融機構代號11<br>
	* 
	* @return String
	*/
  public String getBank11() {
    return this.bank11 == null ? "" : this.bank11;
  }

/**
	* 債權金融機構代號11<br>
	* 
  *
  * @param bank11 債權金融機構代號11
	*/
  public void setBank11(String bank11) {
    this.bank11 = bank11;
  }

/**
	* 債權金融機構代號12<br>
	* 
	* @return String
	*/
  public String getBank12() {
    return this.bank12 == null ? "" : this.bank12;
  }

/**
	* 債權金融機構代號12<br>
	* 
  *
  * @param bank12 債權金融機構代號12
	*/
  public void setBank12(String bank12) {
    this.bank12 = bank12;
  }

/**
	* 債權金融機構代號13<br>
	* 
	* @return String
	*/
  public String getBank13() {
    return this.bank13 == null ? "" : this.bank13;
  }

/**
	* 債權金融機構代號13<br>
	* 
  *
  * @param bank13 債權金融機構代號13
	*/
  public void setBank13(String bank13) {
    this.bank13 = bank13;
  }

/**
	* 債權金融機構代號14<br>
	* 
	* @return String
	*/
  public String getBank14() {
    return this.bank14 == null ? "" : this.bank14;
  }

/**
	* 債權金融機構代號14<br>
	* 
  *
  * @param bank14 債權金融機構代號14
	*/
  public void setBank14(String bank14) {
    this.bank14 = bank14;
  }

/**
	* 債權金融機構代號15<br>
	* 
	* @return String
	*/
  public String getBank15() {
    return this.bank15 == null ? "" : this.bank15;
  }

/**
	* 債權金融機構代號15<br>
	* 
  *
  * @param bank15 債權金融機構代號15
	*/
  public void setBank15(String bank15) {
    this.bank15 = bank15;
  }

/**
	* 債權金融機構代號16<br>
	* 
	* @return String
	*/
  public String getBank16() {
    return this.bank16 == null ? "" : this.bank16;
  }

/**
	* 債權金融機構代號16<br>
	* 
  *
  * @param bank16 債權金融機構代號16
	*/
  public void setBank16(String bank16) {
    this.bank16 = bank16;
  }

/**
	* 債權金融機構代號17<br>
	* 
	* @return String
	*/
  public String getBank17() {
    return this.bank17 == null ? "" : this.bank17;
  }

/**
	* 債權金融機構代號17<br>
	* 
  *
  * @param bank17 債權金融機構代號17
	*/
  public void setBank17(String bank17) {
    this.bank17 = bank17;
  }

/**
	* 債權金融機構代號18<br>
	* 
	* @return String
	*/
  public String getBank18() {
    return this.bank18 == null ? "" : this.bank18;
  }

/**
	* 債權金融機構代號18<br>
	* 
  *
  * @param bank18 債權金融機構代號18
	*/
  public void setBank18(String bank18) {
    this.bank18 = bank18;
  }

/**
	* 債權金融機構代號19<br>
	* 
	* @return String
	*/
  public String getBank19() {
    return this.bank19 == null ? "" : this.bank19;
  }

/**
	* 債權金融機構代號19<br>
	* 
  *
  * @param bank19 債權金融機構代號19
	*/
  public void setBank19(String bank19) {
    this.bank19 = bank19;
  }

/**
	* 債權金融機構代號20<br>
	* 
	* @return String
	*/
  public String getBank20() {
    return this.bank20 == null ? "" : this.bank20;
  }

/**
	* 債權金融機構代號20<br>
	* 
  *
  * @param bank20 債權金融機構代號20
	*/
  public void setBank20(String bank20) {
    this.bank20 = bank20;
  }

/**
	* 債權金融機構代號21<br>
	* 
	* @return String
	*/
  public String getBank21() {
    return this.bank21 == null ? "" : this.bank21;
  }

/**
	* 債權金融機構代號21<br>
	* 
  *
  * @param bank21 債權金融機構代號21
	*/
  public void setBank21(String bank21) {
    this.bank21 = bank21;
  }

/**
	* 債權金融機構代號22<br>
	* 
	* @return String
	*/
  public String getBank22() {
    return this.bank22 == null ? "" : this.bank22;
  }

/**
	* 債權金融機構代號22<br>
	* 
  *
  * @param bank22 債權金融機構代號22
	*/
  public void setBank22(String bank22) {
    this.bank22 = bank22;
  }

/**
	* 債權金融機構代號23<br>
	* 
	* @return String
	*/
  public String getBank23() {
    return this.bank23 == null ? "" : this.bank23;
  }

/**
	* 債權金融機構代號23<br>
	* 
  *
  * @param bank23 債權金融機構代號23
	*/
  public void setBank23(String bank23) {
    this.bank23 = bank23;
  }

/**
	* 債權金融機構代號24<br>
	* 
	* @return String
	*/
  public String getBank24() {
    return this.bank24 == null ? "" : this.bank24;
  }

/**
	* 債權金融機構代號24<br>
	* 
  *
  * @param bank24 債權金融機構代號24
	*/
  public void setBank24(String bank24) {
    this.bank24 = bank24;
  }

/**
	* 債權金融機構代號25<br>
	* 
	* @return String
	*/
  public String getBank25() {
    return this.bank25 == null ? "" : this.bank25;
  }

/**
	* 債權金融機構代號25<br>
	* 
  *
  * @param bank25 債權金融機構代號25
	*/
  public void setBank25(String bank25) {
    this.bank25 = bank25;
  }

/**
	* 債權金融機構代號26<br>
	* 
	* @return String
	*/
  public String getBank26() {
    return this.bank26 == null ? "" : this.bank26;
  }

/**
	* 債權金融機構代號26<br>
	* 
  *
  * @param bank26 債權金融機構代號26
	*/
  public void setBank26(String bank26) {
    this.bank26 = bank26;
  }

/**
	* 債權金融機構代號27<br>
	* 
	* @return String
	*/
  public String getBank27() {
    return this.bank27 == null ? "" : this.bank27;
  }

/**
	* 債權金融機構代號27<br>
	* 
  *
  * @param bank27 債權金融機構代號27
	*/
  public void setBank27(String bank27) {
    this.bank27 = bank27;
  }

/**
	* 債權金融機構代號28<br>
	* 
	* @return String
	*/
  public String getBank28() {
    return this.bank28 == null ? "" : this.bank28;
  }

/**
	* 債權金融機構代號28<br>
	* 
  *
  * @param bank28 債權金融機構代號28
	*/
  public void setBank28(String bank28) {
    this.bank28 = bank28;
  }

/**
	* 債權金融機構代號29<br>
	* 
	* @return String
	*/
  public String getBank29() {
    return this.bank29 == null ? "" : this.bank29;
  }

/**
	* 債權金融機構代號29<br>
	* 
  *
  * @param bank29 債權金融機構代號29
	*/
  public void setBank29(String bank29) {
    this.bank29 = bank29;
  }

/**
	* 債權金融機構代號30<br>
	* 
	* @return String
	*/
  public String getBank30() {
    return this.bank30 == null ? "" : this.bank30;
  }

/**
	* 債權金融機構代號30<br>
	* 
  *
  * @param bank30 債權金融機構代號30
	*/
  public void setBank30(String bank30) {
    this.bank30 = bank30;
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
    return "JcicZ570Log [jcicZ570LogId=" + jcicZ570LogId + ", tranKey=" + tranKey + ", adjudicateDate=" + adjudicateDate + ", bankCount=" + bankCount + ", bank1=" + bank1
           + ", bank2=" + bank2 + ", bank3=" + bank3 + ", bank4=" + bank4 + ", bank5=" + bank5 + ", bank6=" + bank6 + ", bank7=" + bank7
           + ", bank8=" + bank8 + ", bank9=" + bank9 + ", bank10=" + bank10 + ", bank11=" + bank11 + ", bank12=" + bank12 + ", bank13=" + bank13
           + ", bank14=" + bank14 + ", bank15=" + bank15 + ", bank16=" + bank16 + ", bank17=" + bank17 + ", bank18=" + bank18 + ", bank19=" + bank19
           + ", bank20=" + bank20 + ", bank21=" + bank21 + ", bank22=" + bank22 + ", bank23=" + bank23 + ", bank24=" + bank24 + ", bank25=" + bank25
           + ", bank26=" + bank26 + ", bank27=" + bank27 + ", bank28=" + bank28 + ", bank29=" + bank29 + ", bank30=" + bank30 + ", outJcicTxtDate=" + outJcicTxtDate
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
