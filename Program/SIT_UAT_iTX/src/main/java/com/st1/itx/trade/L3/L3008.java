package com.st1.itx.trade.L3;

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
import com.st1.itx.db.domain.LoanCheque;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.LoanChequeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L3008 支票明細資料查詢-依客戶
 * a.提供查詢顧客所有支票明細資料.
 */
/*
 * Tita
 * CaseNo=9,7
 * TimCustNo=9,7
 * CustId=X,10
 * ApplNo=9,7
 * FacmNo=9,3
 */
/**
 * L3008 支票明細查詢-依客戶
 * 
 * @author iza
 * @version 1.0.0
 */

@Service("L3008")
@Scope("prototype")
public class L3008 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L3008.class);

	/* DB服務注入 */
	@Autowired
	public LoanChequeService loanChequeService;
	@Autowired
	public AcReceivableService acReceivableService;

	@Autowired
	Parse parse;

	private int wkTotalCount = 0;
	private OccursList occursList;
	private Slice<LoanCheque> slLoanCheque;
	private List<LoanCheque> lLoanCheque;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3008 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		String iStatusCode = titaVo.getParam("StatusCode");
		// work area
		String wkRvNo;

		List<String> lStatusCode = new ArrayList<String>();
		if (iStatusCode.isEmpty()) {
			this.info("未輸入支票狀況");
			lStatusCode.add("0"); // 0: 未處理
			lStatusCode.add("1"); // 1: 兌現入帳
			lStatusCode.add("2"); // 2: 退票
			lStatusCode.add("3"); // 3: 抽票
			lStatusCode.add("4"); // 4: 兌現未入帳
			lStatusCode.add("5"); // 5: 即期票
		} else {
			this.info("有輸入支票狀況");
			lStatusCode.add(iStatusCode);
		}

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 96 * 500 = 48000

		// 查詢放款主檔
		slLoanCheque = loanChequeService.chequeCustNoEq(iCustNo, lStatusCode, 0, 99991231, this.index, this.limit, titaVo);
		lLoanCheque = slLoanCheque == null ? null : slLoanCheque.getContent();
		if (lLoanCheque == null || lLoanCheque.size() == 0) {
			throw new LogicException(titaVo, "E0001", "支票檔"); // 查詢資料不存在
		}
		for (LoanCheque tLoanCheque : lLoanCheque) {
			// 查詢會計銷帳檔
			wkRvNo = FormatUtil.pad9(String.valueOf(tLoanCheque.getChequeAcct()), 9) + " " + FormatUtil.pad9(String.valueOf(tLoanCheque.getChequeNo()), 7);
			Slice<AcReceivable> slAcReceivable = acReceivableService.acrvRvNoEq("TCK", iCustNo, wkRvNo, 0, Integer.MAX_VALUE, titaVo);
			List<AcReceivable> lAcReceivable = slAcReceivable == null ? null : slAcReceivable.getContent();
			if (lAcReceivable != null && lAcReceivable.size() > 0) {
				for (AcReceivable tAcReceivable : lAcReceivable) {
					if (iFacmNo == 0 || iFacmNo == tAcReceivable.getFacmNo()) {
						occursList = new OccursList();
						occursList.putParam("OOFacmNo", tAcReceivable.getFacmNo());
						moveOccursList(tLoanCheque);
					}
				}
			} else {
				if (iFacmNo == 0) {
					occursList = new OccursList();
					occursList.putParam("OOFacmNo", 0);
					moveOccursList(tLoanCheque);
				}
			}
		}

		if (wkTotalCount == 0) {
			throw new LogicException(titaVo, "E0001", "支票檔"); // 查詢資料不存在
		}
		// 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可
		if (slLoanCheque != null && slLoanCheque.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveOccursList(LoanCheque mLoanCheque) {
		occursList.putParam("OOChequeDate", mLoanCheque.getChequeDate());
		occursList.putParam("OOChequeAcct", mLoanCheque.getChequeAcct());
		occursList.putParam("OOChequeNo", mLoanCheque.getChequeNo());
		occursList.putParam("OOCurrencyCode", mLoanCheque.getCurrencyCode());
		occursList.putParam("OOChequeAmt", mLoanCheque.getChequeAmt());
		occursList.putParam("OOAcDate", mLoanCheque.getAcDate());
		occursList.putParam("OOKinbr", mLoanCheque.getKinbr());
		occursList.putParam("OOTellerNo", mLoanCheque.getTellerNo());
		occursList.putParam("OOTxtNo", mLoanCheque.getTxtNo());
		occursList.putParam("OOStatusCode", mLoanCheque.getStatusCode());
		occursList.putParam("OOEntryDate", mLoanCheque.getEntryDate()); // TODO: 兌現入帳日

		// 將每筆資料放入Tota的OcList
		this.totaVo.addOccursList(occursList);
		wkTotalCount++;
	}
}