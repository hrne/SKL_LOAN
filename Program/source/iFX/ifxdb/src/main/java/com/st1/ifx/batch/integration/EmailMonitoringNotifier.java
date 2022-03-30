package com.st1.ifx.batch.integration;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class EmailMonitoringNotifier implements BatchMonitorNotifier {
	private static final Logger logger = LoggerFactory.getLogger(EmailMonitoringNotifier.class);

	private MailSender mailSender;
	private SimpleMailMessage templateMessage;
	private String receiver;

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setTemplateMessage(SimpleMailMessage templateMessage) {
		this.templateMessage = templateMessage;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	private String formatExceptionMessage(Throwable exception) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		exception.printStackTrace(new PrintStream(baos));
		return baos.toString();
	}

	private String createMessageContent(JobExecution jobExecution) {
		List<Throwable> exceptions = jobExecution.getFailureExceptions();
		StringBuilder content = new StringBuilder();
		content.append("Job execution #");
		content.append(jobExecution.getId());
		content.append(" of job instance #");
		content.append(jobExecution.getJobInstance().getId());
		content.append(" failed with following exceptions:");
		for (Throwable exception : exceptions) {
			content.append("");
			content.append(formatExceptionMessage(exception));
		}
		return content.toString();
	}

	@Override
	public void notify(JobExecution jobExecution) {
		SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
		msg.setTo(receiver);
		String content = createMessageContent(jobExecution);
		msg.setText(content);
		try {
			mailSender.send(msg);
		} catch (MailException ex) {
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			logger.warn(errors.toString());
		}

	}

}
