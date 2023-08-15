package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.domain.PfItDetail;
import com.st1.itx.db.domain.TxControl;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.PfItDetailService;
import com.st1.itx.db.service.TxControlService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L5505")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang 2021.11.4
 * @version 1.0.0
 */
public class L5505 extends TradeBuffer {
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public PfItDetailService pfItDetailService;
	@Autowired
	public LoanBorMainService loanBorMainService;

	@Autowired
	public FacMainService facMainService;

	@Autowired
	public TxControlService txControlService;

	private PfItDetail pfItDetail = new PfItDetail();
	private String iFunCode;
	private int iCustNo;
	private int iFacmNo;
	private int iBormNo;
	private int iWorkMonth;
	private BigDecimal iAdjPerfEqAmt;
	private BigDecimal iAdjPerfReward;

	private int perfDate = 0;
	private String pieceCode = "";
	private int drawdownDate = 0;
	private String prodCode = "";
	private BigDecimal drawdownAmt = BigDecimal.ZERO;
	private BigDecimal perfAmt = BigDecimal.ZERO;
	private String cntingCode = "";
	private BigDecimal perfCnt = BigDecimal.ZERO;
	private String deptManager = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5505 ");
		this.totaVo.init(titaVo);

		iFunCode = titaVo.getParam("FunCode").trim();
		iCustNo = Integer.valueOf(titaVo.getParam("CustNo").trim()); // 戶號
		iFacmNo = Integer.valueOf(titaVo.getParam("FacmNo").trim()); // 額度編號
		iBormNo = Integer.valueOf(titaVo.getParam("BormNo").trim()); // 撥款序號
		iWorkMonth = Integer.valueOf(titaVo.getParam("WorkMonth").trim()) + 191100; // 調整工作月
		iAdjPerfEqAmt = new BigDecimal(titaVo.getParam("AdjPerfEqAmt").trim());
		iAdjPerfReward = new BigDecimal(titaVo.getParam("AdjPerfReward").trim());
		
		String controlCode = "L5510." + iWorkMonth + ".2";
		TxControl txControl = txControlService.findById(controlCode, titaVo);
		if (txControl != null) {
			throw new LogicException(titaVo, "E0010", "已產生媒體檔");
		}

		pfItDetail = pfItDetailService.findBormNoFirst(iCustNo, iFacmNo, iBormNo, titaVo);

		if (pfItDetail != null && pfItDetail.getRepayType() == 0) {
			if (pfItDetail.getWorkMonth() == iWorkMonth) {
				throw new LogicException(titaVo, "E0001", "介紹人業績資料");
			}
			perfDate = pfItDetail.getPerfDate();
			pieceCode = pfItDetail.getPieceCode();
			drawdownDate = pfItDetail.getDrawdownDate();
			prodCode = pfItDetail.getProdCode();
			drawdownAmt = pfItDetail.getDrawdownAmt();
			deptManager = pfItDetail.getDeptManager();
		} else {
			LoanBorMain tLoanBorMain = loanBorMainService.findById(new LoanBorMainId(iCustNo, iFacmNo, iBormNo),
					titaVo);
			if (tLoanBorMain == null) {
				throw new LogicException(titaVo, "E0001", "撥款資料");
			}
			FacMain tFacMain = facMainService.findById(new FacMainId(iCustNo, iFacmNo), titaVo);

			perfDate = tLoanBorMain.getDrawdownDate();
			pieceCode = tLoanBorMain.getPieceCode();
			drawdownDate = tLoanBorMain.getDrawdownDate();
			prodCode = tFacMain.getProdNo();
			drawdownAmt = tLoanBorMain.getDrawdownAmt();

		}

		switch (iFunCode) {

		case "1": // 新增業績調整資料
			if (pfItDetail.getRepayType() == 0) {
				createAdj(titaVo);
			} else {
				throw new LogicException(titaVo, "E0010", ""); // 功能選擇錯誤
			}
			break;
		default:
			throw new LogicException(titaVo, "E0010", ""); // 功能選擇錯誤
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void createAdj(TitaVo titaVo) throws LogicException {

		pfItDetail = new PfItDetail();
		pfItDetail.setCustNo(iCustNo);// 戶號
		pfItDetail.setFacmNo(iFacmNo); // 額度編號
		pfItDetail.setBormNo(iBormNo);// 撥款序號
		pfItDetail.setPerfDate(perfDate); // 業績日期
		pfItDetail.setRepayType(4);
		pfItDetail.setPieceCode(pieceCode); // 計件代碼
		pfItDetail.setDrawdownDate(drawdownDate); // 撥款日
		pfItDetail.setProdCode(prodCode); // 商品代碼
		pfItDetail.setDrawdownAmt(drawdownAmt); // 撥款金額/追回金額
		pfItDetail.setIntroducer(titaVo.getParam("Introducer")); // 介紹人
		pfItDetail.setUnitManager(titaVo.getParam("UnitManager")); // 處經理代號
		pfItDetail.setDistManager(titaVo.getParam("DistManager")); // 區經理代號
		pfItDetail.setDeptManager(deptManager); // 部經理代號
		pfItDetail.setDeptCode(titaVo.getParam("DeptCode")); // 部室代號
		pfItDetail.setDistCode(titaVo.getParam("DistCode")); // 區部代號
		pfItDetail.setUnitCode(titaVo.getParam("UnitCode")); // 單位代號CdEmp.CenterCode單位代號
		pfItDetail.setPerfCnt(perfCnt);
		pfItDetail.setAdjRange(2);
		pfItDetail.setWorkMonth(iWorkMonth);
		int season = 0;
		if (iWorkMonth % 100 <= 3)
			season = 1;
		else if (iWorkMonth % 100 <= 6)
			season = 2;
		else if (iWorkMonth % 100 <= 9)
			season = 3;
		else
			season = 4;
		pfItDetail.setWorkSeason((iWorkMonth / 100) * 10 + season);
		pfItDetail.setPerfEqAmt(iAdjPerfEqAmt);// 換算業績
		pfItDetail.setPerfReward(iAdjPerfReward);// 業務報酬
		pfItDetail.setPerfAmt(perfAmt);
		pfItDetail.setCntingCode(cntingCode);
		this.info("pfItDetail=" + pfItDetail.toString());
		try {
			pfItDetail = pfItDetailService.insert(pfItDetail, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", e.getErrorMsg());
		}
	}
}
