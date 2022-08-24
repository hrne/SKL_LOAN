package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.LoanCustRmk;
import com.st1.itx.db.service.LoanCustRmkService;
import com.st1.itx.db.service.springjpa.cm.L3005ServiceImpl;
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
	L3005ServiceImpl l3005ServiceImpl;
	@Autowired
	LoanCustRmkService loanCustRmkService;

	@Autowired
	BaTxCom baTxCom;

	@Autowired
	Parse parse;

	private DecimalFormat df = new DecimalFormat("##,###,###,###,##0");

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
		String loanCustRmkX = "";
		String oLoanCustRmkFlag = "N";

		String loanIntDetailFg = "N";
		String AcFg;
		String wkCurrencyCode = "";
		List<LoanCustRmk> lLoanCustRmk = new ArrayList<LoanCustRmk>();

		if (iFacmNo != 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
		}
		if (iBormNo != 0) {
			wkBormNoStart = iBormNo;
			wkBormNoEnd = iBormNo;
		}

		// 查詢各項費用 全戶
		baTxCom.settingUnPaid(iEntryDate, iCustNo, 0, 0, 99, BigDecimal.ZERO, titaVo); // 99-費用全部(含未到期)
		int lLoanCustRmkSize = 0;
		// 帳務備忘錄明細檔
		Slice<LoanCustRmk> sLoanCustRmk = loanCustRmkService.findCustNo(iCustNo, 0, Integer.MAX_VALUE, titaVo);

		if (sLoanCustRmk != null) {

			lLoanCustRmk = sLoanCustRmk == null ? null : new ArrayList<LoanCustRmk>(sLoanCustRmk.getContent());

			lLoanCustRmkSize = lLoanCustRmk.size();
//			備忘錄代碼由大到小排序 序號由大到小
			Collections.sort(lLoanCustRmk, new Comparator<LoanCustRmk>() {
				public int compare(LoanCustRmk c1, LoanCustRmk c2) {
					if (!c1.getRmkCode().equals(c2.getRmkCode())) {
						return c2.getRmkCode().compareTo(c1.getRmkCode());
					}
					if (c1.getRmkNo() != c2.getRmkNo()) {
						return c2.getRmkNo() - c1.getRmkNo();
					}

					return 0;
				}

			});
			int cnt = 0;
//			只顯示前5筆備忘錄資料
//			超過最後一筆顯示...未完字眼
			for (LoanCustRmk t : lLoanCustRmk) {
				cnt++;
				if (cnt > 5) {
					break;
				}

				if (!loanCustRmkX.isEmpty()) {
					loanCustRmkX += "\n";
				}
				loanCustRmkX += t.getRmkDesc();
			}

			oLoanCustRmkFlag = "Y";
		}

		this.totaVo.putParam("OCustNo", iCustNo);
		this.totaVo.putParam("OExcessive", baTxCom.getExcessive());
		this.totaVo.putParam("OShortfall", baTxCom.getShortfall());
		this.totaVo.putParam("OCurrencyCode", wkCurrencyCode);
		this.totaVo.putParam("OLoanCustFlag", oLoanCustRmkFlag);
		this.totaVo.putParam("OTmpFacmNoX", baTxCom.getTmpFacmNoX());

		if (lLoanCustRmkSize > 5) {
			this.totaVo.putParam("OLoanCustRmkX", loanCustRmkX + "...未完"); // 帳務備忘錄
		} else {
			this.totaVo.putParam("OLoanCustRmkX", loanCustRmkX); // 帳務備忘錄
		}

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

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			resultList = l3005ServiceImpl.findAll(this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.info("Error ... " + e.getMessage());
		}

		if (resultList != null && resultList.size() != 0) {

			this.info("Size =" + resultList.size());
			String txNo = "";
			String newTxNo = "";
			for (Map<String, String> result : resultList) {
				OccursList occursList = new OccursList();

				String titaTlrNo = result.get("TitaTlrNo");
				String titaTxtNo = result.get("TitaTxtNo");
				String titaHCode = result.get("TitaHCode");
				String displayflag = result.get("Displayflag");
				String titaCurCd = result.get("TitaCurCd");
				String desc = result.get("Desc");
				String titaTxCd = result.get("TitaTxCd");
				String repayCodeX = result.get("Item");
				String createEmpNo = result.get("CreateEmpNo");
				int entryDate = parse.stringToInteger(result.get("EntryDate"));
				BigDecimal needPaidAmt = BigDecimal.ZERO;
				if (entryDate > 0) {
					entryDate = entryDate - 19110000;
				}
				int acDate = parse.stringToInteger(result.get("AcDate"));
				if (acDate > 0) {
					acDate = acDate - 19110000;
				}
				int facmNo = parse.stringToInteger(result.get("FacmNo"));
				int bormNo = parse.stringToInteger(result.get("BormNo"));
				int borxNo = parse.stringToInteger(result.get("BorxNo"));
				BigDecimal txAmt = parse.stringToBigDecimal(result.get("TxAmt"));
				BigDecimal tempAmt = parse.stringToBigDecimal(result.get("TempAmt"));
				BigDecimal overflow = parse.stringToBigDecimal(result.get("Overflow"));
				BigDecimal loanBal = parse.stringToBigDecimal(result.get("LoanBal"));
				BigDecimal rate = parse.stringToBigDecimal(result.get("Rate"));
				BigDecimal unpaidAmt = parse.stringToBigDecimal(result.get("UnpaidInterest"))
						.add(parse.stringToBigDecimal(result.get("UnpaidPrincipal")))
						.add(parse.stringToBigDecimal(result.get("UnpaidCloseBreach")));
				needPaidAmt = txAmt.add(tempAmt).subtract(overflow).add(unpaidAmt);
				newTxNo = titaVo.getKinbr() + titaTlrNo + titaTxtNo;
				TempVo tTempVo = new TempVo();
				tTempVo = tTempVo.getVo(result.get("OtherFields"));
				BigDecimal totTxAmt = parse.stringToBigDecimal(result.get("TotTxAmt"));
				BigDecimal wkOverShort = BigDecimal.ZERO;
				String txMsg = "";
				if (repayCodeX != null && !repayCodeX.isEmpty()) {
					txMsg += repayCodeX;
					if (totTxAmt.compareTo(BigDecimal.ZERO) != 0) {
						txMsg += df.format(totTxAmt);
					}
				}
				occursList.putParam("OOTxMsg", txMsg); // 還款類別 + 金額
				if (txNo.equals(newTxNo)) {
					AcFg = "";
				} else if (displayflag.equals("A") || displayflag.equals("F") || displayflag.equals("I")) {
					AcFg = "Y";
				} else {
					AcFg = "N";
				}
				// 是否顯示L3913計息明細按鈕
				if ((titaHCode.equals("0") || titaHCode.equals("2") || titaHCode.equals("4"))
						&& (displayflag.equals("I") || displayflag.equals("F"))) {
					loanIntDetailFg = "Y";
				} else {
					loanIntDetailFg = "N";
				}
				if (!"".equals(titaCurCd)) {
					wkCurrencyCode = titaCurCd;
				}
				this.totaVo.putParam("OCurrencyCode", wkCurrencyCode);
				wkOverShort = BigDecimal.ZERO;
				// 溢短收 有短收金額時為短收否則為溢收
				if (unpaidAmt.compareTo(BigDecimal.ZERO) > 0) {
					wkOverShort = BigDecimal.ZERO.subtract(unpaidAmt);
				} else {
					wkOverShort = overflow;
				}
				txNo = titaVo.getKinbr() + titaTlrNo + titaTxtNo;
				occursList.putParam("OOEntryDate", entryDate);
				occursList.putParam("OOAcDate", acDate);

				occursList.putParam("OODesc", desc);

				occursList.putParam("OOFacmNo", facmNo);
				occursList.putParam("OOBormNo", bormNo);
				occursList.putParam("OOBorxNo", borxNo);
				occursList.putParam("OORelNo", txNo); // 交易
				occursList.putParam("OOTellerNo", titaTlrNo);
				occursList.putParam("OOTxtNo", titaTxtNo);
				occursList.putParam("OOCurrencyCode", titaCurCd);
				if (titaHCode.equals("1") || titaHCode.equals("3")) {
					occursList.putParam("OOTxAmt", BigDecimal.ZERO.subtract(txAmt));
					occursList.putParam("OOTempAmt", BigDecimal.ZERO.subtract(tempAmt));
					occursList.putParam("OOOverShort", BigDecimal.ZERO.subtract(wkOverShort));
					occursList.putParam("OONeedPaidAmt", BigDecimal.ZERO.subtract(needPaidAmt)); // 應收金額
				} else {
					occursList.putParam("OOTxAmt", txAmt);
					occursList.putParam("OOTempAmt", tempAmt);
					occursList.putParam("OOOverShort", wkOverShort);
					occursList.putParam("OONeedPaidAmt", needPaidAmt); // 應收金額
				}

				occursList.putParam("OOLoanBal", loanBal);
				occursList.putParam("OORate", rate);
				occursList.putParam("OOTitaHCode", titaHCode);
				occursList.putParam("OOAcFg", AcFg); // 分錄清單Fg
				occursList.putParam("OOLoanIntDetailFg", loanIntDetailFg); // 計息明細Fg
				occursList.putParam("OOTxCd", titaTxCd); // 交易代號
				// 新增摘要 跟費用明細
				occursList.putParam("OONote", tTempVo.get("Note")); // 摘要
				occursList.putParam("OOTotTxAmt", totTxAmt); // 交易總金額
				occursList.putParam("OOCreateEmpNo", createEmpNo); // 建檔人員
				occursList.putParam("OODisplayFlag", displayflag); // 顯示記號

				// 將每筆資料放入Tota的OcList
				this.totaVo.addOccursList(occursList);
			}
		} else {

			if (iAcDate == 0) {
				throw new LogicException(titaVo, "E0001", "入帳日期  " + iEntryDate + " 後無交易資料"); // 查詢資料不存在
			} else {
				throw new LogicException(titaVo, "E0001", "會計日期  " + iAcDate + " 後無交易資料"); // 查詢資料不存在
			}
		}

		if (resultList != null && resultList.size() >= this.limit) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}