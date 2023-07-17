package com.st1.itx.trade.LC;

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
import com.st1.itx.db.service.springjpa.cm.LC004ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;

@Service("LC004")
@Scope("prototype")
/**
 * 審核查詢
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class LC004 extends TradeBuffer {

	@Autowired
	LC004ServiceImpl lc004ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC004 ");
		this.totaVo.init(titaVo);

		/*  設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = titaVo.getReturnIndex();
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 40;

		List<Map<String, String>> lc004List = new ArrayList<Map<String, String>>();
		try {
			lc004List = lc004ServiceImpl.findAll(titaVo, index, limit);
		} catch (Exception e) {
			this.info("Error ... " + e.getMessage());
		}

		if (lc004List != null && lc004List.size() != 0) {
			for (Map<String, String> t : lc004List) {
				OccursList occursList = new OccursList();
				occursList.putParam("CalDate", t.get("CalDate"));
				occursList.putParam("CalTime", t.get("CalTime"));
				occursList.putParam("Entdy", t.get("Entdy"));
				occursList.putParam("TxNo", t.get("TxNo"));
				occursList.putParam("TranNo", t.get("TranNo") + " " + t.get("TranNoX"));
				occursList.putParam("MrKey", t.get("MrKey"));
				occursList.putParam("CurName", t.get("CurName"));
				occursList.putParam("TxAmt", t.get("TxAmt"));
				occursList.putParam("BrNo", t.get("BrNo") + " " + t.get("BrNoX"));
				occursList.putParam("TlrNo", t.get("TlrNo") + " " + t.get("TlrNoX"));
				occursList.putParam("FlowType", t.get("FlowType"));
				occursList.putParam("FlowStep", t.get("FlowStep"));
				occursList.putParam("iCode", t.get("iCode"));
				occursList.putParam("FileNm", t.get("FileNm"));
				occursList.putParam("iItem", t.get("iItem"));
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (lc004List != null && lc004List.size() >= this.limit) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}