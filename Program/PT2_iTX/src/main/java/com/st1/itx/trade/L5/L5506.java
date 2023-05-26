package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.PfIntranetAdjust;
import com.st1.itx.db.service.PfIntranetAdjustService;

import com.st1.itx.util.data.DataLog;

@Service("L5506")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L5506 extends TradeBuffer {
	@Autowired
	public PfIntranetAdjustService pfIntranetAdjustService;

	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5506 ");
		this.totaVo.init(titaVo);

		String iFunCode = titaVo.getParam("FunCode").trim();

		if ("1".equals(iFunCode)) {
			PfIntranetAdjust pfIntranetAdjust = new PfIntranetAdjust();

			pfIntranetAdjust = moveData(titaVo, pfIntranetAdjust);

			try {
				pfIntranetAdjust = pfIntranetAdjustService.insert(pfIntranetAdjust, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料已存在
			}
		} else if ("2".equals(iFunCode)) {
			long iLogNo = Long.valueOf(titaVo.getParam("LogNo").trim());

			PfIntranetAdjust pfIntranetAdjust = pfIntranetAdjustService.findById(iLogNo, titaVo);

			if (pfIntranetAdjust == null) {
				throw new LogicException(titaVo, "E0003", "");
			} else {
				PfIntranetAdjust pfIntranetAdjust2 = (PfIntranetAdjust) dataLog.clone(pfIntranetAdjust);

				pfIntranetAdjust = moveData(titaVo, pfIntranetAdjust);

				try {
					pfIntranetAdjust = pfIntranetAdjustService.update2(pfIntranetAdjust, titaVo);

					dataLog.setEnv(titaVo, pfIntranetAdjust2, pfIntranetAdjust);
					dataLog.exec("修改內網報表業績調整資料");

				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg());
				}
			}
		} else if ("4".equals(iFunCode)) {
			long iLogNo = Long.valueOf(titaVo.getParam("LogNo").trim());

			PfIntranetAdjust pfIntranetAdjust = pfIntranetAdjustService.holdById(iLogNo, titaVo);

			if (pfIntranetAdjust == null) {
				throw new LogicException(titaVo, "E0003", "");
			} else {
				PfIntranetAdjust pfIntranetAdjust2 = (PfIntranetAdjust) dataLog.clone(pfIntranetAdjust);
				
				pfIntranetAdjust.setSumAmt(BigDecimal.ZERO);
				pfIntranetAdjust.setSumCnt(BigDecimal.ZERO);

				try {
					pfIntranetAdjustService.delete(pfIntranetAdjust, titaVo);

					dataLog.setEnv(titaVo, pfIntranetAdjust2, pfIntranetAdjust);
					dataLog.exec("刪除內網報表業績調整資料");

				} catch (DBException e) {
					throw new LogicException("E0008", "");
				}
			}

		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private PfIntranetAdjust moveData(TitaVo titaVo, PfIntranetAdjust pfIntranetAdjust) throws LogicException {

		int iCustNo = Integer.valueOf(titaVo.getParam("CustNo").trim());
		int iFacmNo = Integer.valueOf(titaVo.getParam("FacmNo").trim());
		int iBormNo = Integer.valueOf(titaVo.getParam("BormNo").trim());

		int iPerfDate = Integer.valueOf(titaVo.getParam("PerfDate").trim());
		int iWorkMonth = Integer.valueOf(titaVo.getParam("WorkMonth").trim()) + 191100;
		int iWorkSeason = Integer.valueOf(titaVo.getParam("WorkSeason").trim()) + 19110;
		BigDecimal iPerfCnt = new BigDecimal(titaVo.getParam("PerfCnt").trim());
		BigDecimal iPerfAmt = new BigDecimal(titaVo.getParam("PerfAmt").trim());


		pfIntranetAdjust.setCustNo(iCustNo);
		pfIntranetAdjust.setFacmNo(iFacmNo);
		pfIntranetAdjust.setBormNo(iBormNo);

		pfIntranetAdjust.setPerfDate(iPerfDate);
		pfIntranetAdjust.setWorkMonth(iWorkMonth);
		pfIntranetAdjust.setWorkSeason(iWorkSeason);

		pfIntranetAdjust.setIntroducer(titaVo.getParam("Introducer").trim());
		pfIntranetAdjust.setBsOfficer(titaVo.getParam("BsOfficer").trim());

		pfIntranetAdjust.setPerfAmt(iPerfAmt);
		pfIntranetAdjust.setPerfCnt(iPerfCnt);

		BigDecimal bigZero = new BigDecimal("0");
		BigDecimal bigNeg = new BigDecimal("-1");
		BigDecimal iSumAmt = new BigDecimal(titaVo.getParam("SumAmt").trim());
		BigDecimal iSumCnt = new BigDecimal(titaVo.getParam("SumCnt").trim());

		if ("-".equals(titaVo.getParam("SumAmtSign").trim())) {
			iSumAmt = iSumAmt.multiply(bigNeg);
		} 
		
		if ("-".equals(titaVo.getParam("SumCntSign").trim())) {
			iSumCnt = iSumCnt.multiply(bigNeg);
		} 
		
		pfIntranetAdjust.setSumAmt(iSumAmt);
		pfIntranetAdjust.setSumCnt(iSumCnt);

		pfIntranetAdjust.setUnitType(titaVo.getParam("UnitType").trim());
		pfIntranetAdjust.setUnitCode(titaVo.getParam("UnitCode").trim());
		pfIntranetAdjust.setDistCode(titaVo.getParam("DistCode").trim());
		pfIntranetAdjust.setDeptCode(titaVo.getParam("DeptCode").trim());

		return pfIntranetAdjust;
	}
}