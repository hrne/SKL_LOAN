package com.st1.itx.trade.L5;

import java.util.ArrayList;
/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * FinCode=9,3<br>
 */

@Service("L5976")
@Scope("prototype")
/**
 * 
 * 
 * @author ChihCheng
 * @version 1.0.0
 */
public class L5976 extends TradeBuffer {
	/* DB服務注入 */

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("Run L5976");
		this.info("active L5976 ");
		this.totaVo.init(titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

}