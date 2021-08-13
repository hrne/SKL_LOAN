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
import com.st1.itx.db.domain.CdPerformance;
import com.st1.itx.db.domain.CdPerformanceId;
import com.st1.itx.db.service.CdPerformanceService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L6757")
@Scope("prototype")
/**
 * @業績件數及金額核算標準設定整月複製或刪除
 *
 * @author Fegie
 * @version 1.0.0
 */
public class L6757 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6757.class);

	/* DB服務注入 */
	@Autowired
	public CdPerformanceService iCdPerformanceService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;


	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6757 ");
		this.totaVo.init(titaVo);
		
		int iFunc = Integer.valueOf(titaVo.getParam("FuncCode"));
		int iWorkMonth = Integer.valueOf(titaVo.getParam("WorkMonth"))+191100;
		int iTargetWorkMonth = Integer.valueOf(titaVo.getParam("TargetWorkMonth"))+191100;
		Slice<CdPerformance> iCdPerformance = null;
		iCdPerformance = iCdPerformanceService.findWorkMonth(iWorkMonth, this.index, this.limit, titaVo);
 		switch (iFunc) {
		case 3:
			if (iCdPerformance == null) {
				throw new LogicException("E0005", "新增時發生錯誤，原工作月該資料不存在");
			}
			for (CdPerformance cCdPerformance:iCdPerformance) {
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
				aCdPerformance.setBsOffrCntAmt(cCdPerformance.getBsOffrCntAmt());
				aCdPerformance.setBsOffrPerccent(cCdPerformance.getBsOffrPerccent());
				try {
					iCdPerformanceService.insert(aCdPerformance,titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "新增時發生錯誤");
				}
			}
			break;
		case 4:
			if (iCdPerformance == null) {
				throw new LogicException("E0008", "刪除時發生錯誤，原工作月該資料不存在");
			}
			for (CdPerformance xCdPerformance:iCdPerformance) {
				CdPerformance dCdPerformance = null;
				dCdPerformance = iCdPerformanceService.holdById(xCdPerformance.getCdPerformanceId(), titaVo);
				try {
					iCdPerformanceService.delete(dCdPerformance,titaVo);
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
