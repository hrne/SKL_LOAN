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

  // ProdNo % ,AND StatusCode ^i ,AND GovOfferFlag ^i ,AND FinancialFlag ^i ,AND EmpFlag ^i
  public Slice<FacProd> findAllByProdNoLikeAndStatusCodeInAndGovOfferFlagInAndFinancialFlagInAndEmpFlagInOrderByProdNoAsc(String prodNo_0, List<String> statusCode_1, List<String> govOfferFlag_2, List<String> financialFlag_3, List<String> empFlag_4, Pageable pageable);

  // EnterpriseFg ^i
  public Slice<FacProd> findAllByEnterpriseFgInOrderByProdNoAsc(List<String> enterpriseFg_0, Pageable pageable);

  // ProdNo % ,AND StatusCode ^i ,AND EnterpriseFg ^i ,AND GovOfferFlag ^i ,AND FinancialFlag ^i ,AND EmpFlag ^i
  public Slice<FacProd> findAllByProdNoLikeAndStatusCodeInAndEnterpriseFgInAndGovOfferFlagInAndFinancialFlagInAndEmpFlagInOrderByProdNoAsc(String prodNo_0, List<String> statusCode_1, List<String> enterpriseFg_2, List<String> govOfferFlag_3, List<String> financialFlag_4, List<String> empFlag_5, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<FacProd> findByProdNo(String prodNo);

}

