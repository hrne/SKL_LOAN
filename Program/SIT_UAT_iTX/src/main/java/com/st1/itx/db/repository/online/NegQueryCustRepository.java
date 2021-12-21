package com.st1.itx.db.repository.online;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.NegQueryCust;
import com.st1.itx.db.domain.NegQueryCustId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface NegQueryCustRepository extends JpaRepository<NegQueryCust, NegQueryCustId> {

	// CustId=
	public Slice<NegQueryCust> findAllByCustIdIs(String custId_0, Pageable pageable);

	// AcDate= ,AND FileYN=
	public Slice<NegQueryCust> findAllByAcDateIsAndFileYNIsOrderBySeqNoDesc(int acDate_0, String fileYN_1, Pageable pageable);

	// AcDate=
	public Slice<NegQueryCust> findAllByAcDateIsOrderBySeqNoDesc(int acDate_0, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<NegQueryCust> findByNegQueryCustId(NegQueryCustId negQueryCustId);

}
