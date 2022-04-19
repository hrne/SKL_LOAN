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
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L4211AServiceImpl;
import com.st1.itx.db.service.springjpa.cm.L4211BServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;

@Service("L4211")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */

public class L4211 extends TradeBuffer {

	@Autowired
	public L4211AServiceImpl l4211ARServiceImpl;

	@Autowired
	public L4211BServiceImpl l4211BRServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4211 ");
		this.totaVo.init(titaVo);

		Boolean cheakfg = false;

		List<Map<String, String>> fnAllList = new ArrayList<Map<String, String>>();

		if ("1".equals(titaVo.get("FunctionCode"))) {
			// 產生匯款總傳票明細表

			try {
				fnAllList = l4211ARServiceImpl.findAll(titaVo, 1);
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.info("L4211ServiceImpl.findAll error = " + errors.toString());
			}
		} else {
			// 產生匯款總傳票明細表依戶號排序
			try {
				fnAllList = l4211BRServiceImpl.findAll(titaVo);
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.info("L4211ServiceImpl.findAll error = " + errors.toString());
			}

		}

		if (fnAllList.size() == 0) {
			cheakfg = true;
		}


		if (cheakfg) {
			throw new LogicException("E2003", "查無資料"); // 查無資料
		} else {
			// 執行交易
			MySpring.newTask("L4211Batch", this.txBuffer, titaVo);
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}