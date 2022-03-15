package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.service.SystemParasService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * RimToday=9,7<br>
 */

@Service("L4R11")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4R11 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L4R11.class);
	@Autowired
	public SystemParasService systemParasService;

	@Autowired
	public Parse parse;

	@Autowired
	DateUtil dateUtil;

//	private int tbsdyf = 20201008;
	private int tbsdyf = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R11 ");
		this.totaVo.init(titaVo);

		SystemParas tSystemParas = systemParasService.findById("LN");

		tbsdyf = this.getTxBuffer().getTxCom().getTbsdyf();

		this.info("... tbsdyf = " + tbsdyf);

//		特定日扣款---->如果本日~下一營業日之昨日=設定日，放該日，否則放0
		this.info("... postSpecificDd = " + transToRocType(findSpecificDeductDate(tbsdyf, tSystemParas, 1)));

//		特定日二扣---->本日 = 五個營業日前(含本日)
		int n4bsdyf = find4LsDay(tbsdyf);
//		特定日二扣---->如果本日~下一營業日之昨日=設定日，放該日，否則放0
		this.info("... n4bsdyf = " + n4bsdyf);
		this.info("... postSecondSpecificDd = " + transToRocType(findSpecificDeductDate(n4bsdyf, tSystemParas, 1)));

//		每日扣款----->如果明日為假日則放入下一營業日
//		本營業日<應繳日<=次營業日

		this.info("... entryDate = " + transToRocType(findNextBsDay(findNextBsDay(tbsdyf))));

//		本營業日之下下營業日
		this.totaVo.putParam("L4r11EntryDate", transToRocType(findNextBsDay(findNextBsDay(tbsdyf))));

//		特殊日扣款
		this.totaVo.putParam("L4r11PostSpecificDd", transToRocType(findSpecificDeductDate(tbsdyf, tSystemParas, 1)));
		this.totaVo.putParam("L4r11PostSecondSpecificDd", transToRocType(findSpecificDeductDate(n4bsdyf, tSystemParas, 1)));

//		每日扣款
//		本營業日之隔日
		this.totaVo.putParam("L4r11AchSpecificDdFrom", transToRocType(findNextDay(tbsdyf)));
//		本營業日之下營業日
		this.totaVo.putParam("L4r11AchSpecificDdTo", transToRocType(findNextBsDay(tbsdyf)));
//		每日扣款之二扣算法
//		1.找出前四個營業日(5個營業日-今日)
		this.totaVo.putParam("L4r11AchSecondSpecificDdFrom", transToRocType(findNextDay(find4LsDay(tbsdyf))));
//		2.二扣營業日之隔日~下營業日
		this.totaVo.putParam("L4r11AchSecondSpecificDdTo", transToRocType(findNextBsDay(find4LsDay(tbsdyf))));

		this.info("... AchSecDeductDate = " + find4LsDay(tbsdyf));

		this.addList(this.totaVo);
		return this.sendList();
	}

//	找出當月特殊扣款日後第五個營業日
	private int find4BsDd(int dd) throws LogicException {
		dateUtil.init();
		String sdd = FormatUtil.pad9("" + dd, 2);
		this.info("!!! L4R11 sdd :" + sdd);
		String yyyymm = ("" + tbsdyf).substring(0, 6);
		this.info("!!! L4R11 yyyymm :" + yyyymm);
		int today = parse.stringToInteger(yyyymm + sdd);
		this.info("!!! L4R11 today :" + today);

		int todayPlus1BsDay = 0;
		int todayPlus2BsDay = 0;
		int todayPlus3BsDay = 0;
		int todayPlus4BsDay = 0;
//		int todayPlus5BsDay = 0;

		dateUtil.init();
		todayPlus1BsDay = findNextBsDay(today);
		todayPlus2BsDay = findNextBsDay(todayPlus1BsDay);
		todayPlus3BsDay = findNextBsDay(todayPlus2BsDay);
		todayPlus4BsDay = findNextBsDay(todayPlus3BsDay);
//		todayPlus5BsDay = findNextBsDay(todayPlus4BsDay);

		dd = parse.stringToInteger(("" + todayPlus4BsDay).substring(6));

		this.info("!!! L4R11 dd :" + dd);
		return dd;
	}

	private int transToRocType(int date) {
		if (date > 19110000) {
			date = date - 19110000;
		}
		return date;
	}

