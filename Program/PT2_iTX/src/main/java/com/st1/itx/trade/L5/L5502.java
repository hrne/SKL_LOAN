package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.PfBsDetail;
import com.st1.itx.db.service.PfBsDetailService;
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

	PfBsDetail pfBsDetail = new PfBsDetail();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5502 ");
		this.totaVo.init(titaVo);

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
		PfBsDetail pfBsDetail2 = (PfBsDetail) dataLog.clone(pfBsDetail);
		if (pfBsDetail.getRepayType() == 0 && pfBsDetail.getLastUpdate().equals(pfBsDetail.getCreateDate())) {
			pfBsDetail.setAdjPerfAmt(pfBsDetail.getPerfAmt());
			pfBsDetail.setAdjPerfCnt(pfBsDetail.getPerfCnt());
		}
		BigDecimal iAdjPerfAmt = new BigDecimal(titaVo.getParam("PerfAmt").trim());
		BigDecimal iAdjPerfCnt = new BigDecimal(titaVo.getParam("PerfCnt").trim());
		pfBsDetail.setAdjPerfAmt(iAdjPerfAmt);
		pfBsDetail.setAdjPerfCnt(iAdjPerfCnt);
		try {
			pfBsDetail = pfBsDetailService.update2(pfBsDetail, titaVo);
			dataLog.setEnv(titaVo, pfBsDetail2, pfBsDetail);
			dataLog.exec("修改房貸專員業績金額、件數");

		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", e.getErrorMsg());
		}
	}

	private void deleteAdj(TitaVo titaVo) throws LogicException {
		PfBsDetail pfBsDetail2 = (PfBsDetail) dataLog.clone(pfBsDetail);
		if (pfBsDetail.getRepayType() == 0) {
			pfBsDetail.setPerfAmt(pfBsDetail.getAdjPerfAmt());
			pfBsDetail.setPerfCnt(pfBsDetail.getAdjPerfCnt());
			try {
				pfBsDetail = pfBsDetailService.update2(pfBsDetail, titaVo);
				dataLog.setEnv(titaVo, pfBsDetail2, pfBsDetail);
				dataLog.exec("刪除房貸專員業績調整金額、件數");
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", e.getErrorMsg());
			}
		}
	}
}