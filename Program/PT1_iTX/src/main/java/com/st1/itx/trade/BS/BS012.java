package com.st1.itx.trade.BS;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Component("BS012")
@Scope("prototype")

/**
 * 新增應處理明細－年底日呆帳戶產生法務費墊付 <br>
 * 執行時機：日始作業，應處理清單維護(BS001)自動執行 <br>
 * 
 * @author Lai
 * @version 1.0.0
 */

public class BS012 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public LoanBorMainService loanBorMainService;

	@Autowired
	public AcReceivableService acReceivableService;

	@Autowired
	public TxToDoCom txToDoCom;
	
	@Autowired
	WebClient webClient;

	int custNo = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("BS012 ......");

		txToDoCom.setTxBuffer(this.txBuffer);

		// 取本月份
		String entryDateMm = parse.IntegerToString(this.txBuffer.getMgBizDate().getTbsDy() / 100, 5).substring(3, 5);
		this.info("取本月份 = " + parse.stringToInteger(entryDateMm));

		// 年底呆帳戶產生法務費墊付
		procBdLawFee(titaVo);
		this.batchTransaction.commit();
		if ("LC899".equals(titaVo.getTxcd())) {
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "N", "", "", "BS012已完成", titaVo);
		}
		return null;
	}

	/* 年底呆帳產生法務費墊付(全戶為呆帳戶、呆帳結案戶及債權轉讓戶) */
	private void procBdLawFee(TitaVo titaVo) throws LogicException {
		Slice<AcReceivable> slAcReceivable = null;
		List<AcReceivable> lAcReceivable = new ArrayList<AcReceivable>();
		List<String> lAcctCode = new ArrayList<String>();
		int custNo = 0;
		Boolean isAllBadLoan = false;

		lAcctCode.add("F07"); // 法務費
		lAcctCode.add("F24"); // 催收法務費
		// find data
		slAcReceivable = acReceivableService.UseL5074(0, lAcctCode, 0, Integer.MAX_VALUE, titaVo);

		if (slAcReceivable.getContent() != null) {
			for (AcReceivable rv : slAcReceivable.getContent()) {
				// 同一戶符合檢查時寫入
				if (custNo != rv.getCustNo()) {
					isAllBadLoan = checkStatus(rv, titaVo);
					custNo = rv.getCustNo();
				}
				if (isAllBadLoan) {
					lAcReceivable.add(rv);
					continue;
				}
			}
		}

		TxToDoDetail tTxToDoDetail;
		if (lAcReceivable != null) {
			for (AcReceivable rv : lAcReceivable) {

				if (rv.getRvNo().length() == 7) {
					tTxToDoDetail = new TxToDoDetail();
					TempVo tTempVo = new TempVo();
					tTempVo.clear();
					tTempVo.putParam("AcctCode", rv.getAcctCode());
					tTxToDoDetail.setItemCode("BDLW00"); // 呆帳產生法務費墊付
					tTxToDoDetail.setCustNo(rv.getCustNo());
					tTxToDoDetail.setFacmNo(rv.getFacmNo());
					tTxToDoDetail.setDtlValue(rv.getRvNo());
					tTxToDoDetail.setProcessNote(tTempVo.getJsonString());
					txToDoCom.addDetail(true, titaVo.getHCodeI(), tTxToDoDetail, titaVo); // DupSkip = true ->重複跳過
				}
			}
		}
	}

	// 檢查全戶為呆帳戶、債權轉讓戶、呆帳結案戶
	private Boolean checkStatus(AcReceivable rv, TitaVo titaVo) throws LogicException {
		boolean isAllBadLoan = true;
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(rv.getCustNo(), 0, 999, 0, 900, 0,
				Integer.MAX_VALUE, titaVo);
		if (slLoanBorMain != null) {
			for (LoanBorMain t : slLoanBorMain.getContent()) {
				if (t.getStatus() == 0 || t.getStatus() == 2 || t.getStatus() == 7) {
					isAllBadLoan = false;					
				}
			}
		}
		return isAllBadLoan;

	}
}