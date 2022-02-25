package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.AchAuthLogHistory;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface AchAuthLogHistoryRepositoryDay extends JpaRepository<AchAuthLogHistory, Long> {

  // CustNo = ,AND FacmNo = 
  public Slice<AchAuthLogHistory> findAllByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(int custNo_0, int facmNo_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<AchAuthLogHistory> findByLogNo(Long logNo);

}

