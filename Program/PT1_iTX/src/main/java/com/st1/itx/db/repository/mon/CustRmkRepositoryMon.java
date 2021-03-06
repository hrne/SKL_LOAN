package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CustRmk;
import com.st1.itx.db.domain.CustRmkId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CustRmkRepositoryMon extends JpaRepository<CustRmk, CustRmkId> {

	// CustNo =
	public Slice<CustRmk> findAllByCustNoIs(int custNo_0, Pageable pageable);

	// RmkCode =
	public Slice<CustRmk> findAllByRmkCodeIs(String rmkCode_0, Pageable pageable);

	// CustNo =
	public Optional<CustRmk> findTopByCustNoIsOrderByRmkNoDesc(int custNo_0);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<CustRmk> findByCustRmkId(CustRmkId custRmkId);

}
