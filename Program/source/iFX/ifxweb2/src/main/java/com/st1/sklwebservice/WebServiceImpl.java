package com.st1.sklwebservice;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.filter.SafeClose;

@Component
@Scope("prototype")
public class WebServiceImpl {
	private static final Logger logger = LoggerFactory.getLogger(WebServiceImpl.class);

	public static final String SERVICE_NAME = "wsSKL_Authentication";

	public static final String SOAP_ENDPOINT = "SOAP_ENDPOINT";

	public static final String SOAP_ACTION = "SOAP_ACTION";

	public static final String SOAP_NAMESPACE = "SOAP_NAMESPACE";

	public static final String SOAP_SUB_NAMESPACE = "SOAP_SUB_NAMESPACE";

	public static final String SOAP_PRENAMESPACE = "SOAP_PRENAMESPACE";

	public static final String SOAP_TIMEOUT = "SOAP_TIMEOUT";

	public static final String SOAP_TAG_REQUEST = "SOAP_TAG_REQUEST";

	public static final String SOAP_TAG_REQUEST_BODY = "SOAP_TAG_REQUEST_BODY";

	private Map<String, Properties> services;

	private Properties props;

	String serviceName;

	public WebServiceImpl() {
		services = new HashMap<String, Properties>();
		props = new Properties();
		props.setProperty(SOAP_ENDPOINT, "");
		// props.setProperty(SOAP_ACTION, "");
		props.setProperty(SOAP_NAMESPACE, "SOAP_NAMESPACE");
		props.setProperty(SOAP_SUB_NAMESPACE, "");
		props.setProperty(SOAP_PRENAMESPACE, "");
		props.setProperty(SOAP_TIMEOUT, "60");
		props.setProperty(SOAP_TAG_REQUEST, "SOAP_TAG_REQUEST");
		props.setProperty(SOAP_TAG_REQUEST_BODY, "SOAP_TAG_REQUEST_BODY");
		serviceName = "SOAP_SERVICE";
		services.put(serviceName, props);
	}

	@SuppressWarnings("rawtypes")
	public List<?> sendAndReceive(String serviceName, Map<String, Object> params) {
		Properties props = services.get(serviceName);
		String response = sendAndWaitResult(props, params);
		List list = null;
		if (response == null || response.trim().length() == 0) {
			list = new ArrayList();
		} else {
			list = resolveSoapXml(response);
		}
		return list;
	}

