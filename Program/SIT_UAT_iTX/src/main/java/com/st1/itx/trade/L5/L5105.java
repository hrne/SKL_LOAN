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
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;

/**
 * Tita YearMonth=9,5 ConditionCode=9,2 CustNo=9,7 FacmNo=9,3 ReCheckCode=9,1
 * ReChkYearMonth=9,5 ReChkUnit=X,20 FollowMark=9,1 Remark=X,120 END=X,1
 */

@Service("L5105")
@Scope("prototype")
/**
 *
 *
 * @author Fegie
 * @version 1.0.0
 */
public class L5105 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public InnReCheckService iInnReCheckService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	public DataLog idataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5105 ");
		this.totaVo.init(titaVo);
		
		int iYearMonth = Integer.valueOf(titaVo.getParam("YearMonth"))+191100;
		int iConditionCode = Integer.valueOf(titaVo.getParam("ConditionCode"));
		int iCustNo = Integer.valueOf(titaVo.getParam("CustNo"));
		int iFacmNo = Integer.valueOf(titaVo.getParam("FacmNo"));
		String iReCheckCode = titaVo.getParam("ReCheckCode");
		int iReChkYearMonth = Integer.valueOf(titaVo.getParam("ReChkYearMonth"))+191100;
		String iReChkUnit = titaVo.getParam("ReChkUnit");	
		String iFollowMark = titaVo.getParam("FollowMark");	

		int iTraceYearMonth = 0;
		if (Integer.valueOf(titaVo.getParam("TraceYearMonth"))!=0) {
			iTraceYearMonth = Integer.valueOf(titaVo.getParam("TraceYearMonth"))+191100;
		}
		String iRemark = titaVo.getParam("Remark");	

		InnReCheck iInnReCheck = new InnReCheck();
		InnReCheckId iInnReCheckId = new InnReCheckId();
		iInnReCheckId.setConditionCode(iConditionCode);
		iInnReCheckId.setCustNo(iCustNo);
		iInnReCheckId.setFacmNo(iFacmNo);
		iInnReCheckId.setYearMonth(iYearMonth);
		iInnReCheck = iInnReCheckService.holdById(iInnReCheckId, titaVo);
		if (iInnReCheck == null) {
			throw new LogicException(titaVo,"E0007","查無資料");
		}
		
		InnReCheck oldInnReCheck = (InnReCheck) idataLog.clone(iInnReCheck);
		iInnReCheck.setReCheckCode(iReCheckCode);
		iInnReCheck.setReChkYearMonth(iReChkYearMonth);
		iInnReCheck.setReChkUnit(iReChkUnit);
		iInnReCheck.setFollowMark(iFollowMark);
		iInnReCheck.setTraceMonth(iTraceYearMonth);
		iInnReCheck.setRemark(iRemark);
		
		try {
			iInnReCheck = iInnReCheckService.update2(iInnReCheck, titaVo);

			idataLog.setEnv(titaVo, oldInnReCheck, iInnReCheck);
			idataLog.exec();

		} catch (DBException e) {
			throw new LogicException("E0007", "");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}
