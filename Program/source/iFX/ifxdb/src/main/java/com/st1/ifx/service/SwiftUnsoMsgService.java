package com.st1.ifx.service;

import java.util.List;

import com.st1.ifx.domain.SwiftPrinter;
import com.st1.ifx.domain.SwiftUnsolicitedMsg;

public interface SwiftUnsoMsgService {
	public void save(SwiftUnsolicitedMsg unso);

	public List<SwiftUnsolicitedMsg> get(String brno, String date);

	public List<SwiftPrinter> getPrinters(String brno);

	public void updataPrintTime(String brno, String dt, String filename);

	public List<SwiftUnsolicitedMsg> findSwiftListbybrndttime(String brndept, String dt, int printTimes);

	public List<SwiftUnsolicitedMsg> findSwiftListbybrndt(String brndept, String dt);

	public void updateNotprintWithoutdt(String brndept, String dt);

	public String findPathByBrndeptSrhdayFilename(String brndept, String dt, String filename);

	public void updateNotprint();

}
