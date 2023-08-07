package com.st1.itx.util.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.common.data.CheckAmlVo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.st1.itx.db.domain.TxAmlLog;
import com.st1.itx.db.service.TxAmlLogService;

/**
 * AML姓名檢核 1.checkName 姓名檢核 call by TxAmlCom(AML交易檢核)、L8100(AML姓名檢核)
 * 2.reCheckName AML姓名再檢核 call by TxAmlCom 3.refreshStatus 更新指定序號檢核狀態 call by
 * TxAmlCom、L8100
 * 
 * @author st1
 *
 */
@Component("checkAml")
@Scope("prototype")
public class CheckAml extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public TxAmlLogService txAmlLogService;

	@Autowired
	DateUtil dateUtil;

	// aml check control flag 0.正常 1.aml系統異常改由人工確認 2.測試套不檢查,並預設非可疑名單
	private int amlflag = 0;
	// aml web api url
	private String amlurl = "";

	// set timeout to 30 seconds
	private int timeout = 30000;

	// connect AML status
	private boolean connectSuccess;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("CheckAml run ... ");

		return null;

	}

	/**
	 * AML姓名檢核
	 * 
	 * @param checkAmlVo 檢查內容
	 * @param titaVo     TitaVo
	 * @return CheckAmlVo
	 * @throws LogicException LogicException
	 */
	public CheckAmlVo checkName(CheckAmlVo checkAmlVo, TitaVo titaVo) throws LogicException {
		this.info("CheckAml checkName ....." + checkAmlVo.toString());
		amlflag = this.txBuffer.getSystemParas().getAmlFg();
		amlurl = this.txBuffer.getSystemParas().getAmlUrl();

		CheckVo(checkAmlVo);

		checkAmlVo = initRs(checkAmlVo);

		String msgrq = makeXml(checkAmlVo);
		String msgrs = connectAml(amlurl, msgrq);
		// insert TxAmlLog

		TxAmlLog txAmlLog = new TxAmlLog();

		txAmlLog.setEntdy(checkAmlVo.getEntdy());
		txAmlLog.setBrNo(checkAmlVo.getBrNo());
		txAmlLog.setRefNo(checkAmlVo.getRefNo());

		txAmlLog.setTransactionId(checkAmlVo.getTransactionId());
		txAmlLog.setAcctNo(checkAmlVo.getAcctNo());
		txAmlLog.setCaseNo(checkAmlVo.getCaseNo());

		txAmlLog.setMsgRg(msgrq);
		txAmlLog.setMsgRs(msgrs);

		String custKey = checkAmlVo.getCustKey();

		if (custKey != null && !custKey.isEmpty()) {
			txAmlLog.setCustId(custKey);
		}

		String refNo = checkAmlVo.getRefNo();
		if (refNo != null && !refNo.isEmpty()) {
			int custNo = 0;

			try {
				custNo = Integer.parseInt(refNo);
			} catch (Exception e) {
				custNo = 0;
			}
			txAmlLog.setCustNo(custNo);
		}

		if (amlflag == 1) {
			txAmlLog.setConfirmStatus("1");
			txAmlLog.setStatusCode("5009");
			txAmlLog.setStatus("Warning");
			txAmlLog.setStatusDesc("AML系統異常，改由人工檢核");
		} else if (amlflag == 2) {
			txAmlLog.setConfirmStatus("0");
			txAmlLog.setStatusCode("0008");
			txAmlLog.setStatus("Success");
			txAmlLog.setStatusDesc("測試套不檢查");
//		} else if ("".equals(msgrs)) {
		} else if (!this.connectSuccess) {
			// 需審查/確認
			txAmlLog.setConfirmStatus("1");
			txAmlLog.setStatusCode("9999");
			txAmlLog.setStatus("Fail");
			txAmlLog.setStatusDesc("連結 " + amlurl + " 發生錯誤:" + msgrs);
		} else {
			Document doc = convertStringToXml(msgrs);

			this.info("checkName.Severity = " + getXmlValue(doc, "Severity"));
			this.info("checkName.StatusCode = " + getXmlValue(doc, "StatusCode"));
			this.info("checkName.StatusDesc = " + getXmlValue(doc, "StatusDesc"));

			txAmlLog.setStatusCode(getXmlValue(doc, "StatusCode"));
			txAmlLog.setStatus(getXmlValue(doc, "Severity"));
			txAmlLog.setStatusDesc(getXmlValue(doc, "StatusDesc"));

			if ("0".equals(txAmlLog.getStatusCode()) && "INFO".equals(txAmlLog.getStatus())) {
				txAmlLog = parseResult(doc, txAmlLog);
			} else {
				// 需審查/確認
				txAmlLog.setConfirmStatus("1");
			}

		}

		try {
			txAmlLog = txAmlLogService.insert(txAmlLog, titaVo);
		} catch (DBException e) {
			throw new LogicException("EC002", "insert TxAmlLog:" + e.getErrorMsg()); // 新增資料
		}
		if ("1".equals(txAmlLog.getConfirmStatus())) {
			this.txBuffer.addAmlList(txAmlLog);
		}
		this.info("CheckAml getAmlList().size=" + this.txBuffer.getAmlList().size());
		checkAmlVo = rspAmlVo(checkAmlVo, txAmlLog);

		return checkAmlVo;
	}

	/**
	 * AML姓名重新檢核
	 * 
	 * @param logNo      logno
	 * @param checkAmlVo 檢查內容
	 * @param titaVo     TitaVo
	 * @return CheckAmlVo
	 * @throws LogicException LogicException
	 */
	public CheckAmlVo reCheckName(long logNo, CheckAmlVo checkAmlVo, TitaVo titaVo) throws LogicException {
		this.info("CheckAml recheckName ....." + logNo);
		amlflag = this.txBuffer.getSystemParas().getAmlFg();
		amlurl = this.txBuffer.getSystemParas().getAmlUrl();

		if (checkAmlVo.getLogNo() <= 0) {
			throw new LogicException("EC004", "CheckAml.reCheckVo : 參數 LogNo 不可為0");
		}

		TxAmlLog txAmlLog = txAmlLogService.holdById(logNo, titaVo);
		if (txAmlLog == null) {
			throw new LogicException("EC001", "TxAmlLog.LogNo:" + logNo);
		}

		String msgrq = makeXml(checkAmlVo);
		String msgrs = connectAml(amlurl, msgrq);
		// insert TxAmlLog

//		txAmlLog.setEntdy(checkAmlVo.getEntdy());
//		txAmlLog.setBrNo(checkAmlVo.getBrNo());
//		txAmlLog.setRefNo(checkAmlVo.getRefNo());

//		txAmlLog.setTransactionId(checkAmlVo.getTransactionId());
//		txAmlLog.setAcctNo(checkAmlVo.getAcctNo());
//		txAmlLog.setCaseNo(checkAmlVo.getCaseNo());

		txAmlLog.setMsgRg(msgrq);
		txAmlLog.setMsgRs(msgrs);

		if (amlflag == 1) {
			txAmlLog.setConfirmStatus("1");
			txAmlLog.setStatusCode("5009");
			txAmlLog.setStatus("Warning");
			txAmlLog.setStatusDesc("AML系統異常，改由人工檢核");
		} else if (amlflag == 2) {
//			txAmlLog.setConfirmStatus("0");
			txAmlLog.setStatusCode("0008");
			txAmlLog.setStatus("Success");
			txAmlLog.setStatusDesc("測試套不檢查");
//		} else if ("".equals(msgrs)) {
		} else if (!this.connectSuccess) {
			// 需審查/確認
			txAmlLog.setConfirmStatus("1");
			txAmlLog.setStatusCode("9999");
			txAmlLog.setStatus("Fail");
			txAmlLog.setStatusDesc("連結 " + amlurl + " 發生錯誤:" + msgrs);
		} else {
			Document doc = convertStringToXml(msgrs);

			this.info("checkName.Severity = " + getXmlValue(doc, "Severity"));
			this.info("checkName.StatusCode = " + getXmlValue(doc, "StatusCode"));
			this.info("checkName.StatusDesc = " + getXmlValue(doc, "StatusDesc"));

			txAmlLog.setStatusCode(getXmlValue(doc, "StatusCode"));
			txAmlLog.setStatus(getXmlValue(doc, "Severity"));
			txAmlLog.setStatusDesc(getXmlValue(doc, "StatusDesc"));

			if ("0".equals(txAmlLog.getStatusCode()) && "INFO".equals(txAmlLog.getStatus())) {
				txAmlLog = parseResult(doc, txAmlLog);
			} else {
				// 需審查/確認
				txAmlLog.setConfirmStatus("1");
			}

		}

		try {
			txAmlLog = txAmlLogService.update(txAmlLog, titaVo);
		} catch (DBException e) {
			throw new LogicException("EC003", "update TxAmlLog:" + e.getErrorMsg()); // 修改資料
		}
		if ("1".equals(txAmlLog.getConfirmStatus())) {
			this.txBuffer.addAmlList(txAmlLog);
		}
		this.info("reCheckAml getAmlList().size=" + this.txBuffer.getAmlList().size());

		checkAmlVo = rspAmlVo(checkAmlVo, txAmlLog);

		return checkAmlVo;
	}

	private void CheckVo(CheckAmlVo checkAmlVo) throws LogicException {
		if (checkAmlVo.getEntdy() == 0) {
			throw new LogicException("EC004", "CheckAml.CheckVo : 參數 Entdy 不可為0");
		}

		if (checkAmlVo.getBrNo().isEmpty()) {
			throw new LogicException("EC004", "CheckAml.CheckVo : 參數 BrNo 不可為空白");
		}

//		if (checkAmlVo.getRefNo().isEmpty()) {
//			throw new LogicException("EC004", "CheckAml.CheckVo : 參數 RefNo 不可為空白");
//		}

		if (checkAmlVo.getUnit().isEmpty()) {
			throw new LogicException("EC004", "CheckAml.CheckVo : 參數 Unit 不可為空白");
		}

		if (checkAmlVo.getRoleId().isEmpty()) {
			throw new LogicException("EC004", "CheckAml.CheckVo : 參數 RoleId 不可為空白");
		}

		if (checkAmlVo.getTransactionId().isEmpty()) {
			throw new LogicException("EC004", "CheckAml.CheckVo : 參數 TransactionId 不可為空白");
		}

		if (checkAmlVo.getAcctNo().isEmpty()) {
			throw new LogicException("EC004", "CheckAml.CheckVo : 參數 AcctNo 不可為空白");
		}

		if (checkAmlVo.getCaseNo().isEmpty()) {
			throw new LogicException("EC004", "CheckAml.CheckVo : 參數 CaseNo 不可為空白");
		}

		if (checkAmlVo.getName().isEmpty()) {
			throw new LogicException("EC004", "CheckAml.CheckVo : 參數 Name 不可為空白");
		}

		if (checkAmlVo.getIdentityCd().isEmpty()) {
//			throw new LogicException("EC004", "CheckAml.CheckVo : 參數 IdentityCd 不可為空白");
			checkAmlVo.setIdentityCd("1");
		}

		if ("1".equals(checkAmlVo.getIdentityCd()) && checkAmlVo.getSex().isEmpty()) {
// 			throw new LogicException("EC004", "CheckAml.CheckVo : 身份別為自然人時,參數 Sex 不可為空白");
			checkAmlVo.setSex("M");
		} else if ("2".equals(checkAmlVo.getIdentityCd())) {
			checkAmlVo.setSex("");
		}
	}

	/**
	 * 更新指定序號檢核狀態
	 * 
	 * @param logno  AML檢查序號
	 * @param titaVo TitaVo
	 * @return CheckAmlVo
	 * @throws LogicException LogicException
	 */
	public CheckAmlVo refreshStatus(Long logno, TitaVo titaVo) throws LogicException {
		this.info("FunCode   = " + titaVo.getParam("FunCode"));
		String iFunCode = titaVo.getParam("FunCode");
		amlflag = this.txBuffer.getSystemParas().getAmlFg();
		this.info("CheckAml refreshStatus amlflag=" + amlflag);
		amlurl = this.txBuffer.getSystemParas().getAmlUrl();
		TxAmlLog txAmlLog = txAmlLogService.holdById(logno);
		if (txAmlLog == null) {
			throw new LogicException("EC001", "TxAmlLog.LogNo:" + logno);
		}

		CheckAmlVo checkAmlVo = new CheckAmlVo();
		if (amlflag == 2) {
//			txAmlLog.setConfirmStatus("0");
			txAmlLog.setStatusCode("0008");
			txAmlLog.setStatus("Success");
			txAmlLog.setStatusDesc("測試套不檢查");
		} else {
			String msgrs = connectAml(amlurl, txAmlLog.getMsgRg());
			if (amlflag != 0) {
			} else if (!this.connectSuccess) {
				// 需審查/確認
//			txAmlLog.setConfirmStatus("1");
//			throw new LogicException("EC009", "連結 "+amlurl+" 發生錯誤");
			} else {
				Document doc = convertStringToXml(msgrs);

				String Status = getXmlValue(doc, "Severity");
				String StatusCode = getXmlValue(doc, "StatusCode");
				String StatusDesc = getXmlValue(doc, "StatusDesc");

				this.info("refreshStatus.Severity = " + Status);
				this.info("refreshStatus.StatusCode = " + StatusCode);
				this.info("refreshStatus.StatusDesc = " + StatusDesc);

				txAmlLog.setStatusCode(StatusCode);
				txAmlLog.setStatus(Status);
				txAmlLog.setStatusDesc(StatusDesc);
				txAmlLog.setConfirmStatus(txAmlLog.getConfirmStatus());
				this.info("refreshStatus.ConfirmStatus =" + txAmlLog.getConfirmStatus());
				if ("0".equals(StatusCode) && "INFO".equals(Status)) {
					txAmlLog = parseResult(doc, txAmlLog);
				} else {
					// 需審查/確認
					txAmlLog.setConfirmStatus("1");
				}
			}
		}
		if (!iFunCode.equals("5")) {
			try {
				txAmlLogService.update(txAmlLog, titaVo);
			} catch (DBException e) {
				throw new LogicException("EC003", "update TxAmlLog:" + e.getErrorMsg()); // 更新資料
			}
		}
		checkAmlVo = rspAmlVo(checkAmlVo, txAmlLog);

		return checkAmlVo;
	}

	/**
	 * 人工確認檢核
	 * 
	 * @param logno  AML檢查序號
	 * @param titaVo TitaVo
	 * @return true/falas
	 * @throws LogicException LogicException
	 */
	public boolean isManualConFirm(Long logno, TitaVo titaVo) throws LogicException {
		amlflag = this.txBuffer.getSystemParas().getAmlFg();
		this.info("CheckAml isManualConFirm amlflag=" + amlflag);
		amlurl = this.txBuffer.getSystemParas().getAmlUrl();
		TxAmlLog txAmlLog = txAmlLogService.holdById(logno);
		if (txAmlLog == null) {
			throw new LogicException("EC001", "TxAmlLog.LogNo:" + logno);
		}
		if (amlflag > 0) {
			return true;
		} else {
			String msgrs = connectAml(amlurl, txAmlLog.getMsgRg());
			if (!this.connectSuccess) {
				return true;
			} else {
				Document doc = convertStringToXml(msgrs);

				String Status = getXmlValue(doc, "Severity");
				String StatusCode = getXmlValue(doc, "StatusCode");
				String StatusDesc = getXmlValue(doc, "StatusDesc");
				this.info("refreshStatus.Severity = " + Status);
				this.info("refreshStatus.StatusCode = " + StatusCode);
				this.info("refreshStatus.StatusDesc = " + StatusDesc);
				if ("0".equals(StatusCode) && "INFO".equals(Status)) {
					return false;
				} else {
					return true;
				}
			}
		}
	}

	private CheckAmlVo rspAmlVo(CheckAmlVo checkAmlVo, TxAmlLog txAmlLog) throws LogicException {
		checkAmlVo.setLogNo(txAmlLog.getLogNo());

		checkAmlVo.setStatus(txAmlLog.getStatus());
		checkAmlVo.setStatusCode(txAmlLog.getStatusCode());
		checkAmlVo.setStatusDesc(txAmlLog.getStatusDesc());
		checkAmlVo.setIsSimilar(txAmlLog.getIsSimilar());
		checkAmlVo.setIsSan(txAmlLog.getIsSan());
		checkAmlVo.setIsBanNation(txAmlLog.getIsBanNation());

		checkAmlVo.setConfirmStatus(txAmlLog.getConfirmStatus());
		checkAmlVo.setConfirmCode(txAmlLog.getConfirmCode());
		checkAmlVo.setConfirmEmpNo(txAmlLog.getConfirmEmpNo());
		checkAmlVo.setConfirmTranCode(txAmlLog.getConfirmTranCode());

		return checkAmlVo;
	}

	/**
	 * 回覆是否可承做
	 * 
	 * @param logno     AML檢查序號
	 * @param autoError 不可承作時,回覆錯誤
	 * @return true.可承作 / false.不可承做
	 * @throws LogicException LogicException
	 */
	public boolean canDo(Long logno, boolean autoError) throws LogicException {
		TxAmlLog txAmlLog = txAmlLogService.findById(logno);
		if (txAmlLog == null) {
			throw new LogicException("EC001", "TxAmlLog.LogNo:" + logno);
		}

		if ("0".equals(txAmlLog.getConfirmStatus())) {
			return true;
		} else {
			if (autoError) {
				if ("1".equals(txAmlLog.getConfirmStatus())) {
					throw new LogicException("EC008", "AML檢查需審查或確認");
				} else if ("2".equals(txAmlLog.getConfirmStatus())) {
					throw new LogicException("EC008", "AML檢查為凍結名單或未確定名單");
				}
			} else {
				return false;
			}

		}

		return false;
	}

	private CheckAmlVo initRs(CheckAmlVo checkAmlVo) {
		checkAmlVo.setLogNo(0L);
		checkAmlVo.setStatus("");
		checkAmlVo.setStatusCode("");
		checkAmlVo.setStatusDesc("");
		checkAmlVo.setIsSan("");
		checkAmlVo.setIsSimilar("");
		checkAmlVo.setIsBanNation("");

		return checkAmlVo;
	}

	@SuppressWarnings("unchecked")
	private TxAmlLog parseResult(Document doc, TxAmlLog txAmlLog) throws LogicException {
//		Document doc = convertStringToXml(msgrs);		

		if ("0".equals(txAmlLog.getStatusCode()) && "INFO".equals(txAmlLog.getStatus())) {
			String ResultString = getXmlValue(doc, "AML_SCAN_QUERY_FirstResult");

			try {
				Map<String, Object> ResultMap = new ObjectMapper().readValue(ResultString, HashMap.class);

				txAmlLog.setStatus(ResultMap.get("Status").toString());
				txAmlLog.setStatusCode(ResultMap.get("Status_Code").toString());
				txAmlLog.setStatusDesc(ResultMap.get("Status_Desc").toString());

				txAmlLog.setIsSimilar(ResultMap.get("Is_Similar").toString());
				txAmlLog.setIsSan(ResultMap.get("Is_San").toString());
				txAmlLog.setIsBanNation(ResultMap.get("Is_Ban_Nation").toString());

				// 疑似名單需確認
				if ("Warning".equals(txAmlLog.getStatus()) || "Fail".equals(txAmlLog.getStatus())) {
					txAmlLog.setConfirmStatus("1");
				} else if ("Success".equals(txAmlLog.getStatus())) {
					if ("0008".equals(txAmlLog.getStatusCode()) || "0009".equals(txAmlLog.getStatusCode())) {
						txAmlLog.setConfirmStatus("0");
					} else if ("0010".equals(txAmlLog.getStatusCode())) {
						txAmlLog.setConfirmStatus("2");
					}

				}
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				throw new LogicException("EC009", "CheckAml.parseResult error:" + ResultString);
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				throw new LogicException("EC009", "CheckAml.parseResult error:" + ResultString);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new LogicException("EC009", "CheckAml.parseResult error:" + ResultString);
			}

		} else {
			txAmlLog.setConfirmStatus("1");
		}

		return txAmlLog;
	}

	private String makeXml(CheckAmlVo checkAmlVo) {
		String rs = "";

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = factory.newDocumentBuilder();

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document doc = db.newDocument();
		doc.setXmlStandalone(true);

		Element SoapEnvEnvelope = doc.createElement("SOAP-ENV:Envelope");

		SoapEnvEnvelope.setAttribute("xmlns:SOAP-ENV", "http://schemas.xmlsoap.org/soap/envelope/");
		SoapEnvEnvelope.setAttribute("xmlns:skl", "http://www.skl.com.tw/si");
		SoapEnvEnvelope.setAttribute("xmlns:svc", "http://www.skl.com.tw/si/svc");
		SoapEnvEnvelope.setAttribute("xmlns:vlife", "http://www.skl.com.tw/si/vlife");

		Element SoapEnvBody = doc.createElement("SOAP-ENV:Body");
		Element sklMsgRq = doc.createElement("skl:MsgRq");

		// header begin
		Element Header = doc.createElement("Header");

		Header = appendChildElement(doc, Header, "SrcSystemID", checkAmlVo.getSrcSystemID());

		Header = appendChildElement(doc, Header, "TxnID", checkAmlVo.getTxnID());

		sklMsgRq.appendChild(Header);
		// header end

		// content begin
		Element SvcRq = doc.createElement("SvcRq");

		SvcRq = appendChildElement(doc, SvcRq, "Unit", checkAmlVo.getUnit());
		SvcRq = appendChildElement(doc, SvcRq, "Acceptance_Unit", checkAmlVo.getAcceptanceUnit());
		SvcRq = appendChildElement(doc, SvcRq, "Role_Id", checkAmlVo.getRoleId());
		SvcRq = appendChildElement(doc, SvcRq, "Transaction_Id", checkAmlVo.getTransactionId());
		SvcRq = appendChildElement(doc, SvcRq, "Acct_No", checkAmlVo.getAcctNo());
		SvcRq = appendChildElement(doc, SvcRq, "Case_No", checkAmlVo.getCaseNo());
		SvcRq = appendChildElement(doc, SvcRq, "Acct_Id", checkAmlVo.getAcctId());
		SvcRq = appendChildElement(doc, SvcRq, "Insur_Count", String.valueOf(checkAmlVo.getInsurCount()));
		SvcRq = appendChildElement(doc, SvcRq, "Name", checkAmlVo.getName());
		SvcRq = appendChildElement(doc, SvcRq, "English_Name", checkAmlVo.getEnglishName());
		SvcRq = appendChildElement(doc, SvcRq, "Cust_Key", checkAmlVo.getCustKey());
		SvcRq = appendChildElement(doc, SvcRq, "Identity_Cd", checkAmlVo.getIdentityCd());
		SvcRq = appendChildElement(doc, SvcRq, "National_Cd", checkAmlVo.getNationalCd());
		SvcRq = appendChildElement(doc, SvcRq, "Birth_Nation_Cd", checkAmlVo.getBirthNationCd());
		SvcRq = appendChildElement(doc, SvcRq, "SEX", checkAmlVo.getSex());
		SvcRq = appendChildElement(doc, SvcRq, "Birth_Est_Dt", checkAmlVo.getBirthEstDt());
		SvcRq = appendChildElement(doc, SvcRq, "Notify_Email", checkAmlVo.getNotifyEmail());
		SvcRq = appendChildElement(doc, SvcRq, "Query_Id", checkAmlVo.getQueryId());
		SvcRq = appendChildElement(doc, SvcRq, "Source_Id", checkAmlVo.getSourceId());
		SvcRq = appendChildElement(doc, SvcRq, "Modify_Date",
				String.format("%08d%06d", dateUtil.getNowIntegerRoc(), dateUtil.getNowIntegerForBC()));
		SvcRq = appendChildElement(doc, SvcRq, "InsrNHdr_Same", checkAmlVo.getInsrNHdrSame());
		SvcRq = appendChildElement(doc, SvcRq, "Role_Status", checkAmlVo.getRoleStatus());

		sklMsgRq.appendChild(SvcRq);
		// content end

		SoapEnvBody.appendChild(sklMsgRq);
		SoapEnvEnvelope.appendChild(SoapEnvBody);
		doc.appendChild(SoapEnvEnvelope);

		// 輚成String
		StringWriter sw = new StringWriter();

		TransformerFactory tf = TransformerFactory.newInstance();
		try {
			Transformer transformer = tf.newTransformer();
			try {
				transformer.transform(new DOMSource(doc), new StreamResult(sw));
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			rs = sw.toString();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.info("XML = " + rs);
		return rs;
	}

	private Element appendChildElement(Document doc, Element parentElement, String childTagName, String childContent) {
		Element childElement = doc.createElement(childTagName);
//		if (childContent != null && !"".equals(childContent)) childElement.setTextContent(childContent);
		childElement.setTextContent(childContent);
		parentElement.appendChild(childElement);

		return parentElement;
	}

	private String connectAml(String urlString, String parm) throws LogicException {
		this.info("connectAml = " + urlString + " / " + parm);

		this.connectSuccess = true;

		if (amlflag != 0) {
			return "";
		}

		try {
			URL url = new URL(urlString);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setReadTimeout(timeout); // set timeout to 1 seconds
			con.setConnectTimeout(timeout); // set timeout to 1 seconds

			con.setRequestMethod("POST");
			con.setRequestProperty("Content-type", "text/xml;charset=utf-8");
			// set input
			con.setDoInput(true);
			// set output
			con.setDoOutput(true);

			OutputStream os = con.getOutputStream();
			os.write(parm.getBytes());

			int rc = con.getResponseCode();
			this.info("postUrl2.responseCode = " + rc);

			if (rc == 200) {
				InputStream is = con.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while (null != (line = br.readLine())) {
					sb.append(line);
				}
				this.info("connectAml.response = " + sb.toString());

//				getXml(sb.toString());

				is.close();
				isr.close();
				br.close();

				return sb.toString();
			} else {
				this.connectSuccess = false;
				return "Response Code = " + rc;
			}
		} catch (MalformedURLException e) {
//			e.printStackTrace();
//			throw new LogicException(titaVo, "EC009", "CheckAml.connectAml.MalformedURLException : " + e.getMessage());
			this.connectSuccess = false;
			return "Exception = " + e.getMessage();
		} catch (IOException e) {
//			e.printStackTrace();
//			throw new LogicException(titaVo, "EC009", "CheckAml.connectAml.IOException : " + e.getMessage());
			this.connectSuccess = false;
			return "Exception = " + e.getMessage();
		}

	}

//	private static void getXml(String xmlstring) {
//		Document doc = convertStringToXml(xmlstring);
//		
//		this.info("XML StatusCode = " + getXmlValue(doc,"StatusCode"));
//		this.info("XML Severity = " + getXmlValue(doc,"Severity"));
//		this.info("XML StatusDesc = " + getXmlValue(doc,"StatusDesc"));
//
//	}

	/**
	 * 將 string 轉換成 xml
	 * 
	 * @param xmlstring 轉換的XML String
	 * @return xml Document
	 */
	public Document convertStringToXml(String xmlstring) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			factory.setNamespaceAware(true);
			factory.setIgnoringComments(true);
			factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
			factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
			factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
			factory.setXIncludeAware(false);
			factory.setExpandEntityReferences(false);
		} catch (ParserConfigurationException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.warn("Set DocumentBuilderFactory Env Error");
			this.warn(errors.toString());
		}
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();

			Document doc = builder.parse(new InputSource(new StringReader(xmlstring)));
			return doc;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 取 XML 指定TAG值
	 * 
	 * @param doc     XML 的 Document
	 * @param tagName TAG名稱
	 * @return 指定TAG值
	 */
	public String getXmlValue(Document doc, String tagName) {
		NodeList nl = doc.getElementsByTagName(tagName);
		if (nl.getLength() > 0) {
//			this.info("tagname size = " + nl.getLength());
			Node node = nl.item(0);

			return node.getTextContent();
		} else {
			return "";
		}
	}

//	@Override
//	public void exec() throws LogicException {
//		// TODO Auto-generated method stub

//	}
}
