package com.st1.itx.db.repository.online;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.NegAppr02;
import com.st1.itx.db.domain.NegAppr02Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface NegAppr02Repository extends JpaRepository<NegAppr02, NegAppr02Id> {

	// AcDate >= , AND AcDate <=
	public Slice<NegAppr02> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByCustIdAscAcDateAsc(int acDate_0, int acDate_1, Pageable pageable);

	// AcDate =
	public Slice<NegAppr02> findAllByAcDateIsOrderByCustIdAsc(int acDate_0, Pageable pageable);

	// BringUpDate =
	public Slice<NegAppr02> findAllByBringUpDateIsOrderByCustIdAsc(int bringUpDate_0, Pageable pageable);

	// NegTransAcDate = , AND NegTransTlrNo = , AND NegTransTxtNo =
	public Slice<NegAppr02> findAllByNegTransAcDateIsAndNegTransTlrNoIsAndNegTransTxtNoIs(int negTransAcDate_0, String negTransTlrNo_1, int negTransTxtNo_2, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<NegAppr02> findByNegAppr02Id(NegAppr02Id negAppr02Id);

}
