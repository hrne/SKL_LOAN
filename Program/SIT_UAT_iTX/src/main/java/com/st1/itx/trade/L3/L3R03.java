package com.st1.itx.trade.L3;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.LoanBook;
import com.st1.itx.db.domain.LoanBookId;
import com.st1.itx.db.service.LoanBookService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*
 * Tita
 * RimTxCode=X,5
 * RimFuncCode=X,1 1:新增 2:修改 3:拷貝 4:刪除 5:查詢
 * RimCustNo=9,7
 * RimFacmNo=9,3
 * RimBormNoNo=9,7
 * RimBookDate=9,7
 */
/**
 * L3R03 尋找放款約定還本檔
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3R03")
@Scope("prototype")
public class L3R03 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L3R03.class);

	/* DB服務注入 */
	@Autowired
	public LoanBookService loanBookService;

	@Autowired
	Parse parse;
	@Autowired
	DateUtil dDateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		logger.info("active L3R03 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iTxCode = titaVo.getParam("RimTxCode");
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("RimCustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("RimFacmNo"));
		int iBormNo = this.parse.stringToInteger(titaVo.getParam("RimBormNo"));
		int iBookDate = this.parse.stringToInteger(titaVo.getParam("RimBookDate"));

		// 檢查輸入資料
		if (iTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L3R03"); // 交易代號不可為空白
		}
		if (!(iFuncCode >= 1 && iFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "功能 = " + iFuncCode); // 功能選擇錯誤
		}
		// 查詢放款約定還本檔
		LoanBook tLoanBook = loanBookService.findById(new LoanBookId(iCustNo, iFacmNo, iBormNo, iBookDate + 19110000),
				titaVo);
		if (tLoanBook == null) {
			if (iTxCode.equals("L3130") && (iFuncCode == 1)) {
				this.totaVo.putParam("OIncludeIntFlag", "");
				this.totaVo.putParam("OUnpaidIntFlag", "");
				this.totaVo.putParam("OBookAmt", 0);
				this.totaVo.putParam("OBookStatus", 0);
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				throw new LogicException(titaVo, "E0001", "放款約定還本檔"); // 查詢資料不存在
			}
		}
		if (iTxCode.equals("L3130")) { // 約定部分償還登錄
			if (iFuncCode == 1) {
				throw new LogicException(titaVo, "E0002", "放款約定還本檔"); // 新增資料已存在
			}
			if ((iFuncCode == 2 || iFuncCode == 4) && tLoanBook.getStatus() == 1) {
				throw new LogicException(titaVo, "E3056", ""); // 該筆約定部分償還金額已回收
			}
		}

		this.totaVo.putParam("OIncludeIntFlag", tLoanBook.getIncludeIntFlag());
		this.totaVo.putParam("OUnpaidIntFlag", tLoanBook.getUnpaidIntFlag());
		this.totaVo.putParam("OBookAmt", tLoanBook.getBookAmt());
		this.totaVo.putParam("OBookStatus", tLoanBook.getStatus());

		this.addList(this.totaVo);
		return this.sendList();
	}
}