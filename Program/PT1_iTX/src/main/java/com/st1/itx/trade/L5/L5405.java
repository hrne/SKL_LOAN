package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.PfDeparment;
import com.st1.itx.db.domain.PfDeparmentId;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.PfDeparmentService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BcmCom;
import com.st1.itx.util.data.DataLog;

@Component("L5405")
@Scope("prototype")

/**
 * 房貸專員業績統計作業－更改目標金額、累計目標金額
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L5405 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public PfDeparmentService iPfDeparmentService;
	@Autowired
	public CdBcmService iCdBcmService;
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public BcmCom iBcmCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		int iFunctionCd = Integer.valueOf(titaVo.getParam("FunctionCd"));
		String iUnitCode = titaVo.getParam("UnitCode");
		String iDistCode = titaVo.getParam("DistCode");
		String iDeptCode = titaVo.getParam("DeptCode");
		String iEmpNo = titaVo.getParam("EmpNo");
		String iUnitItem = titaVo.getParam("UnitItem").trim();
		String iDistItem = titaVo.getParam("DistItem").trim();
		String iDeptItem = titaVo.getParam("DeptItem").trim();
		if (iUnitCode.equals("") && iDistCode.equals("") && iDeptCode.equals("") && iUnitItem.equals("") && iDistItem.equals("") && iDeptItem.equals("")) {
			throw new LogicException(titaVo, "E0005", ""); // 資料新建錯誤
		}
		if (titaVo.getParam("UnitCode").equals("")) {
			iUnitCode = " ";
		}
		if (titaVo.getParam("DistCode").equals("")) {
			iDistCode = " ";
		}
		if (titaVo.getParam("DeptCode").equals("")) {
			iDeptCode = " ";
		}
		String iDirectorCode = titaVo.getParam("DirectorCode");
		String iEmpName = titaVo.getParam("EmpName");
		String iDepartOfficer = titaVo.getParam("DepartOfficer");
		int iGoalCnt = Integer.valueOf(titaVo.getParam("GoalCnt"));
		BigDecimal iSumGoalCnt = new BigDecimal(titaVo.getParam("SumGoalCnt"));
		BigDecimal iGoalAmt = new BigDecimal(titaVo.getParam("GoalAmt"));
		BigDecimal iSumGoalAmt = new BigDecimal(titaVo.getParam("SumGoalAmt"));
		PfDeparmentId iPfDeparmentId = new PfDeparmentId();
		PfDeparment iPfDeparment = new PfDeparment();
		iPfDeparmentId.setDistCode(iDistCode);
		iPfDeparmentId.setDeptCode(iDeptCode);
		iPfDeparmentId.setUnitCode(iUnitCode);
		PfDeparment xPfDeparment = iPfDeparmentService.findById(iPfDeparmentId);
		switch (iFunctionCd) {
		case 1:
			if (xPfDeparment == null) {
				iPfDeparment.setEmpNo(iEmpNo);
				iPfDeparment.setPfDeparmentId(iPfDeparmentId);
				iPfDeparment.setUnitItem(iUnitItem);
				iPfDeparment.setDistItem(iDistItem);
				iPfDeparment.setDeptItem(iDeptItem);
				iPfDeparment.setDirectorCode(iDirectorCode);
				iPfDeparment.setEmpName(iEmpName);
				iPfDeparment.setDepartOfficer(iDepartOfficer);
				iPfDeparment.setGoalCnt(iGoalCnt);
				iPfDeparment.setSumGoalCnt(iSumGoalCnt);
				iPfDeparment.setGoalAmt(iGoalAmt);
				iPfDeparment.setSumGoalAmt(iSumGoalAmt);
				try {
					iPfDeparmentService.insert(iPfDeparment, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 資料新建錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0002", "");
			}
			break;
		case 2:
			if (xPfDeparment == null) {
				throw new LogicException(titaVo, "E0001", "");
			} else {
				try {
					PfDeparment hPfDeparment = iPfDeparmentService.holdById(iPfDeparmentId);
					PfDeparment oldPfDeparment = (PfDeparment) iDataLog.clone(hPfDeparment);
					hPfDeparment.setEmpNo(iEmpNo);
					hPfDeparment.setPfDeparmentId(iPfDeparmentId);
					hPfDeparment.setUnitItem(iUnitItem);
					hPfDeparment.setDistItem(iDistItem);
					hPfDeparment.setDeptItem(iDeptItem);
					hPfDeparment.setDirectorCode(iDirectorCode);
					hPfDeparment.setEmpName(iEmpName);
					hPfDeparment.setDepartOfficer(iDepartOfficer);
					hPfDeparment.setGoalCnt(iGoalCnt);
					hPfDeparment.setSumGoalCnt(iSumGoalCnt);
					hPfDeparment.setGoalAmt(iGoalAmt);
					hPfDeparment.setSumGoalAmt(iSumGoalAmt);
					iPfDeparmentService.update(hPfDeparment, titaVo);
					// 紀錄變更前變更後
					iDataLog.setEnv(titaVo, oldPfDeparment, hPfDeparment);
					iDataLog.exec();
					iBcmCom.checkDiff(oldPfDeparment, hPfDeparment, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 資料更新錯誤
				}
			}
			break;
		case 4:
			if (xPfDeparment == null) {
				throw new LogicException(titaVo, "E0004", "");
			} else {
				try {
					PfDeparment hPfDeparment = iPfDeparmentService.holdById(iPfDeparmentId);
					iPfDeparmentService.delete(hPfDeparment);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 資料刪除錯誤
				}
			}
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}
