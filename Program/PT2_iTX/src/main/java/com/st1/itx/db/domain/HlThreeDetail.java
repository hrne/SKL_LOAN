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
 * HlThreeDetail 介紹人業績明細檔<br>
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
	private static final long serialVersionUID = 2881037445802794193L;

	@EmbeddedId
	private HlThreeDetailId hlThreeDetailId;

	// 營業單位別
	@Column(name = "`BrNo`", length = 4, insertable = false, updatable = false)
	private String brNo;

	// 借款人戶號
	@Column(name = "`CustNo`", insertable = false, updatable = false)
	private int custNo = 0;

	// 額度編號
	@Column(name = "`FacmNo`", insertable = false, updatable = false)
	private int facmNo = 0;

	// 已用額度
	@Column(name = "`ActAmt`")
	private BigDecimal actAmt = new BigDecimal("0");

	// 計件代碼
	@Column(name = "`PieceCode`", length = 1)
	private String pieceCode;

	// 是否已計件
	@Column(name = "`CntingCode`", length = 1)
	private String cntingCode;

	// 累計已用額度
	@Column(name = "`TActAmt`")
	private BigDecimal tActAmt = new BigDecimal("0");

	// 員工代號(介紹人)
	@Column(name = "`EmpNo`", length = 6)
	private String empNo;

	// 統一編號(介紹人)
	@Column(name = "`EmpId`", length = 10)
	private String empId;

	// 員工姓名(介紹人)
	@Column(name = "`EmpName`", length = 15)
	private String empName;

	// 部室代號
	@Column(name = "`DeptCode`", length = 6)
	private String deptCode;

	// 區部代號
	@Column(name = "`DistCode`", length = 6)
	private String distCode;

	// 單位代號
	@Column(name = "`UnitCode`", length = 6)
	private String unitCode;

	// 部室中文
	@Column(name = "`DeptName`", length = 20)
	private String deptName;

	// 區部中文
	@Column(name = "`DistName`", length = 20)
	private String distName;

	// 單位中文
	@Column(name = "`UnitName`", length = 20)
	private String unitName;

	// 首次撥款日
	@Column(name = "`FirAppDate`")
	private int firAppDate = 0;

	// 商品代碼/基本利率代碼
	@Column(name = "`BiReteNo`", length = 5)
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

	// 統一編號(單位主管/處長)
	/* CdEmp.AgLevel='E' */
	@Column(name = "`UnitChiefNo`", length = 10)
	private String unitChiefNo;

	// 員工姓名
	@Column(name = "`UnitChiefName`", length = 15)
	private String unitChiefName;

	// 統一編號(主任)
	/* CdEmp.AgLevel='H' */
	@Column(name = "`AreaChiefNo`", length = 10)
	private String areaChiefNo;

	// 員工姓名
	@Column(name = "`AreaChiefName`", length = 15)
	private String areaChiefName;

	// 統一編號(組長)
	/* CdEmp.AgLevel='K' */
	@Column(name = "`Id3`", length = 10)
	private String id3;

	// 員工姓名
	@Column(name = "`Id3Name`", length = 15)
	private String id3Name;

	// 統一編號(展業代表)
	/* CdEmp.AgLevel='Z' */
	@Column(name = "`TeamChiefNo`", length = 10)
	private String teamChiefNo;

	// 員工姓名
	@Column(name = "`TeamChiefName`", length = 15)
	private String teamChiefName;

	// 統一編號
	/* AS400範例檔目前空白 */
	@Column(name = "`Id0`", length = 10)
	private String id0;

	// 員工姓名
	/* AS400範例檔目前空白 */
	@Column(name = "`Id0Name`", length = 15)
	private String id0Name;

	// UpdateIdentifier
	/* default 1 */
	@Column(name = "`UpNo`")
	private int upNo = 0;

	// 更新日期
	@Column(name = "`CalDate`")
	private int calDate = 0;

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
	public String getBrNo() {
		return this.brNo == null ? "" : this.brNo;
	}

	/**
	 * 營業單位別<br>
	 * 
	 *
	 * @param brNo 營業單位別
	 */
	public void setBrNo(String brNo) {
		this.brNo = brNo;
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
	 * 已用額度<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getActAmt() {
		return this.actAmt;
	}

	/**
	 * 已用額度<br>
	 * 
	 *
	 * @param actAmt 已用額度
	 */
	public void setActAmt(BigDecimal actAmt) {
		this.actAmt = actAmt;
	}

	/**
	 * 計件代碼<br>
	 * 
	 * @return String
	 */
	public String getPieceCode() {
		return this.pieceCode == null ? "" : this.pieceCode;
	}

	/**
	 * 計件代碼<br>
	 * 
	 *
	 * @param pieceCode 計件代碼
	 */
	public void setPieceCode(String pieceCode) {
		this.pieceCode = pieceCode;
	}

	/**
	 * 是否已計件<br>
	 * 
	 * @return String
	 */
	public String getCntingCode() {
		return this.cntingCode == null ? "" : this.cntingCode;
	}

	/**
	 * 是否已計件<br>
	 * 
	 *
	 * @param cntingCode 是否已計件
	 */
	public void setCntingCode(String cntingCode) {
		this.cntingCode = cntingCode;
	}

	/**
	 * 累計已用額度<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getTActAmt() {
		return this.tActAmt;
	}

	/**
	 * 累計已用額度<br>
	 * 
	 *
	 * @param tActAmt 累計已用額度
	 */
	public void setTActAmt(BigDecimal tActAmt) {
		this.tActAmt = tActAmt;
	}

	/**
	 * 員工代號(介紹人)<br>
	 * 
	 * @return String
	 */
	public String getEmpNo() {
		return this.empNo == null ? "" : this.empNo;
	}

	/**
	 * 員工代號(介紹人)<br>
	 * 
	 *
	 * @param empNo 員工代號(介紹人)
	 */
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	/**
	 * 統一編號(介紹人)<br>
	 * 
	 * @return String
	 */
	public String getEmpId() {
		return this.empId == null ? "" : this.empId;
	}

	/**
	 * 統一編號(介紹人)<br>
	 * 
	 *
	 * @param empId 統一編號(介紹人)
	 */
	public void setEmpId(String empId) {
		this.empId = empId;
	}

	/**
	 * 員工姓名(介紹人)<br>
	 * 
	 * @return String
	 */
	public String getEmpName() {
		return this.empName == null ? "" : this.empName;
	}

	/**
	 * 員工姓名(介紹人)<br>
	 * 
	 *
	 * @param empName 員工姓名(介紹人)
	 */
	public void setEmpName(String empName) {
		this.empName = empName;
	}

	/**
	 * 部室代號<br>
	 * 
	 * @return String
	 */
	public String getDeptCode() {
		return this.deptCode == null ? "" : this.deptCode;
	}

	/**
	 * 部室代號<br>
	 * 
	 *
	 * @param deptCode 部室代號
	 */
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	/**
	 * 區部代號<br>
	 * 
	 * @return String
	 */
	public String getDistCode() {
		return this.distCode == null ? "" : this.distCode;
	}

	/**
	 * 區部代號<br>
	 * 
	 *
	 * @param distCode 區部代號
	 */
	public void setDistCode(String distCode) {
		this.distCode = distCode;
	}

	/**
	 * 單位代號<br>
	 * 
	 * @return String
	 */
	public String getUnitCode() {
		return this.unitCode == null ? "" : this.unitCode;
	}

	/**
	 * 單位代號<br>
	 * 
	 *
	 * @param unitCode 單位代號
	 */
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
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
	public String getDistName() {
		return this.distName == null ? "" : this.distName;
	}

	/**
	 * 區部中文<br>
	 * 
	 *
	 * @param distName 區部中文
	 */
	public void setDistName(String distName) {
		this.distName = distName;
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
	 * @return Integer
	 */
	public int getFirAppDate() {
		return StaticTool.bcToRoc(this.firAppDate);
	}

	/**
	 * 首次撥款日<br>
	 * 
	 *
	 * @param firAppDate 首次撥款日
	 * @throws LogicException when Date Is Warn
	 */
	public void setFirAppDate(int firAppDate) throws LogicException {
		this.firAppDate = StaticTool.rocToBc(firAppDate);
	}

	/**
	 * 商品代碼/基本利率代碼<br>
	 * 
	 * @return String
	 */
	public String getBiReteNo() {
		return this.biReteNo == null ? "" : this.biReteNo;
	}

	/**
	 * 商品代碼/基本利率代碼<br>
	 * 
	 *
	 * @param biReteNo 商品代碼/基本利率代碼
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
	 * 統一編號(單位主管/處長)<br>
	 * CdEmp.AgLevel='E'
	 * 
	 * @return String
	 */
	public String getUnitChiefNo() {
		return this.unitChiefNo == null ? "" : this.unitChiefNo;
	}

	/**
	 * 統一編號(單位主管/處長)<br>
	 * CdEmp.AgLevel='E'
	 *
	 * @param unitChiefNo 統一編號(單位主管/處長)
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
	 * 統一編號(主任)<br>
	 * CdEmp.AgLevel='H'
	 * 
	 * @return String
	 */
	public String getAreaChiefNo() {
		return this.areaChiefNo == null ? "" : this.areaChiefNo;
	}

	/**
	 * 統一編號(主任)<br>
	 * CdEmp.AgLevel='H'
	 *
	 * @param areaChiefNo 統一編號(主任)
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
	 * 統一編號(組長)<br>
	 * CdEmp.AgLevel='K'
	 * 
	 * @return String
	 */
	public String getId3() {
		return this.id3 == null ? "" : this.id3;
	}

	/**
	 * 統一編號(組長)<br>
	 * CdEmp.AgLevel='K'
	 *
	 * @param id3 統一編號(組長)
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
	 * 統一編號(展業代表)<br>
	 * CdEmp.AgLevel='Z'
	 * 
	 * @return String
	 */
	public String getTeamChiefNo() {
		return this.teamChiefNo == null ? "" : this.teamChiefNo;
	}

	/**
	 * 統一編號(展業代表)<br>
	 * CdEmp.AgLevel='Z'
	 *
	 * @param teamChiefNo 統一編號(展業代表)
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
	 * AS400範例檔目前空白
	 * 
	 * @return String
	 */
	public String getId0() {
		return this.id0 == null ? "" : this.id0;
	}

	/**
	 * 統一編號<br>
	 * AS400範例檔目前空白
	 *
	 * @param id0 統一編號
	 */
	public void setId0(String id0) {
		this.id0 = id0;
	}

	/**
	 * 員工姓名<br>
	 * AS400範例檔目前空白
	 * 
	 * @return String
	 */
	public String getId0Name() {
		return this.id0Name == null ? "" : this.id0Name;
	}

	/**
	 * 員工姓名<br>
	 * AS400範例檔目前空白
	 *
	 * @param id0Name 員工姓名
	 */
	public void setId0Name(String id0Name) {
		this.id0Name = id0Name;
	}

	/**
	 * UpdateIdentifier<br>
	 * default 1
	 * 
	 * @return Integer
	 */
	public int getUpNo() {
		return this.upNo;
	}

	/**
	 * UpdateIdentifier<br>
	 * default 1
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
	public int getCalDate() {
		return StaticTool.bcToRoc(this.calDate);
	}

	/**
	 * 更新日期<br>
	 * 
	 *
	 * @param calDate 更新日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setCalDate(int calDate) throws LogicException {
		this.calDate = StaticTool.rocToBc(calDate);
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
		return "HlThreeDetail [hlThreeDetailId=" + hlThreeDetailId + ", actAmt=" + actAmt + ", pieceCode=" + pieceCode + ", cntingCode=" + cntingCode + ", tActAmt=" + tActAmt + ", empNo=" + empNo
				+ ", empId=" + empId + ", empName=" + empName + ", deptCode=" + deptCode + ", distCode=" + distCode + ", unitCode=" + unitCode + ", deptName=" + deptName + ", distName=" + distName
				+ ", unitName=" + unitName + ", firAppDate=" + firAppDate + ", biReteNo=" + biReteNo + ", twoYag=" + twoYag + ", threeYag=" + threeYag + ", twoPay=" + twoPay + ", threePay=" + threePay
				+ ", unitChiefNo=" + unitChiefNo + ", unitChiefName=" + unitChiefName + ", areaChiefNo=" + areaChiefNo + ", areaChiefName=" + areaChiefName + ", id3=" + id3 + ", id3Name=" + id3Name
				+ ", teamChiefNo=" + teamChiefNo + ", teamChiefName=" + teamChiefName + ", id0=" + id0 + ", id0Name=" + id0Name + ", upNo=" + upNo + ", calDate=" + calDate + ", createDate="
				+ createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
