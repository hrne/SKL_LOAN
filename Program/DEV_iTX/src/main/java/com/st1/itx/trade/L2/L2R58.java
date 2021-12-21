package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
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

		AcReceivable tAcReceivable = new AcReceivable();
		Slice<AcReceivable> slAcReceivable = null;

		slAcReceivable = sAcReceivableService.useL2r58Eq(iCustNo, iFacmNo, 0, 9, iRvNo + "%", 0, Integer.MAX_VALUE, titaVo);
		List<AcReceivable> lAcReceivable = slAcReceivable == null ? null : slAcReceivable.getContent();
		if (lAcReceivable == null) {
			switch (iFunCd) {
			case 1:
				break;
			case 2:
			case 4:
			case 5:
				throw new LogicException(titaVo, "E2003", "此筆資料不存在銷帳檔"); // 查無資料
			}
		}
		if (lAcReceivable.size() == 1) {

			this.totaVo.putParam("OCustNo", lAcReceivable.get(0).getCustNo());
			this.totaVo.putParam("OFacmNo", lAcReceivable.get(0).getFacmNo());
			this.totaVo.putParam("OSyndFeeCode", lAcReceivable.get(0).getRvNo().substring(3, 5));
			this.totaVo.putParam("OSyndFee", lAcReceivable.get(0).getRvAmt());
			this.totaVo.putParam("ORmk", lAcReceivable.get(0).getSlipNote());
			this.totaVo.putParam("OAcctCode", lAcReceivable.get(0).getAcctCode());
			this.totaVo.putParam("OIsAllocation", "N");
			this.totaVo.putParam("OSyndFeeYearMonth", "0");
			this.totaVo.putParam("OAllocationFreq", "0");
			this.totaVo.putParam("OAllocationTimes", "0");

		} else {

			BigDecimal wkSyndFeeAmt = BigDecimal.ZERO; // 費用金額
			for (AcReceivable t : lAcReceivable) {

				wkSyndFeeAmt = wkSyndFeeAmt.add(t.getRvAmt());
				OccursList occursList = new OccursList();

				occursList.putParam("OOYearMonth", t.getRvNo().substring(10, 15));// 費用年月
				occursList.putParam("OOAllocationAmt", t.getRvAmt());// 費用年月
				occursList.putParam("OOCloseFg", t.getClsFlag() == 1 ? "Y" : "");// 已銷記號
				occursList.putParam("OOReceivableFg", t.getReceivableFlag());// 已銷記號

				this.totaVo.addOccursList(occursList);
			}

			this.totaVo.putParam("OCustNo", lAcReceivable.get(0).getCustNo());
			this.totaVo.putParam("OFacmNo", lAcReceivable.get(0).getFacmNo());
			this.totaVo.putParam("OSyndFeeCode", lAcReceivable.get(0).getRvNo().substring(3, 5));
			this.totaVo.putParam("OSyndFee", wkSyndFeeAmt);
			this.totaVo.putParam("ORmk", lAcReceivable.get(0).getSlipNote());
			this.totaVo.putParam("OAcctCode", lAcReceivable.get(0).getAcctCode());
			this.totaVo.putParam("OIsAllocation", "Y");// 是否攤提
			this.totaVo.putParam("OSyndFeeYearMonth", lAcReceivable.get(0).getRvNo().substring(10, 15));// 年月
			this.totaVo.putParam("OAllocationFreq", "0");// 攤提週期
			this.totaVo.putParam("OAllocationTimes", lAcReceivable.size());// 攤提次數
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}