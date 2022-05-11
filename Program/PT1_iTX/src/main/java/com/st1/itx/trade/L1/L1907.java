package com.st1.itx.trade.L1;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.domain.FinReportDebt;
import com.st1.itx.db.service.FinReportDebtService;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L1907")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L1907 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService custMainService;

	@Autowired
	public FinReportDebtService finReportDebtService;

	@Autowired
	public CdEmpService cdEmpService;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1907 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 75 * 500 = 37500

		// 取tita統編or戶號
		String iCustId = titaVo.getParam("CustId").trim();

		CustMain custMain = custMainService.custIdFirst(iCustId, titaVo);
		if (custMain == null) {
			throw new LogicException("E1003", "客戶主檔 : " + iCustId);
		}

//		String iCustFullname = titaVo.getParam("CustFullname");

		new CustMain();
		Slice<FinReportDebt> slFinReportDebt = finReportDebtService.findCustUKey(custMain.getCustUKey(), this.index, this.limit, titaVo);
		List<FinReportDebt> lFinReportDebt = slFinReportDebt == null ? null : slFinReportDebt.getContent();

		if (lFinReportDebt == null || lFinReportDebt.size() == 0) {
			throw new LogicException("E0001", "財務報表");
		} else {
			for (FinReportDebt tFinReportDebt : lFinReportDebt) {
				OccursList occursList = new OccursList();

				occursList.putParam("OCustUKey", tFinReportDebt.getCustUKey());
				occursList.putParam("OUKey", tFinReportDebt.getUkey());
				occursList.putParam("OCustId", iCustId);
				occursList.putParam("OYear", tFinReportDebt.getStartYY() - 1911);
				occursList.putParam("OMonth", tFinReportDebt.getStartMM() + "~" + tFinReportDebt.getEndMM());
				occursList.putParam("OLastUpdate", parse.timeStampToString(tFinReportDebt.getLastUpdate()));

				CdEmp cdEmp = cdEmpService.findById(tFinReportDebt.getLastUpdateEmpNo(), titaVo);
				if (cdEmp == null) {
					cdEmp = new CdEmp();
					cdEmp.setFullname("");
				}
				occursList.putParam("OLastEmp", tFinReportDebt.getLastUpdateEmpNo() + " " + cdEmp.getFullname());

				this.totaVo.addOccursList(occursList);
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}