package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBonusCo;
import com.st1.itx.db.service.CdBonusCoService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L6R36")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L6R36 extends TradeBuffer {

	@Autowired
	public CdBonusCoService iCdBonusCoService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		this.info("active L6R36 ");
		this.totaVo.init(titaVo);

		int iWorkMonth = Integer.valueOf(titaVo.getParam("RimWorkMonth")) + 191100;

		Slice<CdBonusCo> iCdBonusCo = null;
		iCdBonusCo = iCdBonusCoService.findYearMonth(iWorkMonth, iWorkMonth, 0, Integer.MAX_VALUE, titaVo);

		if (iCdBonusCo == null) {
			throw new LogicException(titaVo, "E0001", "該工作月尚無資料");
		}

		int iPieceCode = 0;
		int iCondition = 0;

		// 預設20筆
		for (int i = 0; i < 20; i++) {
			totaVo.putParam("L6R36PieceCode" + String.valueOf(i), "");
		}

		for (CdBonusCo rCdBonusCo : iCdBonusCo) {
			if (rCdBonusCo.getConditionCode() == 1) {
				totaVo.putParam("L6R36PieceCode" + String.valueOf(iPieceCode), rCdBonusCo.getCondition());
				if (iPieceCode == 0) { // 第一筆的標準金額
					totaVo.putParam("L6R36ConditionAmt", rCdBonusCo.getConditionAmt());
				}
				iPieceCode++;
			} else if (rCdBonusCo.getConditionCode() == 2) {
				totaVo.putParam("L6R36Bonus" + String.valueOf(iCondition), rCdBonusCo.getBonus());
				totaVo.putParam("L6R36ClassPassBonus" + String.valueOf(iCondition), rCdBonusCo.getClassPassBonus());
				iCondition++;
			} else {
				totaVo.putParam("L6R36PrizeAmt", rCdBonusCo.getBonus());
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}