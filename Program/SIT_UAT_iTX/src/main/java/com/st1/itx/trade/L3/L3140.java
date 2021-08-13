package com.st1.itx.trade.L3;

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
import com.st1.itx.db.domain.LoanNotYet;
import com.st1.itx.db.domain.LoanNotYetId;
import com.st1.itx.db.service.LoanNotYetService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L3140 未齊案件管理
 * a.此功能供維護某一額度其未齊案件資料.
 * b.可於撥款時,於未齊件欄輸入1,即可連結進入本交易.
 * c.未齊件代碼不可重覆
 */
/*
 * Tita
 * TimCustNo=9,7
 * FacmNo=9,3
 * FuncFg1=X,1
 * NotYetCode1=9,2
 * NotYetCodeX1=X,40
 * YetDate1=9,7
 * CloseDate1=9,7
 * FuncFg2=X,1
 * NotYetCode2=9,2
 * NotYetCodeX2=X,40
 * YetDate2=9,7
 * CloseDate2=9,7
 * FuncFg3=X,1
 * NotYetCode3=9,2
 * NotYetCodeX3=X,40
 * YetDate3=9,7
 * CloseDate3=9,7
 * FuncFg4=X,1
 * NotYetCode4=9,2
 * NotYetCodeX4=X,40
 * YetDate4=9,7
 * CloseDate4=9,7
 * FuncFg5=X,1
 * NotYetCode5=9,2
 * NotYetCodeX5=X,40
 * YetDate5=9,7
 * CloseDate5=9,7
 * FuncFg6=X,1
 * NotYetCode6=9,2
 * NotYetCodeX6=X,40
 * YetDate6=9,7
 * CloseDate6=9,7
 * FuncFg7=X,1
 * NotYetCode7=9,2
 * NotYetCodeX7=X,40
 * YetDate7=9,7
 * CloseDate7=9,7
 * FuncFg8=X,1
 * NotYetCode8=9,2
 * NotYetCodeX8=X,40
 * YetDate8=9,7
 * CloseDate8=9,7
 * FuncFg9=X,1
 * NotYetCode9=9,2
 * NotYetCodeX9=X,40
 * YetDate9=9,7
 * CloseDate9=9,7
 * FuncFg10=X,1
 * NotYetCode10=9,2
 * NotYetCodeX10=X,40
 * YetDate10=9,7
 * CloseDate10=9,7
 * FuncFg11=X,1
 * NotYetCode11=9,2
 * NotYetCodeX11=X,40
 * YetDate11=9,7
 * CloseDate11=9,7
 * FuncFg12=X,1
 * NotYetCode12=9,2
 * NotYetCodeX12=X,40
 * YetDate12=9,7
 * CloseDate12=9,7
 * FuncFg13=X,1
 * NotYetCode13=9,2
 * NotYetCodeX13=X,40
 * YetDate13=9,7
 * CloseDate13=9,7
 * FuncFg14=X,1
 * NotYetCode14=9,2
 * NotYetCodeX14=X,40
 * YetDate14=9,7
 * CloseDate14=9,7
 * FuncFg15=X,1
 * NotYetCode15=9,2
 * NotYetCodeX15=X,40
 * YetDate15=9,7
 * CloseDate15=9,7
 * FuncFg16=X,1
 * NotYetCode16=9,2
 * NotYetCodeX16=X,40
 * YetDate16=9,7
 * CloseDate16=9,7
 * FuncFg17=X,1
 * NotYetCode17=9,2
 * NotYetCodeX17=X,40
 * YetDate17=9,7
 * CloseDate17=9,7
 * FuncFg18=X,1
 * NotYetCode18=9,2
 * NotYetCodeX18=X,40
 * YetDate18=9,7
 * CloseDate18=9,7
 * FuncFg19=X,1
 * NotYetCode19=9,2
 * NotYetCodeX19=X,40
 * YetDate19=9,7
 * CloseDate19=9,7
 * FuncFg20=X,1
 * NotYetCode20=9,2
 * NotYetCodeX20=X,40
 * YetDate20=9,7
 * CloseDate20=9,7
 */
