package com.st1.itx.trade.L8;

import java.util.ArrayList;

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
import com.st1.itx.db.service.CdBranchGroupService;
import com.st1.itx.db.domain.CdBranch;
import com.st1.itx.db.domain.CdBranchGroup;
import com.st1.itx.db.domain.CdBranchGroupId;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.CdBranchService;
import com.st1.itx.db.service.CdEmpService;

@Service("L8R53")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class L8R53 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	TxAmlCreditService txAmlCreditService;

	@Autowired
	CdBcmService cdBcmService;

	@Autowired
	CdBranchService cdBranchService;

	@Autowired
	CdBranchGroupService cdBranchGroupService;
	
	@Autowired
	CdEmpService cdEmpService;

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

		this.totaVo.putParam("DataDt", txAmlCredit.getDataDt());
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
		
		String brNoX = "";
		CdBranch cdBranch = cdBranchService.findById(txAmlCredit.getProcessBrNo(), titaVo);
		if (cdBranch != null) {
			brNoX = cdBranch.getBranchShort();
		}
		this.totaVo.putParam("LastProcessBrNoX", brNoX);
		
		String groupNoX = "";
		CdBranchGroupId cdBranchGroupId = new CdBranchGroupId();
		cdBranchGroupId.setBranchNo(txAmlCredit.getProcessBrNo());
		cdBranchGroupId.setGroupNo(txAmlCredit.getProcessGroupNo());
		CdBranchGroup cdBranchGroup = cdBranchGroupService.findById(cdBranchGroupId, titaVo);
		if (cdBranchGroup != null) {
			groupNoX = cdBranchGroup.getGroupItem();
		}
		this.totaVo.putParam("LastGroupNoX", groupNoX);

		this.totaVo.putParam("LastProcessTlrNo", txAmlCredit.getProcessTlrNo());

		String tlrItem = "";
		CdEmp cdEmp = cdEmpService.findById(txAmlCredit.getProcessTlrNo(), titaVo);
		if (cdEmp != null) {
			tlrItem = cdEmp.getFullname();
		}

		this.totaVo.putParam("LastProcessTlrNoX", tlrItem);

		this.totaVo.putParam("LastProcessNote", txAmlCredit.getProcessNote());

		this.addList(this.totaVo);
		return this.sendList();
	}

}