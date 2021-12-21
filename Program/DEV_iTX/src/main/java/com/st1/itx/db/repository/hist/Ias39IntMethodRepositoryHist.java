package com.st1.itx.db.repository.hist;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.Ias39IntMethod;
import com.st1.itx.db.domain.Ias39IntMethodId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface Ias39IntMethodRepositoryHist extends JpaRepository<Ias39IntMethod, Ias39IntMethodId> {

	// YearMonth =
	public Slice<Ias39IntMethod> findAllByYearMonthIsOrderByCustNoAscFacmNoAscBormNoAsc(int yearMonth_0, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<Ias39IntMethod> findByIas39IntMethodId(Ias39IntMethodId ias39IntMethodId);

}
