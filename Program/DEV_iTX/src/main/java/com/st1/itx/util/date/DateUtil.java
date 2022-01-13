package com.st1.itx.util.date;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.db.domain.TxBizDate;
//import com.st1.itx.db.domain.HoliDay;
//import com.st1.itx.db.domain.HoliDayId;
//import com.st1.itx.db.service.HoliDayService;
import com.st1.itx.db.domain.TxHoliday;
import com.st1.itx.db.domain.TxHolidayId;
import com.st1.itx.db.service.TxHolidayService;
import com.st1.itx.util.log.SysLogger;
import com.st1.itx.util.parse.Parse;

/**
 * Calculate dates set date_1 Start and date_2 End
 * 
 * @author AdamPan
 * @version 1.0.0
 *
 */
@Component("dateUtil")
@Scope("prototype")
public class DateUtil extends SysLogger {

//	@Autowired
//	public HoliDayService holidayService;

	@Autowired
	public TxHolidayService sTxHolidayService;

	@Autowired
	public Parse parse;

	private DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");
	private DateTime date_1, date_2;

	private int days = 0, mons = 0, years = 0, dayOfWeek = -1;

	private boolean bcFg = false;

	private final String format = "yyyyMMdd";

	@PostConstruct
	public void init() {
		this.days = 0;
		this.mons = 0;
		this.years = 0;
		this.date_1 = null;
		this.date_2 = null;
		this.bcFg = false;
	}

	/**
	 * check date_1 And date_2
	 * 
	 * @throws LogicException
	 */
	private void check() throws LogicException {
		if (this.date_1.isAfter(this.date_2))
			throw new LogicException("CE000", "日期錯誤 date_1 > date_2 " + this.date_1 + " > " + this.date_2);
	}

	/**
	 * date_1
	 * 
	 * @return Integer for date_1
	 * @throws LogicException LogicException
	 */

	public int getDate_1Integer() throws LogicException {
		if (this.bcFg)
			return parse.stringToInteger(this.date_1.toString(this.format));
		else
			return parse.stringToInteger(this.date_1.toString(this.format)) - 19110000;
	}

	/**
	 * date_1
	 * 
	 * @return String for date_1
	 * @throws LogicException LogicException
	 */
	public String getDate_1String() throws LogicException {
		int temp = parse.stringToInteger(this.date_1.toString(this.format));
		if (!this.bcFg)
			temp = temp - 19110000;

		return Integer.toString(temp);
	}

	/**
	 * set Date1
	 * 
	 * @param date_1 date1
	 */
	public void setDate_1(int date_1) {
		if (date_1 > 19110000)
			this.bcFg = true;
		else
			this.bcFg = false;
		this.date_1 = formatter.parseDateTime(Integer.toString(date_1 < 19110000 ? date_1 + 19110000 : date_1));
	}

	/**
	 * set Date1
	 * 
	 * @param date_1 date1
	 * @throws LogicException If date_1 Is Empty
	 */
	public void setDate_1(String date_1) throws LogicException {
		if (date_1 == null || date_1.trim().isEmpty())
			throw new LogicException("CE000", "DateUtil Date_1 is null || Empty");

		int temp = parse.stringToInteger(date_1);
		if (temp > 19110000)
			this.bcFg = true;
		else
			this.bcFg = false;
		temp = temp < 19110000 ? temp + 19110000 : temp;

		this.date_1 = formatter.parseDateTime(Integer.toString(temp));
	}

	/**
	 * date_2
	 * 
	 * @return Integer for date_2
	 * @throws LogicException LogicException
	 */
	public int getDate_2Integer() throws LogicException {
		if (this.bcFg)
			return parse.stringToInteger(this.date_2.toString(this.format));
		else
			return parse.stringToInteger(this.date_2.toString(this.format)) - 19110000;
	}

	/**
	 * date_2
	 * 
	 * @return String for date_2
	 * @throws LogicException when go warng
	 */
	public String getDate_2String() throws LogicException {
		int temp = parse.stringToInteger(this.date_2.toString(this.format));
		if (this.bcFg)
			temp = temp - 19110000;

		return Integer.toString(temp);
	}

	/**
	 * set Date2
	 * 
	 * @param date_2 date2
	 */
	public void setDate_2(int date_2) {
		if (date_2 > 19110000)
			this.bcFg = true;
		else
			this.bcFg = false;
		this.date_2 = formatter.parseDateTime(Integer.toString(date_2 < 19110000 ? date_2 + 19110000 : date_2));
	}

