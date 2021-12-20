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
import com.st1.itx.db.domain.PfReward;
import com.st1.itx.db.domain.PfRewardMedia;
import com.st1.itx.db.service.PfRewardService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.PfCheckInsuranceCom;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.db.service.PfRewardMediaService;
import com.st1.itx.db.service.PfItDetailService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.MakeExcel;

/**
 * 產生介紹人加碼獎勵津貼發放檔 call by L5512
 * 
 * @author st1
 *
 */
@Service("L5512Batch")
@Scope("prototype")
public class L5512Batch extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5512Batch.class);

	@Autowired
	public PfRewardService pfRewardService;
	@Autowired
	public PfRewardMediaService pfRewardMediaService;
	@Autowired
	PfItDetailService pfItDetailService;
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
	CdWorkMonthService cdWorkMonthService;

	private int iWorkMonth = 0;
	private int commitCnt = 20;
	private int processCnt = 0;
	private ArrayList<PfReward> lPfPlus = new ArrayList<PfReward>(); // 正業績
	private ArrayList<PfReward> lPfMinus = new ArrayList<PfReward>();// 負業績
	private ArrayList<PfInsCheck> lPfInsCheck = new ArrayList<PfInsCheck>();// 房貸獎勵保費檢核檔.xlsx

	private String msg = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		logger.info("active L5512Batch ");
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

	// 檢查並匯入資料 PfReward > PfRewardMedia

	private void toCheck(TitaVo titaVo) throws LogicException {

		iWorkMonth = parse.stringToInteger(titaVo.getParam("WorkMonth")) + 191100;
		int iWorkMonthS = 0;
		int custNo = 0;
		int facmNo = 0;

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
			}
			calculate(custNo, facmNo, lPfFac, titaVo);
		}

		this.batchTransaction.commit();

		logger.info("lPfPlus size=" + lPfPlus.size());
		for (PfReward pf : lPfPlus) {
			logger.info("Plus =" + pf.toString());
		}

		logger.info("lPfMinus size=" + lPfMinus.size());
		for (PfReward pf : lPfMinus) {
			logger.info("Minus =" + pf.toString());
		}
		
		logger.info("lPfInsCheck size=" + lPfInsCheck.size());
		if (lPfInsCheck.size() > 0) {
			makeExcel.open(titaVo, titaVo.getEntDyI(),  titaVo.getKinbr(), "L5512.1", "房貸獎勵保費檢核檔(介紹人加碼獎金)", "房貸獎勵保費檢核檔(介紹人加碼獎金)", "介紹人加碼獎金");
			
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
				logger.info("lPfInsCheck =" + pf.toString());
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

//		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "", "", "", "介紹人加碼獎勵津貼發放檔已產生，業績筆數=" + lPfPlus.size() + ", 追回業績筆數=" + lPfMinus.size(), titaVo);

		toDo1(titaVo);
	}

	// 執行房貸獎勵保費檢核、產生發放媒體
	private void calculate(int iCustNo, int iFacmNo, ArrayList<PfReward> lPfFac, TitaVo titaVo) throws LogicException {
		logger.info("calculate  " + iCustNo + "-" + iFacmNo + ", size=" + lPfFac.size());

		BigDecimal addBonusLM = BigDecimal.ZERO; // 前月累計業績
		// 本工作月正業績，並累計前月業績
		for (PfReward iPf : lPfFac) {
			logger.info("calculate = " + iPf.toString());
			if (iPf.getWorkMonth() == iWorkMonth) {
				PfReward pfIt = (PfReward) dataLog.clone(iPf);
				pfIt.setIntroducerBonus(BigDecimal.ZERO);
				pfIt.setCoorgnizerBonus(BigDecimal.ZERO);
				pfIt.setIntroducerAddBonus(BigDecimal.ZERO);
				if (iPf.getIntroducerAddBonus().compareTo(BigDecimal.ZERO) > 0) {
					pfIt.setIntroducerAddBonus(iPf.getIntroducerAddBonus());
				}
				if (pfIt.getIntroducerAddBonus().compareTo(BigDecimal.ZERO) > 0) {
					lPfPlus.add(pfIt);
				}
			}
			if (iPf.getWorkMonth() < iWorkMonth) {
				addBonusLM = addBonusLM.add(iPf.getIntroducerAddBonus());
			}
		}
		// 無本月正業績，也無累計業績，則結束
		if (lPfPlus.size() == 0 && addBonusLM.compareTo(BigDecimal.ZERO) <= 0) {
			return;
		}

		// 執行房貸獎勵保費檢核
		processCnt++;
		PfInsCheck tPfInsCheck = pfCheckInsuranceCom.check(2, iCustNo, iFacmNo, iWorkMonth, titaVo); // 2.介紹人加碼獎勵津貼

		lPfInsCheck.add(tPfInsCheck);
		
		// 計算負業績，本工作月還款追回或房貸獎勵保費檢核追回(本工作月檢核結果為Y)
		// 1.本工作月撥款，檢核結果為Y要追回
		// 2.本工作月還款，檢核結果為N要追回(檢核結果為Y時已追回撥款，故還款不用追回)
		// 3.檢核結果為Y且檢核工作月為本月，追回前月累計
		for (PfReward iPf : lPfFac) {
			PfReward pfIt = (PfReward) dataLog.clone(iPf);
			pfIt.setIntroducerBonus(BigDecimal.ZERO);
			pfIt.setCoorgnizerBonus(BigDecimal.ZERO);
			pfIt.setIntroducerAddBonus(BigDecimal.ZERO);
			// 1.本工作月撥款，檢核結果為Y要追回
			if (iPf.getWorkMonth() == iWorkMonth && "Y".equals(tPfInsCheck.getCheckResult())) {
				if (iPf.getIntroducerAddBonus().compareTo(BigDecimal.ZERO) > 0) {
					pfIt.setIntroducerAddBonus(BigDecimal.ZERO.subtract(iPf.getIntroducerAddBonus()));
				}
				if (pfIt.getIntroducerAddBonus().compareTo(BigDecimal.ZERO) < 0) {
					lPfMinus.add(pfIt);
				}
			}
			// 2.本工作月還款，檢核結果為N要追回(檢核結果為Y時已追回撥款，故還款不用追回)
			if (iPf.getWorkMonth() == iWorkMonth && "N".equals(tPfInsCheck.getCheckResult())) {
				if (iPf.getIntroducerAddBonus().compareTo(BigDecimal.ZERO) < 0) {
					pfIt.setIntroducerAddBonus(iPf.getIntroducerAddBonus());
				}
				if (pfIt.getIntroducerAddBonus().compareTo(BigDecimal.ZERO) < 0) {
					lPfMinus.add(pfIt);
				}
			}
			// 3.檢核結果為Y且檢核工作月為本月，追回前月累計
			if (iPf.getWorkMonth() < iWorkMonth && "Y".equals(tPfInsCheck.getCheckResult()) && tPfInsCheck.getCheckWorkMonth() == iWorkMonth) {
				logger.info("calculate 3 addBonusLM =" + addBonusLM);
				// 追回前月業績，不超過前月累計
				if (iPf.getIntroducerAddBonus().compareTo(BigDecimal.ZERO) > 0) {
					if (addBonusLM.compareTo(iPf.getIntroducerAddBonus()) > 0) {
						pfIt.setIntroducerAddBonus(BigDecimal.ZERO.subtract(iPf.getIntroducerAddBonus()));
						addBonusLM = addBonusLM.subtract(iPf.getIntroducerAddBonus());
					} else {
						pfIt.setIntroducerAddBonus(BigDecimal.ZERO.subtract(addBonusLM));
						addBonusLM = BigDecimal.ZERO;
					}
				}
				if (pfIt.getIntroducerAddBonus().compareTo(BigDecimal.ZERO) < 0) {
					lPfMinus.add(pfIt);
				}
				logger.info("calculate 3  pfIt =" + pfIt.toString());
			}
		}

	}

	// 匯入發放檔
	private int toDo1(TitaVo titaVo) throws LogicException {
		String iWorkYM = titaVo.getParam("WorkMonth").trim();// 業績起始日
		int iDateFm = Integer.valueOf(titaVo.getParam("DateFm").trim()) + 19110000;// 業績起始日
		int iDateTo = Integer.valueOf(titaVo.getParam("DateTo").trim()) + 19110000;// 業績起始日
		int iBonusDate = Integer.valueOf(titaVo.getParam("BonusDate").trim());// 業績起始日

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
				if (pfReward.getIntroducerAddBonusDate() != 0) {
					continue;
				}

				if ("".equals(pfReward.getIntroducer().trim())) {
					continue;
				}

				// 獎金為0
				if (pfReward.getIntroducerAddBonus().compareTo(BigDecimal.ZERO) == 0) {
					continue;
				}
				
				if (pfReward.getRepayType() == 0 && pfReward.getIntroducerAddBonus().compareTo(BigDecimal.ZERO) < 0 ) {
					continue;
				}


				PfRewardMedia pfRewardMedia = new PfRewardMedia();

				pfRewardMedia.setBonusDate(iBonusDate);
				pfRewardMedia.setPerfDate(pfReward.getPerfDate());
				pfRewardMedia.setCustNo(pfReward.getCustNo());
				pfRewardMedia.setFacmNo(pfReward.getFacmNo());
				pfRewardMedia.setBormNo(pfReward.getBormNo());

				pfRewardMedia.setEmployeeNo(pfReward.getIntroducer());
				pfRewardMedia.setAdjustBonusDate(0);
				pfRewardMedia.setWorkMonth(pfReward.getWorkMonth());
				pfRewardMedia.setWorkSeason(pfReward.getWorkSeason());
				pfRewardMedia.setProdCode(pfReward.getProdCode());
				pfRewardMedia.setPieceCode(pfReward.getPieceCode());
				pfRewardMedia.setRemark("");
				pfRewardMedia.setMediaFg(0);
				pfRewardMedia.setMediaDate(0);
				pfRewardMedia.setManualFg(0);

//				PfItDetailId pfItDetailId = new PfItDetailId();
//
//				pfItDetailId.setPerfDate(pfReward.getPerfDate());
//				pfItDetailId.setCustNo(pfReward.getCustNo());
//				pfItDetailId.setFacmNo(pfReward.getFacmNo());
//				pfItDetailId.setBormNo(pfReward.getBormNo());

//				PfItDetail pfItDetail = pfItDetailService.findByTxFirst(pfReward.getCustNo(), pfReward.getFacmNo(), pfReward.getBormNo(), pfReward.getPerfDate(), pfReward.getRepayType(), pfReward.getPieceCode(), titaVo);
//				if (pfItDetail == null) {
//					pfRewardMedia.setProdCode("");
//					pfRewardMedia.setPieceCode("");
//				} else {
//					pfRewardMedia.setProdCode(pfItDetail.getProdCode());
//					pfRewardMedia.setPieceCode(pfItDetail.getPieceCode());
//				}

				pfRewardMedia.setBonusType(7);
				pfRewardMedia.setBonus(pfReward.getIntroducerAddBonus());
				pfRewardMedia.setAdjustBonus(pfReward.getIntroducerAddBonus());

				try {
					pfRewardMediaService.insert(pfRewardMedia, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", "獎金媒體發放檔");
				}

				pfReward.setIntroducerAddBonusDate(this.txBuffer.getTxCom().getTbsdy());

				try {
					pfRewardService.update(pfReward, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "介紹加碼獎金計算檔");
				}

				cnt++;
			}
		}

		// 出表

		String msg = "共匯入" + cnt + "筆資料，可至L5054查詢匯入資料,【報表及製檔】下傳檢核檔";

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L5054", iWorkYM + "9", msg, titaVo);
		return cnt;

	}

	// 產製娸體檔
	private int makeMedia(TitaVo titaVo) throws LogicException {
		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = 0;
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;// 查全部

		int iBonusDate = Integer.valueOf(titaVo.getParam("BonusDate").trim()) + 19110000;// 業績起始日

		String workYM = titaVo.getParam("WorkMonth").trim();// 業績起始日
		int iWorkYM = Integer.valueOf(workYM) + 191100;// 業績工作月

		List<Integer> typeList = new ArrayList<>();
		typeList.add(7);

		Slice<PfRewardMedia> slPfRewardMedia = pfRewardMediaService.findWorkMonth(iWorkYM, typeList, 0, this.index, this.limit, titaVo);
		List<PfRewardMedia> lPfRewardMedia = slPfRewardMedia == null ? null : slPfRewardMedia.getContent();

		makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5512.2", "介紹加碼獎金媒體檔", "TOTAL.txt", 2);
		int cnt = 0;
		if (lPfRewardMedia != null) {
			for (PfRewardMedia pfRewardMedia : lPfRewardMedia) {

//				if (pfRewardMedia.getMediaFg() == 1) {
//					continue;
//				}

				BigDecimal bbonus = pfRewardMedia.getAdjustBonus();
				bbonus = bbonus.setScale(0, bbonus.ROUND_FLOOR);

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
				s += "10H000"; // 申請單位代號(6)
				s += "0000000001"; // 申請批號(10)

				String employeeId = "";
				CdEmp cdEmp = cdEmpService.findById(pfRewardMedia.getEmployeeNo(), titaVo);
				if (cdEmp == null) {
					throw new LogicException(titaVo, "E0001", "員工編號=" + pfRewardMedia.getEmployeeNo());
				} else {
					employeeId = String.format("%-10s", cdEmp.getAgentId());
				}

				s += employeeId; // 身份證字號(10)
				s += "Q1"; // 薪碼(2)
				s += "放款獎勵津貼        "; // 薪碼說明

				if (bbonus.compareTo(BigDecimal.ZERO) < 0) {
					DecimalFormat df = new DecimalFormat("000000000");
					s += df.format(bbonus);// 金額(10)
				} else {
					DecimalFormat df = new DecimalFormat("0000000000");
					s += df.format(bbonus);// 金額(10)
				}

				s += "0000000000";// 業績(FYC)(10)
				s += "0000000000";// 業績(FYP)(10)
				s += String.format("%07d%03d", pfRewardMedia.getCustNo(), pfRewardMedia.getFacmNo()) + "000放款獎勵津貼               ";// 轉發明細(40)
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
			msg = "共產製 " + cnt + "筆媒體檔資料,請至【報表及製檔】作業,下傳【媒體檔】";
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", String.format("%-8s", titaVo.getTlrNo().trim())+"L5512", msg, titaVo);
		} else {
			msg = "共產製 " + cnt + "筆媒體檔資料";
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L5054", workYM + "9", msg, titaVo);
		}

//		makeFile.toFile(fileSno, "TOTAL.txt");

		return cnt;
	}

	// 取消娸體檔
	private int cancelMedia(TitaVo titaVo) throws LogicException {
		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = 0;
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;// 查全部

		String workYM = titaVo.getParam("WorkMonth").trim();
		int iWorkYM = Integer.valueOf(workYM) + 191100;// 業績工作月

		List<Integer> typeList = new ArrayList<>();
		typeList.add(7);

		Slice<PfRewardMedia> slPfRewardMedia = pfRewardMediaService.findWorkMonth(iWorkYM, typeList, 1, this.index, this.limit, titaVo);
		List<PfRewardMedia> lPfRewardMedia = slPfRewardMedia == null ? null : slPfRewardMedia.getContent();

		int cnt = 0;
		if (lPfRewardMedia != null) {
			for (PfRewardMedia pfRewardMedia : lPfRewardMedia) {
				PfRewardMedia pfRewardMedia2 = pfRewardMediaService.holdById(pfRewardMedia, titaVo);
				if (pfRewardMedia2 == null) {
					throw new LogicException(titaVo, "E0001", "獎金媒體發放檔");
				}

				pfRewardMedia2.setBonusDate(0);
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
		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L5054", workYM + "9", msg, titaVo);

		return cnt;
	}

}