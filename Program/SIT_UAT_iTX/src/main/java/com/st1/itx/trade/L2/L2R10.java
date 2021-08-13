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
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimCaseNo=9,7
 */
/**
 * L2R10 以案件編號尋找戶號, 額度編號
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L2R10")
@Scope("prototype")
public class L2R10 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2R10.class);

	/* DB服務注入 */
	@Autowired
	public CustMainService custMainService;
	@Autowired
	public FacMainService facMainService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R10 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimCaseNo = this.parse.stringToInteger(titaVo.getParam("RimCaseNo"));

		FacMain tFacMain = facMainService.facmCreditSysNoFirst(iRimCaseNo, iRimCaseNo, 1, 999, titaVo);
		if (tFacMain == null) {
			throw new LogicException(titaVo, "E0001", "L2R10 額度主檔 案件編號 = " + iRimCaseNo); // 查詢資料不存在
		}

		this.totaVo.putParam("OCustNo", tFacMain.getCustNo());
		this.totaVo.putParam("OFacmNo", tFacMain.getFacmNo());

		this.addList(this.totaVo);
		return this.sendList();
	}
}