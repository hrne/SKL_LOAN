package com.st1.itx.util.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBcm;
import com.st1.itx.db.domain.CdBonus;
import com.st1.itx.db.domain.CdBonusCo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CdPerformance;
import com.st1.itx.db.domain.CdPerformanceId;
import com.st1.itx.db.domain.CdPfParms;
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.domain.PfBsDetail;
import com.st1.itx.db.domain.PfBsOfficer;
import com.st1.itx.db.domain.PfBsOfficerId;
import com.st1.itx.db.domain.PfCoOfficer;
import com.st1.itx.db.domain.PfDetail;
import com.st1.itx.db.domain.PfItDetail;
import com.st1.itx.db.domain.PfReward;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.CdBonusCoService;
import com.st1.itx.db.service.CdBonusService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CdPerformanceService;
import com.st1.itx.db.service.CdPfParmsService;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.service.PfBsDetailService;
import com.st1.itx.db.service.PfBsOfficerService;
import com.st1.itx.db.service.PfCoOfficerService;
import com.st1.itx.db.service.PfDetailService;
import com.st1.itx.db.service.PfItDetailService;
import com.st1.itx.db.service.PfRewardService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.data.PfDetailVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 業績明細處理<BR>
 * addDetail 計算業績(撥款、提前償還追回、計件代碼變更) call by L3100, L3200, L3420, L3701<BR>
 * 1.寫入業績計算明細檔 PfDetail<BR>
 * 2.寫入介紹人業績明細檔 PfItDetail<BR>
 * 3.寫入房貸專員業績明細檔 PfBsDetail<BR>
 * 4.寫入介紹、協辦獎金發放檔 PfReward<BR>
 * 
 * 
 * @author st1
 *
 */
@Component("pfDetailCom")
@Scope("prototype")
public class PfDetailCom extends TradeBuffer {

	@Autowired
	CdWorkMonthService cdWorkMonthService;

	@Autowired
	CdPfParmsService cdPfParmsService;

	@Autowired
	CdPerformanceService cdPerformanceService;

	@Autowired
	CdBonusService cdBonusService;

	@Autowired
	CdBonusCoService cdBonusCoService;

	@Autowired
	PfDetailService pfDetailService;

	@Autowired
	PfItDetailService pfItDetailService;

	@Autowired
	PfBsDetailService pfBsDetailService;

	@Autowired
	PfBsOfficerService pfBsOfficerService;

	@Autowired
	PfCoOfficerService pfCoOfficerService;

	@Autowired
	PfRewardService pfRewardService;

	@Autowired
	public FacProdService facProdService;

	@Autowired
	public FacMainService facMainService;

	@Autowired
	public FacCaseApplService facCaseApplService;

	@Autowired
	CdEmpService cdEmpService;

	@Autowired
	CdBcmService cdBcmService;

	@Autowired
	Parse parse;

	@Autowired
	EmployeeCom employeeCom;

	@Autowired
	DateUtil dDateUtil;

	private TitaVo titaVo;
	private List<PfDetail> lPfDetail;
	private List<FacMain> lFacMain;
	private FacProd tFacProd;
	private FacCaseAppl tFacCaseAppl;
	private int perfDate; // 業績日期(中曆)
	private int perfDateF; // 業績日期(西元)
	private int workMonth; // 工作月(西元)
	private int workSeason; // 工作季(西元)
	private int workMonthDrawdown; // 撥款工作月(西元)
	private PfDetail pfDetail = new PfDetail();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 業績明細處理
	 * 
	 * @param iPf    PfDetailVo
	 * @param titaVo ..
	 * @throws LogicException ..
	 */
	public PfDetail addDetail(PfDetailVo iPf, TitaVo titaVo) throws LogicException {
		this.info("PfDetailCom addDetail ....." + iPf.toString());

		this.titaVo = titaVo;

		// 放行寫入業績明細，否則為試匴
		if (!titaVo.isActfgRelease()) {
			iPf.setTrial(true);
		}

		// 輸入檢核
		inputCheck(iPf);

		// 部分償還、提前結案，已攤還期數>=3期，則跳過不處理
		if ((iPf.getRepayType() == 2 || iPf.getRepayType() == 3)
				&& iPf.getRepaidPeriod() >= this.txBuffer.getSystemParas().getPerfBackPeriodE()) {
			this.info("iPf skip " + iPf.toString());
			return null;
		}

		// 業績日期(中曆)=輸入業績日期/系統營業日(會計日期)
		if (iPf.getPerfDate() > 0) {
			perfDate = iPf.getPerfDate();
		} else {
			perfDate = this.txBuffer.getTxCom().getTbsdy();
			iPf.setPerfDate(perfDate);
		}
		// 業績日期(西元)
		perfDateF = perfDate + 19110000;
		// 工作月(西曆)
		CdWorkMonth tCdWorkMonth = cdWorkMonthService.findDateFirst(perfDateF, perfDateF, titaVo);
		if (tCdWorkMonth == null)
			throw new LogicException(titaVo, "E0001", "CdWorkMonth 放款業績工作月對照檔，業績日期=" + perfDateF); // 查詢資料不存在
		else {
			workMonth = tCdWorkMonth.getYear() * 100 + tCdWorkMonth.getMonth();
		}
		// 工作季(西曆)
		if (tCdWorkMonth.getMonth() <= 3)
			workSeason = tCdWorkMonth.getYear() * 10 + 1;
		else if (tCdWorkMonth.getMonth() <= 6)
			workSeason = tCdWorkMonth.getYear() * 10 + 2;
		else if (tCdWorkMonth.getMonth() <= 9)
			workSeason = tCdWorkMonth.getYear() * 10 + 3;
		else
			workSeason = tCdWorkMonth.getYear() * 10 + 4;

		// 額度起止編號
		int facmNoS = 1;
		int facmNoE = 999;

		// Load 額度檔
		Slice<FacMain> slFacMain = facMainService.facmCustNoRange(iPf.getCustNo(), iPf.getCustNo(), facmNoS, facmNoE,
				this.index, Integer.MAX_VALUE, titaVo);
		lFacMain = slFacMain == null ? null : slFacMain.getContent();
		if (lFacMain == null) {
			throw new LogicException(titaVo, "E0001", "FacMain 額度檔，CustNo=" + iPf.getCustNo()); // 查詢資料不存在
		}
		// 理財型房貸只算首撥業績
		// 商品參數檔理財型房貸=Y且撥款序號>=2，則跳過不處理
		for (FacMain tFacMain : lFacMain) {
			if (iPf.getFacmNo() == tFacMain.getFacmNo()) {
				tFacProd = facProdService.findById(tFacMain.getProdNo(), titaVo);
				if (tFacProd != null && "Y".equals(tFacProd.getFinancialFlag()) && iPf.getBormNo() >= 2) {
					this.info("pf FinancialFlag=Y " + iPf.toString());
					return null;
				}
			}
		}

		// Load 業績計算明細檔
		this.info("PfDetailCom load lPfDetail");
		Slice<PfDetail> slPfDetail = pfDetailService.findFacmNoRange(iPf.getCustNo(), facmNoS, facmNoE, 0, workMonth,
				this.index, Integer.MAX_VALUE, titaVo);
		lPfDetail = slPfDetail == null ? new ArrayList<PfDetail>() : new ArrayList<PfDetail>(slPfDetail.getContent());

		// 撥款工作月(為參數工作月)
		// 提前還款及變更計件代碼須有撥款業績資料
		workMonthDrawdown = 0;
		if (iPf.getRepayType() > 0) {
			for (PfDetail pf : lPfDetail) {
				if (iPf.getFacmNo() == pf.getFacmNo() && iPf.getBormNo() == pf.getBormNo() && pf.getRepayType() == 0) {
					workMonthDrawdown = pf.getWorkMonth();
				}
			}
			if (workMonthDrawdown == 0) {
				return null;
			}
		}
		if (iPf.getRepayType() == 0) {
			workMonthDrawdown = workMonth;
		}

		// 計件代碼變更，業績日期為撥款業績日期；不可跨撥款工作月；不可有提前還款
		BigDecimal extraRepay = BigDecimal.ZERO;
		if (iPf.getRepayType() == 1) {
			for (PfDetail pf : lPfDetail) {
				if (iPf.getFacmNo() == pf.getFacmNo() && iPf.getBormNo() == pf.getBormNo()) {
					if (pf.getRepayType() == 0) {
						if (pf.getWorkMonth() != workMonth) {
							throw new LogicException(titaVo, "E0015",
									"變更計件代碼，不可跨撥款工作月" + pf.getWorkMonth() + ", " + workMonth); // 檢查錯誤
						} else {
							iPf.setPerfDate(pf.getPerfDate());
						}
					}
					if ((pf.getRepayType() == 2 || pf.getRepayType() == 3)) {
						extraRepay = extraRepay.add(pf.getDrawdownAmt());
					}
				}
			}
			if (extraRepay.compareTo(BigDecimal.ZERO) != 0) {
				throw new LogicException(titaVo, "E0015", "已提前還款，不可變更計件代碼"); // 檢查錯誤
			}
		}

		// 業績日期
		perfDate = iPf.getPerfDate();
		perfDateF = perfDate + 19110000;

		// 訂正交易
		if (titaVo.isHcodeErase()) {
			// 撥款訂定刪除
			if (iPf.getRepayType() == 0) {
				deleteDetail(iPf);
				return null;
			} else {
				// 訂正交易
				processErase(iPf);
			}
		}
		// 正常交易
		else {
			switch (iPf.getRepayType()) {

			case 0:// 撥款
				processDrawDown(iPf);
				break;

			case 1:// 計件代碼變更
				processReverse(iPf); // 沖原計件代碼
				processDrawDown(iPf); // 入新計件代碼金額
				break;

			case 2:// 部分償還
			case 3:// 提前結案
				processExtraRepay(iPf); // 提前還款
				break;
			}

		}

		// END
		return pfDetail;
	}

