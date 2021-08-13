package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.LoanBook;
import com.st1.itx.db.domain.LoanBookId;
import com.st1.itx.db.service.LoanBookService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L3130 約定部分償還登錄
 * a.提供登錄借戶約定部分償還之還本日與金額
 */
/*
 * Tita
 * FuncCode=9,1 1:新增 2:修改 3:拷貝 4:刪除 5:查詢
 * CustNo=9,7
 * FacmNo=9,3
 * BormNo=9,3
 * BookDate=9,7
 * TimBookAmt=9,14.2
 */
/**
 * L3130 約定部分償還登錄
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3130")
@Scope("prototype")
public class L3130 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L3130.class);

	/* DB服務注入 */
	@Autowired
	public LoanBookService loanBookService;

	@Autowired
	Parse parse;
	@Autowired
	DateUtil dDateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3130 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		int iBormNo = this.parse.stringToInteger(titaVo.getParam("BormNo"));
		int iBookDate = this.parse.stringToInteger(titaVo.getParam("BookDate"));
		BigDecimal iBookAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimBookAmt"));

		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "功能 = " + iFuncCode); // 功能選擇錯誤
		}

		// 更新放款約定還本檔
		LoanBookId tLoanBookId = new LoanBookId();
		LoanBook tLoanBook = new LoanBook();
		tLoanBookId.setCustNo(iCustNo);
		tLoanBookId.setFacmNo(iFacmNo);
		tLoanBookId.setBormNo(iBormNo);
		tLoanBookId.setBookDate(iBookDate);
		switch (iFuncCode) {
		case 1: // 新增
			tLoanBook.setCustNo(iCustNo);
			tLoanBook.setFacmNo(iFacmNo);
			tLoanBook.setBormNo(iBormNo);
			tLoanBook.setBookDate(iBookDate);
			tLoanBook.setLoanBookId(tLoanBookId);
			tLoanBook.setStatus(0); // 0: 未回收 1: 已回收
			tLoanBook.setCurrencyCode(titaVo.getCurName());
			tLoanBook.setBookAmt(iBookAmt);
			tLoanBook.setRepayAmt(new BigDecimal(0));
			try {
				tLoanBook.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
				tLoanBook.setCreateEmpNo(titaVo.getTlrNo());
				tLoanBook.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
				tLoanBook.setLastUpdateEmpNo(titaVo.getTlrNo());
				loanBookService.insert(tLoanBook);
			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0005", "放款約定還本檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}
			break;
		case 2: // 修改
			tLoanBook = loanBookService.holdById(tLoanBookId);
			if (tLoanBook == null) {
				throw new LogicException(titaVo, "E0006", "放款約定還本檔"); // 鎖定資料時，發生錯誤
			}
			if (tLoanBook.getStatus() == 1) {
				throw new LogicException(titaVo, "E3056", "放款約定還本檔"); // 該筆資料已回收
			}
			tLoanBook.setBookAmt(iBookAmt);
			try {
				tLoanBook.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
				tLoanBook.setLastUpdateEmpNo(titaVo.getTlrNo());
				loanBookService.update(tLoanBook);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "放款約定還本檔 " + e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			break;
		case 4: // 刪除
			tLoanBook = loanBookService.holdById(tLoanBookId);
			if (tLoanBook == null) {
				throw new LogicException(titaVo, "E0006", "放款約定還本檔"); // 鎖定資料時，發生錯誤
			}
			if (tLoanBook.getStatus() == 1) {
				throw new LogicException(titaVo, "E3056", "放款約定還本檔"); // 該筆資料已回收
			}
			try {
				loanBookService.delete(tLoanBook);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", "放款約定還本檔 " + e.getErrorMsg()); // 刪除資料時，發生錯誤
			}
			break;
		case 5: // 查詢
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}