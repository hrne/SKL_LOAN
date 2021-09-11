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
import com.st1.itx.db.domain.CdBonus;
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.service.CdBonusService;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita Year=9,3 Month=9,2 END=X,1
 */

@Service("L6081") // 介紹人加碼獎勵津貼標準設定檔查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6081 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdBonusService sCdBonusService;
	
	@Autowired
	public CdWorkMonthService iCdWorkMonthService;
	
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6081 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iWorkMonth = Integer.valueOf(titaVo.getParam("WorkMonth")) + 191100;
		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 53 * 200 = 10600
		
		int AcDate = titaVo.getEntDyI()+19110000;
		String tWorkMonthAcDate = "";
		CdWorkMonth iCdWorkMonth = iCdWorkMonthService.findDateFirst(AcDate, AcDate, titaVo);
		if(iCdWorkMonth!=null) {
			tWorkMonthAcDate = String.valueOf(iCdWorkMonth.getYear())+parse.IntegerToString(iCdWorkMonth.getMonth(), 2);
			this.info("tWorkMonthAcDate=="+tWorkMonthAcDate);
		}
		
		

		// 查詢介紹人加碼獎勵津貼標準設定檔
		Slice<CdBonus> slCdBonus;
		if (iWorkMonth == 191100) {
			// 查全部
			slCdBonus = sCdBonusService.findAll(this.index, this.limit, titaVo);
		} else {
			slCdBonus = sCdBonusService.findYearMonth(iWorkMonth, iWorkMonth, this.index, this.limit, titaVo);
		}

		if (slCdBonus == null) {
			throw new LogicException(titaVo, "E0001", "介紹人加碼獎勵津貼標準設定檔"); // 查無資料
		}
		ArrayList<Integer> workMonthList = new ArrayList<>();
		// 如有找到資料，工作月篩選
		for (CdBonus tCdBonus : slCdBonus) {
			if (workMonthList.contains(tCdBonus.getWorkMonth())) {
				continue;
			} else {
				workMonthList.add(0, tCdBonus.getWorkMonth());
			}
		}
		
		int tBonusAcDate =0;
		//找生效中工作月	
		if(!("").equals(tWorkMonthAcDate)) {
			CdBonus iCdBonus  = sCdBonusService.findWorkMonthFirst(Integer.parseInt(tWorkMonthAcDate), titaVo);
			if(iCdBonus!=null) {
				tBonusAcDate = iCdBonus.getWorkMonth();
				this.info("tBonusAcDate=="+tBonusAcDate);
			}
		}
		
		
		for (int reWorkMonth : workMonthList) {
			OccursList occursList = new OccursList();
			this.info("reWorkMonth=="+reWorkMonth);
			if(reWorkMonth > tBonusAcDate) {//0:未生效
				occursList.putParam("OOFlag", 0);
			} else if(reWorkMonth == tBonusAcDate) {//1:生效中
				occursList.putParam("OOFlag", 1);
			} else {
				occursList.putParam("OOFlag", 2);//2:已失效
			}
			
			
			occursList.putParam("OOWorkMonth", reWorkMonth - 191100);
			this.totaVo.addOccursList(occursList);
		}
		/* 將每筆資料放入Tota的OcList */

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdBonus != null && slCdBonus.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}