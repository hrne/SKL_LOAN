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
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.EmpDeductDtl;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.EmpDeductDtlService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * MediaDate=9,7<br>
 */

@Service("L4951")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4951 extends TradeBuffer {
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public EmpDeductDtlService empDeductDtlService;

	@Autowired
	public CustMainService custMainService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4951 ");
		this.totaVo.init(titaVo);

//		1.查詢 入帳日期

		int entryDate = parse.stringToInteger(titaVo.getParam("EntryDate")) + 19110000;
		int mediaType = parse.stringToInteger(titaVo.getParam("MediaType"));

		List<EmpDeductDtl> lEmpDeductDtl = new ArrayList<EmpDeductDtl>();

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		Slice<EmpDeductDtl> sEmpDeductDtl = null;

		List<String> type = new ArrayList<String>();

		if (mediaType == 1) {
			type.add("4");
			type.add("5");
			sEmpDeductDtl = empDeductDtlService.entryDateRng(entryDate, entryDate, type, this.index, this.limit);
		} else if (mediaType == 2) {
			type.add("1");
			type.add("2");
			type.add("3");
			type.add("6");
			type.add("7");
			type.add("8");
			type.add("9");
			sEmpDeductDtl = empDeductDtlService.entryDateRng(entryDate, entryDate, type, this.index, this.limit);
		}

		lEmpDeductDtl = sEmpDeductDtl == null ? null : sEmpDeductDtl.getContent();

		if (lEmpDeductDtl != null && lEmpDeductDtl.size() != 0) {
			for (EmpDeductDtl tEmpDeductDtl : lEmpDeductDtl) {
				OccursList occursList = new OccursList();

				CustMain tCustMain = new CustMain();

				tCustMain = custMainService.custNoFirst(tEmpDeductDtl.getCustNo(), tEmpDeductDtl.getCustNo());

				occursList.putParam("OOEntryDate", tEmpDeductDtl.getEntryDate());
				occursList.putParam("OOPerfMonth", tEmpDeductDtl.getPerfMonth() - 191100);
				occursList.putParam("OOProcCode", tEmpDeductDtl.getProcCode());
				occursList.putParam("OOAcctCode", tEmpDeductDtl.getAcctCode());
				occursList.putParam("OORepayCode", tEmpDeductDtl.getRepayCode());
				occursList.putParam("OOAchRepayCode", tEmpDeductDtl.getAchRepayCode());
				occursList.putParam("OOCustNo", tEmpDeductDtl.getCustNo());
				occursList.putParam("OOFacmNo", tEmpDeductDtl.getFacmNo());
				occursList.putParam("OOBormNo", tEmpDeductDtl.getBormNo());
				occursList.putParam("OOTellerNo", tCustMain.getEmpNo());
				occursList.putParam("OOTellerId", tEmpDeductDtl.getCustId());
				occursList.putParam("OOTellerName", tCustMain.getCustName());
				occursList.putParam("OORepayAmt", tEmpDeductDtl.getRepayAmt());
				this.totaVo.addOccursList(occursList);
			}
		} else {
			throw new LogicException("E0001", "L4951 查無資料");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}