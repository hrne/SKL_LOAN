package com.st1.ifx.file.excel.item.help;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;

import com.st1.ifx.file.excel.ExcelSplitter;

public class MasterWorker extends UntypedWorker {
	private LinkedBlockingDeque<Object> master;
	private LinkedBlockingDeque<Object> itemQueue;
	private LinkedBlockingDeque<Object> chunkQueue;

	ThreadPoolExecutor pool;

	public MasterWorker(MasterSystem system) {
		super(system.pool, system.master, "master");
		this.master = system.master;
		this.itemQueue = system.itemQueue;
		this.chunkQueue = system.chunkQueue;

		this.startListen();
	}

	String excelFile;

	public void setExcelFile(String excelFile) {
		this.excelFile = excelFile;
	}

	int totalListNumber = Integer.MAX_VALUE;
	int convertedNumber = 0;
	final int CHUNK_SIZE = 100;

	public void perform() {
		System.out.println("perform");
		exeuteTask(new Runnable() {
			@Override
			public void run() {
				System.out.println("run");
				ExcelSplitter splitter = new ExcelSplitter();
				splitter.setExcelFile(excelFile);
				HelpDefSheetProcessor sheetProcessor = new HelpDefSheetProcessor(master);
				splitter.setProcessor(sheetProcessor);
				splitter.perform();
				totalListNumber = sheetProcessor.getListCount();

			}
		});
	}

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof HelpDef) {
			itemQueue.add(message);
		} else if (message instanceof StoredMsg) {
			convertedNumber++;
			appendChunk((StoredMsg) message, totalListNumber == convertedNumber);
		} else if (message instanceof StoredMsg[]) {
			chunkQueue.add(message);
		} else if (message instanceof String) {
			System.out.println("IN MasterWorker!");
			System.out.println(message);
		} else {
			unhandled(message);
		}
	}

	List<StoredMsg> itemList = new ArrayList<StoredMsg>(CHUNK_SIZE);

	private void appendChunk(StoredMsg o, boolean flush) {
		if (itemList.size() < CHUNK_SIZE) {
			itemList.add(o);
		} else {
			flushChunk();
			itemList.add(o);
		}
		if (flush)
			flushChunk();

	}

	private void flushChunk() {
		StoredMsg[] msgs = itemList.toArray(new StoredMsg[0]);
		master.add(msgs);
		itemList.clear();

	}

}
