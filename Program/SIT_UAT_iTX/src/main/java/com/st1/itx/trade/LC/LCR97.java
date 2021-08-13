package com.st1.itx.trade.LC;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;

import com.st1.itx.util.common.LockControl;

@Service("LCR97")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class LCR97 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(LCR97.class);

	@Autowired
	public LockControl LockControl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LCR97 ");
		this.totaVo.init(titaVo);

//		int cnt = titaVo.getBodyCount();

//		if (cnt < 1) {
//			throw new LogicException(titaVo, "E0006", "參數不正確");
//		}

//		int iCustNo = Integer.valueOf(titaVo.getParam("BodyFld1").trim());
		int lockCustNo = Integer.valueOf(titaVo.getParam("LockCustNo").trim());
		String iTranNo = titaVo.getTxcd();
//		long unlockno = 0;
//		if (cnt == 2) {
//			String iUnLockNo = titaVo.get("BodyFld2").trim();
//			unlockno = Long.valueOf(iUnLockNo);
//		}

		long unLockNo = Long.valueOf(titaVo.getParam("LockNo").trim());

		this.info("active XXR97 iTranNo = " + iTranNo);
		this.info("active XXR97 lockCustNo = " + lockCustNo);
		this.info("active XXR97 unLockNo = " + unLockNo);

		LockControl.setTitaVo(titaVo);

//		if (unLockNo > 0) {
//			LockControl.ToUnLock(unLockNo, 0, false, false);
//		}

		long lockno = LockControl.ToLock(lockCustNo, iTranNo, unLockNo);

		this.totaVo.putParam("LockNo", lockno);

		this.addList(this.totaVo);
		return this.sendList();
	}
}