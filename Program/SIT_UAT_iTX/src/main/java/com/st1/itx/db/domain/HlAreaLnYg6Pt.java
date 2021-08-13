package com.st1.itx.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * HlAreaLnYg6Pt 匯出用HlAreaLnYg6Pt<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`HlAreaLnYg6Pt`")
public class HlAreaLnYg6Pt implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -7881033314667884477L;

@EmbeddedId
  private HlAreaLnYg6PtId hlAreaLnYg6PtId;

  // 年月份
  @Column(name = "`WorkYM`", length = 10, insertable = false, updatable = false)
  private String workYM;

  // 單位代號
  @Column(name = "`AreaUnitNo`", length = 6, insertable = false, updatable = false)
  private String areaUnitNo;

  // 上月達成件數
  @Column(name = "`LstAppNum`")
  private BigDecimal lstAppNum = new BigDecimal("0");

  // 上月達成金額
  @Column(name = "`LstAppAmt`")
  private BigDecimal lstAppAmt = new BigDecimal("0");

  // 本月達成件數
  @Column(name = "`TisAppNum`")
  private BigDecimal tisAppNum = new BigDecimal("0");

  // 本月達成金額
  @Column(name = "`TisAppAmt`")
  private BigDecimal tisAppAmt = new BigDecimal("0");

  // 年月日
  @Column(name = "`CalDate`", length = 10)
  private String calDate;

  // UpdateIdentifier
  /* 恆為1 */
  @Column(name = "`UpNo`")
  private int upNo = 0;

  // 更新日期
  @Column(name = "`ProcessDate`")
  private int processDate = 0;

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


  public HlAreaLnYg6PtId getHlAreaLnYg6PtId() {
    return this.hlAreaLnYg6PtId;
  }

  public void setHlAreaLnYg6PtId(HlAreaLnYg6PtId hlAreaLnYg6PtId) {
    this.hlAreaLnYg6PtId = hlAreaLnYg6PtId;
  }

/**
	* 年月份<br>
	* 
	* @return String
	*/
  public String getWorkYM() {
    return this.workYM == null ? "" : this.workYM;
  }

/**
	* 年月份<br>
	* 
  *
  * @param workYM 年月份
	*/
  public void setWorkYM(String workYM) {
    this.workYM = workYM;
  }

/**
	* 單位代號<br>
	* 
	* @return String
	*/
  public String getAreaUnitNo() {
    return this.areaUnitNo == null ? "" : this.areaUnitNo;
  }

/**
	* 單位代號<br>
	* 
  *
  * @param areaUnitNo 單位代號
	*/
  public void setAreaUnitNo(String areaUnitNo) {
    this.areaUnitNo = areaUnitNo;
  }

/**
	* 上月達成件數<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLstAppNum() {
    return this.lstAppNum;
  }

/**
	* 上月達成件數<br>
	* 
  *
  * @param lstAppNum 上月達成件數
	*/
  public void setLstAppNum(BigDecimal lstAppNum) {
    this.lstAppNum = lstAppNum;
  }

/**
	* 上月達成金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLstAppAmt() {
    return this.lstAppAmt;
  }

/**
	* 上月達成金額<br>
	* 
  *
  * @param lstAppAmt 上月達成金額
	*/
  public void setLstAppAmt(BigDecimal lstAppAmt) {
    this.lstAppAmt = lstAppAmt;
  }

/**
	* 本月達成件數<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getTisAppNum() {
    return this.tisAppNum;
  }

/**
	* 本月達成件數<br>
	* 
  *
  * @param tisAppNum 本月達成件數
	*/
  public void setTisAppNum(BigDecimal tisAppNum) {
    this.tisAppNum = tisAppNum;
  }

/**
	* 本月達成金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getTisAppAmt() {
    return this.tisAppAmt;
  }

/**
	* 本月達成金額<br>
	* 
  *
  * @param tisAppAmt 本月達成金額
	*/
  public void setTisAppAmt(BigDecimal tisAppAmt) {
    this.tisAppAmt = tisAppAmt;
  }

/**
	* 年月日<br>
	* 
	* @return String
	*/
  public String getCalDate() {
    return this.calDate == null ? "" : this.calDate;
  }

/**
	* 年月日<br>
	* 
  *
  * @param calDate 年月日
	*/
  public void setCalDate(String calDate) {
    this.calDate = calDate;
  }

/**
	* UpdateIdentifier<br>
	* 恆為1
	* @return Integer
	*/
  public int getUpNo() {
    return this.upNo;
  }

/**
	* UpdateIdentifier<br>
	* 恆為1
  *
  * @param upNo UpdateIdentifier
	*/
  public void setUpNo(int upNo) {
    this.upNo = upNo;
  }

/**
	* 更新日期<br>
	* 
	* @return Integer
	*/
  public int getProcessDate() {
    return StaticTool.bcToRoc(this.processDate);
  }

/**
	* 更新日期<br>
	* 
  *
  * @param processDate 更新日期
  * @throws LogicException when Date Is Warn	*/
  public void setProcessDate(int processDate) throws LogicException {
    this.processDate = StaticTool.rocToBc(processDate);
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
    return "HlAreaLnYg6Pt [hlAreaLnYg6PtId=" + hlAreaLnYg6PtId + ", lstAppNum=" + lstAppNum + ", lstAppAmt=" + lstAppAmt + ", tisAppNum=" + tisAppNum + ", tisAppAmt=" + tisAppAmt
           + ", calDate=" + calDate + ", upNo=" + upNo + ", processDate=" + processDate + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
