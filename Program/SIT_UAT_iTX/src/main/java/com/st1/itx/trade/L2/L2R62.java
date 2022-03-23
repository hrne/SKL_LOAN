package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.LoanCustRmk;
import com.st1.itx.db.domain.LoanCustRmkId;
import com.st1.itx.db.service.LoanCustRmkService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R62")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R62 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public LoanCustRmkService loanCustRmkService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R62 ");
		this.totaVo.init(titaVo);

		// tita
		// 功能 1新增 2修改 4刪除 5查詢
		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFunCd"));
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("RimCustNo"));
		// 備忘錄序號
		int iRmkNo = parse.stringToInteger(titaVo.getParam("RimRmkNo"));

		// new table
		LoanCustRmk tLoanCustRmk = new LoanCustRmk();
		// new PK
		LoanCustRmkId loanCustRmkId = new LoanCustRmkId();
		// 塞值到TablePK
		loanCustRmkId.setCustNo(iCustNo);
		loanCustRmkId.setRmkNo(iRmkNo);
		// FunCd 1新增
		if (iFunCd == 1) {

			// 測試該戶號是否存在帳務備忘錄明細檔
			tLoanCustRmk = loanCustRmkService.maxRmkNoFirst(iCustNo, titaVo);
			// 不存在備忘錄序號為1
			if (tLoanCustRmk == null) {
				tLoanCustRmk = new LoanCustRmk();
			}
			this.info("RmkNo :" + tLoanCustRmk.getRmkNo());
			this.totaVo.putParam("L2r62RmkNo", tLoanCustRmk.getRmkNo() + 1);
			this.totaVo.putParam("L2r62RmkCode", "");
			this.totaVo.putParam("L2r62RmkDesc", "");
			// FunCd 2修改.4刪除.5查詢
		} else {
			tLoanCustRmk = loanCustRmkService.findById(loanCustRmkId, titaVo);
			// 該戶號 備忘錄序號查不到資料 拋錯
			if (tLoanCustRmk == null) {
				throw new LogicException(titaVo, "E0001", "  該戶號" + iCustNo + "備忘錄序號" + iRmkNo + "不存在帳務備忘錄明細檔。"); //查詢資料不存在
			}

			this.totaVo.putParam("L2r62RmkNo", tLoanCustRmk.getRmkNo());
			this.totaVo.putParam("L2r62RmkCode", tLoanCustRmk.getRmkCode());
			this.totaVo.putParam("L2r62RmkDesc", tLoanCustRmk.getRmkDesc());

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}