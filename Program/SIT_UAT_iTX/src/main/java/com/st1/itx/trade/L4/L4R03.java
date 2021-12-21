package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/*
 * RimCustId=X,10
 * RimCustNo=9,7
 * */

@Service("L4R03")
@Scope("prototype")
public class L4R03 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L4R03.class);

	/* DB服務注入 */
	@Autowired
	public CustMainService custMainService;

	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R03 ");
		this.totaVo.init(titaVo);

		// RimCustNo=9,7
		int iCustNo = parse.stringToInteger(titaVo.getParam("RimCustNo").trim());

		CustMain tCustMain = new CustMain();

		tCustMain = custMainService.custNoFirst(iCustNo, iCustNo);

		if (tCustMain != null) {
			this.totaVo.putParam("L4r03CustName", tCustMain.getCustName());
			this.totaVo.putParam("L4r03CustId", tCustMain.getCustId());
			this.totaVo.putParam("L4r03Birthday", tCustMain.getBirthday());
			this.totaVo.putParam("L4r03Gender", tCustMain.getSex());
			this.totaVo.putParam("L4r03EmpNo", tCustMain.getEmpNo());
		} else {
			throw new LogicException(titaVo, "E0001", " 此戶號不存在");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}