//	找出當月特殊扣款日後第五個營業日
	private int find4BsDay(int day) throws LogicException {
		dateUtil.init();
		String sdd = FormatUtil.pad9("" + day, 2);
		this.info("!!! L4R11 sdd :" + sdd);
		String yyyymm = ("" + tbsdyf).substring(0, 6);
		this.info("!!! L4R11 yyyymm :" + yyyymm);
		int today = parse.stringToInteger(yyyymm + sdd);
		this.info("!!! L4R11 today :" + today);

		int todayPlus1BsDay = 0;
		int todayPlus2BsDay = 0;
		int todayPlus3BsDay = 0;
		int todayPlus4BsDay = 0;

		dateUtil.init();
		todayPlus1BsDay = findNextBsDay(today);
		todayPlus2BsDay = findNextBsDay(todayPlus1BsDay);
		todayPlus3BsDay = findNextBsDay(todayPlus2BsDay);
		todayPlus4BsDay = findNextBsDay(todayPlus3BsDay);

		day = todayPlus4BsDay;

		this.info("!!! L4R11 dd :" + day);
		return day;
	}

	private int findNextBsDay(int today) throws LogicException {
		while (true) {
			dateUtil.init();
			dateUtil.setDate_1(today);
			dateUtil.setDays(1);
			today = dateUtil.getCalenderDay();
			if (!dateUtil.isHoliDay()) {
				break;
			}
		}
		return today;
	}

	private int findNextDay(int today) throws LogicException {
		dateUtil.init();
		dateUtil.setDate_1(today);
		dateUtil.setDays(1);
		today = dateUtil.getCalenderDay();
		return today;
	}

	private int findLastDay(int today) throws LogicException {
		dateUtil.init();
		dateUtil.setDate_1(today);
		dateUtil.setDays(-1);
		today = dateUtil.getCalenderDay();
		return today;
	}

	private int findLastBsDay(int today) throws LogicException {
		while (true) {
			dateUtil.init();
			dateUtil.setDate_1(today);
			dateUtil.setDays(-1);
			today = dateUtil.getCalenderDay();
			if (!dateUtil.isHoliDay()) {
				break;
			}
		}
		return today;
	}

//	找出當月特殊扣款日前五個營業日
	private int find4LsDay(int day) throws LogicException {
		dateUtil.init();
		String sdd = FormatUtil.pad9("" + day, 2);
		this.info("!!! L4R11 sdd :" + sdd);
		String yyyymm = ("" + tbsdyf).substring(0, 6);
		this.info("!!! L4R11 yyyymm :" + yyyymm);
		int today = parse.stringToInteger(yyyymm + sdd);
		this.info("!!! L4R11 today :" + today);

		int todayMinus1BsDay = 0;
		int todayMinus2BsDay = 0;
		int todayMinus3BsDay = 0;
		int todayMinus4BsDay = 0;

		dateUtil.init();
		todayMinus1BsDay = findLastBsDay(today);
		todayMinus2BsDay = findLastBsDay(todayMinus1BsDay);
		todayMinus3BsDay = findLastBsDay(todayMinus2BsDay);
		todayMinus4BsDay = findLastBsDay(todayMinus3BsDay);

		day = todayMinus4BsDay;

		this.info("!!! L4R11 dd :" + day);
		return day;
	}

//	找出特殊日扣款日期
//	startDate    = 起始日
//                 一般:本日 
//				        二扣:本日-五個營業日(含本日)
//	tSystemParas = 系統設定檔
//	1.找出隔日到下營業日，此區間若符合設定檔之日期(1,10,20)
//	2.return該日期(民國年)
//	type : 1.post 2.ACH
	private int findSpecificDeductDate(int startDate, SystemParas tSystemParas, int type) throws LogicException {
		int SpecificDeductDate = 0;
		int nextDate = 0;
		int endDate = 0;
		int dd1 = 0;
		int dd2 = 0;
		int dd3 = 0;
		int dd4 = 0;
		int dd5 = 0;

//		次日
		nextDate = findNextDay(startDate);
//		下營業日
		endDate = findNextBsDay(startDate);

		this.info("0-本日 = " + startDate);
		this.info("0-次日 = " + nextDate);
		this.info("0-下營業日 = " + endDate);

		if (type == 1) {
			dd1 = tSystemParas.getPostDeductDD1();
			dd2 = tSystemParas.getPostDeductDD2();
			dd3 = tSystemParas.getPostDeductDD3();
			dd4 = tSystemParas.getPostDeductDD4();
			dd5 = tSystemParas.getPostDeductDD5();
		} else {
			dd1 = tSystemParas.getAchDeductDD1();
			dd2 = tSystemParas.getAchDeductDD2();
			dd3 = tSystemParas.getAchDeductDD3();
			dd4 = tSystemParas.getAchDeductDD4();
			dd5 = tSystemParas.getAchDeductDD5();
		}

		this.info("dd1 ... " + dd1);
		this.info("dd2 ... " + dd2);
		this.info("dd3 ... " + dd3);
		this.info("dd4 ... " + dd4);
		this.info("dd5 ... " + dd5);

//		次日非假日
		if (nextDate == endDate) {
			int nextDd = parse.stringToInteger(("" + nextDate).substring(6));
			if (nextDd == dd1 || nextDd == dd2 || nextDd == dd3) {
				SpecificDeductDate = nextDate - 19110000;

				this.info("1-次日非假日，特定日 = " + nextDate);
			}
//		次日為假日
//		次日到下營業日，若符合特殊日回傳
		} else {
			int i = findNextDay(startDate);
			while (i <= endDate) {
				int iDd = parse.stringToInteger(("" + i).substring(6));

				this.info("2-次日 = " + i);

				if (iDd == dd1 || iDd == dd2 || iDd == dd3) {
					SpecificDeductDate = i - 19110000;

					this.info("2-次日為假日，特定日 = " + i);

					break;
				} else {
					i = findNextDay(i);
				}
			}
		}
		return SpecificDeductDate;
	}
}