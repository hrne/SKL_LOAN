package com.st1.itx.trade.L2;

import java.util.ArrayList;
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
import com.st1.itx.util.date.DateUtil;
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

	/* DB服務注入 */
	@Autowired
	public CdBaseRateService cdBaseRateService;

	@Autowired
	public Parse parse;
	@Autowired
	public DateUtil dateUtil;

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
			throw new LogicException(titaVo, "E0009", ""); // 交易代號不可為空白
		}
		// 撥款檢查是否為假日
		if ("L3100".equals(iTxCode)) {
			dateUtil.init();
			dateUtil.setDate_2(iEffectDate);
			if (dateUtil.isHoliDay()) {
				throw new LogicException(titaVo, "E0015", "撥款日期為假日"); // E0015 檢查錯誤
			}
		}
		// 查詢指標利率檔
		if (iBaseRateCode.equals("99")) {
			this.totaVo.putParam("L2r06Rate", 0);
			this.totaVo.putParam("L2r06Remark", "");
		} else {

			Slice<CdBaseRate> lCdBaseRate = cdBaseRateService.baseRateCodeEq2(iCurrencyCode, iBaseRateCode, this.index,
					this.limit, titaVo);

			if (lCdBaseRate == null) {
				throw new LogicException(titaVo, "E0001", " 指標利率檔"); // 查無資料
			}

			CdBaseRate tCdBaseRate = cdBaseRateService.baseRateCodeDescFirst(iCurrencyCode, iBaseRateCode, 19110101,
//					CdBaseRate tCdBaseRate = cdBaseRateService.baseRateCodeDescFirst(iCurrencyCode, iBaseRateCode, 10101,
					iEffectDate + 19110000, titaVo);
			if (tCdBaseRate != null) {
				this.totaVo.putParam("L2r06Rate", tCdBaseRate.getBaseRate());
				this.totaVo.putParam("L2r06Remark", tCdBaseRate.getRemark());
			} else {
				throw new LogicException(titaVo, "E6019", " 指標利率檔"); // 生效日期未生效
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}