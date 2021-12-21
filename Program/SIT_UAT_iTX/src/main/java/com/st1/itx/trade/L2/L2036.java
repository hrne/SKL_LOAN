package com.st1.itx.trade.L2;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.ReltMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.ReltMainService;
import com.st1.itx.tradeService.TradeBuffer;

/**
 * Tita<br>
 * CustId=X,10<br>
 */

@Service("L2036")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L2036 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public ReltMainService iReltMainService;

	@Autowired
	public CustMainService sCustMainService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2036 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 38 * 500 = 19000

		// 取tita戶號
		int iCustNo = Integer.valueOf(titaVo.getParam("CustNo"));
		// 取tita案件編號
		int iCaseNo = Integer.valueOf(titaVo.getParam("CaseNo"));

		CustMain lCustMain = new CustMain();

		String Ukey = "";

		Slice<ReltMain> iReltMain = null;
		if (iCustNo != 0) {
			iReltMain = iReltMainService.findByBoth(iCaseNo, iCustNo, this.index, this.limit, titaVo);
		} else {
			iReltMain = iReltMainService.caseNoEq(iCaseNo, this.index, this.limit, titaVo);
		}

		if (iReltMain == null) {
			throw new LogicException(titaVo, "E2003", "無關係人檔資料"); // 查無資料
		}

		for (ReltMain rReltMain : iReltMain) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOCaseNo", rReltMain.getCaseNo());
			occursList.putParam("OOCustNo", rReltMain.getCustNo());

			Ukey = rReltMain.getReltUKey();

			lCustMain = sCustMainService.findById(Ukey, titaVo);

			if (lCustMain == null) {
				throw new LogicException("E0001", "客戶資料主檔");
			}

			occursList.putParam("OOReltId", lCustMain.getCustId());
			occursList.putParam("OORelName", lCustMain.getCustName());

			occursList.putParam("OOPosInd", rReltMain.getReltCode());
			occursList.putParam("OORemarkType", rReltMain.getRemarkType());
			occursList.putParam("OORemark", rReltMain.getReltmark());
			occursList.putParam("OOApplDate", rReltMain.getApplDate());
			this.totaVo.addOccursList(occursList);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}