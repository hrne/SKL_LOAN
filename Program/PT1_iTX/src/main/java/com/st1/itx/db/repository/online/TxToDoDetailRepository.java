package com.st1.itx.db.repository.online;


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

import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.domain.TxToDoDetailId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxToDoDetailRepository extends JpaRepository<TxToDoDetail, TxToDoDetailId> {

  // ItemCode = ,AND Status >= ,AND Status <=
  public Slice<TxToDoDetail> findAllByItemCodeIsAndStatusGreaterThanEqualAndStatusLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAscDtlValueAsc(String itemCode_0, int status_1, int status_2, Pageable pageable);

  // ItemCode = ,AND DtlValue >= ,AND DtlValue <=
  public Slice<TxToDoDetail> findAllByItemCodeIsAndDtlValueGreaterThanEqualAndDtlValueLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAscDtlValueAsc(String itemCode_0, String dtlValue_1, String dtlValue_2, Pageable pageable);

  // ItemCode = ,AND DtlValue = ,AND Status >= ,AND Status <=  ,AND DataDate >= ,AND DataDate <=
  public Slice<TxToDoDetail> findAllByItemCodeIsAndDtlValueIsAndStatusGreaterThanEqualAndStatusLessThanEqualAndDataDateGreaterThanEqualAndDataDateLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAscDtlValueAsc(String itemCode_0, String dtlValue_1, int status_2, int status_3, int dataDate_4, int dataDate_5, Pageable pageable);

  // ItemCode = ,AND Status >= ,AND Status <=  ,AND DataDate >= ,AND DataDate <=
  public Slice<TxToDoDetail> findAllByItemCodeIsAndStatusGreaterThanEqualAndStatusLessThanEqualAndDataDateGreaterThanEqualAndDataDateLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAscDtlValueAsc(String itemCode_0, int status_1, int status_2, int dataDate_3, int dataDate_4, Pageable pageable);

  // ItemCode = ,AND TitaEntdy = ,AND TitaKinbr = ,AND TitaTlrNo = ,AND TitaTxtNo = 
  public Slice<TxToDoDetail> findAllByItemCodeIsAndTitaEntdyIsAndTitaKinbrIsAndTitaTlrNoIsAndTitaTxtNoIsOrderByItemCodeAscCustNoAscBormNoAscDtlValueAsc(String itemCode_0, int titaEntdy_1, String titaKinbr_2, String titaTlrNo_3, int titaTxtNo_4, Pageable pageable);

  // ItemCode = ,AND DtlValue %
  public Slice<TxToDoDetail> findAllByItemCodeIsAndDtlValueLikeOrderByStatusAscCustNoAscFacmNoAscBormNoAscDtlValueAsc(String itemCode_0, String dtlValue_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<TxToDoDetail> findByTxToDoDetailId(TxToDoDetailId txToDoDetailId);

}

