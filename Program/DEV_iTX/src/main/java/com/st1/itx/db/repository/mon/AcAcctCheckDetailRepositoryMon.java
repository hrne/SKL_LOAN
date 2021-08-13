package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.AcAcctCheckDetail;
import com.st1.itx.db.domain.AcAcctCheckDetailId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface AcAcctCheckDetailRepositoryMon extends JpaRepository<AcAcctCheckDetail, AcAcctCheckDetailId> {

	// AcDate =
	public Slice<AcAcctCheckDetail> findAllByAcDateIsOrderByAcDateAscBranchNoAscCurrencyCodeAscAcctCodeAscCustNoAscFacmNoAscBormNoAsc(int acDate_0, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<AcAcctCheckDetail> findByAcAcctCheckDetailId(AcAcctCheckDetailId acAcctCheckDetailId);

	// (放款關帳 )維護AcAcctCheckDetail會計業務檢核明細檔
	@Procedure(value = "\"Usp_L6_AcAcctCheckDetail_Ins\"")
	public void uspL6AcacctcheckdetailIns(int tbsdyf, String empNo);

}