	// 撥款交易
	private void processDrawDown(PfDetailVo iPf) throws LogicException {
		if (iPf.getPieceCodeSecondAmt().compareTo(BigDecimal.ZERO) > 0) {
			pfDetail = procPfDetail(iPf, iPf.getPieceCodeSecond(), iPf.getPieceCodeSecondAmt());
		}
		if (iPf.getPieceCodeSecondAmt().compareTo(iPf.getDrawdownAmt()) < 0) {
			pfDetail = procPfDetail(iPf, iPf.getPieceCode(),
					iPf.getDrawdownAmt().subtract(iPf.getPieceCodeSecondAmt()));
		}
	}

	// 訂正交易
	private void processErase(PfDetailVo iPf) throws LogicException {
		List<PfDetail> lReverse = new ArrayList<PfDetail>(lPfDetail);
		// 排序 (後進先沖)
		Collections.sort(lReverse, new Comparator<PfDetail>() {
			public int compare(PfDetail c1, PfDetail c2) {
				if (c1.getLogNo() != c2.getLogNo()) {
					return (c1.getLogNo() > c2.getLogNo() ? -1 : 1);
				}
				return 0;
			}
		});
		for (PfDetail pf : lReverse) {
			if (iPf.getFacmNo() == pf.getFacmNo() && iPf.getBormNo() == pf.getBormNo()) {
				if (pf.getBorxNo() == iPf.getBorxNo()) {
					pfDetail = procPfDetail(iPf, pf.getPieceCode(), BigDecimal.ZERO.subtract(pf.getDrawdownAmt()));
				}
			}
		}
	}

	// 沖回交易
	private void processReverse(PfDetailVo iPf) throws LogicException {
		List<PfDetail> lReverse = new ArrayList<PfDetail>(lPfDetail);
		// 排序 (後進先沖)
		Collections.sort(lReverse, new Comparator<PfDetail>() {
			public int compare(PfDetail c1, PfDetail c2) {
				if (c1.getLogNo() != c2.getLogNo()) {
					return (c1.getLogNo() > c2.getLogNo() ? -1 : 1);
				}
				return 0;
			}
		});
		for (PfDetail pf : lReverse) {
			if (iPf.getFacmNo() == pf.getFacmNo() && iPf.getBormNo() == pf.getBormNo()) {
				if (pf.getDrawdownAmt().compareTo(BigDecimal.ZERO) != 0) {
					pfDetail = procPfDetail(iPf, pf.getPieceCode(), BigDecimal.ZERO.subtract(pf.getDrawdownAmt()));
				}
			}
		}
	}

	// 提前還款
	private void processExtraRepay(PfDetailVo iPf) throws LogicException {
		List<PfDetail> lExtraRepay = new ArrayList<PfDetail>(lPfDetail);
		// 將已還款金額累計至撥款那筆
		for (PfDetail pf : lExtraRepay) {
			if (iPf.getFacmNo() == pf.getFacmNo() && iPf.getBormNo() == pf.getBormNo()) {
				if (pf.getRepayType() > 0) {
					for (PfDetail pf0 : lExtraRepay) {
						if (pf.getRepayType() == 0) {
							pf0.setDrawdownAmt(pf0.getDrawdownAmt().add(pf.getDrawdownAmt()));
							pf.setDrawdownAmt(BigDecimal.ZERO);
						}
					}
				}
			}
		}
		// 排序 (介紹獎金、換算業績獎金)低的計件代碼者優先回沖
		Collections.sort(lExtraRepay, new Comparator<PfDetail>() {
			public int compare(PfDetail c1, PfDetail c2) {
				if (c1.getItBonus().compareTo(c1.getItBonus()) != 0) {
					return (c1.getItBonus().compareTo(c2.getItBonus()) < 0 ? -1 : 1);
				}
				if (c1.getItPerfEqAmt().compareTo(c1.getItPerfEqAmt()) != 0) {
					return (c1.getItPerfEqAmt().compareTo(c2.getItPerfEqAmt()) < 0 ? -1 : 1);
				}
				return 0;
			}
		});
		BigDecimal remainderAmt = iPf.getDrawdownAmt();

		for (PfDetail pf : lExtraRepay) {
			if (iPf.getFacmNo() == pf.getFacmNo() && iPf.getBormNo() == pf.getBormNo()) {
				if (pf.getDrawdownAmt().compareTo(BigDecimal.ZERO) > 0 && remainderAmt.compareTo(BigDecimal.ZERO) > 0) {
					if (pf.getDrawdownAmt().compareTo(remainderAmt) > 0) {
						pfDetail = procPfDetail(iPf, pf.getPieceCode(), BigDecimal.ZERO.subtract(remainderAmt)); // 追回金額
						remainderAmt = BigDecimal.ZERO;
					} else {
						pfDetail = procPfDetail(iPf, pf.getPieceCode(), BigDecimal.ZERO.subtract(pf.getDrawdownAmt()));
						remainderAmt = remainderAmt.subtract(pf.getDrawdownAmt());
					}
				}
			}
		}
	}

	private PfDetail procPfDetail(PfDetailVo iPf, String pieceCode, BigDecimal drawdownAmt) throws LogicException {
		PfDetail pf = new PfDetail();
		// 1.計件代碼變更 -> 0.撥款(計件代碼變更)
		pf.setRepayType(iPf.getRepayType());
		if (iPf.getRepayType() == 1) {
			pf.setRepayType(0);
		}
		pf.setPerfDate(iPf.getPerfDate());
		pf.setCustNo(iPf.getCustNo());
		pf.setFacmNo(iPf.getFacmNo());
		pf.setBormNo(iPf.getBormNo());
		pf.setBorxNo(iPf.getBorxNo());
		pf.setDrawdownAmt(drawdownAmt);
		pf.setPieceCode(pieceCode);
		pf.setDrawdownDate(iPf.getDrawdownDate());
		pf.setRepaidPeriod(iPf.getRepaidPeriod());
		pf.setIsReNewEmpUnit(iPf.getEmpResetFg());
		for (FacMain tFacMain : lFacMain) {
			if (pf.getFacmNo() == tFacMain.getFacmNo()) {
				pf.setProdCode(tFacMain.getProdNo()); // 商品代碼
				pf.setCreditSysNo(tFacMain.getCreditSysNo()); // 案件編號 (徵審系統案號)
				pf.setIntroducer(tFacMain.getIntroducer());// 介紹人
				pf.setBsOfficer(tFacMain.getBusinessOfficer()); // 房貸專員, 放款業務專員
				pf.setCoorgnizer(tFacMain.getCoorgnizer());// 協辦人
			}
		}

		// 逐筆計算業績並更新業績明細檔
		pf = procCompute(pf);

		// 是否為試匴
		if (iPf.isTrial()) {
			return pf;
		}

		/* 寫入業績明細計算檔 */
		updDetail(pf);

		// 寫入介紹人業績明細檔
		updItDetail(pf);

		// 寫入房貸專員業績明細檔
		updBsDetail(pf);

		// 寫入介紹、協辦獎金發放檔
		updReward(pf);

		// add to List
		lPfDetail.add(pf);

		return pf;
	}

