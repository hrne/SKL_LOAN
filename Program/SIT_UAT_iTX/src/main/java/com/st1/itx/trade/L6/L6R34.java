package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.domain.CdWorkMonthId;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L6R34")
@Scope("prototype")
/**
 * 
 * 
 * @author chih cheng
 * @version 1.0.0
 */
public class L6R34 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6R34.class);

	/* DB服務注入 */
	@Autowired
	public CdWorkMonthService iCdWorkMonthService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R34 ");
		this.totaVo.init(titaVo);

		int AcDate = titaVo.getEntDyI();// 7碼
		this.info("AcDate=" + AcDate);

		String AcDate3 = titaVo.getEntDy().substring(1, 4);// 年
		String AcDate2 = titaVo.getEntDy().substring(4, 6);// 月

		int cAcDate3 = Integer.parseInt(AcDate3) + 1911;
		int cAcDate2 = Integer.parseInt(AcDate2);
		this.info("AcDate3=" + AcDate3 + ",AcDate2=" + AcDate2);

		// ---前一個月---
		int beforeStartDay = 0;
		int beforeEndDay = 0;

		CdWorkMonthId bCdWorkMonthId = new CdWorkMonthId();

		if (cAcDate2 == 1) { // 如等於1月年度-1
			bCdWorkMonthId.setYear(cAcDate3 - 1);
			bCdWorkMonthId.setMonth(13);
		} else {
			bCdWorkMonthId.setYear(cAcDate3);
			bCdWorkMonthId.setMonth(cAcDate2 - 1);
		}

		CdWorkMonth beforeWorkMonth = iCdWorkMonthService.findById(bCdWorkMonthId, titaVo);

		if (beforeWorkMonth != null) {
			beforeStartDay = beforeWorkMonth.getStartDate();
			beforeEndDay = beforeWorkMonth.getEndDate();
		}
		this.info("Before StartDate=" + beforeStartDay + ",Before EndDay=" + beforeEndDay);

		// ---當月---
		int iStartDay = 0;
		int iEndDay = 0;
		CdWorkMonthId iCdWorkMonthId = new CdWorkMonthId();
		iCdWorkMonthId.setYear(cAcDate3);
		iCdWorkMonthId.setMonth(cAcDate2);
		CdWorkMonth iWorkMonth = iCdWorkMonthService.findById(iCdWorkMonthId, titaVo);

		if (iWorkMonth != null) {
			iStartDay = iWorkMonth.getStartDate();
			iEndDay = iWorkMonth.getEndDate();
		}
		this.info("iStartDate=" + iStartDay + ",iEndDay=" + iEndDay);

		// ---後一個月---
		int afterStartDay = 0;
		int afterEndDay = 0;
		CdWorkMonthId aCdWorkMonthId = new CdWorkMonthId();

		// if(cAcDate2==13) { //如等於13月年度+1
		// aCdWorkMonthId.setYear(cAcDate3+1);
		// aCdWorkMonthId.setMonth(1);
		// } else {
		aCdWorkMonthId.setYear(cAcDate3);
		aCdWorkMonthId.setMonth(cAcDate2 + 1);
		// }

		CdWorkMonth afterWorkMonth = iCdWorkMonthService.findById(aCdWorkMonthId, titaVo);

		if (afterWorkMonth != null) {
			afterStartDay = afterWorkMonth.getStartDate();
			afterEndDay = afterWorkMonth.getEndDate();
		}
		this.info("After StartDate=" + afterStartDay + ",After EndDay=" + afterEndDay);

		// 如果是12月,增加檢查下一年一第一工
		int nextStartDay = 0;
		int nextEndDay = 0;
		CdWorkMonthId nCdWorkMonthId = new CdWorkMonthId();

		if (cAcDate2 == 12) {
			nCdWorkMonthId.setYear(cAcDate3 + 1);
			nCdWorkMonthId.setMonth(1);

			CdWorkMonth nextWorkMonth = iCdWorkMonthService.findById(nCdWorkMonthId, titaVo);

			if (nextWorkMonth != null) {
				nextStartDay = nextWorkMonth.getStartDate();
				nextEndDay = nextWorkMonth.getEndDate();
			}
		}

		this.info("next StartDate=" + nextStartDay + ",next EndDay=" + nextEndDay);

		// 檢查會計日在哪個工作月
		String finallDate = "0";
		if (beforeStartDay <= AcDate && AcDate <= beforeEndDay) {
			this.info("between beforeDay");
			if (bCdWorkMonthId.getMonth() >= 10) {
				finallDate = String.valueOf(bCdWorkMonthId.getYear()) + String.valueOf(bCdWorkMonthId.getMonth());
			} else {
				finallDate = String.valueOf(bCdWorkMonthId.getYear()) + "0" + String.valueOf(bCdWorkMonthId.getMonth());
			}

		} else if (iStartDay <= AcDate && AcDate <= iEndDay) {
			this.info("between iDay");
			if (iCdWorkMonthId.getMonth() >= 10) {
				finallDate = String.valueOf(iCdWorkMonthId.getYear()) + String.valueOf(iCdWorkMonthId.getMonth());
			} else {
				finallDate = String.valueOf(iCdWorkMonthId.getYear()) + "0" + String.valueOf(iCdWorkMonthId.getMonth());
			}

		} else if (afterStartDay <= AcDate && AcDate <= afterEndDay) {
			this.info("between afterDay");

			if (aCdWorkMonthId.getMonth() >= 10) {
				finallDate = String.valueOf(aCdWorkMonthId.getYear()) + String.valueOf(aCdWorkMonthId.getMonth());
			} else {
				finallDate = String.valueOf(aCdWorkMonthId.getYear()) + "0" + String.valueOf(aCdWorkMonthId.getMonth());
			}
		} else if (nextStartDay <= AcDate && AcDate <= nextEndDay) {
			this.info("between nextDay");
			finallDate = String.valueOf(nCdWorkMonthId.getYear()) + "0" + String.valueOf(nCdWorkMonthId.getMonth());

		}

		int dDat = 0;
		if (finallDate != "" || finallDate != null) {
			dDat = Integer.parseInt(finallDate);
		}

		int tRimYear = 0;
		String WorkMonth = titaVo.getParam("RimWorkMonth");

		for (int i = 0; WorkMonth.length() < 5; i++) {
			WorkMonth = 0 + WorkMonth;
			this.info("L6R34 1 WorkMonth" + i + " : " + WorkMonth);
		}

		String iRimYear = WorkMonth.substring(0, 3);
		this.info("iRimYear=====" + iRimYear);
		tRimYear = Integer.parseInt(iRimYear) + 1911;
		iRimYear = String.valueOf(tRimYear);
		String iRimMonth = WorkMonth.substring(3, 5);
		this.info("iRimMonth=====" + iRimMonth);

		tRimYear = Integer.parseInt(iRimYear + iRimMonth);

		this.info("dDat=" + dDat + ",tRimYear=" + tRimYear);

		// 判斷輸入的工作月是否大於等於會計日的工作月
		if (tRimYear >= dDat) {
			this.info("合理範圍日期");
			totaVo.putParam("L6R34Flag", "0");
		} else {
			totaVo.putParam("L6R34Flag", "1");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}