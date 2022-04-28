package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4R32")
@Scope("prototype")

public class L4R32 extends TradeBuffer {

	/* DB服務注入 */

	@Autowired
	DateUtil dateUtil;

	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R32 ");
		this.totaVo.init(titaVo);

//		計算日期+判斷是否營業日 for L4703 L9703 預設天數
//   	丟回 1天2天7天
		int iDate = parse.stringToInteger(titaVo.getParam("RimDate"));

		int days1 = getCashDay(iDate, 1);
		int days2 = getCashDay(iDate, 2);
		int days7 = getCashDay(iDate, 7);
		this.totaVo.putParam("L4R32Date1", days1);
		this.totaVo.putParam("L4R32Date2", days2);
		this.totaVo.putParam("L4R32Date7", days7);
		this.addList(this.totaVo);
		return this.sendList();
	}

	private int getCashDay(int date, int days) throws LogicException {
		int tdate = date;
		int iPayDate = 0;
		int tdays = days;
		while (tdays > 0) {
			dateUtil.init();
			dateUtil.setDate_1(tdate);
			dateUtil.setDays(-1);
			iPayDate = dateUtil.getCalenderDay();
			dateUtil.init();
			dateUtil.setDate_2(iPayDate);
			tdate = iPayDate;
			if (!dateUtil.isHoliDay()) {
				tdays--;
			}
		}

		return tdate;

	}

}