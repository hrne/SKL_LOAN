package com.st1.itx.trade.L5;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
/* Tita & Tota 資料物件 */
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
/*DB服務*/
import com.st1.itx.db.service.CustMainService;
/*DB服務*/
import com.st1.itx.db.service.NegMainService;
import com.st1.itx.db.service.springjpa.cm.L5071ServiceImpl;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.NegCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.StringCut;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
* CustId=X,10<br>
* CaseKindCode=9,1<br>
* CustLoanKind=9,1<br>
* Status=9,1<br>
*/

/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
@Service("L5071")
@Scope("prototype")
public class L5071 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public NegMainService sNegMainService;

	@Autowired
	public CustMainService sCustMainService;

	@Autowired
	L5071ServiceImpl l5071ServiceImpl;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public NegCom sNegCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5071 ");
		this.totaVo.init(titaVo);
		this.info("Run L5071");

		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = titaVo.getReturnIndex();
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 200;// 查全部

		List<Map<String, String>> listL5071 = null;
		try {
			listL5071 = l5071ServiceImpl.findAll(titaVo, this.index, this.limit);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L5071ServiceImpl.findAll error = " + errors.toString());
		}

		if (listL5071 != null && listL5071.size() >= this.limit) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(this.setIndexNext());
			// this.totaVo.setMsgEndToAuto();// 自動折返
			this.totaVo.setMsgEndToEnter();// 手動折返
		}
		String CustName = "";
		if (listL5071 == null || listL5071.size() == 0) {
			throw new LogicException(titaVo, "E0001", "債務協商案件主檔");
		} else {
			CustMain tCustMain = new CustMain();
			for (Map<String, String> t5071 : listL5071) {

				OccursList occursList = new OccursList();
				occursList.putParam("OOCustId", t5071.get("F0"));
				tCustMain = sCustMainService.custIdFirst(t5071.get("F0"), titaVo);

				if (tCustMain != null) {
					CustName = StringCut.replaceLineUp(tCustMain.getCustName());
				}
				occursList.putParam("OOCustName", CustName);
				occursList.putParam("OOCaseKindCode", t5071.get("F1"));
				occursList.putParam("OOCustLoanKind", t5071.get("F2"));
				occursList.putParam("OOStatus", t5071.get("F3"));
				occursList.putParam("OOCustNo", t5071.get("F4"));
				occursList.putParam("OOCaseSeq", t5071.get("F5"));
				occursList.putParam("OOApplDate", parse.stringToInteger(t5071.get("F6")) - 19110000);
				occursList.putParam("OODueAmt", t5071.get("F7"));
				occursList.putParam("OOTotalPeriod", t5071.get("F8"));
				occursList.putParam("OOIntRate", t5071.get("F9"));
				occursList.putParam("OOFirstDueDate", parse.stringToInteger(t5071.get("F10")) - 19110000);
				if (parse.stringToInteger(t5071.get("F11")) == 0) {
					occursList.putParam("OOLastDueDate", 0);
				} else {
					occursList.putParam("OOLastDueDate", parse.stringToInteger(t5071.get("F11")) - 19110000);
				}

				occursList.putParam("OOIsMainFin", t5071.get("F12"));
				occursList.putParam("OOTotalContrAmt", t5071.get("F14"));
				occursList.putParam("OOMainFinCode", t5071.get("F13"));
				String MainFinCodeName = sNegCom.FindNegFinAcc(t5071.get("F13"), titaVo)[0];
				occursList.putParam("OOMainFinCodeName", MainFinCodeName);
				this.totaVo.addOccursList(occursList);
			} // for

		}
