package com.st1.itx.trade.L3;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.LoanCustRmk;
import com.st1.itx.db.domain.LoanCustRmkId;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.LoanCustRmkService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L3R30")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L3R30 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public LoanCustRmkService sLoanCustRmkService;

	@Autowired
	public CdEmpService cdEmpService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3R30 ");
		this.totaVo.init(titaVo);

		// tita
		// 功能 1新增 2修改 4刪除 5查詢
		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFunCd"));
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("RimCustNo"));
		// 備忘錄序號
		int iRmkNo = parse.stringToInteger(titaVo.getParam("RimRmkNo"));
		// 會計日期
		int iAcDate = parse.stringToInteger(titaVo.getParam("RimAcDate"));

		// new table
		LoanCustRmk tLoanCustRmk = new LoanCustRmk();
		// new PK
		LoanCustRmkId LoanCustRmkId = new LoanCustRmkId();
		// 塞值到TablePK
		LoanCustRmkId.setCustNo(iCustNo);
		LoanCustRmkId.setRmkNo(iRmkNo);
		LoanCustRmkId.setAcDate(iAcDate);
		// FunCd 1新增
		if (iFunCd == 1) {

			// 測試該戶號是否存在帳務備忘錄明細資料檔
			tLoanCustRmk = sLoanCustRmkService.maxRmkNoFirst(iCustNo, titaVo);
			// 不存在備忘錄序號為1
			if (tLoanCustRmk == null) {
				tLoanCustRmk = new LoanCustRmk();
			}
			this.info("RmkNo :" + tLoanCustRmk.getRmkNo());
			this.totaVo.putParam("L3r30RmkNo", tLoanCustRmk.getRmkNo() + 1);
			this.totaVo.putParam("L3r30RmkCode", "");
			this.totaVo.putParam("L3r30RmkDesc", "");
			this.totaVo.putParam("L3r30LastUpdateEmpNo", "");
			this.totaVo.putParam("L3r30FullName", "");

			// FunCd 2修改.4刪除.5查詢
		} else {
			tLoanCustRmk = sLoanCustRmkService.findById(LoanCustRmkId, titaVo);
			// 該戶號 備忘錄序號查不到資料 拋錯
			if (tLoanCustRmk == null) {
				throw new LogicException(titaVo, "E0001", "  該戶號、備忘錄序號不存在帳務備忘錄明細資料檔。"); //查詢資料不存在
			}

			// 查詢員工資料檔
			String iEmployeeNo = tLoanCustRmk.getLastUpdateEmpNo().trim();
			CdEmp tCdEmp = cdEmpService.findById(iEmployeeNo, titaVo);
			if (tCdEmp == null) {
				this.totaVo.putParam("L3r30FullName", "");
			} else {
				this.totaVo.putParam("L3r30FullName", tCdEmp.getFullname());
			}
			this.totaVo.putParam("L3r30RmkNo", tLoanCustRmk.getRmkNo());
			this.totaVo.putParam("L3r30RmkCode", tLoanCustRmk.getRmkCode());
			this.totaVo.putParam("L3r30RmkDesc", tLoanCustRmk.getRmkDesc());
			this.totaVo.putParam("L3r30LastUpdateEmpNo", tLoanCustRmk.getLastUpdateEmpNo());

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}