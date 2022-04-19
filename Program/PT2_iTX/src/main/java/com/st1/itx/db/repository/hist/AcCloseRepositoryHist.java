package com.st1.itx.db.repository.hist;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.AcClose;
import com.st1.itx.db.domain.AcCloseId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface AcCloseRepositoryHist extends JpaRepository<AcClose, AcCloseId> {

	// AcDate = ,AND BranchNo = ,AND SecNo >=
	public Slice<AcClose> findAllByAcDateIsAndBranchNoIsAndSecNoGreaterThanEqualOrderBySecNoDesc(int acDate_0, String branchNo_1, String secNo_2, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<AcClose> findByAcCloseId(AcCloseId acCloseId);

}
