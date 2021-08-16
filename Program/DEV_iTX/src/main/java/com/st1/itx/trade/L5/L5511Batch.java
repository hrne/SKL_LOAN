package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
import com.st1.itx.db.domain.PfReward;
import com.st1.itx.db.domain.PfRewardMedia;
import com.st1.itx.db.service.PfRewardService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.PfCheckInsuranceCom;
import com.st1.itx.db.service.PfRewardMediaService;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.PfItDetailService;
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
	private int iWorkSeason = 0;
	private int commitCnt = 20;
	private int processCnt = 0;
	private ArrayList<PfInsCheck> lPfInsCheck = new ArrayList<PfInsCheck>();// 房貸獎勵保費檢核檔.xlsx
	private ArrayList<PfRewardMedia> lPfRewardMedia = new ArrayList<PfRewardMedia>(); // 發放媒體匯入檔
	private ArrayList<PfRewardMedia> lPfRewardMediaLM = new ArrayList<PfRewardMedia>(); // 發放媒體匯入檔(追回前月)

	private String msg = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5511Batch ");
		this.totaVo.init(titaVo);

		String iFunCode = titaVo.getParam("FunCode").trim();// 使用功能
		iWorkMonth = parse.stringToInteger(titaVo.getParam("WorkMonth")) + 191100;
		if (iWorkMonth % 100 <= 3)
			iWorkSeason = (iWorkMonth / 100) * 10 + 1;
		else if (iWorkMonth % 100 <= 6)
			iWorkSeason = (iWorkMonth / 100) * 10 + 2;
		else if (iWorkMonth % 100 <= 9)
			iWorkSeason = (iWorkMonth / 100) * 10 + 3;
		else
			iWorkSeason = (iWorkMonth / 100) * 10 + 4;

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

		int iWorkMonthS = 0;
		int custNo = 0;
		int facmNo = 0;
		// 工作季(西曆)

		// 以前4工作月的資料作房貸獎勵保費檢核
		if (iWorkMonth % 100 <= 3) {
			iWorkMonthS = ((iWorkMonth / 100 - 1) * 100 + 13 + iWorkMonth % 100) - 3;
		} else {
			iWorkMonthS = iWorkMonth - 3;
		}
		Slice<PfReward> slPfReward = pfRewardService.findByWorkMonth(iWorkMonthS, iWorkMonth, 0, Integer.MAX_VALUE);

		if (slPfReward != null) {
			ArrayList<PfReward> lPfFac = new ArrayList<PfReward>(); // 額度業績
			custNo = slPfReward.getContent().get(0).getCustNo();
			facmNo = slPfReward.getContent().get(0).getFacmNo();
			for (PfReward pfIt : slPfReward.getContent()) {
				// 額度不同則執行房貸獎勵保費檢核、產生發放媒體
				if (pfIt.getCustNo() != custNo || pfIt.getFacmNo() != facmNo) {
					calculate(custNo, facmNo, lPfFac, titaVo);
					custNo = pfIt.getCustNo();
					facmNo = pfIt.getFacmNo();
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

		this.info("lPfInsCheck size=" + lPfInsCheck.size());
		if (lPfInsCheck.size() > 0) {
			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5511.1", "房貸獎勵保費檢核檔(介紹、協辦人獎金)",
					"房貸獎勵保費檢核檔(介紹、協辦人獎金)", "介紹、協辦人獎金");

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

//		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "", "", "", "介紹、協辦獎金發放檔已產生，業績筆數=" + lPfPlus.size() + ", 追回業績筆數=" + lPfMinus.size(), titaVo);

		toDo1(titaVo);
	}

	// 執行房貸獎勵保費檢核、產生發放媒體
	private void calculate(int iCustNo, int iFacmNo, ArrayList<PfReward> lPfFac, TitaVo titaVo) throws LogicException {
		this.info("calculate  " + iCustNo + "-" + iFacmNo + ", size=" + lPfFac.size());

		// 至前月累計業績
		BigDecimal introBonusLM = BigDecimal.ZERO;
		BigDecimal coorgBonusLM = BigDecimal.ZERO;

		// 獎金媒體發放檔
		Slice<PfRewardMedia> slPfRewardMedia = pfRewardMediaService.findFacmNo(iCustNo, iFacmNo, 0, Integer.MAX_VALUE,
				titaVo);

		// 1:介紹獎金 5:協辦獎金
		if (slPfRewardMedia != null) {
			for (PfRewardMedia pf : slPfRewardMedia.getContent()) {
				if (pf.getWorkMonth() < iWorkMonth) {
					if (pf.getBonusType() == 1) {
						introBonusLM = introBonusLM.add(pf.getAdjustBonus());
					}
					if (pf.getBonusType() == 5) {
						coorgBonusLM = coorgBonusLM.add(pf.getAdjustBonus());

					}
				}
			}
		}
		// 累計至前月應無負業績(防資料錯誤)
		if (introBonusLM.compareTo(BigDecimal.ZERO) < 0) {
			introBonusLM = BigDecimal.ZERO;
		}
		if (coorgBonusLM.compareTo(BigDecimal.ZERO) < 0) {
			coorgBonusLM = BigDecimal.ZERO;
		}
		// 本月正業績
		BigDecimal introBonusPlus = BigDecimal.ZERO;
		BigDecimal coorgBonusPlus = BigDecimal.ZERO;
		// 本月負業績
		BigDecimal introBonusMinus = BigDecimal.ZERO;
		BigDecimal coorgBonusMinus = BigDecimal.ZERO;

		for (PfReward pf : lPfFac) {
			if (pf.getWorkMonth() == iWorkMonth) {
				if (pf.getIntroducerBonus().compareTo(BigDecimal.ZERO) > 0) {
					introBonusPlus = introBonusPlus.add(pf.getIntroducerBonus());
				}
				if (pf.getCoorgnizerBonus().compareTo(BigDecimal.ZERO) > 0) {
					coorgBonusPlus = coorgBonusPlus.add(pf.getCoorgnizerBonus());
				}
				if (pf.getIntroducerBonus().compareTo(BigDecimal.ZERO) < 0) {
					introBonusMinus = introBonusMinus.add(pf.getIntroducerBonus());
				}
				if (pf.getCoorgnizerBonus().compareTo(BigDecimal.ZERO) < 0) {
					coorgBonusMinus = coorgBonusMinus.add(pf.getCoorgnizerBonus());
				}
			}
		}
		// 無本月正業績，無本月負業績、無累計業績，則結束
		if (introBonusLM.compareTo(BigDecimal.ZERO) == 0 && coorgBonusLM.compareTo(BigDecimal.ZERO) == 0
				&& introBonusPlus.compareTo(BigDecimal.ZERO) == 0 && coorgBonusPlus.compareTo(BigDecimal.ZERO) == 0
				&& introBonusMinus.compareTo(BigDecimal.ZERO) == 0 && coorgBonusMinus.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}

		// 執行房貸獎勵保費檢核
		processCnt++;
		PfInsCheck tPfInsCheck = pfCheckInsuranceCom.check(1, iCustNo, iFacmNo, iWorkMonth, titaVo); // 1.介紹獎金、協辦獎金

		lPfInsCheck.add(tPfInsCheck);

		// 計算正業績，本工作月正業績一率寫入
		// 計算負業績
		// 1.本工作月撥款，檢核結果為Y要追回
		// 2.本工作月還款，檢核結果為N要追回(檢核結果為Y時已追回撥款，故還款不用追回)
		// 3.檢核結果為Y且檢核工作月為本月，追回前月累計

		// 寫入本月正業績
		for (PfReward pf : lPfFac) {
			if (pf.getWorkMonth() == iWorkMonth) {
				if (pf.getIntroducerBonus().compareTo(BigDecimal.ZERO) > 0) {
					addPfRewardMedia(pf, 1, pf.getIntroducerBonus(), titaVo);
				}
				if (pf.getCoorgnizerBonus().compareTo(BigDecimal.ZERO) > 0) {
					addPfRewardMedia(pf, 5, pf.getCoorgnizerBonus(), titaVo);
				}
			}
		}

		// 寫入負業績， 1.本工作月撥款，檢核結果為Y要追回
		if ("Y".equals(tPfInsCheck.getCheckResult())) {
			for (PfReward pf : lPfFac) {
				if (pf.getWorkMonth() == iWorkMonth) {
					if (pf.getIntroducerBonus().compareTo(BigDecimal.ZERO) > 0) {
						addPfRewardMedia(pf, 1, BigDecimal.ZERO.subtract(pf.getIntroducerBonus()), titaVo);
					}
					if (pf.getCoorgnizerBonus().compareTo(BigDecimal.ZERO) > 0) {
						addPfRewardMedia(pf, 5, BigDecimal.ZERO.subtract(pf.getCoorgnizerBonus()), titaVo);
					}
				}
			}
		}

		// 寫入負業績，2.本工作月還款，檢核結果為N要追回(檢核結果為Y時已追回撥款，故還款不用追回)
		if ("N".equals(tPfInsCheck.getCheckResult())) {
			for (PfReward pf : lPfFac) {
				if (pf.getWorkMonth() == iWorkMonth) {
					if (pf.getIntroducerBonus().compareTo(BigDecimal.ZERO) < 0) {
						addPfRewardMedia(pf, 1, pf.getIntroducerBonus(), titaVo);
					}
					if (pf.getCoorgnizerBonus().compareTo(BigDecimal.ZERO) < 0) {
						addPfRewardMedia(pf, 5, pf.getCoorgnizerBonus(), titaVo);
					}
				}
			}
		}

		// 寫入負業績，3.檢核結果為Y且檢核工作月為本月，追回前月累計
		if (slPfRewardMedia != null) {
			for (PfRewardMedia pf : slPfRewardMedia.getContent()) {
				if (pf.getWorkMonth() < iWorkMonth) {
					if ((pf.getBonusType() == 1 && introBonusLM.compareTo(BigDecimal.ZERO) > 0)
							|| (pf.getBonusType() == 5 && introBonusLM.compareTo(BigDecimal.ZERO) > 0)) {
						addPfRewardMediaLM(pf, BigDecimal.ZERO.subtract(pf.getAdjustBonus()), titaVo);
					}
				}
			}
		}
	}

	private void addPfRewardMedia(PfReward pf, int bonusType, BigDecimal bonus, TitaVo titaVo) throws LogicException {
		// 協辦獎金，撥款序號為 0
		if (bonusType == 5) {
			for (PfRewardMedia pfMd : lPfRewardMedia) {
				if (pf.getCustNo() == pfMd.getCustNo() && pf.getFacmNo() == pfMd.getFacmNo()
						&& pf.getCoorgnizer().equals(pfMd.getEmployeeNo())
						&& ((bonus.compareTo(BigDecimal.ZERO) > 0 && pfMd.getBonus().compareTo(BigDecimal.ZERO) > 0)
								|| (bonus.compareTo(BigDecimal.ZERO) < 0
										&& pfMd.getBonus().compareTo(BigDecimal.ZERO) < 0))) {
					pfMd.setBonus(pfMd.getBonus().add(bonus));
					pfMd.setAdjustBonus(pfMd.getAdjustBonus().add(bonus));
					pfMd.setPerfDate(pf.getPerfDate()); // 撥款日/追回日
					return;
				}
			}
		}

		PfRewardMedia pfRewardMedia = new PfRewardMedia();
		pfRewardMedia.setBonusDate(0);
		pfRewardMedia.setPerfDate(pf.getPerfDate()); // 撥款日
		pfRewardMedia.setCustNo(pf.getCustNo());
		pfRewardMedia.setFacmNo(pf.getFacmNo());
		if (bonusType == 1) {
			pfRewardMedia.setBormNo(pf.getBormNo());
			pfRewardMedia.setEmployeeNo(pf.getIntroducer());
		} else {
			pfRewardMedia.setBormNo(0);
			pfRewardMedia.setEmployeeNo(pf.getCoorgnizer());
		}
		pfRewardMedia.setPerfDate(pf.getPerfDate()); // 撥款日
		pfRewardMedia.setProdCode(pf.getProdCode());
		pfRewardMedia.setPieceCode(pf.getPieceCode());
		pfRewardMedia.setBonusType(bonusType);
		pfRewardMedia.setBonus(bonus);
		pfRewardMedia.setAdjustBonus(bonus);
		pfRewardMedia.setWorkMonth(pf.getWorkMonth());
		pfRewardMedia.setWorkSeason(pf.getWorkSeason());
		lPfRewardMedia.add(pfRewardMedia);
	}

	// 寫入負業績，追回前月累計
	private void addPfRewardMediaLM(PfRewardMedia pf, BigDecimal bonus, TitaVo titaVo) throws LogicException {
		for (PfRewardMedia pfLM : lPfRewardMediaLM) {
			if (pf.getCustNo() == pfLM.getCustNo() && pf.getFacmNo() == pfLM.getFacmNo()
					&& pf.getBormNo() == pfLM.getBormNo() && pf.getBonusType() == pfLM.getBonusType()
					&& pf.getEmployeeNo().equals(pfLM.getEmployeeNo())) {
				pfLM.setProdCode(pf.getProdCode());
				pfLM.setPieceCode(pf.getPieceCode());
				pfLM.setBonusType(pf.getBonusType());
				pfLM.setBonus(pfLM.getBonus().add(bonus));
				pfLM.setAdjustBonus(pfLM.getAdjustBonus().add(bonus));
				pfLM.setPerfDate(pf.getPerfDate()); // 撥款日/追回日
				return;
			}
		}
		PfRewardMedia pfRewardMedia = new PfRewardMedia();
		pfRewardMedia.setBonusDate(0);
		pfRewardMedia.setPerfDate(pf.getPerfDate()); // 撥款日
		pfRewardMedia.setCustNo(pf.getCustNo());
		pfRewardMedia.setFacmNo(pf.getFacmNo());
		pfRewardMedia.setBormNo(pf.getBormNo());
		pfRewardMedia.setEmployeeNo(pf.getEmployeeNo());
		pfRewardMedia.setProdCode(pf.getProdCode());
		pfRewardMedia.setPieceCode(pf.getPieceCode());
		pfRewardMedia.setBonusType(pf.getBonusType());
		pfRewardMedia.setBonus(pfRewardMedia.getBonus().add(bonus));
		pfRewardMedia.setAdjustBonus(pfRewardMedia.getAdjustBonus().add(bonus));
		pfRewardMedia.setWorkMonth(iWorkMonth);
		pfRewardMedia.setWorkSeason(iWorkSeason);
		lPfRewardMediaLM.add(pfRewardMedia);
	}

	// 匯入發放檔
	private void toDo1(TitaVo titaVo) throws LogicException {
		lPfRewardMedia.addAll(lPfRewardMediaLM);
		if (lPfRewardMedia.size() > 0) {
			try {
				pfRewardMediaService.insertAll(lPfRewardMedia, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "獎金媒體發放檔");
			}
		}

		msg = "共匯入" + lPfRewardMedia.size() + "筆資料，可至【L5053】查詢匯入資料,【報表及製檔】下傳檢核檔";

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L5053",
				titaVo.getParam("WorkMonth") + "9", msg, titaVo);

	}

	// 產製娸體檔
	private void makeMedia(TitaVo titaVo) throws LogicException {
		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = 0;
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;// 查全部

		int iBonusDate = Integer.valueOf(titaVo.getParam("BonusDate").trim()) + 19110000;// 獎金發放日

		String workYM = titaVo.getParam("WorkMonth").trim();// 業績起始日
		int iWorkYM = Integer.valueOf(workYM) + 191100;// 業績工作月

		List<Integer> typeList = new ArrayList<>();
		typeList.add(1);
		typeList.add(5);
		typeList.add(6);

		Slice<PfRewardMedia> slPfRewardMedia = pfRewardMediaService.findWorkMonth(iWorkYM, typeList, 0, this.index,
				this.limit, titaVo);
		List<PfRewardMedia> lPfRewardMedia = slPfRewardMedia == null ? null : slPfRewardMedia.getContent();

		makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5511.2", "介紹獎金媒體檔", "LNM270P.txt", 2);
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

				if (bbonus.compareTo(BigDecimal.ZERO) == 0) {
					continue;
				}

				PfRewardMedia pfRewardMedia2 = pfRewardMediaService.holdById(pfRewardMedia, titaVo);
				if (pfRewardMedia2 == null) {
					throw new LogicException(titaVo, "E0001", "獎金媒體發放檔");
				}

				pfRewardMedia2.setBonusDate(iBonusDate);
				pfRewardMedia2.setMediaFg(1);
				pfRewardMedia2.setMediaDate(this.txBuffer.getTxCom().getTbsdy());
				try {
					pfRewardMediaService.update(pfRewardMedia2, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "獎金媒體發放檔");
				}
				//
				String s = "";

				String workMonth = String.valueOf(pfRewardMedia.getWorkMonth());
				s += workMonth.substring(0, 4) + "/" + workMonth.substring(4, 6); // 業績年月(7)
				s += "10H400"; // 申請單位代號(6)
				s += "0000000001"; // 申請批號(10)

				String employeeId = "";
				CdEmp cdEmp = cdEmpService.findById(pfRewardMedia.getEmployeeNo(), titaVo);
				if (cdEmp == null) {
					throw new LogicException(titaVo, "E0001", "員工編號=" + pfRewardMedia.getEmployeeNo());
				} else {
					employeeId = String.format("%-10s", cdEmp.getAgentId());
				}

				s += employeeId; // 身份證字號(10)
				s += "Q2"; // 薪碼(2)
				s += " 放款介紹獎金       "; // 薪碼說明

				if (bbonus.compareTo(BigDecimal.ZERO) < 0) {
					DecimalFormat df = new DecimalFormat("000000000");
					s += df.format(bbonus);// 金額(10)
				} else {
					DecimalFormat df = new DecimalFormat("0000000000");
					s += df.format(bbonus);// 金額(10)
				}

				s += "0000000000";// 業績(FYC)(10)
				s += "0000000000";// 業績(FYP)(10)
				if (pfRewardMedia.getBonusType() == 1) {
					s += " 介紹獎金                               ";// 轉發明細(40)
				} else if (pfRewardMedia.getBonusType() == 5) {
					s += " 協辦獎金                               ";// 轉發明細(40)
				}
				s += "0000000000";// 計算基礎(10)
				s += "00000.00";// FP跨售換算率(8)
				s += "00000.00";// FC跨售換算率(8)
				s += " ";// 跨售類別(1)
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

		String workYM = titaVo.getParam("WorkYM").trim();
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

				pfRewardMedia.setBonusDate(0);
				pfRewardMedia2.setMediaFg(0);
				pfRewardMedia2.setMediaDate(0);

				try {
					pfRewardMediaService.update(pfRewardMedia2, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "獎金媒體發放檔");
				}
				cnt++;
			}
		}

		msg = "共取消" + cnt + "筆資料";
//		webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L5053", workYM+"9", msg, titaVo);
		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "N", "", "", msg, titaVo);

	}
}