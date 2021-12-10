package com.st1.itx.trade.L4;

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
import com.st1.itx.util.MySpring;

@Service("L4320")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4320 extends TradeBuffer {
	@Autowired
	public CustMainService custMainService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4320 ");
		this.totaVo.init(titaVo);
		String iGroupId = titaVo.getParam("GroupId").trim(); // 團體戶統編
		if ("".equals(iGroupId)) {
			titaVo.putParam("GroupUKey", "");
		} else {
			CustMain tCustMain = custMainService.custIdFirst(iGroupId, titaVo);
			if (tCustMain == null) {
				throw new LogicException("E0001", " 客戶資料主檔無該團體戶統編"); // 查詢無資料
			}
			titaVo.putParam("GroupUKey", tCustMain.getCustUKey());
		}

		// 執行交易
		MySpring.newTask("L4320Batch", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}