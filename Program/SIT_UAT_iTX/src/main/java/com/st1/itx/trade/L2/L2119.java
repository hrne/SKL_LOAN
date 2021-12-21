package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FacShareLimit;
import com.st1.itx.db.service.FacShareLimitService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L2119 合併額度控管登錄
 */
/*
 * Tita
 * CustNo=9,7       -> MRKEY
 * CreditSysNo=9,7
 * CurrencyCode=X,3  
 * LineAmt==9,14.2 -> TXAMT
 * LineAmtCycle==9,14.2 
 * ShareCustNo=9,7
 * ShareFacmNo=9,7
 * iJcicMiainCustFlag=X,1 
 * END=X,1
 */

/**
 * L2119 共用額度登錄
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L2119")
@Scope("prototype")
public class L2119 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacShareLimitService facShareLimitService;

	@Autowired
	Parse parse;
	@Autowired
	SendRsp sendRsp;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	public DataLog datalog;

	// new table 裝tita
	FacShareLimit tFacShareLimit = new FacShareLimit();
	List<FacShareLimit> lFacShareLimit = new ArrayList<FacShareLimit>();
	BigDecimal iLineAmt = BigDecimal.ZERO;
	String iCurrencyCode = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2119 ");
		this.info("   titaVo.getHsupCode() = " + titaVo.getHsupCode());

		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		iCurrencyCode = titaVo.getParam("CurrencyCode");
		iLineAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimLineAmt"));
		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "iFuncCode = " + iFuncCode); // 功能選擇錯誤
		}
		switch (iFuncCode) {
		case 1: // 新增
			// insert "合併額度控管資料檔
			insertFacShareLimit(titaVo);

			this.info("funcd =  1");
			break;
		case 4: // 刪除

			int iApplNo = parse.stringToInteger(titaVo.get("MApplNo1"));

			Slice<FacShareLimit> slFacShareLimit = facShareLimitService.findMainApplNoEq(iApplNo, 0, Integer.MAX_VALUE, titaVo);
			List<FacShareLimit> lFacShareLimit = slFacShareLimit == null ? null : slFacShareLimit.getContent();
			if (lFacShareLimit != null) {
				try {
					facShareLimitService.deleteAll(lFacShareLimit);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", "合併額度控管"); // 刪除資料時，發生錯誤
				}
			}
			break;
		case 5: // inq
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// insert 共用額度副檔-母公司
	private void insertFacShareLimit(TitaVo titaVo) throws LogicException {
		int wkMainApplNo = 0;
		for (int i = 1; i <= 10; i++) {
			int iApplNo = parse.stringToInteger(titaVo.get("MApplNo" + i));
			if (i == 1) {
				wkMainApplNo = iApplNo;
			}
			// 若該筆無資料就離開迴圈
			if (iApplNo == 0) {
				break;
			}
			tFacShareLimit = new FacShareLimit();
			tFacShareLimit.setApplNo(iApplNo);
			tFacShareLimit.setMainApplNo(wkMainApplNo);
			tFacShareLimit.setCustNo(parse.stringToInteger(titaVo.getParam("MCustNo" + i)));
			tFacShareLimit.setFacmNo(parse.stringToInteger(titaVo.getParam("MFacmNo" + i)));
			tFacShareLimit.setKeyinSeq(i);
			tFacShareLimit.setCurrencyCode(iCurrencyCode);
			tFacShareLimit.setLineAmt(iLineAmt);
			try {
				facShareLimitService.insert(tFacShareLimit, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "合併額度控管" + e.getErrorMsg());
			}
		}
	}

}