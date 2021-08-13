package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CustNotice;
import com.st1.itx.db.domain.CustNoticeId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CustNoticeRepositoryHist extends JpaRepository<CustNotice, CustNoticeId> {

  // CustNo =
  public Slice<CustNotice> findAllByCustNoIs(int custNo_0, Pageable pageable);

  // FormNo =
  public Slice<CustNotice> findAllByFormNoIs(String formNo_0, Pageable pageable);

  // CustNo = ,AND FacmNo >= ,AND FacmNo <=
  public Slice<CustNotice> findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCustNoAscFacmNoAsc(int custNo_0, int facmNo_1, int facmNo_2, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CustNotice> findByCustNoticeId(CustNoticeId custNoticeId);

}

