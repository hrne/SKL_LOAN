package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R21")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R21 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2R21.class);

	/* DB服務注入 */
	@Autowired
	public FacMainService sFacMainService;

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R21 ");
		this.totaVo.init(titaVo);

		// tita
		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFunCd"));
		int iApplNo = parse.stringToInteger(titaVo.getParam("RimApplNo"));

		FacMain tFacMain = sFacMainService.facmApplNoFirst(iApplNo, titaVo);
		// 查無資料,拋錯
		if (tFacMain == null) {
			throw new LogicException("E0001", "該核准號碼不存在額度主檔 L2R21(FacMain)");
		}
		this.info("tFacMain L2R21" + tFacMain);
		int custno = tFacMain.getCustNo();
		CustMain tCustMain = sCustMainService.custNoFirst(custno, custno, titaVo);
		if (tCustMain == null) {
			tCustMain = new CustMain();
		}

		this.totaVo.putParam("L2r21CustNo", custno);
		this.totaVo.putParam("L2r21FacmNo", tFacMain.getFacmNo());
		this.totaVo.putParam("L2r21CustName", tCustMain.getCustName());
		this.totaVo.putParam("L2r21LineAmt", tFacMain.getLineAmt());

		this.addList(this.totaVo);
		return this.sendList();
	}
}