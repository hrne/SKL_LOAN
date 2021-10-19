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
import com.st1.itx.db.domain.CollTel;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CollTelService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;

@Component("L5961")
@Scope("prototype")

/**
 * 電催明細資料查詢
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L5961 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public CollTelService iCollTelService;

	@Autowired
	public DateUtil iDateUtil;

	@Autowired
	public CdEmpService iCdEmpService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		String iCaseCode = titaVo.getParam("CaseCode");
		int iCustNo = Integer.valueOf(titaVo.getParam("CustNo"));
		int iFacmNo = Integer.valueOf(titaVo.getParam("FacmNo"));
		int iDateFlag = Integer.valueOf(titaVo.getParam("DateFlag"));
		int txDate = Integer.valueOf(titaVo.getEntDy()) + 19110000;// 營業日 放acdate

		int iMonth1 = 0;
		int iMonth3 = 0;
		int iMonth6 = 0;

		String exceptionError = "";

		Slice<CollTel> iCollTel = null;
		if (iFacmNo == 0) {
			switch (iDateFlag) {
			case 1:
				exceptionError = "1到3個月內";
				iMonth1 = Dealdate(txDate, -1);
				iMonth3 = Dealdate(txDate, -3);
				iCollTel = iCollTelService.withoutFacmNo(iMonth1, iMonth3, iCaseCode, iCustNo, this.index, this.limit, titaVo);
				break;
			case 2:
				exceptionError = "半年內";
				iMonth6 = Dealdate(txDate, -6);
				iCollTel = iCollTelService.withoutFacmNo(iMonth6, txDate, iCaseCode, iCustNo, this.index, this.limit, titaVo);
				break;
			case 9:
				iCollTel = iCollTelService.withoutFacmNoAll(iCaseCode, iCustNo, this.index, this.limit, titaVo);
				break;
			}
		} else {
			switch (iDateFlag) {
			case 1:
				exceptionError = "1到3個月內";
				iMonth1 = Dealdate(txDate, -1);
				iMonth3 = Dealdate(txDate, -3);
				iCollTel = iCollTelService.telTimeBetween(iMonth1, iMonth3, iCaseCode, iCustNo, iFacmNo, this.index, this.limit, titaVo);
				break;
			case 2:
				exceptionError = "半年內";
				iMonth6 = Dealdate(txDate, -6);
				iCollTel = iCollTelService.telTimeBetween(iMonth6, txDate, iCaseCode, iCustNo, iFacmNo, this.index, this.limit, titaVo);
				break;
			case 9:
				iCollTel = iCollTelService.findSameCust(iCaseCode, iCustNo, iFacmNo, this.index, this.limit, titaVo);
				break;
			}
		}
		if (iCollTel != null) {
			for (CollTel returnVo : iCollTel) {
				CdEmp iCdEmp = null;
				OccursList occursList = new OccursList();
				occursList.putParam("OOFacmNo", returnVo.getFacmNo());
				occursList.putParam("OOTelDate", returnVo.getTelDate());
				occursList.putParam("OOTelTime", returnVo.getTelTime().trim());
				occursList.putParam("OOContactCode", returnVo.getContactCode());
				occursList.putParam("OORecvrCode", returnVo.getRecvrCode());
				occursList.putParam("OOTelArea", returnVo.getTelArea());
				occursList.putParam("OOTelNo", returnVo.getTelNo());
				occursList.putParam("OOTelExt", returnVo.getTelExt());
				occursList.putParam("OOResultCode", returnVo.getResultCode());
				occursList.putParam("OORemark", returnVo.getRemark());
				occursList.putParam("OOEditEmpNo", returnVo.getLastUpdateEmpNo());
				iCdEmp = iCdEmpService.findById(returnVo.getLastUpdateEmpNo(),titaVo);
				if (iCdEmp == null) {
					occursList.putParam("OOEditEmpNoX", "");
				}else {
					occursList.putParam("OOEditEmpNoX", iCdEmp.getFullname());
				}
				occursList.putParam("OOActSeq", returnVo.getTitaTxtNo());
				occursList.putParam("OOTitaTxtNo", returnVo.getTitaTxtNo());
				occursList.putParam("OOTitaTlrNo", returnVo.getTitaTlrNo());
				occursList.putParam("OOTitaAcDate", returnVo.getAcDate());
				this.totaVo.addOccursList(occursList);
			}
		} else {
			if (iFacmNo == 0) {
				throw new LogicException(titaVo, "E0001", "電催主檔" + exceptionError + "無戶號:" + iCustNo + "之資料");
			} else {
				throw new LogicException(titaVo, "E0001", "電催主檔" + exceptionError + "無戶號:" + iCustNo + "額度:" + iFacmNo + "之資料");
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	private int Dealdate(int txDate, int iMonth) throws LogicException {
		int retxdate = 0;
		iDateUtil.init();
		iDateUtil.setDate_1(txDate);
		iDateUtil.setMons(iMonth);

		retxdate = iDateUtil.getCalenderDay();

		return retxdate;
	}
}