	/**
	 * set Date2
	 * 
	 * @param date_2 date2
	 * @throws LogicException If date_2 Is Empty
	 */
	public void setDate_2(String date_2) throws LogicException {
		if (date_2 == null || date_2.trim().isEmpty())
			throw new LogicException("CE000", "DateUtil Date_2 is null || Empty");

		int temp = parse.stringToInteger(date_2);
		if (temp > 19110000)
			this.bcFg = true;
		else
			this.bcFg = false;
		temp = temp < 19110000 ? temp + 19110000 : temp;

		this.date_2 = formatter.parseDateTime(Integer.toString(temp));
	}

	/**
	 * get DayDiff
	 * 
	 * @return int days
	 */
	public int getDays() {
		return days;
	}

	/**
	 * set DayDiff
	 * 
	 * @param days int
	 */
	public void setDays(int days) {
		this.days = days;
	}

	/**
	 * set DayDiff
	 * 
	 * @param days String
	 */
	public void setDays(String days) {
		if (days != null && !days.isEmpty())
			this.days = Integer.parseInt(days.replace(" ", ""));
	}

	/**
	 * get MonDiff
	 * 
	 * @return int mons
	 */
	public int getMons() {
		return mons;
	}

	/**
	 * set MonDiff
	 * 
	 * @param mons Integer
	 */
	public void setMons(int mons) {
		this.mons = mons;
	}

	/**
	 * set MonDiff
	 * 
	 * @param mons String
	 */
	public void setMons(String mons) {
		if (mons != null && !mons.isEmpty())
			this.mons = Integer.parseInt(mons.replace(" ", ""));
	}

	/**
	 * get YearDiff
	 * 
	 * @return years int
	 */
	public int getYears() {
		return years;
	}

	/**
	 * set YearDiff
	 * 
	 * @param years Integer
	 */
	public void setYears(int years) {
		this.years = years;
	}

	/**
	 * set YearDiff
	 * 
	 * @param years String
	 */
	public void setYears(String years) {
		if (years != null && !years.isEmpty())
			this.years = Integer.parseInt(years.replace(" ", ""));
	}

	/**
	 * get DayOfWeek
	 * 
	 * @return Integer dayOfWeek
	 */
	public int getDayOfWeek() {
		return dayOfWeek;
	}

	/**
	 * get day of week
	 * 
	 * @param date_1 String date1
	 * @return Integer dayOfWeek
	 * @throws LogicException When date_1 is not String numeric
	 */
	public int getDayOfWeek(String date_1) throws LogicException {
		this.setDate_1(date_1);
		return this.date_1.getDayOfWeek();
	}

	/**
	 * get day of week
	 * 
	 * @param date_1 Integer date1
	 * @return Integer dayOfWeek
	 * @throws LogicException When date_1 is not numeric
	 */
	public int getDayOfWeek(int date_1) throws LogicException {
		this.setDate_1(date_1);
		return this.date_1.getDayOfWeek();
	}

	/**
	 * get R.O.C Date of Today
	 * 
	 * @return Integer R.O.C date
	 */
	public int getNowIntegerRoc() {
		Date date = new Date();
		SimpleDateFormat dt = new SimpleDateFormat("yyyyMMdd");
		return Integer.parseInt(dt.format(date)) - 19110000;
	}

	/**
	 * get R.O.C Date of Today
	 * 
	 * @return String R.O.C date
	 */
	public String getNowStringRoc() {
		Date date = new Date();
		SimpleDateFormat dt = new SimpleDateFormat("yyyyMMdd");
		return new String("" + (Integer.parseInt(dt.format(date)) - 19110000));
	}

	/**
	 * get B.C Date of Today
	 * 
	 * @return Integer B.C Date
	 */
	public int getNowIntegerForBC() {
		Date date = new Date();
		SimpleDateFormat dt = new SimpleDateFormat("yyyyMMdd");
		return Integer.parseInt(dt.format(date));
	}

	/**
	 * get B.C Date of Today
	 * 
	 * @return String B.C Date
	 */
	public String getNowStringBc() {
		Date date = new Date();
		SimpleDateFormat dt = new SimpleDateFormat("yyyyMMdd");
		return dt.format(date);
	}

	/**
	 * get Time Now
	 * 
	 * @return Integer HHmmss
	 */
	public int getNowIntegerTime() {
		Date date = new Date();
		SimpleDateFormat dt = new SimpleDateFormat("HHmmss");
		return Integer.parseInt(dt.format(date));
	}

	/**
	 * get Time Now
	 * 
	 * @return String HHmmss
	 */
	public String getNowStringTime() {
		Date date = new Date();
		SimpleDateFormat dt = new SimpleDateFormat("HHmmss");
		return dt.format(date);
	}

	/**
	 * get day Of Month with Maximum Value
	 * 
	 * @return Integer days
	 */
	public int getMonLimit() {
		return this.date_1.dayOfMonth().withMaximumValue().getDayOfMonth();
	}

