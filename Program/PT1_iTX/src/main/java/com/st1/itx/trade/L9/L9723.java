package com.st1.itx.trade.L9;

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
import com.st1.itx.db.service.springjpa.cm.L9723ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;

@Service("L9723")
@Scope("prototype")
/**
 * 
 * 
 * @author Xiang Wei Huang
 * @version 1.0.0
 */
public class L9723 extends TradeBuffer {
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	L9723ServiceImpl iL9723;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9723 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();
		// new OccursList
		OccursList OccursList = new OccursList();
		List<Map<String, String>> sL9723 = new ArrayList<Map<String, String>>();
		try {
			sL9723 = iL9723.FindData(titaVo);
			if(sL9723 == null ) {
				throw new LogicException(titaVo, "E2003", "SQL語句有誤");
			}
		} catch (Exception e) {
			this.info("sL9723 have Date ===");
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}
		for(Map<String, String> isL9732 : sL9723) {
			String icount = isL9732.get("F0");
			String ixdate = isL9732.get("F1");
			OccursList = new OccursList();	
			OccursList.putParam("OOReportDate", ixdate);
			OccursList.putParam("OOReportTotal", icount);
			this.totaVo.addOccursList(OccursList);
		}
	this.addList(this.totaVo);
	return this.sendList();
	}
}