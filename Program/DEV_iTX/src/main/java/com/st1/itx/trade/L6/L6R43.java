package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CdPfParms;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CdPfParmsService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L6R43")
@Scope("prototype")
/**
 * 特殊參數設定(L650D)-調Rim[30筆]
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L6R43 extends TradeBuffer {
	@Autowired
	public CdEmpService iCdEmpService;
	@Autowired
	public CdPfParmsService iCdPfParmsService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		this.info("active L6R43 ");
		this.totaVo.init(titaVo);

		Slice<CdPfParms> iCdPfParams = null;
		int i = 1;
		iCdPfParams = iCdPfParmsService.findCode1AndCode2Eq("4", " ", 0, Integer.MAX_VALUE, titaVo);
		if (iCdPfParams == null) {
			while (i <= 30) {
				totaVo.putParam("L6R43EmpNo" + i, "");
				totaVo.putParam("L6R43Email" + i, "");
				totaVo.putParam("L6R43WorkMonthS" + i, "");
				totaVo.putParam("L6R43WorkMonthE" + i, "");
				i++;
			}
		} else {
			for (CdPfParms rCdPfParms : iCdPfParams) {
				if (i > 30) {
					break;
				}
				totaVo.putParam("L6R43EmpNo" + i, rCdPfParms.getCondition());
				CdEmp iCdEmp = new CdEmp();
				iCdEmp = iCdEmpService.findById(rCdPfParms.getCondition(), titaVo);
				if (iCdEmp == null) {
					totaVo.putParam("L6R43Email" + i, "");
				} else {
					totaVo.putParam("L6R43Email" + i, iCdEmp.getEmail());
				}
				totaVo.putParam("L6R43WorkMonthS" + i, Math.max(Integer.valueOf(rCdPfParms.getWorkMonthStart()) - 191100, 0)); // 工作月欄位接受輸入0, 避免-191100的情況,
				totaVo.putParam("L6R43WorkMonthE" + i, Math.max(Integer.valueOf(rCdPfParms.getWorkMonthEnd()) - 191100, 0));   // 用 Math.max(wm, 0) 做斜坡修正
				i++;
			}
			while (i <= 30) {
				totaVo.putParam("L6R43EmpNo" + i, "");
				totaVo.putParam("L6R43Email" + i, "");
				totaVo.putParam("L6R43WorkMonthS" + i, "");
				totaVo.putParam("L6R43WorkMonthE" + i, "");
				i++;
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}