package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.InnReCheck;
import com.st1.itx.db.domain.InnReCheckId;
import com.st1.itx.db.service.InnReCheckService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita YearMonth=9,5 ConditionCode=9,2 CustNo=9,7 FacmNo=9,3 ReCheckCode=9,1
 * ReChkYearMonth=9,5 ReChkUnit=X,20 FollowMark=9,1 Remark=X,120 END=X,1
 */

@Service("L5105")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L5105 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public InnReCheckService sInnReCheckService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5105 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iConditionCode = this.parse.stringToInteger(titaVo.getParam("ConditionCode"));
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		int iYearMonth = this.parse.stringToInteger(titaVo.getParam("YearMonth"));
		int iFYearMonth = iYearMonth + 191100;

		this.info("L5105 iFYearMonth : " + iFYearMonth + "-" + iConditionCode + "-" + iCustNo + "-" + iFacmNo);

		// 更新覆審案件明細檔
		InnReCheck tInnReCheck = new InnReCheck();

		tInnReCheck = sInnReCheckService.holdById(new InnReCheckId(iFYearMonth, iConditionCode, iCustNo, iFacmNo), titaVo);
		if (tInnReCheck != null) {
			try {
				moveInnReCheck(tInnReCheck, iFYearMonth, iConditionCode, iCustNo, iFacmNo, titaVo);
				sInnReCheckService.update(tInnReCheck, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
		} else {
			throw new LogicException(titaVo, "E0003", titaVo.getParam("CustNo")); // 修改資料不存在
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveInnReCheck(InnReCheck mInnReCheck, int mFYearMonth, int mConditionCode, int mCustNo, int mFacmNo, TitaVo titaVo) throws LogicException {

		int reChkYearMonth = this.parse.stringToInteger(titaVo.getParam("ReChkYearMonth"));
		// reChkYearMonth = reChkYearMonth + 191100;
		if (!(reChkYearMonth == 0)) {
			reChkYearMonth = reChkYearMonth + 191100;
		}

		InnReCheckId mInnReCheckId = new InnReCheckId();
		mInnReCheckId.setYearMonth(mFYearMonth);
		mInnReCheckId.setConditionCode(mConditionCode);
		mInnReCheckId.setCustNo(mCustNo);
		mInnReCheckId.setFacmNo(mFacmNo);
		mInnReCheck.setInnReCheckId(mInnReCheckId);

		mInnReCheck.setYearMonth(mFYearMonth); // 年月份
		mInnReCheck.setConditionCode(mConditionCode); // 條件代碼
		mInnReCheck.setCustNo(mCustNo); // 借款人戶號
		mInnReCheck.setFacmNo(mFacmNo); // 額度號碼
		mInnReCheck.setReCheckCode(titaVo.getParam("ReCheckCode")); // 覆審記號
		mInnReCheck.setReChkYearMonth(reChkYearMonth); // 覆審年月
		mInnReCheck.setReChkUnit(titaVo.getParam("ReChkUnit")); // 應覆審單位
		mInnReCheck.setFollowMark(titaVo.getParam("FollowMark")); // 追蹤記號
		mInnReCheck.setRemark(titaVo.getParam("Remark")); // 備註

		int traceYearMonth = this.parse.stringToInteger(titaVo.getParam("TraceYearMonth"));
		if (!(traceYearMonth == 0)) {
			traceYearMonth = traceYearMonth + 191100;
		}
		mInnReCheck.setTraceMonth(traceYearMonth); // 追蹤年月

		mInnReCheck.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		mInnReCheck.setLastUpdateEmpNo(titaVo.getTlrNo());

	}
}
