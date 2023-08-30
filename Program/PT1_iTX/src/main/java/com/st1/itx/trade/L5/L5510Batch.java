package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.domain.CdWorkMonthId;
import com.st1.itx.db.domain.PfInsCheck;
import com.st1.itx.db.domain.PfItDetail;
import com.st1.itx.db.domain.TxControl;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.db.service.PfItDetailService;
import com.st1.itx.db.service.TxControlService;
import com.st1.itx.db.service.springjpa.cm.L5510ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.PfCheckInsuranceCom;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

/**
 * 產生換算業績、業務報酬發放檔 call by L5510
 * 
 * @author st1
 *
 */
@Service("L5510Batch")
@Scope("prototype")

public class L5510Batch extends TradeBuffer {

	@Autowired
	public TxControlService txControlService;
	@Autowired
	public PfItDetailService pfItDetailService;
	@Autowired
	CdWorkMonthService cdWorkMonthService;
	@Autowired
	public Parse parse;
	@Autowired
	public DateUtil dateUtil;
	@Autowired
	public PfCheckInsuranceCom pfCheckInsuranceCom;
	@Autowired
	public DataLog dataLog;
	@Autowired
	public WebClient webClient;
	@Autowired
	public MakeFile makeFile;
	@Autowired
	public MakeExcel makeExcel;
	@Autowired
	public CdEmpService cdEmpService;

	@Autowired
	private L5510ServiceImpl l5510ServiceImpl;

	@Autowired
	DateUtil dDateUtil;

	private int iWorkMonth = 0; // 西元
	private int plusCnt = 0; // 正業績
	private int minusCnt;// 負業績
	private int iWorkMonthS = 0; // 西元

	private ArrayList<PfInsCheck> lPfInsCheck = new ArrayList<PfInsCheck>();// 房貸獎勵保費檢核檔.xlsx

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5510Batch ");
		this.totaVo.init(titaVo);

		String iFunCode = titaVo.getParam("FunCode").trim();// 使用功能
		iWorkMonth = parse.stringToInteger(titaVo.getParam("WorkMonth")) + 191100;
		iWorkMonthS =  parse.stringToInteger(titaVo.getParam("Months")) + 191100;
		if ("1".equals(iFunCode)) {
			toCheck(titaVo);
		} else if ("2".equals(iFunCode)) {
			makeMedia(titaVo);
		} else if ("3".equals(iFunCode)) {
			cancelMedia(titaVo);
		}

