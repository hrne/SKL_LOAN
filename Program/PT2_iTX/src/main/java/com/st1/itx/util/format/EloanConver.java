package com.st1.itx.util.format;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.log.SysLogger;

@Component
@Scope("singleton")
public class EloanConver extends SysLogger {
	@Autowired
	public TxTellerService txTellerService;

	@SuppressWarnings("unchecked")
	public TitaVo exec(String tita) throws JsonParseException, JsonMappingException, IOException, DBException, LogicException {
		this.info("is E-loan!!!");
		Map<String, Object> m = new ObjectMapper().readValue(tita, LinkedHashMap.class);

		TitaVo titaVo = (TitaVo) MySpring.getBean("titaVo");
		if (m.get("BODY") != null) {
			titaVo = this.converToItxTita(m);
			titaVo.setOrgTitaVO(tita);
		} else {
			titaVo = titaVo.getVo(tita);
			titaVo.putParam(ContentName.txCode, titaVo.getTxcd());
		}
		/* 添加交易序號並回寫 */
		TxTeller txTeller = txTellerService.findById(titaVo.getTlrNo());

		if (txTeller == null)
			throw new LogicException("E0001", "上送資料錯誤...");

		int txtno = txTeller.getTxtNo() + 1;
		titaVo.putParam(ContentName.txtno, FormatUtil.pad9(txtno + "", 8));

		txTellerService.holdById(txTeller.getTlrNo());
		txTeller.setTxtNo(txtno);
		txTellerService.update(txTeller);

		return titaVo;
	}

	@SuppressWarnings("unchecked")
	private TitaVo converToItxTita(Map<String, Object> m) throws JsonParseException, JsonMappingException, IOException {

		List<Map<String, Object>> eBody = (List<Map<String, Object>>) m.get("BODY");
		m.remove("BODY");

		TitaVo titaVo = (TitaVo) MySpring.getBean("titaVo");
		titaVo = titaVo.getVo(new ObjectMapper().writeValueAsString(m));
		titaVo.putParam(ContentName.txCode, titaVo.getTxcd());
		titaVo.seteBody(eBody);
		titaVo.putParam(ContentName.kinbr, FormatUtil.padX(titaVo.getKinbr(), 4));
		titaVo.putParam(ContentName.batchno, FormatUtil.padX(titaVo.getBacthNo(), 12));

		return titaVo;
	}

}
