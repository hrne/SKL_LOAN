package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.PfInsCheck;
import com.st1.itx.db.domain.PfItDetail;
import com.st1.itx.db.service.PfItDetailService;
import com.st1.itx.db.service.CdEmpService;

import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.PfCheckInsuranceCom;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.common.MakeExcel;

/**
 * 產生換算業績、業務報酬發放檔 call by L5510
 * 
 * @author st1
 *
 */
@Service("L5510Batch")
@Scope("prototype")

public class L5510Batch extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5510Batch.class);

	@Autowired
	public PfItDetailService pfItDetailService;
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
	DateUtil dDateUtil;

	private int iWorkMonth = 0;
	private int commitCnt = 20;
	private int processCnt = 0;
	private ArrayList<PfItDetail> lPfPlus = new ArrayList<PfItDetail>(); // 正業績
	private ArrayList<PfItDetail> lPfMinus = new ArrayList<PfItDetail>();// 負業績

	private ArrayList<PfInsCheck> lPfInsCheck = new ArrayList<PfInsCheck>();// 房貸獎勵保費檢核檔.xlsx

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5510Batch ");
		this.totaVo.init(titaVo);

		String iFunCode = titaVo.getParam("FunCode").trim();// 使用功能
		iWorkMonth = parse.stringToInteger(titaVo.getParam("WorkMonth")) + 191100;

		if ("1".equals(iFunCode)) {
			toCheck(titaVo);
		} else if ("2".equals(iFunCode)) {
			makeMedia(titaVo);
		} else if ("3".equals(iFunCode)) {
			cancelMedia(titaVo);
		}

		return null;
	}

	/**
	 * 房貸獎勵保費檢核機 PfDetail > PfItDetail
	 * 
	 * @param titaVo
	 * @throws LogicException
	 */
	private void toCheck(TitaVo titaVo) throws LogicException {
		this.info("active L5510Batch.toCheck ");

		int iWorkMonthS = 0;
		int custNo = 0;
		int facmNo = 0;

		// 以前4工作月的資料作房貸獎勵保費檢核
		if (iWorkMonth % 100 <= 3) {
			iWorkMonthS = ((iWorkMonth / 100 - 1) * 100 + 13 + iWorkMonth % 100) - 3;
		} else {
			iWorkMonthS = iWorkMonth - 3;
		}
		Slice<PfItDetail> slPfItDetail = pfItDetailService.findByWorkMonth(iWorkMonthS, iWorkMonth, 0, Integer.MAX_VALUE);
		if (slPfItDetail != null) {
			ArrayList<PfItDetail> lPfFac = new ArrayList<PfItDetail>(); // 額度業績
			custNo = slPfItDetail.getContent().get(0).getCustNo();
			facmNo = slPfItDetail.getContent().get(0).getFacmNo();
			for (PfItDetail pfIt : slPfItDetail.getContent()) {
				// 額度不同則執行房貸獎勵保費檢核、產生發放媒體
				if (pfIt.getCustNo() != custNo || pfIt.getFacmNo() != facmNo) {
					calculate(custNo, facmNo, lPfFac, titaVo);
					custNo = pfIt.getCustNo();
					facmNo = pfIt.getFacmNo();
					lPfFac = new ArrayList<PfItDetail>();
//by eric 2021.7.15
//					if (processCnt == commitCnt) {
//						this.batchTransaction.commit();
//						processCnt = 0;
//					}
					this.batchTransaction.commit();
//end					
				}
				lPfFac.add(pfIt);
			}
			calculate(custNo, facmNo, lPfFac, titaVo);
		}

		this.batchTransaction.commit();

		this.info("lPfPlus size=" + lPfPlus.size());
		for (PfItDetail pf : lPfPlus) {
			this.info("Plus =" + pf.toString());
		}

		this.info("lPfMinus size=" + lPfMinus.size());
		for (PfItDetail pf : lPfMinus) {
			this.info("Minus =" + pf.toString());
		}
		this.info("lPfInsCheck size=" + lPfInsCheck.size());
		if (lPfInsCheck.size() > 0) {
			makeExcel.open(titaVo, titaVo.getEntDyI(),  titaVo.getKinbr(), "L5510", "房貸獎勵保費檢核檔(業績報酬)", "房貸獎勵保費檢核檔(業績報酬)", "業績報酬");
			
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
		this.batchTransaction.commit();

		String msg = "換算業績、業務報酬發放檔已產生，業績筆數=" + lPfPlus.size() + ", 追回業績筆數=" + lPfMinus.size();
		msg += ",【報表及製檔】下傳檢核檔";

		this.info("L5510Batch end = " + msg);

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", String.format("%-8s", titaVo.getTlrNo().trim()) + "L5510.1", msg, titaVo);
	}

	// 執行房貸獎勵保費檢核、產生發放媒體
	public void calculate(int iCustNo, int iFacmNo, ArrayList<PfItDetail> lPfFac, TitaVo titaVo) throws LogicException {
		this.info("calculate  " + iCustNo + "-" + iFacmNo + ", size=" + lPfFac.size());

		BigDecimal eqAmtLM = BigDecimal.ZERO; // 前月累計業績
		BigDecimal rewardLM = BigDecimal.ZERO; // 前月累計業績
		// 本工作月正業績，並累計前月業績
		for (PfItDetail iPf : lPfFac) {
			this.info("calculate = " + iPf.toString());
			if (iPf.getWorkMonth() == iWorkMonth) {
				PfItDetail pfIt = (PfItDetail) dataLog.clone(iPf);
				pfIt.setPerfEqAmt(BigDecimal.ZERO);
				pfIt.setPerfReward(BigDecimal.ZERO);
				if (iPf.getPerfEqAmt().compareTo(BigDecimal.ZERO) > 0) {
					pfIt.setPerfEqAmt(iPf.getPerfEqAmt());
				}
				if (iPf.getPerfReward().compareTo(BigDecimal.ZERO) > 0) {
					pfIt.setPerfReward(iPf.getPerfReward());
				}
				if (pfIt.getPerfEqAmt().compareTo(BigDecimal.ZERO) > 0 || pfIt.getPerfReward().compareTo(BigDecimal.ZERO) > 0) {
					lPfPlus.add(pfIt);
				}

//by eric 2021.7.15				
				PfItDetail pfItDetail = pfItDetailService.holdById(pfIt, titaVo);
				if (pfItDetail == null) {
					throw new LogicException(titaVo, "E0001", "介紹人業績明細檔");
				}

				pfIt.setRewardDate(this.txBuffer.getTxCom().getTbsdy());// 暫不使用

				try {
					pfItDetailService.update(pfIt, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "獎金媒體發放檔");
				}
//end				
			}
			if (iPf.getWorkMonth() < iWorkMonth) {
				eqAmtLM = eqAmtLM.add(iPf.getPerfEqAmt());
				rewardLM = rewardLM.add(iPf.getPerfReward());
			}
		}

		// 無本月正業績，也無累計業績，則結束
		if (lPfPlus.size() == 0 && eqAmtLM.compareTo(BigDecimal.ZERO) <= 0 && rewardLM.compareTo(BigDecimal.ZERO) <= 0) {
			return;
		}

		// 執行房貸獎勵保費檢核
		processCnt++;
		PfInsCheck tPfInsCheck = pfCheckInsuranceCom.check(0, iCustNo, iFacmNo, iWorkMonth, titaVo); // 0.換算業績、業務報酬

		lPfInsCheck.add(tPfInsCheck);

		// 計算負業績，本工作月還款追回或房貸獎勵保費檢核追回(本工作月檢核結果為Y)
		// 1.本工作月撥款，檢核結果為Y要追回
		// 2.本工作月還款，檢核結果為N要追回(檢核結果為Y時已追回撥款，故還款不用追回)
		// 3.檢核結果為Y且檢核工作月為本月，追回前月累計
		for (PfItDetail iPf : lPfFac) {
			PfItDetail pfIt = (PfItDetail) dataLog.clone(iPf);
			pfIt.setPerfEqAmt(BigDecimal.ZERO);
			pfIt.setPerfReward(BigDecimal.ZERO);
			// 1.本工作月撥款，檢核結果為Y要追回
			if (iPf.getWorkMonth() == iWorkMonth && "Y".equals(tPfInsCheck.getCheckResult())) {
				if (iPf.getPerfEqAmt().compareTo(BigDecimal.ZERO) > 0) {
					pfIt.setPerfEqAmt(BigDecimal.ZERO.subtract(iPf.getPerfEqAmt()));
				}
				if (iPf.getPerfReward().compareTo(BigDecimal.ZERO) > 0) {
					pfIt.setPerfReward(BigDecimal.ZERO.subtract(iPf.getPerfReward()));
				}
				if (pfIt.getPerfEqAmt().compareTo(BigDecimal.ZERO) < 0 || pfIt.getPerfReward().compareTo(BigDecimal.ZERO) < 0) {
					lPfMinus.add(pfIt);
				}
			}
			// 2.本工作月還款，檢核結果為N要追回(檢核結果為Y時已追回撥款，故還款不用追回)
			if (iPf.getWorkMonth() == iWorkMonth && "N".equals(tPfInsCheck.getCheckResult())) {
				if (iPf.getPerfEqAmt().compareTo(BigDecimal.ZERO) < 0) {
					pfIt.setPerfEqAmt(iPf.getPerfEqAmt());
				}
				if (iPf.getPerfReward().compareTo(BigDecimal.ZERO) < 0) {
					pfIt.setPerfReward(iPf.getPerfReward());
				}
				if (pfIt.getPerfEqAmt().compareTo(BigDecimal.ZERO) < 0 || pfIt.getPerfReward().compareTo(BigDecimal.ZERO) < 0) {
					lPfMinus.add(pfIt);
				}
			}
			// 3.檢核結果為Y且檢核工作月為本月，追回前月累計
			if (iPf.getWorkMonth() < iWorkMonth && "Y".equals(tPfInsCheck.getCheckResult()) && tPfInsCheck.getCheckWorkMonth() == iWorkMonth) {
				this.info("calculate 3 EqAmtLM =" + eqAmtLM + ", Reward=" + rewardLM);
				// 追回前月業績，不超過前月累計
				if (iPf.getPerfEqAmt().compareTo(BigDecimal.ZERO) > 0) {
					if (eqAmtLM.compareTo(iPf.getPerfEqAmt()) > 0) {
						pfIt.setPerfEqAmt(BigDecimal.ZERO.subtract(iPf.getPerfEqAmt()));
						eqAmtLM = eqAmtLM.subtract(iPf.getPerfEqAmt());
					} else {
						pfIt.setPerfEqAmt(BigDecimal.ZERO.subtract(eqAmtLM));
						eqAmtLM = BigDecimal.ZERO;
					}
				}
				if (iPf.getPerfReward().compareTo(BigDecimal.ZERO) > 0) {
					if (rewardLM.compareTo(iPf.getPerfReward()) > 0) {
						pfIt.setPerfReward(BigDecimal.ZERO.subtract(iPf.getPerfReward()));
						rewardLM = rewardLM.subtract(iPf.getPerfReward());
					} else {
						pfIt.setPerfReward(BigDecimal.ZERO.subtract(rewardLM));
						rewardLM = BigDecimal.ZERO;
					}
				}
				if (pfIt.getPerfEqAmt().compareTo(BigDecimal.ZERO) < 0 || pfIt.getPerfReward().compareTo(BigDecimal.ZERO) < 0) {
					lPfMinus.add(pfIt);
				}
				this.info("calculate 3  pfIt =" + pfIt.toString());
			}
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

		String workYM = titaVo.getParam("WorkMonth").trim();// 業績起始日
		int iWorkYM = Integer.valueOf(workYM) + 191100;// 業績工作月

		Slice<PfItDetail> slPfItDetail = pfItDetailService.findByWorkMonth(iWorkMonth, iWorkMonth, this.index, this.limit, titaVo);
		List<PfItDetail> lPfItDetail = slPfItDetail == null ? null : slPfItDetail.getContent();

		makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5510.2", "業績報酬媒體檔(LNQQQP2NEW)", "LNQQQP2NEW.txt", 2);
		int cnt = 0;
		if (lPfItDetail != null) {
			for (PfItDetail pfItDetail : lPfItDetail) {

				BigDecimal perfReward = pfItDetail.getPerfReward();
				perfReward = perfReward.setScale(0, BigDecimal.ROUND_FLOOR);

				this.info("L5510Batch.makeMedia data = " + pfItDetail.getMediaFg() + "/" + perfReward + '/' + pfItDetail.getIntroducer().trim());

				if (pfItDetail.getMediaFg() == 1) {
					continue;
				}

				if (perfReward.compareTo(BigDecimal.ZERO) == 0) {
					continue;
				}

				if ("".equals(pfItDetail.getIntroducer().trim())) {
					continue;
				}

				PfItDetail pfItDetail2 = pfItDetailService.holdById(pfItDetail, titaVo);
				if (pfItDetail2 == null) {
					throw new LogicException(titaVo, "E0001", "介紹人業績明細檔");
				}

//				pfItDetail2.setRewardDate(this.txBuffer.getTxCom().getTbsdy());//暫不使用
				pfItDetail2.setMediaFg(1);
				pfItDetail2.setMediaDate(this.txBuffer.getTxCom().getTbsdy());
				try {
					pfItDetailService.update(pfItDetail2, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "獎金媒體發放檔");
				}
				//
				String s = "";

				String workMonth = String.valueOf(pfItDetail.getWorkMonth());
				s += workMonth.substring(0, 4) + "/" + workMonth.substring(4, 6); // 業績年月(7)
				s += "10H000"; // 申請單位代號(6)
				s += "0000" + workMonth; // 申請批號(10)

				String employeeId = "";
				CdEmp cdEmp = cdEmpService.findById(pfItDetail.getIntroducer(), titaVo);
				if (cdEmp == null) {
					throw new LogicException(titaVo, "E0001", "員工編號=" + pfItDetail.getIntroducer());
				} else {
					employeeId = String.format("%-10s", cdEmp.getAgentId());
				}

				s += employeeId; // 身份證字號(10)
				s += "QQ"; // 薪碼(2)
				s += "                    "; // 薪碼說明

				String ss = "";
				if (perfReward.compareTo(BigDecimal.ZERO) < 0) {
					DecimalFormat df = new DecimalFormat("000000000");
					ss = df.format(perfReward);
				} else {
					DecimalFormat df = new DecimalFormat("0000000000");
					ss = df.format(perfReward);
				}
				s += ss;// 金額(10)

				s += ss;// 業績(FYC)(10)

				BigDecimal perfEqAmt = pfItDetail.getPerfEqAmt();
				perfEqAmt = perfEqAmt.setScale(0, BigDecimal.ROUND_FLOOR);
				if (perfEqAmt.compareTo(BigDecimal.ZERO) < 0) {
					DecimalFormat df = new DecimalFormat("000000000");
					ss = df.format(perfEqAmt);
				} else {
					DecimalFormat df = new DecimalFormat("0000000000");
					ss = df.format(perfEqAmt);
				}

				s += ss;// 業績(FYP)(10)

				//

				s += String.format("%07d%03d", pfItDetail.getCustNo(), pfItDetail.getFacmNo()) + "000                           ";// 轉發明細(40)

				// 撥款金額/追回金額
				BigDecimal drawdownAmt = pfItDetail.getDrawdownAmt();
				drawdownAmt = drawdownAmt.setScale(0, BigDecimal.ROUND_FLOOR);
				if (drawdownAmt.compareTo(BigDecimal.ZERO) < 0) {
					DecimalFormat df = new DecimalFormat("000000000");
					ss = df.format(drawdownAmt);// 業績(FYP)(10)
				} else {
					DecimalFormat df = new DecimalFormat("0000000000");
					ss = df.format(drawdownAmt);// 業績(FYP)(10)
				}

				s += ss;// 計算基礎(10)

				s += "00035.00";// FP跨售換算率(8)
				s += "00012.50";// FC跨售換算率(8)
				s += "6";// 跨售類別(1)
				makeFile.put(s);
				cnt++;
			}
		}

		if (cnt > 0) {
			makeFile.close();
			String msg = "共產製 " + cnt + "筆媒體檔資料,請至【報表及製檔】作業,下傳【媒體檔】";
			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", String.format("%-8s", titaVo.getTlrNo().trim()) + "L5510.2", msg, titaVo);
		} else {
			String msg = "共產製 " + cnt + "筆媒體檔資料";
			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "N", "", "", msg, titaVo);
		}

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

		String workYM = titaVo.getParam("WorkMonth").trim();// 業績起始日
		int iWorkYM = Integer.valueOf(workYM) + 191100;// 業績工作月

		Slice<PfItDetail> slPfItDetail = pfItDetailService.findByWorkMonth(iWorkYM, iWorkYM, this.index, this.limit, titaVo);
		List<PfItDetail> lPfItDetail = slPfItDetail == null ? null : slPfItDetail.getContent();

		int cnt = 0;
		if (lPfItDetail != null) {
			for (PfItDetail pfItDetail : lPfItDetail) {

				if (pfItDetail.getMediaFg() == 0) {
					continue;
				}

				PfItDetail pfItDetail2 = pfItDetailService.holdById(pfItDetail, titaVo);
				if (pfItDetail2 == null) {
					throw new LogicException(titaVo, "E0001", "介紹人業績明細檔");
				}

//				pfItDetail2.setRewardDate(0);
				pfItDetail2.setMediaFg(0);
				pfItDetail2.setMediaDate(0);
				try {
					pfItDetailService.update(pfItDetail2, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "獎金媒體發放檔");
				}

				cnt++;
			}
		}

		String msg = "共取消" + cnt + "筆資料";
		webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "N", "", "", msg, titaVo);

	}
}