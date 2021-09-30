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
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.PfCoOfficerLogService;
import com.st1.itx.db.service.PfCoOfficerService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;

@Component("L5407")
@Scope("prototype")

/**
 * 房貸協辦人員等級維護
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L5407 extends TradeBuffer {
	/* 轉型共用工具 */

	@Autowired
	public PfCoOfficerService iPfCoOfficerService;
	@Autowired
	public PfCoOfficerLogService iPfCoOfficerLogService;
	@Autowired
	public CdEmpService iCdEmpService;
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public DateUtil iDateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 40;

		String iFunctionCd = titaVo.getParam("FunctionCd");
		String iEmpNo = titaVo.getParam("EmpNo");
		int iEffectiveDate = Integer.valueOf(titaVo.getParam("EffectiveDate")) + 19110000;
		int iIneffectiveDate = Integer.valueOf(titaVo.getParam("IneffectiveDate")) + 19110000;
		String iEmpClass = titaVo.getParam("EmpClass").trim();
		String iClassPass = titaVo.getParam("ClassPass");
		String iAreaCode = titaVo.getParam("UnitCode");
		String iDistCode = titaVo.getParam("DistCode");
		String iDeptCode = titaVo.getParam("DeptCode");
		String iAreaItem = titaVo.getParam("UnitCodeX");
		String iDistItem = titaVo.getParam("DistCodeX");
		String iDeptItem = titaVo.getParam("DeptCodeX");

		PfCoOfficer iPfCoOfficer, nPfCoOfficer = new PfCoOfficer();
		PfCoOfficerId nPfCoOfficerId = new PfCoOfficerId();
		switch (iFunctionCd) {
		case "1": // 1跟3為類似的 新增 ，2是修改，4是刪除
			Slice<PfCoOfficer> xPfCoOfficer = null;
			xPfCoOfficer = iPfCoOfficerService.findByEmpNo(iEmpNo, this.index, this.limit, titaVo);
			if (xPfCoOfficer != null) {
				for (PfCoOfficer xxPfCoOfficer : xPfCoOfficer) {
					if (xxPfCoOfficer.getEffectiveDate() == Integer.valueOf(titaVo.getParam("EffectiveDate"))) {
						throw new LogicException("E0005", "新增時發生錯誤，該生效日期已存在");
					}
				}
			}
			iPfCoOfficer = iPfCoOfficerService.findByEmpNoFirst(iEmpNo, titaVo);
			if (iPfCoOfficer != null) {
				if (iPfCoOfficer.getEffectiveDate() > Integer.valueOf(titaVo.getParam("EffectiveDate"))) {
					throw new LogicException("E0005", "新增時發生錯誤，新生效日需大於舊生效日");
				}
			}
			// 全新資料
			nPfCoOfficerId.setEmpNo(iEmpNo);
			nPfCoOfficerId.setEffectiveDate(iEffectiveDate);
			nPfCoOfficer.setPfCoOfficerId(nPfCoOfficerId);
			nPfCoOfficer.setEmpClass(iEmpClass);
			nPfCoOfficer.setClassPass(iClassPass);
			if (iIneffectiveDate != 19110000) {
				nPfCoOfficer.setIneffectiveDate(iIneffectiveDate);
			}
			nPfCoOfficer.setAreaCode(iAreaCode);
			nPfCoOfficer.setDistCode(iDistCode);
			nPfCoOfficer.setDeptCode(iDeptCode);
			nPfCoOfficer.setAreaItem(iAreaItem);
			nPfCoOfficer.setDistItem(iDistItem);
			nPfCoOfficer.setDeptItem(iDeptItem);

			try {
				iPfCoOfficerService.insert(nPfCoOfficer, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "新增時發生錯誤，該生效日期已存在");
			}
			UpdateLog(titaVo);
			break;
		case "2":
			PfCoOfficerId oPfCoOfficerId = new PfCoOfficerId();
			oPfCoOfficerId.setEffectiveDate(iEffectiveDate);
			oPfCoOfficerId.setEmpNo(iEmpNo);
			PfCoOfficer oPfCoOfficer = iPfCoOfficerService.holdById(oPfCoOfficerId, titaVo);
			PfCoOfficer cPfCoOfficer = (PfCoOfficer) iDataLog.clone(oPfCoOfficer);

			oPfCoOfficer.setClassPass(iClassPass);
			oPfCoOfficer.setIneffectiveDate(iIneffectiveDate);
			oPfCoOfficer.setEmpClass(iEmpClass);
			try {
				oPfCoOfficer = iPfCoOfficerService.update2(oPfCoOfficer, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "修改時發生錯誤");
			}
			// 紀錄變更前變更後
			iDataLog.setEnv(titaVo, cPfCoOfficer, oPfCoOfficer);
			iDataLog.exec();
			UpdateLog(titaVo);
			break;
		case "3":
			Slice<PfCoOfficer> x3PfCoOfficer = null;
			x3PfCoOfficer = iPfCoOfficerService.findByEmpNo(iEmpNo, this.index, this.limit, titaVo);
			if (x3PfCoOfficer != null) {
				for (PfCoOfficer xx3PfCoOfficer : x3PfCoOfficer) {
					if (xx3PfCoOfficer.getEffectiveDate() == Integer.valueOf(titaVo.getParam("EffectiveDate"))) {
						throw new LogicException("E0005", "新增時發生錯誤，該生效日期已存在");
					}
				}
			}
			iPfCoOfficer = iPfCoOfficerService.findByEmpNoFirst(iEmpNo, titaVo);
			if (iPfCoOfficer != null) {
				if (iPfCoOfficer.getEffectiveDate() > Integer.valueOf(titaVo.getParam("EffectiveDate"))) {
					throw new LogicException("E0005", "新增時發生錯誤，新生效日需大於舊生效日");
				}
			}
			// 全新資料
			nPfCoOfficerId.setEmpNo(iEmpNo);
			nPfCoOfficerId.setEffectiveDate(iEffectiveDate);
			nPfCoOfficer.setPfCoOfficerId(nPfCoOfficerId);
			nPfCoOfficer.setEmpClass(iEmpClass);
			nPfCoOfficer.setClassPass(iClassPass);
			if (iIneffectiveDate != 19110000) {
				nPfCoOfficer.setIneffectiveDate(iIneffectiveDate);
			}
			nPfCoOfficer.setAreaCode(iAreaCode);
			nPfCoOfficer.setDistCode(iDistCode);
			nPfCoOfficer.setDeptCode(iDeptCode);
			nPfCoOfficer.setAreaItem(iAreaItem);
			nPfCoOfficer.setDistItem(iDistItem);
			nPfCoOfficer.setDeptItem(iDeptItem);
			try {
				iPfCoOfficerService.insert(nPfCoOfficer, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "新增時發生錯誤，該生效日期已存在");
			}
			UpdateLog(titaVo);
			break;
		case "4":
			PfCoOfficerId iPfCoOfficerId = new PfCoOfficerId();
			iPfCoOfficerId.setEffectiveDate(iEffectiveDate);
			iPfCoOfficerId.setEmpNo(iEmpNo);
			iPfCoOfficer = iPfCoOfficerService.holdById(iPfCoOfficerId, titaVo);
			if (iPfCoOfficer == null) {
				throw new LogicException("E0008", "刪除時發生錯誤，該資料不存在");
			}

			try {
				iPfCoOfficerService.delete(iPfCoOfficer, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "刪除時發生錯誤");
			}
			UpdateLog(titaVo);
			
			break;
		default:
			break;
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
	
	private void UpdateLog(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		String iFunctionCd = titaVo.getParam("FunctionCd");
		String iEmpNo = titaVo.getParam("EmpNo");
		int iEffectiveDate = Integer.valueOf(titaVo.getParam("EffectiveDate")) + 19110000;
		int iIneffectiveDate = Integer.valueOf(titaVo.getParam("IneffectiveDate")) + 19110000;
		String iEmpClass = titaVo.getParam("EmpClass").trim();
		String iClassPass = titaVo.getParam("ClassPass");
		String iAreaCode = titaVo.getParam("UnitCode");
		String iDistCode = titaVo.getParam("DistCode");
		String iDeptCode = titaVo.getParam("DeptCode");
		String iAreaItem = titaVo.getParam("UnitCodeX");
		String iDistItem = titaVo.getParam("DistCodeX");
		String iDeptItem = titaVo.getParam("DeptCodeX");
		int iSerialNo = 0;
		PfCoOfficerLog iPfCoOfficerLog = new PfCoOfficerLog();
		PfCoOfficerLogId iPfCoOfficerLogId = new PfCoOfficerLogId();
		if (iFunctionCd.equals("4")){ //刪除
			Slice<PfCoOfficerLog> dPfCoOfficerLog = null;
			dPfCoOfficerLog = iPfCoOfficerLogService.otherEq(iEmpNo, iEffectiveDate, 0, Integer.MAX_VALUE, titaVo);
			for (PfCoOfficerLog ddPfCoOfficerLog :dPfCoOfficerLog) {
				try {
					iPfCoOfficerLogService.delete(ddPfCoOfficerLog, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "刪除歷程資料時發生錯誤");
				}
			}
			
		}else {
			Slice<PfCoOfficerLog> rPfCoOfficerLog = null;
			rPfCoOfficerLog = iPfCoOfficerLogService.otherEq(iEmpNo, iEffectiveDate, this.index, this.limit, titaVo);
			if (rPfCoOfficerLog == null) {
				iSerialNo = 0;
			}else {
				iSerialNo = rPfCoOfficerLog.getContent().size()+1;
			}
			if (iIneffectiveDate != 19110000) {
				iPfCoOfficerLog.setIneffectiveDate(iIneffectiveDate);
			}
			iPfCoOfficerLog.setEmpClass(iEmpClass);
			iPfCoOfficerLog.setClassPass(iClassPass);
			iPfCoOfficerLog.setAreaCode(iAreaCode);
			iPfCoOfficerLog.setAreaItem(iAreaItem);
			iPfCoOfficerLog.setDeptCode(iDeptCode);
			iPfCoOfficerLog.setDeptItem(iDeptItem);
			iPfCoOfficerLog.setDistCode(iDistCode);
			iPfCoOfficerLog.setDistItem(iDistItem);
			iPfCoOfficerLogId.setEffectiveDate(iEffectiveDate);
			iPfCoOfficerLogId.setEmpNo(iEmpNo);
			iPfCoOfficerLogId.setSerialNo(iSerialNo);
			iPfCoOfficerLog.setPfCoOfficerLogId(iPfCoOfficerLogId);
			
			try {
				iPfCoOfficerLogService.insert(iPfCoOfficerLog, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "新增歷程資料時發生錯誤");
			}	
		}
	}
}
