package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
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
 * L2111 案件申請登錄
 * a.申請號碼:新增時由電腦產生,營業日之民國年(2位)+5位之流水號
 * b.若為團體戶件,則須輸入團體戶之統一編號
 * c.案件核准後,只可修改專辦,協辦, 核決主管,介紹人及駐區資料
 */
/*
 * FuncCode=X,1
 * CustId=X,10
 * ApplNo=9,7
 * ApplDate=9,7
 * ProdNo=X,5
 * AcctCode=9,3
 * CurrencyCode=X,3
 * TimApplAmt=9,14.2
 * Estimate=X,6
 * PieceCode=X,1
 * CreditOfficer=X,6
 * LoanOfficer=X,6
 * Introducer=X,6
 * Supervisor=X,6
 * Coorgnizer=X,6
 * ProcessCode=9,1
 * GroupId=X,10
 */
/**
 * L2111 案件申請登錄
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L2111")
@Scope("prototype")
public class L2111 extends TradeBuffer {

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

	// input area
	private TitaVo titaVo = new TitaVo();
	private int iFuncCode;
	private int iApplNo;
	private String iCustId;
	private String iGroupId;

	// work area
	private FacCaseAppl tFacCaseAppl = new FacCaseAppl();
	private String wkCustUkey = "";
	private String wkGroupUkey = "";

	private boolean isEloan = false;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2111 ");

		this.totaVo.init(titaVo);
		this.titaVo = titaVo;

		// isEloan
		if (titaVo.isEloan() || "ELTEST".equals(titaVo.getTlrNo())) {
			this.isEloan = true;
		}
		// 取得輸入資料
		if (this.isEloan) {
			iFuncCode = 1;
		} else {
			iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		}
		iApplNo = this.parse.stringToInteger(titaVo.getParam("ApplNo"));
		iCustId = titaVo.getParam("CustId").trim();
		iGroupId = titaVo.getParam("GroupId").trim();

		CustMain tCustMain = custMainService.custIdFirst(iCustId);
		if (tCustMain == null) {
			throw new LogicException(titaVo, "E2003", "客戶資料主檔" + iCustId); // 查無資料
		}
		wkCustUkey = tCustMain.getCustUKey();
		if (!iGroupId.isEmpty()) {
			tCustMain = custMainService.custIdFirst(iGroupId);
			if (tCustMain == null) {
				throw new LogicException(titaVo, "E2003", "客戶資料主檔" + iGroupId); // 查無資料
			}
			wkGroupUkey = tCustMain.getCustUKey();
		}

		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 5)) {
			throw new LogicException(titaVo, "E2004", "L2R01"); // 功能選擇錯誤
		}

		// 更新案件申請檔
		int WkTbsYy = this.txBuffer.getTxCom().getTbsdy() / 10000;
		int wkApplNo = iApplNo;
		switch (iFuncCode) {
		case 1: // 新增
		case 3: // 拷貝
			// 新增時由電腦產生,營業日之民國年(2位)+5位之流水號
			wkApplNo = gGSeqCom.getSeqNo(WkTbsYy * 10000, 1, "L2", "0002", 99999, titaVo);
			wkApplNo = (WkTbsYy % 100) * 100000 + wkApplNo;
			tFacCaseAppl = new FacCaseAppl();
			tFacCaseAppl.setApplNo(wkApplNo);
			FacCaseApplRoutine();
			try {
				facCaseApplService.insert(tFacCaseAppl);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E2009", e.getErrorMsg()); // 新增資料時，發生錯誤
			}
			break;
		case 2: /*
				 * 修改 案件核准後,只可修改專辦,協辦, 核決主管,介紹人及駐區資料 非本日所建資料不可修改 非建檔之放款站或櫃員,不可修改
				 */
			tFacCaseAppl = facCaseApplService.holdById(iApplNo);
			if (tFacCaseAppl == null) {
				throw new LogicException(titaVo, "E2006", "案件申請檔"); // 修改資料不存在
			}
			if (tFacCaseAppl.getProcessCode().equals("1")) {
				tFacCaseAppl.setCreditOfficer(titaVo.getParam("CreditOfficer"));
				tFacCaseAppl.setLoanOfficer(titaVo.getParam("LoanOfficer"));
				tFacCaseAppl.setIntroducer(titaVo.getParam("Introducer"));
				tFacCaseAppl.setSupervisor(titaVo.getParam("Supervisor"));
				tFacCaseAppl.setCoorgnizer(titaVo.getParam("Coorgnizer"));
			} else {
				FacCaseApplRoutine();
			}
			try {
				facCaseApplService.update(tFacCaseAppl);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E2010", "案件申請檔"); // 更新資料時，發生錯誤
			}
			break;
		case 4: // 刪除 案件申請准駁後禁止刪除
			tFacCaseAppl = facCaseApplService.holdById(iApplNo);
			if (tFacCaseAppl != null) {
				if (!tFacCaseAppl.getProcessCode().equals("0")) {
					throw new LogicException(titaVo, "E2063", "案件申請檔"); // 已作過准駁處理之案件不可刪除
				}
			} else {
				throw new LogicException(titaVo, "E2007", "案件申請檔"); // 刪除資料不存在
			}
			try {
				facCaseApplService.delete(tFacCaseAppl);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E2008", "案件申請檔"); // 刪除資料時，發生錯誤
			}
			break;
		case 5: // inq
			break;
		}
		this.totaVo.putParam("OApplNo", wkApplNo);
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void FacCaseApplRoutine() throws LogicException {
		tFacCaseAppl.setCustUKey(wkCustUkey);
		tFacCaseAppl.setCreditSysNo(parse.stringToInteger(titaVo.getParam("CreditSysNo")));
		tFacCaseAppl.setApplDate(this.parse.stringToInteger(titaVo.getParam("ApplDate")));
		tFacCaseAppl.setCurrencyCode(titaVo.getParam("CurrencyCode"));
		tFacCaseAppl.setApplAmt(this.parse.stringToBigDecimal(titaVo.getParam("TimApplAmt")));
		tFacCaseAppl.setProdNo(titaVo.getParam("ProdNo"));
//		tFacCaseAppl.setAcctCode(titaVo.getParam("AcctCode"));
		tFacCaseAppl.setEstimate(titaVo.getParam("Estimate"));
		tFacCaseAppl.setPieceCode(titaVo.getParam("PieceCode"));
		tFacCaseAppl.setCreditOfficer(titaVo.getParam("CreditOfficer"));
		tFacCaseAppl.setLoanOfficer(titaVo.getParam("LoanOfficer"));
		tFacCaseAppl.setIsLimit(titaVo.getParam("IsLimit"));
		tFacCaseAppl.setIsLnrelNear(titaVo.getParam("IsLnrelNear"));
		tFacCaseAppl.setIsRelated(titaVo.getParam("IsRelated"));
		tFacCaseAppl.setIntroducer(titaVo.getParam("Introducer"));
		tFacCaseAppl.setSupervisor(titaVo.getParam("Supervisor"));
		tFacCaseAppl.setCoorgnizer(titaVo.getParam("Coorgnizer"));
		tFacCaseAppl.setProcessCode(titaVo.getParam("ProcessCode"));
		tFacCaseAppl.setGroupUKey(wkGroupUkey);
		if (iFuncCode != 2) {
			tFacCaseAppl.setApproveDate(0);
		}
//		新增修改時更新客戶主檔介紹人
		CustMain upCustMain = custMainService.findById(wkCustUkey, titaVo);
		if (upCustMain != null) {
			upCustMain.setIntroducer(titaVo.getParam("Introducer"));
			try {
				custMainService.update2(upCustMain, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E2010", "客戶主檔"); // 更新資料時，發生錯誤
			}
		}

	}
}