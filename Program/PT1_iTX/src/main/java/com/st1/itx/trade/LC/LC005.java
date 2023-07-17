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
import com.st1.itx.db.service.springjpa.cm.LC005ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;

@Service("LC005")
@Scope("prototype")
/**
 * 登錄提交資料查詢
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class LC005 extends TradeBuffer {

	@Autowired
	LC005ServiceImpl lc005ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC005 ");
		this.totaVo.init(titaVo);
		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = titaVo.getReturnIndex();
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 40;

		List<Map<String, String>> lc005List = new ArrayList<Map<String, String>>();
		try {
			lc005List = lc005ServiceImpl.findAll(titaVo, index, limit);
		} catch (Exception e) {
			this.info("Error ... " + e.getMessage());
		}

		if (lc005List != null && lc005List.size() != 0) {
			for (Map<String, String> t : lc005List) {
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
				occursList.putParam("iItem", t.get("iItem"));
				if (!"".equals(t.get("RejectReason").trim())) {
					occursList.putParam("RejectReason", "退回原因:" + t.get("RejectReason"));
				} else {
					occursList.putParam("RejectReason", "");
				}
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (lc005List != null && lc005List.size() >= this.limit) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}