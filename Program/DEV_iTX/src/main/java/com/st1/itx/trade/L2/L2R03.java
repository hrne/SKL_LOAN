package com.st1.itx.trade.L2;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimGrpupId=X,10
 */
/**
 * L2R03 尋找團體戶申請檔資料
 * 
 * @author iza
 * @version 1.0.0
 */

@Service("L2R03")
@Scope("prototype")
public class L2R03 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2R03.class);

	/* DB服務注入 */
	@Autowired
	public FacCaseApplService facCaseApplService;
	@Autowired
	public CustMainService custMainService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R03 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		String iRimGroupId = titaVo.getParam("RimGroupId").trim();

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L2R03"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode == 1 || iRimFuncCode == 2 || iRimFuncCode == 4 || iRimFuncCode == 5)) {
			throw new LogicException(titaVo, "E0010", "L2R03"); // 功能選擇錯誤
		}

		// 查詢客戶資料主檔
		String wkGroupUkey = "";
		CustMain tCustMain = custMainService.custIdFirst(iRimGroupId, titaVo);
		if (tCustMain != null) {
			wkGroupUkey = tCustMain.getCustUKey().trim();
		} else {
			throw new LogicException(titaVo, "E0001", "L2R03 客戶資料主檔"); // 查無資料
		}

		FacCaseAppl tFacCaseAppl = facCaseApplService.caseApplGroupUKeyFirst(wkGroupUkey, 1, 9999999, titaVo);
		/* 如有有找到資料 */
		if (tFacCaseAppl != null) {
			moveTotaCaseAppl(tFacCaseAppl);
		} else {
			if (iRimTxCode.equals("L2112")) {
				moveTotaCaseAppl(new FacCaseAppl());
			} else {
				throw new LogicException(titaVo, "E0001", "L2R03 案件申請檔"); // 查無資料
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 將每筆資料放入Tota
	private void moveTotaCaseAppl(FacCaseAppl mFacCaseAppl) throws LogicException {
		this.totaVo.putParam("OProdNo", mFacCaseAppl.getProdNo());
		this.totaVo.putParam("OCurrencyCode", mFacCaseAppl.getCurrencyCode());
		this.totaVo.putParam("OAcctCode", "");
		this.totaVo.putParam("OApplDate", mFacCaseAppl.getApplDate());
		this.totaVo.putParam("OCreditOfficer", mFacCaseAppl.getCreditOfficer());
		this.totaVo.putParam("OIntroducer", mFacCaseAppl.getIntroducer());
		this.totaVo.putParam("OLoanOfficer", mFacCaseAppl.getLoanOfficer());
		this.totaVo.putParam("OSupervisor", mFacCaseAppl.getSupervisor());
	}
}