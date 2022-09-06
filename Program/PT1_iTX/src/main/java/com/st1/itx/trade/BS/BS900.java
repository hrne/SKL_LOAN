package com.st1.itx.trade.BS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.AcLoanInt;
import com.st1.itx.db.domain.AcLoanIntId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.AcLoanIntService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcBookCom;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.GSeqCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("BS900")
@Scope("prototype")
/**
 * 月底提存計息<br>
 * 執行時機：月底日放款(LC700)<br>
 * 
 * 1.逐筆放款計息，寫入提息明細檔<br>
 * 2.新增應處理明細－應收利息提存入帳<br>
 * 2.1 迴轉上月，傳票批號=98<br>
 * 2.2 本月提存，傳票批號=99<br>
 * 3.發動報表LM008應收利息明細表、LM009應收利息總表
 * 
 * @author LAI
 * @version 1.0.0
 */
public class BS900 extends TradeBuffer {
	@Autowired
	public Parse parse;

	@Autowired
	WebClient webClient;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	private LoanBorMainService loanBorMainService;

	@Autowired
	private AcLoanIntService acLoanIntService;

	@Autowired
	private TxToDoDetailService txToDoDetailService;

	@Autowired
	private AcDetailService acDetailService;

	@Autowired
	private BaTxCom baTxCom;

	@Autowired
	private TxToDoCom txToDoCom;

	@Autowired
	private AcBookCom acBookCom;

	@Autowired
	LoanCom loanCom;

	@Autowired
	private GSeqCom gSeqCom;

	private int commitCnt = 200;
	private int iAcDate;
	private int iAcDateReverse = 0;
	private List<TxToDoDetail> lTxToDoDetail = new ArrayList<TxToDoDetail>();;
	private List<AcDetail> lAcDetail = new ArrayList<AcDetail>();;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("BS900 ......");
		this.totaVo.init(titaVo);
		int yearMonth = this.getTxBuffer().getMgBizDate().getTbsDyf() / 100; // 提息年月
		txToDoCom.setTxBuffer(this.getTxBuffer());
		baTxCom.setTxBuffer(this.getTxBuffer());
		iAcDate = this.getTxBuffer().getMgBizDate().getTmnDy();
		// 迴轉上個月底日
		iAcDateReverse = this.getTxBuffer().getMgBizDate().getLmnDy();
		this.info("iAcDateReverse=" + iAcDateReverse);
		// 1.刪除當月提息明細檔 //
		this.info("1.bs900 delete acLoanint, yearMonth=" + yearMonth);

		Slice<AcLoanInt> slAcLoanInt = acLoanIntService.findYearMonthEq(yearMonth, this.index, Integer.MAX_VALUE, titaVo);
		List<AcLoanInt> lAcLoanInt = slAcLoanInt == null ? null : slAcLoanInt.getContent();
		if (lAcLoanInt != null) {
			try {
				acLoanIntService.deleteAll(lAcLoanInt, titaVo);
			} catch (DBException e) {
				e.printStackTrace();
				throw new LogicException(titaVo, "E0008", "AcLoanInt delete " + e.getErrorMsg());
			}
		}

		this.batchTransaction.commit();

		// 2.刪除處理清單檔 ACCL01-應收利息提存入帳 //
		this.info("2.bs900 delete ACCL01");
		Slice<TxToDoDetail> slTxToDoDetail = txToDoDetailService.detailStatusRange("ACCL01", 0, 3, this.index, Integer.MAX_VALUE, titaVo);
		lTxToDoDetail = slTxToDoDetail == null ? null : slTxToDoDetail.getContent();
		if (lTxToDoDetail != null) {
			txToDoCom.delByDetailList(lTxToDoDetail, titaVo);
		}

		this.batchTransaction.commit();

