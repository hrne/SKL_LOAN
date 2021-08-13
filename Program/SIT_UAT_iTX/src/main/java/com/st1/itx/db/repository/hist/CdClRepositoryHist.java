package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdCl;
import com.st1.itx.db.domain.CdClId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdClRepositoryHist extends JpaRepository<CdCl, CdClId> {

  // ClCode1 >= ,AND ClCode1 <=
  public Slice<CdCl> findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualOrderByClCode1AscClCode2Asc(int clCode1_0, int clCode1_1, Pageable pageable);

  // ClTypeJCIC >= ,AND ClTypeJCIC <=
  public Slice<CdCl> findAllByClTypeJCICGreaterThanEqualAndClTypeJCICLessThanEqual(String clTypeJCIC_0, String clTypeJCIC_1, Pageable pageable);

  // ClCode1 >= ,AND ClCode1 <= ,AND ClCode2 >= ,AND ClCode2 <= 
  public Slice<CdCl> findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClCode2GreaterThanEqualAndClCode2LessThanEqualOrderByClCode1AscClCode2Asc(int clCode1_0, int clCode1_1, int clCode2_2, int clCode2_3, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CdCl> findByCdClId(CdClId cdClId);

}

