package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanRateChange;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanRateChangeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/*
 * L3911 繳息情形查詢
 * a.此功能供查詢某一戶號,其某一額度繳交期款之過程(不含部分償還本金)
 * b.該額度利率如有變動時,最多僅顯示三組利率.
 */
/*
 * Tita
 * TimCustNo=9,7
 * FacmNo=9,3
 * BormNo=9,3
 * EntryStartDate=9,7
 * EntryEndDate=9,7
 */
/**
 * L3911 繳息情形查詢 (不含部分償還本金)
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3911")
@Scope("prototype")
public class L3911 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public LoanBorTxService loanBorTxService;
	@Autowired
	public LoanRateChangeService loanRateChangeService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3911 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		int iBormNo = this.parse.stringToInteger(titaVo.getParam("BormNo"));
		int iEntryStartDate = this.parse.stringToInteger(titaVo.getParam("EntryStartDate"));
		int iEntryEndDate = this.parse.stringToInteger(titaVo.getParam("EntryEndDate"));

		boolean firstFg = true;

		// work area
		int wkFacmNoStart = 1;
		int wkFacmNoEnd = 999;
		int wkBormNoStart = 1;
		int wkBormNoEnd = 900;
		int wkSpecificDd = 0;
		int wkIdx = 0;
		String wkCurrencyCode = " ";
		BigDecimal wkLoanBal = BigDecimal.ZERO;
		BigDecimal wkStoreRate1 = BigDecimal.ZERO;
		BigDecimal wkStoreRate2 = BigDecimal.ZERO;
		BigDecimal wkStoreRate3 = BigDecimal.ZERO;
		int wkEffectDate1 = 0;
		int wkEffectDate2 = 0;
		int wkEffectDate3 = 0;
		List<LoanBorMain> lLoanBorMain;
		List<LoanBorTx> lLoanBorTx;
		List<LoanRateChange> lLoanRateChange;

		if (iFacmNo > 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
		}

		if (iBormNo > 0) {
			wkBormNoStart = iBormNo;
			wkBormNoEnd = iBormNo;
		}
		// 查詢放款主檔

		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, wkFacmNoStart, wkFacmNoEnd,
				wkBormNoStart, wkBormNoEnd, 0, Integer.MAX_VALUE, titaVo);
		lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();
		if (lLoanBorMain == null || lLoanBorMain.size() == 0) {
			throw new LogicException(titaVo, "E0001", "放款主檔"); // 查詢資料不存在
		}
		// 如有有找到資料
		for (LoanBorMain tLoanBorMain : lLoanBorMain) {
			wkLoanBal = wkLoanBal.add(tLoanBorMain.getLoanBal());
			wkCurrencyCode = tLoanBorMain.getCurrencyCode();
			wkSpecificDd = tLoanBorMain.getSpecificDd();
		}
		// 查詢放款利率變動檔
		Slice<LoanRateChange> slLoanRateChange = loanRateChangeService.rateChangeEffectDateRange(iCustNo, wkFacmNoStart,
				wkFacmNoEnd, wkBormNoStart, wkBormNoEnd, 0, iEntryEndDate + 19110000, 0, Integer.MAX_VALUE, titaVo);
		lLoanRateChange = slLoanRateChange == null ? null : slLoanRateChange.getContent();
		if (lLoanRateChange != null && lLoanBorMain.size() != 0) {
			// 如有有找到資料
			for (LoanRateChange ln : lLoanRateChange) {
				wkIdx++;
				switch (wkIdx) {
				case 1:
					wkStoreRate1 = ln.getFitRate();
					wkEffectDate1 = ln.getEffectDate();
					break;
				case 2:
					wkStoreRate2 = ln.getFitRate();
					wkEffectDate2 = ln.getEffectDate();
					break;
				case 3:
					wkStoreRate3 = ln.getFitRate();
					wkEffectDate3 = ln.getEffectDate();
					break;
				}
			}
		}

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 39 + 99 * 500 = 49539
		// 查詢放款交易內容檔
		List<String> lDisplayFlag = new ArrayList<String>();
		lDisplayFlag.add("I"); // 繳息次筆
		lDisplayFlag.add("F"); // 繳息首筆
		lDisplayFlag.add("Y"); // for轉換
		Slice<LoanBorTx> slLoanBorTx = loanBorTxService.borxIntEndDateDescRange(iCustNo, wkFacmNoStart, wkFacmNoEnd,
				wkBormNoStart, wkBormNoEnd, iEntryStartDate + 19110000, iEntryEndDate + 19110000, lDisplayFlag,
				this.index, this.limit, titaVo);
		lLoanBorTx = slLoanBorTx == null ? null : slLoanBorTx.getContent();
		if (lLoanBorTx == null || lLoanBorTx.size() == 0) {
			throw new LogicException(titaVo, "E0001", "放款交易內容檔"); // 查詢資料不存在
		}
		// 如有有找到資料
		this.totaVo.putParam("OCurrencyCode", wkCurrencyCode);
		this.totaVo.putParam("OLoanBal", wkLoanBal);
		this.totaVo.putParam("OSpecificDd", wkSpecificDd);
		this.totaVo.putParam("OStoreRate1", wkStoreRate1);
		this.totaVo.putParam("OStoreRate2", wkStoreRate2);
		this.totaVo.putParam("OStoreRate3", wkStoreRate3);
		this.totaVo.putParam("OEffectDate1", wkEffectDate1);
		this.totaVo.putParam("OEffectDate2", wkEffectDate2);
		this.totaVo.putParam("OEffectDate3", wkEffectDate3);

		int TempFacmNo = 0;
		int TempBormNo = 0;
		int TempEntryDate = 0;
		int TempIntStartDate = 0;
		int TempIntEndDate = 0;
		int TempAcDate = 0;
		String TempCurrencyCode = "";
		String TempTitaTlrNo = "";
		String TempTitaTxtNo = "";
		Object repayCode = "";

		BigDecimal TempShortFall = new BigDecimal("0");
		BigDecimal TempPrincipal = new BigDecimal("0"); // 本金
		BigDecimal TempInterest = new BigDecimal("0"); // 利息
		BigDecimal TempBreachAmt = new BigDecimal("0"); // 違約金
		BigDecimal wkTempAmt = new BigDecimal("0");// 暫收抵繳
		BigDecimal wkRepayAmt = new BigDecimal("0");// 回收金額
		BigDecimal acctFee = new BigDecimal("0");
		BigDecimal modifyFee = new BigDecimal("0");
		BigDecimal fireFee = new BigDecimal("0");
		BigDecimal lawFee = new BigDecimal("0");
		BigDecimal repayAmt = new BigDecimal("0");
		BigDecimal wkTempRepay = new BigDecimal("0");// 暫收金額

		OccursList occursList = new OccursList();

		Boolean sumfg = false;
		int count = 0;
		for (LoanBorTx ln : lLoanBorTx) {
			if (!ln.getTitaHCode().equals("0")) {
				continue;
			}
			if (ln.getPrincipal().compareTo(BigDecimal.ZERO) == 0 && ln.getInterest().compareTo(BigDecimal.ZERO) == 0) {
				continue;
			}

			/* 暫存的額度編號 撥款序號 入帳日 計息起日 計息迄日 會計日期 交易經辦 交易序號 */
			this.info("LoanBorTx = " + ln);
			if (firstFg) {// 先存第一筆資料

				TempFacmNo = ln.getFacmNo();
				TempBormNo = ln.getBormNo();
				TempEntryDate = ln.getEntryDate();
				TempIntStartDate = ln.getIntStartDate();
				TempIntEndDate = ln.getIntEndDate();
				TempCurrencyCode = ln.getTitaCurCd();
				TempAcDate = ln.getAcDate();
				TempTitaTxtNo = ln.getTitaTxtNo();
				TempTitaTlrNo = ln.getTitaTlrNo();
				firstFg = false;
				count = 1;

			}
			// 資料都相同，資金加總
			if (TempFacmNo == ln.getFacmNo() && TempEntryDate == ln.getEntryDate()
					&& TempIntStartDate == ln.getIntStartDate() && TempIntEndDate == ln.getIntEndDate()
					&& TempAcDate == ln.getAcDate() && TempTitaTxtNo.equals(ln.getTitaTxtNo())
					&& TempTitaTlrNo.equals(ln.getTitaTlrNo())) {

				TempPrincipal = TempPrincipal.add(ln.getPrincipal());
				TempInterest = TempInterest.add(ln.getInterest());
				TempShortFall = TempShortFall.add(ln.getOverflow().subtract(ln.getShortfall()));
				TempBreachAmt = TempBreachAmt.add(ln.getBreachAmt()).add(ln.getCloseBreachAmt()).add(ln.getDelayInt()); // 違約金+延遲息+清償違約金

				TempVo tTempVo = new TempVo();

				tTempVo = tTempVo.getVo(ln.getOtherFields());

				acctFee = parse.stringToBigDecimal(tTempVo.getParam("AcctFee"));
				modifyFee = parse.stringToBigDecimal(tTempVo.getParam("ModifyFee"));
				fireFee = parse.stringToBigDecimal(tTempVo.getParam("FireFee"));
				lawFee = parse.stringToBigDecimal(tTempVo.getParam("LawFee"));

				repayAmt = ln.getPrincipal().add(ln.getInterest()).add(ln.getDelayInt()).add(ln.getBreachAmt())
						.add(ln.getCloseBreachAmt()).add(acctFee).add(modifyFee).add(fireFee).add(lawFee);

				wkRepayAmt = wkRepayAmt.add(repayAmt); // 回收金額
				wkTempAmt = wkTempAmt.add(ln.getTempAmt());

				if (ln.getTempAmt().compareTo(BigDecimal.ZERO) < 0) {
					wkTempRepay = wkTempRepay.subtract(ln.getTempAmt());
				}
				// 資料相同合併撥款 顯示0
				sumfg = true;
			} else { // 資料不同 先拿temp資料塞occurs再更新temp資料

				/* 資料不同 先拿temp資料塞occurs */
				occursList.putParam("OOFacmNo", TempFacmNo);

				if (sumfg && count != 1) { // 不為第一筆
					occursList.putParam("OOBormNo", 0);
				} else {
					occursList.putParam("OOBormNo", TempBormNo);
				}
				occursList.putParam("OOEntryDate", TempEntryDate);
				occursList.putParam("OOIntStartDate", TempIntStartDate);
				occursList.putParam("OOIntEndDate", TempIntEndDate);
				occursList.putParam("OOCurrencyCode", TempCurrencyCode);

				occursList.putParam("OOTxAmt", wkRepayAmt.add(wkTempAmt)); // 交易金額
				occursList.putParam("OOTempRepay", wkTempRepay);
				occursList.putParam("OOShortFall", TempShortFall);
				occursList.putParam("OOPrincipal", TempPrincipal);
				occursList.putParam("OOInterest", TempInterest);
				occursList.putParam("OOBreachAmt", TempBreachAmt);

				if (repayCode.toString().length() == 2) {
					occursList.putParam("OORepayCode", repayCode);
				} else {
					occursList.putParam("OORepayCode", "");
				}
				occursList.putParam("OOAcDate", TempAcDate);
				occursList.putParam("OOTellerNo", TempTitaTlrNo);
				occursList.putParam("OOTxtNo", TempTitaTxtNo);

				sumfg = false;

				/* 更新temp資料 */
				TempFacmNo = ln.getFacmNo();
				TempBormNo = ln.getBormNo();
				TempEntryDate = ln.getEntryDate();
				TempIntStartDate = ln.getIntStartDate();
				TempIntEndDate = ln.getIntEndDate();
				TempCurrencyCode = ln.getTitaCurCd();

				TempShortFall = new BigDecimal("0");
				TempPrincipal = new BigDecimal("0");
				TempInterest = new BigDecimal("0");
				TempBreachAmt = new BigDecimal("0");
				wkTempAmt = new BigDecimal("0");
				wkRepayAmt = new BigDecimal("0");

				TempPrincipal = TempPrincipal.add(ln.getPrincipal());
				TempInterest = TempInterest.add(ln.getInterest());
				TempShortFall = TempShortFall.add(ln.getOverflow().subtract(ln.getShortfall()));
				TempBreachAmt = TempBreachAmt.add(ln.getBreachAmt()).add(ln.getCloseBreachAmt()).add(ln.getDelayInt()); // 違約金+延遲息+清償違約金

				TempVo tTempVo = new TempVo();
				repayCode = ln.getRepayCode();

				tTempVo = tTempVo.getVo(ln.getOtherFields());

				acctFee = parse.stringToBigDecimal(tTempVo.getParam("AcctFee"));
				modifyFee = parse.stringToBigDecimal(tTempVo.getParam("ModifyFee"));
				fireFee = parse.stringToBigDecimal(tTempVo.getParam("FireFee"));
				lawFee = parse.stringToBigDecimal(tTempVo.getParam("LawFee"));

				repayAmt = ln.getPrincipal().add(ln.getInterest()).add(ln.getDelayInt()).add(ln.getBreachAmt())
						.add(ln.getCloseBreachAmt()).add(acctFee).add(modifyFee).add(fireFee).add(lawFee);

				wkRepayAmt = wkRepayAmt.add(repayAmt); // 回收金額
				wkTempAmt = wkTempAmt.add(ln.getTempAmt()); // 暫收金額
				TempAcDate = ln.getAcDate();
				TempTitaTlrNo = ln.getTitaTlrNo();
				TempTitaTxtNo = ln.getTitaTxtNo();

				if (ln.getTempAmt().compareTo(BigDecimal.ZERO) < 0) {
					wkTempRepay = wkTempRepay.subtract(ln.getTempAmt()); // 暫收抵繳
				}
				this.totaVo.addOccursList(occursList);
				
				count++;
				
				occursList = new OccursList();
			}

		}

		if (!firstFg) { // 只有一筆資料 或是 最後一筆
			occursList.putParam("OOFacmNo", TempFacmNo);
			occursList.putParam("OOBormNo", TempBormNo);
			occursList.putParam("OOEntryDate", TempEntryDate);
			occursList.putParam("OOIntStartDate", TempIntStartDate);
			occursList.putParam("OOIntEndDate", TempIntEndDate);
			occursList.putParam("OOCurrencyCode", TempCurrencyCode);

			occursList.putParam("OOTxAmt", wkRepayAmt.add(wkTempAmt));
			occursList.putParam("OOTempRepay", wkTempRepay);
			occursList.putParam("OOShortFall", TempShortFall);
			occursList.putParam("OOPrincipal", TempPrincipal);
			occursList.putParam("OOInterest", TempInterest);
			occursList.putParam("OOBreachAmt", TempBreachAmt);

			if (repayCode.toString().length() == 2) {
				occursList.putParam("OORepayCode", repayCode);
			} else {
				occursList.putParam("OORepayCode", "");
			}
			occursList.putParam("OOAcDate", TempAcDate);
			occursList.putParam("OOTellerNo", TempTitaTlrNo);
			occursList.putParam("OOTxtNo", TempTitaTxtNo);
			this.totaVo.addOccursList(occursList);
		}

		// 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可
		if (slLoanBorTx != null && slLoanBorTx.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}