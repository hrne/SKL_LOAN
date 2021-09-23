package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdPerformance;
import com.st1.itx.db.domain.CdPerformanceId;
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.service.CdPerformanceService;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;

@Service("L6757")
@Scope("prototype")
/**
 * @業績件數及金額核算標準設定整月複製或刪除
 *
 * @author Fegie
 * @version 1.0.0
 */
public class L6757 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdPerformanceService iCdPerformanceService;
	@Autowired
	public CdWorkMonthService iCdWorkMonthService;
	@Autowired
	SendRsp iSendRsp;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6757 ");
		this.totaVo.init(titaVo);

		int iFunc = Integer.valueOf(titaVo.getParam("FuncCode"));
		int iWorkMonth = Integer.valueOf(titaVo.getParam("WorkMonth")) + 191100;
		int iTargetWorkMonth = Integer.valueOf(titaVo.getParam("TargetWorkMonth")) + 191100;
		int iEntdy = Integer.valueOf(titaVo.getEntDy())+19110000;
		Slice<CdPerformance> iCdPerformance = null;
		iCdPerformance = iCdPerformanceService.findWorkMonth(iWorkMonth, this.index, this.limit, titaVo);
		switch (iFunc) {
		case 3:
			//若新增工作月為當工作月需主管放行 --2021/9/23
			CdWorkMonth iCdWorkMonth = new CdWorkMonth();
			iCdWorkMonth = iCdWorkMonthService.findDateFirst(iEntdy, iEntdy, titaVo);
			if (iCdWorkMonth == null) {
				throw new LogicException("E0005", "查無工作月設定資料");
			}
			int iYear = iCdWorkMonth.getYear()-1911;
			int iMonth = iCdWorkMonth.getMonth();
			String sYear = StringUtils.leftPad(String.valueOf(iYear), 3,"0");
			String sMonth = StringUtils.leftPad(String.valueOf(iMonth), 2,"0");
			String sWorkMonth = sYear+sMonth;
			if (sWorkMonth.compareTo(titaVo.getParam("TargetWorkMonth")) == 0) { //若工作月與目標工作月相同，則做主管刷卡
				if (!titaVo.getHsupCode().equals("1")) {
					iSendRsp.addvReason(this.txBuffer,titaVo,"0004","");
				}				
			}
			if (iCdPerformance == null) {
				throw new LogicException("E0005", "新增時發生錯誤，原工作月該資料不存在");
			}
			for (CdPerformance cCdPerformance : iCdPerformance) {
				CdPerformance aCdPerformance = new CdPerformance();
				CdPerformanceId aCdPerformanceId = new CdPerformanceId();
				aCdPerformanceId.setPieceCode(cCdPerformance.getPieceCode());
				aCdPerformanceId.setWorkMonth(iTargetWorkMonth);
				CdPerformance bCdPerformance = iCdPerformanceService.findById(aCdPerformanceId, titaVo);
				if (bCdPerformance != null) {
					throw new LogicException("E0005", "新增時發生錯誤，該工作月資料已存在");
				}
				aCdPerformance.setCdPerformanceId(aCdPerformanceId);
				aCdPerformance.setUnitCnt(cCdPerformance.getUnitCnt());
				aCdPerformance.setUnitAmtCond(cCdPerformance.getUnitAmtCond());
				aCdPerformance.setUnitPercent(cCdPerformance.getUnitPercent());
				aCdPerformance.setIntrodPerccent(cCdPerformance.getIntrodPerccent());
				aCdPerformance.setIntrodAmtCond(cCdPerformance.getIntrodAmtCond());
				aCdPerformance.setIntrodPfEqBase(cCdPerformance.getIntrodPfEqBase());
				aCdPerformance.setIntrodPfEqAmt(cCdPerformance.getIntrodPfEqAmt());
				aCdPerformance.setIntrodRewardBase(cCdPerformance.getIntrodRewardBase());
				aCdPerformance.setIntrodReward(cCdPerformance.getIntrodReward());
				aCdPerformance.setBsOffrCnt(cCdPerformance.getBsOffrCnt());
				aCdPerformance.setBsOffrCntLimit(cCdPerformance.getBsOffrCntLimit());
				aCdPerformance.setBsOffrAmtCond(cCdPerformance.getBsOffrAmtCond());
				aCdPerformance.setBsOffrPerccent(cCdPerformance.getBsOffrPerccent());
				try {
					iCdPerformanceService.insert(aCdPerformance, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "新增時發生錯誤");
				}
			}
			break;
		case 4:
			if (iCdPerformance == null) {
				throw new LogicException("E0008", "刪除時發生錯誤，原工作月該資料不存在");
			}
			for (CdPerformance xCdPerformance : iCdPerformance) {
				CdPerformance dCdPerformance = null;
				dCdPerformance = iCdPerformanceService.holdById(xCdPerformance.getCdPerformanceId(), titaVo);
				try {
					iCdPerformanceService.delete(dCdPerformance, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "刪除時發生錯誤");
				}
			}
			break;
		default:
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}
