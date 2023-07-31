package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdLoanNotYet;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.LoanNotYet;
import com.st1.itx.db.domain.LoanNotYetId;
import com.st1.itx.db.service.CdLoanNotYetService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanNotYetService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*
 
/**
 * L2801 未齊案件管理
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L2801")
@Scope("prototype")
public class L2801 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	LoanNotYetService loanNotYetService;
	@Autowired
	FacMainService facMainService;
	@Autowired
	CdLoanNotYetService cdLoanNotYetService;
	@Autowired
	Parse parse;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	DataLog datalog;

	@Autowired
	SendRsp sendRsp;

	// work area
	TitaVo iTitaVo = new TitaVo();
	private LoanNotYetId tLoanNotYetId;
	private LoanNotYet tLoanNotYet;
	private int iCustNo;
	private int iFacmNo;
	private int iFunCd;
	private String wkNotYetCode;
	private boolean isEloan = false;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2801 ");
		this.totaVo.init(titaVo);

		// isEloan
		if (titaVo.isEloan() || "ELTEST".equals(titaVo.getTlrNo())) {
			this.isEloan = true;
		}

		// 取得輸入資料
		iTitaVo = titaVo;
		iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		iFunCd = this.parse.stringToInteger(titaVo.getParam("FunCd"));

		wkNotYetCode = titaVo.getParam("NotYetCode").trim();
		tLoanNotYetId = new LoanNotYetId();
		tLoanNotYetId.setCustNo(iCustNo);
		tLoanNotYetId.setFacmNo(iFacmNo);
		tLoanNotYetId.setNotYetCode(wkNotYetCode);
		tLoanNotYet = new LoanNotYet();

		// Eloan輸入
		if (isEloan) {
			tLoanNotYet = loanNotYetService.findById(tLoanNotYetId);
			// 已存在 改修改功能
			if (tLoanNotYet != null) {
				iFunCd = 2;
			}
		}

		switch (iFunCd) {
		case 1:
			tLoanNotYet = loanNotYetService.findById(tLoanNotYetId);
			if (tLoanNotYet != null) {
				throw new LogicException(titaVo, "E0002",
						"戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 未齊件代碼 = " + wkNotYetCode); // 新增資料已存在
			}
			tLoanNotYet = new LoanNotYet();
			moveLoanNotYet(iFunCd);
			try {
				loanNotYetService.insert(tLoanNotYet);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005",
						"戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 未齊件代碼 = " + wkNotYetCode + " " + e.getErrorMsg()); // 新增資料已存在
			}
			break;
		case 2:
			tLoanNotYet = loanNotYetService.holdById(tLoanNotYetId);
			if (tLoanNotYet == null) {
				throw new LogicException(titaVo, "E0006",
						"戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 未齊件代碼 = " + wkNotYetCode); // 鎖定資料時，發生錯誤
			}

			// 異動銷帳日期須刷主管卡
			if (tLoanNotYet.getCloseDate() != parse.stringToInteger(titaVo.getParam("CloseDate"))
					&& titaVo.getEmpNos().trim().isEmpty()) {
				sendRsp.addvReason(this.txBuffer, titaVo, "0004", "修改銷帳日期");
			}

			// 變更前
			LoanNotYet bLoanNotYet = (LoanNotYet) datalog.clone(tLoanNotYet);
			moveLoanNotYet(iFunCd);
			try {
				loanNotYetService.update(tLoanNotYet);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007",
						"戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 未齊件代碼 = " + wkNotYetCode + " " + e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			bLoanNotYet.setCreateDate(tLoanNotYet.getCreateDate()); // 排除建檔時間
			datalog.setEnv(titaVo, bLoanNotYet, tLoanNotYet);
			datalog.exec("修改未齊案件檔資料");
			break;
		case 4:
			tLoanNotYet = loanNotYetService.holdById(tLoanNotYetId);
			if (tLoanNotYet == null) {
				throw new LogicException(titaVo, "E0006",
						"戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 未齊件代碼 = " + wkNotYetCode); // 鎖定資料時，發生錯誤
			}

			// 異動銷帳日期須刷主管卡
			if (titaVo.getEmpNos().trim().isEmpty()) {
				sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}

			try {
				loanNotYetService.delete(tLoanNotYet);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008",
						"戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 未齊件代碼 = " + wkNotYetCode + " " + e.getErrorMsg()); // 刪除資料時，發生錯誤
			}
		default:
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveLoanNotYet(int iFunCd) throws LogicException {

		tLoanNotYet.setCustNo(iCustNo);
		tLoanNotYet.setFacmNo(iFacmNo);
		tLoanNotYet.setNotYetCode(wkNotYetCode);
		tLoanNotYet.setLoanNotYetId(tLoanNotYetId);

		if (isEloan) {
			// 額外計算齊件日期
			int YetDate = 0;
			FacMain tFacMain = facMainService.findById(new FacMainId(iCustNo, iFacmNo), iTitaVo);
			// 不存在抓日歷日
			if (tFacMain == null) {
				throw new LogicException(iTitaVo, "E0001", " 額度主檔 借款人戶 = " + iCustNo + " 額度編號 = " + iFacmNo); // 查詢資料不存在
			} else {
				// 存在抓首撥日 或 首撥日為0時也抓日曆日
				if (tFacMain.getFirstDrawdownDate() == 0) {
					YetDate = this.parse.stringToInteger(iTitaVo.getParam("CALDY"));
				} else {
					YetDate = tFacMain.getFirstDrawdownDate();
				}
			} // else

			// 調未齊件代碼的工作日

			// 查詢未齊件代碼檔
			int wkYetDays = 0;
			CdLoanNotYet cdLoanNotYet = cdLoanNotYetService.findById(wkNotYetCode, iTitaVo);
			if (cdLoanNotYet != null) {
				wkYetDays = cdLoanNotYet.getYetDays();
			} else {
				throw new LogicException(iTitaVo, "E0001", "未齊件代碼檔 未齊件代碼 = " + wkNotYetCode); // 查詢資料不存在
			}

			// 日期加工作日

			dDateUtil.init();
			dDateUtil.getbussDate(YetDate, wkYetDays);

			tLoanNotYet.setYetDate(dDateUtil.getCalenderDay());
		} else {
			tLoanNotYet.setYetDate(this.parse.stringToInteger(iTitaVo.getParam("YetDate")));
		}

		tLoanNotYet.setCloseDate(this.parse.stringToInteger(iTitaVo.getParam("CloseDate")));
		tLoanNotYet.setReMark(iTitaVo.getParam("ReMark"));
		if (iFunCd == 1) { // 新增才填建檔日和人員
			tLoanNotYet.setBranchNo(iTitaVo.getBrno());
			tLoanNotYet.setCreateDate(
					parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			tLoanNotYet.setCreateEmpNo(iTitaVo.getTlrNo());
		}
		tLoanNotYet
				.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		tLoanNotYet.setLastUpdateEmpNo(iTitaVo.getTlrNo());
	}
}