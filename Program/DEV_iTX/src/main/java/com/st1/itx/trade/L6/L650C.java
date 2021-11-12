package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdPfParms;
import com.st1.itx.db.domain.CdPfParmsId;
import com.st1.itx.db.service.CdPfParmsService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;

@Service("L650C")
@Scope("prototype")
/**
 * 排除部門別-
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L650C extends TradeBuffer {

	/* 轉型共用工具 */

	@Autowired
	public CdPfParmsService iCdPfParmsService;
	@Autowired
	SendRsp iSendRsp;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		this.info("active L650C ");
		this.totaVo.init(titaVo);
		// 先刪除
		Slice<CdPfParms> dCdPfParms = null;
		if (!titaVo.getHsupCode().equals("1")) {
			iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
		}
		dCdPfParms = iCdPfParmsService.findConditionCode1Eq("3", 0, Integer.MAX_VALUE, titaVo);
		if (dCdPfParms != null) {
			for (CdPfParms xCdPfParms : dCdPfParms) {
				try {
					iCdPfParmsService.delete(xCdPfParms, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg());
				}
			}
		}

		CdPfParms iCdPfParams = new CdPfParms();
		CdPfParmsId iCdPfParamsId = new CdPfParmsId();
		// Input
		String iYesNoA = titaVo.getParam("YesNoA");
		String iYesNoB = titaVo.getParam("YesNoB");
		String iYesNoC = titaVo.getParam("YesNoC");
		String iYesNoD = titaVo.getParam("YesNoD");
		String iYesNoE = titaVo.getParam("YesNoE");
		int iWorkMonthSA = Integer.valueOf(titaVo.getParam("WorkMonthSA")) + 191100;
		int iWorkMonthEA = Integer.valueOf(titaVo.getParam("WorkMonthEA")) + 191100;
		int iWorkMonthSB = Integer.valueOf(titaVo.getParam("WorkMonthSB")) + 191100;
		int iWorkMonthEB = Integer.valueOf(titaVo.getParam("WorkMonthEB")) + 191100;
		int iWorkMonthSC = Integer.valueOf(titaVo.getParam("WorkMonthSC")) + 191100;
		int iWorkMonthEC = Integer.valueOf(titaVo.getParam("WorkMonthEC")) + 191100;
		int iWorkMonthSD = Integer.valueOf(titaVo.getParam("WorkMonthSD")) + 191100;
		int iWorkMonthED = Integer.valueOf(titaVo.getParam("WorkMonthED")) + 191100;
		int iWorkMonthSE = Integer.valueOf(titaVo.getParam("WorkMonthSE")) + 191100;
		int iWorkMonthEE = Integer.valueOf(titaVo.getParam("WorkMonthEE")) + 191100;

		// 業績全部
		if (!iYesNoA.trim().isEmpty()) {
			iCdPfParamsId.setConditionCode1("3");
			iCdPfParamsId.setConditionCode2("1");
			iCdPfParamsId.setCondition(iYesNoA);
			iCdPfParams.setCdPfParmsId(iCdPfParamsId);
			iCdPfParams.setWorkMonthStart(iWorkMonthSA);
			iCdPfParams.setWorkMonthEnd(iWorkMonthEA);
			try {
				iCdPfParmsService.insert(iCdPfParams, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", "業績全部");
			}
		}
		// 換算業績、業務報酬
		if (!iYesNoB.trim().isEmpty()) {
			iCdPfParams = new CdPfParms();
			iCdPfParamsId = new CdPfParmsId();
			iCdPfParamsId.setConditionCode1("3");
			iCdPfParamsId.setConditionCode2("2");
			iCdPfParamsId.setCondition(iYesNoB);
			iCdPfParams.setCdPfParmsId(iCdPfParamsId);
			iCdPfParams.setWorkMonthStart(iWorkMonthSB);
			iCdPfParams.setWorkMonthEnd(iWorkMonthEB);
			try {
				iCdPfParmsService.insert(iCdPfParams, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", "換算業績、業務報酬");
			}
		}
		// 介紹獎金
		if (!iYesNoC.trim().isEmpty()) {
			iCdPfParams = new CdPfParms();
			iCdPfParamsId = new CdPfParmsId();
			iCdPfParamsId.setConditionCode1("3");
			iCdPfParamsId.setConditionCode2("3");
			iCdPfParamsId.setCondition(iYesNoC);
			iCdPfParams.setCdPfParmsId(iCdPfParamsId);
			iCdPfParams.setWorkMonthStart(iWorkMonthSC);
			iCdPfParams.setWorkMonthEnd(iWorkMonthEC);
			try {
				iCdPfParmsService.insert(iCdPfParams, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", "介紹獎金");
			}
		}
		// 加碼獎勵津貼
		if (!iYesNoD.trim().isEmpty()) {
			iCdPfParams = new CdPfParms();
			iCdPfParamsId = new CdPfParmsId();
			iCdPfParamsId.setConditionCode1("3");
			iCdPfParamsId.setConditionCode2("4");
			iCdPfParamsId.setCondition(iYesNoD);
			iCdPfParams.setCdPfParmsId(iCdPfParamsId);
			iCdPfParams.setWorkMonthStart(iWorkMonthSD);
			iCdPfParams.setWorkMonthEnd(iWorkMonthED);
			try {
				iCdPfParmsService.insert(iCdPfParams, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", "加碼獎勵津貼");
			}
		}
		// 協辦獎金
		if (!iYesNoE.trim().isEmpty()) {
			iCdPfParams = new CdPfParms();
			iCdPfParamsId = new CdPfParmsId();
			iCdPfParamsId.setConditionCode1("3");
			iCdPfParamsId.setConditionCode2("5");
			iCdPfParamsId.setCondition(iYesNoE);
			iCdPfParams.setCdPfParmsId(iCdPfParamsId);
			iCdPfParams.setWorkMonthStart(iWorkMonthSE);
			iCdPfParams.setWorkMonthEnd(iWorkMonthEE);
			try {
				iCdPfParmsService.insert(iCdPfParams, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", "協辦獎金");
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}