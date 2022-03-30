package com.st1.ifx.hcomm.netty;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.netty.buffer.ByteBuf;

@Component
@Scope("prototype")
public class CallbackService {
	public volatile ByteBuf result;

	public void receiveMessage(ByteBuf receiveBuf) throws Exception {
		synchronized (this) {
			result = receiveBuf;
			this.notify();
		}
	}
}
