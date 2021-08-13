package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BankRmtf;
import com.st1.itx.db.domain.BankRmtfId;
import com.st1.itx.db.service.BankRmtfService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L4R04")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4R04 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L4R04.class);

	/* DB服務注入 */
	@Autowired
	public BankRmtfService bankRmtfService;

	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R04 ");
		this.totaVo.init(titaVo);

//		#RimAcDate=D,7,S
//		#RimBatchNo=X,6,L
//		#RimDetailSeq=A,6,L
		int iAcDate = parse.stringToInteger(titaVo.getParam("RimAcDate").trim()) + 19110000;
		String iBatchNo = titaVo.getParam("RimBatchNo").trim();
		int iDetailSeq = parse.stringToInteger(titaVo.getParam("RimDetailSeq").trim());

		BankRmtf tBankRmtf = new BankRmtf();
		BankRmtfId tBankRmtfId = new BankRmtfId();

		tBankRmtfId.setAcDate(iAcDate);
		tBankRmtfId.setBatchNo(iBatchNo);
		tBankRmtfId.setDetailSeq(iDetailSeq);

		tBankRmtf = bankRmtfService.findById(tBankRmtfId);

		if (tBankRmtf != null) {
			this.totaVo.putParam("L4r04DepAcctNo", tBankRmtf.getDepAcctNo());
			this.totaVo.putParam("L4r04EntryDate", tBankRmtf.getEntryDate());
			this.totaVo.putParam("L4r04DscptCode", tBankRmtf.getDscptCode());
			this.totaVo.putParam("L4r04VirtualAcctNo", tBankRmtf.getVirtualAcctNo());
			this.totaVo.putParam("L4r04WithdrawAmt", tBankRmtf.getWithdrawAmt());
			this.totaVo.putParam("L4r04DepositAmt", tBankRmtf.getDepositAmt());
			this.totaVo.putParam("L4r04Balance", tBankRmtf.getBalance());
			this.totaVo.putParam("L4r04RemintBank", tBankRmtf.getRemintBank());
			this.totaVo.putParam("L4r04TraderInfo", tBankRmtf.getTraderInfo());
		} else {
			throw new LogicException(titaVo, "E0001", " 查無資料");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}