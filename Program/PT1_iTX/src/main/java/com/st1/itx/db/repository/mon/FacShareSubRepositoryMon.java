package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.FacShareSub;
import com.st1.itx.db.domain.FacShareSubId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FacShareSubRepositoryMon extends JpaRepository<FacShareSub, FacShareSubId> {

	// CreditSysNo = ,AND MainCustNo =
	public Slice<FacShareSub> findAllByCreditSysNoIsAndMainCustNoIsOrderByShareSeqAscShareCustNoAscShareFacmNoAsc(int creditSysNo_0, int mainCustNo_1, Pageable pageable);

	// ShareCustNo = ,AND ShareFacmNo >= ,AND ShareFacmNo <=
	public Slice<FacShareSub> findAllByShareCustNoIsAndShareFacmNoGreaterThanEqualAndShareFacmNoLessThanEqualOrderByCreditSysNoAscMainCustNoAscMainFacmNoAscShareSeqAscShareCustNoAscShareFacmNoAsc(
			int shareCustNo_0, int shareFacmNo_1, int shareFacmNo_2, Pageable pageable);

	// MainCustNo = ,AND MainFacmNo =
	public Slice<FacShareSub> findAllByMainCustNoIsAndMainFacmNoIsOrderByShareSeqAscShareCustNoAscShareFacmNoAsc(int mainCustNo_0, int mainFacmNo_1, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<FacShareSub> findByFacShareSubId(FacShareSubId facShareSubId);

}
