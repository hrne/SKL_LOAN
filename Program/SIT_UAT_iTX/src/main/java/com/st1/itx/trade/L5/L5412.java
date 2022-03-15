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
import com.st1.itx.db.service.springjpa.cm.L5412ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L5412")
@Scope("prototype")
/**
 * 新光銀銀扣案件資料產生
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5412 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L5412.class);

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public L5412ServiceImpl iL5412ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5412 ");
		this.totaVo.init(titaVo);
		int iDrawDownDateFm = Integer.valueOf(titaVo.getParam("DrawDownDateFm")) + 19110000;
		int iDrawDownDateTo = Integer.valueOf(titaVo.getParam("DrawDownDateTo")) + 19110000;
		List<Map<String, String>> iL5412SqlReturn = new ArrayList<Map<String, String>>();
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		try {
			iL5412SqlReturn = iL5412ServiceImpl.FindData(iDrawDownDateFm, iDrawDownDateTo, titaVo);
		} catch (Exception e) {
			// E5004 讀取DB語法發生問題
			this.info("L5412 ErrorForSql=" + e);
			throw new LogicException(titaVo, "E5004", "");
		}
		if (iL5412SqlReturn != null) {
			for (Map<String, String> r5412SqlReturn : iL5412SqlReturn) {
				OccursList occursList = new OccursList();
				if (r5412SqlReturn.get("F2").equals("")) {
					continue;
				}
				occursList.putParam("OODeptItem", r5412SqlReturn.get("F0")); // 部室
				occursList.putParam("OODistItem", r5412SqlReturn.get("F1")); // 經辦區部
				occursList.putParam("OOFullname", r5412SqlReturn.get("F2")); // 姓名
				occursList.putParam("OODetailTotal", r5412SqlReturn.get("F3")); // 新貸件數
				occursList.putParam("OOFacMainTotal", r5412SqlReturn.get("F4")); // 新光銀行扣款件數
				occursList.putParam("OOCount", r5412SqlReturn.get("F5")); // 占率
				this.totaVo.addOccursList(occursList);
			}
		} else {
			throw new LogicException(titaVo, "E0001", "");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}