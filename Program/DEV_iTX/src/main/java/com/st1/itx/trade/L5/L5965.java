package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CollRemind;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CollRemindService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Component("L5965")
@Scope("prototype")

/**
 * 法催明細資料查詢
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L5965 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public CollRemindService collremindservice;

	@Autowired
	public CdEmpService iCdEmpService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		String icasecode = titaVo.getParam("CaseCode");
		int icustno = Integer.valueOf(titaVo.getParam("CustNo"));
		int ifacmno = Integer.valueOf(titaVo.getParam("FacmNo"));
		String icondcodeflag = titaVo.getParam("CondCodeFlag");
		String iCondition = "";

		switch (icondcodeflag) {
		case "0":
			iCondition = "已到期";
			break;
		case "1":
			iCondition = "有效";
			break;
		case "4":
			iCondition = "已刪除";
			break;
		case "6":
			iCondition = "已解除";
			break;
		}
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		Slice<CollRemind> tcollremind = collremindservice.findCl(icasecode, icustno, ifacmno, icondcodeflag, this.index, this.limit, titaVo);
		this.info("tcollremind=" + tcollremind);
		if (tcollremind != null) {
			this.info("got tcollremind");
			for (CollRemind recollremind : tcollremind) {
				OccursList occursList = new OccursList();
				occursList.putParam("OOFacmNo", recollremind.getFacmNo());
				occursList.putParam("OOCondCode", recollremind.getCondCode());
				occursList.putParam("OORemindDate", recollremind.getRemindDate());
				occursList.putParam("OOEditDate", recollremind.getEditDate());
				occursList.putParam("OOEditTime", recollremind.getEditTime());
				occursList.putParam("OORemindCode", recollremind.getRemindCode());
				occursList.putParam("OORemark", recollremind.getRemark());
				CdEmp iCdEmp = iCdEmpService.findAgentIdFirst(recollremind.getLastUpdateEmpNo(),titaVo);
				if (iCdEmp == null) {
					occursList.putParam("OOEditEmpNoX", "");
				}else {
					occursList.putParam("OOEditEmpNoX", iCdEmp.getFullname());
				}
				occursList.putParam("OOActSeq", recollremind.getTitaTxtNo());
				occursList.putParam("OOTitaTxtNo", recollremind.getTitaTxtNo());
				occursList.putParam("OOTitaTlrNo", recollremind.getTitaTlrNo());
				occursList.putParam("OOTitaAcDate", recollremind.getAcDate());
				this.totaVo.addOccursList(occursList);
			}
		} else {
			throw new LogicException(titaVo, "E0001", "提醒主檔查無戶號:" + icustno + "額度:" + ifacmno + "之" + iCondition + "資料");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}
