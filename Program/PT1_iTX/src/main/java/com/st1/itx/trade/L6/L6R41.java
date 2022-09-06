package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdPfParms;
import com.st1.itx.db.service.CdPfParmsService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L6R41")
@Scope("prototype")
/**
 * 特殊參數設定(L650C)-調rim
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L6R41 extends TradeBuffer {
	@Autowired
	public CdPfParmsService iCdPfParmsService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		this.info("active L6R41 ");
		this.totaVo.init(titaVo);
		Slice<CdPfParms> aCdPfParms = null;
		Slice<CdPfParms> bCdPfParms = null;
		Slice<CdPfParms> cCdPfParms = null;
		Slice<CdPfParms> dCdPfParms = null;
		Slice<CdPfParms> eCdPfParms = null;
		aCdPfParms = iCdPfParmsService.findCode1AndCode2Eq("3", "1", 0, Integer.MAX_VALUE, titaVo);
		bCdPfParms = iCdPfParmsService.findCode1AndCode2Eq("3", "2", 0, Integer.MAX_VALUE, titaVo);
		cCdPfParms = iCdPfParmsService.findCode1AndCode2Eq("3", "3", 0, Integer.MAX_VALUE, titaVo);
		dCdPfParms = iCdPfParmsService.findCode1AndCode2Eq("3", "4", 0, Integer.MAX_VALUE, titaVo);
		eCdPfParms = iCdPfParmsService.findCode1AndCode2Eq("3", "5", 0, Integer.MAX_VALUE, titaVo);

		if (aCdPfParms != null) {
			totaVo.putParam("L6R41YesNoA", aCdPfParms.getContent().get(0).getCondition());
			totaVo.putParam("L6R41WorkMonthSA", Math.max(aCdPfParms.getContent().get(0).getWorkMonthStart() - 191100, 0)); // 工作月欄位接受輸入0, 避免-191100的情況,
			totaVo.putParam("L6R41WorkMonthEA", Math.max(aCdPfParms.getContent().get(0).getWorkMonthEnd() - 191100, 0)); // 用 Math.max(wm, 0) 做斜坡修正
		} else {
			totaVo.putParam("L6R41YesNoA", "");
			totaVo.putParam("L6R41WorkMonthSA", "");
			totaVo.putParam("L6R41WorkMonthEA", "");
		}

		if (bCdPfParms != null) {
			totaVo.putParam("L6R41YesNoB", bCdPfParms.getContent().get(0).getCondition());
			totaVo.putParam("L6R41WorkMonthSB", Math.max(bCdPfParms.getContent().get(0).getWorkMonthStart() - 191100, 0));
			totaVo.putParam("L6R41WorkMonthEB", Math.max(bCdPfParms.getContent().get(0).getWorkMonthEnd() - 191100, 0));
		} else {
			totaVo.putParam("L6R41YesNoB", "");
			totaVo.putParam("L6R41WorkMonthSB", "");
			totaVo.putParam("L6R41WorkMonthEB", "");
		}

		if (cCdPfParms != null) {
			totaVo.putParam("L6R41YesNoC", cCdPfParms.getContent().get(0).getCondition());
			totaVo.putParam("L6R41WorkMonthSC", Math.max(cCdPfParms.getContent().get(0).getWorkMonthStart() - 191100, 0));
			totaVo.putParam("L6R41WorkMonthEC", Math.max(cCdPfParms.getContent().get(0).getWorkMonthEnd() - 191100, 0));
		} else {
			totaVo.putParam("L6R41YesNoC", "");
			totaVo.putParam("L6R41WorkMonthSC", "");
			totaVo.putParam("L6R41WorkMonthEC", "");
		}

		if (dCdPfParms != null) {
			totaVo.putParam("L6R41YesNoD", dCdPfParms.getContent().get(0).getCondition());
			totaVo.putParam("L6R41WorkMonthSD", Math.max(dCdPfParms.getContent().get(0).getWorkMonthStart() - 191100, 0));
			totaVo.putParam("L6R41WorkMonthED", Math.max(dCdPfParms.getContent().get(0).getWorkMonthEnd() - 191100, 0));
		} else {
			totaVo.putParam("L6R41YesNoD", "");
			totaVo.putParam("L6R41WorkMonthSD", "");
			totaVo.putParam("L6R41WorkMonthED", "");
		}

		if (eCdPfParms != null) {
			totaVo.putParam("L6R41YesNoE", eCdPfParms.getContent().get(0).getCondition());
			totaVo.putParam("L6R41WorkMonthSE", Math.max(eCdPfParms.getContent().get(0).getWorkMonthStart() - 191100, 0));
			totaVo.putParam("L6R41WorkMonthEE", Math.max(eCdPfParms.getContent().get(0).getWorkMonthEnd() - 191100, 0));
		} else {
			totaVo.putParam("L6R41YesNoE", "");
			totaVo.putParam("L6R41WorkMonthSE", "");
			totaVo.putParam("L6R41WorkMonthEE", "");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}