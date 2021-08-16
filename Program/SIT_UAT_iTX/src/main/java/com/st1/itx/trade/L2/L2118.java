package com.st1.itx.trade.L2;

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
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacShareAppl;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacShareApplService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.ClFacCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L2118 共用額度登錄
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
 * L2118 共用額度登錄
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L2118")
@Scope("prototype")
public class L2118 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacShareApplService facShareApplService;
	@Autowired
	public FacMainService facMainService;

	@Autowired
	public ClFacCom clFacCom;
	
	@Autowired
	Parse parse;
	@Autowired
	SendRsp sendRsp;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	public DataLog datalog;

	// new table 裝tita
	FacShareAppl tFacShareAppl = new FacShareAppl();
	FacMain tFacMain = new FacMain();
	List<FacShareAppl> lFacShareAppl = new ArrayList<FacShareAppl>();
	int iApplNo = 0;
	int iMApplNo = 0;

	String iJcicMiainCustFlag = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2118 ");
		this.info("   titaVo.getHsupCode() = " + titaVo.getHsupCode());

		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		iMApplNo = this.parse.stringToInteger(titaVo.getParam("ApplNo"+1));
		iJcicMiainCustFlag = titaVo.getParam("JcicMergeFlag");
		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "iFuncCode = " + iFuncCode); // 功能選擇錯誤
		}
		// 交易需主管核可
		if (!titaVo.getHsupCode().equals("1")) {
			sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");  //交易需主管核可
		}

		switch (iFuncCode) {
		case 1: // 新增
			// insert 共同借款人資料檔
			insertFacShareAppl(titaVo);

			this.info("funcd = " + iFuncCode);
			break;

		case 4: // 刪除

			// delete 共同借款人資料檔
			deleteFacShareAppl(titaVo);

			this.info("funcd = " + iFuncCode);
			break;
		case 5: // inq
		}

		// 額度與擔保品關聯檔變動處理
		clFacCom.changeClFac(iMApplNo, titaVo);
		
		this.addList(this.totaVo);
		return this.sendList();
	}

	// insert 共同借款人資料檔
	private void insertFacShareAppl(TitaVo titaVo) throws LogicException {

		iMApplNo = parse.stringToInteger(titaVo.get("ApplNo" + 1));
		for (int i = 1; i <= 10; i++) {
			iApplNo = parse.stringToInteger(titaVo.get("ApplNo" + i));
			// 若該筆無資料就離開迴圈
			if (iApplNo == 0) {
				break;
			}

			tFacMain = facMainService.facmApplNoFirst(iApplNo, titaVo);
			if (tFacMain == null) {
				throw new LogicException(titaVo, "E2022", "  核准號碼 = " + iApplNo); // 新增資料已存在
			}
			tFacShareAppl = facShareApplService.findById(iApplNo, titaVo);
			if (tFacShareAppl != null) {
				throw new LogicException(titaVo, "E0002", "共同借款人 = " + iApplNo); // 新增資料已存在
			}

			tFacShareAppl = new FacShareAppl();
			tFacShareAppl.setApplNo(iApplNo);
			tFacShareAppl.setMainApplNo(iMApplNo);
			tFacShareAppl.setCustNo(tFacMain.getCustNo());
			tFacShareAppl.setFacmNo(tFacMain.getFacmNo());
			tFacShareAppl.setKeyinSeq(i);
			tFacShareAppl.setJcicMergeFlag(iJcicMiainCustFlag);

			try {
				facShareApplService.insert(tFacShareAppl, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "共同借款人" + e.getErrorMsg());
			}
		}
	}

	// delete 共同借款人資料檔
	private void deleteFacShareAppl(TitaVo titaVo) throws LogicException {

		iApplNo = this.parse.stringToInteger(titaVo.getParam("ApplNo" + 1));

		tFacShareAppl = facShareApplService.findById(iApplNo, titaVo);
		if (tFacShareAppl != null) {
			throw new LogicException(titaVo, "E2007", "共同借款人 核准號碼 = " + iApplNo); // 刪除資料不存在
		}
		Slice<FacShareAppl> slFacShareAppl = facShareApplService.findMainApplNo(tFacShareAppl.getMainApplNo(), 0,
				Integer.MAX_VALUE, titaVo);
		lFacShareAppl = slFacShareAppl == null ? null : slFacShareAppl.getContent();
		try {
			facShareApplService.deleteAll(lFacShareAppl, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0005", "共同借款人" + e.getErrorMsg());
		}

	}

}