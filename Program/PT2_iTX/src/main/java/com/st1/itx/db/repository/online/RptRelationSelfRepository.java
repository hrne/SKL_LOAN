package com.st1.itx.db.repository.online;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.RptRelationSelf;
import com.st1.itx.db.domain.RptRelationSelfId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface RptRelationSelfRepository extends JpaRepository<RptRelationSelf, RptRelationSelfId> {

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<RptRelationSelf> findByRptRelationSelfId(RptRelationSelfId rptRelationSelfId);

}
