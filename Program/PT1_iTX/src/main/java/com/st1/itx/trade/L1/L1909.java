package com.st1.itx.trade.L1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.db.service.BankRelationSuspectedService;
import com.st1.itx.db.service.springjpa.cm.L1909ServiceImpl;

@Service("L1909")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L1909 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L1909.class);

	@Autowired
	public BankRelationSuspectedService bankRelationSuspectedService;
	
	@Autowired
	L1909ServiceImpl sL1909ServiceImpl;
	
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1909 ");
		this.totaVo.init(titaVo);

		String repCustName = titaVo.getParam("repCustName").trim();
//		Slice<BankRelationSuspected> slbankRelationSuspected = bankRelationSuspectedService.RepCusNameEq(repCustName, 0, Integer.MAX_VALUE, titaVo);
		List<Map<String, String>> slbankRelationSuspected;
		try {
			slbankRelationSuspected = sL1909ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			throw new LogicException("E0000", "此人非疑似準利害關係人");
		}
//		List<BankRelationSuspected> lBankRelationSuspected = slbankRelationSuspected == null ? null : slbankRelationSuspected.getContent();

//		if (lBankRelationSuspected == null) {
		if (slbankRelationSuspected == null) {
			throw new LogicException("E0000", "此人非疑似準利害關係人");
		}

//		for (BankRelationSuspected bankRelationSuspected : lBankRelationSuspected) {
		for (Map<String, String> bankRelationSuspected : slbankRelationSuspected) {
			OccursList OccursList = new OccursList();

			OccursList.putParam("oRepCustName", bankRelationSuspected.get("RepCusName"));
			OccursList.putParam("oCustId", bankRelationSuspected.get("CustId"));
			OccursList.putParam("oCustName", bankRelationSuspected.get("CustName"));
			OccursList.putParam("oSubCom", bankRelationSuspected.get("SubCom"));
//			OccursList.putParam("oLastUpdate", this.parse.timeStampToString(bankRelationSuspected.get("LastUpdate")));
			OccursList.putParam("oLastUpdate", bankRelationSuspected.get("LastUpdate"));
			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(OccursList);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}