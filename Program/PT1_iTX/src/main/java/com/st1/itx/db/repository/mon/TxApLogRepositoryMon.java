package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.TxApLog;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxApLogRepositoryMon extends JpaRepository<TxApLog, Long> {

	// TlrNo =
	public Slice<TxApLog> findAllByTlrNoIsOrderByTlrNoAsc(String tlrNo_0, Pageable pageable);

	// Entdy =
	public Slice<TxApLog> findAllByEntdyIsOrderByEntdyAsc(int entdy_0, Pageable pageable);

	// Entdy =,AND TlrNo =
	public Slice<TxApLog> findAllByEntdyIsAndTlrNoIsOrderByEntdyAscTlrNoAsc(int entdy_0, String tlrNo_1, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<TxApLog> findByAutoSeq(Long autoSeq);

}
