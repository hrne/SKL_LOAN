package com.st1.itx.trade.L3;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.trade.L3.L3R13;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*
 * Tita
 * RimFixAdjDd=9,2
 * RimRateAdjFreq=9,2
 * RimDrawdownDate=9,7
 */
/**
 * L3R13 當商品參數利率調整固定日期>0時,計算利率調整日期
 * 
 * @author iza
 * @version 1.0.0
 *
 */
@Service("L3R13")
@Scope("prototype")
public class L3R13 extends TradeBuffer {

	@Autowired
	Parse parse;
	@Autowired
	DateUtil dDateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3R13 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFixAdjDd = this.parse.stringToInteger(titaVo.getParam("RimFixAdjDd"));
		int iRateAdjFreq = this.parse.stringToInteger(titaVo.getParam("RimRateAdjFreq"));
		int iDrawdownDate = this.parse.stringToInteger(titaVo.getParam("RimDrawdownDate"));

		// work area
		int wkDate = 0;
		int wkYear = 0;
		int wkMonth = 0;
		int wkAdjRateDate = 0;

		if (iFixAdjDd == 0 || iFixAdjDd > 31) {
			throw new LogicException(titaVo, "E0019", " 利率調整固定日期 = " + iFixAdjDd); // 輸入資料錯誤
		}

		if (iRateAdjFreq == 0 || iRateAdjFreq > 12) {
			throw new LogicException(titaVo, "E0019", " 利率調整週期 = " + iRateAdjFreq); // 輸入資料錯誤
		}

		if ((12 % iRateAdjFreq) > 0) {
			throw new LogicException(titaVo, "E0019", " 利率調整週期 = " + iRateAdjFreq); // 輸入資料錯誤
		}

		if (iDrawdownDate == 0) {
			throw new LogicException(titaVo, "E0019", " 撥款日期 =  " + iDrawdownDate); // 輸入資料錯誤
		}

		wkYear = iDrawdownDate / 10000;
		wkMonth = (iDrawdownDate % 10000) / 100;
		wkMonth = ((wkMonth + iRateAdjFreq - 1) / iRateAdjFreq) * iRateAdjFreq + 1;
		if (wkMonth > 12) {
			wkMonth = 1;
			wkYear++;
		}
		wkDate = wkYear * 10000 + wkMonth * 100 + 1;
		dDateUtil.init();
		dDateUtil.setDate_1(wkDate);
		if (iFixAdjDd > dDateUtil.getMonLimit()) {
			throw new LogicException(titaVo, "E0019", " 利率調整固定日期 = " + iFixAdjDd); // 輸入資料錯誤
		}
		wkAdjRateDate = wkYear * 10000 + wkMonth * 100 + iFixAdjDd;
		this.info("   wkAdjRateDate = " + wkAdjRateDate);

		this.totaVo.putParam("L3r13AdjRateDate", wkAdjRateDate);
		this.addList(this.totaVo);
		return this.sendList();
	}
}