	/*---------- input Check ----------*/
	public void inputCheck(PfDetailVo iPfList) throws LogicException {
		this.info("PfDetailCom inputCheck");

	}

	/*---------- 計算業績 ----------*/
	private PfDetail procCompute(PfDetail pf) throws LogicException {
		this.info("PfDetailCom procCompute ... " + pf.toString());
// ---------------- 讀取計算參數 ------------------------
		// CdPerformance 業績件數及金額核算標準設定檔
		int workMonthCd; // 適用參數工作月= 不大於 撥款工作月的最新一筆
		CdPerformance tCd = cdPerformanceService.findWorkMonthFirst(workMonthDrawdown, titaVo);
		if (tCd == null) {
			throw new LogicException(titaVo, "E0001", "CdPerformance 業績標準設定檔" + workMonthDrawdown); // 查詢資料不存在
		}
		workMonthCd = tCd.getWorkMonth();
		tCd = cdPerformanceService.findById(new CdPerformanceId(workMonthCd, pf.getPieceCode()), titaVo);
		if (tCd == null) {
			throw new LogicException(titaVo, "E0001", "CdPerformance 業績標準設定檔，計件代碼=" + pf.getPieceCode()); // 查詢資料不存在
		}
// 新貸件(代碼2&B)：核定日起6個月內，就未曾動用之額度撥款者，核發介紹獎金予原額度介紹人。介紹單位及房貸專員之業績計入撥款當月，但不計算件數。
		// 新貸件(代碼2&B)：參數設定為不計件數(業績件數及金額核算標準)，系統要處理6個月內撥款就算1件
		if ("2".equals(pf.getPieceCode()) || "B".equals(pf.getPieceCode())) {
			tCd = specialSettingCd(pf, tCd);
		}
		this.info("PfDetailCom CdPerformance=" + tCd.toString());

// ---------------- 設定業績明細欄位值 ------------------------
		pf = procPfDetail(pf);

// ---------------- 分別取得額度累計計算金額(介紹人、房貸專員)  ------------------------
//撥款後計息期間未逾一個月即結清(含部分還款達60萬元)案件，依還款金額追回各級人員介紹獎金和協辦津貼等各項獎勵。
//增訂已繳1期但未繳足3期期款即結清(含部分還款達60萬元)案件，追回換算業績(業務報酬)及介紹獎金。

// 追回未逾一個月即結清或繳納1期但未繳足3期期款即結清（含部分還款達60萬之案件)，未曾繳款者則同時追回房貸專員業績
		// 取得額度累計計算金額(介紹人)
		BigDecimal computeItAmtFac = getComputeAmtFac(1, pf);
		pf.setComputeItAmtFac(computeItAmtFac);

		// 取得額度累計計算金額(房貸專員)
		BigDecimal computeBsAmtFac = getComputeAmtFac(2, pf);
		pf.setComputeBsAmtFac(computeBsAmtFac);

// ---------------- 以額度累計計算金額(介紹人)計算介紹單位業績  ------------------------

//  介紹單位    UnitCnt    UnitAmtCond     UnitPercent                                                
//case        件數   &    撥款累計條件       撥款業績比例      
//1           1.0        >= 600,000         1.0
//2           0                             1.0
//3           0                             1.0
//4           0                             1.0
//5           0                             0 
//A           1.0        >= 600,000         1.0
//B           0                             1.0
//C           0                             1.0
//D           0                             1.0
//E           0                             0 
//6           0                             0 
//7           0                             0
//8           1.0       >= 600,000          1.0
// 介紹單位及房貸專員件數，同一eLoan 案件編號累積以1件為限

// 介紹單位件數 (有門檻,以整個額度計算)
		// ItPerfCnt 件數 = 標準件數(if 撥款追回金額 < 計件金額門檻 then 0 else 1)
		this.info("PfDetailCom Compute computeItAmtFac=" + computeItAmtFac);
		if (tCd.getUnitCnt().compareTo(BigDecimal.ZERO) > 0) {
			if (computeItAmtFac.compareTo(BigDecimal.ZERO) <= 0
					|| computeItAmtFac.compareTo(tCd.getUnitAmtCond()) < 0) {
				pf.setItPerfCnt(BigDecimal.ZERO);
			} else {
				pf.setItPerfCnt(tCd.getUnitCnt());
			}
		}

// 介紹單位業績(單筆計算)
		// ItPerfAmt 業績金額 = 撥款追回金額 * 撥款業績比例
		pf.setItPerfAmt(pf.getDrawdownAmt().multiply(tCd.getUnitPercent()).setScale(0, RoundingMode.HALF_UP));

// ---------------- 以額度累計計算金額(介紹人)計算介紹人業績  ------------------------

//  介紹人  introdPerccent introdAmtCond introdPfEqAmt introdPfEqBase   introdReward  introdRewardBase                                     
//case        介紹獎金比例&介紹獎金門檻    換算業績獎金    換算業績金額基底     業務報酬獎金   業務報酬金額基準        
//1           0.001                         35        10,000              12.5         10,000 
//2           0.001                         35        10,000              12.5         10,000
//3                                         35        10,000              12.5         10,000
//4           0.001        >= 500,000       35        10,000              12.5         10,000
//5                                                                   
//A           0.0002                        7         10,000              2.5          10,000
//B           0.0002                        7         10,000              2.5          10,000
//C                                         7         10,000              2.5          10,000
//D           0.0002       >= 500,000       7         10,000              2.5          10,000
//E                                                                 
//6                                                                   
//7                                                               
//8                                         35        10,000              12.5         10,000 

// 介紹獎金(有門檻,以整個額度計算)
		this.info("PfDetailCom procCompute itBonus");

		BigDecimal itBonusFac = BigDecimal.ZERO;

		// 累計原先同額度介紹獎金
		if (lPfDetail != null) {
			for (PfDetail rw : lPfDetail) {
				if (rw.getFacmNo() == pf.getFacmNo() && rw.getPieceCode().equals(pf.getPieceCode())) {
					itBonusFac = itBonusFac.add(rw.getItBonus());
				}
			}
		}
		// ItBonus 介紹獎金 = 介紹獎金比例(if 撥款追回金額 < 介紹獎金門檻 then 0) - 已計介紹獎金
		if (computeItAmtFac.compareTo(tCd.getIntrodAmtCond()) < 0)
			pf.setItBonus(BigDecimal.ZERO.subtract(itBonusFac));
		else
			pf.setItBonus((computeItAmtFac.multiply(tCd.getIntrodPerccent()).setScale(0, RoundingMode.HALF_UP))
					.subtract(itBonusFac));

// 換算業績(有基底,單筆計算)
// 業務報酬(有基底,單筆計算)

		// ItPerfEqAmt 換算業績 = (撥款追回金額 / 換算業績金額基底) * 換算業績獎金
		if (tCd.getIntrodPfEqBase().compareTo(BigDecimal.ZERO) == 0) {
			pf.setItPerfEqAmt(BigDecimal.ZERO);
		} else {
			pf.setItPerfEqAmt(pf.getDrawdownAmt().divide(tCd.getIntrodPfEqBase()).multiply(tCd.getIntrodPfEqAmt())
					.setScale(0, RoundingMode.HALF_UP));
		}

		// ItPerfReward 業務報酬 = (撥款追回金額 / 業務報酬金額基底) * 業務報酬獎金
		if (tCd.getIntrodRewardBase().compareTo(BigDecimal.ZERO) == 0) {
			pf.setItPerfReward(BigDecimal.ZERO);
		} else {
			pf.setItPerfReward(pf.getDrawdownAmt().divide(tCd.getIntrodRewardBase()).multiply(tCd.getIntrodReward())
					.setScale(0, RoundingMode.HALF_UP));
		}

// ---------------- 以額度累計撥款金額計算介紹人加碼獎勵津貼  ------------------------

		// 介紹人加碼獎勵津貼
		pf = procItAddBonus(pf);

// ---------------- 以額度累計撥款金額計算協辦人員協辦獎金  ------------------------

		// 計算協辦人員協辦獎金
		if (pf.getCoorgnizer().trim().length() > 0) {
			pf = procCoBonus(pf);
		}

// ---------------- 以額度累計計算金額(房貸專員)計算房貸專員業績  ------------------------
//  房貸專員  bsOffrCnt       bsOffrAmtCond 
//                 bsOffrCntLimit		      BsOffrCntAmt    bsOffrPerccent
//case      基準件數 & 最高件數 &  計件金額門檻   &   折算件數金額基底    撥款業績比例      
//1           1.0    1.0      >= 600,000                             1.0
//2           0      0                                               1.0
//3           0.1    1.0                        100,000              1.0
//4           0.1    1.0                        100,000              1.0
//5           1      1.0      >= 600,000                             0 
//A           1.0    1.0      >= 600,000                             1.0
//B           0      0                                               1.0
//C           0.1    1.0                        100,000              1.0
//D           0.1    1.0                        100,000              1.0
//E           1      1.0     >= 600,000                              0 
//6           0.1    1.0                        100,000              0 
//7           0      0                                               0
//8           1.0    1.0     >= 600,000                              1.0		
// 介紹單位及房貸專員件數，同一eLoan 案件編號累積以1件為限

		this.info("PfDetailCom procCompute bsCnt");
		// 累計業績件數
		BigDecimal bsPerfCntFac = BigDecimal.ZERO;
		// 累計原先同一案件編號業績件數
		if (lPfDetail != null) {
			for (PfDetail bs : lPfDetail) {
				if ((pf.getCreditSysNo() > 0 && bs.getCreditSysNo() == pf.getCreditSysNo())
						|| (pf.getCreditSysNo() == 0 && bs.getFacmNo() == pf.getFacmNo())) {
					bsPerfCntFac = bsPerfCntFac.add(bs.getBsPerfCnt());
				}
			}
		}

// 房貸專員件數(有門檻或基底,以整個額度計算)				
		this.info("PfDetailCom Compute bsCntFac=" + bsPerfCntFac);
// BsPerfCnt 件數 =
// if (基底金額為0 , if 撥款追回金額 < 計件金額門檻 then 0, else 基準件數) else minimum(基準件數 * 撥款追回金額 / 折算件數金額基底), 最高件數)
// - 已計件數
		// 基底金額為0者
		if (tCd.getBsOffrCnt().compareTo(BigDecimal.ZERO) > 0) {
			if (tCd.getBsOffrCntAmt().equals(BigDecimal.ZERO))
				// 額度累計計算金額 <= 0
				// 或 額度累計計算金額 < 撥款累計條件
				if (computeBsAmtFac.compareTo(BigDecimal.ZERO) <= 0
						|| computeBsAmtFac.compareTo(tCd.getBsOffrAmtCond()) < 0)
					// 房貸專員件數計為0 - 已計件數
					pf.setBsPerfCnt(BigDecimal.ZERO.subtract(bsPerfCntFac));
				else
					// 房貸專員件數為計件件數 - 已計件數
					pf.setBsPerfCnt(tCd.getBsOffrCnt().subtract(bsPerfCntFac));
			else {
				// 基底金額不為0者
				pf.setBsPerfCnt((computeBsAmtFac.divide(tCd.getBsOffrCntAmt()).setScale(0, RoundingMode.DOWN))
						.multiply(tCd.getBsOffrCnt()).min(tCd.getBsOffrCntLimit()).subtract(bsPerfCntFac));
			}
		}
// 房貸專員業績(單筆計算)				
		this.info("PfDetailCom procCompute BsPerfAmt");
		// 房貸專員業績 = 額度累計計算金額 * 撥款業績比例
		pf.setBsPerfAmt((pf.getDrawdownAmt().multiply(tCd.getBsOffrPerccent()).setScale(0, RoundingMode.HALF_UP)));

		// END
		this.info("PfDetailCom procCompute END pf =" + pf.toString());

		return pf;
	}

