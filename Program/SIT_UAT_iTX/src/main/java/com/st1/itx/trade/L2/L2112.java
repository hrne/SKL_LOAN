package com.st1.itx.trade.L2;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
/*
 * GroupId=X,10
 * ProdNo=X,5
 * CurrencyCode=X,3
 * AcctCode=9,3
 * ApplDate=9,7
 * CreditOfficer=X,6
 * Introducer=X,6
 * LoanOfficer=X,6
 * Supervisor=X,6
 * CustId1=X,10
 * ApplAmt1=9,14.2
 * CustId2=X,10
 * ApplAmt2=9,14.2
 * CustId3=X,10
 * ApplAmt3=9,14.2
 * CustId4=X,10
 * ApplAmt4=9,14.2
 * CustId5=X,10
 * ApplAmt5=9,14.2
 * CustId6=X,10
 * ApplAmt6=9,14.2
 * CustId7=X,10
 * ApplAmt7=9,14.2
 * CustId8=X,10
 * ApplAmt8=9,14.2
 * CustId9=X,10
 * ApplAmt9=9,14.2
 * CustId10=X,10
 * ApplAmt10=9,14.2
 * CustId11=X,10
 * ApplAmt11=9,14.2
 * CustId12=X,10
 * ApplAmt12=9,14.2
 * CustId13=X,10
 * ApplAmt13=9,14.2
 * CustId14=X,10
 * ApplAmt14=9,14.2
 * CustId15=X,10
 * ApplAmt15=9,14.2
 * CustId16=X,10
 * ApplAmt16=9,14.2
 * CustId17=X,10
 * ApplAmt17=9,14.2
 * CustId18=X,10
 * ApplAmt18=9,14.2
 * ustId19=X,10
 * ApplAmt19=9,14.2
 * CustId20=X,10
 * ApplAmt20=9,14.2
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
	private static final Logger logger = LoggerFactory.getLogger(L2112.class);

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
//				tFacCaseAppl.setAcctCode(titaVo.getParam("AcctCode"));
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

	// 電腦產生,營業日之民國年(2位)+5位之流水號
	/*
	 * private int getGseqApplNo(TitaVo titaVo) throws LogicException { int
	 * gGseqApplNo = 0; int gGseqDate = this.txBuffer.getTxCom().getTbsdy() / 10000
	 * * 10000 + 101; int gGseqCode = 1; String gGseqType = "LN"; String gGseqKind =
	 * "0002";
	 * 
	 * CdGseqId tCdGseqId = new CdGseqId(); tCdGseqId.setGseqDate(gGseqDate);
	 * tCdGseqId.setGseqCode(gGseqCode); tCdGseqId.setGseqType(gGseqType);
	 * tCdGseqId.setGseqKind(gGseqKind); CdGseq tCdGseq =
	 * cdGseqService.holdById(tCdGseqId); if (tCdGseq != null) { gGseqApplNo =
	 * tCdGseq.getSeqNo() + 1; if (gGseqApplNo == tCdGseq.getOffset()) {
	 * tCdGseq.setSeqNo(1); } else { tCdGseq.setSeqNo(gGseqApplNo); }
	 * tCdGseq.setLastUpdate(
	 * parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(),
	 * dDateUtil.getNowIntegerTime()));
	 * tCdGseq.setLastUpdateEmpNo(titaVo.getTlrNo()); try {
	 * cdGseqService.update(tCdGseq); } catch (DBException e) { throw new
	 * LogicException(titaVo, "E2010", "編號編碼檔"); // 更新資料時，發生錯誤 } } else { tCdGseq =
	 * new CdGseq(); tCdGseq.setGseqDate(gGseqDate); tCdGseq.setGseqCode(gGseqCode);
	 * tCdGseq.setGseqType(gGseqType); tCdGseq.setGseqKind(gGseqKind);
	 * tCdGseq.setCdGseqId(tCdGseqId);
	 * tCdGseq.setOffset((this.txBuffer.getTxCom().getTbsdy() - 1000000) / 10000 *
	 * 100000 + 99999); tCdGseq.setSeqNo((this.txBuffer.getTxCom().getTbsdy() -
	 * 1000000) / 10000 * 100000 + 1); tCdGseq.setCreateDate(
	 * parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(),
	 * dDateUtil.getNowIntegerTime())); tCdGseq.setCreateEmpNo(titaVo.getTlrNo());
	 * tCdGseq.setLastUpdate(
	 * parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(),
	 * dDateUtil.getNowIntegerTime()));
	 * tCdGseq.setLastUpdateEmpNo(titaVo.getTlrNo()); try {
	 * cdGseqService.insert(tCdGseq); } catch (DBException e) { throw new
	 * LogicException(titaVo, "E2009", "編號編碼檔"); // 新增資料時，發生錯誤 } gGseqApplNo =
	 * tCdGseq.getSeqNo(); } return gGseqApplNo; }
	 */

}