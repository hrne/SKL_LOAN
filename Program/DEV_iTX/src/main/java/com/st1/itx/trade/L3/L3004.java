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
import com.st1.itx.db.domain.LoanBook;
import com.st1.itx.db.service.LoanBookService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/*
 * L3004 約定部分償還明細資料查詢
 * a.本交易查詢借戶約定部分償還日期及金額。
 */
/*
 * Tita
 * TimCustNo=9,7
 * ApplNo=9,7
 * FacmNo=9,3
 * BormNo=9,3
 */
/**
 * L3004 約定部分償還明細資料查詢
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3004")
@Scope("prototype")
public class L3004 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L3004.class);

	/* DB服務注入 */
	@Autowired
	public LoanBookService loanBookService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3004 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		int iBormNo = this.parse.stringToInteger(titaVo.getParam("BormNo"));

		// work area
		Slice<LoanBook> slLoanBook;
		List<LoanBook> lLoanBook = new ArrayList<LoanBook>();
		int wkCustNoStart = 0;
		int wkCustNoEnd = 9999999;
		int wkFacmNoStart = 0;
		int wkFacmNoEnd = 999;
		int wkBormNoStart = 0;
		int wkBormNoEnd = 999;

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 56 * 1000 = 56000

		// 查詢放款主檔
		if (iCustNo > 0) {
			wkCustNoStart = iCustNo;
			wkCustNoEnd = iCustNo;
		}
		if (iFacmNo > 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
		}
		if (iBormNo > 0) {
			wkBormNoStart = iBormNo;
			wkBormNoEnd = iBormNo;
		}

		slLoanBook = loanBookService.bookCustNoRange(wkCustNoStart, wkCustNoEnd, wkFacmNoStart, wkFacmNoEnd,
				wkBormNoStart, wkBormNoEnd, this.index, this.limit, titaVo);
		lLoanBook = slLoanBook == null ? null : slLoanBook.getContent();
		if (lLoanBook == null || lLoanBook.size() == 0) {
			throw new LogicException(titaVo, "E0001", "放款約定還本檔"); // 查詢資料不存在
		}
		// 如有有找到資料
		for (LoanBook tLoanBook : lLoanBook) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOCustNo", tLoanBook.getCustNo());
			occursList.putParam("OOFacmNo", tLoanBook.getFacmNo());
			occursList.putParam("OOBormNo", tLoanBook.getBormNo());
			occursList.putParam("OOBookDate", tLoanBook.getBookDate());
			occursList.putParam("OOCurrencyCode", tLoanBook.getCurrencyCode());
			occursList.putParam("OOBookAmt", tLoanBook.getBookAmt());
			occursList.putParam("OORepayAmt", tLoanBook.getRepayAmt());
			occursList.putParam("OOBookStatus", tLoanBook.getStatus());

			// 將每筆資料放入Tota的OcList
			this.totaVo.addOccursList(occursList);
		}

		// 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可
		if (slLoanBook != null && slLoanBook.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToAuto(); // 自動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}