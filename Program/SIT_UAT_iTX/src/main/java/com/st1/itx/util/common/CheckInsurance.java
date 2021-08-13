package com.st1.itx.util.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.common.data.CheckInsuranceVo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.st1.itx.db.service.TxAmlLogService;

import com.st1.itx.db.service.CustMainService;

@Component("checkInsurance")
@Scope("prototype")
public class CheckInsurance extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public TxAmlLogService txAmlLogService;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	DateUtil dateUtil;

	// Web api check control flag 0.正常 1.aml系統異常改由人工確認 2.測試套不檢查,並預設非可疑名單
	private int apiFlag = 0;
	// web api url
	private String apiUrl = "";

	// set timeout to 30 seconds
	private int timeout = 30000;

	// connect AML status
	private boolean connectSuccess;

	/**
	 * AML姓名檢核
	 * 
	 * @param titaVo 檢查內容
	 * @return CheckInsuranceVo ...
	 * @throws LogicException LogicException
	 */

	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("CheckInsurance run ... ");

		return null;

	}

	public CheckInsuranceVo checkInsurance(TitaVo titaVo, CheckInsuranceVo checkVo) throws LogicException {

		apiFlag = this.txBuffer.getSystemParas().getAmlFg();
		apiUrl = this.txBuffer.getSystemParas().getAmlUrl();
		this.info("CheckInsurance.checkInsurance:apiFlag=" + apiFlag + ",apiUrl=" + apiUrl);
		CheckVo(checkVo);

		String msgrq = makeXml(checkVo);

		String msgrs = "";
		if (apiFlag == 0) {
			this.info("CheckInsurance.checkInsurance apiFlag0=" + apiFlag);
			msgrs = connectAml(apiUrl, msgrq);
		} else if (apiFlag == 1) {
			this.info("CheckInsurance.checkInsurance apiFlag1=" + apiFlag);
			this.connectSuccess = false;
			msgrs = "";
		} else {
			this.info("CheckInsurance.checkInsurance apiFlag2=" + apiFlag);
			msgrs = "<?xml version=\"1.0\"?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:skl=\"http://www.skl.com.tw/si\" xmlns:svc=\"http://www.skl.com.tw/si/svc\" xmlns:vlife=\"http://www.skl.com.tw/si/vlife\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><SOAP-ENV:Body><skl:MsgRs><Header><SrcSystemID>BPM</SrcSystemID><TxnID>ESB00060</TxnID><Status><SystemID>COR</SystemID><StatusCode>0</StatusCode><Severity>INFO</Severity><StatusDesc /></Status></Header><SvcRs xsi:type=\"vlife:ESB00060SvcRsType\"><Detail><application_date>2012-11-26+08:00</application_date><available>Y</available><collect_po_type>3</collect_po_type><currency_1>USD</currency_1><exchage_val>30.8</exchage_val><face_amt>5000</face_amt><highest_loan>4300</highest_loan><insurance_type_3>R</insurance_type_3><issue_date>2012-11-19+08:00</issue_date><loan_amt>0</loan_amt><medical_ind_bpm /><mode_prem_year>0</mode_prem_year><names_i>張三1(test)</names_i><names_o>李四2</names_o><po_status_code>M</po_status_code><policy_no>1030059691</policy_no><rvl_values>21858</rvl_values></Detail><Detail><application_date>2020-02-25+08:00</application_date><available>Y</available><collect_po_type>3</collect_po_type><currency_1>NTD</currency_1><exchage_val>1</exchage_val><face_amt>1000</face_amt><highest_loan>0</highest_loan><insurance_type_3>C</insurance_type_3><issue_date>2020-02-12+08:00</issue_date><loan_amt>0</loan_amt><medical_ind_bpm /><mode_prem_year>28977</mode_prem_year><names_i>張三2</names_i><names_o>李四2</names_o><po_status_code>P</po_status_code><policy_no>1096709803</policy_no><rvl_values>0</rvl_values></Detail><Detail><application_date>2020-02-25+08:00</application_date><available>Y</available><collect_po_type>3</collect_po_type><currency_1>NTD</currency_1><exchage_val>1</exchage_val><face_amt>1000</face_amt><highest_loan>0</highest_loan><insurance_type_3>C</insurance_type_3><issue_date>2020-02-12+08:00</issue_date><loan_amt>0</loan_amt><medical_ind_bpm /><mode_prem_year>4060</mode_prem_year><names_i>張三3</names_i><names_o>李四3</names_o><po_status_code>P</po_status_code><policy_no>1096709812</policy_no><rvl_values>0</rvl_values></Detail></SvcRs></skl:MsgRs></SOAP-ENV:Body></SOAP-ENV:Envelope>";
			this.connectSuccess = true;
		}
		// String
		// test end

		checkVo.setMsgRs(msgrs);

		// insert TxAmlLog

		if (!this.connectSuccess) {
			checkVo.setSuccess(false);
		} else {
//			Document doc = convertStringToXml(msgrs);

			checkVo = parseXml(checkVo);

		}

		return checkVo;
	}

	public CheckInsuranceVo parseXml(CheckInsuranceVo iVo) {

		String msgRs = iVo.getMsgRs();

		if (msgRs == null || "".equals(msgRs)) {
			iVo.setSuccess(false);
			return iVo;
		}

		Document doc = convertStringToXml(msgRs);

		String statusCode = getXmlValue(doc, "StatusCode");

		if ("0".equals(statusCode)) {
			iVo.setSuccess(true);
//			public List<HashMap<String, String>> getNodeList(Document doc, String tagName) {
			List<HashMap<String, String>> lDetail = getNodeList(doc, "Detail");
			for (int i = 0; i < lDetail.size() - 1; i++) {

				// if (highest_loan == null) -> 0 else -> [loan_amt] + [highest_loan]
				HashMap<String, String> map = lDetail.get(i);
				int loanBal = 0;
				if (map.get("highest_loan") != null) {
					int highestLoan = Integer.valueOf(map.get("highest_loan"));
					int loanAmt = 0;
					if (map.get("loan_amt") != null) {
						loanAmt = Integer.valueOf(map.get("loan_amt"));
					}
					map.put("loan_bal", String.valueOf(highestLoan + loanAmt));
				}
				// 轉換日期
				SimpleDateFormat dformatter1 = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat dformatter2 = new SimpleDateFormat("yyyyMMdd");
				String issueDate = map.get("issue_date");
				String applicationDate = map.get("application_date");

				try {
					if (!"".equals(issueDate) || issueDate != null) {
						Date d = dformatter1.parse(issueDate);
						issueDate = dformatter2.format(d);
						int d7 = Integer.valueOf(issueDate) - 19110000;
						issueDate = String.valueOf(d7);
					} else {
						issueDate = "0";
					}
					map.put("issue_date", issueDate);

					if (!"".equals(applicationDate) || applicationDate != null) {
						Date d = dformatter1.parse(applicationDate);
						applicationDate = dformatter2.format(d);
						int d7 = Integer.valueOf(applicationDate) - 19110000;
						applicationDate = String.valueOf(d7);
					} else {
						applicationDate = "0";
					}
					map.put("application_date", applicationDate);

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			iVo.setDetail(lDetail);
		} else {
			iVo.setSuccess(false);
		}
		return iVo;
	}

	private void CheckVo(CheckInsuranceVo checkVo) throws LogicException {
		if (checkVo.getCustId().isEmpty()) {
			throw new LogicException("EC004", "CheckInsurance.CheckInsuranceVo : 參數 CustId 不可為空白");
		}

	}

	private String makeXml(CheckInsuranceVo checkVo) {
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

		Header = appendChildElement(doc, Header, "SrcSystemID", checkVo.getSrcSystemID());
		Header = appendChildElement(doc, Header, "TxnID", checkVo.getTxnID());

		// 現在時間
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/M/d a hh:mm:ss", Locale.TAIWAN);
		Date date = new Date();
		String nowTime = sdFormat.format(date);

		Header = appendChildElement(doc, Header, "ClientDt", nowTime);
		Header = appendChildElement(doc, Header, "ClientDtSpecified", "false");
		Header = appendChildElement(doc, Header, "MsgId", "");
		Header = appendChildElement(doc, Header, "Status", "");

		sklMsgRq.appendChild(Header);
		// header end

		// content begin
		Element SvcRq = doc.createElement("SvcRq");

		SvcRq = appendChildElement(doc, SvcRq, "g_client_id", checkVo.getCustId());

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
			this.connectSuccess = false;
			return "Exception = " + e.getMessage();
		} catch (IOException e) {
			this.connectSuccess = false;
			return "Exception = " + e.getMessage();
		}

	}

	/**
	 * 將 string 轉換成 xml
	 * 
	 * @param xmlstring 轉換的XML String
	 * @return xml Document
	 */
	public Document convertStringToXml(String xmlstring) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

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

	public List<HashMap<String, String>> getNodeList(Document doc, String tagName) {
		List<HashMap<String, String>> listMap = new ArrayList<HashMap<String, String>>();

		NodeList nl = doc.getElementsByTagName(tagName);

		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);

//			System.out.println("***" + node.getTextContent());

			NodeList nodeList = node.getChildNodes();
			HashMap<String, String> map = new HashMap<String, String>();
			for (int j = 0; j < nodeList.getLength(); j++) {

				Node cNode = nodeList.item(j);

				map.put(cNode.getNodeName(), cNode.getTextContent());
//				System.out.println(cNode.getNodeName() + "=" + cNode.getTextContent());
			}

			listMap.add(map);
		}

		return listMap;
	}

//	@Override
//	public void exec() throws LogicException {
//		// TODO Auto-generated method stub

//	}
}
