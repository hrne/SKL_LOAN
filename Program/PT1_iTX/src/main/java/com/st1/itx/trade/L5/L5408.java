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
import com.st1.itx.db.service.springjpa.cm.L5408ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L5408")
@Scope("prototype")
/**
 * 房貸專員撥款筆數統計表
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5408 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L5408.class);

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public L5408ServiceImpl iL5408ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5408 ");
		this.totaVo.init(titaVo);
		// 處理tita日期為西元並加上日期
		String iYyyMmFm = titaVo.getParam("YyyMmFm");
		int xYyyMmFm = Integer.valueOf(iYyyMmFm) + 191100;
		String iYyyMmTo = titaVo.getParam("YyyMmTo");
		int xYyyMmTo = Integer.valueOf(iYyyMmTo) + 191100;
		List<Map<String, String>> iL5408SqlReturn = new ArrayList<Map<String, String>>();

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		try {
			iL5408SqlReturn = iL5408ServiceImpl.FindData(xYyyMmFm, xYyyMmTo, titaVo);
		} catch (Exception e) {
			// E5004 讀取DB語法發生問題
			this.info("L5408 ErrorForSql=" + e);
			throw new LogicException(titaVo, "E5004", "");
		}
		for (Map<String, String> r5408SqlReturn : iL5408SqlReturn) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOPerfDate", Integer.valueOf(r5408SqlReturn.get("F0")) - 191100);
			occursList.putParam("OODeptCode", r5408SqlReturn.get("F4"));
			occursList.putParam("OODeptCodeX", r5408SqlReturn.get("F5"));
			occursList.putParam("OOBsOfficer", r5408SqlReturn.get("F1"));
			occursList.putParam("OOBsOfficerX", r5408SqlReturn.get("F3"));
			occursList.putParam("OOTotal", r5408SqlReturn.get("F2"));
			this.totaVo.addOccursList(occursList);
		}

		this.addList(this.totaVo);
		{
			return this.sendList();
		}
	}
}
