package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.domain.CdWorkMonthId;
import com.st1.itx.db.domain.PfInsCheck;
import com.st1.itx.db.domain.PfReward;
import com.st1.itx.db.domain.PfRewardMedia;
import com.st1.itx.db.domain.TxControl;
import com.st1.itx.db.service.PfRewardService;
import com.st1.itx.db.service.TxControlService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.PfCheckInsuranceCom;
import com.st1.itx.db.service.PfItDetailService;
import com.st1.itx.db.service.PfRewardMediaService;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.db.service.PfInsCheckService;
import com.st1.itx.trade.L5.L5511Report;

/**
 * 產生介紹、協辦獎金發放檔 call by L5511
 * 
 * @author st1
 *
 */
@Service("L5511Batch")
@Scope("prototype")
public class L5511Batch extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5511Batch.class);

	@Autowired
	public TxControlService txControlService;
	@Autowired
	public PfRewardService pfRewardService;
	@Autowired
	public Parse parse;
	@Autowired
	public DateUtil dateUtil;
	@Autowired
	public PfCheckInsuranceCom pfCheckInsuranceCom;
	@Autowired
	PfItDetailService pfItDetailService;
	@Autowired
	public PfRewardMediaService pfRewardMediaService;
	@Autowired
	public CdEmpService cdEmpService;
	@Autowired
	CdWorkMonthService cdWorkMonthService;
	@Autowired
	public PfInsCheckService pfInsCheckService;

	@Autowired
	public DataLog dataLog;
	@Autowired
	public WebClient webClient;
	@Autowired
	public MakeFile makeFile;
	@Autowired
	public MakeExcel makeExcel;

	/* 報表服務注入 */
	@Autowired
	public L5511Report L5511Report;

	private int iWorkMonth = 0;
	private int commitCnt = 20;
	private int processCnt = 0;
	private int iWorkMonthS = 0;
	private ArrayList<PfReward> lPfPlus = new ArrayList<PfReward>(); // 正業績
	private ArrayList<PfReward> lPfMinus = new ArrayList<PfReward>();// 負業績

	private String msg = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5511Batch ");
		this.totaVo.init(titaVo);

		String iFunCode = titaVo.getParam("FunCode").trim();// 使用功能
		iWorkMonth = parse.stringToInteger(titaVo.getParam("WorkMonth")) + 191100;
		iWorkMonthS = parse.stringToInteger(titaVo.getParam("Months")) + 191100;
		CdWorkMonth tCdWorkMonth = cdWorkMonthService.findById(new CdWorkMonthId(iWorkMonth / 100, iWorkMonth % 100));

		if (tCdWorkMonth == null) {
			throw new LogicException(titaVo, "E0001", "CdWorkMonth 放款業績工作月對照檔，工作年月=" + iWorkMonth); // 查詢資料不存在
		} else if (tCdWorkMonth.getBonusDate() == 0) {
			throw new LogicException(titaVo, "E0015", "CdWorkMonth 放款業績工作月對照檔，工作年月=" + iWorkMonth + "，獎金發放日期未設定"); // 查詢資料不存在
		}

		if ("1".equals(iFunCode)) {
			toCheck(titaVo);
		} else if ("2".equals(iFunCode)) {
			makeMedia(titaVo);
		} else if ("3".equals(iFunCode)) {
			cancelMedia(titaVo);
		}

		return null;
	}

	// 檢查及匯入資料PfReward > PfRewardMedia

	private void toCheck(TitaVo titaVo) throws LogicException {
		// 刪除本月保費檢核資料(重新執行用)
		Slice<PfInsCheck> slPfInsCheck = pfInsCheckService.findCheckWorkMonthEq(iWorkMonth, 1, 0, Integer.MAX_VALUE,
				titaVo);
		if (slPfInsCheck != null) {
			List<PfInsCheck> lPfInsCheckDelete = new ArrayList<PfInsCheck>();
			lPfInsCheckDelete = slPfInsCheck.getContent();
			try {
				pfInsCheckService.deleteAll(lPfInsCheckDelete, titaVo); // update
			} catch (DBException e) {
				this.error(e.getMessage());
				throw new LogicException(titaVo, "E0008", "PfInsCheck " + e.getErrorMsg()); // 刪除資料時，發生錯誤
			}
		}

		int custNo = 0;
		int facmNo = 0;
		int bormNo = 0;

		Slice<PfReward> slPfReward = pfRewardService.findByWorkMonth(iWorkMonthS, iWorkMonth, 0, Integer.MAX_VALUE);

		if (slPfReward != null) {
			ArrayList<PfReward> lPfFac = new ArrayList<PfReward>(); // 額度業績
			custNo = slPfReward.getContent().get(0).getCustNo();
			facmNo = slPfReward.getContent().get(0).getFacmNo();
			bormNo = slPfReward.getContent().get(0).getBormNo();
			for (PfReward pfIt : slPfReward.getContent()) {
				// 額度不同則執行房貸獎勵保費檢核、產生發放媒體
				if (pfIt.getCustNo() != custNo || pfIt.getFacmNo() != facmNo || pfIt.getBormNo() != bormNo) {
					calculate(custNo, facmNo, lPfFac, titaVo);
					custNo = pfIt.getCustNo();
					facmNo = pfIt.getFacmNo();
					bormNo = pfIt.getBormNo();
					lPfFac = new ArrayList<PfReward>();
					if (processCnt == commitCnt) {
						this.batchTransaction.commit();
						processCnt = 0;
					}
				}
				lPfFac.add(pfIt);
				this.info("lPfFac.add " + pfIt.toString());
			}
			calculate(custNo, facmNo, lPfFac, titaVo);
		}

		this.batchTransaction.commit();

		this.info("lPfPlus size=" + lPfPlus.size());
		for (PfReward pf : lPfPlus) {
			this.info("Plus =" + pf.toString());
		}

		this.info("lPfMinus size=" + lPfMinus.size());
		for (PfReward pf : lPfMinus) {
			this.info("Minus =" + pf.toString());
		}

		slPfInsCheck = pfInsCheckService.findCheckWorkMonthEq(iWorkMonth, 1, 0, Integer.MAX_VALUE, titaVo);

		if (slPfInsCheck != null) {
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
			makeExcel.setValue(row, 10, "業績工作月");
			for (PfInsCheck pf : slPfInsCheck.getContent()) {
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
				makeExcel.setValue(row, 10, String.format("%05d", pf.getPerfWorkMonth() - 191100));
			}
			makeExcel.close();
		}

		this.batchTransaction.commit();

//		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "", "", "", "介紹、協辦獎金發放檔已產生，業績筆數=" + lPfPlus.size() + ", 追回業績筆數=" + lPfMinus.size(), titaVo);

		toDo1(titaVo);
	}

	// 執行房貸獎勵保費檢核、產生發放媒體
	private void calculate(int iCustNo, int iFacmNo, ArrayList<PfReward> lPfFac, TitaVo titaVo) throws LogicException {
		this.info("calculate  " + iCustNo + "-" + iFacmNo + ", size=" + lPfFac.size());

		BigDecimal bonusLM = BigDecimal.ZERO; // 前月累計業績
		BigDecimal coBonusLM = BigDecimal.ZERO; // 前月累計業績
		// 本工作月正業績，並累計前月業績
		for (PfReward iPf : lPfFac) {
			this.info("calculate = " + iPf.toString());
			if (iPf.getWorkMonth() == iWorkMonth) {
				PfReward pfIt = (PfReward) dataLog.clone(iPf);
				pfIt.setIntroducerBonus(BigDecimal.ZERO);
				pfIt.setCoorgnizerBonus(BigDecimal.ZERO);
				if (iPf.getIntroducerBonus().compareTo(BigDecimal.ZERO) > 0) {
					pfIt.setIntroducerBonus(iPf.getIntroducerBonus());
				}
				if (iPf.getCoorgnizerBonus().compareTo(BigDecimal.ZERO) > 0) {
					pfIt.setCoorgnizerBonus(iPf.getCoorgnizerBonus());
				}
				if (pfIt.getIntroducerBonus().compareTo(BigDecimal.ZERO) > 0
						|| pfIt.getCoorgnizerBonus().compareTo(BigDecimal.ZERO) > 0) {
					lPfPlus.add(pfIt);
				}
			}
			if (iPf.getWorkMonth() < iWorkMonth) {
				bonusLM = bonusLM.add(iPf.getIntroducerBonus());
				coBonusLM = coBonusLM.add(iPf.getCoorgnizerBonus());
			}
		}
		// 無本月正業績，也無累計業績，則結束
		if (lPfPlus.size() == 0 && bonusLM.compareTo(BigDecimal.ZERO) <= 0
				&& coBonusLM.compareTo(BigDecimal.ZERO) <= 0) {
			return;
		}
		// 執行房貸獎勵保費檢核
		processCnt++;
		PfInsCheck tPfInsCheck = new PfInsCheck();
		// 計算負業績，本工作月還款追回或房貸獎勵保費檢核追回(本工作月檢核結果為Y)
		// 1.本工作月撥款，檢核結果為Y要追回
		// 2.本工作月還款，檢核結果為N要追回(檢核結果為Y時已追回撥款，故還款不用追回)
		// 3.檢核結果為Y且檢核工作月為本月，追回前月累計
		for (PfReward iPf : lPfFac) {
			PfReward pfIt = (PfReward) dataLog.clone(iPf);
			pfIt.setIntroducerBonus(BigDecimal.ZERO);
			pfIt.setCoorgnizerBonus(BigDecimal.ZERO);
			if (lPfPlus.size() >= 0) {
				if (iPf.getWorkMonth() == iWorkMonth) {
					tPfInsCheck = pfCheckInsuranceCom.check(1, iCustNo, iFacmNo, iPf.getWorkMonth(), iWorkMonth,
							titaVo); // 1.介紹獎金、協辦獎金
					// 1.本工作月撥款，檢核結果為Y要追回
					if ("Y".equals(tPfInsCheck.getCheckResult())) {
						if (iPf.getIntroducerBonus().compareTo(BigDecimal.ZERO) > 0) {
							pfIt.setIntroducerBonus(BigDecimal.ZERO.subtract(iPf.getIntroducerBonus()));
						}
						if (iPf.getCoorgnizerBonus().compareTo(BigDecimal.ZERO) > 0) {
							pfIt.setCoorgnizerBonus(BigDecimal.ZERO.subtract(iPf.getCoorgnizerBonus()));
						}
						if (pfIt.getIntroducerBonus().compareTo(BigDecimal.ZERO) < 0
								|| pfIt.getCoorgnizerBonus().compareTo(BigDecimal.ZERO) < 0) {
							lPfMinus.add(pfIt);
						}
					}
					// 2.本工作月還款，檢核結果為N要追回(檢核結果為Y時已追回撥款，故還款不用追回)
					if (!"Y".equals(tPfInsCheck.getCheckResult())) {
						if (iPf.getIntroducerBonus().compareTo(BigDecimal.ZERO) < 0) {
							pfIt.setIntroducerBonus(iPf.getIntroducerBonus());
						}
						if (iPf.getCoorgnizerBonus().compareTo(BigDecimal.ZERO) < 0) {
							pfIt.setCoorgnizerBonus(iPf.getCoorgnizerBonus());
						}
						if (pfIt.getIntroducerBonus().compareTo(BigDecimal.ZERO) < 0
								|| pfIt.getCoorgnizerBonus().compareTo(BigDecimal.ZERO) < 0) {
							lPfMinus.add(pfIt);
						}
					}
				}
			}
			// 3.檢核結果為Y且檢核工作月為本月，追回前月累計
			this.info("calculate 3 bonusLM =" + bonusLM + ", Reward=" + coBonusLM);
			if (bonusLM.compareTo(BigDecimal.ZERO) > 0 || coBonusLM.compareTo(BigDecimal.ZERO) > 0) {
				if (iPf.getWorkMonth() < iWorkMonth) {
					if (iPf.getIntroducerBonus().compareTo(BigDecimal.ZERO) > 0
							|| iPf.getCoorgnizerBonus().compareTo(BigDecimal.ZERO) > 0) {
						tPfInsCheck = pfCheckInsuranceCom.check(1, iCustNo, iFacmNo, iPf.getWorkMonth(), iWorkMonth,
								titaVo); // 1.介紹獎金、協辦獎金
						if ("Y".equals(tPfInsCheck.getCheckResult()) && tPfInsCheck.getCheckWorkMonth() == iWorkMonth) {
							// 追回前月業績，不超過前月累計
							if (iPf.getIntroducerBonus().compareTo(BigDecimal.ZERO) > 0) {
								if (bonusLM.compareTo(iPf.getIntroducerBonus()) > 0) {
									pfIt.setIntroducerBonus(BigDecimal.ZERO.subtract(iPf.getIntroducerBonus()));
									bonusLM = bonusLM.subtract(iPf.getIntroducerBonus());
								} else {
									pfIt.setIntroducerBonus(BigDecimal.ZERO.subtract(bonusLM));
									bonusLM = BigDecimal.ZERO;
								}
							}
							if (iPf.getCoorgnizerBonus().compareTo(BigDecimal.ZERO) > 0) {
								if (coBonusLM.compareTo(iPf.getCoorgnizerBonus()) > 0) {
									pfIt.setCoorgnizerBonus(BigDecimal.ZERO.subtract(iPf.getCoorgnizerBonus()));
									coBonusLM = coBonusLM.subtract(iPf.getCoorgnizerBonus());
								} else {
									pfIt.setCoorgnizerBonus(BigDecimal.ZERO.subtract(coBonusLM));
									coBonusLM = BigDecimal.ZERO;
								}
							}
							if (pfIt.getIntroducerBonus().compareTo(BigDecimal.ZERO) < 0
									|| pfIt.getCoorgnizerBonus().compareTo(BigDecimal.ZERO) < 0) {
								lPfMinus.add(pfIt);
							}
							this.info("calculate 3  pfIt =" + pfIt.toString());
						}
					}
				}
			}
		}

	}

	// 匯入發放檔
	private void toDo1(TitaVo titaVo) throws LogicException {
		this.info("L5511Batch toDo1 begin");
		String iWorkYM = titaVo.getParam("WorkMonth").trim();// 工作年月

		int iDateFm = Integer.valueOf(titaVo.getParam("DateFm").trim()) + 19110000;// 業績起始日
		int iDateTo = Integer.valueOf(titaVo.getParam("DateTo").trim()) + 19110000;// 業績起始日

		CdWorkMonth tCdWorkMonth = cdWorkMonthService.findById(new CdWorkMonthId(iWorkMonth / 100, iWorkMonth % 100));

		if (tCdWorkMonth == null) {
			throw new LogicException(titaVo, "E0001", "CdWorkMonth 放款業績工作月對照檔，工作年月=" + iWorkMonth); // 查詢資料不存在
		} else if (tCdWorkMonth.getBonusDate() == 0) {
			throw new LogicException(titaVo, "E0015", "CdWorkMonth 放款業績工作月對照檔，工作年月=" + iWorkMonth + "，獎金發放日期未設定"); // 查詢資料不存在
		}

		// 寫入交易控制檔

		String controlCode = "L5511." + iWorkMonth + ".1";
		logTxControl(titaVo, controlCode, "");

		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = 0;
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;// 查全部
		Slice<PfReward> slPfReward = pfRewardService.findByPerfDate(iDateFm, iDateTo, this.index, this.limit, titaVo);
		List<PfReward> lPfReward = slPfReward == null ? null : slPfReward.getContent();

		int cnt = 0;
		if (lPfReward != null) {
			for (PfReward pfReward : lPfReward) {
				// 已轉檔
				if (pfReward.getIntroducerBonusDate() != 0) {
					continue;
				}

				// 獎金為0
				if (pfReward.getIntroducerBonus().compareTo(BigDecimal.ZERO) == 0
						&& pfReward.getCoorgnizerBonus().compareTo(BigDecimal.ZERO) == 0) {
					continue;
				}

				if (pfReward.getRepayType() == 0 && (pfReward.getIntroducerBonus().compareTo(BigDecimal.ZERO) < 0
						|| pfReward.getCoorgnizerBonus().compareTo(BigDecimal.ZERO) < 0)) {
					continue;
				}

				PfRewardMedia pfRewardMedia = new PfRewardMedia();

				pfRewardMedia.setBonusDate(0);
				pfRewardMedia.setPerfDate(pfReward.getPerfDate());
				pfRewardMedia.setCustNo(pfReward.getCustNo());
				pfRewardMedia.setFacmNo(pfReward.getFacmNo());
				pfRewardMedia.setBormNo(pfReward.getBormNo());

				pfRewardMedia.setAdjustBonusDate(0);
				pfRewardMedia.setWorkMonth(pfReward.getWorkMonth());
				pfRewardMedia.setWorkSeason(pfReward.getWorkSeason());
				pfRewardMedia.setProdCode(pfReward.getProdCode());
				pfRewardMedia.setPieceCode(pfReward.getPieceCode());
				pfRewardMedia.setRemark("");
				pfRewardMedia.setMediaFg(0);
				pfRewardMedia.setMediaDate(0);
				pfRewardMedia.setManualFg(0);
				pfRewardMedia.setBonusDate(tCdWorkMonth.getBonusDate());

//				PfItDetailId pfItDetailId = new PfItDetailId();
//
//				pfItDetailId.setPerfDate(pfReward.getPerfDate());
//				pfItDetailId.setCustNo(pfReward.getCustNo());
//				pfItDetailId.setFacmNo(pfReward.getFacmNo());
//				pfItDetailId.setBormNo(pfReward.getBormNo());

//				PfItDetail pfItDetail = pfItDetailService.findByTxFirst(pfReward.getCustNo(), pfReward.getFacmNo(),
//						pfReward.getBormNo(), pfReward.getPerfDate(), pfReward.getRepayType(), pfReward.getPieceCode(),
//						titaVo);
//				if (pfItDetail == null) {
//					pfRewardMedia.setProdCode("");
//					pfRewardMedia.setPieceCode("");
//				} else {
//					pfRewardMedia.setProdCode(pfItDetail.getProdCode());
//					pfRewardMedia.setPieceCode(pfItDetail.getPieceCode());
//				}

				boolean updFlag = false;
				if (!"".equals(pfReward.getIntroducer().trim())
						&& pfReward.getIntroducerBonus().compareTo(BigDecimal.ZERO) != 0) {
					cnt++;

					pfRewardMedia.setBonusType(1);
					pfRewardMedia.setEmployeeNo(pfReward.getIntroducer());
					pfRewardMedia.setBonus(pfReward.getIntroducerBonus());
					pfRewardMedia.setAdjustBonus(pfReward.getIntroducerBonus());

					try {
						pfRewardMediaService.insert(pfRewardMedia, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0005", "獎金媒體發放檔");
					}

					updFlag = true;
					pfReward.setIntroducerBonusDate(this.txBuffer.getTxCom().getTbsdy());
				}

				if (!"".equals(pfReward.getCoorgnizer().trim())
						&& pfReward.getCoorgnizerBonus().compareTo(BigDecimal.ZERO) != 0) {
					cnt++;

					pfRewardMedia.setBonusType(5);
					pfRewardMedia.setEmployeeNo(pfReward.getCoorgnizer());
					pfRewardMedia.setBonus(pfReward.getCoorgnizerBonus());
					pfRewardMedia.setAdjustBonus(pfReward.getCoorgnizerBonus());

					try {
						pfRewardMediaService.insert(pfRewardMedia, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0005", "獎金媒體發放檔");
					}

					updFlag = true;
					pfReward.setCoorgnizerBonusDate(this.txBuffer.getTxCom().getTbsdy());
				}

				if (updFlag) {
					try {
						pfRewardService.update(pfReward, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007", "介紹、協辦獎金計算檔");
					}

				}

			}
		}

		this.info("L5511Batch toDo1 end");
		// 出表

		msg = "共匯入" + cnt + "筆資料，可至【L5053】查詢匯入資料,【報表及製檔】下傳檢核檔";

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L5053", iWorkYM + "9", msg,
				titaVo);

	}

	// 產製娸體檔
	private void makeMedia(TitaVo titaVo) throws LogicException {
		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = 0;
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;// 查全部

		CdWorkMonth tCdWorkMonth = cdWorkMonthService.findById(new CdWorkMonthId(iWorkMonth / 100, iWorkMonth % 100));

		if (tCdWorkMonth == null) {
			throw new LogicException(titaVo, "E0001", "CdWorkMonth 放款業績工作月對照檔，工作年月=" + iWorkMonth); // 查詢資料不存在
		} else if (tCdWorkMonth.getBonusDate() == 0) {
			throw new LogicException(titaVo, "E0015", "CdWorkMonth 放款業績工作月對照檔，工作年月=" + iWorkMonth + "，獎金發放日期未設定"); // 查詢資料不存在
		}

//		int iBonusDate = Integer.valueOf(titaVo.getParam("BonusDate").trim()) + 19110000;// 獎金發放日

		int iBonusDate = tCdWorkMonth.getBonusDate();

		String workYM = titaVo.getParam("WorkMonth").trim();// 業績起始日
		int iWorkYM = Integer.valueOf(workYM) + 191100;// 業績工作月

		// 寫入交易控制檔

		String controlCode = "L5511." + iWorkMonth + ".2";
		logTxControl(titaVo, controlCode, "" + iBonusDate);

		List<Integer> typeList = new ArrayList<>();
		typeList.add(1);
		typeList.add(5);
		typeList.add(6);

		Slice<PfRewardMedia> slPfRewardMedia = pfRewardMediaService.findWorkMonth(iWorkYM, typeList, 0, this.index,
				this.limit, titaVo);
		List<PfRewardMedia> lPfRewardMedia = slPfRewardMedia == null ? null : slPfRewardMedia.getContent();

		makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5511.2", "介紹、協辦獎金媒體檔", "LNM270P.csv", 2);
		int cnt = 0;
		if (lPfRewardMedia != null) {
			for (PfRewardMedia pfRewardMedia : lPfRewardMedia) {
				// 媒體檔格式未定先SKIP
				if (pfRewardMedia.getBonusType() == 6) {
					continue;
				}

//				if (pfRewardMedia.getMediaFg() == 1) {
//					continue;
//				}

				BigDecimal bbonus = pfRewardMedia.getAdjustBonus();
				bbonus = bbonus.setScale(0, RoundingMode.FLOOR);

				if (bbonus.compareTo(BigDecimal.ZERO) == 0) {
					continue;
				}

				PfRewardMedia pfRewardMedia2 = pfRewardMediaService.holdById(pfRewardMedia, titaVo);
				if (pfRewardMedia2 == null) {
					throw new LogicException(titaVo, "E0001", "獎金媒體發放檔");
				}

//				pfRewardMedia2.setBonusDate(iBonusDate);
//				pfRewardMedia2.setMediaFg(1);
//				pfRewardMedia2.setMediaDate(this.txBuffer.getTxCom().getTbsdy());
//				try {
//					pfRewardMediaService.update(pfRewardMedia2, titaVo);
//				} catch (DBException e) {
//					throw new LogicException(titaVo, "E0007", "獎金媒體發放檔");
//				}
				//
				String s = "";

				String workMonth = String.valueOf(pfRewardMedia.getWorkMonth());
				s += workMonth.substring(0, 4) + "/" + workMonth.substring(4, 6); // 業績年月(7)
				s += ",";
				s += "10H400"; // 申請單位代號(6)
				s += ",";
				s += "0000000001"; // 申請批號(10)
				s += ",";

				String employeeId = "";
				CdEmp cdEmp = cdEmpService.findById(pfRewardMedia.getEmployeeNo(), titaVo);
				if (cdEmp == null) {
					throw new LogicException(titaVo, "E0001", "員工編號=" + pfRewardMedia.getEmployeeNo());
				} else {
					employeeId = String.format("%-10s", cdEmp.getAgentId());
				}

				s += employeeId; // 身份證字號(10)
				s += ",";
				s += "Q2"; // 薪碼(2)
				s += ",";
				s += " 放款介紹獎金       "; // 薪碼說明
				s += ",";

				if (bbonus.compareTo(BigDecimal.ZERO) < 0) {
					DecimalFormat df = new DecimalFormat("000000000");
					s += df.format(bbonus);// 金額(10)
					s += ",";
				} else {
					DecimalFormat df = new DecimalFormat("0000000000");
					s += df.format(bbonus);// 金額(10)
					s += ",";
				}

				s += "0000000000";// 業績(FYC)(10)
				s += ",";
				s += "0000000000";// 業績(FYP)(10)
				s += ",";
				String transferDetail = "";
				if (pfRewardMedia.getBonusType() == 1) {
					transferDetail = " 介紹獎金";
				} else if (pfRewardMedia.getBonusType() == 5) {
					transferDetail = " 協辦獎金";
				}
				s += FormatUtil.padX(transferDetail, 40);// 轉發明細(40)
				s += ",";
				s += "0000000000";// 計算基礎(10)
				s += ",";
				s += "00000.00";// FP跨售換算率(8)
				s += ",";
				s += "00000.00";// FC跨售換算率(8)
				s += ",";
				s += " ";// 跨售類別(1)
				s += ",";

				// 2022-12-08 Wei 增加 from 淳英提供新格式
				s += "Z"; // 轉發類別(1) A:一般獎勵 C:人件費 Z:特殊拆分
				s += ",";
				s += "                    "; // 專案代碼(20)
				s += ",";
				s += "                  "; // 沖銷碼
				s += ",";
				s += "10H000"; // 成本單位代號

				makeFile.put(s);
				cnt++;
			}
		}

		if (cnt > 0) {
			makeFile.close();
			L5511Report.exec(titaVo, iWorkYM, iBonusDate);
			msg = "共產製 " + cnt + "筆媒體檔資料,請至【報表及製檔】作業,下傳【媒體檔】及列印【車馬費發放明細表】";
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
					String.format("%-8s", titaVo.getTlrNo().trim()) + "L5511", msg, titaVo);
		} else {
			msg = "共產製 " + cnt + "筆媒體檔資料";
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L5053", workYM + "9", msg,
					titaVo);
		}

//		makeFile.toFile(fileSno, "LNM270P");

	}

	// 取消娸體檔
	private void cancelMedia(TitaVo titaVo) throws LogicException {
		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = 0;
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;// 查全部

		String workYM = titaVo.getParam("WorkMonth").trim();
		int iWorkYM = Integer.valueOf(workYM) + 191100;// 業績工作月

		List<Integer> typeList = new ArrayList<>();
		typeList.add(1);
		typeList.add(5);
		typeList.add(6);

		Slice<PfRewardMedia> slPfRewardMedia = pfRewardMediaService.findWorkMonth(iWorkYM, typeList, 1, this.index,
				this.limit, titaVo);
		List<PfRewardMedia> lPfRewardMedia = slPfRewardMedia == null ? null : slPfRewardMedia.getContent();

		int cnt = 0;
		if (lPfRewardMedia != null) {
			for (PfRewardMedia pfRewardMedia : lPfRewardMedia) {
				PfRewardMedia pfRewardMedia2 = pfRewardMediaService.holdById(pfRewardMedia, titaVo);
				if (pfRewardMedia2 == null) {
					throw new LogicException(titaVo, "E0001", "獎金媒體發放檔");
				}

//				pfRewardMedia.setBonusDate(0);
//				pfRewardMedia2.setMediaFg(0);
//				pfRewardMedia2.setMediaDate(0);
//
//				try {
//					pfRewardMediaService.update(pfRewardMedia2, titaVo);
//				} catch (DBException e) {
//					throw new LogicException(titaVo, "E0007", "獎金媒體發放檔");
//				}
				cnt++;
			}
		}

		msg = "共取消" + cnt + "筆資料";
//		webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L5053", workYM+"9", msg, titaVo);
		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "N", "", "", msg, titaVo);

		// 刪除交易控制檔

		String controlCode = "L5511." + iWorkMonth + ".2";
		TxControl txControl = txControlService.holdById(controlCode, titaVo);
		if (txControl != null) {
			try {
				txControlService.delete(txControl, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}
		}
	}

	private void logTxControl(TitaVo titaVo, String controlCode, String desc) throws LogicException {
		TxControl txControl = txControlService.holdById(controlCode, titaVo);
		if (txControl == null) {
			txControl = new TxControl();
			txControl.setCode(controlCode);
			txControl.setDesc(desc);
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