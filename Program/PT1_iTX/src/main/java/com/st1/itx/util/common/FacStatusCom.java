package com.st1.itx.util.common;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.LoanBorMainService;

/**
 * 設定額度戶況<BR>
 * 1.settingStatus 依撥款檔戶況設定額度戶況call by LXXXX<BR>
 * 
 * @author st1
 *
 */
@Component("FacStatusCom")
@Scope("prototype")
public class FacStatusCom {

	/* 日期工具 */
	@Autowired
	DateUtil dateUtil;

	@Autowired
	LoanBorMainService loanBorMainService;

	/**
	 * 額度
	 * 
	 * @return
	 */
	public int getFacmNo() {
		return facmNo;
	}

	private int facmNo = 0;

	/**
	 * 設定額度戶況
	 * 
	 * @param lLoanBorMain List of LoanBorMain
	 * @param tbsdy        會計日期
	 * @return status 0.正常戶(含未撥款) 1.展期戶 2.催收戶 3.結案戶 4.逾期戶(正常戶逾期超過一個月) 6.呆帳戶 7.部分轉呆戶
	 *         8.債權轉讓戶 9.呆帳結案戶
	 * @throws LogicException LogicException
	 */
	public int settingStatus(List<LoanBorMain> lLoanBorMain, int tbsdy) throws LogicException {
		int priorty = 11;
		int status = 0;

		// 2022-05-10 智偉增加:防呆
		if (lLoanBorMain == null || lLoanBorMain.isEmpty()) {
			this.facmNo = 0;
			return status;
		}
		// priority status
		// 1 -- 6.呆帳戶
		// 2 -- 7.部分轉呆戶
		// 3 -- 2.催收戶
		// 4 -- 4.逾期戶(正常戶逾期超過一個月)
		// 5 -- 0.正常戶
		// 6 -- 5.催收結案戶
		// 7 -- 8.債權轉讓戶
		// 8 -- 9.呆帳結案戶
		// 9 -- 3.結案戶
		// 10 -- 1.展期戶
		for (LoanBorMain tmpLoanBorMain : lLoanBorMain) {
			if (tmpLoanBorMain.getStatus() < 90) {
				switch (tmpLoanBorMain.getStatus()) {
				case 6:
					status = 6;
					priorty = 1;
					break;
				case 7:
					if (priorty > 2) {
						status = 7;
						priorty = 2;
					}
					break;

				case 2:
					if (priorty > 3) {
						status = 2;
						priorty = 3;
					}
					break;
				case 0:
					if (priorty > 4) {
						status = 0;
						priorty = 5;
						if (tmpLoanBorMain.getNextPayIntDate() < tbsdy) {
							dateUtil.init();
							dateUtil.setDate_1(tbsdy);
							dateUtil.setMons(-1);
							int payDate = dateUtil.getCalenderDay();
							if (tmpLoanBorMain.getNextPayIntDate() < payDate) { // 逾期超過一個月
								status = 4;
								priorty = 4;
							}
						}
					}
					break;

				case 5:
					if (priorty > 6) {
						status = 5;
						priorty = 6;
					}
					break;
				case 8:
					if (priorty > 7) {
						status = 8;
						priorty = 7;
					}
					break;
				case 9:
					if (priorty > 8) {
						status = 9;
						priorty = 8;
					}
					break;
				case 3:
					if (priorty > 9) {
						status = 3;
						priorty = 9;
					}
					break;
				case 1:
					if (priorty > 10) {
						status = 1;
						priorty = 10;
					}
					break;
				}

			}
		}
		for (LoanBorMain tmpLoanBorMain : lLoanBorMain) {
			if (tmpLoanBorMain.getStatus() == tmpLoanBorMain.getStatus()) {
				this.facmNo = tmpLoanBorMain.getFacmNo();
			}
		}
		return status;
	}

	/**
	 * 取得額度戶況
	 * 
	 * @param iCustNo 戶號
	 * @param iFacmNo 額度
	 * @param tbsdy   會計日期
	 * @param titaVo  TiTaVo
	 * @return status -1.未撥款 0.正常戶 1.展期戶 2.催收戶 3.結案戶 4.逾期戶(正常戶逾期超過一個月) 6.呆帳戶 7.部分轉呆戶
	 *         8.債權轉讓戶 9.呆帳結案戶
	 * @throws LogicException ....
	 */

	public int getLoanStatus(int iCustNo, int iFacmNo, int tbsdy, TitaVo titaVo) throws LogicException {
		int priorty = 11;
		int status = 0;
		// priority status
		// 1 -- 6.呆帳戶
		// 2 -- 7.部分轉呆戶
		// 3 -- 2.催收戶
		// 4 -- 4.逾期戶(正常戶逾期超過一個月)
		// 5 -- 0.正常戶
		// 6 -- 5.催收結案戶
		// 7 -- 8.債權轉讓戶
		// 8 -- 9.呆帳結案戶
		// 9 -- 3.結案戶
		// 10 -- 1.展期戶

		List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();
		Slice<LoanBorMain> slLoanBorMain = null;

		slLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, iFacmNo, iFacmNo, 0, 900, 0, Integer.MAX_VALUE,
				titaVo);

		lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();

		// 2022-05-10 智偉增加:防呆
		if (lLoanBorMain == null || lLoanBorMain.isEmpty()) {
			return -1;
		}
		for (LoanBorMain tmpLoanBorMain : lLoanBorMain) {
			if (tmpLoanBorMain.getStatus() < 90) {
				switch (tmpLoanBorMain.getStatus()) {
				case 6:
					status = 6;
					priorty = 1;
					break;
				case 7:
					if (priorty > 2) {
						status = 7;
						priorty = 2;
					}
					break;

				case 2:
					if (priorty > 3) {
						status = 2;
						priorty = 3;
					}
					break;
				case 0:
					if (priorty > 4) {
						status = 0;
						priorty = 5;
						if (tmpLoanBorMain.getNextPayIntDate() < tbsdy) {
							dateUtil.init();
							dateUtil.setDate_1(tbsdy);
							dateUtil.setMons(-1);
							int payDate = dateUtil.getCalenderDay();
							if (tmpLoanBorMain.getNextPayIntDate() < payDate) { // 逾期超過一個月
								status = 4;
								priorty = 4;
							}
						}
					}
					break;

				case 5:
					if (priorty > 6) {
						status = 5;
						priorty = 6;
					}
					break;
				case 8:
					if (priorty > 7) {
						status = 8;
						priorty = 7;
					}
					break;
				case 9:
					if (priorty > 8) {
						status = 9;
						priorty = 8;
					}
					break;
				case 3:
					if (priorty > 9) {
						status = 3;
						priorty = 9;
					}
					break;
				case 1:
					if (priorty > 10) {
						status = 1;
						priorty = 10;
					}
					break;
				}

			}

		}
		return status;
	}
}
