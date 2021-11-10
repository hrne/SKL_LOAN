package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BatxRateChange;
import com.st1.itx.db.domain.BatxRateChangeId;
import com.st1.itx.db.service.BatxRateChangeService;
import com.st1.itx.db.service.CdBaseRateService;
import com.st1.itx.db.service.LoanRateChangeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4325")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4325 extends TradeBuffer {

	@Autowired
	public Parse parse;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public BatxRateChangeService batxRateChangeService;

	@Autowired
	public LoanRateChangeService loanRateChangeService;

	@Autowired
	public CdBaseRateService cdBaseRateService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4325 ");
		this.totaVo.init(titaVo);

//		調Rim 100筆送上來 有值者才送出 update交易
		int iAdjDate = parse.stringToInteger(titaVo.getParam("RimAdjDate")) + 19110000;

		for (int i = 1; i <= 100; i++) {
			int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo" + i));
			int iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo" + i));
			int iBormNo = parse.stringToInteger(titaVo.getParam("BormNo" + i));
			BigDecimal iAdjustedRate = parse.stringToBigDecimal(titaVo.getParam("AdjustedRate" + i));
			String keyInFg = titaVo.getParam("KeyInColA" + i);
			if (iCustNo != 0) {
				if (iAdjustedRate.compareTo(BigDecimal.ZERO) > 0 || "Y".equals(keyInFg)) {

					BatxRateChange tBatxRateChange = new BatxRateChange();
					BatxRateChangeId tBatxRateChangeId = new BatxRateChangeId();

					tBatxRateChangeId.setAdjDate(iAdjDate);
					tBatxRateChangeId.setCustNo(iCustNo);
					tBatxRateChangeId.setFacmNo(iFacmNo);
					tBatxRateChangeId.setBormNo(iBormNo);

					tBatxRateChange = batxRateChangeService.holdById(tBatxRateChangeId);

					tBatxRateChange.setAdjustedRate(iAdjustedRate);
					tBatxRateChange.setRateKeyInCode(1); // 已調整

					try {
						batxRateChangeService.update(tBatxRateChange);
					} catch (DBException e) {
						throw new LogicException("E0007", "BatxRateChange update is error : " + e.getErrorMsg());
					}
				}
			} else {
				break;
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}