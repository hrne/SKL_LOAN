package com.st1.itx.trade.LC;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("LC101")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class LC101 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(LC101.class);

	@Autowired
	public TxTellerService sTxTellerService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC101 ");
		this.totaVo.init(titaVo);

		TxTeller tTxTeller = sTxTellerService.holdById(titaVo.getTlrNo());

		if (tTxTeller != null) {
			tTxTeller.setLogonFg(0);

			try {
				sTxTellerService.update(tTxTeller);
			} catch (DBException e) {
				this.totaVo.setTxrsutE();
				this.totaVo.setErrorMsgId(new String("" + e.getErrorId()));
				this.totaVo.setErrorMsg(e.getErrorMsg());
			}
		}

		this.totaVo.putParam("OBRNO", titaVo.getBrno());
		this.totaVo.putParam("OTLRNO", titaVo.getTlrNo());

		this.addList(this.totaVo);
		return this.sendList();
	}
}