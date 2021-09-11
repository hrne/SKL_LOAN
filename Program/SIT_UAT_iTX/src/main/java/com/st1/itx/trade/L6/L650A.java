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
import com.st1.itx.util.parse.Parse;

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
	Parse iParse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		this.info("active L650A ");
		this.totaVo.init(titaVo);
		// 先刪除
		Slice<CdPfParms> dCdPfParms = null;
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
		CdPfParms rCdPfParms = new CdPfParms();
		CdPfParmsId rCdPfParamsId = new CdPfParmsId();
		//Input
		String iProdNoA = "";
		String iProdNoB = "";
		String iProdNoC = "";
		String iProdNoD = "";
		String iProdNoE = "";
		int iWorkMonthS = 0;
		int iWorkMonthE = 0;
		int i = 1;
		//業績全部
		while (i<=30) {
			iProdNoA = titaVo.getParam("ProdNoA"+i);
			if (iProdNoA.equals("") || iProdNoA.trim().isEmpty()) {
				break;
			}
			iWorkMonthS = Integer.valueOf(titaVo.getParam("WorkMonthSA"+i))+191100;
			iWorkMonthE = Integer.valueOf(titaVo.getParam("WorkMonthEA"+i))+191100;
			iCdPfParamsId.setConditionCode1("1");
			iCdPfParamsId.setConditionCode2("1");
			iCdPfParamsId.setCondition(iProdNoA);
			iCdPfParams.setCdPfParmsId(iCdPfParamsId);
			iCdPfParams.setWorkMonthStart(iWorkMonthS);
			iCdPfParams.setWorkMonthEnd(iWorkMonthE);
			try {
				iCdPfParmsService.insert(iCdPfParams, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "排除商品別-業績全部");
			}
			i++;
		}
		//換算業績、業務報酬
		i = 1;
		while (i<=30) {
			iProdNoB = titaVo.getParam("ProdNoB"+i);
			if (iProdNoB.equals("") || iProdNoB.trim().isEmpty()) {
				break;
			}
			iWorkMonthS = Integer.valueOf(titaVo.getParam("WorkMonthSB"+i))+191100;
			iWorkMonthE = Integer.valueOf(titaVo.getParam("WorkMonthEB"+i))+191100;
			iCdPfParamsId.setConditionCode1("1");
			iCdPfParamsId.setConditionCode2("2");
			iCdPfParamsId.setCondition(iProdNoB);
			iCdPfParams.setCdPfParmsId(iCdPfParamsId);
			iCdPfParams.setWorkMonthStart(iWorkMonthS);
			iCdPfParams.setWorkMonthEnd(iWorkMonthE);
			try {
				iCdPfParmsService.insert(iCdPfParams, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "排除商品別-換算業績、業務報酬");
			}
			i++;
		}
		
		//介紹獎金
		i = 1;
		while (i<=30) {
			iProdNoC = titaVo.getParam("ProdNoC"+i);
			if (iProdNoC.equals("") || iProdNoC.trim().isEmpty()) {
				break;
			}
			iWorkMonthS = Integer.valueOf(titaVo.getParam("WorkMonthSC"+i))+191100;
			iWorkMonthE = Integer.valueOf(titaVo.getParam("WorkMonthEC"+i))+191100;
			iCdPfParamsId.setConditionCode1("1");
			iCdPfParamsId.setConditionCode2("3");
			iCdPfParamsId.setCondition(iProdNoC);
			iCdPfParams.setCdPfParmsId(iCdPfParamsId);
			iCdPfParams.setWorkMonthStart(iWorkMonthS);
			iCdPfParams.setWorkMonthEnd(iWorkMonthE);
			try {
				iCdPfParmsService.insert(iCdPfParams, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "排除商品別-換算業績、業務報酬");
			}
			i++;
		}
		
		//加碼獎勵津貼
		i = 1;
		while (i<=30) {
			iProdNoD = titaVo.getParam("ProdNoD"+i);
			if (iProdNoD.equals("") || iProdNoD.trim().isEmpty()) {
				break;
			}
			iWorkMonthS = Integer.valueOf(titaVo.getParam("WorkMonthSD"+i))+191100;
			iWorkMonthE = Integer.valueOf(titaVo.getParam("WorkMonthED"+i))+191100;
			iCdPfParamsId.setConditionCode1("1");
			iCdPfParamsId.setConditionCode2("4");
			iCdPfParamsId.setCondition(iProdNoD);
			iCdPfParams.setCdPfParmsId(iCdPfParamsId);
			iCdPfParams.setWorkMonthStart(iWorkMonthS);
			iCdPfParams.setWorkMonthEnd(iWorkMonthE);
			try {
				iCdPfParmsService.insert(iCdPfParams, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "排除商品別-加碼獎勵津貼");
			}
			i++;
		}
		
		//協辦獎金
		i = 1;
		while (i<=30) {
			iProdNoE = titaVo.getParam("ProdNoE"+i);
			if (iProdNoE.equals("") || iProdNoE.trim().isEmpty()) {
				break;
			}
			iWorkMonthS = Integer.valueOf(titaVo.getParam("WorkMonthSE"+i))+191100;
			iWorkMonthE = Integer.valueOf(titaVo.getParam("WorkMonthEE"+i))+191100;
			iCdPfParamsId.setConditionCode1("1");
			iCdPfParamsId.setConditionCode2("5");
			iCdPfParamsId.setCondition(iProdNoE);
			iCdPfParams.setCdPfParmsId(iCdPfParamsId);
			iCdPfParams.setWorkMonthStart(iWorkMonthS);
			iCdPfParams.setWorkMonthEnd(iWorkMonthE);
			try {
				iCdPfParmsService.insert(iCdPfParams, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "排除商品別-協辦獎金");
			}
			i++;
		}
//		int newWorkMonth =0;
//		rCdPfParamsId.setCondition(" ");
//		rCdPfParamsId.setConditionCode1("R");
//		rCdPfParamsId.setConditionCode2(" ");
//		rCdPfParms = iCdPfParmsService.holdById(rCdPfParamsId, titaVo);
//		rCdPfParms.setWorkMonthEnd(0);
//		rCdPfParms.setWorkMonthStart(newWorkMonth);
//		try {
//			iCdPfParmsService.udpate(iCdPfParams, titaVo);
//		}catch (DBException e) {
//			throw new LogicException("E0005", "排除商品別-協辦獎金");
//		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}