package com.st1.itx.trade.L2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacShareLimit;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacShareLimitService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L2019 合併額度控管資料查詢
 */
/**
 * Tita<br>
 * CreditSysNo=9,7<br>
 * CustNo=9,7<br>
 * END=X,1<br>
 */

@Service("L2019")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2019 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacShareLimitService sFacShareLimitService;

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	@Autowired
	public CdEmpService sCdEmpService;
	
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2019 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設100筆 總長不可超過六萬 */
		this.limit = 100; //

		// tita
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		// 核准號碼
		int iApplNo = parse.stringToInteger(titaVo.getParam("ApplNo"));

		FacShareLimit tFacShareLimit = new FacShareLimit();
		List<FacShareLimit> lFacShareLimit = new ArrayList<FacShareLimit>();
		Slice<FacShareLimit> slFacShareLimit = null;
		// 處理邏輯
		if (iApplNo > 0) {
			tFacShareLimit = sFacShareLimitService.findById(iApplNo, titaVo);
			if (tFacShareLimit == null) {
				throw new LogicException("E0001", "合併額度");// E0001 查無資料
			}
			lFacShareLimit.add(tFacShareLimit);
		} else if (iCustNo > 0) {
			slFacShareLimit = sFacShareLimitService.findCustNoEq(iCustNo, this.index, this.limit, titaVo);
			if (slFacShareLimit == null) {
				throw new LogicException("E0001", "合併額度");// E0001 查無資料
			}
			lFacShareLimit = slFacShareLimit == null ? null : slFacShareLimit.getContent();
		} else {
			slFacShareLimit = sFacShareLimitService.findAll(this.index, this.limit, titaVo);
			List<FacShareLimit> l = slFacShareLimit == null ? null : new ArrayList<>(slFacShareLimit.getContent());
			if (l == null) {
				throw new LogicException("E0001", "合併額度");// E0001 查無資料
			}
			for (FacShareLimit t : l) {
				if (t.getKeyinSeq() == 1) {
					lFacShareLimit.add(t);
				}
			}
		}

		if (lFacShareLimit == null || lFacShareLimit.size() == 0) {
			throw new LogicException("E0001", "合併額度");// E0001 查無資料
		}

		int createDate = 0;
		int lastUpdate = 0;
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		for (FacShareLimit t : lFacShareLimit) {

			/* 將每筆資料放入Tota的OcList */

			OccursList occurslist = new OccursList();
			occurslist.putParam("OOApplNo", t.getApplNo());
			occurslist.putParam("OOCustNo", t.getCustNo());
			CustMain tCustMain = sCustMainService.custNoFirst(t.getCustNo(), t.getCustNo(), titaVo);
			if (tCustMain != null) {
				occurslist.putParam("OOCustName", tCustMain.getCustName());
			} else {
				occurslist.putParam("OOCustName", "");
			}
			occurslist.putParam("OOFacmNo", t.getFacmNo());
			occurslist.putParam("OOCurrencyCode", t.getCurrencyCode());
			occurslist.putParam("OOLineAmt", t.getLineAmt());
			if (t.getCreateDate() != null) {
				createDate = parse.stringToInteger(df.format(t.getCreateDate())) - 19110000;
				occurslist.putParam("OOCreateDate", createDate);
			} else {
				occurslist.putParam("OOCreateDate", "");
			}
			occurslist.putParam("OOCreateEmpNo", t.getCreateEmpNo());
			
			String TlrNo = "";
			String EmpName = "";
			CdEmp tCdEmp = new CdEmp();

			if (t.getCreateEmpNo() != null) {
				TlrNo = t.getCreateEmpNo() ;
				tCdEmp = sCdEmpService.findById(TlrNo, titaVo);
				if (tCdEmp != null) {
					EmpName = tCdEmp.getFullname();
				}
			}
			occurslist.putParam("OOCreateEmpName", EmpName);
			
			
			if (t.getLastUpdate() != null) {
				lastUpdate = parse.stringToInteger(df.format(t.getLastUpdate())) - 19110000;
				occurslist.putParam("OOLastUpdate", lastUpdate);
			} else {
				occurslist.putParam("OOLastUpdate", "");
			}
			occurslist.putParam("OOLastUpdateEmpNo", t.getLastUpdateEmpNo());
			
			if (t.getLastUpdateEmpNo() != null) {
				TlrNo = t.getLastUpdateEmpNo() ;
				tCdEmp = sCdEmpService.findById(TlrNo, titaVo);
				if (tCdEmp != null) {
					EmpName = tCdEmp.getFullname();
				}
			}
			occurslist.putParam("OOLastUpdateEmpName", EmpName);
			
			occurslist.putParam("OOMainApplNo", t.getMainApplNo());

			this.totaVo.addOccursList(occurslist);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}