	/**
	 * get day of Month with Maximum Value
	 * 
	 * @return Integer 366 or 365
	 */
	public int getYearLimit() {
		return this.date_1.year().isLeap() ? 366 : 365;
	}

	/**
	 * count Total Years and Total Mons and Total Days call getDays, getMons,
	 * getYears to get result
	 * 
	 * @throws LogicException If date_1 Great date_2
	 */
	public void dateDiff() throws LogicException {
		this.check();
		this.years = Years.yearsBetween(this.date_1, this.date_2).getYears();
		this.mons = Months.monthsBetween(this.date_1, this.date_2).getMonths();
		this.days = Days.daysBetween(this.date_1, this.date_2).getDays();
	}

	/**
	 * count for how many years and mons and days call getDays, getMons, getYears to
	 * get result
	 * 
	 * @throws LogicException If date_1 Great date_2
	 */
	public void dateDiffSp() throws LogicException {
		this.check();

		DateTime dateTemp;

		this.years = Years.yearsBetween(this.date_1, this.date_2).getYears();
		dateTemp = this.date_1.plusYears(this.years);
		this.mons = Months.monthsBetween(dateTemp, this.date_2).getMonths();
		dateTemp = dateTemp.plusMonths(this.mons);
		this.days = Days.daysBetween(dateTemp, this.date_2).getDays();

	}

	/**
	 * count CalenderDay for Date call getDayOfWeek can get dayOfWeek
	 * 
	 * @return Integer of Date
	 * @throws LogicException if date_2 == null
	 */
	public int getCalenderDay() throws LogicException {
		this.date_2 = null;
		this.info("date_1 = " + this.date_1);
		this.info("Years  = " + this.years);
		this.info("Mons   = " + this.mons);
		this.info("days   = " + this.days);

		if (this.years < 0)
			this.date_1 = this.date_1.minusYears(this.years * -1);
		if (this.mons < 0)
			this.date_1 = this.date_1.minusMonths(this.mons * -1);
		if (this.days < 0)
			this.date_1 = this.date_1.minusDays(this.days * -1);
		if (this.years > 0)
			this.date_1 = this.date_1.plusYears(this.years);
		if (this.mons > 0)
			this.date_1 = this.date_1.plusMonths(this.mons);
		if (this.days > 0)
			this.date_1 = this.date_1.plusDays(this.days);

		this.date_2 = this.date_1;

		if (this.date_2 == null)
			throw new LogicException("CE000", "DateUtil getCalenderDay days = 0 mos = 0 years = 0 !!!!");

		this.dayOfWeek = this.date_2.getDayOfWeek();
		this.days = this.date_2.dayOfMonth().withMaximumValue().getDayOfMonth();

		if (this.bcFg)
			return (this.date_2.getYear() * 10000 + this.date_2.getMonthOfYear() * 100 + this.date_2.getDayOfMonth());
		else
			return (this.date_2.getYear() * 10000 + this.date_2.getMonthOfYear() * 100 + this.date_2.getDayOfMonth()) - 19110000;
	}

	/**
	 * 營業日期相關資料
	 * 
	 * @return TxBizDate
	 * @throws LogicException when isHolidySkip Non or False Date_1 is Holiday
	 */
	public TxBizDate getForTxBizDate(boolean... isThrowError) throws LogicException {
		int year = this.date_1.getYear();
		int mon = this.date_1.getMonthOfYear();
		int day = this.date_1.getDayOfMonth();
		int bcDate = year * 10000 + mon * 100 + day;

		this.date_2 = this.date_1;
		if (this.isHoliDay() && isThrowError.length == 0)
			throw new LogicException("CE000", "DateUtil 日期為假日!!");
		else if (isThrowError.length != 0 && isThrowError[0]) {
			while (true) {
				this.init();
				this.setDate_1(bcDate);
				this.setDays(1);
				bcDate = this.getCalenderDay();
				if (!this.isHoliDay()) 
					break;
			}
		}

		TxBizDate txBizDate = new TxBizDate();
		txBizDate.setTbsDyf(bcDate);

		this.init();
		this.setDate_1(bcDate);
		while (true) {
			this.setDays(1);
			bcDate = this.getCalenderDay();
			if (!this.isHoliDay())
				break;
			this.init();
			this.setDate_1(bcDate);
		}
		txBizDate.setNbsDyf(bcDate);
		while (true) {
			this.init();
			this.setDate_1(bcDate);
			this.setDays(1);
			bcDate = this.getCalenderDay();
			if (!this.isHoliDay())
				break;
		}
		txBizDate.setNnbsDyf(bcDate);
		bcDate = txBizDate.getTbsDyf();
		while (true) {
			this.init();
			this.setDate_1(bcDate);
			this.setDays(-1);
			bcDate = this.getCalenderDay();
			if (!this.isHoliDay())
				break;
		}
		txBizDate.setLbsDyf(bcDate);

		this.init();
		this.setDate_1(txBizDate.getTbsDyf());
		this.setMons(-1);
		bcDate = this.getCalenderDay();
		this.setDate_1(bcDate);
		txBizDate.setLmnDyf(bcDate / 100 * 100 + this.getMonLimit());

		this.init();
		this.setDate_1(txBizDate.getTbsDyf());
		txBizDate.setTmnDyf(txBizDate.getTbsDyf() / 100 * 100 + this.getMonLimit());

		bcDate = txBizDate.getTbsDyf() / 100 * 100 + this.getMonLimit();
		this.setDate_2(bcDate);
		if (!this.isHoliDay())
			txBizDate.setMfbsDyf(bcDate);
		else {
			while (true) {
				this.init();
				this.setDate_1(bcDate);
				this.setDays(-1);
				bcDate = this.getCalenderDay();
				if (!this.isHoliDay())
					break;
			}
			txBizDate.setMfbsDyf(bcDate);
		}

		txBizDate.setTbsDy(txBizDate.getTbsDyf() - 19110000);
		txBizDate.setNbsDy(txBizDate.getNbsDyf() - 19110000);
		txBizDate.setNnbsDy(txBizDate.getNnbsDyf() - 19110000);
		txBizDate.setLbsDy(txBizDate.getLbsDyf() - 19110000);
		txBizDate.setLmnDy(txBizDate.getLmnDyf() - 19110000);
		txBizDate.setTmnDy(txBizDate.getTmnDyf() - 19110000);
		txBizDate.setMfbsDy(txBizDate.getMfbsDyf() - 19110000);
		txBizDate.setDateCode("TW");

		return txBizDate;
	}

