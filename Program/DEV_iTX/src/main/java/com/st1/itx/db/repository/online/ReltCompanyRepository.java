package com.st1.itx.db.repository.online;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.ReltCompany;
import com.st1.itx.db.domain.ReltCompanyId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ReltCompanyRepository extends JpaRepository<ReltCompany, ReltCompanyId> {

	// ReltUKey =
	public Slice<ReltCompany> findAllByReltUKeyIs(String reltUKey_0, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<ReltCompany> findByReltCompanyId(ReltCompanyId reltCompanyId);

}
