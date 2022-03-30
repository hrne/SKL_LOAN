package com.st1.ifx.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.st1.ifx.domain.Journal;
import com.st1.ifx.domain.Rqsp;

public interface JournalService {
	public Journal get(Long id);

	public Journal save(Journal journal);

	public Journal saveJnlAndRqsp(Journal journal, Rqsp rqsp);

	public int getLastSeq(String busdate, String brn, String tlrno);

	public List<Journal> findByF4(String busdate, String brn, String tlrno, String txno);

	// public List<Journal> scan();
	//
	// public List<Journal> getToday(Date dt, String tlrno);

	public List<Journal> findByF4MK2(int calDay, String brn, String tlrno, String txno);

	public Page<Journal> findAllByPage(Pageable pageable);

	public List<Journal> findByCriteriaQuery(HashMap<String, String> requestMap);

}
