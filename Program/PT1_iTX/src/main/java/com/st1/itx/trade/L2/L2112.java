package com.st1.itx.trade.L2;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.service.CdGseqService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.GSeqCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L2112 團體戶申請登錄
 * a.此功能提供團體戶放款申請案件資料輸入,以節省輸入時間
 */

/**
 * L2112 團體戶申請登錄
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L2112")
@Scope("prototype")
public class L2112 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacCaseApplService facCaseApplService;
	@Autowired
	public CustMainService custMainService;
	@Autowired
	public CdGseqService cdGseqService;

	@Autowired
	Parse parse;
	@Autowired
	GSeqCom gGSeqCom;
	@Autowired
	DateUtil dDateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2112 ");
		this.totaVo.init(titaVo);

		String iGroupId = titaVo.getParam("GroupId").trim();
		String wkGroupUkey = "";
		CustMain tCustMain = custMainService.custIdFirst(iGroupId);
		if (tCustMain != null) {
			wkGroupUkey = tCustMain.getCustUKey();
		} else {
			throw new LogicException(titaVo, "E2003", "客戶資料主檔"); // 查無資料
		}

		// 新增時由電腦產生,營業日之民國年(2位)+5位之流水號
		int wkApplNo = 0;
		int wkTbsYy = this.txBuffer.getTxCom().getTbsdy() / 10000;
		FacCaseAppl tFacCaseAppl = null;
		for (int i = 1; i <= 20; i++) {
			this.totaVo.putParam("OApplNo" + i, 0);
			String iCustId = titaVo.getParam("CustId" + i).trim();
			if (!iCustId.isEmpty()) {
				String wkCustUkey = "";
				tCustMain = custMainService.custIdFirst(iCustId);
				if (tCustMain != null) {
					wkCustUkey = tCustMain.getCustUKey();
				} else {
					throw new LogicException(titaVo, "E2003", "客戶資料主檔  " + iCustId); // 查無資料
				}
				// 新增時由電腦產生,營業日之民國年(2位)+5位之流水號
				wkApplNo = gGSeqCom.getSeqNo(wkTbsYy * 10000, 1, "L2", "0002", 99999, titaVo);
				wkApplNo = (wkTbsYy % 100) * 100000 + wkApplNo;
				tFacCaseAppl = new FacCaseAppl();
				tFacCaseAppl.setApplNo(wkApplNo);
				tFacCaseAppl.setCustUKey(wkCustUkey);
				tFacCaseAppl.setApplDate(this.parse.stringToInteger(titaVo.getParam("ApplDate")));
				tFacCaseAppl.setCurrencyCode(titaVo.getParam("CurrencyCode"));
				tFacCaseAppl.setApplAmt(this.parse.stringToBigDecimal(titaVo.getParam("timApplAmt" + i)));
				tFacCaseAppl.setProdNo(titaVo.getParam("ProdNo"));
				tFacCaseAppl.setEstimate("");
				tFacCaseAppl.setPieceCode("A");
				tFacCaseAppl.setCreditOfficer(titaVo.getParam("CreditOfficer"));
				tFacCaseAppl.setLoanOfficer(titaVo.getParam("LoanOfficer"));
				tFacCaseAppl.setIntroducer(titaVo.getParam("Introducer"));
				tFacCaseAppl.setSupervisor(titaVo.getParam("Supervisor"));
				tFacCaseAppl.setProcessCode("0");
				tFacCaseAppl.setGroupUKey(wkGroupUkey);
				tFacCaseAppl.setApproveDate(0);
				tFacCaseAppl.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
				tFacCaseAppl.setCreateEmpNo(titaVo.getTlrNo());
				tFacCaseAppl.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
				tFacCaseAppl.setLastUpdateEmpNo(titaVo.getTlrNo());
				try {
					facCaseApplService.insert(tFacCaseAppl);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E2009", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
				this.totaVo.putParam("OApplNo" + i, wkApplNo);
				wkApplNo++;
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}