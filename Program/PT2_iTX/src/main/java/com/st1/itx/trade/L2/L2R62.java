package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.TxTranCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.CustRmkCom;
import com.st1.itx.util.parse.Parse;

@Service("L2R62")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L2R62 extends TradeBuffer {

	@Autowired
	public TxTranCodeService txTranCodeService;

	@Autowired
	private CustRmkCom custRmkCom;

	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R62 = " + titaVo.getTxcd() + "/" + titaVo.getParam("RmkCustNo"));
		this.totaVo.init(titaVo);

		int custNo = parse.stringToInteger(titaVo.getParam("RmkCustNo"));

		TxTranCode txTranCode = txTranCodeService.findById(titaVo.getTxcd(), titaVo);

		if (txTranCode != null && txTranCode.getCustRmkFg() == 1) {
			return custRmkCom.getCustRmk(titaVo, custNo);
		} else {
			this.addList(this.totaVo);
			return this.sendList();
		}

	}
}