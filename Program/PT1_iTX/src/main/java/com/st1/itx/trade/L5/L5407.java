package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.PfCoOfficer;
import com.st1.itx.db.domain.PfCoOfficerId;
import com.st1.itx.db.domain.PfCoOfficerLog;
import com.st1.itx.db.service.PfCoOfficerLogService;
import com.st1.itx.db.service.PfCoOfficerService;
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

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		iFunctionCode = Integer.valueOf(titaVo.getParam("FunctionCd"));
		iEmpNo = titaVo.getParam("EmpNo");
		iEffectiveDate = Integer.valueOf(titaVo.getParam("EffectiveDate"));
		iIneffectiveDate = Integer.valueOf(titaVo.getParam("IneffectiveDate"));
		// log無資料，新增最初歷程檔
		insertPfCoOfficerLogFirst(titaVo);

		switch (iFunctionCode) {
		case 1: // 新增
		case 3: // 複製
		case 8: // 考核職級異動
			updateOrignal(titaVo);
			inSertPfCoOfficer(titaVo);
			break;
		case 2:
		case 6: // 離調職異動
			updatePfCoOfficer(titaVo);
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

	/**
	 * 核核算底稿新增至歷程檔
	 * @param empNo 協辦人員
	 * @param effectiveDate 原生效日 
	 * @param evaluteEffectiveDate  考核生效日 
	 * @param evalueChgClass 考核職級
	 * @param titaVo TitaVo
	 * @throws LogicException ....
	 */
	public void insertEvalutePfCoOfficerLog(String empNo, int effectiveDate, int evaluteEffectiveDate,
			String evalueChgClass, TitaVo titaVo) throws LogicException {
		this.info("insertPfCoOfficerLog7  ... ");
		// log無資料，新增最初歷程檔
		insertPfCoOfficerLogFirst(titaVo);
		tPfCoOfficerId = new PfCoOfficerId();
		tPfCoOfficerId.setEffectiveDate(effectiveDate);
		tPfCoOfficerId.setEmpNo(empNo);
		tPfCoOfficer = pfCoOfficerService.findById(tPfCoOfficerId, titaVo);
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
		tPfCoOfficerLog.setFunctionCode(7);

		try {
			pfCoOfficerLogService.insert(tPfCoOfficerLog, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0005", "新增歷程資料時發生錯誤");
		}
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
		tPfCoOfficer.setClassPass(iClassPass);
		if (iIneffectiveDate != 19110000) {
			tPfCoOfficer.setIneffectiveDate(iIneffectiveDate);
		}
		tPfCoOfficer.setEmpClass(iEmpClass);
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
		tPfCoOfficer = pfCoOfficerService.findByEmpNoFirst(iEmpNo, titaVo);
		if (tPfCoOfficer != null) {
			if (iEffectiveDate <= tPfCoOfficer.getEffectiveDate()) {
				throw new LogicException("E0015", "該生效日期需大於原生效日" + tPfCoOfficer.getEffectiveDate());// 檢查錯誤
			}
			if (tPfCoOfficer.getIneffectiveDate() == 0) {
				// 設定停效日期為新生效日前一日
				dateUtil.init();
				dateUtil.setDate_1(iEffectiveDate);
				dateUtil.setDays(-1);
				tPfCoOfficer.setIneffectiveDate(dateUtil.getCalenderDay());
			}
		}
	}

	// log無資料，新增最初歷程檔
	private void insertPfCoOfficerLogFirst(TitaVo titaVo) throws LogicException {
		this.info("insertPfCoOfficerLogFirst  ... ");
		Slice<PfCoOfficerLog> slPfCoOfficerLog = pfCoOfficerLogService.findEmpNoEq(iEmpNo, 0, 1, titaVo);
		if (slPfCoOfficerLog != null) {
			return;
		}
		// log無資料則新增最初歷程檔
		tPfCoOfficer = pfCoOfficerService.findByEmpNoFirst(iEmpNo, titaVo);
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
