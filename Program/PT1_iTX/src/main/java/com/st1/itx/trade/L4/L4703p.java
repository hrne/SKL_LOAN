package com.st1.itx.trade.L4;

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
import com.st1.itx.db.service.springjpa.cm.L9703ServiceImpl;
import com.st1.itx.trade.L9.L9705Form;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;

/**
 * L4703p
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Service("L4703p")
@Scope("prototype")
public class L4703p extends TradeBuffer {

	@Autowired
	private L9703ServiceImpl l9703ServiceImpl;

	@Autowired
	L9705Form l9705Form;
	
	@Autowired
	DateUtil dDateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4703p ");
		this.totaVo.init(titaVo);
		TxBuffer txbuffer = this.getTxBuffer();

		this.info("L4703p titaVo.getTxcd() = " + titaVo.getTxcd());

		List<Map<String, String>> l4703List = null;
		try {
			l4703List = l9703ServiceImpl.queryForNotice(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("l9703ServiceImpl.findAll error = " + errors.toString());
		}


		//by eric 2021.12.10
		l9705Form.exec(l4703List, titaVo, txbuffer);
		
		this.addList(this.totaVo);
		return this.sendList();
	}

}