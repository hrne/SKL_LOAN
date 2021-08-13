package com.st1.itx.trade.L6;

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
import com.st1.itx.db.domain.CdCashFlow;
import com.st1.itx.db.service.CdCashFlowService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

/**
 * Tita FuncCode=9,1 DataYearMonth=N,5 InterestIncome=m,14
 * PrincipalAmortizeAmt=m,14 PrepaymentAmt=m,14 DuePaymentAmt=m,14
 * ExtendAmt=m,14 LoanAmt=m,14 END=X,1
 */

@Service("L6707")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6707 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6707.class);

	/* DB服務注入 */
	@Autowired
	public CdCashFlowService sCdCashFlowService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6707 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		int iDataYearMonth = this.parse.stringToInteger(titaVo.getParam("DataYearMonth"));
		this.info("L6707 1 iDataYearMonth : " + iDataYearMonth);
		int iYearMonth = iDataYearMonth + 191100;
		this.info("L6707 1 iYearMonth : " + iYearMonth);

		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6707"); // 功能選擇錯誤
		}

		// 更新現金流量預估資料檔
		CdCashFlow tCdCashFlow = new CdCashFlow();
		switch (iFuncCode) {
		case 1: // 新增
			moveCdCashFlow(tCdCashFlow, iFuncCode, iYearMonth, titaVo);
			try {
				this.info("1");
				sCdCashFlowService.insert(tCdCashFlow, titaVo);
				this.info("2");
			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", titaVo.getParam("DataYearMonth")); // 新增資料已存在
				} else {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}
			break;

		case 2: // 修改
			tCdCashFlow = sCdCashFlowService.holdById(iYearMonth);
			if (tCdCashFlow == null) {
				throw new LogicException(titaVo, "E0003", titaVo.getParam("DataYearMonth")); // 修改資料不存在
			}
			CdCashFlow tCdCashFlow2 = (CdCashFlow) dataLog.clone(tCdCashFlow); ////
			try {
				moveCdCashFlow(tCdCashFlow, iFuncCode, iYearMonth, titaVo);
				tCdCashFlow = sCdCashFlowService.update2(tCdCashFlow, titaVo); ////
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tCdCashFlow2, tCdCashFlow); ////
			dataLog.exec(); ////
			break;

		case 4: // 刪除
			tCdCashFlow = sCdCashFlowService.holdById(iYearMonth);
			if (tCdCashFlow != null) {
				try {
					sCdCashFlowService.delete(tCdCashFlow);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0004", titaVo.getParam("DataYearMonth")); // 刪除資料不存在
			}
			break;

		case 5: // inq
			break;
		}
		this.info("3");
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveCdCashFlow(CdCashFlow mCdCashFlow, int mFuncCode, int mYearMonth, TitaVo titaVo) throws LogicException {

		mCdCashFlow.setDataYearMonth(mYearMonth);
		mCdCashFlow.setInterestIncome(this.parse.stringToBigDecimal(titaVo.getParam("InterestIncome")));
		mCdCashFlow.setPrincipalAmortizeAmt(this.parse.stringToBigDecimal(titaVo.getParam("PrincipalAmortizeAmt")));
		mCdCashFlow.setPrepaymentAmt(this.parse.stringToBigDecimal(titaVo.getParam("PrepaymentAmt")));
		mCdCashFlow.setDuePaymentAmt(this.parse.stringToBigDecimal(titaVo.getParam("DuePaymentAmt")));
		mCdCashFlow.setExtendAmt(this.parse.stringToBigDecimal(titaVo.getParam("ExtendAmt")));
		mCdCashFlow.setLoanAmt(this.parse.stringToBigDecimal(titaVo.getParam("LoanAmt")));

		if (mFuncCode != 2) {
			mCdCashFlow.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			mCdCashFlow.setCreateEmpNo(titaVo.getTlrNo());
		}
		mCdCashFlow.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		mCdCashFlow.setLastUpdateEmpNo(titaVo.getTlrNo());
	}
}