/**
 * L3140 未齊案件管理
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3140")
@Scope("prototype")
public class L3140 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L3140.class);

	/* DB服務注入 */
	@Autowired
	public LoanNotYetService loanNotYetService;

	@Autowired
	Parse parse;
	@Autowired
	DateUtil dDateUtil;

	// work area
	TitaVo iTitaVo = new TitaVo();
	private LoanNotYetId tLoanNotYetId;
	private LoanNotYet tLoanNotYet;
	private int iCustNo;
	private int iFacmNo;
	private String wkFuncFg;
	private String wkNotYetCode;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3140 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		iTitaVo = titaVo;
		iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));

		for (int j = 1; j <= 20; j++) {
			wkFuncFg = titaVo.getParam("FuncFg" + j).trim();
			wkNotYetCode = titaVo.getParam("NotYetCode" + j).trim();
			tLoanNotYetId = new LoanNotYetId();
			tLoanNotYetId.setCustNo(iCustNo);
			tLoanNotYetId.setFacmNo(iFacmNo);
			tLoanNotYetId.setNotYetCode(wkNotYetCode);
			tLoanNotYet = new LoanNotYet();
			if (iFacmNo != 0 && !wkFuncFg.isEmpty() && !wkNotYetCode.isEmpty()) {
				switch (wkFuncFg) {
				case "1": // 新增
					moveLoanNotYet(j);
					try {
						loanNotYetService.insert(tLoanNotYet);
					} catch (DBException e) {
						if (e.getErrorId() == 2) {
							throw new LogicException(titaVo, "E0005", "第" + j + "筆戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 未齊件代碼 = " + wkNotYetCode + " " + e.getErrorMsg()); // 新增資料已存在
						}
					}
					break;
				case "2": // 修改
					tLoanNotYet = loanNotYetService.holdById(tLoanNotYetId);
					if (tLoanNotYet == null) {
						throw new LogicException(titaVo, "E0006", "第" + j + "筆戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 未齊件代碼 = " + wkNotYetCode); // 鎖定資料時，發生錯誤
					}
					moveLoanNotYet(j);
					try {
						loanNotYetService.update(tLoanNotYet);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007", "第" + j + "筆戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 未齊件代碼 = " + wkNotYetCode + " " + e.getErrorMsg()); // 更新資料時，發生錯誤
					}
					break;
				case "4": // 刪除
					tLoanNotYet = loanNotYetService.holdById(tLoanNotYetId);
					if (tLoanNotYet == null) {
						throw new LogicException(titaVo, "E0006", "第" + j + "筆戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 未齊件代碼 = " + wkNotYetCode); // 鎖定資料時，發生錯誤
					}
					try {
						loanNotYetService.delete(tLoanNotYet);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0008", "第" + j + "筆戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 未齊件代碼 = " + wkNotYetCode + " " + e.getErrorMsg()); // 刪除資料時，發生錯誤
					}
					break;
				default:
					break;
				}
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveLoanNotYet(int k) throws LogicException {
		tLoanNotYet.setCustNo(iCustNo);
		tLoanNotYet.setFacmNo(iFacmNo);
		tLoanNotYet.setNotYetCode(wkNotYetCode);
		tLoanNotYet.setLoanNotYetId(tLoanNotYetId);
		tLoanNotYet.setNotYetItem(iTitaVo.getParam("NotYetCodeX" + k));
		tLoanNotYet.setYetDate(this.parse.stringToInteger(iTitaVo.getParam("YetDate" + k)));
		tLoanNotYet.setCloseDate(this.parse.stringToInteger(iTitaVo.getParam("CloseDate" + k)));
		tLoanNotYet.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		tLoanNotYet.setCreateEmpNo(iTitaVo.getTlrNo());
		tLoanNotYet.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		tLoanNotYet.setLastUpdateEmpNo(iTitaVo.getTlrNo());
	}
}