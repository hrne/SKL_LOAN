package com.st1.ifx.hcomm.netty;

import java.util.concurrent.atomic.AtomicInteger;

public class IntegerFactory {

	public IntegerFactory() {
	}

	private static class SingletonHolder {
		private static final AtomicInteger INSTANCE = new AtomicInteger();
	}

	public static final AtomicInteger getInstance() {
		return SingletonHolder.INSTANCE;
	}

}
