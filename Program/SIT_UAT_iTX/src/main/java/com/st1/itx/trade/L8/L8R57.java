package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R57")
@Scope("prototype")
/**
 * L8通用客戶名稱
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8R57 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public CustMainService iCustMainService;


	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r14 ");
		this.totaVo.init(titaVo);
		String iCustId = titaVo.getParam("RimCustId");
		CustMain iCustMain = new CustMain();
		iCustMain = iCustMainService.custIdFirst(iCustId, titaVo);
		if (iCustMain == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		}else {
			totaVo.putParam("L8R57CustName", iCustMain.getCustName());
		}	
		this.addList(this.totaVo);
		return this.sendList();
	}
}