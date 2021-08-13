package com.st1.itx.db.repository.day;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.RelsCompany;
import com.st1.itx.db.domain.RelsCompanyId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface RelsCompanyRepositoryDay extends JpaRepository<RelsCompany, RelsCompanyId> {

	// RelsUKey =
	public Slice<RelsCompany> findAllByRelsUKeyIs(String relsUKey_0, Pageable pageable);

	// CompanyId =
	public Slice<RelsCompany> findAllByCompanyIdIs(String companyId_0, Pageable pageable);

	// CompanyName =
	public Slice<RelsCompany> findAllByCompanyNameIs(String companyName_0, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<RelsCompany> findByRelsCompanyId(RelsCompanyId relsCompanyId);

}
