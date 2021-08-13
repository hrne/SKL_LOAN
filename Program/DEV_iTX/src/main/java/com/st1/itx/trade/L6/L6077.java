package com.st1.itx.trade.L6;

import java.math.BigDecimal;
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
import com.st1.itx.db.domain.CdCashFlow;
import com.st1.itx.db.service.CdCashFlowService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita DataYearMonth=9,5 END=X,1
 */

@Service("L6077") // 現金流量預估資料檔查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6077 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6077.class);

	/* DB服務注入 */
	@Autowired
	public CdCashFlowService sCdCashFlowService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6077 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int Month = 0;
		int iDataYearMonth = this.parse.stringToInteger(titaVo.getParam("DataYearMonth"));
		this.info("L6077 1 iDataYearMonth : " + iDataYearMonth);
		Month = this.parse.stringToInteger(titaVo.getParam("DataYearMonth").substring(3,5));
		this.info("L6077 1 Month : " + Month);
		
		int iYearMonth = iDataYearMonth + 191100;
		this.info("L6077 1 iYearMonth : " + iYearMonth);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 37 * 200 = 7400

		// 查詢現金流量預估資料檔
		Slice<CdCashFlow> slCdCashFlow;
		if (iDataYearMonth == 0) {
			slCdCashFlow = sCdCashFlowService.findAll(this.index, this.limit, titaVo);
		} else if(Month == 0){
			//查詢該年度所有月份
			Month = iYearMonth+12;
			slCdCashFlow = sCdCashFlowService.findDataYearMonth(iYearMonth, Month, this.index, this.limit, titaVo);
		} else {
			slCdCashFlow = sCdCashFlowService.findDataYearMonth(iYearMonth, iYearMonth, this.index, this.limit, titaVo);
		}
		List<CdCashFlow> lCdCashFlow = slCdCashFlow == null ? null : slCdCashFlow.getContent();

		if (lCdCashFlow == null || lCdCashFlow.size() == 0) {
			throw new LogicException(titaVo, "E0001", "現金流量預估資料檔"); // 查無資料
		}
		// 如有找到資料
		for (CdCashFlow tCdCashFlow : lCdCashFlow) {
			OccursList occursList = new OccursList();

			int wkYearMonth = tCdCashFlow.getDataYearMonth();
			this.info("L6077 1 tCdCashFlow.getDataYearMonth : " + wkYearMonth);
			wkYearMonth = wkYearMonth - 191100;
			occursList.putParam("OODataYearMonth", wkYearMonth);

			BigDecimal wkIncome = new BigDecimal(0);
			wkIncome = wkIncome.add(tCdCashFlow.getInterestIncome()).add(tCdCashFlow.getPrincipalAmortizeAmt()).add(tCdCashFlow.getPrepaymentAmt()).add(tCdCashFlow.getDuePaymentAmt());
			occursList.putParam("OOIncome", wkIncome);

			BigDecimal wkExpend = new BigDecimal(0);
			wkExpend = wkExpend.add(tCdCashFlow.getExtendAmt()).add(tCdCashFlow.getLoanAmt());
			occursList.putParam("OOExpend", wkExpend);

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdCashFlow != null && slCdCashFlow.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}