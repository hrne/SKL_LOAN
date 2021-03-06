package com.st1.itx.db.repository.online;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.BankRelationSelf;
import com.st1.itx.db.domain.BankRelationSelfId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface BankRelationSelfRepository extends JpaRepository<BankRelationSelf, BankRelationSelfId> {

	// CustId =
	public Slice<BankRelationSelf> findAllByCustIdIs(String custId_0, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<BankRelationSelf> findByBankRelationSelfId(BankRelationSelfId bankRelationSelfId);

}