		return null;
	}

	// 房貸獎勵保費檢核
	private void toCheck(TitaVo titaVo) throws LogicException {
		this.info("active L5510Batch.toCheck ");

		// 刪除本月保費檢核追回資料(重新執行用)
		Slice<PfItDetail> slPfItDetail = pfItDetailService.findByWorkMonth(iWorkMonth, iWorkMonth, 0, Integer.MAX_VALUE,
				titaVo);
		if (slPfItDetail != null) {
			for (PfItDetail pfIt : slPfItDetail.getContent()) {
				if (pfIt.getWorkMonth() == iWorkMonth && pfIt.getRepayType() == 5) {
					deletePfItDetail(pfIt, titaVo);
				}
			}
		}
		this.batchTransaction.commit();

		int custNo = 0;
		int facmNo = 0;


		slPfItDetail = pfItDetailService.findByWorkMonth(iWorkMonthS, iWorkMonth, 0, Integer.MAX_VALUE);

		ArrayList<PfItDetail> lPfItDetail = new ArrayList<PfItDetail>(); // 額度業績

		if (slPfItDetail != null) {
			for (PfItDetail pfIt : slPfItDetail.getContent()) {
				// 排除業績金額為零
				if (pfIt.getPerfEqAmt().compareTo(BigDecimal.ZERO) == 0
						&& pfIt.getPerfReward().compareTo(BigDecimal.ZERO) == 0) {
					continue;
				}
				lPfItDetail.add(pfIt);
			}
		}

		this.info("L5510.1 lPfItDetail.size = " + lPfItDetail.size());

		if (lPfItDetail.size() > 0) {
			plusCnt = lPfItDetail.size();
			ArrayList<PfItDetail> lPfFac = new ArrayList<PfItDetail>(); // 額度業績
			custNo = slPfItDetail.getContent().get(0).getCustNo();
			facmNo = slPfItDetail.getContent().get(0).getFacmNo();
			for (PfItDetail pfIt : lPfItDetail) {
				// 額度不同則執行房貸獎勵保費檢核、產生發放媒體
				if (pfIt.getCustNo() != custNo || pfIt.getFacmNo() != facmNo) {
					calculate(custNo, facmNo, lPfFac, titaVo);
					custNo = pfIt.getCustNo();
					facmNo = pfIt.getFacmNo();
					lPfFac = new ArrayList<PfItDetail>();
					this.batchTransaction.commit();
				}
				lPfFac.add(pfIt);
			}
			calculate(custNo, facmNo, lPfFac, titaVo);
		}

		this.batchTransaction.commit();

		this.info("lPfInsCheck size=" + lPfInsCheck.size());
		if (lPfInsCheck.size() > 0) {
			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5510.1", "房貸獎勵保費檢核檔(業績報酬)",
					"房貸獎勵保費檢核檔(業績報酬)", "業績報酬");

			int row = 1;
			makeExcel.setValue(row, 1, "戶號");
			makeExcel.setValue(row, 2, "額度");
			makeExcel.setValue(row, 3, "eLoan案件編號");
			makeExcel.setValue(row, 4, "借款人身份證字號");
			makeExcel.setValue(row, 5, "借款書申請日");
			makeExcel.setValue(row, 6, "承保日");
			makeExcel.setValue(row, 7, "保單號碼");
			makeExcel.setValue(row, 8, "檢核結果");
			makeExcel.setValue(row, 9, "檢核工作月");
			for (PfInsCheck pf : lPfInsCheck) {
				this.info("lPfInsCheck =" + pf.toString());
				this.info("lPfInsCheck2 =" + pf.getCustNo() + "/" + pf.getFacmNo());
				row++;
				makeExcel.setValue(row, 1, String.format("%07d", pf.getCustNo()));
				makeExcel.setValue(row, 2, String.format("%03d", pf.getFacmNo()));
				makeExcel.setValue(row, 3, pf.getCreditSysNo());
				makeExcel.setValue(row, 4, pf.getCustId());
				makeExcel.setValue(row, 5, pf.getApplDate());
				makeExcel.setValue(row, 6, pf.getInsDate());
				makeExcel.setValue(row, 7, pf.getInsNo());
				makeExcel.setValue(row, 8, pf.getCheckResult());
				makeExcel.setValue(row, 9, String.format("%05d", pf.getCheckWorkMonth() - 191100));
			}
			makeExcel.close();
		}

		// 寫入交易控制檔

		String controlCode = "L5510." + iWorkMonth + ".1";
		logTxControl(titaVo, controlCode);

		this.batchTransaction.commit();

		String msg = "房貸獎勵保費檢核檔(業績報酬)，業績筆數=" + plusCnt + ", 追回業績筆數=" + minusCnt;
		msg += ",【報表及製檔】下傳檢核檔";

		this.info("L5510Batch end = " + msg);

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
				String.format("%-8s", titaVo.getTlrNo().trim()) + "L5510.1", msg, titaVo);
	}

	// 執行房貸獎勵保費檢核、產生發放資料
	private void calculate(int iCustNo, int iFacmNo, ArrayList<PfItDetail> lPfItDetail, TitaVo titaVo)
			throws LogicException {
		this.info("calculate  " + iCustNo + "-" + iFacmNo + ", size=" + lPfItDetail.size());

		// 至前月累計業績
		BigDecimal eqAmtLM = BigDecimal.ZERO;
		BigDecimal rewardLM = BigDecimal.ZERO;
		BigDecimal drawdownAmtLM = BigDecimal.ZERO;

		// 本月正業績
		BigDecimal eqAmtPlus = BigDecimal.ZERO;
		BigDecimal rewardPlus = BigDecimal.ZERO;
		BigDecimal drawdownAmtPlus = BigDecimal.ZERO;

		// 本月負業績
		BigDecimal eqAmtMinus = BigDecimal.ZERO;
		BigDecimal rewardMinus = BigDecimal.ZERO;
		if (lPfItDetail != null) {
			for (PfItDetail pfIt : lPfItDetail) {
				this.info("pfIt=" + pfIt.toString());
				if (pfIt.getWorkMonth() < iWorkMonth) {
					eqAmtLM = eqAmtLM.add(pfIt.getPerfEqAmt());
					rewardLM = rewardLM.add(pfIt.getPerfReward());
					drawdownAmtLM = drawdownAmtLM.add(pfIt.getDrawdownAmt());
				}
				if (pfIt.getWorkMonth() == iWorkMonth) {
					if (pfIt.getPerfEqAmt().compareTo(BigDecimal.ZERO) > 0) {
						eqAmtPlus = eqAmtPlus.add(pfIt.getPerfEqAmt());
					} else {
						eqAmtMinus = eqAmtMinus.add(pfIt.getPerfEqAmt());
					}
					if (pfIt.getPerfReward().compareTo(BigDecimal.ZERO) > 0) {
						rewardPlus = rewardPlus.add(pfIt.getPerfReward());
					} else {
						rewardMinus = rewardMinus.add(pfIt.getPerfReward());
					}
					if (pfIt.getDrawdownAmt().compareTo(BigDecimal.ZERO) > 0) {
						drawdownAmtPlus = drawdownAmtPlus.add(pfIt.getDrawdownAmt());
					}
				}
			}
			// 累計至前月應無負業績(防資料錯誤)
			if (eqAmtLM.compareTo(BigDecimal.ZERO) < 0) {
				eqAmtLM = BigDecimal.ZERO;
			}
			if (rewardLM.compareTo(BigDecimal.ZERO) < 0) {
				rewardLM = BigDecimal.ZERO;
			}
		}

		// 無本月正業績，無本月負業績、無累計業績，則結束
		if (eqAmtPlus.compareTo(BigDecimal.ZERO) == 0 && rewardPlus.compareTo(BigDecimal.ZERO) == 0
				&& eqAmtMinus.compareTo(BigDecimal.ZERO) == 0 && rewardMinus.compareTo(BigDecimal.ZERO) == 0
				&& eqAmtLM.compareTo(BigDecimal.ZERO) == 0 && rewardLM.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}

		// 執行房貸獎勵保費檢核
		PfInsCheck tPfInsCheck = pfCheckInsuranceCom.check(0, iCustNo, iFacmNo, iWorkMonth, titaVo); // 0.換算業績、業務報酬

		this.info("after pfCheckInsuranceCom = " + tPfInsCheck.getCustNo() + "/" + tPfInsCheck.getFacmNo());
		lPfInsCheck.add(tPfInsCheck);

		// 計算正業績，本工作月正業績一率寫入
		// 計算負業績
		// 1.本工作月撥款，檢核結果為Y要追回
		// 2.本工作月還款，檢核結果為N要追回(檢核結果為Y時已追回撥款，故還款不用追回)
		// 3.檢核結果為Y且檢核工作月為本月，追回前月累計

		// 寫入本月正業績
		for (PfItDetail pf : lPfItDetail) {
			if (pf.getWorkMonth() == iWorkMonth) {
				if (pf.getPerfEqAmt().compareTo(BigDecimal.ZERO) > 0
						|| pf.getPerfReward().compareTo(BigDecimal.ZERO) > 0) {
					updatePfItDetail(pf, titaVo);
				}
			}
		}

		// 寫入負業績， 1.本工作月撥款，檢核結果為Y要追回，清除負業績(檢核結果為Y時已追回撥款，故還款不用追回)
		if ("Y".equals(tPfInsCheck.getCheckResult())) {
			if (eqAmtPlus.compareTo(BigDecimal.ZERO) > 0 || rewardPlus.compareTo(BigDecimal.ZERO) >= 0) {
				reverseUpdate(eqAmtPlus, rewardPlus, drawdownAmtPlus, lPfItDetail, titaVo);
			}
			for (PfItDetail pf : lPfItDetail) {
				if (pf.getWorkMonth() == iWorkMonth) {
					updateMediaFg(3, pf, titaVo); // 3.保費檢核結果為Y時已追回撥款，還款不用追回
				}
			}
		}

		// 寫入負業績，2.本工作月還款，檢核結果為N要追回
		if ("N".equals(tPfInsCheck.getCheckResult())) {
			for (PfItDetail pf : lPfItDetail) {
				if (pf.getWorkMonth() == iWorkMonth) {
					if (pf.getPerfEqAmt().compareTo(BigDecimal.ZERO) < 0
							|| pf.getPerfReward().compareTo(BigDecimal.ZERO) < 0) {
						updatePfItDetail(pf, titaVo);
						minusCnt++;
					}
				}
			}
		}

		// 寫入負業績，3.檢核結果為Y且檢核工作月為本月，追回前月累計
		if ("Y".equals(tPfInsCheck.getCheckResult()) && tPfInsCheck.getCheckWorkMonth() == iWorkMonth) {
			if (eqAmtLM.compareTo(BigDecimal.ZERO) > 0 || rewardLM.compareTo(BigDecimal.ZERO) > 0) {
				reverseUpdate(eqAmtLM, rewardLM, drawdownAmtLM, lPfItDetail, titaVo);
			}
		}

	}

	// 寫入負業績，3.檢核結果為Y且檢核工作月為本月，追回金額
	private void reverseUpdate(BigDecimal eqAmt, BigDecimal reward, BigDecimal drawdownAmt,
			List<PfItDetail> lPfItDetail, TitaVo titaVo) throws LogicException {
		this.info("reverse EqAmt=" + eqAmt + ", Reward=" + reward);
		minusCnt++;
		eqAmt = BigDecimal.ZERO.subtract(eqAmt);
		reward = BigDecimal.ZERO.subtract(reward);
		drawdownAmt = BigDecimal.ZERO.subtract(drawdownAmt);

		PfItDetail pfIt = lPfItDetail.get(lPfItDetail.size() - 1); // 最新一筆
		// 工作月(西曆)
		CdWorkMonth tCdWorkMonth = cdWorkMonthService.findById(new CdWorkMonthId(iWorkMonth / 100, iWorkMonth % 100),
				titaVo);
		if (tCdWorkMonth == null) {
			throw new LogicException(titaVo, "E0001", "CdWorkMonth 放款業績工作月對照檔，工作年月=" + iWorkMonth); // 查詢資料不存在
		}
		// 工作季(西曆)
		int workSeason;
		if (tCdWorkMonth.getMonth() <= 3)
			workSeason = tCdWorkMonth.getYear() * 10 + 1;
		else if (tCdWorkMonth.getMonth() <= 6)
			workSeason = tCdWorkMonth.getYear() * 10 + 2;
		else if (tCdWorkMonth.getMonth() <= 9)
			workSeason = tCdWorkMonth.getYear() * 10 + 3;
		else
			workSeason = tCdWorkMonth.getYear() * 10 + 4;

		// key
		// 保費檢核追回，業績日期為該工作月的業績止日
		// 保費檢核追回，以額度為單位，撥款序號=0
		PfItDetail tPfItDetail = new PfItDetail();
		tPfItDetail.setPerfDate(tCdWorkMonth.getEndDate());
		tPfItDetail.setCustNo(pfIt.getCustNo());
		tPfItDetail.setFacmNo(pfIt.getFacmNo());
		tPfItDetail.setBormNo(0);
		tPfItDetail.setRepayType(5); // 還款類別 5.保費檢核追回
		tPfItDetail.setDrawdownDate(0); // 撥款日
		tPfItDetail.setProdCode(pfIt.getProdCode()); // 商品代碼
		tPfItDetail.setPieceCode(pfIt.getPieceCode()); // 計件代碼
		tPfItDetail.setDrawdownAmt(BigDecimal.ZERO); // 撥款金額/追回金額
		tPfItDetail.setUnitCode(pfIt.getUnitCode()); // 單位代號CdEmp.CenterCode單位代號
		tPfItDetail.setDistCode(pfIt.getDistCode()); // 區部代號
		tPfItDetail.setDeptCode(pfIt.getDeptCode()); // 部室代號
		tPfItDetail.setIntroducer(pfIt.getIntroducer()); // 介紹人
		tPfItDetail.setUnitManager(pfIt.getUnitManager()); // 處經理代號
		tPfItDetail.setDistManager(pfIt.getDistManager()); // 區經理代號
		tPfItDetail.setDeptManager(pfIt.getDeptManager()); // 部經理代號
		tPfItDetail.setPerfCnt(BigDecimal.ZERO);// 介紹人計件數
		tPfItDetail.setCntingCode("N");// 是否計件
		tPfItDetail.setPerfAmt(BigDecimal.ZERO);// 業績金額
		tPfItDetail.setWorkMonth(iWorkMonth); // 工作月
		tPfItDetail.setWorkSeason(workSeason); // 工作季
		tPfItDetail.setRewardDate(this.txBuffer.getTxCom().getTbsdy()); // 保費檢核日
		// 換算業績、業務報酬
		tPfItDetail.setPerfEqAmt(eqAmt);
		tPfItDetail.setPerfReward(reward);

		this.info("PfItDetailCom update " + tPfItDetail.toString());
		try {
			pfItDetailService.insert(tPfItDetail, titaVo); // insert
		} catch (DBException e) {
			this.error(e.getMessage());
			throw new LogicException(titaVo, "E0005", "PfItDetail" + e.getErrorMsg()); // 新增資料時，發生錯誤
		}
	}

	// 更新產出媒體檔記號
	private void updateMediaFg(int mediaFg, PfItDetail pf, TitaVo titaVo) throws LogicException {
		this.info("clearUpdate .....");
		if (pf.getPerfEqAmt().compareTo(BigDecimal.ZERO) >= 0 && pf.getPerfReward().compareTo(BigDecimal.ZERO) >= 0) {
			return;
		}
		PfItDetail tPfItDetail = pfItDetailService.holdById(pf, titaVo);
		if (tPfItDetail == null) {
			throw new LogicException(titaVo, "E0006", "PfItDetail"); // 鎖定資料時，發生錯誤
		}
		tPfItDetail.setMediaFg(mediaFg); // 3.保費檢核結果為Y時已追回撥款，還款不用追回
		try {
			pfItDetailService.update(tPfItDetail, titaVo); // update
		} catch (DBException e) {
			this.error(e.getMessage());
			throw new LogicException(titaVo, "E0007", "PfItDetail " + e.getErrorMsg()); // 更新資料時，發生錯誤
		}

	}

	// 更新
	private void updatePfItDetail(PfItDetail pf, TitaVo titaVo) throws LogicException {
		this.info(" updatePfItDetail .....");
		PfItDetail tPfItDetail = pfItDetailService.holdById(pf, titaVo);
		if (tPfItDetail == null) {
			throw new LogicException(titaVo, "E0006", "PfItDetail"); // 鎖定資料時，發生錯誤
		}
		tPfItDetail.setRewardDate(this.txBuffer.getTxCom().getTbsdy()); // 保費檢核日
		try {
			pfItDetailService.update(tPfItDetail, titaVo); // update
		} catch (DBException e) {
			this.error(e.getMessage());
			throw new LogicException(titaVo, "E0007", "PfItDetail " + e.getErrorMsg()); // 更新資料時，發生錯誤
		}

	}

	// 清除負業績
	private void deletePfItDetail(PfItDetail pf, TitaVo titaVo) throws LogicException {
		this.info(" deletePfItDetail .....");
		PfItDetail tPfItDetail = pfItDetailService.holdById(pf, titaVo);
		if (tPfItDetail == null) {
			throw new LogicException(titaVo, "E0006", "PfItDetail"); // 鎖定資料時，發生錯誤
		}
		try {
			pfItDetailService.delete(tPfItDetail, titaVo); // update
		} catch (DBException e) {
			this.error(e.getMessage());
			throw new LogicException(titaVo, "E0008", "PfItDetail " + e.getErrorMsg()); // 刪除資料時，發生錯誤
		}

	}

	/**
	 * 產生媒體檔
	 * 
	 * @param titaVo titaVo
	 * @throws LogicException LogicException
	 */
	private void makeMedia(TitaVo titaVo) throws LogicException {
		this.info("active L5510Batch.makeMedia = " + iWorkMonth);

		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = 0;
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;// 查全部

//		int iBonusDate = Integer.valueOf(titaVo.getParam("BonusDate").trim()) + 19110000;// 獎金發放日

//		Slice<PfItDetail> slPfItDetail = pfItDetailService.findByWorkMonth(iWorkMonth, iWorkMonth, this.index,
//				this.limit, titaVo);
//		List<PfItDetail> lPfItDetail = slPfItDetail == null ? null : slPfItDetail.getContent();

		List<Map<String, String>> L5510List = null;

		try {
			L5510List = l5510ServiceImpl.FindData(titaVo, iWorkMonth, this.index, this.limit);
		} catch (Exception e) {
			// E5004 讀取DB時發生問題
			this.info("L5051 ErrorForDB=" + e);
			throw new LogicException(titaVo, "E5004", "");
		}

		makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5510.2", "業績報酬媒體檔(LNQQQP2NEW)", "LNQQQP2NEW.csv",
				2);
		int cnt = 0;

		Map<String, String> dd = new HashMap<String, String>();

		String introducer = "";
		int custNo = 0;
		int facmNo = 0;
		BigDecimal drawdownAmt = BigDecimal.ZERO;
		BigDecimal perfReward = BigDecimal.ZERO;
		BigDecimal perfEqAmt = BigDecimal.ZERO;

		boolean first = true;
		if (L5510List != null && L5510List.size() > 0) {
			for (Map<String, String> d : L5510List) {
				int custNo2 = Integer.valueOf(d.get("CustNo").trim());
				int facmNo2 = Integer.valueOf(d.get("FacmNo").trim());
				BigDecimal drawdownAmt2 = new BigDecimal(d.get("DrawdownAmt").trim());
				BigDecimal perfReward2 = new BigDecimal(d.get("PerfReward").trim());
				BigDecimal perfEqAmt2 = new BigDecimal(d.get("PerfEqAmt").trim());
				if (first || !introducer.equals(d.get("Introducer").toString().trim()) || custNo != custNo2
						|| facmNo != facmNo2 || drawdownAmt2.compareTo(BigDecimal.ZERO) < 0) {
					if (!first) {
						writeMedia(titaVo, dd, drawdownAmt, perfReward, perfEqAmt);
						drawdownAmt = BigDecimal.ZERO;
						perfReward = BigDecimal.ZERO;
						perfEqAmt = BigDecimal.ZERO;
						cnt++;
					}
					first = false;
				}
				drawdownAmt = drawdownAmt.add(drawdownAmt2);
				perfReward = perfReward.add(perfReward2);
				perfEqAmt = perfEqAmt.add(perfEqAmt2);

				dd.clear();
				dd.putAll(d);
			}
			writeMedia(titaVo, dd, drawdownAmt, perfReward, perfEqAmt);
			cnt++;
		}

		if (cnt > 0) {
			makeFile.close();
			String msg = "共產製 " + cnt + "筆媒體檔資料,請至【報表及製檔】作業,下傳【媒體檔】";
			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
					String.format("%-8s", titaVo.getTlrNo().trim()) + "L5510.2", msg, titaVo);
		} else {
			String msg = "共產製 " + cnt + "筆媒體檔資料";
			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "N", "", "", msg, titaVo);
		}

		// 寫入交易控制檔

		String controlCode = "L5510." + iWorkMonth + ".2";
		logTxControl(titaVo, controlCode);
	}

	private void writeMedia(TitaVo titaVo, Map<String, String> d, BigDecimal drawdownAmt, BigDecimal perfReward,
			BigDecimal perfEqAmt) throws LogicException {
		this.info("L5510Batch writeMedia=" + d.get("CustNo" + "/" + d.get("FacmNo") + "/" + d.get("WorkMonth")));

		String s = "";

		String workMonth = d.get("WorkMonth");
		s += workMonth.substring(0, 4) + "/" + workMonth.substring(4, 6); // 業績年月(7)
		s += ",";
		s += "10H000"; // 申請單位代號(6)
		s += ",";
		s += "0000" + workMonth; // 申請批號(10)
		s += ",";

		String employeeId = String.format("%-10s", d.get("AgentId").trim());

		s += employeeId; // 身份證字號(10)
		s += ",";
		s += "QQ"; // 薪碼(2)
		s += ",";
		s += "                    "; // 薪碼說明
		s += ",";

		String ss = "";

		// BigDecimal perfReward = new BigDecimal(d.get("PerfReward").trim());

		if (perfReward.compareTo(BigDecimal.ZERO) < 0) {
			DecimalFormat df = new DecimalFormat("000000000");
			ss = df.format(perfReward);
		} else {
			DecimalFormat df = new DecimalFormat("0000000000");
			ss = df.format(perfReward);
		}
		s += ss;// 金額(10)
		s += ",";

		s += ss;// 業績(FYC)(10)
		s += ",";

		// BigDecimal perfEqAmt = new BigDecimal(d.get("PerfEqAmt").trim());

		if (perfEqAmt.compareTo(BigDecimal.ZERO) < 0) {
			DecimalFormat df = new DecimalFormat("000000000");
			ss = df.format(perfEqAmt);
		} else {
			DecimalFormat df = new DecimalFormat("0000000000");
			ss = df.format(perfEqAmt);
		}

		s += ss;// 業績(FYP)(10)
		s += ",";

		String custNo = d.get("CustNo").trim();
		String facmNo = d.get("FacmNo").trim();
		custNo = FormatUtil.padLeft(custNo, 7);
		facmNo = FormatUtil.padLeft(facmNo, 3);

		s += FormatUtil.padX(custNo + facmNo + "000", 40);// 轉發明細(40)
		s += ",";

		// 撥款金額/追回金額
		// BigDecimal drawdownAmt = new BigDecimal(d.get("DrawdownAmt").trim());
		if (drawdownAmt.compareTo(BigDecimal.ZERO) < 0) {
			DecimalFormat df = new DecimalFormat("000000000");
			ss = df.format(drawdownAmt);// 業績(FYP)(10)
		} else {
			DecimalFormat df = new DecimalFormat("0000000000");
			ss = df.format(drawdownAmt);// 業績(FYP)(10)
		}

		s += ss;// 計算基礎(10)
		s += ",";

		s += "00035.00";// FP跨售換算率(8)
		s += ",";
		s += "00012.50";// FC跨售換算率(8)
		s += ",";
		s += "6";// 跨售類別(1)
		s += ",";

		// 2022-12-08 Wei 增加 from 淳英提供新格式
		s += "C"; // 轉發類別(1) A:一般獎勵 C:人件費 Z:特殊拆分
		s += ",";
		s += "                    "; // 專案代碼(20)
		s += ",";
		s += "                  "; // 沖銷碼
		s += ",";
		s += "      "; // 成本單位代號

		makeFile.put(s);
	}

	/**
	 * 取消媒體
	 * 
	 * @param titaVo titaVo
	 * @throws LogicException LogicException
	 */
	private void cancelMedia(TitaVo titaVo) throws LogicException {
		this.info("active L5510Batch.cancelMedia ");

		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = 0;
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;// 查全部

//		Slice<PfItDetail> slPfItDetail = pfItDetailService.findByWorkMonth(iWorkMonth, iWorkMonth, this.index,
//				this.limit, titaVo);
//		List<PfItDetail> lPfItDetail = slPfItDetail == null ? null : slPfItDetail.getContent();
//
//		int cnt = 0;
//		if (lPfItDetail != null) {
//			for (PfItDetail pfItDetail : lPfItDetail) {
//
//				if (pfItDetail.getMediaFg() == 0) {
//					continue;
//				}
//
//				PfItDetail pfItDetail2 = pfItDetailService.holdById(pfItDetail, titaVo);
//				if (pfItDetail2 == null) {
//					throw new LogicException(titaVo, "E0001", "介紹人業績明細檔");
//				}
//
////				pfItDetail2.setRewardDate(0);
//				pfItDetail2.setMediaFg(0);
//				pfItDetail2.setMediaDate(0);
//				try {
//					pfItDetailService.update(pfItDetail2, titaVo);
//				} catch (DBException e) {
//					throw new LogicException(titaVo, "E0007", "獎金媒體發放檔");
//				}
//
//				cnt++;
//			}
//		}

		// 刪除交易控制檔

		String controlCode = "L5510." + iWorkMonth + ".2";
		TxControl txControl = txControlService.holdById(controlCode, titaVo);
		if (txControl != null) {
			try {
				txControlService.delete(txControl, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}
		}

		String msg = "已取消";// "取消" + cnt + "筆資料";
		webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "N", "", "", msg, titaVo);

	}

	private void logTxControl(TitaVo titaVo, String controlCode) throws LogicException {
		TxControl txControl = txControlService.holdById(controlCode, titaVo);
		if (txControl == null) {
			txControl = new TxControl();
			txControl.setCode(controlCode);
			txControl.setDesc("");
			try {
				txControlService.insert(txControl, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}
		} else {
			try {
				txControlService.update(txControl, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}
		}

	}
}