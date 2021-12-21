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
 * LoanIfrsGp IFRS9欄位清單7<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`LoanIfrsGp`")
public class LoanIfrsGp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -690723095972319392L;

	@EmbeddedId
	private LoanIfrsGpId loanIfrsGpId;

	// 年月份
	@Column(name = "`DataYM`", insertable = false, updatable = false)
	private int dataYM = 0;

	// 戶號
	@Column(name = "`CustNo`", insertable = false, updatable = false)
	private int custNo = 0;

	// 借款人ID / 統編
	@Column(name = "`CustId`", length = 10)
	private String custId;

	// 額度編號
	@Column(name = "`FacmNo`", insertable = false, updatable = false)
	private int facmNo = 0;

	// 核准號碼
	@Column(name = "`ApplNo`")
	private int applNo = 0;

	// 撥款序號
	@Column(name = "`BormNo`", insertable = false, updatable = false)
	private int bormNo = 0;

	// 企業戶/個人戶
	/* 1=企業戶2=個人戶依現行代碼，唯企業戶與個人戶之分類需參考信用評等模型。自然人採用企金自然人評等模型者，應歸類為企業戶 */
	@Column(name = "`CustKind`")
	private int custKind = 0;

	// 戶況
	/* 1=正常2=催收 */
	@Column(name = "`Status`")
	private int status = 0;

	// 轉催收款日期
	/* 抓取最近一次的轉催收日期YYYYMMDD 例：20100108 */
	@Column(name = "`OvduDate`")
	private int ovduDate = 0;

	// 原始認列時時信用評等
	/* 資料來源：與eloan系統對接（得以空值提供） */
	@Column(name = "`OriRating`", length = 1)
	private String oriRating;

	// 原始認列時信用評等模型
	/* 資料來源：與eloan系統對接 */
	@Column(name = "`OriModel`", length = 1)
	private String oriModel;

	// 財務報導日時信用評等
	/* 資料來源：與eloan系統對接（得以空值提供） */
	@Column(name = "`Rating`", length = 1)
	private String rating;

	// 財務報導日時信用評等模型
	/* 資料來源：與eloan系統對接 */
	@Column(name = "`Model`", length = 1)
	private String model;

	// 逾期繳款天數
	/* 抓取月底日資料，並以天數表示，例:090(2020/12資料有4位) */
	@Column(name = "`OvduDays`")
	private int ovduDays = 0;

	// 債務人屬企業戶，且其歸戶下任一債務逾期90天(含)以上
	/* 1=是 2=否為IAS39減損客觀條件1若「逾期繳款天數」達90天（含）以上，本欄位應註記為1 */
	@Column(name = "`Stage1`")
	private int stage1 = 0;

	// 個人消費性放款逾期超逾90天(含)以上
	/* 1=是 2=否為IAS39減損客觀條件2若「逾期繳款天數」達90天（含）以上，本欄位應註記為1 */
	@Column(name = "`Stage2`")
	private int stage2 = 0;

	// 債務人屬企業戶，且其歸戶任一債務已經本行轉催收款、或符合列報逾期放款條件、或本行對該債務讓步(如協議)
	/* 1=是 2=否為IAS39減損客觀條件3 */
	@Column(name = "`Stage3`")
	private int stage3 = 0;

	// 個人消費性放款已經本行轉催收、或符合列報逾期放款條件、或本行對該債務讓步(如協議)
	/* 1=是 2=否為IAS39減損客觀條件4 */
	@Column(name = "`Stage4`")
	private int stage4 = 0;

	// 債務人申請重組、破產或其他等程序，而進行該等程序可能使債務人免除或延遲償還債務
	/* 1=是 2=否為IAS39減損客觀條件5 */
	@Column(name = "`Stage5`")
	private int stage5 = 0;

	// 內部違約機率降至D評等
	@Column(name = "`PdFlagToD`", length = 1)
	private String pdFlagToD;

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

	public LoanIfrsGpId getLoanIfrsGpId() {
		return this.loanIfrsGpId;
	}

	public void setLoanIfrsGpId(LoanIfrsGpId loanIfrsGpId) {
		this.loanIfrsGpId = loanIfrsGpId;
	}

	/**
	 * 年月份<br>
	 * 
	 * @return Integer
	 */
	public int getDataYM() {
		return this.dataYM;
	}

	/**
	 * 年月份<br>
	 * 
	 *
	 * @param dataYM 年月份
	 */
	public void setDataYM(int dataYM) {
		this.dataYM = dataYM;
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
	 * 借款人ID / 統編<br>
	 * 
	 * @return String
	 */
	public String getCustId() {
		return this.custId == null ? "" : this.custId;
	}

	/**
	 * 借款人ID / 統編<br>
	 * 
	 *
	 * @param custId 借款人ID / 統編
	 */
	public void setCustId(String custId) {
		this.custId = custId;
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
	 * 核准號碼<br>
	 * 
	 * @return Integer
	 */
	public int getApplNo() {
		return this.applNo;
	}

	/**
	 * 核准號碼<br>
	 * 
	 *
	 * @param applNo 核准號碼
	 */
	public void setApplNo(int applNo) {
		this.applNo = applNo;
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
	 * 企業戶/個人戶<br>
	 * 1=企業戶 2=個人戶 依現行代碼，唯企業戶與個人戶之分類需參考信用評等模型。自然人採用企金自然人評等模型者，應歸類為企業戶
	 * 
	 * @return Integer
	 */
	public int getCustKind() {
		return this.custKind;
	}

	/**
	 * 企業戶/個人戶<br>
	 * 1=企業戶 2=個人戶 依現行代碼，唯企業戶與個人戶之分類需參考信用評等模型。自然人採用企金自然人評等模型者，應歸類為企業戶
	 *
	 * @param custKind 企業戶/個人戶
	 */
	public void setCustKind(int custKind) {
		this.custKind = custKind;
	}

	/**
	 * 戶況<br>
	 * 1=正常 2=催收
	 * 
	 * @return Integer
	 */
	public int getStatus() {
		return this.status;
	}

	/**
	 * 戶況<br>
	 * 1=正常 2=催收
	 *
	 * @param status 戶況
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 轉催收款日期<br>
	 * 抓取最近一次的轉催收日期 YYYYMMDD 例：20100108
	 * 
	 * @return Integer
	 */
	public int getOvduDate() {
		return StaticTool.bcToRoc(this.ovduDate);
	}

	/**
	 * 轉催收款日期<br>
	 * 抓取最近一次的轉催收日期 YYYYMMDD 例：20100108
	 *
	 * @param ovduDate 轉催收款日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setOvduDate(int ovduDate) throws LogicException {
		this.ovduDate = StaticTool.rocToBc(ovduDate);
	}

	/**
	 * 原始認列時時信用評等<br>
	 * 資料來源：與eloan系統對接（得以空值提供）
	 * 
	 * @return String
	 */
	public String getOriRating() {
		return this.oriRating == null ? "" : this.oriRating;
	}

	/**
	 * 原始認列時時信用評等<br>
	 * 資料來源：與eloan系統對接（得以空值提供）
	 *
	 * @param oriRating 原始認列時時信用評等
	 */
	public void setOriRating(String oriRating) {
		this.oriRating = oriRating;
	}

	/**
	 * 原始認列時信用評等模型<br>
	 * 資料來源：與eloan系統對接
	 * 
	 * @return String
	 */
	public String getOriModel() {
		return this.oriModel == null ? "" : this.oriModel;
	}

	/**
	 * 原始認列時信用評等模型<br>
	 * 資料來源：與eloan系統對接
	 *
	 * @param oriModel 原始認列時信用評等模型
	 */
	public void setOriModel(String oriModel) {
		this.oriModel = oriModel;
	}

	/**
	 * 財務報導日時信用評等<br>
	 * 資料來源：與eloan系統對接（得以空值提供）
	 * 
	 * @return String
	 */
	public String getRating() {
		return this.rating == null ? "" : this.rating;
	}

	/**
	 * 財務報導日時信用評等<br>
	 * 資料來源：與eloan系統對接（得以空值提供）
	 *
	 * @param rating 財務報導日時信用評等
	 */
	public void setRating(String rating) {
		this.rating = rating;
	}

	/**
	 * 財務報導日時信用評等模型<br>
	 * 資料來源：與eloan系統對接
	 * 
	 * @return String
	 */
	public String getModel() {
		return this.model == null ? "" : this.model;
	}

	/**
	 * 財務報導日時信用評等模型<br>
	 * 資料來源：與eloan系統對接
	 *
	 * @param model 財務報導日時信用評等模型
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * 逾期繳款天數<br>
	 * 抓取月底日資料，並以天數表示，例:090 (2020/12資料有4位)
	 * 
	 * @return Integer
	 */
	public int getOvduDays() {
		return StaticTool.bcToRoc(this.ovduDays);
	}

	/**
	 * 逾期繳款天數<br>
	 * 抓取月底日資料，並以天數表示，例:090 (2020/12資料有4位)
	 *
	 * @param ovduDays 逾期繳款天數
	 * @throws LogicException when Date Is Warn
	 */
	public void setOvduDays(int ovduDays) throws LogicException {
		this.ovduDays = StaticTool.rocToBc(ovduDays);
	}

	/**
	 * 債務人屬企業戶，且其歸戶下任一債務逾期90天(含)以上<br>
	 * 1=是 2=否 為IAS39減損客觀條件1 若「逾期繳款天數」達90天（含）以上，本欄位應註記為1
	 * 
	 * @return Integer
	 */
	public int getStage1() {
		return StaticTool.bcToRoc(this.stage1);
	}

	/**
	 * 債務人屬企業戶，且其歸戶下任一債務逾期90天(含)以上<br>
	 * 1=是 2=否 為IAS39減損客觀條件1 若「逾期繳款天數」達90天（含）以上，本欄位應註記為1
	 *
	 * @param stage1 債務人屬企業戶，且其歸戶下任一債務逾期90天(含)以上
	 * @throws LogicException when Date Is Warn
	 */
	public void setStage1(int stage1) throws LogicException {
		this.stage1 = StaticTool.rocToBc(stage1);
	}

	/**
	 * 個人消費性放款逾期超逾90天(含)以上<br>
	 * 1=是 2=否 為IAS39減損客觀條件2 若「逾期繳款天數」達90天（含）以上，本欄位應註記為1
	 * 
	 * @return Integer
	 */
	public int getStage2() {
		return StaticTool.bcToRoc(this.stage2);
	}

	/**
	 * 個人消費性放款逾期超逾90天(含)以上<br>
	 * 1=是 2=否 為IAS39減損客觀條件2 若「逾期繳款天數」達90天（含）以上，本欄位應註記為1
	 *
	 * @param stage2 個人消費性放款逾期超逾90天(含)以上
	 * @throws LogicException when Date Is Warn
	 */
	public void setStage2(int stage2) throws LogicException {
		this.stage2 = StaticTool.rocToBc(stage2);
	}

	/**
	 * 債務人屬企業戶，且其歸戶任一債務已經本行轉催收款、或符合列報逾期放款條件、或本行對該債務讓步(如協議)<br>
	 * 1=是 2=否 為IAS39減損客觀條件3
	 * 
	 * @return Integer
	 */
	public int getStage3() {
		return StaticTool.bcToRoc(this.stage3);
	}

	/**
	 * 債務人屬企業戶，且其歸戶任一債務已經本行轉催收款、或符合列報逾期放款條件、或本行對該債務讓步(如協議)<br>
	 * 1=是 2=否 為IAS39減損客觀條件3
	 *
	 * @param stage3 債務人屬企業戶，且其歸戶任一債務已經本行轉催收款、或符合列報逾期放款條件、或本行對該債務讓步(如協議)
	 * @throws LogicException when Date Is Warn
	 */
	public void setStage3(int stage3) throws LogicException {
		this.stage3 = StaticTool.rocToBc(stage3);
	}

	/**
	 * 個人消費性放款已經本行轉催收、或符合列報逾期放款條件、或本行對該債務讓步(如協議)<br>
	 * 1=是 2=否 為IAS39減損客觀條件4
	 * 
	 * @return Integer
	 */
	public int getStage4() {
		return StaticTool.bcToRoc(this.stage4);
	}

	/**
	 * 個人消費性放款已經本行轉催收、或符合列報逾期放款條件、或本行對該債務讓步(如協議)<br>
	 * 1=是 2=否 為IAS39減損客觀條件4
	 *
	 * @param stage4 個人消費性放款已經本行轉催收、或符合列報逾期放款條件、或本行對該債務讓步(如協議)
	 * @throws LogicException when Date Is Warn
	 */
	public void setStage4(int stage4) throws LogicException {
		this.stage4 = StaticTool.rocToBc(stage4);
	}

	/**
	 * 債務人申請重組、破產或其他等程序，而進行該等程序可能使債務人免除或延遲償還債務<br>
	 * 1=是 2=否 為IAS39減損客觀條件5
	 * 
	 * @return Integer
	 */
	public int getStage5() {
		return StaticTool.bcToRoc(this.stage5);
	}

	/**
	 * 債務人申請重組、破產或其他等程序，而進行該等程序可能使債務人免除或延遲償還債務<br>
	 * 1=是 2=否 為IAS39減損客觀條件5
	 *
	 * @param stage5 債務人申請重組、破產或其他等程序，而進行該等程序可能使債務人免除或延遲償還債務
	 * @throws LogicException when Date Is Warn
	 */
	public void setStage5(int stage5) throws LogicException {
		this.stage5 = StaticTool.rocToBc(stage5);
	}

	/**
	 * 內部違約機率降至D評等<br>
	 * 
	 * @return String
	 */
	public String getPdFlagToD() {
		return this.pdFlagToD == null ? "" : this.pdFlagToD;
	}

	/**
	 * 內部違約機率降至D評等<br>
	 * 
	 *
	 * @param pdFlagToD 內部違約機率降至D評等
	 */
	public void setPdFlagToD(String pdFlagToD) {
		this.pdFlagToD = pdFlagToD;
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
		return "LoanIfrsGp [loanIfrsGpId=" + loanIfrsGpId + ", custId=" + custId + ", applNo=" + applNo + ", custKind=" + custKind + ", status=" + status + ", ovduDate=" + ovduDate + ", oriRating="
				+ oriRating + ", oriModel=" + oriModel + ", rating=" + rating + ", model=" + model + ", ovduDays=" + ovduDays + ", stage1=" + stage1 + ", stage2=" + stage2 + ", stage3=" + stage3
				+ ", stage4=" + stage4 + ", stage5=" + stage5 + ", pdFlagToD=" + pdFlagToD + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
				+ ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
