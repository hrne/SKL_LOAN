package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.db.domain.PfItDetailAdjust;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.PfItDetailService;
import com.st1.itx.db.service.PfItDetailAdjustService;
import com.st1.itx.util.common.SendRsp;

@Service("L5501")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang 2021.11.4
 * @version 1.0.0
 */
public class L5501 extends TradeBuffer {
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public SendRsp sendRsp;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public PfItDetailService pfItDetailService;

	@Autowired
	public PfItDetailAdjustService pfItDetailAdjustService;
	
	@Autowired
	public CdEmpService cdEmpService;
	
	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5501 ");
		this.totaVo.init(titaVo);
		
	    if ("2".equals(titaVo.getParam("FunCode"))) {
	    	updateAdj(titaVo);
	    }  else if ("4".equals(titaVo.getParam("FunCode"))) {
	    	deleteAdj(titaVo);	    	
	    } else {
	    	throw new LogicException(titaVo, "E0010", "");
	    }
		this.addList(this.totaVo);
		return this.sendList();
	}
	
	private void updateAdj(TitaVo titaVo) throws LogicException {
		String FunCode = titaVo.getParam("FunCode").trim();
		int custNo = Integer.valueOf(titaVo.getParam("CustNo").trim()); // 戶號
		int facmNo = Integer.valueOf(titaVo.getParam("FacmNo").trim()); // 額度編號
		int bormNo = Integer.valueOf(titaVo.getParam("BormNo").trim()); // 撥款序號
		int workMonth = Integer.valueOf(titaVo.getParam("WorkMonth").trim()) + 191100; // 額度編號
		
		PfItDetailAdjust pfItDetailAdjust = pfItDetailAdjustService.findCustFacmBormFirst(custNo, facmNo, bormNo, titaVo);
//		
		if (pfItDetailAdjust == null) {
			pfItDetailAdjust = new PfItDetailAdjust();
			pfItDetailAdjust.setCustNo(custNo);
			pfItDetailAdjust.setFacmNo(facmNo);
			pfItDetailAdjust.setBormNo(bormNo);
			pfItDetailAdjust.setWorkMonth(workMonth);
			pfItDetailAdjust.setWorkSeason(workSeason(workMonth));
			pfItDetailAdjust.setAdjRange(0);
			pfItDetailAdjust.setAdjCntingCode("");
			pfItDetailAdjust.setAdjPerfEqAmt(new BigDecimal("0"));
			pfItDetailAdjust.setAdjPerfReward(new BigDecimal("0"));
			pfItDetailAdjust.setAdjPerfAmt(new BigDecimal("0"));
			
			try {
				pfItDetailAdjust = pfItDetailAdjustService.insert(pfItDetailAdjust, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg());
			}
		} else {
			pfItDetailAdjust = pfItDetailAdjustService.holdById(pfItDetailAdjust.getLogNo(), titaVo);
			if (pfItDetailAdjust == null) {
				throw new LogicException(titaVo, "E0006", "");
			}
		}
		
		PfItDetailAdjust pfItDetailAdjust2 = (PfItDetailAdjust) dataLog.clone(pfItDetailAdjust);
		
		BigDecimal iAdjPerfEqAmt = new BigDecimal(titaVo.getParam("AdjPerfEqAmt").trim());
		BigDecimal iAdjPerfReward = new BigDecimal(titaVo.getParam("AdjPerfReward").trim());
		BigDecimal iAdjPerfAmt = new BigDecimal(titaVo.getParam("AdjPerfAmt").trim());
		int iAdjRange = Integer.valueOf(titaVo.getParam("AdjRange").trim());
		
		pfItDetailAdjust.setAdjPerfEqAmt(iAdjPerfEqAmt);
		pfItDetailAdjust.setAdjPerfReward(iAdjPerfReward);
		pfItDetailAdjust.setAdjPerfAmt(iAdjPerfAmt);
		pfItDetailAdjust.setAdjCntingCode(titaVo.getParam("AdjCntingCode").trim());
		pfItDetailAdjust.setAdjRange(iAdjRange);

		try {
			pfItDetailAdjust = pfItDetailAdjustService.update2(pfItDetailAdjust, titaVo);
			
			dataLog.setEnv(titaVo, pfItDetailAdjust2, pfItDetailAdjust);
			dataLog.exec();
			
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", e.getErrorMsg());
		}
		
	}
	
	private void deleteAdj(TitaVo titaVo) throws LogicException {
		Long logNo = Long.valueOf(titaVo.getParam("AdjLogNo"));
		PfItDetailAdjust pfItDetailAdjust = pfItDetailAdjustService.holdById(logNo, titaVo);
		
		if (pfItDetailAdjust == null) {
			throw new LogicException(titaVo, "E0004", "");
		}
		
		try {
			pfItDetailAdjustService.delete(pfItDetailAdjust, titaVo);
			
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0008", e.getErrorMsg());
		}
	}
	
	private int workSeason(int workMonth) throws LogicException {
		int season = 0;
		
		int month = workMonth % 100;
		
		if (month>= 1 && month <= 3) {
			season = 1;
		} else if (month>= 4 && month <= 6) {
			season = 2;
		} else if (month>= 7 && month <= 9) {
			season = 3;
		} else {
			season = 4;
		}
			
		return season;
	}
}