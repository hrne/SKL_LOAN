package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBonusCo;
import com.st1.itx.db.domain.PfCoOfficer;
import com.st1.itx.db.domain.PfCoOfficerId;
import com.st1.itx.db.domain.PfCoOfficerLog;
import com.st1.itx.db.domain.PfReward;
import com.st1.itx.db.service.CdBonusCoService;
import com.st1.itx.db.service.PfCoOfficerLogService;
import com.st1.itx.db.service.PfCoOfficerService;
import com.st1.itx.db.service.PfRewardService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 房貸協辦人員等級維護
 * 
 * @author Fegie
 * @version 1.0.0
 */
@Component("L5407")
@Scope("prototype")
public class L5407 extends TradeBuffer {

	@Autowired
	private PfCoOfficerService pfCoOfficerService;

	@Autowired
	private PfCoOfficerLogService pfCoOfficerLogService;

	@Autowired
	private PfRewardService pfRewardService;

	@Autowired
	private CdBonusCoService cdBonusCoService;

	@Autowired
	private DateUtil dateUtil;

	@Autowired
	Parse parse;

	private int iFunctionCode = 0;
	private String iEmpNo = "";
	private int iEffectiveDate = 0;
	private int iIneffectiveDate = 0;
	private PfCoOfficer tPfCoOfficer = null;
	private PfCoOfficerId tPfCoOfficerId;
	private String iEmpClass = "";
	private String iClassPass = "";
	private String iAreaCode = "";
	private String iDistCode = "";
	private String iDeptCode = "";
	private String iAreaItem = "";
	private String iDistItem = "";
	private String iDeptItem = "";
	private String orignalEmpClass = "";
	private String orignalClassPass = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		iFunctionCode = Integer.valueOf(titaVo.getParam("FunctionCd"));
		iEmpNo = titaVo.getParam("EmpNo");
		iEffectiveDate = Integer.valueOf(titaVo.getParam("EffectiveDate"));
		iIneffectiveDate = Integer.valueOf(titaVo.getParam("IneffectiveDate"));
		iEmpClass = titaVo.getParam("EmpClass");
		iClassPass = titaVo.getParam("ClassPass");
		iAreaCode = titaVo.getParam("UnitCode");
		iDistCode = titaVo.getParam("DistCode");
		iDeptCode = titaVo.getParam("DeptCode");
		iAreaItem = titaVo.getParam("UnitCodeX");
		iDistItem = titaVo.getParam("DistCodeX");
		iDeptItem = titaVo.getParam("DeptCodeX");
		// log無資料，新增最初歷程檔
		insertPfCoOfficerLogFirst(iEmpNo,titaVo);

