package com.st1.itx.trade.L2;

import java.math.BigDecimal;
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
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.ClOtherRights;
import com.st1.itx.db.domain.ClOtherRightsId;
import com.st1.itx.db.domain.FacClose;
import com.st1.itx.db.domain.FacCloseId;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClOtherRightsService;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L2634")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2634 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacCloseService sFacCloseService;
	@Autowired
	public ClOtherRightsService ClOtherRightsService;
	@Autowired
	public ClFacService clFacService;
	@Autowired
	public AcReceivableService acReceivableService;
	/* 報表服務注入 */
	@Autowired
	public L2634ReportA l2634ReportA;
	@Autowired
	public L2634ReportB l2634ReportB;
	@Autowired
	public L2634ReportC l2634ReportC;
	@Autowired
	public L2634ReportD l2634ReportD;
	@Autowired
	public L2634ReportE l2634ReportE;
	@Autowired
	public WebClient webClient;

	@Autowired
	public DateUtil dateUtil;
	@Autowired
	public Parse parse;
	@Autowired
	public DataLog dataLog;
	@Autowired
	public LoanCom loanCom;
	int iEntryDate = 0;
	int iApplDate = 0;
	int iType = 0;
	int choiceDate = 0;
	List<ClOtherRights> lClOtherRights = new ArrayList<ClOtherRights>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2634 ");
		this.totaVo.init(titaVo);

		// tita
		iEntryDate = parse.stringToInteger(titaVo.getParam("EntryDate"));
		iApplDate = parse.stringToInteger(titaVo.getParam("ApplDate"));
		iType = parse.stringToInteger(titaVo.getParam("Type"));
		choiceDate = dateUtil.getNowIntegerRoc();
//		更新篩選資料日期
		setChoiceDate(titaVo);

//		勾選完成最後一筆,列印
		if (titaVo.get("selectTotal") == null || titaVo.get("selectTotal").equals(titaVo.get("selectIndex"))) {

			Slice<ClOtherRights> slClOtherRights = ClOtherRightsService.findChoiceDateEq(choiceDate + 19110000,
					titaVo.getTlrNo(), 0, Integer.MAX_VALUE, titaVo);

			lClOtherRights = slClOtherRights == null ? null : slClOtherRights.getContent();
			if (lClOtherRights != null) {
//				類別 1 申請書及其他
				if (iType == 1) {
					doReport1(titaVo);
					String checkMsg = "申請書及其他-整批列印已完成。";
					webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
							titaVo.getTlrNo() + "L2634", checkMsg, titaVo);
				}
//				類別 2 清償塗銷同意書
				else {
					doReport2(titaVo);

					String checkMsg = "抵押權塗銷同意書-整批列印已完成。";
					webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
							titaVo.getTlrNo() + "L2634", checkMsg, titaVo);
				}
				setChoiceDate0(titaVo);

			}

		}

		this.addList(this.totaVo);
		return this.sendList();
	}

//	更新篩選資料日期
	private void setChoiceDate(TitaVo titaVo) throws LogicException {

		int custNo = 0;
		int closeNo = 0;
		if (titaVo.get("OOCustNo") != null || !titaVo.get("OOCustNo").isEmpty()) {
			custNo = parse.stringToInteger(titaVo.get("OOCustNo"));
		}
		if (titaVo.get("OOCloseNo") != null || !titaVo.get("OOCloseNo").isEmpty()) {
			closeNo = parse.stringToInteger(titaVo.get("OOCloseNo"));
		}
//		列印塗銷同意書更新清償作業檔領取記號
		if (iType == 2) {
			FacClose tFacClose = sFacCloseService.holdById(new FacCloseId(custNo, closeNo), titaVo);
			if (tFacClose != null) {

				if (tFacClose.getCloseDate() == 0) {
					throw new LogicException(titaVo, "E0015", "結案日期未註記"); // 檢查錯誤
				}
				tFacClose.setReceiveFg(1);
				try {
					sFacCloseService.update(tFacClose, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "L2634A FacClose update " + e.getErrorMsg());
				}
			}
		}

		int clCode1 = 0;
		int clCode2 = 0;
		int clNo = 0;
		String clOtherRightsSeq = "";
		if (titaVo.get("OOClCode1") != null || !titaVo.get("OOClCode1").isEmpty()) {
			clCode1 = parse.stringToInteger(titaVo.get("OOClCode1"));
		}
		if (titaVo.get("OOClCode2") != null || !titaVo.get("OOClCode2").isEmpty()) {
			clCode2 = parse.stringToInteger(titaVo.get("OOClCode2"));
		}
		if (titaVo.get("OOClNo") != null || !titaVo.get("OOClNo").isEmpty()) {
			clNo = parse.stringToInteger(titaVo.get("OOClNo"));
		}
		if (titaVo.get("OOSeq") != null || !titaVo.get("OOSeq").isEmpty()) {
			clOtherRightsSeq = titaVo.get("OOSeq");
		}

		ClOtherRights tClOtherRights = ClOtherRightsService
				.holdById(new ClOtherRightsId(clCode1, clCode2, clNo, clOtherRightsSeq), titaVo);
		if (tClOtherRights != null) {

			if (tClOtherRights.getChoiceDate() == 0) {
				tClOtherRights.setChoiceDate(choiceDate);
				tClOtherRights.setReceiveCustNo(custNo);
				tClOtherRights.setCloseNo(closeNo);
				try {
					ClOtherRightsService.update(tClOtherRights, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "L2634A ClOtherRights update " + e.getErrorMsg());
				}
			}
		}

	}

