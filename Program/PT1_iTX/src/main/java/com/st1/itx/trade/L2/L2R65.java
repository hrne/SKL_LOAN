package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * L2R65 查核准號碼是否已撥款
 * 
 * @author Wei
 * @version 1.0.0
 */
@Service("L2R65")
@Scope("prototype")
public class L2R65 extends TradeBuffer {

	@Autowired
	private FacMainService sFacMainService;
	
	@Autowired
	private Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R65 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iApplNo = this.parse.stringToInteger(titaVo.getParam("RimApplNo"));

		FacMain facMain =sFacMainService.facmApplNoFirst(iApplNo, titaVo);
		
		String drawdownStatus = "N";
		
		if (facMain != null && facMain.getLastBormNo() > 0) {
			drawdownStatus = "Y";
		}
		
		this.totaVo.putParam("L2r65DrawdownStatus", drawdownStatus); // Y:已撥款 N:未撥款

		this.addList(this.totaVo);
		return this.sendList();
	}
}