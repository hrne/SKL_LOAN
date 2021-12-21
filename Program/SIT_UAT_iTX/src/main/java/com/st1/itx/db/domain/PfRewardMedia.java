package com.st1.itx.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * PfRewardMedia 獎金媒體發放檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`PfRewardMedia`")
public class PfRewardMedia implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7050970612646871653L;

// 系統序號
	@Id
	@Column(name = "`BonusNo`")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "`PfRewardMedia_SEQ`")
	@SequenceGenerator(name = "`PfRewardMedia_SEQ`", sequenceName = "`PfRewardMedia_SEQ`", allocationSize = 1)
	private Long bonusNo = 0L;

	// 獎金發放日
	@Column(name = "`BonusDate`")
	private int bonusDate = 0;

	// 業績日期
	/* 撥款日/追回日 */
	@Column(name = "`PerfDate`")
	private int perfDate = 0;

	// 戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度編號
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 撥款序號
	/* 協辦獎金、加碼獎金，撥款序號為0 */
	@Column(name = "`BormNo`")
	private int bormNo = 0;

	// 獎金類別
	/*
	 * CdCode.BonusType1:介紹獎金(L5511寫入)2:放款業務專員津貼(暫時取消)3:晤談一人員津貼(目前沒有)4:晤談二人員津貼(目前沒有)
	 * 5:協辦獎金(L5511寫入)6:專業獎勵金(L5407，初階授信通過時寫入，只寫一次)7:加碼獎金(L5512寫入)(依照LN270檔-獎勵金發放檔)
	 */
	@Column(name = "`BonusType`")
	private int bonusType = 0;

	// 獎金發放員工編號
	@Column(name = "`EmployeeNo`", length = 6)
	private String employeeNo;

	// 商品代碼
	/* FacMain.ProdNo 商品代碼 */
	@Column(name = "`ProdCode`", length = 5)
	private String prodCode;

	// 計件代碼
	/* FacMain.PieceCode 計件代碼 */
	@Column(name = "`PieceCode`", length = 1)
	private String pieceCode;

	// 原始獎金
	/* 實發(追回時,為負值)(預設值PfReward等相關獎金) */
	@Column(name = "`Bonus`")
	private BigDecimal bonus = new BigDecimal("0");

	// 發放獎金
	/* 實發(追回時,為負值)(預設值PfReward等相關獎金) */
	@Column(name = "`AdjustBonus`")
	private BigDecimal adjustBonus = new BigDecimal("0");

	// 調整獎金日期
	@Column(name = "`AdjustBonusDate`")
	private int adjustBonusDate = 0;

	// 工作月
	/* 業績年月 */
	@Column(name = "`WorkMonth`")
	private int workMonth = 0;

	// 工作季
	@Column(name = "`WorkSeason`")
	private int workSeason = 0;

	// 備註
	@Column(name = "`Remark`", length = 50)
	private String remark;

	// 產出媒體檔記號
	/* 0:尚未產生媒體檔1:已產生發放媒體-(不可刪除與異動) */
	@Column(name = "`MediaFg`")
	private int mediaFg = 0;

	// 產出媒體檔日期
	@Column(name = "`MediaDate`")
	private int mediaDate = 0;

	// 人工新增記號
	/* 0:PfReward轉檔進來1:由人工新增 */
	@Column(name = "`ManualFg`")
	private int manualFg = 0;

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
	 * 系統序號<br>
	 * 
	 * @return Long
	 */
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getBonusNo() {
		return this.bonusNo;
	}

	/**
	 * 系統序號<br>
	 * 
	 *
	 * @param bonusNo 系統序號
	 */
	public void setBonusNo(Long bonusNo) {
		this.bonusNo = bonusNo;
	}

	/**
	 * 獎金發放日<br>
	 * 
	 * @return Integer
	 */
	public int getBonusDate() {
		return StaticTool.bcToRoc(this.bonusDate);
	}

	/**
	 * 獎金發放日<br>
	 * 
	 *
	 * @param bonusDate 獎金發放日
	 * @throws LogicException when Date Is Warn
	 */
	public void setBonusDate(int bonusDate) throws LogicException {
		this.bonusDate = StaticTool.rocToBc(bonusDate);
	}

	/**
	 * 業績日期<br>
	 * 撥款日/追回日
	 * 
	 * @return Integer
	 */
	public int getPerfDate() {
		return StaticTool.bcToRoc(this.perfDate);
	}

	/**
	 * 業績日期<br>
	 * 撥款日/追回日
	 *
	 * @param perfDate 業績日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setPerfDate(int perfDate) throws LogicException {
		this.perfDate = StaticTool.rocToBc(perfDate);
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
	 * 撥款序號<br>
	 * 協辦獎金、加碼獎金，撥款序號為0
	 * 
	 * @return Integer
	 */
	public int getBormNo() {
		return this.bormNo;
	}

	/**
	 * 撥款序號<br>
	 * 協辦獎金、加碼獎金，撥款序號為0
	 *
	 * @param bormNo 撥款序號
	 */
	public void setBormNo(int bormNo) {
		this.bormNo = bormNo;
	}

	/**
	 * 獎金類別<br>
	 * CdCode.BonusType 1:介紹獎金(L5511寫入) 2:放款業務專員津貼(暫時取消) 3:晤談一人員津貼(目前沒有)
	 * 4:晤談二人員津貼(目前沒有) 5:協辦獎金(L5511寫入) 6:專業獎勵金(L5407，初階授信通過時寫入，只寫一次) 7:加碼獎金(L5512寫入)
	 * (依照LN270檔-獎勵金發放檔)
	 * 
	 * @return Integer
	 */
	public int getBonusType() {
		return this.bonusType;
	}

	/**
	 * 獎金類別<br>
	 * CdCode.BonusType 1:介紹獎金(L5511寫入) 2:放款業務專員津貼(暫時取消) 3:晤談一人員津貼(目前沒有)
	 * 4:晤談二人員津貼(目前沒有) 5:協辦獎金(L5511寫入) 6:專業獎勵金(L5407，初階授信通過時寫入，只寫一次) 7:加碼獎金(L5512寫入)
	 * (依照LN270檔-獎勵金發放檔)
	 *
	 * @param bonusType 獎金類別
	 */
	public void setBonusType(int bonusType) {
		this.bonusType = bonusType;
	}

	/**
	 * 獎金發放員工編號<br>
	 * 
	 * @return String
	 */
	public String getEmployeeNo() {
		return this.employeeNo == null ? "" : this.employeeNo;
	}

	/**
	 * 獎金發放員工編號<br>
	 * 
	 *
	 * @param employeeNo 獎金發放員工編號
	 */
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	/**
	 * 商品代碼<br>
	 * FacMain.ProdNo 商品代碼
	 * 
	 * @return String
	 */
	public String getProdCode() {
		return this.prodCode == null ? "" : this.prodCode;
	}

	/**
	 * 商品代碼<br>
	 * FacMain.ProdNo 商品代碼
	 *
	 * @param prodCode 商品代碼
	 */
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	/**
	 * 計件代碼<br>
	 * FacMain.PieceCode 計件代碼
	 * 
	 * @return String
	 */
	public String getPieceCode() {
		return this.pieceCode == null ? "" : this.pieceCode;
	}

	/**
	 * 計件代碼<br>
	 * FacMain.PieceCode 計件代碼
	 *
	 * @param pieceCode 計件代碼
	 */
	public void setPieceCode(String pieceCode) {
		this.pieceCode = pieceCode;
	}

	/**
	 * 原始獎金<br>
	 * 實發(追回時,為負值)(預設值PfReward等相關獎金)
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getBonus() {
		return this.bonus;
	}

	/**
	 * 原始獎金<br>
	 * 實發(追回時,為負值)(預設值PfReward等相關獎金)
	 *
	 * @param bonus 原始獎金
	 */
	public void setBonus(BigDecimal bonus) {
		this.bonus = bonus;
	}

	/**
	 * 發放獎金<br>
	 * 實發(追回時,為負值)(預設值PfReward等相關獎金)
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAdjustBonus() {
		return this.adjustBonus;
	}

	/**
	 * 發放獎金<br>
	 * 實發(追回時,為負值)(預設值PfReward等相關獎金)
	 *
	 * @param adjustBonus 發放獎金
	 */
	public void setAdjustBonus(BigDecimal adjustBonus) {
		this.adjustBonus = adjustBonus;
	}

	/**
	 * 調整獎金日期<br>
	 * 
	 * @return Integer
	 */
	public int getAdjustBonusDate() {
		return StaticTool.bcToRoc(this.adjustBonusDate);
	}

	/**
	 * 調整獎金日期<br>
	 * 
	 *
	 * @param adjustBonusDate 調整獎金日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setAdjustBonusDate(int adjustBonusDate) throws LogicException {
		this.adjustBonusDate = StaticTool.rocToBc(adjustBonusDate);
	}

	/**
	 * 工作月<br>
	 * 業績年月
	 * 
	 * @return Integer
	 */
	public int getWorkMonth() {
		return this.workMonth;
	}

	/**
	 * 工作月<br>
	 * 業績年月
	 *
	 * @param workMonth 工作月
	 */
	public void setWorkMonth(int workMonth) {
		this.workMonth = workMonth;
	}

	/**
	 * 工作季<br>
	 * 
	 * @return Integer
	 */
	public int getWorkSeason() {
		return this.workSeason;
	}

	/**
	 * 工作季<br>
	 * 
	 *
	 * @param workSeason 工作季
	 */
	public void setWorkSeason(int workSeason) {
		this.workSeason = workSeason;
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
	 * 產出媒體檔記號<br>
	 * 0:尚未產生媒體檔 1:已產生發放媒體-(不可刪除與異動)
	 * 
	 * @return Integer
	 */
	public int getMediaFg() {
		return this.mediaFg;
	}

	/**
	 * 產出媒體檔記號<br>
	 * 0:尚未產生媒體檔 1:已產生發放媒體-(不可刪除與異動)
	 *
	 * @param mediaFg 產出媒體檔記號
	 */
	public void setMediaFg(int mediaFg) {
		this.mediaFg = mediaFg;
	}

	/**
	 * 產出媒體檔日期<br>
	 * 
	 * @return Integer
	 */
	public int getMediaDate() {
		return StaticTool.bcToRoc(this.mediaDate);
	}

	/**
	 * 產出媒體檔日期<br>
	 * 
	 *
	 * @param mediaDate 產出媒體檔日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setMediaDate(int mediaDate) throws LogicException {
		this.mediaDate = StaticTool.rocToBc(mediaDate);
	}

	/**
	 * 人工新增記號<br>
	 * 0:PfReward轉檔進來 1:由人工新增
	 * 
	 * @return Integer
	 */
	public int getManualFg() {
		return this.manualFg;
	}

	/**
	 * 人工新增記號<br>
	 * 0:PfReward轉檔進來 1:由人工新增
	 *
	 * @param manualFg 人工新增記號
	 */
	public void setManualFg(int manualFg) {
		this.manualFg = manualFg;
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
		return "PfRewardMedia [bonusNo=" + bonusNo + ", bonusDate=" + bonusDate + ", perfDate=" + perfDate + ", custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + ", bonusType="
				+ bonusType + ", employeeNo=" + employeeNo + ", prodCode=" + prodCode + ", pieceCode=" + pieceCode + ", bonus=" + bonus + ", adjustBonus=" + adjustBonus + ", adjustBonusDate="
				+ adjustBonusDate + ", workMonth=" + workMonth + ", workSeason=" + workSeason + ", remark=" + remark + ", mediaFg=" + mediaFg + ", mediaDate=" + mediaDate + ", manualFg=" + manualFg
				+ ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
