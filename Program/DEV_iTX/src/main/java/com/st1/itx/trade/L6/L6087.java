package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBonusCo;
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.service.CdBonusCoService;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L6087")
@Scope("prototype")
public class L6087 extends TradeBuffer {

	/* DB服務注入 */
	/* 轉換工具 */
	@Autowired
	public CdBonusCoService iCdBonusCoService;
	
	@Autowired
	public CdWorkMonthService iCdWorkMonthService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6087 ");
		this.totaVo.init(titaVo);

		int iWorkMonth = Integer.valueOf(titaVo.getParam("WorkMonth")) + 191100;
		int iEntdy = titaVo.getEntDyI()+19110000;
		int nWorkMonth = 0; //當前工作月
		Slice<CdBonusCo> tCdBonusCo = null;
		ArrayList<Integer> workMonth_list = new ArrayList<>();
		if (Integer.valueOf(titaVo.getParam("WorkMonth")) == 0) {
			tCdBonusCo = iCdBonusCoService.findAll(this.index, this.limit, titaVo);
		} else {
			tCdBonusCo = iCdBonusCoService.findYearMonth(iWorkMonth, iWorkMonth, this.index, this.limit, titaVo);
		}
		if (tCdBonusCo == null) {
			throw new LogicException(titaVo, "E0001", "查無資料"); // 查無資料
		}
		
		//取當前工作月
		CdWorkMonth iCdWorkMonth = new CdWorkMonth();
		iCdWorkMonth = iCdWorkMonthService.findDateFirst(iEntdy, iEntdy, titaVo);
		if (iCdWorkMonth == null) {
			throw new LogicException(titaVo, "E0001", "查無工作月資料"); // 查無資料
		}
		String iYear = StringUtils.leftPad(String.valueOf(iCdWorkMonth.getYear()-1911), 3,"0");
		String iMonth = StringUtils.leftPad(String.valueOf(iCdWorkMonth.getMonth()), 2,"0");
		nWorkMonth = Integer.valueOf(iYear+iMonth);
		
		
		for (CdBonusCo iCdBonusCo : tCdBonusCo) {
			int cWorkMonth = iCdBonusCo.getWorkMonth();
			if (workMonth_list.contains(cWorkMonth)) {
				continue;
			} else {
				workMonth_list.add(0, cWorkMonth);
			}
		}
//		for (int reWorkMonth : workMonth_list) {	
		for (int i = 0 ; i<workMonth_list.size();i++) {
			OccursList occursList = new OccursList();
			int reWorkMonth = workMonth_list.get(i);
			if (i+1 < workMonth_list.size()) {
				occursList.putParam("OOWorkMonth", reWorkMonth - 191100);
				if (reWorkMonth-191100 > nWorkMonth) {
					occursList.putParam("OOBtnFg", 1); //開啟修改、刪除按鈕
				}else if (reWorkMonth-191100 == nWorkMonth){
					occursList.putParam("OOBtnFg", 2); //開啟修改、關閉刪除按鈕
				}else if (reWorkMonth-191100 < nWorkMonth && workMonth_list.get(i+1) > nWorkMonth || workMonth_list.get(i+1) == nWorkMonth) {
					occursList.putParam("OOBtnFg", 2); //開啟修改、關閉刪除按鈕
				}else {
					occursList.putParam("OOBtnFg", 0); //關閉修改、刪除按鈕
				}
			}else {
				occursList.putParam("OOWorkMonth", reWorkMonth - 191100);
				if (reWorkMonth-191100 > nWorkMonth) {
					occursList.putParam("OOBtnFg", 1); //開啟修改、刪除按鈕
				}else if (reWorkMonth-191100 == nWorkMonth){
					occursList.putParam("OOBtnFg", 2); //開啟修改、關閉刪除按鈕
				}else {
					occursList.putParam("OOBtnFg", 0); //關閉修改、刪除按鈕
				}
			}
			this.totaVo.addOccursList(occursList);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}