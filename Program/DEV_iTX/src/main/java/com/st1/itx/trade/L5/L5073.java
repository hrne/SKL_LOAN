package com.st1.itx.trade.L5;

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
import com.st1.itx.db.domain.NegAppr;
import com.st1.itx.db.service.NegApprService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * SupvReasonCode=X,4<br>
 * END=X,1<br>
 */

@Service("L5073") // 主管理由檔查詢
@Scope("prototype")
/**
 * 
 * 
 * @author Chih-Cheng
 * @version 1.0.0
 */

public class L5073 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public NegApprService sNegApprService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5073 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iYear = titaVo.getParam("Year");
		int iMonth = Integer.parseInt(titaVo.getParam("Month"));
		int YearMonth = Integer.parseInt(titaVo.getParam("Year") + titaVo.getParam("Month")) + 191100;

		int YMStart = 0;
		int YMEnd = 0;

		int YYYY = 0;
		if (iYear != null && iYear.length() != 0) {
			if (iYear.length() == 3) {
				// 民國年
				YYYY = Integer.parseInt(iYear) + 1911;
			}
		}
		YMStart = YYYY * 100 + 1;
		YMEnd = YYYY * 100 + 12;

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 85 * 200 = 17,000

		Slice<NegAppr> sNegAppr;
		if (iMonth == 0) {
			sNegAppr = sNegApprService.yyyyMmBetween(YMStart, YMEnd, this.index, this.limit, titaVo);
		} else {
			sNegAppr = sNegApprService.yyyyMmEq(YearMonth, this.index, this.limit, titaVo);
		}
		List<NegAppr> lNegAppr = sNegAppr == null ? null : sNegAppr.getContent();

		if (lNegAppr == null || lNegAppr.size() == 0) {
			throw new LogicException(titaVo, "E0001", "撥付日期設定檔"); // 查無資料
		}
		// 如有找到資料
		for (NegAppr tNegAppr : lNegAppr) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOYyyyMm", tNegAppr.getYyyyMm() - 191100);
			occursList.putParam("OOKindCode", tNegAppr.getKindCode());
			occursList.putParam("OOExportDate", tNegAppr.getExportDate());
			occursList.putParam("OOApprAcDate", tNegAppr.getApprAcDate());
			occursList.putParam("OOBringUpDate", tNegAppr.getBringUpDate());
			occursList.putParam("OOExportMark", tNegAppr.getExportMark());
			occursList.putParam("OOApprAcMark", tNegAppr.getApprAcMark());
			occursList.putParam("OOBringUpMark", tNegAppr.getBringUpMark());

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (sNegAppr != null && sNegAppr.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}