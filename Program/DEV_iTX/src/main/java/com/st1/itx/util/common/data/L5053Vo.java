package com.st1.itx.util.common.data;

import java.io.Serializable;
import java.math.BigDecimal;

//import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * L5053專用<br>
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
@Entity
public class L5053Vo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5581404252840113900L;

	@Id
	private Integer PerfDate = 0;// 業績日期
	private Integer CustNo = 0;// 戶號
	private Integer FacmNo = 0;// 額度
	private Integer BormNo = 0;// 撥款
	private String RewardFullName = "";// 姓名
	private String BsOfficer = "";// 房貸專員
	private String OfficerName = "";// 房貸專員名稱
	private String Introducer = "";// 介紹人
	private String IntroducerName = "";// 介紹人名稱
	private BigDecimal IntroducerBonus = BigDecimal.ZERO;// 介紹獎金
	private Integer DrawdownDate = 0;// 撥款日/還款日
	private String ProdCode = "";// 商品代碼
	private String PieceCode = "";// 計件代碼
	private BigDecimal DrawdownAmt = BigDecimal.ZERO;// 撥款金額
	private String LastUpdateEmpNo = "";// 員工代號
	private String LastUpdateEmpNoName = "";// 員工名稱

	public L5053Vo() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param perfDate            業績日期
	 * @param custNo              戶號
	 * @param facmNo              額度
	 * @param bormNo              撥款
	 * @param rewardFullName      姓名
	 * @param bsOfficer           房貸專員
	 * @param officerName         房貸專員名稱
	 * @param introducer          介紹人
	 * @param introducerName      介紹人名稱
	 * @param introducerBonus     介紹獎金
	 * @param drawdownDate        撥款日/還款日
	 * @param prodCode            商品代碼
	 * @param pieceCode           計件代碼
	 * @param drawdownAmt         撥款金額
	 * @param lastUpdateEmpNo     員工代號
	 * @param lastUpdateEmpNoName 員工名稱
	 */
	public L5053Vo(Integer perfDate, Integer custNo, Integer facmNo, Integer bormNo, String rewardFullName, String bsOfficer, String officerName, String introducer, String introducerName,
			BigDecimal introducerBonus, Integer drawdownDate, String prodCode, String pieceCode, BigDecimal drawdownAmt, String lastUpdateEmpNo, String lastUpdateEmpNoName) {
		super();
		PerfDate = perfDate;
		CustNo = custNo;
		FacmNo = facmNo;
		BormNo = bormNo;
		RewardFullName = rewardFullName;
		BsOfficer = bsOfficer;
		OfficerName = officerName;
		Introducer = introducer;
		IntroducerName = introducerName;
		IntroducerBonus = introducerBonus;
		DrawdownDate = drawdownDate;
		ProdCode = prodCode;
		PieceCode = pieceCode;
		DrawdownAmt = drawdownAmt;
		LastUpdateEmpNo = lastUpdateEmpNo;
		LastUpdateEmpNoName = lastUpdateEmpNoName;
	}

	public Integer getPerfDate() {
		return DataChance(PerfDate);
	}

	public void setPerfDate(String perfDate) {
		if (perfDate != null && perfDate.trim().length() != 0) {
			PerfDate = Integer.parseInt(perfDate);
		} else {
			PerfDate = 0;
		}

	}

	public void setPerfDate(Integer perfDate) {
		if (perfDate != null) {
			PerfDate = perfDate;
		} else {
			PerfDate = 0;
		}

	}

	public Integer getCustNo() {
		return CustNo;
	}

	public void setCustNo(String custNo) {
		if (custNo != null && custNo.trim().length() != 0) {
			CustNo = Integer.parseInt(custNo);
		} else {
			CustNo = 0;
		}
	}

	public void setCustNo(Integer custNo) {
		if (custNo != null) {
			CustNo = custNo;
		} else {
			CustNo = 0;
		}
	}

	public Integer getFacmNo() {
		return FacmNo;
	}

	public void setFacmNo(String facmNo) {
		if (facmNo != null && facmNo.trim().length() != 0) {
			FacmNo = Integer.parseInt(facmNo);
		} else {
			FacmNo = 0;
		}

	}

	public void setFacmNo(Integer facmNo) {
		if (facmNo != null) {
			FacmNo = facmNo;
		} else {
			FacmNo = 0;
		}
	}

	public Integer getBormNo() {
		return BormNo;
	}

	public void setBormNo(String bormNo) {
		if (bormNo != null && bormNo.trim().length() != 0) {
			BormNo = Integer.parseInt(bormNo);
		} else {
			BormNo = 0;
		}
	}

	public void setBormNo(Integer bormNo) {
		if (bormNo != null) {
			BormNo = bormNo;
		} else {
			BormNo = 0;
		}
	}

	public String getRewardFullName() {
		return RewardFullName;
	}

	public void setRewardFullName(String rewardFullName) {
		if (rewardFullName != null) {
			RewardFullName = rewardFullName;
		} else {
			RewardFullName = "";
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

	public String getOfficerName() {
		return OfficerName;
	}

	public void setOfficerName(String officerName) {
		if (officerName != null) {
			OfficerName = officerName;
		} else {
			OfficerName = "";
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

	public BigDecimal getIntroducerBonus() {
		// IntroducerBonus=IntroducerBonus.multiply(new BigDecimal(100)).divide(new
		// BigDecimal(100));
		return IntroducerBonus;
	}

	public void setIntroducerBonus(String introducerBonus) {
		if (introducerBonus != null && introducerBonus.trim().length() != 0) {
			IntroducerBonus = new BigDecimal(introducerBonus);
		} else {
			IntroducerBonus = BigDecimal.ZERO;
		}
	}

	public void setIntroducerBonus(BigDecimal introducerBonus) {
		if (introducerBonus != null) {
			IntroducerBonus = introducerBonus;
		} else {
			IntroducerBonus = BigDecimal.ZERO;
		}
	}

	public Integer getDrawdownDate() {
		return DataChance(DrawdownDate);
	}

	public void setDrawdownDate(String drawdownDate) {
		if (drawdownDate != null && drawdownDate.trim().length() != 0) {
			DrawdownDate = Integer.parseInt(drawdownDate);
		} else {
			DrawdownDate = 0;
		}
	}

	public void setDrawdownDate(Integer drawdownDate) {
		if (drawdownDate != null) {
			DrawdownDate = drawdownDate;
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

	public void setDrawdownAmt(String drawdownAmt) {
		if (drawdownAmt != null && drawdownAmt.trim().length() != 0) {
			DrawdownAmt = new BigDecimal(drawdownAmt);
		} else {
			DrawdownAmt = BigDecimal.ZERO;
		}

	}

	public void setDrawdownAmt(BigDecimal drawdownAmt) {
		if (drawdownAmt != null) {
			DrawdownAmt = drawdownAmt;
		} else {
			DrawdownAmt = BigDecimal.ZERO;
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

	public String getLastUpdateEmpNoName() {
		return LastUpdateEmpNoName;
	}

	public void setLastUpdateEmpNoName(String lastUpdateEmpNoName) {
		if (lastUpdateEmpNoName != null) {
			LastUpdateEmpNoName = lastUpdateEmpNoName;
		} else {
			LastUpdateEmpNoName = "";
		}

	}

	@Override
	public String toString() {
		return "L5053Vo [PerfDate=" + PerfDate + ", CustNo=" + CustNo + ", FacmNo=" + FacmNo + ", BormNo=" + BormNo + ", RewardFullName=" + RewardFullName + ", BsOfficer=" + BsOfficer
				+ ", OfficerName=" + OfficerName + ", Introducer=" + Introducer + ", IntroducerName=" + IntroducerName + ", IntroducerBonus=" + IntroducerBonus + ", DrawdownDate=" + DrawdownDate
				+ ", ProdCode=" + ProdCode + ", PieceCode=" + PieceCode + ", DrawdownAmt=" + DrawdownAmt + ", LastUpdateEmpNo=" + LastUpdateEmpNo + ", LastUpdateEmpNoName=" + LastUpdateEmpNoName
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
