package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdVarValue;
import com.st1.itx.db.service.CdVarValueService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.common.SendRsp;

/**
 * Tita FuncCode=9,1 YearMonth=9,5 AvailableFunds=9,14 LoanTotalLmt=9,14
 * NoGurTotalLmt=9,14 Totalequity=9,14 END=X,1
 */

@Service("L6502")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6502 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdVarValueService sCdVarValueService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;
	@Autowired
	SendRsp sendRsp;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6502 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		int iYearMonth = this.parse.stringToInteger(titaVo.getParam("YearMonth"));
		int iFYearMonth = iYearMonth + 191100;
		this.info("L6502 iFYearMonth : " + iFYearMonth);

		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 4)) {
			throw new LogicException(titaVo, "E0010", "L6502"); // 功能選擇錯誤
		}

		// 交易需主管核可
		if (!(iFuncCode == 5)) {
			if (!titaVo.getHsupCode().equals("1")) {
				sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
		}

		// 更新變動數值設定檔
		CdVarValue tCdVarValue = new CdVarValue();
		switch (iFuncCode) {
		case 1: // 新增
			moveCdVarValue(tCdVarValue, iFuncCode, iFYearMonth, titaVo);
			try {
				sCdVarValueService.insert(tCdVarValue, titaVo);
			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", e.getErrorMsg()); // 新增資料已存在
				} else {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}
			break;

		case 2: // 修改
			tCdVarValue = sCdVarValueService.holdById(iFYearMonth);
			if (tCdVarValue == null) {
				throw new LogicException(titaVo, "E0003", titaVo.getParam("YearMonth")); // 修改資料不存在
			}
			CdVarValue tCdVarValue2 = (CdVarValue) dataLog.clone(tCdVarValue); ////
			try {
				moveCdVarValue(tCdVarValue, iFuncCode, iFYearMonth, titaVo);
				tCdVarValue = sCdVarValueService.update2(tCdVarValue, titaVo); ////
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tCdVarValue2, tCdVarValue); ////
			dataLog.exec("修改會計變動值設定"); ////
			break;

		case 4: // 刪除
			tCdVarValue = sCdVarValueService.holdById(iFYearMonth);
			this.info("L6502 del : " + iFuncCode + iFYearMonth);
			if (tCdVarValue != null) {
				try {
					sCdVarValueService.delete(tCdVarValue);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0004", titaVo.getParam("YearMonth")); // 刪除資料不存在
			}
			dataLog.setEnv(titaVo, tCdVarValue, tCdVarValue); ////
			dataLog.exec("刪除會計變動值設定"); ////
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveCdVarValue(CdVarValue mCdVarValue, int mFuncCode, int mFYearMonth, TitaVo titaVo) throws LogicException {

		mCdVarValue.setYearMonth(mFYearMonth);
		mCdVarValue.setAvailableFunds(this.parse.stringToBigDecimal(titaVo.getParam("AvailableFunds")));
		mCdVarValue.setLoanTotalLmt(this.parse.stringToBigDecimal(titaVo.getParam("LoanTotalLmt")));
		mCdVarValue.setNoGurTotalLmt(this.parse.stringToBigDecimal(titaVo.getParam("NoGurTotalLmt")));
		mCdVarValue.setTotalequity(this.parse.stringToBigDecimal(titaVo.getParam("Totalequity")));

		if (mFuncCode != 2) {
			mCdVarValue.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			mCdVarValue.setCreateEmpNo(titaVo.getTlrNo());
		}
		mCdVarValue.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		mCdVarValue.setLastUpdateEmpNo(titaVo.getTlrNo());
	}
}
