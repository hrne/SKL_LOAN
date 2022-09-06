package com.st1.itx.util.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.AcLoanRenew;
import com.st1.itx.db.domain.AcLoanRenewId;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.service.AcLoanRenewService;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * 會計借新還舊檔處理<BR>
 * 1.run ： 展期、借新還舊入帳時更新對照檔 call by AcEntetCom<BR>
 * 1.1.新舊帳號對照檔多對多<BR>
 * 1.2.撥款時，收付欄來源金額最大者為主要額度，其借新還舊結案的第一筆撥款，主要記號=Y<BR>
 * 
 * @author st1
 *
 */
@Component("AcLoanRenewCom")
@Scope("prototype")
public class AcLoanRenewCom extends TradeBuffer {
	@Autowired
	public Parse parse;

	@Autowired
	public AcLoanRenewService acLoanRenewService;

	@Autowired
	public AcReceivableService acReceivableService;

	@Autowired
	public FacMainService facMainService;

	@Autowired
	public FacProdService facProdService;

	private AcLoanRenew tAcLoanRenew = new AcLoanRenew();
	private AcLoanRenewId tAcLoanRenewId = new AcLoanRenewId();
	private TempVo tTempVo = new TempVo();
	private int iNewFacmNo = 0;
	private int iNewBormNo = 0;

	/*----------- 更新會計借新還舊檔 -------------- */
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("AcLoanRenewCom ... ");

//		AcReceivable(AcctCode = TRO)				
//		FacmNo	RvNO	RvAmt	RvBal	SlipNote						
//		001	FacmNo001	300,000	0	展期－一般
//		002	FacmNo002	500,000	0	展期－一般
//  
// 1:應收     L3410  結案   D       300,000     FacmNo001    原額度001
// 1:應收     L3410  結案   D       500,000     FacmNo002    原額度002
// 借新還舊  L3100  撥貸  004-001
// -------------------  C       300,000     FacmNo001    新額度004，原額度001
// -------------------  C       100,000     FacmNo002    新額度004，原額度002
		// 撥款時，收付欄來源金額最大者為主要額度
		int mainFacmNo = 0;
		BigDecimal mainAmt = BigDecimal.ZERO;
		for (AcDetail ac : this.txBuffer.getAcDetailList()) {
			if (ac.getAcctCode().equals("TRO") && ac.getDbCr().equals("C")) { // 借新還舊 撥款
				if (ac.getTxAmt().compareTo(mainAmt) > 0) {
					mainAmt = ac.getTxAmt();
					mainFacmNo = ac.getFacmNo();
				}
			}
		}

