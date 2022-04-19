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
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita Year=9,3 Month=9,2 END=X,1
 */

@Service("L6082") // 放款業績工作月對照檔查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6082 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6082.class);

	/* DB服務注入 */
	@Autowired
	public CdWorkMonthService sCdWorkMonthService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6082 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iYear = this.parse.stringToInteger(titaVo.getParam("Year"));
		this.info("L6082 iYear : " + iYear);
		int iFYear = iYear + 1911;

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500; // 19 * 500 = 9500

		// 查詢放款業績工作月對照檔
		Slice<CdWorkMonth> slCdWorkMonth;
		if (iYear == 0) {
			slCdWorkMonth = sCdWorkMonthService.findAll(this.index, this.limit, titaVo);
		} else {
			slCdWorkMonth = sCdWorkMonthService.findYearMonth(iFYear, iFYear, 00, 99, this.index, this.limit, titaVo);
		}
		List<CdWorkMonth> lCdWorkMonth = slCdWorkMonth == null ? null : slCdWorkMonth.getContent();

		if (lCdWorkMonth == null || lCdWorkMonth.size() == 0) {
			throw new LogicException(titaVo, "E0001", "放款業績工作月對照檔"); // 查無資料
		}
		// 如有找到資料
		int fYear = 0;
		for (CdWorkMonth tCdWorkMonth : lCdWorkMonth) {
			OccursList occursList = new OccursList();
			int wkFYear = tCdWorkMonth.getYear();
			wkFYear = wkFYear - 1911;

			if (fYear == tCdWorkMonth.getYear()) {
				continue;
			}
			occursList.putParam("OOYear", wkFYear);

			fYear = tCdWorkMonth.getYear();

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdWorkMonth != null && slCdWorkMonth.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}