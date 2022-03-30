package com.st1.ifx.file.excel.item.help;

import java.util.concurrent.LinkedBlockingDeque;

public class ChunkWorker extends UntypedWorker {
	LinkedBlockingDeque<Object> master;

	public ChunkWorker(MasterSystem system, String name) {
		super(system.pool, system.chunkQueue, name);
		master = system.master;
		startListen();
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof StoredMsg[]) {
			write((StoredMsg[]) message);
		} else {
			unhandled(message);
		}

	}

	private void write(StoredMsg[] message) {
		master.add("done:" + message.length);

	}

}
