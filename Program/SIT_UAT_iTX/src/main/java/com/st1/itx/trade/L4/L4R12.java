package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBaseRate;
import com.st1.itx.db.service.CdBaseRateService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4R12")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4R12 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L4R12.class);

	@Autowired
	public Parse parse;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public CdBaseRateService cdBaseRateService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R12 ");
		this.totaVo.init(titaVo);

		CdBaseRate t01CdBaseRate = new CdBaseRate();

		String rateCode = titaVo.getParam("RimBaseRateCode");
		BigDecimal rate = BigDecimal.ZERO;
		int date = 0;

		t01CdBaseRate = cdBaseRateService.baseRateCodeDescFirst("TWD", rateCode, 0, 99991231);
		if (t01CdBaseRate != null) {
			rate = t01CdBaseRate.getBaseRate();
			date = t01CdBaseRate.getEffectDate();
			this.info("t01CdBaseRate : " + t01CdBaseRate.getBaseRate() + "EffectFlag = " + t01CdBaseRate.getEffectFlag() + "...");
		}

		this.totaVo.putParam("L4r12Rate", rate);
		this.totaVo.putParam("L4r12EffectDate", date);

		this.addList(this.totaVo);
		return this.sendList();
	}
}