	public int getbussDate(int bcDate, int days) throws LogicException {

//		int rDate = 0;
		int dcnt = 0;
		int dparm = 0;
		if (days < 0) {
			dparm = -1;
		} else if (days > 0) {
			dparm = 1;
		} else {
			return bcDate;
		}

		int absdays = Math.abs(days);

		while (true) {
			this.init();
//			this.info("getbussDate b ="+bcDate);
			this.setDate_1(bcDate);
			this.setDays(dparm);
			bcDate = this.getCalenderDay();
//			this.info("getbussDate a ="+bcDate);
			if (!this.isHoliDay()) {
				dcnt++;
			}
			if (dcnt >= absdays) {
				return bcDate;
			}
		}

	}

	/**
	 * If you have called getCalenderDay before this method, Can call directly<br>
	 * else set date_2 first
	 * 
	 * @return boolean isHoliDay true else false
	 * @throws LogicException 123
	 */
	public boolean isHoliDay() throws LogicException {

		int year = this.date_2.getYear();
		int mon = this.date_2.getMonthOfYear();
		int day = this.date_2.getDayOfMonth();
		int bcDate = year * 10000 + mon * 100 + day;

		TxHolidayId tTxHolidayId = new TxHolidayId();
		tTxHolidayId.setCountry("TW");
		tTxHolidayId.setHoliday(bcDate);
		TxHoliday tTxHoliday = sTxHolidayService.findById(tTxHolidayId);

		if (tTxHoliday != null) {
			return true;
		}

		return false;

//		int year = this.date_2.getYear();
//		int mon = this.date_2.getMonthOfYear();
//		int day = this.date_2.getDayOfMonth();
//
//		HoliDayId holidayId = new HoliDayId(year, "TW");
//		HoliDay holiday = this.holidayService.findById(holidayId);
//
//		if (holiday == null) {
//			this.info("無此假日檔 : " + holidayId);
//			return false;
//		}
//
//		String holFg = "";
//		switch (mon) {
//		case 1:
//			holFg = holiday.getJanuary();
//			break;
//		case 2:
//			holFg = holiday.getFebruary();
//			break;
//		case 3:
//			holFg = holiday.getMarch();
//			break;
//		case 4:
//			holFg = holiday.getApril();
//			break;
//		case 5:
//			holFg = holiday.getMay();
//			break;
//		case 6:
//			holFg = holiday.getJune();
//			break;
//		case 7:
//			holFg = holiday.getJuly();
//			break;
//		case 8:
//			holFg = holiday.getAugust();
//			break;
//		case 9:
//			holFg = holiday.getSeptemper();
//			break;
//		case 10:
//			holFg = holiday.getOctober();
//			break;
//		case 11:
//			holFg = holiday.getNovember();
//			break;
//		case 12:
//			holFg = holiday.getDecember();
//			break;
//		default:
//			this.warn("isHoliDay mon error!!");
//			break;
//		}
//
//		return holFg.charAt(day - 1) != ' ' ? true : false;
	}

}
