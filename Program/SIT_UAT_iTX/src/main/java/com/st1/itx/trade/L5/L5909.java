package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L5909ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;


@Service("L5909")
@Scope("prototype")
/**
 * 案件品質排行表(列印) 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5909 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5909.class);

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public L5909ServiceImpl	iL5909ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5909 ");
		this.totaVo.init(titaVo);
		//撈期間內所有資料，並統計一個專員所有的金額，以擔保品戶號+額度進colllist找是否有逾期>=4，再將逾期比數金額相加為逾期金額，再以高排到低
		String iEndDate = titaVo.getParam("EndDate"); //截止日期
		String iStartDate = titaVo.getParam("StartDate"); //起始日期
		int iiEndDate = Integer.valueOf(iEndDate)+19110000;
		int iiStartDate = Integer.valueOf(iStartDate)+19110000;
		List<Map<String, String>> iL5909SqlReturn = new ArrayList<Map<String,String>>();
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;
		
		try {
			iL5909SqlReturn = iL5909ServiceImpl.FindData(this.index,this.limit,iiStartDate,iiEndDate,titaVo);
		}catch (Exception e) {
			//E5004 讀取DB語法發生問題
			this.info("L5909 ErrorForSql="+e);
			throw new LogicException(titaVo, "E5004","");
		}
		if(iL5909SqlReturn.isEmpty()) {
			throw new LogicException(titaVo, "E0001",iStartDate+"到"+iEndDate+"期間內查無資料");
		}else {
			
			if(iL5909SqlReturn!=null && iL5909SqlReturn.size()>=this.limit) {
				/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
				titaVo.setReturnIndex(this.setIndexNext());
				//this.totaVo.setMsgEndToAuto();// 自動折返
				this.totaVo.setMsgEndToEnter();// 手動折返
			}
			
			for (Map<String, String> r5909SqlReturn:iL5909SqlReturn) {
				OccursList occursList = new OccursList();
				if(r5909SqlReturn.get("F0").equals("")) {
					continue;
				}
				occursList.putParam("OOBsOfficerX", r5909SqlReturn.get("F0"));
				occursList.putParam("OOTotal", r5909SqlReturn.get("F1"));
				if (r5909SqlReturn.get("F2").equals("")) {
					occursList.putParam("OOOvduTotal", 0);	
				}else {
					occursList.putParam("OOOvduTotal", r5909SqlReturn.get("F2"));
				}
				occursList.putParam("OOPercent",r5909SqlReturn.get("F3"));
				this.totaVo.addOccursList(occursList);
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}