		switch (iFunctionCode) {
		case 1: // 新增
		case 3: // 複製
		case 7: // 調職異動
		case 8: // 考核職級異動
			updateOrignal(titaVo);
			inSertPfCoOfficer(titaVo);
			// 職級不同重算獎金
			if (!iEmpClass.equals(orignalEmpClass) || !iClassPass.equals(orignalClassPass)) {
				updatePfReward(titaVo);
			}
			break;
		case 2:
		case 6: // 離職異動
			updatePfCoOfficer(titaVo);
			// 職級不同重算獎金
			if (!iEmpClass.equals(orignalEmpClass) || !iClassPass.equals(orignalClassPass)) {
				updatePfReward(titaVo);
			}
			break;
		case 4: // 刪除
			deletePfCoOfficer(titaVo);
			break;

		default:
			break;
		}
		// 將異動後資料寫入歷程檔
		insertPfCoOfficerLogAfter(titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 重算協辦人員協辦獎金
	private void updatePfReward(TitaVo titaVo) throws LogicException {
		this.info("updatePfReward  ... ");
		Slice<PfReward> slPfReward = pfRewardService.findByPerfDate(iEffectiveDate + 19110000, 99991231, 0,
				Integer.MAX_VALUE, titaVo);
		if (slPfReward == null) {
			return;
		}
		int workMonthCd = 0;// 協辦獎勵津貼標準設定適用工作年月
		Slice<CdBonusCo> slCdBonusCo = null;
		ArrayList<PfReward> lPfRewardUpdate = new ArrayList<PfReward>();
		for (PfReward tPfReward : slPfReward.getContent()) {
			if (!iEmpNo.equals(tPfReward.getCoorgnizer())
					|| tPfReward.getCoorgnizerBonus().compareTo(BigDecimal.ZERO) == 0
					|| tPfReward.getCoorgnizerBonusDate() > 0) {
				return;
			}
			if (workMonthCd != tPfReward.getWorkMonth()) {
				CdBonusCo TCdBonusCo = cdBonusCoService.findWorkMonthFirst(tPfReward.getWorkMonth(), titaVo);
				if (TCdBonusCo == null) {
					throw new LogicException(titaVo, "E0001", "CdBonusCo 協辦獎金標準設定" + tPfReward.getWorkMonth()); // 查詢資料不存在
				}
				workMonthCd = tPfReward.getWorkMonth();
				slCdBonusCo = cdBonusCoService.findYearMonth(workMonthCd, workMonthCd, 0, Integer.MAX_VALUE, titaVo);
			}

			BigDecimal orignalBonus = BigDecimal.ZERO;
			BigDecimal updateBonus = BigDecimal.ZERO;
			for (CdBonusCo cd : slCdBonusCo.getContent()) {
				if (cd.getConditionCode() == 2 && cd.getCondition().equals(orignalEmpClass)) {
					if ("Y".equals(orignalClassPass))
						orignalBonus = cd.getClassPassBonus();
					else
						orignalBonus = cd.getBonus();
				}
				if (cd.getConditionCode() == 2 && cd.getCondition().equals(iEmpClass)) {
					if ("Y".equals(iClassPass))
						updateBonus = cd.getClassPassBonus();
					else
						updateBonus = cd.getBonus();
				}
			}
			if (tPfReward.getCoorgnizerBonus().compareTo(orignalBonus) == 0) {
				this.info("update PfReward updateBonus=" + updateBonus + tPfReward.toString());
				tPfReward.setCoorgnizerBonus(updateBonus);
				lPfRewardUpdate.add(tPfReward);
			} else {
				this.info("skip update PfReward updateBonus=" + updateBonus + ", orignalBonus=" + orignalBonus
						+ tPfReward.toString());
			}
		}
		if (lPfRewardUpdate.size() > 0) {
			try {
				pfRewardService.updateAll(lPfRewardUpdate, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "修改時發生錯誤");
			}
		}
	}

	/**
	 * 核核算底稿新增至歷程檔 call by LP005
	 * 
	 * @param empNo                協辦人員
	 * @param effectiveDate        原生效日
	 * @param evaluteEffectiveDate 考核生效日
	 * @param evalueChgClass       考核職級
	 * @param titaVo               TitaVo
	 * @throws LogicException ....
	 */
	public void insertEvalutePfCoOfficerLog(String empNo, int effectiveDate, int evaluteEffectiveDate,
			String evalueChgClass, TitaVo titaVo) throws LogicException {
		this.info("insertPfCoOfficerLog7  ... ");
		// log無資料，新增最初歷程檔
		insertPfCoOfficerLogFirst(empNo, titaVo);

		tPfCoOfficerId = new PfCoOfficerId();
		tPfCoOfficerId.setEffectiveDate(effectiveDate);
		tPfCoOfficerId.setEmpNo(empNo);
		tPfCoOfficer = pfCoOfficerService.holdById(tPfCoOfficerId, titaVo);
		if (tPfCoOfficer == null) {
			throw new LogicException("E0006", "PfCoOffice資料不存在"); // 鎖定資料時，發生錯誤
		}
		PfCoOfficerLog tPfCoOfficerLog = new PfCoOfficerLog();
		tPfCoOfficerLog.setEmpNo(empNo);
		tPfCoOfficerLog.setEffectiveDate(evaluteEffectiveDate);
		tPfCoOfficerLog.setIneffectiveDate(9991231);
		tPfCoOfficerLog.setEmpClass(evalueChgClass);
		tPfCoOfficerLog.setClassPass(tPfCoOfficer.getClassPass());
		tPfCoOfficerLog.setAreaCode(tPfCoOfficer.getAreaCode());
		tPfCoOfficerLog.setAreaItem(tPfCoOfficer.getAreaItem());
		tPfCoOfficerLog.setDeptCode(tPfCoOfficer.getDeptCode());
		tPfCoOfficerLog.setDeptItem(tPfCoOfficer.getDeptItem());
		tPfCoOfficerLog.setDistCode(tPfCoOfficer.getDistCode());
		tPfCoOfficerLog.setDistItem(tPfCoOfficer.getDistItem());
		tPfCoOfficerLog.setUpdateTlrNo(titaVo.getTlrNo());
		tPfCoOfficerLog
				.setUpdateDate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
		tPfCoOfficerLog.setFunctionCode(9);

		try {
			pfCoOfficerLogService.insert(tPfCoOfficerLog, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0005", "新增歷程資料時發生錯誤");
		}
	}

	private void inSertPfCoOfficer(TitaVo titaVo) throws LogicException {
		tPfCoOfficerId = new PfCoOfficerId();
		tPfCoOfficer = new PfCoOfficer();
		tPfCoOfficerId.setEmpNo(iEmpNo);
		tPfCoOfficerId.setEffectiveDate(iEffectiveDate);
		tPfCoOfficer.setEmpNo(iEmpNo);
		tPfCoOfficer.setEffectiveDate(iEffectiveDate);
		tPfCoOfficer.setPfCoOfficerId(tPfCoOfficerId);
		tPfCoOfficer.setEmpClass(iEmpClass);
		tPfCoOfficer.setClassPass(iClassPass);
		tPfCoOfficer.setIneffectiveDate(iIneffectiveDate > 0 ? iIneffectiveDate : 9991231);
		tPfCoOfficer.setAreaCode(iAreaCode);
		tPfCoOfficer.setDistCode(iDistCode);
		tPfCoOfficer.setDeptCode(iDeptCode);
		tPfCoOfficer.setAreaItem(iAreaItem);
		tPfCoOfficer.setDistItem(iDistItem);
		tPfCoOfficer.setDeptItem(iDeptItem);
		try {
			pfCoOfficerService.insert(tPfCoOfficer, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0005", "新增時發生錯誤，該生效日期已存在");
		}
	}

	private void updatePfCoOfficer(TitaVo titaVo) throws LogicException {
		tPfCoOfficerId = new PfCoOfficerId();
		tPfCoOfficerId.setEffectiveDate(iEffectiveDate);
		tPfCoOfficerId.setEmpNo(iEmpNo);
		tPfCoOfficer = pfCoOfficerService.holdById(tPfCoOfficerId, titaVo);
		if (tPfCoOfficer == null) {
			throw new LogicException("E0006", "PfCoOffice資料不存在"); // 鎖定資料時，發生錯誤
		}
		orignalEmpClass = tPfCoOfficer.getEmpClass();
		orignalClassPass = tPfCoOfficer.getClassPass();
		tPfCoOfficer.setClassPass(iClassPass);
		if (iIneffectiveDate != 0) {
			tPfCoOfficer.setIneffectiveDate(iIneffectiveDate);
		}
		tPfCoOfficer.setEmpClass(iEmpClass);

		tPfCoOfficer.setAreaCode(iAreaCode);
		tPfCoOfficer.setDistCode(iDistCode);
		tPfCoOfficer.setDeptCode(iDeptCode);
		tPfCoOfficer.setAreaItem(iAreaItem);
		tPfCoOfficer.setDistItem(iDistItem);
		tPfCoOfficer.setDeptItem(iDeptItem);

		try {
			tPfCoOfficer = pfCoOfficerService.update(tPfCoOfficer, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0005", "修改時發生錯誤");
		}
	}

	private void deletePfCoOfficer(TitaVo titaVo) throws LogicException {
		tPfCoOfficerId = new PfCoOfficerId();
		tPfCoOfficerId.setEffectiveDate(iEffectiveDate);
		tPfCoOfficerId.setEmpNo(iEmpNo);
		tPfCoOfficer = pfCoOfficerService.holdById(tPfCoOfficerId, titaVo);
		if (tPfCoOfficer == null) {
			throw new LogicException("E0006", "PfCoOffice資料不存在"); // 鎖定資料時，發生錯誤
		}

		try {
			pfCoOfficerService.delete(tPfCoOfficer, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0005", "刪除時發生錯誤");
		}
	}

	private void updateOrignal(TitaVo titaVo) throws LogicException {
		this.info("updateOrignal  ... ");
		tPfCoOfficer = pfCoOfficerService.findByEmpNoFirst(iEmpNo, titaVo);
		if (tPfCoOfficer != null) {
			if (iEffectiveDate <= tPfCoOfficer.getEffectiveDate()) {
				throw new LogicException("E0015", "該生效日期需大於原生效日" + tPfCoOfficer.getEffectiveDate());// 檢查錯誤
			}
			orignalEmpClass = tPfCoOfficer.getEmpClass();
			orignalClassPass = tPfCoOfficer.getClassPass();
			// 設定停效日期為新生效日前一日
			dateUtil.init();
			dateUtil.setDate_1(iEffectiveDate);
			dateUtil.setDays(-1);
			int newIneffectiveDate = dateUtil.getCalenderDay();
			if (newIneffectiveDate < tPfCoOfficer.getIneffectiveDate()) {
				tPfCoOfficer.setIneffectiveDate(newIneffectiveDate);
			}
		}
	}

	// log無資料，新增最初歷程檔
	private void insertPfCoOfficerLogFirst(String empNo, TitaVo titaVo) throws LogicException {
		this.info("insertPfCoOfficerLogFirst  ... ");
		Slice<PfCoOfficerLog> slPfCoOfficerLog = pfCoOfficerLogService.findEmpNoEq(empNo, 0, 1, titaVo);
		if (slPfCoOfficerLog != null) {
			return;
		}
		// log無資料則新增最初歷程檔
		tPfCoOfficer = pfCoOfficerService.findByEmpNoFirst(empNo, titaVo);
		if (tPfCoOfficer != null) {
			PfCoOfficerLog tPfCoOfficerLog = new PfCoOfficerLog();
			tPfCoOfficerLog.setEmpNo(tPfCoOfficer.getEmpNo());
			tPfCoOfficerLog.setEffectiveDate(tPfCoOfficer.getEffectiveDate());
			tPfCoOfficerLog.setIneffectiveDate(tPfCoOfficer.getIneffectiveDate());
			tPfCoOfficerLog.setEmpClass(tPfCoOfficer.getEmpClass());
			tPfCoOfficerLog.setClassPass(tPfCoOfficer.getClassPass());
			tPfCoOfficerLog.setAreaCode(tPfCoOfficer.getAreaCode());
			tPfCoOfficerLog.setAreaItem(tPfCoOfficer.getAreaItem());
			tPfCoOfficerLog.setDeptCode(tPfCoOfficer.getDeptCode());
			tPfCoOfficerLog.setDeptItem(tPfCoOfficer.getDeptItem());
			tPfCoOfficerLog.setDistCode(tPfCoOfficer.getDistCode());
			tPfCoOfficerLog.setDistItem(tPfCoOfficer.getDistItem());
			tPfCoOfficerLog.setUpdateTlrNo(tPfCoOfficer.getLastUpdateEmpNo());
			tPfCoOfficerLog.setUpdateDate(tPfCoOfficer.getLastUpdate());
			tPfCoOfficerLog.setFunctionCode(1);
			try {
				pfCoOfficerLogService.insert(tPfCoOfficerLog, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "新增歷程資料時發生錯誤");
			}
		}
	}

	// 將異動後資料寫入歷程檔
	private void insertPfCoOfficerLogAfter(TitaVo titaVo) throws LogicException {
		this.info("insertPfCoOfficerLog  ... ");
		PfCoOfficerLog tPfCoOfficerLog = new PfCoOfficerLog();
		tPfCoOfficerLog.setEmpNo(tPfCoOfficer.getEmpNo());
		tPfCoOfficerLog.setEffectiveDate(tPfCoOfficer.getEffectiveDate());
		tPfCoOfficerLog.setIneffectiveDate(tPfCoOfficer.getIneffectiveDate());
		tPfCoOfficerLog.setEmpClass(tPfCoOfficer.getEmpClass());
		tPfCoOfficerLog.setClassPass(tPfCoOfficer.getClassPass());
		tPfCoOfficerLog.setAreaCode(tPfCoOfficer.getAreaCode());
		tPfCoOfficerLog.setAreaItem(tPfCoOfficer.getAreaItem());
		tPfCoOfficerLog.setDeptCode(tPfCoOfficer.getDeptCode());
		tPfCoOfficerLog.setDeptItem(tPfCoOfficer.getDeptItem());
		tPfCoOfficerLog.setDistCode(tPfCoOfficer.getDistCode());
		tPfCoOfficerLog.setDistItem(tPfCoOfficer.getDistItem());
		tPfCoOfficerLog.setUpdateTlrNo(titaVo.getTlrNo());
		tPfCoOfficerLog
				.setUpdateDate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
		tPfCoOfficerLog.setFunctionCode(iFunctionCode);

		try {
			pfCoOfficerLogService.insert(tPfCoOfficerLog, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0005", "新增歷程資料時發生錯誤");
		}
	}
}
