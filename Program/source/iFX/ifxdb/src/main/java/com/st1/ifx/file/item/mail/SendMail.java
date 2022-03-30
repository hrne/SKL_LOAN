package com.st1.ifx.file.item.mail;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
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
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.ifx.filter.FilterUtils;

public class SendMail {
	private static final Logger logger = LoggerFactory.getLogger(SendMail.class);

	final static String SWIFT_FOLDER = "_swift";

	// utf8 with 附件 (測試ok)
	public static void doSendattachfiles(String addRip, String swiftFilepath, String messqge_subject2,
			String messqge_body2, Address[] tos, String[] attachFiles) {
		// String messqge_recip = "WISELY@tcb-bank.com.tw";
		logger.info("please.doSendattachfiles...mail go! doSendattachfiles");
		logger.info("messqge_subject:" + messqge_subject2);
		logger.info("messqge_body:" + messqge_body2);
		logger.info("mailIpAddress:" + FilterUtils.escape(addRip));
		logger.info("swiftFilepath:" + FilterUtils.escape(swiftFilepath));
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
						List<File> filelsit = getSwiftFolder(swiftFilepath, brno, today, ".rptsf");
						for (File filePathtmp : filelsit) {
							if (filePathtmp.getName().startsWith(filePath)) {
								filePath = filePathtmp.getPath();
								logger.info("filePath change->" + filePath);
								break;
							}
						}

					}
					if (new File(filePath).exists()) {
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
					}
				}
			}
			mesg.setContent(mp);

			// mesg.setSentDate(new Date()) ;
			mesg.saveChanges();
			logger.info("getEncoding:" + mesg.getEncoding());
			Transport.send(mesg);
		} catch (MessagingException ex) {
			while ((ex = (MessagingException) ex.getNextException()) != null) {
				StringWriter errors = new StringWriter();
				ex.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
			}
		}
	}

	/** 以下複製 ifxweb2中SwiftRepository.java **/
	public static List<File> getSwiftFolder(String swiftfilepath, String brno, String dt, String ext) {
		String folder = combinePaths(swiftfilepath, brno, dt, SWIFT_FOLDER);
		logger.info("getSwiftFolder:" + folder);
		File[] filestmp = new File(folder).listFiles();
		List<File> files = new ArrayList<File>();
		for (int i = 0; i < filestmp.length; i++) {
			logger.info("f.getName():" + filestmp[i].getName());
			if (filestmp[i].getName().endsWith(ext)) {
				files.add(filestmp[i]);
			}
		}
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
