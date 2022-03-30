package com.st1.ifx.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.st1.ifx.domain.TranDocLog;

public interface TranDocLogRepository extends JpaRepository<TranDocLog, Long> {
	List<TranDocLog> findByDocId(Long docId);

	@Query("select count(x) from TranDocLog x where x.docId = (:docId)")
	Long getDupCounts(@Param("docId") Long docId);

	@Query("select count(x) from TranDocLog x where x.docId = (:docId) and x.printBrno = (:printBrno)")
	Long getDupbrnCounts(@Param("docId") Long docId, @Param("printBrno") String printBrno);

}
