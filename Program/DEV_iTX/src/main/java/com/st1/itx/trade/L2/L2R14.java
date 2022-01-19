package com.st1.itx.trade.L2;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimApplNo=9,7
 */
/**
 * L2R14 以核准編號，戶號，額度
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L2R14")
@Scope("prototype")
public class L2R14 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public ClMainService clMainService;
	@Autowired
	public ClFacService clFacService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R14 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("RimCustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("RimFacmNo"));

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 10;

		ClFac lClFac = clFacService.mainClNoFirst(iCustNo, iFacmNo, "Y", titaVo);
		if (lClFac == null) {
			throw new LogicException("E2003", "擔保品與額度關聯檔");
		} else {
			this.totaVo.putParam("L2r14ClCode1", lClFac.getClCode1());
			this.totaVo.putParam("L2r14ClCode2", lClFac.getClCode2());
			this.totaVo.putParam("L2r14ClNo", lClFac.getClNo());
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}