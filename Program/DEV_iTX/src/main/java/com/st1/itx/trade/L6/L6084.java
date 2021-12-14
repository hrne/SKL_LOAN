package com.st1.itx.trade.L6;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdPerformance;
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.service.CdPerformanceService;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita PieceCode=X,1 END=X,1
 */

@Service("L6084") // 業績件數及金額核算標準設定檔查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6084 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdPerformanceService sCdPerformanceService;
	
	@Autowired
	public CdWorkMonthService iCdWorkMonthService;
	
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6084 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iWorkMonth = Integer.valueOf(titaVo.getParam("WorkMonth")) + 191100;
		int AcDate = titaVo.getEntDyI() + 19110000;
		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 168 * 200 = 33,600

		String tWorkMonthAcDate = "";
		CdWorkMonth iCdWorkMonth = iCdWorkMonthService.findDateFirst(AcDate, AcDate, titaVo);
		if (iCdWorkMonth != null) {
			tWorkMonthAcDate = String.valueOf(iCdWorkMonth.getYear()) + parse.IntegerToString(iCdWorkMonth.getMonth(), 2);
			this.info("tWorkMonthAcDate==" + tWorkMonthAcDate);
		}
		
		int tPerformanceAcDate = 0;
		// 找生效中工作月
		if (!("").equals(tWorkMonthAcDate)) {
			CdPerformance iCdPerformance = sCdPerformanceService.findWorkMonthFirst(Integer.parseInt(tWorkMonthAcDate), titaVo);
			if (iCdPerformance != null) {
				tPerformanceAcDate = iCdPerformance.getWorkMonth();
				this.info("tBonusAcDate==" + tPerformanceAcDate);
			}
		}
		
		// 查詢業績件數及金額核算標準設定檔
		Slice<CdPerformance> slCdPerformance;
		if (iWorkMonth == 191100) {
			slCdPerformance = sCdPerformanceService.findAll(this.index, this.limit, titaVo);
		} else {
			slCdPerformance = sCdPerformanceService.findWorkMonth(iWorkMonth, this.index, this.limit, titaVo);
		}

		if (slCdPerformance == null) {
			throw new LogicException(titaVo, "E0001", "業績件數及金額核算標準設定檔"); // 查無資料
		}
		// 如有找到資料
		ArrayList<Integer> iWorkMonthList = new ArrayList<>();
		for (CdPerformance tCdPerformance : slCdPerformance) {

			if (iWorkMonthList.contains(tCdPerformance.getWorkMonth())) {
				continue;
			} else {
				/* 將每筆資料放入Tota的OcList */
				
				iWorkMonthList.add(0, tCdPerformance.getWorkMonth());
			}
		}
		for (int reWorkMonth : iWorkMonthList) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOWorkMonth", reWorkMonth - 191100);
			this.info("reWorkMonth="+reWorkMonth);
			this.info("tPerformanceAcDate="+tPerformanceAcDate);
			if (reWorkMonth > tPerformanceAcDate) {// 0:未生效
				occursList.putParam("OOFlag", 0);
			} else if (reWorkMonth == tPerformanceAcDate) {// 1:生效中
				occursList.putParam("OOFlag", 1);
			} else {
				occursList.putParam("OOFlag", 2);// 2:已失效
			}
			
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdPerformance != null && slCdPerformance.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}