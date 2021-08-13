package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingId;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.ClImm;
import com.st1.itx.db.domain.ClImmId;
import com.st1.itx.db.domain.ClOtherRights;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.domain.Guarantor;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClImmService;
import com.st1.itx.db.service.ClOtherRightsService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.service.GuarantorService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * CUSTNO=9,7<br>
 * CUSTNO1=9,3<br>
 * CUSTNO2=9,3<br>
 * END=X,1<br>
 */

@Service("L2932")
@Scope("prototype")
/**
 * 清償金額計算單
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2932 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2932.class);

	/* DB服務注入 */
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public AcReceivableService acReceivableService;
	@Autowired
	public CustMainService custMainService;
	@Autowired
	public FacProdService facProdService;
	@Autowired
	public GuarantorService guarantorService;
	@Autowired
	public ClFacService clFacService;
	@Autowired
	public ClBuildingService clBuildingService;
	@Autowired
	public ClImmService clImmService;
	@Autowired
	public ClOtherRightsService ClOtherRightsService;

	/* 日期工具 */
	@Autowired
	public DateUtil dDateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public TotaVo totaA; // 額度資料
	@Autowired
	public TotaVo totaB; // 清償金類型
	@Autowired
	public TotaVo totaC; // 抵押權塗銷同意書

	private List<ClOtherRights> lClOtherRights = new ArrayList<ClOtherRights>();
	private List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();
	private List<LoanBorMain> lLoanBorMain2 = new ArrayList<LoanBorMain>();
	private List<FacMain> lFacMain = new ArrayList<FacMain>();
	private List<AcReceivable> lAcReceivable = new ArrayList<AcReceivable>();
	private List<Guarantor> lGuarantor = new ArrayList<Guarantor>();
	private List<ClFac> lClFac = new ArrayList<ClFac>();
	private int iCustNo;
	private int iFacmNo;
	private int wkFacmNoStart = 0;
	private int wkFacmNoEnd = 0;
	private int cntA = 0;
	private int cntB = 0;
	private int cntC = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2932 ");
		this.totaVo.init(titaVo);
		totaA.putParam("MSGID", "L293A");
		totaB.putParam("MSGID", "L293B");
		totaC.putParam("MSGID", "L293C");
		iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		wkFacmNoStart = 0;
		wkFacmNoEnd = 0;
		if (iFacmNo > 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
		} else {
			wkFacmNoStart = 1;
			wkFacmNoEnd = 999;
		}
		// 額度主檔
		Slice<FacMain> slFacMain = facMainService.facmCustNoRange(iCustNo, iCustNo, wkFacmNoStart, wkFacmNoEnd, 0,
				Integer.MAX_VALUE, titaVo);
		lFacMain = slFacMain == null ? null : slFacMain.getContent();
		if (lFacMain == null || lFacMain.size() == 0) {
			throw new LogicException(titaVo, "E0001", "額度主檔"); // 查詢資料不存在
		}

		// 客戶主檔
		CustMain tCustMain = custMainService.custNoFirst(iCustNo, iCustNo, titaVo);
		if (tCustMain == null) {
			throw new LogicException(titaVo, "E0001", "客戶主檔"); // 查詢資料不存在
		}

		// 撥款主檔
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, wkFacmNoStart, wkFacmNoEnd, 1, 900,
				0, Integer.MAX_VALUE, titaVo);
		lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();

		// 暫收支票
		Slice<AcReceivable> slAcReceivable = acReceivableService.acctCodeEq(0, "TCK", iCustNo, iCustNo, 0,
				Integer.MAX_VALUE, titaVo);
		lAcReceivable = slAcReceivable == null ? null : slAcReceivable.getContent();

		// 保證人檔
		Slice<Guarantor> slGuarantor = guarantorService.guaUKeyEq(tCustMain.getCustUKey(), 0, Integer.MAX_VALUE,
				titaVo);
		lGuarantor = slGuarantor == null ? null : slGuarantor.getContent();

		// totaA 額度資料、totaB 清償金類型
		for (FacMain tFacMain : lFacMain) {
			setTotaA(tFacMain, titaVo);
			setTotaB(tFacMain, titaVo);
		}
		// 擔保品與額度關聯檔
		Slice<ClFac> slClFac = clFacService.selectForL2017CustNo(iCustNo, wkFacmNoStart, wkFacmNoEnd, 0,
				Integer.MAX_VALUE, titaVo);
		lClFac = slClFac == null ? null : slClFac.getContent();
		if (lClFac != null) {
			// 擔保品編號重複的不要
			boolean isNew = true;
			List<ClFac> l1ClFac = new ArrayList<ClFac>();
			for (ClFac t : lClFac) {
				isNew = true;
				if (l1ClFac.size() > 0) {
					for (ClFac t2 : l1ClFac) {
						if (t.getClCode1() == t2.getClCode1() && t.getClCode2() == t2.getClCode2()
								&& t.getClNo() == t2.getClNo()) {
							isNew = false;
						}
					}
				}
				if (isNew) {
					l1ClFac.add(t);
				}
			}
			// totaC 抵押權塗銷同意書
			for (ClFac tClFac : l1ClFac) {
				setTotaC(tClFac, titaVo);
			}
		}

		// tota
		if (cntA >= 1) {
			this.addList(totaA);
		}
		if (cntB >= 1) {
			this.addList(totaB);
		}
		if (cntC >= 1) {
			this.addList(totaC);
		}

