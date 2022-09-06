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
 * HlAreaLnYg6Pt 區域中心房貸專員業績統計<br>
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
	private static final long serialVersionUID = -4827419229907470032L;

	@EmbeddedId
	private HlAreaLnYg6PtId hlAreaLnYg6PtId;

	// 年月份
	/* eric 2022.1.6 */
	@Column(name = "`WorkYM`", insertable = false, updatable = false)
	private int workYM = 0;

	// 單位代號
	/* eric 2022.1.6 */
	@Column(name = "`AreaCode`", length = 6, insertable = false, updatable = false)
	private String areaCode;

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
	/* eric 2022.1.6 */
	@Column(name = "`CalDate`")
	private int calDate = 0;

	// UpdateIdentifier
	/* 恆為1 */
	@Column(name = "`UpNo`")
	private int upNo = 0;

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
	 * eric 2022.1.6
	 * 
	 * @return Integer
	 */
	public int getWorkYM() {
		return this.workYM;
	}

	/**
	 * 年月份<br>
	 * eric 2022.1.6
	 *
	 * @param workYM 年月份
	 */
	public void setWorkYM(int workYM) {
		this.workYM = workYM;
	}

	/**
	 * 單位代號<br>
	 * eric 2022.1.6
	 * 
	 * @return String
	 */
	public String getAreaCode() {
		return this.areaCode == null ? "" : this.areaCode;
	}

	/**
	 * 單位代號<br>
	 * eric 2022.1.6
	 *
	 * @param areaCode 單位代號
	 */
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
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
	 * eric 2022.1.6
	 * 
	 * @return Integer
	 */
	public int getCalDate() {
		return StaticTool.bcToRoc(this.calDate);
	}

	/**
	 * 年月日<br>
	 * eric 2022.1.6
	 *
	 * @param calDate 年月日
	 * @throws LogicException when Date Is Warn
	 */
	public void setCalDate(int calDate) throws LogicException {
		this.calDate = StaticTool.rocToBc(calDate);
	}

	/**
	 * UpdateIdentifier<br>
	 * 恆為1
	 * 
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
		return "HlAreaLnYg6Pt [hlAreaLnYg6PtId=" + hlAreaLnYg6PtId + ", lstAppNum=" + lstAppNum + ", lstAppAmt=" + lstAppAmt + ", tisAppNum=" + tisAppNum + ", tisAppAmt=" + tisAppAmt + ", calDate="
				+ calDate + ", upNo=" + upNo + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
