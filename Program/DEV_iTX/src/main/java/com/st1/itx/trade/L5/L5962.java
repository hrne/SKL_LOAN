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
import com.st1.itx.db.domain.CollMeet;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CollMeetService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;

@Component("L5962")
@Scope("prototype")

/**
 * 面催明細資料查詢
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L5962 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public CollMeetService iCollMeetService;

	@Autowired
	public CdEmpService iCdEmpService;
	
	@Autowired
	public DateUtil iDateUtil;

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
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;
		Slice<CollMeet> iCollMeet = null;

		if (iFacmNo == 0) {
			switch (iDateFlag) {
			case 1:
				exceptionError = "1到3個月內";
				iMonth1 = Dealdate(txDate, -1);
				iMonth3 = Dealdate(txDate, -3);
				iCollMeet = iCollMeetService.withoutFacmNo(iMonth1, iMonth3, iCaseCode, iCustNo, this.index, this.limit, titaVo);
				break;
			case 2:
				exceptionError = "半年內";
				iMonth6 = Dealdate(txDate, -6);
				iCollMeet = iCollMeetService.withoutFacmNo(iMonth6, txDate, iCaseCode, iCustNo, this.index, this.limit, titaVo);
				break;
			case 9:
				iCollMeet = iCollMeetService.withoutFacmNoAll(iCaseCode, iCustNo, this.index, this.limit, titaVo);
				break;
			}
		} else {
			switch (iDateFlag) {
			case 1:
				exceptionError = "1到3個月內";
				iMonth1 = Dealdate(txDate, -1);
				iMonth3 = Dealdate(txDate, -3);
				iCollMeet = iCollMeetService.telTimeBetween(iMonth1, iMonth3, iCaseCode, iCustNo, iFacmNo, this.index, this.limit, titaVo);
				break;
			case 2:
				exceptionError = "半年內";
				iMonth6 = Dealdate(txDate, -6);
				iCollMeet = iCollMeetService.telTimeBetween(iMonth6, txDate, iCaseCode, iCustNo, iFacmNo, this.index, this.limit, titaVo);
				break;
			case 9:
				iCollMeet = iCollMeetService.findSameCust(iCaseCode, iCustNo, iFacmNo, this.index, this.limit, titaVo);
				break;
			}
		}

		if (iCollMeet != null) {
			for (CollMeet returnVo : iCollMeet) {
				OccursList occursList = new OccursList();
				occursList.putParam("OOFacmNo", returnVo.getFacmNo());
				occursList.putParam("OOMeetDate", returnVo.getMeetDate());
				occursList.putParam("OOMeetTime", returnVo.getMeetTime());
				occursList.putParam("OOContactCode", returnVo.getContactCode());
				occursList.putParam("OOMeetPsnCode", returnVo.getMeetPsnCode());
				occursList.putParam("OOCollPsnCode", returnVo.getCollPsnCode());
				occursList.putParam("OOCollPsnName", returnVo.getCollPsnName());
				CdEmp iCdEmp = iCdEmpService.findById(returnVo.getCollPsnName(), titaVo);
				if (iCdEmp != null) {
					occursList.putParam("OOCollPsnNameX", iCdEmp.getFullname());
				} else {
					occursList.putParam("OOCollPsnNameX", "");
				}
				occursList.putParam("OOMeetPlace", returnVo.getMeetPlace());
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
				throw new LogicException(titaVo, "E0001", "面催主檔" + exceptionError + "無戶號:" + iCustNo + "之資料");
			} else {
				throw new LogicException(titaVo, "E0001", "面催主檔" + exceptionError + "無戶號:" + iCustNo + "額度:" + iFacmNo + "之資料");
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
