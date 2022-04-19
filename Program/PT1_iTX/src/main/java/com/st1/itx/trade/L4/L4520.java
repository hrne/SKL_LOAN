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
import com.st1.itx.db.service.springjpa.cm.L4520RServiceImpl;
import com.st1.itx.db.service.springjpa.cm.L4520ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;

@Service("L4520")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4520 extends TradeBuffer {

	@Autowired
	public L4520ServiceImpl l4520ServiceImpl;

	@Autowired
	public L4520RServiceImpl l4520RServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4520 ");
		this.totaVo.init(titaVo);

		Boolean printfg = false;
		List<Map<String, String>> fnAllList = new ArrayList<>();

		try {
			fnAllList = l4520ServiceImpl.findSF(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("l4520ServiceImpl.fs error = " + errors.toString());
		}

		if (fnAllList != null && fnAllList.size() != 0) {
			// 產生更新成功失敗明細表
			printfg = true;
		}

		try {
			fnAllList = l4520ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("l4520ServiceImpl.findAll error = " + errors.toString());
		}

		if (fnAllList != null && fnAllList.size() != 0) {
			// 產生員工扣薪總傳票明細表
			printfg = true;
		}

		try {
			fnAllList = l4520RServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("l4520RServiceImpl.findAll error = " + errors.toString());
		}

		if (fnAllList != null && fnAllList.size() != 0) {
			// 產生火險費沖銷明細表(員工扣薪)
			printfg = true;
		}

		if (!printfg) {
			throw new LogicException("E0001", "查無資料");
		} else {
			// 執行交易
			MySpring.newTask("L4520Batch", this.txBuffer, titaVo);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}