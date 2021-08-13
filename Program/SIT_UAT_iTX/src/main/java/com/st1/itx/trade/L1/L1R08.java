package com.st1.itx.trade.L1;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.RelsMain;
import com.st1.itx.db.domain.ReltMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.RelsMainService;
import com.st1.itx.db.service.ReltMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L1R08")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L1R08 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L1R08.class);

	@Autowired
	public CustMainService sCustMainService;

	@Autowired
	public RelsMainService sRelsMainService;

	@Autowired
	public ReltMainService sReltMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1R08 ");
		this.totaVo.init(titaVo);

		// TITA參數
		String iCustId = titaVo.getParam("CustId");
		this.info("CustId   = " + iCustId);

//		!是否為利害關係人IsRelated
//		ReltMain
		String IsRelated = "N";
//		!是否為準利害關係人IsLnrelNear
//		RelsMain
		String IsLnrelNear = "N";

		// 利害關係人檔
		ReltMain tReltMain = sReltMainService.ReltIdFirst(iCustId, titaVo);
		if (tReltMain != null) {
			IsRelated = "Y";
		}
		// 準利害關係人檔
		RelsMain tRelsMain = sRelsMainService.RelsIdFirst(iCustId, titaVo);
		if (tRelsMain != null) {
			IsLnrelNear = "Y";
		}

		// !是否為利害關係人
		this.totaVo.putParam("L1r08IsRelated", IsRelated);
		// !是否為準利害關係人
		this.totaVo.putParam("L1r08IsLnrelNear", IsLnrelNear);

		this.addList(this.totaVo);
		return this.sendList();
	}
}