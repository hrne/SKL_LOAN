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
import com.st1.itx.db.domain.PfCoOfficerLogId;
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

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 40;

		iFunctionCode = Integer.valueOf(titaVo.getParam("FunctionCd"));
		iEmpNo = titaVo.getParam("EmpNo");
		iEffectiveDate = Integer.valueOf(titaVo.getParam("EffectiveDate"));
		iIneffectiveDate = Integer.valueOf(titaVo.getParam("IneffectiveDate"));
		String iEmpClass = titaVo.getParam("EmpClass").trim();
		String iClassPass = titaVo.getParam("ClassPass");
		String iAreaCode = titaVo.getParam("UnitCode");
		String iDistCode = titaVo.getParam("DistCode");
		String iDeptCode = titaVo.getParam("DeptCode");
		String iAreaItem = titaVo.getParam("UnitCodeX");
		String iDistItem = titaVo.getParam("DistCodeX");
		String iDeptItem = titaVo.getParam("DeptCodeX");

		// 資料庫協辦人員等級檔的第一筆資料寫入協辦人員等級歷程檔
		insertOrgLog(titaVo);

		PfCoOfficer tPfCoOfficer = null;
		PfCoOfficerId tPfCoOfficerId;
		// 1跟3為類似的 新增 ，2是修改，4是刪除
		switch (iFunctionCode) {
		
		case 1:
		case 3:
			checkEffectiveDate(titaVo);

			// 全新資料
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
			break;
		case 2:
			tPfCoOfficerId = new PfCoOfficerId();
			tPfCoOfficerId.setEffectiveDate(iEffectiveDate);
			tPfCoOfficerId.setEmpNo(iEmpNo);
			tPfCoOfficer = pfCoOfficerService.holdById(tPfCoOfficerId, titaVo);

			tPfCoOfficer.setClassPass(iClassPass);
			if (iIneffectiveDate != 19110000) {
				tPfCoOfficer.setIneffectiveDate(iIneffectiveDate);
			}
			tPfCoOfficer.setEmpClass(iEmpClass);
			try {
				tPfCoOfficer = pfCoOfficerService.update2(tPfCoOfficer, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "修改時發生錯誤");
			}
		
			break;

		case 4:
			tPfCoOfficerId = new PfCoOfficerId();
			tPfCoOfficerId.setEffectiveDate(iEffectiveDate);
			tPfCoOfficerId.setEmpNo(iEmpNo);
			tPfCoOfficer = pfCoOfficerService.holdById(tPfCoOfficerId, titaVo);
			if (tPfCoOfficer == null) {
				throw new LogicException("E0008", "刪除時發生錯誤，該資料不存在");
			}

			try {
				pfCoOfficerService.delete(tPfCoOfficer, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "刪除時發生錯誤");
			}

			break;
		default:
			break;
		}
		updateLog(tPfCoOfficer, titaVo);
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void checkEffectiveDate(TitaVo titaVo) throws LogicException {
		Slice<PfCoOfficer> slPfCoOfficer = null;
		slPfCoOfficer = pfCoOfficerService.findByEmpNo(iEmpNo, this.index, this.limit, titaVo);
		if (slPfCoOfficer != null) {
			for (PfCoOfficer t : slPfCoOfficer) {
				if (iEffectiveDate == t.getEffectiveDate()) {
					throw new LogicException("E0015", "該生效日期已存在");
				}
				if (iEffectiveDate < t.getEffectiveDate() && iIneffectiveDate >= t.getIneffectiveDate()) {
					throw new LogicException("E0015", "該停效日" + iIneffectiveDate + "已超過原生效日" + t.getEffectiveDate());// 檢查錯誤
				}
				if (iIneffectiveDate > 0) {
					if (iEffectiveDate > t.getEffectiveDate() && iIneffectiveDate <= t.getIneffectiveDate()) {
						if (t.getIneffectiveDate() == 9991231) {
							throw new LogicException("E0015", "該生效日" + t.getEffectiveDate() + "需設定停效日期");// 檢查錯誤
						} 
						else if(iEffectiveDate > t.getIneffectiveDate() && iIneffectiveDate <= t.getIneffectiveDate()){
							throw new LogicException("E0015","該生效日" + iEffectiveDate + "已超過原生效日" + t.getIneffectiveDate()
							 + "且該停效日"+iIneffectiveDate +"等於或小於原停效日" + t.getEffectiveDate());// 檢查錯誤
						}
					}
				}
			}
		}
	}

	private void insertOrgLog(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		this.info("insertOrgLogiEmpNo    = " + iEmpNo);
//		Slice<PfCoOfficerLog> slPfCoOfficerLog = pfCoOfficerLogService.findEmpNoEq(iEmpNo, 0, Integer.MAX_VALUE,
//				titaVo);
//
//		if (slPfCoOfficerLog == null && iFunctionCode == 1 ) {
			PfCoOfficer oPf = pfCoOfficerService.effectiveDateFirst(iEmpNo, 0, 99991231, titaVo);
			if (oPf != null) {
				PfCoOfficerLog tPfCoOfficerLog = new PfCoOfficerLog();
				
				tPfCoOfficerLog.setEmpNo(oPf.getEmpNo());
				tPfCoOfficerLog.setEffectiveDate(oPf.getEffectiveDate());
				tPfCoOfficerLog.setIneffectiveDate(oPf.getIneffectiveDate());
				tPfCoOfficerLog.setEmpClass(oPf.getEmpClass());
				tPfCoOfficerLog.setClassPass(oPf.getClassPass());
				tPfCoOfficerLog.setAreaCode(oPf.getAreaCode());
				tPfCoOfficerLog.setAreaItem(oPf.getAreaItem());
				tPfCoOfficerLog.setDeptCode(oPf.getDeptCode());
				tPfCoOfficerLog.setDeptItem(oPf.getDeptItem());
				tPfCoOfficerLog.setDistCode(oPf.getDistCode());
				tPfCoOfficerLog.setDistItem(oPf.getDistItem());
				tPfCoOfficerLog.setUpdateTlrNo(oPf.getLastUpdateEmpNo());
				tPfCoOfficerLog.setUpdateDate(oPf.getLastUpdate());
				tPfCoOfficerLog.setFunctionCode(1);

				try {
					pfCoOfficerLogService.insert(tPfCoOfficerLog, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "新增歷程資料時發生錯誤");
				}
			}
		}

//	}

	private void updateLog(PfCoOfficer oPf, TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);

		PfCoOfficerLog tPfCoOfficerLog = new PfCoOfficerLog();
		
		tPfCoOfficerLog.setEmpNo(oPf.getEmpNo());
		tPfCoOfficerLog.setEffectiveDate(oPf.getEffectiveDate());
		tPfCoOfficerLog.setIneffectiveDate(oPf.getIneffectiveDate());
		tPfCoOfficerLog.setEmpClass(oPf.getEmpClass());
		tPfCoOfficerLog.setClassPass(oPf.getClassPass());
		tPfCoOfficerLog.setAreaCode(oPf.getAreaCode());
		tPfCoOfficerLog.setAreaItem(oPf.getAreaItem());
		tPfCoOfficerLog.setDeptCode(oPf.getDeptCode());
		tPfCoOfficerLog.setDeptItem(oPf.getDeptItem());
		tPfCoOfficerLog.setDistCode(oPf.getDistCode());
		tPfCoOfficerLog.setDistItem(oPf.getDistItem());
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
