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
import com.st1.itx.db.domain.PfBsDetail;
import com.st1.itx.db.service.PfBsDetailService;
import com.st1.itx.db.domain.PfBsDetailAdjust;
import com.st1.itx.db.service.PfBsDetailAdjustService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L5502")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang 2021.11.04
 * @version 1.0.0
 */
public class L5502 extends TradeBuffer {
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	@Autowired
	public DataLog dataLog;

	@Autowired
	public PfBsDetailService pfBsDetailService;

	@Autowired
	public PfBsDetailAdjustService pfBsDetailAdjustService;

	PfBsDetail pfBsDetail = new PfBsDetail();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5502 ");
		this.totaVo.init(titaVo);
		int ActFg = titaVo.getActFgI();

		long logNo = Long.valueOf(titaVo.getParam("LogNo").trim());
		pfBsDetail = pfBsDetailService.findById(logNo, titaVo);
		if (pfBsDetail == null) {
			throw new LogicException(titaVo, "E0001", "房貸專員業績資料");
		}

		if ("2".equals(titaVo.getParam("FunCode"))) {
			updateAdj(titaVo);
		} else if ("4".equals(titaVo.getParam("FunCode"))) {
			deleteAdj(titaVo);
		} else {
			throw new LogicException(titaVo, "E0010", "");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void updateAdj(TitaVo titaVo) throws LogicException {
		int custNo = Integer.valueOf(titaVo.getParam("CustNo").trim()); // 戶號
		int facmNo = Integer.valueOf(titaVo.getParam("FacmNo").trim()); // 額度編號
		int bormNo = Integer.valueOf(titaVo.getParam("BormNo").trim()); // 額度編號
		long adjLogNo = Long.valueOf(titaVo.getParam("AdjLogNo").trim()); // 調整序號

		PfBsDetailAdjust pfBsDetailAdjust = null;
		if (adjLogNo > 0) {
			pfBsDetailAdjust = pfBsDetailAdjustService.findById(adjLogNo, titaVo);
		} else {
			pfBsDetailAdjust = pfBsDetailAdjustService.findCustBormFirst(custNo, facmNo, bormNo, titaVo);
		}

		if (pfBsDetailAdjust == null) {
			pfBsDetailAdjust = new PfBsDetailAdjust();
			pfBsDetailAdjust.setCustNo(custNo);
			pfBsDetailAdjust.setFacmNo(facmNo);
			pfBsDetailAdjust.setBormNo(bormNo);
			pfBsDetailAdjust.setWorkMonth(pfBsDetail.getWorkMonth());
			pfBsDetailAdjust.setWorkSeason(pfBsDetail.getWorkSeason());

			pfBsDetailAdjust.setAdjPerfAmt(pfBsDetail.getPerfAmt());
			pfBsDetailAdjust.setAdjPerfCnt(pfBsDetail.getPerfCnt());

			try {
				pfBsDetailAdjust = pfBsDetailAdjustService.insert(pfBsDetailAdjust, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg());
			}
		} else {
			pfBsDetailAdjust = pfBsDetailAdjustService.holdById(pfBsDetailAdjust.getLogNo(), titaVo);
			if (pfBsDetailAdjust == null) {
				throw new LogicException(titaVo, "E0006", "");
			}
			if (pfBsDetailAdjust.getWorkMonth() == 0) {
				pfBsDetailAdjust.setWorkMonth(pfBsDetail.getWorkMonth());
				pfBsDetailAdjust.setAdjPerfAmt(pfBsDetail.getPerfAmt());
				pfBsDetailAdjust.setAdjPerfCnt(pfBsDetail.getPerfCnt());
			}
		}

		PfBsDetailAdjust pfBsDetailAdjust2 = (PfBsDetailAdjust) dataLog.clone(pfBsDetailAdjust);

		BigDecimal iAdjPerfAmt = new BigDecimal(titaVo.getParam("PerfAmt").trim());
		BigDecimal iAdjPerfCnt = new BigDecimal(titaVo.getParam("PerfCnt").trim());

		pfBsDetailAdjust.setAdjPerfAmt(iAdjPerfAmt);
		pfBsDetailAdjust.setAdjPerfCnt(iAdjPerfCnt);

		try {
			pfBsDetailAdjust = pfBsDetailAdjustService.update2(pfBsDetailAdjust, titaVo);

			dataLog.setEnv(titaVo, pfBsDetailAdjust2, pfBsDetailAdjust);
			dataLog.exec("修改房貸專員業績金額、件數");

		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", e.getErrorMsg());
		}
	}

	private void deleteAdj(TitaVo titaVo) throws LogicException {
		Long logNo = Long.valueOf(titaVo.getParam("AdjLogNo"));
		PfBsDetailAdjust pfBsDetailAdjust = pfBsDetailAdjustService.holdById(logNo, titaVo);

		if (pfBsDetailAdjust == null) {
			throw new LogicException(titaVo, "E0004", "");
		}

		PfBsDetailAdjust pfBsDetailAdjust2 = (PfBsDetailAdjust) dataLog.clone(pfBsDetailAdjust);

		pfBsDetailAdjust.setAdjPerfAmt(pfBsDetail.getPerfAmt());
		pfBsDetailAdjust.setAdjPerfCnt(pfBsDetail.getPerfCnt());

		try {

			dataLog.setEnv(titaVo, pfBsDetailAdjust2, pfBsDetailAdjust);
			dataLog.exec("還原房貸專員業績金額、件數");

			pfBsDetailAdjust.setWorkMonth(0);

			pfBsDetailAdjustService.update(pfBsDetailAdjust, titaVo);

		} catch (DBException e) {
			throw new LogicException(titaVo, "E0008", e.getErrorMsg());
		}
	}
}