	public String sendAndReceiveString(String serviceName, String str) {
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List resolveSoapXml(String xml) {
		List list = new ArrayList();
		try {
			OMElement root = AXIOMUtil.stringToOM(xml);
			for (Iterator<OMElement> i = root.getChildElements(); i.hasNext();) {
				OMElement element = i.next();
				Iterator<OMElement> iter = element.getChildElements();
				if (iter.hasNext()) {
					Map result = new LinkedHashMap();
					for (; iter.hasNext();) {
						OMElement ele = iter.next();
						parseChild(result, ele);
					}
					list.add(result);
				}
			}
		} catch (Throwable t) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			logger.warn(sw.toString());
			logger.warn(pw.toString());
			logger.warn(t.getMessage());
		}
		return list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void parseChild(Map result, OMElement element) {
		String key = element.getLocalName();
		Object obj = result.get(key);
		Iterator<OMElement> i = element.getChildElements();
		if (i.hasNext()) {
			Map map = new LinkedHashMap();
			for (; i.hasNext();) {
				OMElement child = i.next();
				parseChild(map, child);
			}
			if (obj == null) {
				result.put(key, map);
			} else if (obj instanceof List) {
				List<Map<String, Object>> list = ((List<Map<String, Object>>) obj);
				list.add(map);
				result.put(key, list);
			} else if (obj instanceof Map) {
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				list.add((Map<String, Object>) obj);
				list.add(map);
				result.put(key, list);
			}
		} else {
			String value = element.getText();
			if (obj == null) {
				result.put(key, value);
			} else if (obj instanceof List) {
				List<String> list = ((List<String>) obj);
				list.add(value);
				result.put(key, list);
			} else if (obj instanceof String) {
				List<String> list = new ArrayList<String>();
				list.add((String) obj);
				list.add(value);
				result.put(key, list);
			}
		}
	}

	private String sendAndWaitResult(Properties props, Map<String, Object> params) {
		String targetEpr = props.getProperty(SOAP_ENDPOINT);
		String soapAction = props.getProperty(SOAP_ACTION);
		String namespace = props.getProperty(SOAP_NAMESPACE);
		String subNamespace = props.getProperty(SOAP_SUB_NAMESPACE);
		String preNamespace = props.getProperty(SOAP_PRENAMESPACE, "");
//		long timeout = Long.parseLong(props.getProperty(SOAP_TIMEOUT, "60000"));
		long timeout = Long.parseLong(props.getProperty(SOAP_TIMEOUT));
		String soapRequest = props.getProperty(SOAP_TAG_REQUEST);
		String soapRequestBody = props.getProperty(SOAP_TAG_REQUEST_BODY);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(soapRequestBody, params);
		OMElement result = null;
		ServiceClient sender = null;

		try {
			sender = new ServiceClient();
			Options opts = buildOptions(targetEpr, soapAction, timeout);
			sender.setOptions(opts);
			OMElement reqParams = buildParam(namespace, subNamespace, preNamespace, soapRequest, map);

			/* Xml Log */
//			this.writeToAmlLog(reqParams.toString(), params);
			/* Xml Log End */

			result = sender.sendReceive(reqParams);

			return result.toString();
		} catch (Throwable t) {
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			logger.warn(errors.toString());
			throw new RuntimeException("Failed to send and receive, reason => " + t.getLocalizedMessage(), t);
		} finally {
			try {
				sender.cleanupTransport();
			} catch (AxisFault ignored) {
				logger.info("AML cleanupTransport");
			}
		}
	}

	private Options buildOptions(String targetEpr, String soapAction, long timeout) {
		Options options = new Options();
		options.setTimeOutInMilliSeconds(timeout);
		options.setSoapVersionURI(SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI);
		options.setAction(soapAction);
		options.setTo(new EndpointReference(targetEpr));
		options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
		options.setExceptionToBeThrownOnSOAPFault(false);
		return options;
	}

	private OMElement buildParam(String namespace, String subNamespace, String preNamespace, String request, Map<String, Object> params) {
		OMFactory factory = OMAbstractFactory.getOMFactory();
		OMNamespace omNamespace = factory.createOMNamespace(namespace, preNamespace);
		OMElement data = factory.createOMElement(request, omNamespace);
		if (params != null) {
			if (subNamespace != null) {
				omNamespace = OMAbstractFactory.getOMFactory().createOMNamespace(subNamespace, preNamespace);
			}
			addChildElement(factory, omNamespace, data, params);
		}
		return data;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private OMElement addChildElement(OMFactory factory, OMNamespace omNamespace, OMElement data, Map<String, Object> params) {
		for (Iterator<?> i = params.keySet().iterator(); i.hasNext();) {
			String name = (String) i.next();
			OMElement param = factory.createOMElement(name, omNamespace);
			Object obj = params.get(name);
			if (obj instanceof String) {
				String value = (String) params.get(name);
				param.setText(value);
				data.addChild(param);
			} else if (obj instanceof Map) {
				addChildElement(factory, omNamespace, param, (Map) obj);
				data.addChild(param);
			} else if (obj instanceof List) {
				List list = (List) obj;
				for (Iterator<Map<String, Object>> j = list.iterator(); j.hasNext();) {
					Map<String, Object> map = j.next();
					OMElement seq = factory.createOMElement(name, omNamespace);
					addChildElement(factory, omNamespace, seq, map);
					data.addChild(seq);
				}
			}
		}
		return null;
	}

	public String getNowwithFormat(String fmt) {
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		Calendar c1 = Calendar.getInstance(); // today
		return sdf.format(c1.getTime());
	}

	public void writeToAmlLog(String reqParams, Map<String, Object> params) {
		FileOutputStream fout = null;
		OutputStreamWriter osw = null;
		BufferedWriter fw = null;

		StringReader sr = null;
		StringWriter stringWriter = null;
		try {
			sr = new StringReader(reqParams);
			Source xmlInput = new StreamSource(sr);
			stringWriter = new StringWriter();
			StreamResult xmlOutput = new StreamResult(stringWriter);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute("indent-number", 2);
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(xmlInput, xmlOutput);

			fout = new FileOutputStream(FilterUtils.filter("/" + getNowwithFormat("yyyy-MM-dd_HHmmss") + "_" + params.get("unique_key") + ".xml"), true);
			osw = new OutputStreamWriter(fout, "UTF-8");
			fw = new BufferedWriter(osw);

			fw.write(xmlOutput.getWriter().toString());
			fw.flush();
		} catch (Throwable t) {
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			logger.warn(errors.toString());
		} finally {
			SafeClose.close(fw);
			SafeClose.close(osw);
			SafeClose.close(fout);

			SafeClose.close(stringWriter);
			SafeClose.close(sr);
		}
	}

	public String sendAndReceiveXmlString(Map<String, Object> params) {
		Properties props = services.get(this.serviceName);
		String response = sendAndWaitResult(props, params);
		return response;
	}
}
