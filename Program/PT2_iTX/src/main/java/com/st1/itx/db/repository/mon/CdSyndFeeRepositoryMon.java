package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdSyndFee;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdSyndFeeRepositoryMon extends JpaRepository<CdSyndFee, String> {

	// AcctCode >= ,AND AcctCode <=
	public Optional<CdSyndFee> findTopByAcctCodeGreaterThanEqualAndAcctCodeLessThanEqual(String acctCode_0, String acctCode_1);

	// SyndFeeCode >= ,AND SyndFeeCode <=
	public Slice<CdSyndFee> findAllBySyndFeeCodeGreaterThanEqualAndSyndFeeCodeLessThanEqualOrderBySyndFeeCodeAsc(String syndFeeCode_0, String syndFeeCode_1, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<CdSyndFee> findBySyndFeeCode(String syndFeeCode);

}
