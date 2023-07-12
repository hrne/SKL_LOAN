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
import com.st1.itx.db.service.springjpa.cm.L9705ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;

/**
 * L9705p
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Service("L9705p")
@Scope("prototype")
public class L9705p extends TradeBuffer {

	@Autowired
	private L9705ServiceImpl l9705ServiceImpl;

	@Autowired
	L9705Report l9705Report;

	@Autowired
	L9705Form l9705Form;

	@Autowired
	DateUtil dDateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9705p ");
		this.totaVo.init(titaVo);
		TxBuffer txbuffer = this.getTxBuffer();

		this.info("L9705p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		l9705Report.setParentTranCode(parentTranCode);
		List<Map<String, String>> l9705List = null;
		try {
			l9705List = l9705ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("l9705ServiceImpl.findAll error = " + errors.toString());
		}
		// 繳息通知單
		if (l9705List != null) {
			l9705Report.exec(l9705List, titaVo, txbuffer);
		}
        // 套印存入憑條
		l9705Form.exec(l9705List, titaVo, txbuffer);

		this.addList(this.totaVo);
		return this.sendList();
	}

}