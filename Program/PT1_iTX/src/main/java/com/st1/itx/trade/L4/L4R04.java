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
import com.st1.itx.db.domain.BatxDetail;
import com.st1.itx.db.domain.BatxDetailId;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.service.BankRmtfService;
import com.st1.itx.db.service.BatxDetailService;
import com.st1.itx.db.service.CdCodeService;
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

	/* DB服務注入 */
	@Autowired
	public BatxDetailService batxDetailService;
	@Autowired
	public BankRmtfService bankRmtfService;
	@Autowired
	public CdCodeService cdCodeService;

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
		BatxDetailId tBatxDetailId = new BatxDetailId();
		tBatxDetailId.setAcDate(iAcDate);
		tBatxDetailId.setBatchNo(iBatchNo);
		tBatxDetailId.setDetailSeq(iDetailSeq);
		BatxDetail tDetail = batxDetailService.findById(tBatxDetailId, titaVo);
		if (tDetail == null) {
			throw new LogicException(titaVo, "E0001", " 查無資料");
		}
		BankRmtf tBankRmtf = new BankRmtf();
		BankRmtfId tBankRmtfId = new BankRmtfId();
		if (!"".equals(tDetail.getMediaKind())) {
			tBankRmtfId.setAcDate(tDetail.getMediaDate());
			tBankRmtfId.setBatchNo("BATX" + tDetail.getMediaKind()); // 上傳批號後兩碼(匯款轉帳、支票兌現)
			tBankRmtfId.setDetailSeq(tDetail.getMediaSeq());
		} else {
			tBankRmtfId.setAcDate(tDetail.getAcDate());
			tBankRmtfId.setBatchNo(tDetail.getBatchNo());
			tBankRmtfId.setDetailSeq(tDetail.getDetailSeq());
		}
		tBankRmtf = bankRmtfService.findById(tBankRmtfId);

		if (tBankRmtf == null) {
			throw new LogicException(titaVo, "E0001", " 查無資料");
		}
		String dscpItem = "";
		CdCode tCdCode = cdCodeService.findById(new CdCodeId("BankRmftCode", tBankRmtf.getDscptCode()), titaVo);
		if (tCdCode != null) {
			dscpItem = tCdCode.getItem();
		}
		this.totaVo.putParam("L4r04DepAcctNo", tBankRmtf.getDepAcctNo());
		this.totaVo.putParam("L4r04EntryDate", tBankRmtf.getEntryDate());
		this.totaVo.putParam("L4r04DscptCode", tBankRmtf.getDscptCode() + " " + dscpItem);
		this.totaVo.putParam("L4r04VirtualAcctNo", tBankRmtf.getVirtualAcctNo());
		this.totaVo.putParam("L4r04WithdrawAmt", tBankRmtf.getWithdrawAmt());
		this.totaVo.putParam("L4r04DepositAmt", tBankRmtf.getDepositAmt());
		this.totaVo.putParam("L4r04Balance", tBankRmtf.getBalance());
		this.totaVo.putParam("L4r04RemintBank", tBankRmtf.getRemintBank());
		this.totaVo.putParam("L4r04TraderInfo", tBankRmtf.getTraderInfo());
		this.addList(this.totaVo);
		return this.sendList();
	}
}