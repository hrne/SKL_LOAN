package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
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
	public L5022ServiceImpl l5022ServiceImpl;
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
		int iEffectiveDateS = Integer.valueOf(titaVo.getParam("EffectiveDateS"));
		int iEffectiveDateE = Integer.valueOf(titaVo.getParam("EffectiveDateE")) + 19110000;
		String iStatusFg = titaVo.getParam("StatusFlag");

		List<Map<String, String>> l5022SqlReturn = new ArrayList<Map<String, String>>();
		new CdEmp();
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 40;

		try {
			l5022SqlReturn = l5022ServiceImpl.findByStatus(cDate, iEffectiveDateS, iEffectiveDateE, iEmpNo, iStatusFg,
					this.index, this.limit, titaVo);
		} catch (Exception e) {
			throw new LogicException(titaVo, "E5004", "");
		}

		if (this.index == 0 && (l5022SqlReturn == null || l5022SqlReturn.size() == 0)) {
			throw new LogicException(titaVo, "E0001", "會計帳務明細檔");
		}

		for (Map<String, String> result : l5022SqlReturn) {
			OccursList occursList = new OccursList();
			int ineffectiveDate = Integer.valueOf(result.get("IneffectiveDate"));
			int effectiveDate = Integer.valueOf(result.get("EffectiveDate"));

			occursList.putParam("OOEmpNo", result.get("EmpNo"));
			occursList.putParam("OOFullname", result.get("Fullname"));
			occursList.putParam("OOEffectiveDate", effectiveDate - 19110000);
			occursList.putParam("OOIneffectiveDate", ineffectiveDate == 0 ? 0 : ineffectiveDate - 19110000);
			occursList.putParam("OOEmpClass", result.get("EmpClass"));
			occursList.putParam("OOClassPass", result.get("ClassPass"));
			occursList.putParam("OOUnitCode", result.get("AreaCode"));
			occursList.putParam("OODistCode", result.get("DistCode"));
			occursList.putParam("OODeptCode", result.get("DeptCode"));
			occursList.putParam("OOUnitCodeX", result.get("AreaItem"));
			occursList.putParam("OODistCodeX", result.get("DistItem"));
			occursList.putParam("OODeptCodeX", result.get("DeptItem"));
			occursList.putParam("OOStatusFg", result.get("StatusFg"));
			// <歷程>按鈕 => LogCount > 0 則顯示
			occursList.putParam("OOLogCount", result.get("LogCount"));

			
			// <離調職異動>按鈕 ==> 離調職異動日 > 0 則顯示
			// 按鈕連結<L5407>FunctionCd=6-離調職異動，將[離調職異動日[帶入[停效日期]欄，其他欄不可改
			int quitDate = Integer.valueOf(result.get("QuitDate"));// 離職/停約日
			int agPostChgDate = Integer.valueOf(result.get("AgPostChgDate")); // 職務異動日
			int quitChgDateDate = 0; // 離調職異動日
			// 離職/停約日在有在有效期間、 單位不同且職務異動日在有效期間
			if (ineffectiveDate > quitDate) {
				quitChgDateDate = quitDate;
			} else {
				if (result.get("CenterCode").equals(result.get("AreaCode"))) {
					if (ineffectiveDate > agPostChgDate) {
						quitChgDateDate = agPostChgDate;
					}
				}
			}
			occursList.putParam("OOquitChgDateDate", quitChgDateDate == 0 ? 0 : quitChgDateDate - 19110000); // 離調職異動日

			int evalueChgDate = Integer.valueOf(result.get("EvalueChgDate")); // 考核職級異動日
			// 考核職級異動日 > 0 則顯示<考核職級異動>按鈕，連結<L5407>FunctionCd=8-考核職級異動，
			// 將[考核職級異動日]帶入[生效日期]欄、[考核職級]帶入[協辦等級]欄，其他欄不可改
			occursList.putParam("OOEvalueChgDate", evalueChgDate == 0 ? 0 : evalueChgDate - 19110000);  // 考核職級異動日 
			occursList.putParam("OOEvalueChgClass", result.get("EvalueChgClass")); // 考核職級 
			this.totaVo.addOccursList(occursList);
		}
		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (l5022SqlReturn != null && l5022SqlReturn.size() >= this.limit) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}