		for (AcDetail ac : this.txBuffer.getAcDetailList()) {
			if (ac.getAcctCode().equals("TRO") && ac.getDbCr().equals("C")) { // 借新還舊 撥款
				iNewFacmNo = parse.stringToInteger(titaVo.getMrKey().substring(8, 11)); // 0123456-890-234
				iNewBormNo = parse.stringToInteger(titaVo.getMrKey().substring(12, 15));
				if (this.txBuffer.getTxCom().getBookAcHcode() == 0) // 帳務訂正記號 AcHCode 0.正常 1.當日訂正 2.隔日訂正
					procInsert(ac, mainFacmNo, titaVo);
				else
					procDelete(ac, titaVo);
			}
		}
		return null;
	}

	/* ----------- insert ----------- */
	private void procInsert(AcDetail ac, int mainFacmNo, TitaVo titaVo) throws LogicException {
		// 協議件若原有協議編號需續編
		Slice<AcLoanRenew> slAcLoanRenew = acLoanRenewService.custNoEq(ac.getCustNo(), this.index, Integer.MAX_VALUE, titaVo);
		int tNegNo = 0;
		if (slAcLoanRenew == null) {
		} else {
			for (AcLoanRenew lAcLoanRenew : slAcLoanRenew.getContent()) {
				int iNegNo = 0;
				tTempVo = new TempVo();
				tTempVo = tTempVo.getVo(lAcLoanRenew.getOtherFields());
				if ("2".equals(lAcLoanRenew.getRenewCode()) && !"".equals(tTempVo.getParam("NegNo"))) {
					iNegNo = parse.stringToInteger(tTempVo.getParam("NegNo"));
				}
				if (tNegNo < iNegNo) {
					tNegNo = iNegNo;
				}
			}
		}
		tNegNo = tNegNo + 1;

		// ClsFlag = 1,AND CustNo = ,AND AcctFlag = 1,AND FacmNo >= ,AND FacmNo <=
		// 找戶號、額度下已銷的資負明細科目(放款、催收)，且結案區分為展期或借新還舊
		// 主要額度之借新還舊結案的第一筆撥款，主要記號=Y
		Slice<AcReceivable> acRvList = acReceivableService.acrvFacmNoRange(1, ac.getCustNo(), 1, ac.getFacmNo(), ac.getFacmNo(), this.index, Integer.MAX_VALUE, titaVo);
		String mainFlag = "Y";
		if (acRvList == null) {
			throw new LogicException("E6003", "AcLoanRenewCom 銷帳檔不存在 CustNo=" + ac.getCustNo() + ", FacmNo=" + ac.getFacmNo());
		}
		for (AcReceivable acRv : acRvList.getContent()) {
			tTempVo = new TempVo();
			tTempVo = tTempVo.getVo(acRv.getJsonFields());
			// 1:展期(新額度) 2:借新還舊(同額度)
			if ("1".equals(tTempVo.get("CaseCloseCode")) || "2".equals(tTempVo.get("CaseCloseCode"))) {
				if (acRv.getLastAcDate() != ac.getAcDate()) {
					continue;
				}
				tAcLoanRenewId = new AcLoanRenewId();
				tAcLoanRenewId.setCustNo(acRv.getCustNo());
				tAcLoanRenewId.setNewFacmNo(iNewFacmNo); // 0123456-890-234
				tAcLoanRenewId.setNewBormNo(iNewBormNo);
				tAcLoanRenewId.setOldFacmNo(acRv.getFacmNo());
				tAcLoanRenewId.setOldBormNo(parse.stringToInteger(acRv.getRvNo().substring(0, 3)));
				tAcLoanRenew = new AcLoanRenew();
				tAcLoanRenew.setAcLoanRenewId(tAcLoanRenewId);
				tAcLoanRenew.setRenewCode(tTempVo.get("RenewCode"));
				if ("2".equals(tTempVo.get("RenewCode"))) {// 協議件需寫協議編號
					tTempVo = new TempVo();
					tTempVo.putParam("NegNo", tNegNo);
					tAcLoanRenew.setOtherFields(tTempVo.getJsonString());
				}

				tAcLoanRenew.setAcDate(ac.getAcDate());
				if (ac.getFacmNo() == mainFacmNo) {
					tAcLoanRenew.setMainFlag(mainFlag);
					mainFlag = " ";
				} else {
					tAcLoanRenew.setMainFlag(" ");
				}
				try {
					acLoanRenewService.insert(tAcLoanRenew, titaVo); // insert
				} catch (DBException e) {
					e.printStackTrace();
					throw new LogicException(titaVo, "E6003", "AcLoanRenew insert " + tAcLoanRenewId + e.getErrorMsg());
				}

			}
		}
	}

	/* ----------- delete ----------- */
	private void procDelete(AcDetail ac, TitaVo titaVo) throws LogicException {
		Slice<AcLoanRenew> sacRenewList = acLoanRenewService.NewFacmNoNoRange(ac.getCustNo(), iNewFacmNo, iNewFacmNo, iNewBormNo, iNewBormNo, this.index, Integer.MAX_VALUE, titaVo);
		List<AcLoanRenew> acRenewList = sacRenewList == null ? null : sacRenewList.getContent();
		if (acRenewList != null) {
			try {
				acLoanRenewService.deleteAll(acRenewList, titaVo); // delete
			} catch (DBException e) {
				e.printStackTrace();
				throw new LogicException(titaVo, "E6003", "AcLoanRenew delete " + acRenewList + e.getErrorMsg());
			}
		}
	}
}
