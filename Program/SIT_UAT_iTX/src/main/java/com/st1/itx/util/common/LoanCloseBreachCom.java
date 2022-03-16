package com.st1.itx.util.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.data.LoanCloseBreachVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 計算清償違約金<BR>
 * 1.getCloseBreachAmtPaid : 計算即時收取清償違約金(收取方式=1-即時收取) call by LXXXX<BR>
 * 1.1 部分償還 1.2 提前結案時不領清償證明 2.getCloseBreachAmtAll 計算全部清償違約金 call by LXXXX<BR>
 * 2.1 結案時領清償證明(清償作業登錄)，當期(if提前結案)+交易明細(部分償還)<BR>
 * 2.1.1 收取方式=1-即時收取<BR>
 * 2.1.2 收取方式=2-領清償證明時收取<BR>
 * 2.2. 補領領清償證明<BR>
 * 2.2.1 回收登錄-清償違約金時，交易明細(提前結案+部分償還)<BR>
 *
 * @author st1
 *
 */
@Component("loanCloseBreachCom")
@Scope("prototype")
public class LoanCloseBreachCom extends TradeBuffer {

	@Autowired
	public FacProdService facProdService;
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public LoanBorTxService loanBorTxService;

	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;

	private ArrayList<LoanCloseBreachVo> lLoanCloseBreach = new ArrayList<LoanCloseBreachVo>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("LoanCloseBreachCom run ... " + titaVo);
		return null;
	}

	/**
	 * 計算即時收取清償違約金，收取方式 "1":即時收取
	 * 
	 * @param iCustNo 戶號
	 * @param iFacmNo 額度
	 * @param iBormNo 撥款
	 * @param iListVo 清償違約金當期明細
	 * @param titaVo  TitaVo
	 * @return 清償違約金計算明細
	 * @throws LogicException LogicException
	 */
	public ArrayList<LoanCloseBreachVo> getCloseBreachAmtPaid(int iCustNo, int iFacmNo, int iBormNo,
			List<LoanCloseBreachVo> iListVo, TitaVo titaVo) throws LogicException {
		this.info("getCloseBreachAmt  ... ");

		int wkFacmNoStart = 1;
		int wkFacmNoEnd = 999;
		if (iFacmNo > 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
		}
		// 查詢額度主檔
		Slice<FacMain> slFacMain = facMainService.facmCustNoRange(iCustNo, iCustNo, wkFacmNoStart, wkFacmNoEnd, 0,
				Integer.MAX_VALUE, titaVo);
		List<FacMain> lFacMain = slFacMain == null ? null : slFacMain.getContent();
		if (lFacMain == null) {
			throw new LogicException(titaVo, "E2003", "額度主檔"); // 查無資料
		}

		// 依額度的清償違約條件計算清償違約金
		for (FacMain tFacMain : lFacMain) {
			// 查詢商品參數檔
			FacProd tFacProd = facProdService.findById(tFacMain.getProdNo(), titaVo);
			if (tFacProd == null) {
				throw new LogicException(titaVo, "E0001", "商品參數檔 商品代碼 = " + tFacMain.getProdNo()); // 查詢資料不存在
			}
			// 是否限制清償 Y:是 N:否
			if (!"Y".equals(tFacProd.getBreachFlag())) {
				this.info("ProdNo = " + tFacMain.getProdNo() + " skip BreachFlag=" + tFacProd.getBreachFlag());
				continue;
			}

			// 只處理收取方式 "1":即時收取
			if ("1".equals(tFacProd.getBreachGetCode())) {
				// 前期
				loadBortxRoutine(iCustNo, tFacMain.getFacmNo(), iBormNo, titaVo);

				// 本期
				if (iListVo != null && iListVo.size() > 0) {
					for (LoanCloseBreachVo iVo : iListVo) {
						if (iVo.getFacmNo() == tFacMain.getFacmNo()) {
							addListVoRoutine(iVo, titaVo);
						}
					}
				}
				// 計算清償違約金 By 額度
				calculateRoutine(tFacMain, tFacProd, titaVo);
			}
		}

		// LogOutput
		logOutputRoutine();

		// 即時收取清償違約金，不含前期
		ArrayList<LoanCloseBreachVo> lLoanCloseBreach2 = new ArrayList<LoanCloseBreachVo>();
		if (lLoanCloseBreach.size() > 0) {
			for (LoanCloseBreachVo vo : lLoanCloseBreach) {
				if (vo.getAcDate() == 0) {
					lLoanCloseBreach2.add(vo);
				}
			}
		}

		// end
		return lLoanCloseBreach2;
	}

	/**
	 * 計算全部清償違約金 1.當期未繳(收取方式"1":即時收取 "2":領清償證明時收取) 2.交易明細(收取方式"2":領清償證明時收取)
	 * 
	 * @param iCustNo 戶號
	 * @param iFacmNo 額度
	 * @param iBormNo 撥款
	 * @param iListVo 清償違約金當期明細
	 * @param titaVo  TitaV
	 * @return 清償違約金計算明細
	 * @throws LogicException LogicException
	 */
	public ArrayList<LoanCloseBreachVo> getCloseBreachAmtAll(int iCustNo, int iFacmNo, int iBormNo,
			List<LoanCloseBreachVo> iListVo, TitaVo titaVo) throws LogicException {
		this.info("getCloseBreachAmtAll  ");

		int wkFacmNoStart = 1;
		int wkFacmNoEnd = 999;
		if (iFacmNo > 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
		}
		// 查詢額度主檔
		Slice<FacMain> slFacMain = facMainService.facmCustNoRange(iCustNo, iCustNo, wkFacmNoStart, wkFacmNoEnd, 0,
				Integer.MAX_VALUE, titaVo);
		List<FacMain> lFacMain = slFacMain == null ? null : slFacMain.getContent();
		if (lFacMain == null) {
			throw new LogicException(titaVo, "E2003", "額度主檔"); // 查無資料
		}

		// 依額度的清償違約條件計算清償違約金
		for (FacMain tFacMain : lFacMain) {
			// 查詢商品參數檔
			FacProd tFacProd = facProdService.findById(tFacMain.getProdNo(), titaVo);
			if (tFacProd == null) {
				throw new LogicException(titaVo, "E0001", "商品參數檔 商品代碼 = " + tFacMain.getProdNo()); // 查詢資料不存在
			}
			// 是否限制清償 Y:是 N:否
			if (!"Y".equals(tFacProd.getBreachFlag())) {
				this.info("ProdNo = " + tFacMain.getProdNo() + " skip BreachFlag=" + tFacProd.getBreachFlag());
				continue;
			}

			// 前期
			loadBortxRoutine(iCustNo, tFacMain.getFacmNo(), iBormNo, titaVo);

			// 本期
			if (iListVo != null && iListVo.size() > 0) {
				for (LoanCloseBreachVo iVo : iListVo) {
					if (iVo.getFacmNo() == tFacMain.getFacmNo()) {
						addListVoRoutine(iVo, titaVo);
					}
				}
			}
			// 計算清償違約金 By 額度
			calculateRoutine(tFacMain, tFacProd, titaVo);
		}

		// LogOutput
		logOutputRoutine();

		// 全部:L2931清償違約明細、2-領清償證明時收取，當期未繳：1-即時收取
		ArrayList<LoanCloseBreachVo> lLoanCloseBreach2 = new ArrayList<LoanCloseBreachVo>();
		if (lLoanCloseBreach.size() > 0) {
			for (LoanCloseBreachVo vo : lLoanCloseBreach) {
				if ("L2931".equals(titaVo.getTxcd()) || "2".equals(vo.getBreachGetCode()) || vo.getAcDate() == 0) {
					lLoanCloseBreach2.add(vo);
				}
			}
		}
		// end
		return lLoanCloseBreach2;
	}

	// Loan前期提前清償明細
	private void loadBortxRoutine(int iCustNo, int iFacmNo, int iBormNo, TitaVo titaVo) throws LogicException {
		this.info("loadBortxRoutine ... ");
		int wkFacmNoStart = 1;
		int wkFacmNoEnd = 999;
		int wkBormNoStart = 1;
		int wkBormNoEnd = 900;
		if (iFacmNo > 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
		}

		if (iBormNo > 0) {
			wkBormNoStart = iBormNo;
			wkBormNoEnd = iBormNo;
		}

		List<String> lDisplayFlag = new ArrayList<String>();
		lDisplayFlag.add("F"); // 繳息首筆
		lDisplayFlag.add("I"); // 繳息
		List<LoanBorTx> lLoanBorTx = new ArrayList<LoanBorTx>();
		Slice<LoanBorTx> slLoanBorTx = loanBorTxService.borxAcDateRange(iCustNo, wkFacmNoStart, wkFacmNoEnd,
				wkBormNoStart, wkBormNoEnd, 0, 99991231, lDisplayFlag, 0, Integer.MAX_VALUE, titaVo);
		lLoanBorTx = slLoanBorTx == null ? null : slLoanBorTx.getContent();
		if (lLoanBorTx != null) {
			for (LoanBorTx tLoanBorTx : lLoanBorTx) {
				if ("0".equals(tLoanBorTx.getTitaHCode())
						&& tLoanBorTx.getExtraRepay().compareTo(BigDecimal.ZERO) > 0) {
					LoanCloseBreachVo v = new LoanCloseBreachVo();
					v.setCustNo(tLoanBorTx.getCustNo()); // 戶號
					v.setFacmNo(tLoanBorTx.getFacmNo()); // 額度編號
					v.setBormNo(tLoanBorTx.getBormNo()); // 撥款序號
					v.setEndDate(tLoanBorTx.getEntryDate()); // 計算止日
					v.setExtraRepay(tLoanBorTx.getExtraRepay()); // 提前還款金額
					v.setAcDate(tLoanBorTx.getAcDate()); // 會計日期
					v.setTitaTxCd(tLoanBorTx.getTitaTxCd()); // 交易代號
					v.setTitaKinBr(tLoanBorTx.getTitaKinBr());// 單位別
					v.setTitaTlrNo(tLoanBorTx.getTitaTlrNo());// 經辦
					v.setTitaTxtNo(tLoanBorTx.getTitaTxtNo());// 交易序號
					addListVoRoutine(v, titaVo);
				}
			}
		}
	}

	// add List
	private void addListVoRoutine(LoanCloseBreachVo iVo, TitaVo titaVo) throws LogicException {
		// 額度、入帳日、會計日期、交易代號、交易序號，提前還款金額加總、撥款序號=0
		if (lLoanCloseBreach == null || lLoanCloseBreach.size() == 0) {
			lLoanCloseBreach.add(iVo);
		} else {
			Boolean isAddList = true;
			for (LoanCloseBreachVo v : lLoanCloseBreach) {
				if (iVo.getFacmNo() == v.getFacmNo() && iVo.getEndDate() == v.getEndDate()
						&& iVo.getAcDate() == v.getAcDate() && iVo.getTitaTxCd().equals(v.getTitaTxCd())
						&& iVo.getTitaKinBr().equals(v.getTitaKinBr()) && iVo.getTitaTlrNo().equals(v.getTitaTlrNo())
						&& iVo.getTitaTxtNo().equals(v.getTitaTxtNo())) {
					v.setExtraRepay(v.getExtraRepay().add(iVo.getExtraRepay()));
					v.setBormNo(0);
					isAddList = false;
					break;
				}
			}
			if (isAddList) {
				lLoanCloseBreach.add(iVo);
			}
		}
	}

	// 計算
	private void calculateRoutine(FacMain m, FacProd p, TitaVo titaVo) throws LogicException {
		this.info("v=" + lLoanCloseBreach.toString());
		if (lLoanCloseBreach != null && lLoanCloseBreach.size() > 0) {
			BigDecimal extraRepayAcc = BigDecimal.ZERO; // 提前還款金額累計
			for (LoanCloseBreachVo v : lLoanCloseBreach) {
				if (v.getFacmNo() == m.getFacmNo()) {
					v.setProdNo(m.getProdNo()); // 商品代碼
					v.setAmortizedCode(m.getAmortizedCode()); // 攤還方式,還本方式 1.按月繳息(按期繳息到期還本) 2.到期取息(到期繳息還本)
																// 3.本息平均法(期金) 4.本金平均法
					v.setStartDate(m.getFirstDrawdownDate());
					v.setBreachGetCode(p.getBreachGetCode()); // 清償違約金收取方式 "1":即時收取 "2":領清償證明時收取
					v.setBreachCode(p.getBreachCode()); // 違約適用方式
					v.setProhibitMonth(p.getProhibitMonth());// 限制清償年限
					v.setBreachPercent(p.getBreachPercent()); // 違約金百分比;
					v.setBreachDecreaseMonth(p.getBreachDecreaseMonth()); // 違約金分段月數
					v.setBreachDecrease(p.getBreachDecrease()); // 分段遞減百分比;
					v.setLineAmt(m.getLineAmt()); // 核准額度
					v.setUtilBal(m.getUtilBal()); // 撥款金額
					extraRepayAcc = extraRepayAcc.add(v.getExtraRepay());
					v.setExtraRepayAcc(extraRepayAcc); // 提前還款金額累計
					v.setBreachStartPercent(p.getBreachStartPercent()); // 還款起算比例
					this.info("v=" + v.toString());
					v = calcCloseBreachAmt(v, titaVo);
				}
			}
		}
	}

	// 計算清償違約金
	private LoanCloseBreachVo calcCloseBreachAmt(LoanCloseBreachVo calcVo, TitaVo titaVo) throws LogicException {
		int wkMonIdx = 0;
		BigDecimal wkBreachRate = BigDecimal.ZERO;
		BigDecimal wkBreachAmount = BigDecimal.ZERO;
		BigDecimal wkCloseBreachAmt = BigDecimal.ZERO;
		BigDecimal wkBreachStartAmt = BigDecimal.ZERO;

		// 不符計算方式
		if (calcVo.getProhibitMonth() == 0 || calcVo.getStartDate() == 0) {
			this.info("calcCloseBreachAmtRoutine end A");
			return calcVo;
		}

		// 入帳日期>= 禁領期限, 就不計算違約金
		dDateUtil.init();
		dDateUtil.setDate_1(calcVo.getStartDate());
		dDateUtil.setMons(calcVo.getProhibitMonth());
		if (calcVo.getEndDate() >= dDateUtil.getCalenderDay()) {
			this.info("calcCloseBreachAmtRoutine end B");
			return calcVo;
		}

		// 計算遞減段數：計算起日(首撥日) 到計算止日 (入帳日)之間有幾個違約金分段月數
		if (calcVo.getBreachDecreaseMonth() > 0) {
			while (true) {
				wkMonIdx++;
				dDateUtil.init();
				dDateUtil.setDate_1(calcVo.getStartDate());
				dDateUtil.setMons(wkMonIdx * calcVo.getBreachDecreaseMonth());
				if (calcVo.getEndDate() <= dDateUtil.getCalenderDay()) {
					break;
				}
			}
			wkMonIdx--;
		}
		calcVo.setMonIdx(wkMonIdx);

		// 計算百分比 = 違約金百分比 - 遞減段數 * 分段遞減百分比
		wkBreachRate = calcVo.getBreachPercent()
				.subtract(calcVo.getBreachDecrease().multiply(new BigDecimal(wkMonIdx)));
		if (wkBreachRate.compareTo(BigDecimal.ZERO) == 0) {
			this.info("calcCloseBreachAmtRoutine end C");
			return calcVo;
		}

		// 違約起算金額 = 撥款金額 * 還款起算比例
		// 2022-03-16 智偉修改:模仿AS400運算過程中小數位數最多9位，超過時無條件捨去
		wkBreachStartAmt = calcVo.getUtilBal().multiply(new BigDecimal(calcVo.getBreachStartPercent()))
				.divide(new BigDecimal(100), 9, RoundingMode.DOWN).setScale(0, RoundingMode.HALF_UP);
		calcVo.setBreachStartAmt(wkBreachStartAmt);

		// 違約金計算金額
		switch (calcVo.getBreachCode()) { // 違約適用方式
		case "001": // "001":綁約專案[按年分段]
		case "002": // "002":綁約專案[按月分段]
			// 提前還款金額累計 <= 違約起算金額 => 0
			// 提前還款金額累計-違約起算金額 < 提前還款金額 => 提前還款金額累計-違約起算金額
			// else 提前還款金額
			if (calcVo.getExtraRepayAcc().compareTo(wkBreachStartAmt) <= 0) {
				wkBreachAmount = BigDecimal.ZERO;
			} else if ((calcVo.getExtraRepayAcc().subtract(wkBreachStartAmt)).compareTo(calcVo.getExtraRepay()) < 0) {
				wkBreachAmount = calcVo.getExtraRepayAcc().subtract(wkBreachStartAmt);
			} else {
				wkBreachAmount = calcVo.getExtraRepay();
			}
			break;
		case "003": // "003":依核准額度
			// 提前還款金額累計 > 核准額度 => 核准額度
			// else 0
			if (calcVo.getExtraRepayAcc().compareTo(wkBreachStartAmt) > 0) {
				wkBreachAmount = calcVo.getLineAmt();
			} else {
				wkBreachAmount = BigDecimal.ZERO;
			}
			break;
		case "004": // "004":依撥款金額 (已動用額度金額)
			// 提前還款金額累計 > 違約起算金額 => 已動用額度金額
			// else 0
			if (calcVo.getExtraRepayAcc().compareTo(wkBreachStartAmt) > 0) {
				wkBreachAmount = calcVo.getUtilBal();
			} else {
				wkBreachAmount = BigDecimal.ZERO;
			}
			break;
		case "005": // "005":本息均攤依提前償還金額
			if ("3".equals(calcVo.getAmortizedCode()) || "4".equals(calcVo.getAmortizedCode())) {
				if (calcVo.getExtraRepayAcc().compareTo(wkBreachStartAmt) <= 0) {
					wkBreachAmount = BigDecimal.ZERO;
				} else if ((calcVo.getExtraRepayAcc().subtract(wkBreachStartAmt))
						.compareTo(calcVo.getExtraRepay()) < 0) {
					wkBreachAmount = calcVo.getExtraRepayAcc().subtract(wkBreachStartAmt);
				} else {
					wkBreachAmount = calcVo.getExtraRepay();
				}
			} else {
				wkBreachAmount = BigDecimal.ZERO;
			}
			break;
		default:
			this.info("calcCloseBreachAmtRoutine end D");
			return calcVo;
		}
		// 清償違約金 = 計算金額 * 計算百分比 / 100
		// 2022-03-16 智偉修改:模仿AS400運算過程中小數位數最多9位，超過時無條件捨去
		wkCloseBreachAmt = wkBreachAmount.multiply(wkBreachRate).divide(new BigDecimal(100), 9, RoundingMode.DOWN)
				.setScale(0, RoundingMode.HALF_UP);

		calcVo.setAmount(wkBreachAmount); // 計算金額
		calcVo.setBreachRate(wkBreachRate); // 計算利率
		calcVo.setCloseBreachAmt(wkCloseBreachAmt); // 清償違約金
		if ("1".equals(calcVo.getBreachGetCode())) { // 清償違約金收取方式 "1":即時收取 "2":領清償證明時收取
			calcVo.setCloseBreachAmtPaid(wkCloseBreachAmt);
		} else {
			calcVo.setCloseBreachAmtUnpaid(wkCloseBreachAmt);
		}
		this.info("calcCloseBreachAmtRoutine end D");
		this.info("calcCloseBreachAmtRoutine end Vo=" + calcVo.toString());
		return calcVo;
	}

	private void logOutputRoutine() {
		this.info("logOutputRoutine ... ");
		if (lLoanCloseBreach != null && lLoanCloseBreach.size() > 0) {
			for (LoanCloseBreachVo v : lLoanCloseBreach) {
				this.info("  ---------------------------------------- ");
				this.info("  CustNo  戶號 =  " + v.getCustNo());
				this.info("  FacmNo  額度編號  =  " + v.getFacmNo());
				this.info("  BormNo  撥款序號 =   " + v.getBormNo());
				this.info("  ProdNo  商品代碼 =   " + v.getProdNo());
				this.info("  AmortizedCode  攤還方式 =   " + v.getAmortizedCode());
				this.info("  BreachGetCode  清償違約金收取方式 =   " + v.getBreachGetCode());
				this.info("  BreachCode  違約適用方式 =   " + v.getBreachCode());
				this.info("  ProhibitMonth  綁約年限(月) =   " + v.getProhibitMonth());
				this.info("  BreachStartPercent  還款起算比例  =  " + v.getBreachStartPercent());
				this.info("  BreachPercent  違約金百分比 =   " + v.getBreachPercent());
				this.info("  BreachDecreaseMonth  違約金分段月數  =  " + v.getBreachDecreaseMonth());
				this.info("  BreachDecrease  分段遞減百分比 =   " + v.getBreachDecrease());
				this.info("  Amount  計算金額  =  " + v.getAmount());
				this.info("  StartDate  計算起日(首撥日) =   " + v.getStartDate());
				this.info("  EndDate    計算止日 (入帳日)=   " + v.getEndDate());
				this.info("  MonIdx     遞減段數              =   " + v.getMonIdx());
				this.info("  BreachRate  計算百分比         =   " + v.getBreachRate());
				this.info("  CloseBreachAmt  提前清償違約金 =   " + v.getCloseBreachAmt());
				this.info("  CloseBreachAmtPaid  提前清償違約金(即時收取) =   " + v.getCloseBreachAmtPaid());
				this.info("  CloseBreachAmtUnpaid  提前清償違約金(領清償證明時收取)  =  " + v.getCloseBreachAmtUnpaid());
				this.info("  ExtraRepay  提前還款金額  =  " + v.getExtraRepay());
				this.info("  LineAmt  核准額度 =   " + v.getLineAmt());
				this.info("  UtilBal  已動用金額 =   " + v.getUtilBal());
				this.info("  ExtraRepayAcc  提前還款金額累計 =   " + v.getExtraRepayAcc());
				this.info("  BreachStartAmt  違約起算金額 =   " + v.getBreachStartAmt());
				this.info("  AcDate 會計日期 =  " + v.getAcDate());
				this.info("  TitaTxCd  交易代號=   " + v.getTitaTxCd());
				this.info("  TitaKinBr  單位別=   " + v.getTitaKinBr());
				this.info("  TitaTlrNo  經辦 =  " + v.getTitaTlrNo());
				this.info("  TitaTxtNo  交易序號 =  " + v.getTitaTxtNo());
			}
		}
	}

	// 清償違約說明
	public String getBreachDescription(String ProdNo, TitaVo titaVo) throws LogicException {
		this.info("getBreachDescription  ");

		String wkBreachDescription = "";
		String wkBreachA = "";
		String wkBreachB = "";
		String wkBreachC = "";
		String wkBreachD = "";
		String wkBreachE = "";
		String wkBreachF = "";

		FacProd tFacProd = facProdService.findById(ProdNo, titaVo);
		if (tFacProd != null) {
			if ("Y".equals(tFacProd.getBreachFlag())) {
				wkBreachA = "自借款日起算，於未滿 " + tFacProd.getProhibitMonth() + "個月期間提前清償者";
				if (tFacProd.getBreachStartPercent() != 0) {
					wkBreachB = "，還款金額達 " + tFacProd.getBreachStartPercent() + "% 以上時";
				}
				switch (tFacProd.getBreachCode()) {
				case "001":
					wkBreachC = "，按各次提前清償金額";
					break;
				case "002":
					wkBreachC = "，按各次提前清償金額";
					break;
				case "003":
					wkBreachC = "，每次還款按核准額度";
					break;
				case "004":
					wkBreachC = "，每次還款依撥款金額";
					break;
				case "005":
					wkBreachC = "，按各次提前清償金額";
					break;
				}
				if (tFacProd.getBreachPercent().compareTo(BigDecimal.ZERO) > 0) {
					wkBreachD = "，" + tFacProd.getBreachPercent() + "% 計付違約金";
				}
				if (tFacProd.getBreachDecreaseMonth() != 0) {
					wkBreachE = "，但每" + tFacProd.getBreachDecreaseMonth() + "個月遞減違約金" + tFacProd.getBreachDecrease()
							+ "%";
				}
				switch (tFacProd.getBreachGetCode()) {
				case "1":
					wkBreachF = "，即時收取";
					break;

				case "2":
					wkBreachF = "，領清償證明時收取";
					break;
				}

			}
			wkBreachDescription = wkBreachA + wkBreachB + wkBreachC + wkBreachD + wkBreachE + wkBreachF;

		}

		return wkBreachDescription;
	}
}
