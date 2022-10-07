package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L9741ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;

/**
 * L9741p
 * 
 * @author Linda
 * @version 1.0.0
 */
@Service("L9741p")
@Scope("prototype")
public class L9741p extends TradeBuffer {

	@Autowired
	private L9741ServiceImpl l9741ServiceImpl;

	@Autowired
	L9741Report l9741Report;

	@Autowired
	DateUtil dDateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9741p ");
		this.totaVo.init(titaVo);
		TxBuffer txbuffer = this.getTxBuffer();

		this.info("L9741p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		l9741Report.setParentTranCode(parentTranCode);
		List<Map<String, String>> l9741ListAll = null;
		try {
			l9741ListAll = l9741ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("l9741ServiceImpl.findAll error = " + errors.toString());
		}

		l9741Report.exec(l9741ListAll, titaVo, txbuffer);

		this.addList(this.totaVo);
		return this.sendList();
	}
}