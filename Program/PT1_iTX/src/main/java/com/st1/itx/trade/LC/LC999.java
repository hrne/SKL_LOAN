package com.st1.itx.trade.LC;

import java.text.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;

import com.st1.itx.db.domain.TxHoliday;
import com.st1.itx.db.domain.TxHolidayId;
import com.st1.itx.db.service.TxHolidayService;
import com.st1.itx.db.service.TxTranCodeService;
import com.st1.itx.util.date.DateUtil;

import com.st1.itx.util.data.DataLog;

import com.st1.itx.db.service.springjpa.cm.TableColumnServiceImpl;

@Service("LC999")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class LC999 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(LC999.class);

	@Autowired
	public TableColumnServiceImpl tableColumnServiceImpl;

	@Autowired
	public TxHolidayService sTxHolidayService;

	@Autowired
	public TxTranCodeService txTranCodeService;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC999 ");
		this.totaVo.init(titaVo);

//		String iFuncd = titaVo.get("iFuncd").trim();
//
//		String iParm = titaVo.get("iParm").trim();

//		switch (iFuncd) {
//		case "1":
//			procHoliday(titaVo, iParm);
//			break;
//		case "2":
//			testDateUtil(titaVo, iParm);
//			break;
//		}

//		testDataLog(titaVo);

//		testCE901(titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void testCE901(TitaVo titaVo) throws LogicException {

		String msg = String.format("%-50s", "測試主管刷卡");

		this.info("LC999.testCE901=" + msg);

		throw new LogicException(titaVo, "CE901", msg);
	}

	private void testDataLog(TitaVo titaVo) throws LogicException {
//		MySpring.newTask("L9132", this.txBuffer, titaVo);
	}

	private void testTableColumn2(TitaVo titaVo) throws LogicException {
		this.info("LC999.testTableColumn");

//		tableColumnServiceImpl.getEntityManager(titaVo);
//		try {
//			List<TableColumnVo> lTableColumnVo = tableColumnServiceImpl.findAll2("TxTranCode");
//			for (TableColumnVo tTableColumnVo : lTableColumnVo) {
//				this.info("table/column/comments=" + tTableColumnVo.gettABLE_NAME() + "/" + tTableColumnVo.getcOLUMN_NAME() + "/" + tTableColumnVo.getcOMMENTS());
//			}
//		} catch (Throwable e) {
//			throw new LogicException(titaVo, "EC004", e.getMessage());
//		}

	}

	private void testTableColumn(TitaVo titaVo) throws LogicException {
		this.info("LC999.testTableColumn2");

		try {
			List<HashMap<String, String>> lTableColumnVo = tableColumnServiceImpl.findAll("TxTranCode");

			for (HashMap<String, String> tVo : lTableColumnVo) {
				this.info("Vo = " + tVo.get("F1") + "/" + tVo.get("F2") + "/" + tVo.get("F3"));
			}

		} catch (Throwable e) {
			throw new LogicException(titaVo, "EC004", e.getMessage());
		}

	}

	private void testDateUtil(TitaVo titaVo, String iParm) throws LogicException {
		this.info("LC999.testDateUtil b = " + iParm);

		if (iParm.length() < 12) {
			throw new LogicException(titaVo, "E0000", "需輸入12參數");
		}
		int tbsdy = Integer.valueOf(iParm.substring(0, 8));
		int m = Integer.valueOf(iParm.substring(8, 10));
		int d = Integer.valueOf(iParm.substring(10, 12));
		dDateUtil.init();
		dDateUtil.setDate_1(tbsdy);
		dDateUtil.setMons(m);
		dDateUtil.setDays(d);
		int payDate = dDateUtil.getCalenderDay();

		this.info("LC999.testDateUtil r = " + payDate);
	}

	private void procHoliday(TitaVo titaVo, String iParm) throws LogicException {
		this.info("LC999.procHoliday=" + iParm);
		if (iParm.length() < 6) {
			throw new LogicException(titaVo, "E0000", "需輸入6位西元年月");
		}
		String year = iParm.substring(0, 4);

		String month = iParm.substring(4, 6);

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		Calendar c = Calendar.getInstance();

		Date ymd = new Date();

		try {
			ymd = dateFormat.parse(year + month + "01");
		} catch (Exception e) {
			throw new LogicException(titaVo, "E0000", "日期有誤[" + year + "0101]");
		}

		for (int i = 0; i < 31; i++) {
			String y = String.format("%tY", ymd);
			String m = String.format("%tm", ymd);
			String d = String.format("%td", ymd);
			String w = String.format("%ta", ymd);

			this.info("date = " + y + m + d + "," + w + "/" + year + month);

			if (!y.equals(year) || !m.equals(month)) {
				break;
			}

			boolean holiday = isHoliday(m + d);
			if (holiday || "Sat".equals(w) || "Sun".equals(w) || "星期六".equals(w) || "星期日".equals(w)) {
				this.info("date = " + y + m + d + "/" + w + " is holiday");
				TxHoliday tTxHoliday = new TxHoliday();

				TxHolidayId tTxHolidayId = new TxHolidayId();

				tTxHolidayId.setCountry("TW");
				tTxHolidayId.setHoliday(Integer.valueOf(y + m + d));
				tTxHoliday.setTxHolidayId(tTxHolidayId);
				tTxHoliday.setTypeCode("1");
				try {
					sTxHolidayService.insert(tTxHoliday);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料已存在
				}
			} else {
				this.info("date = " + y + m + d + "/" + w);
			}

			c.setTime(ymd);
			c.add(Calendar.DAY_OF_YEAR, 1);// 日期加1天
			ymd = c.getTime();
		}

	}

	private boolean isHoliday(String md) {
		if ("0101".equals(md) || "0228".equals(md) || "0404".equals(md) || "0405".equals(md) || "0501".equals(md) || "1010".equals(md)) {
			return true;
		} else {
			return false;
		}
	}
}