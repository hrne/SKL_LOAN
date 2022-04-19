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
 * HlThreeDetail 房貸換算業績網頁查詢檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`HlThreeDetail`")
public class HlThreeDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6035965181715861947L;

	@EmbeddedId
	private HlThreeDetailId hlThreeDetailId;

	// 營業單位別
	@Column(name = "`CusBNo`", length = 2, insertable = false, updatable = false)
	private String cusBNo;

	// 借款人戶號
	@Column(name = "`HlCusNo`", insertable = false, updatable = false)
	private BigDecimal hlCusNo = new BigDecimal("0");

	// 額度編號
	@Column(name = "`AmountNo`", length = 3, insertable = false, updatable = false)
	private String amountNo;

	// 已用額度
	@Column(name = "`AplAmount`")
	private BigDecimal aplAmount = new BigDecimal("0");

	// 計件代碼
	@Column(name = "`CaseNo`", length = 1, insertable = false, updatable = false)
	private String caseNo;

	// 是否已計件
	@Column(name = "`IfCal`", length = 1)
	private String ifCal;

	// 累計達成金額
	@Column(name = "`TActAmt`", length = 30)
	private String tActAmt;

	// 員工代號
	@Column(name = "`EmpNo`", length = 10)
	private String empNo;

	// 統一編號
	@Column(name = "`EmpId`", length = 10)
	private String empId;

	// 員工姓名
	@Column(name = "`HlEmpName`", length = 15)
	private String hlEmpName;

	// 部室代號
	@Column(name = "`DeptNo`", length = 6)
	private String deptNo;

	// 區部代號
	@Column(name = "`BranchNo`", length = 50)
	private String branchNo;

	// 單位代號
	@Column(name = "`UnitNo`", length = 6)
	private String unitNo;

	// 部室中文
	@Column(name = "`DeptName`", length = 20)
	private String deptName;

	// 區部中文
	@Column(name = "`BranchName`", length = 20)
	private String branchName;

	// 單位中文
	@Column(name = "`UnitName`", length = 20)
	private String unitName;

	// 首次撥款日
	@Column(name = "`FirAppDate`", length = 8)
	private String firAppDate;

	// 基本利率代碼
	@Column(name = "`BiReteNo`", length = 2)
	private String biReteNo;

	// 二階換算業績
	@Column(name = "`TwoYag`")
	private BigDecimal twoYag = new BigDecimal("0");

	// 三階換算業績
	@Column(name = "`ThreeYag`")
	private BigDecimal threeYag = new BigDecimal("0");

	// 二階業務報酬
	@Column(name = "`TwoPay`")
	private BigDecimal twoPay = new BigDecimal("0");

	// 三階業務報酬
	@Column(name = "`ThreePay`")
	private BigDecimal threePay = new BigDecimal("0");

	// 統一編號
	@Column(name = "`UnitChiefNo`", length = 10)
	private String unitChiefNo;

	// 員工姓名
	@Column(name = "`UnitChiefName`", length = 15)
	private String unitChiefName;

	// 統一編號
	@Column(name = "`AreaChiefNo`", length = 10)
	private String areaChiefNo;

	// 員工姓名
	@Column(name = "`AreaChiefName`", length = 15)
	private String areaChiefName;

	// 統一編號
	@Column(name = "`Id3`", length = 10)
	private String id3;

	// 員工姓名
	@Column(name = "`Id3Name`", length = 15)
	private String id3Name;

	// 統一編號
	@Column(name = "`TeamChiefNo`", length = 10)
	private String teamChiefNo;

	// 員工姓名
	@Column(name = "`TeamChiefName`", length = 15)
	private String teamChiefName;

	// 統一編號
	@Column(name = "`Id0`", length = 10)
	private String id0;

	// 員工姓名
	@Column(name = "`Id0Name`", length = 15)
	private String id0Name;

	// UpdateIdentifier
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

	public HlThreeDetailId getHlThreeDetailId() {
		return this.hlThreeDetailId;
	}

	public void setHlThreeDetailId(HlThreeDetailId hlThreeDetailId) {
		this.hlThreeDetailId = hlThreeDetailId;
	}

	/**
	 * 營業單位別<br>
	 * 
	 * @return String
	 */
	public String getCusBNo() {
		return this.cusBNo == null ? "" : this.cusBNo;
	}

	/**
	 * 營業單位別<br>
	 * 
	 *
	 * @param cusBNo 營業單位別
	 */
	public void setCusBNo(String cusBNo) {
		this.cusBNo = cusBNo;
	}

	/**
	 * 借款人戶號<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getHlCusNo() {
		return this.hlCusNo;
	}

	/**
	 * 借款人戶號<br>
	 * 
	 *
	 * @param hlCusNo 借款人戶號
	 */
	public void setHlCusNo(BigDecimal hlCusNo) {
		this.hlCusNo = hlCusNo;
	}

	/**
	 * 額度編號<br>
	 * 
	 * @return String
	 */
	public String getAmountNo() {
		return this.amountNo == null ? "" : this.amountNo;
	}

	/**
	 * 額度編號<br>
	 * 
	 *
	 * @param amountNo 額度編號
	 */
	public void setAmountNo(String amountNo) {
		this.amountNo = amountNo;
	}

	/**
	 * 已用額度<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAplAmount() {
		return this.aplAmount;
	}

	/**
	 * 已用額度<br>
	 * 
	 *
	 * @param aplAmount 已用額度
	 */
	public void setAplAmount(BigDecimal aplAmount) {
		this.aplAmount = aplAmount;
	}

	/**
	 * 計件代碼<br>
	 * 
	 * @return String
	 */
	public String getCaseNo() {
		return this.caseNo == null ? "" : this.caseNo;
	}

	/**
	 * 計件代碼<br>
	 * 
	 *
	 * @param caseNo 計件代碼
	 */
	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	/**
	 * 是否已計件<br>
	 * 
	 * @return String
	 */
	public String getIfCal() {
		return this.ifCal == null ? "" : this.ifCal;
	}

	/**
	 * 是否已計件<br>
	 * 
	 *
	 * @param ifCal 是否已計件
	 */
	public void setIfCal(String ifCal) {
		this.ifCal = ifCal;
	}

	/**
	 * 累計達成金額<br>
	 * 
	 * @return String
	 */
	public String getTActAmt() {
		return this.tActAmt == null ? "" : this.tActAmt;
	}

	/**
	 * 累計達成金額<br>
	 * 
	 *
	 * @param tActAmt 累計達成金額
	 */
	public void setTActAmt(String tActAmt) {
		this.tActAmt = tActAmt;
	}

	/**
	 * 員工代號<br>
	 * 
	 * @return String
	 */
	public String getEmpNo() {
		return this.empNo == null ? "" : this.empNo;
	}

	/**
	 * 員工代號<br>
	 * 
	 *
	 * @param empNo 員工代號
	 */
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	/**
	 * 統一編號<br>
	 * 
	 * @return String
	 */
	public String getEmpId() {
		return this.empId == null ? "" : this.empId;
	}

	/**
	 * 統一編號<br>
	 * 
	 *
	 * @param empId 統一編號
	 */
	public void setEmpId(String empId) {
		this.empId = empId;
	}

	/**
	 * 員工姓名<br>
	 * 
	 * @return String
	 */
	public String getHlEmpName() {
		return this.hlEmpName == null ? "" : this.hlEmpName;
	}

	/**
	 * 員工姓名<br>
	 * 
	 *
	 * @param hlEmpName 員工姓名
	 */
	public void setHlEmpName(String hlEmpName) {
		this.hlEmpName = hlEmpName;
	}

	/**
	 * 部室代號<br>
	 * 
	 * @return String
	 */
	public String getDeptNo() {
		return this.deptNo == null ? "" : this.deptNo;
	}

	/**
	 * 部室代號<br>
	 * 
	 *
	 * @param deptNo 部室代號
	 */
	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}

	/**
	 * 區部代號<br>
	 * 
	 * @return String
	 */
	public String getBranchNo() {
		return this.branchNo == null ? "" : this.branchNo;
	}

	/**
	 * 區部代號<br>
	 * 
	 *
	 * @param branchNo 區部代號
	 */
	public void setBranchNo(String branchNo) {
		this.branchNo = branchNo;
	}

	/**
	 * 單位代號<br>
	 * 
	 * @return String
	 */
	public String getUnitNo() {
		return this.unitNo == null ? "" : this.unitNo;
	}

	/**
	 * 單位代號<br>
	 * 
	 *
	 * @param unitNo 單位代號
	 */
	public void setUnitNo(String unitNo) {
		this.unitNo = unitNo;
	}

	/**
	 * 部室中文<br>
	 * 
	 * @return String
	 */
	public String getDeptName() {
		return this.deptName == null ? "" : this.deptName;
	}

	/**
	 * 部室中文<br>
	 * 
	 *
	 * @param deptName 部室中文
	 */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	/**
	 * 區部中文<br>
	 * 
	 * @return String
	 */
	public String getBranchName() {
		return this.branchName == null ? "" : this.branchName;
	}

	/**
	 * 區部中文<br>
	 * 
	 *
	 * @param branchName 區部中文
	 */
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	/**
	 * 單位中文<br>
	 * 
	 * @return String
	 */
	public String getUnitName() {
		return this.unitName == null ? "" : this.unitName;
	}

	/**
	 * 單位中文<br>
	 * 
	 *
	 * @param unitName 單位中文
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	/**
	 * 首次撥款日<br>
	 * 
	 * @return String
	 */
	public String getFirAppDate() {
		return this.firAppDate == null ? "" : this.firAppDate;
	}

	/**
	 * 首次撥款日<br>
	 * 
	 *
	 * @param firAppDate 首次撥款日
	 */
	public void setFirAppDate(String firAppDate) {
		this.firAppDate = firAppDate;
	}

	/**
	 * 基本利率代碼<br>
	 * 
	 * @return String
	 */
	public String getBiReteNo() {
		return this.biReteNo == null ? "" : this.biReteNo;
	}

	/**
	 * 基本利率代碼<br>
	 * 
	 *
	 * @param biReteNo 基本利率代碼
	 */
	public void setBiReteNo(String biReteNo) {
		this.biReteNo = biReteNo;
	}

	/**
	 * 二階換算業績<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getTwoYag() {
		return this.twoYag;
	}

	/**
	 * 二階換算業績<br>
	 * 
	 *
	 * @param twoYag 二階換算業績
	 */
	public void setTwoYag(BigDecimal twoYag) {
		this.twoYag = twoYag;
	}

	/**
	 * 三階換算業績<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getThreeYag() {
		return this.threeYag;
	}

	/**
	 * 三階換算業績<br>
	 * 
	 *
	 * @param threeYag 三階換算業績
	 */
	public void setThreeYag(BigDecimal threeYag) {
		this.threeYag = threeYag;
	}

	/**
	 * 二階業務報酬<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getTwoPay() {
		return this.twoPay;
	}

	/**
	 * 二階業務報酬<br>
	 * 
	 *
	 * @param twoPay 二階業務報酬
	 */
	public void setTwoPay(BigDecimal twoPay) {
		this.twoPay = twoPay;
	}

	/**
	 * 三階業務報酬<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getThreePay() {
		return this.threePay;
	}

	/**
	 * 三階業務報酬<br>
	 * 
	 *
	 * @param threePay 三階業務報酬
	 */
	public void setThreePay(BigDecimal threePay) {
		this.threePay = threePay;
	}

	/**
	 * 統一編號<br>
	 * 
	 * @return String
	 */
	public String getUnitChiefNo() {
		return this.unitChiefNo == null ? "" : this.unitChiefNo;
	}

	/**
	 * 統一編號<br>
	 * 
	 *
	 * @param unitChiefNo 統一編號
	 */
	public void setUnitChiefNo(String unitChiefNo) {
		this.unitChiefNo = unitChiefNo;
	}

	/**
	 * 員工姓名<br>
	 * 
	 * @return String
	 */
	public String getUnitChiefName() {
		return this.unitChiefName == null ? "" : this.unitChiefName;
	}

	/**
	 * 員工姓名<br>
	 * 
	 *
	 * @param unitChiefName 員工姓名
	 */
	public void setUnitChiefName(String unitChiefName) {
		this.unitChiefName = unitChiefName;
	}

	/**
	 * 統一編號<br>
	 * 
	 * @return String
	 */
	public String getAreaChiefNo() {
		return this.areaChiefNo == null ? "" : this.areaChiefNo;
	}

	/**
	 * 統一編號<br>
	 * 
	 *
	 * @param areaChiefNo 統一編號
	 */
	public void setAreaChiefNo(String areaChiefNo) {
		this.areaChiefNo = areaChiefNo;
	}

	/**
	 * 員工姓名<br>
	 * 
	 * @return String
	 */
	public String getAreaChiefName() {
		return this.areaChiefName == null ? "" : this.areaChiefName;
	}

	/**
	 * 員工姓名<br>
	 * 
	 *
	 * @param areaChiefName 員工姓名
	 */
	public void setAreaChiefName(String areaChiefName) {
		this.areaChiefName = areaChiefName;
	}

	/**
	 * 統一編號<br>
	 * 
	 * @return String
	 */
	public String getId3() {
		return this.id3 == null ? "" : this.id3;
	}

	/**
	 * 統一編號<br>
	 * 
	 *
	 * @param id3 統一編號
	 */
	public void setId3(String id3) {
		this.id3 = id3;
	}

	/**
	 * 員工姓名<br>
	 * 
	 * @return String
	 */
	public String getId3Name() {
		return this.id3Name == null ? "" : this.id3Name;
	}

	/**
	 * 員工姓名<br>
	 * 
	 *
	 * @param id3Name 員工姓名
	 */
	public void setId3Name(String id3Name) {
		this.id3Name = id3Name;
	}

	/**
	 * 統一編號<br>
	 * 
	 * @return String
	 */
	public String getTeamChiefNo() {
		return this.teamChiefNo == null ? "" : this.teamChiefNo;
	}

	/**
	 * 統一編號<br>
	 * 
	 *
	 * @param teamChiefNo 統一編號
	 */
	public void setTeamChiefNo(String teamChiefNo) {
		this.teamChiefNo = teamChiefNo;
	}

	/**
	 * 員工姓名<br>
	 * 
	 * @return String
	 */
	public String getTeamChiefName() {
		return this.teamChiefName == null ? "" : this.teamChiefName;
	}

	/**
	 * 員工姓名<br>
	 * 
	 *
	 * @param teamChiefName 員工姓名
	 */
	public void setTeamChiefName(String teamChiefName) {
		this.teamChiefName = teamChiefName;
	}

	/**
	 * 統一編號<br>
	 * 
	 * @return String
	 */
	public String getId0() {
		return this.id0 == null ? "" : this.id0;
	}

	/**
	 * 統一編號<br>
	 * 
	 *
	 * @param id0 統一編號
	 */
	public void setId0(String id0) {
		this.id0 = id0;
	}

	/**
	 * 員工姓名<br>
	 * 
	 * @return String
	 */
	public String getId0Name() {
		return this.id0Name == null ? "" : this.id0Name;
	}

	/**
	 * 員工姓名<br>
	 * 
	 *
	 * @param id0Name 員工姓名
	 */
	public void setId0Name(String id0Name) {
		this.id0Name = id0Name;
	}

	/**
	 * UpdateIdentifier<br>
	 * 
	 * @return Integer
	 */
	public int getUpNo() {
		return this.upNo;
	}

	/**
	 * UpdateIdentifier<br>
	 * 
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
	 * @throws LogicException when Date Is Warn
	 */
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
		return "HlThreeDetail [hlThreeDetailId=" + hlThreeDetailId + ", aplAmount=" + aplAmount + ", ifCal=" + ifCal + ", tActAmt=" + tActAmt + ", empNo=" + empNo + ", empId=" + empId + ", hlEmpName="
				+ hlEmpName + ", deptNo=" + deptNo + ", branchNo=" + branchNo + ", unitNo=" + unitNo + ", deptName=" + deptName + ", branchName=" + branchName + ", unitName=" + unitName
				+ ", firAppDate=" + firAppDate + ", biReteNo=" + biReteNo + ", twoYag=" + twoYag + ", threeYag=" + threeYag + ", twoPay=" + twoPay + ", threePay=" + threePay + ", unitChiefNo="
				+ unitChiefNo + ", unitChiefName=" + unitChiefName + ", areaChiefNo=" + areaChiefNo + ", areaChiefName=" + areaChiefName + ", id3=" + id3 + ", id3Name=" + id3Name + ", teamChiefNo="
				+ teamChiefNo + ", teamChiefName=" + teamChiefName + ", id0=" + id0 + ", id0Name=" + id0Name + ", upNo=" + upNo + ", processDate=" + processDate + ", createDate=" + createDate
				+ ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
