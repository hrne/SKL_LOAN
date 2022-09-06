package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.TxCruiser;
import com.st1.itx.db.domain.TxCruiserId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxCruiserRepositoryMon extends JpaRepository<TxCruiser, TxCruiserId> {

	// Status =
	public Slice<TxCruiser> findAllByStatusIsOrderByTxSeqAsc(String status_0, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<TxCruiser> findByTxCruiserId(TxCruiserId txCruiserId);

}
