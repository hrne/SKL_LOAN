package com.st1.itx.trade.L8;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.db.domain.TxAmlCredit;
import com.st1.itx.db.domain.TxAmlCreditId;
import com.st1.itx.db.service.TxAmlCreditService;

import com.st1.itx.db.domain.CdBcm;
import com.st1.itx.db.service.CdBcmService;

import com.st1.itx.db.domain.CdBranch;
import com.st1.itx.db.service.CdBranchService;

import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.service.TxTellerService;

@Service("L8R53")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class L8R53 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R53.class);

	/* DB服務注入 */
	@Autowired
	TxAmlCreditService txAmlCreditService;

	@Autowired
	CdBcmService cdBcmService;

	@Autowired
	CdBranchService cdBranchService;

	@Autowired
	TxTellerService txTellerService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8R53 ");
		this.totaVo.init(titaVo);

		int dataDt = parse.stringToInteger(titaVo.get("DataDt")) + 19110000;
		String custKey = titaVo.get("CustKey").trim();

		TxAmlCreditId txAmlCreditId = new TxAmlCreditId(dataDt, custKey);
		TxAmlCredit txAmlCredit = txAmlCreditService.findById(txAmlCreditId, titaVo);

		if (txAmlCredit == null) {
			throw new LogicException("E0001", dataDt + "/" + custKey);
		}

		this.totaVo.putParam("DataDt", txAmlCredit.getDataDt() - 19110000);
		this.totaVo.putParam("CustKey", txAmlCredit.getCustKey());
		this.totaVo.putParam("RRSeq", txAmlCredit.getRRSeq());
		this.totaVo.putParam("ReviewType", txAmlCredit.getReviewType());
		this.totaVo.putParam("Unit", txAmlCredit.getUnit());
		CdBcm cdBcm = cdBcmService.findById(txAmlCredit.getUnit(), titaVo);
		String unitItem = "";
		if (cdBcm != null) {
			unitItem = cdBcm.getUnitItem();
		}
		this.totaVo.putParam("UnitItem", unitItem);
		this.totaVo.putParam("IsStatus", txAmlCredit.getIsStatus());
		this.totaVo.putParam("ConfirmStatus", txAmlCredit.getWlfConfirmStatus());
		this.totaVo.putParam("ProcessType", txAmlCredit.getProcessType());
		this.totaVo.putParam("ProcessCount", txAmlCredit.getProcessCount());

		this.totaVo.putParam("LastProcessDate", txAmlCredit.getProcessDate());
		
		this.totaVo.putParam("LastProcessBrNo", txAmlCredit.getProcessBrNo());
		String groupNoX = getGroupNoX(txAmlCredit, titaVo);
		this.totaVo.putParam("LastGroupNoX", groupNoX);
		
		this.totaVo.putParam("LastProcessTlrNo", txAmlCredit.getProcessTlrNo());

		String tlrItem ="";
		TxTeller txTeller = txTellerService.findById(txAmlCredit.getProcessTlrNo(), titaVo);
		if (txTeller != null) {
			tlrItem = txTeller.getTlrItem();
		}
		
		this.totaVo.putParam("LastProcessTlrNoX", tlrItem);
		
		this.totaVo.putParam("LastProcessNote", txAmlCredit.getProcessNote());

		this.addList(this.totaVo);
		return this.sendList();
	}

	private String getGroupNoX(TxAmlCredit txAmlCredit, TitaVo titaVo) {
		String groupNoX = "";

		CdBranch cdBranch = cdBranchService.findById(txAmlCredit.getProcessBrNo(), titaVo);

		if (cdBranch != null) {
			if ("1".equals(txAmlCredit.getProcessGroupNo())) {
				groupNoX = cdBranch.getGroup1();
			} else if ("2".equals(txAmlCredit.getProcessGroupNo())) {
				groupNoX = cdBranch.getGroup2();
			} else if ("3".equals(txAmlCredit.getProcessGroupNo())) {
				groupNoX = cdBranch.getGroup3();
			} else if ("4".equals(txAmlCredit.getProcessGroupNo())) {
				groupNoX = cdBranch.getGroup4();
			} else if ("5".equals(txAmlCredit.getProcessGroupNo())) {
				groupNoX = cdBranch.getGroup5();
			} else if ("6".equals(txAmlCredit.getProcessGroupNo())) {
				groupNoX = cdBranch.getGroup6();
			} else if ("7".equals(txAmlCredit.getProcessGroupNo())) {
				groupNoX = cdBranch.getGroup7();
			} else if ("8".equals(txAmlCredit.getProcessGroupNo())) {
				groupNoX = cdBranch.getGroup8();
			} else if ("9".equals(txAmlCredit.getProcessGroupNo())) {
				groupNoX = cdBranch.getGroup9();
			} else if ("A".equals(txAmlCredit.getProcessGroupNo())) {
				groupNoX = cdBranch.getGroup10();
			}
		}
		return groupNoX;
	}
}