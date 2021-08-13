package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.InnReCheck;
import com.st1.itx.db.domain.InnReCheckId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.InnReCheckService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimYearMonth=9,5
 * RimConditionCode=9,2
 * RimCustNo=9,7
 * RimFacmNo=9,3
 */
@Service("L5R08") // 尋找覆審案件明細檔資料
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L5R08 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L5R08.class);

	/* DB服務注入 */
	@Autowired
	public InnReCheckService sInnReCheckService;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5R08 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iConditionCode = this.parse.stringToInteger(titaVo.getParam("RimConditionCode"));
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("RimCustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("RimFacmNo"));
		int iYearMonth = this.parse.stringToInteger(titaVo.getParam("RimYearMonth"));
		int iFYearMonth = iYearMonth + 191100;

		// 初始值Tota
		moveTotaInnReCheck(new InnReCheck(), " ");

		// 查詢客戶資料主檔
		CustMain tCustMain = new CustMain();
		tCustMain = sCustMainService.custNoFirst(iCustNo, iCustNo, titaVo);
		if (tCustMain == null) {
			throw new LogicException(titaVo, "E0001", "客戶資料主檔"); // 查無資料
		}

		// 查詢覆審案件明細檔
		InnReCheck tInnReCheck = sInnReCheckService.findById(new InnReCheckId(iFYearMonth, iConditionCode, iCustNo, iFacmNo), titaVo);

		/* 如有找到資料 */
		if (tInnReCheck != null) {
			moveTotaInnReCheck(tInnReCheck, tCustMain.getCustName());
		} else {
			throw new LogicException(titaVo, "E0001", "覆審案件明細檔"); // 查無資料
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 將資料放入Tota
	// 覆審案件明細檔
	private void moveTotaInnReCheck(InnReCheck mInnReCheck, String mCustName) throws LogicException {
		int iReChkYearMonth = mInnReCheck.getReChkYearMonth();
		this.totaVo.putParam("L5R08CustName", mCustName);
		this.totaVo.putParam("L5R08ReCheckCode", mInnReCheck.getReCheckCode());
		if (iReChkYearMonth == 0) {
			this.totaVo.putParam("L5R08ReChkYear", 0);
			this.totaVo.putParam("L5R08ReChkMonth", 0);
		}else {
			iReChkYearMonth = iReChkYearMonth-191100;
			String sReChkYearMonth = String.valueOf(iReChkYearMonth);
			int sReChkYear = Integer.valueOf(sReChkYearMonth.substring(0,3));
			int sReChkMonth = Integer.valueOf(sReChkYearMonth.substring(3,5));
			this.totaVo.putParam("L5R08ReChkYear", sReChkYear);
			this.totaVo.putParam("L5R08ReChkMonth", sReChkMonth);
		}
		this.totaVo.putParam("L5R08ReChkUnit", mInnReCheck.getReChkUnit());
		this.totaVo.putParam("L5R08FollowMark", mInnReCheck.getFollowMark());
		this.totaVo.putParam("L5R08Remark", mInnReCheck.getRemark());

	}

}