package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.ForeclosureFinished;
import com.st1.itx.db.domain.ForeclosureFinishedId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ForeclosureFinishedRepositoryMon extends JpaRepository<ForeclosureFinished, ForeclosureFinishedId> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<ForeclosureFinished> findByForeclosureFinishedId(ForeclosureFinishedId foreclosureFinishedId);

}

