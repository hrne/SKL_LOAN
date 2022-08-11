package com.st1.itx.maintain;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
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

		/* 主管授權 */
		if ((this.titaVo.getSupNo() != null && !this.titaVo.getSupNo().isEmpty() && this.titaVo.getEmpNos() != null && !this.titaVo.getEmpNos().isEmpty()))
			;
		else
			return;

		/* 更正&放行跳過 */
//		if (this.titaVo.getActFgI() == 2 || this.titaVo.isHcodeErase())
//			return;

		String supNo = this.titaVo.getSupNo().trim().isEmpty() ? this.titaVo.getEmpNos().trim() : this.titaVo.getSupNo().trim();
		String tlrNo = this.titaVo.getTlrNo().trim().isEmpty() ? this.titaVo.getEmpNot().trim() : this.titaVo.getTlrNo().trim();

		if (supNo.equals(tlrNo) || supNo.isEmpty() || tlrNo.isEmpty())
			throw new LogicException("EC000", supNo.equals(tlrNo) ? "授權記錄檔錯誤,交易與授權人員一致" : "授權記錄檔錯誤,交易或授權人員錯誤");

		String reason = Objects.isNull(this.titaVo.getRqsp()) ? "" : this.titaVo.getRqsp();
		List<Map<String, String>> reasonLi = new ArrayList<Map<String, String>>();

		for (String s : reason.split(";")) {
			String no = s.substring(0, 5).trim();
			String msg = s.substring(5).trim();

			Map<String, String> m = new LinkedHashMap<String, String>();
			m.put("NO", no);
			m.put("MSG", msg);
			reasonLi.add(m);
		}

		String txcd = this.titaVo.getTxcd();
		String txSeq = this.titaVo.getTxSeq();
		int entdy = this.titaVo.getEntDyI();

		String temp = this.titaVo.getDataBase();
		this.titaVo.setDataBaseOnLine();

		TxAuthorize txAuthorize = new TxAuthorize();
		txAuthorize.setSupNo(supNo);
		txAuthorize.setTlrNo(tlrNo);
		if (!titaVo.getReason().isEmpty())
			txAuthorize.setTradeReason(titaVo.getReason());
		txAuthorize.setEntdy(entdy);
		txAuthorize.setTxcd(txcd);
		txAuthorize.setTxSeq(txSeq);

		try {
			txAuthorize.setReasonFAJson(new ObjectMapper().configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true).writeValueAsString(reasonLi));
			this.txAuthorizeService.insert(txAuthorize, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			throw new LogicException("EC000", "授權記錄檔錯誤");
		} finally {
			this.titaVo.putParam(ContentName.dataBase, temp);
		}
	}
}