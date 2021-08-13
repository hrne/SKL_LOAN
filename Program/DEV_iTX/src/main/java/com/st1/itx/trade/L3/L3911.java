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
	// private static final Logger logger = LoggerFactory.getLogger(L3911.class);

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
		
		boolean iFacmFg = false;
		boolean loopFg = true;
		if(iBormNo == 0 ) {   // 撥款序號不打 顯示額度加總
			iFacmFg = true;
		}
		
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
					break;
				case 2:
					wkStoreRate2 = ln.getFitRate();
					break;
				case 3:
					wkStoreRate3 = ln.getFitRate();
					break;
				}
			}
		}

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500; // 39 + 99 * 500 = 49539
		// 查詢放款交易內容檔
		List<String> lDisplayFlag = new ArrayList<String>();
		lDisplayFlag.add("I"); // 繳息次筆
		lDisplayFlag.add("F"); // 繳息首筆
		lDisplayFlag.add("Y"); // for轉換
		Slice<LoanBorTx> slLoanBorTx = loanBorTxService.borxEntryDateRange(iCustNo, wkFacmNoStart, wkFacmNoEnd,
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
		
		int TempFacmNo = 0;
		int TempBormNo = 0;
		int TempEntryDate = 0;
		int TempIntStartDate = 0;
		int TempIntEndDate = 0;
		int TempAcDate  = 0;
		String TempCurrencyCode = "";
		String TempTitaTlrNo = "";
		String TempTitaTxtNo = "";
		Object TempRepayCode = "";
		
		
		BigDecimal TempTxAmt = new BigDecimal("0");  
		BigDecimal TempShortFall = new BigDecimal("0");  
		BigDecimal TempBreachAmt = new BigDecimal("0");  
		
		OccursList occursList = new OccursList();
		
		for (LoanBorTx ln : lLoanBorTx) {
			if (!ln.getTitaHCode().equals("0")) {
				continue;
			}
			if (ln.getPrincipal().compareTo(BigDecimal.ZERO) == 0 && ln.getInterest().compareTo(BigDecimal.ZERO) == 0) {
				continue;
			}
			

			/*暫存的額度編號 撥款序號 入帳日 計息起日 計息迄日*/
			this.info("LoanBorTx" + ln);
			if(iFacmFg) { // 撥款序號不打 顯示額度加總
				
				if(loopFg) {// 先存第一筆資料
				
					TempFacmNo = ln.getFacmNo();
					TempBormNo = ln.getBormNo();
					TempEntryDate = ln.getEntryDate();
					TempIntStartDate = ln.getIntStartDate();
					TempIntEndDate = ln.getIntEndDate();
					TempCurrencyCode = ln.getTitaCurCd();
					TempTxAmt = TempTxAmt.add(ln.getTxAmt());
					TempShortFall = TempShortFall.add(ln.getOverflow().subtract(ln.getShortfall()));
					TempBreachAmt = TempBreachAmt.add(ln.getBreachAmt());
					
						
					TempVo tTempVo = new TempVo();
					TempRepayCode = tTempVo.getVo(ln.getOtherFields()).getParam("RepayCode");
					
//					/*抓OtherFields中的JSON資料*/
//					JSONObject jsonObject = null;
//					try {
//						jsonObject = new JSONObject(ln.getOtherFields());
//						for(int i = 0 ; i <jsonObject.length();i++) {
//							if(jsonObject.has("RepayCode")) {
//								TempRepayCode = jsonObject.get("RepayCode");
//							}
//						}
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					
					TempAcDate = ln.getAcDate();
					TempTitaTlrNo = ln.getTitaTlrNo();
					TempTitaTxtNo = ln.getTitaTxtNo();
					
					
					loopFg = false;
					
				} else {
					
					if( TempFacmNo == ln.getFacmNo() && 
						TempBormNo == ln.getBormNo()&&
						TempEntryDate == ln.getEntryDate()&&
						TempIntStartDate == ln.getIntStartDate()&&
						TempIntEndDate == ln.getIntEndDate()) {  // 資料都相同 資金加總
						
						TempTxAmt = TempTxAmt.add(ln.getTxAmt());
						TempShortFall = TempShortFall.add(ln.getOverflow().subtract(ln.getShortfall()));
						TempBreachAmt = TempBreachAmt.add(ln.getBreachAmt());
						
					} else {  // 資料不同 先拿temp資料塞occurs再更新temp資料
						
						/*資料不同 先拿temp資料塞occurs*/
						occursList.putParam("OOFacmNo", TempFacmNo);
						occursList.putParam("OOBormNo", TempBormNo);
						occursList.putParam("OOEntryDate", TempEntryDate);
						occursList.putParam("OOIntStartDate", TempIntStartDate);
						occursList.putParam("OOIntEndDate", TempIntEndDate);
						occursList.putParam("OOCurrencyCode", TempCurrencyCode);

						occursList.putParam("OOTxAmt", TempTxAmt);
						occursList.putParam("OOShortFall", TempShortFall);
						occursList.putParam("OOBreachAmt", TempBreachAmt);
						
						
						if(TempRepayCode.toString().length() == 2) {
							occursList.putParam("OORepayCode",TempRepayCode);
						} else {
							occursList.putParam("OORepayCode","");
						}
						occursList.putParam("OOAcDate", TempAcDate);
						occursList.putParam("OOTellerNo", TempTitaTlrNo);
						occursList.putParam("OOTxtNo", TempTitaTxtNo);
						
						
						/*更新temp資料*/
						TempFacmNo = ln.getFacmNo();
						TempBormNo = ln.getBormNo();
						TempEntryDate = ln.getEntryDate();
						TempIntStartDate = ln.getIntStartDate();
						TempIntEndDate = ln.getIntEndDate();
						TempCurrencyCode = ln.getTitaCurCd();
						
						TempTxAmt = new BigDecimal("0");  
						TempShortFall = new BigDecimal("0");  
						TempBreachAmt = new BigDecimal("0");  
						
						TempTxAmt = TempTxAmt.add(ln.getTxAmt());
						TempShortFall = TempShortFall.add(ln.getOverflow().subtract(ln.getShortfall()));
						TempBreachAmt = TempBreachAmt.add(ln.getBreachAmt());
						
						TempVo tTempVo = new TempVo();
						TempRepayCode = tTempVo.getVo(ln.getOtherFields()).getParam("RepayCode");
						
//						/*抓OtherFields中的JSON資料*/
//						JSONObject jsonObject = null;
//						try {
//							jsonObject = new JSONObject(ln.getOtherFields());
//							for(int i = 0 ; i <jsonObject.length();i++) {
//								if(jsonObject.has("RepayCode")) {
//									TempRepayCode = jsonObject.get("RepayCode");
//								}
//							}
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
						
						TempAcDate = ln.getAcDate();
						TempTitaTlrNo = ln.getTitaTlrNo();
						TempTitaTxtNo = ln.getTitaTxtNo();
						
						
						this.totaVo.addOccursList(occursList);
						occursList = new OccursList();
					} // else 
					
					
					
				} // else 
		
				
			} else { // 有打撥款序號的情形
				
				occursList.putParam("OOFacmNo", ln.getFacmNo());
				occursList.putParam("OOBormNo", ln.getBormNo());
				occursList.putParam("OOEntryDate", ln.getEntryDate());
				occursList.putParam("OOIntStartDate", ln.getIntStartDate());
				occursList.putParam("OOIntEndDate", ln.getIntEndDate());
				occursList.putParam("OOCurrencyCode", ln.getTitaCurCd());

				occursList.putParam("OOTxAmt", ln.getTxAmt());
				occursList.putParam("OOShortFall", ln.getOverflow().subtract(ln.getShortfall()));
				occursList.putParam("OOBreachAmt", ln.getBreachAmt());
				
				
				TempVo tTempVo = new TempVo();
				TempRepayCode = tTempVo.getVo(ln.getOtherFields()).getParam("RepayCode");
				
//				/*抓OtherFields中的JSON資料*/
//				JSONObject jsonObject = null;
//				Object RepayCode = "";
//				try {
//					jsonObject = new JSONObject(ln.getOtherFields());
//					for(int i = 0 ; i <jsonObject.length();i++) {
//						if(jsonObject.has("RepayCode")) {
//							RepayCode = jsonObject.get("RepayCode");
//						}
//					}
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
				if(TempRepayCode.toString().length() == 2) {
					occursList.putParam("OORepayCode",TempRepayCode);
				} else {
					occursList.putParam("OORepayCode","");
				}
				occursList.putParam("OOAcDate", ln.getAcDate());
				occursList.putParam("OOTellerNo", ln.getTitaTlrNo());
				occursList.putParam("OOTxtNo", ln.getTitaTxtNo());
				
				// 將每筆資料放入Tota的OcList
				this.totaVo.addOccursList(occursList);
				occursList = new OccursList();
			}

			

		} // for

		if(!loopFg) { // 只有一筆資料 或是 最後一筆 
			occursList.putParam("OOFacmNo", TempFacmNo);
			occursList.putParam("OOBormNo", TempBormNo);
			occursList.putParam("OOEntryDate", TempEntryDate);
			occursList.putParam("OOIntStartDate", TempIntStartDate);
			occursList.putParam("OOIntEndDate", TempIntEndDate);
			occursList.putParam("OOCurrencyCode", TempCurrencyCode);

			occursList.putParam("OOTxAmt", TempTxAmt);
			occursList.putParam("OOShortFall", TempShortFall);
			occursList.putParam("OOBreachAmt", TempBreachAmt);
			
			
			if(TempRepayCode.toString().length() == 2) {
				occursList.putParam("OORepayCode",TempRepayCode);
			} else {
				occursList.putParam("OORepayCode","");
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