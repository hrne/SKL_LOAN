package com.st1.itx.db.repository.online;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.InsuRenewMediaTemp;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface InsuRenewMediaTempRepository extends JpaRepository<InsuRenewMediaTemp, Long> {

	// FireInsuMonth >= ,AND FireInsuMonth <=
	public Slice<InsuRenewMediaTemp> findAllByFireInsuMonthGreaterThanEqualAndFireInsuMonthLessThanEqualOrderByFireInsuMonthAscCustNoAscSeqAsc(String fireInsuMonth_0, String fireInsuMonth_1,
			Pageable pageable);

	// FireInsuMonth = ,AND ClCode1 = ,AND ClCode2 = ,AND ClNo = ,AND InsuNo =
	public Optional<InsuRenewMediaTemp> findTopByFireInsuMonthIsAndClCode1IsAndClCode2IsAndClNoIsAndInsuNoIs(String fireInsuMonth_0, String clCode1_1, String clCode2_2, String clNo_3,
			String insuNo_4);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<InsuRenewMediaTemp> findByLogNo(Long logNo);

}
