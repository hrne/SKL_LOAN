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

@Service("L650A")
@Scope("prototype")
/**
 * 排除商品別-
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L650A extends TradeBuffer {

	/* 轉型共用工具 */

	@Autowired
	public CdPfParmsService iCdPfParmsService;
	@Autowired
	SendRsp iSendRsp;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		this.info("active L650A ");
		this.totaVo.init(titaVo);
		// 先刪除
		Slice<CdPfParms> dCdPfParms = null;
		if (!titaVo.getHsupCode().equals("1")) {
			iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
		}
		dCdPfParms = iCdPfParmsService.findConditionCode1Eq("1", 0, Integer.MAX_VALUE, titaVo);
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
		String iProdNoA = "";
		String iProdNoB = "";
		String iProdNoC = "";
		String iProdNoD = "";
		String iProdNoE = "";
		int iWorkMonthS = 0;
		int iWorkMonthE = 0;
		int i = 1;
		// 業績全部
		while (i <= 30) {
			iProdNoA = titaVo.getParam("ProdNoA" + i);
			if (iProdNoA.equals("") || iProdNoA.trim().isEmpty()) {
				break;
			}
			int workMonthSA = Integer.valueOf(titaVo.getParam("WorkMonthSA" + i));
			int workMonthEA = Integer.valueOf(titaVo.getParam("WorkMonthEA" + i));
			iWorkMonthS = workMonthSA > 0 ? workMonthSA + 191100 : 0;
			iWorkMonthE = workMonthEA > 0 ? workMonthEA + 191100 : 0;
			iCdPfParamsId.setConditionCode1("1");
			iCdPfParamsId.setConditionCode2("1");
			iCdPfParamsId.setCondition(iProdNoA);
			iCdPfParams.setCdPfParmsId(iCdPfParamsId);
			iCdPfParams.setWorkMonthStart(iWorkMonthS);
			iCdPfParams.setWorkMonthEnd(iWorkMonthE);
			try {
				iCdPfParmsService.insert(iCdPfParams, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "排除商品別-業績全部");
			}
			i++;
		}
		// 換算業績、業務報酬
		i = 1;
		while (i <= 30) {
			iProdNoB = titaVo.getParam("ProdNoB" + i);
			if (iProdNoB.equals("") || iProdNoB.trim().isEmpty()) {
				break;
			}
			int workMonthSB = Integer.valueOf(titaVo.getParam("WorkMonthSB" + i));
			int workMonthEB = Integer.valueOf(titaVo.getParam("WorkMonthEB" + i));
			iWorkMonthS = workMonthSB > 0 ? workMonthSB + 191100 : 0;
			iWorkMonthE = workMonthEB > 0 ? workMonthEB + 191100 : 0;
			iCdPfParamsId.setConditionCode1("1");
			iCdPfParamsId.setConditionCode2("2");
			iCdPfParamsId.setCondition(iProdNoB);
			iCdPfParams.setCdPfParmsId(iCdPfParamsId);
			iCdPfParams.setWorkMonthStart(iWorkMonthS);
			iCdPfParams.setWorkMonthEnd(iWorkMonthE);
			try {
				iCdPfParmsService.insert(iCdPfParams, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "排除商品別-換算業績、業務報酬");
			}
			i++;
		}

		// 介紹獎金
		i = 1;
		while (i <= 30) {
			iProdNoC = titaVo.getParam("ProdNoC" + i);
			if (iProdNoC.equals("") || iProdNoC.trim().isEmpty()) {
				break;
			}
			int workMonthSC = Integer.valueOf(titaVo.getParam("WorkMonthSC" + i));
			int workMonthEC = Integer.valueOf(titaVo.getParam("WorkMonthEC" + i));
			iWorkMonthS = workMonthSC > 0 ? workMonthSC + 191100 : 0;
			iWorkMonthE = workMonthEC > 0 ? workMonthEC + 191100 : 0;
			iCdPfParamsId.setConditionCode1("1");
			iCdPfParamsId.setConditionCode2("3");
			iCdPfParamsId.setCondition(iProdNoC);
			iCdPfParams.setCdPfParmsId(iCdPfParamsId);
			iCdPfParams.setWorkMonthStart(iWorkMonthS);
			iCdPfParams.setWorkMonthEnd(iWorkMonthE);
			try {
				iCdPfParmsService.insert(iCdPfParams, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "排除商品別-換算業績、業務報酬");
			}
			i++;
		}

		// 加碼獎勵津貼
		i = 1;
		while (i <= 30) {
			iProdNoD = titaVo.getParam("ProdNoD" + i);
			if (iProdNoD.equals("") || iProdNoD.trim().isEmpty()) {
				break;
			}
			int workMonthSD = Integer.valueOf(titaVo.getParam("WorkMonthSD" + i));
			int workMonthED = Integer.valueOf(titaVo.getParam("WorkMonthED" + i));
			iWorkMonthS = workMonthSD > 0 ? workMonthSD + 191100 : 0;
			iWorkMonthE = workMonthED > 0 ? workMonthED + 191100 : 0;
			iCdPfParamsId.setConditionCode1("1");
			iCdPfParamsId.setConditionCode2("4");
			iCdPfParamsId.setCondition(iProdNoD);
			iCdPfParams.setCdPfParmsId(iCdPfParamsId);
			iCdPfParams.setWorkMonthStart(iWorkMonthS);
			iCdPfParams.setWorkMonthEnd(iWorkMonthE);
			try {
				iCdPfParmsService.insert(iCdPfParams, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "排除商品別-加碼獎勵津貼");
			}
			i++;
		}

		// 協辦獎金
		i = 1;
		while (i <= 30) {
			iProdNoE = titaVo.getParam("ProdNoE" + i);
			if (iProdNoE.equals("") || iProdNoE.trim().isEmpty()) {
				break;
			}
			int workMonthSE = Integer.valueOf(titaVo.getParam("WorkMonthSE" + i));
			int workMonthEE = Integer.valueOf(titaVo.getParam("WorkMonthEE" + i));
			iWorkMonthS = workMonthSE > 0 ? workMonthSE + 191100 : 0;
			iWorkMonthE = workMonthEE > 0 ? workMonthEE + 191100 : 0;
			iCdPfParamsId.setConditionCode1("1");
			iCdPfParamsId.setConditionCode2("5");
			iCdPfParamsId.setCondition(iProdNoE);
			iCdPfParams.setCdPfParmsId(iCdPfParamsId);
			iCdPfParams.setWorkMonthStart(iWorkMonthS);
			iCdPfParams.setWorkMonthEnd(iWorkMonthE);
			try {
				iCdPfParmsService.insert(iCdPfParams, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "排除商品別-協辦獎金");
			}
			i++;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}