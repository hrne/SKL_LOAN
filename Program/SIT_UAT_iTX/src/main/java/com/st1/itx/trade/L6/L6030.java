package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxHoliday;
import com.st1.itx.db.service.TxHolidayService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita Country=X,2 YearMonthStart=9,5 YearMonthEnd=9,5 END=X,1
 */

@Service("L6030") // 特殊/例假日查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6030 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6030.class);

	/* DB服務注入 */
	@Autowired
	public TxHolidayService sTxHolidayService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6030 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iCountry = titaVo.getParam("Country");
		int iYearMonth = this.parse.stringToInteger(titaVo.getParam("YearMonthStart"));

		String iYearMonthStart = titaVo.getParam("YearMonthStart") + "01";
		String iYearMonthEnd = titaVo.getParam("YearMonthEnd") + "31";
		int yearMonthStart = this.parse.stringToInteger(iYearMonthStart) + 19110000;
		int yearMonthEnd = this.parse.stringToInteger(iYearMonthEnd) + 19110000;

		this.info("L6030 yearMonth : " + yearMonthStart + "~" + yearMonthEnd);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 400; // 8 * 400 = 3,200

		// 查詢假日檔
		Slice<TxHoliday> slTxHoliday;
		if (iYearMonth == 0) {
			slTxHoliday = sTxHolidayService.findAll(this.index, this.limit, titaVo);
		} else {
			slTxHoliday = sTxHolidayService.findHoliday(iCountry, yearMonthStart, yearMonthEnd, this.index, this.limit, titaVo);
		}
		List<TxHoliday> lTxHoliday = slTxHoliday == null ? null : slTxHoliday.getContent();

		if (lTxHoliday == null || lTxHoliday.size() == 0) {
			throw new LogicException(titaVo, "E0001", "假日檔"); // 查無資料
		}
		// 如有找到資料
		for (TxHoliday tTxHoliday : lTxHoliday) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOHoliday", tTxHoliday.getHoliday());
			occursList.putParam("OOTypeCode", tTxHoliday.getTypeCode());
			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slTxHoliday != null && slTxHoliday.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}