	//
	private BigDecimal getComputeAmtFac(int kind, PfDetail pf) throws LogicException {
		// kind = 1.介紹人額度累計計算金額，扣除未逾一個月即結清及繳納1期但未繳足3期期款即結清（含部分還款達60萬之案件)
		// kind = 2.房貸專員額度累計計算金額，追回未逾一個月即結清(含部分還款達60萬元),
		// 累計撥款金額
		BigDecimal drawDownAmtFac = BigDecimal.ZERO;
		// 累計提前償還金額
		BigDecimal preRepayAmtFac = BigDecimal.ZERO;

		// 累計原先同額度之累計撥款金額、累計提前償還金額
		if (lPfDetail != null) {
			for (PfDetail it : lPfDetail) {
				if (it.getFacmNo() == pf.getFacmNo() && it.getPieceCode().equals(pf.getPieceCode())) {
					if (it.getRepayType() == 0) {
						drawDownAmtFac = drawDownAmtFac.add(it.getDrawdownAmt());
					}
					if (it.getRepayType() == 2 || it.getRepayType() == 3) {
						if ((kind == 1 && it.getRepaidPeriod() <= this.txBuffer.getSystemParas().getPerfBackPeriodE())
								|| (kind == 2 && it.getRepaidPeriod() == 0)) {
							preRepayAmtFac = preRepayAmtFac.add(it.getDrawdownAmt());
						}
					}
				}
			}
		}

		// 加本次已計同額度之累計撥款金額、累計提前償還金額
		if (pf.getRepayType() == 0) {
			drawDownAmtFac = drawDownAmtFac.add(pf.getDrawdownAmt());
		}
		if (pf.getRepayType() == 2 || pf.getRepayType() == 3) {
			if ((kind == 1 && pf.getRepaidPeriod() <= this.txBuffer.getSystemParas().getPerfBackPeriodE())
					|| (kind == 2 && pf.getRepaidPeriod() == 0)) {
				preRepayAmtFac = preRepayAmtFac.add(pf.getDrawdownAmt());
			}
		}
		// 額度累計計算金額
		BigDecimal computeAmtFac = BigDecimal.ZERO;
		// 結案或部分還款達60萬之案件，追回提前償還金額
		if (drawDownAmtFac.add(preRepayAmtFac).compareTo(BigDecimal.ZERO) <= 0) {
			computeAmtFac = BigDecimal.ZERO;
		} else if (preRepayAmtFac.add(this.txBuffer.getSystemParas().getPerfBackRepayAmt())
				.compareTo(BigDecimal.ZERO) <= 0) {
			computeAmtFac = drawDownAmtFac.add(preRepayAmtFac);
		} else {
			computeAmtFac = drawDownAmtFac;
		}

		return computeAmtFac;
	}

