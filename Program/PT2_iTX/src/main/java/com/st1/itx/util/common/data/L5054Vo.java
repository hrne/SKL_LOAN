package com.st1.itx.util.common.data;

import java.io.Serializable;
import java.math.BigDecimal;
//import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * L5054專用<br>
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
@Entity
public class L5054Vo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5994014861089472457L;
	@Id
	private Integer PerfDate = 0;// 業績日期-PfReward
	private String CustNm = "";// 戶名-PfReward
	private Integer CustNo = 0;// 戶號-PfReward
	private Integer FacmNo = 0;// 額度-PfReward
	private Integer BormNo = 0;// 撥款-PfReward
	private String BsOfficer = "";// 房貸專員-PfBsDetail
	private String BsOfficerName = "";// 房貸專員名稱
	private Integer DrawdownDate = 0;// 撥款日-PfBsDetail
	private String ProdCode = "";// 商品代碼-PfBsDetail
	private String PieceCode = "";// 計件代碼-PfBsDetail
	private BigDecimal DrawdownAmt = BigDecimal.ZERO;// 撥款金額 -PfBsDetail
	private String Introducer = "";// 介紹人
	private String IntroducerName = "";// 介紹人名稱
	private String InterviewerA = "";// 晤談一-PfReward
	private String InterviewerB = "";// 晤談二-PfReward
	private String Coorgnizer = "";// 協辦人員-PfReward
	private BigDecimal CoorgnizerBonus = BigDecimal.ZERO;// 協辦獎金-PfReward
	private BigDecimal IntroducerAddBonus = BigDecimal.ZERO;// 介紹人加碼獎勵津貼-PfReward
	private String CreateEmpNo = "";// 建檔人員-PfReward
	private String LastUpdateEmpNo = "";// 經辦-PfReward

	public L5054Vo() {
		// TODO Auto-generated constructor stub
	}

	public L5054Vo(Integer perfDate, String custNm, Integer custNo, Integer facmNo, Integer bormNo, String bsOfficer, String bsOfficerName, Integer drawdownDate, String prodCode, String pieceCode,
			BigDecimal drawdownAmt, String introducer, String introducerName, String interviewerA, String interviewerB, String coorgnizer, BigDecimal coorgnizerBonus, BigDecimal introducerAddBonus,
			String createEmpNo, String lastUpdateEmpNo) {
		super();
		PerfDate = perfDate;
		CustNm = custNm;
		CustNo = custNo;
		FacmNo = facmNo;
		BormNo = bormNo;
		BsOfficer = bsOfficer;
		BsOfficerName = bsOfficerName;
		DrawdownDate = drawdownDate;
		ProdCode = prodCode;
		PieceCode = pieceCode;
		DrawdownAmt = drawdownAmt;
		Introducer = introducer;
		IntroducerName = introducerName;
		InterviewerA = interviewerA;
		InterviewerB = interviewerB;
		Coorgnizer = coorgnizer;
		CoorgnizerBonus = coorgnizerBonus;
		IntroducerAddBonus = introducerAddBonus;
		CreateEmpNo = createEmpNo;
		LastUpdateEmpNo = lastUpdateEmpNo;
	}

	public Integer getPerfDate() {
		return DataChance(PerfDate);
	}

	public void setPerfDate(String perfDate) {
		if (perfDate != null) {
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

	public String getInterviewerA() {
		return InterviewerA;
	}

	public void setInterviewerA(String interviewerA) {
		if (interviewerA != null) {
			InterviewerA = interviewerA;
		} else {
			InterviewerA = "";
		}
	}

	public String getInterviewerB() {
		return InterviewerB;
	}

	public void setInterviewerB(String interviewerB) {
		if (interviewerB != null) {
			InterviewerB = interviewerB;
		} else {
			InterviewerB = "";
		}
	}

	public String getCoorgnizer() {
		return Coorgnizer;
	}

	public void setCoorgnizer(String coorgnizer) {
		if (coorgnizer != null) {
			Coorgnizer = coorgnizer;
		} else {
			Coorgnizer = "";
		}
	}

	public BigDecimal getCoorgnizerBonus() {
		return CoorgnizerBonus;
	}

	public void setCoorgnizerBonus(BigDecimal coorgnizerBonus) {
		if (coorgnizerBonus != null) {
			CoorgnizerBonus = coorgnizerBonus;
		} else {
			CoorgnizerBonus = BigDecimal.ZERO;
		}
	}

	public void setCoorgnizerBonus(String coorgnizerBonus) {
		if (coorgnizerBonus != null && coorgnizerBonus.trim().length() != 0) {
			CoorgnizerBonus = new BigDecimal(coorgnizerBonus);
		} else {
			CoorgnizerBonus = BigDecimal.ZERO;
		}
	}

	public BigDecimal getIntroducerAddBonus() {
		return IntroducerAddBonus;
	}

	public void setIntroducerAddBonus(BigDecimal introducerAddBonus) {
		if (introducerAddBonus != null) {
			IntroducerAddBonus = introducerAddBonus;
		} else {
			IntroducerAddBonus = BigDecimal.ZERO;
		}
	}

	public void setIntroducerAddBonus(String introducerAddBonus) {
		if (introducerAddBonus != null && introducerAddBonus.trim().length() != 0) {
			IntroducerAddBonus = new BigDecimal(introducerAddBonus);
		} else {
			IntroducerAddBonus = BigDecimal.ZERO;
		}
	}

	public String getCreateEmpNo() {
		return CreateEmpNo;
	}

	public void setCreateEmpNo(String createEmpNo) {
		if (createEmpNo != null) {
			CreateEmpNo = createEmpNo;
		} else {
			CreateEmpNo = "";
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

	@Override
	public String toString() {
		return "L5054Vo [PerfDate=" + PerfDate + ", CustNm=" + CustNm + ", CustNo=" + CustNo + ", FacmNo=" + FacmNo + ", BormNo=" + BormNo + ", BsOfficer=" + BsOfficer + ", BsOfficerName="
				+ BsOfficerName + ", DrawdownDate=" + DrawdownDate + ", ProdCode=" + ProdCode + ", PieceCode=" + PieceCode + ", DrawdownAmt=" + DrawdownAmt + ", Introducer=" + Introducer
				+ ", IntroducerName=" + IntroducerName + ", InterviewerA=" + InterviewerA + ", InterviewerB=" + InterviewerB + ", Coorgnizer=" + Coorgnizer + ", CoorgnizerBonus=" + CoorgnizerBonus
				+ ", IntroducerAddBonus=" + IntroducerAddBonus + ", CreateEmpNo=" + CreateEmpNo + ", LastUpdateEmpNo=" + LastUpdateEmpNo + "]";
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
