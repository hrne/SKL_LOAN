package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBuildingCost;
import com.st1.itx.db.domain.CdBuildingCostId;
import com.st1.itx.db.service.CdBuildingCostService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;



@Service("L4607")
@Scope("prototype")

public class L4607 extends TradeBuffer {

	@Autowired
	public Parse parse;

	@Autowired
	public CdBuildingCostService cdBuildingCostService;


	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4607 ");
		this.totaVo.init(titaVo);

		this.index = titaVo.getReturnIndex();
		this.limit = 100;
		
		String CityCode = titaVo.getParam("CityCode");
		
		for(int i = 1 ; i <= 50; i++) {
			CdBuildingCost tCdBuildingCost = new CdBuildingCost();
			CdBuildingCostId tCdBuildingCostId = new CdBuildingCostId();
			BigDecimal Cost = parse.stringToBigDecimal(titaVo.getParam("Cost" + i));
			
			if(Cost.compareTo(new BigDecimal("0"))  == 0) {
				break;
			} else {
				tCdBuildingCostId.setCityCode(CityCode);
				tCdBuildingCostId.setFloorLowerLimit(i);
				
				tCdBuildingCost = cdBuildingCostService.findById(tCdBuildingCostId, titaVo);
				
				if(tCdBuildingCost == null) { // 新增
					
					tCdBuildingCost = new CdBuildingCost();
					tCdBuildingCost.setCdBuildingCostId(tCdBuildingCostId);
					tCdBuildingCost.setCityCode(CityCode);
					tCdBuildingCost.setFloorLowerLimit(i);
					tCdBuildingCost.setCost(Cost);
					
					try {
						cdBuildingCostService.insert(tCdBuildingCost, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0005", "建築造價參考檔" + e.getErrorMsg());
					}
				} else { // 更新
					tCdBuildingCost.setCityCode(CityCode);
					tCdBuildingCost.setFloorLowerLimit(i);
					tCdBuildingCost.setCost(Cost);
					
					try {
						cdBuildingCostService.update(tCdBuildingCost, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "建築造價參考檔" + e.getErrorMsg());
					}
				}
			}
		} // for
		this.addList(this.totaVo);
		return this.sendList();
	}
}