//		this.addList(this.totaVo);
		return this.sendList();
	}

	// totaA 額度資料
	private void setTotaA(FacMain tFacMain, TitaVo titaVo) throws LogicException {
		cntA++;
		// 額度 商品代碼 核准額度 放款餘額 是否保證 暫收支票 門牌號碼 建物標示備註
		totaA.putParam("MSGID", "L293A");
		OccursList occursList = new OccursList();
		occursList.putParam("OOAFacmNo", tFacMain.getFacmNo()); // 額度
		occursList.putParam("OOAProdNo", tFacMain.getProdNo()); // 商品代碼
		occursList.putParam("OOALineAmt", tFacMain.getLineAmt()); // 核准額度

		Slice<LoanBorMain> slLoanBorMain2 = loanBorMainService.bormCustNoEq(iCustNo, tFacMain.getFacmNo(),
				tFacMain.getFacmNo(), 1, 900, 0, Integer.MAX_VALUE, titaVo);

		lLoanBorMain2 = slLoanBorMain2 == null ? null : slLoanBorMain2.getContent();
		// 放款餘額
		BigDecimal wkLoanBal = BigDecimal.ZERO;
		if (lLoanBorMain2 != null && lLoanBorMain2.size() > 0) {
//			lLoanBorMain = new ArrayList<LoanBorMain>();
			for (LoanBorMain tLoanBorMain : lLoanBorMain2) {
				wkLoanBal = wkLoanBal.add(tLoanBorMain.getLoanBal());
			}
		}
		occursList.putParam("OOALoanBal", wkLoanBal); // 放款餘額

		// 是否保證 ???
		occursList.putParam("OOAGuarantee", "");
		if (lGuarantor != null) {
			for (Guarantor gu : lGuarantor) {
				if (gu.getApproveNo() != tFacMain.getApplNo()) {
					occursList.putParam("OOAGuarantee", "Y");
				}
			}
		}

		// 暫收支票
		occursList.putParam("OOATempCheque", "");
		if (lAcReceivable != null) {
			for (AcReceivable rv : lAcReceivable) {
				if (rv.getFacmNo() == tFacMain.getFacmNo()) {
					occursList.putParam("OOATempCheque", "Y");
				}
			}
		}

		occursList.putParam("OOABdLocation", ""); // 門牌號碼
		occursList.putParam("OOABdRmk", ""); // 建物標示備註

		// 門牌號碼、土地建號

		// ClFac擔保品與額度關聯檔

		ClFac tClFac = clFacService.mainClNoFirst(tFacMain.getCustNo(), tFacMain.getFacmNo(), "Y", titaVo);

		if (tClFac != null) {
			// 房地
			if (tClFac.getClCode1() == 1) {
				// ClBuilding 擔保品建物檔
				ClBuilding tClBuilding = clBuildingService
						.findById(new ClBuildingId(tClFac.getClCode1(), tClFac.getClCode2(), tClFac.getClNo()), titaVo);
				if (tClBuilding != null) {
					occursList.putParam("OOABdLocation", tClBuilding.getBdLocation());
				}
			}
			// ClImm擔保品不動產檔
			ClImm tClImm = clImmService
					.findById(new ClImmId(tClFac.getClCode1(), tClFac.getClCode2(), tClFac.getClNo()), titaVo);
			if (tClImm != null) {
				occursList.putParam("OOABdRmk", tClImm.getBdRmk()); // 建物標示備註
			}
		}

		totaA.addOccursList(occursList);
	}

	// totaB 清償金類型
	private void setTotaB(FacMain tFacMain, TitaVo titaVo) throws LogicException {
		// 額度 商品 是否綁約 違約適用方式 綁約期限 還款起算比例 違約金百分比 分段月數 遞減百分比 違約金收取方式
		cntB++;
		totaB.putParam("MSGID", "L293B");
		OccursList occursList = new OccursList();

		// 商品主檔
		FacProd tFacProd = facProdService.findById(tFacMain.getProdNo(), titaVo);
		if (tFacProd == null) {
			throw new LogicException(titaVo, "E0001", "商品主檔"); // 查詢資料不存在
		}
		if (tFacProd.getProhibitMonth() > 0 && tFacMain.getFirstDrawdownDate() > 0) {
			dDateUtil.init();
			dDateUtil.setDate_1(tFacMain.getFirstDrawdownDate());
			dDateUtil.setMons(tFacProd.getProhibitMonth());
		}
		occursList.putParam("OOBFacmNo", tFacMain.getFacmNo()); // 額度
		occursList.putParam("OOBProdNo", tFacMain.getProdNo()); // 商品代碼
		occursList.putParam("OOBBreachFlag", tFacProd.getBreachFlag()); // 是否綁約
		occursList.putParam("OOBBreachCode", ""); // 違約適用方式
		occursList.putParam("OOBProhibitDate", ""); // 綁約期限
		occursList.putParam("OOBBreachStartPercent", ""); // 還款起算比例
		occursList.putParam("OOBBreachPercent", ""); // 違約金百分比
		occursList.putParam("OOBBreachDecreaseMonth", ""); // 分段月數
		occursList.putParam("OOBBreachDecrease", ""); // 遞減百分比
		occursList.putParam("OOBBreachGetCode", ""); // 違約金收取方式
		if ("Y".equals(tFacProd.getBreachFlag())) {
			occursList.putParam("OOBBreachCode", tFacProd.getBreachCode()); // 違約適用方式
			if (tFacProd.getProhibitMonth() > 0 && tFacMain.getFirstDrawdownDate() > 0) {
				dDateUtil.init();
				dDateUtil.setDate_1(tFacMain.getFirstDrawdownDate());
				dDateUtil.setMons(tFacProd.getProhibitMonth());
				occursList.putParam("OOBProhibitDate", dDateUtil.getCalenderDay()); // 綁約期限
			}
			occursList.putParam("OOBBreachStartPercent", tFacProd.getBreachStartPercent()); // 還款起算比例
			occursList.putParam("OOBBreachPercent", tFacProd.getBreachPercent()); // 違約金百分比
			occursList.putParam("OOBBreachDecreaseMonth", tFacProd.getBreachDecreaseMonth()); // 分段月數
			occursList.putParam("OOBBreachDecrease", tFacProd.getBreachDecrease()); // 遞減百分比
			occursList.putParam("OOBBreachGetCode", tFacProd.getBreachGetCode()); // 違約金收取方式
		}
		totaB.addOccursList(occursList);
	}

	// totaC 抵押權塗銷同意書
	private void setTotaC(ClFac tClFac, TitaVo titaVo) throws LogicException {
// 擔保品編號  縣市	地政	收件年	收件字	收件號 權利價值說明 擔保債權總金額  全部結案
		cntC++;
		totaC.putParam("MSGID", "L293C");
		OccursList occursList = new OccursList();
		occursList.putParam("OOCClCode1", tClFac.getClCode1());
		occursList.putParam("OOCClCode2", tClFac.getClCode2());
		occursList.putParam("OOCClNo", tClFac.getClNo());
		occursList.putParam("OOCSeq", "");
		occursList.putParam("OOCCity", "");
		occursList.putParam("OOCLandAdm", "");
		occursList.putParam("OOCRecYear", "");
		occursList.putParam("OOCRecWord", "");
		occursList.putParam("OOCRecNumber", "");
		occursList.putParam("OOCRightsNote", "");
		occursList.putParam("OOCSecuredTotal", "0");
		// 全部結案
		List<ClFac> l2ClFac = new ArrayList<ClFac>(); // 擔保品與額度關聯檔
		Slice<ClFac> slClFac = clFacService.clNoEq(tClFac.getClCode1(), tClFac.getClCode2(), tClFac.getClNo(), 0,
				Integer.MAX_VALUE, titaVo);
		l2ClFac = slClFac == null ? null : slClFac.getContent();
		boolean isAllClose = true;
		for (ClFac c : l2ClFac) {

			if ((c.getCustNo() == iCustNo && iFacmNo == 0) || (c.getCustNo() == iCustNo && c.getFacmNo() == iFacmNo)) {

				// 撥款主檔
				Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(c.getCustNo(), c.getFacmNo(),
						c.getFacmNo(), 1, 900, 0, Integer.MAX_VALUE, titaVo);
				if (slLoanBorMain != null) {
					for (LoanBorMain t : slLoanBorMain.getContent()) {
						// 戶況 0: 正常戶1:展期2: 催收戶3: 結案戶4: 逾期戶5: 催收結案戶6: 呆帳戶7: 部分轉呆戶8: 債權轉讓戶9: 呆帳結案戶
						if (t.getStatus() == 0 || t.getStatus() == 2 || t.getStatus() == 4 || t.getStatus() == 6
								|| t.getStatus() == 8) {
							isAllClose = false;
							break;
						}
					}
				}
			}
		}
		Slice<ClOtherRights> slClOtherRights = ClOtherRightsService.findClNo(tClFac.getClCode1(), tClFac.getClCode2(),
				tClFac.getClNo(), 0, Integer.MAX_VALUE, titaVo);

		lClOtherRights = slClOtherRights == null ? null : slClOtherRights.getContent();

		if (lClOtherRights != null) {
			for (ClOtherRights tClOtherRights : lClOtherRights) {
				occursList = new OccursList();

				occursList.putParam("OOCClCode1", tClFac.getClCode1());
				occursList.putParam("OOCClCode2", tClFac.getClCode2());
				occursList.putParam("OOCClNo", tClFac.getClNo());
				occursList.putParam("OOCSeq", tClOtherRights.getSeq());
				occursList.putParam("OOCCity", tClOtherRights.getCity());
				occursList.putParam("OOCLandAdm", tClOtherRights.getLandAdm());
				occursList.putParam("OOCRecYear", tClOtherRights.getRecYear());
				occursList.putParam("OOCRecWord", tClOtherRights.getRecWord());
				occursList.putParam("OOCRecNumber", tClOtherRights.getRecNumber());
				occursList.putParam("OOCRightsNote", tClOtherRights.getRightsNote());
				occursList.putParam("OOCSecuredTotal", tClOtherRights.getSecuredTotal());

				this.info("isAllClose" + isAllClose);
				if (isAllClose) {
					occursList.putParam("OOCAllClose", "Y");
				} else {
					occursList.putParam("OOCAllClose", "N");
				}

				totaC.addOccursList(occursList);
			}
		} else {
			this.info("isAllClose" + isAllClose);
			if (isAllClose) {
				occursList.putParam("OOCAllClose", "Y");
			} else {
				occursList.putParam("OOCAllClose", "N");
			}
			totaC.addOccursList(occursList);
		}
//		// 全部結案
//		List<ClFac> l2ClFac = new ArrayList<ClFac>(); // 擔保品與額度關聯檔
//		Slice<ClFac> slClFac = clFacService.clNoEq(tClFac.getClCode1(), tClFac.getClCode2(), tClFac.getClNo(), 0,
//				Integer.MAX_VALUE, titaVo);
//		l2ClFac = slClFac == null ? null : slClFac.getContent();
//		boolean isAllClose = true;
//		for (ClFac c : l2ClFac) {
//
//			if ((c.getCustNo() == iCustNo && iFacmNo == 0) || (c.getCustNo() == iCustNo && c.getFacmNo() == iFacmNo)) {
//
//				// 撥款主檔
//				Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(c.getCustNo(), c.getFacmNo(),
//						c.getFacmNo(), 1, 900, 0, Integer.MAX_VALUE, titaVo);
//				if (slLoanBorMain != null) {
//					for (LoanBorMain t : slLoanBorMain.getContent()) {
//						// 戶況 0: 正常戶1:展期2: 催收戶3: 結案戶4: 逾期戶5: 催收結案戶6: 呆帳戶7: 部分轉呆戶8: 債權轉讓戶9: 呆帳結案戶
//						if (t.getStatus() == 0 || t.getStatus() == 2 || t.getStatus() == 4 || t.getStatus() == 6
//								|| t.getStatus() == 8) {
//							isAllClose = false;
//							break;
//						}
//					}
//				}
//			}
//		}
//
//		this.info("isAllClose" + isAllClose);
//		if (isAllClose) {
//			occursList.putParam("OOCAllClose", "Y");
//		} else {
//			occursList.putParam("OOCAllClose", "N");
//		}
//		totaC.addOccursList(occursList);
	}
}