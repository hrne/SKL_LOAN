package com.st1.itx.trade.L1;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.common.SendRsp;

@Component("L1111")
@Scope("prototype")

/**
 * 放款專員業績統計作業－協辦人員等級明細資料查詢
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L1111 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public CustMainService iCustMainService;
	@Autowired
	public CdCodeService iCdCodeService;

	@Autowired
	public DataLog iDataLog;

	@Autowired
	public SendRsp sendRsp;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);

		if (!titaVo.getHsupCode().equals("1")) {
			sendRsp.addvReason(this.txBuffer, titaVo, "0101", "身份證號／統一編號變更");
		}

		String iCustIdBefore = titaVo.getParam("CustIdBefore");
		String iCustIdAfter = titaVo.getParam("CustIdAfter");
		String iMark = titaVo.getParam("Mark");
		String iMarkX = titaVo.getParam("MarkX");

		CdCode iCdCode = new CdCode();
		String iItem = "";
		CustMain iCustMain = iCustMainService.custIdFirst(iCustIdBefore, titaVo);
		if (iCustMain == null) {
			throw new LogicException(titaVo, "E0003", "查無身份證號/統一編號 : " + iCustIdBefore);
		}

		CustMain iCustMain2 = iCustMainService.custIdFirst(iCustIdAfter, titaVo);
		if (iCustMain2 != null) {
			throw new LogicException(titaVo, "E0012", "變更後身份證號/統一編號 : " + iCustIdAfter);
		}

		CustMain uCustMain = iCustMainService.holdById(iCustMain.getCustUKey(), titaVo);
		
		titaVo.putParam("CustNo", uCustMain.getCustNo());
		
		CustMain beforeCustMain = (CustMain) iDataLog.clone(iCustMain);
		uCustMain.setCustId(iCustIdAfter);
		try {
			uCustMain = iCustMainService.update2(uCustMain, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0007", "客戶主檔");
		}

//		iCdCode = iCdCodeService.getItemFirst(1, "CustMark", iMark, titaVo);
//		if (iCdCode != null) {
//			iItem = iCdCode.getItem();	
//		}
		// 紀錄變更前變更後
		iDataLog.setEnv(titaVo, beforeCustMain, uCustMain);
		iDataLog.exec("變更顧客 " + beforeCustMain.getCustId() + " 證號/" + iMarkX, "CustUKey:" + beforeCustMain.getCustUKey());

		this.addList(this.totaVo);
		return this.sendList();
	}
}
