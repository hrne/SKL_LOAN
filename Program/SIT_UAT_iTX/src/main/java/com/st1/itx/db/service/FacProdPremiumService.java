package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.FacProdPremium;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.FacProdPremiumId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FacProdPremiumService {

  /**
   * findByPrimaryKey
   *
   * @param facProdPremiumId PK
   * @param titaVo Variable-Length Argument
   * @return FacProdPremium FacProdPremium
   */
  public FacProdPremium findById(FacProdPremiumId facProdPremiumId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacProdPremium FacProdPremium of List
   */
  public Slice<FacProdPremium> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * ProdNo = ,AND PremiumLow &gt;= ,AND PremiumLow &lt;=
   *
   * @param prodNo_0 prodNo_0
   * @param premiumLow_1 premiumLow_1
   * @param premiumLow_2 premiumLow_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacProdPremium FacProdPremium of List
   */
  public Slice<FacProdPremium> premiumProdNoEq(String prodNo_0, BigDecimal premiumLow_1, BigDecimal premiumLow_2, int index, int limit, TitaVo... titaVo);

  /**
   * hold By FacProdPremium
   * 
   * @param facProdPremiumId key
   * @param titaVo Variable-Length Argument
   * @return FacProdPremium FacProdPremium
   */
  public FacProdPremium holdById(FacProdPremiumId facProdPremiumId, TitaVo... titaVo);

  /**
   * hold By FacProdPremium
   * 
   * @param facProdPremium key
   * @param titaVo Variable-Length Argument
   * @return FacProdPremium FacProdPremium
   */
  public FacProdPremium holdById(FacProdPremium facProdPremium, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param facProdPremium Entity
   * @param titaVo Variable-Length Argument
   * @return FacProdPremium Entity
   * @throws DBException exception
   */
  public FacProdPremium insert(FacProdPremium facProdPremium, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param facProdPremium Entity
   * @param titaVo Variable-Length Argument
   * @return FacProdPremium Entity
   * @throws DBException exception
   */
  public FacProdPremium update(FacProdPremium facProdPremium, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param facProdPremium Entity
   * @param titaVo Variable-Length Argument
   * @return FacProdPremium Entity
   * @throws DBException exception
   */
  public FacProdPremium update2(FacProdPremium facProdPremium, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param facProdPremium Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(FacProdPremium facProdPremium, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param facProdPremium Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<FacProdPremium> facProdPremium, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param facProdPremium Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<FacProdPremium> facProdPremium, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param facProdPremium Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<FacProdPremium> facProdPremium, TitaVo... titaVo) throws DBException;

}
