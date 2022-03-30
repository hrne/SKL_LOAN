package com.st1.ifx.file.excel.item.help;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class UntypedWorker {
	static final Logger logger = LoggerFactory.getLogger(UntypedWorker.class);

	protected LinkedBlockingDeque<Object> queue;
	protected String name = "noname";

	ThreadPoolExecutor pool = null;

	public UntypedWorker() {

	}

	public UntypedWorker(ThreadPoolExecutor pool, LinkedBlockingDeque<Object> queue) {
		this.pool = pool;
		this.queue = queue;
	}

	public UntypedWorker(ThreadPoolExecutor pool, LinkedBlockingDeque<Object> queue, String name) {
		this(pool, queue);
		this.name = name;

	}

	protected void exeuteTask(Runnable runnable) {
		this.pool.execute(runnable);
	}

	public void startListen() {
		System.out.println(name + " startListen");
		exeuteTask(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						listenTo();
						Thread.yield();
					} catch (InterruptedException e) {
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						logger.warn(errors.toString());
						break;
					}
				}
			}
		});

	}

	private void listenTo() throws InterruptedException {
		Object obj = queue.poll(100, TimeUnit.MILLISECONDS);
		if (obj != null)
			try {
				onReceive(obj);
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.warn(errors.toString());
				throw new RuntimeException(e);
			}

	}

	abstract public void onReceive(Object message) throws Exception;

	protected void unhandled(Object obj) {
		System.out.println("I can't handle it, either : " + obj.toString());
	}
}
