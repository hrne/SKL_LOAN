package com.st1.itx.db.repository.day;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.TxTeller;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxTellerRepositoryDay extends JpaRepository<TxTeller, String> {

	// BrNo = ,AND TlrNo %
	public Slice<TxTeller> findAllByBrNoIsAndTlrNoLikeOrderByTlrNoAsc(String brNo_0, String tlrNo_1, Pageable pageable);

	// BrNo = ,AND GroupNo >=,AND GroupNo <=,AND LevelFg>=,AND LevelFg<=
	public Slice<TxTeller> findAllByBrNoIsAndGroupNoGreaterThanEqualAndGroupNoLessThanEqualAndLevelFgGreaterThanEqualAndLevelFgLessThanEqualOrderByTlrNoAsc(String brNo_0, String groupNo_1,
			String groupNo_2, int levelFg_3, int levelFg_4, Pageable pageable);

	// TlrNo %
	public Slice<TxTeller> findAllByTlrNoLikeOrderByTlrNoAsc(String tlrNo_0, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<TxTeller> findByTlrNo(String tlrNo);

}
