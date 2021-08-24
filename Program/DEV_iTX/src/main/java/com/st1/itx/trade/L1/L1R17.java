package com.st1.itx.trade.L1;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdArea;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L1R17")
@Scope("prototype")
/**
 * 客戶關係調Rim
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L1R17 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdAreaService iCdAreaService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1R17 ");

		this.totaVo.init(titaVo);

		String iCityCode = titaVo.getParam("RimCityCode2");
		String iAreaCode = titaVo.getParam("RimAreaCode2");
		this.info("CityCode ==== " + iCityCode);
		this.info("AreaCode ==== " + iAreaCode);
		Slice<CdArea> iCdArea = null;
		iCdArea = iCdAreaService.areaCodeRange(iCityCode, iCityCode, iAreaCode, iAreaCode, 0, Integer.MAX_VALUE, titaVo);
		if (iCdArea == null) {
			throw new LogicException("E0001", ""); // 查無資料
		}
		CdArea iiCdArea = new CdArea();
		iiCdArea = iCdArea.getContent().get(0);

		totaVo.putParam("L1R17AreaItem", iiCdArea.getAreaItem());

		this.addList(this.totaVo);
		return this.sendList();
	}
}