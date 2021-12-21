package com.st1.itx.trade.L2;

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
import com.st1.itx.db.domain.AcLoanRenew;
import com.st1.itx.db.service.AcLoanRenewService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L2079")
@Scope("prototype")
/**
 * 
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class L2079 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2079.class);

	/* DB服務注入 */
	@Autowired
	public AcLoanRenewService sAcLoanRenewService;
	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2079 ");
		this.totaVo.init(titaVo);

		// 取tita傳入參數
		String custNo = titaVo.getParam("CustNo").trim();
		String oldFacmNo = titaVo.getParam("OldFacmNo").trim();
		String newFacmNo = titaVo.getParam("NewFacmNo").trim();
		int yearMonthStart = parse.stringToInteger(titaVo.getParam("YearMonthStart").trim() + "00");
		int yearMonthEnd = parse.stringToInteger(titaVo.getParam("YearMonthEnd").trim() + "00");

		int iCustNo = 0;
		int iOldFacmNo = 0;
		int iNewFacmNo = 0;

		if (!custNo.isEmpty()) {
			iCustNo = Integer.valueOf(custNo);
		}
		if (!oldFacmNo.isEmpty()) {
			iOldFacmNo = Integer.valueOf(oldFacmNo);
		}
		if (!newFacmNo.isEmpty()) {
			iNewFacmNo = Integer.valueOf(newFacmNo);
		}

		// 宣告資料容器
		List<AcLoanRenew> lAcLoanRenew = new ArrayList<AcLoanRenew>();

		// 根據傳入參數修改資料篩選範圍
		int custNoEnd = 9999999;

		if (iCustNo > 0) {
			custNoEnd = iCustNo;
		}

		int oldFacmNoEnd = 999;

		if (iOldFacmNo > 0) {
			oldFacmNoEnd = iOldFacmNo;
		}

		int newFacmNoEnd = 999;

		if (iNewFacmNo > 0) {
			newFacmNoEnd = iNewFacmNo;
		}

		if (yearMonthStart == 0) {
			yearMonthStart = 0;
			yearMonthEnd = 99999999;
		} else {
			yearMonthStart = yearMonthStart + 19110001;
			yearMonthEnd = yearMonthEnd + 19110031;
		}

		this.info("yearMonthStart = " + yearMonthStart + "yearMonthEnd = " + yearMonthEnd);
		// 查會計借新還舊檔
		Slice<AcLoanRenew> slAcLoanRenew = sAcLoanRenewService.findL2079(iCustNo, custNoEnd, iOldFacmNo, oldFacmNoEnd, iOldFacmNo, newFacmNoEnd, yearMonthStart, yearMonthEnd, 0, Integer.MAX_VALUE);
		lAcLoanRenew = slAcLoanRenew == null ? null : slAcLoanRenew.getContent();

		// 查無資料處理
		if (lAcLoanRenew == null || lAcLoanRenew.size() == 0) {
			throw new LogicException("E0001", "會計借新還舊檔");
		}

		// 裝入Tota OccursList
		for (AcLoanRenew tAcLoanRenew : lAcLoanRenew) {

			OccursList occursList = new OccursList();
			occursList.putParam("OOCustNo", tAcLoanRenew.getCustNo());
			occursList.putParam("OOOldFacmNo", tAcLoanRenew.getOldFacmNo());
			occursList.putParam("OOOldBormNo", tAcLoanRenew.getOldBormNo());
			occursList.putParam("OONewFacmNo", tAcLoanRenew.getNewFacmNo());
			occursList.putParam("OONewBormNo", tAcLoanRenew.getNewBormNo());
			occursList.putParam("OORenewCode", tAcLoanRenew.getRenewCode());
			occursList.putParam("OOMainFlag", tAcLoanRenew.getMainFlag());
			occursList.putParam("OOAcDate", tAcLoanRenew.getAcDate());
			this.totaVo.addOccursList(occursList);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}