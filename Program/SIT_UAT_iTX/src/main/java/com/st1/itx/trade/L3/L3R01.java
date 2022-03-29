package com.st1.itx.trade.L3;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdLoanNotYet;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.LoanNotYet;
import com.st1.itx.db.domain.LoanNotYetId;
import com.st1.itx.db.service.CdLoanNotYetService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanNotYetService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * L3R01 尋找未齊件管理檔
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3R01")
@Scope("prototype")
public class L3R01 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public LoanNotYetService loanNotYetService;
	@Autowired
	public CdLoanNotYetService cdLoanNotYetService;
	@Autowired
	Parse parse;
	@Autowired
	DateUtil dDateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3R01 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimCustNo = this.parse.stringToInteger(titaVo.getParam("RimCustNo"));
		int iRimFacmNo = this.parse.stringToInteger(titaVo.getParam("RimFacmNo"));
		int iFunCd = this.parse.stringToInteger(titaVo.getParam("RimFunCd"));
		int iRimWkYetDate = this.parse.stringToInteger(titaVo.getParam("RimWkYetDate"));
		String iRimNotYetCode = titaVo.getParam("RimNotYetCode");

		int wkYetDays = 0;
		int wkYetDate = 0;
		int wkCloseDate = 0;

		// 查詢額度主檔
		FacMain tFacMain = facMainService.findById(new FacMainId(iRimCustNo, iRimFacmNo), titaVo);
		if (tFacMain == null) {
			throw new LogicException(titaVo, "E0001", " 額度主檔 借款人戶 = " + iRimCustNo + " 額度編號 = " + iRimFacmNo); // 查詢資料不存在
		}
		// 查詢未齊件代碼檔
		CdLoanNotYet cdLoanNotYet = cdLoanNotYetService.findById(iRimNotYetCode, titaVo);
		if (cdLoanNotYet != null) {
			wkYetDays = cdLoanNotYet.getYetDays();
		}

		dDateUtil.init();
		dDateUtil.getbussDate(iRimWkYetDate, wkYetDays);
		wkYetDate = dDateUtil.getCalenderDay() - 19110000;
		// 查詢未齊件管理檔
		LoanNotYet tLoanNotYet = loanNotYetService.findById(new LoanNotYetId(iRimCustNo, iRimFacmNo, iRimNotYetCode), titaVo);

		// 新增時,有資料時顯示錯誤訊息
		if (iFunCd == 1) {
			if (tLoanNotYet != null) {
				throw new LogicException(titaVo, "E0002", "請使用修改功能"); // 新增資料已存在
			}
		} else if (iFunCd == 2) {
			if (tLoanNotYet == null) {
				throw new LogicException(titaVo, "E0003", "戶號 = " + iRimCustNo + "額度編號 = " + iRimFacmNo + " 未齊件代號 = " + iRimNotYetCode); // 修改資料不存在
			} else {
				wkCloseDate = tLoanNotYet.getCloseDate();
				wkYetDate = tLoanNotYet.getYetDate();
			}
			
		} else if (iFunCd >= 4) {
			if (tLoanNotYet == null) {
				throw new LogicException(titaVo, "E0001", "戶號 = " + iRimCustNo + "額度編號 = " + iRimFacmNo + " 未齊件代號 = " + iRimNotYetCode); // 查詢資料不存在
			} else {
				wkCloseDate = tLoanNotYet.getCloseDate();
				wkYetDate = tLoanNotYet.getYetDate();
			}
		}

		this.totaVo.putParam("L3r01YetDate", wkYetDate);
		this.totaVo.putParam("L3r01CloseDate", wkCloseDate);

		if (tLoanNotYet != null) {
			if (tLoanNotYet.getReMark() != null) {
				this.totaVo.putParam("L3r01ReMark", tLoanNotYet.getReMark());
			} else {
				this.totaVo.putParam("L3r01ReMark", "");
			}
		} else {
			this.totaVo.putParam("L3r01ReMark", "");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}