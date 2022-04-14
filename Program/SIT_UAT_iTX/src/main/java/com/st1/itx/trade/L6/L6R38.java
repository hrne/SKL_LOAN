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
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.service.CdPfParmsService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L6R38")
@Scope("prototype")
/**
 * 特殊參數設定(L650A)-調rim
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L6R38 extends TradeBuffer {
	@Autowired
	public CdPfParmsService iCdPfParmsService;
	@Autowired
	public FacProdService iFacProdService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		this.info("active L6R38 ");
		this.totaVo.init(titaVo);
		Slice<CdPfParms> aCdPfParms = null;
		Slice<CdPfParms> bCdPfParms = null;
		Slice<CdPfParms> cCdPfParms = null;
		Slice<CdPfParms> dCdPfParms = null;
		Slice<CdPfParms> eCdPfParms = null;
		aCdPfParms = iCdPfParmsService.findCode1AndCode2Eq("1", "1", 0, Integer.MAX_VALUE, titaVo);
		bCdPfParms = iCdPfParmsService.findCode1AndCode2Eq("1", "2", 0, Integer.MAX_VALUE, titaVo);
		cCdPfParms = iCdPfParmsService.findCode1AndCode2Eq("1", "3", 0, Integer.MAX_VALUE, titaVo);
		dCdPfParms = iCdPfParmsService.findCode1AndCode2Eq("1", "4", 0, Integer.MAX_VALUE, titaVo);
		eCdPfParms = iCdPfParmsService.findCode1AndCode2Eq("1", "5", 0, Integer.MAX_VALUE, titaVo);
		FacProd iFacProd = new FacProd();
		int r = 1;
		if (aCdPfParms != null) {
			for (CdPfParms aaCdPfParms : aCdPfParms) {
				totaVo.putParam("L6R38ProdNoA" + r, aaCdPfParms.getCondition());
				iFacProd = iFacProdService.findById(aaCdPfParms.getCondition(), titaVo);
				if (iFacProd == null) {
					totaVo.putParam("L6R38ProdNoXA" + r, "");
				} else {
					totaVo.putParam("L6R38ProdNoXA" + r, iFacProd.getProdName());
				}
				totaVo.putParam("L6R38WorkMonthSA" + r, Math.max(aaCdPfParms.getWorkMonthStart() - 191100, 0)); // 工作月欄位接受輸入0, 避免-191100的情況,
				totaVo.putParam("L6R38WorkMonthEA" + r, Math.max(aaCdPfParms.getWorkMonthEnd() - 191100, 0));   // 用 Math.max(wm, 0) 做斜坡修正
				r++;
			}
		}
		while (r <= 30) {
			totaVo.putParam("L6R38ProdNoA" + r, "");
			totaVo.putParam("L6R38ProdNoXA" + r, "");
			totaVo.putParam("L6R38WorkMonthSA" + r, "");
			totaVo.putParam("L6R38WorkMonthEA" + r, "");
			r++;
		}
		r = 1;
		if (bCdPfParms != null) {
			for (CdPfParms bbCdPfParms : bCdPfParms) {
				totaVo.putParam("L6R38ProdNoB" + r, bbCdPfParms.getCondition());
				iFacProd = iFacProdService.findById(bbCdPfParms.getCondition(), titaVo);
				if (iFacProd == null) {
					totaVo.putParam("L6R38ProdNoXB" + r, "");
				} else {
					totaVo.putParam("L6R38ProdNoXB" + r, iFacProd.getProdName());
				}
				totaVo.putParam("L6R38WorkMonthSB" + r, Math.max(bbCdPfParms.getWorkMonthStart() - 191100, 0));
				totaVo.putParam("L6R38WorkMonthEB" + r, Math.max(bbCdPfParms.getWorkMonthEnd() - 191100, 0));
				r++;
			}
		}
		while (r <= 30) {
			totaVo.putParam("L6R38ProdNoB" + r, "");
			totaVo.putParam("L6R38ProdNoXB" + r, "");
			totaVo.putParam("L6R38WorkMonthSB" + r, "");
			totaVo.putParam("L6R38WorkMonthEB" + r, "");
			r++;
		}
		r = 1;
		if (cCdPfParms != null) {
			for (CdPfParms ccCdPfParms : cCdPfParms) {
				totaVo.putParam("L6R38ProdNoC" + r, ccCdPfParms.getCondition());
				iFacProd = iFacProdService.findById(ccCdPfParms.getCondition(), titaVo);
				if (iFacProd == null) {
					totaVo.putParam("L6R38ProdNoXC" + r, "");
				} else {
					totaVo.putParam("L6R38ProdNoXC" + r, iFacProd.getProdName());
				}
				totaVo.putParam("L6R38WorkMonthSC" + r, Math.max(ccCdPfParms.getWorkMonthStart() - 191100, 0));
				totaVo.putParam("L6R38WorkMonthEC" + r, Math.max(ccCdPfParms.getWorkMonthEnd() - 191100, 0));
				r++;
			}
		}
		while (r <= 30) {
			totaVo.putParam("L6R38ProdNoC" + r, "");
			totaVo.putParam("L6R38ProdNoXC" + r, "");
			totaVo.putParam("L6R38WorkMonthSC" + r, "");
			totaVo.putParam("L6R38WorkMonthEC" + r, "");
			r++;
		}
		r = 1;
		if (dCdPfParms != null) {
			for (CdPfParms ddCdPfParms : dCdPfParms) {
				totaVo.putParam("L6R38ProdNoD" + r, ddCdPfParms.getCondition());
				iFacProd = iFacProdService.findById(ddCdPfParms.getCondition(), titaVo);
				if (iFacProd == null) {
					totaVo.putParam("L6R38ProdNoXD" + r, "");
				} else {
					totaVo.putParam("L6R38ProdNoXD" + r, iFacProd.getProdName());
				}
				totaVo.putParam("L6R38WorkMonthSD" + r, Math.max(ddCdPfParms.getWorkMonthStart() - 191100, 0));
				totaVo.putParam("L6R38WorkMonthED" + r, Math.max(ddCdPfParms.getWorkMonthEnd() - 191100, 0));
				r++;
			}
		}
		while (r <= 30) {
			totaVo.putParam("L6R38ProdNoD" + r, "");
			totaVo.putParam("L6R38ProdNoXD" + r, "");
			totaVo.putParam("L6R38WorkMonthSD" + r, "");
			totaVo.putParam("L6R38WorkMonthED" + r, "");
			r++;
		}
		r = 1;
		if (eCdPfParms != null) {
			for (CdPfParms eeCdPfParms : eCdPfParms) {
				totaVo.putParam("L6R38ProdNoE" + r, eeCdPfParms.getCondition());
				iFacProd = iFacProdService.findById(eeCdPfParms.getCondition(), titaVo);
				if (iFacProd == null) {
					totaVo.putParam("L6R38ProdNoXE" + r, "");
				} else {
					totaVo.putParam("L6R38ProdNoXE" + r, iFacProd.getProdName());
				}
				totaVo.putParam("L6R38WorkMonthSE" + r, Math.max(eeCdPfParms.getWorkMonthStart() - 191100, 0));
				totaVo.putParam("L6R38WorkMonthEE" + r, Math.max(eeCdPfParms.getWorkMonthEnd() - 191100, 0));
				r++;
			}
		}
		while (r <= 30) {
			totaVo.putParam("L6R38ProdNoE" + r, "");
			totaVo.putParam("L6R38ProdNoXE" + r, "");
			totaVo.putParam("L6R38WorkMonthSE" + r, "");
			totaVo.putParam("L6R38WorkMonthEE" + r, "");
			r++;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}