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
 * InnReCheck 覆審案件明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`InnReCheck`")
public class InnReCheck implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -7254578420467145174L;

@EmbeddedId
  private InnReCheckId innReCheckId;

  // 資料年月
  @Column(name = "`YearMonth`", insertable = false, updatable = false)
  private int yearMonth = 0;

  // 條件代碼
  /* 01-個金3000萬以上02-企金3000萬以上03-個金2000萬以上小於3000萬04-個金100萬以上小於2000萬05-企金未達3000萬06-土地追蹤 */
  @Column(name = "`ConditionCode`", insertable = false, updatable = false)
  private int conditionCode = 0;

  // 借款人戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 額度號碼
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 覆審記號
  /* 1:免覆審 2:要覆審 3:不覆審 ; 空白 */
  @Column(name = "`ReCheckCode`", length = 1)
  private String reCheckCode;

  // 追蹤記號
  /* 1:免追蹤 2:要追蹤 3:不追蹤 ; 空白 */
  @Column(name = "`FollowMark`", length = 1)
  private String followMark;

  // 覆審年月
  @Column(name = "`ReChkYearMonth`")
  private int reChkYearMonth = 0;

  // 撥款日期
  @Column(name = "`DrawdownDate`")
  private int drawdownDate = 0;

  // 貸放餘額
  @Column(name = "`LoanBal`")
  private BigDecimal loanBal = new BigDecimal("0");

  // 評等
  @Column(name = "`Evaluation`")
  private int evaluation = 0;

  // 客戶別
  /* 中文 */
  @Column(name = "`CustTypeItem`", length = 10)
  private String custTypeItem;

  // 用途別
  /* 中文 */
  @Column(name = "`UsageItem`", length = 10)
  private String usageItem;

  // 地區別
  /* 中文 */
  @Column(name = "`CityItem`", length = 10)
  private String cityItem;

  // 應覆審單位
  /* 中文，同區域中心 */
  @Column(name = "`ReChkUnit`", length = 10)
  private String reChkUnit;

  // 備註
  @Column(name = "`Remark`", length = 60)
  private String remark;

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


  public InnReCheckId getInnReCheckId() {
    return this.innReCheckId;
  }

  public void setInnReCheckId(InnReCheckId innReCheckId) {
    this.innReCheckId = innReCheckId;
  }

/**
	* 資料年月<br>
	* 
	* @return Integer
	*/
  public int getYearMonth() {
    return this.yearMonth;
  }

/**
	* 資料年月<br>
	* 
  *
  * @param yearMonth 資料年月
	*/
  public void setYearMonth(int yearMonth) {
    this.yearMonth = yearMonth;
  }

/**
	* 條件代碼<br>
	* 01-個金3000萬以上
02-企金3000萬以上
03-個金2000萬以上小於3000萬
04-個金100萬以上小於2000萬
05-企金未達3000萬
06-土地追蹤
	* @return Integer
	*/
  public int getConditionCode() {
    return this.conditionCode;
  }

