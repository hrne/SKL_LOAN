package com.st1.itx.trade.LC;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;

import com.st1.itx.util.common.LockControl;
import com.st1.itx.util.common.SendRsp;

@Service("LC110")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class LC110 extends TradeBuffer {

	@Autowired
	public LockControl LockControl;

	@Autowired
	public SendRsp sendRsp;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC110 ");
		this.totaVo.init(titaVo);

		if (!titaVo.getHsupCode().equals("1")) {
			sendRsp.addvReason(this.txBuffer, titaVo, "0004", "解除戶號鎖定");
		}
		
		LockControl.setTitaVo(titaVo);

		long iLockNo = Long.valueOf(titaVo.get("iLockNo").toString());
		int iCustNo = Integer.valueOf(titaVo.get("iCustNo").toString());

		LockControl.ToUnLock(iLockNo, iCustNo, true, true);

		this.addList(this.totaVo);
		return this.sendList();
	}
}