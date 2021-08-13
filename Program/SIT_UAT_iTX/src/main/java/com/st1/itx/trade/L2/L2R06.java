package com.st1.itx.trade.L2;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBaseRate;
import com.st1.itx.db.service.CdBaseRateService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimTxCode=X,5
 * RimCurrencyCode=X,3
 * RimBaseRateCode=X,2
 * RimEffectDate=9,7
 * RimEffectTime=9,4
 */

/**
 * L2R06 尋找指標利率檔資料
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L2R06")
@Scope("prototype")
public class L2R06 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2R06.class);

	/* DB服務注入 */
	@Autowired
	public CdBaseRateService cdBaseRateService;

	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R06 ");
		this.totaVo.init(titaVo);

		
		// 取得輸入資料
		String iTxCode = titaVo.getParam("RimTxCode").trim();
		String iCurrencyCode = titaVo.getParam("RimCurrencyCode").trim();
		String iBaseRateCode = titaVo.getParam("RimBaseRateCode").trim();
		int iEffectDate = this.parse.stringToInteger(titaVo.getParam("RimEffectDate"));
		// int iEffectTime = parae.stringToInteger(titaVo.getParam("EffectTime"));

		// 檢查輸入資料
		if (iTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L2R06"); // 交易代號不可為空白
		}

		// 查詢指標利率檔
		if (iBaseRateCode.equals("99")) {
			this.totaVo.putParam("OBaseRate", 0);
			this.totaVo.putParam("ORemark", "");
		} else {
			
			Slice<CdBaseRate> lCdBaseRate = cdBaseRateService.baseRateCodeEq2(iCurrencyCode, iBaseRateCode,this.index, this.limit, titaVo);
			
			if(lCdBaseRate == null) {
				throw new LogicException(titaVo, "E0001", "L2R06 指標利率檔"); // 查無資料
			}
			
			CdBaseRate tCdBaseRate = cdBaseRateService.baseRateCodeDescFirst(iCurrencyCode, iBaseRateCode, 19110101,
//					CdBaseRate tCdBaseRate = cdBaseRateService.baseRateCodeDescFirst(iCurrencyCode, iBaseRateCode, 10101,
					iEffectDate + 19110000, titaVo);
			if (tCdBaseRate != null) {
				this.totaVo.putParam("OBaseRate", tCdBaseRate.getBaseRate());
				this.totaVo.putParam("ORemark", tCdBaseRate.getRemark());
			} else {
				throw new LogicException(titaVo, "E6019", "L2R06 指標利率檔"); // 生效日期未生效
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}