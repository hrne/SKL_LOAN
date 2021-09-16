package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.RepayActChangeLog;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface RepayActChangeLogRepository extends JpaRepository<RepayActChangeLog, Long> {

  // CustNo = , AND FacmNo = 
  public Slice<RepayActChangeLog> findAllByCustNoIsAndFacmNoIsOrderByLogNoDesc(int custNo_0, int facmNo_1, Pageable pageable);

  // RelDy = , AND RelTxseq =  
  public Optional<RepayActChangeLog> findTopByRelDyIsAndRelTxseqIsOrderByLogNoDesc(int relDy_0, String relTxseq_1);

  // CustNo = , AND FacmNo = 
  public Optional<RepayActChangeLog> findTopByCustNoIsAndFacmNoIsOrderByLogNoDesc(int custNo_0, int facmNo_1);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<RepayActChangeLog> findByLogNo(Long logNo);

}

