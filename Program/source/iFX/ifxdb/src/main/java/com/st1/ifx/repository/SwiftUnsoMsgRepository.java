package com.st1.ifx.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.st1.ifx.domain.SwiftUnsolicitedMsg;

public interface SwiftUnsoMsgRepository extends JpaRepository<SwiftUnsolicitedMsg, Long> {

	SwiftUnsolicitedMsg findByBrndeptAndSrhdayAndFileName(String brndept, String dt, String filename);

	List<SwiftUnsolicitedMsg> findByBrndeptAndPrintTimesAndSrhdayNot(String brndept, int i, String dt);

	// List<SwiftUnsolicitedMsg> findByBrndeptAndSrhdayAndPrintTimes(String
	// brndept,String
	// dt, int i);
	List<SwiftUnsolicitedMsg> findByBrndeptAndSrhdayAndPrintTimesOrderByIdAsc(String brndept, String dt, int i);// 潘
																												// 加oderbyId

	List<SwiftUnsolicitedMsg> findByBrndeptAndSrhday(String brndept, String dt);

	List<SwiftUnsolicitedMsg> findByPrintTimes(int i);

	List<SwiftUnsolicitedMsg> findByPrintTimesAndSrhdayNot(int i, String stoday);

}
