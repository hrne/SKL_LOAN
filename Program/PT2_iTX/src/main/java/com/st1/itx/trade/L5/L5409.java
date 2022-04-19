package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L5409ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L5409")
@Scope("prototype")
/**
 * 案件品質排行表(列印)
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5409 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L5409.class);

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public L5409ServiceImpl iL5409ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5409 ");
		this.totaVo.init(titaVo);
		// 撈期間內所有資料，並統計一個專員所有的金額，以擔保品戶號+額度進colllist找是否有逾期>=4，再將逾期比數金額相加為逾期金額，再以高排到低
		String iEndDate = titaVo.getParam("EndDate"); // 截止日期
		String iStartDate = titaVo.getParam("StartDate"); // 起始日期
		int iiEndDate = Integer.valueOf(iEndDate) + 19110000;
		int iiStartDate = Integer.valueOf(iStartDate) + 19110000;
		List<Map<String, String>> iL5409SqlReturn = new ArrayList<Map<String, String>>();
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		try {
			iL5409SqlReturn = iL5409ServiceImpl.FindData(iiStartDate, iiEndDate, titaVo);
		} catch (Exception e) {
			// E5004 讀取DB語法發生問題
			this.info("L5409 ErrorForSql=" + e);
			throw new LogicException(titaVo, "E5004", "");
		}
		for (Map<String, String> r5409SqlReturn : iL5409SqlReturn) {
			OccursList occursList = new OccursList();
			if (r5409SqlReturn.get("F0").equals("")) {
				continue;
			}
			occursList.putParam("OOBsOfficerX", r5409SqlReturn.get("F0"));
			occursList.putParam("OOTotal", r5409SqlReturn.get("F1"));
			if (r5409SqlReturn.get("F2").equals("")) {
				occursList.putParam("OOOvduTotal", 0);
			} else {
				occursList.putParam("OOOvduTotal", r5409SqlReturn.get("F2"));
			}
			occursList.putParam("OOPercent", r5409SqlReturn.get("F3"));
			this.totaVo.addOccursList(occursList);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}