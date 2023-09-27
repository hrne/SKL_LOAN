package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.PfItDetail;
import com.st1.itx.db.domain.PfItDetailAdjust;
import com.st1.itx.db.domain.TxControl;
import com.st1.itx.db.service.PfItDetailAdjustService;
import com.st1.itx.db.service.PfItDetailService;
import com.st1.itx.db.service.TxControlService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

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
	public TxControlService txControlService;

	@Autowired
	public DataLog dataLog;

	private PfItDetail pfItDetail = new PfItDetail();
	private String iFunCode;
	private int iOption;
	private int iCustNo;
	private int iFacmNo;
	private int iBormNo;
	private int iWorkMonth;
	private int iAdjRange;
	private long iLogNo;
	private BigDecimal iAdjPerfEqAmt;
	private BigDecimal iAdjPerfReward;
	private BigDecimal iAdjPerfAmt;
	private String iAdjCntingCode;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5501 ");
		this.totaVo.init(titaVo);

		iLogNo = Long.valueOf(titaVo.getParam("LogNo").trim());
		iFunCode = titaVo.getParam("FunCode").trim();
		iCustNo = Integer.valueOf(titaVo.getParam("CustNo").trim()); // 戶號
		iFacmNo = Integer.valueOf(titaVo.getParam("FacmNo").trim()); // 額度編號
		iBormNo = Integer.valueOf(titaVo.getParam("BormNo").trim()); // 撥款序號
		iWorkMonth = Integer.valueOf(titaVo.getParam("WorkMonth").trim()) + 191100; // 調整工作月
		iOption = Integer.valueOf(titaVo.getParam("Option").trim());
		iAdjRange = Integer.valueOf(titaVo.getParam("AdjRange").trim());
		iAdjPerfEqAmt = new BigDecimal(titaVo.getParam("AdjPerfEqAmt").trim());
		iAdjPerfReward = new BigDecimal(titaVo.getParam("AdjPerfReward").trim());
		iAdjPerfAmt = new BigDecimal(titaVo.getParam("AdjPerfAmt").trim());
		iAdjCntingCode = titaVo.getParam("AdjCntingCode").trim();

		pfItDetail = pfItDetailService.holdById(iLogNo, titaVo);
		if (pfItDetail == null) {
			throw new LogicException(titaVo, "E0001", "介紹人業績資料");
		}
		String controlCode = "L5510." + iWorkMonth + ".2";
		TxControl txControl = txControlService.findById(controlCode, titaVo);
		if (txControl != null) {
			throw new LogicException(titaVo, "E0010", "已產生媒體檔");
		}

		if (iOption == 1) {// 人員異動
			if (pfItDetail.getRepayType() == 0 || pfItDetail.getRepayType() == 4) {
				updateEmploee(titaVo);
			} else {
				throw new LogicException(titaVo, "E0010", ""); // 功能選擇錯誤
			}
			updateEmploee(titaVo);
		} else {
			switch (iFunCode) {

			case "1": // 新增業績調整資料
				if (pfItDetail.getRepayType() == 0) {
					createAdj(titaVo);
				} else {
					throw new LogicException(titaVo, "E0010", ""); // 功能選擇錯誤
				}
				break;

			case "2": // 修改業績資料
				// 修改撥款業績則保留業績原始資料
				if (pfItDetail.getRepayType() == 0) {
					updateAdjust(titaVo);
				}
				// 修改業績資料
				updateAdj(titaVo);
				break;

			case "4":// 刪除業績調整資料
				if (pfItDetail.getRepayType() == 0 || pfItDetail.getRepayType() == 4) {
					deleteAdj(titaVo);
				} else {
					throw new LogicException(titaVo, "E0010", ""); // 功能選擇錯誤
				}
				break;

			default:
				throw new LogicException(titaVo, "E0010", ""); // 功能選擇錯誤
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void updateEmploee(TitaVo titaVo) throws LogicException {
		PfItDetail pfItDetailOrg = (PfItDetail) dataLog.clone(pfItDetail);
		pfItDetail.setIntroducer(titaVo.getParam("Introducer").trim());
		pfItDetail.setUnitManager(titaVo.getParam("UnitManager").trim());
		pfItDetail.setDistManager(titaVo.getParam("DistManager").trim());
		pfItDetail.setDeptCode(titaVo.getParam("DeptCode").trim());
		pfItDetail.setDistCode(titaVo.getParam("DistCode").trim());
		pfItDetail.setUnitCode(titaVo.getParam("UnitCode").trim());
		try {
			pfItDetail = pfItDetailService.update(pfItDetail, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", e.getErrorMsg());
		}
		dataLog.setEnv(titaVo, pfItDetailOrg, pfItDetail);
		dataLog.exec("修改介紹人業績檔人員資料");
	}

	private void updateAdjust(TitaVo titaVo) throws LogicException {
		PfItDetailAdjust pfItDetailAdjust = pfItDetailAdjustService.findCustFacmBormFirst(iCustNo, iFacmNo, iBormNo,
				titaVo);
		if (pfItDetailAdjust == null) {
			pfItDetailAdjust = new PfItDetailAdjust();
			pfItDetailAdjust.setCustNo(pfItDetail.getCustNo());
			pfItDetailAdjust.setFacmNo(pfItDetail.getFacmNo());
			pfItDetailAdjust.setBormNo(pfItDetail.getBormNo());
			pfItDetailAdjust.setWorkMonth(pfItDetail.getWorkMonth());
			pfItDetailAdjust.setWorkSeason(pfItDetail.getWorkSeason());
			pfItDetailAdjust.setAdjCntingCode(pfItDetail.getCntingCode());
			pfItDetailAdjust.setAdjPerfEqAmt(pfItDetail.getPerfEqAmt());
			pfItDetailAdjust.setAdjPerfReward(pfItDetail.getPerfReward());
			pfItDetailAdjust.setAdjPerfAmt(pfItDetail.getPerfAmt());
			pfItDetailAdjust.setAdjRange(iAdjRange);
			pfItDetailAdjust.setCreateDate(pfItDetail.getCreateDate());
			pfItDetailAdjust.setCreateEmpNo(pfItDetail.getCreateEmpNo());
			pfItDetailAdjust.setLastUpdate(pfItDetail.getLastUpdate());
			pfItDetailAdjust.setLastUpdateEmpNo(pfItDetail.getLastUpdateEmpNo());
			try {
				pfItDetailAdjust = pfItDetailAdjustService.insert(pfItDetailAdjust, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg());
			}
		}
	}

	private void createAdj(TitaVo titaVo) throws LogicException {
		PfItDetail pf2 = (PfItDetail) dataLog.clone(pfItDetail);
		Slice<PfItDetail> slPfItDetail = pfItDetailService.findBormNoEq(iCustNo, iFacmNo, iBormNo, 0, Integer.MAX_VALUE,
				titaVo);
		if (slPfItDetail != null) {
			for (PfItDetail pf : slPfItDetail.getContent()) {
				if (pf.getWorkMonth() == iWorkMonth && pf.getRepayType() == 4) {
					throw new LogicException(titaVo, "E0015", "該工作月已有業績調整資料"); // 檢查錯誤
				}
			}
		}
		try {
			pfItDetail = pfItDetailService.update(pfItDetail, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", e.getErrorMsg());
		}
		pfItDetail = new PfItDetail();
		pfItDetail.setCustNo(pf2.getCustNo());// 戶號
		pfItDetail.setFacmNo(pf2.getFacmNo()); // 額度編號
		pfItDetail.setBormNo(pf2.getBormNo());// 撥款序號
		pfItDetail.setPerfDate(pf2.getPerfDate()); // 業績日期
		pfItDetail.setRepayType(4);
		pfItDetail.setPieceCode(pf2.getPieceCode()); // 計件代碼
		pfItDetail.setDrawdownDate(pf2.getDrawdownDate()); // 撥款日
		pfItDetail.setProdCode(pf2.getProdCode()); // 商品代碼
		pfItDetail.setDrawdownAmt(pf2.getDrawdownAmt()); // 撥款金額/追回金額
		pfItDetail.setUnitCode(pf2.getUnitCode()); // 單位代號CdEmp.CenterCode單位代號
		pfItDetail.setDistCode(pf2.getDistCode()); // 區部代號
		pfItDetail.setDeptCode(pf2.getDeptCode()); // 部室代號
		pfItDetail.setIntroducer(pf2.getIntroducer()); // 介紹人
		pfItDetail.setUnitManager(pf2.getUnitManager()); // 處經理代號
		pfItDetail.setDistManager(pf2.getDistManager()); // 區經理代號
		pfItDetail.setDeptManager(pf2.getDeptManager()); // 部經理代號
		pfItDetail.setPerfCnt(pf2.getPerfCnt());
		pfItDetail.setAdjRange(iAdjRange);
		pfItDetail.setWorkMonth(iWorkMonth);
		int season = 0;
		if (iWorkMonth % 100 <= 3)
			season = 1;
		else if (iWorkMonth % 100 <= 6)
			season = 2;
		else if (iWorkMonth % 100 <= 9)
			season = 3;
		else
			season = 4;
		pfItDetail.setWorkSeason((iWorkMonth / 100) * 10 + season);
		pfItDetail.setPerfEqAmt(iAdjPerfEqAmt);
		pfItDetail.setPerfReward(iAdjPerfReward);
		pfItDetail.setPerfAmt(iAdjPerfAmt);
		pfItDetail.setCntingCode(iAdjCntingCode);
		pfItDetail.setAdjRange(iAdjRange);
		this.info("pfItDetail=" + pfItDetail.toString());
		try {
			pfItDetail = pfItDetailService.insert(pfItDetail, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", e.getErrorMsg());
		}
	}

	private void updateAdj(TitaVo titaVo) throws LogicException {
		PfItDetail pfItDetail2 = (PfItDetail) dataLog.clone(pfItDetail);
		pfItDetail.setPerfEqAmt(iAdjPerfEqAmt);
		pfItDetail.setPerfReward(iAdjPerfReward);
		pfItDetail.setPerfAmt(iAdjPerfAmt);
		pfItDetail.setCntingCode(iAdjCntingCode);
		pfItDetail.setAdjRange(iAdjRange);
		this.info("pfItDetail=" + pfItDetail.toString());
		try {
			pfItDetail = pfItDetailService.update2(pfItDetail, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", e.getErrorMsg());
		}
		dataLog.setEnv(titaVo, pfItDetail2, pfItDetail);
		dataLog.exec("修改介紹人業績案件");

	}

	// 刪除調整資料，修改撥款則還原為原撥款業績資料
	private void deleteAdj(TitaVo titaVo) throws LogicException {
		PfItDetail pfItDetail2 = (PfItDetail) dataLog.clone(pfItDetail);

		if (pfItDetail.getRepayType() == 0) {
			PfItDetailAdjust pfItDetailAdjust = pfItDetailAdjustService.findCustFacmBormFirst(iCustNo, iFacmNo, iBormNo,
					titaVo);
			if (pfItDetailAdjust == null) {
				throw new LogicException(titaVo, "E0004", "pfItDetailAdjust"); // 刪除資料不存在
			}
			pfItDetail.setCntingCode(pfItDetailAdjust.getAdjCntingCode());
			pfItDetail.setPerfEqAmt(pfItDetailAdjust.getAdjPerfEqAmt());
			pfItDetail.setPerfReward(pfItDetailAdjust.getAdjPerfReward());
			pfItDetail.setPerfAmt(pfItDetailAdjust.getAdjPerfAmt());
			try {
				pfItDetail = pfItDetailService.update2(pfItDetail, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}
			pfItDetailAdjust = pfItDetailAdjustService.holdById(pfItDetailAdjust, titaVo);
			try {
				pfItDetailAdjustService.delete(pfItDetailAdjust, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", e.getErrorMsg());
			}
		}
		if (pfItDetail.getRepayType() == 4) {
			try {
				pfItDetailService.delete(pfItDetail, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}
		}
		dataLog.setEnv(titaVo, pfItDetail2, pfItDetail);
		dataLog.exec("刪除介紹人業績調整案件");

	}
}