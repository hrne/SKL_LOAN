package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.service.CdBonusCoService;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.db.service.springjpa.cm.L6087ServiceImpl;
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
	
	@Autowired
	public L6087ServiceImpl iL6087ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6087 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 40;
		
		int iWorkMonth = Integer.valueOf(titaVo.getParam("WorkMonth"))+191100;
		int iEntdy = titaVo.getEntDyI() + 19110000;
		int nWorkMonth = 0; // 當前工作月
		int maxWorkMonth = 0; //生效中工作月(CdBonusCo中<=當前工作月中之最大值)
		List<Map<String, String>> wCdBonusCo = new ArrayList<Map<String, String>>();
		List<Map<String, String>> aCdBonusCo = new ArrayList<Map<String, String>>();
		//查詢當下最大工作月(生效工作月)-Start
		// 取當前工作月
		CdWorkMonth iCdWorkMonth = new CdWorkMonth();
		iCdWorkMonth = iCdWorkMonthService.findDateFirst(iEntdy, iEntdy, titaVo);
		if (iCdWorkMonth == null) {
			throw new LogicException(titaVo, "E0001", "查無工作月資料"); // 查無資料
		}
		String iYear = StringUtils.leftPad(String.valueOf(iCdWorkMonth.getYear() - 1911), 3, "0");
		String iMonth = StringUtils.leftPad(String.valueOf(iCdWorkMonth.getMonth()), 2, "0");
		nWorkMonth = Integer.valueOf(iYear + iMonth)+191100;
		this.info("今工作月==="+nWorkMonth);
		//查詢當下最大工作月(生效工作月)-End
		try {
			wCdBonusCo = iL6087ServiceImpl.findAllData(Integer.valueOf(nWorkMonth), titaVo);
		} catch (Exception e) {
			throw new LogicException(titaVo, "E5004", "");
		}
		if (wCdBonusCo == null) {
			throw new LogicException(titaVo, "E0001", "查無對應工作月");
		}
		for (Map<String, String> wwCdBonusCo : wCdBonusCo) {
			if (wwCdBonusCo.get("Sort").equals("1")) {
				maxWorkMonth = Integer.valueOf(wwCdBonusCo.get("WorkMonth"));
			}
		}
		
		this.info("生效中工作月="+maxWorkMonth);
		
		try {
			aCdBonusCo = iL6087ServiceImpl.findDistinct(titaVo);
		} catch (Exception e) {
			throw new LogicException(titaVo, "E5004", "");
		}
		if (aCdBonusCo==null) {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}
		
		for (Map<String, String> aaCdBonusCo : aCdBonusCo) {
			OccursList occursList = new OccursList();
			int reWorkMonth = Integer.valueOf(aaCdBonusCo.get("WorkMonth"));
			if (reWorkMonth != iWorkMonth && iWorkMonth != 191100) {
				continue;
			}
			occursList.putParam("OOWorkMonth", reWorkMonth-191100);
			if (reWorkMonth<maxWorkMonth) {
				//開啟按鈕 0:關閉 ;1:開啟
				occursList.putParam("OOUpdateBtn", 0);
				occursList.putParam("OODelBtn", 0);
			}else if (reWorkMonth==maxWorkMonth) {
				//開啟按鈕 0:關閉 ;1:開啟
				occursList.putParam("OOUpdateBtn", 1);
				occursList.putParam("OODelBtn", 0);
			}else if (reWorkMonth>maxWorkMonth) {
				//開啟按鈕 0:關閉 ;1:開啟
				occursList.putParam("OOUpdateBtn", 1);
				occursList.putParam("OODelBtn", 1);
			}
			this.totaVo.addOccursList(occursList);
		}
		
		if (this.totaVo.getOccursList().size() == 0) {
			throw new LogicException(titaVo, "E0001", "該年月查無資料"); // 查無資料
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}