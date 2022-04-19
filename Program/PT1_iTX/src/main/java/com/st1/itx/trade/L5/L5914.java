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
import com.st1.itx.db.domain.PfCoOfficerLog;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.PfCoOfficerLogService;
import com.st1.itx.tradeService.TradeBuffer;

@Component("L5914")
@Scope("prototype")

/**
 * 協辦人員等級歷程查詢
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L5914 extends TradeBuffer {
	/* 轉型共用工具 */

	@Autowired
	public PfCoOfficerLogService iPfCoOfficerLogService;

	@Autowired
	public CdEmpService iCdEmpService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);

		String iEmpNo = titaVo.getParam("EmpNo");
		int iEffectiveDate = Integer.valueOf(titaVo.getParam("EffectiveDate")) + 19110000;

		Slice<PfCoOfficerLog> iPfCoOfficerLog = null;

		this.index = titaVo.getReturnIndex();

		this.limit = 40;

		iPfCoOfficerLog = iPfCoOfficerLogService.otherEq(iEmpNo, iEffectiveDate, this.index, this.limit, titaVo);

		if (iPfCoOfficerLog == null) {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}

		for (PfCoOfficerLog rPfCoOfficerLog : iPfCoOfficerLog) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOIneffectiveDate", rPfCoOfficerLog.getIneffectiveDate());
			occursList.putParam("OOEmpClass", rPfCoOfficerLog.getEmpClass());
			occursList.putParam("OOClassPass", rPfCoOfficerLog.getClassPass());
			String taU = rPfCoOfficerLog.getLastUpdate().toString();
			String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
			occursList.putParam("OOLastUpdate", uaDate);
			String rEmpNo = rPfCoOfficerLog.getLastUpdateEmpNo();
			CdEmp iCdEmp = iCdEmpService.findById(rEmpNo, titaVo);
			if (iCdEmp == null) {
				throw new LogicException(titaVo, "E0001", "無此員工資料:" + rEmpNo);
			}
			occursList.putParam("OOLastUpdateEmpNo", rEmpNo + " " + iCdEmp.getFullname());

			this.totaVo.addOccursList(occursList);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}
