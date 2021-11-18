package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L6R46")
@Scope("prototype")

public class L6R46 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public CdAcCodeService sCdAcCodeService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R46 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRepayCode = this.parse.stringToInteger(titaVo.getParam("RimRepayCode"));

		CdAcCode tCdAcCode = new CdAcCode();
		if(iRepayCode == 5) {
			tCdAcCode = sCdAcCodeService.acCodeAcctFirst("OPL", titaVo);
		} else if(iRepayCode == 6) {
			tCdAcCode = sCdAcCodeService.acCodeAcctFirst("C02", titaVo);
		} else if(iRepayCode == 11) {
			tCdAcCode = sCdAcCodeService.acCodeAcctFirst("P03", titaVo);
		}
		
		this.totaVo.putParam("L6R46AcNoCode", tCdAcCode.getAcNoCode());
		this.totaVo.putParam("L6R46AcSubCode", tCdAcCode.getAcSubCode());
		this.totaVo.putParam("L6R46AcDtlCode", tCdAcCode.getAcDtlCode());
		
		this.addList(this.totaVo);
		return this.sendList();
	}


}