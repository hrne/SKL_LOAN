package com.st1.ifx.file.fdp.item;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;

import com.google.common.base.Joiner;
import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.filter.SafeClose;

@Component
@Scope("prototype")
public class MailConverter {
	private static final Logger logger = LoggerFactory.getLogger(MailConverter.class);

	@Value("${import.mailbox}")
	private String outputmailFolder;

	@Value("${import.backupFolder}")
	private String backupFolder;

	@Value("${import.convertmailToExt}")
	private String extName = "mail";

	@Value("${import.mailIpAddress}")
	private String addrip = "";

	@Value("${import.repbox}")
	private String swiftFilepath = "";

	final static String SWIFT_FOLDER = "_swift";

	@ServiceActivator
	public void convert(Message msg) throws Throwable {
		logger.info("converting msg:{}", msg);
		File file = (File) msg.getPayload();
		logger.info(FilterUtils.escape("convert " + file.getAbsolutePath() + " to " + outputmailFolder));

		convertTomail(file.getAbsolutePath());
		moveFileToBackupFolder(file.getAbsolutePath());
	}

	/*
	 * 		
	 */
	public boolean doMail(Properties pEmailInfo) {
		boolean bSucess = false;
		BufferedWriter fw = null;
		FileOutputStream fout = null;
		// 第一行:主旨
		// 第二行:收件人,收件人,
		// 第三行:附件
		// 檔名:日期-分行?-序號.mail
		// String[] filenm = filename.split("-");

		logger.info("Start doMail!!");
		/*
		 * 172.29.100.86 172.29.100.87 (batch) 172.29.100.84 172.29.100.85 (online)
		 */
		// SMTP info
		String host = pEmailInfo.getProperty("addrip", "");
		logger.info(FilterUtils.escape("addrip:" + host));
		if (StringUtils.isBlank(host)) {
			host = addrip;
		}
		String port = "";
		String mailFrom = "TCB-IFX <ifx@tcb-bank.com.tw>"; // ?
		String pd = "";
		logger.info(FilterUtils.escape("addrip:" + host + " ,mailFrom:" + mailFrom));
		// message info
		String subject = pEmailInfo.getProperty("subject");
		logger.info("subject:" + subject);
		String[] mailTo = pEmailInfo.getProperty("sender").split(";");
		logger.info("mailTo LEN:" + mailTo.length);
		String[] attachFiles = pEmailInfo.getProperty("file").split(";");
		logger.info("attachFiles LEN:" + attachFiles.length);
		String message = pEmailInfo.getProperty("body");
		logger.info("message:" + message);

		try {
			// if (host.isEmpty()) {
			// logger.info("Email don't sent.");
			// } else {
			sendEmailWithToaddress(host, port, mailFrom, pd, mailTo, subject, message, attachFiles);
			logger.info("Email sent.");
			// }
		} catch (Exception ex) {
			logger.error("Could not send email:");
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
		return bSucess;
	}

	private void convertTomail(String filePath) throws Exception {
		logger.info("!!!convertTomail filePath:" + filePath);
		String name = FilenameUtils.getBaseName(filePath);
		List<String> lines = new ArrayList<String>();

		BufferedReader br = null;
		FileInputStream fis = null;

		try {

			String sCurrentLine;
			File f = new File(filePath);
			fis = new FileInputStream(f);
			br = new BufferedReader(new InputStreamReader(fis, "UTF-8")); // 電文轉換
			// br = new BufferedReader(new FileReader(filePath));
			logger.info("temp after BufferedReader");
			while ((sCurrentLine = br.readLine()) != null) {
				logger.info("List mail:" + sCurrentLine);
				lines.add(sCurrentLine);
			}
			logger.info("end ....,do doSend!");
			// doSend();
			logger.info("end doSend,do doMail!");
			doMail(lines, name);
			// doSend5();

		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} finally {
			SafeClose.close(br);
			SafeClose.close(fis);
		}
		// BufferedWriter fw = null;
		// File tmpFile = new File(tmpFilePath);
		// 建立假資料
		System.out.println("mailConverter START");

		// 還沒完成
		// doMail(lines, name);
		logger.info("mailConverter done ");

	}

	private void moveFileToBackupFolder(String filePath) {
		// errorFolder
		String fileName = FilenameUtils.getName(filePath);
		String newFilePath = FilenameUtils.concat(backupFolder, fileName);
		File f = new File(FilterUtils.filter(newFilePath));
		if (f.exists()) {
			logger.warn("convertTomail " + newFilePath + " exists, delete it");
			f.delete();
		}

		try {
			FileUtils.copyFileToDirectory(new File(FilterUtils.filter(filePath)),
					new File(FilterUtils.filter(outputmailFolder)), true);
		} catch (IOException e) {
			logger.error("convertTomail move file outputmail:", e);
		}

		try {
			FileUtils.moveFileToDirectory(new File(FilterUtils.filter(filePath)),
					new File(FilterUtils.filter(backupFolder)), true);
		} catch (IOException e) {
			logger.error("convertTomail move file backup:", e);
		}

		logger.info("convertTomail " + fileName + " is moved to " + newFilePath);
	}

	private void doMail(List<String> list, String filename) {
		BufferedWriter fw = null;
		FileOutputStream fout = null;
		// 第一行:主旨
		// 第二行:收件人,收件人,
		// 第三行:附件
		// 檔名:日期-分行?-序號.mail
		String[] filenm = filename.split("-");

		logger.info("Start doMail!!");
		/*
		 * 172.29.100.86 172.29.100.87 (batch) 172.29.100.84 172.29.100.85 (online)
		 */
		// SMTP info
		String host = addrip;
		String port = "";
		String mailFrom = "TCB-IFX <ifx@tcb-bank.com.tw>"; // ?
		String pd = "";
		logger.info(FilterUtils.escape("addrip:" + host + " ,mailFrom:" + mailFrom));
		// message info
		String subject = list.get(0);
		logger.info("subject:" + subject);
		list.remove(0);

		String[] mailTo = list.get(0).split(";");
		logger.info("mailTo LEN:" + mailTo.length);
		list.remove(0);

		String[] attachFiles = list.get(0).split(";");
		logger.info("attachFiles LEN:" + attachFiles.length);
		list.remove(0);

		String message = Joiner.on("\n").join(list);
		logger.info("message:" + message);

		try {
			if (host.isEmpty()) {
				logger.info("Email don't sent.");
			} else {
				sendEmailWithToaddress(host, port, mailFrom, pd, mailTo, subject, message, attachFiles);
				logger.info("Email sent.");
			}
		} catch (Exception ex) {
			logger.error("Could not send email:", ex);
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}

	}

	/**
	 * 此method為簡易的寄件功能
	 * 
	 * @param userName   寄件者email
	 * @param subject    寄件主旨
	 * @param message    寄件內容
	 * @param 寄件內容格式為utf -8
	 */
	/** 測試完成 */
	public void sendEmailWithToaddress(String host, String port, final String userName, final String password,
			String[] toAddress, String subject, String message, String[] attachFiles)
			throws AddressException, MessagingException {
		try {
			Address[] tos = null;
			List<String> addressString = new ArrayList<String>();
			logger.info("toAddress.length:" + toAddress.length);
			if (toAddress.length > 0) {
				// 為每個郵件接收者創建一個地址
				/*
				 * 包含寄件人 tos = new InternetAddress[toAddress.length + 1]; tos[0] = new
				 * InternetAddress(userName); for (int i=0; i<toAddress.length; i++){ tos[i+1] =
				 * new InternetAddress(toAddress[i]); }
				 */
				// 不包含寄件人
				for (int i = 0; i < toAddress.length; i++) {
					// toAddress string去空白
					if (toAddress[i].trim().length() > 0)
						addressString.add(toAddress[i].trim());
				}
				logger.info("addressString.length:" + addressString.size());
				tos = new InternetAddress[addressString.size()];
				for (int i = 0; i < addressString.size(); i++) {
					tos[i] = new InternetAddress(addressString.get(i));
				}

			} else {
				// 沒人?
				tos = new InternetAddress[1];
				tos[0] = new InternetAddress(userName);
			}

			// string去空白
			for (int k = 0; k < attachFiles.length; k++) {
				attachFiles[k] = attachFiles[k].trim();
			}

			logger.info("before setRecipients! tos LEN:" + tos.length);
			// doSend(host,subject,message,tos,attachFiles);
			doSendattachfiles(host, subject, message, tos, attachFiles);

		} catch (MessagingException ex) {
			while ((ex = (MessagingException) ex.getNextException()) != null) {
				StringWriter errors = new StringWriter();
				ex.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
			}
			logger.error("MessagingException:", ex);
		}
	}

	/**
	 * 合庫提供範例程式(已測試ok)
	 * 
	 * @param
	 * 
	 * 
	 */
	// String messqge_subject = "TEST EMAIL USING JAVA MAIL BY TCB MIKELIU";

	// utf8 測試ok
	public void doSend(String addRip, String messqge_subject2, String messqge_body2, Address[] tos,
			String[] attachFiles) {
		// String messqge_recip = "WISELY@tcb-bank.com.tw";
		logger.info("please.doSendtest3...mail go! test4");
		logger.info("messqge_subject:" + messqge_subject2);
		logger.info("messqge_body:" + messqge_body2);
		if (addRip == null || addRip.isEmpty()) {
			addRip = addrip;
		}
		logger.info("addrip:" + FilterUtils.escape(addRip));
		Properties props = new Properties();
		props.put("mail.smtp.host", addRip);
		Session session;
		MimeMessage mesg;

		session = Session.getDefaultInstance(props, null);
		session.setDebug(true);

		try {
			mesg = new MimeMessage(session);
			mesg.setFrom(new InternetAddress("TCB-IFX <ifx@tcb-bank.com.tw>"));

			mesg.setRecipients(javax.mail.Message.RecipientType.TO, tos);

			mesg.setSubject(messqge_subject2, "utf-8");
			mesg.setText(messqge_body2, "utf-8", "plain");

			logger.info("before setHeader base64");
			mesg.setHeader("Content-Transfer-Encoding", "base64");
			mesg.saveChanges();
			logger.info("getEncoding" + mesg.getEncoding());
			Transport.send(mesg);
		} catch (MessagingException ex) {
			while ((ex = (MessagingException) ex.getNextException()) != null) {
				StringWriter errors = new StringWriter();
				ex.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
			}
		}
	}

	// utf8 with 附件 (測試ok)
	public void doSendattachfiles(String addRip, String messqge_subject2, String messqge_body2, Address[] tos,
			String[] attachFiles) {
		// String messqge_recip = "WISELY@tcb-bank.com.tw";
		logger.info("please.doSendattachfiles...mail go! doSendattachfiles");
		logger.info("messqge_subject:" + messqge_subject2);
		logger.info("messqge_body:" + messqge_body2);
		if (addRip == null || addRip.isEmpty()) {
			addRip = addrip;
		}
		logger.info("addrip:" + FilterUtils.escape(addRip));
		Properties props = new Properties();
		props.put("mail.smtp.host", addRip);
		Session session;// javamail session object
		MimeMessage mesg;// javamail message object
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		String today = sdf2.format(new Date());

		session = Session.getDefaultInstance(props, null);
		session.setDebug(true);

		try {
			mesg = new MimeMessage(session);
			mesg.setFrom(new InternetAddress("TCB-IFX <ifx@tcb-bank.com.tw>"));
			// 收件人
			mesg.setRecipients(javax.mail.Message.RecipientType.TO, tos);
			// 主旨
			mesg.setSubject(messqge_subject2, "utf-8");

			MimeBodyPart mbp1 = new MimeBodyPart();
			// 內文
			mbp1.setText(messqge_body2, "utf-8", "plain");

			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp1);
			// 加入附件
			if (attachFiles != null && attachFiles.length > 0) {
				logger.info("attachFiles.length:" + attachFiles.length);
				for (String filePath : attachFiles) {
					MimeBodyPart attachPart = new MimeBodyPart();
					logger.info("filePath:" + filePath);
					if (filePath.startsWith("SWIFT:")) {
						// 移除前置記號
						filePath = filePath.substring(6);
						logger.info("SWIFT　filePath:" + filePath);
						// 1058比較複雜要請中心告知科別..
						String[] filearr = filePath.split("-");
						String brno = filearr[0];
						logger.info("brno:" + brno);
						if (brno.length() > 4) {
							filearr[0] = filearr[0].substring(0, 4);
							// 只有1058分科別儲存
							if (!brno.startsWith("1058")) {
								brno = brno.substring(0, 4);
							}
						}

						// 該swift資料夾目錄下檔案
						// ex:X:\ifxwriter\repos\report\10582\20160111\_swift\...
						filePath = join(filearr, "-");
						logger.info("real filePath:" + filePath);
						List<File> filelsit = getSwiftFolder(brno, today, ".rptsf");
						if (filelsit != null) {
							for (File filePathtmp : filelsit) {
								if (filePathtmp.getName().startsWith(filePath)) {
									filePath = filePathtmp.getPath();
									logger.info("filePath change->" + filePath);
									break;
								}
							}
						}
					}
					if (new File(FilterUtils.filter(filePath)).exists()) {
						logger.info("do attachFile.");
					} else {
						logger.info("filePath:[" + filePath + "] is not exists!");
						break;
					}

					try {
						attachPart.attachFile(filePath);
						logger.info("after attachFile.");
						mp.addBodyPart(attachPart);
					} catch (IOException ex) {
						StringWriter errors = new StringWriter();
						ex.printStackTrace(new PrintWriter(errors));
						logger.error(errors.toString());
						logger.error("doSendattachfiles1:");
					}
				}
			}
			mesg.setContent(mp);

			// mesg.setSentDate(new Date()) ;
			mesg.saveChanges();
			logger.info("getEncoding:" + mesg.getEncoding());
			// mesg.writeTo(new FileOutputStream("/home/weblogic/ifxDoc/test.eml"));
			Transport.send(mesg);
		} catch (MessagingException ex) {
			logger.error("doSendattachfiles2::", ex);
			while ((ex = (MessagingException) ex.getNextException()) != null) {
				StringWriter errors = new StringWriter();
				ex.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
			}
		}
	}

