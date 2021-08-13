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
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L5R33ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L5R33")
@Scope("prototype")
/**
 * 抓戶名+流水號
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5R33 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5R33.class);

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public L5R33ServiceImpl l5R33ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		this.info("active L5R33 ");
		this.totaVo.init(titaVo);
		int iCustNo = Integer.valueOf(titaVo.getParam("RimCustNo"));

		List<Map<String, String>> lL5R33List = new ArrayList<Map<String, String>>();

		// 取申請序號和客戶名稱
		try {
			lL5R33List = l5R33ServiceImpl.getSeq(iCustNo, titaVo);
		} catch (Exception e) {
			// E5004 讀取DB語法發生問題
			this.info("L5R33 ErrorForSql=" + e);
			throw new LogicException(titaVo, "E5004", "");
		}
		
		this.info("db return = " + lL5R33List.toString());

		if (lL5R33List.size() == 0) {
			throw new LogicException(titaVo, "E0001", "客戶檔無戶號:" + iCustNo + "資料");
		} else {
			Map<String, String> tL5R33Map = lL5R33List.get(0);
			
			totaVo.putParam("L5R33CustName", tL5R33Map.get("F0"));
			if ("".equals(tL5R33Map.get("F1"))) {
				totaVo.putParam("L5R33ApplSeq", 1);
			} else {
				totaVo.putParam("L5R33ApplSeq", parse.stringToInteger(tL5R33Map.get("F1")) + 1);
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}