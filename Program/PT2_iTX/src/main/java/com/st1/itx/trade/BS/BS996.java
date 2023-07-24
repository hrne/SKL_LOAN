package com.st1.itx.trade.BS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdPfParms;
import com.st1.itx.db.domain.CdPfParmsId;
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.domain.CdWorkMonthId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.PfBsDetail;
import com.st1.itx.db.domain.PfDetail;
import com.st1.itx.db.domain.PfItDetail;
import com.st1.itx.db.domain.PfReward;
import com.st1.itx.db.service.CdPfParmsService;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.PfBsDetailService;
import com.st1.itx.db.service.PfDetailService;
import com.st1.itx.db.service.PfItDetailService;
import com.st1.itx.db.service.PfRewardService;
import com.st1.itx.db.service.springjpa.cm.BS996ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.PfDetailCom;
import com.st1.itx.util.common.data.PfDetailVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

// 重新計算業績，更新業績明細檔PfItDetail、 PfBsDetail、PfReward業績計算欄
//
// 自動執行 :修改當工作月業績參數(設定業績特殊參數設定檔)，夜間批次自動執行
// 人工執行 : LC899 手動執行批次程式 
//   1.執行模式 :  2.Background CALL
//   2.執行程式 :  BS996
//   3.參數     :  業績起日,業績起日, 是否更新員工資料相關欄Y/N, 起戶號, 止戶號      ex.1090101,N,0,0 
//      
// 功能 :
//   1.自LoanBorTx放款交易內容檔，挑出會計日期>=業績起日的，重跑業績計算
//     1.撥款：計件代碼(LoanBorMain)。
//     2.部分償還 、提前結案：計件代碼(LoanBorMain)、已攤還期數(計算起日的相對期數+本筆回收期數)。
//     3.業績資料已人工調整，則不重算。
//   2.是否更新介紹人所屬資料欄(單位、區部、部室及處經理代號、區經理代號、部經理代號)
//     Y.以新的員工資料檔更新業績明細檔
//     H.業績明細檔介紹人所屬資料欄為空白時更新
//     N.不更新
//   3.先清除業績明細檔的業績計算欄，再更新
//

/**
 * 重新計算業績，更新業績明細檔<BR>
 * 
 * @author Lai
 * @version 1.0.0
 */
