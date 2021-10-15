package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdArea;
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L5R43") // 尋找各類代碼檔資料與縣市與鄉鎮區對照檔
@Scope("prototype")
/**
 *
 *
 * @author ChihCheng
 * @version 1.0.0
 */
public class L5R43 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdCodeService sCdCodeService;
	@Autowired
	public CdAreaService sCdAreaService;
	@Autowired
	public CdCityService sCdCityService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5R43 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iFuncCode = titaVo.getParam("FuncCode");
		String iCourtCode = titaVo.getParam("CourtCode");
		String iCity="";
		String iArea="";
		this.totaVo.putParam("L5R43CourtItem", "");
			
			
		CdArea tCdArea = sCdAreaService.Zip3First(iCourtCode, titaVo);
			
		if(tCdArea!=null) {
					
			CdCity tCdCity = sCdCityService.findById(tCdArea.getCityCode(), titaVo);
			if(tCdCity!=null) {
				iCity = tCdCity.getCityItem();
				iArea = tCdArea.getAreaItem();
				this.totaVo.putParam("L5R43CourtItem", iCity+iArea+"調解委員會");
			}
					
				
		} else {
		
			CdCode tCdCode2 = sCdCodeService.findById(new CdCodeId("CodeType", "CourtCode"), titaVo);
			
			if (tCdCode2 != null) {
				CdCodeId tCdCodeId = new CdCodeId("CourtCode", iCourtCode);
				CdCode tCdCode = sCdCodeService.findById(tCdCodeId, titaVo);
				
					if(tCdCode != null) {
					this.totaVo.putParam("L5R43CourtItem", tCdCode.getItem());
					}
					
			}
		}
		
			

		this.addList(this.totaVo);
		return this.sendList();
	}

	

}