package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.YearlyHouseLoanInt;
import com.st1.itx.db.domain.YearlyHouseLoanIntId;
import com.st1.itx.db.service.YearlyHouseLoanIntService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

@Service("L5812")
@Scope("prototype")
/**
 * 
 * 
 * @author Chih Cheng
 * @version 1.0.0
 */
public class L5812 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5812.class);

	/* DB服務注入 */
	@Autowired
	public YearlyHouseLoanIntService sYearlyHouseLoanIntService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5812 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		int iYearMonth = this.parse.stringToInteger(titaVo.getParam("YearMonth")) + 191100;
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		String iUsageCode = titaVo.getParam("UsageCode").trim();
		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L5812"); // 功能選擇錯誤
		}

		// 更新每年房屋擔保借款繳息工作檔
		YearlyHouseLoanInt tYearlyHouseLoanInt = new YearlyHouseLoanInt();
		YearlyHouseLoanIntId tYearlyHouseLoanIntId = new YearlyHouseLoanIntId();
		switch (iFuncCode) {
		case 1: // 新增

			break;

		case 2: // 修改
			tYearlyHouseLoanInt = sYearlyHouseLoanIntService.holdById(new YearlyHouseLoanIntId(iYearMonth, iCustNo, iFacmNo, iUsageCode));
			if (tYearlyHouseLoanInt == null) {
				throw new LogicException(titaVo, "E0003", ""); // 修改資料不存在
			}
			YearlyHouseLoanInt tYearlyHouseLoanInt2 = (YearlyHouseLoanInt) dataLog.clone(tYearlyHouseLoanInt); ////
			try {
				moveYearlyHouse(tYearlyHouseLoanInt, tYearlyHouseLoanIntId, iFuncCode, iYearMonth, iCustNo, iFacmNo, iUsageCode, titaVo);
				tYearlyHouseLoanInt = sYearlyHouseLoanIntService.update2(tYearlyHouseLoanInt, titaVo); ////
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tYearlyHouseLoanInt2, tYearlyHouseLoanInt); ////
			dataLog.exec(); ////
			break;

		case 4: // 刪除
			tYearlyHouseLoanInt = sYearlyHouseLoanIntService.holdById(new YearlyHouseLoanIntId(iYearMonth, iCustNo, iFacmNo, iUsageCode));
			if (tYearlyHouseLoanInt != null) {
				try {
					sYearlyHouseLoanIntService.delete(tYearlyHouseLoanInt);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0004", ""); // 刪除資料不存在
			}
			break;

		case 5: // inq
			break;
		}
		logger.info("3");
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveYearlyHouse(YearlyHouseLoanInt mYearlyHouseLoanInt, YearlyHouseLoanIntId mYearlyHouseLoanIntId, int mFuncCode, int mYearMonth, int mCustNo, int mFacmNo, String mUsageCode,
			TitaVo titaVo) throws LogicException {
		TempVo tTempVo = new TempVo();
		mYearlyHouseLoanIntId.setYearMonth(mYearMonth);
		mYearlyHouseLoanIntId.setCustNo(mCustNo);
		mYearlyHouseLoanIntId.setFacmNo(mFacmNo);
		mYearlyHouseLoanIntId.setUsageCode(mUsageCode);
		mYearlyHouseLoanInt.setYearlyHouseLoanIntId(mYearlyHouseLoanIntId);

		mYearlyHouseLoanInt.setAcctCode(titaVo.getParam("AcctCode"));// 業務科目代號
//		int RepayCode = Integer.parseInt(titaVo.getParam("RepayCode"));
//		String iRepayCode = String.valueOf(RepayCode);
//		mYearlyHouseLoanInt.setRepayCode(iRepayCode);// 繳款方式 
		mYearlyHouseLoanInt.setLoanAmt(this.parse.stringToBigDecimal(titaVo.getParam("LoanAmt"))); // 撥款金額
		mYearlyHouseLoanInt.setLoanBal(this.parse.stringToBigDecimal(titaVo.getParam("LoanBal"))); // 放款餘額
		mYearlyHouseLoanInt.setYearlyInt(this.parse.stringToBigDecimal(titaVo.getParam("YearlyInt"))); // 年度繳息金額

		mYearlyHouseLoanInt.setFirstDrawdownDate(this.parse.stringToInteger(titaVo.getParam("FirstDrawdownDate")));// 初貸日
		mYearlyHouseLoanInt.setMaturityDate(this.parse.stringToInteger(titaVo.getParam("MaturityDate"))); // 到期日
		mYearlyHouseLoanInt.setHouseBuyDate(this.parse.stringToInteger(titaVo.getParam("HouseBuyDate"))); // 房屋取得日期

		tTempVo.putParam("F0", titaVo.getParam("CustName"));   //戶名
		tTempVo.putParam("F1", titaVo.getParam("CustId")); //統編
		tTempVo.putParam("F5", titaVo.getParam("LineAmt"));  //核准額度
		tTempVo.putParam("F21", titaVo.getParam("Location"));  //核准額度
		mYearlyHouseLoanInt.setJsonFields(tTempVo.getJsonString());
		

	}
}
