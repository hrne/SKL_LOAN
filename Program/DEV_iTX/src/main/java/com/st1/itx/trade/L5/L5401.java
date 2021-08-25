package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.PfBsDetailService;
import com.st1.itx.db.service.PfBsOfficerService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.PfBsDetail;
import com.st1.itx.db.domain.PfBsOfficer;
import com.st1.itx.db.domain.PfBsOfficerId;

@Component("L5401")
@Scope("prototype")

/**
 * 房貸專員業績明細資料查詢
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L5401 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public PfBsDetailService iPfBsDetailService;
	@Autowired
	public PfBsOfficerService iPfBsOfficerService;
	@Autowired
	public CustMainService iCustMainService;
	@Autowired
	public ClFacService iClFacService;

	@Autowired
	public DataLog dataLog;

	// 資料來源: 輸入欄位=年月份撈PfBsOfficerService的GoalAmt(目標金額)
	// =工作月撈PfBsDetailService的PerfAmt(業績金額)
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 40;

		this.totaVo.init(titaVo);
		String ifFunctionCode = titaVo.getParam("FunctionCode");
		int iWorkMonth = Integer.valueOf(titaVo.getParam("WorkMonth")) + 191100;
		String iEmpNo = titaVo.getParam("EmpNo");
		String iFullname = titaVo.getParam("EmpNoName");
		String iAreaCode = titaVo.getParam("AreaCode");
		String iAreaItem = titaVo.getParam("AreaItem");
		String iDeptCode = titaVo.getParam("DeptCode");
		String iDeptItem = titaVo.getParam("DepItem");
		String iDistCode = titaVo.getParam("DistCode");
		String iDistItem = titaVo.getParam("DistItem");
		String iStationName = titaVo.getParam("StationName");
		BigDecimal iGoalAmt = new BigDecimal(titaVo.getParam("GoalAmt"));
		BigDecimal iSmryGoalAmt = new BigDecimal(titaVo.getParam("SmryGoalAmt"));
		PfBsOfficerId iPfBsOfficerId = new PfBsOfficerId();
		PfBsOfficer iPfBsOfficer = new PfBsOfficer();
		iPfBsOfficerId.setEmpNo(iEmpNo);
		iPfBsOfficerId.setWorkMonth(iWorkMonth);
		iPfBsOfficer = iPfBsOfficerService.holdById(iPfBsOfficerId);
		switch (ifFunctionCode) {
		case "1": // 新增
			if (iPfBsOfficer == null) {
				PfBsOfficer sPfBsOfficer = new PfBsOfficer();
				sPfBsOfficer.setPfBsOfficerId(iPfBsOfficerId);
				sPfBsOfficer.setFullname(iFullname);
				sPfBsOfficer.setAreaCode(iAreaCode);
				sPfBsOfficer.setAreaItem(iAreaItem);
				sPfBsOfficer.setDeptCode(iDeptCode);
				sPfBsOfficer.setDepItem(iDeptItem);
				sPfBsOfficer.setDistCode(iDistCode);
				sPfBsOfficer.setDistItem(iDistItem);
				sPfBsOfficer.setGoalAmt(iGoalAmt);
				sPfBsOfficer.setSmryGoalAmt(iSmryGoalAmt);
				sPfBsOfficer.setStationName(iStationName);
				try {
					iPfBsOfficerService.insert(sPfBsOfficer, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 資料新建錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0005", "已有相同資料"); // 已有相同資料錯誤
			}
			break;
		case "2": // 修改
			if (iPfBsOfficer == null) {
				throw new LogicException(titaVo, "E2006", ""); // 無資料錯誤
			} else {
				iPfBsOfficer.setPfBsOfficerId(iPfBsOfficerId);
				iPfBsOfficer.setAreaCode(iAreaCode);
				iPfBsOfficer.setAreaItem(iAreaItem);
				iPfBsOfficer.setDeptCode(iDeptCode);
				iPfBsOfficer.setDepItem(iDeptItem);
				iPfBsOfficer.setDistCode(iDistCode);
				iPfBsOfficer.setDistItem(iDistItem);
				iPfBsOfficer.setGoalAmt(iGoalAmt);
				iPfBsOfficer.setSmryGoalAmt(iSmryGoalAmt);
				iPfBsOfficer.setStationName(iStationName);
				try {
					iPfBsOfficer = iPfBsOfficerService.update2(iPfBsOfficer, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 資料新建錯誤
				}
			}
			break;
		case "3": // 複製
			if (iPfBsOfficer == null) {
				PfBsOfficer sPfBsOfficer = new PfBsOfficer();
				sPfBsOfficer.setPfBsOfficerId(iPfBsOfficerId);
				sPfBsOfficer.setFullname(iFullname);
				sPfBsOfficer.setAreaCode(iAreaCode);
				sPfBsOfficer.setAreaItem(iAreaItem);
				sPfBsOfficer.setDeptCode(iDeptCode);
				sPfBsOfficer.setDepItem(iDeptItem);
				sPfBsOfficer.setDistCode(iDistCode);
				sPfBsOfficer.setDistItem(iDistItem);
				sPfBsOfficer.setGoalAmt(iGoalAmt);
				sPfBsOfficer.setSmryGoalAmt(iSmryGoalAmt);
				sPfBsOfficer.setStationName(iStationName);
				try {
					iPfBsOfficerService.insert(sPfBsOfficer, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 資料新建錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0005", "已有相同資料"); // 已有相同資料錯誤
			}
			break;
		case "4": // 刪除
			if (iPfBsOfficer == null) {
				throw new LogicException(titaVo, "E2007", ""); // 無資料錯誤
			} else {
				try {
					iPfBsOfficerService.delete(iPfBsOfficer);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 資料新建錯誤
				}
			}
			break;
		case "5": // 查詢???
			Slice<PfBsDetail> iPfBsDetail = null;
			iPfBsDetail = iPfBsDetailService.findBsOfficerOneMonth(iEmpNo, iWorkMonth, this.index, this.limit, titaVo);

			if (iPfBsDetail != null) {
				for (PfBsDetail xPfBsDetail : iPfBsDetail) {
					OccursList occursList = new OccursList();
					CustMain iCustMain = new CustMain();
					ClFac iClFac = new ClFac();
					int iCustNo = xPfBsDetail.getCustNo();
					int iFacmNo = xPfBsDetail.getFacmNo();
					int iBormNo = xPfBsDetail.getBormNo();
					iCustMain = iCustMainService.custNoFirst(xPfBsDetail.getCustNo(), xPfBsDetail.getCustNo(), titaVo);
					iClFac = iClFacService.mainClNoFirst(iCustNo, iFacmNo, "Y", titaVo);
					occursList.putParam("OOCustName", iCustMain.getCustName());
					occursList.putParam("OOCustNo", iCustNo);
					occursList.putParam("OOFacmNo", iFacmNo);
					occursList.putParam("OOBormNo", iBormNo);
					occursList.putParam("OODrawdownDate", Integer.valueOf(xPfBsDetail.getDrawdownDate()));
					occursList.putParam("OODrawdownAmt", xPfBsDetail.getDrawdownAmt());
					occursList.putParam("OOPerfAmt", xPfBsDetail.getPerfAmt());
					occursList.putParam("OOPerfCnt", xPfBsDetail.getPerfCnt());
					if (iClFac == null) {
						occursList.putParam("OOProdCode", "");
					} else {
						if (iClFac.getClCode1() == 9 && iClFac.getClCode2() == 1) {
							occursList.putParam("OOProdCode", "C");
						} else {
							occursList.putParam("OOProdCode", "H");
						}
					}
					occursList.putParam("OOPieceCode", xPfBsDetail.getPieceCode());

					this.totaVo.addOccursList(occursList);
				}
				if (iPfBsDetail != null && iPfBsDetail.hasNext()) {
					titaVo.setReturnIndex(this.setIndexNext());
					this.totaVo.setMsgEndToEnter();// 手動折返
				}
			} else {
				throw new LogicException(titaVo, "E0001", "查無詳細撥款資料");
			}

			break;
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}
