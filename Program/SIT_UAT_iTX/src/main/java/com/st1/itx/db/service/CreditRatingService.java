package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CreditRating;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CreditRatingId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CreditRatingService {

  /**
   * findByPrimaryKey
   *
   * @param creditRatingId PK
   * @param titaVo Variable-Length Argument
   * @return CreditRating CreditRating
   */
  public CreditRating findById(CreditRatingId creditRatingId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CreditRating CreditRating of List
   */
  public Slice<CreditRating> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By CreditRating
   * 
   * @param creditRatingId key
   * @param titaVo Variable-Length Argument
   * @return CreditRating CreditRating
   */
  public CreditRating holdById(CreditRatingId creditRatingId, TitaVo... titaVo);

  /**
   * hold By CreditRating
   * 
   * @param creditRating key
   * @param titaVo Variable-Length Argument
   * @return CreditRating CreditRating
   */
  public CreditRating holdById(CreditRating creditRating, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param creditRating Entity
   * @param titaVo Variable-Length Argument
   * @return CreditRating Entity
   * @throws DBException exception
   */
  public CreditRating insert(CreditRating creditRating, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param creditRating Entity
   * @param titaVo Variable-Length Argument
   * @return CreditRating Entity
   * @throws DBException exception
   */
  public CreditRating update(CreditRating creditRating, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param creditRating Entity
   * @param titaVo Variable-Length Argument
   * @return CreditRating Entity
   * @throws DBException exception
   */
  public CreditRating update2(CreditRating creditRating, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param creditRating Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CreditRating creditRating, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param creditRating Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CreditRating> creditRating, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param creditRating Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CreditRating> creditRating, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param creditRating Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CreditRating> creditRating, TitaVo... titaVo) throws DBException;

}
