package com.st1.itx.trade.xx;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.config.TXBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.TellerService;
import com.st1.itx.tradeService.SendBuffer;
import com.st1.itx.tradeService.TradeIn;

@Service("XXR22")
@Scope("prototype")
public class XXR22 extends SendBuffer implements TradeIn {
	private static final Logger logger = LoggerFactory.getLogger(XXR22.class);

	@Autowired
	public TXBuffer globalBuffer;

	@Autowired
	public TotaVo totaVo;

	@Autowired
	public TellerService tellerService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		logger.info("Active XXR22");
		this.totaVo.init(titaVo);

//		TellerId tellerId = new TellerId(titaVo.getBrno(), titaVo.getTlrno());
//
//		Teller teller = tellerService.holdById(tellerId);
//
//		if (teller != null) {
//			teller.setLogon("0");
//		}
//
//		try {
//			tellerService.update(teller);
//		} catch (DBException e) {
//			this.totaVo.setTxrsutE();
//			this.totaVo.setErrorMsgId(new String("" + e.getErrorId()));
//			this.totaVo.setErrorMsg(e.getErrorMsg());
//		}

		this.totaVo.putParam("TR_SECNO", "L");
		this.totaVo.putParam("TR_DBUCD", "1");
		this.totaVo.putParam("TR_DRELCD", "1");
		this.totaVo.putParam("TR_DABRNO", "0017");
		this.totaVo.putParam("TR_DFBRNO", "0017");
		this.totaVo.putParam("TR_PREFIX", "SKLC");
		this.totaVo.putParam("TR_OBUCD", "1");
		this.totaVo.putParam("TR_ORELCD", "1");
		this.totaVo.putParam("TR_OFBRNO", "0017");
		this.totaVo.putParam("TR_OPREFIX", "SKLC");

		this.addList(this.totaVo);

		return this.sendList();
	}

}
