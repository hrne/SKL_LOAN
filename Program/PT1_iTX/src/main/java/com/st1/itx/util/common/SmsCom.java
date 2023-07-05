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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.service.SystemParasService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.data.SmsDataVo;
import com.st1.itx.util.common.data.SmsVo;

@Component("smsCom")
@Scope("prototype")
public class SmsCom extends TradeBuffer {

	// web api url
	private String apiUrl = "";

	// set timeout to 30 seconds
	private int timeout = 30000;

	// connect AML status
	private boolean connectSuccess;

	@Autowired
	private SystemParasService sSystemParasService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("SmsCom run ... ");
		return null;
	}

	public SmsVo sendSms(TitaVo titaVo, String mobile, String msg) throws LogicException {

		SystemParas tSystemParas = sSystemParasService.findById("LN", titaVo);

		if (tSystemParas == null) {
			throw new LogicException("E0001", "SmsCom,SystemParas");
		}
		apiUrl = tSystemParas.getAmlUrl();
		this.info("SmsCom.sendSms:apiUrl=" + apiUrl);

		SmsVo smsVo = new SmsVo();
		SmsDataVo smsDataVo = new SmsDataVo();
		smsDataVo.setMobile(mobile);
		smsDataVo.setMsgContent(msg);
		try {
			smsVo.setSmsData(smsDataVo);
			String jsonString = smsDataVo.getJsonString();
			String decryptJsonString = smsDataVo.getDecryptJsonString(jsonString);
			this.info("jsonString = " + jsonString);
			this.info("decryptJsonString = " + decryptJsonString);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("smsVo.setSmsData error = " + errors.toString());
			return null;
		}
		checkVo(smsVo);

		String msgrq = makeXml(smsVo);

		String msgrs = "";

		msgrs = connect(apiUrl, msgrq);

		// String
		// test end

		smsVo.setMsgRs(msgrs);

		// insert TxAmlLog

		if (!this.connectSuccess) {
			smsVo.setSuccess(false);
		} else {
			smsVo = parseXml(smsVo);
		}

		return smsVo;
	}

	private SmsVo parseXml(SmsVo iVo) {

		String msgRs = iVo.getMsgRs();

		if (msgRs == null || "".equals(msgRs)) {
			iVo.setSuccess(false);
			return iVo;
		}

		Document doc = convertStringToXml(msgRs);

		String success = getXmlValue(doc, "Success");
		String message = getXmlValue(doc, "Message");

		this.info("SmsCom.parseXml success=" + success);
		this.info("SmsCom.parseXml message=" + message);

		iVo.setSuccess(success);
		iVo.setMessage(message);

		return iVo;
	}

	private void checkVo(SmsVo checkVo) throws LogicException {
		if (checkVo.getSmsData().isEmpty()) {
			throw new LogicException("EC004", "SmsCom.checkVo : 參數 SmsData 不可為空白");
		}
	}

	private String makeXml(SmsVo checkVo) {
		String rs = "";

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Document doc = db.newDocument();
		doc.setXmlStandalone(true);

		Element soapEnvEnvelope = doc.createElement("SOAP-ENV:Envelope");

		soapEnvEnvelope.setAttribute("xmlns:SOAP-ENV", "http://schemas.xmlsoap.org/soap/envelope/");
		soapEnvEnvelope.setAttribute("xmlns:skl", "http://www.skl.com.tw/si");
		soapEnvEnvelope.setAttribute("xmlns:svc", "http://www.skl.com.tw/si/svc");
		soapEnvEnvelope.setAttribute("xmlns:vlife", "http://www.skl.com.tw/si/vlife");

		Element soapEnvBody = doc.createElement("SOAP-ENV:Body");
		Element sklMsgRq = doc.createElement("skl:MsgRq");

		// header begin
		Element header = doc.createElement("Header");

		header = appendChildElement(doc, header, "SrcSystemID", checkVo.getSrcSystemID());
		header = appendChildElement(doc, header, "TxnID", checkVo.getTxnID());

		// 現在時間
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/M/d a hh:mm:ss", Locale.TAIWAN);
		Date date = new Date();
		String nowTime = sdFormat.format(date);

		header = appendChildElement(doc, header, "ClientDt", nowTime);
		header = appendChildElement(doc, header, "ClientDtSpecified", "false");
		header = appendChildElement(doc, header, "MsgId", "");
		header = appendChildElement(doc, header, "Status", "");

		sklMsgRq.appendChild(header);
		// header end

		// content begin
		Element svcRq = doc.createElement("SvcRq");

		svcRq = appendChildElement(doc, svcRq, "SMSData", checkVo.getSmsData());

		sklMsgRq.appendChild(svcRq);
		// content end

		soapEnvBody.appendChild(sklMsgRq);
		soapEnvEnvelope.appendChild(soapEnvBody);
		doc.appendChild(soapEnvEnvelope);

		// 輚成String
		StringWriter sw = new StringWriter();

		TransformerFactory tf = TransformerFactory.newInstance();
		try {
			Transformer transformer = tf.newTransformer();
			try {
				transformer.transform(new DOMSource(doc), new StreamResult(sw));
			} catch (TransformerException e) {
				e.printStackTrace();
			}
			rs = sw.toString();
		} catch (TransformerConfigurationException e) {
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

	private String connect(String urlString, String parm) throws LogicException {
		this.info("connect = " + urlString + " / " + parm);

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
				this.info("connect.response = " + sb.toString());

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
}
