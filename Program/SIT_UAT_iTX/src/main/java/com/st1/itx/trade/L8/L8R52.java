package com.st1.itx.trade.L8;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;

import com.st1.itx.db.domain.TxAmlLog;
import com.st1.itx.db.service.TxAmlLogService;

import com.st1.itx.util.common.CheckAml;

@Service("L8R52")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class L8R52 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R52.class);

	/* DB服務注入 */
	@Autowired
	public TxAmlLogService txAmlLogService;

	@Autowired
	public CheckAml checkAml;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8R52 ");
		this.totaVo.init(titaVo);

		String FunCode = titaVo.get("FunCode").trim();
		Long LogNo = Long.valueOf(titaVo.get("LogNo").trim());

		TxAmlLog txAmlLog = txAmlLogService.holdById(LogNo);
		if (txAmlLog == null) {
			throw new LogicException("EC001", "TxAmlLog.LogNo:" + LogNo);
		}

		this.totaVo.putParam("LogNo", txAmlLog.getLogNo());
		this.totaVo.putParam("TransactionId1", txAmlLog.getTransactionId().substring(0, 2));
		this.totaVo.putParam("TransactionId2", txAmlLog.getTransactionId().substring(2));

		Document doc = checkAml.convertStringToXml(txAmlLog.getMsgRg());
		String oName = checkAml.getXmlValue(doc, "Name");

		this.totaVo.putParam("Unit", checkAml.getXmlValue(doc, "Unit"));
		this.totaVo.putParam("AcceptanceUnit", checkAml.getXmlValue(doc, "Acceptance_Unit"));
		this.totaVo.putParam("RoleId", checkAml.getXmlValue(doc, "Role_Id"));
		this.totaVo.putParam("AcctNo", checkAml.getXmlValue(doc, "Acct_No"));
		this.totaVo.putParam("CaseNo", checkAml.getXmlValue(doc, "Case_No"));
		this.totaVo.putParam("Name", checkAml.getXmlValue(doc, "Name"));
		this.totaVo.putParam("EnglishName", checkAml.getXmlValue(doc, "English_Name"));
		this.totaVo.putParam("CustKey", checkAml.getXmlValue(doc, "Cust_Key"));
		this.totaVo.putParam("IdentityCd", checkAml.getXmlValue(doc, "Identity_Cd"));
		this.totaVo.putParam("NationalCd", checkAml.getXmlValue(doc, "National_Cd"));
		this.totaVo.putParam("BirthNationCd", checkAml.getXmlValue(doc, "Birth_Nation_Cd"));
		this.totaVo.putParam("Sex", checkAml.getXmlValue(doc, "SEX"));
		this.totaVo.putParam("BirthEstDt", checkAml.getXmlValue(doc, "Birth_Est_Dt"));
		this.totaVo.putParam("NotifyEmail", checkAml.getXmlValue(doc, "Notify_Email"));
		this.totaVo.putParam("QueryId", checkAml.getXmlValue(doc, "Query_Id"));
		this.totaVo.putParam("SourceId", checkAml.getXmlValue(doc, "Source_Id"));

		this.totaVo.putParam("Status", txAmlLog.getStatus());
		this.totaVo.putParam("StatusCode", txAmlLog.getStatusCode());
		this.totaVo.putParam("StatusDesc", txAmlLog.getStatusDesc());
		this.totaVo.putParam("IsSimilar", txAmlLog.getIsSimilar());
		this.totaVo.putParam("IsSan", txAmlLog.getIsSan());
		this.totaVo.putParam("IsBanNation", txAmlLog.getIsBanNation());

		this.totaVo.putParam("ConfirmStatus", txAmlLog.getConfirmStatus());
		this.totaVo.putParam("ConfirmCode", txAmlLog.getConfirmCode());
		this.totaVo.putParam("ConfirmEmpNo", txAmlLog.getConfirmEmpNo());
		this.totaVo.putParam("ConfirmTranCode", txAmlLog.getConfirmTranCode());

		this.addList(this.totaVo);
		return this.sendList();
	}
}