package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.InnReCheck;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.InnReCheckService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita InqFg=9,1 YearMonth=9,5 ConditionCode=9,2 YearMonthS=9,5 YearMonthE=9,5
 * ReChkMonth=9,2 CustNo=9,7 END=X,1
 */

@Service("L5905") // 覆審案件明細檔查詢
@Scope("prototype")
/**
 *
 *
 * @author Fegie
 * @version 1.0.0
 */

public class L5905 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public InnReCheckService sInnReCheckService;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5905 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iInqFg = this.parse.stringToInteger(titaVo.getParam("InqFg"));
		int iConditionCode = this.parse.stringToInteger(titaVo.getParam("ConditionCode"));
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo"));
		int iYearMonth = this.parse.stringToInteger(titaVo.getParam("YearMonth"));
		int iFYearMonth = iYearMonth + 191100;
		int iYearMonthS = this.parse.stringToInteger(titaVo.getParam("YearMonthS"));
		int iFYearMonthS = iYearMonthS + 191100;
		int iYearMonthE = this.parse.stringToInteger(titaVo.getParam("YearMonthE"));
		int iFYearMonthE = iYearMonthE + 191100;
		int iTraceYearMonthS = Integer.valueOf(titaVo.getParam("TraceYearMonthS"))+191100;
		int iTraceYearMonthE = Integer.valueOf(titaVo.getParam("TraceYearMonthE"))+191100;
		this.info("L5905 iFYearMonth : " + iFYearMonth + "-" + iFYearMonthS + "-" + iFYearMonthE);
		String iReChkMonth = titaVo.getParam("ReChkMonth");
		int wkFYearMonth = 0;
		int wkFReChkYearMonth = 0;
		String wkYearMonth;
		String wkReChkMonth;
		if (iInqFg == 4) {
			iTraceYearMonthE = Integer.valueOf(titaVo.getCalDy().substring(0,5))+191100;
		}
		this.info("迄日==="+iTraceYearMonthE);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 361 * 100 = 34,900

		// 查詢覆審案件明細檔
		Slice<InnReCheck> slInnReCheck = null;
		switch(iInqFg) {
		case 1:
			if (iCustNo == 0) {
				slInnReCheck = sInnReCheckService.findCustNo(iFYearMonth, iConditionCode, 0000000, 9999999, this.index, this.limit, titaVo);
			} else {
				slInnReCheck = sInnReCheckService.findCustNo(iFYearMonth, iConditionCode, iCustNo, iCustNo, this.index, this.limit, titaVo);
			}
			break;
		case 2:
			if (iCustNo == 0) {
				slInnReCheck = sInnReCheckService.findYearMonth(iFYearMonthS, iFYearMonthE, 0000000, 9999999, this.index, this.limit, titaVo);
			} else {
				slInnReCheck = sInnReCheckService.findYearMonth(iFYearMonthS, iFYearMonthE, iCustNo, iCustNo, this.index, this.limit, titaVo);
			}
			break;
		case 3:
			slInnReCheck = sInnReCheckService.findTraceMonth(iTraceYearMonthS, iTraceYearMonthE, this.index, this.limit, titaVo);
			break;
		case 4:
			slInnReCheck = sInnReCheckService.findTraceMonth(00101, iTraceYearMonthE, this.index, this.limit, titaVo);
			break;
		}

		if (slInnReCheck == null) {
			throw new LogicException(titaVo, "E0001", "覆審案件明細檔"); // 查無資料
		}
		// 如有找到資料
		for (InnReCheck tInnReCheck : slInnReCheck) {

			wkYearMonth = this.parse.IntegerToString(tInnReCheck.getReChkYearMonth(), 6);
			wkReChkMonth = FormatUtil.right(wkYearMonth, 2);
			this.info("L5905 wkReChkMonth : " + wkReChkMonth);

			if (iInqFg == 2 && !(iReChkMonth.equals(wkReChkMonth))) {
//			if (iInqFg == 2) {
				continue;
			}
			if (iInqFg == 4 && !tInnReCheck.getFollowMark().equals("2")) {
				continue;
			}

			OccursList occursList = new OccursList();

			// 查詢客戶資料主檔
			CustMain tCustMain = new CustMain();
			tCustMain = sCustMainService.custNoFirst(tInnReCheck.getCustNo(), tInnReCheck.getCustNo(), titaVo);
			if (tCustMain == null) {
				throw new LogicException(titaVo, "E0001", "客戶資料主檔"); // 查無資料
			}
			occursList.putParam("OOCustName", tCustMain.getCustName());

			wkFYearMonth = tInnReCheck.getYearMonth();
			wkFYearMonth = wkFYearMonth - 191100;
			occursList.putParam("OOYearMonth", wkFYearMonth);

			wkFReChkYearMonth = tInnReCheck.getReChkYearMonth();
			if (!(wkFReChkYearMonth == 0)) {
				wkFReChkYearMonth = wkFReChkYearMonth - 191100;
			}
			occursList.putParam("OOReChkYearMonth", wkFReChkYearMonth);

			occursList.putParam("OOConditionCode", tInnReCheck.getConditionCode());
			occursList.putParam("OOCustNo", tInnReCheck.getCustNo());
			occursList.putParam("OOFacmNo", tInnReCheck.getFacmNo());
			occursList.putParam("OOReCheckCode", tInnReCheck.getReCheckCode());
			occursList.putParam("OOFollowMark", tInnReCheck.getFollowMark());
			occursList.putParam("OODrawdownDate", tInnReCheck.getDrawdownDate());
			occursList.putParam("OOLoanBal", tInnReCheck.getLoanBal());
			occursList.putParam("OOEvaluation", tInnReCheck.getEvaluation());
			occursList.putParam("OOCustTypeItem", tInnReCheck.getCustTypeItem());
			occursList.putParam("OOUsageItem", tInnReCheck.getUsageItem());
			occursList.putParam("OOCityItem", tInnReCheck.getCityItem());
			occursList.putParam("OOReChkUnit", tInnReCheck.getReChkUnit());
			occursList.putParam("OORemark", tInnReCheck.getRemark());
			if (tInnReCheck.getTraceMonth()==0) {
				occursList.putParam("OOTraceYearMonth", 0);
			}else {
				occursList.putParam("OOTraceYearMonth", tInnReCheck.getTraceMonth()-191100);
			}
			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		if (this.totaVo.getOccursList().size() == 0) {
			throw new LogicException(titaVo, "E0001", "覆審案件明細"); // 查無資料
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slInnReCheck != null && slInnReCheck.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}