		// 刪除提存當月AcDetal:98
		this.info("2-1.bs900 delete AcDetal-98, yearMonth=" + yearMonth);
		Slice<AcDetail> slAcDetail = acDetailService.findL9RptData(iAcDate + 19110000, 98, this.index, Integer.MAX_VALUE, titaVo);
		if (slAcDetail != null) {
			lAcDetail = new ArrayList<AcDetail>();
			lAcDetail = slAcDetail == null ? null : slAcDetail.getContent();
			try {
				acDetailService.deleteAll(lAcDetail, titaVo); // delete AcDetail
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", "AcDetail delete " + e.getErrorMsg());
			}
		}
		// 刪除提存當月AcDetal:99
		this.info("2-2.bs900 delete AcDetal-99, yearMonth=" + yearMonth);
		Slice<AcDetail> tlAcDetail = acDetailService.findL9RptData(iAcDate + 19110000, 99, this.index, Integer.MAX_VALUE, titaVo);
		if (tlAcDetail != null) {
			lAcDetail = new ArrayList<AcDetail>();
			lAcDetail = tlAcDetail == null ? null : tlAcDetail.getContent();
			try {
				acDetailService.deleteAll(lAcDetail, titaVo); // delete AcDetail
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", "AcDetail delete " + e.getErrorMsg());
			}
		}

		lTxToDoDetail = new ArrayList<TxToDoDetail>();

		// 3.迴轉上月
		this.info("3.bs900 last month ACCL01");
		slAcDetail = acDetailService.findL9RptData(iAcDateReverse + 19110000, 99, this.index, Integer.MAX_VALUE, titaVo);
		if (slAcDetail != null) {
			for (AcDetail t : slAcDetail.getContent()) {
				if ("ICR".equals(t.getAcctCode()) && t.getEntAc() == 1) {
					continue;
				}
				// 銷帳碼=原銷帳碼
				String rvNo = t.getRvNo();
				// 無銷帳碼則自編
				if (rvNo.trim().isEmpty()) {
					rvNo = getRvNo(titaVo);
				}
				TxToDoDetail tTxToDoDetail = new TxToDoDetail();
				tTxToDoDetail.setItemCode("ACCL01");
				tTxToDoDetail.setDtlValue(rvNo);
				// 迴轉上月(傳票批號:98)
				// 借：應收利息 貸：利息收入
				TempVo tTempVo = new TempVo();
				tTempVo.clear();
				tTempVo.putParam("AcDate", iAcDate);
				tTempVo.putParam("SlipBatNo", "98");
				tTempVo.putParam("AcclType", "迴轉上月");
				tTempVo.putParam("AcctCode", t.getAcctCode());
				tTempVo.putParam("AcBookCode", t.getAcBookCode());
				tTempVo.putParam("AcSubBookCode", t.getAcSubBookCode());
				tTempVo.putParam("SlipNote", t.getSlipNote());
				tTempVo.putParam("CrAcctCode1", "ICR");
				tTempVo.putParam("CrRvNo1", rvNo);
				tTempVo.putParam("CrTxAmt1", t.getTxAmt());
				tTempVo.putParam("DbAcctCode1", t.getAcctCode());
				tTempVo.putParam("DbTxAmt1", t.getTxAmt());
				tTxToDoDetail.setProcessNote(tTempVo.getJsonString());
				lTxToDoDetail.add(tTxToDoDetail);
			}
			this.info("3.last month ACCL01 " + lTxToDoDetail.size());
			// 應處理明細檔
			if (lTxToDoDetail != null && lTxToDoDetail.size() > 0) {
				txToDoCom.addByDetailList(false, 0, lTxToDoDetail, titaVo); // DupSkip = false ->重複 error
			}
		}

		lTxToDoDetail = new ArrayList<TxToDoDetail>();
		// 4.提息並寫入提息明細檔，計算止日 =>下個月1日
		this.info("3.bs900 acLoanInt");
		dateUtil.init();
		dateUtil.setDate_1(this.getTxBuffer().getMgBizDate().getTbsDy());
		dateUtil.setMons(1);
		int intEndDate = (dateUtil.getCalenderDay() / 100) * 100 + 01; // 計算止日 =>下個月1日
		int iEntryDate = this.getTxBuffer().getMgBizDate().getTmnDy(); // 入帳日 ==> 月底日曆日

		procAcLoanint(yearMonth, iEntryDate, intEndDate, titaVo);

