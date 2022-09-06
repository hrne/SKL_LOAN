package com.st1.itx.db.repository.hist;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.BatxBaseRateChange;
import com.st1.itx.db.domain.BatxBaseRateChangeId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface BatxBaseRateChangeRepositoryHist extends JpaRepository<BatxBaseRateChange, BatxBaseRateChangeId> {

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<BatxBaseRateChange> findByBatxBaseRateChangeId(BatxBaseRateChangeId batxBaseRateChangeId);

}
