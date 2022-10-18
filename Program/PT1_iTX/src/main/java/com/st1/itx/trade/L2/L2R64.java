package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
import com.st1.itx.db.domain.Guarantor;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.GuarantorService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R64")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L2R64 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public GuarantorService guarantorService;
	@Autowired
	public CustMainService custMainService;
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public LoanBorMainService loanBorMainService;

	@Autowired
	public Parse parse;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R64  ");
		this.totaVo.init(titaVo);

		int iCustNo = parse.stringToInteger(titaVo.getParam("RimCustNo"));
		BigDecimal GuaAmt = BigDecimal.ZERO;
		// new ArrayList
		List<Guarantor> lGuarantor = new ArrayList<Guarantor>();
		List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();

		CustMain tCustMain = new CustMain();
		FacMain tFacMain = new FacMain();

		// 測試該統編是否存在保證人檔

		tCustMain = custMainService.custNoFirst(iCustNo, iCustNo, titaVo);
		if (tCustMain == null) {
			throw new LogicException("E2003", "該戶號不存在客戶主檔");
		}

		List<LinkedHashMap<String, String>> chkOccursList = null;

		Slice<Guarantor> slGuarantor = guarantorService.guaUKeyEq(tCustMain.getCustUKey(), this.index, this.limit,
				titaVo);
		if (slGuarantor != null) {
			for (Guarantor tGuarantor : slGuarantor.getContent()) {
				// new occurs
				OccursList occurslist = new OccursList();
				// new Table
				tCustMain = new CustMain();
				tFacMain = new FacMain();

				lLoanBorMain = new ArrayList<LoanBorMain>();
				GuaAmt = BigDecimal.ZERO;
				int[] test = { 0, 0 };

				// 取額度號碼
				tFacMain = facMainService.facmApplNoFirst(tGuarantor.getApproveNo(), titaVo);
				if (tFacMain == null) {
					tFacMain = new FacMain();
				}
				int custNo = tFacMain.getCustNo();
				// 取戶號,戶名
				tCustMain = custMainService.custNoFirst(custNo, custNo, titaVo);
				if (tCustMain == null) {
					tCustMain = new CustMain();
				}
				// 取戶況,繳息迄日
				Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(tFacMain.getCustNo(),
						tFacMain.getFacmNo(), tFacMain.getFacmNo(), 0, 900, 0, Integer.MAX_VALUE, titaVo);

				lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();

				if (lLoanBorMain != null && !lLoanBorMain.isEmpty()) {
					test = judgeStatus(lLoanBorMain);
					occurslist.putParam("L2r64Stat", 1);
				} else {
					occurslist.putParam("L2r64Stat", 0);
				}

				if (lLoanBorMain != null) {
					for (LoanBorMain tmpLoanBorMain : lLoanBorMain) {
						GuaAmt = GuaAmt.add(tmpLoanBorMain.getLoanBal());
					}
				} else {
					GuaAmt = BigDecimal.ZERO;
				}

				occurslist.putParam("L2r64CustNo", tFacMain.getCustNo());
				occurslist.putParam("L2r64FacmNo", tFacMain.getFacmNo());
				occurslist.putParam("L2r64CustName", tCustMain.getCustName());
				occurslist.putParam("L2r64CustStat", test[0]);
				// 最高保證金額
				occurslist.putParam("L2r64HighGuaAmt", tGuarantor.getGuaAmt());
				occurslist.putParam("L2r64GuaAmt", GuaAmt);
				occurslist.putParam("L2r64PayIntDate", test[1]);
				occurslist.putParam("L2r64GuaStatCode", tGuarantor.getGuaStatCode());

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occurslist);
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	/**
	 * 根據該額度底下所有撥款戶戶況對應的優先度判斷tota應放的戶況及繳息迄日<BR>
	 * 優先度 : 戶況 : 中文<BR>
	 * 1 : 6 : 呆帳戶<BR>
	 * 2 : 7 : 部分轉呆戶<BR>
	 * 3 : 2 : 催收戶<BR>
	 * 4 : 4 : 逾期戶<BR>
	 * 5 : 0 : 正常戶<BR>
	 * 6 : 5 : 催收結案戶<BR>
	 * 7 : 8 : 債權轉讓戶<BR>
	 * 8 : 9 : 呆帳結案戶<BR>
	 * 9 : 3 : 結案戶<BR>
	 * 10 : 97 : 預約撥款已刪除<BR>
	 * 10 : 98 : 預約已撥款<BR>
	 * 10 : 99 : 預約撥款<BR>
	 * 
	 * @param lLoanBorMain 撥款資料
	 * @return [0]=戶況<BR>
	 *         [1]=繳息迄日
	 * @throws LogicException LogicException
	 */
	public int[] judgeStatus(List<LoanBorMain> lLoanBorMain) throws LogicException {
		int priorty = 10;
		int status = 0;
		int tbsdy = this.getTxBuffer().getTxCom().getTbsdy();
		int loandate = 0;
		int[] result = new int[2];

		for (LoanBorMain tmpLoanBorMain : lLoanBorMain) {
			if (tmpLoanBorMain.getStatus() < 90) {
//						if (tmpLoanBorMain.getStatus() > 1 && tmpLoanBorMain.getStatus() < 90) {
				int thisStatus = tmpLoanBorMain.getStatus();
				int thisPriorty;
				switch (thisStatus) {
				case 6:
					thisPriorty = 1;
					break;
				case 7:
					thisPriorty = 2;
					break;
				case 2:
					thisPriorty = 3;
					break;
				case 4:
					thisPriorty = 4;
					break;
				case 0:
					thisPriorty = 5;
					break;
				case 5:
					thisPriorty = 6;
					break;
				case 8:
					thisPriorty = 7;
					break;
				case 9:
					thisPriorty = 8;
					break;
				case 3:
					thisPriorty = 9;
					break;
				default:
					thisPriorty = 10;
					break;
				}

				if (thisPriorty < priorty) {
					status = thisStatus;
					if (tmpLoanBorMain.getPrevPayIntDate() == 0) {
						loandate = tmpLoanBorMain.getDrawdownDate();
					} else {
						loandate = tmpLoanBorMain.getPrevPayIntDate();
					}
					priorty = thisPriorty;
					// 判斷是否為逾期戶
					if (status == 0 && tmpLoanBorMain.getNextPayIntDate() < tbsdy) {
						dateUtil.init();
						dateUtil.setDate_1(tbsdy);
						dateUtil.setMons(-1);
						int payDate = dateUtil.getCalenderDay();
						if (tmpLoanBorMain.getNextPayIntDate() < payDate) { // 逾期超過一個月
							status = 4;
							priorty = 4;
							if (tmpLoanBorMain.getPrevPayIntDate() == 0) {
								loandate = tmpLoanBorMain.getDrawdownDate();
							} else {
								loandate = tmpLoanBorMain.getPrevPayIntDate();
							}
						}
					}
				}
			} // if
		} // for

		result[0] = status;
		result[1] = loandate;

		return result;
	}
}
