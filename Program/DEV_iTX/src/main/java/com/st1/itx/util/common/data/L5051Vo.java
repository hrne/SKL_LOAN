package com.st1.itx.util.common.data;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * L5051專用<br>
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
@Entity
public class L5051Vo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8134184183575018106L;
	@Id
	private String LastUpdateEmpNo = "";// 經辦部門
	private String BsOfficer = "";// 房貸專員
	private String BsOfficerName = "";// 房貸專員名稱
	private String CustNm = "";// 戶名
	private Integer CustNo = 0;// 戶號
	private Integer FacmNo = 0;// 額度編號
	private Integer BormNo = 0;// 撥款序號
	private Integer DrawdownDate = 0;// 撥款日
	private String ProdCode = "";// 商品代碼
	private String PieceCode = "";// 計件代碼
	private String CntingCode = "";// 是否計件
	private BigDecimal DrawdownAmt = new BigDecimal(0);// 撥款金額
	private String UnitCode = "";// 單位代號
	private String DistCode = "";// 區部代號
	private String DeptCode = "";// 部室代號
	private String DeptCodeX = "";// 單位名稱
	private String DistCodeX = "";// 區部名稱
	private String UnitCodeX = "";// 部室名稱
	private String EmpNo = "";// 員工名稱
	private String Introducer = "";// 介紹人
	private String IntroducerName = "";// 介紹人
	private String UnitManager = "";// 處經理
	private String DistManager = "";// 區經理
	private String DeptManager = "";// 部經理
	private BigDecimal PerfCnt = new BigDecimal(0);// 件數
	private BigDecimal PerfEqAmt = new BigDecimal(0);// 換算業績
	private BigDecimal PerfReward = new BigDecimal(0);// 業務報酬
	private BigDecimal PerfAmt = new BigDecimal(0);// 業績金額
	private BigDecimal IntroducerBonus = new BigDecimal(0);// 介紹獎金
	private Integer PerfDate = 0;// 業績日期

	public L5051Vo() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param lastUpdateEmpNo 經辦部門
	 * @param bsOfficer       房貸專員
	 * @param bsOfficerName   房貸專員名稱
	 * @param custNm          戶名
	 * @param custNo          戶號
	 * @param facmNo          額度編號
	 * @param bormNo          撥款序號
	 * @param drawdownDate    撥款日
	 * @param prodCode        商品代碼
	 * @param pieceCode       計件代碼
	 * @param cntingCode      是否計件
	 * @param drawdownAmt     撥款金額
	 * @param unitCode        單位代號
	 * @param distCode        區部代號
	 * @param deptCode        部室名稱
	 * @param deptCodeX       單位名稱
	 * @param distCodeX       區部名稱
	 * @param unitCodeX       部室名稱
	 * @param empNo           員工名稱
	 * @param introducer      介紹人
	 * @param introducerName  介紹人名稱
	 * @param unitManager     處經理
	 * @param distManager     區經理
	 * @param deptManager     部經理
	 * @param perfCnt         件數
	 * @param perfEqAmt       換算業績
	 * @param perfReward      業務報酬
	 * @param perfAmt         業績金額
	 * @param introducerBonus 介紹獎金
	 * @param perfDate        業績日期
	 */
	public L5051Vo(String lastUpdateEmpNo, String bsOfficer, String bsOfficerName, String custNm, Integer custNo, Integer facmNo, Integer bormNo, Integer drawdownDate, String prodCode,
			String pieceCode, String cntingCode, BigDecimal drawdownAmt, String unitCode, String distCode, String deptCode, String deptCodeX, String distCodeX, String unitCodeX, String empNo,
			String introducer, String introducerName, String unitManager, String distManager, String deptManager, BigDecimal perfCnt, BigDecimal perfEqAmt, BigDecimal perfReward, BigDecimal perfAmt,
			BigDecimal introducerBonus, Integer perfDate) {
		super();
		if (lastUpdateEmpNo != null) {
			this.LastUpdateEmpNo = lastUpdateEmpNo;
		}
		if (bsOfficer != null) {
			this.BsOfficer = bsOfficer;
		}
		if (bsOfficerName != null) {
			this.BsOfficerName = bsOfficerName;
		}
		if (custNm != null) {
			this.CustNm = custNm;
		}
		if (custNo != null) {
			this.CustNo = custNo;
		}
		if (facmNo != null) {
			this.FacmNo = facmNo;
		}
		if (bormNo != null) {
			this.BormNo = bormNo;
		}
		if (drawdownDate != null) {
			this.DrawdownDate = drawdownDate;
		}
		if (prodCode != null) {
			this.ProdCode = prodCode;
		}
		if (pieceCode != null) {
			this.PieceCode = pieceCode;
		}
		if (cntingCode != null) {
			this.CntingCode = cntingCode;
		}
		if (drawdownAmt != null) {
			this.DrawdownAmt = drawdownAmt;
		}
		if (unitCode != null) {
			this.UnitCode = unitCode;
		}
		if (distCode != null) {
			this.DistCode = distCode;
		}
		if (deptCode != null) {
			this.DeptCode = deptCode;
		}
		if (deptCodeX != null) {
			this.DeptCodeX = deptCodeX;
		}
		if (distCodeX != null) {
			this.DistCodeX = distCodeX;
		}
		if (unitCodeX != null) {
			this.UnitCodeX = unitCodeX;
		}
		if (empNo != null) {
			this.EmpNo = empNo;
		}
		if (introducer != null) {
			this.Introducer = introducer;
		}
		if (introducerName != null) {
			this.IntroducerName = introducerName;
		}
		if (unitManager != null) {
			this.UnitManager = unitManager;
		}
		if (distManager != null) {
			this.DistManager = distManager;
		}
		if (deptManager != null) {
			this.DeptManager = deptManager;
		}
		if (perfCnt != null) {
			this.PerfCnt = perfCnt;
		}

		if (perfEqAmt != null) {
			this.PerfEqAmt = perfEqAmt;
		}
		if (perfReward != null) {
			this.PerfReward = perfReward;
		}
		if (perfAmt != null) {
			this.PerfAmt = perfAmt;
		}
		if (introducerBonus != null) {
			this.IntroducerBonus = introducerBonus;
		}
		if (perfDate != null) {
			this.PerfDate = perfDate;
		}

	}

	public String getLastUpdateEmpNo() {
		return LastUpdateEmpNo;
	}

	public void setLastUpdateEmpNo(String lastUpdateEmpNo) {
		if (lastUpdateEmpNo != null) {
			LastUpdateEmpNo = lastUpdateEmpNo;
		} else {
			LastUpdateEmpNo = "";
		}
	}

	public String getBsOfficer() {
		return BsOfficer;
	}

	public void setBsOfficer(String bsOfficer) {
		BsOfficer = bsOfficer;
	}

	public String getBsOfficerName() {
		return BsOfficerName;
	}

	public void setBsOfficerName(String bsOfficerName) {
		BsOfficerName = bsOfficerName;
	}

	public String getCustNm() {
		return CustNm;
	}

	public void setCustNm(String custNm) {
		CustNm = custNm;
	}

	public Integer getCustNo() {
		return CustNo;
	}

	public void setCustNo(String custNo) {
		if (custNo != null && custNo.length() != 0) {
			CustNo = Integer.parseInt(custNo);
		}

	}

	public void setCustNo(Integer custNo) {
		CustNo = custNo;
	}

	public Integer getFacmNo() {
		return FacmNo;
	}

	public void setFacmNo(String facmNo) {
		if (facmNo != null && facmNo.length() != 0) {
			FacmNo = Integer.parseInt(facmNo);
		}

	}

	public void setFacmNo(Integer facmNo) {
		FacmNo = facmNo;
	}

	public Integer getBormNo() {
		return BormNo;
	}

	public void setBormNo(String bormNo) {
		if (bormNo != null && bormNo.length() != 0) {
			BormNo = Integer.parseInt(bormNo);
		}
	}

	public void setBormNo(Integer bormNo) {
		BormNo = bormNo;
	}

	public Integer getDrawdownDate() {
		return DataChance(DrawdownDate);
	}

	public void setDrawdownDate(String drawdownDate) {
		if (drawdownDate != null && drawdownDate.length() != 0) {
			DrawdownDate = Integer.parseInt(drawdownDate);
		}
	}

	public void setDrawdownDate(Integer drawdownDate) {
		DrawdownDate = drawdownDate;
	}

	public String getProdCode() {
		return ProdCode;
	}

	public void setProdCode(String prodCode) {
		ProdCode = prodCode;
	}

	public String getPieceCode() {
		return PieceCode;
	}

	public void setPieceCode(String pieceCode) {
		PieceCode = pieceCode;
	}

	public String getCntingCode() {
		return CntingCode;
	}

	public void setCntingCode(String cntingCode) {
		CntingCode = cntingCode;
	}

	public BigDecimal getDrawdownAmt() {
		return DrawdownAmt;
	}

	public void setDrawdownAmt(String drawdownAmt) {
		if (drawdownAmt != null && drawdownAmt.length() != 0) {
			DrawdownAmt = new BigDecimal(drawdownAmt);
		}

	}

	public void setDrawdownAmt(BigDecimal drawdownAmt) {
		DrawdownAmt = drawdownAmt;
	}

	public String getUnitCode() {
		return UnitCode;
	}

	public void setUnitCode(String unitCode) {
		UnitCode = unitCode;
	}

	public String getDistCode() {
		return DistCode;
	}

	public void setDistCode(String distCode) {
		DistCode = distCode;
	}

	public String getDeptCode() {
		return DeptCode;
	}

	public void setDeptCode(String deptCode) {
		DeptCode = deptCode;
	}

	public String getDeptCodeX() {
		return DeptCodeX;
	}

	public void setDeptCodeX(String deptCodeX) {
		DeptCodeX = deptCodeX;
	}

	public String getDistCodeX() {
		return DistCodeX;
	}

	public void setDistCodeX(String distCodeX) {
		DistCodeX = distCodeX;
	}

	public String getUnitCodeX() {
		return UnitCodeX;
	}

	public void setUnitCodeX(String unitCodeX) {
		UnitCodeX = unitCodeX;
	}

	public String getEmpNo() {
		return EmpNo;
	}

	public void setEmpNo(String empNo) {
		EmpNo = empNo;
	}

	public String getIntroducer() {
		return Introducer;
	}

	public void setIntroducer(String introducer) {
		Introducer = introducer;
	}

	public String getIntroducerName() {
		return IntroducerName;
	}

	public void setIntroducerName(String introducerName) {
		IntroducerName = introducerName;
	}

	public String getUnitManager() {
		return UnitManager;
	}

	public void setUnitManager(String unitManager) {
		UnitManager = unitManager;
	}

	public String getDistManager() {
		return DistManager;
	}

	public void setDistManager(String distManager) {
		DistManager = distManager;
	}

	public String getDeptManager() {
		return DeptManager;
	}

	public void setDeptManager(String deptManager) {
		DeptManager = deptManager;
	}

	public BigDecimal getPerfCnt() {
		return PerfCnt;
	}

	public void setPerfCnt(String perfCnt) {
		if (perfCnt != null && perfCnt.length() != 0) {
			PerfCnt = new BigDecimal(perfCnt);
		}

	}

	public void setPerfCnt(BigDecimal perfCnt) {
		PerfCnt = perfCnt;
	}

	public BigDecimal getPerfEqAmt() {
		return PerfEqAmt;
	}

	public void setPerfEqAmt(String perfEqAmt) {
		if (perfEqAmt != null && perfEqAmt.length() != 0) {
			PerfEqAmt = new BigDecimal(perfEqAmt);
		}
	}

	public void setPerfEqAmt(BigDecimal perfEqAmt) {
		PerfEqAmt = perfEqAmt;
	}

	public BigDecimal getPerfReward() {
		return PerfReward;
	}

	public void setPerfReward(String perfReward) {
		if (perfReward != null && perfReward.length() != 0) {
			PerfReward = new BigDecimal(perfReward);
		}
	}

	public void setPerfReward(BigDecimal perfReward) {
		PerfReward = perfReward;
	}

	public BigDecimal getPerfAmt() {
		return PerfAmt;
	}

	public void setPerfAmt(String perfAmt) {
		if (perfAmt != null && perfAmt.length() != 0) {
			PerfAmt = new BigDecimal(perfAmt);
		}

	}

	public void setPerfAmt(BigDecimal perfAmt) {
		PerfAmt = perfAmt;
	}

	public BigDecimal getIntroducerBonus() {
		return IntroducerBonus;
	}

	public void setIntroducerBonus(String introducerBonus) {
		if (introducerBonus != null && introducerBonus.length() != 0) {
			IntroducerBonus = new BigDecimal(introducerBonus);
		}
	}

	public void setIntroducerBonus(BigDecimal introducerBonus) {
		IntroducerBonus = introducerBonus;
	}

	public Integer getPerfDate() {
		return DataChance(PerfDate);
	}

	public void setPerfDate(String perfDate) {
		if (perfDate != null && perfDate.length() != 0) {
			PerfDate = Integer.parseInt(perfDate);
		}

	}

	public void setPerfDate(Integer perfDate) {
		PerfDate = perfDate;
	}

	@Override
	public String toString() {
		return "L5051Vo [LastUpdateEmpNo=" + LastUpdateEmpNo + ", BsOfficer=" + BsOfficer + ", BsOfficerName=" + BsOfficerName + ", CustNm=" + CustNm + ", CustNo=" + CustNo + ", FacmNo=" + FacmNo
				+ ", BormNo=" + BormNo + ", DrawdownDate=" + DrawdownDate + ", ProdCode=" + ProdCode + ", PieceCode=" + PieceCode + ", CntingCode=" + CntingCode + ", DrawdownAmt=" + DrawdownAmt
				+ ", UnitCode=" + UnitCode + ", DistCode=" + DistCode + ", DeptCode=" + DeptCode + ", DeptCodeX=" + DeptCodeX + ", DistCodeX=" + DistCodeX + ", UnitCodeX=" + UnitCodeX + ", EmpNo="
				+ EmpNo + ", Introducer=" + Introducer + ", IntroducerName=" + IntroducerName + ", UnitManager=" + UnitManager + ", DistManager=" + DistManager + ", DeptManager=" + DeptManager
				+ ", PerfCnt=" + PerfCnt + ", PerfEqAmt=" + PerfEqAmt + ", PerfReward=" + PerfReward + ", PerfAmt=" + PerfAmt + ", IntroducerBonus=" + IntroducerBonus + ", PerfDate=" + PerfDate + "]";
	}

	/**
	 * 
	 * @param Date 西元年YMD
	 * @return 民國年YMD
	 */
	public Integer DataChance(Integer Date) {
		if (Date != null) {
			int DateL = String.valueOf(Date).length();
			if (DateL == 8) {
				Date = Date - 19110000;
			} else if (DateL == 7) {

			} else {
				Date = 0;
			}
		} else {
			Date = 0;
		}
		return Date;
	}

}
