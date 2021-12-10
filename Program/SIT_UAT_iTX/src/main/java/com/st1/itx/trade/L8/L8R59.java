package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R59")
@Scope("prototype")
/**
 * L8通用三碼找法院或地區別中文
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8R59 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public CdCodeService iCdCodeService;
	@Autowired
	public CdCityService iCdCityService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r14 ");
		this.totaVo.init(titaVo);
		
		String iRimCourtCode = titaVo.getParam("RimCourtCode");
		CdCodeId iCdCodeId = new CdCodeId();
		iCdCodeId.setDefCode("CourtCode");
		iCdCodeId.setCode(iRimCourtCode);
		CdCode iCdCode = iCdCodeService.findById(iCdCodeId, titaVo);
		if (iCdCode == null) {
			CdCity iCdCity = iCdCityService.findById(iRimCourtCode, titaVo);
			if (iCdCity !=null) {
				totaVo.putParam("L8R59CourtCodeX", iCdCity.getCityItem());
			}else {
				throw new LogicException(titaVo, "E0001", "");
			}
		}else {
			totaVo.putParam("L8R59CourtCodeX", iCdCode.getItem());
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}