package com.st1.itx.db.repository.day;


import java.util.Optional;

import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.PostAuthLog;
import com.st1.itx.db.domain.PostAuthLogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PostAuthLogRepositoryDay extends JpaRepository<PostAuthLog, PostAuthLogId> {

  // CustNo =
  public Slice<PostAuthLog> findAllByCustNoIs(int custNo_0, Pageable pageable);

  // RepayAcct =
  public Slice<PostAuthLog> findAllByRepayAcctIs(String repayAcct_0, Pageable pageable);

  // RepayAcct %
  public Slice<PostAuthLog> findAllByRepayAcctLike(String repayAcct_0, Pageable pageable);

  // AuthCreateDate >= ,AND AuthCreateDate <=
  public Slice<PostAuthLog> findAllByAuthCreateDateGreaterThanEqualAndAuthCreateDateLessThanEqual(int authCreateDate_0, int authCreateDate_1, Pageable pageable);

  // PropDate >= ,AND PropDate <=
  public Slice<PostAuthLog> findAllByPropDateGreaterThanEqualAndPropDateLessThanEqualOrderByAuthCreateDateDescCreateDateDesc(int propDate_0, int propDate_1, Pageable pageable);

  // RetrDate >= ,AND RetrDate <=
  public Slice<PostAuthLog> findAllByRetrDateGreaterThanEqualAndRetrDateLessThanEqualOrderByAuthCreateDateDescCreateDateDesc(int retrDate_0, int retrDate_1, Pageable pageable);

  // CustId = ,AND PostDepCode = ,AND CustNo =
  public Optional<PostAuthLog> findTopByCustIdIsAndPostDepCodeIsAndCustNoIsOrderByRepayAcctSeqDesc(String custId_0, String postDepCode_1, int custNo_2);

  // AuthApplCode = ,AND CustNo = ,AND PostDepCode = ,AND RepayAcct = ,AND AuthCode = ,AND FacmNo = 
  public Optional<PostAuthLog> findTopByAuthApplCodeIsAndCustNoIsAndPostDepCodeIsAndRepayAcctIsAndAuthCodeIsAndFacmNoIsOrderByRepayAcctSeqDesc(String authApplCode_0, int custNo_1, String postDepCode_2, String repayAcct_3, String authCode_4, int facmNo_5);

  // PostMediaCode ! ,AND AuthErrorCode ^i ,AND CustNo = ,AND PropDate = 
  public Slice<PostAuthLog> findAllByPostMediaCodeIsNullAndAuthErrorCodeInAndCustNoIsAndPropDateIs(List<String> authErrorCode_1, int custNo_2, int propDate_3, Pageable pageable);

  // PostMediaCode ! ,AND AuthErrorCode ^i ,AND PropDate = 
  public Slice<PostAuthLog> findAllByPostMediaCodeIsNullAndAuthErrorCodeInAndPropDateIs(List<String> authErrorCode_1, int propDate_2, Pageable pageable);

  // PostMediaCode ! ,AND AuthErrorCode ^i ,AND CustNo = 
  public Slice<PostAuthLog> findAllByPostMediaCodeIsNullAndAuthErrorCodeInAndCustNoIs(List<String> authErrorCode_1, int custNo_2, Pageable pageable);

  // PostMediaCode ! ,AND AuthErrorCode ^i 
  public Slice<PostAuthLog> findAllByPostMediaCodeIsNullAndAuthErrorCodeIn(List<String> authErrorCode_1, Pageable pageable);

  // PostMediaCode = ,AND PropDate >= ,AND PropDate <= 
  public Slice<PostAuthLog> findAllByPostMediaCodeIsAndPropDateGreaterThanEqualAndPropDateLessThanEqual(String postMediaCode_0, int propDate_1, int propDate_2, Pageable pageable);

  // PostMediaCode ! ,AND PropDate >= ,AND PropDate <= 
  public Slice<PostAuthLog> findAllByPostMediaCodeIsNullAndPropDateGreaterThanEqualAndPropDateLessThanEqual(int propDate_1, int propDate_2, Pageable pageable);

  // PropDate = , AND AuthCode = , AND FileSeq = 
  public Optional<PostAuthLog> findTopByPropDateIsAndAuthCodeIsAndFileSeqIs(int propDate_0, String authCode_1, int fileSeq_2);

  // CustNo = ,AND FacmNo = 
  public Slice<PostAuthLog> findAllByCustNoIsAndFacmNoIsOrderByRepayAcctSeqDesc(int custNo_0, int facmNo_1, Pageable pageable);

  // CustNo = ,AND PostDepCode = ,AND RepayAcct = ,AND AuthCode = 
  public Optional<PostAuthLog> findTopByCustNoIsAndPostDepCodeIsAndRepayAcctIsAndAuthCodeIsOrderByAuthCreateDateDescCreateDateDesc(int custNo_0, String postDepCode_1, String repayAcct_2, String authCode_3);

  // CustNo = ,AND FacmNo = 
  public Optional<PostAuthLog> findTopByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(int custNo_0, int facmNo_1);

  // CustNo = ,AND AuthApplCode ^i ,AND AuthErrorCode ^i
  public Slice<PostAuthLog> findAllByCustNoIsAndAuthApplCodeInAndAuthErrorCodeInOrderByRepayAcctDescAuthCreateDateDesc(int custNo_0, List<String> authApplCode_1, List<String> authErrorCode_2, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<PostAuthLog> findByPostAuthLogId(PostAuthLogId postAuthLogId);

}

