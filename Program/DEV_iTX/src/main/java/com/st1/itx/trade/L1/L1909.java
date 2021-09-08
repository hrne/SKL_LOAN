package com.st1.itx.trade.L1;

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
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.db.domain.BankRelationSuspected;
import com.st1.itx.db.service.BankRelationSuspectedService;
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
	Parse parse;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1909 ");
		this.totaVo.init(titaVo);
		
		String repCustName = titaVo.getParam("repCustName").trim();
		Slice<BankRelationSuspected> slbankRelationSuspected = bankRelationSuspectedService.RepCusNameEq(repCustName, 0, Integer.MAX_VALUE, titaVo);
		List<BankRelationSuspected> lBankRelationSuspected = slbankRelationSuspected == null ? null : slbankRelationSuspected.getContent();

		if (lBankRelationSuspected == null) {
			throw new LogicException("E0001", "");
		}

		for (BankRelationSuspected bankRelationSuspected : lBankRelationSuspected) {
			OccursList OccursList = new OccursList();
			
			OccursList.putParam("oRepCustName", bankRelationSuspected.getRepCusName());
			OccursList.putParam("oCustId", bankRelationSuspected.getCustId());
			OccursList.putParam("oCustName", bankRelationSuspected.getCustName());
			OccursList.putParam("oSubCom", bankRelationSuspected.getSubCom());
//			OccursList.putParam("oLastUpdate", this.parse.timeStampToString(bankRelationSuspected.getLastUpdate()));
			OccursList.putParam("oLastUpdate", String.valueOf(titaVo.getEntDyI()));
			
			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(OccursList);
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}