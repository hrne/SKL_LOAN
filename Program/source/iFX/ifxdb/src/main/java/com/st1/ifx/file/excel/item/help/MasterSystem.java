package com.st1.ifx.file.excel.item.help;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;

public class MasterSystem {
	LinkedBlockingDeque<Object> master = new LinkedBlockingDeque<Object>();
	LinkedBlockingDeque<Object> itemQueue = new LinkedBlockingDeque<Object>();
	LinkedBlockingDeque<Object> chunkQueue = new LinkedBlockingDeque<Object>();

	ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newCachedThreadPool();

	public static void main(String[] args) {
		MasterSystem system = new MasterSystem();

		List<JsonWorker> jsonWorkers = new ArrayList<JsonWorker>();
		for (int i = 0; i < 3; i++) {
			jsonWorkers.add(new JsonWorker(system, "jsonWorker#" + i));
		}
		ChunkWorker chunkWorker = new ChunkWorker(system, "chunkWorker");
		MasterWorker master = new MasterWorker(system);

		String excelFile = "d:/ifxfolder/runtime/props/HELP.xls";
		master.setExcelFile(excelFile);
		master.perform();

	}

}
