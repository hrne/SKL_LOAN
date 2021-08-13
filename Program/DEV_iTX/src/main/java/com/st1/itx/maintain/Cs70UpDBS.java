package com.st1.itx.maintain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.db.domain.TxAuthorize;
import com.st1.itx.db.service.TxAuthorizeService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.tradeService.CommBuffer;

@Component("cs70UpDBS")
@Scope("prototype")
public class Cs70UpDBS extends CommBuffer {

	@Autowired
	private TxAuthorizeService txAuthorizeService;

	@Override
	public void exec() throws LogicException {
		/* 授權紀錄 */
		this.authorizeRecord();
	}

	private void authorizeRecord() throws LogicException {
		this.info("Cs70UpDBS authorizeRecord..");

		/* 主管授權 && 主管放行 */
		if ((this.titaVo.getSupNo() != null && !this.titaVo.getSupNo().isEmpty() && this.titaVo.getEmpNos() != null && !this.titaVo.getEmpNos().isEmpty()) || this.titaVo.getActFgI() == 2)
			;
		else
			return;

		String supNo = this.titaVo.isActfgRelease() && !this.titaVo.isHcodeErase() ? this.titaVo.getTlrNo() : this.titaVo.getSupNo();
		String tlrNo = this.titaVo.isActfgRelease() && !this.titaVo.isHcodeErase() ? this.titaVo.getEmpNot() : this.titaVo.getTlrNo();
		String reasonCode = this.titaVo.getRqsp() != null && this.titaVo.getRqsp().length() >= 5 ? this.titaVo.getRqsp().substring(0, 5) : "";
		String reason = !reasonCode.isEmpty() && this.titaVo.getRqsp().length() >= 6 ? this.titaVo.getRqsp().substring(5) : "";

		reasonCode = this.titaVo.isActfgRelease() && !this.titaVo.isHcodeErase() ? "00000" : reasonCode;
		reason = this.titaVo.isActfgRelease() && !this.titaVo.isHcodeErase() ? "交易主管放行" : reason;

		String txcd = this.titaVo.getTxcd();
		String txSeq = this.titaVo.getTxSeq();
		int entdy = this.titaVo.getEntDyI();

		String temp = this.titaVo.getDataBase();
		this.titaVo.setDataBaseOnLine();

		TxAuthorize txAuthorize = new TxAuthorize();
		txAuthorize.setSupNo(supNo);
		txAuthorize.setTlrNo(tlrNo);
		txAuthorize.setReasonCode(reasonCode);
		txAuthorize.setReason(reason);
		txAuthorize.setEntdy(entdy);
		txAuthorize.setTxcd(txcd);
		txAuthorize.setTxSeq(txSeq);

		try {
			this.txAuthorizeService.insert(txAuthorize, titaVo);
			this.titaVo.putParam(ContentName.dataBase, temp);
		} catch (DBException e) {
			this.titaVo.putParam(ContentName.dataBase, temp);
			throw new LogicException("EC000", "授權記錄檔錯誤");
		}
	}
}