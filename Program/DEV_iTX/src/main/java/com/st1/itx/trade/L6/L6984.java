package com.st1.itx.trade.L6;

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
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * FUNCD=9,1<br>
 * SELECT_CD=9,1<br>
 * END=X,1<br>
 */

@Service("L6984")
@Scope("prototype")
/**
 * 預約撥款到期作業
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L6984 extends TradeBuffer {

	@Autowired
	public Parse parse;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	public TxToDoDetailService txToDoDetailService;

	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public LoanBorMainService sLoanBorMainService;
	@Autowired
	public FacMainService sFacMainService;

	private int selectCode = 0;
	private int custNo = 0;
	private int trasCollDate = 0;
	private int cnt = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6984 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500; // 95 * 500 = 47500

		selectCode = parse.stringToInteger(titaVo.getParam("SelectCode"));

		// 0.未處理
		// 1.已保留
		// 2.已處理
		// 3.已刪除

		List<TxToDoDetail> lTxToDoDetail = new ArrayList<TxToDoDetail>();
		Slice<TxToDoDetail> slTxToDoDetail = null;
//		RVTX00 預約撥款到期

//		! 1:昨日留存 
//		! 2:本日新增 
//		! 3:全部
//		! 4:本日處理 
//		! 5:本日刪除 
//		! 6:保留
//		! 7:未處理
//		! 9:未處理 (按鈕處理)

		this.info("selectCode = " + selectCode);

		switch (selectCode) {
		case 1:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("RVTX00", 0, 3, this.index, this.limit, titaVo);

			break;
		case 2:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("RVTX00", 0, 3, this.index, this.limit, titaVo);
			break;
		case 3:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("RVTX00", 0, 3, this.index, this.limit, titaVo);
			break;
		case 4:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("RVTX00", 2, 2, this.index, this.limit, titaVo);
			break;
		case 5:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("RVTX00", 3, 3, this.index, this.limit, titaVo);
			break;
		case 6:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("RVTX00", 1, 1, this.index, this.limit, titaVo);
			break;
		case 7:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("RVTX00", 0, 0, this.index, this.limit, titaVo);
			break;
		case 9:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("RVTX00", 0, 0, this.index, this.limit, titaVo);
			break;
		default:
			break;
		}

		lTxToDoDetail = slTxToDoDetail == null ? null : slTxToDoDetail.getContent();

		this.info("lTxToDoDetail = " + lTxToDoDetail);

		if (lTxToDoDetail != null && lTxToDoDetail.size() != 0) {
			for (TxToDoDetail tTxToDoDetail : lTxToDoDetail) {
				OccursList occursList = new OccursList();
				this.info("custno = " + tTxToDoDetail.getCustNo());
				this.info("facmno = " + tTxToDoDetail.getFacmNo());
				this.info("bormno = " + tTxToDoDetail.getBormNo());
				if (selectCodeIsNotQualify(tTxToDoDetail)) {
					continue;
				}
//				InsuRenew tIsuRenew = new InsuRenew();
				CustMain tCustMain = new CustMain();
				LoanBorMain tLoanBorMain = new LoanBorMain();
				FacMain tFacMain = new FacMain();

				// 取戶名
				tCustMain = sCustMainService.custNoFirst(tTxToDoDetail.getCustNo(), tTxToDoDetail.getCustNo(), titaVo);
				// 預約日期,幣別,撥款金額
				tLoanBorMain = sLoanBorMainService.findById(new LoanBorMainId(tTxToDoDetail.getCustNo(),
						tTxToDoDetail.getFacmNo(), tTxToDoDetail.getBormNo()), titaVo);
				// 案件編號,核准號碼
				tFacMain = sFacMainService.findById(new FacMainId(tTxToDoDetail.getCustNo(), tTxToDoDetail.getFacmNo()),
						titaVo);

				occursList.putParam("OODrawdownDate", tLoanBorMain.getDrawdownDate()); // 預約日期
				occursList.putParam("OOCaseNo", tFacMain.getCreditSysNo()); // 案件編號
				occursList.putParam("OOApplNo", tFacMain.getApplNo()); // 核准號碼
				occursList.putParam("OOFacmNo", tTxToDoDetail.getFacmNo()); // 額度號碼
				occursList.putParam("OOBormNo", tTxToDoDetail.getBormNo()); // 預約序號
				occursList.putParam("OOCurrencyCode", tLoanBorMain.getCurrencyCode()); // 幣別
				occursList.putParam("OODrawdownAmt", tLoanBorMain.getDrawdownAmt()); // 撥款金額
				occursList.putParam("OORelNo", titaVo.getParam("KINBR") + tTxToDoDetail.getTitaTlrNo()
						+ parse.IntegerToString(tTxToDoDetail.getTitaTxtNo(), 8)); // 登放序號
				occursList.putParam("OOItemCode", tTxToDoDetail.getItemCode());
				occursList.putParam("OOCustNo", tTxToDoDetail.getCustNo());
				occursList.putParam("OODtlValue", tTxToDoDetail.getDtlValue());
				occursList.putParam("OOCustId", tCustMain.getCustId());

				occursList.putParam("TimCustNo", tLoanBorMain.getCustNo());
				occursList.putParam("FacmNo", tLoanBorMain.getFacmNo());
				occursList.putParam("BormNo", tLoanBorMain.getBormNo());
				occursList.putParam("RateIncr", tLoanBorMain.getRateIncr());
				occursList.putParam("ApproveRate", tLoanBorMain.getApproveRate());
				occursList.putParam("RateCode", tLoanBorMain.getRateCode());
				occursList.putParam("RateAdjFreq", tLoanBorMain.getRateAdjFreq());
				occursList.putParam("DrawdownCode", tLoanBorMain.getDrawdownCode());
				occursList.putParam("CurrencyCode", tLoanBorMain.getCurrencyCode());
				occursList.putParam("TimDrawdownAmt", tLoanBorMain.getDrawdownAmt());
				occursList.putParam("DrawdownDate", tLoanBorMain.getDrawdownDate());
				occursList.putParam("LoanTermYy", tLoanBorMain.getLoanTermYy());
				occursList.putParam("LoanTermMm", tLoanBorMain.getLoanTermMm());
				occursList.putParam("LoanTermDd", tLoanBorMain.getLoanTermDd());
				occursList.putParam("MaturityDate", tLoanBorMain.getMaturityDate());
				occursList.putParam("AmortizedCode", tLoanBorMain.getAmortizedCode());
				occursList.putParam("CompensateFlag", tFacMain.getCompensateFlag());
				occursList.putParam("IntCalcCode", tFacMain.getIntCalcCode());

				occursList.putParam("FreqBase", tLoanBorMain.getFreqBase());
				occursList.putParam("PayIntFreq", tLoanBorMain.getPayIntFreq());
				occursList.putParam("RepayFreq", tLoanBorMain.getRepayFreq());
				occursList.putParam("GracePeriod", tLoanBorMain.getGracePeriod());
				occursList.putParam("GraceDate", tLoanBorMain.getGraceDate());
				occursList.putParam("SpecificDd", tLoanBorMain.getSpecificDd());
				occursList.putParam("SpecificDate", tLoanBorMain.getSpecificDate());
				occursList.putParam("FirstDueDate", tLoanBorMain.getFirstDueDate());
				occursList.putParam("FirstAdjRateDate", tLoanBorMain.getFirstAdjRateDate());
				occursList.putParam("NextIntDate", tLoanBorMain.getNextPayIntDate());
				occursList.putParam("NextRepayDate", tLoanBorMain.getNextRepayDate());
				occursList.putParam("TotalPeriod", tLoanBorMain.getTotalPeriod());
				occursList.putParam("TimAcctFee", tLoanBorMain.getAcctFee());
				occursList.putParam("TimFinalBal", tLoanBorMain.getFinalBal());
				occursList.putParam("NotYetFlag", tLoanBorMain.getNotYetFlag());
				occursList.putParam("PieceCode", tLoanBorMain.getPieceCode());
				occursList.putParam("PieceCodeSecond", tLoanBorMain.getPieceCodeSecond());
				occursList.putParam("PieceCodeSecondAmt", tLoanBorMain.getPieceCodeSecondAmt());
				occursList.putParam("UsageCode", tLoanBorMain.getUsageCode());
				occursList.putParam("SyndNo", tLoanBorMain.getSyndNo());
				occursList.putParam("RenewFlag", tLoanBorMain.getRenewFlag());

				occursList.putParam("RpCode1", 01);
				occursList.putParam("RpAmt1", tLoanBorMain.getDrawdownAmt());
				occursList.putParam("RpRemitBank1", tLoanBorMain.getRemitBank());
				occursList.putParam("RpRemitBranch1", tLoanBorMain.getRemitBranch());
				occursList.putParam("RpRemitAcctNo1", tLoanBorMain.getRemitAcctNo());
				occursList.putParam("RpCustName1", tCustMain.getCustName());
				occursList.putParam("RpRemark1", tLoanBorMain.getRemark());

				occursList.putParam("RpFlag", 2);
				occursList.putParam("RpAcCode1", "");
				occursList.putParam("RpShortAmts", 0);
				occursList.putParam("RpType1", 0);
				occursList.putParam("RpDetailSeq1", 0);
				occursList.putParam("RpEntryDate1", 0);
				occursList.putParam("RpAcctCode1", "");
				occursList.putParam("RpFacmNo1", 0);
				occursList.putParam("RpBormNo1", 0);
				occursList.putParam("RpCustNo1", 0);
				occursList.putParam("NOTE1", "");
				occursList.putParam("TITFCD", 1);
				occursList.putParam("RpRvno1", "");

				cnt++;
				this.totaVo.addOccursList(occursList);
			}
		}
		if (cnt == 0) {
			throw new LogicException(titaVo, "E0001", "預約撥款到期作業"); // 查詢資料不存在
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	private Boolean selectCodeIsNotQualify(TxToDoDetail tTxToDoDetail) throws LogicException {
		Boolean result = false;
		int today = this.getTxBuffer().getTxCom().getTbsdy();
		switch (selectCode) {
		case 1:
			if (tTxToDoDetail.getDataDate() >= today) {
				result = true;
			}
			break;
		case 2:
			if (tTxToDoDetail.getDataDate() != today) {
				result = true;
			}
			break;

		default:
			break;
		}

		if (custNo > 0) {
			if (tTxToDoDetail.getCustNo() != custNo) {
				result = true;
			}
		}

		if (trasCollDate > 0) {
			if (tTxToDoDetail.getTitaEntdy() != trasCollDate) {
				result = true;
			}
		}

		return result;
	}

}