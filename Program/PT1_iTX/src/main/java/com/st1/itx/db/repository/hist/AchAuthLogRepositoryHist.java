package com.st1.itx.db.repository.hist;


import java.util.Optional;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.AchAuthLog;
import com.st1.itx.db.domain.AchAuthLogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface AchAuthLogRepositoryHist extends JpaRepository<AchAuthLog, AchAuthLogId> {

  // CustNo %
  public Slice<AchAuthLog> findAllByCustNoLike(int custNo_0, Pageable pageable);

  // RepayAcct %
  public Slice<AchAuthLog> findAllByRepayAcctLike(String repayAcct_0, Pageable pageable);

  // CustNo =
  public Slice<AchAuthLog> findAllByCustNoIs(int custNo_0, Pageable pageable);

  // RepayAcct =
  public Slice<AchAuthLog> findAllByRepayAcctIs(String repayAcct_0, Pageable pageable);

  // AuthCreateDate >= ,AND AuthCreateDate <=
  public Slice<AchAuthLog> findAllByAuthCreateDateGreaterThanEqualAndAuthCreateDateLessThanEqual(int authCreateDate_0, int authCreateDate_1, Pageable pageable);

  // PropDate >= ,AND PropDate <=
  public Slice<AchAuthLog> findAllByPropDateGreaterThanEqualAndPropDateLessThanEqual(int propDate_0, int propDate_1, Pageable pageable);

  // RetrDate >= ,AND RetrDate <=
  public Slice<AchAuthLog> findAllByRetrDateGreaterThanEqualAndRetrDateLessThanEqual(int retrDate_0, int retrDate_1, Pageable pageable);

  // CustNo = ,AND RepayBank = ,AND RepayAcct = ,AND FacmNo = 
  public Optional<AchAuthLog> findTopByCustNoIsAndRepayBankIsAndRepayAcctIsAndFacmNoIsOrderByAuthCreateDateDesc(int custNo_0, String repayBank_1, String repayAcct_2, int facmNo_3);

  // MediaCode ! ,AND AuthStatus ^i ,AND CustNo = ,AND PropDate = 
  public Slice<AchAuthLog> findAllByMediaCodeIsNullAndAuthStatusInAndCustNoIsAndPropDateIs(List<String> authStatus_1, int custNo_2, int propDate_3, Pageable pageable);

  // MediaCode ! ,AND AuthStatus ^i ,AND PropDate = 
  public Slice<AchAuthLog> findAllByMediaCodeIsNullAndAuthStatusInAndPropDateIs(List<String> authStatus_1, int propDate_2, Pageable pageable);

  // MediaCode ! ,AND AuthStatus ^i ,AND CustNo = 
  public Slice<AchAuthLog> findAllByMediaCodeIsNullAndAuthStatusInAndCustNoIs(List<String> authStatus_1, int custNo_2, Pageable pageable);

  // MediaCode ! ,AND AuthStatus ^i 
  public Slice<AchAuthLog> findAllByMediaCodeIsNullAndAuthStatusIn(List<String> authStatus_1, Pageable pageable);

  // MediaCode ! ,AND PropDate >= ,AND PropDate <= 
  public Slice<AchAuthLog> findAllByMediaCodeIsNullAndPropDateGreaterThanEqualAndPropDateLessThanEqual(int propDate_1, int propDate_2, Pageable pageable);

  // MediaCode = ,AND PropDate >= ,AND PropDate <= 
  public Slice<AchAuthLog> findAllByMediaCodeIsAndPropDateGreaterThanEqualAndPropDateLessThanEqual(String mediaCode_0, int propDate_1, int propDate_2, Pageable pageable);

  // CustNo = ,AND FacmNo = ,AND CreateFlag = 
  public Optional<AchAuthLog> findTopByCustNoIsAndFacmNoIsAndCreateFlagIsOrderByAuthCreateDateDescCreateDateDesc(int custNo_0, int facmNo_1, String createFlag_2);

  // CustNo = ,AND RepayBank = ,AND RepayAcct = 
  public Optional<AchAuthLog> findTopByCustNoIsAndRepayBankIsAndRepayAcctIsOrderByAuthCreateDateDescCreateDateDesc(int custNo_0, String repayBank_1, String repayAcct_2);

  // CustNo = ,AND FacmNo = 
  public Slice<AchAuthLog> findAllByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(int custNo_0, int facmNo_1, Pageable pageable);

  // CustNo = ,AND FacmNo = 
  public Optional<AchAuthLog> findTopByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(int custNo_0, int facmNo_1);

  // CustNo = ,AND RepayBank = ,AND AuthStatus ^i ,AND CreateFlag ^i
  public Slice<AchAuthLog> findAllByCustNoIsAndRepayBankIsAndAuthStatusInAndCreateFlagInOrderByRepayAcctDescAuthCreateDateDesc(int custNo_0, String repayBank_1, List<String> authStatus_2, List<String> createFlag_3, Pageable pageable);

  // CustNo = ,AND FacmNo = ,AND RepayBank = ,AND RepayAcct = 
  public Optional<AchAuthLog> findTopByCustNoIsAndFacmNoIsAndRepayBankIsAndRepayAcctIs(int custNo_0, int facmNo_1, String repayBank_2, String repayAcct_3);

  // CustNo = ,AND FacmNo = ,AND RepayBank = ,AND RepayAcct = ,AND PropDate = 
  public Optional<AchAuthLog> findTopByCustNoIsAndFacmNoIsAndRepayBankIsAndRepayAcctIsAndPropDateIs(int custNo_0, int facmNo_1, String repayBank_2, String repayAcct_3, int propDate_4);

  // PropDate = ,AND BatchNo %
  public Slice<AchAuthLog> findAllByPropDateIsAndBatchNoLikeOrderByBatchNoDesc(int propDate_0, String batchNo_1, Pageable pageable);

  // PropDate <= ,AND RetrDate = ,AND BatchNo %
  public Optional<AchAuthLog> findTopByPropDateLessThanEqualAndRetrDateIsAndBatchNoLikeOrderByPropDateDescBatchNoDesc(int propDate_0, int retrDate_1, String batchNo_2);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<AchAuthLog> findByAchAuthLogId(AchAuthLogId achAuthLogId);

}

