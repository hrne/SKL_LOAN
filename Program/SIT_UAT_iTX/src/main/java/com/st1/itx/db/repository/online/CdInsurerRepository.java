package com.st1.itx.db.repository.online;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdInsurer;
import com.st1.itx.db.domain.CdInsurerId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdInsurerRepository extends JpaRepository<CdInsurer, CdInsurerId> {

	// InsurerItem %
	public Slice<CdInsurer> findAllByInsurerItemLike(String insurerItem_0, Pageable pageable);

	// InsurerType >= ,AND InsurerType <= ,AND InsurerCode >= ,AND InsurerCode <=
	public Slice<CdInsurer> findAllByInsurerTypeGreaterThanEqualAndInsurerTypeLessThanEqualAndInsurerCodeGreaterThanEqualAndInsurerCodeLessThanEqualOrderByInsurerTypeAscInsurerCodeAsc(
			String insurerType_0, String insurerType_1, String insurerCode_2, String insurerCode_3, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<CdInsurer> findByCdInsurerId(CdInsurerId cdInsurerId);

}
