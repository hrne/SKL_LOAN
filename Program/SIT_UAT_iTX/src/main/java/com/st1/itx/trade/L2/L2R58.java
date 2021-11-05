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
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.AcReceivableId;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L2R58")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R58 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2R58.class);

	@Autowired
	public AcReceivableService sAcReceivableService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R58 ");
		this.totaVo.init(titaVo);

		// tita
		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFunCd"));
		int iCustNo = parse.stringToInteger(titaVo.getParam("RimCustNo"));
		int iFacmNo = parse.stringToInteger(titaVo.getParam("RimFacmNo"));
		String iRvNo = titaVo.getParam("RimRvNo");
		String iAcctCode = titaVo.getParam("RimAcctCode");

		AcReceivable tAcReceivable = sAcReceivableService
				.findById(new AcReceivableId(iAcctCode, iCustNo, iFacmNo, iRvNo), titaVo);
		if (tAcReceivable == null) {
			switch (iFunCd) {
			case 1:
				break;
			case 2:
			case 4:
			case 5:
				throw new LogicException(titaVo, "E2003", "此筆資料不存在銷帳檔"); // 查無資料
			}
		}
		this.totaVo.putParam("OCustNo", tAcReceivable.getCustNo());
		this.totaVo.putParam("OFacmNo", tAcReceivable.getFacmNo());
		this.totaVo.putParam("OSyndFeeCode", tAcReceivable.getRvNo().substring(2, 4));
		this.totaVo.putParam("OSyndFee", tAcReceivable.getRvAmt());
		this.totaVo.putParam("ORmk", tAcReceivable.getSlipNote());
		this.totaVo.putParam("OAcctCode", tAcReceivable.getAcctCode());

		this.addList(this.totaVo);
		return this.sendList();
	}
}