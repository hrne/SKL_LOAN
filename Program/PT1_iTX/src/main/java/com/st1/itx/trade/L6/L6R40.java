package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBcm;
import com.st1.itx.db.domain.CdPfParms;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.CdPfParmsService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L6R40")
@Scope("prototype")
/**
 * 特殊參數設定(L650B)-調rim
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L6R40 extends TradeBuffer {
	@Autowired
	public CdPfParmsService iCdPfParmsService;
	@Autowired
	public FacProdService iFacProdService;
	@Autowired
	public CdBcmService iCdBcmService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		this.info("active L6R40 ");
		this.totaVo.init(titaVo);
		Slice<CdPfParms> aCdPfParms = null;
		Slice<CdPfParms> bCdPfParms = null;
		Slice<CdPfParms> cCdPfParms = null;
		Slice<CdPfParms> dCdPfParms = null;
		Slice<CdPfParms> eCdPfParms = null;
		aCdPfParms = iCdPfParmsService.findCode1AndCode2Eq("2", "1", 0, Integer.MAX_VALUE, titaVo);
		bCdPfParms = iCdPfParmsService.findCode1AndCode2Eq("2", "2", 0, Integer.MAX_VALUE, titaVo);
		cCdPfParms = iCdPfParmsService.findCode1AndCode2Eq("2", "3", 0, Integer.MAX_VALUE, titaVo);
		dCdPfParms = iCdPfParmsService.findCode1AndCode2Eq("2", "4", 0, Integer.MAX_VALUE, titaVo);
		eCdPfParms = iCdPfParmsService.findCode1AndCode2Eq("2", "5", 0, Integer.MAX_VALUE, titaVo);
		new FacProd();
		CdBcm iCdBcm = new CdBcm();
		int r = 1;
		if (aCdPfParms != null) {
			for (CdPfParms aaCdPfParms : aCdPfParms) {
				totaVo.putParam("L6R40DeptCodeA" + r, aaCdPfParms.getCondition());
				iCdBcm = iCdBcmService.deptCodeFirst(aaCdPfParms.getCondition(), titaVo);
				if (iCdBcm == null) {
					totaVo.putParam("L6R40DeptCodeXA" + r, "");
				} else {
					totaVo.putParam("L6R40DeptCodeXA" + r, iCdBcm.getDeptItem());
				}
				totaVo.putParam("L6R40WorkMonthSA" + r, Math.max(Integer.valueOf(aaCdPfParms.getWorkMonthStart()) - 191100, 0)); // 工作月欄位接受輸入0, 避免-191100的情況,
				totaVo.putParam("L6R40WorkMonthEA" + r, Math.max(Integer.valueOf(aaCdPfParms.getWorkMonthEnd()) - 191100, 0));   // 用 Math.max(wm, 0) 做斜坡修正
				r++;
			}
		}
		while (r <= 30) {
			totaVo.putParam("L6R40DeptCodeA" + r, "");
			totaVo.putParam("L6R40DeptCodeXA" + r, "");
			totaVo.putParam("L6R40WorkMonthSA" + r, "");
			totaVo.putParam("L6R40WorkMonthEA" + r, "");
			r++;
		}
		r = 1;
		if (bCdPfParms != null) {
			for (CdPfParms bbCdPfParms : bCdPfParms) {
				totaVo.putParam("L6R40DeptCodeB" + r, bbCdPfParms.getCondition());
				iCdBcm = iCdBcmService.deptCodeFirst(bbCdPfParms.getCondition(), titaVo);
				if (iCdBcm == null) {
					totaVo.putParam("L6R40DeptCodeXB" + r, "");
				} else {
					totaVo.putParam("L6R40DeptCodeXB" + r, iCdBcm.getDeptItem());
				}
				totaVo.putParam("L6R40WorkMonthSB" + r, Math.max(Integer.valueOf(bbCdPfParms.getWorkMonthStart()) - 191100, 0));
				totaVo.putParam("L6R40WorkMonthEB" + r, Math.max(Integer.valueOf(bbCdPfParms.getWorkMonthEnd()) - 191100, 0));
				r++;
			}
		}
		while (r <= 30) {
			totaVo.putParam("L6R40DeptCodeB" + r, "");
			totaVo.putParam("L6R40DeptCodeXB" + r, "");
			totaVo.putParam("L6R40WorkMonthSB" + r, "");
			totaVo.putParam("L6R40WorkMonthEB" + r, "");
			r++;
		}
		r = 1;
		if (cCdPfParms != null) {
			for (CdPfParms ccCdPfParms : cCdPfParms) {
				totaVo.putParam("L6R40DeptCodeC" + r, ccCdPfParms.getCondition());
				iCdBcm = iCdBcmService.deptCodeFirst(ccCdPfParms.getCondition(), titaVo);
				if (iCdBcm == null) {
					totaVo.putParam("L6R40DeptCodeXC" + r, "");
				} else {
					totaVo.putParam("L6R40DeptCodeXC" + r, iCdBcm.getDeptItem());
				}
				totaVo.putParam("L6R40WorkMonthSC" + r, Math.max(Integer.valueOf(ccCdPfParms.getWorkMonthStart()) - 191100, 0));
				totaVo.putParam("L6R40WorkMonthEC" + r, Math.max(Integer.valueOf(ccCdPfParms.getWorkMonthEnd()) - 191100, 0));
				r++;
			}
		}
		while (r <= 30) {
			totaVo.putParam("L6R40DeptCodeC" + r, "");
			totaVo.putParam("L6R40DeptCodeXC" + r, "");
			totaVo.putParam("L6R40WorkMonthSC" + r, "");
			totaVo.putParam("L6R40WorkMonthEC" + r, "");
			r++;
		}
		r = 1;
		if (dCdPfParms != null) {
			for (CdPfParms ddCdPfParms : dCdPfParms) {
				totaVo.putParam("L6R40DeptCodeD" + r, ddCdPfParms.getCondition());
				iCdBcm = iCdBcmService.deptCodeFirst(ddCdPfParms.getCondition(), titaVo);
				if (iCdBcm == null) {
					totaVo.putParam("L6R40DeptCodeXD" + r, "");
				} else {
					totaVo.putParam("L6R40DeptCodeXD" + r, iCdBcm.getDeptItem());
				}
				totaVo.putParam("L6R40WorkMonthSD" + r, Math.max(Integer.valueOf(ddCdPfParms.getWorkMonthStart()) - 191100, 0));
				totaVo.putParam("L6R40WorkMonthED" + r, Math.max(Integer.valueOf(ddCdPfParms.getWorkMonthEnd()) - 191100, 0));
				r++;
			}
		}
		while (r <= 30) {
			totaVo.putParam("L6R40DeptCodeD" + r, "");
			totaVo.putParam("L6R40DeptCodeXD" + r, "");
			totaVo.putParam("L6R40WorkMonthSD" + r, "");
			totaVo.putParam("L6R40WorkMonthED" + r, "");
			r++;
		}
		r = 1;
		if (eCdPfParms != null) {
			for (CdPfParms eeCdPfParms : eCdPfParms) {
				totaVo.putParam("L6R40DeptCodeE" + r, eeCdPfParms.getCondition());
				iCdBcm = iCdBcmService.deptCodeFirst(eeCdPfParms.getCondition(), titaVo);
				if (iCdBcm == null) {
					totaVo.putParam("L6R40DeptCodeXE" + r, "");
				} else {
					totaVo.putParam("L6R40DeptCodeXE" + r, iCdBcm.getDeptItem());
				}
				totaVo.putParam("L6R40WorkMonthSE" + r, Math.max(Integer.valueOf(eeCdPfParms.getWorkMonthStart()) - 191100, 0));
				totaVo.putParam("L6R40WorkMonthEE" + r, Math.max(Integer.valueOf(eeCdPfParms.getWorkMonthEnd()) - 191100, 0));
				r++;
			}
		}
		while (r <= 30) {
			totaVo.putParam("L6R40DeptCodeE" + r, "");
			totaVo.putParam("L6R40DeptCodeXE" + r, "");
			totaVo.putParam("L6R40WorkMonthSE" + r, "");
			totaVo.putParam("L6R40WorkMonthEE" + r, "");
			r++;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}