package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L4721ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;

@Service("L4721")
@Scope("prototype")
/**
 * 
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class L4721 extends TradeBuffer {

	@Autowired
	public L4721ServiceImpl l4721ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4721 ");
		this.totaVo.init(titaVo);

//		還本繳息對帳單.pdf
		// 執行交易
		MySpring.newTask("L4721Batch", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}