	/* 介紹人加碼獎勵津貼 */
	private PfDetail procItAddBonus(PfDetail pf) throws LogicException {

		// CdBonus 介紹人加碼獎勵津貼標準設定
		int workMonthCd; // 適用工作年月
		CdBonus tCdBonus = cdBonusService.findWorkMonthFirst(workMonthDrawdown, titaVo);
		if (tCdBonus == null) {
			throw new LogicException(titaVo, "E0001", "CdBonus 介紹人加碼獎勵津貼標準設定" + workMonthDrawdown); // 查詢資料不存在
		}
		workMonthCd = tCdBonus.getWorkMonth();
		Slice<CdBonus> slCdBonus = cdBonusService.findYearMonth(workMonthCd, workMonthCd, this.index, Integer.MAX_VALUE,
				titaVo); // 全部
		List<CdBonus> lCdBonus = slCdBonus == null ? null : slCdBonus.getContent();

		if (lCdBonus == null || !isAddBonus(pf.getPieceCode(), lCdBonus)) {
			return pf;
		}
// ConditionCode	條件記號
//  1.篩選條件-計件代碼
//	       以計件代碼為篩選條件，可選擇多筆計件代碼；
//  3.金額級距 
//	       勵津貼發放條件為新貸案件及撥貸金額達到一定級距時可領取該級距的津貼：		
//		撥貸金額60萬以上 ~ 500(含)萬，津貼200元
//		撥貸金額500萬以上~1000(含)萬，津貼500元
//		撥貸金額1000萬以上          ，津貼1200元
// 同一額度、同一撥款工作月累計計算  
// 撥款後計息期間未逾一個月即結清(含部分還款達60萬元)案件，依還款金額追回各級人員介紹獎金和協辦津貼等各項獎勵。
		BigDecimal drawDownAmtFac = BigDecimal.ZERO;
		BigDecimal preRepayAmtFac = BigDecimal.ZERO;
		BigDecimal addBonusFac = BigDecimal.ZERO;
		if (lPfDetail != null) {
			for (PfDetail it : lPfDetail) {
				if (isAddBonus(it.getPieceCode(), lCdBonus)) {
					if (it.getFacmNo() == pf.getFacmNo() && it.getWorkMonth() == workMonthDrawdown) {
						addBonusFac = addBonusFac.add(it.getItAddBonus());
						if (it.getRepayType() == 0) {
							drawDownAmtFac = drawDownAmtFac.add(it.getDrawdownAmt());
						}
						if (it.getRepaidPeriod() == 0) {
							if (it.getRepayType() == 2 || it.getRepayType() == 3) {
								preRepayAmtFac = preRepayAmtFac.add(it.getDrawdownAmt());
							}
						}
					}
				}
			}
		}
		BigDecimal computeAddBonusAmt = BigDecimal.ZERO;
		// 額度累計計算金額
		// 累計本次撥款金額、提前償還金額
		if (pf.getRepayType() == 0) {
			drawDownAmtFac = drawDownAmtFac.add(pf.getDrawdownAmt());
			computeAddBonusAmt = drawDownAmtFac;
		}
		if (pf.getRepaidPeriod() == 0) {
			if (pf.getRepayType() == 2 || pf.getRepayType() == 3) {
				preRepayAmtFac = preRepayAmtFac.add(pf.getDrawdownAmt());
			}
			if (drawDownAmtFac.add(preRepayAmtFac).compareTo(BigDecimal.ZERO) <= 0) {
				computeAddBonusAmt = BigDecimal.ZERO;
			} else if (preRepayAmtFac.add(this.txBuffer.getSystemParas().getPerfBackRepayAmt())
					.compareTo(BigDecimal.ZERO) <= 0) {
				computeAddBonusAmt = drawDownAmtFac.add(preRepayAmtFac);
			} else {
				computeAddBonusAmt = drawDownAmtFac;
			}
		}

		// 同一額度撥款累計扣除未逾一個月即結清(含部分還款達60萬元)
		if (drawDownAmtFac.add(preRepayAmtFac).compareTo(BigDecimal.ZERO) <= 0) {
			computeAddBonusAmt = BigDecimal.ZERO;
		} else if (preRepayAmtFac.add(this.txBuffer.getSystemParas().getPerfBackRepayAmt())
				.compareTo(BigDecimal.ZERO) <= 0) {
			computeAddBonusAmt = drawDownAmtFac.add(preRepayAmtFac);
		} else {
			computeAddBonusAmt = drawDownAmtFac;
		}

		// 計算加碼獎勵津貼
		pf.setComputeAddBonusAmt(computeAddBonusAmt);
		BigDecimal itAddBonus = BigDecimal.ZERO;
		for (CdBonus cd : lCdBonus) {
			if (cd.getConditionCode() == 3) {
				if (computeAddBonusAmt.compareTo(cd.getAmtStartRange()) >= 0
						&& computeAddBonusAmt.compareTo(cd.getAmtEndRange()) <= 0)
					itAddBonus = cd.getBonus();
			}
		}
		// 加碼獎勵 = 新加碼獎勵 - 已計加碼獎勵
		pf.setItAddBonus(itAddBonus.subtract(addBonusFac));

		return pf;
	}

	// 計件代碼，是否計入
	private boolean isAddBonus(String pieceCode, List<CdBonus> lCdBonus) {
		boolean piceInclude = false; // 篩選條件-計件代碼，是否計入，預設false
		boolean isAddBonus = false;
		for (CdBonus cd : lCdBonus) {
			if (cd.getConditionCode() == 1) {
				if (pieceCode.equals(cd.getCondition()))
					piceInclude = true;
			}
		}
		if (piceInclude) {
			isAddBonus = true;
		}
		return isAddBonus;
	}

	/* 計算協辦人員協辦獎金 */
	private PfDetail procCoBonus(PfDetail pf) throws LogicException {
		this.info("PfDetailCom compCoBonus ");
		// 生效日期<=撥款日<停效日期
		PfCoOfficer tPfCoOfficer = pfCoOfficerService.EffectiveDateFirst(pf.getCoorgnizer(), 0,
				pf.getDrawdownDate() + 19110000, titaVo);
		if (tPfCoOfficer == null || pf.getDrawdownDate() >= tPfCoOfficer.getIneffectiveDate()) {
			return pf;
		}
		// CdBonusCo 協辦獎金標準設定
		int workMonthCd; // 適用工作年月
		CdBonusCo TCdBonusCo = cdBonusCoService.findWorkMonthFirst(workMonthDrawdown, titaVo);
		if (TCdBonusCo == null) {
			throw new LogicException(titaVo, "E0001", "CdBonusCo 協辦獎金標準設定" + workMonthDrawdown); // 查詢資料不存在
		}
		workMonthCd = TCdBonusCo.getWorkMonth();
		Slice<CdBonusCo> slCdBonusCo = cdBonusCoService.findYearMonth(workMonthCd, workMonthCd, this.index,
				Integer.MAX_VALUE, titaVo); // 全部
		List<CdBonusCo> lCdBonusCo = slCdBonusCo == null ? null : slCdBonusCo.getContent();
		// 計件代碼，不計入
		if (lCdBonusCo == null || !isCoBonus(pf.getPieceCode(), lCdBonusCo)) {
			return pf;
		}

// 條件記號 ConditionCode
//   篩選條件-計件代碼2.協辦等級 

// 標準條件 Condition
//   條件記號=1時為計件代碼1位(1、2、A、B)
//   條件記號=2時為協辦等級1位(1:初級、2:中級、3:高級) 

// 標準金額 conditionAmt
//   條件記號=1時輸入新貸案件撥貸金額60萬以上  

// 獎勵津貼  bonus
//   初級   800  中級 1,000  高級 1,200 

// 獎勵津貼-初階授信通過	classPassBonus		
//   初級   900中級 1,100高級 1,300 		

// 同一額度、同一撥款工作月累計計算
// 撥款後計息期間未逾一個月即結清(含部分還款達60萬元)案件，依還款金額追回各級人員介紹獎金和協辦津貼等各項獎勵
// 依同一額度累計計算，追回未逾一個月即結清(含部分還款達60萬元)

		BigDecimal drawDownAmtFac = BigDecimal.ZERO;
		BigDecimal preRepayAmtFac = BigDecimal.ZERO;
		BigDecimal coBonusFac = BigDecimal.ZERO;
		// 累計原先同一案件累計撥款金額、累計提前償還金額(繳期數=0)、協辦獎金
		if (lPfDetail != null) {
			for (PfDetail it : lPfDetail) {
				if (isCoBonus(it.getPieceCode(), lCdBonusCo)) {
					if (it.getFacmNo() == pf.getFacmNo() && it.getWorkMonth() == workMonthDrawdown) {
						coBonusFac = coBonusFac.add(it.getCoorgnizerBonus());
						if (it.getRepayType() == 0) {
							drawDownAmtFac = drawDownAmtFac.add(it.getDrawdownAmt());
						}
						if (it.getRepaidPeriod() == 0) {
							if (it.getRepayType() == 2 || it.getRepayType() == 3) {
								preRepayAmtFac = preRepayAmtFac.add(it.getDrawdownAmt());
							}
						}
					}
				}
			}
		}
		// 累計本次撥款金額、提前償還金額
		if (pf.getRepayType() == 0) {
			drawDownAmtFac = drawDownAmtFac.add(pf.getDrawdownAmt());
		}
		if (pf.getRepaidPeriod() == 0) {
			if (pf.getRepayType() == 2 || pf.getRepayType() == 3) {
				preRepayAmtFac = preRepayAmtFac.add(pf.getDrawdownAmt());
			}
		}

		BigDecimal computeCoBonusAmt = BigDecimal.ZERO;
		// 同一額度撥款累計扣除未逾一個月即結清(含部分還款達60萬元)
		// 額度累計計算金額
		if (drawDownAmtFac.add(preRepayAmtFac).compareTo(BigDecimal.ZERO) <= 0) {
			computeCoBonusAmt = BigDecimal.ZERO;
		} else if (preRepayAmtFac.add(this.txBuffer.getSystemParas().getPerfBackRepayAmt())
				.compareTo(BigDecimal.ZERO) <= 0) {
			computeCoBonusAmt = drawDownAmtFac.add(preRepayAmtFac);
		} else {
			computeCoBonusAmt = drawDownAmtFac;
		}

		// 計算協辦獎金
		pf.setComputeCoBonusAmt(computeCoBonusAmt);

		BigDecimal coBonus = BigDecimal.ZERO;
		for (CdBonusCo cd : lCdBonusCo) {
			if (cd.getConditionCode() == 2 && cd.getCondition().equals(tPfCoOfficer.getEmpClass())) {
				if ("Y".equals(tPfCoOfficer.getClassPass()))
					coBonus = cd.getClassPassBonus();
				else
					coBonus = cd.getBonus();
			}
		}
		// 協辦獎金 = 新協辦獎金 - 已計協辦獎金
		pf.setCoorgnizerBonus(coBonus.subtract(coBonusFac));

		return pf;

	}

