package com.st1.itx.trade.LC;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.service.TxTellerService;

@Service("LCR07")
@Scope("prototype")
/**
 * 主管授權
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class LCR07 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(LCR07.class);

	@Autowired
	public TxTellerService sTxTellerService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LCR07 ");
		this.totaVo.init(titaVo);

		TxTeller tTxTeller = sTxTellerService.findById(titaVo.getParam("SUPID"));
		if (tTxTeller != null) {
			if (tTxTeller.getLevelFg() != 1) {
				throw new LogicException("EC004", "員編: " + titaVo.getParam("SUPID") + " 非主管");
			}
		} else {
			throw new LogicException("EC001", "員編:" + titaVo.getParam("SUPID"));
		}

		this.totaVo.putParam("BrNo", tTxTeller.getBrNo());
		this.totaVo.putParam("TlrNo", tTxTeller.getTlrNo());
		this.totaVo.putParam("AdNo", tTxTeller.getAdFg() == 1 ? titaVo.getTlrNo() : "");
		this.totaVo.putParam("Name", tTxTeller.getTlrItem().trim());
		this.totaVo.putParam("PsWd", titaVo.getParam("PW"));

		this.addList(this.totaVo);
		return this.sendList();
	}
}