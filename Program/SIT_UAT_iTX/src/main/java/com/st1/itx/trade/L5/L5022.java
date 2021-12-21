package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.PfCoOfficer;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.PfCoOfficerService;
import com.st1.itx.db.service.springjpa.cm.L5022ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;

@Component("L5022")
@Scope("prototype")

/**
 * 放款專員業績統計作業－協辦人員等級明細資料查詢
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L5022 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public L5022ServiceImpl iL5022ServiceImpl;
	@Autowired
	public PfCoOfficerService iPfCoOfficerService;
	@Autowired
	public CdEmpService iCdEmpService;

	// 輸入的員工編號必須存在於員工檔

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		String iEmpNo = titaVo.getParam("EmpNo");
		int cDate = Integer.valueOf(titaVo.getEntDy()) + 19110000;
		int iEffectiveDateS = Integer.valueOf(titaVo.getParam("EffectiveDateS")) + 19110000;
		int iEffectiveDateE = Integer.valueOf(titaVo.getParam("EffectiveDateE")) + 19110000;
		String iStatusFg = titaVo.getParam("StatusFlag");

		this.info("cDate=" + cDate);
		List<Map<String, String>> iL5022SqlReturn = new ArrayList<Map<String, String>>();
		Slice<PfCoOfficer> sPfCoOfficer = null;
		CdEmp iCdEmp = new CdEmp();
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 40;

		try {
			iL5022SqlReturn = iL5022ServiceImpl.findByStatus(cDate, iEmpNo, this.index, this.limit, titaVo);
		} catch (Exception e) {
			throw new LogicException(titaVo, "E5004", "");
		}

		if (iL5022SqlReturn.size() == 0) {
			throw new LogicException(titaVo, "E0001", "協辦人員等級檔查無 " + iEmpNo + " 資料");
		}

		if (iL5022SqlReturn != null && iL5022SqlReturn.size() >= this.limit) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}
		String rEmpNo = "";
		String rStatusFg = "";
		int reCount = 0;
		for (Map<String, String> r5022SqlReturn : iL5022SqlReturn) {
			OccursList occursList = new OccursList();

			if (!iStatusFg.trim().isEmpty()) { // 有輸入狀態
				occursList.putParam("OOEmpNo", r5022SqlReturn.get("EmpNo"));
				if (r5022SqlReturn.get("EffectiveDate").equals("") || r5022SqlReturn.get("EffectiveDate").equals("0")) {
					occursList.putParam("OOEffectiveDate", "");
				} else {
					occursList.putParam("OOEffectiveDate", Integer.valueOf(r5022SqlReturn.get("EffectiveDate")) - 19110000);
				}
				occursList.putParam("OOEmpClass", r5022SqlReturn.get("EmpClass"));
				occursList.putParam("OOClassPass", r5022SqlReturn.get("ClassPass"));
				occursList.putParam("OOFullname", r5022SqlReturn.get("Fullname"));
				occursList.putParam("OOUnitCode", r5022SqlReturn.get("AreaCode"));
				occursList.putParam("OODistCode", r5022SqlReturn.get("DistCode"));
				occursList.putParam("OODeptCode", r5022SqlReturn.get("DeptCode"));
				occursList.putParam("OOUnitCodeX", r5022SqlReturn.get("AreaItem"));
				occursList.putParam("OODistCodeX", r5022SqlReturn.get("DistItem"));
				occursList.putParam("OODeptCodeX", r5022SqlReturn.get("DeptItem"));
				if (r5022SqlReturn.get("IneffectiveDate").equals("") || r5022SqlReturn.get("IneffectiveDate").equals("0")) {
					occursList.putParam("OOIneffectiveDate", "");
				} else {
					occursList.putParam("OOIneffectiveDate", Integer.valueOf(r5022SqlReturn.get("IneffectiveDate")) - 19110000);
				}
				rStatusFg = r5022SqlReturn.get("StatusFg");
				if (rStatusFg.equals("1")) { // 若狀態為1:已生效，第一筆之後的狀態須改為已停用
					if (rEmpNo.compareTo(r5022SqlReturn.get("EmpNo")) == 0) {
						rStatusFg = "2";
					} else {
						rStatusFg = "1";
						rEmpNo = r5022SqlReturn.get("EmpNo");
					}
				}

				if (!iStatusFg.equals("9")) {
					if (rStatusFg.compareTo(iStatusFg) == 0) {
						occursList.putParam("OOStatusFg", rStatusFg);
					} else {
						continue;
					}
				} else {
					occursList.putParam("OOStatusFg", rStatusFg);
				}
			} else { // 有輸入生效日期
				// A.停效日等於0(1.生效日不大於迄日)
				// B.停效日不等於0(1.生效日不大於迄日2.停效日大於起日)
				if (Integer.valueOf(r5022SqlReturn.get("EffectiveDate")) > iEffectiveDateE) {
					continue;
				}
				if (Integer.valueOf(r5022SqlReturn.get("IneffectiveDate")) != 19110000) {
					if (Integer.valueOf(r5022SqlReturn.get("IneffectiveDate")) < iEffectiveDateE) {
						continue;
					}
				}
				occursList.putParam("OOEmpNo", r5022SqlReturn.get("EmpNo"));
				if (r5022SqlReturn.get("EffectiveDate").equals("") || r5022SqlReturn.get("EffectiveDate").equals("0")) {
					occursList.putParam("OOEffectiveDate", "");
				} else {
					occursList.putParam("OOEffectiveDate", Integer.valueOf(r5022SqlReturn.get("EffectiveDate")) - 19110000);
				}
				occursList.putParam("OOEmpClass", r5022SqlReturn.get("EmpClass"));
				occursList.putParam("OOClassPass", r5022SqlReturn.get("ClassPass"));
				occursList.putParam("OOFullname", r5022SqlReturn.get("Fullname"));
				occursList.putParam("OOUnitCode", r5022SqlReturn.get("AreaCode"));
				occursList.putParam("OODistCode", r5022SqlReturn.get("DistCode"));
				occursList.putParam("OODeptCode", r5022SqlReturn.get("DeptCode"));
				occursList.putParam("OOUnitCodeX", r5022SqlReturn.get("AreaItem"));
				occursList.putParam("OODistCodeX", r5022SqlReturn.get("DistItem"));
				occursList.putParam("OODeptCodeX", r5022SqlReturn.get("DeptItem"));
				if (r5022SqlReturn.get("IneffectiveDate").equals("") || r5022SqlReturn.get("IneffectiveDate").equals("0")) {
					occursList.putParam("OOIneffectiveDate", "");
				} else {
					occursList.putParam("OOIneffectiveDate", Integer.valueOf(r5022SqlReturn.get("IneffectiveDate")) - 19110000);
				}
				rStatusFg = r5022SqlReturn.get("StatusFg");
				if (rStatusFg.equals("1")) { // 若狀態為1:已生效，第一筆之後的狀態須改為已停用
					if (rEmpNo.compareTo(r5022SqlReturn.get("EmpNo")) == 0) {
						rStatusFg = "2";
					} else {
						rStatusFg = "1";
						rEmpNo = r5022SqlReturn.get("EmpNo");
					}
				}
				occursList.putParam("OOStatusFg", rStatusFg);
			}

			reCount++;
			this.totaVo.addOccursList(occursList);
		}
		if (reCount == 0) {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}
