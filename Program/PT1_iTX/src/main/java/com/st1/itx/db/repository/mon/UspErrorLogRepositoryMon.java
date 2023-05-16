package com.st1.itx.db.repository.mon;


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

import com.st1.itx.db.domain.UspErrorLog;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface UspErrorLogRepositoryMon extends JpaRepository<UspErrorLog, String> {

  // LogDate >= ,AND LogDate <=
  public Slice<UspErrorLog> findAllByLogDateGreaterThanEqualAndLogDateLessThanEqualOrderByLogDateDescLogTimeDescUspNameDesc(int logDate_0, int logDate_1, Pageable pageable);

  // LogDate >= ,AND LogDate <= ,AND UspName %
  public Slice<UspErrorLog> findAllByLogDateGreaterThanEqualAndLogDateLessThanEqualAndUspNameLikeOrderByLogDateDescLogTimeDescUspNameDesc(int logDate_0, int logDate_1, String uspName_2, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<UspErrorLog> findByLogUkey(String logUkey);

}

