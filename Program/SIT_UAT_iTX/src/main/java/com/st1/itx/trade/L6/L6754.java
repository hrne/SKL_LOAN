package com.st1.itx.trade.L6;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdPerformance;
import com.st1.itx.db.domain.CdPerformanceId;
import com.st1.itx.db.service.CdPerformanceService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

/**
 * Tita FuncCode=9,1 PieceCode=X,1 UnitCnt=n,2.2 UnitAmtCond=m,14.2
 * UnitPercent=n,3.6 IntrodPerccent=n,3.6 IntrodPerccentCond=m,14.2
 * IntrodPfEqBase=m,14.2 IntrodPfEqAmt=m,14.2 IntrodRewardBase=m,14.2
 * IntrodReward=m,14.2 BsOffrCnt=n,2.2 BsOffrCntLimit=n,2.2 BsOffrAmtCond=m,14.2
 * BsOffrCntAmt=m,14.2 BsOffrPerccent=n,3.6 END=X,1
 */

@Service("L6754")
@Scope("prototype")
/**
 *
 *
 * @author Fegie
 * @version 1.0.0
 */
public class L6754 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdPerformanceService iCdPerformanceService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse iParse;
	@Autowired
	public DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6754 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFuncCode = this.iParse.stringToInteger(titaVo.getParam("FuncCode"));
		int iWorkMonth = Integer.valueOf(titaVo.getParam("WorkMonth")) + 191100;
		String iPieceCode = titaVo.getParam("PieceCode");
		BigDecimal iUnitCnt = new BigDecimal(titaVo.getParam("UnitCnt"));
		BigDecimal iUnitAmtCond = new BigDecimal(titaVo.getParam("UnitAmtCond"));
		BigDecimal iUnitPercent = new BigDecimal(titaVo.getParam("UnitPercent"));
		BigDecimal iIntrodPerccent = new BigDecimal(titaVo.getParam("IntrodPerccent"));
		BigDecimal iIntrodAmtCond = new BigDecimal(titaVo.getParam("IntrodAmtCond"));
		BigDecimal iIntrodPfEqBase = new BigDecimal(titaVo.getParam("IntrodPfEqBase"));
		BigDecimal iIntrodPfEqAmt = new BigDecimal(titaVo.getParam("IntrodPfEqAmt"));
		BigDecimal iIntrodRewardBase = new BigDecimal(titaVo.getParam("IntrodRewardBase"));
		BigDecimal iIntrodReward = new BigDecimal(titaVo.getParam("IntrodReward"));
		BigDecimal iBsOffrCnt = new BigDecimal(titaVo.getParam("BsOffrCnt"));
		BigDecimal iBsOffrCntLimit = new BigDecimal(titaVo.getParam("BsOffrCntLimit"));
		BigDecimal iBsOffrAmtCond = new BigDecimal(titaVo.getParam("BsOffrAmtCond"));
		BigDecimal iBsOffrPerccent = new BigDecimal(titaVo.getParam("BsOffrPerccent"));

		CdPerformance iCdPerformance = new CdPerformance();
		CdPerformanceId iCdPerformanceId = new CdPerformanceId();
		iCdPerformanceId.setPieceCode(iPieceCode);
		iCdPerformanceId.setWorkMonth(iWorkMonth);

		switch (iFuncCode) {
		case 1:
			iCdPerformance = iCdPerformanceService.findById(iCdPerformanceId, titaVo);
			if (iCdPerformance == null) {
				iCdPerformance = new CdPerformance();
			} else {
				throw new LogicException(titaVo, "E0005", "已有資料");
			}
			iCdPerformance.setCdPerformanceId(iCdPerformanceId);
			iCdPerformance.setUnitCnt(iUnitCnt);
			iCdPerformance.setUnitAmtCond(iUnitAmtCond);
			iCdPerformance.setUnitPercent(iUnitPercent);
			iCdPerformance.setIntrodPerccent(iIntrodPerccent);
			iCdPerformance.setIntrodAmtCond(iIntrodAmtCond);
			iCdPerformance.setIntrodPfEqBase(iIntrodPfEqBase);
			iCdPerformance.setIntrodPfEqAmt(iIntrodPfEqAmt);
			iCdPerformance.setIntrodRewardBase(iIntrodRewardBase);
			iCdPerformance.setIntrodReward(iIntrodReward);
			iCdPerformance.setBsOffrCnt(iBsOffrCnt);
			iCdPerformance.setBsOffrCntLimit(iBsOffrCntLimit);
			iCdPerformance.setBsOffrAmtCond(iBsOffrAmtCond);
			iCdPerformance.setBsOffrPerccent(iBsOffrPerccent);
			try {
				iCdPerformanceService.insert(iCdPerformance, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "新增時發生錯誤");
			}
			break;
		case 2:
			iCdPerformance = iCdPerformanceService.holdById(iCdPerformanceId, titaVo);
			CdPerformance beforeCdPerformance = new CdPerformance();
			if (iCdPerformance != null) {
				beforeCdPerformance = (CdPerformance) iDataLog.clone(iCdPerformance);
			} else {
				throw new LogicException(titaVo, "E0005", "更新時無資料");
			}
			iCdPerformance.setUnitCnt(iUnitCnt);
			iCdPerformance.setUnitAmtCond(iUnitAmtCond);
			iCdPerformance.setUnitPercent(iUnitPercent);
			iCdPerformance.setIntrodPerccent(iIntrodPerccent);
			iCdPerformance.setIntrodAmtCond(iIntrodAmtCond);
			iCdPerformance.setIntrodPfEqBase(iIntrodPfEqBase);
			iCdPerformance.setIntrodPfEqAmt(iIntrodPfEqAmt);
			iCdPerformance.setIntrodRewardBase(iIntrodRewardBase);
			iCdPerformance.setIntrodReward(iIntrodReward);
			iCdPerformance.setBsOffrCnt(iBsOffrCnt);
			iCdPerformance.setBsOffrCntLimit(iBsOffrCntLimit);
			iCdPerformance.setBsOffrAmtCond(iBsOffrAmtCond);
			iCdPerformance.setBsOffrPerccent(iBsOffrPerccent);
			try {
				iCdPerformanceService.update(iCdPerformance, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0007", "更新時發生錯誤");
			}
			// 紀錄變更前變更後
			iDataLog.setEnv(titaVo, beforeCdPerformance, iCdPerformance);
			iDataLog.exec();
			break;
		case 4:
			iCdPerformance = iCdPerformanceService.holdById(iCdPerformanceId, titaVo);
			if (iCdPerformance != null) {
				try {
					iCdPerformanceService.delete(iCdPerformance, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "刪除時無資料發生錯誤");
				}
			} else {
				throw new LogicException(titaVo, "E0005", "刪除時無資料");
			}
			break;
		default:
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}
