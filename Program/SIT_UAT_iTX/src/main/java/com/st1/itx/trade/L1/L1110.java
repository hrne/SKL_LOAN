package com.st1.itx.trade.L1;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

@Service("L1110")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L1110 extends TradeBuffer {
	@Autowired
	public CustMainService custMainService;

	@Autowired
	public DataLog iDataLog;

	@Autowired
	public SendRsp sendRsp;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1110 ");
		this.totaVo.init(titaVo);

		String custId = titaVo.getParam("CustId");

		CustMain custMain = custMainService.custIdFirst(custId, titaVo);
		if (custMain == null) {
			throw new LogicException(titaVo, "E0003", "查無身份證號/統一編號 : " + custId);
		}

		if (custMain.getDataStatus() != 0) {
			throw new LogicException(titaVo, "E0015", "請先完成客戶資料建檔");
		}

		if (!titaVo.getHsupCode().equals("1")) {
			sendRsp.addvReason(this.txBuffer, titaVo, "0101", "");
		}

		custMain = custMainService.holdById(custMain, titaVo);
		CustMain custMain2 = (CustMain) iDataLog.clone(custMain);
		custMain.setAllowInquire(titaVo.getParam("AllowInquireAft").toString());
		try {
			custMain = custMainService.update2(custMain, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0007", "客戶主檔");
		}

		// 紀錄變更前變更後
		iDataLog.setEnv(titaVo, custMain2, custMain);
		iDataLog.exec();

		this.addList(this.totaVo);
		return this.sendList();
	}
}