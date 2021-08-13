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
import com.st1.itx.db.domain.FacShareAppl;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacShareApplService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L2R49")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R49 extends TradeBuffer {


	/* DB服務注入 */
	@Autowired
	public FacShareApplService facShareApplService;
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public CustMainService custMainService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R49 ");

		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFunCd = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		int iApplNo = this.parse.stringToInteger(titaVo.getParam("RimApplNo"));

		List<FacShareAppl> lFacShareAppl = new ArrayList<FacShareAppl>();
		Slice<FacShareAppl> slFacShareAppl = null;

		slFacShareAppl = facShareApplService.findMainApplNo(iApplNo, 0, Integer.MAX_VALUE, titaVo);
		lFacShareAppl = slFacShareAppl == null ? null : new ArrayList<>(slFacShareAppl.getContent());
		

		if (iFunCd == 1) {
			if (lFacShareAppl != null) {
				throw new LogicException(titaVo, "E2005", "主要核准號碼 = " + iApplNo); // 新增資料已存在

			}
		} else if (iFunCd == 4) {
			if (lFacShareAppl == null) {
				throw new LogicException(titaVo, "E2007", "主要核准號碼 = " + iApplNo); // 刪除資料不存在
			}
		}

		// 資料筆數
		if (lFacShareAppl == null) {
			lFacShareAppl = new ArrayList<FacShareAppl>();
		}
		// 合併額度控管資料檔
		// 檢查資料筆數
		int wkFacShareApplSize = lFacShareAppl.size();
		this.info("L2r49 FacShareAppl size in DB = " + wkFacShareApplSize);

		// 處理10筆
		// 暫時只抓前10筆,把第10筆之後的刪除
		if (wkFacShareApplSize > 10) {
			for (int i = wkFacShareApplSize + 1; i <= wkFacShareApplSize; i++) {
				lFacShareAppl.remove(i);
			}
		} else if (wkFacShareApplSize < 10) {
			// 若不足10筆,補足10筆
			for (int i = wkFacShareApplSize + 1; i <= 10; i++) {
				FacShareAppl tFacShareAppl = new FacShareAppl();
				lFacShareAppl.add(tFacShareAppl);
			}
		}
		if (lFacShareAppl != null) {

			this.totaVo.putParam("L2r49JcicMergeFlag", lFacShareAppl.get(0).getJcicMergeFlag());

			int i = 1;
			for (FacShareAppl t : lFacShareAppl) {
				FacMain tFacMain = facMainService.findById(new FacMainId(t.getCustNo(), t.getFacmNo()), titaVo);
				if (tFacMain == null) {
					tFacMain = new FacMain();
				}
				CustMain tCustMain = custMainService.custNoFirst(t.getCustNo(), t.getCustNo(), titaVo);
				if (tCustMain == null) {
					tCustMain = new CustMain();
				}
				this.info("ApplNo = " + t.getApplNo());
				this.totaVo.putParam("L2r49ApplNo" + i, t.getApplNo());
				this.totaVo.putParam("L2r49CustNo" + i, t.getCustNo());
				this.totaVo.putParam("L2r49FacmNo" + i, t.getFacmNo());
				this.totaVo.putParam("L2r49CustName" + i, tCustMain.getCustName());
				this.totaVo.putParam("L2r49CaseNo" + i, tFacMain.getCreditSysNo());
				if (tFacMain.getRecycleCode().equals("1")) {
					this.totaVo.putParam("L2r49Deadline" + i, tFacMain.getRecycleDeadline());
					this.totaVo.putParam("L2r49RecycleCode" + i, "Y");// 循環動用 1-循環動用
				} else {
					this.totaVo.putParam("L2r49Deadline" + i, tFacMain.getUtilDeadline());
					this.totaVo.putParam("L2r49RecycleCode" + i, "");// 循環動用 0-非循環動用
				}
				this.totaVo.putParam("L2r49CurrencyCode" + i, tFacMain.getCurrencyCode());
				this.totaVo.putParam("L2r49LineAmt" + i, tFacMain.getLineAmt());

				i++;
			}
		}

		
		
		
		
		

		this.addList(this.totaVo);
		return this.sendList();
	}
}