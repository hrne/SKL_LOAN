package com.st1.ifx.batch.integration;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

public class ChannelAuditor implements ChannelInterceptor {
	private ConsoleAuditService auditService = new ConsoleAuditService();

	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		this.auditService.audit(message.getPayload());
		return message;

	}

}
