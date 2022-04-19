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
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacShareAppl;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacShareApplService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L2018 共同借款人資料查詢
 */


@Service("L2018")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2018 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacShareApplService facShareApplService;
	@Autowired
	public FacMainService facMainService;
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
		this.info("active L2018 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500; //

		// tita
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		// 核准號碼
		int iApplNo = parse.stringToInteger(titaVo.getParam("ApplNo"));

		List<FacShareAppl> lFacShareAppl = new ArrayList<FacShareAppl>();
		FacShareAppl tFacShareAppl = new FacShareAppl();
		Slice<FacShareAppl> slFacShareAppl = null;
		// 處理邏輯
		if (iApplNo > 0) {
			tFacShareAppl = facShareApplService.findById(iApplNo, titaVo);
			if (tFacShareAppl == null) {
				throw new LogicException("E0001", "");// E0001 查無資料
			}
			lFacShareAppl.add(tFacShareAppl);
		} else if (iCustNo > 0) {
			slFacShareAppl = facShareApplService.findCustNoEq(iCustNo, this.index, this.limit, titaVo);
			if (slFacShareAppl == null) {
				throw new LogicException("E0001", "");// E0001 查無資料
			}
			lFacShareAppl = slFacShareAppl == null ? null : slFacShareAppl.getContent();
		} else {
			slFacShareAppl = facShareApplService.findAll(this.index, this.limit, titaVo);
			List<FacShareAppl> l = slFacShareAppl == null ? null : new ArrayList<>(slFacShareAppl.getContent());
			if (l == null) {
				throw new LogicException("E0001", "合併額度");// E0001 查無資料
			}
			for (FacShareAppl t : l) {
				if (t.getKeyinSeq() == 1) {
					lFacShareAppl.add(t);
				}
			}
		}

		if (lFacShareAppl.size() == 0) {
			throw new LogicException("E0001", "共用額度");// E0001 查無資料
		}

		int createDate = 0;
		int lastUpdate = 0;
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		for (FacShareAppl t : lFacShareAppl) {

			FacMain tFacMain = facMainService.facmApplNoFirst(t.getApplNo(), titaVo);
			if (tFacMain == null) {
				tFacMain = new FacMain();
			}

			/* 將每筆資料放入Tota的OcList */

			OccursList occurslist = new OccursList();
			occurslist.putParam("OOCustNo", t.getCustNo());
			occurslist.putParam("OOFacmNo", t.getFacmNo());
			CustMain tCustMain = sCustMainService.custNoFirst(t.getCustNo(), t.getCustNo(), titaVo);
			if (tCustMain != null) {
				occurslist.putParam("OOCustName", tCustMain.getCustName());
			} else {
				occurslist.putParam("OOCustName", "");
			}
			occurslist.putParam("OOApplNo", t.getApplNo());
			occurslist.putParam("OOCurrencyCode", tFacMain.getCurrencyCode());
			occurslist.putParam("OOLineAmt", tFacMain.getLineAmt());

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
			occurslist.putParam("OOMApplNo", t.getMainApplNo());

			this.totaVo.addOccursList(occurslist);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}