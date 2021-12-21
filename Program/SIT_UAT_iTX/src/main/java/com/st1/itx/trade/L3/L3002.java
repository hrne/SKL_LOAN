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
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.LoanBook;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBookService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/*
 * Tita
 * CaseNo=9,7
 * TimCustNo=9,7
 * TimCustId=X,10
 * ApplNo=9,7
 * FacmNo=9,3
 * BormNo=9,3
 */
/**
 * L3002 撥款明細資料查詢
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3002")
@Scope("prototype")
public class L3002 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public LoanBookService loanBookService;
	@Autowired
	public CustMainService custMainService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3002 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iCaseNo = this.parse.stringToInteger(titaVo.getParam("CaseNo"));
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		int iDrawdownDateS = this.parse.stringToInteger(titaVo.getParam("DrawdownDateS"));
		int iDrawdownDateE = this.parse.stringToInteger(titaVo.getParam("DrawdownDateE"));

		int iCustDataCtrl = 0;
		// work area
		List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();
		Slice<LoanBorMain> slLoanBorMain;
		int wkCustNo = 0;
		int wkFacmNoStart = 0;
		int wkFacmNoEnd = 0;

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 68 * 600 = 40800

		// 查詢放款主檔
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
			slLoanBorMain = loanBorMainService.bormFacmNoIn(iCustNo, lFacmNo, 1, 900, this.index, this.limit, titaVo);

			iCustDataCtrl = this.getTxBuffer().getTxCom().getCustDataCtrl();
		} else if (iDrawdownDateS > 0) {

			// 查詢放款主檔, 預約撥款者不會顯示
			List<Integer> lBormStatus = new ArrayList<Integer>();
			lBormStatus.add(0); // 0: 正常戶
			lBormStatus.add(1); // 1:展期
			lBormStatus.add(2); // 2: 催收戶
			lBormStatus.add(3); // 3: 結案戶
			lBormStatus.add(4); // 4: 逾期戶
			lBormStatus.add(5); // 5: 催收結案戶
			lBormStatus.add(6); // 6: 呆帳戶
			lBormStatus.add(7); // 7: 部分轉呆戶
			lBormStatus.add(8); // 8: 債權轉讓戶
			lBormStatus.add(9); // 9: 呆帳結案戶

			slLoanBorMain = loanBorMainService.bormDrawdownDateRange(iDrawdownDateS + 19110000, iDrawdownDateE + 19110000, 1, 900, lBormStatus, this.index, this.limit, titaVo);

		} else {

			wkCustNo = iCustNo;
			if (iFacmNo > 0) {
				wkFacmNoStart = iFacmNo;
				wkFacmNoEnd = iFacmNo;
			} else {
				wkFacmNoStart = 1;
				wkFacmNoEnd = 999;
			}
			slLoanBorMain = loanBorMainService.bormCustNoEq(wkCustNo, wkFacmNoStart, wkFacmNoEnd, 1, 900, this.index, this.limit, titaVo);
			iCustDataCtrl = this.getTxBuffer().getTxCom().getCustDataCtrl();
		}

		lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();

		if (lLoanBorMain == null || lLoanBorMain.size() == 0) {
			throw new LogicException(titaVo, "E0001", "放款主檔"); // 查詢資料不存在
		}
		// 如有有找到資料
		FacMain tFacMain = new FacMain();
		CustMain tCustMain = new CustMain();
		LoanBook tLoanBook = new LoanBook();
		for (LoanBorMain tLoanBorMain : lLoanBorMain) {
			OccursList occursList = new OccursList();
			// 查詢額度檔
			tFacMain = facMainService.findById(new FacMainId(tLoanBorMain.getCustNo(), tLoanBorMain.getFacmNo()), titaVo);
			if (tFacMain == null) {
				throw new LogicException(titaVo, "E0001", "額度主檔 借款人戶號 = " + tLoanBorMain.getCustNo() + "額度編號 = " + tLoanBorMain.getFacmNo()); // 查詢資料不存在
			}
			occursList.putParam("OOCaseNo", tFacMain.getCreditSysNo());
			occursList.putParam("OOApplNo", tFacMain.getApplNo());
			occursList.putParam("OOCustNo", tLoanBorMain.getCustNo());

			tCustMain = custMainService.custNoFirst(tLoanBorMain.getCustNo(), tLoanBorMain.getCustNo(), titaVo);
			if (tCustMain == null) {
				occursList.putParam("OOCustName", "");
			} else {
				occursList.putParam("OOCustName", tCustMain.getCustName());
			}

			if (iCustDataCtrl == 1) {
				occursList.putParam("OOCustNo", "");
				occursList.putParam("OOCustName", "");
			}
			occursList.putParam("OOFacmNo", tLoanBorMain.getFacmNo());
			occursList.putParam("OOBormNo", tLoanBorMain.getBormNo());
			occursList.putParam("OODrawdownDate", tLoanBorMain.getDrawdownDate());
			occursList.putParam("OOMaturityDate", tLoanBorMain.getMaturityDate());
			occursList.putParam("OOStatus", tLoanBorMain.getStatus());
			// 未繳過期款 日期 顯示撥款日
			if (tLoanBorMain.getPrevPayIntDate() == 0) {
				occursList.putParam("OOPrevIntDate", tLoanBorMain.getDrawdownDate());
			} else {
				occursList.putParam("OOPrevIntDate", tLoanBorMain.getPrevPayIntDate());
			}
			occursList.putParam("OOStoreRate", tLoanBorMain.getStoreRate());
			occursList.putParam("OOCurrencyCode", tLoanBorMain.getCurrencyCode());
			occursList.putParam("OOLoanBal", tLoanBorMain.getLoanBal());
			// 查詢放款約定還本檔
			tLoanBook = loanBookService.bookBormNoFirst(tLoanBorMain.getCustNo(), tLoanBorMain.getFacmNo(), tLoanBorMain.getBormNo(), titaVo);
			if (tLoanBook == null) {
				occursList.putParam("OOBookFlag", 0);
			} else {
				occursList.putParam("OOBookFlag", 1);
			}

			// 將每筆資料放入Tota的OcList
			this.totaVo.addOccursList(occursList);
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