package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.TxToDoDetailReserve;
import com.st1.itx.db.domain.TxToDoDetailReserveId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxToDoDetailReserveRepositoryDay extends JpaRepository<TxToDoDetailReserve, TxToDoDetailReserveId> {

  // ItemCode = ,AND Status >= ,AND Status <=
  public Slice<TxToDoDetailReserve> findAllByItemCodeIsAndStatusGreaterThanEqualAndStatusLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAscDtlValueAsc(String itemCode_0, int status_1, int status_2, Pageable pageable);

  // ItemCode = ,AND Status >= ,AND Status <=  ,AND DataDate >= ,AND DataDate <=
  public Slice<TxToDoDetailReserve> findAllByItemCodeIsAndStatusGreaterThanEqualAndStatusLessThanEqualAndDataDateGreaterThanEqualAndDataDateLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAsc(String itemCode_0, int status_1, int status_2, int dataDate_3, int dataDate_4, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<TxToDoDetailReserve> findByTxToDoDetailReserveId(TxToDoDetailReserveId txToDoDetailReserveId);

}

