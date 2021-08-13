package com.st1.itx.db.repository.online;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.TxTemp;
import com.st1.itx.db.domain.TxTempId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxTempRepository extends JpaRepository<TxTemp, TxTempId> {

	// Entdy = ,AND Kinbr = ,AND TlrNo = ,AND TxtNo =
	public Slice<TxTemp> findAllByEntdyIsAndKinbrIsAndTlrNoIsAndTxtNoIsOrderBySeqNoAsc(int entdy_0, String kinbr_1, String tlrNo_2, String txtNo_3, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<TxTemp> findByTxTempId(TxTempId txTempId);

}
