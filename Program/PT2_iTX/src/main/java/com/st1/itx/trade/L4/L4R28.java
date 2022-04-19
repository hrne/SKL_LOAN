package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBuildingCost;
import com.st1.itx.db.service.CdBuildingCostService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L4R28")
@Scope("prototype")

public class L4R28 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdBuildingCostService cdBuildingCostService;
	
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R28 ");
		this.totaVo.init(titaVo);

//		L4607建築造價參考作業調rim

		String CityCode = titaVo.getParam("RimCityCode");
		int Material = parse.stringToInteger(titaVo.getParam("RimMaterial"));
		int VersionDate = parse.stringToInteger(titaVo.getParam("RimVersionDate")) + 19110000;
		
		Slice<CdBuildingCost> slCdBuildingCost = cdBuildingCostService.findCityCode(CityCode, Material, VersionDate, index, limit, titaVo);

		List<CdBuildingCost> lCdBuildingCost = new ArrayList<CdBuildingCost>();
		
		lCdBuildingCost = slCdBuildingCost == null ? null : slCdBuildingCost.getContent();
		
		
		for (int i = 1; i <= 50; i++) {
			this.totaVo.putParam("L4r28FloorLowerLimit" + i, 0);
			this.totaVo.putParam("L4r28Cost" + i, "");
		}
		
		if(lCdBuildingCost != null) {
			int m = 1;
			for(CdBuildingCost tCdBuildingCost: lCdBuildingCost) {
				this.totaVo.putParam("L4r28FloorLowerLimit" + m, tCdBuildingCost.getFloorLowerLimit());
				this.totaVo.putParam("L4r28Cost" + m, tCdBuildingCost.getCost());
				m++;
		    }
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}