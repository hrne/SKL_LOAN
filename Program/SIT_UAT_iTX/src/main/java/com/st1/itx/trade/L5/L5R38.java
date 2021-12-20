package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;

import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.domain.CdWorkMonthId;
import com.st1.itx.db.service.CdWorkMonthService;

@Service("L5R38")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L5R38 extends TradeBuffer {

	@Autowired
	CdWorkMonthService cdWorkMonthService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5R38 ");
		this.totaVo.init(titaVo);

		String iCode = titaVo.get("L5R38iCode").trim();

		int wYear = 0;
		int wMonth = 0;
		int startDate = 0;
		int endDate = 0;
		int bonusDate = Integer.valueOf(titaVo.getCalDy());
		
		if ("1".equals(iCode)) {
			// 依日期回上個月工作年月及業績起訖日
			int iDate = Integer.valueOf(titaVo.get("L5R38iDate").trim());
			CdWorkMonth cdWorkMonth = cdWorkMonthService.findDateFirst(iDate + 19110000, iDate + 19110000, titaVo);
			if (cdWorkMonth == null) {
				throw new LogicException("E0001", "放款業績工作月對照檔");
			}
			wMonth = cdWorkMonth.getMonth();
			wYear = cdWorkMonth.getYear();
			if (wMonth == 1) {
				wMonth = 13;
				wYear--;
			} else {
				wMonth--;
			}
			// 上工作月
			CdWorkMonthId cdWorkMonthId = new CdWorkMonthId();
			cdWorkMonthId.setYear(wYear);
			cdWorkMonthId.setMonth(wMonth);
			cdWorkMonth = cdWorkMonthService.findById(cdWorkMonthId, titaVo);
			if (cdWorkMonth == null) {
				throw new LogicException("E0001", "放款業績工作月對照檔");
			}
			startDate = cdWorkMonth.getStartDate();
			endDate = cdWorkMonth.getEndDate();
			bonusDate = cdWorkMonth.getBonusDate();
		} else if ("2".equals(iCode)) {
			// 依工作年月回業績起訖日
			wYear = Integer.valueOf(titaVo.get("L5R38iWorkYM").trim().substring(0, 3)) + 1911;
			wMonth = Integer.valueOf(titaVo.get("L5R38iWorkYM").trim().substring(3, 5));

			// 上工作月
			CdWorkMonthId cdWorkMonthId = new CdWorkMonthId();
			cdWorkMonthId.setYear(wYear);
			cdWorkMonthId.setMonth(wMonth);
			CdWorkMonth cdWorkMonth = cdWorkMonthService.findById(cdWorkMonthId, titaVo);
			if (cdWorkMonth == null) {
				throw new LogicException("E0001", "放款業績工作月對照檔");
			}
			startDate = cdWorkMonth.getStartDate();
			endDate = cdWorkMonth.getEndDate();
			bonusDate = cdWorkMonth.getBonusDate();
		} else if ("3".equals(iCode)) {
			// 依日期回工作年月及業績起訖日
			int iDate = Integer.valueOf(titaVo.get("L5R38iDate").trim());
			CdWorkMonth cdWorkMonth = cdWorkMonthService.findDateFirst(iDate + 19110000, iDate + 19110000, titaVo);
			if (cdWorkMonth == null) {
				throw new LogicException("E0001", "放款業績工作月對照檔");
			}
			wMonth = cdWorkMonth.getMonth();
			wYear = cdWorkMonth.getYear();
			startDate = cdWorkMonth.getStartDate();
			endDate = cdWorkMonth.getEndDate();
			bonusDate = cdWorkMonth.getBonusDate();
		}

		wYear -= 1911;

		int wSeason = wYear * 10;

		if (wMonth >= 1 && wMonth <= 3) {
			wSeason += 1;
		} else if (wMonth >= 4 && wMonth <= 6) {
			wSeason += 2;
		} else if (wMonth >= 7 && wMonth <= 9) {
			wSeason += 3;
		} else if (wMonth >= 10 && wMonth <= 13) {
			wSeason += 4;
		}

		this.totaVo.putParam("L5R38oWorkYM", String.format("%03d%02d", wYear, wMonth));
		this.totaVo.putParam("L5R38oWorkYear", wYear);
		this.totaVo.putParam("L5R38oWorkMonth", wMonth);
		this.totaVo.putParam("L5R38oWorkSeason", wSeason);
		this.totaVo.putParam("L5R38oStartDate", startDate);
		this.totaVo.putParam("L5R38oEndDate", endDate);
		this.totaVo.putParam("L5R38oBonusDate", bonusDate);

		this.addList(this.totaVo);
		return this.sendList();
	}
}