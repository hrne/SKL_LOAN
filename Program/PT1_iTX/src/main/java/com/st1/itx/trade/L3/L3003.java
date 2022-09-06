package com.st1.itx.trade.L3;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.parse.Parse;

/*
 * L3003 預約撥款明細資料查詢
 * a.此功能供查詢某一戶號下,其下各筆預約撥款資料,已完成撥款者不會顯示.
 */

/**
 * L3003 預約撥款明細資料查詢
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3003")
@Scope("prototype")
public class L3003 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public LoanCom loanCom;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3003 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRvStartDate = this.parse.stringToInteger(titaVo.getParam("RvStartDate"));
		int iRvEndDate = this.parse.stringToInteger(titaVo.getParam("RvEndDate"));
		int iCaseNo = this.parse.stringToInteger(titaVo.getParam("CaseNo"));
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		int iStatus = parse.stringToInteger(titaVo.getParam("Status"));
//		1.已撥款
//		2.已放行
//		3.未放行
//		4.刪除
//		9.全部
		int iCustDataCtrl = 0;

		// work area
		Slice<LoanBorMain> slLoanBorMain;
		List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();
		int wkFacmNo1 = 1;
		int wkFacmNo2 = 999;
		FacMain tFacMain = new FacMain();
		int wkTotalCount = 0;

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 55 * 600 = 33000

		// 查詢放款主檔, 已完成撥款者不會顯示
		List<Integer> lBormStatus = new ArrayList<Integer>();
		lBormStatus.add(97); // 97:預約撥款已刪除
		lBormStatus.add(98); // 98:預約撥款已撥款
		lBormStatus.add(99); // 99:預約撥款
		if (iRvStartDate > 0) {
			slLoanBorMain = loanBorMainService.bormDrawdownDateRange(iRvStartDate + 19110000, iRvEndDate + 19110000, 901, 999, lBormStatus, this.index, this.limit, titaVo);
			lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();
		} else {
			if (iCaseNo > 0) {
				Slice<FacMain> slFacMain = facMainService.facmCreditSysNoRange(iCaseNo, iCaseNo, 1, 999, 0, Integer.MAX_VALUE, titaVo);
				List<FacMain> lFacMain = slFacMain == null ? null : slFacMain.getContent();
				if (lFacMain == null || lFacMain.size() == 0) {
					throw new LogicException(titaVo, "E0001", "額度主檔 案件編號 = " + iCaseNo); // 查詢資料不存在
				}
				List<Integer> lFacmNo = new ArrayList<Integer>();
				for (FacMain xFacMain : lFacMain) {
					lFacmNo.add(xFacMain.getFacmNo());
				}
				slLoanBorMain = loanBorMainService.bormFacmNoIn(iCustNo, lFacmNo, 901, 999, this.index, this.limit, titaVo);
				lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();
			} else {
				if (iFacmNo > 0) {
					wkFacmNo1 = iFacmNo;
					wkFacmNo2 = iFacmNo;
				}
				slLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, wkFacmNo1, wkFacmNo2, 901, 999, this.index, this.limit, titaVo);
				lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();
			}

			iCustDataCtrl = this.getTxBuffer().getTxCom().getCustDataCtrl();

		}
		if (lLoanBorMain == null || lLoanBorMain.size() == 0) {
			throw new LogicException(titaVo, "E0001", "放款主檔"); // 查詢資料不存在
		}

		for (LoanBorMain tLoanBorMain : lLoanBorMain) {

//			String iStatus = titaVo.getParam("Status");

			// 1.已撥款
			if (iStatus == 1 && tLoanBorMain.getStatus() != 98) {
				continue;
			}
			// 2.已放行
			if (iStatus == 2 && tLoanBorMain.getActFg() != 2) {
				continue;
			}
			// 3.未放行
			if (iStatus == 3 && tLoanBorMain.getActFg() != 1) {
				continue;
			}
			// 4.刪除
			if (iStatus == 4 && tLoanBorMain.getStatus() != 97) {
				continue;
			}

			tFacMain = facMainService.findById(new FacMainId(tLoanBorMain.getCustNo(), tLoanBorMain.getFacmNo()), titaVo);
			if (tFacMain == null) {
				throw new LogicException(titaVo, "E0001", "額度主檔 借款人戶號 = " + tLoanBorMain.getCustNo() + "額度編號 = " + tLoanBorMain.getFacmNo()); // 查詢資料不存在
			}
			OccursList occursList = new OccursList();
			occursList.putParam("OODrawdownDate", tLoanBorMain.getDrawdownDate());
			occursList.putParam("OOCaseNo", tFacMain.getCreditSysNo());
			occursList.putParam("OOApplNo", tFacMain.getApplNo());
			occursList.putParam("OOCustNo", tLoanBorMain.getCustNo());
			occursList.putParam("OOCustName", loanCom.getCustNameByNo(tLoanBorMain.getCustNo()));
			occursList.putParam("OOFacmNo", tLoanBorMain.getFacmNo());
			occursList.putParam("OOBormNo", tLoanBorMain.getBormNo());
			occursList.putParam("OOCurrencyCode", tLoanBorMain.getCurrencyCode());
			occursList.putParam("OODrawdownAmt", tLoanBorMain.getDrawdownAmt());
			occursList.putParam("OOStatus", tLoanBorMain.getStatus());
			occursList.putParam("OOActFlag", tLoanBorMain.getActFg());

			// if (iCustDataCtrl == 1) {
			// occursList.putParam("OOCustNo", "");
			// }
			// 將每筆資料放入Tota的OcList
			this.totaVo.addOccursList(occursList);
			wkTotalCount++;

		}
		if (wkTotalCount == 0) {
			throw new LogicException(titaVo, "E0001", "放款主檔"); // 查詢資料不存在
		}

		// 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可
		if (slLoanBorMain != null && slLoanBorMain.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}