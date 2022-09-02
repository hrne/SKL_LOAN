package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanRateChange;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanRateChangeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.CustRmkCom;
import com.st1.itx.util.common.LoanAvailableAmt;
import com.st1.itx.util.parse.Parse;

/*
 * L3001 放款明細資料查詢
 * a.此功能供查詢某一戶號下,其各筆額度及撥款資料.
 * b.有效額度筆數計算條件為:
 * b1.循環動用者: (核貸金額-已用額度)需>0且未超過循環動用止日
 * b2.非循環動用者: (核貸金額-已撥款金額)需>0且未超過動用期限
 * c.連結按鈕
 * c1.在某筆額度資料前可按[額度],以查閱該額度之明細資料.
 * c2.在某筆額度資料前可按[撥款],以查閱該額度下之各筆撥款資料.
 */

/**
 * L3001 放款明細資料查詢
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3001")
@Scope("prototype")
public class L3001 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public AcReceivableService acReceivableService;
	@Autowired
	LoanBorTxService sLoanBorTxService;
	@Autowired
	LoanRateChangeService sLoanRateChangeService;

	@Autowired
	Parse parse;
	@Autowired
	CustRmkCom custRmkCom;
	@Autowired
	LoanAvailableAmt loanAvailableAmt;

	String wkFacShareFg = "";
	BigDecimal wkFacShareAvailable = BigDecimal.ZERO;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3001 ");
		this.totaVo.init(titaVo);
		loanAvailableAmt.setTxBuffer(this.txBuffer);
		// 取得輸入資料
		int iCaseNo = this.parse.stringToInteger(titaVo.getParam("CaseNo"));
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		BigDecimal wkAvailable = BigDecimal.ZERO;
		// work area
		int wkFacmNoStart = 0;
		int wkFacmNoEnd = 0;
		int wkValidFacm = 0;
		BigDecimal totalLineAmt = BigDecimal.ZERO;
		BigDecimal totalUtilAmt = BigDecimal.ZERO;
		BigDecimal totalUtilBal = BigDecimal.ZERO;
		BigDecimal totalAvailable = BigDecimal.ZERO;

		String wkClShareFg = "";
		Slice<FacMain> slFacMain;
		List<FacMain> lFacMain;
		new ArrayList<ClFac>();
		this.info("L3001 getReturnIndex = " + titaVo.getReturnIndex());
		// 首次進入, 計算有效額度筆數
		if (titaVo.getReturnIndex() == 0) {
			this.index = titaVo.getReturnIndex();
			this.limit = Integer.MAX_VALUE;
			if (iCaseNo > 0) {
				FacMain tFacMain = facMainService.facmCreditSysNoFirst(iCaseNo, iCaseNo, 1, 999, titaVo);
				if (tFacMain == null) {
					throw new LogicException(titaVo, "E0001", "額度主檔 案件編號 = " + iCaseNo); // 查詢資料不存在
				}
				tFacMain.getCustNo();
				slFacMain = facMainService.facmCreditSysNoRange(iCaseNo, iCaseNo, 1, 999, this.index, this.limit,
						titaVo);
				lFacMain = slFacMain == null ? null : slFacMain.getContent();
			} else {
				if (iFacmNo > 0) {
					wkFacmNoStart = iFacmNo;
					wkFacmNoEnd = iFacmNo;
				} else {
					wkFacmNoStart = 1;
					wkFacmNoEnd = 999;
				}
				slFacMain = facMainService.facmCustNoRange(iCustNo, iCustNo, wkFacmNoStart, wkFacmNoEnd, this.index,
						this.limit, titaVo);
				lFacMain = slFacMain == null ? null : slFacMain.getContent();
			}
			if (lFacMain == null || lFacMain.size() == 0) {
				throw new LogicException(titaVo, "E0001", "額度主檔"); // 查詢資料不存在
			}
			for (FacMain tFacMain : lFacMain) {

				totalLineAmt = totalLineAmt.add(tFacMain.getLineAmt());// 合計核准額度
				totalUtilAmt = totalUtilAmt.add(tFacMain.getUtilAmt());// 合計目前餘額
				totalUtilBal = totalUtilBal.add(tFacMain.getUtilBal());// 合計已動用額度餘額
				if (tFacMain.getRecycleCode().equals("1")) {
					if (tFacMain.getRecycleDeadline() >= this.txBuffer.getTxCom().getTbsdy()) {
						totalAvailable = totalAvailable.add(loanAvailableAmt.caculate(tFacMain, titaVo)); // 合計可用額度
					}
				} else {
					if (tFacMain.getUtilDeadline() >= this.txBuffer.getTxCom().getTbsdy()) {
						totalAvailable = totalAvailable.add(loanAvailableAmt.caculate(tFacMain, titaVo)); // 合計可用額度
					}
				}

				if (tFacMain.getLineAmt().compareTo(tFacMain.getUtilBal()) == 1) {
					switch (tFacMain.getRecycleCode()) {
					case "0":
						if (tFacMain.getUtilDeadline() >= this.txBuffer.getTxCom().getTbsdy()) {
							wkValidFacm++;
						}
						break;
					case "1":
						if (tFacMain.getRecycleDeadline() >= this.txBuffer.getTxCom().getTbsdy()) {
							wkValidFacm++;
						}
						break;
					}
				}
			}
			titaVo.putParam("ValidFacm", wkValidFacm);
			this.totaVo.putParam("OValidFacm", wkValidFacm);
			this.totaVo.putParam("OLineAmt", totalLineAmt);
			this.totaVo.putParam("OUtilAmt", totalUtilAmt);
			this.totaVo.putParam("OUtilBal", totalUtilBal);
			this.totaVo.putParam("OAvailable", totalAvailable);
			this.info("L3001 OValidFacm = " + wkValidFacm);
			this.info("L3001 OLineAmt = " + totalLineAmt);
			this.info("L3001 OUtilAmt = " + totalUtilAmt);
			this.info("L3001 OUtilBal = " + totalUtilBal);
			this.info("L3001 OAvailable = " + totalAvailable);
		}

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 3 + 75 * 600 = 45003

		// 查詢額度主檔
		if (iCaseNo > 0) {
			FacMain tFacMain = facMainService.facmCreditSysNoFirst(iCaseNo, iCaseNo, 1, 999, titaVo);
			if (tFacMain == null) {
				throw new LogicException(titaVo, "E0001", "額度主檔 案件編號 = " + iCaseNo); // 查詢資料不存在
			}
			slFacMain = facMainService.facmCreditSysNoRange(iCaseNo, iCaseNo, 1, 999, this.index, this.limit, titaVo);
			lFacMain = slFacMain == null ? null : slFacMain.getContent();
		} else {
			if (iFacmNo > 0) {
				wkFacmNoStart = iFacmNo;
				wkFacmNoEnd = iFacmNo;
			} else {
				wkFacmNoStart = 1;
				wkFacmNoEnd = 999;
			}
			slFacMain = facMainService.facmCustNoRange(iCustNo, iCustNo, wkFacmNoStart, wkFacmNoEnd, this.index,
					this.limit, titaVo);
			lFacMain = slFacMain == null ? null : slFacMain.getContent();
		}

		if (lFacMain == null || lFacMain.size() == 0) {
			throw new LogicException(titaVo, "E0001", "額度主檔"); // 查詢資料不存在
		}
		
		Slice<AcReceivable> slAcReceivable = acReceivableService.acrvFacmNoRange(0, iCustNo, 0, 0, 999, this.index,
				Integer.MAX_VALUE, titaVo); // 銷帳記號 0-未銷, 業務科目記號 0: 一般科目
		
		// 如有有找到資料
		for (FacMain tFacMain : lFacMain) {
			int deadline = 0;
			// 判斷循環非循環日期
			if (tFacMain.getRecycleCode().equals("1")) {
				deadline = tFacMain.getRecycleDeadline();
			} else {
				deadline = tFacMain.getUtilDeadline();
			}

			wkAvailable = loanAvailableAmt.caculate(tFacMain, titaVo); // 可用額度
			wkClShareFg = loanAvailableAmt.getClShareFlag();
			// 動支期限循環動用止日到期時顯示0
			if (deadline < this.txBuffer.getTxCom().getTbsdy()) {
				wkAvailable = BigDecimal.ZERO;
			}

			// 1. 當有合併額度控管時，「共用額度」欄顯示[限額]按鈕，連結L2118額度合併控管登錄-查詢
			wkFacShareFg = loanAvailableAmt.getFacShareFlag();
			wkFacShareAvailable = loanAvailableAmt.getAvailableShare();
			OccursList occursList = new OccursList();
			occursList.putParam("OOCaseNo", tFacMain.getCreditSysNo());
			occursList.putParam("OOCustNo", tFacMain.getCustNo());
			occursList.putParam("OOFacmNo", tFacMain.getFacmNo());
			occursList.putParam("OOApplNo", tFacMain.getApplNo());

			occursList.putParam("OODeadline", deadline);
			occursList.putParam("OOCurrencyCode", tFacMain.getCurrencyCode());
			occursList.putParam("OOLineAmt", tFacMain.getLineAmt());
			occursList.putParam("OOUtilAmt", tFacMain.getUtilAmt());
			occursList.putParam("OOUtilBal", tFacMain.getUtilBal());
			occursList.putParam("OOAvailable", wkAvailable); // 可用額度
			occursList.putParam("OOShareFacFg", wkFacShareFg); // 合併額度控管記號
			occursList.putParam("OOClShareFg", wkClShareFg); // 擔保品配額記號
			occursList.putParam("OOLastBormNo", tFacMain.getLastBormNo()); // 已撥款序號
			
			// 檢查是否有【交易】按鈕
			// 邏輯同 L3005
			
			// acdate: 本月第一天
			int acDate = (titaVo.getEntDyI() + 19110000) / 100 * 100 + 1;
			
			Slice<LoanBorTx> slLoanBorTx = sLoanBorTxService.borxAcDateRange(iCustNo, tFacMain.getFacmNo(), tFacMain.getFacmNo(), 0,
					999, acDate, 99991231, Arrays.asList("Y", "I", "A", "F"), this.index, 1, titaVo);
			List<LoanBorTx> lLoanBorTx = slLoanBorTx != null ? slLoanBorTx.getContent() : null;
			
			occursList.putParam("OOHasL3005", lLoanBorTx != null && !lLoanBorTx.isEmpty() ? "Y" : "N");
			
			// 檢查是否有【繳息】按鈕
			// 邏輯同 L3911
			
			slLoanBorTx = sLoanBorTxService.borxIntEndDateDescRange(iCustNo, tFacMain.getFacmNo(), tFacMain.getFacmNo(),
					0, 999, 19110101, titaVo.getEntDyI() + 19110000, Arrays.asList("Y", "I", "F"),
					this.index, this.limit, titaVo);
			
			lLoanBorTx = slLoanBorTx != null ? slLoanBorTx.getContent() : null;
			
			occursList.putParam("OOHasL3911", lLoanBorTx != null && !lLoanBorTx.isEmpty() ? "Y" : "N");
			
			// 檢查是否有【利率】按鈕
			// 邏輯同 L3932
			
			Slice<LoanRateChange> slLoanRateChange = sLoanRateChangeService.rateChangeFacmNoRange(iCustNo, tFacMain.getFacmNo(), tFacMain.getFacmNo(), 0, 999, 0, 99991231, this.index,
					1, titaVo);
			
			List<LoanRateChange> lLoanRateChange = slLoanRateChange != null ? slLoanRateChange.getContent() : null;
			
			occursList.putParam("OOHasL3932", lLoanRateChange != null && !lLoanRateChange.isEmpty() ? "Y" : "N");
			
			BigDecimal tavAmt = BigDecimal.ZERO; 
			BigDecimal tamAmt = BigDecimal.ZERO; 
			if (slAcReceivable !=null) {
				for (AcReceivable rv : slAcReceivable.getContent()) {
					if ("TAV".equals(rv.getAcctCode()) && rv.getFacmNo() == tFacMain.getFacmNo()) {
						tavAmt = tavAmt.add(rv.getRvBal());
					}
					if ("TAM".equals(rv.getAcctCode()) && rv.getFacmNo() == tFacMain.getFacmNo()) {
						tamAmt = tamAmt.add(rv.getRvBal());
					}
				}
			}
			
			occursList.putParam("OOTavAmt", tavAmt); // 暫收可抵繳
			occursList.putParam("OOTamAmt", tamAmt);  // 暫收款AML戶
		
			this.totaVo.addOccursList(occursList);
		}

		// 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可
		if (slFacMain != null && slFacMain.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		// 顧客控管警訊通知訊息
		if (titaVo.getReturnIndex() == 0) {
//			custRmkCom.getCustRmk(titaVo, iCustNo);
//			this.addAllList(custRmkCom.getCustRmk(titaVo, iCustNo));
		}
		// end
		this.addList(this.totaVo);
		return this.sendList();

	}
}