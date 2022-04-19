package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
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
	public CollRemindService iCollRemindService;

	@Autowired
	public CdEmpService iCdEmpService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		String iCaseCode = titaVo.getParam("CaseCode");
		int iCustNo = Integer.valueOf(titaVo.getParam("CustNo"));
		int iFacmNo = Integer.valueOf(titaVo.getParam("FacmNo"));
		String iCondCodeFlag = titaVo.getParam("CondCodeFlag");
		String iCondition = "";

		switch (iCondCodeFlag) {
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
		this.limit = 80;

		Slice<CollRemind> tCollRemind = iCollRemindService.findCl(iCaseCode, iCustNo, iFacmNo, iCondCodeFlag, this.index, this.limit, titaVo);
		this.info("tcollremind=" + tCollRemind);
		if (tCollRemind != null) {
			this.info("got tcollremind");
			for (CollRemind reCollRemind : tCollRemind) {
				OccursList occursList = new OccursList();
				occursList.putParam("OOFacmNo", reCollRemind.getFacmNo());
				occursList.putParam("OOCondCode", reCollRemind.getCondCode());
				occursList.putParam("OORemindDate", reCollRemind.getRemindDate());
				String tU = reCollRemind.getLastUpdate().toString();
				String uDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(tU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
				String uTime = tU.substring(11, 13) + tU.substring(14, 16);
				occursList.putParam("OOEditDate", uDate);
				occursList.putParam("OOEditTime", uTime);
				occursList.putParam("OORemindCode", reCollRemind.getRemindCode());
				occursList.putParam("OORemark", reCollRemind.getRemark());
				occursList.putParam("OOEditEmpNo", reCollRemind.getLastUpdateEmpNo());
				CdEmp iCdEmp = iCdEmpService.findById(reCollRemind.getLastUpdateEmpNo(), titaVo);
				if (iCdEmp == null) {
					occursList.putParam("OOEditEmpNoX", "");
				} else {
					occursList.putParam("OOEditEmpNoX", iCdEmp.getFullname());
				}
				occursList.putParam("OOTitaTxtNo", reCollRemind.getTitaTxtNo());
				occursList.putParam("OOTitaTlrNo", reCollRemind.getTitaTlrNo());
				occursList.putParam("OOTitaAcDate", reCollRemind.getAcDate());
				this.totaVo.addOccursList(occursList);
			}
		} else {
			throw new LogicException(titaVo, "E0001", "提醒主檔查無戶號:" + iCustNo + "額度:" + iFacmNo + "之" + iCondition + "資料");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}