//		String CustId = titaVo.getParam("CustId").trim(); // 身份證號
//		String CaseKindCode = titaVo.getParam("CaseKindCode").trim(); // 案件種類
//		String CustLoanKind = titaVo.getParam("CustLoanKind").trim(); // 債權戶別
//		String Status = titaVo.getParam("Status").trim(); // 戶況
//
//		int CustNo = 0;// 戶號
//		if (CustId != null && CustId.length() != 0) {
//			this.info("L5071 CustId=[" + CustId + "]");
//			CustMain CustMainVO = sCustMainService.custIdFirst(CustId, titaVo);
//			if (CustMainVO != null) {
//				CustNo = CustMainVO.getCustNo();
//				if (CustNo == 0) {
//					// E1002 戶號不得為0
//					throw new LogicException(titaVo, "E1002", "客戶資料主檔");
//				}
//			} else {
//				// E0001 查詢資料不存在
//				throw new LogicException(titaVo, "E0001", "客戶資料主檔");
//			}
//		}
//		int TestType = 0;
//		if (CaseKindCode != null && CaseKindCode.length() != 0) {
//			TestType++;
//		}
//
//		if (CustLoanKind != null && CustLoanKind.length() != 0) {
//			TestType++;
//		}
//
//		if (Status != null && Status.length() != 0) {
//			TestType++;
//		}
//		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
//		this.index = titaVo.getReturnIndex();
//		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
//		this.limit = 60;// 查全部
//
//		Slice<NegMain> slNegMain = null;
//		List<NegMain> lNegMain = new ArrayList<NegMain>();
//		// 找資料庫資料而已
//		int IsFind = 0;
//		if (TestType == 3) {
//			if (CustNo != 0) {
//				slNegMain = sNegMainService.HaveCustNo(CaseKindCode, CustLoanKind, Status, CustNo, this.index, this.limit, titaVo);
//			} else {
//				slNegMain = sNegMainService.NoCustNo(CaseKindCode, CustLoanKind, Status, this.index, this.limit, titaVo);
//			}
//		} else if (TestType == 0) {
//			if (CustNo != 0) {
//				slNegMain = sNegMainService.CustNoEq(CustNo, this.index, this.limit, titaVo);
//			} else {
//				this.info("L5071 FindAll");
//				slNegMain = sNegMainService.findAll(this.index, this.limit, titaVo);
//			}
//		} else if (TestType == 1) {
//			if (CaseKindCode != null && CaseKindCode.length() != 0) {
//				slNegMain = sNegMainService.CaseKindCodeEq(CaseKindCode, this.index, this.limit, titaVo);
//			}
//
//			if (CustLoanKind != null && CustLoanKind.length() != 0) {
//				slNegMain = sNegMainService.CustLoanKindEq(CustLoanKind, this.index, this.limit, titaVo);
//			}
//
//			if (Status != null && Status.length() != 0) {
//				slNegMain = sNegMainService.StatusEq(Status, this.index, this.limit, titaVo);
//			}
//		} else {
//			if (CustNo != 0) {
//				slNegMain = sNegMainService.CustNoEq(CustNo, this.index, this.limit, titaVo);
//			} else {
//				int TestHadData = 0;
//				if (TestHadData == 0) {
//					if (CaseKindCode != null && CaseKindCode.length() != 0) {
//						slNegMain = sNegMainService.CaseKindCodeEq(CaseKindCode, this.index, this.limit, titaVo);
//						TestHadData++;
//					}
//				}
//
//				if (TestHadData == 0) {
//					if (CustLoanKind != null && CustLoanKind.length() != 0) {
//						slNegMain = sNegMainService.CustLoanKindEq(CustLoanKind, this.index, this.limit, titaVo);
//						TestHadData++;
//					}
//				}
//
//				if (TestHadData == 0) {
//					if (Status != null && Status.length() != 0) {
//						slNegMain = sNegMainService.StatusEq(Status, this.index, this.limit, titaVo);
//						TestHadData++;
//					}
//				}
//
//			}
//			IsFind = 1;
//			lNegMain = slNegMain == null ? null : slNegMain.getContent();
//			List<NegMain> lNegMainTemp = new ArrayList<NegMain>();
//			if (lNegMain != null) {
//				for (NegMain NegMainVO : lNegMain) {
//					String NegMainCaseKindCode = NegMainVO.getCaseKindCode();
//					String NegMainCustLoanKind = NegMainVO.getCustLoanKind();
//					String NegMainStatus = NegMainVO.getStatus();
//
//					if (CaseKindCode != null && CaseKindCode.length() != 0) {
//						if (!CaseKindCode.equals(NegMainCaseKindCode)) {
//							continue;
//						}
//					}
//
//					if (CustLoanKind != null && CustLoanKind.length() != 0) {
//						if (!CustLoanKind.equals(NegMainCustLoanKind)) {
//							continue;
//						}
//					}
//
//					if (Status != null && Status.length() != 0) {
//						if (!Status.equals(NegMainStatus)) {
//							continue;
//						}
//					}
//					lNegMainTemp.add(NegMainVO);
//				}
//				lNegMain = lNegMainTemp;
//			} else {
//				// lNegMain==null
//			}
//		}
//		if (IsFind != 1) {
//			lNegMain = slNegMain == null ? null : slNegMain.getContent();
//		}
//
//		// 主要邏輯
//		this.info("L5071 this.index=[" + this.index + "],this.limit=[" + this.limit + "],this.setIndexNext()=[" + this.setIndexNext() + "]");
//		if (lNegMain != null && lNegMain.size() != 0) {
//			// lNegMain=lNegMain.sor
//			for (NegMain NegMainVO : lNegMain) {
//				OccursList occursList = new OccursList();
//				/* key 名稱需與L5071.tom相同 檔案位於.8的iTX/L5/L5071.tom */
//				/* 將每筆資料放入Tota的OcList */
//
//				int ThisCustNo = NegMainVO.getCustNo();
//				if (CustNo != 0 && CustNo != ThisCustNo) {
//					continue;
//				}
//				String ThisCustId = CustId;
//				CustMain CustMainVO = sCustMainService.custNoFirst(ThisCustNo, ThisCustNo, titaVo);
//				ThisCustId = CustMainVO.getCustId();
//				String MainFinCode = NegMainVO.getMainFinCode();
//				String MainFinCodeName = sNegCom.FindCdBank(NegMainVO.getMainFinCode(), titaVo)[0];
//				occursList.putParam("OOCustId", ThisCustId);
//				occursList.putParam("OOCaseKindCode", NegMainVO.getCaseKindCode());
//				occursList.putParam("OOCustLoanKind", NegMainVO.getCustLoanKind());
//				occursList.putParam("OOStatus", NegMainVO.getStatus());
//				occursList.putParam("OOCustNo", NegMainVO.getCustNo());
//				occursList.putParam("OOCaseSeq", NegMainVO.getCaseSeq());
//				occursList.putParam("OOApplDate", NegMainVO.getApplDate());
//				occursList.putParam("OODueAmt", NegMainVO.getDueAmt());
//				occursList.putParam("OOTotalPeriod", NegMainVO.getTotalPeriod());
//				occursList.putParam("OOIntRate", NegMainVO.getIntRate());
//				occursList.putParam("OOFirstDueDate", NegMainVO.getFirstDueDate());
//				occursList.putParam("OOLastDueDate", NegMainVO.getLastDueDate());
//				occursList.putParam("OOIsMainFin", NegMainVO.getIsMainFin());
//				occursList.putParam("OOTotalContrAmt", NegMainVO.getTotalContrAmt());
//				occursList.putParam("OOMainFinCode", MainFinCode);
//				occursList.putParam("OOMainFinCodeName", MainFinCodeName);
//				this.totaVo.addOccursList(occursList);
//			}
//			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
//			titaVo.setReturnIndex(this.setIndexNext());
//			// this.totaVo.setMsgEndToAuto();// 自動折返
//			this.totaVo.setMsgEndToEnter();// 手動折返
//		} else {
//			// 查無資料
//			throw new LogicException(titaVo, "E0001", "債務協商案件主檔");
//		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}