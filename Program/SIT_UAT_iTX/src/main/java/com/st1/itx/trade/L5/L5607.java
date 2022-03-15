package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.CollListService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.CollList;
import com.st1.itx.db.domain.CollListId;
import com.st1.itx.util.data.DataLog;

@Component("L5607")
@Scope("prototype")

/**
 * 個案人員指派維護
 * 
 * @author 
 * @version 1.0.0
 */

public class L5607 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public CollListService iCollListService;
	@Autowired
	public DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		this.info("L5607 start");
		int iCustNo = Integer.valueOf(titaVo.getParam("CustNo"));
		int iFacmNo = Integer.valueOf(titaVo.getParam("FacmNo"));
		String iIsSpecify = titaVo.getParam("IsSpecify");
		String iAccCollPsn = titaVo.getParam("AccCollPsn");
		String iAccTelArea = titaVo.getParam("AccTelArea");
		String iAccTelNo = titaVo.getParam("AccTelNo");
		String iAccTelExt = titaVo.getParam("AccTelExt");
		String iLegalPsn = titaVo.getParam("LegalPsn");
		String iLegalArea = titaVo.getParam("LegalArea");
		String iLegalNo = titaVo.getParam("LegalNo");
		String iLegalExt = titaVo.getParam("LegalExt");

		CollListId CollListId = new CollListId();
		CollList iCollList = new CollList();
		CollList uCollList = new CollList();
		CollListId.setCustNo(iCustNo);
		CollListId.setFacmNo(iFacmNo);

		iCollList = iCollListService.holdById(CollListId, titaVo);
		CollList beforeCollList = (CollList) iDataLog.clone(iCollList);
		iCollList.setIsSpecify(iIsSpecify);
		iCollList.setAccCollPsn(iAccCollPsn);
		iCollList.setAccTelArea(iAccTelArea);
		iCollList.setAccTelExt(iAccTelExt);
		iCollList.setAccTelNo(iAccTelNo);
		iCollList.setLegalPsn(iLegalPsn);
		iCollList.setLegalArea(iLegalArea);
		iCollList.setLegalNo(iLegalNo);
		iCollList.setLegalExt(iLegalExt);
		try {
			uCollList = iCollListService.update2(iCollList, titaVo);
			iDataLog.setEnv(titaVo, beforeCollList, uCollList);
			iDataLog.exec("修改法催紀錄清單檔法催人員資料");
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", e.getErrorMsg());
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

}