/**
	* 條件代碼<br>
	* 01-個金3000萬以上
02-企金3000萬以上
03-個金2000萬以上小於3000萬
04-個金100萬以上小於2000萬
05-企金未達3000萬
06-土地追蹤
  *
  * @param conditionCode 條件代碼
	*/
  public void setConditionCode(int conditionCode) {
    this.conditionCode = conditionCode;
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
	* 覆審記號<br>
	* 1:免覆審 2:要覆審 3:不覆審 ; 空白
	* @return String
	*/
  public String getReCheckCode() {
    return this.reCheckCode == null ? "" : this.reCheckCode;
  }

/**
	* 覆審記號<br>
	* 1:免覆審 2:要覆審 3:不覆審 ; 空白
  *
  * @param reCheckCode 覆審記號
	*/
  public void setReCheckCode(String reCheckCode) {
    this.reCheckCode = reCheckCode;
  }

/**
	* 追蹤記號<br>
	* 1:免追蹤 2:要追蹤 3:不追蹤 ; 空白
	* @return String
	*/
  public String getFollowMark() {
    return this.followMark == null ? "" : this.followMark;
  }

/**
	* 追蹤記號<br>
	* 1:免追蹤 2:要追蹤 3:不追蹤 ; 空白
  *
  * @param followMark 追蹤記號
	*/
  public void setFollowMark(String followMark) {
    this.followMark = followMark;
  }

/**
	* 覆審年月<br>
	* 
	* @return Integer
	*/
  public int getReChkYearMonth() {
    return this.reChkYearMonth;
  }

/**
	* 覆審年月<br>
	* 
  *
  * @param reChkYearMonth 覆審年月
	*/
  public void setReChkYearMonth(int reChkYearMonth) {
    this.reChkYearMonth = reChkYearMonth;
  }

/**
	* 撥款日期<br>
	* 
	* @return Integer
	*/
  public int getDrawdownDate() {
    return StaticTool.bcToRoc(this.drawdownDate);
  }

/**
	* 撥款日期<br>
	* 
  *
  * @param drawdownDate 撥款日期
  * @throws LogicException when Date Is Warn	*/
  public void setDrawdownDate(int drawdownDate) throws LogicException {
    this.drawdownDate = StaticTool.rocToBc(drawdownDate);
  }

/**
	* 貸放餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLoanBal() {
    return this.loanBal;
  }

/**
	* 貸放餘額<br>
	* 
  *
  * @param loanBal 貸放餘額
	*/
  public void setLoanBal(BigDecimal loanBal) {
    this.loanBal = loanBal;
  }

/**
	* 評等<br>
	* 
	* @return Integer
	*/
  public int getEvaluation() {
    return this.evaluation;
  }

/**
	* 評等<br>
	* 
  *
  * @param evaluation 評等
	*/
  public void setEvaluation(int evaluation) {
    this.evaluation = evaluation;
  }

/**
	* 客戶別<br>
	* 中文
	* @return String
	*/
  public String getCustTypeItem() {
    return this.custTypeItem == null ? "" : this.custTypeItem;
  }

/**
	* 客戶別<br>
	* 中文
  *
  * @param custTypeItem 客戶別
	*/
  public void setCustTypeItem(String custTypeItem) {
    this.custTypeItem = custTypeItem;
  }

/**
	* 用途別<br>
	* 中文
	* @return String
	*/
  public String getUsageItem() {
    return this.usageItem == null ? "" : this.usageItem;
  }

/**
	* 用途別<br>
	* 中文
  *
  * @param usageItem 用途別
	*/
  public void setUsageItem(String usageItem) {
    this.usageItem = usageItem;
  }

/**
	* 地區別<br>
	* 中文
	* @return String
	*/
  public String getCityItem() {
    return this.cityItem == null ? "" : this.cityItem;
  }

/**
	* 地區別<br>
	* 中文
  *
  * @param cityItem 地區別
	*/
  public void setCityItem(String cityItem) {
    this.cityItem = cityItem;
  }

/**
	* 應覆審單位<br>
	* 中文，同區域中心
	* @return String
	*/
  public String getReChkUnit() {
    return this.reChkUnit == null ? "" : this.reChkUnit;
  }

/**
	* 應覆審單位<br>
	* 中文，同區域中心
  *
  * @param reChkUnit 應覆審單位
	*/
  public void setReChkUnit(String reChkUnit) {
    this.reChkUnit = reChkUnit;
  }

/**
	* 備註<br>
	* 
	* @return String
	*/
  public String getRemark() {
    return this.remark == null ? "" : this.remark;
  }

/**
	* 備註<br>
	* 
  *
  * @param remark 備註
	*/
  public void setRemark(String remark) {
    this.remark = remark;
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
    return "InnReCheck [innReCheckId=" + innReCheckId + ", reCheckCode=" + reCheckCode + ", followMark=" + followMark
           + ", reChkYearMonth=" + reChkYearMonth + ", drawdownDate=" + drawdownDate + ", loanBal=" + loanBal + ", evaluation=" + evaluation + ", custTypeItem=" + custTypeItem + ", usageItem=" + usageItem
           + ", cityItem=" + cityItem + ", reChkUnit=" + reChkUnit + ", remark=" + remark + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