//	更新篩選資料日期
	private void setChoiceDate0(TitaVo titaVo) throws LogicException {

		for (ClOtherRights t : lClOtherRights) {
			ClOtherRights tClOtherRights = ClOtherRightsService.holdById(t, titaVo);
			if (tClOtherRights != null) {
				tClOtherRights.setChoiceDate(0);
				tClOtherRights.setReceiveCustNo(0);
				tClOtherRights.setCloseNo(0);
				if (iType == 2) {
//					更新他項權利領取記號 1已領取
					tClOtherRights.setReceiveFg(1);
				}

				try {
					ClOtherRightsService.update(tClOtherRights, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "L2634A ClOtherRights update " + e.getErrorMsg());
				}

			}
		}

	}

	// 撈今日篩選資料日期 用印申請書及其他
	private void doReport1(TitaVo titaVo) throws LogicException {

		doRptB(lClOtherRights, titaVo); // 用印申請書
		doRptC(lClOtherRights, titaVo);// 簽收回條
		doRptD(lClOtherRights, titaVo);// 雙掛號信封
		doRptE(lClOtherRights, titaVo);// 雙掛號小單

	}

	// 撈今日篩選資料日期 塗銷同意書
	private void doReport2(TitaVo titaVo) throws LogicException {

		doRptA(lClOtherRights, titaVo); // 清償塗銷同意書
	}

	// 清償塗銷同意書
	private void doRptA(List<ClOtherRights> lClOtherRights, TitaVo titaVo) throws LogicException {
		this.info("L2634A doRptA started.");

		// 撈資料組報表
		l2634ReportA.exec(lClOtherRights, titaVo);
	}

	// 用印申請書
	private void doRptB(List<ClOtherRights> lClOtherRights, TitaVo titaVo) throws LogicException {
		this.info("L2634B doRptB started.");

		// 撈資料組報表
		l2634ReportB.exec(lClOtherRights, titaVo);
	}

	// 簽收回條
	private void doRptC(List<ClOtherRights> lClOtherRights, TitaVo titaVo) throws LogicException {
		this.info("L2634C doRptC started.");

		// 撈資料組報表
		l2634ReportC.exec(lClOtherRights, titaVo);

	}

	// 雙掛號信封
	private void doRptD(List<ClOtherRights> lClOtherRights, TitaVo titaVo) throws LogicException {
		this.info("L2634D doRptD started.");

		// 撈資料組報表
		l2634ReportD.exec(lClOtherRights, titaVo);

	}

	// 雙掛號小單
	private void doRptE(List<ClOtherRights> lClOtherRights, TitaVo titaVo) throws LogicException {
		this.info("L2634E doRptE started.");

		// 撈資料組報表
		l2634ReportE.exec(lClOtherRights, titaVo);

	}

	private void checkCloseBreach(int custNo, ClOtherRights tClOtherRights, TitaVo titaVo) throws LogicException {
		Slice<AcReceivable> slAcReceivable = acReceivableService.acrvFacmNoRange(0, custNo, 0, 0, 999, 0,
				Integer.MAX_VALUE);

		if (slAcReceivable == null) {
			return;
		}

		Slice<ClFac> slClFac = clFacService.clNoEq(tClOtherRights.getClCode1(), tClOtherRights.getClCode2(),
				tClOtherRights.getClNo(), 0, Integer.MAX_VALUE, titaVo);
		if (slClFac == null) {
			return;
		}
		// 銷帳檔全銷(減免導致與入帳金額不一致，需自行銷帳)
		for (AcReceivable ac : slAcReceivable.getContent()) {
			if (ac.getAcctCode().equals("IOP") && (ac.getRvBal().compareTo(BigDecimal.ZERO) > 0)) {
				for (ClFac tClFac : slClFac.getContent()) {
					if (ac.getCustNo() == tClFac.getCustNo() && ac.getFacmNo() == tClFac.getFacmNo()) {
						throw new LogicException(titaVo, "E0015",
								"有清償違約金未償還 戶號=" + ac.getCustNo() + "-" + ac.getFacmNo()); // 檢查錯誤

					}
				}
			}
		}
	}
}