package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.Ias39Loss;
import com.st1.itx.db.domain.Ias39LossId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface Ias39LossRepository extends JpaRepository<Ias39Loss, Ias39LossId> {

  // CustNo = ,AND FacmNo >= ,AND FacmNo <= 
  public Slice<Ias39Loss> findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByFacmNoAscMarkDateAsc(int custNo_0, int facmNo_1, int facmNo_2, Pageable pageable);

  // CustNo >= ,AND CustNo <= ,AND FacmNo >= ,AND FacmNo <= 
  public Slice<Ias39Loss> findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCustNoAscFacmNoAscMarkDateAsc(int custNo_0, int custNo_1, int facmNo_2, int facmNo_3, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<Ias39Loss> findByIas39LossId(Ias39LossId ias39LossId);

}

