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
import com.st1.itx.db.service.springjpa.cm.L5912ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L5912")
@Scope("prototype")
/**
 * 新光銀銀扣案件資料產生
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5912 extends TradeBuffer {
	@Autowired
	public L5912ServiceImpl iL5912ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 40;

		this.info("active L5912 ");
		this.totaVo.init(titaVo);
		String sDrawDownDateFm = titaVo.getParam("DrawDownDateFm");
		String sDrawDownDateTo = titaVo.getParam("DrawDownDateTo");
		int iDrawDownDateFm = Integer.valueOf(sDrawDownDateFm) + 19110000;
		int iDrawDownDateTo = Integer.valueOf(sDrawDownDateTo) + 19110000;

		List<Map<String, String>> iL5912SqlReturn = new ArrayList<Map<String, String>>();
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		try {
			iL5912SqlReturn = iL5912ServiceImpl.FindData(this.index, this.limit, iDrawDownDateFm, iDrawDownDateTo, titaVo);
		} catch (Exception e) {
			// E5004 讀取DB語法發生問題
			this.info("L5912 ErrorForSql=" + e);
			throw new LogicException(titaVo, "E5004", "");
		}
		if (iL5912SqlReturn.isEmpty()) {
			throw new LogicException(titaVo, "E0001", "撥款日期" + sDrawDownDateFm + "到" + sDrawDownDateTo + "查無資料");
		}
		if (iL5912SqlReturn != null && iL5912SqlReturn.size() >= this.limit) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(this.setIndexNext());
			// this.totaVo.setMsgEndToAuto();// 自動折返
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		for (Map<String, String> r5912SqlReturn : iL5912SqlReturn) {
			OccursList occursList = new OccursList();
			if (r5912SqlReturn.get("F2").equals("")) {
				continue;
			}
			occursList.putParam("OODeptItem", r5912SqlReturn.get("F0")); // 部室
			occursList.putParam("OODistItem", r5912SqlReturn.get("F1")); // 經辦區部
			occursList.putParam("OOFullname", r5912SqlReturn.get("F2")); // 姓名
			occursList.putParam("OODetailTotal", r5912SqlReturn.get("F3")); // 新貸件數
			occursList.putParam("OOFacMainTotal", r5912SqlReturn.get("F4")); // 新光銀行扣款件數
			occursList.putParam("OOCount", r5912SqlReturn.get("F5")); // 占率
			this.totaVo.addOccursList(occursList);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}