package com.st1.itx.trade.L1;

import java.util.ArrayList;
import java.util.List;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
/* Tita & Tota 資料物件 */
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.CustCross;
//import com.st1.itx.db.domain.CustFin;
/* DB容器 */
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.CustTelNo;
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacShareAppl;
import com.st1.itx.db.domain.Guarantor;
import com.st1.itx.db.domain.LoanNotYet;
import com.st1.itx.db.domain.ReltMain;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.CustCrossService;
//import com.st1.itx.db.service.CustFinService;
import com.st1.itx.db.domain.FinReportDebt;
import com.st1.itx.db.service.FinReportDebtService;
/* DB服務 */
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.CustTelNoService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacShareApplService;
import com.st1.itx.db.service.GuarantorService;
import com.st1.itx.db.service.LoanNotYetService;
import com.st1.itx.db.service.ReltMainService;

import com.st1.itx.db.domain.CustDataCtrl;
import com.st1.itx.db.service.CustDataCtrlService;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * L1101 顧客明細資料查詢<br>
 * CustNo=X,7<br>
 * CustId=X,10<br>
 * CustName=X,40<br>
 * Mobile=X,15<br>
 * RETURN=9,1<br>
 * END=X,1<br>
 * RTNTXT=X,9<br>
 * 
 * @author AdamPan
 * @version 1.0.0
 *
 */

