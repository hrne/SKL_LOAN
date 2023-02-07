package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxAmlRating;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxAmlRatingService {

  /**
   * findByPrimaryKey
   *
   * @param logNo PK
   * @param titaVo Variable-Length Argument
   * @return TxAmlRating TxAmlRating
   */
  public TxAmlRating findById(Long logNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxAmlRating TxAmlRating of List
   */
  public Slice<TxAmlRating> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CaseNo = ,
   *
   * @param caseNo_0 caseNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxAmlRating TxAmlRating of List
   */
  public Slice<TxAmlRating> findByCaseNo(String caseNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * CaseNo = ,
   *
   * @param caseNo_0 caseNo_0
   * @param titaVo Variable-Length Argument
   * @return Slice TxAmlRating TxAmlRating of List
   */
  public TxAmlRating caseNoDescFirst(String caseNo_0, TitaVo... titaVo);

  /**
   * hold By TxAmlRating
   * 
   * @param logNo key
   * @param titaVo Variable-Length Argument
   * @return TxAmlRating TxAmlRating
   */
  public TxAmlRating holdById(Long logNo, TitaVo... titaVo);

  /**
   * hold By TxAmlRating
   * 
   * @param txAmlRating key
   * @param titaVo Variable-Length Argument
   * @return TxAmlRating TxAmlRating
   */
  public TxAmlRating holdById(TxAmlRating txAmlRating, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param txAmlRating Entity
   * @param titaVo Variable-Length Argument
   * @return TxAmlRating Entity
   * @throws DBException exception
   */
  public TxAmlRating insert(TxAmlRating txAmlRating, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param txAmlRating Entity
   * @param titaVo Variable-Length Argument
   * @return TxAmlRating Entity
   * @throws DBException exception
   */
  public TxAmlRating update(TxAmlRating txAmlRating, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param txAmlRating Entity
   * @param titaVo Variable-Length Argument
   * @return TxAmlRating Entity
   * @throws DBException exception
   */
  public TxAmlRating update2(TxAmlRating txAmlRating, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param txAmlRating Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(TxAmlRating txAmlRating, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param txAmlRating Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<TxAmlRating> txAmlRating, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param txAmlRating Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<TxAmlRating> txAmlRating, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param txAmlRating Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<TxAmlRating> txAmlRating, TitaVo... titaVo) throws DBException;

}
