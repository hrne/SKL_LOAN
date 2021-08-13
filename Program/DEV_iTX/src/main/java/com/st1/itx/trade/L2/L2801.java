package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.LoanNotYet;
import com.st1.itx.db.domain.LoanNotYetId;
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
	// private static final Logger logger = LoggerFactory.getLogger(L2801.class);

	/* DB服務注入 */
	@Autowired
	public LoanNotYetService loanNotYetService;

	@Autowired
	Parse parse;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	public DataLog datalog;

	@Autowired
	public SendRsp sendRsp;
	
	// work area
	TitaVo iTitaVo = new TitaVo();
	private LoanNotYetId tLoanNotYetId;
	private LoanNotYet tLoanNotYet;
	private int iCustNo;
	private int iFacmNo;
	private int iFunCd;
	private String wkNotYetCode;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2801 ");
		this.totaVo.init(titaVo);

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
		
		switch(iFunCd){
		case 1:
			tLoanNotYet = loanNotYetService.findById(tLoanNotYetId);
			if (tLoanNotYet != null) {
				throw new LogicException(titaVo, "E0002", "戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 未齊件代碼 = " + wkNotYetCode); // 新增資料已存在
			}
			tLoanNotYet = new LoanNotYet();
			moveLoanNotYet();
			try {
				loanNotYetService.insert(tLoanNotYet);
			} catch (DBException e) {		
					throw new LogicException(titaVo, "E0005", "戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 未齊件代碼 = " + wkNotYetCode + " " + e.getErrorMsg()); // 新增資料已存在
			}
			break;
		case 2:
			tLoanNotYet = loanNotYetService.holdById(tLoanNotYetId);
			if (tLoanNotYet == null) {
				throw new LogicException(titaVo, "E0006", "戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 未齊件代碼 = " + wkNotYetCode); // 鎖定資料時，發生錯誤
			}
			
			// 異動銷帳日期須刷主管卡
			if (tLoanNotYet.getCloseDate() != parse.stringToInteger(titaVo.getParam("CloseDate")) && titaVo.getEmpNos().trim().isEmpty()) {
				sendRsp.addvReason(this.txBuffer, titaVo, "0004", "修改銷帳日期");
			}
			
			LoanNotYet bLoanNotYet = tLoanNotYet;
			moveLoanNotYet();
			try {
				loanNotYetService.update(tLoanNotYet);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 未齊件代碼 = " + wkNotYetCode + " " + e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			datalog.setEnv(titaVo, bLoanNotYet, tLoanNotYet);
			datalog.exec();
			break;
		case 4:
			tLoanNotYet = loanNotYetService.holdById(tLoanNotYetId);
			if (tLoanNotYet == null) {
				throw new LogicException(titaVo, "E0006", "戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 未齊件代碼 = " + wkNotYetCode); // 鎖定資料時，發生錯誤
			}

			// 異動銷帳日期須刷主管卡
			if (titaVo.getEmpNos().trim().isEmpty()) {
				sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			
			try {
				loanNotYetService.delete(tLoanNotYet);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", "戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 未齊件代碼 = " + wkNotYetCode + " " + e.getErrorMsg()); // 刪除資料時，發生錯誤
			}
	    default:
	    	break;
		}


		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveLoanNotYet() throws LogicException {
		tLoanNotYet.setCustNo(iCustNo);
		tLoanNotYet.setFacmNo(iFacmNo);
		tLoanNotYet.setNotYetCode(wkNotYetCode);
		tLoanNotYet.setLoanNotYetId(tLoanNotYetId);
		tLoanNotYet.setNotYetItem(iTitaVo.getParam("NotYetCodeX"));
		tLoanNotYet.setYetDate(this.parse.stringToInteger(iTitaVo.getParam("YetDate")));
		tLoanNotYet.setCloseDate(this.parse.stringToInteger(iTitaVo.getParam("CloseDate")));
		tLoanNotYet.setReMark(iTitaVo.getParam("ReMark"));
		tLoanNotYet.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		tLoanNotYet.setCreateEmpNo(iTitaVo.getTlrNo());
		tLoanNotYet.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		tLoanNotYet.setLastUpdateEmpNo(iTitaVo.getTlrNo());
	}
}