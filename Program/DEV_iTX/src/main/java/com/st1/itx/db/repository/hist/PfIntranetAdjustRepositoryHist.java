package com.st1.itx.db.repository.hist;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.PfIntranetAdjust;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PfIntranetAdjustRepositoryHist extends JpaRepository<PfIntranetAdjust, Long> {

	// CustNo = ,AND FacmNo = ,AND BormNo =
	public Optional<PfIntranetAdjust> findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByCustNoAscFacmNoAscBormNoAsc(int custNo_0, int facmNo_1, int bormNo_2);

	// WorkMonth =
	public Slice<PfIntranetAdjust> findAllByWorkMonthIsOrderByCustNoAscFacmNoAscBormNoAsc(int workMonth_0, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<PfIntranetAdjust> findByLogNo(Long logNo);

}
