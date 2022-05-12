package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanRateChangeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L3250 暫收款退還沖正(轉換前交易)
 * a.使用時機:費用回收，依交易序號執行沖正
 * b.本交易不可訂正
 */

/*
 * Tita
 * TimCustNo=9,7 戶號
 * FacmNo=9,3  額度編號 
 * EntryDate 入帳日期
 * AcDate 會計日期
 * AcDate 經辦
 * TxtNo 交易序號
  * 沖正交易序號 ，會計日(7)+單位別(4)+經辦(6)+交易序號(8)
 */

/**
 * L3250 回收登錄
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3250")
@Scope("prototype")
public class L3250 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public InsuRenewService insuRenewService;
	@Autowired
	public LoanBorTxService loanBorTxService;
	@Autowired
	public LoanRateChangeService loanRateChangeService;

	@Autowired
	Parse parse;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	LoanCom loanCom;
	@Autowired
	AcDetailCom acDetailCom;
	@Autowired
	AcReceivableCom acReceivableCom;

	private TitaVo titaVo;
	private int iCustNo;
	private int iFacmNo;
	private int iEntryDate;
	private int iAcDate;
	private String iTellerNo;
	private String iTxtNo;
	private BigDecimal iTxAmt = BigDecimal.ZERO;
	private String iRvNo;
	private String iAcctCode;

	// work area
	private AcDetail acDetail;
	private List<AcDetail> lAcDetail = new ArrayList<AcDetail>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3250 ");

		this.totaVo.init(titaVo);
		this.titaVo = titaVo;
		loanCom.setTxBuffer(this.txBuffer);

		// 取得輸入資料
		iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		iEntryDate = this.parse.stringToInteger(titaVo.getParam("EntryDate"));
		iAcDate = this.parse.stringToInteger(titaVo.getParam("AcDate"));
		iTellerNo = titaVo.getParam("TellerNo");
		iTxtNo = titaVo.getParam("TxtNo");
		iTxAmt = parse.stringToBigDecimal(titaVo.getTxAmt());
		iRvNo = titaVo.getParam("RvNo");
		iAcctCode = titaVo.getParam("AcctCode");
		// Check Input

		checkInputRoutine();

		// 沖正處理
		repayEraseRoutine();

		// 帳務處理
		if (this.txBuffer.getTxCom().isBookAcYes()) {

//			// 暫收可抵繳
			acDetail = new AcDetail();
			acDetail.setDbCr("D");
			acDetail.setAcctCode(iAcctCode);
			acDetail.setTxAmt(iTxAmt);
			acDetail.setCustNo(iCustNo);
			acDetail.setFacmNo(iFacmNo);
			acDetail.setRvNo(iRvNo);
			if ("TMI".equals(iAcctCode)) {
				checkInsuRenew(acDetail, titaVo);
			}
			lAcDetail.add(acDetail);

			acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode("TAV");
			acDetail.setTxAmt(iTxAmt);
			acDetail.setCustNo(iCustNo);
			acDetail.setFacmNo(iFacmNo);
			lAcDetail.add(acDetail);

			// 借： 本金利息、貸：暫收可抵繳
			this.txBuffer.addAllAcDetailList(lAcDetail);

			// 產生會計分錄
			acDetailCom.setTxBuffer(this.txBuffer);
			acDetailCom.run(titaVo);

		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void checkInputRoutine() throws LogicException {
		if (titaVo.isHcodeErase()) {
			throw new LogicException(titaVo, "E0010", "本交易不可訂正"); // 功能選擇錯誤
		}
	}

	// 沖正處理
	private void repayEraseRoutine() throws LogicException {
		this.info("calcRepayEraseRoutine ...");
		// 查詢放款交易內容檔
		List<String> ltitaHCode = new ArrayList<String>();
		ltitaHCode.add("0"); // 正常
		Slice<LoanBorTx> slLoanBorTx = loanBorTxService.findIntEndDateEq(iCustNo, iFacmNo, 1, 990, 0, ltitaHCode,
				iAcDate + 19110000, iTellerNo, iTxtNo, 0, Integer.MAX_VALUE, titaVo);
		if (slLoanBorTx == null) {
			throw new LogicException(titaVo, "E0001", "放款交易內容檔"); // 查詢資料不存在
		}
		for (LoanBorTx tx : slLoanBorTx.getContent()) {
			if (!tx.getTitaHCode().equals("0")) {
				continue;
			}
			if (tx.getEntryDate() != iEntryDate) {
				continue;
			}
			// 註記交易內容檔
			loanCom.setFacmBorTxHcode(tx.getCustNo(), tx.getFacmNo(), titaVo);
		}
	}

	private void checkInsuRenew(AcDetail ac, TitaVo titaVo) throws LogicException {
		Slice<InsuRenew> slInsuRenew = insuRenewService.findCustEq(iCustNo, 0, Integer.MAX_VALUE, titaVo);
		if (slInsuRenew != null) {
			for (InsuRenew tInsuRenew : slInsuRenew.getContent()) {
				if (tInsuRenew.getAcDate() > 0 && tInsuRenew.getTitaTxtNo().equalsIgnoreCase(iTxtNo)
						&& tInsuRenew.getTotInsuPrem().equals(iTxAmt)) {
					switch (tInsuRenew.getStatusCode()) {
					case 0:
						ac.setReceivableFlag(3);
						ac.setAcctCode("TMI"); // TMI 火險保費
						break;
					case 1:
						ac.setReceivableFlag(2);
						ac.setAcctCode("F09"); // F09 暫付款－火險保費
						break;
					case 2:
						ac.setReceivableFlag(2);
						ac.setAcctCode("F24"); // F24 催收款項－法務費用
						break;
					default:
						break;
					}
					ac.setCustNo(tInsuRenew.getCustNo());// 戶號+額度
					ac.setFacmNo(tInsuRenew.getFacmNo());
					ac.setRvNo(tInsuRenew.getPrevInsuNo()); // 銷帳編號
					TempVo tTempVo = new TempVo();
					tTempVo.clear();
					tTempVo.putParam("OpenAcDate", tInsuRenew.getInsuStartDate());
					ac.setJsonFields(tTempVo.getJsonString());
				}
			}
		}
	}

}