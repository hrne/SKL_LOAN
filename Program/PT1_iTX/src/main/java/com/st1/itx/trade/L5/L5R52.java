package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.JcicZ048;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ048Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L5R52")
@Scope("prototype")
/**
 * L5R52 檢核統編是否存在JcicZ048
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L5R52 extends TradeBuffer {

	@Autowired
	JcicZ048Service jcicZ048Service;
	@Autowired
	CustMainService custMainService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5R52 ");
		this.totaVo.init(titaVo);

		int iFunCd = Integer.parseInt(titaVo.getParam("RimFunCd"));
		String iCustId = titaVo.getParam("RimCustId");

		String custName = "";
		if (iFunCd == 1) {
			Slice<JcicZ048> slJcicZ048 = jcicZ048Service.custIdEq(iCustId, 0, 1, titaVo);
			if (slJcicZ048 == null) {
				throw new LogicException("E2003", "統編不存在債務人基本資料檔");// 查無資料
			}
			CustMain tCustMain = custMainService.custIdFirst(iCustId, titaVo);
			if (tCustMain != null) {
				custName = tCustMain.getCustName();
			}
		} else {

		}

		this.totaVo.putParam("L5r52CustName", custName);
		this.addList(this.totaVo);
		return this.sendList();

	}

}