package com.st1.ifx.batch.integration;

public class ConsoleAuditService {
	public void audit(Object payload) {
		System.out.println("audit:" + payload.toString());
	}
}
