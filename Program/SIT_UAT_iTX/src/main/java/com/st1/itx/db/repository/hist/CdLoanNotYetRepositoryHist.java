package com.st1.itx.db.repository.hist;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdLoanNotYet;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdLoanNotYetRepositoryHist extends JpaRepository<CdLoanNotYet, String> {

	// NotYetCode %
	public Slice<CdLoanNotYet> findAllByNotYetCodeLikeOrderByNotYetCodeAsc(String notYetCode_0, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<CdLoanNotYet> findByNotYetCode(String notYetCode);

}
