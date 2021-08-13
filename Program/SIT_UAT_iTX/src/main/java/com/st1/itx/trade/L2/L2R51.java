package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.FacShareLimit;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacShareLimitService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L2R51")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R51 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacShareLimitService facShareLimitService;
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public CustMainService custMainService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R51 ");

		this.totaVo.init(titaVo);

		// 取得輸入資料

		int iFunCd = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		int iApplNo = this.parse.stringToInteger(titaVo.getParam("RimApplNo"));

		List<FacShareLimit> lFacShareLimit = new ArrayList<FacShareLimit>();
		Slice<FacShareLimit> slFacShareLimit = null;

		slFacShareLimit = facShareLimitService.findMainApplNoEq(iApplNo, 0, Integer.MAX_VALUE, titaVo);
		lFacShareLimit = slFacShareLimit == null ? null : new ArrayList<>(slFacShareLimit.getContent());

		if (iFunCd == 1) {
			if (lFacShareLimit != null) {
				throw new LogicException(titaVo, "E2005", "主要核准號碼 = " + iApplNo); // 新增資料已存在

			}
		} else if (iFunCd == 4) {
			if (lFacShareLimit == null) {
				throw new LogicException(titaVo, "E2007", "主要核准號碼 = " + iApplNo); // 刪除資料不存在
			}
		}

		// 資料筆數
		if (lFacShareLimit == null) {
			lFacShareLimit = new ArrayList<FacShareLimit>();
		}
		// 合併額度控管資料檔
		// 檢查資料筆數
		int wkFacShareLimitSize = lFacShareLimit.size();
		this.info("L2r51 FacShareLimit size in DB = " + wkFacShareLimitSize);

		// 處理10筆
		// 暫時只抓前10筆,把第10筆之後的刪除
		if (wkFacShareLimitSize > 10) {
			for (int i = wkFacShareLimitSize + 1; i <= wkFacShareLimitSize; i++) {
				lFacShareLimit.remove(i);
			}
		} else if (wkFacShareLimitSize < 10) {
			// 若不足10筆,補足10筆
			for (int i = wkFacShareLimitSize + 1; i <= 10; i++) {
				FacShareLimit tFacShareLimit = new FacShareLimit();
				lFacShareLimit.add(tFacShareLimit);
			}
		}
		//
		if (lFacShareLimit != null) {

			this.totaVo.putParam("L2r51CurrencyCode", lFacShareLimit.get(0).getCurrencyCode());
			this.totaVo.putParam("L2r51LineAmt", lFacShareLimit.get(0).getLineAmt());

			int i = 1;
			for (FacShareLimit t : lFacShareLimit) {
				FacMain tFacMain = facMainService.findById(new FacMainId(t.getCustNo(), t.getFacmNo()), titaVo);
				if (tFacMain == null) {
					tFacMain = new FacMain();
				}
				CustMain tCustMain = custMainService.custNoFirst(t.getCustNo(), t.getCustNo(), titaVo);
				if (tCustMain == null) {
					tCustMain = new CustMain();
				}
				this.info("ApplNo = " + t.getApplNo());
				this.totaVo.putParam("L2r51MApplNo" + i, t.getApplNo());
				this.totaVo.putParam("L2r51MCustNo" + i, t.getCustNo());
				this.totaVo.putParam("L2r51MFacmNo" + i, t.getFacmNo());
				this.totaVo.putParam("L2r51MCustName" + i, tCustMain.getCustName());
				this.totaVo.putParam("L2r51MCaseNo" + i, tFacMain.getCreditSysNo());
				if (tFacMain.getRecycleCode().equals("1")) {
					this.totaVo.putParam("L2r51MDeadline" + i, tFacMain.getRecycleDeadline());
					this.totaVo.putParam("L2r51MRecycleCode" + i, "Y");// 循環動用 1-循環動用
				} else {
					this.totaVo.putParam("L2r51MDeadline" + i, tFacMain.getUtilDeadline());
					this.totaVo.putParam("L2r51MRecycleCode" + i, "");// 循環動用 0-非循環動用
				}
				this.totaVo.putParam("L2r51MCurrencyCode" + i, tFacMain.getCurrencyCode());
				this.totaVo.putParam("L2r51MLineAmt" + i, tFacMain.getLineAmt());

				i++;
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}