	// 計件代碼，是否計入
	private boolean isCoBonus(String pieceCode, List<CdBonusCo> lCdBonusCo) {
		boolean piceInclude = false; // 篩選條件-計件代碼，是否計入，預設false
		for (CdBonusCo cd : lCdBonusCo) {
			if (cd.getConditionCode() == 1 && pieceCode.equals(cd.getCondition())) {
				piceInclude = true;
			}
		}
		return piceInclude;
	}

	/* 設定業績明細欄位值 */
	private PfDetail procPfDetail(PfDetail pf) throws LogicException {
		this.info("PfDetailCom procPfDetail .....");
		// 還款、計件代碼變更、重轉時更新記號=N => 抓最新介紹人及所屬資料(變動 by L5501房貸介紹人業績案件維護)
		if (pf.getRepayType() > 0 || "N".equals(pf.getIsReNewEmpUnit())) {
			PfItDetail It = pfItDetailService.findBormNoLatestFirst(pf.getCustNo(), pf.getFacmNo(), pf.getBormNo(),
					titaVo);
			if (It != null) {
				pf.setUnitCode(It.getUnitCode());
				pf.setDistCode(It.getDistCode());
				pf.setDeptCode(It.getDeptCode());
				pf.setUnitManager(It.getUnitManager());
				pf.setDistManager(It.getDistManager());
				pf.setDeptManager(It.getDeptManager());

			}
		}

		// 條件記號1 1.排除商品別 2.排除部門別 3.是否排除15日薪非業績人員
		// 條件記號2 1.全部業績 2.換算業績、業務報酬 3.介紹獎金 4.加碼獎勵津貼 5.協辦獎金
		Slice<CdPfParms> slCdPfParms = cdPfParmsService.findAll(0, Integer.MAX_VALUE, titaVo);
		List<CdPfParms> lCdPfParms = slCdPfParms == null ? null : slCdPfParms.getContent();
		if (lCdPfParms != null) {
			for (CdPfParms cd : lCdPfParms) {
				if ((cd.getWorkMonthStart() == 0 || cd.getWorkMonthStart() >= workMonthDrawdown)
						&& (cd.getWorkMonthEnd() == 0 || cd.getWorkMonthEnd() <= workMonthDrawdown)) {
					if ("1".equals(cd.getConditionCode1()) && cd.getCondition().equals(pf.getProdCode())) {
						if ("1".equals(cd.getConditionCode2())) {
							pf.setIsProdExclude1("Y");
							pf.setIsProdExclude2("Y");
							pf.setIsProdExclude3("Y");
							pf.setIsProdExclude4("Y");
							pf.setIsProdExclude5("Y");
						} else if ("2".equals(cd.getConditionCode2())) {
							pf.setIsProdExclude2("Y");
						} else if ("3".equals(cd.getConditionCode2())) {
							pf.setIsProdExclude3("Y");
						} else if ("4".equals(cd.getConditionCode2())) {
							pf.setIsProdExclude4("Y");
						} else if ("5".equals(cd.getConditionCode2())) {
							pf.setIsProdExclude5("Y");
						}
					}
					if ("2".equals(cd.getConditionCode1()) && cd.getCondition().equals(pf.getDeptCode())) {
						if ("1".equals(cd.getConditionCode2())) {
							pf.setIsDeptExclude1("Y");
							pf.setIsDeptExclude2("Y");
							pf.setIsDeptExclude3("Y");
							pf.setIsDeptExclude4("Y");
							pf.setIsDeptExclude5("Y");
						} else if ("2".equals(cd.getConditionCode2())) {
							pf.setIsDeptExclude2("Y");
						} else if ("3".equals(cd.getConditionCode2())) {
							pf.setIsDeptExclude3("Y");
						} else if ("4".equals(cd.getConditionCode2())) {
							pf.setIsDeptExclude4("Y");
						} else if ("5".equals(cd.getConditionCode2())) {
							pf.setIsDeptExclude5("Y");
						}
					}
					if ("3".equals(cd.getConditionCode1())) {
						if ("1".equals(cd.getConditionCode2())) {
							pf.setIsDay15Exclude1("Y");
							pf.setIsDay15Exclude2("Y");
							pf.setIsDay15Exclude3("Y");
							pf.setIsDay15Exclude4("Y");
							pf.setIsDay15Exclude5("Y");
						} else if ("2".equals(cd.getConditionCode2())) {
							pf.setIsDay15Exclude2("Y");
						} else if ("3".equals(cd.getConditionCode2())) {
							pf.setIsDay15Exclude3("Y");
						} else if ("4".equals(cd.getConditionCode2())) {
							pf.setIsDay15Exclude4("Y");
						} else if ("5".equals(cd.getConditionCode2())) {
							pf.setIsDay15Exclude5("Y");
						}
					}
				}
			}
		}
		// 介紹人是否為15日薪(Y/Null)，單位代號(介紹人)
		if (!"".equals(pf.getIntroducer())) {
			CdEmp tCdEmp = cdEmpService.findById(pf.getIntroducer(), titaVo);
			if (tCdEmp != null) {
				if (employeeCom.isDay15Salary(tCdEmp, titaVo)) {
					pf.setIsIntroducerDay15("Y");
				}
				if ("".equals(pf.getUnitCode())) {
					pf.setUnitCode(tCdEmp.getCenterCode());
				}
			}
		}

		// 協辦人員是否為15日薪(Y/Null)
		if (!"".equals(pf.getCoorgnizer())) {
			CdEmp tCdEmp = cdEmpService.findById(pf.getCoorgnizer(), titaVo);
			if (tCdEmp != null) {
				if (employeeCom.isDay15Salary(tCdEmp, titaVo)) {
					pf.setIsCoorgnizerDay15("Y");
				}
			}
		}

		// 區部、部室及處經理代號、區經理代號、部經理代號
		if (!"".equals(pf.getUnitCode()) && "".equals(pf.getDistCode())) {
			// 分公司資料檔
			CdBcm tCdBcm = cdBcmService.findById(pf.getUnitCode(), titaVo);
			if (tCdBcm != null) {
				pf.setDistCode(tCdBcm.getDistCode()); // 區部代號(介紹人)
				pf.setDeptCode(tCdBcm.getDeptCode()); // 部室代號(介紹人)
				pf.setUnitManager(tCdBcm.getUnitManager()); // 處經理代號(介紹人)
				pf.setDistManager(tCdBcm.getDistManager()); // 區經理代號(介紹人)
				pf.setDeptManager(tCdBcm.getDeptManager()); // 部經理代號(介紹人)
			}
		}

		// 還款、.計件代碼變更、重轉時更新記號=N => 抓最新房貸專員及所屬部室(變動 by L5502房貸專員業績案件維護)
		if (pf.getRepayType() > 0) {
			PfBsDetail bs = pfBsDetailService.findBormNoLatestFirst(pf.getCustNo(), pf.getFacmNo(), pf.getBormNo(),
					titaVo);
			if (bs != null) {
				pf.setBsOfficer(bs.getBsOfficer()); // 房貸專員
				pf.setBsDeptCode(bs.getDeptCode()); // 部室代號
			}
		}
		// 部室代號找該月份房貸專員業績目標檔
		if (!"".equals(pf.getBsOfficer()) && "".equals(pf.getBsDeptCode())) {
			PfBsOfficer tPfBsOfficer = pfBsOfficerService
					.findById(new PfBsOfficerId(workMonthDrawdown, pf.getBsOfficer()), titaVo);
			if (tPfBsOfficer != null) {
				pf.setBsDeptCode(tPfBsOfficer.getDeptCode());
			}
		}
		// 理財型房貸(Y/N)
		tFacProd = facProdService.findById(pf.getProdCode(), titaVo);
		if (tFacProd != null) {
			pf.setIsProdFinancial(tFacProd.getFinancialFlag());

		}

		pf.setWorkMonth(workMonth); // 工作月
		pf.setWorkSeason(workSeason); // 工作季

		return pf;

	}

