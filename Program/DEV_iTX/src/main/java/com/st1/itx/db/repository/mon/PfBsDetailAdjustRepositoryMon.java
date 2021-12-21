package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.PfBsDetailAdjust;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PfBsDetailAdjustRepositoryMon extends JpaRepository<PfBsDetailAdjust, Long> {

	// CustNo = ,AND FacmNo = ,AND BormNo =
	public Optional<PfBsDetailAdjust> findTopByCustNoIsAndFacmNoIsAndBormNoIs(int custNo_0, int facmNo_1, int bormNo_2);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<PfBsDetailAdjust> findByLogNo(Long logNo);

}
