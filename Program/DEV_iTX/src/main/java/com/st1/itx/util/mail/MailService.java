package com.st1.itx.util.mail;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.tradeService.CommBuffer;

/**
 * send Mail<br>
 * must param 'from, to, subject, bodyText'<br>
 * option param 'cc, attachedPath' first call setParams(String name, String
 * from, String to, String subject, String bodyText)<br>
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("mailService")
@Scope("prototype")
public class MailService extends CommBuffer {
	@Value("${mailHost}")
	private String mailHost = "smtp.office365.com";

	@Value("${mailPort}")
	private String mailPort = "587";

	@Value("${mailAcc}")
	private String username = "systemOne86995619@outlook.com"; // like yourname@outlook.com

	@Value("${mailPWD}")
	private final String password = "86995619@st1"; // password here

	private String mailServer = "";

	@Value("${mailAcc}")
	private String from = "systemOne86995619@outlook.com";

	private String to = "";

	private String[] toList;

	private String cc = "";

	private String[] ccList;

	private String subject = "";

	private String bodyText = "";

	private String attachedPath = "";

	private String[] attachedPathList;

	private Properties properties;

	private Session session;

	private MimeMessage message;

	public MailService() {
	}

	@PostConstruct
	public void init() {
		this.properties = System.getProperties();

		// 設定傳輸協定為smtp
		this.properties.setProperty("mail.transport.protocol", "smtp");
		// 設定mail Server
		this.properties.setProperty("mail.smtp.host", this.mailHost);
		this.properties.setProperty("mail.smtp.port", this.mailPort);

		this.properties.put("mail.smtp.auth", "true");// 需要驗證帳號密碼
		// SSL authentication
//		this.properties.put("mail.smtp.ssl.enable", "true");
		this.properties.put("mail.smtp.starttls.enable", "true");

		properties.setProperty(mailServer, mailHost);
		this.session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		this.session.setDebug(true);
//		this.session = Session.getDefaultInstance(properties);
		this.message = new MimeMessage(this.session);
	}

	/**
	 * 
	 * @param to       Rev Mail Address
	 * @param subject  subject Text
	 * @param bodyText body Text
	 * @throws LogicException when params is null
	 */
	public void setParams(String to, String subject, String bodyText) throws LogicException {
		if (from == null || to == null || subject == null || bodyText == null)
			throw new LogicException("CE000", "Send Mail Missing parameters..Main");

		this.from = "SKL<" + from + ">";
		this.to = to;
		this.subject = subject;
		this.bodyText = bodyText;
		this.toList = this.to.split(";");
		this.ccList = this.cc.split(";");
	}

	/**
	 * set Params Main from And to multiple split by ;
	 * 
	 * @param name     name
	 * @param from     Send Mail Address
	 * @param to       Rev Mail Address
	 * @param subject  Title Text
	 * @param bodyText Content Text
	 * @throws LogicException when params is null
	 */
	public void setParams(String name, String from, String to, String subject, String bodyText) throws LogicException {
		if (name == null)
			name = "";
		if (from == null || to == null || subject == null || bodyText == null)
			throw new LogicException("CE000", "Send Mail Missing parameters..Main");

		this.from = "SKL " + name + " <" + from + ">";
		this.to = to;
		this.subject = subject;
		this.bodyText = bodyText;
		this.toList = this.to.split(";");
		this.ccList = this.cc.split(";");
	}

	/**
	 * set Params Second
	 * 
	 * @param cc           cc
	 * @param attachedPath attachedPath
	 * @throws LogicException when params is null
	 */
	public void setParams(String cc, String attachedPath) throws LogicException {
		if (cc == null || attachedPath == null)
			throw new LogicException("CE000", "Send Mail Missing parameters..Second");

		this.cc = cc;
		this.attachedPath = attachedPath;

		this.ccList = this.cc.trim().split(";");
		this.attachedPathList = this.attachedPath.trim().split(";");
	}

	private void send() throws LogicException {
		try {
			this.message.setFrom(new InternetAddress(from));

			for (String s : this.toList)
				this.message.addRecipient(Message.RecipientType.TO, new InternetAddress(s.trim()));

			if (!this.cc.trim().isEmpty())
				for (String s : this.ccList)
					this.message.addRecipient(Message.RecipientType.CC, new InternetAddress(s.trim()));

			// Set Subject: header field
			this.message.setSubject(this.subject, "UTF-8");

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();
			// Fill the message
			messageBodyPart.setText(this.bodyText);

			// Create a multipar message
			Multipart multipart = new MimeMultipart();
			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			if (!this.attachedPath.trim().isEmpty())
				for (String url : this.attachedPathList) {
					messageBodyPart = new MimeBodyPart();
					DataSource source = new FileDataSource(url);
					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(new File(url).getName());
					multipart.addBodyPart(messageBodyPart);
				}

			// Send the complete message parts
			this.message.setContent(multipart);

			// Send message
			Transport.send(this.message);
			this.info("Sent message successfully....");
		} catch (MessagingException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
//			throw new LogicException("CE000", "Send Mail Fail..");
		}
	}

	@Override
	public void exec() throws LogicException {
		this.info("Send Mail...");
		this.info("from     : " + this.from);
		this.info("To       : " + this.to);
		this.info("CC       : " + this.cc);
		this.info("Subject  : " + this.subject);
		this.info("BodyText : " + this.bodyText);

		this.send();
	}

}
