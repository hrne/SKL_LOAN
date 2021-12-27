package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L6870")
@Scope("prototype")
/**
 * 發動每日夜間批次
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class L6870 extends TradeBuffer {
	@Autowired
	L6880 sL6880;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6870 ");
		this.totaVo.init(titaVo);

		// tita的批次日期
		String iEntday = titaVo.get("iEntday").trim();

		this.info("L6870 iEntday = " + iEntday);

		// 更新批次日期檔
		sL6880.proc(titaVo, "BATCH", iEntday);

		this.info("L6870 每日夜間批次 eodFlow");

		// 每日夜間批次程式，參考文件.\itxConfig\spring\batch\eodFlow.xml
		// EOD : End Of Day
		titaVo.setBatchJobId("eodFlow");

		this.info("L6870 end.");

		this.addList(this.totaVo);
		return this.sendList();
	}
}