@Component("BS996")
@Scope("prototype")
public class BS996 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired

	public Parse parse;
	/* 日期工具 */
	@Autowired
	public DateUtil dDateUtil;

	@Autowired
	PfDetailService pfDetailService;

	@Autowired
	PfItDetailService pfItDetailService;

	@Autowired
	PfBsDetailService pfBsDetailService;

	@Autowired
	PfRewardService pfRewardService;

	@Autowired
	public LoanBorTxService loanBorTxService;

	@Autowired
	public LoanBorMainService loanBorMainService;

	@Autowired
	public CdPfParmsService cdPfParmsService;

	@Autowired
	public CdWorkMonthService cdWorkMonthService;

	@Autowired
	LoanCom loanCom;

	@Autowired
	PfDetailCom pfDetailCom;

	@Autowired
	public WebClient webClient;

	@Autowired
	public BS996ServiceImpl bs996ServiceImpl;

	private int commitCnt = 20;
	private int iAcdate = 0;
	private String iEmpResetFg = null;
	private int iCustNoS = 0;
	private int iCustNoE = 0;
	private int cntTrans = 0;
	private List<Map<String, String>> adjustList = null;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active BS996 ......");
		pfDetailCom.setTxBuffer(this.getTxBuffer());
		loanCom.setTxBuffer(this.getTxBuffer());
		// 業績起日, 是否更新員工資料相關欄Y/R/N, 起戶號, 止戶號
		//業績重算註記
		//Y:業績重算時以新員工資料更新介紹人所屬單位及介紹人、協辦是否為15日薪
		//R:資料轉換業績重算(以新員工資料更新介紹人、協辦是否為15日薪)
		//N:業績重算時不以新員工資料更新
		// Parm = 1090101,N,0,0
		String[] strAr = titaVo.getParam("Parm").split(",");
		List<String> strList = Arrays.asList(strAr);
		if (strList.size() != 4 || (!"Y".equals(strAr[1]) && !"R".equals(strAr[1]) && !"N".equals(strAr[1]))) {
			throw new LogicException(titaVo, "E0000", "參數：EX.1090402,Y,1,9999999( 業績起日,是否更新員工資料欄Y/R/N, 起戶號, 止戶號)");
		}
		iAcdate = this.parse.stringToInteger(strAr[0]); // 業績起日
		iEmpResetFg = strAr[1]; // 是否更新員工資料欄
		iCustNoS = this.parse.stringToInteger(strAr[2]); // 起戶號
		iCustNoE = this.parse.stringToInteger(strAr[3]); // 止戶號
		if (iCustNoS == 0 && iCustNoE == 0) {
			iCustNoE = 9999999;
		}
		// 自動重算 Parm = 0,N,0,0
		if (iAcdate == 0) {
			CdPfParms tCdPfParms = cdPfParmsService.findById(new CdPfParmsId("R", " ", " "), titaVo);
			if (tCdPfParms != null) {
				if (tCdPfParms.getWorkMonthEnd() == 0) {
					CdWorkMonth tCdWorkMonth = cdWorkMonthService.findById(new CdWorkMonthId(
							tCdPfParms.getWorkMonthStart() / 100, tCdPfParms.getWorkMonthStart() % 100), titaVo);
					if (tCdWorkMonth != null) {
						iAcdate = tCdWorkMonth.getStartDate();
						updatePf(titaVo);
						tCdPfParms = cdPfParmsService.holdById(tCdPfParms, titaVo);
						tCdPfParms.setWorkMonthEnd(tCdPfParms.getWorkMonthStart());
						try {
							cdPfParmsService.update(tCdPfParms, titaVo);
						} catch (DBException e) {
							throw new LogicException(titaVo, "E0007", "tCdPfParms " + e.getErrorMsg());
						}
					}
				}
			}
		} else {
			updatePf(titaVo);
			// 通知訊息
			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "N", "", "",
					"更新業績明細檔，筆數 = " + cntTrans, titaVo);
		}

		this.batchTransaction.commit();

		this.info(" BS996 END");

		// end
		return null;
	}

	private void updatePf(TitaVo titaVo) throws LogicException {
		this.info("iAcdate=" + iAcdate + " , iEmpResetFg=" + iEmpResetFg + ", iCustNoS=" + iCustNoS + ", iCustNoE="
				+ iCustNoE);
		// 抓取業績已做人工調整的戶號、額度
		try {
			adjustList = bs996ServiceImpl.findAdjustList(iAcdate + 19110000, titaVo);
		} catch (Exception e) {
			this.info("noToDoList" + e);
			throw new LogicException(titaVo, "E5004", "");
		}

		// 清除業績明細檔的業績計算欄
		clearPfDetail(iAcdate, iCustNoS, iCustNoE, titaVo);
		cntTrans = 0;
		// 重新計算業績，更新業績明細檔
		Slice<LoanBorTx> slLoanBorTx = loanBorTxService.findAcDateRange(iAcdate + 19110000, 99991231,
				Arrays.asList(new String[] { "0" }), 0, Integer.MAX_VALUE, titaVo);
		if (slLoanBorTx != null) {
			for (LoanBorTx tx : slLoanBorTx.getContent()) {
				if (tx.getCustNo() >= iCustNoS && tx.getCustNo() <= iCustNoE) {
					if (!checkAdjust(tx.getCustNo(), tx.getFacmNo(), titaVo)) {
						if (updatePfDetail(iAcdate, iEmpResetFg, tx, titaVo)) {
							cntTrans++;
							if (cntTrans % this.commitCnt == 0) {
								this.batchTransaction.commit();
							}
						}
					}
				}
			}
		}
	}

	private boolean updatePfDetail(int iAcdate, String iEmpResetFg, LoanBorTx tx, TitaVo titaVo) throws LogicException {
		int repayType = -1;

		if ("L3100".equals(tx.getTitaTxCd())) {
			repayType = 0; // 0.撥款
		}
		if ("L3200".equals(tx.getTitaTxCd()) && tx.getExtraRepay().compareTo(BigDecimal.ZERO) > 0) {
			repayType = 2; // 2.部分償還
		}
		if ("L3420".equals(tx.getTitaTxCd()) && tx.getExtraRepay().compareTo(BigDecimal.ZERO) > 0) {
			repayType = 3; // 3.提前結案
		}

		if (repayType == -1) {
			return false;
		}

		LoanBorMain ln = loanBorMainService.findById(new LoanBorMainId(tx.getCustNo(), tx.getFacmNo(), tx.getBormNo()),
				titaVo);
		if (ln == null) {
			this.info("LoanBorMain notfound " + tx.toString());
			return false;
		}

		PfDetailVo pf = new PfDetailVo();
		pf.setPerfDate(tx.getAcDate());
		pf.setCustNo(tx.getCustNo()); // 借款人戶號
		pf.setFacmNo(tx.getFacmNo()); // 額度編號
		pf.setBormNo(tx.getBormNo()); // 撥款序號
		pf.setBorxNo(tx.getBorxNo()); // 交易內容檔序號
		pf.setPieceCode(ln.getPieceCode()); // 計件代碼
		pf.setPieceCodeSecond(ln.getPieceCodeSecond()); // 計件代碼2
		pf.setDrawdownDate(ln.getDrawdownDate());// 撥款日期
		pf.setRepayType(repayType); // 還款類別 0.撥款 2.部分償還 3.提前結案
		pf.setEmpResetFg(iEmpResetFg); // 是否更新介紹人所屬單位資料欄Y/N
		switch (repayType) {
		case 0: // 撥款
			pf.setDrawdownAmt(ln.getDrawdownAmt());// 撥款金額
			pf.setPieceCodeSecondAmt(ln.getPieceCodeSecondAmt());// 計件代碼2金額
			pf.setRepaidPeriod(0); // 已攤還期數
			this.info("tx.getTitaTxCd()" + ln.getDrawdownDate() + ',' + iAcdate);
			break;
		case 2: // 部分償還
		case 3: // 提前結案
			pf.setDrawdownAmt(tx.getExtraRepay());// 追回金額
			pf.setRepaidPeriod(loanCom.getTermNo(2, ln.getFreqBase(), ln.getPayIntFreq(), ln.getSpecificDate(),
					ln.getSpecificDd(), tx.getIntEndDate())); // 已攤還期數
			break;
		}

		try {
			pfDetailCom.addDetail(pf, titaVo); // 產生業績明細
		} catch (LogicException e) {
			this.error(e.getErrorMsg());
		}
		return true;
	}

	private boolean checkAdjust(int custNo, int facmNo, TitaVo titaVo) throws LogicException {
		// 檢查該戶號、額度是否已人工調整
		if (adjustList != null) {
			for (Map<String, String> d : adjustList) {
				if (parse.stringToInteger(d.get("CustNo")) == custNo
						&& parse.stringToInteger(d.get("FacmNo")) == facmNo) {
					this.info("CheckAdjust ture CustNo=" + custNo + " FacmNo=" + facmNo);
					return true;
				}
			}
		}
		return false;
	}

	private void clearPfDetail(int iAcdate, int iCustNoS, int iCustNoE, TitaVo titaVo) throws LogicException {
		/* 業績明細計算檔 */
		Slice<PfDetail> slPfDetail = pfDetailService.findByPerfDate(iAcdate + 19110000, 99991231, 0, Integer.MAX_VALUE,
				titaVo);
		List<PfDetail> pfList = new ArrayList<PfDetail>();
		if (slPfDetail != null) {
			for (PfDetail pf : slPfDetail.getContent()) {
				if (pf.getCustNo() >= iCustNoS && pf.getCustNo() <= iCustNoE) {
					if (!checkAdjust(pf.getCustNo(), pf.getFacmNo(), titaVo)) {
						pfList.add(pf);
					}
				}
			}
			if (pfList.size() > 0) {
				try {
					pfDetailService.deleteAll(pfList, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "PfDetail delete " + e.getErrorMsg());
				}
			}
		}
		// 介紹人業績明細檔
		Slice<PfItDetail> slItDetail = pfItDetailService.findByPerfDate(iAcdate + 19110000, 99991231, 0,
				Integer.MAX_VALUE, titaVo);
		List<PfItDetail> itList = new ArrayList<PfItDetail>();
		if (slItDetail != null) {
			for (PfItDetail it : slItDetail.getContent()) {
				if (it.getCustNo() >= iCustNoS && it.getCustNo() <= iCustNoE) {
					if (!checkAdjust(it.getCustNo(), it.getFacmNo(), titaVo)) {
						it.setDrawdownAmt(BigDecimal.ZERO); // 撥款金額/追回金額
						it.setPerfCnt(BigDecimal.ZERO); // 件數
						it.setPerfEqAmt(BigDecimal.ZERO); // 換算業績
						it.setPerfReward(BigDecimal.ZERO); // 業務報酬
						it.setPerfAmt(BigDecimal.ZERO); // 業績金額
						itList.add(it);
					}
				}
			}
			if (itList.size() > 0) {
				try {
					pfItDetailService.updateAll(itList, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "PfItDetail update " + e.getErrorMsg());
				}
			}
		}

		this.batchTransaction.commit();

		// 房貸專員業績明細檔
		Slice<PfBsDetail> slbsDetail = pfBsDetailService.findByPerfDate(iAcdate + 19110000, 99991231, 0,
				Integer.MAX_VALUE, titaVo);
		List<PfBsDetail> bsList = new ArrayList<PfBsDetail>();
		if (slbsDetail != null) {
			for (PfBsDetail bs : slbsDetail.getContent()) {
				if (bs.getCustNo() >= iCustNoS && bs.getCustNo() <= iCustNoE) {
					if (!checkAdjust(bs.getCustNo(), bs.getFacmNo(), titaVo)) {
						bs.setDrawdownAmt(BigDecimal.ZERO); // 撥款金額/追回金額
						bs.setPerfCnt(BigDecimal.ZERO); // 件數
						bs.setPerfAmt(BigDecimal.ZERO); // 業績金額
						bsList.add(bs);
					}
				}
			}
			if (bsList.size() > 0) {
				try {
					pfBsDetailService.updateAll(bsList, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "PfBsDetail update " + e.getErrorMsg());
				}
			}
		}

		this.batchTransaction.commit();

		// 介紹、協辦獎金發放檔
		Slice<PfReward> slReward = pfRewardService.findByPerfDate(iAcdate + 19110000, 99991231, 0, Integer.MAX_VALUE,
				titaVo);
		List<PfReward> rwList = new ArrayList<PfReward>();
		if (slReward != null) {
			for (PfReward rw : slReward.getContent()) {
				if (rw.getCustNo() >= iCustNoS && rw.getCustNo() <= iCustNoE) {
					if (!checkAdjust(rw.getCustNo(), rw.getFacmNo(), titaVo)) {
						rw.setIntroducerBonus(BigDecimal.ZERO); // 介紹人介紹獎金
						rw.setIntroducerAddBonus(BigDecimal.ZERO); // 介紹人加碼獎勵津貼
						rw.setCoorgnizerBonus(BigDecimal.ZERO); // 協辦人員協辦獎金
						rwList.add(rw);
					}
				}
			}
			if (rwList.size() > 0) {
				try {
					pfRewardService.updateAll(rwList, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "PfReward update " + e.getErrorMsg());
				}
			}
		}

		this.batchTransaction.commit();

	}
}