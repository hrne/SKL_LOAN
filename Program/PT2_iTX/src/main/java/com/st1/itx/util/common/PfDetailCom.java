package com.st1.itx.util.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
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
import com.st1.itx.util.mail.MailService;
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

	@Autowired
	private MailService mailService;

	private TitaVo titaVo;
	private ArrayList<PfDetail> lPfDetail = new ArrayList<PfDetail>();;
	private ArrayList<PfDetail> lPfDetailProcess = new ArrayList<PfDetail>();
	private ArrayList<PfDetail> lPfDetailCntingCode = new ArrayList<PfDetail>();
	private ArrayList<PfDetail> lPfDetailReverse = new ArrayList<PfDetail>();
	private List<FacMain> lFacMain;
	private List<CdPfParms> lCdPfParms;
	private FacProd tFacProd;
	private FacCaseAppl tFacCaseAppl;
	private int perfDate; // 業績日期(中曆)
	private int perfDateF; // 業績日期(西元)
	private int workMonth; // 工作月(西元)
	private int workSeason; // 工作季(西元)
	private int workMonthDrawdown; // 撥款工作月(西元)
	private boolean isRerun = false;
	private String bsOfficer = "";
	private DecimalFormat mDecimalFormat = new DecimalFormat("#,###");

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		// TODO Auto-generated method stub
		return null;
	}

	public void init(TitaVo titaVo) throws LogicException {
		lPfDetail = new ArrayList<PfDetail>();
		lPfDetailProcess = new ArrayList<PfDetail>();
		lPfDetailCntingCode = new ArrayList<PfDetail>();
		lPfDetailReverse = new ArrayList<PfDetail>();
		lFacMain = null;
		lCdPfParms = null;
		tFacProd = null;
		tFacCaseAppl = null;
		perfDate = 0;
		perfDateF = 0;
		workMonth = 0;
		workSeason = 0;
		workMonthDrawdown = 0;
		isRerun = false;
		bsOfficer = "";
	}

	/**
	 * 業績明細處理
	 * 
	 * @param iPf    PfDetailVo
	 * @param titaVo ..
	 * @return PfDetailVo List
	 * @throws LogicException ...
	 */
	public ArrayList<PfDetail> addDetail(PfDetailVo iPf, TitaVo titaVo) throws LogicException {
		this.info("PfDetailCom addDetail ....." + iPf.toString());

		this.titaVo = titaVo;

		this.init(titaVo);

		// 輸入檢核
		inputCheck(iPf);

		// 放行寫入業績明細，否則為試匴
		if (!titaVo.isActfgRelease()) {
			if (titaVo.isHcodeErase()) {
				this.info("iPf skip !titaVo.isActfgRelease and isHcodeErase" + iPf.toString());
				return null;
			} else {
				iPf.setTrial(true);
			}
		}

		// 變更計件代碼，不可訂正
		if (titaVo.isHcodeErase() && iPf.getRepayType() == 1) {
			throw new LogicException(titaVo, "E0015", "變更計件代碼，不可訂正"); // 檢查錯誤
		}

		// 重新計算業績
		isRerun = false;
		if ("Y".equals(iPf.getEmpResetFg()) || "N".equals(iPf.getEmpResetFg())) {
			isRerun = true;
		}

		// 部分償還、提前結案，已攤還期數>=3期，則跳過不處理
		if ((iPf.getRepayType() == 2 || iPf.getRepayType() == 3)
				&& iPf.getRepaidPeriod() >= this.txBuffer.getSystemParas().getPerfBackPeriodE()) {
			this.info("iPf skip " + iPf.toString());
			return null;
		}

		// 2022-10-25 Wei 修改
		// 預撥不寫入業績檔，由日始批次的BS996寫入當日業績檔
		if (titaVo.getEntDyI() > this.txBuffer.getTxBizDate().getTbsDy()) {
			return null;
		}
		
		// 業績日期(中曆)=輸入業績日期/系統營業日(會計日期)
		if (iPf.getPerfDate() > 0) {
			perfDate = iPf.getPerfDate();
		} else {
			perfDate = titaVo.getEntDyI();
			iPf.setPerfDate(perfDate);
		}
		perfDateF = perfDate + 19110000; // 業績日期(西元)
		// 工作月(西曆)
		CdWorkMonth tCdWorkMonth = cdWorkMonthService.findDateFirst(perfDateF, perfDateF, titaVo);
		if (tCdWorkMonth == null) {
			throw new LogicException(titaVo, "E0001", "CdWorkMonth 放款業績工作月對照檔，業績日期=" + perfDateF); // 查詢資料不存在
		}
		workMonth = tCdWorkMonth.getYear() * 100 + tCdWorkMonth.getMonth();
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
				bsOfficer = tFacMain.getBusinessOfficer();
				tFacProd = facProdService.findById(tFacMain.getProdNo(), titaVo);
				if (tFacProd != null && "Y".equals(tFacProd.getFinancialFlag()) && iPf.getBormNo() >= 2) {
					this.info("pf FinancialFlag=Y " + iPf.toString());
					return null;
				}
			}
		}

		// Load 業績計算明細檔
		this.info("PfDetailCom load lPfDetail");
		Slice<PfDetail> slPfDetail = pfDetailService.findFacmNoRange(iPf.getCustNo(), facmNoS, facmNoE, 0, 999999,
				this.index, Integer.MAX_VALUE, titaVo);

		// 排除計件代碼變更留存資料
		// 撥款工作月(為參數工作月)
		// 提前還款及變更計件代碼須有撥款業績資料
		// 計件代碼變更，業績日期為撥款業績日期；不可跨撥款工作月；不可有提前還款
		BigDecimal extraRepay = BigDecimal.ZERO;
		workMonthDrawdown = 0;
		if (iPf.getRepayType() == 0) {
			workMonthDrawdown = workMonth;
		}
		if (slPfDetail != null) {
			for (PfDetail pf : slPfDetail.getContent()) {
				// 排除變更計件代碼留存資料
				if (pf.getRepayType() == 1) {
					continue;
				}
				// 撥款工作月、追回業績金額
				if (pf.getFacmNo() == iPf.getFacmNo() && pf.getBormNo() == iPf.getBormNo()) {
					if (pf.getRepayType() == 0) {
						workMonthDrawdown = pf.getWorkMonth();
					} else {
						extraRepay = extraRepay.add(pf.getDrawdownAmt());
					}
				}
				// 撥款、件代碼變更，排除變追回業績資料
				if (iPf.getRepayType() == 0 || iPf.getRepayType() == 1) {
					if (pf.getRepayType() >= 2) {
						continue;
					}
				}
				// 撥款，正常時累計撥款業績資料，訂正時回沖該筆撥款
				if (iPf.getRepayType() == 0) {
					if (titaVo.isHcodeNormal()) {
						lPfDetail.add(pf);
					}
					if (titaVo.isHcodeErase()) {
						if (iPf.getFacmNo() == pf.getFacmNo() && iPf.getBormNo() == pf.getBormNo()) {
							lPfDetailReverse.add(pf);
						} else {
							continue;
						}
					}
				}
				// 追回，時累計撥款、追回業績資料
				if (iPf.getRepayType() == 2 || iPf.getRepayType() == 3) {
					lPfDetail.add(pf);
				}
				// 計件代碼變更，回沖該筆撥款及之後撥款者，再重算
				if (iPf.getRepayType() == 1) {
					if (iPf.getFacmNo() == pf.getFacmNo() && iPf.getBormNo() == pf.getBormNo()) {
						workMonthDrawdown = pf.getWorkMonth();// 撥款工作月(西曆)
						if ("L3701".equals(titaVo.getTxCode()) && workMonth > workMonthDrawdown) {
							throw new LogicException(titaVo, "E0015", "跨撥款工作月，不可變更計件代碼"); // 檢查錯誤
						}
						iPf.setPerfDate(pf.getPerfDate()); // 業績日期為撥款業績日期
						perfDate = pf.getPerfDate();
						perfDateF = perfDate + 19110000; // 業績日期(西元)
						workMonth = pf.getWorkMonth();// 工作月(西曆)
						workSeason = pf.getWorkSeason();// 工作季(西曆)
						pf.setRepayType(1); // 計件代碼變更留存資料
						lPfDetailReverse.add(pf);
					} else {
						if ((pf.getFacmNo() > iPf.getFacmNo())
								|| (pf.getFacmNo() == iPf.getFacmNo() && pf.getBormNo() > iPf.getBormNo())) {
							pf.setRepayType(1); // 計件代碼變更留存資料
							lPfDetailReverse.add(pf);
						} else {
							lPfDetail.add(pf);
						}
					}
				}
			}
		}

		for (PfDetail pf : lPfDetail) {
			this.info("PfDetailCom load " + pf.toString());
		}

		// 計件代碼變更，不可有提前還款
		if (iPf.getRepayType() == 1 && extraRepay.compareTo(BigDecimal.ZERO) != 0) {
			throw new LogicException(titaVo, "E0015", "已提前還款，不可變更計件代碼"); // 檢查錯誤
		}

		// 計件代碼、還款應有撥款工作月
		if (iPf.getRepayType() > 0 && workMonthDrawdown == 0) {
			this.info("iPf skip workMonthDrawdown == 0" + iPf.toString());
			return null;
		}

		this.info("iPf workMonthDrawdown =" + workMonthDrawdown);

		// Load 業績特殊參數設定檔
		Slice<CdPfParms> slCdPfParms = cdPfParmsService.findAll(0, Integer.MAX_VALUE, titaVo);
		lCdPfParms = slCdPfParms == null ? null : slCdPfParms.getContent();

		switch (iPf.getRepayType()) {

		case 0:// 撥款
			if (titaVo.isHcodeNormal()) {
				processDrawDown(iPf); // 計算撥款業績
			} else {
				processReverse(iPf); // 沖回業績資料
			}
			break;

		case 1:// 計件代碼變更
			processReverse(iPf); // 沖回業績資料
			processDrawDown(iPf); // 計算新計件代碼業績
			processReDrawDown(iPf); // 重算沖回業績資料(之後撥款者)
			break;

		case 2:// 部分償還
		case 3:// 提前結案
			if (titaVo.isHcodeNormal()) {
				processExtraRepay(iPf); // 提前還款
			} else {
				processErase(iPf); // 訂正交易
			}
			break;
		}

		// 更新計件代碼
		updCntingCode();

		// 提前還款email通知
		if (iPf.getRepayType() == 2 || iPf.getRepayType() == 3) {
			processEmail(iPf); // 提前還款email通知
		}

		// END
		return lPfDetailProcess;
	}

	// 撥款交易
	private void processDrawDown(PfDetailVo iPf) throws LogicException {
		this.info("processDrawDown..." + iPf.toString());
		if (iPf.getPieceCodeSecondAmt().compareTo(BigDecimal.ZERO) > 0) {
			procPfDetail(iPf, iPf.getPieceCodeSecond(), iPf.getPieceCodeSecondAmt());
		}
		if (iPf.getPieceCodeSecondAmt().compareTo(iPf.getDrawdownAmt()) < 0) {
			procPfDetail(iPf, iPf.getPieceCode(), iPf.getDrawdownAmt().subtract(iPf.getPieceCodeSecondAmt()));
		}
	}

	// 沖回交易
	private void processReverse(PfDetailVo iPf) throws LogicException {
		this.info("processReverse..." + iPf.toString());
		// 是否為試匴
		if (iPf.isTrial() || lPfDetailReverse.size() == 0) {
			return;
		}
		// 撥款訂正刪除
		if (iPf.getRepayType() == 0) {
			try {
				pfDetailService.deleteAll(lPfDetailReverse, titaVo);
			} catch (DBException e) {
				e.printStackTrace();
				throw new LogicException(titaVo, "E0008", "PfDetail" + e.getErrorMsg()); // 刪除資料時，發生錯誤
			}
		}

		// 計件代碼變更留存
		if (iPf.getRepayType() == 1) {
			try {
				pfDetailService.updateAll(lPfDetailReverse, titaVo);
			} catch (DBException e) {
				e.printStackTrace();
				throw new LogicException(titaVo, "E0007", "PfDetail" + e.getErrorMsg()); // 更新資料時，發生錯誤
			}
		}

		// 刪除其餘業績明細檔
		for (PfDetail pf : lPfDetailReverse) {
			deleteDetail(pf);
		}
	}

	// 重算沖回業績資料(之後撥款者)
	private void processReDrawDown(PfDetailVo iPf) throws LogicException {
		this.info("processReDrawDown..." + iPf.toString());
		// 是否為試匴
		if (iPf.isTrial() || lPfDetailReverse.size() == 0) {
			return;
		}

		for (PfDetail pf : lPfDetailReverse) {
			if (iPf.getFacmNo() == pf.getFacmNo() && iPf.getBormNo() == pf.getBormNo()) {
				continue;
			} else {
				PfDetailVo rPf = new PfDetailVo();
				rPf.setPerfDate(pf.getPerfDate());
				rPf.setCustNo(pf.getCustNo());
				rPf.setFacmNo(pf.getFacmNo());
				rPf.setBormNo(pf.getBormNo());
				rPf.setBorxNo(pf.getBorxNo());
				rPf.setDrawdownDate(pf.getDrawdownDate());
				rPf.setRepaidPeriod(pf.getRepaidPeriod());
				rPf.setEmpResetFg(iPf.getEmpResetFg());
				perfDate = pf.getPerfDate();
				perfDateF = perfDate + 19110000; // 業績日期(西元)
				workMonth = pf.getWorkMonth();// 工作月(西曆)
				workMonthDrawdown = pf.getWorkMonth();// 撥款工作月(西曆)
				workSeason = pf.getWorkSeason();// 工作季(西曆)
				procPfDetail(rPf, pf.getPieceCode(), pf.getDrawdownAmt());
			}
		}
	}

	// 訂正交易
	private void processErase(PfDetailVo iPf) throws LogicException {
		this.info("processErase..." + iPf.toString());
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
					procPfDetail(iPf, pf.getPieceCode(), BigDecimal.ZERO.subtract(pf.getDrawdownAmt()));
				}
			}
		}
	}

	// 提前還款
	private void processExtraRepay(PfDetailVo iPf) throws LogicException {
		this.info("processExtraRepay..." + iPf.toString());
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
						procPfDetail(iPf, pf.getPieceCode(), BigDecimal.ZERO.subtract(remainderAmt)); // 追回金額
						remainderAmt = BigDecimal.ZERO;
					} else {
						procPfDetail(iPf, pf.getPieceCode(), BigDecimal.ZERO.subtract(pf.getDrawdownAmt()));
						remainderAmt = remainderAmt.subtract(pf.getDrawdownAmt());
					}
				}
			}
		}
	}

	private void procPfDetail(PfDetailVo iPf, String pieceCode, BigDecimal drawdownAmt) throws LogicException {
		this.info("procPfDetail..." + iPf.toString());
		this.info("lPfDetail.size=" + lPfDetail.size() + ", pieceCode=" + pieceCode + ", drawdownAmt=" + drawdownAmt);
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
		// 設定業績明細欄位值
		pf = setPfDetail(pf);

		// add to List
		lPfDetail.add(pf);

		// 逐筆計算業績並更新業績明細檔
		pf = procCompute(pf);

		// add to ListProcess
		lPfDetailProcess.add(pf);

		// 是否為試匴
		if (iPf.isTrial()) {
			return;
		}

		/* 寫入業績明細計算檔 */
		updDetail(pf);

		// 寫入介紹人業績明細檔
		updItDetail(pf);

		// 寫入房貸專員業績明細檔
		updBsDetail(pf);

		// 寫入介紹、協辦獎金發放檔
		updReward(pf);

	}

	/*---------- 業績追回時email通知 ----------*/
	public void processEmail(PfDetailVo iPf) throws LogicException {
		this.info("processEmail");
		String subject = "業績追回通知 借戶 " + iPf.getCustNo() + "-" + iPf.getFacmNo() + "-" + iPf.getBormNo()
				+ (iPf.getRepayType() == 2 ? ", 部分償還金額 " : "提前結案金額") + mDecimalFormat.format(iPf.getDrawdownAmt())
				+ (titaVo.isHcodeErase() ? ", 已訂正" : "");

		String bodyText = "";
		for (PfDetail pf : lPfDetailProcess) {
			bodyText += "   計件代碼 " + pf.getPieceCode() + ", 追回金額" + mDecimalFormat.format(pf.getDrawdownAmt());
		}
		this.info("subject=" + subject + " ,bodyText=" + bodyText);

		// 重算業績不處理
		if (isRerun) {
			return;
		}

		// 排除全部業績，不處理
		for (PfDetail pf : lPfDetailProcess) {
			if ("Y".equals(pf.getIsProdExclude1()) || "Y".equals(pf.getIsDeptExclude1())
					|| "Y".equals(pf.getIsDay15Exclude1())) {
				return;
			}
		}
		// 4.業績追回時通知房貸專員(email)
		if (!bsOfficer.isEmpty()) {
			CdEmp tCdEmp = cdEmpService.findById(bsOfficer, titaVo);
			if (tCdEmp != null && !"".equals(tCdEmp.getEmail().trim())) {
				this.info("tCdEmp.getEmail()=" + tCdEmp.getEmail().trim());
				mailService.setParams(tCdEmp.getEmail(), subject, bodyText);
				mailService.exec();
			}
		}

		// 5.業績追回時通知員工代碼(email)
		if (lCdPfParms != null) {
			for (CdPfParms cd : lCdPfParms) {
				if ((cd.getWorkMonthStart() == 0 || cd.getWorkMonthStart() <= workMonth)
						&& (cd.getWorkMonthEnd() == 0 || cd.getWorkMonthEnd() >= workMonth)) {
					if ("4".equals(cd.getConditionCode1())) {
						CdEmp tCdEmp = cdEmpService.findById(cd.getCondition(), titaVo);
						if (tCdEmp != null && !"".equals(tCdEmp.getEmail().trim())) {
							this.info("tCdEmp.getEmail()=" + tCdEmp.getEmail().trim());
							mailService.setParams(tCdEmp.getEmail(), subject, bodyText);
							mailService.exec();
						}
					}
				}
			}
		}
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
// 新貸件(代碼2&B)：核定日起6個月內，就未曾動用之額度撥款者，核發介紹獎金予原額度介紹人。介紹單位及房貸專員之業績計入撥款當月，但不計算件數。
		// 新貸件(代碼2&B)：參數設定為不計件數(業績件數及金額核算標準)，系統要處理6個月內撥款連同(代碼1&A)計算
		if ("2".equals(pf.getPieceCode()) || "B".equals(pf.getPieceCode())) {
			pf.setPieceCodeCombine(getPieceCodeCombine(pf));
		}
		String cdPieceCode = "".equals(pf.getPieceCodeCombine()) ? pf.getPieceCode() : pf.getPieceCodeCombine();

		tCd = cdPerformanceService.findById(new CdPerformanceId(workMonthCd, cdPieceCode), titaVo);
		if (tCd == null) {
			throw new LogicException(titaVo, "E0001", "CdPerformance 業績標準設定檔，計件代碼=" + pf.getPieceCode()); // 查詢資料不存在
		}
		this.info("PfDetailCom CdPerformance=" + tCd.toString());

// ---------------- 分別取得額度累計計算金額(介紹人、房貸專員)  ------------------------
//撥款後計息期間未逾一個月即結清(含部分還款達60萬元)案件，依還款金額追回各級人員介紹獎金和協辦津貼等各項獎勵。
//增訂已繳1期但未繳足3期期款即結清(含部分還款達60萬元)案件，追回換算業績(業務報酬)及介紹獎金。
// 追回未逾一個月即結清或繳納1期但未繳足3期期款即結清（含部分還款達60萬之案件)，未曾繳款者則同時追回房貸專員業績

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
// 介紹單位及房貸專員件數
// 介紹單位件數 (有門檻同一eLoan 案件編號累積計算)
// 同一額度、同一撥款工作月、同一計件代碼，累計計算金額
// 追回未繳足3期期款即結清（含部分還款達60萬之案件)
// 同案件新貸件(代碼2&B)於6個月內撥款，連同(代碼1&A)計算

		// 取得額度累計計算金額(介紹單位件數)
		pf.setComputeItAmtFac(getComputeAmtFac(pf, false));

		// ItPerfCnt 件數 = 標準件數(if 撥款追回金額 < 計件金額門檻 then 0)
		if (tCd.getUnitCnt().compareTo(BigDecimal.ZERO) > 0) {
			if (pf.getComputeItAmtFac().compareTo(BigDecimal.ZERO) <= 0
					|| pf.getComputeItAmtFac().compareTo(tCd.getUnitAmtCond()) < 0) {
				pf.setItPerfCnt(BigDecimal.ZERO);
			} else {
				pf.setItPerfCnt(tCd.getUnitCnt());
			}
		}
		// 是否計件，代碼(2&B)之是否計件寫入(代碼1&A)
		if ("2".equals(pf.getPieceCode()) || "B".equals(pf.getPieceCode())) {
			if (pf.getRepayType() == 0 && pf.getItPerfCnt().compareTo(BigDecimal.ZERO) > 0) {
				for (PfDetail it : lPfDetail) {
					if (it.getWorkMonth() == workMonthDrawdown && it.getItPerfCnt().compareTo(BigDecimal.ZERO) == 0
							&& pf.getCreditSysNo() > 0 && it.getCreditSysNo() == pf.getCreditSysNo()
							&& it.getPieceCode().equals(pf.getPieceCodeCombine())) {
						lPfDetailCntingCode.add(it);
					}
				}
			}
		}
		// 是否計件，同額度、同撥款工作月相同
		if (pf.getRepayType() == 0 && pf.getItPerfCnt().compareTo(BigDecimal.ZERO) > 0) {
			for (PfDetail it : lPfDetail) {
				if (it.getWorkMonth() == workMonthDrawdown && pf.getItPerfCnt().compareTo(BigDecimal.ZERO) == 0
						&& it.getFacmNo() == pf.getFacmNo() && it.getPieceCode().equals(pf.getPieceCode())) {
					lPfDetailCntingCode.add(it);
				}
			}
		}

		// 介紹人業績計算金額 = 撥款金額、追回差額
		if (pf.getRepayType() == 0) {
			pf.setComputeItAmt(pf.getDrawdownAmt());
		} else {
			pf.setComputeItAmt(getComputeAmtRepay(pf, false));
		}

		// ItPerfAmt 業績金額 = 撥款追回金額 * 撥款業績比例
		pf.setItPerfAmt(pf.getComputeItAmt().multiply(tCd.getUnitPercent()).setScale(0, RoundingMode.HALF_UP));

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
// 繳納1期但未繳足3期期款即結清（含部分還款達60萬）之案件， 追回換算業績（業務報酬）及介紹獎金
		// 換算業績(有基底,單筆計算)
		// 業務報酬(有基底,單筆計算)

		// ItPerfEqAmt 換算業績 = (撥款追回金額 / 換算業績金額基底) * 換算業績獎金
		if (tCd.getIntrodPfEqBase().compareTo(BigDecimal.ZERO) == 0) {
			pf.setItPerfEqAmt(BigDecimal.ZERO);
		} else {
			pf.setItPerfEqAmt(pf.getComputeItAmt().divide(tCd.getIntrodPfEqBase()).multiply(tCd.getIntrodPfEqAmt())
					.setScale(0, RoundingMode.HALF_UP));
		}

		// ItPerfReward 業務報酬 = (撥款追回金額 / 業務報酬金額基底) * 業務報酬獎金
		if (tCd.getIntrodRewardBase().compareTo(BigDecimal.ZERO) == 0) {
			pf.setItPerfReward(BigDecimal.ZERO);
		} else {
			pf.setItPerfReward(pf.getComputeItAmt().divide(tCd.getIntrodRewardBase()).multiply(tCd.getIntrodReward())
					.setScale(0, RoundingMode.HALF_UP));
		}

		// 介紹人介紹獎金
		pf = procItBonus(pf, tCd);

		// 介紹人加碼獎勵津貼
		pf = procItAddBonus(pf);

		// 計算協辦人員協辦獎金
		if (pf.getCoorgnizer().trim().length() > 0) {
			pf = procCoBonus(pf);
		}

// ---------------- 以額度累計計算金額(房貸專員)計算房貸專員業績  ------------------------
//  房貸專員 BsOffrCnt       BsOffrAmtCond 
//                 BsOffrCntLimit		        BsOffrPerccent
//case       件數 & 最高件數 &  計件金額門檻        撥款業績比例      
//1           1.0    1.0         600,000          1.0
//2           0      0                            1.0
//3           0.1    1.0         100,000          1.0
//4           0.1    1.0         100,000          1.0
//5           1      1.0         600,000          0 
//A           1.0    1.0         600,000          1.0
//B           0      0                            1.0
//C           0.1    1.0         100,000          1.0
//D           0.1    1.0         100,000          1.0
//E           1      1.0         600,000          0 
//6           0.1    1.0         100,000          0 
//7           0      0           0
//8           1.0    1.0         600,000          1.0		
// 房貸專員件數，同一撥款工作月、同一eLoan 案件編號累積以1件為限
		// 取得額度累計計算金額(房貸專員件數)
		pf.setComputeBsAmtFac(getComputeAmtFac(pf, true));

		// 累計業績件數
		BigDecimal bsPerfCntFac = BigDecimal.ZERO;
		for (PfDetail it : lPfDetail) {
			if (it.getWorkMonth() == workMonthDrawdown && isComputeFac(pf, it)) {
				bsPerfCntFac = bsPerfCntFac.add(it.getBsPerfCnt());
			}
		}

// 房貸專員件數(有門檻或基底,以整個額度計算)				

// BsPerfCnt 件數 = (minimum(基準件數 * 撥款追回金額 / 計件金額門檻), 最高件數) - 已計件數

		// 最高件數
		BigDecimal bsCntLimit = tCd.getBsOffrCntLimit();
		if (bsCntLimit.equals(BigDecimal.ZERO)) {
			bsCntLimit = tCd.getBsOffrCnt();
		}

		// 計件金額門檻額不為0者
		if (tCd.getBsOffrAmtCond().compareTo(BigDecimal.ZERO) > 0) {
			if (bsPerfCntFac.compareTo(bsCntLimit) > 0) {
				bsPerfCntFac = bsCntLimit;
			}
			pf.setBsPerfCnt((pf.getComputeBsAmtFac().divide(tCd.getBsOffrAmtCond(), 0, RoundingMode.DOWN))
					.multiply(tCd.getBsOffrCnt()).min(bsCntLimit).subtract(bsPerfCntFac));
		}
		// 房貸專員業績計算金額 = 撥款金額、追回差額
		if (pf.getRepayType() == 0) {
			pf.setComputeBsAmt(pf.getDrawdownAmt());
		} else {
			pf.setComputeBsAmt(getComputeAmtRepay(pf, false));
		}

		// 房貸專員業績 = 額度累計計算金額 * 撥款業績比例
		pf.setBsPerfAmt((pf.getComputeBsAmt().multiply(tCd.getBsOffrPerccent()).setScale(0, RoundingMode.HALF_UP)));

		// END
		this.info("PfDetailCom procCompute END pf =" + pf.toString());

		return pf;
	}

	/**
	 * 同一額度、同一撥款工作月、同一計件代碼，累計計算金額
	 * 
	 * @param pf        PfDetail
	 * @param neverPaid 未曾繳款 true/flase
	 * @return 額度累計計算金額
	 * @throws LogicException ...
	 */
	private BigDecimal getComputeAmtFac(PfDetail pf, boolean neverPaid) throws LogicException {
		BigDecimal drawDownAmtFac = BigDecimal.ZERO;
		BigDecimal precloseAmtFac = BigDecimal.ZERO;
		BigDecimal preRepayAmtFac = BigDecimal.ZERO;
		for (PfDetail it : lPfDetail) {
			if (it.getWorkMonth() == workMonthDrawdown && isComputeFac(pf, it)) {
				if (it.getRepayType() == 0) {
					drawDownAmtFac = drawDownAmtFac.add(it.getDrawdownAmt());
				} else {
					if ((it.getRepaidPeriod() == 0 && neverPaid) || !neverPaid) {
						if (it.getRepayType() == 2) {
							precloseAmtFac = precloseAmtFac.add(it.getDrawdownAmt());
						} else {
							preRepayAmtFac = preRepayAmtFac.add(it.getDrawdownAmt());
						}
					}
				}
			}
		}

		// 部分還款未達60萬元
		if (preRepayAmtFac.add(this.txBuffer.getSystemParas().getPerfBackRepayAmt()).compareTo(BigDecimal.ZERO) >= 0) {
			preRepayAmtFac = BigDecimal.ZERO;
		}

		// 額度累計計算金額
		BigDecimal computeAmtFac = BigDecimal.ZERO;
		computeAmtFac = drawDownAmtFac.add(precloseAmtFac).add(preRepayAmtFac);

		return computeAmtFac;
	}

	/**
	 * 額度累計計算時是否計入
	 * 
	 * @param pf (PfDetail
	 * @param it PfDetail
	 * @return true/false
	 * @throws LogicException ...
	 */
	private boolean isComputeFac(PfDetail pf, PfDetail it) throws LogicException {
		// 同額度、同一計件代碼 or 同案件編號、連同計件代碼
		boolean isComputeFac = false;
		if (it.getFacmNo() == pf.getFacmNo() && it.getPieceCode().equals(pf.getPieceCode())
				|| (pf.getCreditSysNo() > 0 && it.getCreditSysNo() == pf.getCreditSysNo()
						&& it.getPieceCode().equals(pf.getPieceCodeCombine()))
				|| (pf.getCreditSysNo() > 0 && it.getCreditSysNo() == pf.getCreditSysNo()
						&& it.getPieceCodeCombine().equals(pf.getPieceCode()))) {
			isComputeFac = true;
		}
		return isComputeFac;
	}

	// 業績計算金額
	/**
	 * 追回業績計算金額
	 * 
	 * @param pf        PfDetail
	 * @param neverPaid 未曾繳款 true/flase
	 * @return 追回業績計算金額
	 * @throws LogicException ...
	 */
	private BigDecimal getComputeAmtRepay(PfDetail pf, boolean neverPaid) throws LogicException {
		// 同一額度、同一撥款工作月、同一計件代碼，累計計算金額
		BigDecimal precloseAmtFac = BigDecimal.ZERO;
		BigDecimal preRepayAmtFac = BigDecimal.ZERO;
		for (PfDetail it : lPfDetail) {
			if (it.getWorkMonth() == workMonthDrawdown && it.getFacmNo() == pf.getFacmNo()) {
				if (it.getFacmNo() == pf.getFacmNo()) {
					if ((it.getRepaidPeriod() == 0 && neverPaid) || !neverPaid) {
						if (it.getRepayType() == 2) {
							precloseAmtFac = precloseAmtFac.add(it.getDrawdownAmt());
						} else {
							preRepayAmtFac = preRepayAmtFac.add(it.getDrawdownAmt());
						}
					}
				}
			}
		}
		// 含本筆業績計算金額
		BigDecimal computeAmtFac = precloseAmtFac;
		// 部分還款達60萬元
		if (preRepayAmtFac.add(this.txBuffer.getSystemParas().getPerfBackRepayAmt()).compareTo(BigDecimal.ZERO) < 0) {
			computeAmtFac = computeAmtFac.add(preRepayAmtFac);
		}

		// 不含本筆業績計算金額
		if (pf.getRepayType() == 2) {
			precloseAmtFac = precloseAmtFac.subtract(pf.getDrawdownAmt());
		} else {
			preRepayAmtFac = preRepayAmtFac.subtract(pf.getDrawdownAmt());
		}
		BigDecimal computeAmtOrg = precloseAmtFac;

		// 部分還款達60萬元
		if (preRepayAmtFac.add(this.txBuffer.getSystemParas().getPerfBackRepayAmt()).compareTo(BigDecimal.ZERO) < 0) {
			computeAmtOrg = computeAmtFac.add(preRepayAmtFac);
		}

		// 業績計算金額 = 含本筆業績計算金額 - 不含本筆業績計算金額
		return computeAmtFac.subtract(computeAmtOrg);
	}

	/* 介紹人介紹獎金 */
	private PfDetail procItBonus(PfDetail pf, CdPerformance tCd) throws LogicException {
// 同一額度、同一撥款工作月、同一計件代碼，累計計算金額
		BigDecimal drawDownAmtFac = BigDecimal.ZERO;
		BigDecimal precloseAmtFac = BigDecimal.ZERO;
		BigDecimal preRepayAmtFac = BigDecimal.ZERO;
		BigDecimal drawDownBonusFac = BigDecimal.ZERO;
		BigDecimal repayBonusFac = BigDecimal.ZERO;
		for (PfDetail it : lPfDetail) {
			if (it.getFacmNo() == pf.getFacmNo() && it.getWorkMonth() == workMonthDrawdown
					&& it.getPieceCode().equals(pf.getPieceCode())) {
				if (it.getRepayType() == 0) {
					drawDownBonusFac = drawDownBonusFac.add(it.getItBonus());
					drawDownAmtFac = drawDownAmtFac.add(it.getDrawdownAmt());
				} else {
					repayBonusFac = repayBonusFac.add(it.getItBonus());
					if (it.getRepaidPeriod() == 0) {
						if (it.getRepayType() == 2) {
							precloseAmtFac = precloseAmtFac.add(it.getDrawdownAmt());
						} else {
							preRepayAmtFac = preRepayAmtFac.add(it.getDrawdownAmt());
						}
					}
				}
			}
		}

		// 部分還款未達60萬元
		if (preRepayAmtFac.add(this.txBuffer.getSystemParas().getPerfBackRepayAmt()).compareTo(BigDecimal.ZERO) > 0) {
			preRepayAmtFac = BigDecimal.ZERO;
		}

		// 額度累計計算金額，撥款與追回分開計算
		BigDecimal computeBonusAmt = BigDecimal.ZERO;
		BigDecimal bonusFac = BigDecimal.ZERO;
		if (pf.getRepayType() == 0) {
			computeBonusAmt = drawDownAmtFac;
			bonusFac = drawDownBonusFac;
		} else {
			computeBonusAmt = drawDownAmtFac.add(precloseAmtFac).add(preRepayAmtFac);
			bonusFac = drawDownBonusFac.add(repayBonusFac);
		}
		pf.setComputeItBonusAmt(computeBonusAmt);

		// ItBonus 介紹獎金 = 介紹獎金比例(if 撥款追回金額 < 介紹獎金門檻 then 0) - 已計介紹獎金
		if (computeBonusAmt.compareTo(tCd.getIntrodAmtCond()) < 0)
			pf.setItBonus(BigDecimal.ZERO.subtract(bonusFac));
		else
			pf.setItBonus((computeBonusAmt.multiply(tCd.getIntrodPerccent()).setScale(0, RoundingMode.HALF_UP))
					.subtract(bonusFac));

		return pf;

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
// 同一額度、同一撥款工作月，累計計算
// 撥款後計息期間未逾一個月即結清(含部分還款達60萬元)案件，依還款金額追回各級人員介紹獎金和協辦津貼等各項獎勵。
		BigDecimal drawDownAmtFac = BigDecimal.ZERO;
		BigDecimal precloseAmtFac = BigDecimal.ZERO;
		BigDecimal preRepayAmtFac = BigDecimal.ZERO;
		BigDecimal drawDownBonusFac = BigDecimal.ZERO;
		BigDecimal repayBonusFac = BigDecimal.ZERO;
		for (PfDetail it : lPfDetail) {
			if (it.getFacmNo() == pf.getFacmNo() && it.getWorkMonth() == workMonthDrawdown) {
				if (isAddBonus(it.getPieceCode(), lCdBonus)) {
					if (it.getRepayType() == 0) {
						drawDownBonusFac = drawDownBonusFac.add(it.getItAddBonus());
						drawDownAmtFac = drawDownAmtFac.add(it.getDrawdownAmt());
						this.info("drawDownBonusFac=" + drawDownBonusFac + it.toString());
					} else {
						repayBonusFac = repayBonusFac.add(it.getItAddBonus());
						this.info("repayBonusFac=" + repayBonusFac + it.toString());
						if (it.getRepaidPeriod() == 0) {
							if (it.getRepayType() == 2) {
								precloseAmtFac = precloseAmtFac.add(it.getDrawdownAmt());
							} else {
								preRepayAmtFac = preRepayAmtFac.add(it.getDrawdownAmt());
							}
						}
					}
				}
			}
		}

		// 部分還款未達60萬元
		if (preRepayAmtFac.add(this.txBuffer.getSystemParas().getPerfBackRepayAmt()).compareTo(BigDecimal.ZERO) > 0) {
			preRepayAmtFac = BigDecimal.ZERO;
		}

		// 額度累計計算金額，撥款與追回分開計算
		BigDecimal computeBonusAmt = drawDownAmtFac.add(precloseAmtFac).add(preRepayAmtFac);
		BigDecimal bonusFac = drawDownBonusFac.add(repayBonusFac);
		pf.setComputeAddBonusAmt(computeBonusAmt);

		// 計算加碼獎勵津貼
		BigDecimal bonus = BigDecimal.ZERO;
		for (CdBonus cd : lCdBonus) {
			if (cd.getConditionCode() == 3) {
				if (computeBonusAmt.compareTo(cd.getAmtStartRange()) >= 0
						&& computeBonusAmt.compareTo(cd.getAmtEndRange()) <= 0)
					bonus = cd.getBonus();
			}
		}
		// 加碼獎勵 = 新加碼獎勵 - 已計加碼獎勵
		pf.setItAddBonus(bonus.subtract(bonusFac));
		this.info("bonusFac=" + bonusFac + ",bonus =" + bonus);

		return pf;
	}

	// 計件代碼，是否計入
	private boolean isAddBonus(String pieceCode, List<CdBonus> lCdBonus) {
		boolean piceInclude = false; // 篩選條件-計件代碼，是否計入，預設false
		boolean isAddBonus = false;
		for (CdBonus cd : lCdBonus) {
			if (cd.getConditionCode() == 1) {
				if (pieceCode.equals(cd.getCondition())) {
					piceInclude = true;
					break;
				}
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
		if ("".equals(pf.getCoorgnizer())) {
			this.info("PfDetailCom PfCoOfficer space skip ");
			return pf;
		}
		PfCoOfficer tPfCoOfficer = pfCoOfficerService.effectiveDateFirst(pf.getCoorgnizer(), 0,
				pf.getDrawdownDate() + 19110000, titaVo);
		if (tPfCoOfficer == null) {
			this.info("PfDetailCom PfCoOfficer null skip ");
			return pf;
		}
		if (tPfCoOfficer.getIneffectiveDate() > 0 && pf.getDrawdownDate() >= tPfCoOfficer.getIneffectiveDate()) {
			this.info("PfDetailCom PfCoOfficer IneffectiveDate skip " + tPfCoOfficer.toString());
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
		if (lCdBonusCo == null || !isCoBonusPieceCode(pf.getPieceCode(), lCdBonusCo)) {
			this.info("PfDetailCom CdBonusCo not include ");
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

// 同一額度、同一撥款工作月、同一計件代碼，累計計算
// 追回撥款後計息期間未逾一個月即結清(含部分還款達60萬元)案件，依還款金額追回各級人員介紹獎金和協辦津貼等各項獎勵

		BigDecimal drawDownAmtFac = BigDecimal.ZERO;
		BigDecimal precloseAmtFac = BigDecimal.ZERO;
		BigDecimal preRepayAmtFac = BigDecimal.ZERO;
		BigDecimal drawDownBonusFac = BigDecimal.ZERO;
		BigDecimal repayBonusFac = BigDecimal.ZERO;
		if (lPfDetail != null) {
			for (PfDetail it : lPfDetail) {
				if (isCoBonusPieceCode(it.getPieceCode(), lCdBonusCo)) {
					if (it.getFacmNo() == pf.getFacmNo() && it.getWorkMonth() == workMonthDrawdown) {
						if (it.getRepayType() == 0) {
							drawDownBonusFac = drawDownBonusFac.add(it.getCoorgnizerBonus());
							drawDownAmtFac = drawDownAmtFac.add(it.getDrawdownAmt());
						} else {
							repayBonusFac = repayBonusFac.add(it.getCoorgnizerBonus());
							if (it.getRepaidPeriod() == 0) {
								if (it.getRepayType() == 2) {
									precloseAmtFac = precloseAmtFac.add(it.getDrawdownAmt());
								} else {
									preRepayAmtFac = preRepayAmtFac.add(it.getDrawdownAmt());
								}
							}
						}
					}
				}
			}
		}

		// 部分還款未達60萬元
		if (preRepayAmtFac.add(this.txBuffer.getSystemParas().getPerfBackRepayAmt()).compareTo(BigDecimal.ZERO) > 0) {
			preRepayAmtFac = BigDecimal.ZERO;
		}

		// 額度累計計算金額，撥款與追回分開計算
		BigDecimal computeBonusAmt = BigDecimal.ZERO;
		BigDecimal bonusFac = BigDecimal.ZERO;

		if (pf.getRepayType() == 0) {
			computeBonusAmt = drawDownAmtFac;
			bonusFac = drawDownBonusFac;
		} else {
			computeBonusAmt = drawDownAmtFac.add(precloseAmtFac).add(preRepayAmtFac);
			bonusFac = drawDownBonusFac.add(repayBonusFac);
		}
		pf.setComputeCoBonusAmt(computeBonusAmt);

		// 計算協辦獎金
		BigDecimal coBonus = BigDecimal.ZERO;
		if (isCoBonusConditionAmt(computeBonusAmt, lCdBonusCo))
			for (CdBonusCo cd : lCdBonusCo) {
				if (cd.getConditionCode() == 2 && cd.getCondition().equals(tPfCoOfficer.getEmpClass())) {
					if ("Y".equals(tPfCoOfficer.getClassPass()))
						coBonus = cd.getClassPassBonus();
					else
						coBonus = cd.getBonus();
				}
			}
		// 協辦獎金 = 新協辦獎金 - 已計協辦獎金
		pf.setCoorgnizerBonus(coBonus.subtract(bonusFac));

		return pf;

	}

	// 是否達到金額門檻
	private boolean isCoBonusConditionAmt(BigDecimal computeBonusAmt, List<CdBonusCo> lCdBonusCo) {
		boolean isConditionAmt = false; // 篩選條件-計件代碼，是否計入，預設false
		for (CdBonusCo cd : lCdBonusCo) {
			if (cd.getConditionCode() == 1 && computeBonusAmt.compareTo(cd.getConditionAmt()) >= 0) {
				isConditionAmt = true;
				break;
			}
		}
		this.info("computeBonusAmt " + computeBonusAmt + "," + isConditionAmt);
		return isConditionAmt;
	}

	// 計件代碼，是否計入
	private boolean isCoBonusPieceCode(String pieceCode, List<CdBonusCo> lCdBonusCo) {
		boolean piceInclude = false; // 篩選條件-計件代碼，是否計入，預設false
		for (CdBonusCo cd : lCdBonusCo) {
			if (cd.getConditionCode() == 1 && pieceCode.equals(cd.getCondition())) {
				piceInclude = true;
				break;
			}
		}
		this.info("pieceCode " + pieceCode + "," + piceInclude);
		return piceInclude;
	}

	/* 設定業績明細欄位值 */
	private PfDetail setPfDetail(PfDetail pf) throws LogicException {
		this.info("PfDetailCom setPfDetail ...");
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
		// 業績特殊參數設定檔
		// 條件記號1 1.排除商品別 2.排除部門別 3.是否排除15日薪非業績人員
		// 條件記號2 1.全部業績 2.換算業績、業務報酬 3.介紹獎金 4.加碼獎勵津貼 5.協辦獎金
		if (lCdPfParms != null) {
			for (CdPfParms cd : lCdPfParms) {
				if ((cd.getWorkMonthStart() == 0 || cd.getWorkMonthStart() <= workMonthDrawdown)
						&& (cd.getWorkMonthEnd() == 0 || cd.getWorkMonthEnd() >= workMonthDrawdown)) {
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
					if ("Y".equals(pf.getIsIntroducerDay15()) && "3".equals(cd.getConditionCode1())) {
						if ("1".equals(cd.getConditionCode2())) {
							pf.setIsDay15Exclude1("Y");
							pf.setIsDay15Exclude2("Y");
							pf.setIsDay15Exclude3("Y");
							pf.setIsDay15Exclude4("Y");
						} else if ("2".equals(cd.getConditionCode2())) {
							pf.setIsDay15Exclude2("Y");
						} else if ("3".equals(cd.getConditionCode2())) {
							pf.setIsDay15Exclude3("Y");
						} else if ("4".equals(cd.getConditionCode2())) {
							pf.setIsDay15Exclude4("Y");
						}
					}
					if ("Y".equals(pf.getIsCoorgnizerDay15()) && "3".equals(cd.getConditionCode1())) {
						if ("1".equals(cd.getConditionCode2())) {
							pf.setIsDay15Exclude5("Y");
						} else if ("5".equals(cd.getConditionCode2())) {
							pf.setIsDay15Exclude5("Y");
						}
					}
				}
			}
		}

		// 區部、部室及處經理代號、區經理代號、部經理代號
		if (!"".equals(pf.getUnitCode()) && "".equals(pf.getDistCode())) {
			// 分公司資料檔
			CdBcm tCdBcm = cdBcmService.findById(pf.getUnitCode(), titaVo);
			if (tCdBcm == null) {
				throw new LogicException(titaVo, "E0001", "CdBcm 分公司資料檔" + pf.getUnitCode()); // 查詢資料不存在
			}
			if ("N".equals(tCdBcm.getEnable())) {
				throw new LogicException(titaVo, "E0019", "分公司資料未啟用" + pf.getUnitCode()); // 輸入資料錯誤
			}
			pf.setDistCode(tCdBcm.getDistCode()); // 區部代號(介紹人)
			pf.setDeptCode(tCdBcm.getDeptCode()); // 部室代號(介紹人)
			pf.setUnitManager(tCdBcm.getUnitManager()); // 處經理代號(介紹人)
			pf.setDistManager(tCdBcm.getDistManager()); // 區經理代號(介紹人)
			pf.setDeptManager(tCdBcm.getDeptManager()); // 部經理代號(介紹人)
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
				&& !"Y".equals(pf.getIsDay15Exclude1())) {
			tPfItDetail.setPerfCnt(pf.getItPerfCnt());
			tPfItDetail.setPerfAmt(tPfItDetail.getPerfAmt().add(pf.getItPerfAmt()));
		}
		// 是否計件
		// 代碼(2&B)之是否計件寫入(代碼1&A)
		if ("2".equals(pf.getPieceCode()) || "B".equals(pf.getPieceCode())) {
			tPfItDetail.setCntingCode("N");
		} else {
			if (pf.getItPerfCnt().compareTo(BigDecimal.ZERO) > 0) {
				tPfItDetail.setCntingCode("Y");
			} else {
				tPfItDetail.setCntingCode("N");
			}
		}
		// 2.換算業績、業務報酬
		if (!"Y".equals(pf.getIsProdExclude2()) && !"Y".equals(pf.getIsDeptExclude2())
				&& !"Y".equals(pf.getIsDay15Exclude2())) {
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

	// 是否計件，同額度、同工作月相同，代碼(2&B)之是否計件寫入(代碼1&A)
	public void updCntingCode() throws LogicException {
		this.info("PfDetailCom updCntingCode ....size=" + lPfDetailCntingCode.size());
		for (PfDetail pf : lPfDetailCntingCode) {
			PfItDetail tPfItDetail = pfItDetailService.findByTxFirst(pf.getCustNo(), pf.getFacmNo(), pf.getBormNo(),
					pf.getPerfDate() + 19110000, pf.getRepayType(), pf.getPieceCode(), titaVo);
			if (tPfItDetail == null) {
				return;
			}
			tPfItDetail = pfItDetailService.holdById(tPfItDetail, titaVo);
			tPfItDetail.setCntingCode("Y");
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
		tPfReward.setIntroducer(pf.getIntroducer()); // 介紹人
		// 3.介紹獎金
		if (!"Y".equals(pf.getIsProdExclude3()) && !"Y".equals(pf.getIsDeptExclude3())
				&& !"Y".equals(pf.getIsDay15Exclude3())) {
			tPfReward.setIntroducerBonus(tPfReward.getIntroducerBonus().add(pf.getItBonus()));
		}
		// 4.加碼獎勵津貼
		if (!"Y".equals(pf.getIsProdExclude4()) && !"Y".equals(pf.getIsDeptExclude4())
				&& !"Y".equals(pf.getIsDay15Exclude4())) {
			tPfReward.setIntroducerAddBonus(tPfReward.getIntroducerAddBonus().add(pf.getItAddBonus()));
		}
		// 5.協辦獎金
		if (!"Y".equals(pf.getIsProdExclude5()) && !"Y".equals(pf.getIsDeptExclude5())
				&& !"Y".equals(pf.getIsDay15Exclude5())) {
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

	// 同案件新貸件(代碼2&B)於6個月內撥款，連同(代碼1&A)計算
	// (代碼2&B)參數設定為不計件數(業績件數及金額核算標準)
	private String getPieceCodeCombine(PfDetail pf) throws LogicException {
		String pieceCodeCombine = "";
		for (FacMain tFacMain : lFacMain) {
			if (pf.getFacmNo() == tFacMain.getFacmNo()) {
				tFacCaseAppl = facCaseApplService.findById(tFacMain.getApplNo(), titaVo);
				if (tFacCaseAppl != null) {
					dDateUtil.init();
					dDateUtil.setDate_1(tFacCaseAppl.getApproveDate());
					dDateUtil.setMons(6);
					// 撥款日在核准日的六個月內
					if (pf.getDrawdownDate() <= dDateUtil.getCalenderDay()) {
						if ("2".equals(pf.getPieceCode())) {
							pieceCodeCombine = "1";
						} else {
							pieceCodeCombine = "A";
						}
					}
				}
			}
		}
		return pieceCodeCombine;
	}

	/* 刪除業績明細檔 */
	private void deleteDetail(PfDetail pf) throws LogicException {
		this.info(" deleteDetail ");

		Slice<PfItDetail> slPfItDetail = pfItDetailService.findBormNoEq(pf.getCustNo(), pf.getFacmNo(), pf.getBormNo(),
				0, Integer.MAX_VALUE, titaVo);
		if (slPfItDetail != null) {
			try {
				pfItDetailService.deleteAll(slPfItDetail.getContent(), titaVo);
			} catch (DBException e) {
				e.printStackTrace();
				throw new LogicException(titaVo, "E0008", "PfItDetail" + e.getErrorMsg()); // 更新資料時，發生錯誤
			}

		}
		Slice<PfBsDetail> slPfBsDetail = pfBsDetailService.findBormNoEq(pf.getCustNo(), pf.getFacmNo(), pf.getBormNo(),
				0, Integer.MAX_VALUE, titaVo);
		if (slPfBsDetail != null) {
			try {
				pfBsDetailService.deleteAll(slPfBsDetail.getContent(), titaVo);
			} catch (DBException e) {
				e.printStackTrace();
				throw new LogicException(titaVo, "E0008", "PfBsDetail" + e.getErrorMsg()); // 更新資料時，發生錯誤
			}

		}
		Slice<PfReward> slPfReward = pfRewardService.findBormNoEq(pf.getCustNo(), pf.getFacmNo(), pf.getBormNo(), 0,
				Integer.MAX_VALUE, titaVo);
		if (slPfReward != null) {
			try {
				pfRewardService.deleteAll(slPfReward.getContent(), titaVo);
			} catch (DBException e) {
				e.printStackTrace();
				throw new LogicException(titaVo, "E0008", "PfReward" + e.getErrorMsg()); // 更新資料時，發生錯誤
			}

		}

	}
}