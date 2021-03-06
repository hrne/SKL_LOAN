package com.st1.ifx.file.fdp.item;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.filter.SafeClose;

@Component
@Scope("prototype")
public class XmlConverter {
	private static final Logger logger = LoggerFactory.getLogger(XmlConverter.class);

	@Value("${import.xmlbox}")
	private String outputxmlFolder;

	@Value("${import.backupFolder}")
	private String backupFolder;

	@Value("${import.convertXmlToExt}")
	private String extName = ".mlx";

	@ServiceActivator
	public void convert(Message msg) throws Throwable {
		logger.info("converting msg:{}", msg);
		File file = (File) msg.getPayload();
		logger.info(FilterUtils.escape("convert " + file.getAbsolutePath() + " to " + outputxmlFolder));

		convertToxml(file.getAbsolutePath());
		moveFileToBackupFolder(file.getAbsolutePath());
	}

	private void convertToxml(String filePath) throws Exception {
		logger.info("convertToxml filePath:" + filePath);
		String name = FilenameUtils.getBaseName(filePath);

		BufferedReader br = null;
		String xmlfiledata = "";
		try {

			String sCurrentLine = "";

			StringBuffer sb = new StringBuffer();
			br = new BufferedReader(new FileReader(filePath));
			logger.info("temp after BufferedReader");
			while ((sCurrentLine = br.readLine()) != null) {
				// logger.info("StringBuffer:" + sCurrentLine);
				sb.append(sCurrentLine);
			}
			xmlfiledata = sb.toString();

		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} finally {
			try {
				if (br != null) {
					br.close();
					logger.info("temp br.close");
				}
			} catch (IOException ex) {
				StringWriter errors = new StringWriter();
				ex.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
			}
		}
		// BufferedWriter fw = null;
		// File tmpFile = new File(tmpFilePath);
		// ???????????????
		System.out.println("XmlConverter START");

		addContent(xmlfiledata, name);
		logger.info("XmlConverter done ");
		// FileUtils.moveFile(tmpFile, new File(targetFilePath));

	}

	private void moveFileToBackupFolder(String filePath) {
		// errorFolder
		String fileName = FilenameUtils.getName(filePath);
		String newFilePath = FilenameUtils.concat(backupFolder, fileName);
		File f = new File(FilterUtils.filter(newFilePath));
		if (f.exists()) {
			logger.warn("convertToxml " + newFilePath + " exists, delete it");
			f.delete();
		}

		try {
			FileUtils.moveFileToDirectory(new File(FilterUtils.filter(filePath)),
					new File(FilterUtils.filter(backupFolder)), true);
		} catch (IOException e) {
			logger.error("convertToxml move file", e);
		}

		logger.info("convertToxml " + fileName + " is moved to " + newFilePath);
	}

	/**
	 * [???????????????SAA????????????] ???????????????Sw:RMARecrd???Type??????:Sw:Tp=> Received
	 * ????????????????????????<Doc:Issr>BKCHTWT0</Doc:Issr>???RMA?????????Enabled???(????????????????????????) PS : 1.
	 * ??????????????????RMA?????????Enabled??????????????????????????? 2. Sw:Tp=>
	 * Issued????????????????????????????????????????????????????????????RMA??????(????????????????????????)
	 ***/
	private void addContent(String xmlfiledata, String filename) {
		BufferedWriter fw = null;
		FileOutputStream fout = null;
		String outputdata = "";
		Document doc = null;
		try {
			File targetfilepath = new File(FilenameUtils.concat(outputxmlFolder, filename + extName));

			fout = new FileOutputStream(targetfilepath, true);
			fw = new BufferedWriter(new OutputStreamWriter(fout, "UTF-8"));

			logger.info("xmlfiledata:" + xmlfiledata);
			doc = DocumentHelper.parseText(xmlfiledata);
			Element rootElt = doc.getRootElement();
			logger.info("RMAFile:" + rootElt.getName()); // ?????????????????????,??????????????????????????????
			// Element rmafile = rootElt.element("Sw:RMAFile");
			Iterator it = rootElt.elementIterator("RMARecrd");
			while (it.hasNext()) {
				Element element = (Element) it.next();

				// ???????????????????????????
				// System.out.println("id: " + element.attributeValue("Doc:Issr"));
				// ????????????????????????
				if (!element.elementText("Tp").equals("Received")) {
					continue;
				}
				String docissr = element.elementText("Issr");
				logger.info("Issr: " + docissr);
				fw.write(docissr);
				fw.newLine();
				outputdata += docissr + "\r\n";
			}
			if (fw != null)
				fw.flush();
			logger.info("xml outputdata: " + outputdata);
		} catch (DocumentException e) {
			logger.error("xml?????? doc error" + e);
		} catch (Exception e) {
			logger.error("xml?????? error" + e);
		} finally {
			SafeClose.close(fw);
			SafeClose.close(fout);
		}
		// ?????????????????????CLOSE

	}
}
