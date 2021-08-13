package com.st1.itx.trade.LC;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;

@Service("LC700")
@Scope("prototype")
/**
 * 批次換日,發動日終維護
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class LC700 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(LC700.class);

	@Autowired
	LC800 sLC800;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC700 ");
		this.totaVo.init(titaVo);

		// tita的批次日期
		String iEntday = titaVo.get("iEntday").trim();

		this.info("LC700 iEntday = " + iEntday);

		// 更新日期檔
		sLC800.proc(titaVo, "BATCH", iEntday);

		this.info("LC700 每日執行 endOfDayFlow");

		titaVo.setBatchJobId("eodFlow");

		this.info("LC700 exit.");

		this.addList(this.totaVo);
		return this.sendList();
	}

}