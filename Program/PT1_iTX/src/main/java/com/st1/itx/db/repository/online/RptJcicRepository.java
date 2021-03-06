package com.st1.itx.db.repository.online;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.RptJcic;
import com.st1.itx.db.domain.RptJcicId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface RptJcicRepository extends JpaRepository<RptJcic, RptJcicId> {

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<RptJcic> findByRptJcicId(RptJcicId rptJcicId);

}