	/** 以下複製 ifxweb2中SwiftRepository.java **/
	public List<File> getSwiftFolder(String brno, String dt, String ext) {
		String folder = combinePaths(swiftFilepath, brno, dt, SWIFT_FOLDER);
		logger.info("getSwiftFolder:" + folder);
		File[] filestmp = new File(folder).listFiles();
		List<File> files = new ArrayList<File>();
		for (int i = 0; i < filestmp.length; i++) {
			logger.info("f.getName():" + filestmp[i].getName());
			if (filestmp[i].getName().endsWith(ext)) {
				files.add(filestmp[i]);
			}
		}
		if (files != null)
			logger.info("getSwiftFolder end.files size:" + files.size());
		return files;
	}

	public static String combinePaths(String... paths) {
		if (paths.length == 0) {
			return "";
		}
		File combined = new File(FilterUtils.filter(paths[0]));
		int i = 1;
		while (i < paths.length) {
			combined = new File(combined, FilterUtils.filter(paths[i]));
			++i;
		}
		return combined.getPath();
	}

	class EndsWithFilter implements FilenameFilter {
		String ext;

		EndsWithFilter(String ext) {
			this.ext = ext;
		}

		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(ext);
		}

	}

	// join(String array,delimiter)
	public static String join(String r[], String d) {
		if (r.length == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		int i;
		for (i = 0; i < r.length - 1; i++)
			sb.append(r[i] + d);
		return sb.toString() + r[i];
	}
}
