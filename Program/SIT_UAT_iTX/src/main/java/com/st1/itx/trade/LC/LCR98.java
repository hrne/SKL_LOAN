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

@Service("LCR98")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class LCR98 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(LCR98.class);

	@Autowired
	public LockControl LockControl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LCR98 ");
		this.totaVo.init(titaVo);

		long unlockno = 0;
		String iUnLockNo = titaVo.getParam("LockNo").trim();
		try {
			unlockno = Long.valueOf(iUnLockNo);
		} catch (Throwable e) {
			throw new LogicException(titaVo, "E0006", "參數LockNo:" + iUnLockNo);
		}

		this.info("LCR98 unlockno = " + unlockno);

		if (unlockno > 0) {
			LockControl.setTitaVo(titaVo);
			LockControl.ToUnLock(unlockno, 0, false, false);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}