package com.st1.itx.trade.L6;

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
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CdVarValue;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CdVarValueService;
import com.st1.itx.db.service.TxDataLogService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita YearMonth=9,5 END=X,1
 */

@Service("L6052") // 變動數值設定查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6052 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdVarValueService sCdVarValueService;
	@Autowired
	public CdEmpService cdEmpService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6052 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iYearMonth = this.parse.stringToInteger(titaVo.getParam("YearMonth"));
		int iFYearMonth = iYearMonth + 191100;
		this.info("L6052 iFYearMonth : " + iFYearMonth);
		int wkFYearMonth = 0;

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 69 * 200 = 13,800

		// 查詢變動數值設定檔
		Slice<CdVarValue> slCdVarValue;
		if (iYearMonth == 0) {
			slCdVarValue = sCdVarValueService.findAll(this.index, this.limit, titaVo);
		} else {
			slCdVarValue = sCdVarValueService.findYearMonth(iFYearMonth, iFYearMonth, this.index, this.limit, titaVo);
		}
		List<CdVarValue> lCdVarValue = slCdVarValue == null ? null : slCdVarValue.getContent();

		if (lCdVarValue == null || lCdVarValue.size() == 0) {
			throw new LogicException(titaVo, "E0001", "變動數值設定檔"); // 查無資料
		}
		// 如有找到資料
		for (CdVarValue tCdVarValue : lCdVarValue) {
			OccursList occursList = new OccursList();

			wkFYearMonth = tCdVarValue.getYearMonth();
			wkFYearMonth = wkFYearMonth - 191100;
			occursList.putParam("OOYearMonth", wkFYearMonth);

			occursList.putParam("OOAvailableFunds", tCdVarValue.getAvailableFunds());
			occursList.putParam("OOLoanTotalLmt", tCdVarValue.getLoanTotalLmt());
			occursList.putParam("OONoGurTotalLmt", tCdVarValue.getNoGurTotalLmt());
			occursList.putParam("OOTotalequity", tCdVarValue.getTotalequity());
			occursList.putParam("OOLastUpdate", parse.timeStampToStringDate(tCdVarValue.getLastUpdate())+ " " +parse.timeStampToStringTime(tCdVarValue.getLastUpdate()));
			occursList.putParam("OOLastEmp", tCdVarValue.getLastUpdateEmpNo() + " " + empName(titaVo, tCdVarValue.getLastUpdateEmpNo()));
			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdVarValue != null && slCdVarValue.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}
		

		this.addList(this.totaVo);
		return this.sendList();
	}
	private String empName(TitaVo titaVo, String empNo) throws LogicException {
		String rs = empNo;

		CdEmp cdEmp = cdEmpService.findById(empNo, titaVo);
		if (cdEmp != null) {
			rs = cdEmp.getFullname();
		}
		return rs;
	}
}