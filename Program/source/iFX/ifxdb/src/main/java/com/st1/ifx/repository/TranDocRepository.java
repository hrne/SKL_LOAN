package com.st1.ifx.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.st1.ifx.domain.TranDoc;

public interface TranDocRepository extends JpaRepository<TranDoc, Long> {
	@Query("SELECT t FROM TranDoc t JOIN FETCH t.buffers WHERE t.docId = (:docId)")
	TranDoc findByDocIdEager(@Param("docId") Long docId);

	List<TranDoc> findByJnlId(Long jnlId);

}
