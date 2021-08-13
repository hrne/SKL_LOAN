package com.st1.itx.db.repository.online;


import java.util.Optional;

import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.FacProd;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FacProdRepository extends JpaRepository<FacProd, String> {

  // ProdNo %
  public Slice<FacProd> findAllByProdNoLikeOrderByProdNoAsc(String prodNo_0, Pageable pageable);

  // ProdNo % ,AND StatusCode ^i
  public Slice<FacProd> findAllByProdNoLikeAndStatusCodeInOrderByProdNoAsc(String prodNo_0, List<String> statusCode_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<FacProd> findByProdNo(String prodNo);

}

