package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.TxAuthorize;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxAuthorizeRepository extends JpaRepository<TxAuthorize, Long> {

  // Entdy = ,AND SupNo =
  public Slice<TxAuthorize> findAllByEntdyIsAndSupNoIsOrderBySupNoAsc(int entdy_0, String supNo_1, Pageable pageable);

  // Entdy >= ,AND Entdy <=,AND SupNo %
  public Slice<TxAuthorize> findAllByEntdyGreaterThanEqualAndEntdyLessThanEqualAndSupNoLikeOrderByEntdyAsc(int entdy_0, int entdy_1, String supNo_2, Pageable pageable);

  // CreateDate>=, AND CreateDate<= ,AND SupNo %
  public Slice<TxAuthorize> findAllByCreateDateGreaterThanEqualAndCreateDateLessThanEqualAndSupNoLikeOrderByCreateDateAsc(java.sql.Timestamp createDate_0, java.sql.Timestamp createDate_1, String supNo_2, Pageable pageable);

  // Entdy >= ,AND Entdy <=,AND SupNo %
  public Slice<TxAuthorize> findAllByEntdyGreaterThanEqualAndEntdyLessThanEqualAndSupNoLikeOrderBySupNoAscEntdyAsc(int entdy_0, int entdy_1, String supNo_2, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<TxAuthorize> findByAutoSeq(Long autoSeq);

}

