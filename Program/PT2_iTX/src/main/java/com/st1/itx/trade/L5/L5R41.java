package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.YearlyHouseLoanInt;
import com.st1.itx.db.domain.YearlyHouseLoanIntId;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.YearlyHouseLoanIntService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L5R41") // 每年房屋擔保借款繳息工作檔
@Scope("prototype")
/**
 *
 *
 * @author Chih Chen
 * @version 1.0.0
 */
public class L5R41 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public YearlyHouseLoanIntService sYearlyHouseLoanIntService;

	@Autowired
	public CustMainService sCustMainService;

	@Autowired
	public FacMainService sFacMainService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5R41 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		int iYearMonth = this.parse.stringToInteger(titaVo.getParam("RimYearMonth")) + 191100;
		int iRimCustNo = this.parse.stringToInteger(titaVo.getParam("RimCustNo"));
		int iRimFacmNo = this.parse.stringToInteger(titaVo.getParam("RimFacmNo"));
		String iRimUsageCode = titaVo.getParam("RimUsageCode").trim();

		// 檢查輸入資料
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L5R41"); // 功能選擇錯誤
		}

		// 初始值Tota
		moveTotaYearlyHouseLoanInt(iRimCustNo, iRimFacmNo, new YearlyHouseLoanInt(), titaVo);

		// 查詢營業單位對照檔
		YearlyHouseLoanInt tYearlyHouseLoanIntService = sYearlyHouseLoanIntService.findById(new YearlyHouseLoanIntId(iYearMonth, iRimCustNo, iRimFacmNo, iRimUsageCode), titaVo);
		/* 如有找到資料 */
		if (tYearlyHouseLoanIntService != null) {

			moveTotaYearlyHouseLoanInt(iRimCustNo, iRimFacmNo, tYearlyHouseLoanIntService, titaVo);

		} else {
			if (iRimFuncCode == 5) {
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				throw new LogicException(titaVo, "E0001", "每年房屋擔保借款繳息工作檔"); // 查無資料
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 將每筆資料放入Tota
	// 營業單位對照檔
	private void moveTotaYearlyHouseLoanInt(int iRimCustNo, int iRimFacmNo, YearlyHouseLoanInt mYearlyHouseLoanInt, TitaVo titaVo) throws LogicException {
		TempVo tTempVo = new TempVo();

		tTempVo = tTempVo.getVo(mYearlyHouseLoanInt.getJsonFields());

		CustMain tCustMain = sCustMainService.custNoFirst(iRimCustNo, iRimCustNo, titaVo);
		if (tCustMain != null) {
			this.totaVo.putParam("L5R41CustName", tCustMain.getCustName());
			this.totaVo.putParam("L5R41CustId", tCustMain.getCustId());
		} else {
			this.totaVo.putParam("L5R41CustName", "");
			this.totaVo.putParam("L5R41CustId", "");
		}

		FacMain tFacMain = sFacMainService.findById(new FacMainId(iRimCustNo, iRimFacmNo), titaVo);
		if (tFacMain != null) {
			this.totaVo.putParam("L5R41LineAmt", tFacMain.getLineAmt());
		} else {
			this.totaVo.putParam("L5R41LineAmt", "");
		}

		this.totaVo.putParam("L5R41AcctCode", mYearlyHouseLoanInt.getAcctCode());
		this.totaVo.putParam("L5R41RepayCode", mYearlyHouseLoanInt.getRepayCode());
		this.totaVo.putParam("L5R41LoanAmt", mYearlyHouseLoanInt.getLoanAmt());
		this.totaVo.putParam("L5R41LoanBal", mYearlyHouseLoanInt.getLoanBal());
		this.totaVo.putParam("L5R41FirstDrawdownDate", mYearlyHouseLoanInt.getFirstDrawdownDate());
		this.totaVo.putParam("L5R41MaturityDate", mYearlyHouseLoanInt.getMaturityDate());
		this.totaVo.putParam("L5R41YearlyInt", mYearlyHouseLoanInt.getYearlyInt());
		this.totaVo.putParam("L5R41HouseBuyDate", mYearlyHouseLoanInt.getHouseBuyDate());
		this.totaVo.putParam("L5R41Location", tTempVo.get("BdLoacation"));

	}

}