	/* 寫入業績明細計算檔 */
	private void updDetail(PfDetail pf) throws LogicException {
		this.info("PfDetailCom updDetail .....");
		try {
			pfDetailService.insert(pf, titaVo); // insert
		} catch (DBException e) {
			this.error(e.getMessage());
			throw new LogicException(titaVo, "E0005", "PfDetail insert " + e.getErrorMsg()); // 新增資料時，發生錯誤
		}
	}

	// 寫入介紹人業績明細檔
	private void updItDetail(PfDetail pf) throws LogicException {
		this.info("PfDetailCom updItDetail .....");
		boolean isInsert = true;
		PfItDetail tPfItDetail = pfItDetailService.findByTxFirst(pf.getCustNo(), pf.getFacmNo(), pf.getBormNo(),
				perfDateF, pf.getRepayType(), pf.getPieceCode(), titaVo);
		if (tPfItDetail != null) {
			isInsert = false;
			tPfItDetail = pfItDetailService.holdById(tPfItDetail, titaVo);
		} else {
			tPfItDetail = new PfItDetail();
			tPfItDetail.setCustNo(pf.getCustNo());// 戶號
			tPfItDetail.setFacmNo(pf.getFacmNo()); // 額度編號
			tPfItDetail.setBormNo(pf.getBormNo());// 撥款序號
			tPfItDetail.setPerfDate(perfDate); // 業績日期
			tPfItDetail.setRepayType(pf.getRepayType()); // 還款類別
			tPfItDetail.setPieceCode(pf.getPieceCode()); // 計件代碼
		}
		tPfItDetail.setDrawdownDate(pf.getDrawdownDate()); // 撥款日
		tPfItDetail.setProdCode(pf.getProdCode()); // 商品代碼
		tPfItDetail.setDrawdownAmt(tPfItDetail.getDrawdownAmt().add(pf.getDrawdownAmt())); // 撥款金額/追回金額
		tPfItDetail.setUnitCode(pf.getUnitCode()); // 單位代號CdEmp.CenterCode單位代號
		tPfItDetail.setDistCode(pf.getDistCode()); // 區部代號
		tPfItDetail.setDeptCode(pf.getDeptCode()); // 部室代號
		tPfItDetail.setIntroducer(pf.getIntroducer()); // 介紹人
		tPfItDetail.setUnitManager(pf.getUnitManager()); // 處經理代號
		tPfItDetail.setDistManager(pf.getDistManager()); // 區經理代號
		tPfItDetail.setDeptManager(pf.getDeptManager()); // 部經理代號
		// 計算業績時排除商品別
		// 計算業績時排徐部門別
		// 計算業績時排徐15日薪
		// 1.全部業績
		// 件數、業績金額
		if (!"Y".equals(pf.getIsProdExclude1()) && !"Y".equals(pf.getIsDeptExclude1())
				&& !("Y".equals(pf.getIsDay15Exclude1()) && "Y".equals(pf.getIsIntroducerDay15()))) {
			tPfItDetail.setPerfCnt(tPfItDetail.getPerfCnt().add(pf.getItPerfCnt()));
			tPfItDetail.setPerfAmt(tPfItDetail.getPerfAmt().add(pf.getItPerfAmt()));
		}
		// 是否計件
		if (tPfItDetail.getPerfCnt().compareTo(BigDecimal.ZERO) > 0) {
			tPfItDetail.setCntingCode("Y");
		} else {
			tPfItDetail.setCntingCode("N");
		}
		// 2.換算業績、業務報酬
		if (!"Y".equals(pf.getIsProdExclude2()) && !"Y".equals(pf.getIsDeptExclude2())
				&& !("Y".equals(pf.getIsDay15Exclude2()) && "Y".equals(pf.getIsIntroducerDay15()))) {
			tPfItDetail.setPerfEqAmt(tPfItDetail.getPerfEqAmt().add(pf.getItPerfEqAmt()));
			tPfItDetail.setPerfReward(tPfItDetail.getPerfReward().add(pf.getItPerfReward()));
		}
		tPfItDetail.setWorkMonth(pf.getWorkMonth()); // 工作月
		tPfItDetail.setWorkSeason(pf.getWorkSeason()); // 工作季
		this.info("PfDetailCom update " + tPfItDetail.toString());
		if (isInsert) {
			try {
				pfItDetailService.insert(tPfItDetail, titaVo); // insert
			} catch (DBException e) {
				this.error(e.getMessage());
				throw new LogicException(titaVo, "E0005", "PfItDetail" + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		} else {
			try {
				pfItDetailService.update(tPfItDetail, titaVo); // update
			} catch (DBException e) {
				this.error(e.getMessage());
				throw new LogicException(titaVo, "E0007", "PfItDetail" + e.getErrorMsg()); // 更新資料時，發生錯誤
			}
		}
	}

	// 寫入房貸專員業績明細檔
	public void updBsDetail(PfDetail pf) throws LogicException {
		this.info("PfDetailCom updBsDetail .....");
		boolean isInsert = true;
		PfBsDetail tPfBsDetail = pfBsDetailService.findByTxFirst(pf.getCustNo(), pf.getFacmNo(), pf.getBormNo(),
				perfDateF, pf.getRepayType(), pf.getPieceCode(), titaVo);
		if (tPfBsDetail != null) {
			isInsert = false;
			tPfBsDetail = pfBsDetailService.holdById(tPfBsDetail, titaVo);
		} else {
			tPfBsDetail = new PfBsDetail();
			tPfBsDetail.setCustNo(pf.getCustNo()); // 戶號
			tPfBsDetail.setFacmNo(pf.getFacmNo()); // 額度編號
			tPfBsDetail.setBormNo(pf.getBormNo()); // 撥款序號
			tPfBsDetail.setPerfDate(perfDate); // 業績日期
			tPfBsDetail.setRepayType(pf.getRepayType()); // 還款類別
			tPfBsDetail.setPieceCode(pf.getPieceCode()); // 計件代碼
		}

		tPfBsDetail.setBsOfficer(pf.getBsOfficer());// 房貸專員
		tPfBsDetail.setDeptCode(pf.getBsDeptCode());// 部室代號
		tPfBsDetail.setDrawdownDate(pf.getDrawdownDate()); // 撥款日/還款日
		tPfBsDetail.setProdCode(pf.getProdCode()); // 商品代碼
		tPfBsDetail.setDrawdownAmt(tPfBsDetail.getDrawdownAmt().add(pf.getDrawdownAmt())); // 撥款金額/追回金額
		tPfBsDetail.setPerfCnt(tPfBsDetail.getPerfCnt().add(pf.getBsPerfCnt())); // 件數
		tPfBsDetail.setPerfAmt(tPfBsDetail.getPerfAmt().add(pf.getBsPerfAmt())); // 業績金額
		tPfBsDetail.setWorkMonth(pf.getWorkMonth()); // 工作月
		tPfBsDetail.setWorkSeason(pf.getWorkSeason()); // 工作季
		this.info("PfDetailCom update " + tPfBsDetail.toString());
		if (isInsert) {
			try {
				pfBsDetailService.insert(tPfBsDetail, titaVo); // insert
			} catch (DBException e) {
				this.error(e.getMessage());
				throw new LogicException(titaVo, "E0005", "PfBsDetail" + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		} else {
			try {
				pfBsDetailService.update(tPfBsDetail, titaVo); // update
			} catch (DBException e) {
				this.error(e.getMessage());
				throw new LogicException(titaVo, "E0007", "PfBsDetail " + e.getErrorMsg()); // 更新資料時，發生錯誤
			}
		}
	}

	// 寫入介紹、協辦獎金發放檔
	private void updReward(PfDetail pf) throws LogicException {
		boolean isInsert = true;
		PfReward tPfReward = pfRewardService.findByTxFirst(pf.getCustNo(), pf.getFacmNo(), pf.getBormNo(), perfDateF,
				pf.getRepayType(), pf.getPieceCode(), titaVo);

		if (tPfReward != null) {
			isInsert = false;
			tPfReward = pfRewardService.holdById(tPfReward, titaVo);
		} else {
			tPfReward = new PfReward();
			tPfReward.setCustNo(pf.getCustNo()); // 戶號
			tPfReward.setFacmNo(pf.getFacmNo()); // 額度編號
			tPfReward.setBormNo(pf.getBormNo()); // 撥款序號
			tPfReward.setPerfDate(perfDate); // 業績日期
			tPfReward.setRepayType(pf.getRepayType()); // 還款類別
			tPfReward.setPieceCode(pf.getPieceCode()); // 計件代碼
		}
		this.info("PfDetailCom isInsert " + isInsert);
		tPfReward.setProdCode(pf.getProdCode()); // 商品代碼
		tPfReward.setCoorgnizer(pf.getCoorgnizer()); // 協辦人員編
		tPfReward.setInterviewerA(pf.getInterviewerA()); // 晤談一員編
		tPfReward.setInterviewerB(pf.getInterviewerB()); // 晤談二員編
		tPfReward.setIntroducerBonus(BigDecimal.ZERO); // 介紹人介紹獎金
		tPfReward.setIntroducerAddBonus(BigDecimal.ZERO); // 介紹人加碼獎勵津貼
		tPfReward.setCoorgnizerBonus(BigDecimal.ZERO); // 協辦人員協辦獎金
		tPfReward.setIntroducer(pf.getIntroducer()); // 介紹人
		// 3.介紹獎金
		if (!"Y".equals(pf.getIsProdExclude3()) && !"Y".equals(pf.getIsDeptExclude3())
				&& !("Y".equals(pf.getIsDay15Exclude3()) && "Y".equals(pf.getIsIntroducerDay15()))) {
			tPfReward.setIntroducerBonus(tPfReward.getIntroducerBonus().add(pf.getItBonus()));
		}
		// 4.加碼獎勵津貼
		if (!"Y".equals(pf.getIsProdExclude4()) && !"Y".equals(pf.getIsDeptExclude4())
				&& !("Y".equals(pf.getIsDay15Exclude4()) && "Y".equals(pf.getIsIntroducerDay15()))) {
			tPfReward.setIntroducerAddBonus(tPfReward.getIntroducerAddBonus().add(pf.getItAddBonus()));
		}
		// 5.協辦獎金
		if (!"Y".equals(pf.getIsProdExclude5()) && !"Y".equals(pf.getIsDeptExclude5())
				&& !("Y".equals(pf.getIsDay15Exclude5()) && "Y".equals(pf.getIsIntroducerDay15()))) {
			tPfReward.setCoorgnizerBonus(tPfReward.getCoorgnizerBonus().add(pf.getCoorgnizerBonus()));
		}
		tPfReward.setWorkMonth(pf.getWorkMonth()); // 工作月
		tPfReward.setWorkSeason(pf.getWorkSeason()); // 工作季
		this.info("PfDetailCom update " + tPfReward.toString());
		if (isInsert) {
			try {
				pfRewardService.insert(tPfReward, titaVo); // insert
			} catch (DBException e) {
				this.error(e.getMessage());
				throw new LogicException(titaVo, "E0005", "PfReward " + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		} else {
			try {
				pfRewardService.update(tPfReward, titaVo); // update
			} catch (DBException e) {
				this.error(e.getMessage());
				throw new LogicException(titaVo, "E0007", "PfReward " + e.getErrorMsg()); // 更新資料時，發生錯誤
			}
		}
	}

	// 新貸件(代碼2&B)：參數設定為不計件數(業績件數及金額核算標準)，系統要處理6個月內撥款就算1件
	private CdPerformance specialSettingCd(PfDetail pf, CdPerformance cd) throws LogicException {
		for (FacMain tFacMain : lFacMain) {
			if (pf.getFacmNo() == tFacMain.getFacmNo()) {
				tFacCaseAppl = facCaseApplService.findById(tFacMain.getApplNo(), titaVo);
				if (tFacCaseAppl != null) {
					dDateUtil.init();
					dDateUtil.setDate_1(tFacCaseAppl.getApproveDate());
					dDateUtil.setMons(6);
					// 首撥日在核准日的六個月內
					if (tFacMain.getFirstDrawdownDate() <= dDateUtil.getCalenderDay()) {
						cd.setUnitCnt(BigDecimal.valueOf(1)); // 件數
						cd.setUnitAmtCond(BigDecimal.ZERO); // 撥款累計條件
						// 基底金額為0者
						cd.setBsOffrCntAmt(BigDecimal.ZERO); // 基底金額
						cd.setBsOffrAmtCond(BigDecimal.ZERO); // 撥款累計條件
						cd.setBsOffrCnt(BigDecimal.valueOf(1)); // 計件件數
					}
				}
			}
		}
		this.info(" specialSettingCd  " + cd.toString());
		return cd;
	}

	/* 刪除業績明細檔 */
	private void deleteDetail(PfDetailVo iPf) throws LogicException {
		this.info(" deleteDetail ");
		Slice<PfItDetail> slPfItDetail = pfItDetailService.findBormNoEq(iPf.getCustNo(), iPf.getFacmNo(),
				iPf.getBormNo(), 0, Integer.MAX_VALUE, titaVo);
		if (slPfItDetail != null) {
			try {
				pfItDetailService.deleteAll(slPfItDetail.getContent(), titaVo);
			} catch (DBException e) {
				e.printStackTrace();
				throw new LogicException(titaVo, "E0008", "PfItDetail" + e.getErrorMsg()); // 更新資料時，發生錯誤
			}

		}
		Slice<PfBsDetail> slPfBsDetail = pfBsDetailService.findBormNoEq(iPf.getCustNo(), iPf.getFacmNo(),
				iPf.getBormNo(), 0, Integer.MAX_VALUE, titaVo);
		if (slPfBsDetail != null) {
			try {
				pfBsDetailService.deleteAll(slPfBsDetail.getContent(), titaVo);
			} catch (DBException e) {
				e.printStackTrace();
				throw new LogicException(titaVo, "E0008", "PfBsDetail" + e.getErrorMsg()); // 更新資料時，發生錯誤
			}

		}
		Slice<PfReward> slPfReward = pfRewardService.findBormNoEq(iPf.getCustNo(), iPf.getFacmNo(), iPf.getBormNo(), 0,
				Integer.MAX_VALUE, titaVo);
		if (slPfReward != null) {
			try {
				pfRewardService.deleteAll(slPfReward.getContent(), titaVo);
			} catch (DBException e) {
				e.printStackTrace();
				throw new LogicException(titaVo, "E0008", "PfReward" + e.getErrorMsg()); // 更新資料時，發生錯誤
			}

		}

		Slice<PfDetail> slPfDetail = pfDetailService.findByBorxNo(iPf.getCustNo(), iPf.getFacmNo(), iPf.getBormNo(),
				iPf.getBorxNo(), 0, Integer.MAX_VALUE, titaVo);
		if (slPfDetail != null) {
			try {
				pfDetailService.deleteAll(slPfDetail.getContent(), titaVo);
			} catch (DBException e) {
				e.printStackTrace();
				throw new LogicException(titaVo, "E0008", "PfDetail" + e.getErrorMsg()); // 更新資料時，發生錯誤
			}

		}
	}
}