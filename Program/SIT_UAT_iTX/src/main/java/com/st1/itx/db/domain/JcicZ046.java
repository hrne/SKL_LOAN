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
 * JcicZ046 結案通知資料檔案格式<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ046`")
public class JcicZ046 implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 2246529320322514069L;

@EmbeddedId
  private JcicZ046Id jcicZ046Id;

  // 交易代碼
  /* A:新增C:異動D:刪除 */
  @Column(name = "`TranKey`", length = 1)
  private String tranKey;

  // 報送單位代號
  /* 三位文數字 */
  @Column(name = "`SubmitKey`", length = 3, insertable = false, updatable = false)
  private String submitKey;

  // 債務人IDN
  @Column(name = "`CustId`", length = 10, insertable = false, updatable = false)
  private String custId;

  // 協商申請日
  @Column(name = "`RcDate`", insertable = false, updatable = false)
  private int rcDate = 0;

  // 結案原因代號
  /* 00:毀諾01:協商終止11:未能接受足以負擔之還款方案12:要求折讓本金未為金融機構所接受13:要求撤銷原已協商通過之還款方案並要求更優惠還款方案14:無法負擔任何還款條件15:本行/本公司未能於文件齊全後30日內開始協商17:協商意願低落18:債務人於協商前大量借款或密集消費19:債務人於最大債權金融機構通知簽署協議書10日曆天內未完成簽約手續21:資產大於負債49:其他(協商不成立)53:經最大債權金融機構通知面談後兩次無故不到場面談55:債務人主動撤案，終止協商56:與債務人聯絡多日（多次），仍無法聯繫上89:其他(協商自始未開始)90:毀諾後清償95:申請資格不符96:債務人透過代辦業者申請，經勸導自行撤件。97:資料key值報送錯誤，本行結案98:依規定轉他行承辦，本行結案99:依債務清償方案履行完畢 */
  @Column(name = "`CloseCode`", length = 2)
  private String closeCode;

  // 毀諾原因代號
  /* 01:債務人失業02:債務人收入減少03:債務人支出增加04:債務人往生05:債務人入獄06:債務人欲聲請前置調解/更生/清算07:債務人失聯或聯絡困難 */
  @Column(name = "`BreakCode`", length = 2)
  private String breakCode;

  // 結案日期
  @Column(name = "`CloseDate`", insertable = false, updatable = false)
  private int closeDate = 0;

  // 轉出JCIC文字檔日期
  @Column(name = "`OutJcicTxtDate`")
  private int outJcicTxtDate = 0;

  // 流水號
  @Column(name = "`Ukey`", length = 32)
  private String ukey;

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


  public JcicZ046Id getJcicZ046Id() {
    return this.jcicZ046Id;
  }

  public void setJcicZ046Id(JcicZ046Id jcicZ046Id) {
    this.jcicZ046Id = jcicZ046Id;
  }

/**
	* 交易代碼<br>
	* A:新增C:異動D:刪除
	* @return String
	*/
  public String getTranKey() {
    return this.tranKey == null ? "" : this.tranKey;
  }

/**
	* 交易代碼<br>
	* A:新增C:異動D:刪除
  *
  * @param tranKey 交易代碼
	*/
  public void setTranKey(String tranKey) {
    this.tranKey = tranKey;
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
	* 協商申請日<br>
	* 
	* @return Integer
	*/
  public int getRcDate() {
    return StaticTool.bcToRoc(this.rcDate);
  }

/**
	* 協商申請日<br>
	* 
  *
  * @param rcDate 協商申請日
  * @throws LogicException when Date Is Warn	*/
  public void setRcDate(int rcDate) throws LogicException {
    this.rcDate = StaticTool.rocToBc(rcDate);
  }

/**
	* 結案原因代號<br>
	* 00:毀諾
01:協商終止
11:未能接受足以負擔之還款方案
12:要求折讓本金未為金融機構所接受
13:要求撤銷原已協商通過之還款方案並要求更優惠還款方案
14:無法負擔任何還款條件
15:本行/本公司未能於文件齊全後30日內開始協商
17:協商意願低落
18:債務人於協商前大量借款或密集消費
19:債務人於最大債權金融機構通知簽署協議書10日曆天內未完成簽約手續
21:資產大於負債
49:其他(協商不成立)
53:經最大債權金融機構通知面談後兩次無故不到場面談
55:債務人主動撤案，終止協商
56:與債務人聯絡多日（多次），仍無法聯繫上
89:其他(協商自始未開始)
90:毀諾後清償
95:申請資格不符
96:債務人透過代辦業者申請，經勸導自行撤件。
97:資料key值報送錯誤，本行結案
98:依規定轉他行承辦，本行結案
99:依債務清償方案履行完畢
	* @return String
	*/
  public String getCloseCode() {
    return this.closeCode == null ? "" : this.closeCode;
  }

/**
	* 結案原因代號<br>
	* 00:毀諾
01:協商終止
11:未能接受足以負擔之還款方案
12:要求折讓本金未為金融機構所接受
13:要求撤銷原已協商通過之還款方案並要求更優惠還款方案
14:無法負擔任何還款條件
15:本行/本公司未能於文件齊全後30日內開始協商
17:協商意願低落
18:債務人於協商前大量借款或密集消費
19:債務人於最大債權金融機構通知簽署協議書10日曆天內未完成簽約手續
21:資產大於負債
49:其他(協商不成立)
53:經最大債權金融機構通知面談後兩次無故不到場面談
55:債務人主動撤案，終止協商
56:與債務人聯絡多日（多次），仍無法聯繫上
89:其他(協商自始未開始)
90:毀諾後清償
95:申請資格不符
96:債務人透過代辦業者申請，經勸導自行撤件。
97:資料key值報送錯誤，本行結案
98:依規定轉他行承辦，本行結案
99:依債務清償方案履行完畢
  *
  * @param closeCode 結案原因代號
	*/
  public void setCloseCode(String closeCode) {
    this.closeCode = closeCode;
  }

/**
	* 毀諾原因代號<br>
	* 01:債務人失業
02:債務人收入減少
03:債務人支出增加
04:債務人往生
05:債務人入獄
06:債務人欲聲請前置調解/更生/清算
07:債務人失聯或聯絡困難
	* @return String
	*/
  public String getBreakCode() {
    return this.breakCode == null ? "" : this.breakCode;
  }

/**
	* 毀諾原因代號<br>
	* 01:債務人失業
02:債務人收入減少
03:債務人支出增加
04:債務人往生
05:債務人入獄
06:債務人欲聲請前置調解/更生/清算
07:債務人失聯或聯絡困難
  *
  * @param breakCode 毀諾原因代號
	*/
  public void setBreakCode(String breakCode) {
    this.breakCode = breakCode;
  }

/**
	* 結案日期<br>
	* 
	* @return Integer
	*/
  public int getCloseDate() {
    return StaticTool.bcToRoc(this.closeDate);
  }

/**
	* 結案日期<br>
	* 
  *
  * @param closeDate 結案日期
  * @throws LogicException when Date Is Warn	*/
  public void setCloseDate(int closeDate) throws LogicException {
    this.closeDate = StaticTool.rocToBc(closeDate);
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
    return "JcicZ046 [jcicZ046Id=" + jcicZ046Id + ", tranKey=" + tranKey + ", closeCode=" + closeCode + ", breakCode=" + breakCode
           + ", outJcicTxtDate=" + outJcicTxtDate + ", ukey=" + ukey + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
