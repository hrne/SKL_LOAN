package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.service.CdCodeService;
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
	public CdCodeService sCdCodeDefService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R18 ");
		this.totaVo.init(titaVo);

		String StockCode = titaVo.getParam("RimStockCode");

		CdCode tCdCode = sCdCodeDefService.getItemFirst(2, "StockCode", StockCode, titaVo);

		if (tCdCode != null) {
			this.totaVo.putParam("L2r18StockCodeX", tCdCode.getItem());
		} else {
			this.totaVo.putParam("L2r18StockCodeX", "");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}