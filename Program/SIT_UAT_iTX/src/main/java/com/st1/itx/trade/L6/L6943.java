package com.st1.itx.trade.L6;

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
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.CheckAuth;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L6943")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L6943 extends TradeBuffer {
	@Autowired
	CheckAuth checkAuth;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6943 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 200;

		List<Map<String, String>> dList = checkAuth.canDoPgms(titaVo.getParam("BrNo"), titaVo.getParam("TlrNo"), titaVo.getParam("TranNo"));

		if (dList == null || dList.size() == 0) {
			throw new LogicException(titaVo, "E0001", "交易權限");
		}

		int cnt = 0;
		int idx = 0;
		for (Map<String, String> dVo : dList) {
			idx++;
			if (this.index > 0 && this.index > idx) {
				continue;
			}
			cnt++;
			if (cnt > this.limit) {
				break;
			}
			OccursList occursList = new OccursList();
//			String sql = "select A.\"BrNo\",E.\"BranchShort\",A.\"TlrNo\",F.\"Fullname\",B.\"AuthNo\",G.\"AuthItem\",C.\"TranNo\",D.\"TranItem\",C.\"AuthFg\",";
//			sql += "case C.\"AuthFg\" when 1 then '限查詢' else '全部' end as \"AuthFgX\",C.\"LastUpdate\",C.\"LastUpdateEmpNo\" ";

			occursList.putParam("OBrNo", dVo.get("BrNo"));
			occursList.putParam("OBrNoX", dVo.get("BranchShort"));
			occursList.putParam("OTlrNo", dVo.get("TlrNo"));
			occursList.putParam("OTlrNoX", dVo.get("Fullname"));
			occursList.putParam("OAuthNo", dVo.get("AuthNo"));
			occursList.putParam("OAuthNoX", dVo.get("AuthItem"));
			occursList.putParam("OTranNo", dVo.get("TranNo"));
			occursList.putParam("OTranNoX", dVo.get("TranItem"));
			occursList.putParam("OAuthFg", dVo.get("AuthFg"));
			occursList.putParam("OAuthFgX", dVo.get("AuthFgX"));
			occursList.putParam("OLastUpdate", parse.stringToStringDateTime(dVo.get("LastUpdate")));
			occursList.putParam("OLastEmp", dVo.get("LastUpdateEmpNo") + " " + dVo.get("LastUpdateEmpName"));

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		this.info("dList.size = " + dList.size() + "/" + idx);

		if (dList != null && idx < dList.size()) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(idx);
			if ("".equals(titaVo.getParam("TlrNo")) && "".equals(titaVo.getParam("TranNo"))) {
				this.totaVo.setMsgEndToEnter();// 手動折返
			} else {
				this.totaVo.setMsgEndToAuto();// 自動折返
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}