		this.batchTransaction.commit();

		// 5.寫入應處理清單
		this.info("5.bs900 TxToDo ThisMonth");
		procTxToDo(yearMonth, titaVo);

		// 應處理明細檔
		if (lTxToDoDetail != null && lTxToDoDetail.size() > 0) {
			txToDoCom.addByDetailList(false, 0, lTxToDoDetail, titaVo); // DupSkip = false ->重複 error
		}

		// END

		// end
		this.batchTransaction.commit();

		// 3.發動報表LM008應收利息明細表、LM009應收利息總表
		titaVo.setBatchJobId("jLM008;jLM009");

		this.addList(this.totaVo);

		this.info("bs900 process end");

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "", "", "BS900已完成", titaVo);

		return this.sendList();
	}

	/* 寫入應處理清單 ACCL01-各項提存作業，寫入新提存利息 */
	private void procTxToDo(int yearMonth, TitaVo titaVo) throws LogicException {
		HashMap<String, BigDecimal> map = new HashMap<>();

		// AcReceivable 會計銷帳檔，ICR 應收利息－放款部 ，未銷//
		Slice<AcLoanInt> slAcLoanInt = acLoanIntService.findYearMonthEq(yearMonth, this.index, Integer.MAX_VALUE, titaVo); // ASC
		List<AcLoanInt> lAcLoanInt = slAcLoanInt == null ? null : slAcLoanInt.getContent();
		if (lAcLoanInt != null) {
			for (AcLoanInt ac : lAcLoanInt) {
				if (ac.getInterest().compareTo(BigDecimal.ZERO) > 0) {
					String key = ac.getAcctCode() + "," + parse.IntegerToString(ac.getAging(), 1) + "," + ac.getAcBookCode() + "," + ac.getAcSubBookCode();
					if (map.containsKey(key)) {
						map.put(key, map.get(key).add(ac.getInterest()));
					} else {
						map.put(key, ac.getInterest());
					}
				}
			}
			for (Map.Entry<String, BigDecimal> entry : map.entrySet()) {
				String[] keyArray = entry.getKey().split(",");
				addTxToDoThisMonth(keyArray[0], keyArray[1], keyArray[2], keyArray[3], entry.getValue(), titaVo);
				this.info("bs900 TxToDo =" + entry.getKey() + ", Interest=" + entry.getValue());
			}
		}
	}

	private String getRvNo(TitaVo titaVo) throws LogicException {
		// 銷帳編號：AC+民國年後兩碼+流水號六碼
		String rvNo = "AC" + parse.IntegerToString(this.getTxBuffer().getMgBizDate().getTbsDyf() / 10000, 4).substring(2, 4)
				+ parse.IntegerToString(gSeqCom.getSeqNo(this.getTxBuffer().getMgBizDate().getTbsDy(), 1, "L6", "RvNo", 999999, titaVo), 6);
		return rvNo;
	}

	private void addTxToDoThisMonth(String acctCode, String aging, String acBookCode, String acSubBookCode, BigDecimal intAmt, TitaVo titaVo) throws LogicException {

		// 銷帳碼(15)=年月(5)-業務科目(3)-帳齡(1)-區隔帳冊(3)
		String rvNo = iAcDate / 100 + acctCode + "-" + aging + "-" + acSubBookCode;
		TxToDoDetail tTxToDoDetail = new TxToDoDetail();
		tTxToDoDetail.setItemCode("ACCL01");
		tTxToDoDetail.setDtlValue(rvNo);
		// 借：應收利息 貸：利息收入
		TempVo tTempVo = new TempVo();
		tTempVo.clear();
		tTempVo.putParam("AcDate", iAcDate);
		tTempVo.putParam("SlipBatNo", "99");// 提存(傳票批號:99)
		tTempVo.putParam("AcclType", "利息提存");
		tTempVo.putParam("AcctCode", acctCode);
		tTempVo.putParam("SlipNote", gettingSlipNote(aging));
		tTempVo.putParam("AcBookCode", acBookCode);
		tTempVo.putParam("AcSubBookCode", acSubBookCode);
		tTempVo.putParam("DbAcctCode1", "ICR");
		tTempVo.putParam("DbRvNo1", rvNo);
		tTempVo.putParam("DbTxAmt1", intAmt);
		tTempVo.putParam("CrAcctCode1", acctCode);
		tTempVo.putParam("CrTxAmt1", intAmt);
		tTxToDoDetail.setProcessNote(tTempVo.getJsonString());
		lTxToDoDetail.add(tTxToDoDetail);
	}

	private String gettingSlipNote(String aging) {
		// Aging 帳齡 0.一個月以下 1.一～三個月 2.三～六個月3.六個月以上
		String slipNote;
		if ("0".equals(aging))
			slipNote = "帳齡一個月以下";
		else if ("1".equals(aging))
			slipNote = "帳齡一~三個月";

		else if ("2".equals(aging))
			slipNote = "三~六個月";
		else if ("3".equals(aging))
			slipNote = "六個月以上";
		else
			slipNote = "";
		return slipNote;
	}

	private void procAcLoanint(int yearMonth, int iEntryDate, int intEndDate, TitaVo titaVo) throws LogicException {
		this.info("bs900 procAcLoanint, yearMonth=" + yearMonth + ", intDate=" + intEndDate);
		int last1MonthDate = 0;
		int last3MonthDate = 0;
		int last6MonthDate = 0;

		dateUtil.init();
		dateUtil.setDate_1(intEndDate);
		dateUtil.setMons(-1);
		last1MonthDate = dateUtil.getCalenderDay();
		this.info("last1MonthDate = " + last1MonthDate);

		dateUtil.init();
		dateUtil.setDate_1(intEndDate);
		dateUtil.setMons(-3);
		last3MonthDate = dateUtil.getCalenderDay();
		this.info("last3MonthDate = " + last3MonthDate);

		dateUtil.init();
		dateUtil.setDate_1(intEndDate);
		dateUtil.setMons(-6);
		last6MonthDate = dateUtil.getCalenderDay();
		this.info("last6MonthDate = " + last6MonthDate);

		baTxCom.setTxBuffer(this.getTxBuffer());

		ArrayList<BaTxVo> lBaTxVo = new ArrayList<BaTxVo>();
		ArrayList<AcLoanInt> lAcLoanInt = new ArrayList<AcLoanInt>();
		int termNo = 0;
		String acBookCode = null;
		String acSubBookCode = null;
		int custNo = 0;
		// find all loanBorMain status = 0 //
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.nextPayIntDateRange(0, 99999999, 0, this.index, Integer.MAX_VALUE, titaVo);
		List<LoanBorMain> lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();
		if (lLoanBorMain != null) {
			int cntTrans = 0;
			// 逐筆 call BaTxCom 計算並寫入提息明細檔 //
			for (LoanBorMain ln : lLoanBorMain) {
				try {
					lBaTxVo = baTxCom.acLoanInt(iEntryDate, intEndDate, ln.getCustNo(), ln.getFacmNo(), ln.getBormNo(), titaVo); // 提存
				} catch (LogicException e) {
					this.error("ErrorMsg :" + e.getErrorMsg(titaVo) + " " + ln.getCustNo() + "-" + ln.getFacmNo() + "-" + ln.getBormNo());
					continue;
				}
				//
				// 以上次應繳日與月底日的月差判斷 0.一個月以下 1.一~三個月 2.三~六個月3.六個月以上
				// 2022/1 月底提存 , last1MonthDate=2022/01/01 , last3MonthDate=2022/11/01,
				// last6MonthDate = 2022/08/01
				// 上次應繳日 >= 2022/01/01 => 0.一個月以下 , 2022/01 ...
				// 上次應繳日 >= 2022/11/01 => 1.一~三個月 , 2022/11, 2021/12
				// 上次應繳日 >= 2022/08/01 => 2.三~六個月 , 2021/08, 2021/09, 2021/10
				// 上次應繳日 < 2022/08/01 => 3.六個月以上 , 2021/07 ...

				int aging = 0;
				if (ln.getPrevPayIntDate() >= last1MonthDate) {
					aging = 0;
				} else if (ln.getPrevPayIntDate() >= last3MonthDate) {
					aging = 1;
				} else if (ln.getPrevPayIntDate() >= last6MonthDate) {
					aging = 2;
				} else {
					aging = 3;
				}

				// 帳冊別、區隔帳冊
				if (ln.getCustNo() != custNo) {
					custNo = ln.getCustNo();
					String[] strAr = acBookCom.getAcBookCode(custNo, titaVo);
					acBookCode = strAr[0];
					acSubBookCode = strAr[1];
				}

				termNo = 0; // 期數編號需重算，因月底最後一段期數與前一段相同
				if (lBaTxVo != null) {
					for (BaTxVo ba : lBaTxVo) {
						AcLoanInt tAcLoanInt = new AcLoanInt();
						AcLoanIntId tAcLoanIntId = new AcLoanIntId();
						tAcLoanIntId.setYearMonth(yearMonth); // 提息年月
						tAcLoanIntId.setCustNo(ba.getCustNo()); // 借款人戶號
						tAcLoanIntId.setFacmNo(ba.getFacmNo()); // 額度編號
						tAcLoanIntId.setBormNo(ba.getBormNo()); // 撥款序號
						termNo++;
						// tAcLoanIntId.setTermNo(ba.getPaidTerms()); // 期數編號
						tAcLoanIntId.setTermNo(termNo); // 期數編號
						tAcLoanInt.setAcLoanIntId(tAcLoanIntId);
						tAcLoanInt.setIntStartDate(ba.getIntStartDate()); // 計息起日
						tAcLoanInt.setIntEndDate(ba.getIntEndDate()); // 計息止日
						tAcLoanInt.setAmount(ba.getAmount()); // 計息本金
						tAcLoanInt.setIntRate(ba.getIntRate()); // 計息利率
						tAcLoanInt.setPrincipal(ba.getPrincipal()); // 回收本金
						tAcLoanInt.setInterest(ba.getInterest()); // 利息
						tAcLoanInt.setDelayInt(ba.getDelayInt()); // 延滯息
						tAcLoanInt.setBreachAmt(ba.getBreachAmt()); // 違約金
						tAcLoanInt.setRateIncr(ba.getRateIncr()); // 加碼利率
						tAcLoanInt.setIndividualIncr(ba.getIndividualIncr()); // 個別加碼利率
						if ("3".equals(ba.getAcctCode().substring(0, 1))) {
							tAcLoanInt.setAcctCode(loanCom.setIntAcctCode(ba.getAcctCode())); //
						} else {
							tAcLoanInt.setAcctCode(ba.getAcctCode()); //
						}
						tAcLoanInt.setPayIntDate(ba.getPayIntDate()); // 應繳息日
						tAcLoanInt.setLoanBal(ba.getLoanBal()); // 放款餘額
						tAcLoanInt.setAging(aging); // 帳齡
						tAcLoanInt.setAcBookCode(acBookCode); // 帳冊別
						tAcLoanInt.setAcSubBookCode(acSubBookCode); // 區隔帳冊
						tAcLoanInt.setBranchNo(ln.getBranchNo());
						lAcLoanInt.add(tAcLoanInt);
						cntTrans++;
						if (cntTrans > this.commitCnt) {
							try {
								acLoanIntService.insertAll(lAcLoanInt, titaVo);
							} catch (DBException e) {
								e.printStackTrace();
								throw new LogicException(titaVo, "E0005", "AcLoanInt insert " + e.getErrorMsg());
							}
							lAcLoanInt = new ArrayList<AcLoanInt>();
							cntTrans = 0;
							this.batchTransaction.commit();
						}
					}
				}
			}
			if (lAcLoanInt.size() > 0) {
				try {
					acLoanIntService.insertAll(lAcLoanInt, titaVo);
				} catch (DBException e) {
					e.printStackTrace();
					throw new LogicException(titaVo, "E0005", "AcLoanInt insert " + e.getErrorMsg());
				}
			}
		}
	}
}