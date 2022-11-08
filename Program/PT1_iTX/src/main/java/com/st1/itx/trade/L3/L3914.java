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
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.parse.Parse;

/*
 * L3914 暫收款退還/銷帳資料查詢，供訂正(轉換前資料)使用
 * a.此功能供查詢某一戶號,其所有交易之暫收款退還、銷帳資料.
 * b.畫面會計日期(起日)之初值為當月1日
 * c.正常交易資料
 */

/**
 * L3914 暫收款退還/銷帳資料查詢
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3914")
@Scope("prototype")
public class L3914 extends TradeBuffer {

	/* DB服務注入 */

	@Autowired
	public LoanBorTxService loanBorTxService;

	@Autowired
	public AcDetailService acDetailService;
	@Autowired
	public AcReceivableService acReceivableService;
	@Autowired
	public LoanCom loanCom;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3914 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		int iAcDate = this.parse.stringToInteger(titaVo.getParam("AcDate"));
		int iEntryDate = this.parse.stringToInteger(titaVo.getParam("EntryDate"));

		// work area
		int wkAcDateStart = iAcDate + 19110000;
		int wkEntryDateStart = iEntryDate + 19110000;
		int wkDateEnd = 99991231;

		Slice<LoanBorTx> slLoanBorTx;

		List<LoanBorTx> lLoanBorTx;

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設50筆 總長不可超過六萬
		this.limit = 150; // 100* 365 = 15400

		// 查詢放款交易內容檔
		List<String> lDisplayFlag = new ArrayList<String>();
		lDisplayFlag.add("Y");
		lDisplayFlag.add("I"); // 繳息次筆
		lDisplayFlag.add("A"); // 帳務
		lDisplayFlag.add("F"); // 繳息首筆

		if (iAcDate == 0) {
			slLoanBorTx = loanBorTxService.borxEntryDateRange(iCustNo, 0, 999, 0, 900, wkEntryDateStart,
					wkEntryDateStart, lDisplayFlag, 0, Integer.MAX_VALUE, titaVo);
		} else {
			slLoanBorTx = loanBorTxService.borxAcDateRange(iCustNo, 0, 999, 0, 900, wkAcDateStart, wkAcDateStart,
					lDisplayFlag, 0, Integer.MAX_VALUE, titaVo);
		}

		lLoanBorTx = slLoanBorTx == null ? null : new ArrayList<LoanBorTx>(slLoanBorTx.getContent());
		if (lLoanBorTx == null || lLoanBorTx.size() == 0) {
			if (iAcDate == 0) {
				throw new LogicException(titaVo, "E0001", "入帳日期  " + iEntryDate + " 後無交易資料"); // 查詢資料不存在
			} else {
				throw new LogicException(titaVo, "E0001", "會計日期  " + iAcDate + " 後無交易資料"); // 查詢資料不存在
			}
		}
		// 如有有找到資料
		for (LoanBorTx ln : lLoanBorTx) {
			OccursList occursList = new OccursList();
			if (!ln.getTitaHCode().equals("0")) {
				continue;
			}
			if (!("L3210".equals(ln.getTitaTxCd()) || "L3220".equals(ln.getTitaTxCd())
					|| "L3230".equals(ln.getTitaTxCd()))) {
				continue;
			}
			Slice<AcDetail> slAcDetail = acDetailService.findTxtNoEq(ln.getAcDate() + 19110000, ln.getTitaKinBr(),
					ln.getTitaTlrNo(), parse.stringToInteger(ln.getTitaTxtNo()), 0, Integer.MAX_VALUE, titaVo);
			if (slAcDetail == null) {
				this.info("slAcDetail = " + slAcDetail);
				continue;
			}
			BigDecimal txAmt = BigDecimal.ZERO;
			// 將每筆資料放入Tota的OcList
			occursList = new OccursList();
			occursList.putParam("OOEntryDate", ln.getEntryDate());
			occursList.putParam("OOAcDate", ln.getAcDate());
			occursList.putParam("OODesc",loanCom.getTxDescCodeX(ln, titaVo));
			occursList.putParam("OOFacmNo", ln.getFacmNo());
			if (ln.getTxAmt().compareTo(BigDecimal.ZERO) > 0) {
				txAmt = ln.getTxAmt();
			} else if (ln.getTxAmt().compareTo(BigDecimal.ZERO) < 0) {
				txAmt = BigDecimal.ZERO.subtract(ln.getTxAmt());
			} else {
				txAmt = ln.getTempAmt().subtract(ln.getOverflow());
				if (txAmt.compareTo(BigDecimal.ZERO) < 0) {
					txAmt = BigDecimal.ZERO.subtract(txAmt);
				}
			}
			occursList.putParam("OOAcctCode", "");
			occursList.putParam("OORvNo", "");
			occursList.putParam("OOTxAmt", txAmt);
			occursList.putParam("OOTellerNo", ln.getTitaTlrNo());
			occursList.putParam("OOTxtNo", ln.getTitaTxtNo());
			for (AcDetail ac : slAcDetail.getContent()) {
				if ("C".equals(ac.getDbCr())) {
					if ("F10".equals(ac.getAcctCode()) || "F27".equals(ac.getAcctCode())
							|| "TMI".equals(ac.getAcctCode()) || "F07".equals(ac.getAcctCode())
							|| "F08".equals(ac.getAcctCode()) || "F09".equals(ac.getAcctCode())
							|| "F10".equals(ac.getAcctCode()) || "F12".equals(ac.getAcctCode())
							|| "F13".equals(ac.getAcctCode()) || "F14".equals(ac.getAcctCode())
							|| "F15".equals(ac.getAcctCode()) || "F16".equals(ac.getAcctCode())
							|| "F18".equals(ac.getAcctCode()) || "F21".equals(ac.getAcctCode())
							|| "F24".equals(ac.getAcctCode()) || "F25".equals(ac.getAcctCode())
							|| "F27".equals(ac.getAcctCode()) || "F29".equals(ac.getAcctCode())) {
						occursList.putParam("OOAcctCode", ac.getAcctCode());
						occursList.putParam("OORvNo", ac.getRvNo());
					}

				}
			}
			this.totaVo.addOccursList(occursList);

			if (slAcDetail != null && slAcDetail.getContent().size() >= this.limit) {
				/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
				titaVo.setReturnIndex(this.setIndexNext());
				this.totaVo.setMsgEndToEnter();// 手動折返
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}