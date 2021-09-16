package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;

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
import com.st1.itx.db.domain.YearlyHouseLoanInt;
import com.st1.itx.db.service.YearlyHouseLoanIntService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * ClCode1=9,1<br>
 * ClCode2=9,2<br>
 * END=X,1<br>
 */

@Service("L5982") // 國稅局申報檔查詢
@Scope("prototype")
/**
 * 
 * 
 * @author ZhiCheng
 * @version 1.0.0
 */

public class L5982 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5982.class);

	/* DB服務注入 */
	@Autowired
	public YearlyHouseLoanIntService sYearlyHouseLoanIntService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		logger.info("active L5982 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iYearMonth = this.parse.stringToInteger(titaVo.getParam("YearMonth"));
		int iCustNo=this.parse.stringToInteger(titaVo.getParam("CustNo"));
		
		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 45 * 200 = 9000

		// 查詢擔保品代號資料檔
		Slice<YearlyHouseLoanInt> sYearlyHouseLoanInt;
		if (iYearMonth!=0 && iCustNo!=0) {
			sYearlyHouseLoanInt = sYearlyHouseLoanIntService.findYearCustNo(iYearMonth+191100,iCustNo, this.index, this.limit, titaVo);
		} else if(iYearMonth!=0){
			sYearlyHouseLoanInt = sYearlyHouseLoanIntService.findYearMonth(iYearMonth+191100, this.index, this.limit, titaVo);
		}else if(iCustNo != 0){
			sYearlyHouseLoanInt = sYearlyHouseLoanIntService.findCustNo(iCustNo, this.index, this.limit, titaVo);
		} else {		
			sYearlyHouseLoanInt = sYearlyHouseLoanIntService.findAll(this.index, this.limit, titaVo);
		}
		List<YearlyHouseLoanInt> lYearlyHouseLoanInt = sYearlyHouseLoanInt == null ? null : sYearlyHouseLoanInt.getContent();

		if (lYearlyHouseLoanInt == null || lYearlyHouseLoanInt.size() == 0) {
			throw new LogicException(titaVo, "E0001", "每年房屋擔保借款繳息工作檔"); // 查無資料
		}
		// 如有找到資料
		for (YearlyHouseLoanInt tYearlyHouseLoanInt : lYearlyHouseLoanInt) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOYearMonth", tYearlyHouseLoanInt.getYearMonth()-191100);
			occursList.putParam("OOCustNo", tYearlyHouseLoanInt.getCustNo());
			occursList.putParam("OOFacmNo", tYearlyHouseLoanInt.getFacmNo());
			occursList.putParam("OOUsageCode", tYearlyHouseLoanInt.getUsageCode());
			occursList.putParam("OOAcctCode", tYearlyHouseLoanInt.getAcctCode());
			occursList.putParam("OORepayCode", tYearlyHouseLoanInt.getRepayCode());
			occursList.putParam("OOLoanAmt", tYearlyHouseLoanInt.getLoanAmt());
			occursList.putParam("OOLoanBal", tYearlyHouseLoanInt.getLoanBal());
			occursList.putParam("OOFirstDrawdownDate", tYearlyHouseLoanInt.getFirstDrawdownDate());
			occursList.putParam("OOMaturityDate", tYearlyHouseLoanInt.getMaturityDate());
			occursList.putParam("OOYearlyInt", tYearlyHouseLoanInt.getYearlyInt());
			occursList.putParam("OOHouseBuyDate", tYearlyHouseLoanInt.getHouseBuyDate());
			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (sYearlyHouseLoanInt != null && sYearlyHouseLoanInt.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}