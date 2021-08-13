package com.st1.itx.util.common.data;

import java.io.Serializable;
import java.math.BigDecimal;

//import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * L5052專用<br>
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
@Entity
public class L5052Vo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3904881457846842616L;

	@Id
	private Integer PerfDate = 0;// 業績日期
	private String AreaCenter = "";// 區域中心
	private String AreaCenterX = "";// 區域中心名稱
	private String DeptCode = "";// 部室別
	private String DeptCodeX = "";// 部室別名稱
	private String BsOfficer = "";// 房貸專員
	private String BsOfficerName = "";// 房貸專員名稱
	private String CustNm = "";// 戶名
	private Integer CustNo = 0;// 戶號
	private Integer FacmNo = 0;// 額度編號
	private Integer BormNo = 0;// 撥款序號
	private Integer DrawdownDate = 0;// 撥款日
	private String ProdCode = "";// 商品代碼
	private String PieceCode = "";// 計件代碼
	private BigDecimal DrawdownAmt = BigDecimal.ZERO;// 撥款金額 
	private String WorkMonth = "";// 工作月-民國
	private BigDecimal PerfCnt = BigDecimal.ZERO;// 件數
	private BigDecimal PerfAmt = BigDecimal.ZERO;// 業績金額
	private String IntroduceDeptCode = "";// 介紹人部室代號
	private String IntroduceDistCode = "";// 介紹人區部代號
	private String IntroduceUnitCode = "";// 介紹人單位代號
	private String Introducer = "";// 介紹人員編
	private String IntroducerName = "";// 介紹人姓名

	/***
	 * 
	 * @param perfDate          業績日期
	 * @param areaCenter        區域中心
	 * @param areaCenterX       區域中心名稱
	 * @param deptCode          部室別
	 * @param deptCodeX         部室別名稱
	 * @param bsOfficer         房貸專員
	 * @param bsOfficerName     房貸專員名稱
	 * @param custNm            戶名
	 * @param custNo            戶號
	 * @param facmNo            額度編號
	 * @param bormNo            撥款編號
	 * @param drawdownDate      撥款日
	 * @param prodCode          商品代碼
	 * @param pieceCode         計件代碼
	 * @param drawdownAmt       撥款金額 
	 * @param workMonth         工作月-民國
	 * @param perfCnt           件數
	 * @param perfAmt           業績金額
	 * @param introduceDeptCode 介紹人部室代號
	 * @param introduceDistCode 介紹人區部代號
	 * @param introduceUnitCode 介紹人單位代號
	 * @param introducer        介紹人員編
	 * @param introducerName    介紹人姓名
	 */
	public L5052Vo(Integer perfDate, String areaCenter, String areaCenterX, String deptCode, String deptCodeX, String bsOfficer, String bsOfficerName, String custNm, Integer custNo, Integer facmNo,
			Integer bormNo, int drawdownDate, String prodCode, String pieceCode, BigDecimal drawdownAmt, String workMonth, BigDecimal perfCnt, BigDecimal perfAmt, String introduceDeptCode,
			String introduceDistCode, String introduceUnitCode, String introducer, String introducerName) {
		super();
		PerfDate = perfDate;
		AreaCenter = areaCenter;
		AreaCenterX = areaCenterX;
		DeptCode = deptCode;
		DeptCodeX = deptCodeX;
		BsOfficer = bsOfficer;
		BsOfficerName = bsOfficerName;
		CustNm = custNm;
		CustNo = custNo;
		FacmNo = facmNo;
		BormNo = bormNo;
		DrawdownDate = drawdownDate;
		ProdCode = prodCode;
		PieceCode = pieceCode;
		DrawdownAmt = drawdownAmt;
		WorkMonth = workMonth;
		PerfCnt = perfCnt;
		PerfAmt = perfAmt;
		IntroduceDeptCode = introduceDeptCode;
		IntroduceDistCode = introduceDistCode;
		IntroduceUnitCode = introduceUnitCode;
		Introducer = introducer;
		IntroducerName = introducerName;
	}

	public L5052Vo() {
		// TODO Auto-generated constructor stub
	}

	public Integer getPerfDate() {
		return DataChance(PerfDate);
	}

	public void setPerfDate(Integer perfDate) {
		if (perfDate != null) {
			PerfDate = perfDate;
		} else {
			PerfDate = 0;
		}
	}

	public void setPerfDate(String perfDate) {
		if (perfDate != null && perfDate.trim().length() != 0) {
			PerfDate = Integer.parseInt(perfDate);
		} else {
			PerfDate = 0;
		}
	}

	public String getAreaCenter() {
		return AreaCenter;
	}

	public void setAreaCenter(String areaCenter) {
		if (areaCenter != null) {
			AreaCenter = areaCenter;
		} else {
			AreaCenter = "";
		}
	}

	public String getAreaCenterX() {
		return AreaCenterX;
	}

	public void setAreaCenterX(String areaCenterX) {
		if (areaCenterX != null) {
			AreaCenterX = areaCenterX;
		} else {
			AreaCenterX = "";
		}
	}

	public String getDeptCode() {
		return DeptCode;
	}

	public void setDeptCode(String deptCode) {
		if (deptCode != null) {
			DeptCode = deptCode;
		} else {
			DeptCode = "";
		}
	}

	public String getDeptCodeX() {
		return DeptCodeX;
	}

	public void setDeptCodeX(String deptCodeX) {
		if (deptCodeX != null) {
			DeptCodeX = deptCodeX;
		} else {
			DeptCodeX = "";
		}
	}

	public String getBsOfficer() {
		return BsOfficer;
	}

	public void setBsOfficer(String bsOfficer) {
		if (bsOfficer != null) {
			BsOfficer = bsOfficer;
		} else {
			BsOfficer = "";
		}

	}

	public String getBsOfficerName() {
		return BsOfficerName;
	}

	public void setBsOfficerName(String bsOfficerName) {
		if (bsOfficerName != null) {
			BsOfficerName = bsOfficerName;
		} else {
			BsOfficerName = "";
		}

	}

	public String getCustNm() {
		return CustNm;
	}

	public void setCustNm(String custNm) {
		if (custNm != null) {
			CustNm = custNm;
		} else {
			CustNm = "";
		}
	}

	public Integer getCustNo() {
		return CustNo;
	}

	public void setCustNo(Integer custNo) {
		if (custNo != null) {
			CustNo = custNo;
		} else {
			CustNo = 0;
		}
	}

	public void setCustNo(String custNo) {
		if (custNo != null && custNo.trim().length() != 0) {
			CustNo = Integer.parseInt(custNo);
		} else {
			CustNo = 0;
		}
	}

	public Integer getFacmNo() {
		return FacmNo;
	}

	public void setFacmNo(Integer facmNo) {
		if (facmNo != null) {
			FacmNo = facmNo;
		} else {
			FacmNo = 0;
		}
	}

	public void setFacmNo(String facmNo) {
		if (facmNo != null && facmNo.trim().length() != 0) {
			FacmNo = Integer.parseInt(facmNo);
		} else {
			FacmNo = 0;
		}
	}

	public Integer getBormNo() {
		return BormNo;
	}

	public void setBormNo(Integer bormNo) {
		if (bormNo != null) {
			BormNo = bormNo;
		} else {
			BormNo = 0;
		}
	}

	public void setBormNo(String bormNo) {
		if (bormNo != null && bormNo.trim().length() != 0) {
			BormNo = Integer.parseInt(bormNo);
		} else {
			BormNo = 0;
		}
	}

	public int getDrawdownDate() {
		return DataChance(DrawdownDate);
	}

	public void setDrawdownDate(Integer drawdownDate) {
		if (drawdownDate != null) {
			DrawdownDate = drawdownDate;
		} else {
			DrawdownDate = 0;
		}
	}

	public void setDrawdownDate(String drawdownDate) {
		if (drawdownDate != null && drawdownDate.trim().length() != 0) {
			DrawdownDate = Integer.parseInt(drawdownDate);
		} else {
			DrawdownDate = 0;
		}
	}

	public String getProdCode() {
		return ProdCode;
	}

	public void setProdCode(String prodCode) {
		if (prodCode != null) {
			ProdCode = prodCode;
		} else {
			ProdCode = "";
		}

	}

	public String getPieceCode() {
		return PieceCode;
	}

	public void setPieceCode(String pieceCode) {
		if (pieceCode != null) {
			PieceCode = pieceCode;
		} else {
			PieceCode = "";
		}

	}

	public BigDecimal getDrawdownAmt() {
		return DrawdownAmt;
	}

	public void setDrawdownAmt(BigDecimal drawdownAmt) {
		if (drawdownAmt != null) {
			DrawdownAmt = drawdownAmt;
		} else {
			DrawdownAmt = BigDecimal.ZERO;
		}
	}

	public void setDrawdownAmt(String drawdownAmt) {
		if (drawdownAmt != null && drawdownAmt.trim().length() != 0) {
			DrawdownAmt = new BigDecimal(drawdownAmt);
		} else {
			DrawdownAmt = BigDecimal.ZERO;
		}
	}

	public String getWorkMonth() {
		if (WorkMonth != null) {
			if (WorkMonth.length() == 6) {
				// 西元年格式 要轉成民國年
				String YYYY = WorkMonth.substring(0, 4);
				String MM = WorkMonth.substring(4, 6);
				WorkMonth = String.valueOf(Integer.parseInt(YYYY) - 1911) + MM;
			}
		}
		return WorkMonth;
	}

	public void setWorkMonth(String workMonth) {
		if (workMonth != null) {
			WorkMonth = workMonth;
		} else {
			WorkMonth = "";
		}

	}

	public BigDecimal getPerfCnt() {
		return PerfCnt;
	}

	public void setPerfCnt(BigDecimal perfCnt) {
		if (perfCnt != null) {
			PerfCnt = perfCnt;
		} else {
			PerfCnt = BigDecimal.ZERO;
		}
	}

	public void setPerfCnt(String perfCnt) {
		if (perfCnt != null && perfCnt.trim().length() != 0) {
			PerfCnt = new BigDecimal(perfCnt);
		} else {
			PerfCnt = BigDecimal.ZERO;
		}
	}

	public BigDecimal getPerfAmt() {
		return PerfAmt;
	}

	public void setPerfAmt(BigDecimal perfAmt) {
		if (perfAmt != null) {
			PerfAmt = perfAmt;
		} else {
			PerfAmt = BigDecimal.ZERO;
		}
	}

	public void setPerfAmt(String perfAmt) {
		if (perfAmt != null && perfAmt.trim().length() != 0) {
			PerfAmt = new BigDecimal(perfAmt);
		} else {
			PerfAmt = BigDecimal.ZERO;
		}
	}

	public String getIntroduceDeptCode() {
		return IntroduceDeptCode;
	}

	public void setIntroduceDeptCode(String introduceDeptCode) {
		if (introduceDeptCode != null) {
			IntroduceDeptCode = introduceDeptCode;
		} else {
			IntroduceDeptCode = "";
		}
	}

	public String getIntroduceDistCode() {
		return IntroduceDistCode;
	}

	public void setIntroduceDistCode(String introduceDistCode) {
		if (introduceDistCode != null) {
			IntroduceDistCode = introduceDistCode;
		} else {
			IntroduceDistCode = "";
		}

	}

	public String getIntroduceUnitCode() {
		return IntroduceUnitCode;
	}

	public void setIntroduceUnitCode(String introduceUnitCode) {
		if (introduceUnitCode != null) {
			IntroduceUnitCode = introduceUnitCode;
		} else {
			IntroduceUnitCode = "";
		}

	}

	public String getIntroducer() {
		return Introducer;
	}

	public void setIntroducer(String introducer) {
		if (introducer != null) {
			Introducer = introducer;
		} else {
			Introducer = "";
		}

	}

	public String getIntroducerName() {
		return IntroducerName;
	}

	public void setIntroducerName(String introducerName) {
		if (introducerName != null) {
			IntroducerName = introducerName;
		} else {
			IntroducerName = "";
		}

	}

	@Override
	public String toString() {
		return "L5052Vo [PerfDate=" + PerfDate + ", AreaCenter=" + AreaCenter + ", AreaCenterX=" + AreaCenterX + ", DeptCode=" + DeptCode + ", DeptCodeX=" + DeptCodeX + ", BsOfficer=" + BsOfficer
				+ ", BsOfficerName=" + BsOfficerName + ", CustNm=" + CustNm + ", CustNo=" + CustNo + ", FacmNo=" + FacmNo + ", BormNo=" + BormNo + ", DrawdownDate=" + DrawdownDate + ", ProdCode="
				+ ProdCode + ", PieceCode=" + PieceCode + ", DrawdownAmt=" + DrawdownAmt + ", WorkMonth=" + WorkMonth + ", PerfCnt=" + PerfCnt + ", PerfAmt=" + PerfAmt + ", IntroduceDeptCode="
				+ IntroduceDeptCode + ", IntroduceDistCode=" + IntroduceDistCode + ", IntroduceUnitCode=" + IntroduceUnitCode + ", Introducer=" + Introducer + ", IntroducerName=" + IntroducerName
				+ "]";
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
