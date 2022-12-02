package com.st1.itx.trade.L4;

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
import com.st1.itx.db.domain.RepayActChangeLog;
import com.st1.itx.db.service.BankAuthActService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.RepayActChangeLogService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L4923")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L4923 extends TradeBuffer {

	@Autowired
	Parse parse;
	@Autowired
	BankAuthActService bankAuthActService;
	@Autowired
	CustMainService custMainService;
	@Autowired
	FacMainService facMainService;
	@Autowired
	CdEmpService cdEmpService;
	@Autowired
	RepayActChangeLogService repayActChangeLogService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4923 ");
		this.totaVo.init(titaVo);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 3 + 75 * 600 = 45003

		// 取得輸入資料
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));

		// wk
		String wkUser = "";
		Slice<RepayActChangeLog> slRepayActChangeLog;
		List<RepayActChangeLog> lRepayActChangeLog;

		// 戶號額度查還款帳號變更紀錄檔
		slRepayActChangeLog = repayActChangeLogService.findFacmNoEq(iCustNo, iFacmNo, this.index, this.limit, titaVo);
		lRepayActChangeLog = slRepayActChangeLog == null ? null : slRepayActChangeLog.getContent();
		// 如沒有找到資料
		if (lRepayActChangeLog == null || lRepayActChangeLog.size() == 0) {
			throw new LogicException(titaVo, "E0001", "還款帳號變更紀錄檔"); // 查詢資料不存在
		}
		// 如有有找到資料
		for (RepayActChangeLog t : lRepayActChangeLog) {

			String TlrNo = "";
			if (t.getCreateEmpNo() != null) {
				TlrNo = t.getCreateEmpNo();
			}

			wkUser = TlrNo;
			if ("999999".equals(TlrNo)) {
				wkUser = "系統轉換";
			} else {
				// 組經辦人員姓名
				CdEmp tCdEmp = cdEmpService.findById(TlrNo, titaVo);
				if (tCdEmp != null) {
					wkUser = wkUser + " " + tCdEmp.getFullname();
				}
			}
			OccursList occursList = new OccursList();

			occursList.putParam("OORepayCode", t.getRepayCode());
			occursList.putParam("OORepayBank", "");
			occursList.putParam("OORepayAcct", "");
			if (t.getRepayCode() == 2) {
				occursList.putParam("OORepayBank", t.getRepayBank());
				// 存款別處理
				if ("".equals(t.getPostDepCode())) {
					occursList.putParam("OORepayAcct", t.getRepayAcct());
				} else {
					occursList.putParam("OORepayAcct", t.getPostDepCode() + t.getRepayAcct());
				}
			}
			occursList.putParam("OOTlrNo", wkUser);
			String sDate = "";
			int iDate = 0;
			if (t.getLastUpdate() != null) {
				sDate = t.getLastUpdate().toString().substring(0, 10).trim();
				sDate = sDate.replace("-", "");
				iDate = parse.stringToInteger(sDate);
				if (iDate > 19110000)
					iDate -= 19110000;
			}

			occursList.putParam("OOupdatadate", iDate);

			this.totaVo.addOccursList(occursList);
		}
		// 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可
		if (slRepayActChangeLog != null && slRepayActChangeLog.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}