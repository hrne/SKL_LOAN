package com.st1.itx.db.repository.hist;

import java.util.Optional;

import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.AchAuthLogHistory;
import com.st1.itx.db.domain.AchAuthLogHistoryId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface AchAuthLogHistoryRepositoryHist extends JpaRepository<AchAuthLogHistory, AchAuthLogHistoryId> {

	// CustNo %
	public Slice<AchAuthLogHistory> findAllByCustNoLike(int custNo_0, Pageable pageable);

	// RepayAcct %
	public Slice<AchAuthLogHistory> findAllByRepayAcctLike(String repayAcct_0, Pageable pageable);

	// CustNo =
	public Slice<AchAuthLogHistory> findAllByCustNoIs(int custNo_0, Pageable pageable);

	// RepayAcct =
	public Slice<AchAuthLogHistory> findAllByRepayAcctIs(String repayAcct_0, Pageable pageable);

	// AuthCreateDate >= ,AND AuthCreateDate <=
	public Slice<AchAuthLogHistory> findAllByAuthCreateDateGreaterThanEqualAndAuthCreateDateLessThanEqual(int authCreateDate_0, int authCreateDate_1, Pageable pageable);

	// PropDate >= ,AND PropDate <=
	public Slice<AchAuthLogHistory> findAllByPropDateGreaterThanEqualAndPropDateLessThanEqual(int propDate_0, int propDate_1, Pageable pageable);

	// RetrDate >= ,AND RetrDate <=
	public Slice<AchAuthLogHistory> findAllByRetrDateGreaterThanEqualAndRetrDateLessThanEqual(int retrDate_0, int retrDate_1, Pageable pageable);

	// CustNo = ,AND RepayBank = ,AND RepayAcct = ,AND FacmNo =
	public Optional<AchAuthLogHistory> findTopByCustNoIsAndRepayBankIsAndRepayAcctIsAndFacmNoIsOrderByAuthCreateDateDesc(int custNo_0, String repayBank_1, String repayAcct_2, int facmNo_3);

	// MediaCode ! ,AND AuthStatus ^i ,AND CustNo = ,AND PropDate =
	public Slice<AchAuthLogHistory> findAllByMediaCodeIsNullAndAuthStatusInAndCustNoIsAndPropDateIs(List<String> authStatus_1, int custNo_2, int propDate_3, Pageable pageable);

	// MediaCode ! ,AND AuthStatus ^i ,AND PropDate =
	public Slice<AchAuthLogHistory> findAllByMediaCodeIsNullAndAuthStatusInAndPropDateIs(List<String> authStatus_1, int propDate_2, Pageable pageable);

	// MediaCode ! ,AND AuthStatus ^i ,AND CustNo =
	public Slice<AchAuthLogHistory> findAllByMediaCodeIsNullAndAuthStatusInAndCustNoIs(List<String> authStatus_1, int custNo_2, Pageable pageable);

	// MediaCode ! ,AND AuthStatus ^i
	public Slice<AchAuthLogHistory> findAllByMediaCodeIsNullAndAuthStatusIn(List<String> authStatus_1, Pageable pageable);

	// MediaCode ! ,AND PropDate >= ,AND PropDate <=
	public Slice<AchAuthLogHistory> findAllByMediaCodeIsNullAndPropDateGreaterThanEqualAndPropDateLessThanEqual(int propDate_1, int propDate_2, Pageable pageable);

	// MediaCode = ,AND PropDate >= ,AND PropDate <=
	public Slice<AchAuthLogHistory> findAllByMediaCodeIsAndPropDateGreaterThanEqualAndPropDateLessThanEqual(String mediaCode_0, int propDate_1, int propDate_2, Pageable pageable);

	// CustNo = ,AND FacmNo = ,AND CreateFlag =
	public Optional<AchAuthLogHistory> findTopByCustNoIsAndFacmNoIsAndCreateFlagIsOrderByAuthCreateDateDescCreateDateDesc(int custNo_0, int facmNo_1, String createFlag_2);

	// CustNo = ,AND RepayBank = ,AND RepayAcct =
	public Optional<AchAuthLogHistory> findTopByCustNoIsAndRepayBankIsAndRepayAcctIsOrderByAuthCreateDateDescCreateDateDesc(int custNo_0, String repayBank_1, String repayAcct_2);

	// CustNo = ,AND FacmNo =
	public Slice<AchAuthLogHistory> findAllByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(int custNo_0, int facmNo_1, Pageable pageable);

	// CustNo = ,AND FacmNo =
	public Optional<AchAuthLogHistory> findTopByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(int custNo_0, int facmNo_1);

	// CustNo = ,AND FacmNo =
	public Optional<AchAuthLogHistory> findTopByCustNoIsAndFacmNoIsOrderByDetailSeqDesc(int custNo_0, int facmNo_1);

	// AuthCreateDate = ,AND CustNo = ,AND RepayBank = ,AND RepayAcct = ,AND
	// CreateFlag =
	public Optional<AchAuthLogHistory> findTopByAuthCreateDateIsAndCustNoIsAndRepayBankIsAndRepayAcctIsAndCreateFlagIsOrderByDetailSeqDesc(int authCreateDate_0, int custNo_1, String repayBank_2,
			String repayAcct_3, String createFlag_4);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<AchAuthLogHistory> findByAchAuthLogHistoryId(AchAuthLogHistoryId achAuthLogHistoryId);

}
