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
	// private static final Logger logger = LoggerFactory.getLogger(L1R17.class);

	/* DB服務注入 */
	@Autowired
	public CdAreaService iCdAreaService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1R17 ");
		
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 400; // 124 * 400 = 52000
		
		String iCityCode = titaVo.getParam("RimCityCode2");
		String iAreaCode = titaVo.getParam("RimAreaCode2");
		this.info("CityCode ==== "+iCityCode);
		this.info("AreaCode ==== "+iAreaCode);
		Slice<CdArea> iCdArea = null;
		iCdArea = iCdAreaService.areaCodeRange(iCityCode, iCityCode, iAreaCode, iAreaCode, this.index, this.limit, titaVo);
		if (iCdArea == null) {
			throw new LogicException("E0001", ""); // 查無資料
		}
		CdArea iiCdArea = new CdArea();
		iiCdArea = iCdArea.getContent().get(0);
		
		totaVo.putParam("L1R17AreaItem",iiCdArea.getAreaItem());
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}