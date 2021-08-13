package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/*
 * Tita
 * Principal=9,14
 * Rate=9,2.4
 * LoanTerm=9,3
 * iDuePayAmt=9,14
 * PayIntLimit=9,14
 */
/**
 * L3902 以房養老貸款試算
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3902")
@Scope("prototype")
public class L3902 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L3902.class);

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3902 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		BigDecimal iPrincipal = this.parse.stringToBigDecimal(titaVo.getParam("Principal"));
		BigDecimal iRate = this.parse.stringToBigDecimal(titaVo.getParam("Rate"));
		int iLoanTerm = this.parse.stringToInteger(titaVo.getParam("LoanTerm"));
		BigDecimal iDuePayAmt = this.parse.stringToBigDecimal(titaVo.getParam("DuePayAmt"));
		BigDecimal iPayIntLimit = this.parse.stringToBigDecimal(titaVo.getParam("PayIntLimit"));

		// work area
		BigDecimal wkInterest = BigDecimal.ZERO;
		BigDecimal wkUnpaidInt = BigDecimal.ZERO;
		BigDecimal wkSumUnpaidInt = BigDecimal.ZERO;
		BigDecimal wkDuePayAmt = iDuePayAmt;
		BigDecimal wkBalance = BigDecimal.ZERO;
		BigDecimal wkRealPayAmt = BigDecimal.ZERO;
		OccursList occursList;

		for (int i = 1; i <= iLoanTerm; i++) {
			if (i == iLoanTerm) {
				wkDuePayAmt = iPrincipal.subtract(wkBalance);
			}
			wkInterest = wkBalance.multiply(iRate).divide(new BigDecimal(1200), 5, RoundingMode.HALF_UP).setScale(0, RoundingMode.HALF_UP);
			wkUnpaidInt = BigDecimal.ZERO;
			if (wkInterest.compareTo(iPayIntLimit) > 0) {
				wkUnpaidInt = wkInterest.subtract(iPayIntLimit);
				// wkInterest = iPayIntLimit;
			}
			wkSumUnpaidInt = wkSumUnpaidInt.add(wkUnpaidInt);
			wkBalance = wkBalance.add(wkDuePayAmt);
			wkRealPayAmt = wkDuePayAmt.add(wkUnpaidInt).subtract(wkInterest);
			occursList = new OccursList();
			occursList.putParam("OLoanTerm", i);
			occursList.putParam("ODuePayAmt", wkDuePayAmt);
			occursList.putParam("OBalance", wkBalance);
			occursList.putParam("OInterest", wkInterest);
			occursList.putParam("OUnpaidInt", wkUnpaidInt);
			occursList.putParam("OSumUnpaidInt", wkSumUnpaidInt);
			occursList.putParam("ORealPayAmt", wkRealPayAmt);
			// 將每筆資料放入Tota的OcList
			this.totaVo.addOccursList(occursList);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}