package com.st1.itx.trade.L9;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.MonthlyLM052Loss;
import com.st1.itx.db.service.MonthlyLM052LossService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * L9911會計部核定備抵損失查詢
 * 
 * @author ST1 - Chih Wei
 * @version 1.0.0
 */
@Service("L9911")
@Scope("prototype")
public class L9911 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L9911.class);

	/* DB服務注入 */
	@Autowired
	MonthlyLM052LossService sMonthlyLM052LossService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		logger.info("active L9911 ");
		this.totaVo.init(titaVo);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 85 * 100=

		int startMonth = Integer.parseInt(titaVo.getParam("StartMonth")) + 191100;
		int endMonth = Integer.parseInt(titaVo.getParam("EndMonth")) + 191100;

		Slice<MonthlyLM052Loss> resultList = null;

		resultList = sMonthlyLM052LossService.findYearMonth(startMonth, endMonth, index, limit, titaVo);
		
		if (resultList != null && !resultList.isEmpty()) {

			for (MonthlyLM052Loss result : resultList.toList()) {
				OccursList occursList = new OccursList();

				occursList.putParam("OOYearMonth", result.getYearMonth() - 191100);
				occursList.putParam("OOAssetEvaTotal", result.getAssetEvaTotal());
				occursList.putParam("OOLegalLoss", result.getLegalLoss());
				occursList.putParam("OOApprovedLoss", result.getApprovedLoss());
				occursList.putParam("OOLastUpdate", parse.timeStampToString(result.getLastUpdate()));
				occursList.putParam("OOLastUpdateEmpNo", result.getLastUpdateEmpNo());

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			if (resultList.getSize() >= this.limit) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}
		} else {
			throw new LogicException(titaVo, "E0001", "備抵損失資料檔"); // 查無資料
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}