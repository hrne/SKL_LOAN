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
import com.st1.itx.db.domain.CustRmk;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.service.CustRmkService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.parse.Parse;

/*
 * L3005 交易明細資料查詢
 * a.此功能供查詢某一撥款,其所有交易之資料.
 * b.可改變會計日期,以顯示所指定之會計日之後各筆交易情形.
 * c.畫面會計日期之初值為當月1日
 * d.在某筆交易資料前可按[查詢],以查閱該筆交易之明細資料.
 */

/**
 * L3005 交易明細資料查詢
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3005")
@Scope("prototype")
public class L3005 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustRmkService custRmkService;
	@Autowired
	public LoanBorTxService loanBorTxService;
	@Autowired
	public LoanBorMainService loanBorMainService;

	@Autowired
	BaTxCom baTxCom;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3005 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		int iBormNo = this.parse.stringToInteger(titaVo.getParam("BormNo"));
		int iAcDate = this.parse.stringToInteger(titaVo.getParam("AcDate"));
		int iEntryDate = this.parse.stringToInteger(titaVo.getParam("EntryDate"));
		int iTitaHCode = this.parse.stringToInteger(titaVo.getParam("TitaHCode"));

		// work area
		int wkFacmNoStart = 0;
		int wkFacmNoEnd = 999;
		int wkBormNoStart = 0;
		int wkBormNoEnd = 900;
		int wkAcDateStart = iAcDate + 19110000;
		int wkEntryDateStart = iEntryDate + 19110000;
		int wkDateEnd = 99991231;

		String oCustRmkFlag = "N";
		String loanIntDetailFg = "N";

		String AcFg;
		String FeeFg;
		String wkCurrencyCode = "";

		Slice<CustRmk> slCustRmk;
		Slice<LoanBorTx> slLoanBorTx;

		TempVo tTempVo = new TempVo();

		List<CustRmk> lCustRmk;
		List<LoanBorTx> lLoanBorTx;

//		LoanEachFeeVo loanEachFeeVo = new LoanEachFeeVo();

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設50筆 總長不可超過六萬
		this.limit = 100; // 100* 154 = 15400

		if (iFacmNo != 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
		}
		if (iBormNo != 0) {
			wkBormNoStart = iBormNo;
			wkBormNoEnd = iBormNo;
		}

		// 查詢顧客控管警訊檔
		slCustRmk = custRmkService.findCustNo(iCustNo, 0, Integer.MAX_VALUE, titaVo);
		lCustRmk = slCustRmk == null ? null : slCustRmk.getContent();
		if (lCustRmk != null && lCustRmk.size() > 0) {
			oCustRmkFlag = "Y";
		}
		// 查詢各項費用
		baTxCom.settingUnPaid(iEntryDate, iCustNo, iFacmNo, iBormNo, 99, BigDecimal.ZERO, titaVo); // 99-費用全部(含未到期)

		this.totaVo.putParam("OCustNo", iCustNo);
		this.totaVo.putParam("OCustRmkFlag", oCustRmkFlag);
		this.totaVo.putParam("OExcessive", baTxCom.getExcessive());
		this.totaVo.putParam("OShortfall", baTxCom.getShortfall());
		this.totaVo.putParam("OCurrencyCode", wkCurrencyCode);
		// if (iCustDataCtrl == 1) {
		// 	this.totaVo.putParam("OCustNo", "");
		// }
		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 39 + 98 * 500 = 49039

		// 查詢放款交易內容檔
		List<String> lDisplayFlag = new ArrayList<String>();
		lDisplayFlag.add("Y");
		lDisplayFlag.add("I"); // 繳息次筆
		lDisplayFlag.add("A"); // 帳務
		lDisplayFlag.add("F"); // 繳息首筆

		if (iAcDate == 0) {
			slLoanBorTx = loanBorTxService.borxEntryDateRange(iCustNo, wkFacmNoStart, wkFacmNoEnd, wkBormNoStart,
					wkBormNoEnd, wkEntryDateStart, wkDateEnd, lDisplayFlag, this.index, this.limit, titaVo);
		} else {
			slLoanBorTx = loanBorTxService.borxAcDateRange(iCustNo, wkFacmNoStart, wkFacmNoEnd, wkBormNoStart,
					wkBormNoEnd, wkAcDateStart, wkDateEnd, lDisplayFlag, this.index, this.limit, titaVo);
		}

		lLoanBorTx = slLoanBorTx == null ? null : new ArrayList<LoanBorTx>(slLoanBorTx.getContent());
		if (lLoanBorTx == null || lLoanBorTx.size() == 0) {
			if (iAcDate == 0) {
				throw new LogicException(titaVo, "E0001", "入帳日期  " + iEntryDate + " 後無交易資料"); // 查詢資料不存在
			} else {
				throw new LogicException(titaVo, "E0001", "會計日期  " + iAcDate + " 後無交易資料"); // 查詢資料不存在
			}
		}

		String relNo = "";
		String newRelNo = "";
		// 如有有找到資料

		for (LoanBorTx ln : lLoanBorTx) {
			newRelNo = titaVo.getKinbr() + ln.getTitaTlrNo() + ln.getTitaTxtNo();
			OccursList occursList = new OccursList();
			BigDecimal wkTempAmt = BigDecimal.ZERO;
			BigDecimal wkShortfall = BigDecimal.ZERO;

			tTempVo = new TempVo();
			if (iTitaHCode == 0 && !ln.getTitaHCode().equals("0")) {
				continue;
			}

			// 是否顯示分錄清單： Y-有分錄清單 、N-無分錄清單、空白(同交易序號次筆)
			if ((ln.getTitaHCode().equals("0") || ln.getTitaHCode().equals("3") || ln.getTitaHCode().equals("4"))
					&& (ln.getDisplayflag().equals("A") || ln.getDisplayflag().equals("F")))
				AcFg = "Y";
			else if (relNo.equals(newRelNo))
				AcFg = "";
			else
				AcFg = "N";
			// 費用
			tTempVo = tTempVo.getVo(ln.getOtherFields());
			if ((!"".equals(tTempVo.getParam("AcctFee"))
					&& (parse.stringToBigDecimal(tTempVo.getParam("AcctFee")).compareTo(BigDecimal.ZERO) != 0)
					|| (!"".equals(tTempVo.getParam("ModifyFee"))
							&& parse.stringToBigDecimal(tTempVo.getParam("ModifyFee")).compareTo(BigDecimal.ZERO) != 0)
					|| (!"".equals(tTempVo.getParam("FireFee"))
							&& parse.stringToBigDecimal(tTempVo.getParam("FireFee")).compareTo(BigDecimal.ZERO) != 0)
					|| (!"".equals(tTempVo.getParam("LawFee"))
							&& parse.stringToBigDecimal(tTempVo.getParam("LawFee")).compareTo(BigDecimal.ZERO) != 0)))
				FeeFg = "Y";
			else
				FeeFg = "";
			// 是否顯示L3913計息明細按鈕
			if ((ln.getTitaHCode().equals("0") || ln.getTitaHCode().equals("2") || ln.getTitaHCode().equals("4"))
					&& (ln.getDisplayflag().equals("I") || ln.getDisplayflag().equals("F"))) {
				loanIntDetailFg = "Y";
			} else {
				loanIntDetailFg = "N";
			}
			if (!"".equals(ln.getTitaCurCd())) {
				wkCurrencyCode = ln.getTitaCurCd();
			}
			this.totaVo.putParam("OCurrencyCode", wkCurrencyCode);

			wkShortfall = ln.getOverflow().subtract(ln.getShortfall());
			// 暫收款金額為負時，為暫收抵繳
			if (ln.getTempAmt().compareTo(BigDecimal.ZERO) < 0) {
				wkTempAmt = BigDecimal.ZERO.subtract(ln.getTempAmt());
			} else {
				wkTempAmt = BigDecimal.ZERO;
			}
			relNo = titaVo.getKinbr() + ln.getTitaTlrNo() + ln.getTitaTxtNo();
			occursList.putParam("OOEntryDate", ln.getEntryDate());
			occursList.putParam("OOAcDate", ln.getAcDate());
			occursList.putParam("OODesc", ln.getDesc());
			occursList.putParam("OOFacmNo", ln.getFacmNo());
			occursList.putParam("OOBormNo", ln.getBormNo());
			occursList.putParam("OORelNo", relNo); // 登放序號
			occursList.putParam("OOTellerNo", ln.getTitaTlrNo());
			occursList.putParam("OOTxtNo", ln.getTitaTxtNo());
			occursList.putParam("OOCurrencyCode", ln.getTitaCurCd());
			if (ln.getTitaHCode().equals("1") || ln.getTitaHCode().equals("3")) {
				occursList.putParam("OOTxAmt", BigDecimal.ZERO.subtract(ln.getTxAmt()));
				occursList.putParam("OOTempAmt", BigDecimal.ZERO.subtract(wkTempAmt));
				occursList.putParam("OOShortfall", BigDecimal.ZERO.subtract(wkShortfall));

			} else {
				occursList.putParam("OOTxAmt", ln.getTxAmt());
				occursList.putParam("OOTempAmt", wkTempAmt);
				occursList.putParam("OOShortfall", wkShortfall);

			}

			occursList.putParam("OOLoanBal", ln.getLoanBal());
			occursList.putParam("OORate", ln.getRate());
			occursList.putParam("OOTitaHCode", ln.getTitaHCode());
			occursList.putParam("OOAcFg", AcFg); // 分錄清單Fg
			occursList.putParam("OOFeeFg", FeeFg); // 資料
			occursList.putParam("OOLoanIntDetailFg", loanIntDetailFg); // 計息明細Fg
			occursList.putParam("OOTxCd", ln.getTitaTxCd()); // 交易代號

			// 將每筆資料放入Tota的OcList
			this.totaVo.addOccursList(occursList);
		}

		// 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可
		if (slLoanBorTx != null && slLoanBorTx.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter(); // 自動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}