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
import com.st1.itx.db.service.springjpa.cm.L5913ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;


@Service("L5913")
@Scope("prototype")
/**
 * 房貸專員績效津貼計算
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5913 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L5913.class);

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;
	
	@Autowired
	public L5913ServiceImpl iL5913ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5913 ");
		this.totaVo.init(titaVo);
		int iYyyMm = Integer.valueOf(titaVo.getParam("YyyMm"))+191100;
		List<Map<String, String>> i5913SqlReturn = new ArrayList<Map<String,String>>();
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;
		
		try {
			i5913SqlReturn = iL5913ServiceImpl.FindData(iYyyMm,titaVo);
		}catch (Exception e) {
			//E5004 讀取DB語法發生問題
			this.info("L5913 ErrorForSql="+e);
			throw new LogicException(titaVo, "E5004","");
		}
		this.info("db return = "+i5913SqlReturn.toString());
		if(i5913SqlReturn.isEmpty()) {
			throw new LogicException(titaVo, "E0001","年月份: "+titaVo.getParam("YyyMm")+" 查無資料");
		}else {
			for (Map<String, String> r5913SqlReturn:i5913SqlReturn) {
				OccursList occursList = new OccursList();			
				occursList.putParam("OOEmpNo", r5913SqlReturn.get("F0"));
				occursList.putParam("OOFullName", r5913SqlReturn.get("F1"));
				occursList.putParam("OODistItem", r5913SqlReturn.get("F2"));
				occursList.putParam("OODeptItem", r5913SqlReturn.get("F3"));
				occursList.putParam("OOGoalAmt", r5913SqlReturn.get("F4")); //責任額
				occursList.putParam("OOPerfCnt", r5913SqlReturn.get("F6"));
				occursList.putParam("OOTotalDrawdownAmt", r5913SqlReturn.get("F5")); //房貸撥款金額
				occursList.putParam("OOCount", r5913SqlReturn.get("F7"));
				this.totaVo.addOccursList(occursList);
			}
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}