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
import com.st1.itx.db.domain.CustFin;
/* DB容器 */
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.CustRelMain;
import com.st1.itx.db.domain.CustRelDetail;
import com.st1.itx.db.domain.CustTelNo;
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacShareAppl;
import com.st1.itx.db.domain.Guarantor;
import com.st1.itx.db.domain.LoanNotYet;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.CustCrossService;
import com.st1.itx.db.service.CustFinService;
/* DB服務 */
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.CustRelDetailService;
import com.st1.itx.db.service.CustRelMainService;
import com.st1.itx.db.service.CustTelNoService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacShareApplService;
import com.st1.itx.db.service.GuarantorService;
import com.st1.itx.db.service.LoanNotYetService;
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
	public CustFinService sCustFinService;

	@Autowired
	public FacMainService sFacMainService;

	@Autowired
	public CustRelMainService iCustRelMainService;

	@Autowired
	public CustRelDetailService iCustRelDetailService;

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
		} else {
			throw new LogicException(titaVo, "E0001", "查詢功能選擇錯誤");
		}
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 250; // 232 * 40 = 9280

		switch (iKind) {
		case 1:
			Slice<CustMain> iCustMain1 = null;
			iCustMain1 = iCustMainService.custNoRange(iCustNoSt, iCustNoEd, this.index, this.limit, titaVo);
			if (iCustMain1 == null) {
				throw new LogicException("E0001", "客戶檔查無資料"); // 查無資料
			}

			for (CustMain aCustMain : iCustMain1) {
				if (iCustType == 1) {
					if (aCustMain.getCustId().length() != 10) {
						continue;
					}
				} else if (iCustType == 2) {
					if (aCustMain.getCustId().length() != 8) {
						continue;
					}
				}
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
				// new
				CustMain tmpCustMain = new CustMain();
				List<CustFin> tmpCustFin = new ArrayList<CustFin>();
				List<FacMain> tmpFacMain = new ArrayList<FacMain>();
				List<LoanNotYet> tmpLoanNotYet = new ArrayList<LoanNotYet>();
				List<FacCaseAppl> tmplFacCaseAppl = new ArrayList<FacCaseAppl>();
				List<ClFac> tmpClFac1 = new ArrayList<ClFac>(); // 擔保品
				List<CustCross> tmpCustCross = new ArrayList<CustCross>();
				List<CustTelNo> tmpCustTelNo = new ArrayList<CustTelNo>(); // 客戶電話
				// ArrayList of 核准號碼
				ArrayList<Integer> listApproveNo = new ArrayList<Integer>();
				// 顧客按鈕fg
				tmpCustMain = iCustMainService.custIdFirst(aCustMain.getCustId(), titaVo);
				this.info("顧客" + tmpCustMain);
				if (tmpCustMain != null) {
					CustMainBTNFg = 1;
				}
				// 財報按鈕fg
				Slice<CustFin> stmpCustFin = sCustFinService.custUKeyEq(aCustMain.getCustUKey(), 0, Integer.MAX_VALUE, titaVo);
				tmpCustFin = stmpCustFin == null ? null : stmpCustFin.getContent();
				this.info("財報" + tmpCustFin);
				if (tmpCustFin != null) {
					CustFinBTNFg = 1;
				}
				// 放款按鈕fg
				if (aCustMain.getCustNo() != 0) {
					Slice<FacMain> stmpFacMain = sFacMainService.facmCustNoRange(aCustMain.getCustNo(), aCustMain.getCustNo(), 0, 999, 0, Integer.MAX_VALUE, titaVo);
					tmpFacMain = stmpFacMain == null ? null : stmpFacMain.getContent();
					this.info("放款按鈕" + tmpFacMain);
					if (tmpFacMain != null) {
						FacMainBTNFg = 1;
					}
				}
				Slice<FacCaseAppl> stmplFacCaseAppl = sFacCaseApplService.caseApplCustUKeyEq(aCustMain.getCustUKey(), "0", "2", 0, Integer.MAX_VALUE, titaVo);
				tmplFacCaseAppl = stmplFacCaseAppl == null ? null : stmplFacCaseAppl.getContent();
				this.info("案件=" + tmpLoanNotYet);
				// 案件按鈕
				if (tmplFacCaseAppl != null) {
					FacCaseApplBTNFg = 1;
				}
				// 未齊件按鈕fg
				Slice<LoanNotYet> stmpLoanNotYet = sLoanNotYetService.findCustNoEq(aCustMain.getCustNo(), 0, Integer.MAX_VALUE, titaVo);
				tmpLoanNotYet = stmpLoanNotYet == null ? null : stmpLoanNotYet.getContent();
				this.info("未齊件=" + tmpLoanNotYet);
				if (tmpLoanNotYet != null) {
					LoanNotYetBTNFg = 1;
				}
				// 保證人按鈕fg
				Slice<FacMain> slFacMain = sFacMainService.facmCustNoRange(aCustMain.getCustNo(), aCustMain.getCustNo(), 0, 999, 0, Integer.MAX_VALUE, titaVo);
				List<FacMain> lFacMain = slFacMain == null ? null : slFacMain.getContent();
				if (lFacMain != null) {
					for (FacMain tFacMain : lFacMain) {
						listApproveNo.add(tFacMain.getApplNo());
					}
					for (int approveNo : listApproveNo) {
						Slice<Guarantor> stmpListGuarantor = sGuarantorService.approveNoEq(approveNo, 0, Integer.MAX_VALUE, titaVo);
						List<Guarantor> tmpListGuarantor = stmpListGuarantor == null ? null : stmpListGuarantor.getContent();
						if (tmpListGuarantor != null) {
							this.info("保證人=" + tmpListGuarantor);
							GuarantorBTNFg = 1;
						}
					}
				}
				// 擔保品按鈕fg
				Slice<ClFac> stmpClFac1 = sClFacService.custNoEq(aCustMain.getCustNo(), 0, Integer.MAX_VALUE, titaVo);
				tmpClFac1 = stmpClFac1 == null ? null : stmpClFac1.getContent();
				this.info("擔保品=" + tmpClFac1);
				if (tmpClFac1 != null) {
					ClMainBTNFg1 = 1;
				}
				// 共同借款人
				Slice<FacShareAppl> stmpFacShareAppl = sFacShareApplService.findCustNoEq(aCustMain.getCustNo(), 0, Integer.MAX_VALUE, titaVo);
				if (stmpFacShareAppl != null) {
					FacShareApplBTNFg = 1;
				}
				// 關聯戶按鈕fg
				CustRelMain stmpCustRelMain = iCustRelMainService.custRelIdFirst(aCustMain.getCustId(), titaVo);
				this.info("關聯戶 =" + stmpCustRelMain);
				if (stmpCustRelMain != null) {
					Slice<CustRelDetail> stmpCustRelDetail = iCustRelDetailService.custRelMainUKeyEq(stmpCustRelMain.getUkey(), 0, Integer.MAX_VALUE, titaVo);
					if (stmpCustRelDetail != null) {
						CustRelBTNFg = 1;
					}
				}
				// 交互運用按鈕fg
				Slice<CustCross> stmpCustCross = sCustCrossService.custUKeyEq(aCustMain.getCustUKey(), 0, Integer.MAX_VALUE, titaVo);
				tmpCustCross = stmpCustCross == null ? null : stmpCustCross.getContent();
				if (tmpCustCross != null) {
					CustCrossBTNFg = 1;
				}

				// 客戶電話按鈕fg
				Slice<CustTelNo> stmpCustTelNo = iCustTelNoService.findCustUKey(aCustMain.getCustUKey(), this.index, this.limit, titaVo);
				tmpCustTelNo = stmpCustTelNo == null ? null : stmpCustTelNo.getContent();
				if (tmpCustTelNo != null) {
					CustTleNoBTNFg = 1;
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
				this.totaVo.addOccursList(occursList);
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
			if (iCustType == 1) {
				if (iCustMain2.getCustId().length() != 10) {
					throw new LogicException("E0001", "客戶檔查無資料"); // 查無資料
				}
			} else if (iCustType == 2) {
				if (iCustMain2.getCustId().length() != 8) {
					throw new LogicException("E0001", "客戶檔查無資料"); // 查無資料
				}
			}
			OccursList occursList2 = new OccursList();
			// 顧客
			int CustMainBTNFg2 = 0;
			// 財報
			int CustFinBTNFg2 = 0;
			// 放款
			int FacMainBTNFg2 = 0;
			// 案件
			int FacCaseApplBTNFg2 = 0;
			// 未齊件
			int LoanNotYetBTNFg2 = 0;
			// 保證人
			int GuarantorBTNFg2 = 0;
			// 關聯戶
			int CustRelBTNFg2 = 0;
			// 擔保品
			int ClMainBTNFg12 = 0;
			// 共同借款人
			int FacShareApplBTNFg2 = 0;
			// 交互運用
			int CustCrossBTNFg2 = 0;
			// 客戶電話
			int CustTleNoBTNFg2 = 0;
			// new
			CustMain tmpCustMain2 = new CustMain();
			List<CustFin> tmpCustFin2 = new ArrayList<CustFin>();
			List<FacMain> tmpFacMain2 = new ArrayList<FacMain>();
			List<LoanNotYet> tmpLoanNotYet2 = new ArrayList<LoanNotYet>();
			List<FacCaseAppl> tmplFacCaseAppl2 = new ArrayList<FacCaseAppl>();
			List<ClFac> tmpClFac12 = new ArrayList<ClFac>(); // 擔保品
			List<CustCross> tmpCustCross2 = new ArrayList<CustCross>();
			List<CustTelNo> tmpCustTelNo2 = new ArrayList<CustTelNo>(); // 客戶電話
			// ArrayList of 核准號碼
			ArrayList<Integer> listApproveNo2 = new ArrayList<Integer>();
			// 顧客按鈕fg
			tmpCustMain2 = iCustMainService.custIdFirst(iCustMain2.getCustId(), titaVo);
			this.info("顧客" + tmpCustMain2);
			if (tmpCustMain2 != null) {
				CustMainBTNFg2 = 1;
			}
			// 財報按鈕fg
			Slice<CustFin> stmpCustFin2 = sCustFinService.custUKeyEq(iCustMain2.getCustUKey(), 0, Integer.MAX_VALUE, titaVo);
			tmpCustFin2 = stmpCustFin2 == null ? null : stmpCustFin2.getContent();
			this.info("財報" + tmpCustFin2);
			if (tmpCustFin2 != null) {
				CustFinBTNFg2 = 1;
			}
			// 放款按鈕fg
			if (iCustMain2.getCustNo() != 0) {
				Slice<FacMain> stmpFacMain2 = sFacMainService.facmCustNoRange(iCustMain2.getCustNo(), iCustMain2.getCustNo(), 0, 999, 0, Integer.MAX_VALUE, titaVo);
				tmpFacMain2 = stmpFacMain2 == null ? null : stmpFacMain2.getContent();
				this.info("放款按鈕" + tmpFacMain2);
				if (tmpFacMain2 != null) {
					FacMainBTNFg2 = 1;
				}
			}
			Slice<FacCaseAppl> stmplFacCaseAppl2 = sFacCaseApplService.caseApplCustUKeyEq(iCustMain2.getCustUKey(), "0", "2", 0, Integer.MAX_VALUE, titaVo);
			tmplFacCaseAppl2 = stmplFacCaseAppl2 == null ? null : stmplFacCaseAppl2.getContent();
			this.info("案件=" + tmpLoanNotYet2);
			// 案件按鈕
			if (tmplFacCaseAppl2 != null) {
				FacCaseApplBTNFg2 = 1;
			}
			// 未齊件按鈕fg
			Slice<LoanNotYet> stmpLoanNotYet2 = sLoanNotYetService.findCustNoEq(iCustMain2.getCustNo(), 0, Integer.MAX_VALUE, titaVo);
			tmpLoanNotYet2 = stmpLoanNotYet2 == null ? null : stmpLoanNotYet2.getContent();
			this.info("未齊件=" + tmpLoanNotYet2);
			if (tmpLoanNotYet2 != null) {
				LoanNotYetBTNFg2 = 1;
			}
			// 保證人按鈕fg
			Slice<FacMain> slFacMain2 = sFacMainService.facmCustNoRange(iCustMain2.getCustNo(), iCustMain2.getCustNo(), 0, 999, 0, Integer.MAX_VALUE, titaVo);
			List<FacMain> lFacMain2 = slFacMain2 == null ? null : slFacMain2.getContent();
			if (lFacMain2 != null) {
				for (FacMain tFacMain2 : lFacMain2) {
					listApproveNo2.add(tFacMain2.getApplNo());
				}
				for (int approveNo2 : listApproveNo2) {
					Slice<Guarantor> stmpListGuarantor2 = sGuarantorService.approveNoEq(approveNo2, 0, Integer.MAX_VALUE, titaVo);
					List<Guarantor> tmpListGuarantor2 = stmpListGuarantor2 == null ? null : stmpListGuarantor2.getContent();
					if (tmpListGuarantor2 != null) {
						this.info("保證人=" + tmpListGuarantor2);
						GuarantorBTNFg2 = 1;
					}
				}
			}
			// 擔保品按鈕fg
			Slice<ClFac> stmpClFac12 = sClFacService.custNoEq(iCustMain2.getCustNo(), 0, Integer.MAX_VALUE, titaVo);
			tmpClFac12 = stmpClFac12 == null ? null : stmpClFac12.getContent();
			this.info("擔保品=" + tmpClFac12);
			if (tmpClFac12 != null) {
				ClMainBTNFg12 = 1;
			}

			// 共同借款人
			Slice<FacShareAppl> stmpFacShareAppl2 = sFacShareApplService.findCustNoEq(iCustMain2.getCustNo(), 0, Integer.MAX_VALUE, titaVo);
			if (stmpFacShareAppl2 != null) {
				FacShareApplBTNFg2 = 1;
			}
			// 關聯戶按鈕fg
			CustRelMain stmpCustRelMain2 = iCustRelMainService.custRelIdFirst(iCustMain2.getCustId(), titaVo);
			this.info("關聯戶 =" + stmpCustRelMain2);
			if (stmpCustRelMain2 != null) {
				Slice<CustRelDetail> stmpCustRelDetail2 = iCustRelDetailService.custRelMainUKeyEq(stmpCustRelMain2.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (stmpCustRelDetail2 != null) {
					CustRelBTNFg2 = 1;
				}
			}
			// 交互運用按鈕fg
			Slice<CustCross> stmpCustCross2 = sCustCrossService.custUKeyEq(iCustMain2.getCustUKey(), 0, Integer.MAX_VALUE, titaVo);
			tmpCustCross2 = stmpCustCross2 == null ? null : stmpCustCross2.getContent();
			if (tmpCustCross2 != null) {
				CustCrossBTNFg2 = 1;
			}

			// 客戶電話按鈕fg
			Slice<CustTelNo> stmpCustTelNo2 = iCustTelNoService.findCustUKey(iCustMain2.getCustUKey(), this.index, this.limit, titaVo);
			tmpCustTelNo2 = stmpCustTelNo2 == null ? null : stmpCustTelNo2.getContent();
			if (tmpCustTelNo2 != null) {
				CustTleNoBTNFg2 = 1;
			}
			// 顧客
			occursList2.putParam("OOCustMainBTNFg", CustMainBTNFg2);
			// 財報
			occursList2.putParam("OOCustFinBTNFg", CustFinBTNFg2);
			// 放款
			occursList2.putParam("OOFacMainBTNFg", FacMainBTNFg2);
			// 案件
			occursList2.putParam("OOFacCaseApplBTNFg", FacCaseApplBTNFg2);
			// 未齊件
			occursList2.putParam("OOLoanNotYetBTNFg", LoanNotYetBTNFg2);
			// 保證人
			occursList2.putParam("OOGuarantorBTNFg", GuarantorBTNFg2);
			// 擔保品
			occursList2.putParam("OOClMainBTNFg1", ClMainBTNFg12);
			// 共同借款人
			occursList2.putParam("OOFacShareApplBTNFg", FacShareApplBTNFg2);
			// 關聯戶
			occursList2.putParam("OOCustRelBTNFg", CustRelBTNFg2);
			// 交互運用
			occursList2.putParam("OOCustCrossBTNFg", CustCrossBTNFg2);
			// 電話
			occursList2.putParam("OOCustTelNoBTNFg", CustTleNoBTNFg2);
			occursList2.putParam("OOCustId", iCustMain2.getCustId());
			occursList2.putParam("OOCustNo", iCustMain2.getCustNo());
			occursList2.putParam("OOCustTypeCode", iCustMain2.getCustTypeCode());
			occursList2.putParam("OOCustName", iCustMain2.getCustName().replace("$n", "\n"));
			this.totaVo.addOccursList(occursList2);
			break;
		case 3:
			Slice<CustMain> iCustMain3 = null;
			iCustMain3 = iCustMainService.custNameLike("%" + iCustNm + "%", this.index, this.limit, titaVo);
			if (iCustMain3 == null) {
				throw new LogicException("E0001", "客戶檔查無資料"); // 查無資料
			}

			for (CustMain bCustMain : iCustMain3) {
				if (iCustType == 1) {
					if (bCustMain.getCustId().length() != 10) {
						continue;
					}
				} else if (iCustType == 2) {
					if (bCustMain.getCustId().length() != 8) {
						continue;
					}
				}
				OccursList occursList3 = new OccursList();
				// 顧客
				int CustMainBTNFg3 = 0;
				// 財報
				int CustFinBTNFg3 = 0;
				// 放款
				int FacMainBTNFg3 = 0;
				// 案件
				int FacCaseApplBTNFg3 = 0;
				// 未齊件
				int LoanNotYetBTNFg3 = 0;
				// 保證人
				int GuarantorBTNFg3 = 0;
				// 關聯戶
				int CustRelBTNFg3 = 0;
				// 擔保品
				int ClMainBTNFg13 = 0;
				// 共同借款人
				int FacShareApplBTNFg3 = 0;
				// 交互運用
				int CustCrossBTNFg3 = 0;
				// 客戶電話
				int CustTleNoBTNFg3 = 0;
				// new
				CustMain tmpCustMain3 = new CustMain();
				List<CustFin> tmpCustFin3 = new ArrayList<CustFin>();
				List<FacMain> tmpFacMain3 = new ArrayList<FacMain>();
				List<LoanNotYet> tmpLoanNotYet3 = new ArrayList<LoanNotYet>();
				List<FacCaseAppl> tmplFacCaseAppl3 = new ArrayList<FacCaseAppl>();
				List<ClFac> tmpClFac13 = new ArrayList<ClFac>(); // 擔保品
				List<CustCross> tmpCustCross3 = new ArrayList<CustCross>();
				List<CustTelNo> tmpCustTelNo3 = new ArrayList<CustTelNo>(); // 客戶電話
				// ArrayList of 核准號碼
				ArrayList<Integer> listApproveNo3 = new ArrayList<Integer>();
				// 顧客按鈕fg
				tmpCustMain3 = iCustMainService.custIdFirst(bCustMain.getCustId(), titaVo);
				this.info("顧客" + tmpCustMain3);
				if (tmpCustMain3 != null) {
					CustMainBTNFg3 = 1;
				}
				// 財報按鈕fg
				Slice<CustFin> stmpCustFin3 = sCustFinService.custUKeyEq(bCustMain.getCustUKey(), 0, Integer.MAX_VALUE, titaVo);
				tmpCustFin3 = stmpCustFin3 == null ? null : stmpCustFin3.getContent();
				this.info("財報" + tmpCustFin3);
				if (tmpCustFin3 != null) {
					CustFinBTNFg3 = 1;
				}
				// 放款按鈕fg
				if (bCustMain.getCustNo() != 0) {
					Slice<FacMain> stmpFacMain3 = sFacMainService.facmCustNoRange(bCustMain.getCustNo(), bCustMain.getCustNo(), 0, 999, 0, Integer.MAX_VALUE, titaVo);
					tmpFacMain3 = stmpFacMain3 == null ? null : stmpFacMain3.getContent();
					this.info("放款按鈕" + tmpFacMain3);
					if (tmpFacMain3 != null) {
						FacMainBTNFg3 = 1;
					}
				}
				Slice<FacCaseAppl> stmplFacCaseAppl3 = sFacCaseApplService.caseApplCustUKeyEq(bCustMain.getCustUKey(), "0", "2", 0, Integer.MAX_VALUE, titaVo);
				tmplFacCaseAppl3 = stmplFacCaseAppl3 == null ? null : stmplFacCaseAppl3.getContent();
				this.info("案件=" + tmpLoanNotYet3);
				// 案件按鈕
				if (tmplFacCaseAppl3 != null) {
					FacCaseApplBTNFg3 = 1;
				}
				// 未齊件按鈕fg
				Slice<LoanNotYet> stmpLoanNotYet3 = sLoanNotYetService.findCustNoEq(bCustMain.getCustNo(), 0, Integer.MAX_VALUE, titaVo);
				tmpLoanNotYet3 = stmpLoanNotYet3 == null ? null : stmpLoanNotYet3.getContent();
				this.info("未齊件=" + tmpLoanNotYet3);
				if (tmpLoanNotYet3 != null) {
					LoanNotYetBTNFg3 = 1;
				}
				// 保證人按鈕fg
				Slice<FacMain> slFacMain3 = sFacMainService.facmCustNoRange(bCustMain.getCustNo(), bCustMain.getCustNo(), 0, 999, 0, Integer.MAX_VALUE, titaVo);
				List<FacMain> lFacMain3 = slFacMain3 == null ? null : slFacMain3.getContent();
				if (lFacMain3 != null) {
					for (FacMain tFacMain3 : lFacMain3) {
						listApproveNo3.add(tFacMain3.getApplNo());
					}
					for (int approveNo3 : listApproveNo3) {
						Slice<Guarantor> stmpListGuarantor3 = sGuarantorService.approveNoEq(approveNo3, 0, Integer.MAX_VALUE, titaVo);
						List<Guarantor> tmpListGuarantor3 = stmpListGuarantor3 == null ? null : stmpListGuarantor3.getContent();
						if (tmpListGuarantor3 != null) {
							this.info("保證人=" + tmpListGuarantor3);
							GuarantorBTNFg3 = 1;
						}
					}
				}
				// 擔保品按鈕fg
				Slice<ClFac> stmpClFac13 = sClFacService.custNoEq(bCustMain.getCustNo(), 0, Integer.MAX_VALUE, titaVo);
				tmpClFac13 = stmpClFac13 == null ? null : stmpClFac13.getContent();
				this.info("擔保品=" + tmpClFac13);
				if (tmpClFac13 != null) {
					ClMainBTNFg13 = 1;
				}

				// 共同借款人
				Slice<FacShareAppl> stmpFacShareAppl3 = sFacShareApplService.findCustNoEq(bCustMain.getCustNo(), 0, Integer.MAX_VALUE, titaVo);
				if (stmpFacShareAppl3 != null) {
					FacShareApplBTNFg3 = 1;
				}
				
				// 關聯戶按鈕fg
				CustRelMain stmpCustRelMain3 = iCustRelMainService.custRelIdFirst(bCustMain.getCustId(), titaVo);
				this.info("關聯戶 =" + stmpCustRelMain3);
				if (stmpCustRelMain3 != null) {
					Slice<CustRelDetail> stmpCustRelDetail3 = iCustRelDetailService.custRelMainUKeyEq(stmpCustRelMain3.getUkey(), 0, Integer.MAX_VALUE, titaVo);
					if (stmpCustRelDetail3 != null) {
						CustRelBTNFg3 = 1;
					}
				}
				// 交互運用按鈕fg
				Slice<CustCross> stmpCustCross3 = sCustCrossService.custUKeyEq(bCustMain.getCustUKey(), 0, Integer.MAX_VALUE, titaVo);
				tmpCustCross3 = stmpCustCross3 == null ? null : stmpCustCross3.getContent();
				if (tmpCustCross3 != null) {
					CustCrossBTNFg3 = 1;
				}

				// 客戶電話按鈕fg
				Slice<CustTelNo> stmpCustTelNo3 = iCustTelNoService.findCustUKey(bCustMain.getCustUKey(), this.index, this.limit, titaVo);
				tmpCustTelNo3 = stmpCustTelNo3 == null ? null : stmpCustTelNo3.getContent();
				if (tmpCustTelNo3 != null) {
					CustTleNoBTNFg3 = 1;
				}
				// 顧客
				occursList3.putParam("OOCustMainBTNFg", CustMainBTNFg3);
				// 財報
				occursList3.putParam("OOCustFinBTNFg", CustFinBTNFg3);
				// 放款
				occursList3.putParam("OOFacMainBTNFg", FacMainBTNFg3);
				// 案件
				occursList3.putParam("OOFacCaseApplBTNFg", FacCaseApplBTNFg3);
				// 未齊件
				occursList3.putParam("OOLoanNotYetBTNFg", LoanNotYetBTNFg3);
				// 保證人
				occursList3.putParam("OOGuarantorBTNFg", GuarantorBTNFg3);
				// 擔保品
				occursList3.putParam("OOClMainBTNFg1", ClMainBTNFg13);
				// 共同借款人
				occursList3.putParam("OOFacShareApplBTNFg", FacShareApplBTNFg3);
				// 關聯戶
				occursList3.putParam("OOCustRelBTNFg", CustRelBTNFg3);
				// 交互運用
				occursList3.putParam("OOCustCrossBTNFg", CustCrossBTNFg3);
				// 電話
				occursList3.putParam("OOCustTelNoBTNFg", CustTleNoBTNFg3);
				occursList3.putParam("OOCustId", bCustMain.getCustId());
				occursList3.putParam("OOCustNo", bCustMain.getCustNo());
				occursList3.putParam("OOCustTypeCode", bCustMain.getCustTypeCode());
				occursList3.putParam("OOCustName", bCustMain.getCustName().replace("$n", "\n"));
				this.totaVo.addOccursList(occursList3);
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
				if (iCustType == 1) {
					if (dCustMain.getCustId().length() != 10) {
						continue;
					}
				} else if (iCustType == 2) {
					if (dCustMain.getCustId().length() != 8) {
						continue;
					}
				}
				OccursList occursList4 = new OccursList();
				// 顧客
				int CustMainBTNFg4 = 0;
				// 財報
				int CustFinBTNFg4 = 0;
				// 放款
				int FacMainBTNFg4 = 0;
				// 案件
				int FacCaseApplBTNFg4 = 0;
				// 未齊件
				int LoanNotYetBTNFg4 = 0;
				// 保證人
				int GuarantorBTNFg4 = 0;
				// 關聯戶
				int CustRelBTNFg4 = 0;
				// 擔保品
				int ClMainBTNFg14 = 0;
				// 共同借款人
				int FacShareApplBTNFg4 = 0;
				// 交互運用
				int CustCrossBTNFg4 = 0;
				// 客戶電話
				int CustTleNoBTNFg4 = 0;
				// new
				CustMain tmpCustMain4 = new CustMain();
				List<CustFin> tmpCustFin4 = new ArrayList<CustFin>();
				List<FacMain> tmpFacMain4 = new ArrayList<FacMain>();
				List<LoanNotYet> tmpLoanNotYet4 = new ArrayList<LoanNotYet>();
				List<FacCaseAppl> tmplFacCaseAppl4 = new ArrayList<FacCaseAppl>();
				List<ClFac> tmpClFac14 = new ArrayList<ClFac>(); // 擔保品
				List<CustCross> tmpCustCross4 = new ArrayList<CustCross>();
				List<CustTelNo> tmpCustTelNo4 = new ArrayList<CustTelNo>(); // 客戶電話
				// ArrayList of 核准號碼
				ArrayList<Integer> listApproveNo4 = new ArrayList<Integer>();
				// 顧客按鈕fg
				tmpCustMain4 = iCustMainService.custIdFirst(dCustMain.getCustId(), titaVo);
				this.info("顧客" + tmpCustMain4);
				if (tmpCustMain4 != null) {
					CustMainBTNFg4 = 1;
				}
				// 財報按鈕fg
				Slice<CustFin> stmpCustFin4 = sCustFinService.custUKeyEq(dCustMain.getCustUKey(), 0, Integer.MAX_VALUE, titaVo);
				tmpCustFin4 = stmpCustFin4 == null ? null : stmpCustFin4.getContent();
				this.info("財報" + tmpCustFin4);
				if (tmpCustFin4 != null) {
					CustFinBTNFg4 = 1;
				}
				// 放款按鈕fg
				if (dCustMain.getCustNo() != 0) {
					Slice<FacMain> stmpFacMain4 = sFacMainService.facmCustNoRange(dCustMain.getCustNo(), dCustMain.getCustNo(), 0, 999, 0, Integer.MAX_VALUE, titaVo);
					tmpFacMain4 = stmpFacMain4 == null ? null : stmpFacMain4.getContent();
					this.info("放款按鈕" + tmpFacMain4);
					if (tmpFacMain4 != null) {
						FacMainBTNFg4 = 1;
					}
				}
				Slice<FacCaseAppl> stmplFacCaseAppl4 = sFacCaseApplService.caseApplCustUKeyEq(dCustMain.getCustUKey(), "0", "2", 0, Integer.MAX_VALUE, titaVo);
				tmplFacCaseAppl4 = stmplFacCaseAppl4 == null ? null : stmplFacCaseAppl4.getContent();
				this.info("案件=" + tmpLoanNotYet4);
				// 案件按鈕
				if (tmplFacCaseAppl4 != null) {
					FacCaseApplBTNFg4 = 1;
				}
				// 未齊件按鈕fg
				Slice<LoanNotYet> stmpLoanNotYet4 = sLoanNotYetService.findCustNoEq(dCustMain.getCustNo(), 0, Integer.MAX_VALUE, titaVo);
				tmpLoanNotYet4 = stmpLoanNotYet4 == null ? null : stmpLoanNotYet4.getContent();
				this.info("未齊件=" + tmpLoanNotYet4);
				if (tmpLoanNotYet4 != null) {
					LoanNotYetBTNFg4 = 1;
				}
				// 保證人按鈕fg
				Slice<FacMain> slFacMain4 = sFacMainService.facmCustNoRange(dCustMain.getCustNo(), dCustMain.getCustNo(), 0, 999, 0, Integer.MAX_VALUE, titaVo);
				List<FacMain> lFacMain4 = slFacMain4 == null ? null : slFacMain4.getContent();
				if (lFacMain4 != null) {
					for (FacMain tFacMain4 : lFacMain4) {
						listApproveNo4.add(tFacMain4.getApplNo());
					}
					for (int approveNo4 : listApproveNo4) {
						Slice<Guarantor> stmpListGuarantor4 = sGuarantorService.approveNoEq(approveNo4, 0, Integer.MAX_VALUE, titaVo);
						List<Guarantor> tmpListGuarantor4 = stmpListGuarantor4 == null ? null : stmpListGuarantor4.getContent();
						if (tmpListGuarantor4 != null) {
							this.info("保證人=" + tmpListGuarantor4);
							GuarantorBTNFg4 = 1;
						}
					}
				}
				// 擔保品按鈕fg
				Slice<ClFac> stmpClFac14 = sClFacService.custNoEq(dCustMain.getCustNo(), 0, Integer.MAX_VALUE, titaVo);
				tmpClFac14 = stmpClFac14 == null ? null : stmpClFac14.getContent();
				this.info("擔保品=" + tmpClFac14);
				if (tmpClFac14 != null) {
					ClMainBTNFg14 = 1;
				}
				
				// 共同借款人
				Slice<FacShareAppl> stmpFacShareAppl4 = sFacShareApplService.findCustNoEq(dCustMain.getCustNo(), 0, Integer.MAX_VALUE, titaVo);
				if (stmpFacShareAppl4 != null) {
					FacShareApplBTNFg4 = 1;
				}
				

				// 關聯戶按鈕fg
				CustRelMain stmpCustRelMain4 = iCustRelMainService.custRelIdFirst(dCustMain.getCustId(), titaVo);
				this.info("關聯戶 =" + stmpCustRelMain4);
				if (stmpCustRelMain4 != null) {
					Slice<CustRelDetail> stmpCustRelDetail4 = iCustRelDetailService.custRelMainUKeyEq(stmpCustRelMain4.getUkey(), 0, Integer.MAX_VALUE, titaVo);
					if (stmpCustRelDetail4 != null) {
						CustRelBTNFg4 = 1;
					}
				}
				// 交互運用按鈕fg
				Slice<CustCross> stmpCustCross4 = sCustCrossService.custUKeyEq(dCustMain.getCustUKey(), 0, Integer.MAX_VALUE, titaVo);
				tmpCustCross4 = stmpCustCross4 == null ? null : stmpCustCross4.getContent();
				if (tmpCustCross4 != null) {
					CustCrossBTNFg4 = 1;
				}

				// 客戶電話按鈕fg
				Slice<CustTelNo> stmpCustTelNo4 = iCustTelNoService.findCustUKey(dCustMain.getCustUKey(), this.index, this.limit, titaVo);
				tmpCustTelNo4 = stmpCustTelNo4 == null ? null : stmpCustTelNo4.getContent();
				if (tmpCustTelNo4 != null) {
					CustTleNoBTNFg4 = 1;
				}
				// 顧客
				occursList4.putParam("OOCustMainBTNFg", CustMainBTNFg4);
				// 財報
				occursList4.putParam("OOCustFinBTNFg", CustFinBTNFg4);
				// 放款
				occursList4.putParam("OOFacMainBTNFg", FacMainBTNFg4);
				// 案件
				occursList4.putParam("OOFacCaseApplBTNFg", FacCaseApplBTNFg4);
				// 未齊件
				occursList4.putParam("OOLoanNotYetBTNFg", LoanNotYetBTNFg4);
				// 保證人
				occursList4.putParam("OOGuarantorBTNFg", GuarantorBTNFg4);
				// 擔保品
				occursList4.putParam("OOClMainBTNFg1", ClMainBTNFg14);
				// 擔保品
				occursList4.putParam("OOClMainBTNFg1", ClMainBTNFg14);
				// 共同借款人
				occursList4.putParam("OOFacShareApplBTNFg", FacShareApplBTNFg4);
				// 關聯戶
				occursList4.putParam("OOCustRelBTNFg", CustRelBTNFg4);
				// 交互運用
				occursList4.putParam("OOCustCrossBTNFg", CustCrossBTNFg4);
				// 電話
				occursList4.putParam("OOCustTelNoBTNFg", CustTleNoBTNFg4);
				occursList4.putParam("OOCustId", dCustMain.getCustId());
				occursList4.putParam("OOCustNo", dCustMain.getCustNo());
				occursList4.putParam("OOCustTypeCode", dCustMain.getCustTypeCode());
				occursList4.putParam("OOCustName", dCustMain.getCustName().replace("$n", "\n"));
				this.totaVo.addOccursList(occursList4);
			}
			if (iCustTelNo != null && iCustTelNo.hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				this.totaVo.setMsgEndToEnter(); // 手動折返
			}
			break;
		default:
			break;
		}

		/* 將其中一個tota放入待發送List */
		this.addList(this.totaVo);

		/* 執行結束並返回totaList */
		return this.sendList();
	}
}