@Service("L1001")
@Scope("prototype")
public class L1001 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService iCustMainService;

	@Autowired
	public CustTelNoService iCustTelNoService;

	@Autowired
	public FinReportDebtService finReportDebtService;

	@Autowired
	public FacMainService sFacMainService;

	@Autowired
	public ReltMainService iReltMainService;

	@Autowired
	public GuarantorService sGuarantorService;

	@Autowired
	public ClMainService sClMainService;

	@Autowired
	public LoanNotYetService sLoanNotYetService;

	@Autowired
	public FacCaseApplService sFacCaseApplService;

	@Autowired
	public FacShareApplService sFacShareApplService;

	@Autowired
	public CustCrossService sCustCrossService;

	@Autowired
	public ClFacService sClFacService;

	@Autowired
	public CustDataCtrlService custDataCtrlService;

	/* 轉型共用工具 */
	@Autowired
	public Parse iParse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("L1001 Start");
		this.totaVo.init(titaVo);

		int iCustNoSt = iParse.stringToInteger(titaVo.get("CustNoSt"));
		int iCustNoEd = iParse.stringToInteger(titaVo.get("CustNoEd"));
		String iCustId = titaVo.getParam("CustId");
		String iCustNm = titaVo.get("CustName").trim();
		String iMobile = titaVo.get("Mobile").trim();
		String iIndustryCode = titaVo.getParam("IndustryCode");
		int iCustType = iParse.stringToInteger(titaVo.getParam("IdKind"));
		int iKind = 0;
		if (iCustNoSt != 0 && iCustNoEd != 0) {
			// find by custno
			iKind = 1;
		} else if (iCustNoSt == 0 && iCustNoEd != 0) {
			// find by custno
			iKind = 1;
		} else if (!iCustId.equals("")) {
			// find by custid
			iKind = 2;
		} else if (!iCustNm.equals("")) {
			// find by custname
			iKind = 3;
		} else if (!iMobile.equals("")) {
			// find by telno
			iKind = 4;
		} else if (!iIndustryCode.equals("")) {
			iKind = 5;
		} else {
			throw new LogicException(titaVo, "E0001", "查詢功能選擇錯誤");
		}
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 40; // 232 * 40 = 9280

		switch (iKind) {
		case 1:
			Slice<CustMain> iCustMain1 = null;
			iCustMain1 = iCustMainService.custNoRange(iCustNoSt, iCustNoEd, this.index, this.limit, titaVo);
			if (iCustMain1 == null) {
				throw new LogicException("E0001", "客戶檔查無資料"); // 查無資料
			}

			for (CustMain aCustMain : iCustMain1) {
//				if (iCustType == 1) {
//					if (aCustMain.getCustId().length() != 10) {
//						continue;
//					}
//				} else if (iCustType == 2) {
//					if (aCustMain.getCustId().length() != 8) {
//						continue;
//					}
//				}
				if (iCustType > 0 && Integer.valueOf(aCustMain.getCuscCd()) != iCustType) {
					continue;
				}

				totaList(titaVo, aCustMain);
			}
			if (iCustMain1.hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				this.totaVo.setMsgEndToEnter(); // 手動折返
			}
			break;
		case 2:
			CustMain iCustMain2 = new CustMain();
			iCustMain2 = iCustMainService.custIdFirst(iCustId, titaVo);
			if (iCustMain2 == null) {
				throw new LogicException("E0001", "客戶檔查無資料"); // 查無資料
			}
//			if (iCustType == 1) {
//				if (iCustMain2.getCustId().length() != 10) {
//					throw new LogicException("E0001", "客戶檔查無資料"); // 查無資料
//				}
//			} else if (iCustType == 2) {
//				if (iCustMain2.getCustId().length() != 8) {
//					throw new LogicException("E0001", "客戶檔查無資料"); // 查無資料
//				}
//			}
			if (iCustType > 0 && Integer.valueOf(iCustMain2.getCuscCd()) != iCustType) {
				throw new LogicException("E0001", "客戶檔查無資料"); // 查無資料
			}

			totaList(titaVo, iCustMain2);

			break;
		case 3:
			Slice<CustMain> iCustMain3 = null;
			iCustMain3 = iCustMainService.custNameLike("%" + iCustNm + "%", this.index, this.limit, titaVo);
			if (iCustMain3 == null) {
				throw new LogicException("E0001", "客戶檔查無資料"); // 查無資料
			}

			for (CustMain bCustMain : iCustMain3) {
//				if (iCustType == 1) {
//					if (bCustMain.getCustId().length() != 10) {
//						continue;
//					}
//				} else if (iCustType == 2) {
//					if (bCustMain.getCustId().length() != 8) {
//						continue;
//					}
//				}
				
				if (iCustType > 0 && Integer.valueOf(bCustMain.getCuscCd()) != iCustType) {
					continue;
				}
				
				totaList(titaVo, bCustMain);

			}
			if (iCustMain3.hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				this.totaVo.setMsgEndToEnter(); // 手動折返
			}
			break;
		case 4:
			Slice<CustTelNo> iCustTelNo = null;
			iCustTelNo = iCustTelNoService.mobileEq("03", iMobile, this.index, this.limit, titaVo);
			if (iCustTelNo == null) {
				throw new LogicException("E0001", "電話檔查無資料"); // 查無資料
			}

			for (CustTelNo aCustTelNo : iCustTelNo) {
				CustMain dCustMain = new CustMain();
				dCustMain = iCustMainService.findById(aCustTelNo.getCustUKey(), titaVo);
				if (dCustMain == null) {
					throw new LogicException("E0001", "客戶檔查無資料"); // 查無資料
				}
//				if (iCustType == 1) {
//					if (dCustMain.getCustId().length() != 10) {
//						continue;
//					}
//				} else if (iCustType == 2) {
//					if (dCustMain.getCustId().length() != 8) {
//						continue;
//					}
//				}
				if (iCustType > 0 && Integer.valueOf(dCustMain.getCuscCd()) != iCustType) {
					continue;
				}

				totaList(titaVo, dCustMain);
			}
			if (iCustTelNo != null && iCustTelNo.hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				this.totaVo.setMsgEndToEnter(); // 手動折返
			}
			break;
		case 5:
			Slice<CustMain> iCustMain5 = null;
			iCustMain5 = iCustMainService.industryCodeAll(iIndustryCode, this.index, this.limit, titaVo);
			if (iCustMain5 == null) {
				throw new LogicException("E0001", "客戶檔查無資料"); // 查無資料
			}

			for (CustMain eCustMain : iCustMain5) {
//				if (iCustType == 1) {
//					if (eCustMain.getCustId().length() != 10) {
//						continue;
//					}
//				} else if (iCustType == 2) {
//					if (eCustMain.getCustId().length() != 8) {
//						continue;
//					}
//				}
				
				if (iCustType > 0 && Integer.valueOf(eCustMain.getCuscCd()) != iCustType) {
					continue;
				}

				totaList(titaVo, eCustMain);
			}
			if (iCustMain5.hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				this.totaVo.setMsgEndToEnter(); // 手動折返
			}
			break;
		default:
			break;
		}
		if (this.totaVo.getOccursList().size() == 0) {
			throw new LogicException(titaVo, "E0001", "客戶資料主檔"); // 查無資料
		}
		/* 將其中一個tota放入待發送List */
		this.addList(this.totaVo);

		/* 執行結束並返回totaList */
		return this.sendList();
	}

	private void totaList(TitaVo titaVo, CustMain aCustMain) {
		OccursList occursList = new OccursList();
		// 顧客
		int CustMainBTNFg = 0;
		// 財報
		int CustFinBTNFg = 0;
		// 放款
		int FacMainBTNFg = 0;
		// 案件
		int FacCaseApplBTNFg = 0;
		// 未齊件
		int LoanNotYetBTNFg = 0;
		// 保證人
		int GuarantorBTNFg = 0;
		// 關聯戶
		int CustRelBTNFg = 0;
		// 擔保品
		int ClMainBTNFg1 = 0;
		// 共同借款人
		int FacShareApplBTNFg = 0;
		// 交互運用
		int CustCrossBTNFg = 0;
		// 客戶電話
		int CustTleNoBTNFg = 0;

		boolean custDataControl = false;

		CustDataCtrl custDataCtrl = custDataCtrlService.findById(aCustMain.getCustNo(), titaVo);
		if (custDataCtrl != null && custDataCtrl.getApplMark() == 1) {
			custDataControl = true;
		}

		new CustMain();
//		List<CustFin> tmpCustFin = new ArrayList<CustFin>();
		List<FacMain> tmpFacMain = new ArrayList<FacMain>();
		List<LoanNotYet> tmpLoanNotYet = new ArrayList<LoanNotYet>();
		List<FacCaseAppl> tmplFacCaseAppl = new ArrayList<FacCaseAppl>();
		List<ClFac> tmpClFac1 = new ArrayList<ClFac>(); // 擔保品
		List<CustCross> tmpCustCross = new ArrayList<CustCross>();
		List<CustTelNo> tmpCustTelNo = new ArrayList<CustTelNo>(); // 客戶電話
		// ArrayList of 核准號碼
		ArrayList<Integer> listApproveNo = new ArrayList<Integer>();
		// 顧客按鈕fg
//		tmpCustMain = iCustMainService.custIdFirst(aCustMain.getCustId(), titaVo);
//		this.info("顧客" + tmpCustMain);
//		if (tmpCustMain != null) {
		if (!custDataControl) {
			CustMainBTNFg = 1;
		}
//		}
		// 財報按鈕fg
//		Slice<CustFin> stmpCustFin = sCustFinService.custUKeyEq(aCustMain.getCustUKey(), 0, Integer.MAX_VALUE, titaVo);
//		tmpCustFin = stmpCustFin == null ? null : stmpCustFin.getContent();
		if (!custDataControl) {
			Slice<FinReportDebt> slFinReportDebt = finReportDebtService.findCustUKey(aCustMain.getCustUKey(),
					this.index, this.limit, titaVo);
			List<FinReportDebt> lFinReportDebt = slFinReportDebt == null ? null : slFinReportDebt.getContent();

			if (lFinReportDebt != null) {
				CustFinBTNFg = 1;
			}
		}
		// 放款按鈕fg
		if (aCustMain.getCustNo() != 0) {
			Slice<FacMain> stmpFacMain = sFacMainService.facmCustNoRange(aCustMain.getCustNo(), aCustMain.getCustNo(),
					0, 999, 0, Integer.MAX_VALUE, titaVo);
			tmpFacMain = stmpFacMain == null ? null : stmpFacMain.getContent();
//			this.info("放款按鈕" + tmpFacMain);
			if (tmpFacMain != null) {
				FacMainBTNFg = 1;
			}
		}

		if (!custDataControl) {
			Slice<FacCaseAppl> stmplFacCaseAppl = sFacCaseApplService.caseApplCustUKeyEq(aCustMain.getCustUKey(), "0",
					"2", 0, Integer.MAX_VALUE, titaVo);
			tmplFacCaseAppl = stmplFacCaseAppl == null ? null : stmplFacCaseAppl.getContent();
//		this.info("案件=" + tmpLoanNotYet);
			// 案件按鈕
			if (tmplFacCaseAppl != null) {
				FacCaseApplBTNFg = 1;
			}
		}

		// 未齊件按鈕fg
		if (!custDataControl) {
			Slice<LoanNotYet> stmpLoanNotYet = sLoanNotYetService.findCustNoEq(aCustMain.getCustNo(), 0,
					Integer.MAX_VALUE, titaVo);
			tmpLoanNotYet = stmpLoanNotYet == null ? null : stmpLoanNotYet.getContent();
//		this.info("未齊件=" + tmpLoanNotYet);
			if (tmpLoanNotYet != null) {
				LoanNotYetBTNFg = 1;
			}
		}

		// 保證人按鈕fg
		if (!custDataControl) {
			Slice<FacMain> slFacMain = sFacMainService.facmCustNoRange(aCustMain.getCustNo(), aCustMain.getCustNo(), 0,
					999, 0, Integer.MAX_VALUE, titaVo);
			List<FacMain> lFacMain = slFacMain == null ? null : slFacMain.getContent();
			if (lFacMain != null) {
				for (FacMain tFacMain : lFacMain) {
					listApproveNo.add(tFacMain.getApplNo());
				}
				for (int approveNo : listApproveNo) {
					Slice<Guarantor> stmpListGuarantor = sGuarantorService.approveNoEq(approveNo, 0, Integer.MAX_VALUE,
							titaVo);
					List<Guarantor> tmpListGuarantor = stmpListGuarantor == null ? null
							: stmpListGuarantor.getContent();
					if (tmpListGuarantor != null) {
//					this.info("保證人=" + tmpListGuarantor);
						GuarantorBTNFg = 1;
					}
				}
			}
		}

		// 擔保品按鈕fg
		if (!custDataControl) {
			Slice<ClFac> stmpClFac1 = sClFacService.custNoEq(aCustMain.getCustNo(), 0, Integer.MAX_VALUE, titaVo);
			tmpClFac1 = stmpClFac1 == null ? null : stmpClFac1.getContent();
//		this.info("擔保品=" + tmpClFac1);
			if (tmpClFac1 != null) {
				ClMainBTNFg1 = 1;
			}
		}

		// 共同借款人
		if (!custDataControl) {
			Slice<FacShareAppl> stmpFacShareAppl = sFacShareApplService.findCustNoEq(aCustMain.getCustNo(), 0,
					Integer.MAX_VALUE, titaVo);
			if (stmpFacShareAppl != null) {
				FacShareApplBTNFg = 1;
			}
		}

		// 關聯戶按鈕fg
		if (!custDataControl) {
			ReltMain stmpReltMain = iReltMainService.ReltUKeyFirst(aCustMain.getCustUKey(), titaVo);
//		this.info("關聯戶 =" + stmpReltMain);
			if (stmpReltMain != null && !custDataControl) {
				CustRelBTNFg = 1;
			} else {
				ReltMain stmpReltMain1 = iReltMainService.custNoFirst(aCustMain.getCustNo(), titaVo);
				if (stmpReltMain1 != null) {
					CustRelBTNFg = 1;
				}
			}
		}
		// 交互運用按鈕fg
		if (!custDataControl) {
			Slice<CustCross> stmpCustCross = sCustCrossService.custUKeyEq(aCustMain.getCustUKey(), 0, Integer.MAX_VALUE,
					titaVo);
			tmpCustCross = stmpCustCross == null ? null : stmpCustCross.getContent();
			if (tmpCustCross != null) {
				CustCrossBTNFg = 1;
			}
		} else {
			CustCrossBTNFg = 2;
		}

		// 客戶電話按鈕fg
		if (!custDataControl) {
			Slice<CustTelNo> stmpCustTelNo = iCustTelNoService.findCustUKey(aCustMain.getCustUKey(), this.index,
					this.limit, titaVo);
			tmpCustTelNo = stmpCustTelNo == null ? null : stmpCustTelNo.getContent();
			if (tmpCustTelNo != null) {
				CustTleNoBTNFg = 1;
			}
		} else {
			CustTleNoBTNFg = 2;
		}

		// 顧客
		occursList.putParam("OOCustMainBTNFg", CustMainBTNFg);
		// 財報
		occursList.putParam("OOCustFinBTNFg", CustFinBTNFg);
		// 放款
		occursList.putParam("OOFacMainBTNFg", FacMainBTNFg);
		// 案件
		occursList.putParam("OOFacCaseApplBTNFg", FacCaseApplBTNFg);
		// 未齊件
		occursList.putParam("OOLoanNotYetBTNFg", LoanNotYetBTNFg);
		// 保證人
		occursList.putParam("OOGuarantorBTNFg", GuarantorBTNFg);
		// 擔保品
		occursList.putParam("OOClMainBTNFg1", ClMainBTNFg1);
		// 共同借款人
		occursList.putParam("OOFacShareApplBTNFg", FacShareApplBTNFg);
		// 關聯戶
		occursList.putParam("OOCustRelBTNFg", CustRelBTNFg);
		// 交互運用
		occursList.putParam("OOCustCrossBTNFg", CustCrossBTNFg);
		// 電話
		occursList.putParam("OOCustTelNoBTNFg", CustTleNoBTNFg);
		occursList.putParam("OOCustId", aCustMain.getCustId());
		occursList.putParam("OOCustNo", aCustMain.getCustNo());
		occursList.putParam("OOCustTypeCode", aCustMain.getCustTypeCode());
		occursList.putParam("OOCustName", aCustMain.getCustName().replace("$n", "\n"));
		if (custDataControl) {
			occursList.putParam("OODataStatus", "X");
		} else {
			occursList.putParam("OODataStatus", aCustMain.getDataStatus());
		}
		this.totaVo.addOccursList(occursList);

	}
}