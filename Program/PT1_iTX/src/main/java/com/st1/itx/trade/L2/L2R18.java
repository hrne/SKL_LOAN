package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdStock;
import com.st1.itx.db.service.CdStockService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L2R18")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R18 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	CdStockService sCdStockService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R18 ");
		this.totaVo.init(titaVo);

		String StockCode = titaVo.getParam("RimStockCode");

		CdStock tCdStock = sCdStockService.findById(StockCode, titaVo);

		if (tCdStock != null) {
			this.totaVo.putParam("L2r18StockCodeX", tCdStock.getStockItem());
		} else {
			this.totaVo.putParam("L2r18StockCodeX", "");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}