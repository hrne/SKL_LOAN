package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.SpecInnReCheck;
import com.st1.itx.db.domain.SpecInnReCheckId;
import com.st1.itx.db.service.SpecInnReCheckService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Component("L5107")
@Scope("prototype")

/**
 * 指定覆審名單維護
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L5107 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public SpecInnReCheckService iSpecInnReCheckService;
	@Autowired
	public DataLog idataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		int iCustNo = Integer.valueOf(titaVo.getParam("CustNo"));
		int iFacmNo = Integer.valueOf(titaVo.getParam("FacmNo"));
		int iCycle = Integer.valueOf(titaVo.getParam("Cycle"));
		String iRemark = titaVo.getParam("Remark");
		int iReChkYearMonth = Integer.valueOf(titaVo.getParam("ReChkYearMonth")) + 191100;
		int iFunctionCode = Integer.valueOf(titaVo.getParam("FunctionCode"));

		SpecInnReCheck iSpecInnReCheck = new SpecInnReCheck();
		SpecInnReCheckId iSpecInnReCheckId = new SpecInnReCheckId();
		iSpecInnReCheckId.setCustNo(iCustNo);
		iSpecInnReCheckId.setFacmNo(iFacmNo);
		switch (iFunctionCode) {
		case 1:
			iSpecInnReCheck.setSpecInnReCheckId(iSpecInnReCheckId);
			iSpecInnReCheck.setCycle(iCycle);
			iSpecInnReCheck.setRemark(iRemark);
			iSpecInnReCheck.setReChkYearMonth(iReChkYearMonth);
			try {
				iSpecInnReCheckService.insert(iSpecInnReCheck, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "已有相同資料"); // 資料新建錯誤
			}
			break;
		case 2:
			this.info("UPDATEEEEEEEEEEEEEEEEEEE");
			iSpecInnReCheck = iSpecInnReCheckService.holdById(iSpecInnReCheckId, titaVo);
			if (iSpecInnReCheck == null) {
				throw new LogicException(titaVo, "E0007", "查無資料");
			}

			SpecInnReCheck oldSpecInnReCheck = (SpecInnReCheck) idataLog.clone(iSpecInnReCheck);
			iSpecInnReCheck.setCycle(iCycle);
			iSpecInnReCheck.setRemark(iRemark);
			iSpecInnReCheck.setReChkYearMonth(iReChkYearMonth);
			try {
				iSpecInnReCheck = iSpecInnReCheckService.update2(iSpecInnReCheck, titaVo);

				idataLog.setEnv(titaVo, oldSpecInnReCheck, iSpecInnReCheck);
				idataLog.exec();

			} catch (DBException e) {
				throw new LogicException("E0007", "");
			}
			break;
		case 4:
			iSpecInnReCheck = iSpecInnReCheckService.holdById(iSpecInnReCheckId, titaVo);
			if (iSpecInnReCheck == null) {
				throw new LogicException(titaVo, "E0008", "查無資料");
			}
			try {
				iSpecInnReCheckService.delete(iSpecInnReCheck, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0008", "");
			}
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}
