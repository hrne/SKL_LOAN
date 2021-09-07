package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdPfParms;
import com.st1.itx.db.domain.CdPfParmsId;
import com.st1.itx.db.service.CdPfParmsService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L650D")
@Scope("prototype")
/**
 * 排除部門別-
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L650D extends TradeBuffer {

	/* 轉型共用工具 */

	@Autowired
	public CdPfParmsService iCdPfParmsService;
	@Autowired
	Parse iParse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		this.info("active L650D ");
		this.totaVo.init(titaVo);
		// 先刪除
		Slice<CdPfParms> dCdPfParms = null;
		dCdPfParms = iCdPfParmsService.findConditionCode1Eq("4", 0, Integer.MAX_VALUE, titaVo);
		if (dCdPfParms != null) {
			for (CdPfParms xCdPfParms : dCdPfParms) {
				try {
					iCdPfParmsService.delete(xCdPfParms, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg());
				}
			}
		}

		CdPfParms iCdPfParams = new CdPfParms();
		CdPfParmsId iCdPfParamsId = new CdPfParmsId();
		String iEmpNo = "";
		int iWorkMonthS = 0;
		int iWorkMonthE = 0;
 		for (int i = 1;i<=30;i++) {
 			iEmpNo = titaVo.getParam("EmpNo"+i);
 			if (iEmpNo.trim().isEmpty()) {
 				continue;
 			}
 			iCdPfParamsId.setCondition(iEmpNo);
 			iCdPfParamsId.setConditionCode1("4");
 			iCdPfParamsId.setConditionCode2(" ");
 			iCdPfParams.setCdPfParmsId(iCdPfParamsId);
 			iCdPfParams.setWorkMonthStart(Integer.valueOf(titaVo.getParam("WorkMonthS"+i)));
 			iCdPfParams.setWorkMonthEnd(Integer.valueOf(titaVo.getParam("WorkMonthE"+i)));
 			try {
				iCdPfParmsService.insert(iCdPfParams, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg());
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}