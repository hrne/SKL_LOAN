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
 * TxToDoDetailReserve 應處理明細留存檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxToDoDetailReserve`")
public class TxToDoDetailReserve implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 6415262505373599422L;

@EmbeddedId
  private TxToDoDetailReserveId txToDoDetailReserveId;

  // 項目
  @Column(name = "`ItemCode`", length = 6, insertable = false, updatable = false)
  private String itemCode;

  // 借款人戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 撥款序號
  @Column(name = "`BormNo`", insertable = false, updatable = false)
  private int bormNo = 0;

  // 明細鍵值
  /* 應處理清單檔&amp;lt;應處理清單&amp;gt; */
  @Column(name = "`DtlValue`", length = 30, insertable = false, updatable = false)
  private String dtlValue;

  // 資料狀態
  /* 0.未處理1.已保留2.已處理3.已刪除 */
  @Column(name = "`Status`")
  private int status = 0;

  // 處理事項說明
  @Column(name = "`ProcessNote`", length = 300)
  private String processNote;

  // 執行交易
  /* 應處理清單檔&amp;lt;應處理清單&amp;gt; */
  @Column(name = "`ExcuteTxcd`", length = 5)
  private String excuteTxcd;

  // 資料日期
  @Column(name = "`DataDate`")
  private int dataDate = 0;

  // 作帳日期
  @Column(name = "`TitaEntdy`")
  private int titaEntdy = 0;

  // 登錄單位別
  @Column(name = "`TitaKinbr`", length = 4)
  private String titaKinbr;

  // 登錄經辦
  @Column(name = "`TitaTlrNo`", length = 6)
  private String titaTlrNo;

  // 登錄交易序號
  @Column(name = "`TitaTxtNo`")
  private int titaTxtNo = 0;

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


  public TxToDoDetailReserveId getTxToDoDetailReserveId() {
    return this.txToDoDetailReserveId;
  }

  public void setTxToDoDetailReserveId(TxToDoDetailReserveId txToDoDetailReserveId) {
    this.txToDoDetailReserveId = txToDoDetailReserveId;
  }

/**
	* 項目<br>
	* 
	* @return String
	*/
  public String getItemCode() {
    return this.itemCode == null ? "" : this.itemCode;
  }

/**
	* 項目<br>
	* 
  *
  * @param itemCode 項目
	*/
  public void setItemCode(String itemCode) {
    this.itemCode = itemCode;
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
	* 撥款序號<br>
	* 
	* @return Integer
	*/
  public int getBormNo() {
    return this.bormNo;
  }

/**
	* 撥款序號<br>
	* 
  *
  * @param bormNo 撥款序號
	*/
  public void setBormNo(int bormNo) {
    this.bormNo = bormNo;
  }

/**
	* 明細鍵值<br>
	* 應處理清單檔&amp;lt;應處理清單&amp;gt;
	* @return String
	*/
  public String getDtlValue() {
    return this.dtlValue == null ? "" : this.dtlValue;
  }

/**
	* 明細鍵值<br>
	* 應處理清單檔&amp;lt;應處理清單&amp;gt;
  *
  * @param dtlValue 明細鍵值
	*/
  public void setDtlValue(String dtlValue) {
    this.dtlValue = dtlValue;
  }

/**
	* 資料狀態<br>
	* 0.未處理
1.已保留
2.已處理
3.已刪除
	* @return Integer
	*/
  public int getStatus() {
    return this.status;
  }

/**
	* 資料狀態<br>
	* 0.未處理
1.已保留
2.已處理
3.已刪除
  *
  * @param status 資料狀態
	*/
  public void setStatus(int status) {
    this.status = status;
  }

/**
	* 處理事項說明<br>
	* 
	* @return String
	*/
  public String getProcessNote() {
    return this.processNote == null ? "" : this.processNote;
  }

/**
	* 處理事項說明<br>
	* 
  *
  * @param processNote 處理事項說明
	*/
  public void setProcessNote(String processNote) {
    this.processNote = processNote;
  }

/**
	* 執行交易<br>
	* 應處理清單檔&amp;lt;應處理清單&amp;gt;
	* @return String
	*/
  public String getExcuteTxcd() {
    return this.excuteTxcd == null ? "" : this.excuteTxcd;
  }

/**
	* 執行交易<br>
	* 應處理清單檔&amp;lt;應處理清單&amp;gt;
  *
  * @param excuteTxcd 執行交易
	*/
  public void setExcuteTxcd(String excuteTxcd) {
    this.excuteTxcd = excuteTxcd;
  }

/**
	* 資料日期<br>
	* 
	* @return Integer
	*/
  public int getDataDate() {
    return StaticTool.bcToRoc(this.dataDate);
  }

/**
	* 資料日期<br>
	* 
  *
  * @param dataDate 資料日期
  * @throws LogicException when Date Is Warn	*/
  public void setDataDate(int dataDate) throws LogicException {
    this.dataDate = StaticTool.rocToBc(dataDate);
  }

/**
	* 作帳日期<br>
	* 
	* @return Integer
	*/
  public int getTitaEntdy() {
    return StaticTool.bcToRoc(this.titaEntdy);
  }

/**
	* 作帳日期<br>
	* 
  *
  * @param titaEntdy 作帳日期
  * @throws LogicException when Date Is Warn	*/
  public void setTitaEntdy(int titaEntdy) throws LogicException {
    this.titaEntdy = StaticTool.rocToBc(titaEntdy);
  }

/**
	* 登錄單位別<br>
	* 
	* @return String
	*/
  public String getTitaKinbr() {
    return this.titaKinbr == null ? "" : this.titaKinbr;
  }

/**
	* 登錄單位別<br>
	* 
  *
  * @param titaKinbr 登錄單位別
	*/
  public void setTitaKinbr(String titaKinbr) {
    this.titaKinbr = titaKinbr;
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
    return "TxToDoDetailReserve [txToDoDetailReserveId=" + txToDoDetailReserveId + ", status=" + status
           + ", processNote=" + processNote + ", excuteTxcd=" + excuteTxcd + ", dataDate=" + dataDate + ", titaEntdy=" + titaEntdy + ", titaKinbr=" + titaKinbr + ", titaTlrNo=" + titaTlrNo
           + ", titaTxtNo=" + titaTxtNo + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
